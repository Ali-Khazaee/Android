package co.biogram.main.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class FlowLayout extends ViewGroup {
    private ArrayList<ArrayList<View>> AllView = new ArrayList<>();
    private ArrayList<Integer> LineHeight = new ArrayList<>();
    private ArrayList<View> LineView = new ArrayList<>();

    public FlowLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context) {
        this(context, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int Width = 0;
        int Height = 0;
        int LineWidth = 0;
        int LineHeight = 0;
        int ChildCount = getChildCount();
        int SizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int ModeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int SizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int ModeHeight = MeasureSpec.getMode(heightMeasureSpec);

        for (int I = 0; I < ChildCount; I++) {
            View Child = getChildAt(I);

            measureChild(Child, widthMeasureSpec, heightMeasureSpec);
            MarginLayoutParams Param = (MarginLayoutParams) Child.getLayoutParams();

            int ChildWidth = Child.getMeasuredWidth() + Param.leftMargin + Param.rightMargin;
            int ChildHeight = Child.getMeasuredHeight() + Param.topMargin + Param.bottomMargin;

            if (LineWidth + ChildWidth > SizeWidth - getPaddingLeft() - getPaddingRight()) {
                Width = Math.max(Width, LineWidth);
                LineWidth = ChildWidth;
                Height += LineHeight;
                LineHeight = ChildHeight;
            } else {
                LineWidth += ChildWidth;
                LineHeight = Math.max(LineHeight, ChildHeight);
            }

            if (I == ChildCount - 1) {
                Width = Math.max(LineWidth, Width);
                Height += LineHeight;
            }
        }

        setMeasuredDimension(ModeWidth == MeasureSpec.EXACTLY ? SizeWidth : Width + getPaddingLeft() + getPaddingRight(), ModeHeight == MeasureSpec.EXACTLY ? SizeHeight : Height + getPaddingTop() + getPaddingBottom());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        AllView.clear();
        LineView.clear();
        LineHeight.clear();

        int LineWidth = 0;
        int LineHeight2 = 0;
        int Width = getWidth();
        int ChildCount = getChildCount();

        for (int I = 0; I < ChildCount; I++) {
            View Child = getChildAt(I);
            MarginLayoutParams Param = (MarginLayoutParams) Child.getLayoutParams();

            int ChildWidth = Child.getMeasuredWidth();
            int ChildHeight = Child.getMeasuredHeight();

            if (ChildWidth + LineWidth + Param.leftMargin + Param.rightMargin > Width - getPaddingLeft() - getPaddingRight()) {
                LineHeight.add(LineHeight2);
                AllView.add(LineView);

                LineWidth = 0;
                LineHeight2 = ChildHeight + Param.topMargin + Param.bottomMargin;
                LineView = new ArrayList<>();
            }

            LineWidth += ChildWidth + Param.leftMargin + Param.rightMargin;
            LineHeight2 = Math.max(LineHeight2, ChildHeight + Param.topMargin + Param.bottomMargin);
            LineView.add(Child);
        }

        LineHeight.add(LineHeight2);
        AllView.add(LineView);

        int Left = getPaddingLeft();
        int Top = getPaddingTop();
        int LineNum = AllView.size();

        for (int I = 0; I < LineNum; I++) {
            LineView = AllView.get(I);
            LineHeight2 = LineHeight.get(I);

            for (int J = 0; J < LineView.size(); J++) {
                View Child = LineView.get(J);

                if (Child.getVisibility() == View.GONE)
                    continue;

                MarginLayoutParams Param = (MarginLayoutParams) Child.getLayoutParams();

                int LC = Left + Param.leftMargin;
                int TC = Top + Param.topMargin;
                int RC = LC + Child.getMeasuredWidth();
                int BC = TC + Child.getMeasuredHeight();

                Child.layout(LC, TC, RC, BC);

                Left += Child.getMeasuredWidth() + Param.leftMargin + Param.rightMargin;
            }

            Left = getPaddingLeft();
            Top += LineHeight2;
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}
