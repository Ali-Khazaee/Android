package co.biogram.main.misc;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import co.biogram.main.R;
import co.biogram.main.handler.MiscHandler;

public class PullToRefreshView extends LinearLayout
{
    private static final int STATE_NORMAL = 0;
    private static final int STATE_RELEASE_TO_REFRESH = 1;
    public static final int STATE_REFRESHING = 2;
    private static final int STATE_DONE = 3;

    private final RelativeLayout RelativeLayoutMain;
    private final LoadingView LoadingViewMain;
    private final ImageView ImageViewArrow;
    private final TextView TextViewTip;

    private final int RefreshHeight;
    private int RefreshState = 0;

    private PullToRefreshListener pullToRefreshListener;

    public PullToRefreshView(Context context)
    {
        super(context);
        RefreshHeight = GetScreenHeight() / 6;

        RelativeLayoutMain = new RelativeLayout(context);
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

        RelativeLayout.LayoutParams LinearLayoutMainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 120));
        LinearLayoutMainParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        LinearLayout LinearLayoutMain = new LinearLayout(context);
        LinearLayoutMain.setLayoutParams(LinearLayoutMainParam);
        LinearLayoutMain.setGravity(Gravity.CENTER);

        RelativeLayoutMain.addView(LinearLayoutMain);

        LoadingViewMain = new LoadingView(context);
        LoadingViewMain.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), MiscHandler.ToDimension(context, 56)));
        LoadingViewMain.SetColor(R.color.Gray5);
        LoadingViewMain.setVisibility(GONE);

        LinearLayoutMain.addView(LoadingViewMain);

        ImageViewArrow = new ImageView(context);
        ImageViewArrow.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), MiscHandler.ToDimension(context, 56)));
        ImageViewArrow.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewArrow.setPadding(MiscHandler.ToDimension(context, 17), MiscHandler.ToDimension(context, 17), MiscHandler.ToDimension(context, 17), MiscHandler.ToDimension(context, 17));
        ImageViewArrow.setImageResource(R.drawable.ic_arrow_down);

        LinearLayoutMain.addView(ImageViewArrow);

        TextViewTip = new TextView(context);
        TextViewTip.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewTip.setTextColor(ContextCompat.getColor(context, R.color.Gray5));
        TextViewTip.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        TextViewTip.setText(getContext().getString(R.string.RecyclerViewWithPull));

        LinearLayoutMain.addView(TextViewTip);

        addView(RelativeLayoutMain);
        OnScroll(0);
    }

    public void OnScroll(int Y)
    {
        int NewVisibleHeight = GetVisibleHeight() + Y;

        if (NewVisibleHeight >= RefreshHeight && RefreshState != STATE_RELEASE_TO_REFRESH)
        {
            if (ImageViewArrow.getVisibility() != VISIBLE)
                ImageViewArrow.setVisibility(VISIBLE);

            TextViewTip.setText(getContext().getString(R.string.RecyclerViewWithPullRelease));
            RotationAnimator(180f);
            RefreshState = STATE_RELEASE_TO_REFRESH;
        }

        if (NewVisibleHeight < RefreshHeight && RefreshState != STATE_NORMAL)
        {
            if (ImageViewArrow.getVisibility() != VISIBLE)
                ImageViewArrow.setVisibility(VISIBLE);

            TextViewTip.setText(getContext().getString(R.string.RecyclerViewWithPull));
            RotationAnimator(0);
            RefreshState = STATE_NORMAL;
        }

        SetVisibleHeight(GetVisibleHeight() + Y);
    }

    private void SetVisibleHeight(int Height)
    {
        if (Height < 0)
            Height = 0;

        LayoutParams LayoutParam = (LayoutParams) RelativeLayoutMain.getLayoutParams();
        LayoutParam.height = Height;

        RelativeLayoutMain.setLayoutParams(LayoutParam);
    }

    public int GetRefreshState()
    {
        return RefreshState;
    }

    public void SetRefreshComplete()
    {
        SetState(STATE_DONE);
    }

    private void SetState(int State)
    {
        if (RefreshState == State)
            return;

        if (State == STATE_REFRESHING)
        {
            RefreshState = State;
            ImageViewArrow.setVisibility(GONE);
            LoadingViewMain.setVisibility(VISIBLE);
            LoadingViewMain.Start();
            TextViewTip.setText(getContext().getString(R.string.RecyclerViewWithPullLoading));
            SmoothScrollTo(GetScreenHeight() / 9);

            if (pullToRefreshListener != null)
                pullToRefreshListener.OnRefresh();
        }
        else if (State == STATE_DONE)
        {
            if (RefreshState == STATE_REFRESHING)
            {
                RefreshState = State;
                LoadingViewMain.Stop();
                LoadingViewMain.setVisibility(GONE);
                TextViewTip.setText(getContext().getString(R.string.RecyclerViewWithPullSuccess));
                SmoothScrollTo(0);
            }
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

    public void SetPullToRefreshListener(PullToRefreshListener p)
    {
        pullToRefreshListener = p;
    }

    public void CheckRefresh()
    {
        if (GetVisibleHeight() <= 0)
            return;

        if (RefreshState == STATE_NORMAL)
        {
            SmoothScrollTo(0);
            RefreshState = STATE_DONE;
            return;
        }

        if (RefreshState == STATE_RELEASE_TO_REFRESH)
            SetState(STATE_REFRESHING);
    }

    private void SmoothScrollTo(int Dest)
    {
        ValueAnimator animator = ValueAnimator.ofInt(GetVisibleHeight(), Dest);
        animator.setDuration(300).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                SetVisibleHeight((int) animation.getAnimatedValue());
            }
        });
        animator.start();
    }

    private void RotationAnimator(float Rotation)
    {
        ValueAnimator animator = ValueAnimator.ofFloat(ImageViewArrow.getRotation(), Rotation);
        animator.setDuration(200).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                ImageViewArrow.setRotation((Float) animation.getAnimatedValue());
            }
        });
        animator.start();
    }

    public interface PullToRefreshListener
    {
        void OnRefresh();
    }
}
