package co.biogram.main.misc;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

public class TouchImageView extends ImageView
{
    private Matrix matrix;
    private int MotionMode = 0;

    private float MidScale = 1f;

    private int ViewWidth, ViewHeight;
    private float OrigWidth, OrigHeight;

    private final PointF End = new PointF();
    private final PointF Start = new PointF();

    private int OldMeasuredHeight;

    private float[] MultiTrans;
    private GestureDetector DoubleTabDetector;
    private ScaleGestureDetector ScaleDetector;

    public TouchImageView(Context context)
    {
        super(context);
        SharedConstructing(context);
    }

    public TouchImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        SharedConstructing(context);
    }

    @Override
    protected void onMeasure(int WidthMeasureSpec, int HeightMeasureSpec)
    {
        super.onMeasure(WidthMeasureSpec, HeightMeasureSpec);

        ViewWidth = MeasureSpec.getSize(WidthMeasureSpec);
        ViewHeight = MeasureSpec.getSize(HeightMeasureSpec);

        if (OldMeasuredHeight == ViewWidth && OldMeasuredHeight == ViewHeight || ViewWidth == 0 || ViewHeight == 0)
            return;

        OldMeasuredHeight = ViewHeight;

        if (MidScale == 1)
        {
            Drawable drawable = getDrawable();

            if (drawable == null || drawable.getIntrinsicWidth() == 0 || drawable.getIntrinsicHeight() == 0)
                return;

            int Width = drawable.getIntrinsicWidth();
            int Height = drawable.getIntrinsicHeight();

            float Scale = Math.min((float) ViewWidth / (float) Width, (float) ViewHeight / (float) Height);
            float RedundantYSpace = ((float) ViewHeight - (Scale * (float) Height)) / 2;
            float RedundantXSpace = ((float) ViewWidth - (Scale * (float) Width)) / 2;

            matrix.setScale(Scale, Scale);
            matrix.postTranslate(RedundantXSpace, RedundantYSpace);

            OrigWidth = ViewWidth - 2 * RedundantXSpace;
            OrigHeight = ViewHeight - 2 * RedundantYSpace;

            setImageMatrix(matrix);
        }

        FixTrans();
    }

    private void SharedConstructing(Context context)
    {
        super.setClickable(true);

        matrix = new Matrix();
        setImageMatrix(matrix);
        MultiTrans = new float[9];
        setScaleType(ScaleType.MATRIX);
        ScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        DoubleTabDetector = new GestureDetector(context, new GestureListener());

        setOnTouchListener(new OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent e)
            {
                ScaleDetector.onTouchEvent(e);
                DoubleTabDetector.onTouchEvent(e);
                PointF CurrentPoint = new PointF(e.getX(), e.getY());

                switch (e.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        End.set(CurrentPoint);
                        Start.set(End);
                        MotionMode = 1;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (MotionMode == 1)
                        {
                            float FixTransX = OrigWidth * MidScale <= ViewWidth ? 0 : CurrentPoint.x - End.x;
                            float FixTransY = OrigHeight * MidScale <= ViewHeight ? 0 : CurrentPoint.y - End.y;

                            matrix.postTranslate(FixTransX, FixTransY);
                            End.set(CurrentPoint.x, CurrentPoint.y);
                            FixTrans();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        MotionMode = 0;
                        int XDiff = (int) Math.abs(CurrentPoint.x - Start.x);
                        int YDiff = (int) Math.abs(CurrentPoint.y - Start.y);

                        if (XDiff < 3 && YDiff < 3)
                            performClick();
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        MotionMode = 0;
                        break;
                }

                setImageMatrix(matrix);
                invalidate();
                return true;
            }
        });
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener
    {
        @Override
        public boolean onDown(MotionEvent e)
        {
            return true;
        }

        /*@Override
        public boolean onDoubleTap(MotionEvent e)
        {
            // TODO Zoom In Zoom Out

            return super.onDoubleTap(e);
        }*/
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener
    {
        @Override
        public boolean onScale(ScaleGestureDetector detector)
        {
            float ScaleFactor = detector.getScaleFactor();
            float OrigScale = MidScale;

            MidScale *= ScaleFactor;

            if (MidScale > MidScale)
            {
                MidScale = 3f;
                ScaleFactor = 3f / OrigScale;
            }
            else if (MidScale < 1f)
            {
                MidScale = 1f;
                ScaleFactor = 1f / OrigScale;
            }

            if (OrigWidth * MidScale <= ViewWidth || OrigHeight * MidScale <= ViewHeight)
                matrix.postScale(ScaleFactor, ScaleFactor, ViewWidth / 2, ViewHeight / 2);
            else
                matrix.postScale(ScaleFactor, ScaleFactor, detector.getFocusX(), detector.getFocusY());

            FixTrans();
            return true;
        }
    }

    private void FixTrans()
    {
        matrix.getValues(MultiTrans);

        float FixTransX = CalculateTrans(MultiTrans[Matrix.MTRANS_X], ViewWidth, OrigWidth * MidScale);
        float FixTransY = CalculateTrans(MultiTrans[Matrix.MTRANS_Y], ViewHeight, OrigHeight * MidScale);

        if (FixTransX != 0 || FixTransY != 0)
            matrix.postTranslate(FixTransX, FixTransY);
    }

    private float CalculateTrans(float Trans, float ViewSize, float ContentSize)
    {
        float MinTrans, MaxTrans;

        if (ContentSize <= ViewSize)
        {
            MinTrans = 0;
            MaxTrans = ViewSize - ContentSize;
        }
        else
        {
            MinTrans = ViewSize - ContentSize;
            MaxTrans = 0;
        }

        if (Trans < MinTrans)
            return -Trans + MinTrans;

        if (Trans > MaxTrans)
            return -Trans + MaxTrans;

        return 0;
    }
}
