package co.biogram.main.misc;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import co.biogram.main.handler.MiscHandler;

public class LinearLayoutManager2 extends LinearLayoutManager
{
    public LinearLayoutManager2(Context context)
    {
        super(context);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state)
    {
        try
        {
            super.onLayoutChildren(recycler, state);
        }
        catch (Exception e)
        {
            MiscHandler.Debug("LinearLayoutManager2: " + e.toString());
        }
    }
}
