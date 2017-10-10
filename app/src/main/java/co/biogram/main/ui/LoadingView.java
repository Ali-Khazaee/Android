package co.biogram.main.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Keep;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import co.biogram.main.handler.MiscHandler;

public class LoadingView extends LinearLayout
{
    private final List<ValueAnimator> AnimatorList = new ArrayList<>();
    private final List<Bounce> BounceList = new ArrayList<>();

    private int BounceColor = Color.parseColor("#a9bac4");
    private float BounceScale = 2.0f;
    private int BounceSize;

    private boolean ShouldStart = false;

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

        BounceSize = MiscHandler.ToDimension(context, 6);

        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);
    }

    @Override
    public void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        LocalStop();
    }

    @Override
    public void onAttachedToWindow()
    {
        super.onAttachedToWindow();

        if (ShouldStart)
            Start();
    }

    public void Start()
    {
        LocalStop();

        Context context = getContext();
        LayoutParams BounceParam = new LayoutParams(BounceSize, BounceSize);
        LayoutParams SpaceParam = new LayoutParams(MiscHandler.ToDimension(context, 5), BounceSize * 3);

        GradientDrawable GradientDrawableBounce = new GradientDrawable();
        GradientDrawableBounce.setShape(GradientDrawable.OVAL);
        GradientDrawableBounce.setColor(BounceColor);

        for (int I = 0; I < 3; I++)
        {
            Bounce bounce = new Bounce(context);
            bounce.setBackground(GradientDrawableBounce);

            addView(bounce, BounceParam);
            BounceList.add(bounce);

            if (I < 2)
            {
                View view = new View(context);
                addView(view, SpaceParam);
            }
        }

        for (int I = 0; I < 3; I++)
        {
            int BounceSpeed = 300;
            Bounce bounce = BounceList.get(I);
            long StartDelay = I * (int) (0.4 * BounceSpeed);

            ValueAnimator BounceGrowAnimator = ObjectAnimator.ofFloat(bounce, "scale", 1.0f, BounceScale, 1.0f);
            BounceGrowAnimator.setDuration(BounceSpeed);

            if (I == 2)
            {
                BounceGrowAnimator.addListener(new AnimatorListenerAdapter()
                {
                    @Override
                    public void onAnimationEnd(Animator animation)
                    {
                        for (ValueAnimator animator : AnimatorList)
                            if (animator != null)
                                animator.start();
                    }
                });
            }

            BounceGrowAnimator.setStartDelay(StartDelay);
            BounceGrowAnimator.start();
            AnimatorList.add(BounceGrowAnimator);
        }

        ShouldStart = true;
    }

    public void Stop()
    {
        LocalStop();
        ShouldStart = false;
    }

    private void LocalStop()
    {
        for (Bounce bounce : BounceList)
        {
            if (bounce == null)
                continue;

            bounce.clearAnimation();
        }

        for (ValueAnimator animator : AnimatorList)
        {
            if (animator == null)
                continue;

            animator.end();
            animator.cancel();
            animator.removeAllUpdateListeners();
            animator.removeAllListeners();
        }

        AnimatorList.clear();
        BounceList.clear();

        removeAllViews();
    }

    public void SetSize(int Size)
    {
        BounceSize = MiscHandler.ToDimension(getContext(), Size);
    }

    public void SetScale(float Scale)
    {
        BounceScale = Scale;
    }

    public void SetColor(int Color)
    {
        BounceColor = ContextCompat.getColor(getContext(), Color);
    }

    private class Bounce extends View
    {
        Bounce(Context context)
        {
            super(context);
        }

        @Keep
        @SuppressWarnings("unused")
        void setScale(float Scale)
        {
            setScaleX(Scale);
            setScaleY(Scale);
        }
    }
}
