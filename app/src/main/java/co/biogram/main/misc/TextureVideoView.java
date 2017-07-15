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

import co.biogram.main.handler.MiscHandler;

public class TextureVideoView extends TextureView implements MediaPlayerControl
{
    private static final int STATE_ERROR = -1;
    private static final int STATE_IDLE = 0;
    private static final int STATE_PREPARING = 1;
    private static final int STATE_PREPARED = 2;
    private static final int STATE_PLAYING = 3;
    private static final int STATE_PAUSED = 4;

    private Uri mUri;

    private int mCurrentState = STATE_IDLE;
    private int mTargetState = STATE_IDLE;

    private Surface mSurface = null;
    private MediaPlayer mMediaPlayer = null;
    private int mAudioSession;
    private int mVideoWidth;
    private int mVideoHeight;
    private int mCurrentBufferPercentage;
    private int mSeekWhenPrepared;
    private boolean mCanPause;
    private boolean mCanSeekBack;
    private boolean mCanSeekForward;
    private boolean mShouldRequestAudioFocus = true;

    public TextureVideoView(Context context)
    {
        this(context, null);
    }

    public TextureVideoView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public TextureVideoView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);

        mVideoWidth = 0;
        mVideoHeight = 0;

        setSurfaceTextureListener(mSurfaceTextureListener);

        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();

        mCurrentState = STATE_IDLE;
        mTargetState = STATE_IDLE;
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

    @Override
    public void start()
    {
        if (isInPlaybackState())
        {
            mMediaPlayer.start();
            mCurrentState = STATE_PLAYING;
        }

        mTargetState = STATE_PLAYING;
    }

    @Override
    public void pause()
    {
        if (isInPlaybackState())
        {
            if (mMediaPlayer.isPlaying())
            {
                mMediaPlayer.pause();
                mCurrentState = STATE_PAUSED;
            }
        }

        mTargetState = STATE_PAUSED;
    }

    @Override
    public int getDuration()
    {
        if (isInPlaybackState())
            return mMediaPlayer.getDuration();

        return -1;
    }

    @Override
    public int getCurrentPosition()
    {
        if (isInPlaybackState())
            return mMediaPlayer.getCurrentPosition();

        return 0;
    }

    @Override
    public void seekTo(int time)
    {
        if (isInPlaybackState())
        {
            mMediaPlayer.seekTo(time);
            mSeekWhenPrepared = 0;
            return;
        }

        mSeekWhenPrepared = time;
    }

    @Override
    public boolean isPlaying()
    {
        return isInPlaybackState() && mMediaPlayer.isPlaying();
    }

    @Override
    public int getBufferPercentage()
    {
        if (mMediaPlayer != null)
            return mCurrentBufferPercentage;

        return 0;
    }

    @Override
    public boolean canPause()
    {
        return mCanPause;
    }

    @Override
    public boolean canSeekBackward()
    {
        return mCanSeekBack;
    }

    @Override
    public boolean canSeekForward()
    {
        return mCanSeekForward;
    }

    public void SetVideoURI(String Url)
    {
        mUri = Uri.parse(Url);
        mSeekWhenPrepared = 0;
        openVideo();
        requestLayout();
        invalidate();
    }

    public void stopPlayback() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
            mCurrentState = STATE_IDLE;
            mTargetState  = STATE_IDLE;
            if (mShouldRequestAudioFocus) {
                AudioManager am = (AudioManager) getContext().getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
                am.abandonAudioFocus(null);
            }
        }
        clearSurface();
    }


    private void clearSurface()
    {
        if (mSurface == null)
            return;

        EGL10 egl = (EGL10) EGLContext.getEGL();
        EGLDisplay display = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        egl.eglInitialize(display, null);

        int[] attribList = {
                EGL10.EGL_RED_SIZE, 8,
                EGL10.EGL_GREEN_SIZE, 8,
                EGL10.EGL_BLUE_SIZE, 8,
                EGL10.EGL_ALPHA_SIZE, 8,
                EGL10.EGL_RENDERABLE_TYPE, EGL10.EGL_WINDOW_BIT,
                EGL10.EGL_NONE, 0,      // placeholder for recordable [@-3]
                EGL10.EGL_NONE
        };
        EGLConfig[] configs = new EGLConfig[1];
        int[] numConfigs = new int[1];
        egl.eglChooseConfig(display, attribList, configs, configs.length, numConfigs);
        EGLConfig config = configs[0];
        EGLContext context = egl.eglCreateContext(display, config, EGL10.EGL_NO_CONTEXT, new int[]{
                12440, 2, EGL10.EGL_NONE
        });
        EGLSurface eglSurface = egl.eglCreateWindowSurface(display, config, mSurface, new int[]{
                EGL10.EGL_NONE
        });

        egl.eglMakeCurrent(display, eglSurface, eglSurface, context);
        GLES20.glClearColor(0, 0, 0, 1);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        egl.eglSwapBuffers(display, eglSurface);
        egl.eglDestroySurface(display, eglSurface);
        egl.eglMakeCurrent(display, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
        egl.eglDestroyContext(display, context);
        egl.eglTerminate(display);
    }

    private void openVideo()
    {
        if (mUri == null || mSurface == null)
        {
            // not ready for playback just yet, will try again later
            return;
        }
        // we shouldn't clear the target state, because somebody might have
        // called start() previously
        release(false);

        if (mShouldRequestAudioFocus)
        {
            AudioManager am = (AudioManager) getContext().getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
            am.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        }

        try
        {
            mMediaPlayer = new MediaPlayer();

            if (mAudioSession != 0) {
                mMediaPlayer.setAudioSessionId(mAudioSession);
            } else {
                mAudioSession = mMediaPlayer.getAudioSessionId();
            }


            //mMediaPlayer.setVolume(0.0f, 0.0f);

            mMediaPlayer.setOnPreparedListener(mPreparedListener);
            mMediaPlayer.setOnCompletionListener(mCompletionListener);
            mMediaPlayer.setOnErrorListener(mErrorListener);
            mMediaPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
            mCurrentBufferPercentage = 0;
            mMediaPlayer.setDataSource(getContext().getApplicationContext(), mUri);
            mMediaPlayer.setSurface(mSurface);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setScreenOnWhilePlaying(true);
            mMediaPlayer.prepareAsync();

            // we don't set the target state here either, but preserve the
            // target state that was there before.
            mCurrentState = STATE_PREPARING;
        }
        catch (Exception e)
        {
            mCurrentState = STATE_ERROR;
            mTargetState = STATE_ERROR;
            mErrorListener.onError(mMediaPlayer, MediaPlayer.MEDIA_ERROR_UNKNOWN, 0);
        }
    }

    MediaPlayer.OnPreparedListener mPreparedListener = new MediaPlayer.OnPreparedListener()
    {
        public void onPrepared(MediaPlayer mp)
        {
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

            ViewGroup ParentView = (ViewGroup) getParent();

            ViewGroup.LayoutParams ParentViewParam = ParentView.getLayoutParams();
            ParentViewParam.height = Height;

            ParentView.setLayoutParams(ParentViewParam);

            ViewGroup.LayoutParams TextureViewParam = getLayoutParams();
            TextureViewParam.width = ScreenWidth;
            TextureViewParam.height = Height;

            setLayoutParams(TextureViewParam);

            mCurrentState = STATE_PREPARED;

            mCanPause = mCanSeekBack = mCanSeekForward = true;

            mVideoWidth = mp.getVideoWidth();
            mVideoHeight = mp.getVideoHeight();

            int seekToPosition = mSeekWhenPrepared;  // mSeekWhenPrepared may be changed after seekTo() call
            if (seekToPosition != 0) {
                seekTo(seekToPosition);
            }
            if (mVideoWidth != 0 && mVideoHeight != 0) {
                //Log.i("@@@@", "video size: " + mVideoWidth +"/"+ mVideoHeight);
                getSurfaceTexture().setDefaultBufferSize(mVideoWidth, mVideoHeight);
                // We won't get a "surface changed" callback if the surface is already the right size, so
                // start the video here instead of in the callback.
                if (mTargetState == STATE_PLAYING) {
                    start();

                }
            } else {
                // We don't know the video size yet, but should start anyway.
                // The video size might be reported to us later.
                if (mTargetState == STATE_PLAYING) {
                    start();
                }
            }
        }
    };

    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener()
    {
        public void onCompletion(MediaPlayer mp)
        {
            mp.seekTo(0);
            mp.start();
        }
    };

    private MediaPlayer.OnErrorListener mErrorListener = new MediaPlayer.OnErrorListener()
    {
        public boolean onError(MediaPlayer mp, int framework_err, int impl_err)
        {
            mCurrentState = STATE_ERROR;
            mTargetState = STATE_ERROR;
            return true;
        }
    };

    private MediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener()
    {
        public void onBufferingUpdate(MediaPlayer mp, int percent)
        {
            mCurrentBufferPercentage = percent;
        }
    };

    TextureView.SurfaceTextureListener mSurfaceTextureListener = new SurfaceTextureListener()
    {
        @Override
        public void onSurfaceTextureSizeChanged(final SurfaceTexture surface, final int width, final int height)
        {
            boolean isValidState =  (mTargetState == STATE_PLAYING);
            boolean hasValidSize = (width > 0 && height > 0);

            if (mMediaPlayer != null && isValidState && hasValidSize)
            {
                if (mSeekWhenPrepared != 0)
                    seekTo(mSeekWhenPrepared);

                start();
            }
        }

        @Override
        public void onSurfaceTextureAvailable(final SurfaceTexture surface, final int width, final int height)
        {
            mSurface = new Surface(surface);
            openVideo();
        }

        @Override
        public boolean onSurfaceTextureDestroyed(final SurfaceTexture surface)
        {
            // after we return from this we can't use the surface any more
            if (mSurface != null)
            {
                mSurface.release();
                mSurface = null;
            }

            release(true);
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(final SurfaceTexture surface) { }
    };

    private void release(boolean cleartargetstate)
    {
        if (mMediaPlayer != null)
        {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
            mCurrentState = STATE_IDLE;

            if (cleartargetstate)
            {
                mTargetState  = STATE_IDLE;
            }

            if (mShouldRequestAudioFocus)
            {
                AudioManager am = (AudioManager) getContext().getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
                am.abandonAudioFocus(null);
            }
        }
    }

    public void Suspend()
    {
        release(false);
    }

    public void Resume()
    {
        openVideo();
    }

    private boolean isInPlaybackState()
    {
        return (mMediaPlayer != null && mCurrentState != STATE_ERROR && mCurrentState != STATE_IDLE && mCurrentState != STATE_PREPARING);
    }

    public int getAudioSessionId()
    {
        if (mAudioSession == 0)
        {
            MediaPlayer TempMediaPlayer = new MediaPlayer();
            mAudioSession = TempMediaPlayer.getAudioSessionId();
            TempMediaPlayer.release();
        }

        return mAudioSession;
    }
}
