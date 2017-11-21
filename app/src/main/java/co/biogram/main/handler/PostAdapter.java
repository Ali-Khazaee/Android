package co.biogram.main.handler;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import co.biogram.main.R;
import co.biogram.main.fragment.FragmentActivity;
import co.biogram.main.ui.view.CircleView;
import co.biogram.main.ui.view.TextView;

public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private List<PostStruct> PostList = new ArrayList<>();
    private final FragmentActivity Activity;

    public PostAdapter(FragmentActivity activity, List<PostStruct> postList)
    {
        Activity = activity;
        PostList = postList;
    }

    private class ViewHolderMain extends RecyclerView.ViewHolder
    {
        ViewHolderMain(View v, int viewType)
        {
            super(v);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if (viewType == 2)
        {
            RelativeLayout RelativeLayoutInfo = new RelativeLayout(Activity);
            RelativeLayoutInfo.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(Activity, 150)));

            View ViewLine = new View(Activity);
            ViewLine.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(Activity, 5)));
            ViewLine.setBackgroundResource(R.color.Gray);
            ViewLine.setId(MiscHandler.GenerateViewID());

            RelativeLayoutInfo.addView(ViewLine);

            RelativeLayout.LayoutParams ImageViewCloseParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(Activity, 48), MiscHandler.ToDimension(Activity, 48));
            ImageViewCloseParam.addRule(RelativeLayout.BELOW, ViewLine.getId());
            ImageViewCloseParam.addRule(MiscHandler.Align("L"));

            ImageView ImageViewClose = new ImageView(Activity);
            ImageViewClose.setLayoutParams(ImageViewCloseParam);
            ImageViewClose.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ImageViewClose.setImageResource(R.drawable.ic_close_gray);

            RelativeLayoutInfo.addView(ImageViewClose);

            RelativeLayout.LayoutParams TextViewLevelParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewLevelParam.addRule(RelativeLayout.BELOW, ViewLine.getId());
            TextViewLevelParam.setMargins(MiscHandler.ToDimension(Activity, 15), MiscHandler.ToDimension(Activity, 10), MiscHandler.ToDimension(Activity, 15), MiscHandler.ToDimension(Activity, 10));
            TextViewLevelParam.addRule(MiscHandler.Align("R"));

            TextView TextViewLevel = new TextView(Activity, 14, true);
            TextViewLevel.setLayoutParams(TextViewLevelParam);
            TextViewLevel.setText(Activity.getString(R.string.InboxUIUserLevel));
            TextViewLevel.setTextColor(ContextCompat.getColor(Activity, R.color.Gray4));
            TextViewLevel.setId(MiscHandler.GenerateViewID());

            RelativeLayoutInfo.addView(TextViewLevel);

            RelativeLayout.LayoutParams CircleViewLevelParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(Activity, 80), MiscHandler.ToDimension(Activity, 80));
            CircleViewLevelParam.addRule(RelativeLayout.BELOW, TextViewLevel.getId());
            CircleViewLevelParam.setMargins(MiscHandler.ToDimension(Activity, 15), 0, MiscHandler.ToDimension(Activity, 15), 0);
            CircleViewLevelParam.addRule(MiscHandler.Align("R"));

            CircleView CircleViewLevel = new CircleView(Activity);
            CircleViewLevel.setLayoutParams(CircleViewLevelParam);

            RelativeLayoutInfo.addView(CircleViewLevel);

            return new ViewHolderMain(RelativeLayoutInfo, viewType);
        }

        return new ViewHolderMain(new View(Activity), viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {

    }

    @Override
    public int getItemViewType(int Position)
    {
        return PostList.get(Position).ViewType;
    }

    @Override
    public int getItemCount()
    {
        return PostList.size();
    }

    public static class PostStruct
    {
        int ViewType;

        public PostStruct(int viewType)
        {
            ViewType = viewType;
        }
    }
}
