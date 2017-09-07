package co.biogram.main.misc;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import co.biogram.main.R;
import co.biogram.main.handler.MiscHandler;

public class RecyclerViewWithPull extends RecyclerView
{
    private final AdapterDataObserver Observer = new DataObserver();
    private MainAdapter AdapterMain;
    private final RefreshView HeaderView;
    private float LastY = -1;

    public RecyclerViewWithPull(Context context)
    {
        super(context);
        HeaderView = new RefreshView(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e)
    {
        if (LastY == -1)
            LastY = e.getRawY();

        switch (e.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                LastY = e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float MoveY = e.getRawY() - LastY;
                LastY = e.getRawY();

                if (HeaderView.GetVisibleHeight() == 0 && MoveY < 0)
                    return super.onTouchEvent(e);

                if (HeaderView.getParent() != null && HeaderView.GetRefreshState() != RefreshView.STATE_REFRESHING)
                {
                    HeaderView.OnScroll((int) (MoveY / 2));
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
                HeaderView.CheckRefresh();
                break;
        }

        return super.onTouchEvent(e);
    }

    @Override
    public void setAdapter(Adapter adapter)
    {
        AdapterMain = new MainAdapter(adapter);

        super.setAdapter(AdapterMain);

        adapter.registerAdapterDataObserver(Observer);
        Observer.onChanged();
    }

    @Override
    public Adapter getAdapter()
    {
        if (AdapterMain != null)
            return AdapterMain.GetAdapter();

        return null;
    }

    public void SetPullToRefreshListener(PullToRefreshListener pullToRefreshListener)
    {
        HeaderView.SetPullToRefreshListener(pullToRefreshListener);
    }

    public void SetRefreshComplete()
    {
        HeaderView.SetRefreshComplete();
    }

    private class MainAdapter extends RecyclerView.Adapter<ViewHolder>
    {
        private final Adapter adapter;

        MainAdapter(Adapter a)
        {
            adapter = a;
        }

        @Override
        public ViewHolder  onCreateViewHolder(ViewGroup Parent, int ViewType)
        {
            if (ViewType == 10000)
                return new ViewHolderMain(HeaderView);

            return adapter.onCreateViewHolder(Parent, ViewType);
        }

        @Override
        public int getItemCount()
        {
            return adapter.getItemCount();
        }

        @Override
        public int getItemViewType(int Position)
        {
            if (IsRefreshHeader(Position))
                return 10000;

            return 0;
        }

        @Override
        public void onBindViewHolder(ViewHolder Holder, int Position)
        {
            if (IsRefreshHeader(Position))
                return;

            adapter.onBindViewHolder(Holder, Position);
        }

        @Override
        public void onBindViewHolder(ViewHolder Holder, int Position, List<Object> Payloads)
        {
            if (IsRefreshHeader(Position))
                return;

            if (Payloads.isEmpty())
                adapter.onBindViewHolder(Holder, Position);
            else
                adapter.onBindViewHolder(Holder, Position, Payloads);
        }

        @Override
        public long getItemId(int Position)
        {
            if (Position >= 1)
                return adapter.getItemId(Position);

            return -1;
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView)
        {
            super.onAttachedToRecyclerView(recyclerView);
            adapter.onAttachedToRecyclerView(recyclerView);
        }

        @Override
        public void onDetachedFromRecyclerView(RecyclerView recyclerView)
        {
            adapter.onDetachedFromRecyclerView(recyclerView);
        }

        @Override
        public void onViewAttachedToWindow(ViewHolder holder)
        {
            super.onViewAttachedToWindow(holder);
            adapter.onViewAttachedToWindow(holder);
        }

        @Override
        public void onViewDetachedFromWindow(ViewHolder holder)
        {
            adapter.onViewDetachedFromWindow(holder);
        }

        @Override
        public void onViewRecycled(ViewHolder holder)
        {
            adapter.onViewRecycled(holder);
        }

        @Override
        public boolean onFailedToRecycleView(ViewHolder holder)
        {
            return adapter.onFailedToRecycleView(holder);
        }

        @Override
        public void unregisterAdapterDataObserver(AdapterDataObserver observer)
        {
            adapter.unregisterAdapterDataObserver(observer);
        }

        @Override
        public void registerAdapterDataObserver(AdapterDataObserver observer)
        {
            adapter.registerAdapterDataObserver(observer);
        }

        boolean IsRefreshHeader(int Position)
        {
            return Position == 0;
        }

        private Adapter GetAdapter()
        {
            return adapter;
        }

        class ViewHolderMain extends RecyclerView.ViewHolder
        {
            ViewHolderMain(View view)
            {
                super(view);
            }
        }
    }

    private class DataObserver extends AdapterDataObserver
    {

        @Override
        public void onChanged()
        {
            AdapterMain.notifyDataSetChanged();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount)
        {
            AdapterMain.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount)
        {
            AdapterMain.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload)
        {
            AdapterMain.notifyItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount)
        {
            AdapterMain.notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount)
        {
            AdapterMain.notifyItemMoved(fromPosition, toPosition);
        }
    }

    public interface PullToRefreshListener
    {
        void OnRefresh();
    }

    private class RefreshView extends LinearLayout
    {
        public static final int STATE_NORMAL = 0;
        public static final int STATE_RELEASE_TO_REFRESH = 1;
        public static final int STATE_REFRESHING = 2;
        public static final int STATE_DONE = 3;

        private RelativeLayout RelativeLayoutMain;
        private LoadingView LoadingViewMain;
        private ImageView ImageViewArrow;
        private TextView TextViewTip;

        private int RefreshHeight;
        private int RefreshState = 0;

        private RecyclerViewWithPull.PullToRefreshListener PullToRefreshListener;

        public RefreshView(Context context)
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

        public void SetVisibleHeight(int Height)
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

        public void SetState(int State)
        {
            if (RefreshState == State)
                return;

            if (State == STATE_REFRESHING)
            {
                RefreshState = State;
                ImageViewArrow.setVisibility(GONE);
                LoadingViewMain.setVisibility(VISIBLE);
                LoadingViewMain.Start();
                TextViewTip.setText(getContext().getString(R.string.RecyclerViewWithPullRefreshing));
                SmoothScrollTo(GetScreenHeight() / 9);

                if (PullToRefreshListener != null)
                    PullToRefreshListener.OnRefresh();
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

        public void SetPullToRefreshListener(RecyclerViewWithPull.PullToRefreshListener pullToRefreshListener)
        {
            PullToRefreshListener = pullToRefreshListener;
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
    }
}
