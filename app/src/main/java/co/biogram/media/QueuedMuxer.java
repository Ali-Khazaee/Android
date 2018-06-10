package co.biogram.media;

import android.annotation.TargetApi;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.MediaMuxer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

@TargetApi(18)
class QueuedMuxer {
    private static final int BUFFER_SIZE = 64 * 1024;
    private final MediaMuxer mMuxer;
    private final List<SampleInfo> mSampleInfoList;
    private MediaFormat mVideoFormat;
    private MediaFormat mAudioFormat;
    private int mVideoTrackIndex;
    private int mAudioTrackIndex;
    private ByteBuffer mByteBuffer;
    private boolean mStarted;

    QueuedMuxer(MediaMuxer muxer) {
        mMuxer = muxer;

        mSampleInfoList = new ArrayList<>();
    }

    void setOutputFormat(SampleType sampleType, MediaFormat format) {
        switch (sampleType) {
            case VIDEO:
                mVideoFormat = format;
                break;
            case AUDIO:
                mAudioFormat = format;
                break;
            default:
                throw new AssertionError();
        }

        onSetOutputFormat();
    }

    private void onSetOutputFormat() {
        if (mVideoFormat == null || mAudioFormat == null)
            return;

        mVideoTrackIndex = mMuxer.addTrack(mVideoFormat);
        mAudioTrackIndex = mMuxer.addTrack(mAudioFormat);
        mMuxer.start();
        mStarted = true;

        if (mByteBuffer == null)
            mByteBuffer = ByteBuffer.allocate(0);

        mByteBuffer.flip();

        MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        int offset = 0;

        for (SampleInfo sampleInfo : mSampleInfoList) {
            sampleInfo.writeToBufferInfo(bufferInfo, offset);
            mMuxer.writeSampleData(getTrackIndexForSampleType(sampleInfo.mSampleType), mByteBuffer, bufferInfo);
            offset += sampleInfo.mSize;
        }

        mSampleInfoList.clear();
        mByteBuffer = null;
    }

    void writeSampleData(SampleType sampleType, ByteBuffer byteBuf, MediaCodec.BufferInfo bufferInfo) {
        if (mStarted) {
            mMuxer.writeSampleData(getTrackIndexForSampleType(sampleType), byteBuf, bufferInfo);
            return;
        }

        byteBuf.limit(bufferInfo.offset + bufferInfo.size);
        byteBuf.position(bufferInfo.offset);

        if (mByteBuffer == null)
            mByteBuffer = ByteBuffer.allocateDirect(BUFFER_SIZE).order(ByteOrder.nativeOrder());

        mByteBuffer.put(byteBuf);
        mSampleInfoList.add(new SampleInfo(sampleType, bufferInfo.size, bufferInfo));
    }

    private int getTrackIndexForSampleType(SampleType sampleType) {
        switch (sampleType) {
            case VIDEO:
                return mVideoTrackIndex;
            case AUDIO:
                return mAudioTrackIndex;
            default:
                throw new AssertionError();
        }
    }

    enum SampleType {VIDEO, AUDIO}

    private static class SampleInfo {
        private final SampleType mSampleType;
        private final int mSize;
        private final long mPresentationTimeUs;
        private final int mFlags;

        private SampleInfo(SampleType sampleType, int size, MediaCodec.BufferInfo bufferInfo) {
            mSampleType = sampleType;
            mSize = size;
            mPresentationTimeUs = bufferInfo.presentationTimeUs;
            mFlags = bufferInfo.flags;
        }

        private void writeToBufferInfo(MediaCodec.BufferInfo bufferInfo, int offset) {
            bufferInfo.set(offset, mSize, mPresentationTimeUs, mFlags);
        }
    }
}
