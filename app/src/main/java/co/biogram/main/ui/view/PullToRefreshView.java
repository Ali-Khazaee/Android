package co.biogram.main.ui.view;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import co.biogram.main.R;
import co.biogram.main.handler.Misc;

// TODO: Scroll B Samte Bala Bug e, Nabayad Scroll She Ta Vaghti k Pull View Hanoz Height esh 0 Nashode

public class PullToRefreshView extends RelativeLayout {
    private static final int STATE_NORMAL = 0;
    private static final int STATE_RELEASE = 1;
    private static final int STATE_REFRESHING = 2;
    private static final int STATE_DONE = 3;

    private OnRefreshListener Listener;
    private LoadingView LoadingViewMain;
    private CircleView CircleViewMain;

    private int RefreshState = 0;
    private int RefreshHeight;
    private float LastY = -1;

    public PullToRefreshView(Context c) {
        super(c);

        RelativeLayout.LayoutParams LoadingViewMainParam = new RelativeLayout.LayoutParams(Misc.ToDP(56), Misc.ToDP(56));
        LoadingViewMainParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        LoadingViewMain = new LoadingView(c);
        LoadingViewMain.setLayoutParams(LoadingViewMainParam);
        LoadingViewMain.SetColor(R.color.Gray);
        LoadingViewMain.setVisibility(GONE);

        addView(LoadingViewMain);

        RelativeLayout.LayoutParams CircleViewMainParam = new RelativeLayout.LayoutParams(Misc.ToDP(40), Misc.ToDP(40));
        CircleViewMainParam.setMargins(0, Misc.ToDP(8), 0, Misc.ToDP(8));
        CircleViewMainParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        CircleViewMain = new CircleView(c);
        CircleViewMain.setLayoutParams(CircleViewMainParam);
        CircleViewMain.SetProgressColor(R.color.Gray);
        CircleViewMain.SetProgressWidth(2);
        CircleViewMain.InvalidateTextPaints();

        addView(CircleViewMain);

        RefreshHeight = GetScreenHeight() / 6;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                LastY = e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float MoveY = e.getRawY() - LastY;
                LastY = e.getRawY();

                if (MoveY > 80.0f || (getHeight() == 0 && MoveY < 0))
                    return super.onTouchEvent(e);

                if (RefreshState != STATE_REFRESHING) {
                    int Y = (int) (MoveY / 2);
                    int NewVisibleHeight = getHeight() + Y;

                    if (NewVisibleHeight >= RefreshHeight && RefreshState != STATE_RELEASE)
                        RefreshState = STATE_RELEASE;
                    else if (NewVisibleHeight < RefreshHeight && RefreshState != STATE_NORMAL)
                        RefreshState = STATE_NORMAL;

                    CircleViewMain.SetProgressPercentage(Math.min(Math.max(1, NewVisibleHeight) / (RefreshHeight / 100), 100));

                    SetVisibleHeight(NewVisibleHeight);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (getHeight() <= 0)
                    super.onTouchEvent(e);

                if (RefreshState == STATE_NORMAL) {
                    SmoothScrollTo(0);
                    RefreshState = STATE_DONE;
                } else if (RefreshState == STATE_RELEASE) {
                    RefreshState = STATE_REFRESHING;
                    CircleViewMain.setVisibility(GONE);
                    LoadingViewMain.setVisibility(VISIBLE);
                    LoadingViewMain.Start();
                    SmoothScrollTo(GetScreenHeight() / 9);
                    Listener.OnRefresh();
                }
                break;
        }

        return super.onTouchEvent(e);
    }

    public void SetOnRefreshListener(OnRefreshListener l) {
        Listener = l;
    }

    private void SetVisibleHeight(int Height) {
        if (Height > RefreshHeight)
            Height = RefreshHeight;
        else if (Height < 0)
            Height = 0;

        ViewGroup.LayoutParams LayoutParam = getLayoutParams();

        if (LayoutParam != null) {
            LayoutParam.height = Height;
            setLayoutParams(LayoutParam);
        }
    }

    public void SetRefreshComplete() {
        if (RefreshState == STATE_REFRESHING) {
            RefreshState = STATE_DONE;
            LoadingViewMain.Stop();
            LoadingViewMain.setVisibility(GONE);
            CircleViewMain.setVisibility(VISIBLE);
            SmoothScrollTo(0);
        }
    }

    private int GetScreenHeight() {
        DisplayMetrics DisplayMetric = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(DisplayMetric);

        return DisplayMetric.heightPixels;
    }

    private void SmoothScrollTo(int Dest) {
        ValueAnimator Anim = ValueAnimator.ofInt(getHeight(), Dest);
        Anim.setDuration(300).start();
        Anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator a) {
                CircleViewMain.SetProgressPercentage(Math.max(1, getHeight()) / (RefreshHeight / 100));
                SetVisibleHeight((int) a.getAnimatedValue());
            }
        });
        Anim.start();
    }

    public interface OnRefreshListener {
        void OnRefresh();
    }
}
