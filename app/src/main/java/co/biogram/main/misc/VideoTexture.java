package co.biogram.main.misc;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.opengl.GLES20;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.MediaController.MediaPlayerControl;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;

import co.biogram.main.App;
import co.biogram.main.handler.CacheHandler;

public class VideoTexture extends TextureView implements MediaPlayerControl
{
    private static final int STATE_ERROR = -1;
    private static final int STATE_IDLE = 0;
    private static final int STATE_PREPARING = 1;
    private static final int STATE_PREPARED = 2;
    private static final int STATE_PLAYING = 3;
    private static final int STATE_PAUSED = 4;
    private static final int STATE_PLAYBACK_COMPLETED = 5;

    private int VideoWidth;
    private int VideoHeight;

    private Uri VideoPath;

    private int CurrentState = STATE_IDLE;
    private int TargetState = STATE_IDLE;

    private Surface surface = null;
    private MediaPlayer mediaPlayer = null;

    private int AudioSession;
    private int CurrentBufferPercentage;
    private int SeekWhenPrepared = 0;
    private boolean CanPause;
    private boolean CanSeekBack;
    private boolean CanSeekForward;

    public VideoTexture(Context context)
    {
        this(context, null, 0);
    }

    public VideoTexture(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public VideoTexture(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);

        VideoWidth = 0;
        VideoHeight = 0;

        setSurfaceTextureListener(new SurfaceTextureListener()
        {
            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height)
            {
                if (mediaPlayer != null && TargetState == STATE_PLAYING && width > 0 && height > 0)
                {
                    if (SeekWhenPrepared != 0)
                        seekTo(SeekWhenPrepared);

                    start();
                }
            }

            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface2, int width, int height)
            {
                surface = new Surface(surface2);
                Resume();
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface2)
            {
                if (surface != null)
                {
                    surface.release();
                    surface = null;
                }

                Release(true);
                return true;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) { }
        });

        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();

        CurrentState = STATE_IDLE;
        TargetState = STATE_IDLE;
    }

    @Override
    public void onInitializeAccessibilityEvent(AccessibilityEvent event)
    {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(VideoTexture.class.getName());
    }

    @Override
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info)
    {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(VideoTexture.class.getName());
    }

    @Override
    public void start()
    {
        if (IsInPlaybackState())
        {
            mediaPlayer.start();
            CurrentState = STATE_PLAYING;
        }

        TargetState = STATE_PLAYING;
    }

    @Override
    public void pause()
    {
        if (IsInPlaybackState())
        {
            if (mediaPlayer.isPlaying())
            {
                mediaPlayer.pause();
                CurrentState = STATE_PAUSED;
            }
        }

        TargetState = STATE_PAUSED;
    }

    @Override
    public int getDuration()
    {
        if (IsInPlaybackState())
            return mediaPlayer.getDuration();

        return -1;
    }

    @Override
    public int getCurrentPosition()
    {
        if (IsInPlaybackState())
            return mediaPlayer.getCurrentPosition();

        return 0;
    }

    @Override
    public void seekTo(int msec)
    {
        if (IsInPlaybackState())
        {
            mediaPlayer.seekTo(msec);
            SeekWhenPrepared = 0;
            return;
        }

        SeekWhenPrepared = msec;
    }

    @Override
    public boolean isPlaying()
    {
        return IsInPlaybackState() && mediaPlayer.isPlaying();
    }

    @Override
    public int getBufferPercentage()
    {
        if (mediaPlayer != null)
            return CurrentBufferPercentage;

        return 0;
    }

    @Override
    public boolean canPause()
    {
        return CanPause;
    }

    @Override
    public boolean canSeekBackward()
    {
        return CanSeekBack;
    }

    @Override
    public boolean canSeekForward()
    {
        return CanSeekForward;
    }

    @Override
    public int getAudioSessionId()
    {
        if (AudioSession == 0)
        {
            MediaPlayer mp = new MediaPlayer();
            AudioSession = mp.getAudioSessionId();
            mp.release();
        }

        return AudioSession;
    }

    public Uri GetURL()
    {
        return VideoPath;
    }

    public void SetURL(String Path, String Tag)
    {
        String Name = Path.split("/")[Path.split("/").length -1];

        if (CacheHandler.VideoCache(Name))
        {
            VideoPath = Uri.parse(CacheHandler.VideoLoad(Name));
        }
        else
        {
            VideoPath = Uri.parse(Path);
            CacheHandler.VideoSave(Name, Path, Tag);
        }

        Resume();
        requestLayout();
        invalidate();
    }

    private boolean Calculate = true;

    public void Resume()
    {
        if (VideoPath == null || surface == null)
            return;

        try
        {
            Release(false);

            AudioManager audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
            audioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

            mediaPlayer = new MediaPlayer();

            if (AudioSession != 0)
                mediaPlayer.setAudioSessionId(AudioSession);
            else
                AudioSession = mediaPlayer.getAudioSessionId();

            CurrentBufferPercentage = 0;

            mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener()
            {
                public void onBufferingUpdate(MediaPlayer mp, int percent)
                {
                    CurrentBufferPercentage = percent;
                }
            });
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
            {
                public void onPrepared(MediaPlayer mp)
                {
                    CurrentState = STATE_PREPARED;

                    CanPause = CanSeekBack = CanSeekForward = true;

                    VideoWidth = mp.getVideoWidth();
                    VideoHeight = mp.getVideoHeight();

                    if (SeekWhenPrepared != 0)
                        seekTo(SeekWhenPrepared);

                    if (VideoWidth != 0 && VideoHeight != 0)
                        getSurfaceTexture().setDefaultBufferSize(VideoWidth, VideoHeight);

                    if (TargetState == STATE_PLAYING)
                        start();
                }
            });
            mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener()
            {
                public void onVideoSizeChanged(final MediaPlayer mp, int width, int height)
                {
                    VideoWidth = mp.getVideoWidth();
                    VideoHeight = mp.getVideoHeight();

                    if (Calculate)
                    {
                        final int A = getWidth();
                        int C = A - VideoWidth;
                        final int H = C + VideoHeight;

                        post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                ViewGroup.LayoutParams params = getLayoutParams();
                                params.width = A;
                                params.height = H;
                                setLayoutParams(params);

                                requestLayout();
                                invalidate();
                            }
                        });

                        Calculate = false;
                    }

                    if (VideoWidth != 0 && VideoHeight != 0)
                    {
                        getSurfaceTexture().setDefaultBufferSize(VideoWidth, VideoHeight);
                        requestLayout();
                    }
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
            {
                public void onCompletion(final MediaPlayer mp)
                {
                    CurrentState = STATE_PLAYBACK_COMPLETED;
                    TargetState = STATE_PLAYBACK_COMPLETED;
                }
            });

            mediaPlayer.setDataSource(VideoPath.toString());
            mediaPlayer.setSurface(surface);
            mediaPlayer.setLooping(true);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setScreenOnWhilePlaying(true);
            mediaPlayer.prepareAsync();

            CurrentState = STATE_PREPARING;
        }
        catch (Exception e)
        {
            CurrentState = STATE_ERROR;
            TargetState = STATE_ERROR;
        }
    }

    private void Release(boolean ClearTargetState)
    {
        if (mediaPlayer == null)
            return;

        mediaPlayer.reset();
        mediaPlayer.release();
        mediaPlayer = null;
        CurrentState = STATE_IDLE;

        if (ClearTargetState)
            TargetState  = STATE_IDLE;

        AudioManager audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        audioManager.abandonAudioFocus(null);
    }

    private boolean IsInPlaybackState()
    {
        return (mediaPlayer != null && CurrentState != STATE_ERROR && CurrentState != STATE_IDLE && CurrentState != STATE_PREPARING);
    }

    public void StopPlayback()
    {
        if (mediaPlayer != null)
        {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;

            CurrentState = STATE_IDLE;
            TargetState  = STATE_IDLE;

            AudioManager audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
            audioManager.abandonAudioFocus(null);
        }

        if (surface == null)
            return;

        EGL10 EGL = (EGL10) EGLContext.getEGL();
        EGLDisplay EglDisplay = EGL.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        EGL.eglInitialize(EglDisplay, null);

        int[] AttrList = { EGL10.EGL_RED_SIZE, 8, EGL10.EGL_GREEN_SIZE, 8, EGL10.EGL_BLUE_SIZE, 8, EGL10.EGL_ALPHA_SIZE, 8, EGL10.EGL_RENDERABLE_TYPE, EGL10.EGL_WINDOW_BIT, EGL10.EGL_NONE, 0, EGL10.EGL_NONE };

        EGLConfig[] EglConfig = new EGLConfig[1];
        EGL.eglChooseConfig(EglDisplay, AttrList, EglConfig, EglConfig.length, new int[1]);
        EGLConfig Config = EglConfig[0];
        EGLContext context = EGL.eglCreateContext(EglDisplay, Config, EGL10.EGL_NO_CONTEXT, new int[] { 12440, 2, EGL10.EGL_NONE });
        EGLSurface EglSurface = EGL.eglCreateWindowSurface(EglDisplay, Config, surface, new int[] { EGL10.EGL_NONE });
        EGL.eglMakeCurrent(EglDisplay, EglSurface, EglSurface, context);
        GLES20.glClearColor(0, 0, 0, 1);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        EGL.eglSwapBuffers(EglDisplay, EglSurface);
        EGL.eglDestroySurface(EglDisplay, EglSurface);
        EGL.eglMakeCurrent(EglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
        EGL.eglDestroyContext(EglDisplay, context);
        EGL.eglTerminate(EglDisplay);
    }
}
