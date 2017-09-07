package co.biogram.main.misc;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.widget.NestedScrollView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class ScrollViewSticky extends NestedScrollView
{
    private final ArrayList<View> StickyViews = new ArrayList<>();
    private View CurrentlyStickingView;
    private float StickyViewTopOffset;
    private int StickyViewLeftOffset;
    private boolean redirectTouchesToStickyView;
    private boolean clippingToPadding;
    private boolean clipToPaddingHasBeenSet;
    private boolean hasNotDoneActionDown = true;

    private final Runnable invalidateRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            if (CurrentlyStickingView != null)
            {
                int l = getLeftForViewRelativeOnlyChild(CurrentlyStickingView);
                int t  = getBottomForViewRelativeOnlyChild(CurrentlyStickingView);
                int r = getRightForViewRelativeOnlyChild(CurrentlyStickingView);
                int b = (int) (getScrollY() + (CurrentlyStickingView.getHeight() + StickyViewTopOffset));
                invalidate(l,t,r,b);
            }

            postDelayed(this, 16);
        }
    };

    public ScrollViewSticky(Context context)
    {
        super(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        super.onLayout(changed, l, t, r, b);

        if (!clipToPaddingHasBeenSet)
            clippingToPadding = true;

        notifyHierarchyChanged();
    }

    @Override
    public void setClipToPadding(boolean clipToPadding)
    {
        super.setClipToPadding(clipToPadding);
        clippingToPadding  = clipToPadding;
        clipToPaddingHasBeenSet = true;
    }

    @Override
    public void addView(View child)
    {
        super.addView(child);
        FindStickyViews(child);
    }

    @Override
    public void addView(View child, int index)
    {
        super.addView(child, index);
        FindStickyViews(child);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params)
    {
        super.addView(child, index, params);
        FindStickyViews(child);
    }

    @Override
    public void addView(View child, int width, int height)
    {
        super.addView(child, width, height);
        FindStickyViews(child);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params)
    {
        super.addView(child, params);
        FindStickyViews(child);
    }

    @Override
    protected void dispatchDraw(Canvas canvas)
    {
        super.dispatchDraw(canvas);

        if (CurrentlyStickingView != null)
        {
            canvas.save();
            canvas.translate(getPaddingLeft() + StickyViewLeftOffset, getScrollY() + StickyViewTopOffset + (clippingToPadding ? getPaddingTop() : 0));
            canvas.clipRect(0, (clippingToPadding ? -StickyViewTopOffset : 0), getWidth() - StickyViewLeftOffset, CurrentlyStickingView.getHeight() + 1);
            canvas.clipRect(0, (clippingToPadding ? -StickyViewTopOffset : 0), getWidth(), CurrentlyStickingView.getHeight());
            CurrentlyStickingView.draw(canvas);
            canvas.restore();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev)
    {
        if (ev.getAction() == MotionEvent.ACTION_DOWN)
            redirectTouchesToStickyView = true;

        if (redirectTouchesToStickyView)
        {
            redirectTouchesToStickyView = CurrentlyStickingView != null;

            if (redirectTouchesToStickyView)
                redirectTouchesToStickyView = ev.getY() <= (CurrentlyStickingView.getHeight() + StickyViewTopOffset) && ev.getX() >= getLeftForViewRelativeOnlyChild(CurrentlyStickingView) && ev.getX() <= getRightForViewRelativeOnlyChild(CurrentlyStickingView);
        }
        else if (CurrentlyStickingView == null)
            redirectTouchesToStickyView = false;

        if (redirectTouchesToStickyView)
            ev.offsetLocation(0, -1 * ((getScrollY() + StickyViewTopOffset) - getTopForViewRelativeOnlyChild(CurrentlyStickingView)));

        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        if (redirectTouchesToStickyView)
            ev.offsetLocation(0, ((getScrollY() + StickyViewTopOffset) - getTopForViewRelativeOnlyChild(CurrentlyStickingView)));

        if (ev.getAction() == MotionEvent.ACTION_DOWN)
            hasNotDoneActionDown = false;

        if (hasNotDoneActionDown)
        {
            MotionEvent down = MotionEvent.obtain(ev);
            down.setAction(MotionEvent.ACTION_DOWN);
            super.onTouchEvent(down);
            hasNotDoneActionDown = false;
        }

        if (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL)
            hasNotDoneActionDown = true;

        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt)
    {
        super.onScrollChanged(l, t, oldl, oldt);
        DoTheStickyThing();
    }

    private int getLeftForViewRelativeOnlyChild(View v)
    {
        int left = v.getLeft();

        while(v.getParent() != getChildAt(0))
        {
            v = (View) v.getParent();
            left += v.getLeft();
        }

        return left;
    }

    private int getTopForViewRelativeOnlyChild(View v)
    {
        int top = v.getTop();

        while(v.getParent() != getChildAt(0))
        {
            v = (View) v.getParent();
            top += v.getTop();
        }

        return top;
    }

    private int getRightForViewRelativeOnlyChild(View v)
    {
        int right = v.getRight();

        while(v.getParent() != getChildAt(0))
        {
            v = (View) v.getParent();
            right += v.getRight();
        }

        return right;
    }

    private int getBottomForViewRelativeOnlyChild(View v)
    {
        int bottom = v.getBottom();

        while (v.getParent() != getChildAt(0))
        {
            v = (View) v.getParent();
            bottom += v.getBottom();
        }

        return bottom;
    }

    private void DoTheStickyThing()
    {
        View ViewThatShouldStick = null;
        View ApproachingView = null;

        for (View v : StickyViews)
        {
            int ViewTop = getTopForViewRelativeOnlyChild(v) - getScrollY() + (clippingToPadding ? 0 : getPaddingTop());

            if (ViewTop <= 0)
            {
                if (ViewThatShouldStick == null || ViewTop > (getTopForViewRelativeOnlyChild(ViewThatShouldStick) - getScrollY() + (clippingToPadding ? 0 : getPaddingTop())))
                    ViewThatShouldStick = v;
            }
            else
            {
                if (ApproachingView == null || ViewTop < (getTopForViewRelativeOnlyChild(ApproachingView) - getScrollY() + (clippingToPadding ? 0 : getPaddingTop())))
                    ApproachingView = v;
            }
        }

        if (ViewThatShouldStick != null)
        {
            StickyViewTopOffset = ApproachingView == null ? 0 : Math.min(0, getTopForViewRelativeOnlyChild(ApproachingView) - getScrollY()  + (clippingToPadding ? 0 : getPaddingTop()) - ViewThatShouldStick.getHeight());

            if (ViewThatShouldStick != CurrentlyStickingView)
            {
                if (CurrentlyStickingView != null)
                    stopStickingCurrentlyStickingView();

                StickyViewLeftOffset = getLeftForViewRelativeOnlyChild(ViewThatShouldStick);
                startStickingView(ViewThatShouldStick);
            }
        }
        else if (CurrentlyStickingView != null)
            stopStickingCurrentlyStickingView();
    }

    private void startStickingView(View viewThatShouldStick)
    {
        CurrentlyStickingView = viewThatShouldStick;
    }

    private void stopStickingCurrentlyStickingView()
    {
        CurrentlyStickingView = null;
        removeCallbacks(invalidateRunnable);
    }

    private void notifyHierarchyChanged()
    {
        if (CurrentlyStickingView!=null)
            stopStickingCurrentlyStickingView();

        StickyViews.clear();
        FindStickyViews(getChildAt(0));
        DoTheStickyThing();
        invalidate();
    }

    private void FindStickyViews(View view)
    {
        if (view instanceof ViewGroup)
        {
            ViewGroup viewGroup = (ViewGroup) view;

            for (int I = 0 ; I < viewGroup.getChildCount() ; I++)
            {
                String Tag = String.valueOf(viewGroup.getChildAt(I).getTag());

                if (Tag != null && Tag.contains("sticky"))
                    StickyViews.add(viewGroup.getChildAt(I));
                else if (viewGroup.getChildAt(I) instanceof ViewGroup)
                    FindStickyViews(viewGroup.getChildAt(I));
            }
        }
        else
        {
            String Tag = String.valueOf(view.getTag());

            if (Tag != null && Tag.contains("sticky"))
                StickyViews.add(view);
        }
    }
}
