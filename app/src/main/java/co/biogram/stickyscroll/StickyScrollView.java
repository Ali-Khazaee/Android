package co.biogram.stickyscroll;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

import co.biogram.main.R;

public class StickyScrollView extends NestedScrollView implements IStickyScrollPresentation
{
    private View stickyFooterView;
    private View stickyHeaderView;

    private static final String SCROLL_STATE = "scroll_state";
    private static final String SUPER_STATE = "super_state";

    private StickyScrollPresenter mStickyScrollPresenter;
    int[] updatedFooterLocation = new int[2];

    public StickyScrollView(Context context)
    {
        this(context, null, 0);
    }

    public StickyScrollView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public StickyScrollView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        IScreenInfoProvider screenInfoProvider = new ScreenInfoProvider(context);
        IResourceProvider resourceProvider = new ResourceProvider(context, attrs, R.styleable.StickyScrollView);
        mStickyScrollPresenter = new StickyScrollPresenter(this, screenInfoProvider, resourceProvider);

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
        {
            @Override
            public void onGlobalLayout()
            {
                mStickyScrollPresenter.onGlobalLayoutChange(R.styleable.StickyScrollView_stickyHeader, R.styleable.StickyScrollView_stickyFooter);
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        super.onLayout(changed, l, t, r, b);

        if (stickyFooterView != null && !changed)
        {
            stickyFooterView.getLocationInWindow(updatedFooterLocation);
            mStickyScrollPresenter.recomputeFooterLocation(getRelativeTop(stickyFooterView), updatedFooterLocation[1]);
        }
    }

    @Override
    public void initHeaderView(int id)
    {
        stickyHeaderView = findViewById(id);
        mStickyScrollPresenter.initStickyHeader(stickyHeaderView.getTop());
    }

    @Override
    public void initFooterView(int id)
    {
        stickyFooterView = findViewById(id);
        mStickyScrollPresenter.initStickyFooter(stickyFooterView.getMeasuredHeight(), getRelativeTop(stickyFooterView));
    }

    @Override
    public void freeHeader()
    {
        if (stickyHeaderView != null)
        {
            stickyHeaderView.setTranslationY(0);
            SetTranslationZ(stickyHeaderView, 0);
        }
    }

    @Override
    public void freeFooter()
    {
        if (stickyFooterView != null)
            stickyFooterView.setTranslationY(0);
    }

    @Override
    public void stickHeader(int translationY)
    {
        if (stickyHeaderView != null)
        {
            stickyHeaderView.setTranslationY(translationY);
            SetTranslationZ(stickyHeaderView, 1);
        }
    }

    @Override
    public void stickFooter(int translationY)
    {
        if (stickyFooterView != null)
            stickyFooterView.setTranslationY(translationY);
    }

    @Override
    protected void onScrollChanged(int mScrollX, int mScrollY, int oldX, int oldY)
    {
        super.onScrollChanged(mScrollX, mScrollY, oldX, oldY);
        mStickyScrollPresenter.onScroll(mScrollY);
    }

     @Override
    public Parcelable onSaveInstanceState()
    {
        Bundle bundle = new Bundle();
        bundle.putParcelable(SUPER_STATE, super.onSaveInstanceState());
        bundle.putBoolean(SCROLL_STATE, mStickyScrollPresenter.mScrolled);
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable State)
    {
        if (State instanceof Bundle)
        {
            Bundle bundle = (Bundle) State;
            mStickyScrollPresenter.mScrolled = bundle.getBoolean(SCROLL_STATE);
            State = bundle.getParcelable(SUPER_STATE);
        }

        super.onRestoreInstanceState(State);
    }

    void SetTranslationZ(View view, float translationZ)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            ViewCompat.setTranslationZ(view, translationZ);
        }
        else if (translationZ != 0)
        {
            view.bringToFront();

            if (view.getParent() != null)
            {
                ((View) view.getParent()).invalidate();
            }
        }
    }

    private int getRelativeTop(View myView)
    {
        if (myView.getParent() == myView.getRootView())
        {
            return myView.getTop();
        }
        else
        {
            return myView.getTop() + getRelativeTop((View) myView.getParent());
        }
    }
}
