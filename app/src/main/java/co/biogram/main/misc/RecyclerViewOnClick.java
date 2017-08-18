package co.biogram.main.misc;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class RecyclerViewOnClick implements RecyclerView.OnItemTouchListener
{
    private final OnItemClickListener OnClickListener;
    private final GestureDetector Detector;

    public RecyclerViewOnClick(Context context, final RecyclerView recyclerView, OnItemClickListener listener)
    {
        OnClickListener = listener;

        Detector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener()
        {
            @Override
            public boolean onSingleTapUp(MotionEvent e)
            {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e)
            {
                View child = recyclerView.findChildViewUnder(e.getX(), e.getY());

                if (child != null && OnClickListener != null)
                    OnClickListener.OnLongClick(child, recyclerView.getChildAdapterPosition(child));
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e)
    {
        View childView = view.findChildViewUnder(e.getX(), e.getY());

        if (childView != null && OnClickListener != null && Detector.onTouchEvent(e))
        {
            OnClickListener.OnClick(childView, view.getChildAdapterPosition(childView));
            return true;
        }

        return false;
    }

    @Override public void onTouchEvent(RecyclerView a, MotionEvent b) { }
    @Override public void onRequestDisallowInterceptTouchEvent(boolean a) { }

    public interface OnItemClickListener
    {
        void OnClick(View view, int Position);
        void OnLongClick(View view, int Position);
    }
}
