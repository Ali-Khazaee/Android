package co.biogram.main.misc;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class RecyclerViewScroll extends RecyclerView.OnScrollListener
{
    private final LinearLayoutManager LinearLayoutManagerMain;
    private int PreviousTotalItemCount = 0;
    private boolean IsLoading = true;

    protected RecyclerViewScroll(LinearLayoutManager linearLayoutManager)
    {
        LinearLayoutManagerMain = linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView View, int X, int Y)
    {
        if (X == 0 && Y == 0)
            return;

        final int TotalItemCount = LinearLayoutManagerMain.getItemCount();
        final int LastVisibleItemPosition = LinearLayoutManagerMain.findLastVisibleItemPosition() + 5;

        if (IsLoading && (TotalItemCount > PreviousTotalItemCount))
        {
            IsLoading = false;
            PreviousTotalItemCount = TotalItemCount;
        }

        if (!IsLoading && LastVisibleItemPosition > TotalItemCount)
        {
            OnLoadMore();
            IsLoading = true;
            PreviousTotalItemCount++;
        }
    }

    public void ResetLoading(boolean ResetPrevious)
    {
        IsLoading = false;

        if (ResetPrevious)
            PreviousTotalItemCount = 0;
    }

    public abstract void OnLoadMore();
}
