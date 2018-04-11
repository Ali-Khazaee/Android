package co.biogram.main.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class FlowLayout extends ViewGroup
{
    private List<View> LineView = new ArrayList<>();
    private List<List<View>> Line = new ArrayList<>();
    private List<Integer> LineHeight = new ArrayList<>();
    private List<Integer> LineMargin = new ArrayList<>();

    public FlowLayout(Context context)
    {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        int width = 0;
        int height = getPaddingTop() + getPaddingBottom();
        int lineWidth = 0;
        int lineHeight = 0;
        int childCount = getChildCount();

        for (int i = 0; i < childCount; i++)
        {
            View child = getChildAt(i);
            boolean lastChild = i == childCount - 1;

            if (child.getVisibility() == View.GONE)
            {
                if (lastChild)
                {
                    width = Math.max(width, lineWidth);
                    height += lineHeight;
                }

                continue;
            }

            measureChildWithMargins(child, widthMeasureSpec, lineWidth, heightMeasureSpec, height);

            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

            int childWidthMode = MeasureSpec.AT_MOST;
            int childWidthSize = sizeWidth;

            int childHeightMode = MeasureSpec.AT_MOST;
            int childHeightSize = sizeHeight;

            if(lp.width == MarginLayoutParams.MATCH_PARENT) {
                childWidthMode = MeasureSpec.EXACTLY    ;
                childWidthSize -= lp.leftMargin + lp.rightMargin;
            } else if(lp.width >= 0) {
                childWidthMode = MeasureSpec.EXACTLY;
                childWidthSize = lp.width;
            }

            if(lp.height >= 0) {
                childHeightMode = MeasureSpec.EXACTLY;
                childHeightSize = lp.height;
            } else if (modeHeight == MeasureSpec.UNSPECIFIED) {
                childHeightMode = MeasureSpec.UNSPECIFIED;
                childHeightSize = 0;
            }

            child.measure(MeasureSpec.makeMeasureSpec(childWidthSize, childWidthMode), MeasureSpec.makeMeasureSpec(childHeightSize, childHeightMode));

            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;

            if(lineWidth + childWidth > sizeWidth)
            {

                width = Math.max(width, lineWidth);
                lineWidth = childWidth;

                height += lineHeight;
                lineHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

            }
            else
                {
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight, child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
            }

            if(lastChild){
                width = Math.max(width, lineWidth);
                height += lineHeight;
            }

        }

        width += getPaddingLeft() + getPaddingRight();

        setMeasuredDimension((modeWidth == MeasureSpec.EXACTLY) ? sizeWidth : width, (modeHeight == MeasureSpec.EXACTLY) ? sizeHeight : height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int mGravity = Gravity.START | Gravity.TOP;
        Line.clear();
        LineHeight.clear();
        LineMargin.clear();

        int width = getWidth();
        int height = getHeight();

        int linesSum = getPaddingTop();

        int lineWidth = 0;
        int lineHeight = 0;

        LineView.clear();


        float horizontalGravityFactor;
        switch ((mGravity & Gravity.HORIZONTAL_GRAVITY_MASK)) {
            case Gravity.LEFT:
            default:
                horizontalGravityFactor = 0;
                break;
            case Gravity.CENTER_HORIZONTAL:
                horizontalGravityFactor = .5f;
                break;
            case Gravity.RIGHT:
                horizontalGravityFactor = 1;
                break;
        }

        for(int i = 0; i < getChildCount(); i++) {

            View child = getChildAt(i);

            if(child.getVisibility() == View.GONE) {
                continue;
            }

            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.bottomMargin + lp.topMargin;

            if(lineWidth + childWidth > width) {
                LineHeight.add(lineHeight);
                Line.add(LineView);
                LineMargin.add((int) ((width - lineWidth) * horizontalGravityFactor) + getPaddingLeft());

                linesSum += lineHeight;

                lineHeight = 0;
                lineWidth = 0;
                LineView = new ArrayList<>();
            }

            lineWidth += childWidth;
            lineHeight = Math.max(lineHeight, childHeight);
            LineView.add(child);
        }

        LineHeight.add(lineHeight);
        Line.add(LineView);
        LineMargin.add((int) ((width - lineWidth) * horizontalGravityFactor) + getPaddingLeft());

        linesSum += lineHeight;

        int verticalGravityMargin = 0;

        switch ((mGravity & Gravity.VERTICAL_GRAVITY_MASK))
        {
            case Gravity.TOP:
            default:
                break;
            case Gravity.CENTER_VERTICAL:
                verticalGravityMargin = (height - linesSum) / 2;
                break;
            case Gravity.BOTTOM:
                verticalGravityMargin = height - linesSum;
                break;
        }

        int numLines = Line.size();
        int left;
        int top = getPaddingTop();

        for (int i = 0; i < numLines; i++)
        {
            lineHeight = LineHeight.get(i);
            LineView = Line.get(i);
            left = LineMargin.get(i);

            int children = LineView.size();

            for (int j = 0; j < children; j++)
            {
                View child = LineView.get(j);

                if(child.getVisibility() == View.GONE) {
                    continue;
                }

                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

                // if height is match_parent we need to remeasure child to line height
                if(lp.height == MarginLayoutParams.MATCH_PARENT) {
                    int childWidthMode = MeasureSpec.AT_MOST;
                    int childWidthSize = lineWidth;

                    if(lp.width == MarginLayoutParams.MATCH_PARENT) {
                        childWidthMode = MeasureSpec.EXACTLY;
                    } else if(lp.width >= 0) {
                        childWidthMode = MeasureSpec.EXACTLY;
                        childWidthSize = lp.width;
                    }

                    child.measure(
                            MeasureSpec.makeMeasureSpec(childWidthSize, childWidthMode),
                            MeasureSpec.makeMeasureSpec(lineHeight - lp.topMargin - lp.bottomMargin, MeasureSpec.EXACTLY)
                    );
                }

                int childWidth = child.getMeasuredWidth();
                int childHeight = child.getMeasuredHeight();

                int gravityMargin = (lineHeight - childHeight - lp.topMargin - lp.bottomMargin) / 2 ;

                child.layout(left + lp.leftMargin,
                        top + lp.topMargin + gravityMargin + verticalGravityMargin,
                        left + childWidth + lp.leftMargin,
                        top + childHeight + lp.topMargin + gravityMargin + verticalGravityMargin);

                left += childWidth + lp.leftMargin + lp.rightMargin;

            }

            top += lineHeight;
        }
    }

    @Override
    protected MarginLayoutParams generateLayoutParams(ViewGroup.LayoutParams p)
    {
        return new MarginLayoutParams(p);
    }

    @Override
    public MarginLayoutParams generateLayoutParams(AttributeSet attrs)
    {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected MarginLayoutParams generateDefaultLayoutParams()
    {
        return new MarginLayoutParams(MarginLayoutParams.MATCH_PARENT, MarginLayoutParams.MATCH_PARENT);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p)
    {
        return super.checkLayoutParams(p) && p instanceof MarginLayoutParams;
    }
}
