package co.biogram.main.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private List<Struct> LikeList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Context context = getActivity();

        RelativeLayout Root = new RelativeLayout(context);
        Root.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        Root.setBackgroundColor(ContextCompat.getColor(context, R.color.White));

        RelativeLayout Header = new RelativeLayout(context);
        Header.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(56)));
        Header.setBackgroundColor(ContextCompat.getColor(context, R.color.White5));
        Header.setId(MiscHandler.GenerateViewID());

        Root.addView(Header);

        ImageView Back = new ImageView(context);
        Back.setPadding(MiscHandler.ToDimension(12), MiscHandler.ToDimension(12), MiscHandler.ToDimension(12), MiscHandler.ToDimension(12));
        Back.setScaleType(ImageView.ScaleType.FIT_CENTER);
        Back.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(56), MiscHandler.ToDimension(56)));
        Back.setImageResource(R.drawable.ic_back_blue);
        Back.setId(MiscHandler.GenerateViewID());
        Back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                getActivity().getSupportFragmentManager().beginTransaction().remove(FragmentLike.this).commit();
            }
        });

        Header.addView(Back);

        RelativeLayout.LayoutParams NameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        NameParam.addRule(RelativeLayout.RIGHT_OF, Back.getId());
        NameParam.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

        TextView Title = new TextView(context);
        Title.setLayoutParams(NameParam);
        Title.setTextColor(ContextCompat.getColor(context, R.color.Black));
        Title.setText(getString(R.string.FragmentLikeTextLike));
        Title.setTypeface(null, Typeface.BOLD);
        Title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

        Header.addView(Title);

        RelativeLayout.LayoutParams LineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(1));
        LineParam.addRule(RelativeLayout.BELOW, Header.getId());

        View Line = new View(context);
        Line.setLayoutParams(LineParam);
        Line.setBackgroundColor(ContextCompat.getColor(context, R.color.Gray2));
        Line.setId(MiscHandler.GenerateViewID());

        Root.addView(Line);

        RelativeLayout.LayoutParams RVCategoryParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        RVCategoryParam.addRule(RelativeLayout.BELOW, Line.getId());

        RecyclerView RVCategory = new RecyclerView(context);
        RVCategory.setLayoutParams(RVCategoryParam);

        Root.addView(RVCategory);

        PostID = getArguments().getString("PostID", "");
        adapterLike = new AdapterLike();

        RVCategory.setLayoutManager(new LinearLayoutManager(context));
        RVCategory.setAdapter(adapterLike);
        RVCategory.addOnScrollListener(new RecyclerView.OnScrollListener()
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

                    AndroidNetworking.post(URLHandler.GetURL(URLHandler.URL.POST_LIKE_LIST))
                    .addBodyParameter("PostID", PostID)
                    .addBodyParameter("Skip", String.valueOf(LikeList.size()))
                    .addHeaders("TOKEN", SharedHandler.GetString("TOKEN"))
                    .setTag("FragmentLike").build().getAsString(new StringRequestListener()
                    {
                        @Override
                        public void onResponse(String Response)
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
                                        LikeList.add(new Struct(Like.getString("Username"), Like.getString("OwnerID"), Like.getLong("Time"), Like.getString("Avatar")));
                                    }

                                    adapterLike.notifyDataSetChanged();
                                }
                            }
                            catch (Exception e)
                            {
                                // Leave Me Alone
                            }
                        }

                        @Override
                        public void onError(ANError error)
                        {
                            LikeList.remove(LikeList.size() - 1);
                            adapterLike.notifyItemRemoved(LikeList.size());
                            LoadingBottom = false;
                        }
                    });
                }
            }
        });

        RetrieveDataFromServer();

        return Root;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        AndroidNetworking.cancel("FragmentLike");
    }

    private void RetrieveDataFromServer()
    {
        AndroidNetworking.post(URLHandler.GetURL(URLHandler.URL.POST_LIKE_LIST))
        .addBodyParameter("PostID", PostID)
        .addBodyParameter("Skip", String.valueOf(LikeList.size()))
        .addHeaders("TOKEN", SharedHandler.GetString("TOKEN"))
        .setTag("FragmentLike").build().getAsString(new StringRequestListener()
        {
            @Override
            public void onResponse(String Response)
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
                            LikeList.add(new Struct(Like.getString("Username"), Like.getString("OwnerID"), Like.getLong("Time"), Like.getString("Avatar")));
                        }

                        adapterLike.notifyDataSetChanged();
                    }
                }
                catch (Exception e)
                {
                    // Leave Me Alone
                }
            }

            @Override
            public void onError(ANError error) { }
        });
    }

    class AdapterLike extends RecyclerView.Adapter<AdapterLike.ViewHolderLike>
    {
        private @IdRes int ID_ICON;
        private @IdRes int ID_NAME;
        private @IdRes int ID_TIME;
        private @IdRes int ID_LINE;

        AdapterLike()
        {
            ID_ICON = MiscHandler.GenerateViewID();
            ID_NAME = MiscHandler.GenerateViewID();
            ID_TIME = MiscHandler.GenerateViewID();
            ID_LINE = MiscHandler.GenerateViewID();
        }

        class ViewHolderLike extends RecyclerView.ViewHolder
        {
            ImageViewCircle Profile;
            TextView Username;
            TextView Time;
            View Line;

            ViewHolderLike(View view, boolean Content)
            {
                super(view);

                if (Content)
                {
                    Profile = (ImageViewCircle) view.findViewById(ID_ICON);
                    Username = (TextView) view.findViewById(ID_NAME);
                    Time = (TextView) view.findViewById(ID_TIME);
                    Line = view.findViewById(ID_LINE);
                }
            }
        }

        @Override
        public void onBindViewHolder(ViewHolderLike Holder, int Position)
        {
            if (LikeList.get(Position) == null)
                return;

            RequestHandler.Core().LoadImage(Holder.Profile, LikeList.get(Position).Avatar, "FragmentLike", MiscHandler.ToDimension(55), MiscHandler.ToDimension(55), true);

            Holder.Username.setText(LikeList.get(Position).Username);
            Holder.Time.setText(MiscHandler.GetTimeName(LikeList.get(Position).Time));

            if (Position == LikeList.size() - 1)
                Holder.Line.setVisibility(View.GONE);
            else
                Holder.Line.setVisibility(View.VISIBLE);
        }

        @Override
        public ViewHolderLike onCreateViewHolder(ViewGroup parent, int ViewType)
        {
            Context context = getActivity();

            if (ViewType == 0)
            {
                RelativeLayout Root = new RelativeLayout(context);
                Root.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

                RelativeLayout.LayoutParams ProfileParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(55), MiscHandler.ToDimension(55));
                ProfileParam.setMargins(MiscHandler.ToDimension(10), MiscHandler.ToDimension(10), MiscHandler.ToDimension(10), MiscHandler.ToDimension(10));

                ImageViewCircle Profile = new ImageViewCircle(context);
                Profile.setLayoutParams(ProfileParam);
                Profile.setImageResource(R.color.BlueGray);
                Profile.setId(ID_ICON);

                Root.addView(Profile);

                RelativeLayout.LayoutParams LinearParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                LinearParam.addRule(RelativeLayout.RIGHT_OF, Profile.getId());
                LinearParam.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);

                LinearLayout Linear = new LinearLayout(context);
                Linear.setLayoutParams(LinearParam);
                Linear.setOrientation(LinearLayout.VERTICAL);

                Root.addView(Linear);

                TextView Username = new TextView(context);
                Username.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                Username.setTextColor(ContextCompat.getColor(context, R.color.Black));
                Username.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                Username.setId(ID_NAME);

                Linear.addView(Username);

                TextView Time = new TextView(context);
                Time.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                Time.setTextColor(ContextCompat.getColor(context, R.color.BlueGray2));
                Time.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                Time.setId(ID_TIME);

                Linear.addView(Time);

                RelativeLayout.LayoutParams LineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(1));
                LineParam.addRule(RelativeLayout.BELOW, Profile.getId());

                View Line = new View(context);
                Line.setLayoutParams(LineParam);
                Line.setBackgroundColor(ContextCompat.getColor(context, R.color.Gray));
                Line.setId(ID_LINE);

                Root.addView(Line);

                return new ViewHolderLike(Root, true);
            }

            LinearLayout Root = new LinearLayout(context);
            Root.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(56)));
            Root.setGravity(Gravity.CENTER);

            LoadingView Loading = new LoadingView(context);

            Loading.Start();

            Root.addView(Loading);

            return new ViewHolderLike(Root, false);
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

    class Struct
    {
        String Username;
        String OwnerID;
        long Time;
        String Avatar;

        Struct(String username, String owner, long time, String avatar)
        {
            Username = username;
            OwnerID = owner;
            Time = time;
            Avatar = avatar;
        }
    }
}
