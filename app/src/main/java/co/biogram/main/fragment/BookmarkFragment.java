package co.biogram.main.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import co.biogram.main.handler.MiscHandler;
import co.biogram.main.handler.SharedHandler;
import co.biogram.main.misc.AdapterPost;
import co.biogram.main.misc.LoadingView;
import co.biogram.main.misc.RecyclerViewScroll;

public class BookmarkFragment extends Fragment
{
    private final List<AdapterPost.Struct> PostList = new ArrayList<>();
    private AdapterPost PostAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final Context context = getActivity();

        RelativeLayout RelativeLayoutMain = new RelativeLayout(context);
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayoutMain.setClickable(true);
        RelativeLayoutMain.setBackgroundResource(R.color.White);

        RelativeLayout RelativeLayoutHeader = new RelativeLayout(context);
        RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56)));
        RelativeLayoutHeader.setBackgroundResource(R.color.White5);
        RelativeLayoutHeader.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain.addView(RelativeLayoutHeader);

        ImageView ImageViewBack = new ImageView(context);
        ImageViewBack.setPadding(MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12));
        ImageViewBack.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewBack.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), MiscHandler.ToDimension(context, 56)));
        ImageViewBack.setImageResource(R.drawable.ic_back_blue);
        ImageViewBack.setId(MiscHandler.GenerateViewID());
        ImageViewBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                getActivity().onBackPressed();
            }
        });

        RelativeLayoutHeader.addView(ImageViewBack);

        RelativeLayout.LayoutParams TextViewTitleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTitleParam.addRule(RelativeLayout.CENTER_VERTICAL);
        TextViewTitleParam.addRule(RelativeLayout.RIGHT_OF, ImageViewBack.getId());

        TextView TextViewTitle = new TextView(context);
        TextViewTitle.setLayoutParams(TextViewTitleParam);
        TextViewTitle.setText(getString(R.string.BookmarkFragment));
        TextViewTitle.setTextColor(ContextCompat.getColor(context, R.color.Black));
        TextViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        TextViewTitle.setTypeface(null, Typeface.BOLD);

        RelativeLayoutHeader.addView(TextViewTitle);

        RelativeLayout.LayoutParams ImageViewSearchParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), RelativeLayout.LayoutParams.MATCH_PARENT);
        ImageViewSearchParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        ImageView ImageViewSearch = new ImageView(context);
        ImageViewSearch.setLayoutParams(ImageViewSearchParam);
        ImageViewSearch.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewSearch.setPadding(MiscHandler.ToDimension(context, 16), MiscHandler.ToDimension(context, 16), MiscHandler.ToDimension(context, 16), MiscHandler.ToDimension(context, 16));
        ImageViewSearch.setImageResource(R.drawable.ic_search_blue);
        ImageViewSearch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.ActivityMainFullContainer, new SearchFragment()).addToBackStack("SearchFragment").commit();
            }
        });

        RelativeLayoutHeader.addView(ImageViewSearch);

        RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1));
        ViewLineParam.addRule(RelativeLayout.BELOW, RelativeLayoutHeader.getId());

        View ViewLine = new View(context);
        ViewLine.setLayoutParams(ViewLineParam);
        ViewLine.setBackgroundResource(R.color.Gray2);
        ViewLine.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain.addView(ViewLine);

        RelativeLayout.LayoutParams RecyclerViewMainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        RecyclerViewMainParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

        LinearLayoutManager LinearLayoutManagerMain = new LinearLayoutManager(context);

        RecyclerView RecyclerViewMain = new RecyclerView(context);
        RecyclerViewMain.setLayoutParams(RecyclerViewMainParam);
        RecyclerViewMain.setLayoutManager(LinearLayoutManagerMain);
        RecyclerViewMain.setAdapter(PostAdapter = new AdapterPost(getActivity(), PostList, "BookmarkFragment"));
        RecyclerViewMain.addOnScrollListener(new RecyclerViewScroll(LinearLayoutManagerMain)
        {
            @Override
            public void OnLoadMore()
            {
                PostList.add(null);
                PostAdapter.notifyItemInserted(PostList.size());

                AndroidNetworking.post(MiscHandler.GetRandomServer("PostListBookmark"))
                .addBodyParameter("Skip", String.valueOf(PostList.size() - 1))
                .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
                .setTag("BookmarkFragment")
                .build()
                .getAsString(new StringRequestListener()
                {
                    @Override
                    public void onResponse(String Response)
                    {
                        PostList.remove(PostList.size() - 1);
                        PostAdapter.notifyItemRemoved(PostList.size());

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

                                PostAdapter.notifyDataSetChanged();
                            }
                        }
                        catch (Exception e)
                        {
                            ResetLoading(false);
                        }
                    }

                    @Override
                    public void onError(ANError anError)
                    {
                        ResetLoading(false);
                        PostList.remove(PostList.size() - 1);
                        PostAdapter.notifyItemRemoved(PostList.size());
                    }
                });
            }
        });

        RelativeLayoutMain.addView(RecyclerViewMain);

        RelativeLayout.LayoutParams LoadingViewMainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56));
        LoadingViewMainParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        final LoadingView LoadingViewMain = new LoadingView(context);
        LoadingViewMain.setLayoutParams(LoadingViewMainParam);

        RelativeLayoutMain.addView(LoadingViewMain);

        RelativeLayout.LayoutParams TextViewTryAgainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTryAgainParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        final TextView TextViewTryAgain = new TextView(context);
        TextViewTryAgain.setLayoutParams(TextViewTryAgainParam);
        TextViewTryAgain.setText(getString(R.string.TryAgain));
        TextViewTryAgain.setTextColor(ContextCompat.getColor(context, R.color.BlueGray));
        TextViewTryAgain.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        TextViewTryAgain.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                RetrieveDataFromServer(context, LoadingViewMain, TextViewTryAgain);
            }
        });

        RelativeLayoutMain.addView(TextViewTryAgain);

        RetrieveDataFromServer(context, LoadingViewMain, TextViewTryAgain);

        return RelativeLayoutMain;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        AndroidNetworking.forceCancel("BookmarkFragment");
    }

    private void RetrieveDataFromServer(final Context context, final LoadingView LoadingViewMain, final TextView TextViewTryAgain)
    {
        TextViewTryAgain.setVisibility(View.GONE);
        LoadingViewMain.Start();

        AndroidNetworking.post(MiscHandler.GetRandomServer("PostListBookmark"))
        .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
        .setTag("BookmarkFragment")
        .build()
        .getAsString(new StringRequestListener()
        {
            @Override
            public void onResponse(String Response)
            {
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

                        PostAdapter.notifyDataSetChanged();
                    }
                }
                catch (Exception e)
                {
                    // Leave Me Alone
                }

                LoadingViewMain.Stop();
                TextViewTryAgain.setVisibility(View.GONE);
            }

            @Override
            public void onError(ANError anError)
            {
                LoadingViewMain.Stop();
                TextViewTryAgain.setVisibility(View.VISIBLE);
                MiscHandler.Toast(context, getString(R.string.NoInternet));
            }
        });
    }
}
