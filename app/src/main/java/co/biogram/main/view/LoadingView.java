package co.biogram.main.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Keep;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import co.biogram.main.R;
import co.biogram.main.handler.MiscHandler;

public class LoadingView extends LinearLayout
{
    private final List<ValueAnimator> AnimatorList = new ArrayList<>();
    private final List<Bounce> BounceList = new ArrayList<>();

    private boolean ShouldPlay = false;
    private float BounceScale;
    private int BounceColor;
    private int BounceSize;

    public LoadingView(Context context)
    {
        super(context);

        BounceScale = 2.0f;
        BounceSize = MiscHandler.ToDimension(context, 6);
        BounceColor = ContextCompat.getColor(context, R.color.BlueGray2);

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

        if (ShouldPlay)
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

            BounceList.add(bounce);
            addView(bounce, BounceParam);

            if (I < 2)
                addView(new View(context), SpaceParam);
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
    }

    public void Stop()
    {
        LocalStop();
        ShouldPlay = false;
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
