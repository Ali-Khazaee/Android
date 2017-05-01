package co.biogram.main.fragment;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
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
import co.biogram.main.handler.DataBaseHandler;
import co.biogram.main.handler.SharedHandler;
import co.biogram.main.handler.URLHandler;
import co.biogram.main.misc.PostAdapter;
import co.biogram.main.misc.LoadingView;

public class FragmentMoment extends Fragment
{
    private TextView TextViewTryAgain;
    private LoadingView LoadingViewMoment;
    private RelativeLayout RelativeLayoutLoading;

    private boolean IsTop = false;
    private boolean IsBottom = false;
    private boolean IsRunning = false;
    private PostAdapter MomentAdapter;
    private List<PostAdapter.Struct> MomentList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View RootView = inflater.inflate(R.layout.fragment_moment, container, false);

        TextViewTryAgain = (TextView) RootView.findViewById(R.id.TextViewTryAgain);
        TextViewTryAgain.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { RequestDataFromServer(); } });
        LoadingViewMoment = (LoadingView) RootView.findViewById(R.id.AnimationLoadingMoment);
        RelativeLayoutLoading = (RelativeLayout) RootView.findViewById(R.id.RelativeLayoutLoading);

        final ImageView ImageViewWrite = (ImageView) RootView.findViewById(R.id.ImageViewWrite);
        ImageViewWrite.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.FullScreenContainer, new FragmentMomentWrite(), "FragmentMomentWrite").addToBackStack("FragmentMomentWrite").commit();
            }
        });

        RecyclerView RecyclerViewMoment = (RecyclerView) RootView.findViewById(R.id.RecyclerViewMoment);
        MomentAdapter = new PostAdapter((AppCompatActivity) getActivity(), MomentList, "FragmentMoment");

        RecyclerViewMoment.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerViewMoment.setAdapter(MomentAdapter);
        RecyclerViewMoment.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView View, int DX, int DY)
            {
                if (!IsRunning)
                {
                    IsRunning = true;

                    if (DY > 0)
                        ImageViewWrite.animate().setDuration(400).translationY(ImageViewWrite.getHeight() + 150).withEndAction(new Runnable() { @Override public void run() { IsRunning = false; } }).setInterpolator(new AccelerateInterpolator(2)).start();
                    else
                        ImageViewWrite.animate().setDuration(400).translationY(0).withEndAction(new Runnable() { @Override public void run() { IsRunning = false; } }).setInterpolator(new DecelerateInterpolator(2)).start();
                }

                if (DY <= 0)
                    return;

                if ((((LinearLayoutManager) View.getLayoutManager()).findLastVisibleItemPosition() + 2) > View.getAdapter().getItemCount() && !IsBottom)
                {
                    IsBottom = true;
                    MomentList.add(null);
                    MomentAdapter.notifyItemInserted(MomentList.size());

                    AndroidNetworking.post(URLHandler.GetURL(URLHandler.URL.POST_LIST))
                    .addBodyParameter("Skip", String.valueOf(MomentList.size()))
                    .addHeaders("TOKEN", SharedHandler.GetString("TOKEN"))
                    .setTag("FragmentMoment").build().getAsString(new StringRequestListener()
                    {
                        @Override
                        public void onResponse(String Response)
                        {
                            MomentList.remove(MomentList.size() - 1);
                            MomentAdapter.notifyItemRemoved(MomentList.size());
                            IsBottom = false;

                            try
                            {
                                JSONObject Result = new JSONObject(Response);

                                if (Result.getInt("Message") == 1000 && !Result.getString("Result").equals(""))
                                {
                                    JSONArray PostList = new JSONArray(Result.getString("Result"));

                                    for (int K = 0; K < PostList.length(); K++)
                                    {
                                        JSONObject Post = PostList.getJSONObject(K);
                                        MomentList.add(new PostAdapter.Struct(Post.getString("PostID"), Post.getString("OwnerID"), Post.getInt("Type"), Post.getInt("Category"), Post.getLong("Time"), Post.getBoolean("Comment"), Post.getString("Message"), Post.getString("Data"), Post.getString("Username"), Post.getString("Avatar"), Post.getBoolean("Like"), Post.getInt("LikeCount"), Post.getInt("CommentCount"), Post.getBoolean("BookMark")));

                                        ContentValues Value = new ContentValues();
                                        Value.put("PostID", Post.getString("PostID"));
                                        Value.put("OwnerID", Post.getString("OwnerID"));
                                        Value.put("Type", Post.getInt("Type"));
                                        Value.put("Category", Post.getInt("Category"));
                                        Value.put("Time", Post.getLong("Time"));
                                        Value.put("Comment", Post.getBoolean("Comment"));
                                        Value.put("Message", Post.getString("Message"));
                                        Value.put("Data", Post.getString("Data"));
                                        Value.put("Username", Post.getString("Username"));
                                        Value.put("Avatar", Post.getString("Avatar"));
                                        Value.put("Like", Post.getBoolean("Like"));
                                        Value.put("LikeCount", Post.getInt("LikeCount"));
                                        Value.put("CommentCount", Post.getInt("CommentCount"));
                                        Value.put("BookMark", Post.getBoolean("BookMark"));

                                        DataBaseHandler.AddOrUpdate("POST", new String[] { "PostID" }, "PostID = ?", new String[] { Post.getString("PostID") }, Value);
                                    }

                                    MomentAdapter.notifyDataSetChanged();
                                }
                            }
                            catch (Exception e)
                            {
                                // Leave Me Alone
                            }
                        }
                        @Override
                        public void onError(ANError e)
                        {
                            MomentList.remove(MomentList.size() - 1);
                            MomentAdapter.notifyItemRemoved(MomentList.size());
                            IsBottom = false;
                        }
                    });
                }
            }
        });

        final SwipeRefreshLayout SwipeRefreshLayoutMoment = (SwipeRefreshLayout) RootView.findViewById(R.id.SwipeRefreshLayoutMoment);
        SwipeRefreshLayoutMoment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                if (IsTop)
                    return;

                IsTop = true;
                SwipeRefreshLayoutMoment.setEnabled(false);
                SwipeRefreshLayoutMoment.setRefreshing(false);

                if (MomentList.size() <= 0)
                {
                    SwipeRefreshLayoutMoment.setEnabled(true);
                    return;
                }

                AndroidNetworking.post(URLHandler.GetURL(URLHandler.URL.POST_LIST))
                .addBodyParameter("Time", String.valueOf(MomentList.get(0).Time))
                .addHeaders("TOKEN", SharedHandler.GetString("TOKEN"))
                .setTag("FragmentMoment").build().getAsString(new StringRequestListener()
                {
                    @Override
                    public void onResponse(String Response)
                    {
                        try
                        {
                            JSONObject Result = new JSONObject(Response);

                            if (Result.getInt("Message") == 1000 && !Result.getString("Result").equals(""))
                            {
                                JSONArray PostList = new JSONArray(Result.getString("Result"));

                                for (int K = 0; K < PostList.length(); K++)
                                {
                                    JSONObject Post = PostList.getJSONObject(K);
                                    MomentList.add(0, new PostAdapter.Struct(Post.getString("PostID"), Post.getString("OwnerID"), Post.getInt("Type"), Post.getInt("Category"), Post.getLong("Time"), Post.getBoolean("Comment"), Post.getString("Message"), Post.getString("Data"), Post.getString("Username"), Post.getString("Avatar"), Post.getBoolean("Like"), Post.getInt("LikeCount"), Post.getInt("CommentCount"), Post.getBoolean("BookMark")));

                                    DataBaseHandler.Remove("POST", null, null);

                                    ContentValues Value = new ContentValues();
                                    Value.put("PostID", Post.getString("PostID"));
                                    Value.put("OwnerID", Post.getString("OwnerID"));
                                    Value.put("Type", Post.getInt("Type"));
                                    Value.put("Category", Post.getInt("Category"));
                                    Value.put("Time", Post.getLong("Time"));
                                    Value.put("Comment", Post.getBoolean("Comment"));
                                    Value.put("Message", Post.getString("Message"));
                                    Value.put("Data", Post.getString("Data"));
                                    Value.put("Username", Post.getString("Username"));
                                    Value.put("Avatar", Post.getString("Avatar"));
                                    Value.put("Like", Post.getBoolean("Like"));
                                    Value.put("LikeCount", Post.getInt("LikeCount"));
                                    Value.put("CommentCount", Post.getInt("CommentCount"));
                                    Value.put("BookMark", Post.getBoolean("BookMark"));

                                    DataBaseHandler.AddOrUpdate("POST", new String[] { "PostID" }, "PostID = ?", new String[] { Post.getString("PostID") }, Value);
                                }

                                MomentAdapter.notifyDataSetChanged();
                            }
                        }
                        catch (Exception e)
                        {
                            // Leave Me Alone
                        }

                        IsTop = false;
                        SwipeRefreshLayoutMoment.setEnabled(true);
                    }

                    @Override
                    public void onError(ANError error)
                    {
                        IsTop = false;
                        SwipeRefreshLayoutMoment.setEnabled(true);
                    }
                });
            }
        });

        RequestDataFromServer();

        return RootView;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        AndroidNetworking.cancel("FragmentMoment");
    }

    private void RequestDataFromServer()
    {
        TextViewTryAgain.setVisibility(View.GONE);
        LoadingViewMoment.Start();
        RelativeLayoutLoading.setVisibility(View.VISIBLE);

        AndroidNetworking.post(URLHandler.GetURL(URLHandler.URL.POST_LIST))
        .addHeaders("TOKEN", SharedHandler.GetString("TOKEN"))
        .setTag("FragmentMoment").build().getAsString(new StringRequestListener()
        {
            @Override
            public void onResponse(String Response)
            {
                try
                {
                    JSONObject Result = new JSONObject(Response);

                    if (Result.getInt("Message") == 1000 && !Result.getString("Result").equals(""))
                    {
                        JSONArray PostList = new JSONArray(Result.getString("Result"));

                        for (int K = 0; K < PostList.length(); K++)
                        {
                            JSONObject Post = PostList.getJSONObject(K);
                            MomentList.add(new PostAdapter.Struct(Post.getString("PostID"), Post.getString("OwnerID"), Post.getInt("Type"), Post.getInt("Category"), Post.getLong("Time"), Post.getBoolean("Comment"), Post.getString("Message"), Post.getString("Data"), Post.getString("Username"), Post.getString("Avatar"), Post.getBoolean("Like"), Post.getInt("LikeCount"), Post.getInt("CommentCount"), Post.getBoolean("BookMark")));

                            ContentValues Value = new ContentValues();
                            Value.put("PostID", Post.getString("PostID"));
                            Value.put("OwnerID", Post.getString("OwnerID"));
                            Value.put("Type", Post.getInt("Type"));
                            Value.put("Category", Post.getInt("Category"));
                            Value.put("Time", Post.getLong("Time"));
                            Value.put("Comment", Post.getBoolean("Comment"));
                            Value.put("Message", Post.getString("Message"));
                            Value.put("Data", Post.getString("Data"));
                            Value.put("Username", Post.getString("Username"));
                            Value.put("Avatar", Post.getString("Avatar"));
                            Value.put("Like", Post.getBoolean("Like"));
                            Value.put("LikeCount", Post.getInt("LikeCount"));
                            Value.put("CommentCount", Post.getInt("CommentCount"));
                            Value.put("BookMark", Post.getBoolean("BookMark"));

                            DataBaseHandler.AddOrUpdate("POST", new String[] { "PostID" }, "PostID = ?", new String[] { Post.getString("PostID") }, Value);
                        }

                        MomentAdapter.notifyDataSetChanged();
                    }
                }
                catch (Exception e)
                {
                    // Leave Me Alone
                }

                TextViewTryAgain.setVisibility(View.GONE);
                LoadingViewMoment.Stop();
                RelativeLayoutLoading.setVisibility(View.GONE);
            }

            @Override
            public void onError(ANError e)
            {
                Cursor cursor = DataBaseHandler.Find("POST", null, null, null, "`Time` DESC", null);

                if (cursor.moveToFirst())
                {
                    do
                    {
                        PostAdapter.Struct Post = new PostAdapter.Struct();
                        Post.PostID = cursor.getString(cursor.getColumnIndex("PostID"));
                        Post.OwnerID = cursor.getString(cursor.getColumnIndex("OwnerID"));
                        Post.Type = cursor.getInt(cursor.getColumnIndex("Type"));
                        Post.Category = cursor.getInt(cursor.getColumnIndex("Category"));
                        Post.Time = cursor.getLong(cursor.getColumnIndex("Time"));
                        Post.Comment = cursor.getInt(cursor.getColumnIndex("Comment")) > 0;
                        Post.Message = cursor.getString(cursor.getColumnIndex("Message"));
                        Post.Data = cursor.getString(cursor.getColumnIndex("Data"));
                        Post.Username = cursor.getString(cursor.getColumnIndex("Username"));
                        Post.Avatar = cursor.getString(cursor.getColumnIndex("Avatar"));
                        Post.Like = cursor.getInt(cursor.getColumnIndex("Like")) > 0;
                        Post.LikeCount = cursor.getInt(cursor.getColumnIndex("LikeCount"));
                        Post.CommentCount = cursor.getInt(cursor.getColumnIndex("CommentCount"));
                        Post.BookMark = cursor.getInt(cursor.getColumnIndex("BookMark")) > 0;

                        MomentList.add(Post);
                    }
                    while (cursor.moveToNext());
                }

                cursor.close();

                MomentAdapter.notifyDataSetChanged();
                TextViewTryAgain.setVisibility(View.GONE);
                LoadingViewMoment.Stop();
                RelativeLayoutLoading.setVisibility(View.GONE);
            }
        });
    }
}
