package co.biogram.main.ui.social;

import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.androidnetworking.AndroidNetworking;

import org.json.JSONObject;

import co.biogram.main.R;
import co.biogram.main.fragment.FragmentView;
import co.biogram.main.handler.Misc;
import co.biogram.main.handler.PostAdapter;
import co.biogram.main.ui.view.TextView;

public class InboxUI extends FragmentView
{
    private PostAdapter Adapter;

    @Override
    public void OnCreate()
    {
        RelativeLayout RelativeLayoutMain = new RelativeLayout(GetActivity());
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayoutMain.setBackgroundResource(Misc.IsDark() ? R.color.GroundDark : R.color.GroundWhite);
        RelativeLayoutMain.setClickable(true);

        RelativeLayout RelativeLayoutHeader = new RelativeLayout(GetActivity());
        RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56)));
        RelativeLayoutHeader.setBackgroundResource(Misc.IsDark() ? R.color.ActionBarDark : R.color.ActionBarWhite);
        RelativeLayoutHeader.setId(Misc.GenerateViewID());

        RelativeLayoutMain.addView(RelativeLayoutHeader);

        RelativeLayout.LayoutParams ImageViewAddParam = new RelativeLayout.LayoutParams(Misc.ToDP(56), Misc.ToDP(56));
        ImageViewAddParam.addRule(Misc.Align("R"));

        ImageView ImageViewWrite = new ImageView(GetActivity());
        ImageViewWrite.setLayoutParams(ImageViewAddParam);
        ImageViewWrite.setScaleType(ImageView.ScaleType.FIT_XY);
        ImageViewWrite.setId(Misc.GenerateViewID());
        ImageViewWrite.setImageResource(R.drawable.write_plus_blue);
        ImageViewWrite.setPadding(Misc.ToDP(13), Misc.ToDP(13), Misc.ToDP(13), Misc.ToDP(13));
        ImageViewWrite.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { GetActivity().GetManager().OpenView(new WriteUI(), R.id.ContainerFull, "WriteUI"); } });

        RelativeLayoutHeader.addView(ImageViewWrite);

        RelativeLayout.LayoutParams TextViewTitleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTitleParam.addRule(Misc.AlignTo("R"), ImageViewWrite.getId());
        TextViewTitleParam.addRule(RelativeLayout.CENTER_VERTICAL);

        TextView TextViewTitle = new TextView(GetActivity(), 16, true);
        TextViewTitle.setLayoutParams(TextViewTitleParam);
        TextViewTitle.setTextColor(ContextCompat.getColor(GetActivity(), Misc.IsDark() ? R.color.TextDark : R.color.TextWhite));
        TextViewTitle.setPadding(0, Misc.ToDP(6), 0, 0);
        TextViewTitle.setText(GetActivity().getString(R.string.InboxUI));

        RelativeLayoutHeader.addView(TextViewTitle);

        RelativeLayout.LayoutParams ImageViewSearchParam = new RelativeLayout.LayoutParams(Misc.ToDP(56), Misc.ToDP(56));
        ImageViewSearchParam.addRule(Misc.Align("L"));

        ImageView ImageViewSearch = new ImageView(GetActivity());
        ImageViewSearch.setLayoutParams(ImageViewSearchParam);
        ImageViewSearch.setScaleType(ImageView.ScaleType.FIT_XY);
        ImageViewSearch.setId(Misc.GenerateViewID());
        ImageViewSearch.setImageResource(R.drawable.ic_search_blue);
        ImageViewSearch.setPadding(Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15));
        ImageViewSearch.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { /* TODO Search */  } });

        RelativeLayoutHeader.addView(ImageViewSearch);

        RelativeLayout.LayoutParams ImageViewBookmarkParam = new RelativeLayout.LayoutParams(Misc.ToDP(56), Misc.ToDP(56));
        ImageViewBookmarkParam.addRule(Misc.AlignTo("L"), ImageViewSearch.getId());

        ImageView ImageViewBookmark = new ImageView(GetActivity());
        ImageViewBookmark.setLayoutParams(ImageViewBookmarkParam);
        ImageViewBookmark.setScaleType(ImageView.ScaleType.FIT_XY);
        ImageViewBookmark.setId(Misc.GenerateViewID());
        ImageViewBookmark.setImageResource(R.drawable.bookmark_blue);
        ImageViewBookmark.setPadding(Misc.ToDP(16), Misc.ToDP(16), Misc.ToDP(16), Misc.ToDP(16));
        ImageViewBookmark.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { /* TODO Bookmark */  } });

        RelativeLayoutHeader.addView(ImageViewBookmark);

        RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1));
        ViewLineParam.addRule(RelativeLayout.BELOW, RelativeLayoutHeader.getId());

        View ViewLine = new View(GetActivity());
        ViewLine.setLayoutParams(ViewLineParam);
        ViewLine.setBackgroundResource(Misc.IsDark() ? R.color.LineDark : R.color.LineWhite);
        ViewLine.setId(Misc.GenerateViewID());

        RelativeLayoutMain.addView(ViewLine);

        RelativeLayout.LayoutParams RecyclerViewMainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        RecyclerViewMainParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

        RecyclerView RecyclerViewMain = new RecyclerView(GetActivity());
        RecyclerViewMain.setLayoutParams(RecyclerViewMainParam);
        RecyclerViewMain.setAdapter(Adapter = new PostAdapter(GetActivity(), RecyclerViewMain, "InboxUI"));
        RecyclerViewMain.setLayoutManager(new LinearLayoutManager(GetActivity()));

        RelativeLayoutMain.addView(RecyclerViewMain);

        ViewMain = RelativeLayoutMain;
    }

    @Override
    public void OnResume()
    {
        if (Build.VERSION.SDK_INT > 20)
            GetActivity().getWindow().setStatusBarColor(ContextCompat.getColor(GetActivity(), Misc.IsDark() ? R.color.StatusBarDark : R.color.StatusBarWhite));

        Adapter.Update();
    }

    @Override
    public void OnPause()
    {
        AndroidNetworking.forceCancel("InboxUI");
    }

    void Update(JSONObject post)
    {
        Adapter.Update(post);
    }
}
