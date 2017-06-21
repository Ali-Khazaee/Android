package co.biogram.main.misc;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import co.biogram.main.handler.MiscHandler;

public abstract class RecyclerViewScroll extends RecyclerView.OnScrollListener
{
    private boolean IsLoading = true;
    private int PreviousTotalCount = 0;
    private final LinearLayoutManager LayoutManager;

    protected RecyclerViewScroll(LinearLayoutManager linearLayoutManager)
    {
        LayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView view, int X, int Y)
    {
        MiscHandler.Debug(X + " - " + Y);

        if (X == 0 && Y == 0)
            return;

        int LastVisibleItemPosition = LayoutManager.findLastVisibleItemPosition();
        int TotalCount = LayoutManager.getItemCount();

        if (TotalCount < PreviousTotalCount)
        {
            PreviousTotalCount = TotalCount;

            if (TotalCount == 0)
                IsLoading = true;
        }

        if (IsLoading && (TotalCount > PreviousTotalCount))
        {
            IsLoading = false;
            PreviousTotalCount = TotalCount;
        }

        if (!IsLoading && (LastVisibleItemPosition + 5) > TotalCount)
        {
            OnLoadMore();
            IsLoading = true;
        }
    }

    public abstract void OnLoadMore();
}
