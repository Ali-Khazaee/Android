package co.biogram.main.ui.social;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
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
import co.biogram.main.handler.FontHandler;
import co.biogram.main.handler.GlideApp;
import co.biogram.main.handler.Misc;
import co.biogram.main.handler.OnScrollRecyclerView;
import co.biogram.main.handler.SharedHandler;
import co.biogram.main.handler.TagHandler;
import co.biogram.main.ui.view.CircleImageView;
import co.biogram.main.ui.view.LoadingView;
import co.biogram.main.ui.view.TextView;

public class CommentUI extends FragmentView
{
    private List<Struct> CommentList = new ArrayList<>();
    private EditText EditTextMessage;
    private AdapterComment Adapter;
    private boolean IsOwner;
    private String PostID;

    public CommentUI(String id, String owner)
    {
        PostID = id;
        IsOwner = SharedHandler.GetString("ID").equals(owner);
    }

    @Override
    public void OnCreate()
    {
        final LinearLayoutManager LinearLayoutManagerMain = new LinearLayoutManager(GetActivity());

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

        RelativeLayout.LayoutParams CircleImageViewProfileParam = new RelativeLayout.LayoutParams(Misc.ToDP(40), Misc.ToDP(40));
        CircleImageViewProfileParam.setMargins(Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10));
        CircleImageViewProfileParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

        CircleImageView CircleImageViewProfile = new CircleImageView(GetActivity());
        CircleImageViewProfile.setLayoutParams(CircleImageViewProfileParam);
        CircleImageViewProfile.SetBorderColor(R.color.LineWhite);
        CircleImageViewProfile.SetBorderWidth(1);
        CircleImageViewProfile.setId(Misc.ViewID());

        if (SharedHandler.GetString("Avatar").isEmpty())
            CircleImageViewProfile.setVisibility(View.GONE);
        else
            GlideApp.with(GetActivity()).load(SharedHandler.GetString("Avatar")).placeholder(R.drawable._general_avatar).into(CircleImageViewProfile);

        RelativeLayoutBottom.addView(CircleImageViewProfile);

        RelativeLayout.LayoutParams RelativeLayoutSendParam = new RelativeLayout.LayoutParams(Misc.ToDP(56), Misc.ToDP(56));
        RelativeLayoutSendParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        RelativeLayout RelativeLayoutSend = new RelativeLayout(GetActivity());
        RelativeLayoutSend.setLayoutParams(RelativeLayoutSendParam);
        RelativeLayoutSend.setId(Misc.ViewID());

        RelativeLayoutBottom.addView(RelativeLayoutSend);

        final ImageView ImageViewSend = new ImageView(GetActivity());
        ImageViewSend.setLayoutParams(new RelativeLayout.LayoutParams(Misc.ToDP(56), Misc.ToDP(56)));
        ImageViewSend.setImageResource(R.drawable._comment_send_gray);
        ImageViewSend.setPadding(Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10));

        RelativeLayoutSend.addView(ImageViewSend);

        final LoadingView LoadingViewSend = new LoadingView(GetActivity());
        LoadingViewSend.setLayoutParams(new RelativeLayout.LayoutParams(Misc.ToDP(56), Misc.ToDP(56)));
        LoadingViewSend.SetColor(R.color.Primary);

        RelativeLayoutSend.addView(LoadingViewSend);

        ImageViewSend.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (EditTextMessage.getText().length() == 0)
                    return;

                ImageViewSend.setVisibility(View.GONE);
                LoadingViewSend.Start();

                AndroidNetworking.post(Misc.GetRandomServer("PostComment"))
                .addBodyParameter("Message", EditTextMessage.getText().toString())
                .addBodyParameter("PostID", PostID)
                .addHeaders("Token", SharedHandler.GetString("Token"))
                .setTag("CommentUI")
                .build()
                .getAsString(new StringRequestListener()
                {
                    @Override
                    public void onResponse(String Response)
                    {
                        LoadingViewSend.Stop();
                        ImageViewSend.setVisibility(View.VISIBLE);

                        try
                        {
                            JSONObject Result = new JSONObject(Response);

                            if (Result.getInt("Message") == 0)
                            {
                                CommentList.add(0, new Struct(Result.getString("ID"), SharedHandler.GetString("Username"), SharedHandler.GetString("Avatar"), EditTextMessage.getText().toString(), SharedHandler.GetString("ID"), 0, (int) (System.currentTimeMillis() / 1000), false));
                                Adapter.notifyDataSetChanged();
                                EditTextMessage.setText("");

                                LinearLayoutManagerMain.scrollToPositionWithOffset(0, 0);
                            }
                        }
                        catch (Exception e)
                        {
                            Misc.Debug("CommentUI-Send: " + e.toString());
                        }
                    }

                    @Override
                    public void onError(ANError e)
                    {
                        LoadingViewSend.Stop();
                        ImageViewSend.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

        RelativeLayout.LayoutParams EditTextMessageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        EditTextMessageParam.addRule(RelativeLayout.RIGHT_OF, CircleImageViewProfile.getId());
        EditTextMessageParam.addRule(RelativeLayout.LEFT_OF, RelativeLayoutSend.getId());

        EditTextMessage = new EditText(GetActivity());
        EditTextMessage.setLayoutParams(EditTextMessageParam);
        EditTextMessage.setPadding(Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10));
        EditTextMessage.setMaxLines(2);
        EditTextMessage.setHint(R.string.CommentUIComment);
        EditTextMessage.setBackground(null);
        EditTextMessage.setTypeface(FontHandler.GetTypeface(GetActivity()));
        EditTextMessage.setTextColor(Misc.Color(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite));
        EditTextMessage.setHintTextColor(Misc.Color(R.color.Gray));
        EditTextMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        EditTextMessage.setFilters(new InputFilter[] { new InputFilter.LengthFilter(300) });
        EditTextMessage.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        EditTextMessage.addTextChangedListener(new TextWatcher()
        {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override public void afterTextChanged(Editable editable) { }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2)
            {
                ImageViewSend.setImageResource(s.length() == 0 ? R.drawable._comment_send_gray : R.drawable._comment_send_blue);
            }
        });

        RelativeLayoutBottom.addView(EditTextMessage);

        RelativeLayout.LayoutParams ViewLine2Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1));
        ViewLine2Param.addRule(RelativeLayout.ABOVE, RelativeLayoutBottom.getId());

        View ViewLine2 = new View(GetActivity());
        ViewLine2.setLayoutParams(ViewLine2Param);
        ViewLine2.setBackgroundResource(Misc.IsDark() ? R.color.LineDark : R.color.LineWhite);
        ViewLine2.setId(Misc.ViewID());

        RelativeLayoutMain.addView(ViewLine2);

        RelativeLayout.LayoutParams RecyclerViewMainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        RecyclerViewMainParam.addRule(RelativeLayout.ABOVE, ViewLine2.getId());
        RecyclerViewMainParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

        RecyclerView RecyclerViewMain = new RecyclerView(GetActivity());
        RecyclerViewMain.setLayoutParams(RecyclerViewMainParam);
        RecyclerViewMain.setAdapter(Adapter = new AdapterComment());
        RecyclerViewMain.setLayoutManager(LinearLayoutManagerMain);
        RecyclerViewMain.addOnScrollListener(new OnScrollRecyclerView(LinearLayoutManagerMain) { @Override public void OnLoadMore() { Update(); } });

        RelativeLayoutMain.addView(RecyclerViewMain);

        Update();

        ViewMain = RelativeLayoutMain;
    }

    @Override
    public void OnPause()
    {
        AndroidNetworking.forceCancel("CommentUI");
        Misc.HideSoftKey(GetActivity());
    }

    private void Update()
    {
        AndroidNetworking.post(Misc.GetRandomServer("PostCommentList"))
        .addBodyParameter("Skip", String.valueOf(CommentList.size()))
        .addBodyParameter("PostID", PostID)
        .addHeaders("Token", SharedHandler.GetString("Token"))
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

                            CommentList.add(new Struct(D.getString("ID"), D.getString("Username"), D.getString("Avatar"), D.getString("Message"), D.getString("Owner"), D.getInt("Count"), D.getInt("Time"), D.getBoolean("Like")));
                        }

                        Adapter.notifyDataSetChanged();
                    }
                }
                catch (Exception e)
                {
                    Misc.Debug("CommentUI-Update: " + e.toString());
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
        public void onBindViewHolder(final ViewHolderMain Holder, int p)
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

            Holder.TextViewUsername.setText(CommentList.get(Position).Username);
            Holder.TextViewUsername.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    // TODO Open Profile
                }
            });

            Holder.TextViewTime.setText(Misc.TimeAgo(CommentList.get(Position).Time));
            Holder.TextViewMessage.setText(CommentList.get(Position).Message);
            TagHandler.Show(Holder.TextViewMessage);

            if (CommentList.get(Position).LikeCount <= 0)
                Holder.TextViewLikeCount.setVisibility(View.GONE);
            else
                Holder.TextViewLikeCount.setVisibility(View.VISIBLE);
            
            Holder.TextViewLikeCount.setText((CommentList.get(Position).LikeCount + " " + GetActivity().getString(R.string.CommentUILike)));
            Holder.TextViewLikeCount.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { GetActivity().GetManager().OpenView(new LikeUI(CommentList.get(Position).ID, true), R.id.ContainerFull, "LikeUI"); } });

            if (CommentList.get(Position).Like)
                GlideApp.with(GetActivity()).load(R.drawable._general_like_red).into(Holder.ImageViewLike);
            else
                GlideApp.with(GetActivity()).load(R.drawable._general_like).into(Holder.ImageViewLike);

            Holder.ImageViewLike.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (CommentList.get(Position).Like)
                    {
                        Holder.ImageViewLike.setImageResource(R.drawable._general_like);

                        ObjectAnimator Fade = ObjectAnimator.ofFloat(Holder.ImageViewLike, "alpha",  0.1f, 1f);
                        Fade.setDuration(200);

                        AnimatorSet AnimationSet = new AnimatorSet();
                        AnimationSet.play(Fade);
                        AnimationSet.start();

                        CommentList.get(Position).DesLike();
                        CommentList.get(Position).RevLike();

                        Holder.TextViewLikeCount.setText((String.valueOf(CommentList.get(Position).LikeCount) + " " + GetActivity().getString(R.string.CommentUILike)));
                    }
                    else
                    {
                        Holder.ImageViewLike.setImageResource(R.drawable._general_like_red);

                        ObjectAnimator SizeX = ObjectAnimator.ofFloat(Holder.ImageViewLike, "scaleX", 1.5f);
                        SizeX.setDuration(200);

                        ObjectAnimator SizeY = ObjectAnimator.ofFloat(Holder.ImageViewLike, "scaleY", 1.5f);
                        SizeY.setDuration(200);

                        ObjectAnimator Fade = ObjectAnimator.ofFloat(Holder.ImageViewLike, "alpha",  0.1f, 1f);
                        Fade.setDuration(400);

                        ObjectAnimator SizeX2 = ObjectAnimator.ofFloat(Holder.ImageViewLike, "scaleX", 1f);
                        SizeX2.setDuration(200);
                        SizeX2.setStartDelay(200);

                        ObjectAnimator SizeY2 = ObjectAnimator.ofFloat(Holder.ImageViewLike, "scaleY", 1f);
                        SizeY2.setDuration(200);
                        SizeY2.setStartDelay(200);

                        AnimatorSet AnimationSet = new AnimatorSet();
                        AnimationSet.playTogether(SizeX, SizeY, Fade, SizeX2, SizeY2);
                        AnimationSet.start();

                        CommentList.get(Position).InsLike();
                        CommentList.get(Position).RevLike();

                        Holder.TextViewLikeCount.setText((String.valueOf(CommentList.get(Position).LikeCount) + " " + GetActivity().getString(R.string.CommentUILike)));
                    }

                    if (CommentList.get(Position).LikeCount <= 0)
                        Holder.TextViewLikeCount.setVisibility(View.GONE);
                    else
                        Holder.TextViewLikeCount.setVisibility(View.VISIBLE);

                    AndroidNetworking.post(Misc.GetRandomServer("PostCommentLike"))
                    .addBodyParameter("CommentID", CommentList.get(Position).ID)
                    .addHeaders("Token", SharedHandler.GetString( "Token"))
                    .setTag("CommentUI")
                    .build()
                    .getAsString(null);
                }
            });

            Holder.ImageViewShort.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (EditTextMessage.getText().toString().length() >= 300)
                        return;

                    EditTextMessage.requestFocus();
                    EditTextMessage.setText((EditTextMessage.getText().toString() + "@" + CommentList.get(Position).Username + " "));
                    EditTextMessage.setSelection(EditTextMessage.getText().toString().length());

                    Misc.ShowSoftKey(EditTextMessage);
                }
            });

            if (IsOwner || SharedHandler.GetString("ID").equals(CommentList.get(Position).Owner))
                Holder.ImageViewDelete.setVisibility(View.VISIBLE);
            else
                Holder.ImageViewDelete.setVisibility(View.GONE);

            Holder.ImageViewDelete.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    final Dialog DialogDelete = new Dialog(GetActivity());
                    DialogDelete.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    DialogDelete.setCancelable(true);

                    LinearLayout LinearLayoutMain = new LinearLayout(GetActivity());
                    LinearLayoutMain.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    LinearLayoutMain.setBackgroundResource(Misc.IsDark() ? R.color.GroundDark : R.color.GroundWhite);
                    LinearLayoutMain.setOrientation(LinearLayout.VERTICAL);

                    TextView TextViewTitle = new TextView(GetActivity(), 16, true);
                    TextViewTitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56)));
                    TextViewTitle.SetColor(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite);
                    TextViewTitle.setPadding(Misc.ToDP(15), 0, Misc.ToDP(15), 0);
                    TextViewTitle.setText(Misc.String(R.string.CommentUIMessage));
                    TextViewTitle.setGravity(Gravity.CENTER_VERTICAL);

                    LinearLayoutMain.addView(TextViewTitle);

                    View ViewLine = new View(GetActivity());
                    ViewLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1)));
                    ViewLine.setBackgroundResource(R.color.LineWhite);

                    LinearLayoutMain.addView(ViewLine);

                    LinearLayout LinearLayoutButton = new LinearLayout(GetActivity());
                    LinearLayoutButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56)));
                    LinearLayoutButton.setOrientation(LinearLayout.HORIZONTAL);

                    LinearLayoutMain.addView(LinearLayoutButton);

                    TextView TextViewDelete = new TextView(GetActivity(), 14, false);
                    TextViewDelete.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
                    TextViewDelete.SetColor(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite);
                    TextViewDelete.setText(Misc.String(R.string.CommentUIDelete));
                    TextViewDelete.setGravity(Gravity.CENTER);
                    TextViewDelete.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            AndroidNetworking.post(Misc.GetRandomServer("PostCommentDelete"))
                            .addBodyParameter("CommentID", CommentList.get(Position).ID)
                            .addHeaders("Token", SharedHandler.GetString( "Token"))
                            .setTag("CommentUI")
                            .build()
                            .getAsString(null);

                            CommentList.remove(Position);
                            notifyDataSetChanged();
                            DialogDelete.dismiss();
                        }
                    });

                    TextView TextViewCancel = new TextView(GetActivity(), 14, false);
                    TextViewCancel.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
                    TextViewCancel.SetColor(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite);
                    TextViewCancel.setText(Misc.String(R.string.CommentUICancel));
                    TextViewCancel.setGravity(Gravity.CENTER);
                    TextViewCancel.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { DialogDelete.dismiss(); } });

                    if (Misc.IsRTL())
                    {
                        LinearLayoutButton.addView(TextViewCancel);
                        LinearLayoutButton.addView(TextViewDelete);
                    }
                    else
                    {
                        LinearLayoutButton.addView(TextViewDelete);
                        LinearLayoutButton.addView(TextViewCancel);
                    }

                    DialogDelete.setContentView(LinearLayoutMain);
                    DialogDelete.show();
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

                TextView TextViewContent = new TextView(GetActivity(), 16, false);
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

            TextView TextViewUsername = new TextView(GetActivity(), 14, true);
            TextViewUsername.setLayoutParams(TextViewUsernameParam);
            TextViewUsername.SetColor(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite);
            TextViewUsername.setId(ID_USERNAME);

            RelativeLayoutMain.addView(TextViewUsername);

            RelativeLayout.LayoutParams TextViewTimeParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewTimeParam.setMargins(Misc.ToDP(10), Misc.ToDP(13), Misc.ToDP(8), 0);
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
            TextViewMessage.setPadding(0, 0, Misc.ToDP(8), 0);
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
            ImageViewLike.setPadding(Misc.ToDP(5), Misc.ToDP(5), Misc.ToDP(5), Misc.ToDP(5));
            ImageViewLike.setImageResource(R.drawable._general_like);
            ImageViewLike.setId(ID_LIKE);

            LinearLayoutTool.addView(ImageViewLike);

            ImageView ImageViewShort = new ImageView(GetActivity());
            ImageViewShort.setLayoutParams(new RelativeLayout.LayoutParams(Misc.ToDP(32), Misc.ToDP(32)));
            ImageViewShort.setPadding(Misc.ToDP(5), Misc.ToDP(5), Misc.ToDP(5), Misc.ToDP(5));
            ImageViewShort.setImageResource(R.drawable._comment_short);
            ImageViewShort.setId(ID_SHORTCUT);

            LinearLayoutTool.addView(ImageViewShort);

            ImageView ImageViewDelete = new ImageView(GetActivity());
            ImageViewDelete.setLayoutParams(new RelativeLayout.LayoutParams(Misc.ToDP(32), Misc.ToDP(32)));
            ImageViewDelete.setPadding(Misc.ToDP(1), Misc.ToDP(1), Misc.ToDP(1), Misc.ToDP(1));
            ImageViewDelete.setImageResource(R.drawable._comment_delete);
            ImageViewDelete.setId(ID_DELETE);

            LinearLayoutTool.addView(ImageViewDelete);

            RelativeLayout.LayoutParams TextViewLikeParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewLikeParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            TextViewLikeParam.addRule(RelativeLayout.BELOW, ID_MESSAGE);

            TextView TextViewLike = new TextView(GetActivity(), 12, false);
            TextViewLike.setLayoutParams(TextViewLikeParam);
            TextViewLike.setPadding(0, Misc.ToDP(5), Misc.ToDP(8), 0);
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
        String Owner;
        int LikeCount;
        int Time;
        boolean Like;

        Struct(String I, String U,String P, String M, String O, int LC, int T, boolean L)
        {
            ID = I;
            Username = U;
            Profile = P;
            Message = M;
            Owner = O;
            LikeCount = LC;
            Time = T;
            Like = L;
        }

        void RevLike() { Like = !Like; }
        void InsLike() { LikeCount++; }
        void DesLike() { LikeCount--; }
    }
}
