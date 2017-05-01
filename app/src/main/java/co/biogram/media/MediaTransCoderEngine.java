package co.biogram.media;

import android.annotation.TargetApi;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;
import android.media.MediaMuxer;

import java.io.FileDescriptor;

@TargetApi(18)
class MediaTransCoderEngine
{
    void TransCodeVideo(FileDescriptor InPut, String OutPut, MediaTransCoder.MediaStrategy Strategy, ProgressCallback CallbackProgress) throws Exception
    {
        MediaExtractor Extractor = new MediaExtractor();
        Extractor.setDataSource(InPut);

        MediaMuxer Muxer = new MediaMuxer(OutPut, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);

        MediaMetadataRetriever Retriever = new MediaMetadataRetriever();
        Retriever.setDataSource(InPut);

        long Duration = Long.parseLong(Retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)) * 1000;
        Muxer.setOrientationHint(Integer.parseInt(Retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION)));

        TrackResult Track = GetFirstVideoAndAudioTrack(Extractor);
        MediaFormat VideoOutputFormat = Strategy.CreateVideo(Track.VideoFormat);
        MediaFormat AudioOutputFormat = Strategy.CreateAudio(Track.AudioFormat);

        TrackTranscoder VideoTrackTransCoder;
        TrackTranscoder AudioTrackTransCoder;
        QueuedMuxer Queue = new QueuedMuxer(Muxer);

        if (VideoOutputFormat == null)
            VideoTrackTransCoder = new PassThroughTrackTranscoder(Extractor, Track.VideoIndex, Queue, QueuedMuxer.SampleType.VIDEO);
        else
            VideoTrackTransCoder = new VideoTrackTranscoder(Extractor, Track.VideoIndex, VideoOutputFormat, Queue);

        VideoTrackTransCoder.setup();

        if (AudioOutputFormat == null)
            AudioTrackTransCoder = new PassThroughTrackTranscoder(Extractor, Track.AudioIndex, Queue, QueuedMuxer.SampleType.AUDIO);
        else
            AudioTrackTransCoder = new AudioTrackTranscoder(Extractor, Track.AudioIndex, AudioOutputFormat, Queue);

        AudioTrackTransCoder.setup();

        Extractor.selectTrack(Track.VideoIndex);
        Extractor.selectTrack(Track.AudioIndex);

        long LoopCount = 0;

        while (!(VideoTrackTransCoder.isFinished() && AudioTrackTransCoder.isFinished()))
        {
            LoopCount++;
            boolean Step = VideoTrackTransCoder.stepPipeline() || AudioTrackTransCoder.stepPipeline();

            if (Duration > 0 && LoopCount % 10 == 0)
            {
                double VideoProgress = VideoTrackTransCoder.isFinished() ? 1.0 : Math.min(1.0, (double) VideoTrackTransCoder.getWrittenPresentationTimeUs() / Duration);
                double AudioProgress = AudioTrackTransCoder.isFinished() ? 1.0 : Math.min(1.0, (double) AudioTrackTransCoder.getWrittenPresentationTimeUs() / Duration);
                double Progress = (VideoProgress + AudioProgress) / 2.0;

                CallbackProgress.OnProgress(Progress);
            }

            if (!Step)
                Thread.sleep(10);
        }

        Muxer.stop();
        VideoTrackTransCoder.release();
        AudioTrackTransCoder.release();
        Extractor.release();
        Muxer.release();
    }

    private static class TrackResult
    {
        int VideoIndex;
        MediaFormat VideoFormat;

        int AudioIndex;
        MediaFormat AudioFormat;
    }

    private static TrackResult GetFirstVideoAndAudioTrack(MediaExtractor Extractor)
    {
        TrackResult Track = new TrackResult();
        Track.VideoIndex = -1;
        Track.AudioIndex = -1;
        int trackCount = Extractor.getTrackCount();

        for (int i = 0; i < trackCount; i++)
        {
            MediaFormat Format = Extractor.getTrackFormat(i);
            String Mime = Format.getString(MediaFormat.KEY_MIME);

            if (Track.VideoIndex < 0 && Mime.startsWith("video/"))
            {
                Track.VideoIndex = i;
                Track.VideoFormat = Format;
            }
            else if (Track.AudioIndex < 0 && Mime.startsWith("audio/"))
            {
                Track.AudioIndex = i;
                Track.AudioFormat = Format;
            }

            if (Track.VideoIndex >= 0 && Track.AudioIndex >= 0)
                break;
        }

        return Track;
    }

    interface ProgressCallback
    {
        void OnProgress(double progress);
    }
}
