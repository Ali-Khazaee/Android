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
import co.biogram.main.handler.RecyclerViewOnScroll;
import co.biogram.main.handler.PostAdapter;
import co.biogram.main.ui.view.TextView;

public class InboxUI extends FragmentView {
    private PostAdapter Adapter;

    @Override
    public void OnCreate() {
        RelativeLayout RelativeLayoutMain = new RelativeLayout(Activity);
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayoutMain.setBackgroundResource(Misc.IsDark() ? R.color.GroundDark : R.color.GroundWhite);
        RelativeLayoutMain.setClickable(true);

        RelativeLayout RelativeLayoutHeader = new RelativeLayout(Activity);
        RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56)));
        RelativeLayoutHeader.setBackgroundResource(Misc.IsDark() ? R.color.ActionBarDark : R.color.ActionBarWhite);
        RelativeLayoutHeader.setId(Misc.generateViewId());

        RelativeLayoutMain.addView(RelativeLayoutHeader);

        RelativeLayout.LayoutParams TextViewTitleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTitleParam.setMargins(Misc.ToDP(15), 0, Misc.ToDP(15), 0);
        TextViewTitleParam.addRule(RelativeLayout.CENTER_VERTICAL);
        TextViewTitleParam.addRule(Misc.Align("R"));

        TextView TextViewTitle = new TextView(Activity, 16, true);
        TextViewTitle.setLayoutParams(TextViewTitleParam);
        TextViewTitle.SetColor(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite);
        TextViewTitle.setPadding(0, Misc.ToDP(6), 0, 0);
        TextViewTitle.setText(Misc.String(R.string.InboxUI));

        RelativeLayoutHeader.addView(TextViewTitle);

        RelativeLayout.LayoutParams ImageViewSearchParam = new RelativeLayout.LayoutParams(Misc.ToDP(56), Misc.ToDP(56));
        ImageViewSearchParam.addRule(Misc.Align("L"));

        ImageView ImageViewSearch = new ImageView(Activity);
        ImageViewSearch.setLayoutParams(ImageViewSearchParam);
        ImageViewSearch.setId(Misc.generateViewId());
        ImageViewSearch.setImageResource(R.drawable._inbox_search);
        ImageViewSearch.setPadding(Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15));
        ImageViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { /* TODO Search */ }
        });

        RelativeLayoutHeader.addView(ImageViewSearch);

        RelativeLayout.LayoutParams ImageViewWriteParam = new RelativeLayout.LayoutParams(Misc.ToDP(56), Misc.ToDP(56));
        ImageViewWriteParam.addRule(Misc.AlignTo("L"), ImageViewSearch.getId());

        ImageView ImageViewWrite = new ImageView(Activity);
        ImageViewWrite.setLayoutParams(ImageViewWriteParam);
        ImageViewWrite.setImageResource(R.drawable._inbox_write);
        ImageViewWrite.setPadding(Misc.ToDP(5), Misc.ToDP(5), Misc.ToDP(5), Misc.ToDP(5));
        ImageViewWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity.GetManager().OpenView(new WriteUI(), "WriteUI", true);
            }
        });

        RelativeLayoutHeader.addView(ImageViewWrite);

        RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1));
        ViewLineParam.addRule(RelativeLayout.BELOW, RelativeLayoutHeader.getId());

        View ViewLine = new View(Activity);
        ViewLine.setLayoutParams(ViewLineParam);
        ViewLine.setBackgroundResource(Misc.IsDark() ? R.color.LineDark : R.color.LineWhite);
        ViewLine.setId(Misc.generateViewId());

        RelativeLayoutMain.addView(ViewLine);

        RelativeLayout.LayoutParams RecyclerViewMainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        RecyclerViewMainParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

        LinearLayoutManager LinearLayoutManagerMain = new LinearLayoutManager(Activity) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        };

        RecyclerView RecyclerViewMain = new RecyclerView(Activity);
        RecyclerViewMain.setLayoutParams(RecyclerViewMainParam);
        RecyclerViewMain.setAdapter(Adapter = new PostAdapter(Activity, "InboxUI"));
        RecyclerViewMain.setLayoutManager(LinearLayoutManagerMain);
        RecyclerViewMain.addOnScrollListener(new RecyclerViewOnScroll() {
            @Override
            public void OnLoadMore() {
                Adapter.Update();
            }
        });
        RecyclerViewMain.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent e) {
                Adapter.OnTouchEvent(e);
                return false;
            }
        });

        RelativeLayoutMain.addView(RecyclerViewMain);

        ViewMain = RelativeLayoutMain;
    }

    @Override
    public void OnResume() {
        Misc.IsFullScreen(Activity, true);

        if (Build.VERSION.SDK_INT > 20)
            Activity.getWindow().setStatusBarColor(Misc.Color(Misc.IsDark() ? R.color.StatusBarDark : R.color.StatusBarWhite));

        Adapter.Update();
    }

    @Override
    public void OnPause() {
        Misc.IsFullScreen(Activity, false);
        AndroidNetworking.forceCancel("InboxUI");
    }

    void Update(JSONObject post) {
        Adapter.Insert(post);
    }
}
