package co.biogram.main.ui.social;

import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import co.biogram.main.R;
import co.biogram.main.fragment.FragmentBase;
import co.biogram.main.handler.Misc;
import co.biogram.main.handler.PostAdapter;
import co.biogram.main.handler.OnScrollRecyclerView;
import co.biogram.main.ui.view.TextView;

public class InboxUI extends FragmentBase
{
    private final List<PostAdapter.PostStruct> PostList = new ArrayList<>();
    private PostAdapter AdapterMain;

    @Override
    public void OnCreate()
    {
        RelativeLayout RelativeLayoutMain = new RelativeLayout(GetActivity());
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayoutMain.setBackgroundResource(Misc.IsDark(GetActivity()) ? R.color.GroundDark : R.color.GroundWhite);
        RelativeLayoutMain.setClickable(true);

        RelativeLayout RelativeLayoutHeader = new RelativeLayout(GetActivity());
        RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(GetActivity(), 56)));
        RelativeLayoutHeader.setBackgroundResource(Misc.IsDark(GetActivity()) ? R.color.ActionBarDark : R.color.ActionBarWhite);
        RelativeLayoutHeader.setId(Misc.GenerateViewID());

        RelativeLayoutMain.addView(RelativeLayoutHeader);

        RelativeLayout.LayoutParams ImageViewAddParam = new RelativeLayout.LayoutParams(Misc.ToDP(GetActivity(), 56), Misc.ToDP(GetActivity(), 56));
        ImageViewAddParam.addRule(Misc.Align("R"));

        ImageView ImageViewWrite = new ImageView(GetActivity());
        ImageViewWrite.setLayoutParams(ImageViewAddParam);
        ImageViewWrite.setScaleType(ImageView.ScaleType.FIT_XY);
        ImageViewWrite.setId(Misc.GenerateViewID());
        ImageViewWrite.setImageResource(R.drawable.ic_write_plus);
        ImageViewWrite.setPadding(Misc.ToDP(GetActivity(), 13), Misc.ToDP(GetActivity(), 13), Misc.ToDP(GetActivity(), 13), Misc.ToDP(GetActivity(), 13));
        ImageViewWrite.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { GetActivity().GetManager().OpenView(new WriteUI(), R.id.SocialActivityContainerFull, "WriteUI"); } });

        RelativeLayoutHeader.addView(ImageViewWrite);

        RelativeLayout.LayoutParams TextViewTitleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTitleParam.addRule(Misc.AlignTo("R"), ImageViewWrite.getId());
        TextViewTitleParam.addRule(RelativeLayout.CENTER_VERTICAL);

        TextView TextViewTitle = new TextView(GetActivity(), 16, true);
        TextViewTitle.setLayoutParams(TextViewTitleParam);
        TextViewTitle.setTextColor(ContextCompat.getColor(GetActivity(), Misc.IsDark(GetActivity()) ? R.color.TextDark : R.color.TextWhite));
        TextViewTitle.setPadding(0, Misc.ToDP(GetActivity(), 6), 0, 0);
        TextViewTitle.setText(GetActivity().getString(R.string.InboxUI));

        RelativeLayoutHeader.addView(TextViewTitle);

        RelativeLayout.LayoutParams ImageViewSearchParam = new RelativeLayout.LayoutParams(Misc.ToDP(GetActivity(), 56), Misc.ToDP(GetActivity(), 56));
        ImageViewSearchParam.addRule(Misc.Align("L"));

        ImageView ImageViewSearch = new ImageView(GetActivity());
        ImageViewSearch.setLayoutParams(ImageViewSearchParam);
        ImageViewSearch.setScaleType(ImageView.ScaleType.FIT_XY);
        ImageViewSearch.setId(Misc.GenerateViewID());
        ImageViewSearch.setImageResource(R.drawable.ic_search_blue);
        ImageViewSearch.setPadding(Misc.ToDP(GetActivity(), 15), Misc.ToDP(GetActivity(), 15), Misc.ToDP(GetActivity(), 15), Misc.ToDP(GetActivity(), 15));
        ImageViewSearch.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) {  } });

        RelativeLayoutHeader.addView(ImageViewSearch);

        RelativeLayout.LayoutParams ImageViewBookmarkParam = new RelativeLayout.LayoutParams(Misc.ToDP(GetActivity(), 56), Misc.ToDP(GetActivity(), 56));
        ImageViewBookmarkParam.addRule(Misc.AlignTo("L"), ImageViewSearch.getId());

        ImageView ImageViewBookmark = new ImageView(GetActivity());
        ImageViewBookmark.setLayoutParams(ImageViewBookmarkParam);
        ImageViewBookmark.setScaleType(ImageView.ScaleType.FIT_XY);
        ImageViewBookmark.setId(Misc.GenerateViewID());
        ImageViewBookmark.setImageResource(R.drawable.ic_bookmark_blue);
        ImageViewBookmark.setPadding(Misc.ToDP(GetActivity(), 16), Misc.ToDP(GetActivity(), 16), Misc.ToDP(GetActivity(), 16), Misc.ToDP(GetActivity(), 16));
        ImageViewBookmark.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) {  } });

        RelativeLayoutHeader.addView(ImageViewBookmark);

        RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(GetActivity(), 1));
        ViewLineParam.addRule(RelativeLayout.BELOW, RelativeLayoutHeader.getId());

        View ViewLine = new View(GetActivity());
        ViewLine.setLayoutParams(ViewLineParam);
        ViewLine.setBackgroundResource(Misc.IsDark(GetActivity()) ? R.color.LineDark : R.color.LineWhite);
        ViewLine.setId(Misc.GenerateViewID());

        RelativeLayoutMain.addView(ViewLine);

        RelativeLayout.LayoutParams RecyclerViewMainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        RecyclerViewMainParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

        LinearLayoutManager LinearLayoutManagerMain = new LinearLayoutManager(GetActivity());

        String I1 = "[ \"http://www.sample-videos.com/img/Sample-jpg-image-50kb.jpg\" ]";
        String I2 = "[ \"http://www.sample-videos.com/img/Sample-png-image-100kb.png\", \"http://www.sample-videos.com/img/Sample-jpg-image-50kb.jpg\" ]";
        String I3 = "[ \"http://www.sample-videos.com/img/Sample-jpg-image-50kb.jpg\", \"http://www.sample-videos.com/img/Sample-png-image-100kb.png\", \"http://www.sample-videos.com/img/Sample-jpg-image-50kb.jpg\" ]";
        String I4 = "[ \"http://www.sample-videos.com/img/Sample-png-image-100kb.png\", \"152\", \"http://www.sample-videos.com/video/mp4/240/big_buck_bunny_240p_1mb.mp4\" ]";
        String I6 = "{ \"File\": \"http://www.sample-videos.com/img/Sample-png-image-100kb.png\", \"Name\" : \"ali_pdf.zip\", \"Detail\": \"13MB / ZIP\" }";
        String I5 = "{ \"Vote\" : \"1\", \"Total\" : \"67\", \"V1\" : \"Ali Khobe ?\", \"V2\" : \"Ali Bade ?\" , \"V3\" : \"Ali Gav e ?\", \"V1V\" : \"22\", \"V2V\" : \"34\" , \"V3V\" : \"11\" }";

        PostList.add(new PostAdapter.PostStruct(0));
        PostList.add(new PostAdapter.PostStruct(2));
        PostList.add(new PostAdapter.PostStruct("", "Ali Khazaee", "https://image.flaticon.com/icons/png/128/310/310831.png", "@mohammad", 1512525803, "Post e Jadid e Man\n #NewPost\n#GoodLuck", 1, I1, true, 100, true, 341));
        PostList.add(new PostAdapter.PostStruct("https://image.flaticon.com/icons/png/128/310/310831.png", "Ali Khazaee", "https://image.flaticon.com/icons/png/128/310/310831.png", "@ali", 1512325803, "پوست جدید من \n #NewPost\n#GoodLuck", 1,I2, false, 521, true, 18));
        PostList.add(new PostAdapter.PostStruct("", "Ali Khazaee", "https://image.flaticon.com/icons/png/128/310/310831.png", "@alireza", 1512125803, "  \n #News \n #Good پوست جدید من", 1, I3, false, 521, false, 18));
        PostList.add(new PostAdapter.PostStruct("", "Ali Khazaee", "", "@alireza", 1511525803, "با سلام\nدوستان گلم #Ali هشتگ #علی سلام", 2, I4, false, 231, true, 412));
        PostList.add(new PostAdapter.PostStruct("http://www.sample-videos.com/img/Sample-jpg-image-50kb.jpg", "File Am", "https://image.flaticon.com/icons/png/128/310/310831.png", "@manfileam", 1511525803, "#File_To_File", 4, I6, false, 6812, true, 8541));
        PostList.add(new PostAdapter.PostStruct("", "Ali Khazaee", "https://image.flaticon.com/icons/png/128/310/310831.png", "@alireza", 1511525803, "با سلام\nدوستان گلم #Ali هشتگ #علی سلام", 3, I5, false, 231, true, 412));

        AdapterMain = new PostAdapter(GetActivity(), PostList, new PostAdapter.PullToRefreshListener()
        {
            @Override
            public void OnRefresh()
            {
                PostList.add(new PostAdapter.PostStruct(2));
                AdapterMain.SetRefreshComplete();
            }
        });

        RecyclerView RecyclerViewMain = new RecyclerView(GetActivity())
        {
            @Override
            public boolean onTouchEvent(MotionEvent e)
            {
                AdapterMain.GetPullToRefreshView().onTouchEvent(e);
                return super.onTouchEvent(e);
            }
        };
        RecyclerViewMain.setLayoutParams(RecyclerViewMainParam);
        RecyclerViewMain.setAdapter(AdapterMain);
        RecyclerViewMain.setLayoutManager(LinearLayoutManagerMain);
        RecyclerViewMain.addOnScrollListener(new OnScrollRecyclerView(LinearLayoutManagerMain)
        {
            @Override
            public void OnLoadMore()
            {
                Misc.Debug("OnLoadMore Called");
            }
        });

        RelativeLayoutMain.addView(RecyclerViewMain);

        ViewMain = RelativeLayoutMain;
    }

    @Override
    public void OnResume()
    {
        if (Build.VERSION.SDK_INT > 20)
            GetActivity().getWindow().setStatusBarColor(ContextCompat.getColor(GetActivity(), Misc.IsDark(GetActivity()) ? R.color.StatusBarDark : R.color.StatusBarWhite));
    }
}
