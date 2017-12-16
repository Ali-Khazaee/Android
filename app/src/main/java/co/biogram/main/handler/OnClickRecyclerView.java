package co.biogram.main.handler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class OnClickRecyclerView implements RecyclerView.OnItemTouchListener
{
    private final OnItemClickListener OnClickListener;
    private final GestureDetector Detector;

    public OnClickRecyclerView(Context context, OnItemClickListener listener)
    {
        OnClickListener = listener;

        Detector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener()
        {
            @Override
            public boolean onSingleTapUp(MotionEvent e)
            {
                return true;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e)
    {
        View childView = view.findChildViewUnder(e.getX(), e.getY());

        if (childView != null && OnClickListener != null && Detector.onTouchEvent(e))
        {
            OnClickListener.OnClick(view.getChildAdapterPosition(childView));
            return true;
        }

        return false;
    }

    @Override public void onTouchEvent(RecyclerView a, MotionEvent b) { }
    @Override public void onRequestDisallowInterceptTouchEvent(boolean a) { }

    public interface OnItemClickListener
    {
        void OnClick(int Position);
    }
}
