package co.biogram.main.ui.social;

import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import co.biogram.main.R;
import co.biogram.main.fragment.FragmentBase;
import co.biogram.main.handler.Misc;
import co.biogram.main.handler.PostAdapter;
import co.biogram.main.handler.OnScrollRecyclerView;
import co.biogram.main.handler.SharedHandler;
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
        ImageViewWrite.setImageResource(R.drawable.write_plus_blue);
        ImageViewWrite.setPadding(Misc.ToDP(GetActivity(), 13), Misc.ToDP(GetActivity(), 13), Misc.ToDP(GetActivity(), 13), Misc.ToDP(GetActivity(), 13));
        ImageViewWrite.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { GetActivity().GetManager().OpenView(new WriteUI(), R.id.ContainerFull, "WriteUI"); } });

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
        ImageViewBookmark.setImageResource(R.drawable.bookmark_blue);
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

        PostList.add(new PostAdapter.PostStruct(0));
        AdapterMain = new PostAdapter(GetActivity(), PostList, new PostAdapter.PullToRefreshListener()
        {
            @Override
            public void OnRefresh()
            {
                AdapterMain.SetRefreshComplete();
            }
        });

        RecyclerView RecyclerViewMain = new RecyclerView(GetActivity())
        {
            @Override
            public boolean onTouchEvent(MotionEvent e)
            {
                //AdapterMain.GetPullToRefreshView().onTouchEvent(e);
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
                UpdatePost();
            }
        });

        RelativeLayoutMain.addView(RecyclerViewMain);

        UpdatePost();

        ViewMain = RelativeLayoutMain;
    }

    @Override
    public void OnResume()
    {
        if (Build.VERSION.SDK_INT > 20)
            GetActivity().getWindow().setStatusBarColor(ContextCompat.getColor(GetActivity(), Misc.IsDark(GetActivity()) ? R.color.StatusBarDark : R.color.StatusBarWhite));
    }

    @Override
    public void OnPause()
    {
        AndroidNetworking.forceCancel("InboxUI");
    }

    private void UpdatePost()
    {
        AndroidNetworking.post(Misc.GetRandomServer("PostListInbox"))
        .addBodyParameter("Skip", String.valueOf(PostAdapter.Size(PostList)))
        .addHeaders("Token", SharedHandler.GetString(GetActivity(), "Token"))
        .setTag("InboxUI")
        .build()
        .getAsString(new StringRequestListener()
        {
            @Override
            public void onResponse(String Response)
            {
                Misc.Debug(Response);
                try
                {
                    JSONObject Result = new JSONObject(Response);

                    if (Result.getInt("Message") == 0)
                    {
                        JSONArray ResultList = new JSONArray(Result.getString("Result"));

                        for (int K = 0; K < ResultList.length(); K++)
                        {
                            JSONObject D = ResultList.getJSONObject(K);

                            PostAdapter.PostStruct P = new PostAdapter.PostStruct();
                            P.ID = D.getString("ID");

                            if (!D.isNull("Profile"))
                                P.Profile = D.getString("Profile");

                            P.Name = D.getString("Name");

                            if (!D.isNull("Medal"))
                                P.Medal = D.getString("Medal");

                            P.Username = D.getString("Username");
                            P.Time = D.getInt("Time");

                            if (!D.isNull("Message"))
                                P.Message = D.getString("Message");

                            if (D.getInt("Type") != 0)
                            {
                                P.DataType = D.getInt("Type");
                                P.Data = D.getString("Data");
                            }

                            P.Owner = D.getString("Owner");

                            if (!D.isNull("View"))
                                P.View = D.getInt("View");

                            P.Category = D.getInt("Category");
                            P.LikeCount = D.getInt("LikeCount");
                            P.CommentCount = D.getInt("CommentCount");
                            P.IsLike = D.getInt("Like") != 0;
                            P.IsFollow = D.getInt("Follow") != 0;

                            if (!D.isNull("Comment"))
                                P.IsComment = D.getInt("Comment") != 0;

                            P.IsBookmark = D.getInt("Bookmark") != 0;

                            PostList.add(P);
                        }

                        Collections.sort(PostList, new Comparator<PostAdapter.PostStruct>()
                        {
                            @Override
                            public int compare(PostAdapter.PostStruct P1, PostAdapter.PostStruct P2)
                            {
                                int V = 0;

                                if (P1.Time < P2.Time)
                                    V = 1;
                                else if (P1.Time > P2.Time)
                                    V = -1;

                                return V;
                            }
                        });

                        AdapterMain.notifyDataSetChanged();
                    }
                }
                catch (Exception e)
                {
                    Misc.Debug("InboxUI-UpdateList: " + e.toString());
                }
            }

            @Override
            public void onError(ANError e)
            {
                Misc.Debug("InboxUI-UpdateList: " + e.toString());
            }
        });
    }

    void Update(JSONObject D)
    {
        try
        {
            PostAdapter.PostStruct P = new PostAdapter.PostStruct();
            P.ID = D.getString("_id");
            P.Profile = SharedHandler.GetString(GetActivity(), "Avatar");
            P.Name = SharedHandler.GetString(GetActivity(), "Name");
            P.Medal = SharedHandler.GetString(GetActivity(), "Medal");
            P.Username = SharedHandler.GetString(GetActivity(), "Username");
            P.Time = D.getInt("Time");

            if (!D.isNull("Message"))
                P.Message = D.getString("Message");

            if (D.getInt("Type") != 0)
            {
                P.DataType = D.getInt("Type");
                P.Data = D.getString("Data");
            }

            P.Owner = D.getString("Owner");
            P.View = 0;
            P.Category = D.getInt("Category");
            P.LikeCount = 0;
            P.CommentCount = 0;
            P.IsLike = false;
            P.IsFollow = false;
            P.IsComment = false;
            P.IsBookmark = false;

            PostList.add(1, P);
        }
        catch (Exception e)
        {
            Misc.Debug("InboxUI-Update: " + e.toString());
        }
    }
}
