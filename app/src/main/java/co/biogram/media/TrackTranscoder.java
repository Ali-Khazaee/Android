package co.biogram.media;

interface TrackTranscoder {
    void setup();

    boolean stepPipeline();

    long getWrittenPresentationTimeUs();

    boolean isFinished();

    void release();
}
