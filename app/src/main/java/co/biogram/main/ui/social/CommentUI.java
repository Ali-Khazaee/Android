package co.biogram.main.ui.social;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import co.biogram.main.ui.view.TextView;

public class CommentUI extends FragmentView
{
    private List<Struct> CommentList = new ArrayList<>();
    private AdapterComment Adapter;
    private String PostID;

    public CommentUI(String id)
    {
        PostID = id;
    }

    @Override
    public void OnCreate()
    {
        RelativeLayout RelativeLayoutMain = new RelativeLayout(GetActivity());
        RelativeLayoutMain.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        RelativeLayoutMain.setBackgroundResource(Misc.IsDark() ? R.color.GroundDark : R.color.GroundWhite);
        RelativeLayoutMain.setClickable(true);

        RelativeLayout RelativeLayoutHeader = new RelativeLayout(GetActivity());
        RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56)));
        RelativeLayoutHeader.setBackgroundResource(Misc.IsDark() ? R.color.ActionBarDark : R.color.ActionBarWhite);
        RelativeLayoutHeader.setId(Misc.ViewID());

        RelativeLayoutMain.addView(RelativeLayoutHeader);

        RelativeLayout.LayoutParams ImageViewBackParam = new RelativeLayout.LayoutParams(Misc.ToDP(56), Misc.ToDP(56));
        ImageViewBackParam.addRule(Misc.Align("R"));

        ImageView ImageViewBack = new ImageView(GetActivity());
        ImageViewBack.setLayoutParams(ImageViewBackParam);
        ImageViewBack.setPadding(Misc.ToDP(13), Misc.ToDP(13), Misc.ToDP(13), Misc.ToDP(13));
        ImageViewBack.setImageResource(Misc.IsRTL() ? R.drawable.back_blue_rtl : R.drawable.back_blue);
        ImageViewBack.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { GetActivity().onBackPressed(); } });
        ImageViewBack.setId(Misc.ViewID());

        RelativeLayoutHeader.addView(ImageViewBack);

        RelativeLayout.LayoutParams TextViewTitleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTitleParam.addRule(Misc.AlignTo("R"), ImageViewBack.getId());
        TextViewTitleParam.addRule(RelativeLayout.CENTER_VERTICAL);

        TextView TextViewTitle = new TextView(GetActivity(), 16, true);
        TextViewTitle.setLayoutParams(TextViewTitleParam);
        TextViewTitle.SetColor(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite);
        TextViewTitle.setText(Misc.String(R.string.CommentUI));
        TextViewTitle.setPadding(0, Misc.ToDP(6), 0, 0);

        RelativeLayoutHeader.addView(TextViewTitle);

        RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1));
        ViewLineParam.addRule(RelativeLayout.BELOW, RelativeLayoutHeader.getId());

        View ViewLine = new View(GetActivity());
        ViewLine.setLayoutParams(ViewLineParam);
        ViewLine.setBackgroundResource(Misc.IsDark() ? R.color.LineDark : R.color.LineWhite);
        ViewLine.setId(Misc.ViewID());

        RelativeLayoutMain.addView(ViewLine);

        RelativeLayout.LayoutParams RelativeLayoutBottomParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56));
        RelativeLayoutBottomParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        RelativeLayout RelativeLayoutBottom = new RelativeLayout(GetActivity());
        RelativeLayoutBottom.setLayoutParams(RelativeLayoutBottomParam);
        RelativeLayoutBottom.setBackgroundResource(Misc.IsDark() ? R.color.ActionBarDark : R.color.ActionBarWhite);
        RelativeLayoutBottom.setId(Misc.ViewID());

        RelativeLayoutMain.addView(RelativeLayoutBottom);

        RelativeLayout.LayoutParams RecyclerViewMainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        RecyclerViewMainParam.addRule(RelativeLayout.ABOVE, RelativeLayoutBottom.getId());
        RecyclerViewMainParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

        LinearLayoutManager LinearLayoutManagerMain = new LinearLayoutManager(GetActivity());

        RecyclerView RecyclerViewMain = new RecyclerView(GetActivity());
        RecyclerViewMain.setLayoutParams(RecyclerViewMainParam);
        RecyclerViewMain.setAdapter(Adapter = new AdapterComment());
        RecyclerViewMain.setLayoutManager(LinearLayoutManagerMain);
        RecyclerViewMain.addOnScrollListener(new OnScrollRecyclerView(LinearLayoutManagerMain) { @Override public void OnLoadMore() { Update(); } });

        RelativeLayoutMain.addView(RecyclerViewMain);

        Update();

        CommentList.add(new Struct("A", "Ali", "Avatar", "Salam Sob bekheyr", 51, 1518266206 , false));
        CommentList.add(new Struct("A", "Ali", "Avatar", "Salam Sob bekheyr", 51, 1514666206 , true));
        CommentList.add(new Struct("A", "Ali", "https://avatars3.githubusercontent.com/u/4707579?s=460&v=4", "سلام صبح بخیر", 51, 1518666206 , true));
        CommentList.add(new Struct("A", "Ali", "Avatar", "#Morning سلام دوستان خوبم khobid ?", 51, 1518666206 , true));
        CommentList.add(new Struct("A", "Ali", "Avatar", "Salam Sob bekheyr", 51, 1518166206 , true));

        Adapter.notifyDataSetChanged();

        ViewMain = RelativeLayoutMain;
    }

    @Override
    public void OnPause()
    {
        AndroidNetworking.forceCancel("CommentUI");
    }

    private void Update()
    {
        AndroidNetworking.post(Misc.GetRandomServer("PostCommentList"))
        .addBodyParameter("Skip", String.valueOf(CommentList.size()))
        .addBodyParameter("PostID", PostID)
        .addHeaders("Token", SharedHandler.GetString(GetActivity(), "Token"))
        .setTag("CommentUI")
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

                            CommentList.add(new Struct(D.getString("ID"), D.getString("Username"), D.getString("Avatar"), D.getString("Message"), D.getInt("Count"), D.getInt("Time"), D.getBoolean("Like")));
                        }

                        Adapter.notifyDataSetChanged();
                    }
                }
                catch (Exception e)
                {
                    Misc.Debug("LikeUI-Update: " + e.toString());
                }
            }

            @Override public void onError(ANError e) { }
        });
    }

    private class AdapterComment extends RecyclerView.Adapter<AdapterComment.ViewHolderMain>
    {
        private int ID_PROFILE = Misc.ViewID();
        private int ID_USERNAME = Misc.ViewID();
        private int ID_TIME = Misc.ViewID();
        private int ID_MESSAGE = Misc.ViewID();
        private int ID_LIKE = Misc.ViewID();
        private int ID_SHORTCUT = Misc.ViewID();
        private int ID_DELETE = Misc.ViewID();
        private int ID_LIKE_COUNT = Misc.ViewID();
        private int ID_LINE = Misc.ViewID();

        class ViewHolderMain extends RecyclerView.ViewHolder
        {
            CircleImageView CircleImageViewProfile;
            TextView TextViewUsername;
            TextView TextViewTime;
            TextView TextViewMessage;
            ImageView ImageViewLike;
            ImageView ImageViewShort;
            ImageView ImageViewDelete;
            TextView TextViewLikeCount;
            View ViewLine;

            ViewHolderMain(View v, boolean NoContent)
            {
                super(v);

                if (NoContent)
                    return;

                CircleImageViewProfile = v.findViewById(ID_PROFILE);
                TextViewUsername = v.findViewById(ID_USERNAME);
                TextViewTime = v.findViewById(ID_TIME);
                TextViewMessage = v.findViewById(ID_MESSAGE);
                ImageViewLike = v.findViewById(ID_LIKE);
                ImageViewShort = v.findViewById(ID_SHORTCUT);
                ImageViewDelete = v.findViewById(ID_DELETE);
                TextViewLikeCount = v.findViewById(ID_LIKE_COUNT);
                ViewLine = v.findViewById(ID_LINE);
            }
        }

        @Override
        public void onBindViewHolder(ViewHolderMain Holder, int p)
        {
            if (Holder.getItemViewType() == 0)
                return;

            final int Position = Holder.getAdapterPosition();

            GlideApp.with(GetActivity()).load(CommentList.get(Position).Profile).placeholder(R.drawable._general_avatar).into(Holder.CircleImageViewProfile);
            Holder.CircleImageViewProfile.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    // TODO Open Profile
                }
            });

            Holder.TextViewUsername.setText(("@" + CommentList.get(Position).Username));
            Holder.TextViewUsername.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    // TODO Open Profile
                }
            });

            if (Position == CommentList.size() - 1)
                Holder.ViewLine.setVisibility(View.GONE);
            else
                Holder.ViewLine.setVisibility(View.VISIBLE);
        }

        @Override
        public ViewHolderMain onCreateViewHolder(ViewGroup p, int ViewType)
        {
            if (ViewType == 0)
            {
                RelativeLayout RelativeLayoutMain = new RelativeLayout(GetActivity());
                RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

                RelativeLayout.LayoutParams LinearLayoutMainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                LinearLayoutMainParam.addRule(RelativeLayout.CENTER_IN_PARENT);

                LinearLayout LinearLayoutMain = new LinearLayout(GetActivity());
                LinearLayoutMain.setLayoutParams(LinearLayoutMainParam);
                LinearLayoutMain.setOrientation(LinearLayout.VERTICAL);
                LinearLayoutMain.setGravity(Gravity.CENTER);

                RelativeLayoutMain.addView(LinearLayoutMain);

                RelativeLayout.LayoutParams ImageViewContentParam = new RelativeLayout.LayoutParams(Misc.ToDP(56), Misc.ToDP(56));
                ImageViewContentParam.addRule(RelativeLayout.CENTER_IN_PARENT);

                ImageView ImageViewContent = new CircleImageView(GetActivity());
                ImageViewContent.setLayoutParams(ImageViewContentParam);
                ImageViewContent.setImageResource(R.drawable._general_comment_gray);
                ImageViewContent.setId(Misc.ViewID());

                LinearLayoutMain.addView(ImageViewContent);

                RelativeLayout.LayoutParams TextViewContentParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                TextViewContentParam.addRule(RelativeLayout.BELOW, ImageViewContent.getId());
                TextViewContentParam.addRule(RelativeLayout.CENTER_HORIZONTAL);

                TextView TextViewContent = new TextView(GetActivity(), 16, true);
                TextViewContent.setLayoutParams(TextViewContentParam);
                TextViewContent.SetColor(R.color.Gray);
                TextViewContent.setText(GetActivity().getString(R.string.CommentUINo));

                LinearLayoutMain.addView(TextViewContent);

                return new ViewHolderMain(RelativeLayoutMain, true);
            }

            StateListDrawable StatePress = new StateListDrawable();
            StatePress.addState(new int[] { android.R.attr.state_pressed }, new ColorDrawable(Color.parseColor("#b0eeeeee")));

            RelativeLayout RelativeLayoutMain = new RelativeLayout(GetActivity());
            RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            RelativeLayoutMain.setBackground(StatePress);
            RelativeLayoutMain.setOnClickListener(null);

            RelativeLayout.LayoutParams CircleImageViewProfileParam = new RelativeLayout.LayoutParams(Misc.ToDP(48), Misc.ToDP(48));
            CircleImageViewProfileParam.setMargins(Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10));
            CircleImageViewProfileParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

            CircleImageView CircleImageViewProfile = new CircleImageView(GetActivity());
            CircleImageViewProfile.setLayoutParams(CircleImageViewProfileParam);
            CircleImageViewProfile.SetBorderColor(R.color.LineWhite);
            CircleImageViewProfile.SetBorderWidth(1);
            CircleImageViewProfile.setId(ID_PROFILE);

            RelativeLayoutMain.addView(CircleImageViewProfile);

            RelativeLayout.LayoutParams TextViewUsernameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewUsernameParam.addRule(RelativeLayout.RIGHT_OF, ID_PROFILE);
            TextViewUsernameParam.setMargins(0, Misc.ToDP(13), 0, 0);

            TextView TextViewUsername = new TextView(GetActivity(), 14, false);
            TextViewUsername.setLayoutParams(TextViewUsernameParam);
            TextViewUsername.SetColor(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite);
            TextViewUsername.setId(ID_USERNAME);

            RelativeLayoutMain.addView(TextViewUsername);

            RelativeLayout.LayoutParams TextViewTimeParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewTimeParam.setMargins(Misc.ToDP(10), Misc.ToDP(13), Misc.ToDP(10), 0);
            TextViewTimeParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

            TextView TextViewTime = new TextView(GetActivity(), 12, false);
            TextViewTime.setLayoutParams(TextViewTimeParam);
            TextViewTime.SetColor(R.color.Gray);
            TextViewTime.setId(ID_TIME);

            RelativeLayoutMain.addView(TextViewTime);

            RelativeLayout.LayoutParams TextViewMessageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewMessageParam.addRule(RelativeLayout.RIGHT_OF, ID_PROFILE);
            TextViewMessageParam.addRule(RelativeLayout.BELOW, ID_USERNAME);

            TextView TextViewMessage = new TextView(GetActivity(), 14, false);
            TextViewMessage.setLayoutParams(TextViewMessageParam);
            TextViewMessage.SetColor(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite);
            TextViewMessage.setId(ID_MESSAGE);

            RelativeLayoutMain.addView(TextViewMessage);

            RelativeLayout.LayoutParams LinearLayoutToolParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            LinearLayoutToolParam.addRule(RelativeLayout.RIGHT_OF, ID_PROFILE);
            LinearLayoutToolParam.addRule(RelativeLayout.BELOW, ID_MESSAGE);

            LinearLayout LinearLayoutTool = new LinearLayout(GetActivity());
            LinearLayoutTool.setLayoutParams(LinearLayoutToolParam);
            LinearLayoutTool.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayoutTool.setId(Misc.ViewID());

            RelativeLayoutMain.addView(LinearLayoutTool);

            ImageView ImageViewLike = new ImageView(GetActivity());
            ImageViewLike.setLayoutParams(new RelativeLayout.LayoutParams(Misc.ToDP(32), Misc.ToDP(32)));
            ImageViewLike.setId(ID_LIKE);

            RelativeLayoutMain.addView(ImageViewLike);

            ImageView ImageViewShort = new ImageView(GetActivity());
            ImageViewShort.setLayoutParams(new RelativeLayout.LayoutParams(Misc.ToDP(32), Misc.ToDP(32)));
            ImageViewShort.setImageResource(R.drawable.);
            ImageViewShort.setId(ID_SHORTCUT);

            RelativeLayoutMain.addView(ImageViewShort);

            ImageView ImageViewDelete = new ImageView(GetActivity());
            ImageViewDelete.setLayoutParams(new RelativeLayout.LayoutParams(Misc.ToDP(32), Misc.ToDP(32)));
            ImageViewDelete.setId(ID_DELETE);

            RelativeLayoutMain.addView(ImageViewDelete);

            RelativeLayout.LayoutParams TextViewLikeParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewLikeParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            TextViewLikeParam.addRule(RelativeLayout.BELOW, ID_MESSAGE);

            TextView TextViewLike = new TextView(GetActivity(), 12, false);
            TextViewLike.setLayoutParams(TextViewLikeParam);
            TextViewLike.SetColor(R.color.Gray);
            TextViewLike.setId(ID_LIKE_COUNT);

            RelativeLayoutMain.addView(TextViewLike);

            RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1));
            ViewLineParam.addRule(RelativeLayout.BELOW, LinearLayoutTool.getId());

            View ViewLine = new View(GetActivity());
            ViewLine.setLayoutParams(ViewLineParam);
            ViewLine.setBackgroundResource(Misc.IsDark() ? R.color.LineDark : R.color.LineWhite);
            ViewLine.setId(ID_LINE);

            RelativeLayoutMain.addView(ViewLine);

            return new ViewHolderMain(RelativeLayoutMain, false);
        }

        @Override
        public int getItemViewType(int Position)
        {
            return CommentList.size() == 0 ? 0 : 1;
        }

        @Override
        public int getItemCount()
        {
            return CommentList.size() == 0 ? 1 : CommentList.size();
        }
    }

    private class Struct
    {
        String ID;
        String Username;
        String Profile;
        String Message;
        int LikeCount;
        int Time;
        boolean Like;

        Struct(String I, String U,String P, String M, int LC, int T, boolean L)
        {
            ID = I;
            Username = U;
            Profile = P;
            Message = M;
            LikeCount = LC;
            Time = T;
            Like = L;
        }
    }
}
