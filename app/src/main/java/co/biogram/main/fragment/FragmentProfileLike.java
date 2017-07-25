package co.biogram.main.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import co.biogram.main.R;
import co.biogram.main.handler.MiscHandler;
import co.biogram.main.handler.SharedHandler;
import co.biogram.main.misc.AdapterPost;
import co.biogram.main.misc.LoadingView;

public class FragmentProfileLike extends Fragment
{
    private RelativeLayout RelativeLayoutLoading;
    private LoadingView LoadingViewData;
    private TextView TextViewTry;
    private String Username;
    private AdapterPost Adapter;
    private boolean LoadingBottom = false;
    private final List<AdapterPost.Struct> PostList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        final Context context = getActivity();

        Username = SharedHandler.GetString(context, "Username");

        if (getArguments() != null && !getArguments().getString("Username", "").equals(""))
            Username = getArguments().getString("Username");

        RelativeLayout Root = new RelativeLayout(context);
        Root.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        Root.setBackgroundResource(R.color.White);

        final LinearLayoutManager LinearLayoutManagerLike = new LinearLayoutManager(context);

        RecyclerView RecyclerViewLike = new RecyclerView(context);
        RecyclerViewLike.setLayoutParams(new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.MATCH_PARENT));
        RecyclerViewLike.setLayoutManager(LinearLayoutManagerLike);
        RecyclerViewLike.setNestedScrollingEnabled(false);
        RecyclerViewLike.setAdapter(Adapter = new AdapterPost(getActivity(), PostList, "FragmentProfileLike"));
        RecyclerViewLike.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView View, int DX, int DY)
            {
                if (!LoadingBottom && (LinearLayoutManagerLike.findLastVisibleItemPosition() + 5) > LinearLayoutManagerLike.getItemCount())
                {
                    LoadingBottom = true;
                    PostList.add(null);
                    Adapter.notifyItemInserted(PostList.size());

                    AndroidNetworking.post(MiscHandler.GetRandomServer("ProfileLikeGet"))
                    .addBodyParameter("Skip", String.valueOf(PostList.size()))
                    .addBodyParameter("Username", Username)
                    .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
                    .setTag("FragmentProfileLike")
                    .build().getAsString(new StringRequestListener()
                    {
                        @Override
                        public void onResponse(String Response)
                        {
                            LoadingBottom = false;
                            PostList.remove(PostList.size() - 1);
                            Adapter.notifyItemRemoved(PostList.size());

                            try
                            {
                                JSONObject Result = new JSONObject(Response);

                                if (Result.getInt("Message") == 1000 && !Result.getString("Result").equals(""))
                                {
                                    JSONArray ResultList = new JSONArray(Result.getString("Result"));

                                    for (int K = 0; K < ResultList.length(); K++)
                                    {
                                        JSONObject Post = ResultList.getJSONObject(K);

                                        AdapterPost.Struct PostStruct = new AdapterPost.Struct();
                                        PostStruct.PostID = Post.getString("PostID");
                                        PostStruct.OwnerID = Post.getString("OwnerID");
                                        PostStruct.Type = Post.getInt("Type");
                                        PostStruct.Category = Post.getInt("Category");
                                        PostStruct.Time = Post.getInt("Time");
                                        PostStruct.Comment = Post.getBoolean("Comment");
                                        PostStruct.Message = Post.getString("Message");
                                        PostStruct.Data = Post.getString("Data");
                                        PostStruct.Username = Post.getString("Username");
                                        PostStruct.Avatar = Post.getString("Avatar");
                                        PostStruct.Like = Post.getBoolean("Like");
                                        PostStruct.LikeCount = Post.getInt("LikeCount");
                                        PostStruct.CommentCount = Post.getInt("CommentCount");
                                        PostStruct.BookMark = Post.getBoolean("BookMark");
                                        PostStruct.Follow = Post.getBoolean("Follow");

                                        PostList.add(PostStruct);
                                    }

                                    Adapter.notifyDataSetChanged();
                                }
                            }
                            catch (Exception e)
                            {
                                // Leave Me Alone
                            }
                        }

                        @Override
                        public void onError(ANError anError)
                        {
                            LoadingBottom = false;
                            PostList.remove(PostList.size() - 1);
                            Adapter.notifyItemRemoved(PostList.size());
                        }
                    });
                }
            }
        });

        Root.addView(RecyclerViewLike);

        RelativeLayoutLoading = new RelativeLayout(context);
        RelativeLayoutLoading.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayoutLoading.setBackgroundResource(R.color.White);

        Root.addView(RelativeLayoutLoading);

        RelativeLayout.LayoutParams LoadingViewDataParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56));
        LoadingViewDataParam.addRule(RelativeLayout.CENTER_HORIZONTAL);
        LoadingViewDataParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);

        LoadingViewData = new LoadingView(context);
        LoadingViewData.setLayoutParams(LoadingViewDataParam);

        RelativeLayoutLoading.addView(LoadingViewData);

        RelativeLayout.LayoutParams TextViewTryParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTryParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        TextViewTry = new TextView(context);
        TextViewTry.setLayoutParams(TextViewTryParam);
        TextViewTry.setText(getString(R.string.TryAgain));
        TextViewTry.setTextColor(ContextCompat.getColor(context, R.color.BlueGray2));
        TextViewTry.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        TextViewTry.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { RetrieveDataFromServer(context); } });

        RelativeLayoutLoading.addView(TextViewTry);

        RetrieveDataFromServer(context);

        return Root;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        AndroidNetworking.cancel("FragmentProfileLike");
    }

    private void RetrieveDataFromServer(final Context context)
    {
        TextViewTry.setVisibility(View.GONE);
        LoadingViewData.Start();

        AndroidNetworking.post(MiscHandler.GetRandomServer("ProfileLikeGet"))
        .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
        .addBodyParameter("Username", Username)
        .setTag("FragmentProfileLike")
        .build().getAsString(new StringRequestListener()
        {
            @Override
            public void onResponse(String Response)
            {
                try
                {
                    JSONObject Result = new JSONObject(Response);

                    if (Result.getInt("Message") == 1000 && !Result.getString("Result").equals(""))
                    {
                        JSONArray postList = new JSONArray(Result.getString("Result"));

                        for (int K = 0; K < postList.length(); K++)
                        {
                            JSONObject Post = postList.getJSONObject(K);

                            AdapterPost.Struct PostStruct = new AdapterPost.Struct();
                            PostStruct.PostID = Post.getString("PostID");
                            PostStruct.OwnerID = Post.getString("OwnerID");
                            PostStruct.Type = Post.getInt("Type");
                            PostStruct.Category = Post.getInt("Category");
                            PostStruct.Time = Post.getInt("Time");
                            PostStruct.Comment = Post.getBoolean("Comment");
                            PostStruct.Message = Post.getString("Message");
                            PostStruct.Data = Post.getString("Data");
                            PostStruct.Username = Post.getString("Username");
                            PostStruct.Avatar = Post.getString("Avatar");
                            PostStruct.Like = Post.getBoolean("Like");
                            PostStruct.LikeCount = Post.getInt("LikeCount");
                            PostStruct.CommentCount = Post.getInt("CommentCount");
                            PostStruct.BookMark = Post.getBoolean("BookMark");
                            PostStruct.Follow = Post.getBoolean("Follow");

                            PostList.add(PostStruct);
                        }

                        Adapter.notifyDataSetChanged();
                    }
                }
                catch (Exception e)
                {
                    // Leave Me Alone
                }

                LoadingViewData.Stop();
                TextViewTry.setVisibility(View.GONE);
                RelativeLayoutLoading.setVisibility(View.GONE);
            }

            @Override
            public void onError(ANError anError)
            {
                MiscHandler.Toast(context, getString(R.string.NoInternet));
                TextViewTry.setVisibility(View.VISIBLE);
                LoadingViewData.Stop();
            }
        });
    }
}
