package co.biogram.media;

import android.media.MediaCodec;
import android.media.MediaFormat;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.ArrayDeque;
import java.util.Queue;

class AudioChannel
{
    private static class AudioBuffer
    {
        int bufferIndex;
        long presentationTimeUs;
        ShortBuffer data;
    }

    static final int BUFFER_INDEX_END_OF_STREAM = -1;

    private static final int BYTES_PER_SHORT = 2;
    private static final long MICROSECS_PER_SEC = 1000000;

    private final Queue<AudioBuffer> mEmptyBuffers = new ArrayDeque<>();
    private final Queue<AudioBuffer> mFilledBuffers = new ArrayDeque<>();

    private final MediaCodec mDecoder;
    private final MediaCodec mEncoder;
    private final MediaFormat mEncodeFormat;

    private int mInputSampleRate;
    private int mInputChannelCount;
    private int mOutputChannelCount;

    private AudioRemixer mRemixer;

    private final MediaCodecBufferCompatWrapper mDecoderBuffers;
    private final MediaCodecBufferCompatWrapper mEncoderBuffers;

    private final AudioBuffer mOverflowBuffer = new AudioBuffer();

    private MediaFormat mActualDecodedFormat;

    AudioChannel(final MediaCodec decoder, final MediaCodec encoder, final MediaFormat encodeFormat)
    {
        mDecoder = decoder;
        mEncoder = encoder;
        mEncodeFormat = encodeFormat;

        mDecoderBuffers = new MediaCodecBufferCompatWrapper(mDecoder);
        mEncoderBuffers = new MediaCodecBufferCompatWrapper(mEncoder);
    }

    void setActualDecodedFormat(final MediaFormat decodedFormat)
    {
        mActualDecodedFormat = decodedFormat;
        mInputSampleRate = mActualDecodedFormat.getInteger(MediaFormat.KEY_SAMPLE_RATE);

        if (mInputSampleRate != mEncodeFormat.getInteger(MediaFormat.KEY_SAMPLE_RATE))
            throw new UnsupportedOperationException("Audio sample rate conversion not supported yet.");

        mInputChannelCount = mActualDecodedFormat.getInteger(MediaFormat.KEY_CHANNEL_COUNT);
        mOutputChannelCount = mEncodeFormat.getInteger(MediaFormat.KEY_CHANNEL_COUNT);

        if (mInputChannelCount != 1 && mInputChannelCount != 2)
            throw new UnsupportedOperationException("Input channel count (" + mInputChannelCount + ") not supported.");

        if (mOutputChannelCount != 1 && mOutputChannelCount != 2)
            throw new UnsupportedOperationException("Output channel count (" + mOutputChannelCount + ") not supported.");

        if (mInputChannelCount > mOutputChannelCount)
            mRemixer = AudioRemixer.DOWNMIX;
        else if (mInputChannelCount < mOutputChannelCount)
            mRemixer = AudioRemixer.UPMIX;
        else
            mRemixer = AudioRemixer.PASSTHROUGH;

        mOverflowBuffer.presentationTimeUs = 0;
    }

    void drainDecoderBufferAndQueue(final int bufferIndex, final long presentationTimeUs)
    {
        if (mActualDecodedFormat == null)
            throw new RuntimeException("Buffer received before format!");

        final ByteBuffer data = bufferIndex == BUFFER_INDEX_END_OF_STREAM ? null : mDecoderBuffers.getOutputBuffer(bufferIndex);
        AudioBuffer buffer = mEmptyBuffers.poll();

        if (buffer == null)
            buffer = new AudioBuffer();

        buffer.bufferIndex = bufferIndex;
        buffer.presentationTimeUs = presentationTimeUs;
        buffer.data = data == null ? null : data.asShortBuffer();

        if (mOverflowBuffer.data == null && data != null)
        {
            mOverflowBuffer.data = ByteBuffer.allocateDirect(data.capacity()).order(ByteOrder.nativeOrder()).asShortBuffer();
            mOverflowBuffer.data.clear().flip();
        }

        mFilledBuffers.add(buffer);
    }

    boolean feedEncoder()
    {
        final boolean hasOverflow = mOverflowBuffer.data != null && mOverflowBuffer.data.hasRemaining();

        if (mFilledBuffers.isEmpty() && !hasOverflow)
            return false;

        final int encoderInBuffIndex = mEncoder.dequeueInputBuffer((long) 0);
        if (encoderInBuffIndex < 0)
            return false;

        final ShortBuffer outBuffer = mEncoderBuffers.getInputBuffer(encoderInBuffIndex).asShortBuffer();

        if (hasOverflow)
        {
            final long presentationTimeUs = drainOverflow(outBuffer);
            mEncoder.queueInputBuffer(encoderInBuffIndex, 0, outBuffer.position() * BYTES_PER_SHORT, presentationTimeUs, 0);
            return true;
        }

        final AudioBuffer inBuffer = mFilledBuffers.poll();

        if (inBuffer.bufferIndex == BUFFER_INDEX_END_OF_STREAM)
        {
            mEncoder.queueInputBuffer(encoderInBuffIndex, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
            return false;
        }

        final long presentationTimeUs = remixAndMaybeFillOverflow(inBuffer, outBuffer);
        mEncoder.queueInputBuffer(encoderInBuffIndex, 0, outBuffer.position() * BYTES_PER_SHORT, presentationTimeUs, 0);
        mDecoder.releaseOutputBuffer(inBuffer.bufferIndex, false);
        mEmptyBuffers.add(inBuffer);

        return true;
    }

    private static long sampleCountToDurationUs(final int sampleCount, final int sampleRate, final int channelCount)
    {
        return (sampleCount / (sampleRate * MICROSECS_PER_SEC)) / channelCount;
    }

    private long drainOverflow(final ShortBuffer outBuff)
    {
        final ShortBuffer overflowBuff = mOverflowBuffer.data;
        final int overflowLimit = overflowBuff.limit();
        final int overflowSize = overflowBuff.remaining();
        final long beginPresentationTimeUs = mOverflowBuffer.presentationTimeUs + sampleCountToDurationUs(overflowBuff.position(), mInputSampleRate, mOutputChannelCount);

        outBuff.clear();
        overflowBuff.limit(outBuff.capacity());
        outBuff.put(overflowBuff);

        if (overflowSize >= outBuff.capacity())
            overflowBuff.clear().limit(0);
        else
            overflowBuff.limit(overflowLimit);

        return beginPresentationTimeUs;
    }

    private long remixAndMaybeFillOverflow(final AudioBuffer input, final ShortBuffer outBuff)
    {
        final ShortBuffer inBuff = input.data;
        final ShortBuffer overflowBuff = mOverflowBuffer.data;

        outBuff.clear();
        inBuff.clear();

        if (inBuff.remaining() > outBuff.remaining())
        {
            inBuff.limit(outBuff.capacity());
            mRemixer.remix(inBuff, outBuff);
            inBuff.limit(inBuff.capacity());
            final long consumedDurationUs = sampleCountToDurationUs(inBuff.position(), mInputSampleRate, mInputChannelCount);
            mRemixer.remix(inBuff, overflowBuff);
            overflowBuff.flip();
            mOverflowBuffer.presentationTimeUs = input.presentationTimeUs + consumedDurationUs;
        }
        else
            mRemixer.remix(inBuff, outBuff);

        return input.presentationTimeUs;
    }
}
