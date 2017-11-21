package co.biogram.main.handler;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import co.biogram.main.fragment.FragmentActivity;

public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private List<PostStruct> PostList = new ArrayList<>();
    private final FragmentActivity Activity;

    public PostAdapter(FragmentActivity activity, List<PostStruct> postList)
    {
        Activity = activity;
        PostList = postList;
    }

    class ViewHolderMain extends RecyclerView.ViewHolder
    {
        ViewHolderMain(View v)
        {
            super(v);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {

    }

    @Override
    public int getItemCount()
    {
        return PostList.size();
    }

    public class PostStruct
    {

    }
}
