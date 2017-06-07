package co.biogram.main.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
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
import co.biogram.main.handler.URLHandler;
import co.biogram.main.misc.ImageViewCircle;
import co.biogram.main.misc.LoadingView;

public class FragmentFollowers extends Fragment
{
    private RelativeLayout RelativeLayoutLoading;
    private LoadingView LoadingViewData;
    private TextView TextViewTry;

    private String Username;
    private AdapterFollowers Adapter;
    private boolean LoadingBottom = false;
    private final List<Struct> FollowersList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final Context context = getActivity();

        if (getArguments() != null && !getArguments().getString("Username", "").equals(""))
            Username = getArguments().getString("Username");

        RelativeLayout Root = new RelativeLayout(context);
        Root.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        Root.setBackgroundResource(R.color.White);
        Root.setClickable(true);

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
        TextViewTitle.setText(getString(R.string.FragmentFollowers));
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

        RelativeLayout.LayoutParams RecyclerViewFollowersParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        RecyclerViewFollowersParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

        final LinearLayoutManager LinearLayoutManagerFollowers;

        RecyclerView RecyclerViewFollowers = new RecyclerView(context);
        RecyclerViewFollowers.setLayoutParams(RecyclerViewFollowersParam);
        RecyclerViewFollowers.setLayoutManager(LinearLayoutManagerFollowers = new LinearLayoutManager(context));
        RecyclerViewFollowers.setAdapter(Adapter = new AdapterFollowers(context));
        RecyclerViewFollowers.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int DX, int DY)
            {
                if (!LoadingBottom && (LinearLayoutManagerFollowers.findLastVisibleItemPosition() + 5) > LinearLayoutManagerFollowers.getItemCount())
                {
                    LoadingBottom = true;
                    FollowersList.add(null);
                    Adapter.notifyItemInserted(FollowersList.size());

                    AndroidNetworking.post(URLHandler.GetURL(URLHandler.URL.FOLLOWERS_GET))
                    .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
                    .addBodyParameter("Skip", String.valueOf(FollowersList.size()))
                    .addBodyParameter("Username", Username)
                    .setTag("FragmentFollowers")
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
                                    JSONArray ResultList = new JSONArray(Result.getString("Result"));

                                    for (int I = 0; I < ResultList.length(); I++)
                                    {
                                        JSONObject Followers = ResultList.getJSONObject(I);

                                        Struct StructFollowers = new Struct();
                                        StructFollowers.Username = Followers.getString("Username");
                                        StructFollowers.Avatar = Followers.getString("Avatar");

                                        FollowersList.add(StructFollowers);
                                    }
                                }
                            }
                            catch (Exception e)
                            {
                                // Leave Me Alone
                            }

                            LoadingBottom = false;
                            FollowersList.remove(FollowersList.size() - 1);
                            Adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onError(ANError anError)
                        {
                            LoadingBottom = false;
                            FollowersList.remove(FollowersList.size() - 1);
                            Adapter.notifyItemRemoved(FollowersList.size());

                            MiscHandler.Toast(context, getString(R.string.GeneralCheckInternet));
                        }
                    });
                }
            }
        });

        Root.addView(RecyclerViewFollowers);

        RelativeLayout.LayoutParams RelativeLayoutLoadingParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        RelativeLayoutLoadingParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

        RelativeLayoutLoading = new RelativeLayout(context);
        RelativeLayoutLoading.setLayoutParams(RelativeLayoutLoadingParam);
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
        TextViewTry.setTextColor(ContextCompat.getColor(context, R.color.BlueGray2));
        TextViewTry.setText(getString(R.string.GeneralTryAgain));
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
        AndroidNetworking.cancel("FragmentFollowers");
    }

    private void RetrieveDataFromServer(final Context context)
    {
        TextViewTry.setVisibility(View.GONE);
        LoadingViewData.Start();

        AndroidNetworking.post(URLHandler.GetURL(URLHandler.URL.FOLLOWERS_GET))
        .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
        .addBodyParameter("Username", Username)
        .setTag("FragmentFollowers")
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
                        JSONArray ResultList = new JSONArray(Result.getString("Result"));

                        for (int I = 0; I < ResultList.length(); I++)
                        {
                            JSONObject Followers = ResultList.getJSONObject(I);

                            Struct StructFollowers = new Struct();
                            StructFollowers.Username = Followers.getString("Username");
                            StructFollowers.Avatar = Followers.getString("Avatar");

                            FollowersList.add(StructFollowers);
                        }

                        Adapter.notifyDataSetChanged();
                    }
                }
                catch (Exception e)
                {
                    // Leave Me Alone
                }

                RelativeLayoutLoading.setVisibility(View.GONE);
                TextViewTry.setVisibility(View.GONE);
                LoadingViewData.Stop();
            }

            @Override
            public void onError(ANError anError)
            {
                MiscHandler.Toast(context, getString(R.string.GeneralCheckInternet));
                TextViewTry.setVisibility(View.VISIBLE);
                LoadingViewData.Stop();
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

                    Fragment fragment = new FragmentProfile();
                    fragment.setArguments(bundle);

                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.ActivityMainFullContainer, fragment).addToBackStack("FragmentProfile").commit();
                }
            });

            Holder.TextViewUsername.setText(FollowersList.get(Position).Username);

            String Since = getString(R.string.FragmentFollowersSince) + MiscHandler.GetTimeName(FollowersList.get(Position).Since);

            Holder.TextViewTime.setText(Since);

            if (FollowersList.get(Position).Username.equals(SharedHandler.GetString(context, "Username")))
                Holder.RelativeLayoutFollow.setVisibility(View.GONE);

            Holder.RelativeLayoutFollow.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Holder.TextViewFollow.setVisibility(View.GONE);
                    Holder.LoadingViewFollow.Start();

                    AndroidNetworking.post(URLHandler.GetURL(URLHandler.URL.FOLLOW))
                    .addBodyParameter("Username", FollowersList.get(Position).Username)
                    .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
                    .setTag("FragmentFollowers")
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
                                        Holder.TextViewFollow.setText(getString(R.string.FragmentFollowing));
                                    else
                                        Holder.TextViewFollow.setText(getString(R.string.FragmentFollowingFollow));

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

                RelativeLayout.LayoutParams ImageViewCircleProfileParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 55), MiscHandler.ToDimension(context, 55));
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

                GradientDrawable ShapeButton = new GradientDrawable();
                ShapeButton.setShape(GradientDrawable.RECTANGLE);
                ShapeButton.setCornerRadii(new float[] { 8, 8, 8, 8, 8, 8, 8, 8 });
                ShapeButton.setStroke(3, ContextCompat.getColor(context, R.color.BlueLight));

                RelativeLayout RelativeLayoutFollow = new RelativeLayout(context);
                RelativeLayoutFollow.setLayoutParams(RelativeLayoutFollowParam);
                RelativeLayoutFollow.setBackground(ShapeButton);
                RelativeLayoutFollow.setId(MiscHandler.GenerateViewID());
                RelativeLayoutFollow.setId(IDLayout);

                Root.addView(RelativeLayoutFollow);

                RelativeLayout.LayoutParams TextViewFollowParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                TextViewFollowParam.addRule(RelativeLayout.CENTER_IN_PARENT);

                TextView TextViewFollow = new TextView(context);
                TextViewFollow.setLayoutParams(TextViewFollowParam);
                TextViewFollow.setTextColor(ContextCompat.getColor(context, R.color.BlueLight));
                TextViewFollow.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                TextViewFollow.setId(IDFollow);
                TextViewFollow.setText(getString(R.string.FragmentFollowing));

                RelativeLayoutFollow.addView(TextViewFollow);

                RelativeLayout.LayoutParams LoadingViewFollowParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 60), RelativeLayout.LayoutParams.WRAP_CONTENT);
                LoadingViewFollowParam.addRule(RelativeLayout.CENTER_IN_PARENT);

                LoadingView LoadingViewFollow = new LoadingView(context);
                LoadingViewFollow.setLayoutParams(LoadingViewFollowParam);
                LoadingViewFollow.SetShow(true);
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
            Loading.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), MiscHandler.ToDimension(context, 56)));
            Loading.SetShow(true);
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
    }
}
