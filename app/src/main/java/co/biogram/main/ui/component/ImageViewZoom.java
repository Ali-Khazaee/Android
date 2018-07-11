package co.biogram.main.ui.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.view.ScaleGestureDetector;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.OverScroller;

@SuppressLint("AppCompatCustomView")
public class ImageViewZoom extends ImageView
{
    private PhotoViewAttach Attach;
    private ScaleType PendingScaleType;

    public ImageViewZoom(Context context)
    {
        this(context, null, 0);
    }

    public ImageViewZoom(Context context, AttributeSet attr)
    {
        this(context, attr, 0);
    }

    public ImageViewZoom(Context context, AttributeSet attr, int defStyle)
    {
        super(context, attr, defStyle);

        Attach = new PhotoViewAttach();

        super.setScaleType(ScaleType.MATRIX);

        if (PendingScaleType != null)
        {
            setScaleType(PendingScaleType);
            PendingScaleType = null;
        }
    }

    @Override
    public ScaleType getScaleType()
    {
        return Attach.getScaleType();
    }

    @Override
    public Matrix getImageMatrix()
    {
        return Attach.getImageMatrix();
    }

    @Override
    public void setOnClickListener(OnClickListener l)
    {
        Attach.setOnClickListener(l);
    }

    @Override
    public void setScaleType(ScaleType scaleType)
    {
        if (Attach == null)
            PendingScaleType = scaleType;
        else
            Attach.setScaleType(scaleType);
    }

    @Override
    public void setImageDrawable(Drawable drawable)
    {
        super.setImageDrawable(drawable);

        if (Attach != null)
            Attach.Update();
    }

    @Override
    public void setImageResource(int resId)
    {
        super.setImageResource(resId);

        if (Attach != null)
            Attach.Update();
    }

    @Override
    public void setImageURI(Uri uri)
    {
        super.setImageURI(uri);

        if (Attach != null)
            Attach.Update();
    }

    @Override
    protected boolean setFrame(int l, int t, int r, int b)
    {
        boolean Change = super.setFrame(l, t, r, b);

        if (Change)
            Attach.Update();

        return Change;
    }

    interface OnGestureListener
    {
        void OnDrag(float X, float Y);
        void OnScale(float Scale, float X, float Y);
        void OnFling(float X, float Y, float VelocityX, float VelocityY);
    }

    // TODO Clean Up Below

    class PhotoViewAttach
    {
        private static final int EDGE_NONE = -1;
        private static final int EDGE_LEFT = 0;
        private static final int EDGE_RIGHT = 1;
        private static final int EDGE_BOTH = 2;
        private Interpolator mInterpolator = new AccelerateDecelerateInterpolator();
        private int mZoomDuration = 200;
        private float mMinScale = 1.0f;
        private float mMidScale = 1.75f;
        private float mMaxScale = 3.0f;
        private boolean mAllowParentInterceptOnEdge = true;
        private boolean mBlockParentIntercept = false;
        private GestureDetector mGestureDetector;
        private CustomGestureDetector mScaleDragDetector;
        private final Matrix mBaseMatrix = new Matrix();
        private final Matrix mDrawMatrix = new Matrix();
        private final Matrix mSuppMatrix = new Matrix();
        private final RectF mDisplayRect = new RectF();
        private final float[] mMatrixValues = new float[ 9 ];
        private View.OnClickListener mOnClickListener;
        private FlingRunnable mCurrentFlingRunnable;
        private int mScrollEdge = EDGE_BOTH;
        private float mBaseRotation;
        private boolean mZoomEnabled = true;
        private ScaleType mScaleType = ScaleType.FIT_CENTER;

        private OnGestureListener onGestureListener = new OnGestureListener()
        {
            @Override
            public void OnDrag(float X, float Y)
            {
                if (mScaleDragDetector.isScaling())
                    return;

                mSuppMatrix.postTranslate(X, Y);
                checkAndDisplayMatrix();

                ViewParent parent = getParent();

                if (parent == null)
                    return;

                if (mAllowParentInterceptOnEdge && !mScaleDragDetector.isScaling() && !mBlockParentIntercept)
                {
                    if (mScrollEdge == EDGE_BOTH || (mScrollEdge == EDGE_LEFT && X >= 1f) || (mScrollEdge == EDGE_RIGHT && X <= -1f))
                    {
                        parent.requestDisallowInterceptTouchEvent(false);
                    }
                }
                else
                    parent.requestDisallowInterceptTouchEvent(true);
            }

            @Override
            public void OnFling(float X, float Y, float VelocityX, float VelocityY)
            {
                mCurrentFlingRunnable = new FlingRunnable(getContext());
                mCurrentFlingRunnable.fling(getImageViewWidth(), getImageViewHeight(), (int) VelocityX, (int) VelocityY);

                post(mCurrentFlingRunnable);
            }

            @Override
            public void OnScale(float Scale, float X, float Y)
            {
                if ((GetScale() < mMaxScale || Scale < 1f) && (GetScale() > mMinScale || Scale > 1f))
                {

                    mSuppMatrix.postScale(Scale, Scale, X, Y);
                    checkAndDisplayMatrix();
                }
            }
        };

        @SuppressLint("ClickableViewAccessibility")
        PhotoViewAttach()
        {
            setOnTouchListener(new OnTouchListener()
            {
                @Override
                public boolean onTouch(View v, MotionEvent ev)
                {
                    boolean Handle = false;

                    if (mZoomEnabled && getDrawable() != null)
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
                                if (GetScale() < mMinScale)
                                {
                                    RectF rect = GetDisplayRect();

                                    if (rect != null)
                                    {
                                        v.post(new AnimatedZoomRunnable(GetScale(), mMinScale, rect.centerX(), rect.centerY()));
                                        Handle = true;
                                    }
                                }
                                else if (GetScale() > mMaxScale)
                                {
                                    RectF rect = GetDisplayRect();

                                    if (rect != null)
                                    {
                                        v.post(new AnimatedZoomRunnable(GetScale(), mMaxScale, rect.centerX(), rect.centerY()));
                                        Handle = true;
                                    }
                                }
                                break;
                        }

                        if (mScaleDragDetector != null)
                        {
                            boolean wasScaling = mScaleDragDetector.isScaling();
                            boolean wasDragging = mScaleDragDetector.isDragging();

                            Handle = mScaleDragDetector.onTouchEvent(ev);

                            boolean didntScale = !wasScaling && !mScaleDragDetector.isScaling();
                            boolean didntDrag = !wasDragging && !mScaleDragDetector.isDragging();

                            mBlockParentIntercept = didntScale && didntDrag;
                        }

                        if (mGestureDetector != null && mGestureDetector.onTouchEvent(ev))
                            Handle = true;
                    }

                    return Handle;
                }
            });

            addOnLayoutChangeListener(new OnLayoutChangeListener()
            {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom)
                {
                    if (left != oldLeft || top != oldTop || right != oldRight || bottom != oldBottom)
                        updateBaseMatrix(getDrawable());
                }
            });

            mBaseRotation = 0.0f;
            mScaleDragDetector = new CustomGestureDetector(getContext(), onGestureListener);

            mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener()
            {
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
                        mOnClickListener.onClick(ImageViewZoom.this);

                    RectF DisplayRect = GetDisplayRect();

                    return DisplayRect != null && DisplayRect.contains(e.getX(), e.getY());
                }

                @Override
                public boolean onDoubleTap(MotionEvent ev)
                {
                    try
                    {
                        float Scale = GetScale();
                        float X = ev.getX();
                        float Y = ev.getY();

                        if (Scale < getMediumScale())
                            SetScale(getMediumScale(), X, Y);
                        else if (Scale >= getMediumScale() && Scale < getMaximumScale())
                            SetScale(getMaximumScale(), X, Y);
                        else
                            SetScale(getMinimumScale(), X, Y);
                    }
                    catch (Exception e)
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

        private RectF GetDisplayRect()
        {
            checkMatrixBounds();
            return GetDisplayRect(getDrawMatrix());
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

        private float GetScale()
        {
            return (float) Math.sqrt((float) Math.pow(getValue(mSuppMatrix, Matrix.MSCALE_X), 2) + (float) Math.pow(getValue(mSuppMatrix, Matrix.MSKEW_Y), 2));
        }

        private ScaleType getScaleType()
        {
            return mScaleType;
        }

        public void setOnClickListener(View.OnClickListener listener)
        {
            mOnClickListener = listener;
        }

        private void SetScale(float scale, float focalX, float focalY)
        {
            // Check to see if the scale is within bounds
            if (scale < mMinScale || scale > mMaxScale)
            {
                throw new IllegalArgumentException("Scale must be within the range of minScale and maxScale");
            }

            post(new AnimatedZoomRunnable(GetScale(), scale, focalX, focalY));
        }

        private void setScaleType(ScaleType scaleType)
        {
            if (isSupportedScaleType(scaleType) && scaleType != mScaleType)
            {
                mScaleType = scaleType;
                Update();
            }
        }

        private void Update()
        {
            if (mZoomEnabled)
            {
                // Update the base matrix using the current drawable
                updateBaseMatrix(getDrawable());
            }
            else
            {
                // Reset the Matrix...
                resetMatrix();
            }
        }

        private Matrix getDrawMatrix()
        {
            mDrawMatrix.set(mBaseMatrix);
            mDrawMatrix.postConcat(mSuppMatrix);
            return mDrawMatrix;
        }

        private Matrix getImageMatrix()
        {
            return mDrawMatrix;
        }

        private float getValue(Matrix matrix, int whichValue)
        {
            matrix.getValues(mMatrixValues);
            return mMatrixValues[ whichValue ];
        }

        /**
         * Resets the Matrix back to FIT_CENTER, and then displays its contents
         */
        private void resetMatrix()
        {
            mSuppMatrix.reset();
            setRotationBy(mBaseRotation);
            setImageViewMatrix(getDrawMatrix());
            checkMatrixBounds();
        }

        private void setImageViewMatrix(Matrix matrix)
        {
            setImageMatrix(matrix);
        }

        private void checkAndDisplayMatrix()
        {
            if (checkMatrixBounds())
            {
                setImageViewMatrix(getDrawMatrix());
            }
        }

        private RectF GetDisplayRect(Matrix matrix)
        {
            Drawable d = getDrawable();
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
            {
                return;
            }

            final float viewWidth = getImageViewWidth();
            final float viewHeight = getImageViewHeight();
            final int DrawableW = drawable.getIntrinsicWidth();
            final int DrawableH = drawable.getIntrinsicHeight();

            mBaseMatrix.reset();

            final float widthScale = viewWidth / DrawableW;
            final float heightScale = viewHeight / DrawableH;

            if (mScaleType == ScaleType.CENTER)
            {
                mBaseMatrix.postTranslate((viewWidth - DrawableW) / 2F, (viewHeight - DrawableH) / 2F);

            }
            else if (mScaleType == ScaleType.CENTER_CROP)
            {
                float scale = Math.max(widthScale, heightScale);
                mBaseMatrix.postScale(scale, scale);
                mBaseMatrix.postTranslate((viewWidth - DrawableW * scale) / 2F, (viewHeight - DrawableH * scale) / 2F);

            }
            else if (mScaleType == ScaleType.CENTER_INSIDE)
            {
                float scale = Math.min(1.0f, Math.min(widthScale, heightScale));
                mBaseMatrix.postScale(scale, scale);
                mBaseMatrix.postTranslate((viewWidth - DrawableW * scale) / 2F, (viewHeight - DrawableH * scale) / 2F);

            }
            else
            {
                RectF mTempSrc = new RectF(0, 0, DrawableW, DrawableH);
                RectF mTempDst = new RectF(0, 0, viewWidth, viewHeight);

                if ((int) mBaseRotation % 180 != 0)
                {
                    mTempSrc = new RectF(0, 0, DrawableH, DrawableW);
                }

                switch (mScaleType)
                {
                    case FIT_CENTER:
                        mBaseMatrix.setRectToRect(mTempSrc, mTempDst, Matrix.ScaleToFit.CENTER);
                        break;

                    case FIT_START:
                        mBaseMatrix.setRectToRect(mTempSrc, mTempDst, Matrix.ScaleToFit.START);
                        break;

                    case FIT_END:
                        mBaseMatrix.setRectToRect(mTempSrc, mTempDst, Matrix.ScaleToFit.END);
                        break;

                    case FIT_XY:
                        mBaseMatrix.setRectToRect(mTempSrc, mTempDst, Matrix.ScaleToFit.FILL);
                        break;

                    default:
                        break;
                }
            }

            resetMatrix();
        }

        private boolean checkMatrixBounds()
        {

            final RectF rect = GetDisplayRect(getDrawMatrix());
            if (rect == null)
            {
                return false;
            }

            final float height = rect.height(), width = rect.width();
            float deltaX = 0, deltaY = 0;

            final int viewHeight = getImageViewHeight();
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
            {
                deltaY = -rect.top;
            }
            else if (rect.bottom < viewHeight)
            {
                deltaY = viewHeight - rect.bottom;
            }

            final int viewWidth = getImageViewWidth();
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
            {
                mScrollEdge = EDGE_NONE;
            }

            mSuppMatrix.postTranslate(deltaX, deltaY);
            return true;
        }

        private int getImageViewWidth()
        {
            return getWidth() - getPaddingLeft() - getPaddingRight();
        }

        private int getImageViewHeight()
        {
            return getHeight() - getPaddingTop() - getPaddingBottom();
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
                float deltaScale = scale / GetScale();

                onGestureListener.OnScale(deltaScale, mFocalX, mFocalY);

                if (t < 1f)
                    postOnAnimation(this);
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

            private FlingRunnable(Context context)
            {
                mScroller = new OverScroller(context);
            }

            private void cancelFling()
            {
                mScroller.forceFinished(true);
            }

            public void fling(int viewWidth, int viewHeight, int velocityX, int velocityY)
            {
                final RectF rect = GetDisplayRect();
                if (rect == null)
                {
                    return;
                }

                final int startX = Math.round(-rect.left);
                final int minX, maxX, minY, maxY;

                if (viewWidth < rect.width())
                {
                    minX = 0;
                    maxX = Math.round(rect.width() - viewWidth);
                }
                else
                {
                    minX = maxX = startX;
                }

                final int startY = Math.round(-rect.top);
                if (viewHeight < rect.height())
                {
                    minY = 0;
                    maxY = Math.round(rect.height() - viewHeight);
                }
                else
                {
                    minY = maxY = startY;
                }

                mCurrentX = startX;
                mCurrentY = startY;

                // If we actually can move, fling the scroller
                if (startX != maxX || startY != maxY)
                {
                    mScroller.fling(startX, startY, velocityX, velocityY, minX, maxX, minY, maxY, 0, 0);
                }
            }

            @Override
            public void run()
            {
                if (mScroller.isFinished())
                {
                    return; // remaining post that should not be handled
                }

                if (mScroller.computeScrollOffset())
                {

                    final int newX = mScroller.getCurrX();
                    final int newY = mScroller.getCurrY();

                    mSuppMatrix.postTranslate(mCurrentX - newX, mCurrentY - newY);
                    checkAndDisplayMatrix();

                    mCurrentX = newX;
                    mCurrentY = newY;

                    postOnAnimation(this);
                }
            }
        }
    }

    boolean isSupportedScaleType(final ImageView.ScaleType scaleType)
    {
        if (scaleType == null)
        {
            return false;
        }
        switch (scaleType)
        {
            case MATRIX:
                throw new IllegalStateException("Matrix scale type is not supported");
        }
        return true;
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
        private OnGestureListener mListener;

        CustomGestureDetector(Context context, OnGestureListener listener)
        {
            final ViewConfiguration configuration = ViewConfiguration.get(context);
            mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
            mTouchSlop = configuration.getScaledTouchSlop();

            mListener = listener;
            ScaleGestureDetector.OnScaleGestureListener mScaleListener = new ScaleGestureDetector.OnScaleGestureListener()
            {

                @Override
                public boolean onScale(ScaleGestureDetector detector)
                {
                    float scaleFactor = detector.getScaleFactor();

                    if (Float.isNaN(scaleFactor) || Float.isInfinite(scaleFactor))
                        return false;

                    mListener.OnScale(scaleFactor, detector.getFocusX(), detector.getFocusY());
                    return true;
                }

                @Override
                public boolean onScaleBegin(ScaleGestureDetector detector)
                {
                    return true;
                }

                @Override
                public void onScaleEnd(ScaleGestureDetector detector)
                {
                    // NO-OP
                }
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

        public boolean isScaling()
        {
            return mDetector.isInProgress();
        }

        public boolean isDragging()
        {
            return mIsDragging;
        }

        public boolean onTouchEvent(MotionEvent ev)
        {
            try
            {
                mDetector.onTouchEvent(ev);
                return processTouchEvent(ev);
            }
            catch (IllegalArgumentException e)
            {
                // Fix for support lib bug, happening when onDestroy is called
                return true;
            }
        }

        private boolean processTouchEvent(MotionEvent ev)
        {
            final int action = ev.getAction();
            switch (action & MotionEvent.ACTION_MASK)
            {
                case MotionEvent.ACTION_DOWN:
                    mActivePointerId = ev.getPointerId(0);

                    mVelocityTracker = VelocityTracker.obtain();
                    if (null != mVelocityTracker)
                    {
                        mVelocityTracker.addMovement(ev);
                    }

                    mLastTouchX = getActiveX(ev);
                    mLastTouchY = getActiveY(ev);
                    mIsDragging = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                    final float x = getActiveX(ev);
                    final float y = getActiveY(ev);
                    final float dx = x - mLastTouchX, dy = y - mLastTouchY;

                    if (!mIsDragging)
                    {
                        // Use Pythagoras to see if drag length is larger than
                        // touch slop
                        mIsDragging = Math.sqrt((dx * dx) + (dy * dy)) >= mTouchSlop;
                    }

                    if (mIsDragging)
                    {
                        mListener.OnDrag(dx, dy);
                        mLastTouchX = x;
                        mLastTouchY = y;

                        if (null != mVelocityTracker)
                        {
                            mVelocityTracker.addMovement(ev);
                        }
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                    mActivePointerId = INVALID_POINTER_ID;
                    // Recycle Velocity Tracker
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

                            // Compute velocity within the last 1000ms
                            mVelocityTracker.addMovement(ev);
                            mVelocityTracker.computeCurrentVelocity(1000);

                            final float vX = mVelocityTracker.getXVelocity(), vY = mVelocityTracker.getYVelocity();

                            // If the velocity is greater than minVelocity, Result
                            // listener
                            if (Math.max(Math.abs(vX), Math.abs(vY)) >= mMinimumVelocity)
                            {
                                mListener.OnFling(mLastTouchX, mLastTouchY, -vX, -vY);
                            }
                        }
                    }

                    // Recycle Velocity Tracker
                    if (null != mVelocityTracker)
                    {
                        mVelocityTracker.recycle();
                        mVelocityTracker = null;
                    }
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                    final int pointerId = ev.getPointerId(pointerIndex);
                    if (pointerId == mActivePointerId)
                    {
                        // This was our active pointer going up. Choose a new
                        // active pointer and adjust accordingly.
                        final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
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
}
