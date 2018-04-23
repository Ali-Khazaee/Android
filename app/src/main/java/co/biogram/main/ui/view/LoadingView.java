package co.biogram.main.ui.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Keep;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import co.biogram.main.R;
import co.biogram.main.handler.Misc;

public class LoadingView extends LinearLayout
{
    private List<ValueAnimator> AnimatorList = new ArrayList<>();
    private List<Bounce> BounceList = new ArrayList<>();

    private boolean ShouldPlay = false;
    private float BounceScale;
    private int BounceColor;
    private int BounceSize;

    public LoadingView(Context context)
    {
        this(context, null);
    }

    public LoadingView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);

        BounceScale = 2.0f;
        BounceSize = Misc.ToDP(6);
        BounceColor = Misc.Color(R.color.Gray);
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

        if (ShouldPlay)
            Start();
    }

    public void Start()
    {
        LocalStop();

        LayoutParams BounceParam = new LayoutParams(BounceSize, BounceSize);
        LayoutParams SpaceParam = new LayoutParams(Misc.ToDP(5), BounceSize * 3);

        GradientDrawable DrawableBounce = new GradientDrawable();
        DrawableBounce.setShape(GradientDrawable.OVAL);
        DrawableBounce.setColor(BounceColor);

        for (int I = 0; I < 3; I++)
        {
            Bounce bounce = new Bounce(getContext());
            bounce.setBackground(DrawableBounce);

            BounceList.add(bounce);
            addView(bounce, BounceParam);

            if (I < 2)
                addView(new View(getContext()), SpaceParam);
        }

        for (int I = 0; I < 3; I++)
        {
            int BounceSpeed = 300;
            Bounce bounce = BounceList.get(I);

            ValueAnimator ValueAnimatorGrow = ObjectAnimator.ofFloat(bounce, "scale", 1.0f, BounceScale, 1.0f);
            ValueAnimatorGrow.setDuration(BounceSpeed);

            if (I == 2)
            {
                ValueAnimatorGrow.addListener(new AnimatorListenerAdapter()
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

            ValueAnimatorGrow.setStartDelay(I * (int) (0.4 * BounceSpeed));
            ValueAnimatorGrow.start();

            AnimatorList.add(ValueAnimatorGrow);
        }

        ShouldPlay = true;
        setVisibility(VISIBLE);
    }

    public void Stop()
    {
        LocalStop();
        ShouldPlay = false;
        setVisibility(INVISIBLE);
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
        BounceSize = Misc.ToDP(Size);
    }

    public void SetScale(float Scale)
    {
        BounceScale = Scale;
    }

    public void SetColor(int Color)
    {
        BounceColor = Misc.Color(Color);
    }

    private class Bounce extends View
    {
        Bounce(Context context)
        {
            super(context);
        }

        @Keep
        public void setScale(float Scale)
        {
            setScaleX(Scale);
            setScaleY(Scale);
        }
    }
}
