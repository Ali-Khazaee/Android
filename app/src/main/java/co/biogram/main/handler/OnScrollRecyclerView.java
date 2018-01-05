package co.biogram.main.handler;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class OnScrollRecyclerView extends RecyclerView.OnScrollListener
{
    private LinearLayoutManager LayoutManager;
    private int PreviousTotalCount = 0;
    private boolean IsLoading = true;

    OnScrollRecyclerView(LinearLayoutManager llm)
    {
        LayoutManager = llm;
    }

    @Override
    public void onScrolled(RecyclerView View, int X, int Y)
    {
        if (X == 0 && Y == 0)
            return;

        int TotalCount = LayoutManager.getItemCount();

        if (IsLoading && (TotalCount > PreviousTotalCount))
        {
            IsLoading = false;
            PreviousTotalCount = TotalCount;
        }

        if (!IsLoading && (LayoutManager.findLastVisibleItemPosition() + 5) > TotalCount)
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
