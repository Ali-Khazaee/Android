package co.biogram.main.misc;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import co.biogram.main.handler.MiscHandler;

public class LoadingView extends LinearLayout
{
    private int BounceColor = Color.GRAY;
    private int BounceSize;

    private List<View> BounceList;
    private ValueAnimator BounceAnimation;

    public LoadingView(Context context)
    {
        this(context, null, 0);
    }

    public LoadingView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, AttributeSet attrs, int style)
    {
        super(context, attrs, style);

        BounceSize = MiscHandler.DpToPx(6);


        mLoopDuration = 600;
        mLoopStartDelay = 100;
        mJumpDuration =  400;
        mJumpHeight = MiscHandler.DpToPx(12);

        setOrientation(HORIZONTAL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
    }

    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();

        if (BounceAnimation != null)
        {
            BounceAnimation.end();
            BounceAnimation.removeAllUpdateListeners();
        }
    }

    public void Stop()
    {
        if (BounceAnimation != null)
        {
            BounceAnimation.end();
            BounceAnimation.removeAllUpdateListeners();
        }
    }

    public void Start()
    {
        if (BounceAnimation != null)
            return;

        int startOffset = (mLoopDuration - (mJumpDuration + mLoopStartDelay)) / (3 - 1);

        mJumpHalfTime = mJumpDuration / 2;
        mDotsStartTime = new int[3];
        mDotsJumpUpEndTime = new int[3];
        mDotsJumpDownEndTime = new int[3];

        for (int i = 0; i < 3; i++)
        {
            int startTime = mLoopStartDelay + startOffset * i;
            mDotsStartTime[i] = startTime;
            mDotsJumpUpEndTime[i] = startTime + mJumpHalfTime;
            mDotsJumpDownEndTime[i] = startTime + mJumpDuration;
        }

        removeAllViews();
        Context context = getContext();
        BounceList = new ArrayList<>(3);
        LayoutParams BounceParam = new LayoutParams(BounceSize, BounceSize);
        LayoutParams SpaceParam = new LayoutParams(MiscHandler.DpToPx(5), BounceSize);

        for (int I = 0; I < 3; I++)
        {
            GradientDrawable Shape = new GradientDrawable();
            Shape.setShape(GradientDrawable.OVAL);
            Shape.setColor(BounceColor);

            ImageView Bounce = new ImageView(context);
            Bounce.setBackground(Shape);

            addView(Bounce, BounceParam);
            BounceList.add(Bounce);

            if (I < 3 - 1)
                addView(new View(context), SpaceParam);
        }

        BounceAnimation = ValueAnimator.ofInt(0, mLoopDuration);
        BounceAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator)
            {
                int dotsCount = BounceList.size();
                int from = 0;

                int animationValue = (Integer) valueAnimator.getAnimatedValue();

                if (animationValue < mLoopStartDelay) {
                    // Do nothing
                    return;
                }

                for (int i = 0; i < dotsCount; i++) {
                    View dot = BounceList.get(i);

                    int dotStartTime = mDotsStartTime[i];

                    float animationFactor;
                    if (animationValue < dotStartTime) {
                        // No animation is needed for this dot yet
                        animationFactor = 0f;
                    } else if (animationValue < mDotsJumpUpEndTime[i]) {
                        // Animate jump up
                        animationFactor = (float) (animationValue - dotStartTime) / mJumpHalfTime;
                    } else if (animationValue < mDotsJumpDownEndTime[i]) {
                        // Animate jump down
                        animationFactor = 1 - ((float) (animationValue - dotStartTime - mJumpHalfTime) / (mJumpHalfTime));
                    } else {
                        // Dot finished animation for this loop
                        animationFactor = 0f;
                    }

                    float translationY = (-mJumpHeight - from) * animationFactor;
                    dot.setTranslationY(translationY);
                }
            }
        });
        BounceAnimation.setDuration(mLoopDuration);
        BounceAnimation.setRepeatCount(Animation.INFINITE);
        BounceAnimation.start();
    }

    public void SetSize(int Size)
    {
        BounceSize = MiscHandler.DpToPx(Size);
    }

    public void SetColor(int Color)
    {
        BounceColor = Color;
    }






    // Animation time attributes
    private int mLoopDuration;
    private int mLoopStartDelay;

    // Animation behavior attributes
    private int mJumpDuration;
    private int mJumpHeight;

    // Cached Calculations
    private int mJumpHalfTime;
    private int[] mDotsStartTime;
    private int[] mDotsJumpUpEndTime;
    private int[] mDotsJumpDownEndTime;
}
