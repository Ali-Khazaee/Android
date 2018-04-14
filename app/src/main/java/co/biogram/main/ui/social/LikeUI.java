package co.biogram.main.ui.social;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import co.biogram.main.R;
import co.biogram.main.fragment.FragmentView;
import co.biogram.main.handler.GlideApp;
import co.biogram.main.handler.Misc;
import co.biogram.main.handler.OnScrollRecyclerView;
import co.biogram.main.handler.SharedHandler;
import co.biogram.main.ui.view.CircleImageView;
import co.biogram.main.ui.view.LoadingView;
import co.biogram.main.ui.view.TextView;

public class LikeUI extends FragmentView
{
    private List<Struct> PeopleList = new ArrayList<>();
    private AdapterLike Adapter;
    private boolean IsComment;
    private String ID;

    public LikeUI(String id, boolean isComment)
    {
        ID = id;
        IsComment= isComment;
    }

    @Override
    public void OnCreate()
    {
        LinearLayout LinearLayoutMain = new LinearLayout(Activity);
        LinearLayoutMain.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        LinearLayoutMain.setBackgroundResource(Misc.IsDark() ? R.color.GroundDark : R.color.GroundWhite);
        LinearLayoutMain.setOrientation(LinearLayout.VERTICAL);
        LinearLayoutMain.setClickable(true);

        RelativeLayout RelativeLayoutHeader = new RelativeLayout(Activity);
        RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56)));
        RelativeLayoutHeader.setBackgroundResource(Misc.IsDark() ? R.color.ActionBarDark : R.color.ActionBarWhite);

        LinearLayoutMain.addView(RelativeLayoutHeader);

        RelativeLayout.LayoutParams ImageViewBackParam = new RelativeLayout.LayoutParams(Misc.ToDP(56), Misc.ToDP(56));
        ImageViewBackParam.addRule(Misc.Align("R"));

        ImageView ImageViewBack = new ImageView(Activity);
        ImageViewBack.setLayoutParams(ImageViewBackParam);
        ImageViewBack.setPadding(Misc.ToDP(13), Misc.ToDP(13), Misc.ToDP(13), Misc.ToDP(13));
        ImageViewBack.setImageResource(Misc.IsRTL() ? R.drawable.__general_back_blue_rtl : R.drawable.__general_back_blue);
        ImageViewBack.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { Activity.onBackPressed(); } });
        ImageViewBack.setId(Misc.generateViewId());

        RelativeLayoutHeader.addView(ImageViewBack);

        RelativeLayout.LayoutParams TextViewTitleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTitleParam.addRule(Misc.AlignTo("R"), ImageViewBack.getId());
        TextViewTitleParam.addRule(RelativeLayout.CENTER_VERTICAL);

        TextView TextViewTitle = new TextView(Activity, 16, true);
        TextViewTitle.setLayoutParams(TextViewTitleParam);
        TextViewTitle.SetColor(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite);
        TextViewTitle.setText(Misc.String(R.string.LikeUI));
        TextViewTitle.setPadding(0, Misc.ToDP(6), 0, 0);

        RelativeLayoutHeader.addView(TextViewTitle);

        View ViewLine = new View(Activity);
        ViewLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1)));
        ViewLine.setBackgroundResource(Misc.IsDark() ? R.color.LineDark : R.color.LineWhite);

        LinearLayoutMain.addView(ViewLine);

        RelativeLayout RelativeLayoutContent = new RelativeLayout(Activity);
        RelativeLayoutContent.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

        LinearLayoutMain.addView(RelativeLayoutContent);

        LinearLayoutManager LinearLayoutManagerMain = new LinearLayoutManager(Activity);

        RecyclerView RecyclerViewMain = new RecyclerView(Activity);
        RecyclerViewMain.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        RecyclerViewMain.setAdapter(Adapter = new AdapterLike());
        RecyclerViewMain.setLayoutManager(LinearLayoutManagerMain);
        RecyclerViewMain.addOnScrollListener(new OnScrollRecyclerView(LinearLayoutManagerMain) { @Override public void OnLoadMore() { Update(null); } });

        RelativeLayoutContent.addView(RecyclerViewMain);

        LoadingView LoadingViewMain = new LoadingView(Activity);
        LoadingViewMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        LoadingViewMain.setBackgroundResource(R.color.GroundWhite);
        LoadingViewMain.Start();

        RelativeLayoutContent.addView(LoadingViewMain);

        Update(LoadingViewMain);

        ViewMain = LinearLayoutMain;
    }

    @Override
    public void OnPause()
    {
        AndroidNetworking.forceCancel("LikeUI");
    }

    private void Update(final LoadingView Loading)
    {
        if (IsComment)
        {
            AndroidNetworking.post(Misc.GetRandomServer("PostCommentLikeList"))
            .addBodyParameter("Skip", String.valueOf(PeopleList.size()))
            .addBodyParameter("CommentID", ID)
            .addHeaders("Token", SharedHandler.GetString("Token"))
            .setTag("LikeUI")
            .build()
            .getAsString(new StringRequestListener()
            {
                @Override
                public void onResponse(String Response)
                {
                    try
                    {
                        JSONObject Result = new JSONObject(Response);

                        if (Result.getInt("Message") == 0 && !Result.isNull("Result"))
                        {
                            JSONArray ResultList = new JSONArray(Result.getString("Result"));

                            for (int K = 0; K < ResultList.length(); K++)
                            {
                                JSONObject D = ResultList.getJSONObject(K);

                                PeopleList.add(new Struct(D.getString("ID"), D.getString("Name"), D.getString("Username"), D.getString("Avatar"), D.getBoolean("Follow")));
                            }

                            Adapter.notifyDataSetChanged();
                        }
                    }
                    catch (Exception e)
                    {
                        Misc.Debug("LikeUI-Update: " + e.toString());
                    }

                    if (Loading != null)
                    {
                        Loading.Stop();
                        Loading.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onError(ANError e)
                {
                    if (Loading != null)
                    {
                        Loading.Stop();
                        Loading.setVisibility(View.GONE);
                    }
                }
            });

            return;
        }

        AndroidNetworking.post(Misc.GetRandomServer("PostLikeList"))
        .addBodyParameter("Skip", String.valueOf(PeopleList.size()))
        .addBodyParameter("PostID", ID)
        .addHeaders("Token", SharedHandler.GetString("Token"))
        .setTag("LikeUI")
        .build()
        .getAsString(new StringRequestListener()
        {
            @Override
            public void onResponse(String Response)
            {
                try
                {
                    JSONObject Result = new JSONObject(Response);

                    if (Result.getInt("Message") == 0 && !Result.isNull("Result"))
                    {
                        JSONArray ResultList = new JSONArray(Result.getString("Result"));

                        for (int K = 0; K < ResultList.length(); K++)
                        {
                            JSONObject D = ResultList.getJSONObject(K);

                            PeopleList.add(new Struct(D.getString("ID"), D.getString("Name"), D.getString("Username"), D.getString("Avatar"), D.getBoolean("Follow")));
                        }

                        Adapter.notifyDataSetChanged();
                    }
                }
                catch (Exception e)
                {
                    Misc.Debug("LikeUI-Update: " + e.toString());
                }

                if (Loading != null)
                {
                    Loading.Stop();
                    Loading.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(ANError e)
            {
                if (Loading != null)
                {
                    Loading.Stop();
                    Loading.setVisibility(View.GONE);
                }
            }
        });
    }

    private class AdapterLike extends RecyclerView.Adapter<AdapterLike.ViewHolderMain>
    {
        private int ID_PROFILE = Misc.generateViewId();
        private int ID_NAME = Misc.generateViewId();
        private int ID_USERNAME = Misc.generateViewId();
        private int ID_FOLLOW = Misc.generateViewId();
        private int ID_LINE = Misc.generateViewId();

        private GradientDrawable DrawableFollow;
        private GradientDrawable DrawableUnfollow;

        AdapterLike()
        {
            DrawableFollow = new GradientDrawable();
            DrawableFollow.setColor(Misc.Color(R.color.Primary));
            DrawableFollow.setCornerRadius(Misc.ToDP(20));

            DrawableUnfollow = new GradientDrawable();
            DrawableUnfollow.setColor(Misc.Color(R.color.Gray));
            DrawableUnfollow.setCornerRadius(Misc.ToDP(20));
        }

        class ViewHolderMain extends RecyclerView.ViewHolder
        {
            CircleImageView CircleImageViewProfile;
            TextView TextViewName;
            TextView TextViewUsername;
            TextView TextViewFollow;
            View ViewLine;

            ViewHolderMain(View v, boolean NoContent)
            {
                super(v);

                if (NoContent)
                    return;

                CircleImageViewProfile = v.findViewById(ID_PROFILE);
                TextViewName = v.findViewById(ID_NAME);
                TextViewUsername = v.findViewById(ID_USERNAME);
                TextViewFollow = v.findViewById(ID_FOLLOW);
                ViewLine = v.findViewById(ID_LINE);
            }
        }

        @Override
        public void onBindViewHolder(ViewHolderMain Holder, int p)
        {
            if (Holder.getItemViewType() == 0)
                return;

            final int Position = Holder.getAdapterPosition();

            GlideApp.with(Activity).load(PeopleList.get(Position).Profile).placeholder(R.drawable._general_avatar).into(Holder.CircleImageViewProfile);
            Holder.CircleImageViewProfile.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    // TODO Open Profile
                }
            });

            Holder.TextViewName.setText(PeopleList.get(Position).Name);
            Holder.TextViewName.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    // TODO Open Profile
                }
            });

            Holder.TextViewUsername.setText(("@" + PeopleList.get(Position).Username));
            Holder.TextViewUsername.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    // TODO Open Profile
                }
            });

            if (SharedHandler.GetString("ID").equals(PeopleList.get(Position).ID))
                Holder.TextViewFollow.setVisibility(View.GONE);
            else
                Holder.TextViewFollow.setVisibility(View.VISIBLE);

            if (PeopleList.get(Position).Follow)
            {
                Holder.TextViewFollow.SetColor(R.color.TextDark);
                Holder.TextViewFollow.setText(Activity.getString(R.string.LikeUIUnfollow));
                Holder.TextViewFollow.setBackground(DrawableUnfollow);
            }
            else
            {
                Holder.TextViewFollow.SetColor(R.color.TextDark);
                Holder.TextViewFollow.setText(Activity.getString(R.string.LikeUIFollow));
                Holder.TextViewFollow.setBackground(DrawableFollow);
            }

            Holder.TextViewFollow.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    AndroidNetworking.post(Misc.GetRandomServer("ProfileFollow"))
                    .addBodyParameter("Username", PeopleList.get(Position).Username)
                    .addHeaders("Token", SharedHandler.GetString("Token"))
                    .setTag("LikeUI")
                    .build()
                    .getAsString(null);

                    PeopleList.get(Position).Follow = !PeopleList.get(Position).Follow;
                    notifyDataSetChanged();

                    Misc.Toast(PeopleList.get(Position).Follow ? Activity.getString(R.string.LikeUIFollowed) : Activity.getString(R.string.LikeUIUnfollowed));
                }
            });

            if (Position == PeopleList.size() - 1)
                Holder.ViewLine.setVisibility(View.GONE);
            else
                Holder.ViewLine.setVisibility(View.VISIBLE);
        }

        @Override
        public ViewHolderMain onCreateViewHolder(ViewGroup p, int ViewType)
        {
            if (ViewType == 0)
            {
                RelativeLayout RelativeLayoutMain = new RelativeLayout(Activity);
                RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

                RelativeLayout.LayoutParams LinearLayoutMainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                LinearLayoutMainParam.addRule(RelativeLayout.CENTER_IN_PARENT);

                LinearLayout LinearLayoutMain = new LinearLayout(Activity);
                LinearLayoutMain.setLayoutParams(LinearLayoutMainParam);
                LinearLayoutMain.setOrientation(LinearLayout.VERTICAL);
                LinearLayoutMain.setGravity(Gravity.CENTER);

                RelativeLayoutMain.addView(LinearLayoutMain);

                RelativeLayout.LayoutParams ImageViewContentParam = new RelativeLayout.LayoutParams(Misc.ToDP(56), Misc.ToDP(56));
                ImageViewContentParam.addRule(RelativeLayout.CENTER_IN_PARENT);

                ImageView ImageViewContent = new CircleImageView(Activity);
                ImageViewContent.setLayoutParams(ImageViewContentParam);
                ImageViewContent.setImageResource(R.drawable._general_like);
                ImageViewContent.setId(Misc.generateViewId());

                LinearLayoutMain.addView(ImageViewContent);

                RelativeLayout.LayoutParams TextViewContentParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                TextViewContentParam.addRule(RelativeLayout.BELOW, ImageViewContent.getId());
                TextViewContentParam.addRule(RelativeLayout.CENTER_HORIZONTAL);

                TextView TextViewContent = new TextView(Activity, 16, false);
                TextViewContent.setLayoutParams(TextViewContentParam);
                TextViewContent.SetColor(R.color.Gray);
                TextViewContent.setText(Activity.getString(R.string.LikeUINo));

                LinearLayoutMain.addView(TextViewContent);

                return new ViewHolderMain(RelativeLayoutMain, true);
            }

            StateListDrawable StatePress = new StateListDrawable();
            StatePress.addState(new int[] { android.R.attr.state_pressed }, new ColorDrawable(Color.parseColor("#b0eeeeee")));

            RelativeLayout RelativeLayoutMain = new RelativeLayout(Activity);
            RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            RelativeLayoutMain.setBackground(StatePress);
            RelativeLayoutMain.setOnClickListener(null);

            RelativeLayout.LayoutParams CircleImageViewProfileParam = new RelativeLayout.LayoutParams(Misc.ToDP(48), Misc.ToDP(48));
            CircleImageViewProfileParam.setMargins(Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10));
            CircleImageViewProfileParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

            CircleImageView CircleImageViewProfile = new CircleImageView(Activity);
            CircleImageViewProfile.setLayoutParams(CircleImageViewProfileParam);
            CircleImageViewProfile.SetBorderColor(R.color.LineWhite);
            CircleImageViewProfile.SetBorderWidth(1);
            CircleImageViewProfile.setId(ID_PROFILE);

            RelativeLayoutMain.addView(CircleImageViewProfile);

            RelativeLayout.LayoutParams TextViewNameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewNameParam.addRule(RelativeLayout.RIGHT_OF, ID_PROFILE);
            TextViewNameParam.setMargins(0, Misc.ToDP(13), 0, 0);

            TextView TextViewName = new TextView(Activity, 14, false);
            TextViewName.setLayoutParams(TextViewNameParam);
            TextViewName.SetColor(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite);
            TextViewName.setId(ID_NAME);

            RelativeLayoutMain.addView(TextViewName);

            RelativeLayout.LayoutParams TextViewUsernameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewUsernameParam.addRule(RelativeLayout.RIGHT_OF, ID_PROFILE);
            TextViewUsernameParam.setMargins(0, Misc.ToDP(35), 0, 0);

            TextView TextViewUsername = new TextView(Activity, 12, false);
            TextViewUsername.setLayoutParams(TextViewUsernameParam);
            TextViewUsername.SetColor(R.color.Gray);
            TextViewUsername.setId(ID_USERNAME);

            RelativeLayoutMain.addView(TextViewUsername);

            RelativeLayout.LayoutParams TextViewFollowParam = new RelativeLayout.LayoutParams(Misc.ToDP(90), RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewFollowParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            TextViewFollowParam.addRule(RelativeLayout.CENTER_VERTICAL);
            TextViewFollowParam.setMargins(0, 0, Misc.ToDP(10), 0);

            TextView TextViewFollow = new TextView(Activity, 12, true);
            TextViewFollow.setLayoutParams(TextViewFollowParam);
            TextViewFollow.setPadding(Misc.ToDP(8), Misc.ToDP(5), Misc.ToDP(8), Misc.ToDP(3));
            TextViewFollow.setGravity(Gravity.CENTER_HORIZONTAL);
            TextViewFollow.setId(ID_FOLLOW);

            RelativeLayoutMain.addView(TextViewFollow);

            RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1));
            ViewLineParam.addRule(RelativeLayout.BELOW, ID_PROFILE);

            View ViewLine = new View(Activity);
            ViewLine.setLayoutParams(ViewLineParam);
            ViewLine.setBackgroundResource(Misc.IsDark() ? R.color.LineDark : R.color.LineWhite);
            ViewLine.setId(ID_LINE);

            RelativeLayoutMain.addView(ViewLine);

            return new ViewHolderMain(RelativeLayoutMain, false);
        }

        @Override
        public int getItemViewType(int Position)
        {
            return PeopleList.size() == 0 ? 0 : 1;
        }

        @Override
        public int getItemCount()
        {
            return PeopleList.size() == 0 ? 1 : PeopleList.size();
        }
    }

    private class Struct
    {
        String ID;
        String Name;
        String Username;
        String Profile;
        boolean Follow;

        Struct(String I, String N, String U,String P, boolean F)
        {
            ID = I;
            Name = N;
            Username = U;
            Profile = P;
            Follow = F;
        }
    }
}
