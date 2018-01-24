package co.biogram.main.ui.social;

import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.androidnetworking.AndroidNetworking;

import org.json.JSONObject;

import co.biogram.main.R;
import co.biogram.main.fragment.FragmentView;
import co.biogram.main.handler.Misc;
import co.biogram.main.handler.OnScrollRecyclerView;
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
        RelativeLayoutHeader.setId(Misc.ViewID());

        RelativeLayoutMain.addView(RelativeLayoutHeader);

        RelativeLayout.LayoutParams TextViewTitleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTitleParam.setMargins(Misc.ToDP(15), 0, Misc.ToDP(15), 0);
        TextViewTitleParam.addRule(RelativeLayout.CENTER_VERTICAL);
        TextViewTitleParam.addRule(Misc.Align("R"));

        TextView TextViewTitle = new TextView(GetActivity(), 16, true);
        TextViewTitle.setLayoutParams(TextViewTitleParam);
        TextViewTitle.setTextColor(Misc.Color(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite));
        TextViewTitle.setPadding(0, Misc.ToDP(6), 0, 0);
        TextViewTitle.setText(GetActivity().getString(R.string.InboxUI));

        RelativeLayoutHeader.addView(TextViewTitle);

        RelativeLayout.LayoutParams ImageViewSearchParam = new RelativeLayout.LayoutParams(Misc.ToDP(56), Misc.ToDP(56));
        ImageViewSearchParam.addRule(Misc.Align("L"));

        ImageView ImageViewSearch = new ImageView(GetActivity());
        ImageViewSearch.setLayoutParams(ImageViewSearchParam);
        ImageViewSearch.setId(Misc.ViewID());
        ImageViewSearch.setImageResource(R.drawable._inbox_search);
        ImageViewSearch.setPadding(Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15));
        ImageViewSearch.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { /* TODO Search */  } });

        RelativeLayoutHeader.addView(ImageViewSearch);

        RelativeLayout.LayoutParams ImageViewWriteParam = new RelativeLayout.LayoutParams(Misc.ToDP(56), Misc.ToDP(56));
        ImageViewWriteParam.addRule(Misc.AlignTo("L"), ImageViewSearch.getId());

        ImageView ImageViewWrite = new ImageView(GetActivity());
        ImageViewWrite.setLayoutParams(ImageViewWriteParam);
        ImageViewWrite.setId(Misc.ViewID());
        ImageViewWrite.setImageResource(R.drawable._inbox_write);
        ImageViewWrite.setPadding(Misc.ToDP(6), Misc.ToDP(6), Misc.ToDP(6), Misc.ToDP(6));
        ImageViewWrite.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { GetActivity().GetManager().OpenView(new WriteUI(), R.id.ContainerFull, "WriteUI");  } });

        RelativeLayoutHeader.addView(ImageViewWrite);

        RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1));
        ViewLineParam.addRule(RelativeLayout.BELOW, RelativeLayoutHeader.getId());

        View ViewLine = new View(GetActivity());
        ViewLine.setLayoutParams(ViewLineParam);
        ViewLine.setBackgroundResource(Misc.IsDark() ? R.color.LineDark : R.color.LineWhite);
        ViewLine.setId(Misc.ViewID());

        RelativeLayoutMain.addView(ViewLine);

        RelativeLayout.LayoutParams RecyclerViewMainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        RecyclerViewMainParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

        LinearLayoutManager LinearLayoutManagerMain = new LinearLayoutManager(GetActivity());

        RecyclerView RecyclerViewMain = new RecyclerView(GetActivity());
        RecyclerViewMain.setLayoutParams(RecyclerViewMainParam);
        RecyclerViewMain.setAdapter(Adapter = new PostAdapter(GetActivity(), "InboxUI"));
        RecyclerViewMain.setLayoutManager(LinearLayoutManagerMain);
        RecyclerViewMain.addOnScrollListener(new OnScrollRecyclerView(LinearLayoutManagerMain) { @Override public void OnLoadMore() { Adapter.Update(); } });
        RecyclerViewMain.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent e)
            {
                Adapter.OnTouchEvent(e);
                return false;
            }
        });

        RelativeLayoutMain.addView(RecyclerViewMain);

        ViewMain = RelativeLayoutMain;
    }

    @Override
    public void OnResume()
    {
        Misc.IsFullScreen(GetActivity(), true);

        if (Build.VERSION.SDK_INT > 20)
            GetActivity().getWindow().setStatusBarColor(Misc.Color(Misc.IsDark() ? R.color.StatusBarDark : R.color.StatusBarWhite));

        Adapter.Update();
    }

    @Override
    public void OnPause()
    {
        Misc.IsFullScreen(GetActivity(), false);
        AndroidNetworking.forceCancel("InboxUI");
    }

    void Update(JSONObject post)
    {
        Adapter.Update(post);
    }
}
