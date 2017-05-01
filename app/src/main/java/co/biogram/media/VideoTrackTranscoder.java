package co.biogram.media;

import android.annotation.TargetApi;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;

import java.io.IOException;
import java.nio.ByteBuffer;

@TargetApi(18)
class VideoTrackTranscoder implements TrackTranscoder
{
    private static final int DRAIN_STATE_NONE = 0;
    private static final int DRAIN_STATE_SHOULD_RETRY_IMMEDIATELY = 1;
    private static final int DRAIN_STATE_CONSUMED = 2;

    private final MediaExtractor mExtractor;
    private final int mTrackIndex;
    private final MediaFormat mOutputFormat;
    private final QueuedMuxer mMuxer;
    private final MediaCodec.BufferInfo mBufferInfo = new MediaCodec.BufferInfo();
    private MediaCodec mDecoder;
    private MediaCodec mEncoder;
    private ByteBuffer[] mDecoderInputBuffers;
    private ByteBuffer[] mEncoderOutputBuffers;
    private MediaFormat mActualOutputFormat;
    private OutputSurface mDecoderOutputSurfaceWrapper;
    private InputSurface mEncoderInputSurfaceWrapper;
    private boolean mIsExtractorEOS;
    private boolean mIsDecoderEOS;
    private boolean mIsEncoderEOS;
    private boolean mDecoderStarted;
    private boolean mEncoderStarted;
    private long mWrittenPresentationTimeUs;

    VideoTrackTranscoder(MediaExtractor extractor, int trackIndex, MediaFormat outputFormat, QueuedMuxer muxer)
    {
        mExtractor = extractor;
        mTrackIndex = trackIndex;
        mOutputFormat = outputFormat;
        mMuxer = muxer;
    }

    @Override
    public void setup()
    {
        mExtractor.selectTrack(mTrackIndex);

        try
        {
            mEncoder = MediaCodec.createEncoderByType(mOutputFormat.getString(MediaFormat.KEY_MIME));
        }
        catch (IOException e)
        {
            throw new IllegalStateException(e);
        }

        mEncoder.configure(mOutputFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
        mEncoderInputSurfaceWrapper = new InputSurface(mEncoder.createInputSurface());
        mEncoderInputSurfaceWrapper.makeCurrent();
        mEncoder.start();
        mEncoderStarted = true;
        mEncoderOutputBuffers = mEncoder.getOutputBuffers();
        MediaFormat inputFormat = mExtractor.getTrackFormat(mTrackIndex);

        if (inputFormat.containsKey("rotation-degrees"))
            inputFormat.setInteger("rotation-degrees", 0);

        mDecoderOutputSurfaceWrapper = new OutputSurface();

        try
        {
            mDecoder = MediaCodec.createDecoderByType(inputFormat.getString(MediaFormat.KEY_MIME));
        }
        catch (IOException e)
        {
            throw new IllegalStateException(e);
        }

        mDecoder.configure(inputFormat, mDecoderOutputSurfaceWrapper.getSurface(), null, 0);
        mDecoder.start();
        mDecoderStarted = true;
        mDecoderInputBuffers = mDecoder.getInputBuffers();
    }

    @Override
    public boolean stepPipeline()
    {
        boolean busy = false;
        int status;

        while (drainEncoder(0) != DRAIN_STATE_NONE)
            busy = true;

        do
        {
            status = drainDecoder(0);
            if (status != DRAIN_STATE_NONE)
                busy = true;
        }
        while (status == DRAIN_STATE_SHOULD_RETRY_IMMEDIATELY);

        while (drainExtractor(0) != DRAIN_STATE_NONE)
            busy = true;

        return busy;
    }

    @Override
    public long getWrittenPresentationTimeUs()
    {
        return mWrittenPresentationTimeUs;
    }

    @Override
    public boolean isFinished()
    {
        return mIsEncoderEOS;
    }

    @Override
    public void release()
    {
        if (mDecoderOutputSurfaceWrapper != null)
        {
            mDecoderOutputSurfaceWrapper.release();
            mDecoderOutputSurfaceWrapper = null;
        }

        if (mEncoderInputSurfaceWrapper != null)
        {
            mEncoderInputSurfaceWrapper.release();
            mEncoderInputSurfaceWrapper = null;
        }

        if (mDecoder != null)
        {
            if (mDecoderStarted)
                mDecoder.stop();

            mDecoder.release();
            mDecoder = null;
        }

        if (mEncoder != null)
        {
            if (mEncoderStarted)
                mEncoder.stop();

            mEncoder.release();
            mEncoder = null;
        }
    }

    private int drainExtractor(long timeoutUs)
    {
        if (mIsExtractorEOS)
            return DRAIN_STATE_NONE;

        int trackIndex = mExtractor.getSampleTrackIndex();

        if (trackIndex >= 0 && trackIndex != mTrackIndex)
            return DRAIN_STATE_NONE;

        int result = mDecoder.dequeueInputBuffer(timeoutUs);

        if (result < 0)
            return DRAIN_STATE_NONE;

        if (trackIndex < 0)
        {
            mIsExtractorEOS = true;
            mDecoder.queueInputBuffer(result, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
            return DRAIN_STATE_NONE;
        }

        int sampleSize = mExtractor.readSampleData(mDecoderInputBuffers[result], 0);
        boolean isKeyFrame = (mExtractor.getSampleFlags() & MediaExtractor.SAMPLE_FLAG_SYNC) != 0;
        mDecoder.queueInputBuffer(result, 0, sampleSize, mExtractor.getSampleTime(), isKeyFrame ? MediaCodec.BUFFER_FLAG_SYNC_FRAME : 0);
        mExtractor.advance();
        return DRAIN_STATE_CONSUMED;
    }

    private int drainDecoder(long timeoutUs)
    {
        if (mIsDecoderEOS)
            return DRAIN_STATE_NONE;

        int result = mDecoder.dequeueOutputBuffer(mBufferInfo, timeoutUs);

        switch (result)
        {
            case MediaCodec.INFO_TRY_AGAIN_LATER:
                return DRAIN_STATE_NONE;
            case MediaCodec.INFO_OUTPUT_FORMAT_CHANGED:
            case MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED:
                return DRAIN_STATE_SHOULD_RETRY_IMMEDIATELY;
        }

        if ((mBufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0)
        {
            mEncoder.signalEndOfInputStream();
            mIsDecoderEOS = true;
            mBufferInfo.size = 0;
        }

        boolean doRender = (mBufferInfo.size > 0);
        mDecoder.releaseOutputBuffer(result, doRender);

        if (doRender)
        {
            mDecoderOutputSurfaceWrapper.awaitNewImage();
            mDecoderOutputSurfaceWrapper.drawImage();
            mEncoderInputSurfaceWrapper.setPresentationTime(mBufferInfo.presentationTimeUs * 1000);
            mEncoderInputSurfaceWrapper.swapBuffers();
        }

        return DRAIN_STATE_CONSUMED;
    }

    private int drainEncoder(long timeoutUs)
    {
        if (mIsEncoderEOS)
            return DRAIN_STATE_NONE;

        int result = mEncoder.dequeueOutputBuffer(mBufferInfo, timeoutUs);

        switch (result)
        {
            case MediaCodec.INFO_TRY_AGAIN_LATER:
                return DRAIN_STATE_NONE;
            case MediaCodec.INFO_OUTPUT_FORMAT_CHANGED:
                if (mActualOutputFormat != null)
                    throw new RuntimeException("Video output format changed twice.");
                mActualOutputFormat = mEncoder.getOutputFormat();
                mMuxer.setOutputFormat(QueuedMuxer.SampleType.VIDEO, mActualOutputFormat);
                return DRAIN_STATE_SHOULD_RETRY_IMMEDIATELY;
            case MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED:
                mEncoderOutputBuffers = mEncoder.getOutputBuffers();
                return DRAIN_STATE_SHOULD_RETRY_IMMEDIATELY;
        }

        if (mActualOutputFormat == null)
            throw new RuntimeException("Could not determine actual output format.");

        if ((mBufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0)
        {
            mIsEncoderEOS = true;
            mBufferInfo.set(0, 0, 0, mBufferInfo.flags);
        }

        if ((mBufferInfo.flags & MediaCodec.BUFFER_FLAG_CODEC_CONFIG) != 0)
        {
            mEncoder.releaseOutputBuffer(result, false);
            return DRAIN_STATE_SHOULD_RETRY_IMMEDIATELY;
        }

        mMuxer.writeSampleData(QueuedMuxer.SampleType.VIDEO, mEncoderOutputBuffers[result], mBufferInfo);
        mWrittenPresentationTimeUs = mBufferInfo.presentationTimeUs;
        mEncoder.releaseOutputBuffer(result, false);
        return DRAIN_STATE_CONSUMED;
    }
}
