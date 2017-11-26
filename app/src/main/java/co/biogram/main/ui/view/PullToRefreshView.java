package co.biogram.main.ui.view;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import co.biogram.main.R;
import co.biogram.main.handler.Misc;
import co.biogram.main.handler.PostAdapter;

public class PullToRefreshView extends LinearLayout
{
    private static final int STATE_NORMAL = 0;
    private static final int STATE_RELEASE = 1;
    public  static final int STATE_REFRESHING = 2;
    private static final int STATE_DONE = 3;

    private PostAdapter.PullToRefreshListener Listener;
    private final RelativeLayout RelativeLayoutMain;
    private final LoadingView LoadingViewMain;
    private final CircleView CircleViewMain;

    private final int RefreshHeight;
    private int RefreshState = 0;
    private float LastY = -1;

    public PullToRefreshView(Context context)
    {
        super(context);
        RefreshHeight = GetScreenHeight() / 6;

        RelativeLayoutMain = new RelativeLayout(context);
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

        RelativeLayout.LayoutParams LinearLayoutMainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(context, 120));
        LinearLayoutMainParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        LinearLayout LinearLayoutMain = new LinearLayout(context);
        LinearLayoutMain.setLayoutParams(LinearLayoutMainParam);
        LinearLayoutMain.setGravity(Gravity.CENTER);

        RelativeLayoutMain.addView(LinearLayoutMain);

        LoadingViewMain = new LoadingView(context);
        LoadingViewMain.setLayoutParams(new RelativeLayout.LayoutParams(Misc.ToDP(context, 56), Misc.ToDP(context, 56)));
        LoadingViewMain.SetColor(R.color.BlueGray2);
        LoadingViewMain.setVisibility(GONE);

        LinearLayoutMain.addView(LoadingViewMain);

        CircleViewMain = new CircleView(context);
        CircleViewMain.setLayoutParams(new RelativeLayout.LayoutParams(Misc.ToDP(getContext(), 40), Misc.ToDP(getContext(), 40)));
        CircleViewMain.SetProgressColor(R.color.BlueGray2);
        CircleViewMain.SetProgressWidth(2);
        CircleViewMain.InvalidateTextPaints();

        LinearLayoutMain.addView(CircleViewMain);

        addView(RelativeLayoutMain);
        SetVisibleHeight(0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e)
    {
        switch (e.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                LastY = e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                boolean IsUp = LastY < e.getRawY();
                float MoveY = e.getRawY() - LastY;
                LastY = e.getRawY();

                if (GetVisibleHeight() == 0 && MoveY < 0)
                    return super.onTouchEvent(e);

                if (RefreshState != PullToRefreshView.STATE_REFRESHING)
                {
                    OnScroll((int) (MoveY / 2), IsUp);
                    return super.onTouchEvent(e);
                }
                break;
            case MotionEvent.ACTION_UP:
            {
                if (GetVisibleHeight() <= 0)
                    super.onTouchEvent(e);

                if (RefreshState == STATE_NORMAL)
                {
                    SmoothScrollTo(0);
                    RefreshState = STATE_DONE;
                }
                else if (RefreshState == STATE_RELEASE)
                {
                    RefreshState = STATE_REFRESHING;
                    CircleViewMain.setVisibility(GONE);
                    LoadingViewMain.setVisibility(VISIBLE);
                    LoadingViewMain.Start();
                    SmoothScrollTo(GetScreenHeight() / 9);
                    Listener.OnRefresh();
                }

                break;
            }
        }

        return super.onTouchEvent(e);
    }

    public void OnScroll(int Y, boolean IsUp)
    {
        if (Y == 0)
            Y = IsUp ? 1 : -2;

        int NewVisibleHeight = GetVisibleHeight() + Y;

        if (NewVisibleHeight >= RefreshHeight && RefreshState != STATE_RELEASE)
            RefreshState = STATE_RELEASE;
        else if (NewVisibleHeight < RefreshHeight && RefreshState != STATE_NORMAL)
            RefreshState = STATE_NORMAL;

        SetVisibleHeight(GetVisibleHeight() + Y);

        int Percentage = Math.max(1, GetVisibleHeight()) / (RefreshHeight / 100);
        CircleViewMain.SetProgressPercentage(Math.min(Percentage, 100));
    }

    private void SetVisibleHeight(int Height)
    {
        if (Height > RefreshHeight)
            Height = RefreshHeight;
        else if (Height < 0)
            Height = 0;

        LayoutParams LayoutParam = (LayoutParams) RelativeLayoutMain.getLayoutParams();
        LayoutParam.height = Height;

        RelativeLayoutMain.setLayoutParams(LayoutParam);
    }

    public void SetRefreshComplete()
    {
        if (RefreshState == STATE_REFRESHING)
        {
            RefreshState = STATE_DONE;
            LoadingViewMain.Stop();
            LoadingViewMain.setVisibility(GONE);
            CircleViewMain.setVisibility(VISIBLE);
            SmoothScrollTo(0);
        }
    }

    public int GetVisibleHeight()
    {
        LayoutParams LayoutParam = (LayoutParams) RelativeLayoutMain.getLayoutParams();
        return LayoutParam.height;
    }

    private int GetScreenHeight()
    {
        DisplayMetrics DisplayMetric = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(DisplayMetric);

        return DisplayMetric.heightPixels;
    }

    private void SmoothScrollTo(int Dest)
    {
        ValueAnimator Anim = ValueAnimator.ofInt(GetVisibleHeight(), Dest);
        Anim.setDuration(300).start();
        Anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator a)
            {
                CircleViewMain.SetProgressPercentage(Math.max(1, GetVisibleHeight()) / (RefreshHeight / 100));
                SetVisibleHeight((int) a.getAnimatedValue());
            }
        });
        Anim.start();
    }

    public void SetPullToRefreshListener(PostAdapter.PullToRefreshListener l)
    {
        Listener = l;
    }
}
