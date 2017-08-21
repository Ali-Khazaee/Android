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

public class FollowingFragment extends Fragment
{
    private final List<Struct> FollowingList = new ArrayList<>();
    private AdapterFollowing Adapter;

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
        TextViewTitle.setText(getString(R.string.FollowingFragment));
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

        RelativeLayout.LayoutParams RecyclerViewMainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        RecyclerViewMainParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

        LinearLayoutManager LinearLayoutManagerMain = new LinearLayoutManager(context);

        RecyclerViewScroll RecyclerViewScrollMain = new RecyclerViewScroll(LinearLayoutManagerMain)
        {
            @Override
            public void OnLoadMore()
            {
                FollowingList.add(null);
                Adapter.notifyDataSetChanged();

                AndroidNetworking.post(MiscHandler.GetRandomServer("FollowingList"))
                .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
                .addBodyParameter("Skip", String.valueOf(FollowingList.size() - 1))
                .addBodyParameter("Username", Username)
                .setTag("FollowingFragment")
                .build()
                .getAsString(new StringRequestListener()
                {
                    @Override
                    public void onResponse(String Response)
                    {
                        FollowingList.remove(FollowingList.size() - 1);

                        try
                        {
                            JSONObject Result = new JSONObject(Response);

                            if (Result.getInt("Message") == 1000 && !Result.getString("Result").equals(""))
                            {
                                JSONArray ResultList = new JSONArray(Result.getString("Result"));

                                for (int I = 0; I < ResultList.length(); I++)
                                {
                                    JSONObject Following = ResultList.getJSONObject(I);

                                    Struct StructFollowing = new Struct();
                                    StructFollowing.Username = Following.getString("Username");
                                    StructFollowing.Avatar = Following.getString("Avatar");
                                    StructFollowing.Follow = true;
                                    StructFollowing.Since = Following.getInt("Time");

                                    FollowingList.add(StructFollowing);
                                }
                            }
                        }
                        catch (Exception e)
                        {
                            ResetLoading(false);
                            MiscHandler.Debug("FollowingFragment-RequestMore: " + e.toString());
                        }

                        Adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(ANError anError)
                    {
                        ResetLoading(false);
                        FollowingList.remove(FollowingList.size() - 1);
                        Adapter.notifyDataSetChanged();
                    }
                });
            }
        };

        RecyclerView RecyclerViewMain = new RecyclerView(context);
        RecyclerViewMain.setLayoutParams(RecyclerViewMainParam);
        RecyclerViewMain.setLayoutManager(LinearLayoutManagerMain);
        RecyclerViewMain.setAdapter(Adapter = new AdapterFollowing(context));
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
        AndroidNetworking.forceCancel("FollowingFragment");
    }

    private void RetrieveDataFromServer(final Context context, String Username, final RelativeLayout RelativeLayoutLoading, final LoadingView LoadingViewMain, final TextView TextViewTryAgain)
    {
        TextViewTryAgain.setVisibility(View.GONE);
        LoadingViewMain.Start();

        AndroidNetworking.post(MiscHandler.GetRandomServer("FollowingList"))
        .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
        .addBodyParameter("Username", Username)
        .setTag("FollowingFragment")
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
                            JSONObject Following = ResultList.getJSONObject(I);

                            Struct StructFollowing = new Struct();
                            StructFollowing.Username = Following.getString("Username");
                            StructFollowing.Avatar = Following.getString("Avatar");
                            StructFollowing.Follow = true;
                            StructFollowing.Since = Following.getInt("Time");

                            FollowingList.add(StructFollowing);
                        }

                        Adapter.notifyDataSetChanged();
                    }
                }
                catch (Exception e)
                {
                    MiscHandler.Debug("FollowingFragment-RequestNew: " + e.toString());
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

    private class AdapterFollowing extends RecyclerView.Adapter<AdapterFollowing.ViewHolderFollowing>
    {
        private final int ID_PROFILE = MiscHandler.GenerateViewID();
        private final int ID_USERNAME = MiscHandler.GenerateViewID();
        private final int ID_TIME = MiscHandler.GenerateViewID();
        private final int ID_LAYOUT = MiscHandler.GenerateViewID();
        private final int ID_FOLLOW = MiscHandler.GenerateViewID();
        private final int ID_LOADIN = MiscHandler.GenerateViewID();
        private final int ID_LINE = MiscHandler.GenerateViewID();

        private final Context context;

        AdapterFollowing(Context c)
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
                    ImageViewCircleProfile = (ImageViewCircle) view.findViewById(ID_PROFILE);
                    TextViewUsername = (TextView) view.findViewById(ID_USERNAME);
                    TextViewTime = (TextView) view.findViewById(ID_TIME);
                    RelativeLayoutFollow = (RelativeLayout) view.findViewById(ID_LAYOUT);
                    TextViewFollow = (TextView) view.findViewById(ID_FOLLOW);
                    LoadingViewFollow = (LoadingView) view.findViewById(ID_LOADIN);
                    ViewLine = view.findViewById(ID_LINE);
                }
            }
        }

        @Override
        public void onBindViewHolder(final ViewHolderFollowing Holder, int position)
        {
            if (getItemViewType(position) != 0)
                return;

            final int Position = Holder.getAdapterPosition();

            Glide.with(context)
            .load(FollowingList.get(Position).Avatar)
            .placeholder(R.color.BlueGray)
            .override(MiscHandler.ToDimension(context, 40), MiscHandler.ToDimension(context, 40))
            .dontAnimate()
            .into(Holder.ImageViewCircleProfile);

            Holder.ImageViewCircleProfile.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (SharedHandler.GetString(context, "Username").equals(FollowingList.get(Position).Username))
                        return;

                    Bundle bundle = new Bundle();
                    bundle.putString("Username", FollowingList.get(Position).Username);

                    Fragment fragment = new ProfileFragment();
                    fragment.setArguments(bundle);

                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.MainActivityFullContainer, fragment).addToBackStack("ProfileFragment").commit();
                }
            });

            Holder.TextViewUsername.setText(FollowingList.get(Position).Username);
            Holder.TextViewTime.setText((getString(R.string.FollowingFragmentSince) + MiscHandler.GetTimeName(FollowingList.get(Position).Since)));

            if (FollowingList.get(Position).Follow)
            {
                Holder.TextViewFollow.setText(getString(R.string.FollowingFragment));
                Holder.TextViewFollow.setTextColor(ContextCompat.getColor(context, R.color.Gray6));
            }
            else
            {
                Holder.TextViewFollow.setText(getString(R.string.FollowingFragmentFollow));
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
                    .addBodyParameter("Username", FollowingList.get(Position).Username)
                    .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
                    .setTag("FollowingFragment")
                    .build()
                    .getAsString(new StringRequestListener()
                    {
                        @Override
                        public void onResponse(String Response)
                        {
                            Holder.LoadingViewFollow.Stop();
                            Holder.TextViewFollow.setVisibility(View.VISIBLE);

                            try
                            {
                                JSONObject Result = new JSONObject(Response);

                                if (Result.getInt("Message") == 1000)
                                {
                                    if (Result.getBoolean("Follow"))
                                    {
                                        FollowingList.get(Position).Follow = true;
                                        Holder.TextViewTime.setVisibility(View.VISIBLE);
                                        Holder.TextViewTime.setText((getString(R.string.FollowingFragmentSince) + " " + getString(R.string.FollowingFragmentNow)));
                                        Holder.TextViewFollow.setText(getString(R.string.FollowingFragment));
                                        Holder.TextViewFollow.setTextColor(ContextCompat.getColor(context, R.color.Gray6));
                                    }
                                    else
                                    {
                                        FollowingList.get(Position).Follow = false;
                                        Holder.TextViewTime.setVisibility(View.INVISIBLE);
                                        Holder.TextViewFollow.setText(getString(R.string.FollowingFragmentFollow));
                                        Holder.TextViewFollow.setTextColor(ContextCompat.getColor(context, R.color.BlueLight));
                                    }
                                }
                            }
                            catch (Exception e)
                            {
                                MiscHandler.Debug("FollowingFragment-RequestFollow: " + e.toString());
                            }
                        }

                        @Override
                        public void onError(ANError anError)
                        {
                            Holder.LoadingViewFollow.Stop();
                            Holder.TextViewFollow.setVisibility(View.VISIBLE);
                        }
                    });
                }
            });

            if (Position == FollowingList.size() - 1)
                Holder.ViewLine.setVisibility(View.GONE);
            else
                Holder.ViewLine.setVisibility(View.VISIBLE);
        }

        @Override
        public ViewHolderFollowing onCreateViewHolder(ViewGroup parent, int ViewType)
        {
            if (ViewType == 0)
            {
                RelativeLayout RelativeLayoutMain = new RelativeLayout(context);
                RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

                RelativeLayout.LayoutParams ImageViewCircleProfileParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 40), MiscHandler.ToDimension(context, 40));
                ImageViewCircleProfileParam.setMargins(MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10));

                ImageViewCircle ImageViewCircleProfile = new ImageViewCircle(context);
                ImageViewCircleProfile.setLayoutParams(ImageViewCircleProfileParam);
                ImageViewCircleProfile.setId(ID_PROFILE);

                RelativeLayoutMain.addView(ImageViewCircleProfile);

                RelativeLayout.LayoutParams RelativeLayoutFollowParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 90), MiscHandler.ToDimension(context, 35));
                RelativeLayoutFollowParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                RelativeLayoutFollowParam.addRule(RelativeLayout.CENTER_VERTICAL);
                RelativeLayoutFollowParam.setMargins(0, 0, MiscHandler.ToDimension(context, 10), 0);

                RelativeLayout RelativeLayoutFollow = new RelativeLayout(context);
                RelativeLayoutFollow.setLayoutParams(RelativeLayoutFollowParam);
                RelativeLayoutFollow.setId(ID_LAYOUT);

                RelativeLayoutMain.addView(RelativeLayoutFollow);

                RelativeLayout.LayoutParams TextViewFollowParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                TextViewFollowParam.addRule(RelativeLayout.CENTER_IN_PARENT);

                TextView TextViewFollow = new TextView(context);
                TextViewFollow.setLayoutParams(TextViewFollowParam);
                TextViewFollow.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                TextViewFollow.setTypeface(null, Typeface.BOLD);
                TextViewFollow.setId(ID_FOLLOW);

                RelativeLayoutFollow.addView(TextViewFollow);

                RelativeLayout.LayoutParams LoadingViewFollowParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 60), RelativeLayout.LayoutParams.WRAP_CONTENT);
                LoadingViewFollowParam.addRule(RelativeLayout.CENTER_IN_PARENT);

                LoadingView LoadingViewFollow = new LoadingView(context);
                LoadingViewFollow.setLayoutParams(LoadingViewFollowParam);
                LoadingViewFollow.SetScale(1.7f);
                LoadingViewFollow.SetColor(R.color.BlueLight);
                LoadingViewFollow.setId(ID_LOADIN);

                RelativeLayoutFollow.addView(LoadingViewFollow);

                RelativeLayout.LayoutParams LinearLayoutRowParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                LinearLayoutRowParam.addRule(RelativeLayout.RIGHT_OF, ImageViewCircleProfile.getId());
                LinearLayoutRowParam.addRule(RelativeLayout.LEFT_OF, RelativeLayoutFollow.getId());
                LinearLayoutRowParam.addRule(RelativeLayout.CENTER_VERTICAL);

                LinearLayout LinearLayoutRow = new LinearLayout(context);
                LinearLayoutRow.setLayoutParams(LinearLayoutRowParam);
                LinearLayoutRow.setOrientation(LinearLayout.VERTICAL);

                RelativeLayoutMain.addView(LinearLayoutRow);

                TextView TextViewUsername = new TextView(context);
                TextViewUsername.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TextViewUsername.setTextColor(ContextCompat.getColor(context, R.color.Black));
                TextViewUsername.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                TextViewUsername.setId(ID_USERNAME);

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

                RelativeLayoutMain.addView(ViewLine);

                return new ViewHolderFollowing(RelativeLayoutMain, true);
            }

            LoadingView LoadingViewMain = new LoadingView(context);
            LoadingViewMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56)));
            LoadingViewMain.Start();

            return new ViewHolderFollowing(LoadingViewMain, false);
        }

        @Override
        public int getItemViewType(int position)
        {
            return FollowingList.get(position)!= null ? 0 : 1;
        }

        @Override
        public int getItemCount()
        {
            return FollowingList.size();
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
