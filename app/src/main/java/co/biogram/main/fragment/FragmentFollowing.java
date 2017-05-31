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
import co.biogram.main.handler.URLHandler;
import co.biogram.main.misc.ImageViewCircle;
import co.biogram.main.misc.LoadingView;

public class FragmentFollowing extends Fragment
{
    private RelativeLayout RelativeLayoutLoading;
    private LoadingView LoadingViewData;
    private TextView TextViewTry;

    private String ID;
    private AdapterFollowing Adapter;
    private boolean LoadingBottom = false;
    private final List<Struct> FollowingList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final Context context = getActivity();

        if (getArguments() != null && !getArguments().getString("ID", "").equals(""))
            ID = getArguments().getString("ID");

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
        TextViewTitle.setText(getString(R.string.FragmentFollowing));
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

        RelativeLayout.LayoutParams RecyclerViewFollowingParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        RecyclerViewFollowingParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

        RecyclerView RecyclerViewFollowing = new RecyclerView(context);
        RecyclerViewFollowing.setLayoutParams(RecyclerViewFollowingParam);
        RecyclerViewFollowing.setLayoutManager(new LinearLayoutManager(context));
        RecyclerViewFollowing.setAdapter(Adapter = new AdapterFollowing(context));
        RecyclerViewFollowing.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int DX, int DY)
            {
                if (!LoadingBottom && (((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition() + 2) > recyclerView.getAdapter().getItemCount())
                {
                    LoadingBottom = true;
                    FollowingList.add(null);
                    Adapter.notifyItemInserted(FollowingList.size());

                    AndroidNetworking.post(URLHandler.GetURL(URLHandler.URL.FOLLOWING_GET))
                    .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
                    .addBodyParameter("Skip", String.valueOf(FollowingList.size()))
                    .addBodyParameter("ID", ID)
                    .setTag("FragmentFollowing")
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
                                        JSONObject Following = ResultList.getJSONObject(I);

                                        Struct StructFollow = new Struct();
                                        StructFollow.Username = Following.getString("Username");
                                        StructFollow.Avatar = Following.getString("Avatar");
                                        StructFollow.Since = Following.getInt("Since");

                                        FollowingList.add(StructFollow);
                                    }

                                    Adapter.notifyDataSetChanged();
                                }
                            }
                            catch (Exception e)
                            {
                                // Leave Me Alone
                            }
                        }

                        @Override
                        public void onError(ANError anError)
                        {
                            LoadingBottom = false;
                            FollowingList.remove(FollowingList.size() - 1);
                            Adapter.notifyItemRemoved(FollowingList.size());

                            MiscHandler.Toast(getActivity(), getString(R.string.GeneralCheckInternet));
                        }
                    });
                }
            }
        });

        Root.addView(RecyclerViewFollowing);

        RelativeLayout.LayoutParams RelativeLayoutLoadingParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        RelativeLayoutLoadingParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

        RelativeLayoutLoading = new RelativeLayout(context);
        RelativeLayoutLoading.setLayoutParams(RelativeLayoutLoadingParam);
        RelativeLayoutLoading.setBackgroundResource(R.color.White);

        Root.addView(RelativeLayoutLoading);

        RelativeLayout.LayoutParams LoadingViewDataParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
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
        AndroidNetworking.cancel("FragmentFollowing");
    }

    private void RetrieveDataFromServer(Context context)
    {
        TextViewTry.setVisibility(View.GONE);
        LoadingViewData.Start();

        AndroidNetworking.post(URLHandler.GetURL(URLHandler.URL.FOLLOWING_GET))
        .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
        .addBodyParameter("ID", ID)
        .setTag("FragmentFollowing")
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

                            Struct StructFollow = new Struct();
                            StructFollow.Username = Following.getString("Username");
                            StructFollow.Avatar = Following.getString("Avatar");
                            StructFollow.Since = Following.getInt("Since");

                            FollowingList.add(StructFollow);
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
                MiscHandler.Toast(getActivity(), getString(R.string.GeneralCheckInternet));
                TextViewTry.setVisibility(View.VISIBLE);
                LoadingViewData.Stop();
            }
        });
    }

    private class AdapterFollowing extends RecyclerView.Adapter<AdapterFollowing.ViewHolderFollowing>
    {
        private Context context;

        private final int ID_ICON = MiscHandler.GenerateViewID();
        private final int ID_NAME = MiscHandler.GenerateViewID();
        private final int ID_TIME = MiscHandler.GenerateViewID();
        private final int ID_LINE = MiscHandler.GenerateViewID();

        AdapterFollowing(Context c)
        {
            context = c;
        }

        class ViewHolderFollowing extends RecyclerView.ViewHolder
        {
            ImageViewCircle ImageViewCircleProfile;
            TextView TextViewUsername;
            TextView TextViewTime;
            View ViewLine;

            ViewHolderFollowing(View view, boolean Content)
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
        public void onBindViewHolder(ViewHolderFollowing Holder, int Position)
        {
            if (FollowingList.get(Position) == null)
                return;

            Glide.with(context)
            .load(FollowingList.get(Position).Avatar)
            .placeholder(R.color.BlueGray)
            .error(R.color.BlueGray)
            .override(MiscHandler.ToDimension(context, 55), MiscHandler.ToDimension(context, 55))
            .into(Holder.ImageViewCircleProfile);

            Holder.TextViewUsername.setText(FollowingList.get(Position).Username);

            String Since = getString(R.string.FragmentFollowingSince) + MiscHandler.GetTimeName(FollowingList.get(Position).Since);

            Holder.TextViewTime.setText(Since);

            // TODO Button Following

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
                RelativeLayout Root = new RelativeLayout(context);
                Root.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

                RelativeLayout.LayoutParams ImageViewCircleProfileParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 55), MiscHandler.ToDimension(context, 55));
                ImageViewCircleProfileParam.setMargins(MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10));

                ImageViewCircle ImageViewCircleProfile = new ImageViewCircle(context);
                ImageViewCircleProfile.setLayoutParams(ImageViewCircleProfileParam);
                ImageViewCircleProfile.setImageResource(R.color.BlueGray);
                ImageViewCircleProfile.setId(ID_ICON);

                Root.addView(ImageViewCircleProfile);

                RelativeLayout.LayoutParams RelativeLayoutButtonParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 60), MiscHandler.ToDimension(context, 35));
                RelativeLayoutButtonParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                RelativeLayoutButtonParam.addRule(RelativeLayout.CENTER_VERTICAL);

                RelativeLayout RelativeLayoutButton = new RelativeLayout(context);
                RelativeLayoutButton.setLayoutParams(RelativeLayoutButtonParam);

                Root.addView(RelativeLayoutButton);

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
        boolean IsFollowing;

        Struct()
        {

        }
    }
}
