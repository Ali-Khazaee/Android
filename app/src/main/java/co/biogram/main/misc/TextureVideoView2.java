package co.biogram.main.misc;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.ImageView;
import android.widget.MediaController.MediaPlayerControl;
import android.widget.VideoView;


import java.io.IOException;

public class TextureVideoView2 extends TextureView implements MediaPlayerControl {
    private static final String LOG_TAG = "CenterCropVideoView";

    private static final int STATE_ERROR = -1;
    private static final int STATE_IDLE = 0;
    private static final int STATE_PREPARING = 1;
    private static final int STATE_PREPARED = 2;
    private static final int STATE_PLAYING = 3;
    private static final int STATE_PAUSED = 4;
    private static final int STATE_PLAYBACK_COMPLETED = 5;

    private int mCurrentState = STATE_IDLE;
    private int mTargetState = STATE_IDLE;

    // Components
    private Context mContext;
    private MediaPlayer mMediaPlayer;
    private Surface mSurface;
    private OnInfoListener mOnInfoListener;
    private OnCompletionListener mOCompletionListener;
    private OnErrorListener mOnErrorListener;
    private OnPreparedListener mOnPreparedListener;

    // Attributes
    private ScaleType mScaleType;
    private int mVideoWidth;
    private int mVideoHeight;
    private int mCurrentBufferPercentage;
    private boolean isSound;
    private Uri uri;

    public enum ScaleType {
        MATRIX(0),
        FIT_XY(1),
        FIT_START(2),
        FIT_CENTER(3),
        FIT_END(4),
        CENTER(5),
        CENTER_CROP(6),
        CENTER_INSIDE(7);

        ScaleType(int ni) {
            nativeInt = ni;
        }

        final int nativeInt;
    }

    private static final ScaleType[] sScaleTypeArray = {
            ScaleType.MATRIX,
            ScaleType.FIT_XY,
            ScaleType.FIT_START,
            ScaleType.FIT_CENTER,
            ScaleType.FIT_END,
            ScaleType.CENTER,
            ScaleType.CENTER_CROP,
            ScaleType.CENTER_INSIDE
    };

    public TextureVideoView2(final Context context) {
        super(context);

        isSound = true;
        mContext = context;
        initVideoView();
        setScaleType(ScaleType.FIT_CENTER);
    }

    public TextureVideoView2(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        isSound = true;
        mContext = context;
        initVideoView();
        initAttributes(context, attrs, 0);
    }

    public TextureVideoView2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        isSound = true;
        mContext = context;
        initVideoView();
        initAttributes(context, attrs, defStyle);
    }

    private void initAttributes(Context context, AttributeSet attrs, int defStyleAttr)
    {
        /*TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextureVideoView, defStyleAttr, 0);

        final int index = typedArray.getInt(R.styleable.TextureVideoView_scaleType, -1);
        if (index >= 0) {
            setScaleType(sScaleTypeArray[index]);
        } else {
            setScaleType(ScaleType.FIT_CENTER);
        }*/

        setScaleType(ScaleType.FIT_CENTER);
    }

    public void setScaleType(ScaleType scaleType) {
        if (scaleType == null) {
            throw new NullPointerException();
        }

        if (mScaleType != scaleType) {
            mScaleType = scaleType;

            setWillNotCacheDrawing(mScaleType == ScaleType.CENTER);

            requestLayout();
            invalidate();
            initVideo();
        }
    }

    @Override
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(VideoView.class.getName());
    }

    @Override
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(VideoView.class.getName());
    }

    public int resolveAdjustedSize(int desiredSize, int measureSpec) {
        Log.d(LOG_TAG, "Resolve called.");
        int result = desiredSize;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                /* Parent says we can be as big as we want. Just don't be larger
                 * than max size imposed on ourselves.
                 */
                result = desiredSize;
                break;

            case MeasureSpec.AT_MOST:
                /* Parent says we can be as big as we want, up to specSize.
                 * Don't be larger than specSize, and don't be larger than
                 * the max size imposed on ourselves.
                 */
                result = Math.min(desiredSize, specSize);
                break;

            case MeasureSpec.EXACTLY:
                // No choice. Do what we are told.
                result = specSize;
                break;
        }
        return result;
    }

    public void initVideoView() {
        mVideoHeight = 0;
        mVideoWidth = 0;
        setFocusable(false);
        setSurfaceTextureListener(mSurfaceTextureListener);
        mCurrentState = STATE_IDLE;
        mTargetState = STATE_IDLE;
    }

    private void initVideo() {
        // FIXME dirty code
        switch(mScaleType) {
            case FIT_CENTER:
                fitCenter();
                break;
            case CENTER_CROP:
                centerCrop();
                break;
            case MATRIX:
                matrix();
                break;
            case FIT_XY:
                fitXY();
                break;
            case FIT_START:
                fitStart();
                break;
            case FIT_END:
                fitEnd();
                break;
            case CENTER:
                center();
                break;
            case CENTER_INSIDE:
                centerInside();
                break;
        }
    }

    // FIXME dirty code
    private void matrix() {
        try {
            Matrix mMatrix = new Matrix();

            int viewWidth = getWidth();
            int viewHeight = getHeight();

            float mScaleX = (float) mVideoWidth / viewWidth;
            float mScaleY = (float) mVideoHeight / viewHeight;

            mMatrix.setScale(mScaleX, mScaleY);

            setTransform(mMatrix);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    // FIXME dirty code
    private void fitXY() {
        try {
            Matrix mMatrix = new Matrix();

            mMatrix.setScale(1f, 1f);

            setTransform(mMatrix);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    // FIXME dirty code
    private void center() {
        try {
            Matrix mMatrix = new Matrix();

            int viewWidth = getWidth();
            int viewHeight = getHeight();

            float mScaleX = (float) mVideoWidth / viewWidth;
            float mScaleY = (float) mVideoHeight / viewHeight;

            float mBoundX = viewWidth - mVideoWidth;
            float mBoundY = viewHeight - mVideoHeight;

            mMatrix.setScale(mScaleX, mScaleY);
            mMatrix.postTranslate(mBoundX / 2, mBoundY / 2);

            setTransform(mMatrix);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    // FIXME dirty code
    private void centerCrop() {
        try {
            Matrix mMatrix = new Matrix();

            int viewWidth = getWidth();
            int viewHeight = getHeight();

            float mScaleX = (float) mVideoWidth / viewWidth;
            float mScaleY = (float) mVideoHeight / viewHeight;

            float mBoundX = viewWidth - mVideoWidth / mScaleY;
            float mBoundY = viewHeight - mVideoHeight / mScaleX;

            if (mScaleX < mScaleY) {
                mScaleY = mScaleY * (1.0f / mScaleX);
                mScaleX = 1.0f;
                mBoundX = 0;
            } else {
                mScaleX = mScaleX * (1.0f / mScaleY);
                mScaleY = 1.0f;
                mBoundY = 0;
            }

            mMatrix.setScale(mScaleX, mScaleY);
            mMatrix.postTranslate(mBoundX / 2, mBoundY / 2);

            setTransform(mMatrix);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    // FIXME dirty code
    private void centerInside() {
        try {
            Matrix mMatrix = new Matrix();

            int viewWidth = getWidth();
            int viewHeight = getHeight();

            float mScaleX = (float) mVideoWidth / viewWidth;
            float mScaleY = (float) mVideoHeight / viewHeight;

            if(mScaleX > 1 || mScaleY > 1) {
                fitCenter();
            } else {
                float mBoundX = viewWidth - mVideoWidth;
                float mBoundY = viewHeight - mVideoHeight;

                mMatrix.setScale(mScaleX, mScaleY);
                mMatrix.postTranslate(mBoundX / 2, mBoundY / 2);

                setTransform(mMatrix);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    // FIXME dirty code
    private void fitStart() {
        try {
            Matrix mMatrix = new Matrix();

            int viewWidth = getWidth();
            int viewHeight = getHeight();

            float mScaleX = (float) mVideoWidth / viewWidth;
            float mScaleY = (float) mVideoHeight / viewHeight;

            float mBoundX = 0;
            float mBoundY = 0;

            if (mScaleX > mScaleY) {
                mScaleY = mScaleY * (1.0f / mScaleX);
                mScaleX = 1.0f;
            } else {
                mScaleX = mScaleX * (1.0f / mScaleY);
                mScaleY = 1.0f;
            }

            mMatrix.setScale(mScaleX, mScaleY);
            mMatrix.postTranslate(mBoundX / 2, mBoundY / 2);

            setTransform(mMatrix);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    // FIXME dirty code
    private void fitCenter() {
        try {
            Matrix mMatrix = new Matrix();

            int viewWidth = getWidth();
            int viewHeight = getHeight();

            float mScaleX = (float) mVideoWidth / viewWidth;
            float mScaleY = (float) mVideoHeight / viewHeight;

            float mBoundX = viewWidth - mVideoWidth / mScaleY;
            float mBoundY = viewHeight - mVideoHeight / mScaleX;

            if (mScaleX > mScaleY) {
                mScaleY = mScaleY * (1.0f / mScaleX);
                mScaleX = 1.0f;
                mBoundX = 0;
            } else {
                mScaleX = mScaleX * (1.0f / mScaleY);
                mScaleY = 1.0f;
                mBoundY = 0;
            }

            mMatrix.setScale(mScaleX, mScaleY);
            mMatrix.postTranslate(mBoundX / 2, mBoundY / 2);

            setTransform(mMatrix);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    // FIXME dirty code
    private void fitEnd() {
        try {
            Matrix mMatrix = new Matrix();

            int viewWidth = getWidth();
            int viewHeight = getHeight();

            float mScaleX = (float) mVideoWidth / viewWidth;
            float mScaleY = (float) mVideoHeight / viewHeight;

            float mBoundX = viewWidth - mVideoWidth / mScaleY;
            float mBoundY = viewHeight - mVideoHeight / mScaleX;

            if (mScaleX > mScaleY) {
                mScaleY = mScaleY * (1.0f / mScaleX);
                mScaleX = 1.0f;
                mBoundX = 0;
            } else {
                mScaleX = mScaleX * (1.0f / mScaleY);
                mScaleY = 1.0f;
                mBoundY = 0;
            }

            mMatrix.setScale(mScaleX, mScaleY);
            mMatrix.postTranslate(mBoundX, mBoundY);

            setTransform(mMatrix);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public void setVideoPath(String path) {
        if (path != null) {
            setVideoURI(Uri.parse(path));
        }
    }

    public void setVideoURI(Uri pVideoURI) {
        uri = pVideoURI;
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
            mTargetState = STATE_IDLE;
        }
    }

    public void openVideo() {
        if ((uri == null) || (mSurface == null)) {
            // not ready for playback just yet, will try again later
            return;
        }
        // Tell the music playback service to pause
        // TODO: these constants need to be published somewhere in the framework.
        Intent intent = new Intent("com.android.music.musicservicecommand");
        intent.putExtra("command", "pause");
        mContext.sendBroadcast(intent);

        // we shouldn't clear the target state, because somebody might have
        // called start() previously
        release(false);
        try {
            mMediaPlayer = new MediaPlayer();
            // TODO: create SubtitleController in MediaPlayer, but we need
            // a context for the subtitle renderers
            if (!isSound) {
                mMediaPlayer.setVolume(0f, 0f);
            } else {
                mMediaPlayer.setVolume(1f, 1f);
            }

            mMediaPlayer.setOnPreparedListener(mPreparedListener);
            mMediaPlayer.setOnVideoSizeChangedListener(mSizeChangedListener);
            mMediaPlayer.setOnCompletionListener(mCompletionListener);
            mMediaPlayer.setOnErrorListener(mErrorListener);
            mMediaPlayer.setOnInfoListener(mInfoListener);
            mMediaPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
            mCurrentBufferPercentage = 0;
            mMediaPlayer.setDataSource(mContext, uri);
            mMediaPlayer.setSurface(mSurface);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            mMediaPlayer.setScreenOnWhilePlaying(true);
            mMediaPlayer.prepareAsync();
            mCurrentState = STATE_PREPARING;
        } catch (IllegalStateException e) {
            mCurrentState = STATE_ERROR;
            mTargetState = STATE_ERROR;
            e.printStackTrace();
        } catch (IOException e) {
            mCurrentState = STATE_ERROR;
            mTargetState = STATE_ERROR;
            e.printStackTrace();
        }
    }

    private OnVideoSizeChangedListener mSizeChangedListener = new OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(final MediaPlayer mp, final int width, final int height) {
            mVideoWidth = mp.getVideoWidth();
            mVideoHeight = mp.getVideoHeight();
            if (mVideoWidth != 0 && mVideoHeight != 0) {
                requestLayout();
                initVideo();
            }
        }
    };

    private OnPreparedListener mPreparedListener = new OnPreparedListener() {
        @Override
        public void onPrepared(final MediaPlayer mp) {
            mCurrentState = STATE_PREPARED;

            if (mOnPreparedListener != null) {
                mOnPreparedListener.onPrepared(mp);
            }

            mVideoWidth = mp.getVideoWidth();
            mVideoHeight = mp.getVideoHeight();

            seekTo(1);

            if ((mVideoWidth != 0) && (mVideoHeight != 0)) {
                initVideo();

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

    private OnCompletionListener mCompletionListener = new OnCompletionListener() {
        @Override
        public void onCompletion(final MediaPlayer mp) {
            mCurrentState = STATE_PLAYBACK_COMPLETED;
            mTargetState = STATE_PLAYBACK_COMPLETED;

            if (mOCompletionListener != null) {
                mOCompletionListener.onCompletion(mMediaPlayer);
            }
        }
    };

    private OnInfoListener mInfoListener = new OnInfoListener() {
        public boolean onInfo(MediaPlayer mp, int arg1, int arg2) {
            if (mOnInfoListener != null) {
                mOnInfoListener.onInfo(mp, arg1, arg2);
            }
            return true;
        }
    };

    private OnErrorListener mErrorListener = new OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int framework_err, int impl_err) {
            Log.d(LOG_TAG, "Error: " + framework_err + "," + impl_err);
            mCurrentState = STATE_ERROR;
            mTargetState = STATE_ERROR;

        	/* If an error handler has been supplied, use it and finish. */
            if (mOnErrorListener != null) {
                if (mOnErrorListener.onError(mMediaPlayer, framework_err, impl_err)) {
                    return true;
                }
            }
            return true;
        }
    };

    private OnBufferingUpdateListener mBufferingUpdateListener = new OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(final MediaPlayer mp, final int percent) {
            mCurrentBufferPercentage = percent;
        }
    };

    public void setOnPreparedListener(OnPreparedListener listener) {
        mOnPreparedListener = listener;
    }

    public void setOnCompletionListener(OnCompletionListener listener) {
        mOCompletionListener = listener;
    }

    public void setOnErrorListener(OnErrorListener listener) {
        mOnErrorListener = listener;
    }

    public void setOnInfoListener(OnInfoListener listener) {
        mOnInfoListener = listener;
    }

    private void release(boolean cleartargetstate) {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
            mCurrentState = STATE_IDLE;
            if (cleartargetstate) {
                mTargetState = STATE_IDLE;
            }
        }
    }

    @Override
    public void start() {
        if (isInPlaybackState()) {
            mMediaPlayer.setLooping(true);
            mMediaPlayer.start();
            mCurrentState = STATE_PLAYING;
        }
        mTargetState = STATE_PLAYING;
    }

    @Override
    public void pause() {
        if (isInPlaybackState()) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
                mCurrentState = STATE_PAUSED;
            }
        }
        mTargetState = STATE_PAUSED;
    }

    public void suspend() {
        release(false);
    }

    public void resume() {
        openVideo();
    }

    @Override
    public int getDuration() {
        if (isInPlaybackState()) {
            return mMediaPlayer.getDuration();
        }

        return -1;
    }

    @Override
    public int getCurrentPosition() {
        if (isInPlaybackState()) {
            return mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public void seekTo(int msec) {
        if (isInPlaybackState()) {
            mMediaPlayer.seekTo(msec);
        }
    }

    @Override
    public boolean isPlaying() {
        return isInPlaybackState() && mMediaPlayer.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        if (mMediaPlayer != null) {
            return mCurrentBufferPercentage;
        }
        return 0;
    }

    private boolean isInPlaybackState() {
        return (mMediaPlayer != null &&
                mCurrentState != STATE_ERROR &&
                mCurrentState != STATE_IDLE &&
                mCurrentState != STATE_PREPARING);
    }

    @Override
    public boolean canPause() {
        return false;
    }

    @Override
    public boolean canSeekBackward() {
        return false;
    }

    @Override
    public boolean canSeekForward() {
        return false;
    }

    @Override
    public int getAudioSessionId() {
        return -1;
    }

    private SurfaceTextureListener mSurfaceTextureListener = new SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, final int width, final int height) {
            mSurface = new Surface(surface);
            openVideo();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, final int width, final int height) {
            boolean isValidState = (mTargetState == STATE_PLAYING);
            boolean hasValidSize = (mVideoWidth == width && mVideoHeight == height);
            if (mMediaPlayer != null && isValidState && hasValidSize) {
                start();
            }
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            if (mMediaPlayer != null) {
                mMediaPlayer.reset();
                mMediaPlayer.release();
                mMediaPlayer = null;
            }

            if (mSurface != null) {
                mSurface.release();
                mSurface = null;
            }

            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        }
    };

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);

        if (visibility == View.INVISIBLE || visibility == View.GONE) {
            if (isPlaying()) {
                stopPlayback();
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopPlayback();
    }

    public void setSound(boolean isSound) {
        this.isSound = isSound;

        if(mMediaPlayer != null) {
            if (!isSound) {
                mMediaPlayer.setVolume(0f, 0f);
            } else {
                mMediaPlayer.setVolume(1f, 1f);
            }
        }
    }

    public int getVideoWidth() {
        return mVideoWidth;
    }

    public int getVideoHeight() {
        return mVideoHeight;
    }
}