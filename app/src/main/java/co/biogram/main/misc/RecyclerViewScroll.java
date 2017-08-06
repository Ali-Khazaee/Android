package co.biogram.main.misc;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class RecyclerViewScroll extends RecyclerView.OnScrollListener
{
    private final LinearLayoutManager LayoutManager;
    private int PreviousTotalCount = 0;
    private boolean IsLoading = false;
    private long PreviousRequestTime = 0;

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

        if (IsLoading && (TotalCount > PreviousTotalCount))
        {
            PreviousTotalCount = TotalCount;

            if (PreviousRequestTime > System.currentTimeMillis())
                return;

            IsLoading = false;
        }

        if (!IsLoading && LastVisibleItemPosition > TotalCount)
        {
            OnLoadMore();
            IsLoading = true;
            PreviousRequestTime = System.currentTimeMillis() + 500;
        }
    }

    public void ResetLoading(boolean ResetPrevious)
    {
        IsLoading = false;

        if (ResetPrevious)
            PreviousTotalCount = 0;
    }

    public abstract void OnLoadMore();
}
