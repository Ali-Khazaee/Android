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

public class TextureVideoView extends TextureView implements MediaPlayerControl
{
    private static final int STATE_ERROR = -1;
    private static final int STATE_IDLE = 0;
    private static final int STATE_PREPARING = 1;
    private static final int STATE_PREPARED = 2;

    private OnVideoPrepared OnPrepared;
    private OnVideoCompletion OnCompletion;

    private Uri VideoUri;

    private boolean CanPause = false;
    private boolean CanSeekBack = false;
    private boolean CanSeekForward = false;

    private int AudioSession;
    private int CurrentBufferPercentage;
    private int CurrentState = STATE_IDLE;

    private Surface surface = null;
    private MediaPlayer mediaPlayer = null;

    public TextureVideoView(Context context)
    {
        this(context, null, 0);
    }

    public TextureVideoView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public TextureVideoView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);

        setSurfaceTextureListener(new SurfaceTextureListener()
        {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture s, int width, int height)
            {
                surface = new Surface(s);
                PrepareVideo();
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture s)
            {
                if (surface != null)
                {
                    surface.release();
                    surface = null;
                }

                Release();
                return true;
            }

            @Override public void onSurfaceTextureSizeChanged(SurfaceTexture s, int width, int height) { }
            @Override public void onSurfaceTextureUpdated(SurfaceTexture s) { }
        });

        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();

        CurrentState = STATE_IDLE;
    }

    @Override
    public void start()
    {
        if (IsPlayable())
            mediaPlayer.start();
    }

    @Override
    public void pause()
    {
        if (IsPlayable())
        {
            if (mediaPlayer.isPlaying())
                mediaPlayer.pause();
        }
    }

    @Override
    public int getDuration()
    {
        if (IsPlayable())
            return mediaPlayer.getDuration();

        return -1;
    }

    @Override
    public int getCurrentPosition()
    {
        if (IsPlayable())
            return mediaPlayer.getCurrentPosition();

        return 0;
    }

    @Override
    public void seekTo(int msec)
    {
        if (IsPlayable())
            mediaPlayer.seekTo(msec);
    }

    @Override
    public boolean isPlaying()
    {
        return IsPlayable() && mediaPlayer.isPlaying();
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
            MediaPlayer TempMediaPlayer = new MediaPlayer();
            AudioSession = TempMediaPlayer.getAudioSessionId();
            TempMediaPlayer.release();
        }

        return AudioSession;
    }

    @Override
    public void onInitializeAccessibilityEvent(AccessibilityEvent event)
    {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(TextureVideoView.class.getName());
    }

    @Override
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info)
    {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(TextureVideoView.class.getName());
    }

    public void SetVideoURI(String Url)
    {
        VideoUri = Uri.parse(Url);
        PrepareVideo();
        requestLayout();
        invalidate();
    }

    private void Release()
    {
        if (mediaPlayer != null)
        {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;

            CurrentState = STATE_IDLE;

            AudioManager audioManager = (AudioManager) getContext().getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
            audioManager.abandonAudioFocus(null);
        }
    }

    private boolean IsPlayable()
    {
        return (mediaPlayer != null && CurrentState != STATE_ERROR && CurrentState != STATE_IDLE && CurrentState != STATE_PREPARING);
    }

    public void ClearPlayback()
    {
        if (mediaPlayer != null)
        {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;

            CurrentState = STATE_IDLE;

            AudioManager audioManager = (AudioManager) getContext().getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
            audioManager.abandonAudioFocus(null);
        }

        if (surface == null)
            return;

        EGLConfig[] Configs = new EGLConfig[1];
        EGL10 egl = (EGL10) EGLContext.getEGL();
        EGLDisplay display = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        egl.eglInitialize(display, null);
        int[] AttrList = { EGL10.EGL_RED_SIZE, 8, EGL10.EGL_GREEN_SIZE, 8, EGL10.EGL_BLUE_SIZE, 8, EGL10.EGL_ALPHA_SIZE, 8, EGL10.EGL_RENDERABLE_TYPE, EGL10.EGL_WINDOW_BIT, EGL10.EGL_NONE, 0, EGL10.EGL_NONE };
        egl.eglChooseConfig(display, AttrList, Configs, Configs.length, new int[1]);
        EGLContext context = egl.eglCreateContext(display, Configs[0], EGL10.EGL_NO_CONTEXT, new int[] { 12440, 2, EGL10.EGL_NONE });
        EGLSurface eglSurface = egl.eglCreateWindowSurface(display, Configs[0], surface, new int[] { EGL10.EGL_NONE });
        egl.eglMakeCurrent(display, eglSurface, eglSurface, context);
        GLES20.glClearColor(0, 0, 0, 1);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        egl.eglSwapBuffers(display, eglSurface);
        egl.eglDestroySurface(display, eglSurface);
        egl.eglMakeCurrent(display, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
        egl.eglDestroyContext(display, context);
        egl.eglTerminate(display);
    }

    private void PrepareVideo()
    {
        if (VideoUri == null || surface == null)
            return;

        try
        {
            Release();

            AudioManager audioManager = (AudioManager) getContext().getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
            audioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

            mediaPlayer = new MediaPlayer();

            if (AudioSession != 0)
                mediaPlayer.setAudioSessionId(AudioSession);
            else
                AudioSession = mediaPlayer.getAudioSessionId();

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
            {
                public void onPrepared(MediaPlayer mp)
                {
                    if (OnPrepared != null)
                        OnPrepared.OnPrepared(mp);

                    int VideoWidth = mp.getVideoWidth();
                    int VideoHeight = mp.getVideoHeight();
                    int ScreenWidth = getWidth();

                    int Height;

                    if (VideoWidth > ScreenWidth)
                    {
                        int Width = VideoWidth - ScreenWidth;
                        Height = VideoHeight - Width;
                    }
                    else
                    {
                        int Width = ScreenWidth - VideoWidth;
                        Height = VideoHeight + Width;
                    }

                    ViewGroup.LayoutParams TextureViewParam = getLayoutParams();
                    TextureViewParam.width = ScreenWidth;
                    TextureViewParam.height = Height;

                    setLayoutParams(TextureViewParam);

                    CurrentState = STATE_PREPARED;

                    CanPause = CanSeekBack = CanSeekForward = true;
                }
            });
            mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener()
            {
                public void onVideoSizeChanged(MediaPlayer mp, int width, int height)
                {
                    int VideoWidth = mp.getVideoWidth();
                    int VideoHeight = mp.getVideoHeight();

                    if (VideoWidth != 0 && VideoHeight != 0)
                    {
                        getSurfaceTexture().setDefaultBufferSize(VideoWidth, VideoHeight);
                        requestLayout();
                    }
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
            {
                public void onCompletion(MediaPlayer mp)
                {
                    if (OnCompletion != null)
                        OnCompletion.OnCompletion(mp);
                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener()
            {
                public boolean onError(MediaPlayer mp, int framework_err, int impl_err)
                {
                    CurrentState = STATE_ERROR;
                    return true;
                }
            });
            mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener()
            {
                public void onBufferingUpdate(MediaPlayer mp, int percent)
                {
                    CurrentBufferPercentage = percent;
                }
            });
            mediaPlayer.setDataSource(getContext().getApplicationContext(), VideoUri);
            mediaPlayer.setSurface(surface);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setScreenOnWhilePlaying(true);
            mediaPlayer.prepareAsync();

            CurrentBufferPercentage = 0;
            CurrentState = STATE_PREPARING;
        }
        catch (Exception e)
        {
            CurrentState = STATE_ERROR;
        }
    }

    public interface OnVideoCompletion
    {
        void OnCompletion(MediaPlayer mp);
    }

    public void SetOnCompletion(OnVideoCompletion OnVideoCompletionListener)
    {
        OnCompletion = OnVideoCompletionListener;
    }

    public interface OnVideoPrepared
    {
        void OnPrepared(MediaPlayer mp);
    }

    public void SetOnPrepared(OnVideoPrepared OnVideoPreparedListener)
    {
        OnPrepared = OnVideoPreparedListener;
    }
}
