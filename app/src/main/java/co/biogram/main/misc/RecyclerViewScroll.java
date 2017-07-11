package co.biogram.main.misc;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class RecyclerViewScroll extends RecyclerView.OnScrollListener
{
    private boolean IsLoading = false;
    private final LinearLayoutManager LayoutManager;

    protected RecyclerViewScroll(LinearLayoutManager linearLayoutManager)
    {
        LayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView view, int X, int Y)
    {
        if (X == 0 && Y == 0)
            return;

        int LastVisibleItemPosition = LayoutManager.findLastVisibleItemPosition() + 5;
        int TotalCount = LayoutManager.getItemCount();

        if (!IsLoading && LastVisibleItemPosition > TotalCount)
        {
            OnLoadMore();
            IsLoading = true;
        }
    }

    public void ResetLoading()
    {
        IsLoading = false;
    }

    public abstract void OnLoadMore();
}
