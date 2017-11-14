package co.biogram.main.ui.view;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.view.ScaleGestureDetector;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.graphics.Matrix.ScaleToFit;
import android.view.View.OnLongClickListener;
import android.view.ViewParent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView.ScaleType;
import android.widget.OverScroller;

public class PhotoView extends ImageView
{
    private PhotoViewAttacher Attacher;
    private ScaleType scaleType;

    public PhotoView(Context context)
    {
        super(context);

        Attacher = new PhotoViewAttacher(this);

        super.setScaleType(ImageView.ScaleType.MATRIX);

        if (scaleType != null)
        {
            setScaleType(scaleType);
            scaleType = null;
        }
    }

    @Override
    public ScaleType getScaleType()
    {
        return Attacher.getScaleType();
    }

    @Override
    public Matrix getImageMatrix()
    {
        return Attacher.getImageMatrix();
    }

    @Override
    public void setOnLongClickListener(OnLongClickListener l)
    {
        Attacher.setOnLongClickListener(l);
    }

    @Override
    public void setOnClickListener(OnClickListener l)
    {
        Attacher.setOnClickListener(l);
    }

    @Override
    public void setScaleType(ScaleType scaletype)
    {
        if (Attacher == null)
            scaleType = scaletype;
        else
            Attacher.setScaleType(scaletype);
    }

    @Override
    public void setImageDrawable(Drawable drawable)
    {
        super.setImageDrawable(drawable);

        if (Attacher != null)
            Attacher.update();
    }

    @Override
    public void setImageResource(int resId)
    {
        super.setImageResource(resId);

        if (Attacher != null)
            Attacher.update();
    }

    @Override
    public void setImageURI(Uri uri)
    {
        super.setImageURI(uri);

        if (Attacher != null)
            Attacher.update();
    }

    @Override
    protected boolean setFrame(int l, int t, int r, int b)
    {
        boolean Changed = super.setFrame(l, t, r, b);

        if (Changed)
            Attacher.update();

        return Changed;
    }

    interface OnGestureListener
    {
        void onDrag(float dx, float dy);
        void onScale(float scaleFactor, float focusX, float focusY);
        void onFling(float startX, float startY, float velocityX, float velocityY);
    }
}

class CustomGestureDetector
{
    private static final int INVALID_POINTER_ID = -1;
    private int mActivePointerId = INVALID_POINTER_ID;
    private int mActivePointerIndex = 0;
    private final ScaleGestureDetector mDetector;
    private VelocityTracker mVelocityTracker;
    private boolean mIsDragging;
    private float mLastTouchX;
    private float mLastTouchY;
    private final float mTouchSlop;
    private final float mMinimumVelocity;
    private PhotoView.OnGestureListener mListener;

    CustomGestureDetector(Context context, PhotoView.OnGestureListener listener)
    {
        ViewConfiguration configuration = ViewConfiguration.get(context);
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mTouchSlop = configuration.getScaledTouchSlop();
        mListener = listener;

        ScaleGestureDetector.OnScaleGestureListener mScaleListener = new ScaleGestureDetector.OnScaleGestureListener()
        {
            @Override
            public boolean onScale(ScaleGestureDetector d)
            {
                float Factor = d.getScaleFactor();

                if (Float.isNaN(Factor) || Float.isInfinite(Factor))
                    return false;

                mListener.onScale(Factor, d.getFocusX(), d.getFocusY());
                return true;
            }

            @Override
            public boolean onScaleBegin(ScaleGestureDetector d)
            {
                return true;
            }

            @Override public void onScaleEnd(ScaleGestureDetector detector) { }
        };

        mDetector = new ScaleGestureDetector(context, mScaleListener);
    }

    private float getActiveX(MotionEvent ev)
    {
        try
        {
            return ev.getX(mActivePointerIndex);
        }
        catch (Exception e)
        {
            return ev.getX();
        }
    }

    private float getActiveY(MotionEvent ev)
    {
        try
        {
            return ev.getY(mActivePointerIndex);
        }
        catch (Exception e)
        {
            return ev.getY();
        }
    }

    boolean isScaling()
    {
        return mDetector.isInProgress();
    }

    boolean isDragging()
    {
        return mIsDragging;
    }

    boolean onTouchEvent(MotionEvent ev)
    {
        try
        {
            mDetector.onTouchEvent(ev);
            return processTouchEvent(ev);
        }
        catch (IllegalArgumentException e)
        {
            return true;
        }
    }

    private boolean processTouchEvent(MotionEvent ev)
    {
        int action = ev.getAction();

        switch (action & MotionEvent.ACTION_MASK)
        {
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = ev.getPointerId(0);
                mVelocityTracker = VelocityTracker.obtain();

                if (null != mVelocityTracker)
                    mVelocityTracker.addMovement(ev);

                mLastTouchX = getActiveX(ev);
                mLastTouchY = getActiveY(ev);
                mIsDragging = false;
                break;
            case MotionEvent.ACTION_MOVE:
                float x = getActiveX(ev);
                float y = getActiveY(ev);
                float dx = x - mLastTouchX, dy = y - mLastTouchY;

                if (!mIsDragging)
                    mIsDragging = Math.sqrt((dx * dx) + (dy * dy)) >= mTouchSlop;

                if (mIsDragging)
                {
                    mListener.onDrag(dx, dy);
                    mLastTouchX = x;
                    mLastTouchY = y;

                    if (null != mVelocityTracker)
                        mVelocityTracker.addMovement(ev);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                mActivePointerId = INVALID_POINTER_ID;

                if (null != mVelocityTracker)
                {
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }
                break;
            case MotionEvent.ACTION_UP:
                mActivePointerId = INVALID_POINTER_ID;

                if (mIsDragging)
                {
                    if (null != mVelocityTracker)
                    {
                        mLastTouchX = getActiveX(ev);
                        mLastTouchY = getActiveY(ev);

                        mVelocityTracker.addMovement(ev);
                        mVelocityTracker.computeCurrentVelocity(1000);

                        float vX = mVelocityTracker.getXVelocity(), vY = mVelocityTracker.getYVelocity();

                        if (Math.max(Math.abs(vX), Math.abs(vY)) >= mMinimumVelocity)
                            mListener.onFling(mLastTouchX, mLastTouchY, -vX, -vY);
                    }
                }

                if (null != mVelocityTracker)
                {
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                int pointerId = ev.getPointerId(pointerIndex);

                if (pointerId == mActivePointerId)
                {
                    int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mActivePointerId = ev.getPointerId(newPointerIndex);
                    mLastTouchX = ev.getX(newPointerIndex);
                    mLastTouchY = ev.getY(newPointerIndex);
                }
                break;
        }

        mActivePointerIndex = ev.findPointerIndex(mActivePointerId != INVALID_POINTER_ID ? mActivePointerId : 0);
        return true;
    }
}

class PhotoViewAttacher implements View.OnTouchListener, View.OnLayoutChangeListener
{
    private static float DEFAULT_MAX_SCALE = 3.0f;
    private static float DEFAULT_MID_SCALE = 1.75f;
    private static float DEFAULT_MIN_SCALE = 1.0f;
    private static int DEFAULT_ZOOM_DURATION = 200;
    private static final int EDGE_NONE = -1;
    private static final int EDGE_LEFT = 0;
    private static final int EDGE_RIGHT = 1;
    private static final int EDGE_BOTH = 2;
    private Interpolator mInterpolator = new AccelerateDecelerateInterpolator();
    private int mZoomDuration = DEFAULT_ZOOM_DURATION;
    private float mMinScale = DEFAULT_MIN_SCALE;
    private float mMidScale = DEFAULT_MID_SCALE;
    private float mMaxScale = DEFAULT_MAX_SCALE;
    private boolean mBlockParentIntercept = false;
    private ImageView mImageView;
    private GestureDetector mGestureDetector;
    private CustomGestureDetector mScaleDragDetector;
    private final Matrix mBaseMatrix = new Matrix();
    private final Matrix mDrawMatrix = new Matrix();
    private final Matrix mSuppMatrix = new Matrix();
    private final RectF mDisplayRect = new RectF();
    private final float[] mMatrixValues = new float[9];
    private View.OnClickListener mOnClickListener;
    private OnLongClickListener mLongClickListener;
    private FlingRunnable mCurrentFlingRunnable;
    private int mScrollEdge = EDGE_BOTH;
    private float mBaseRotation;
    private boolean mZoomEnabled = true;
    private ScaleType mScaleType = ScaleType.FIT_CENTER;

    private PhotoView.OnGestureListener onGestureListener = new PhotoView.OnGestureListener()
    {
        @Override
        public void onDrag(float dx, float dy)
        {
            if (mScaleDragDetector.isScaling())
                return;

            mSuppMatrix.postTranslate(dx, dy);
            checkAndDisplayMatrix();

            ViewParent parent = mImageView.getParent();

            if (!mScaleDragDetector.isScaling() && !mBlockParentIntercept)
            {
                if (mScrollEdge == EDGE_BOTH || (mScrollEdge == EDGE_LEFT && dx >= 1f) || (mScrollEdge == EDGE_RIGHT && dx <= -1f))
                    if (parent != null)
                        parent.requestDisallowInterceptTouchEvent(false);
            }
            else if (parent != null)
                    parent.requestDisallowInterceptTouchEvent(true);
        }

        @Override
        public void onFling(float startX, float startY, float velocityX, float velocityY)
        {
            mCurrentFlingRunnable = new FlingRunnable(mImageView.getContext());
            mCurrentFlingRunnable.fling(getImageViewWidth(mImageView), getImageViewHeight(mImageView), (int) velocityX, (int) velocityY);
            mImageView.post(mCurrentFlingRunnable);
        }

        @Override
        public void onScale(float scaleFactor, float focusX, float focusY)
        {
            if ((getScale() < mMaxScale || scaleFactor < 1f) && (getScale() > mMinScale || scaleFactor > 1f))
            {
                mSuppMatrix.postScale(scaleFactor, scaleFactor, focusX, focusY);
                checkAndDisplayMatrix();
            }
        }
    };

     PhotoViewAttacher(ImageView imageView)
     {
        mImageView = imageView;
        imageView.setOnTouchListener(this);
        imageView.addOnLayoutChangeListener(this);

        if (imageView.isInEditMode())
            return;

        mBaseRotation = 0.0f;
        mScaleDragDetector = new CustomGestureDetector(imageView.getContext(), onGestureListener);
        mGestureDetector = new GestureDetector(imageView.getContext(), new GestureDetector.SimpleOnGestureListener()
        {
            @Override
            public void onLongPress(MotionEvent e)
            {
                if (mLongClickListener != null)
                    mLongClickListener.onLongClick(mImageView);
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
            {
                return false;
            }
        });

        mGestureDetector.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener()
        {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e)
            {
                if (mOnClickListener != null)
                    mOnClickListener.onClick(mImageView);

                RectF displayRect = getDisplayRect();
                float x = e.getX(), y = e.getY();

                return (displayRect != null && displayRect.contains(x, y));
            }

            @Override
            public boolean onDoubleTap(MotionEvent ev)
            {
                try
                {
                    float scale = getScale();
                    float x = ev.getX();
                    float y = ev.getY();

                    if (scale < getMediumScale())
                        setScale(getMediumScale(), x, y, true);
                    else if (scale >= getMediumScale() && scale < getMaximumScale())
                        setScale(getMaximumScale(), x, y, true);
                    else
                        setScale(getMinimumScale(), x, y, true);
                }
                catch (ArrayIndexOutOfBoundsException e)
                {
                    //
                }

                return true;
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent e)
            {
                return false;
            }
        });
    }

    private RectF getDisplayRect()
    {
        checkMatrixBounds();
        return getDisplayRect(getDrawMatrix());
    }

    private void setRotationBy(float degrees)
    {
        mSuppMatrix.postRotate(degrees % 360);
        checkAndDisplayMatrix();
    }

    private float getMinimumScale()
    {
        return mMinScale;
    }

    private float getMediumScale()
    {
        return mMidScale;
    }

    private float getMaximumScale()
    {
        return mMaxScale;
    }

    private float getScale()
    {
        return (float) Math.sqrt((float) Math.pow(getValue(mSuppMatrix, Matrix.MSCALE_X), 2) + (float) Math.pow(getValue(mSuppMatrix, Matrix.MSKEW_Y), 2));
    }

    ScaleType getScaleType()
    {
        return mScaleType;
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom)
    {
        if (left != oldLeft || top != oldTop || right != oldRight || bottom != oldBottom)
            updateBaseMatrix(mImageView.getDrawable());
    }

    @Override
    public boolean onTouch(View v, MotionEvent ev)
    {
        boolean handled = false;

        if (mZoomEnabled && ((ImageView) v).getDrawable() != null)
        {
            switch (ev.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    ViewParent parent = v.getParent();

                    if (parent != null)
                        parent.requestDisallowInterceptTouchEvent(true);

                    cancelFling();
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    if (getScale() < mMinScale)
                    {
                        RectF rect = getDisplayRect();

                        if (rect != null)
                        {
                            v.post(new AnimatedZoomRunnable(getScale(), mMinScale, rect.centerX(), rect.centerY()));
                            handled = true;
                        }
                    }
                    else if (getScale() > mMaxScale)
                    {
                        RectF rect = getDisplayRect();

                        if (rect != null)
                        {
                            v.post(new AnimatedZoomRunnable(getScale(), mMaxScale, rect.centerX(), rect.centerY()));
                            handled = true;
                        }
                    }
                    break;
            }

            if (mScaleDragDetector != null)
            {
                boolean wasScaling = mScaleDragDetector.isScaling();
                boolean wasDragging = mScaleDragDetector.isDragging();

                handled = mScaleDragDetector.onTouchEvent(ev);

                boolean didntScale = !wasScaling && !mScaleDragDetector.isScaling();
                boolean didntDrag = !wasDragging && !mScaleDragDetector.isDragging();

                mBlockParentIntercept = didntScale && didntDrag;
            }

            if (mGestureDetector != null && mGestureDetector.onTouchEvent(ev))
                handled = true;
        }

        return handled;
    }

    void setOnLongClickListener(OnLongClickListener listener)
    {
        mLongClickListener = listener;
    }

    public void setOnClickListener(View.OnClickListener listener)
    {
        mOnClickListener = listener;
    }

    private void setScale(float scale, float focalX, float focalY, boolean animate)
    {
        if (scale < mMinScale || scale > mMaxScale)
            throw new IllegalArgumentException("Scale must be within the range of minScale and maxScale");

        if (animate)
            mImageView.post(new AnimatedZoomRunnable(getScale(), scale, focalX, focalY));
        else
        {
            mSuppMatrix.setScale(scale, scale, focalX, focalY);
            checkAndDisplayMatrix();
        }
    }

    public void setScaleType(ScaleType scaleType)
    {
        if (scaleType != ImageView.ScaleType.MATRIX && scaleType != mScaleType)
        {
            mScaleType = scaleType;
            update();
        }
    }

    void update()
    {
        if (mZoomEnabled)
            updateBaseMatrix(mImageView.getDrawable());
        else
            resetMatrix();
    }

    private Matrix getDrawMatrix()
    {
        mDrawMatrix.set(mBaseMatrix);
        mDrawMatrix.postConcat(mSuppMatrix);
        return mDrawMatrix;
    }

    Matrix getImageMatrix()
    {
        return mDrawMatrix;
    }

    private float getValue(Matrix matrix, int whichValue)
    {
        matrix.getValues(mMatrixValues);
        return mMatrixValues[whichValue];
    }

    private void resetMatrix()
    {
        mSuppMatrix.reset();
        setRotationBy(mBaseRotation);
        setImageViewMatrix(getDrawMatrix());
        checkMatrixBounds();
    }

    private void setImageViewMatrix(Matrix matrix)
    {
        mImageView.setImageMatrix(matrix);
    }

    private void checkAndDisplayMatrix()
    {
        if (checkMatrixBounds())
            setImageViewMatrix(getDrawMatrix());
    }

    private RectF getDisplayRect(Matrix matrix)
    {
        Drawable d = mImageView.getDrawable();

        if (d != null)
        {
            mDisplayRect.set(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            matrix.mapRect(mDisplayRect);
            return mDisplayRect;
        }

        return null;
    }

    private void updateBaseMatrix(Drawable drawable)
    {
        if (drawable == null)
            return;

        float viewWidth = getImageViewWidth(mImageView);
        float viewHeight = getImageViewHeight(mImageView);
        int drawableW = drawable.getIntrinsicWidth();
        int drawableH = drawable.getIntrinsicHeight();

        mBaseMatrix.reset();

        float widthScale = viewWidth / drawableW;
        float heightScale = viewHeight / drawableH;

        if (mScaleType == ScaleType.CENTER)
            mBaseMatrix.postTranslate((viewWidth - drawableW) / 2F, (viewHeight - drawableH) / 2F);
        else if (mScaleType == ScaleType.CENTER_CROP)
        {
            float scale = Math.max(widthScale, heightScale);
            mBaseMatrix.postScale(scale, scale);
            mBaseMatrix.postTranslate((viewWidth - drawableW * scale) / 2F, (viewHeight - drawableH * scale) / 2F);
        }
        else if (mScaleType == ScaleType.CENTER_INSIDE)
        {
            float scale = Math.min(1.0f, Math.min(widthScale, heightScale));
            mBaseMatrix.postScale(scale, scale);
            mBaseMatrix.postTranslate((viewWidth - drawableW * scale) / 2F, (viewHeight - drawableH * scale) / 2F);
        }
        else
        {
            RectF mTempSrc = new RectF(0, 0, drawableW, drawableH);
            RectF mTempDst = new RectF(0, 0, viewWidth, viewHeight);

            if ((int) mBaseRotation % 180 != 0)
                mTempSrc = new RectF(0, 0, drawableH, drawableW);

            switch (mScaleType)
            {
                case FIT_CENTER:
                    mBaseMatrix.setRectToRect(mTempSrc, mTempDst, ScaleToFit.CENTER);
                    break;
                case FIT_START:
                    mBaseMatrix.setRectToRect(mTempSrc, mTempDst, ScaleToFit.START);
                    break;
                case FIT_END:
                    mBaseMatrix.setRectToRect(mTempSrc, mTempDst, ScaleToFit.END);
                    break;
                case FIT_XY:
                    mBaseMatrix.setRectToRect(mTempSrc, mTempDst, ScaleToFit.FILL);
                    break;
            }
        }

        resetMatrix();
    }

    private boolean checkMatrixBounds()
    {
        RectF rect = getDisplayRect(getDrawMatrix());

        if (rect == null)
            return false;

        float height = rect.height(), width = rect.width();
        float deltaX = 0, deltaY = 0;
        int viewHeight = getImageViewHeight(mImageView);

        if (height <= viewHeight)
        {
            switch (mScaleType)
            {
                case FIT_START:
                    deltaY = -rect.top;
                    break;
                case FIT_END:
                    deltaY = viewHeight - height - rect.top;
                    break;
                default:
                    deltaY = (viewHeight - height) / 2 - rect.top;
                    break;
            }
        }
        else if (rect.top > 0)
            deltaY = -rect.top;
        else if (rect.bottom < viewHeight)
            deltaY = viewHeight - rect.bottom;

        int viewWidth = getImageViewWidth(mImageView);

        if (width <= viewWidth)
        {
            switch (mScaleType)
            {
                case FIT_START:
                    deltaX = -rect.left;
                    break;
                case FIT_END:
                    deltaX = viewWidth - width - rect.left;
                    break;
                default:
                    deltaX = (viewWidth - width) / 2 - rect.left;
                    break;
            }

            mScrollEdge = EDGE_BOTH;
        }
        else if (rect.left > 0)
        {
            mScrollEdge = EDGE_LEFT;
            deltaX = -rect.left;
        }
        else if (rect.right < viewWidth)
        {
            deltaX = viewWidth - rect.right;
            mScrollEdge = EDGE_RIGHT;
        }
        else
            mScrollEdge = EDGE_NONE;

        mSuppMatrix.postTranslate(deltaX, deltaY);
        return true;
    }

    private int getImageViewWidth(ImageView imageView)
    {
        return imageView.getWidth() - imageView.getPaddingLeft() - imageView.getPaddingRight();
    }

    private int getImageViewHeight(ImageView imageView)
    {
        return imageView.getHeight() - imageView.getPaddingTop() - imageView.getPaddingBottom();
    }

    private void cancelFling()
    {
        if (mCurrentFlingRunnable != null)
        {
            mCurrentFlingRunnable.cancelFling();
            mCurrentFlingRunnable = null;
        }
    }

    private class AnimatedZoomRunnable implements Runnable
    {
        private final float mFocalX, mFocalY;
        private final long mStartTime;
        private final float mZoomStart, mZoomEnd;

         AnimatedZoomRunnable(final float currentZoom, final float targetZoom, final float focalX, final float focalY)
         {
            mFocalX = focalX;
            mFocalY = focalY;
            mStartTime = System.currentTimeMillis();
            mZoomStart = currentZoom;
            mZoomEnd = targetZoom;
        }

        @Override
        public void run()
        {
            float t = interpolate();
            float scale = mZoomStart + t * (mZoomEnd - mZoomStart);
            float deltaScale = scale / getScale();

            onGestureListener.onScale(deltaScale, mFocalX, mFocalY);

            if (t < 1f)
                mImageView.postOnAnimation(this);
        }

        private float interpolate()
        {
            float t = 1f * (System.currentTimeMillis() - mStartTime) / mZoomDuration;
            t = Math.min(1f, t);
            t = mInterpolator.getInterpolation(t);
            return t;
        }
    }

    private class FlingRunnable implements Runnable
    {
        private final OverScroller mScroller;
        private int mCurrentX, mCurrentY;

        FlingRunnable(Context context)
        {
            mScroller = new OverScroller(context);
        }

        void cancelFling()
        {
            mScroller.forceFinished(true);
        }

        void fling(int viewWidth, int viewHeight, int velocityX, int velocityY)
        {
            final RectF rect = getDisplayRect();

            if (rect == null)
                return;

            final int startX = Math.round(-rect.left);
            final int minX, maxX, minY, maxY;

            if (viewWidth < rect.width())
            {
                minX = 0;
                maxX = Math.round(rect.width() - viewWidth);
            }
            else
                minX = maxX = startX;

            final int startY = Math.round(-rect.top);

            if (viewHeight < rect.height())
            {
                minY = 0;
                maxY = Math.round(rect.height() - viewHeight);
            }
            else
                minY = maxY = startY;

            mCurrentX = startX;
            mCurrentY = startY;

            if (startX != maxX || startY != maxY)
                mScroller.fling(startX, startY, velocityX, velocityY, minX, maxX, minY, maxY, 0, 0);
        }

        @Override
        public void run()
        {
            if (mScroller.isFinished())
                return;

            if (mScroller.computeScrollOffset())
            {
                final int newX = mScroller.getCurrX();
                final int newY = mScroller.getCurrY();

                mSuppMatrix.postTranslate(mCurrentX - newX, mCurrentY - newY);
                checkAndDisplayMatrix();

                mCurrentX = newX;
                mCurrentY = newY;

                mImageView.postOnAnimation(this);
            }
        }
    }
}
