package co.biogram.main.handler;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class RecyclerViewOnScroll extends RecyclerView.OnScrollListener
{
    private RecyclerView.LayoutManager LayoutManager;
    private int PreviousTotalCount = 0;
    private boolean IsLoading = true;

    @Override
    public void onScrolled(RecyclerView rc, int X, int Y)
    {
        if (LayoutManager == null)
            LayoutManager = rc.getLayoutManager();

        if (LayoutManager == null || (X == 0 && Y == 0))
            return;

        int TotalCount = LayoutManager.getItemCount();

        if (IsLoading && (TotalCount > PreviousTotalCount))
        {
            IsLoading = false;
            PreviousTotalCount = TotalCount;
        }

        int LastPosition = 5;

        if (LayoutManager instanceof LinearLayoutManager)
            LastPosition += ((LinearLayoutManager) LayoutManager).findLastVisibleItemPosition();
        else if (LayoutManager instanceof GridLayoutManager)
            LastPosition += ((GridLayoutManager) LayoutManager).findLastVisibleItemPosition();

        if (!IsLoading && LastPosition > TotalCount)
        {
            OnLoadMore();
            IsLoading = true;
            PreviousTotalCount++;
        }
    }

    public void ResetState(boolean Full)
    {
        IsLoading = false;

        if (Full)
            PreviousTotalCount = 0;
    }

    public abstract void OnLoadMore();
}
