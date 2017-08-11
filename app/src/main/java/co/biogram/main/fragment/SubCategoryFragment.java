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

public class SubCategoryFragment extends Fragment
{
    private LoadingView LoadingViewInbox;
    private TextView TextViewTryAgain;

    private int CatType = 17;

    private AdapterPost Adapter;
    private final List<AdapterPost.Struct> InboxList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        final Context context = getActivity();

        String CatName = "";

        if (getArguments() != null)
        {
            CatName = getArguments().getString("CatName", "");
            CatType = getArguments().getInt("CatType", 17);
        }

        RelativeLayout RelativeLayoutMain = new RelativeLayout(context);
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
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
        TextViewTitle.setText(CatName);
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
        ImageViewBookMark.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.ActivityMainFullContainer, new BookmarkFragment()).addToBackStack("BookmarkFragment").commit();
            }
        });

        RelativeLayoutHeader.addView(ImageViewBookMark);

        RelativeLayout.LayoutParams ImageViewSearchParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), RelativeLayout.LayoutParams.MATCH_PARENT);
        ImageViewSearchParam.addRule(RelativeLayout.LEFT_OF, ImageViewBookMark.getId());

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

        RelativeLayout.LayoutParams RecyclerViewInboxParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        RecyclerViewInboxParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

        LinearLayoutManager LinearLayoutManagerNotification = new LinearLayoutManager(context);

        RecyclerView RecyclerViewInbox = new RecyclerView(context);
        RecyclerViewInbox.setLayoutParams(RecyclerViewInboxParam);
        RecyclerViewInbox.setLayoutManager(LinearLayoutManagerNotification);
        RecyclerViewInbox.setAdapter(Adapter = new AdapterPost(getActivity(), InboxList, "SubCategoryFragment"));
        RecyclerViewInbox.addOnScrollListener(new RecyclerViewScroll(LinearLayoutManagerNotification)
        {
            @Override
            public void OnLoadMore()
            {
                InboxList.add(null);
                Adapter.notifyItemInserted(InboxList.size());

                AndroidNetworking.post(MiscHandler.GetRandomServer("PostListCategory"))
                .addBodyParameter("Skip", String.valueOf(InboxList.size()))
                .addBodyParameter("CatType", String.valueOf(CatType))
                .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
                .setTag("SubCategoryFragment")
                .build().getAsString(new StringRequestListener()
                {
                    @Override
                    public void onResponse(String Response)
                    {
                        InboxList.remove(InboxList.size() - 1);
                        Adapter.notifyItemRemoved(InboxList.size());

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

                                    InboxList.add(PostStruct);
                                }

                                Adapter.notifyDataSetChanged();
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
                        InboxList.remove(InboxList.size() - 1);
                        Adapter.notifyItemRemoved(InboxList.size());
                    }
                });
            }
        });

        RelativeLayoutMain.addView(RecyclerViewInbox);

        RelativeLayout.LayoutParams LoadingViewInboxParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56));
        LoadingViewInboxParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        LoadingViewInbox = new LoadingView(context);
        LoadingViewInbox.setLayoutParams(LoadingViewInboxParam);

        RelativeLayoutMain.addView(LoadingViewInbox);

        RelativeLayout.LayoutParams TextViewTryAgainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTryAgainParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        TextViewTryAgain = new TextView(context);
        TextViewTryAgain.setLayoutParams(TextViewTryAgainParam);
        TextViewTryAgain.setText(getString(R.string.TryAgain));
        TextViewTryAgain.setTextColor(ContextCompat.getColor(context, R.color.BlueGray));
        TextViewTryAgain.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        TextViewTryAgain.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { RetrieveDataFromServer(context); } });

        RelativeLayoutMain.addView(TextViewTryAgain);

        RetrieveDataFromServer(context);

        return RelativeLayoutMain;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        AndroidNetworking.forceCancel("SubCategoryFragment");
    }

    private void RetrieveDataFromServer(final Context context)
    {
        TextViewTryAgain.setVisibility(View.GONE);
        LoadingViewInbox.Start();

        AndroidNetworking.post(MiscHandler.GetRandomServer("PostListCategory"))
        .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
        .addBodyParameter("CatType", String.valueOf(CatType))
        .setTag("SubCategoryFragment")
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

                            InboxList.add(PostStruct);
                        }

                        Adapter.notifyDataSetChanged();
                    }
                }
                catch (Exception e)
                {
                    // Leave Me Alone
                }

                LoadingViewInbox.Stop();
                TextViewTryAgain.setVisibility(View.GONE);
            }

            @Override
            public void onError(ANError anError)
            {
                MiscHandler.Toast(context, getString(R.string.NoInternet));
                TextViewTryAgain.setVisibility(View.VISIBLE);
                LoadingViewInbox.Stop();
            }
        });
    }
}
