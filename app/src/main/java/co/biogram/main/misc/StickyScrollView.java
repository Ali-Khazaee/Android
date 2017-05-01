package co.biogram.main.misc;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class StickyScrollView extends NestedScrollView
{
    private final Runnable InvalidateRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            if (currentlyStickingView != null)
            {
                int l = getLeftForViewRelativeOnlyChild(currentlyStickingView);
                int t  = getBottomForViewRelativeOnlyChild(currentlyStickingView);
                int r = getRightForViewRelativeOnlyChild(currentlyStickingView);
                int b = (int) (getScrollY() + (currentlyStickingView.getHeight() + stickyViewTopOffset));
                invalidate(l, t, r, b);
            }

            postDelayed(this, 16);
        }
    };

    public StickyScrollView(Context context)
    {
        this(context, null);
    }

    public StickyScrollView(Context context, AttributeSet attrs)
    {
        this(context, attrs, android.R.attr.scrollViewStyle);
    }

    public StickyScrollView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    @Override
    public void addView(View child)
    {
        super.addView(child);
        findStickyViews(child);
    }

    @Override
    public void addView(View child, int index)
    {
        super.addView(child, index);
        findStickyViews(child);
    }

    @Override
    public void addView(View child, int index, android.view.ViewGroup.LayoutParams params)
    {
        super.addView(child, index, params);
        findStickyViews(child);
    }

    @Override
    public void addView(View child, int width, int height)
    {
        super.addView(child, width, height);
        findStickyViews(child);
    }

    @Override
    public void addView(View child, android.view.ViewGroup.LayoutParams params)
    {
        super.addView(child, params);
        findStickyViews(child);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt)
    {
        super.onScrollChanged(l, t, oldl, oldt);
        DoTheStickyThing();
    }

    @Override
    protected void dispatchDraw(Canvas canvas)
    {
        super.dispatchDraw(canvas);

        if (currentlyStickingView != null)
        {
            canvas.save();
            canvas.translate(getPaddingLeft() + stickyViewLeftOffset, getScrollY() + stickyViewTopOffset + (clippingToPadding ? getPaddingTop() : 0));
            canvas.clipRect(0, (clippingToPadding ? -stickyViewTopOffset : 0), getWidth() - stickyViewLeftOffset, currentlyStickingView.getHeight() + 1);
            canvas.clipRect(0, (clippingToPadding ? -stickyViewTopOffset : 0), getWidth(), currentlyStickingView.getHeight());
            currentlyStickingView.draw(canvas);
            canvas.restore();
        }
    }

    private void DoTheStickyThing()
    {
        View ViewThatShouldStick = null;
        View ApproachingView = null;

        for (View v : stickyViews)
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
            stickyViewTopOffset = ApproachingView == null ? 0 : Math.min(0, getTopForViewRelativeOnlyChild(ApproachingView) - getScrollY()  + (clippingToPadding ? 0 : getPaddingTop()) - ViewThatShouldStick.getHeight());

            if (ViewThatShouldStick != currentlyStickingView)
            {
                if (currentlyStickingView!=null)
                    stopStickingCurrentlyStickingView();

                stickyViewLeftOffset = getLeftForViewRelativeOnlyChild(ViewThatShouldStick);
                currentlyStickingView = ViewThatShouldStick;
            }
        }
        else if (currentlyStickingView != null)
        {
            stopStickingCurrentlyStickingView();
        }
    }


















    private ArrayList<View> stickyViews = new ArrayList<>();
    private View currentlyStickingView;
    private float stickyViewTopOffset;
    private int stickyViewLeftOffset;
    private boolean redirectTouchesToStickyView;
    private boolean clippingToPadding;
    private boolean clipToPaddingHasBeenSet;

    private int getLeftForViewRelativeOnlyChild(View v){
        int left = v.getLeft();
        while(v.getParent() != getChildAt(0)){
            v = (View) v.getParent();
            left += v.getLeft();
        }
        return left;
    }
    
    private int getTopForViewRelativeOnlyChild(View v){
        int top = v.getTop();
        while(v.getParent() != getChildAt(0)){
            v = (View) v.getParent();
            top += v.getTop();
        }
        return top;
    }
    
    private int getRightForViewRelativeOnlyChild(View v){
        int right = v.getRight();
        while(v.getParent() != getChildAt(0)){
            v = (View) v.getParent();
            right += v.getRight();
        }
        return right;
    }

    private int getBottomForViewRelativeOnlyChild(View v){
        int bottom = v.getBottom();
        while(v.getParent() != getChildAt(0)){
            v = (View) v.getParent();
            bottom += v.getBottom();
        }
        return bottom;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(!clipToPaddingHasBeenSet){
            clippingToPadding = true;
        }
        notifyHierarchyChanged();
    }

    @Override
    public void setClipToPadding(boolean clipToPadding) {
        super.setClipToPadding(clipToPadding);
        clippingToPadding  = clipToPadding;
        clipToPaddingHasBeenSet = true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(ev.getAction()==MotionEvent.ACTION_DOWN){
            redirectTouchesToStickyView = true;
        }

        if(redirectTouchesToStickyView)
        {
            redirectTouchesToStickyView = currentlyStickingView != null;

            if (redirectTouchesToStickyView)
            {
                redirectTouchesToStickyView =
                    ev.getY() <= (currentlyStickingView.getHeight() + stickyViewTopOffset) &&
                    ev.getX() >= getLeftForViewRelativeOnlyChild(currentlyStickingView) &&
                    ev.getX() <= getRightForViewRelativeOnlyChild(currentlyStickingView);
            }
        }else if(currentlyStickingView == null){
            redirectTouchesToStickyView = false;
        }
        if(redirectTouchesToStickyView){
            ev.offsetLocation(0, -1*((getScrollY() + stickyViewTopOffset) - getTopForViewRelativeOnlyChild(currentlyStickingView)));
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean hasNotDoneActionDown = true;

    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        if(redirectTouchesToStickyView){
            ev.offsetLocation(0, ((getScrollY() + stickyViewTopOffset) - getTopForViewRelativeOnlyChild(currentlyStickingView)));
        }

        if(ev.getAction()==MotionEvent.ACTION_DOWN){
            hasNotDoneActionDown = false;
        }

        if(hasNotDoneActionDown)
        {
            MotionEvent down = MotionEvent.obtain(ev);
            down.setAction(MotionEvent.ACTION_DOWN);
            super.onTouchEvent(down);
            hasNotDoneActionDown = false;
        }

        if(ev.getAction()==MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL)
        {
            hasNotDoneActionDown = true;
        }

        return super.onTouchEvent(ev);
    }

    private void stopStickingCurrentlyStickingView()
    {
        currentlyStickingView = null;
        removeCallbacks(InvalidateRunnable);
    }

    private void notifyHierarchyChanged()
    {
        if (currentlyStickingView != null)
            stopStickingCurrentlyStickingView();

        stickyViews.clear();
        findStickyViews(getChildAt(0));
        DoTheStickyThing();
        invalidate();
    }

    private void findStickyViews(View v)
    {
        if (v instanceof ViewGroup)
        {
            ViewGroup view = (ViewGroup) v;

            for(int i = 0 ; i < view.getChildCount(); i++)
            {
                String tag = getStringTagForView(view.getChildAt(i));

                if (tag != null && tag.contains("sticky"))
                    stickyViews.add(view.getChildAt(i));
                else if (view.getChildAt(i) instanceof ViewGroup)
                    findStickyViews(view.getChildAt(i));
            }
        }
        else
        {
            String tag = (String) v.getTag();

            if (tag != null && tag.contains("sticky"))
                stickyViews.add(v);
        }
    }

    private String getStringTagForView(View v)
    {
        Object tagObject = v.getTag();
        return String.valueOf(tagObject);
    }
}
