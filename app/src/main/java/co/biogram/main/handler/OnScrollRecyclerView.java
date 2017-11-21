package co.biogram.main.handler;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class OnScrollRecyclerView extends RecyclerView.OnScrollListener
{
    private final LinearLayoutManager LinearLayoutManagerMain;
    private int PreviousTotalItemCount = 0;
    private boolean IsLoading = true;

    protected OnScrollRecyclerView(LinearLayoutManager linearLayoutManager)
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

    public void ResetState(boolean Full)
    {
        IsLoading = false;

        if (Full)
            PreviousTotalItemCount = 0;
    }

    public abstract void OnLoadMore();
}
