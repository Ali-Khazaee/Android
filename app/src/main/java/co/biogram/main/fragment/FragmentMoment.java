package co.biogram.main.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import co.biogram.main.R;
import co.biogram.main.handler.MiscHandler;
import co.biogram.main.handler.RequestHandler;
import co.biogram.main.handler.SharedHandler;
import co.biogram.main.handler.URLHandler;
import co.biogram.main.misc.AdapterPost;
import co.biogram.main.misc.LoadingView;

public class FragmentMoment extends Fragment
{
    private RelativeLayout RelativeLayoutLoading;
    private LoadingView LoadingViewData;
    private TextView TextViewTry;

    private AdapterPost PostAdapter;
    private boolean IsRunning = false;
    private boolean LoadingTop = false;
    private boolean LoadingBottom = false;
    private List<AdapterPost.PostStruct> PostList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final Context context = getActivity();

        RelativeLayout Root = new RelativeLayout(context);
        Root.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        Root.setBackgroundResource(R.color.White);

        RelativeLayout RelativeLayoutHeader = new RelativeLayout(context);
        RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56)));
        RelativeLayoutHeader.setBackgroundResource(R.color.White5);
        RelativeLayoutHeader.setId(MiscHandler.GenerateViewID());

        Root.addView(RelativeLayoutHeader);

        RelativeLayout.LayoutParams TextViewTitleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTitleParam.addRule(RelativeLayout.CENTER_VERTICAL);
        TextViewTitleParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        TextViewTitleParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));

        TextView TextViewTitle = new TextView(context);
        TextViewTitle.setLayoutParams(TextViewTitleParam);
        TextViewTitle.setText(getString(R.string.FragmentMoment));
        TextViewTitle.setTextColor(ContextCompat.getColor(context, R.color.Black));
        TextViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        TextViewTitle.setTypeface(null, Typeface.BOLD);

        RelativeLayoutHeader.addView(TextViewTitle);

        RelativeLayout.LayoutParams ImageViewBookMarkParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), RelativeLayout.LayoutParams.MATCH_PARENT);
        ImageViewBookMarkParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        ImageView ImageViewBookMark = new ImageView(context);
        ImageViewBookMark.setLayoutParams(ImageViewBookMarkParam);
        ImageViewBookMark.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewBookMark.setPadding(MiscHandler.ToDimension(context, 16), MiscHandler.ToDimension(context, 16), MiscHandler.ToDimension(context, 16), MiscHandler.ToDimension(context, 16));
        ImageViewBookMark.setImageResource(R.drawable.ic_bookmark_blue);
        ImageViewBookMark.setId(MiscHandler.GenerateViewID());

        RelativeLayoutHeader.addView(ImageViewBookMark);

        RelativeLayout.LayoutParams ImageViewSearchParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), RelativeLayout.LayoutParams.MATCH_PARENT);
        ImageViewSearchParam.addRule(RelativeLayout.LEFT_OF, ImageViewBookMark.getId());

        ImageView ImageViewSearch = new ImageView(context);
        ImageViewSearch.setLayoutParams(ImageViewSearchParam);
        ImageViewSearch.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewSearch.setPadding(MiscHandler.ToDimension(context, 16), MiscHandler.ToDimension(context, 16), MiscHandler.ToDimension(context, 16), MiscHandler.ToDimension(context, 16));
        ImageViewSearch.setImageResource(R.drawable.ic_search_blue);

        RelativeLayoutHeader.addView(ImageViewSearch);

        RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1));
        ViewLineParam.addRule(RelativeLayout.BELOW, RelativeLayoutHeader.getId());

        View ViewLine = new View(context);
        ViewLine.setLayoutParams(ViewLineParam);
        ViewLine.setBackgroundResource(R.color.Gray2);
        ViewLine.setId(MiscHandler.GenerateViewID());

        Root.addView(ViewLine);

        RelativeLayout.LayoutParams SwipeRefreshLayoutMomentParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        SwipeRefreshLayoutMomentParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

        final SwipeRefreshLayout SwipeRefreshLayoutMoment = new SwipeRefreshLayout(context);
        SwipeRefreshLayoutMoment.setLayoutParams(SwipeRefreshLayoutMomentParam);
        SwipeRefreshLayoutMoment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                if (LoadingTop)
                    return;

                LoadingTop = true;
                SwipeRefreshLayoutMoment.setEnabled(false);
                SwipeRefreshLayoutMoment.setRefreshing(false);

                if (PostList.size() == 0)
                {
                    SwipeRefreshLayoutMoment.setEnabled(true);
                    return;
                }

                RequestHandler.Core().Method("POST")
                .Address(URLHandler.GetURL(URLHandler.URL.POST_LIST))
                .Param("Time", String.valueOf(PostList.get(0).Time))
                .Header("TOKEN", SharedHandler.GetString(context, "TOKEN"))
                .Tag("FragmentMoment")
                .Build(new RequestHandler.OnCompleteCallBack()
                {
                    @Override
                    public void OnFinish(String Response, int Status)
                    {
                        LoadingTop = false;
                        SwipeRefreshLayoutMoment.setEnabled(true);

                        if (Status != 200)
                        {
                            MiscHandler.Toast(context, getString(R.string.NoInternet));
                            return;
                        }

                        try
                        {
                            JSONObject Result = new JSONObject(Response);

                            if (Result.getInt("Message") == 1000 && !Result.getString("Result").equals(""))
                            {
                                JSONArray postList = new JSONArray(Result.getString("Result"));

                                for (int K = 0; K < postList.length(); K++)
                                {
                                    JSONObject Post = postList.getJSONObject(K);
                                    PostList.add(0, new AdapterPost.PostStruct(Post.getString("PostID"), Post.getString("OwnerID"), Post.getInt("Type"), Post.getInt("Category"), Post.getLong("Time"), Post.getBoolean("Comment"), Post.getString("Message"), Post.getString("Data"), Post.getString("Username"), Post.getString("Avatar"), Post.getBoolean("Like"), Post.getInt("LikeCount"), Post.getInt("CommentCount"), Post.getBoolean("BookMark")));
                                }

                                PostAdapter.notifyDataSetChanged();
                            }
                        }
                        catch (Exception e)
                        {
                            // Leave Me Alone
                        }
                    }
                });
            }
        });

        Root.addView(SwipeRefreshLayoutMoment);

        final ImageView ImageViewWrite = new ImageView(context);
        PostAdapter = new AdapterPost(getActivity(), PostList, "FragmentMoment");

        RecyclerView RecyclerViewMoment = new RecyclerView(context);
        RecyclerViewMoment.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RecyclerViewMoment.setLayoutManager(new LinearLayoutManager(context));
        RecyclerViewMoment.setAdapter(PostAdapter);
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

                if ((((LinearLayoutManager) View.getLayoutManager()).findLastVisibleItemPosition() + 2) > View.getAdapter().getItemCount() && !LoadingBottom)
                {
                    LoadingBottom = true;
                    PostList.add(null);
                    PostAdapter.notifyItemInserted(PostList.size());

                    RequestHandler.Core().Method("POST")
                    .Address(URLHandler.GetURL(URLHandler.URL.POST_LIST))
                    .Param("Skip", String.valueOf(PostList.size()))
                    .Header("TOKEN", SharedHandler.GetString(context, "TOKEN"))
                    .Tag("FragmentMoment")
                    .Build(new RequestHandler.OnCompleteCallBack()
                    {
                        @Override
                        public void OnFinish(String Response, int Status)
                        {
                            LoadingBottom = false;
                            PostList.remove(PostList.size() - 1);
                            PostAdapter.notifyItemRemoved(PostList.size());

                            try
                            {
                                JSONObject Result = new JSONObject(Response);

                                if (Result.getInt("Message") == 1000 && !Result.getString("Result").equals(""))
                                {
                                    JSONArray postList = new JSONArray(Result.getString("Result"));

                                    for (int K = 0; K < postList.length(); K++)
                                    {
                                        JSONObject Post = postList.getJSONObject(K);
                                        PostList.add(new AdapterPost.PostStruct(Post.getString("PostID"), Post.getString("OwnerID"), Post.getInt("Type"), Post.getInt("Category"), Post.getLong("Time"), Post.getBoolean("Comment"), Post.getString("Message"), Post.getString("Data"), Post.getString("Username"), Post.getString("Avatar"), Post.getBoolean("Like"), Post.getInt("LikeCount"), Post.getInt("CommentCount"), Post.getBoolean("BookMark")));
                                    }

                                    PostAdapter.notifyDataSetChanged();
                                }
                            }
                            catch (Exception e)
                            {
                                // Leave Me Alone
                            }
                        }
                    });
                }
            }
        });

        SwipeRefreshLayoutMoment.addView(RecyclerViewMoment);

        RelativeLayout.LayoutParams ImageViewWriteParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 60), MiscHandler.ToDimension(context, 60));
        ImageViewWriteParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        ImageViewWriteParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        ImageViewWriteParam.setMargins(MiscHandler.ToDimension(context, 20), MiscHandler.ToDimension(context, 20), MiscHandler.ToDimension(context, 20), MiscHandler.ToDimension(context, 20));

        GradientDrawable Shape = new GradientDrawable();
        Shape.setShape(GradientDrawable.OVAL);
        Shape.setColor(Color.parseColor("#1f000000"));

        GradientDrawable Shape2 = new GradientDrawable();
        Shape2.setShape(GradientDrawable.OVAL);
        Shape2.setColor(ContextCompat.getColor(context, R.color.BlueLight));
        Shape2.setStroke(MiscHandler.ToDimension(context, 4), Color.TRANSPARENT);

        ImageViewWrite.setLayoutParams(ImageViewWriteParam);
        ImageViewWrite.setScaleType(ImageView.ScaleType.FIT_XY);
        ImageViewWrite.setPadding(MiscHandler.ToDimension(context, 18), MiscHandler.ToDimension(context, 18), MiscHandler.ToDimension(context, 18), MiscHandler.ToDimension(context, 18));
        ImageViewWrite.setBackground(new LayerDrawable(new Drawable[] { Shape, Shape2 }));
        ImageViewWrite.setImageResource(R.drawable.ic_write);
        ImageViewWrite.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.ActivityMainFullContainer, new FragmentMomentWrite()).addToBackStack("FragmentMomentWrite").commit();
            }
        });

        Root.addView(ImageViewWrite);

        RelativeLayoutLoading = new RelativeLayout(context);
        RelativeLayoutLoading.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayoutLoading.setBackgroundResource(R.color.White);

        Root.addView(RelativeLayoutLoading);

        RelativeLayout.LayoutParams LoadingViewDataParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), MiscHandler.ToDimension(context, 56));
        LoadingViewDataParam.addRule(RelativeLayout.CENTER_IN_PARENT);

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
        TextViewTry.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { RetrieveDataFromServer(); } });

        RelativeLayoutLoading.addView(TextViewTry);

        RetrieveDataFromServer();

        return Root;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        RequestHandler.Core().Cancel("FragmentMoment");
    }

    private void RetrieveDataFromServer()
    {
        final Context context = getActivity();

        TextViewTry.setVisibility(View.GONE);
        LoadingViewData.Start();

        RequestHandler.Core().Method("POST")
        .Address(URLHandler.GetURL(URLHandler.URL.POST_LIST))
        .Header("TOKEN", SharedHandler.GetString(context, "TOKEN"))
        .Tag("FragmentMoment")
        .Build(new RequestHandler.OnCompleteCallBack()
        {
            @Override
            public void OnFinish(String Response, int Status)
            {
                if (Status != 200)
                {
                    MiscHandler.Toast(context, getString(R.string.NoInternet));
                    TextViewTry.setVisibility(View.VISIBLE);
                    LoadingViewData.Stop();
                    return;
                }

                try
                {
                    JSONObject Result = new JSONObject(Response);

                    if (Result.getInt("Message") == 1000 && !Result.getString("Result").equals(""))
                    {
                        JSONArray postList = new JSONArray(Result.getString("Result"));

                        for (int K = 0; K < postList.length(); K++)
                        {
                            JSONObject Post = postList.getJSONObject(K);
                            PostList.add(new AdapterPost.PostStruct(Post.getString("PostID"), Post.getString("OwnerID"), Post.getInt("Type"), Post.getInt("Category"), Post.getLong("Time"), Post.getBoolean("Comment"), Post.getString("Message"), Post.getString("Data"), Post.getString("Username"), Post.getString("Avatar"), Post.getBoolean("Like"), Post.getInt("LikeCount"), Post.getInt("CommentCount"), Post.getBoolean("BookMark")));
                        }

                        PostAdapter.notifyDataSetChanged();
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
        });
    }
}
