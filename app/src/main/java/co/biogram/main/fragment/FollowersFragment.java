package co.biogram.main.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
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

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import co.biogram.main.R;
import co.biogram.main.handler.MiscHandler;
import co.biogram.main.handler.SharedHandler;
import co.biogram.main.misc.ImageViewCircle;
import co.biogram.main.misc.LoadingView;
import co.biogram.main.misc.RecyclerViewScroll;

public class FollowersFragment extends Fragment
{
    private final List<Struct> FollowersList = new ArrayList<>();
    private AdapterFollowers Adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final Context context = getActivity();
        final String Username;

        if (getArguments() != null)
            Username = getArguments().getString("Username", "");
        else
            Username = "";

        RelativeLayout RelativeLayoutMain = new RelativeLayout(context);
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayoutMain.setBackgroundResource(R.color.White);
        RelativeLayoutMain.setClickable(true);

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
        ImageViewBack.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { getActivity().onBackPressed(); } });

        RelativeLayoutHeader.addView(ImageViewBack);

        RelativeLayout.LayoutParams TextViewTitleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTitleParam.addRule(RelativeLayout.RIGHT_OF, ImageViewBack.getId());
        TextViewTitleParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        TextView TextViewTitle = new TextView(context);
        TextViewTitle.setLayoutParams(TextViewTitleParam);
        TextViewTitle.setTextColor(ContextCompat.getColor(context, R.color.Black));
        TextViewTitle.setText(getString(R.string.FollowersFragment));
        TextViewTitle.setTypeface(null, Typeface.BOLD);
        TextViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

        RelativeLayoutHeader.addView(TextViewTitle);

        RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1));
        ViewLineParam.addRule(RelativeLayout.BELOW, RelativeLayoutHeader.getId());

        View ViewLine = new View(context);
        ViewLine.setLayoutParams(ViewLineParam);
        ViewLine.setBackgroundResource(R.color.Gray2);
        ViewLine.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain.addView(ViewLine);

        RelativeLayout.LayoutParams RecyclerViewFollowersParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        RecyclerViewFollowersParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

        LinearLayoutManager LinearLayoutManagerMain = new LinearLayoutManager(context);

        RecyclerViewScroll RecyclerViewScrollMain = new RecyclerViewScroll(LinearLayoutManagerMain)
        {
            @Override
            public void OnLoadMore()
            {
                FollowersList.add(null);
                Adapter.notifyDataSetChanged();

                AndroidNetworking.post(MiscHandler.GetRandomServer("FollowersGet"))
                .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
                .addBodyParameter("Skip", String.valueOf(FollowersList.size() - 1))
                .addBodyParameter("Username", Username)
                .setTag("FollowersFragment")
                .build()
                .getAsString(new StringRequestListener()
                {
                    @Override
                    public void onResponse(String Response)
                    {
                        FollowersList.remove(FollowersList.size() - 1);

                        try
                        {
                            JSONObject Result = new JSONObject(Response);

                            if (Result.getInt("Message") == 1000 && !Result.getString("Result").equals(""))
                            {
                                JSONArray ResultList = new JSONArray(Result.getString("Result"));

                                for (int I = 0; I < ResultList.length(); I++)
                                {
                                    JSONObject Follower = ResultList.getJSONObject(I);

                                    Struct StructFollowers = new Struct();
                                    StructFollowers.Username = Follower.getString("Username");
                                    StructFollowers.Avatar = Follower.getString("Avatar");
                                    StructFollowers.Follow = Follower.getBoolean("Follow");
                                    StructFollowers.Since = Follower.getInt("Since");

                                    FollowersList.add(StructFollowers);
                                }
                            }
                        }
                        catch (Exception e)
                        {
                            ResetLoading(false);
                            MiscHandler.Debug("FollowersFragment-RequestMore: " + e.toString());
                        }

                        Adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(ANError anError)
                    {
                        ResetLoading(false);
                        FollowersList.remove(FollowersList.size() - 1);
                        Adapter.notifyDataSetChanged();
                    }
                });
            }
        };

        RecyclerView RecyclerViewMain = new RecyclerView(context);
        RecyclerViewMain.setLayoutParams(RecyclerViewFollowersParam);
        RecyclerViewMain.setLayoutManager(LinearLayoutManagerMain);
        RecyclerViewMain.setAdapter(Adapter = new AdapterFollowers(context));
        RecyclerViewMain.addOnScrollListener(RecyclerViewScrollMain);

        RelativeLayoutMain.addView(RecyclerViewMain);

        RelativeLayout.LayoutParams RelativeLayoutLoadingParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        RelativeLayoutLoadingParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

        final RelativeLayout RelativeLayoutLoading = new RelativeLayout(context);
        RelativeLayoutLoading.setLayoutParams(RelativeLayoutLoadingParam);
        RelativeLayoutLoading.setBackgroundResource(R.color.White);

        RelativeLayoutMain.addView(RelativeLayoutLoading);

        RelativeLayout.LayoutParams LoadingViewMainParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), MiscHandler.ToDimension(context, 56));
        LoadingViewMainParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        final LoadingView LoadingViewMain = new LoadingView(context);
        LoadingViewMain.setLayoutParams(LoadingViewMainParam);

        RelativeLayoutLoading.addView(LoadingViewMain);

        final RelativeLayout.LayoutParams TextViewTryAgainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTryAgainParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        final TextView TextViewTryAgain = new TextView(context);
        TextViewTryAgain.setLayoutParams(TextViewTryAgainParam);
        TextViewTryAgain.setTextColor(ContextCompat.getColor(context, R.color.BlueGray2));
        TextViewTryAgain.setText(getString(R.string.TryAgain));
        TextViewTryAgain.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        TextViewTryAgain.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { RetrieveDataFromServer(context, Username, RelativeLayoutLoading, LoadingViewMain, TextViewTryAgain); } });

        RelativeLayoutLoading.addView(TextViewTryAgain);

        RetrieveDataFromServer(context, Username, RelativeLayoutLoading, LoadingViewMain, TextViewTryAgain);

        return RelativeLayoutMain;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        AndroidNetworking.forceCancel("FollowersFragment");
    }

    private void RetrieveDataFromServer(final Context context, String Username, final RelativeLayout RelativeLayoutLoading, final LoadingView LoadingViewMain, final TextView TextViewTryAgain)
    {
        TextViewTryAgain.setVisibility(View.GONE);
        LoadingViewMain.Start();

        AndroidNetworking.post(MiscHandler.GetRandomServer("FollowersGet"))
        .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
        .addBodyParameter("Username", Username)
        .setTag("FollowersFragment")
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

                        for (int I = 0; I < ResultList.length(); I++)
                        {
                            JSONObject Followers = ResultList.getJSONObject(I);

                            Struct StructFollowers = new Struct();
                            StructFollowers.Username = Followers.getString("Username");
                            StructFollowers.Avatar = Followers.getString("Avatar");
                            StructFollowers.Follow = Followers.getBoolean("Follow");

                            FollowersList.add(StructFollowers);
                        }

                        Adapter.notifyDataSetChanged();
                    }
                }
                catch (Exception e)
                {
                    MiscHandler.Debug("FollowersFragment-RequestNew: " + e.toString());
                }

                LoadingViewMain.Stop();
                TextViewTryAgain.setVisibility(View.GONE);
                RelativeLayoutLoading.setVisibility(View.GONE);
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

    private class AdapterFollowers extends RecyclerView.Adapter<AdapterFollowers.ViewHolderFollowing>
    {
        private final int IDProfile = MiscHandler.GenerateViewID();
        private final int IDUsername = MiscHandler.GenerateViewID();
        private final int IDTime = MiscHandler.GenerateViewID();
        private final int IDLayout = MiscHandler.GenerateViewID();
        private final int IDFollow = MiscHandler.GenerateViewID();
        private final int IDLoading = MiscHandler.GenerateViewID();
        private final int IDLine = MiscHandler.GenerateViewID();

        private final Context context;

        AdapterFollowers(Context c)
        {
            context = c;
        }

        class ViewHolderFollowing extends RecyclerView.ViewHolder
        {
            ImageViewCircle ImageViewCircleProfile;
            TextView TextViewUsername;
            TextView TextViewTime;
            RelativeLayout RelativeLayoutFollow;
            TextView TextViewFollow;
            LoadingView LoadingViewFollow;
            View ViewLine;

            ViewHolderFollowing(View view, boolean Content)
            {
                super(view);

                if (Content)
                {
                    ImageViewCircleProfile = (ImageViewCircle) view.findViewById(IDProfile);
                    TextViewUsername = (TextView) view.findViewById(IDUsername);
                    TextViewTime = (TextView) view.findViewById(IDTime);
                    RelativeLayoutFollow = (RelativeLayout) view.findViewById(IDLayout);
                    TextViewFollow = (TextView) view.findViewById(IDFollow);
                    LoadingViewFollow = (LoadingView) view.findViewById(IDLoading);
                    ViewLine = view.findViewById(IDLine);
                }
            }
        }

        @Override
        public void onBindViewHolder(final ViewHolderFollowing Holder, int position)
        {
            if (FollowersList.get(position) == null)
                return;

            final int Position = Holder.getAdapterPosition();

            Glide.with(context)
            .load(FollowersList.get(Position).Avatar)
            .placeholder(R.color.BlueGray)
            .override(MiscHandler.ToDimension(context, 55), MiscHandler.ToDimension(context, 55))
            .dontAnimate()
            .into(Holder.ImageViewCircleProfile);

            Holder.ImageViewCircleProfile.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("Username", FollowersList.get(Position).Username);

                    Fragment fragment = new ProfileFragment();
                    fragment.setArguments(bundle);

                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.ActivityMainFullContainer, fragment).addToBackStack("FragmentProfile").commit();
                }
            });

            Holder.TextViewUsername.setText(FollowersList.get(Position).Username);

            String Since = getString(R.string.FragmentFollowersSince) + MiscHandler.GetTimeName(FollowersList.get(Position).Since);

            Holder.TextViewTime.setText(Since);

            if (FollowersList.get(Position).Follow)
            {
                Holder.TextViewFollow.setText(getString(R.string.FragmentFollowing));
                Holder.TextViewFollow.setTextColor(ContextCompat.getColor(context, R.color.Gray6));
            }
            else
            {
                Holder.TextViewFollow.setText(getString(R.string.FragmentFollowersFollow));
                Holder.TextViewFollow.setTextColor(ContextCompat.getColor(context, R.color.BlueLight));
            }

            Holder.RelativeLayoutFollow.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Holder.TextViewFollow.setVisibility(View.GONE);
                    Holder.LoadingViewFollow.Start();

                    AndroidNetworking.post(MiscHandler.GetRandomServer("Follow"))
                    .addBodyParameter("Username", FollowersList.get(Position).Username)
                    .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
                    .setTag("FollowersFragment")
                    .build().getAsString(new StringRequestListener()
                    {
                        @Override
                        public void onResponse(String Response)
                        {
                            try
                            {
                                JSONObject Result = new JSONObject(Response);

                                if (Result.getInt("Message") == 1000)
                                {
                                    if (Result.getBoolean("Follow"))
                                    {
                                        Holder.TextViewFollow.setText(getString(R.string.FragmentFollowing));
                                        Holder.TextViewFollow.setTextColor(ContextCompat.getColor(context, R.color.Gray6));
                                    }
                                    else
                                    {
                                        Holder.TextViewFollow.setText(getString(R.string.FollowersFragmentFollow));
                                        Holder.TextViewFollow.setTextColor(ContextCompat.getColor(context, R.color.BlueLight));
                                    }

                                    Adapter.notifyDataSetChanged();
                                }
                            }
                            catch (Exception e)
                            {
                                // Leave Me Alone
                            }

                            Holder.TextViewFollow.setVisibility(View.VISIBLE);
                            Holder.LoadingViewFollow.Stop();
                        }

                        @Override
                        public void onError(ANError anError)
                        {
                            Holder.TextViewFollow.setVisibility(View.VISIBLE);
                            Holder.LoadingViewFollow.Stop();

                            MiscHandler.Toast(context, getString(R.string.NoInternet));
                        }
                    });
                }
            });

            if (Position == FollowersList.size() - 1)
                Holder.ViewLine.setVisibility(View.GONE);
            else
                Holder.ViewLine.setVisibility(View.VISIBLE);
        }

        @Override
        public ViewHolderFollowing onCreateViewHolder(ViewGroup parent, int ViewType)
        {
            if (ViewType == 0)
            {
                RelativeLayout Root = new RelativeLayout(context);
                Root.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

                RelativeLayout.LayoutParams ImageViewCircleProfileParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 40), MiscHandler.ToDimension(context, 40));
                ImageViewCircleProfileParam.setMargins(MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10));

                ImageViewCircle ImageViewCircleProfile = new ImageViewCircle(context);
                ImageViewCircleProfile.setLayoutParams(ImageViewCircleProfileParam);
                ImageViewCircleProfile.setImageResource(R.color.BlueGray);
                ImageViewCircleProfile.setId(IDProfile);

                Root.addView(ImageViewCircleProfile);

                RelativeLayout.LayoutParams RelativeLayoutFollowParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 90), MiscHandler.ToDimension(context, 35));
                RelativeLayoutFollowParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                RelativeLayoutFollowParam.addRule(RelativeLayout.CENTER_VERTICAL);
                RelativeLayoutFollowParam.setMargins(0, 0, MiscHandler.ToDimension(context, 10), 0);

                RelativeLayout RelativeLayoutFollow = new RelativeLayout(context);
                RelativeLayoutFollow.setLayoutParams(RelativeLayoutFollowParam);
                RelativeLayoutFollow.setId(MiscHandler.GenerateViewID());
                RelativeLayoutFollow.setId(IDLayout);

                Root.addView(RelativeLayoutFollow);

                RelativeLayout.LayoutParams TextViewFollowParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                TextViewFollowParam.addRule(RelativeLayout.CENTER_IN_PARENT);

                TextView TextViewFollow = new TextView(context);
                TextViewFollow.setLayoutParams(TextViewFollowParam);
                TextViewFollow.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                TextViewFollow.setTypeface(null, Typeface.BOLD);
                TextViewFollow.setId(IDFollow);

                RelativeLayoutFollow.addView(TextViewFollow);

                RelativeLayout.LayoutParams LoadingViewFollowParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 60), RelativeLayout.LayoutParams.WRAP_CONTENT);
                LoadingViewFollowParam.addRule(RelativeLayout.CENTER_IN_PARENT);

                LoadingView LoadingViewFollow = new LoadingView(context);
                LoadingViewFollow.setLayoutParams(LoadingViewFollowParam);
                LoadingViewFollow.SetScale(1.7f);
                LoadingViewFollow.SetColor(R.color.BlueLight);
                LoadingViewFollow.setId(IDLoading);

                RelativeLayoutFollow.addView(LoadingViewFollow);

                RelativeLayout.LayoutParams LinearLayoutRowParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                LinearLayoutRowParam.addRule(RelativeLayout.RIGHT_OF, ImageViewCircleProfile.getId());
                LinearLayoutRowParam.addRule(RelativeLayout.LEFT_OF, RelativeLayoutFollow.getId());
                LinearLayoutRowParam.addRule(RelativeLayout.CENTER_VERTICAL);

                LinearLayout LinearLayoutRow = new LinearLayout(context);
                LinearLayoutRow.setLayoutParams(LinearLayoutRowParam);
                LinearLayoutRow.setOrientation(LinearLayout.VERTICAL);

                Root.addView(LinearLayoutRow);

                TextView TextViewUsername = new TextView(context);
                TextViewUsername.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TextViewUsername.setTextColor(ContextCompat.getColor(context, R.color.Black));
                TextViewUsername.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                TextViewUsername.setId(IDUsername);

                LinearLayoutRow.addView(TextViewUsername);

                TextView TextViewTime = new TextView(context);
                TextViewTime.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TextViewTime.setTextColor(ContextCompat.getColor(context, R.color.BlueGray2));
                TextViewTime.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                TextViewTime.setId(IDTime);

                LinearLayoutRow.addView(TextViewTime);

                RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1));
                ViewLineParam.addRule(RelativeLayout.BELOW, ImageViewCircleProfile.getId());

                View ViewLine = new View(context);
                ViewLine.setLayoutParams(ViewLineParam);
                ViewLine.setBackgroundResource(R.color.Gray);
                ViewLine.setId(IDLine);

                Root.addView(ViewLine);

                return new ViewHolderFollowing(Root, true);
            }

            LoadingView Loading = new LoadingView(context);
            Loading.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56)));
            Loading.setGravity(Gravity.CENTER);
            Loading.Start();

            return new ViewHolderFollowing(Loading, false);
        }

        @Override
        public int getItemViewType(int position)
        {
            return FollowersList.get(position)!= null ? 0 : 1;
        }

        @Override
        public int getItemCount()
        {
            return FollowersList.size();
        }
    }

    private class Struct
    {
        String Username;
        String Avatar;
        int Since;
        boolean Follow;
    }
}
