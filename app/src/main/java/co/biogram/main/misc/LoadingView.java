package co.biogram.main.misc;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;

import co.biogram.main.App;
import co.biogram.main.R;
import co.biogram.main.handler.MiscHandler;

import java.util.ArrayList;
import java.util.List;

public class LoadingView extends View
{
    private int Color;
    private float Radius;
    private float MaxRadius;
    private float Scale;
    private float Spacing;
    private boolean ShouldAnimate;
    private boolean IsRunning = false;
    private boolean IsBuilded = false;
    private int AnimationDuration;
    private final List<Animator> Animations = new ArrayList<>();

    private int WidthBetweenDotCenters;
    private final ArrayList<DotDrawable> Drawables = new ArrayList<>();

    public LoadingView(Context context)
    {
        this(context, null, 0);
    }

    public LoadingView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        if (isInEditMode())
            return;

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.LoadingView);
        AnimationDuration = a.getInt(R.styleable.LoadingView_LB_Duration, 280);
        Radius = a.getDimension(R.styleable.LoadingView_LB_Radius, MiscHandler.DpToPx(3));
        Color = a.getColor(R.styleable.LoadingView_LB_Color, 0xff000000);
        Scale = a.getFloat(R.styleable.LoadingView_LB_Scale, 2f);
        Spacing = a.getDimension(R.styleable.LoadingView_LB_Spacing, MiscHandler.DpToPx(8));
        a.recycle();
    }

    private void Build()
    {
        if (IsBuilded)
            return;

        IsBuilded = true;
        ShouldAnimate = false;

        CalculateMaxRadius();
        CalculateWidthBetweenDotCenters();

        Drawables.clear();
        Animations.clear();

        for (int I = 1; I <= 3; I++)
        {
            DotDrawable Dot = new DotDrawable(Color, Radius, MaxRadius);
            Dot.setCallback(this);
            Drawables.add(Dot);

            long StartDelay = (I - 1) * (int) (0.35 * AnimationDuration);

            ValueAnimator GrowAnimator = ObjectAnimator.ofFloat(Dot, "radius", Radius, MaxRadius, Radius);
            GrowAnimator.setDuration(AnimationDuration);

            if (I == 3)
            {
                GrowAnimator.addListener(new AnimatorListenerAdapter()
                {
                    @Override
                    public void onAnimationEnd(Animator animation)
                    {
                        if (ShouldAnimate())
                            StartAnimation();
                    }
                });
            }

            GrowAnimator.setStartDelay(StartDelay);
            Animations.add(GrowAnimator);
        }

        UpdateDots();

        setVisibility(View.GONE);
        setVisibility(View.VISIBLE);
    }

    public void SetColor(int color)
    {
        Color = ContextCompat.getColor(App.GetContext(), color);
    }

    private void UpdateDots()
    {
        if (Radius <= 0)
            Radius = getHeight() / 2 / Scale;

        int Left = (int) (MaxRadius - Radius);
        int Right = (int) (Left + Radius * 2) + 2;
        int Top = 0;
        int Bottom = (int) (MaxRadius * 2) + 2;

        for (int i = 0; i < Drawables.size(); i++)
        {
            DotDrawable Dot = Drawables.get(i);
            Dot.setRadius(Radius);
            Dot.setBounds(Left, Top, Right, Bottom);

            ValueAnimator growAnimator = (ValueAnimator) Animations.get(i);
            growAnimator.setFloatValues(Radius, Radius * Scale, Radius);

            Left += WidthBetweenDotCenters;
            Right += WidthBetweenDotCenters;
        }
    }

    private void StartAnimation()
    {
        ShouldAnimate = true;

        for (Animator anim : Animations)
            anim.start();
    }

    public void Stop()
    {
        IsRunning = false;
        ShouldAnimate = false;
        setVisibility(View.GONE);

        for (Animator anim : Animations)
            anim.cancel();
    }

    public void Start()
    {
        Build();

        if (IsRunning)
            return;

        IsRunning = true;
        setVisibility(View.VISIBLE);
        StartAnimation();
    }

    private float ComputeMaxHeight()
    {
        return MaxRadius * 2;
    }

    private float ComputeMaxWidth()
    {
        return ComputeWidth() + ((MaxRadius - Radius) * 2);
    }

    private float ComputeWidth()
    {
        return (((Radius * 2) + Spacing) * Drawables.size()) - Spacing;
    }

    private void CalculateMaxRadius()
    {
        MaxRadius = Radius * Scale;
    }

    private void CalculateWidthBetweenDotCenters()
    {
                WidthBetweenDotCenters = (int) (Radius * 2) + (int) Spacing;
    }

    private boolean ShouldAnimate()
    {
        return ShouldAnimate;
    }

    @Override
    protected void onSizeChanged(int w, int h, int ow, int oh)
    {
        super.onSizeChanged(w, h, ow, oh);

        if (ComputeMaxHeight() != h || w != ComputeMaxWidth())
            UpdateDots();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        if (ShouldAnimate())
            for (DotDrawable Dot : Drawables)
                Dot.draw(canvas);
    }

    @Override
    protected boolean verifyDrawable(@NonNull Drawable who)
    {
        if (ShouldAnimate())
            return Drawables.contains(who);

        return super.verifyDrawable(who);
    }

    @Override
    protected void onMeasure(int w, int h)
    {
        setMeasuredDimension((int) ComputeMaxWidth(), (int) ComputeMaxHeight());
    }

    class DotDrawable extends Drawable
    {
        private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private float Radius;
        private float MaxRadius;
        private Rect DirtyBounds = new Rect(0, 0, 0, 0);

        DotDrawable(int Color, float radius, float maxRadius)
        {
            paint.setColor(Color);
            paint.setStyle(Paint.Style.FILL);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setStrokeJoin(Paint.Join.ROUND);

            this.Radius = radius;
            this.MaxRadius = maxRadius;

            DirtyBounds.bottom = (int) (maxRadius * 2) + 2;
            DirtyBounds.right = (int) (maxRadius * 2) + 2;
        }

        void setRadius(float radius)
        {
            this.Radius = radius;
            invalidateSelf();
        }

        @Override
        public void draw(@NonNull Canvas canvas)
        {
            Rect bounds = getBounds();
            canvas.drawCircle(bounds.centerX(), bounds.centerY(), Radius - 1, paint);
        }

        @Override
        public void setAlpha(int Alpha)
        {
            if (Alpha != paint.getAlpha())
            {
                paint.setAlpha(Alpha);
                invalidateSelf();
            }
        }

        @Override
        public void setColorFilter(ColorFilter colorFilter)
        {
            paint.setColorFilter(colorFilter);
            invalidateSelf();
        }

        @Override
        public int getOpacity()
        {
            return PixelFormat.TRANSLUCENT;
        }

        @Override
        public int getIntrinsicWidth()
        {
            return (int) (MaxRadius * 2) + 2;
        }

        @Override
        public int getIntrinsicHeight()
        {
            return (int) (MaxRadius * 2) + 2;
        }

        @Override
        @NonNull
        public Rect getDirtyBounds()
        {
            return DirtyBounds;
        }

        @Override
        protected void onBoundsChange(Rect bounds)
        {
            super.onBoundsChange(bounds);
            DirtyBounds.offsetTo(bounds.left, bounds.top);
        }
    }
}
