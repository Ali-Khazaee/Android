package co.biogram.media;

import android.media.MediaFormat;
import android.os.Looper;
import android.os.Process;

import java.io.FileInputStream;

public class MediaTransCoder {
    public static void Start(final String Input, final String Output, final MediaStrategy Strategy, final CallBack CallBackListener) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.getMainLooper();
                Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
                Process.setThreadPriority(-20);

                FileInputStream InputStream = null;

                try {
                    InputStream = new FileInputStream(Input);

                    new MediaTransCoderEngine().TransCodeVideo(InputStream.getFD(), Output, Strategy, new MediaTransCoderEngine.ProgressCallback() {
                        @Override
                        public void OnProgress(double Progress) {
                            CallBackListener.OnProgress(Progress);
                        }
                    });

                    CallBackListener.OnCompleted();
                } catch (Exception e) {
                    CallBackListener.OnFailed(e);
                } finally {
                    try {
                        if (InputStream != null)
                            InputStream.close();
                    } catch (Exception ee) {
                        // Leave Me Alone
                    }
                }
            }
        });
        thread.start();
    }

    public interface CallBack {
        void OnCompleted();

        void OnProgress(double Progress);

        void OnFailed(Exception e);
    }

    public interface MediaStrategy {
        MediaFormat CreateVideo(MediaFormat inputFormat);

        MediaFormat CreateAudio(MediaFormat inputFormat);
    }
}
