package co.biogram.main.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import co.biogram.main.App;
import co.biogram.main.R;
import co.biogram.main.handler.MiscHandler;
import co.biogram.main.handler.RequestHandler;
import co.biogram.main.handler.SharedHandler;
import co.biogram.main.handler.URLHandler;
import co.biogram.main.misc.PostAdapter;
import co.biogram.main.misc.LoadingView;

public class FragmentProfilePost extends Fragment
{
    private RelativeLayout RelativeLayoutLoading;
    private LoadingView LoadingViewData;
    private TextView TextViewTry;

    private boolean IsBottom = false;
    private PostAdapter postAdapter;
    private List<PostAdapter.Struct> PostList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        RelativeLayout Root = new RelativeLayout(App.GetContext());
        Root.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        Root.setBackgroundColor(ContextCompat.getColor(App.GetContext(), R.color.White));
        Root.setClickable(true);

        postAdapter = new PostAdapter((AppCompatActivity) getActivity(), PostList, "FragmentProfilePost");

        RecyclerView RecyclerViewPost = new RecyclerView(App.GetContext());
        RecyclerViewPost.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerViewPost.setAdapter(postAdapter);
        RecyclerViewPost.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int DX, int DY)
            {
                if ((((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition() + 2) > recyclerView.getAdapter().getItemCount() && !IsBottom)
                {
                    IsBottom = true;
                    PostList.add(null);
                    postAdapter.notifyItemInserted(PostList.size());

                    String ID = SharedHandler.GetString("ID");

                    if (getArguments() != null && !getArguments().getString("ID", "").equals(""))
                        ID = getArguments().getString("ID");

                    RequestHandler.Method("POST")
                    .Address(URLHandler.GetURL(URLHandler.URL.PROFILE_GET_POST))
                    .Header("TOKEN", SharedHandler.GetString("TOKEN"))
                    .Param("Skip", String.valueOf(PostList.size()))
                    .Param("ID", ID)
                    .Tag("FragmentProfilePost")
                    .Build(new RequestHandler.OnCompleteCallBack()
                    {
                        @Override
                        public void OnFinish(String Response, int Status)
                        {
                            IsBottom = false;
                            PostList.remove(PostList.size() - 1);
                            postAdapter.notifyItemRemoved(PostList.size());

                            if (Status < 0)
                            {
                                MiscHandler.Toast(getString(R.string.GeneralCheckInternet));
                                return;
                            }
                            MiscHandler.Log(Response);
                            try
                            {
                                JSONObject Result = new JSONObject(Response);

                                if (Result.getInt("Message") == 1000 && !Result.getString("Result").equals(""))
                                {
                                    JSONArray PostListArray = new JSONArray(Result.getString("Result"));

                                    for (int K = 0; K < PostListArray.length(); K++)
                                    {
                                        JSONObject Post = PostListArray.getJSONObject(K);
                                        PostList.add(new PostAdapter.Struct(Post.getString("PostID"), Post.getString("OwnerID"), Post.getInt("Type"), Post.getInt("Category"), Post.getLong("Time"), Post.getBoolean("Comment"), Post.getString("Message"), Post.getString("Data"), Post.getString("Username"), Post.getString("Avatar"), Post.getBoolean("Like"), Post.getInt("LikeCount"), Post.getInt("CommentCount"), Post.getBoolean("BookMark")));
                                    }

                                    postAdapter.notifyDataSetChanged();
                                }
                            }
                            catch (Exception e)
                            {MiscHandler.Log(e.toString());
                                // Leave Me Alone
                            }
                        }
                    });
                }
            }
        });

        RelativeLayoutLoading = new RelativeLayout(App.GetContext());
        RelativeLayoutLoading.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayoutLoading.setBackgroundColor(ContextCompat.getColor(App.GetContext(), R.color.White));

        Root.addView(RelativeLayoutLoading);

        RelativeLayout.LayoutParams LoadingViewDataParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LoadingViewDataParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        LoadingViewData = new LoadingView(App.GetContext());
        LoadingViewData.setLayoutParams(LoadingViewDataParam);
        LoadingViewData.SetColor(ContextCompat.getColor(App.GetContext(), R.color.BlueGray2));

        RelativeLayoutLoading.addView(LoadingViewData);

        RelativeLayout.LayoutParams TextViewTryParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTryParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        TextViewTry = new TextView(App.GetContext());
        TextViewTry.setLayoutParams(TextViewTryParam);
        TextViewTry.setTextColor(ContextCompat.getColor(App.GetContext(), R.color.BlueGray2));
        TextViewTry.setText(getString(R.string.GeneralTryAgain));
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
        RequestHandler.Cancel("FragmentProfilePost");
    }

    private void RetrieveDataFromServer()
    {
        TextViewTry.setVisibility(View.GONE);
        LoadingViewData.Start();

        String ID = SharedHandler.GetString("ID");

        if (getArguments() != null && !getArguments().getString("ID", "").equals(""))
            ID = getArguments().getString("ID");

        RequestHandler.Method("POST")
        .Address(URLHandler.GetURL(URLHandler.URL.PROFILE_GET_POST))
        .Header("TOKEN", SharedHandler.GetString("TOKEN"))
        .Param("ID", ID)
        .Tag("FragmentProfilePost")
        .Build(new RequestHandler.OnCompleteCallBack()
        {
            @Override
            public void OnFinish(String Response, int Status)
            {
                if (Status < 0)
                {
                    MiscHandler.Toast(getString(R.string.GeneralCheckInternet));
                    TextViewTry.setVisibility(View.VISIBLE);
                    LoadingViewData.Stop();
                    return;
                }
                MiscHandler.Log(Response);
                try
                {
                    JSONObject Result = new JSONObject(Response);

                    if (Result.getInt("Message") == 1000 && !Result.getString("Result").equals(""))
                    {
                        JSONArray PostListArray = new JSONArray(Result.getString("Result"));

                        for (int K = 0; K < PostListArray.length(); K++)
                        {
                            JSONObject Post = PostListArray.getJSONObject(K);
                            PostList.add(new PostAdapter.Struct(Post.getString("PostID"), Post.getString("OwnerID"), Post.getInt("Type"), Post.getInt("Category"), Post.getLong("Time"), Post.getBoolean("Comment"), Post.getString("Message"), Post.getString("Data"), Post.getString("Username"), Post.getString("Avatar"), Post.getBoolean("Like"), Post.getInt("LikeCount"), Post.getInt("CommentCount"), Post.getBoolean("BookMark")));
                        }

                        postAdapter.notifyDataSetChanged();
                    }
                }
                catch (Exception e)
                {MiscHandler.Log(e.toString());
                    // Leave Me Alone
                }

                RelativeLayoutLoading.setVisibility(View.GONE);
                TextViewTry.setVisibility(View.GONE);
                LoadingViewData.Stop();
            }
        });
    }
}
