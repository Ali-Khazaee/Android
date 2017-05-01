package co.biogram.main.misc;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class ViewPagerWrapContentHeight extends ViewPager
{
    public ViewPagerWrapContentHeight(Context context)
    {
        super(context);
    }

    public ViewPagerWrapContentHeight(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int WidthMeasureSpec, int HeightMeasureSpec)
    {
        super.onMeasure(WidthMeasureSpec, HeightMeasureSpec);

        int Height = 0;

        for (int i = 0; i < getChildCount(); i++)
        {
            View Child = getChildAt(i);
            Child.measure(WidthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int ChildHeight = Child.getMeasuredHeight();

            if (ChildHeight > Height)
                Height = ChildHeight;
        }

        HeightMeasureSpec = MeasureSpec.makeMeasureSpec(Height, MeasureSpec.EXACTLY);

        super.onMeasure(WidthMeasureSpec, HeightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event)
    {
        return false;
    }
}