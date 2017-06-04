package co.biogram.main.misc;

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

@SuppressWarnings("unused")
public class LoadingView extends LinearLayout
{
    private boolean IsRunning = false;
    private boolean ShouldShow = false;
    private float BounceScale = 2.0f;

    private int BounceSize;
    private int BounceSpeed = 300;
    private int BounceColor = Color.parseColor("#a9bac4");

    private final List<Bounce> BounceList = new ArrayList<>();
    private final List<Animator> AnimatorList = new ArrayList<>();

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
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();

        Stop();
    }

    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();

        if (ShouldShow && IsRunning)
            Start();
    }

    public void Stop()
    {
        for (Animator animator : AnimatorList)
        {
            animator.removeAllListeners();
            animator.end();
        }

        IsRunning = false;

        AnimatorList.clear();
        BounceList.clear();

        removeAllViews();
    }

    public void Start()
    {
        Stop();

        Context context = getContext();
        LayoutParams BounceParam = new LayoutParams(BounceSize, BounceSize);
        LayoutParams SpaceParam = new LayoutParams(MiscHandler.ToDimension(context, 5), BounceSize * 3);

        GradientDrawable Shape = new GradientDrawable();
        Shape.setShape(GradientDrawable.OVAL);
        Shape.setColor(BounceColor);

        for (int I = 0; I < 3; I++)
        {
            Bounce bounce = new Bounce(context);
            bounce.setBackground(Shape);

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
            View Bounce = BounceList.get(I);
            long StartDelay = I * (int) (0.4 * BounceSpeed);

            ValueAnimator GrowAnimator = ObjectAnimator.ofFloat(Bounce, "scale", 1.0f, BounceScale, 1.0f);
            GrowAnimator.setDuration(BounceSpeed);

            if (I == 2)
            {
                GrowAnimator.addListener(new AnimatorListenerAdapter()
                {
                    @Override
                    public void onAnimationEnd(Animator animation)
                    {
                        for (Animator animator : AnimatorList)
                            animator.start();
                    }
                });
            }

            GrowAnimator.setStartDelay(StartDelay);
            GrowAnimator.start();
            AnimatorList.add(GrowAnimator);
        }

        IsRunning = true;
    }

    public void SetShow(boolean Show)
    {
        ShouldShow = Show;
    }

    public void SetSpeed(int Speed)
    {
        BounceSpeed = Speed;
    }

    public void SetSize(int Size)
    {
        BounceSize = Size;
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
        void setScale(float Scale)
        {
            setScaleX(Scale);
            setScaleY(Scale);
        }
    }
}
