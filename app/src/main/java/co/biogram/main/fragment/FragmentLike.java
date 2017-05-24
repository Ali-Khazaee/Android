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
import android.widget.LinearLayout;
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
import co.biogram.main.misc.LoadingView;
import co.biogram.main.misc.ImageViewCircle;

public class FragmentLike extends Fragment
{
    private String PostID;
    private AdapterLike adapterLike;

    private boolean LoadingBottom = false;
    private final List<LikeStruct> LikeList = new ArrayList<>();

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
        TextViewTitleParam.addRule(RelativeLayout.RIGHT_OF, ImageViewBack.getId());
        TextViewTitleParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        TextView TextViewTitle = new TextView(context);
        TextViewTitle.setLayoutParams(TextViewTitleParam);
        TextViewTitle.setTextColor(ContextCompat.getColor(context, R.color.Black));
        TextViewTitle.setText(getString(R.string.FragmentLike));
        TextViewTitle.setTypeface(null, Typeface.BOLD);
        TextViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

        RelativeLayoutHeader.addView(TextViewTitle);

        RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1));
        ViewLineParam.addRule(RelativeLayout.BELOW, RelativeLayoutHeader.getId());

        View ViewLine = new View(context);
        ViewLine.setLayoutParams(ViewLineParam);
        ViewLine.setBackgroundResource(R.color.Gray2);
        ViewLine.setId(MiscHandler.GenerateViewID());

        Root.addView(ViewLine);

        RelativeLayout.LayoutParams RVLikeParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        RVLikeParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

        RecyclerView RecyclerViewLike = new RecyclerView(context);
        RecyclerViewLike.setLayoutParams(RVLikeParam);

        Root.addView(RecyclerViewLike);

        PostID = getArguments().getString("PostID", "");
        adapterLike = new AdapterLike();

        RecyclerViewLike.setLayoutManager(new LinearLayoutManager(context));
        RecyclerViewLike.setAdapter(adapterLike);
        RecyclerViewLike.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView View, int dx, int DY)
            {
                if (DY <= 0)
                    return;

                if (((LinearLayoutManager) View.getLayoutManager()).findLastVisibleItemPosition() + 2 > View.getAdapter().getItemCount() && !LoadingBottom)
                {
                    LikeList.add(null);
                    LoadingBottom = true;
                    adapterLike.notifyItemInserted(LikeList.size());

                    RequestHandler.Core().Method("POST")
                    .Address(URLHandler.GetURL(URLHandler.URL.POST_LIKE_LIST))
                    .Param("PostID", PostID)
                    .Param("Skip", String.valueOf(LikeList.size()))
                    .Header("TOKEN", SharedHandler.GetString(context, "TOKEN"))
                    .Tag("FragmentLike")
                    .Build(new RequestHandler.OnCompleteCallBack()
                    {
                        @Override
                        public void OnFinish(String Response, int Status)
                        {
                            LikeList.remove(LikeList.size() - 1);
                            adapterLike.notifyItemRemoved(LikeList.size());
                            LoadingBottom = false;

                            try
                            {
                                JSONObject Result = new JSONObject(Response);

                                if (Result.getInt("Message") == 1000 && !Result.getString("Result").equals(""))
                                {
                                    JSONArray Likes = new JSONArray(Result.getString("Result"));

                                    for (int K = 0; K < Likes.length(); K++)
                                    {
                                        JSONObject Like = Likes.getJSONObject(K);
                                        LikeList.add(new LikeStruct(Like.getString("Username"), Like.getLong("Time"), Like.getString("Avatar")));
                                    }

                                    adapterLike.notifyDataSetChanged();
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

        RetrieveDataFromServer();

        return Root;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        RequestHandler.Core().Cancel("FragmentLike");
    }

    private void RetrieveDataFromServer()
    {
        RequestHandler.Core().Method("POST")
        .Address(URLHandler.GetURL(URLHandler.URL.POST_LIKE_LIST))
        .Param("PostID", PostID)
        .Param("Skip", String.valueOf(LikeList.size()))
        .Header("TOKEN", SharedHandler.GetString(getActivity(), "TOKEN"))
        .Tag("FragmentLike")
        .Build(new RequestHandler.OnCompleteCallBack()
        {
            @Override
            public void OnFinish(String Response, int Status)
            {
                try
                {
                    JSONObject Result = new JSONObject(Response);

                    if (Result.getInt("Message") == 1000 && !Result.getString("Result").equals(""))
                    {
                        JSONArray Likes = new JSONArray(Result.getString("Result"));

                        for (int K = 0; K < Likes.length(); K++)
                        {
                            JSONObject Like = Likes.getJSONObject(K);
                            LikeList.add(new LikeStruct(Like.getString("Username"), Like.getLong("Time"), Like.getString("Avatar")));
                        }

                        adapterLike.notifyDataSetChanged();
                    }
                }
                catch (Exception e)
                {
                    // Leave Me Alone
                }
            }
        });
    }

    class AdapterLike extends RecyclerView.Adapter<AdapterLike.ViewHolderLike>
    {
        private final int ID_ICON = MiscHandler.GenerateViewID();
        private final int ID_NAME = MiscHandler.GenerateViewID();
        private final int ID_TIME = MiscHandler.GenerateViewID();
        private final int ID_LINE = MiscHandler.GenerateViewID();

        class ViewHolderLike extends RecyclerView.ViewHolder
        {
            ImageViewCircle ImageViewCircleProfile;
            TextView TextViewUsername;
            TextView TextViewTime;
            View ViewLine;

            ViewHolderLike(View view, boolean Content)
            {
                super(view);

                if (Content)
                {
                    ImageViewCircleProfile = (ImageViewCircle) view.findViewById(ID_ICON);
                    TextViewUsername = (TextView) view.findViewById(ID_NAME);
                    TextViewTime = (TextView) view.findViewById(ID_TIME);
                    ViewLine = view.findViewById(ID_LINE);
                }
            }
        }

        @Override
        public void onBindViewHolder(ViewHolderLike Holder, int Position)
        {
            if (LikeList.get(Position) == null)
                return;

            RequestHandler.Core().LoadImage(Holder.ImageViewCircleProfile, LikeList.get(Position).Avatar, "FragmentLike", MiscHandler.ToDimension(getActivity(), 55), MiscHandler.ToDimension(getActivity(), 55), true);

            Holder.TextViewUsername.setText(LikeList.get(Position).Username);
            Holder.TextViewTime.setText(MiscHandler.GetTimeName(LikeList.get(Position).Time));

            if (Position == LikeList.size() - 1)
                Holder.ViewLine.setVisibility(View.GONE);
            else
                Holder.ViewLine.setVisibility(View.VISIBLE);
        }

        @Override
        public ViewHolderLike onCreateViewHolder(ViewGroup parent, int ViewType)
        {
            Context context = getActivity();

            if (ViewType == 0)
            {
                RelativeLayout Root = new RelativeLayout(context);
                Root.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

                RelativeLayout.LayoutParams ImageViewCircleProfileParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 55), MiscHandler.ToDimension(context, 55));
                ImageViewCircleProfileParam.setMargins(MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10));

                ImageViewCircle ImageViewCircleProfile = new ImageViewCircle(context);
                ImageViewCircleProfile.setLayoutParams(ImageViewCircleProfileParam);
                ImageViewCircleProfile.setImageResource(R.color.BlueGray);
                ImageViewCircleProfile.setId(ID_ICON);

                Root.addView(ImageViewCircleProfile);

                RelativeLayout.LayoutParams LinearLayoutRowParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                LinearLayoutRowParam.addRule(RelativeLayout.RIGHT_OF, ImageViewCircleProfile.getId());
                LinearLayoutRowParam.addRule(RelativeLayout.CENTER_VERTICAL);

                LinearLayout LinearLayoutRow = new LinearLayout(context);
                LinearLayoutRow.setLayoutParams(LinearLayoutRowParam);
                LinearLayoutRow.setOrientation(LinearLayout.VERTICAL);

                Root.addView(LinearLayoutRow);

                TextView TextViewUsername = new TextView(context);
                TextViewUsername.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TextViewUsername.setTextColor(ContextCompat.getColor(context, R.color.Black));
                TextViewUsername.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                TextViewUsername.setId(ID_NAME);

                LinearLayoutRow.addView(TextViewUsername);

                TextView TextViewTime = new TextView(context);
                TextViewTime.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TextViewTime.setTextColor(ContextCompat.getColor(context, R.color.BlueGray2));
                TextViewTime.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                TextViewTime.setId(ID_TIME);

                LinearLayoutRow.addView(TextViewTime);

                RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1));
                ViewLineParam.addRule(RelativeLayout.BELOW, ImageViewCircleProfile.getId());

                View ViewLine = new View(context);
                ViewLine.setLayoutParams(ViewLineParam);
                ViewLine.setBackgroundResource(R.color.Gray);
                ViewLine.setId(ID_LINE);

                Root.addView(ViewLine);

                return new ViewHolderLike(Root, true);
            }

            LoadingView Loading = new LoadingView(context);
            Loading.setLayoutParams(new LinearLayout.LayoutParams(MiscHandler.ToDimension(context, 56), MiscHandler.ToDimension(context, 56)));
            Loading.SetShow(true);
            Loading.Start();

            return new ViewHolderLike(Loading, false);
        }

        @Override
        public int getItemViewType(int position)
        {
            return LikeList.get(position)!= null ? 0 : 1;
        }

        @Override
        public int getItemCount()
        {
            return LikeList.size();
        }
    }

    private class LikeStruct
    {
        final String Username;
        final long Time;
        final String Avatar;

        LikeStruct(String username, long time, String avatar)
        {
            Username = username;
            Time = time;
            Avatar = avatar;
        }
    }
}
