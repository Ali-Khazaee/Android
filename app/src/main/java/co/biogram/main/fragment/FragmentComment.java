package co.biogram.main.fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
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
import co.biogram.main.handler.TagHandler;
import co.biogram.main.handler.URLHandler;
import co.biogram.main.misc.LoadingView;
import co.biogram.main.misc.ImageViewCircle;

public class FragmentComment extends Fragment
{
    private RelativeLayout RelativeLayoutLoading;
    private LoadingView LoadingViewData;
    private TextView TextViewTry;

    private String PostID = "";
    private boolean IsOwner = false;
    private boolean IsSending = false;
    private boolean LoadingTop = false;
    private boolean LoadingBottom = false;

    private EditText EditTextComment;
    private AdapterComment RecyclerViewCommentAdapter;
    private final List<Struct> CommentList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final Context context = getActivity();

        if (getArguments() != null)
        {
            PostID = getArguments().getString("PostID", "");

            if (getArguments().getString("OwnerID", "").equals(SharedHandler.GetString(context, "ID")))
                IsOwner = true;
        }

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
        ImageViewBack.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), RelativeLayout.LayoutParams.MATCH_PARENT));
        ImageViewBack.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewBack.setPadding(MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12));
        ImageViewBack.setImageResource(R.drawable.ic_back_blue);
        ImageViewBack.setId(MiscHandler.GenerateViewID());
        ImageViewBack.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { MiscHandler.HideSoftKey(getActivity()); getActivity().onBackPressed(); } });

        RelativeLayoutHeader.addView(ImageViewBack);

        RelativeLayout.LayoutParams TextViewTitleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTitleParam.addRule(RelativeLayout.CENTER_VERTICAL);
        TextViewTitleParam.addRule(RelativeLayout.RIGHT_OF, ImageViewBack.getId());

        TextView TextViewTitle = new TextView(context);
        TextViewTitle.setLayoutParams(TextViewTitleParam);
        TextViewTitle.setText(getString(R.string.FragmentComment));
        TextViewTitle.setTextColor(ContextCompat.getColor(context, R.color.Black));
        TextViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        TextViewTitle.setTypeface(null, Typeface.BOLD);

        RelativeLayoutHeader.addView(TextViewTitle);

        RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1));
        ViewLineParam.addRule(RelativeLayout.BELOW, RelativeLayoutHeader.getId());

        View ViewLine = new View(context);
        ViewLine.setLayoutParams(ViewLineParam);
        ViewLine.setBackgroundResource(R.color.Gray2);
        ViewLine.setId(MiscHandler.GenerateViewID());

        Root.addView(ViewLine);

        RelativeLayout.LayoutParams RelativeLayoutBottomParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56));
        RelativeLayoutBottomParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        RelativeLayout RelativeLayoutBottom = new RelativeLayout(context);
        RelativeLayoutBottom.setLayoutParams(RelativeLayoutBottomParam);
        RelativeLayoutBottom.setBackgroundResource(R.color.White5);
        RelativeLayoutBottom.setId(MiscHandler.GenerateViewID());

        Root.addView(RelativeLayoutBottom);

        RelativeLayout.LayoutParams RelativeLayoutSendParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), MiscHandler.ToDimension(context, 56));
        RelativeLayoutSendParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        RelativeLayoutSendParam.addRule(RelativeLayout.CENTER_VERTICAL);

        RelativeLayout RelativeLayoutSend = new RelativeLayout(context);
        RelativeLayoutSend.setLayoutParams(RelativeLayoutSendParam);
        RelativeLayoutSend.setId(MiscHandler.GenerateViewID());

        RelativeLayoutBottom.addView(RelativeLayoutSend);

        final LoadingView LoadingViewSend = new LoadingView(context);
        final RecyclerView RecyclerViewComment = new RecyclerView(context);

        final ImageView ImageViewSend = new ImageView(context);
        ImageViewSend.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), MiscHandler.ToDimension(context, 56)));
        ImageViewSend.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewSend.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
        ImageViewSend.setImageResource(R.drawable.ic_send_gray);
        ImageViewSend.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (IsSending || EditTextComment.getText().toString().equals(""))
                    return;

                IsSending = true;
                LoadingViewSend.Start();
                ImageViewSend.setVisibility(View.GONE);

                AndroidNetworking.post(URLHandler.GetURL(URLHandler.URL.POST_COMMENT))
                .addBodyParameter("Message", EditTextComment.getText().toString())
                .addBodyParameter("PostID", PostID)
                .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
                .setTag("FragmentComment")
                .build().getAsString(new StringRequestListener()
                {
                    @Override
                    public void onResponse(String Response)
                    {
                        IsSending = false;
                        LoadingViewSend.Stop();

                        ImageViewSend.setVisibility(View.VISIBLE);
                        ImageViewSend.setImageResource(R.drawable.ic_send_blue);

                        try
                        {
                            JSONObject Result = new JSONObject(Response);

                            if (Result.getInt("Message") == 1000)
                            {
                                RecyclerViewComment.scrollToPosition(0);
                                CommentList.add(0, new Struct(Result.getString("CommentID"), SharedHandler.GetString(context, "ID"), SharedHandler.GetString(context, "Username"), (System.currentTimeMillis() + 50000) , EditTextComment.getText().toString(), 0, false, SharedHandler.GetString(context, "Avatar")));
                                RecyclerViewCommentAdapter.notifyItemInserted(0);
                                ImageViewSend.setImageResource(R.drawable.ic_send_gray);
                                EditTextComment.setText("");
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
                        IsSending = false;
                        LoadingViewSend.Stop();

                        ImageViewSend.setVisibility(View.VISIBLE);
                        ImageViewSend.setImageResource(R.drawable.ic_send_blue);

                        MiscHandler.Toast(context, getString(R.string.NoInternet));
                    }
                });
            }
        });

        RelativeLayoutSend.addView(ImageViewSend);

        RelativeLayout.LayoutParams LoadingViewSendParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        LoadingViewSendParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        LoadingViewSend.setLayoutParams(LoadingViewSendParam);
        LoadingViewSend.SetColor(R.color.BlueLight);

        RelativeLayoutSend.addView(LoadingViewSend);

        RelativeLayout.LayoutParams EditTextCommentParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        EditTextCommentParam.addRule(RelativeLayout.LEFT_OF, RelativeLayoutSend.getId());

        EditTextComment = new EditText(context);
        EditTextComment.setLayoutParams(EditTextCommentParam);
        EditTextComment.setHint(R.string.FragmentCommentHint);
        EditTextComment.setPadding(MiscHandler.ToDimension(context, 15), 0, MiscHandler.ToDimension(context, 15), 0);
        EditTextComment.setId(MiscHandler.GenerateViewID());
        EditTextComment.setBackground(null);
        EditTextComment.setTextColor(ContextCompat.getColor(context, R.color.Black));
        EditTextComment.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        EditTextComment.setFilters(new InputFilter[] { new InputFilter.LengthFilter(150) });
        EditTextComment.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        EditTextComment.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2)
            {
                if (s.length() == 0)
                    ImageViewSend.setImageResource(R.drawable.ic_send_gray);
                else if (!IsSending)
                    ImageViewSend.setImageResource(R.drawable.ic_send_blue);
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        RelativeLayoutBottom.addView(EditTextComment);

        RelativeLayout.LayoutParams ViewLine2Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1));
        ViewLine2Param.addRule(RelativeLayout.ABOVE, RelativeLayoutBottom.getId());

        View ViewLine2 = new View(context);
        ViewLine2.setLayoutParams(ViewLine2Param);
        ViewLine2.setBackgroundResource(R.color.Gray2);
        ViewLine2.setId(MiscHandler.GenerateViewID());

        Root.addView(ViewLine2);

        RelativeLayout.LayoutParams SwipeRefreshLayoutCommentParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        SwipeRefreshLayoutCommentParam.addRule(RelativeLayout.BELOW, ViewLine.getId());
        SwipeRefreshLayoutCommentParam.addRule(RelativeLayout.ABOVE, ViewLine2.getId());

        final SwipeRefreshLayout SwipeRefreshLayoutComment = new SwipeRefreshLayout(context);
        SwipeRefreshLayoutComment.setLayoutParams(SwipeRefreshLayoutCommentParam);
        SwipeRefreshLayoutComment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                if (LoadingTop || CommentList.size() <= 0)
                    return;

                LoadingTop = true;
                SwipeRefreshLayoutComment.setEnabled(false);
                SwipeRefreshLayoutComment.setRefreshing(false);

                AndroidNetworking.post(URLHandler.GetURL(URLHandler.URL.POST_COMMENT_LIST))
                .addBodyParameter("CommentTime", String.valueOf(CommentList.get(0).Time))
                .addBodyParameter("PostID", PostID)
                .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
                .setTag("FragmentComment")
                .build().getAsString(new StringRequestListener()
                {
                    @Override
                    public void onResponse(String Response)
                    {
                        LoadingTop = false;
                        SwipeRefreshLayoutComment.setEnabled(true);

                        try
                        {
                            JSONObject Result = new JSONObject(Response);

                            if (Result.getInt("Message") == 1000 && !Result.getString("Result").equals(""))
                            {
                                JSONArray commentList = new JSONArray(Result.getString("Result"));

                                for (int K = 0; K < commentList.length(); K++)
                                {
                                    JSONObject Comment = commentList.getJSONObject(K);
                                    CommentList.add(0, new Struct(Comment.getString("CommentID"), Comment.getString("OwnerID"), Comment.getString("Username"), Comment.getInt("Time"), Comment.getString("Message"), Comment.getInt("LikeCount"), Comment.getBoolean("Like"), Comment.getString("Avatar")));
                                }

                                RecyclerViewCommentAdapter.notifyDataSetChanged();
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
                        LoadingTop = false;
                        SwipeRefreshLayoutComment.setEnabled(true);

                        MiscHandler.Toast(context, getString(R.string.NoInternet));
                    }
                });
            }
        });

        Root.addView(SwipeRefreshLayoutComment);

        RecyclerViewComment.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RecyclerViewComment.setLayoutManager(new LinearLayoutManager(context));
        RecyclerViewComment.setAdapter(RecyclerViewCommentAdapter = new AdapterComment(context));
        RecyclerViewComment.setOverScrollMode(View.OVER_SCROLL_NEVER);
        RecyclerViewComment.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView View, int DX, int DY)
            {
                if (DY <= 0)
                    return;

                if (((LinearLayoutManager) View.getLayoutManager()).findLastVisibleItemPosition() + 2 > View.getAdapter().getItemCount() && !LoadingBottom)
                {
                    LoadingBottom = true;
                    CommentList.add(null);
                    RecyclerViewCommentAdapter.notifyItemInserted(CommentList.size());

                    AndroidNetworking.post(URLHandler.GetURL(URLHandler.URL.POST_COMMENT_LIST))
                    .addBodyParameter("Skip", String.valueOf(CommentList.size()))
                    .addBodyParameter("PostID", PostID)
                    .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
                    .setTag("FragmentComment")
                    .build().getAsString(new StringRequestListener()
                    {
                        @Override
                        public void onResponse(String Response)
                        {
                            LoadingBottom = false;
                            CommentList.remove(CommentList.size() - 1);
                            RecyclerViewCommentAdapter.notifyItemRemoved(CommentList.size());

                            try
                            {
                                JSONObject Result = new JSONObject(Response);

                                if (Result.getInt("Message") == 1000 && !Result.getString("Result").equals(""))
                                {
                                    JSONArray Comments = new JSONArray(Result.getString("Result"));

                                    for (int K = 0; K < Comments.length(); K++)
                                    {
                                        JSONObject Comment = Comments.getJSONObject(K);
                                        CommentList.add(new Struct(Comment.getString("CommentID"), Comment.getString("OwnerID"), Comment.getString("Username"), Comment.getInt("Time"), Comment.getString("Message"), Comment.getInt("LikeCount"), Comment.getBoolean("Like"), Comment.getString("Avatar")));
                                    }

                                    RecyclerViewCommentAdapter.notifyDataSetChanged();
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
                            LoadingBottom = false;
                            CommentList.remove(CommentList.size() - 1);
                            RecyclerViewCommentAdapter.notifyItemRemoved(CommentList.size());

                            MiscHandler.Toast(context, getString(R.string.NoInternet));
                        }
                    });
                }
            }
        });

        SwipeRefreshLayoutComment.addView(RecyclerViewComment);

        RelativeLayout.LayoutParams RelativeLayoutLoadingParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        RelativeLayoutLoadingParam.addRule(RelativeLayout.BELOW, ViewLine.getId());
        RelativeLayoutLoadingParam.addRule(RelativeLayout.ABOVE, ViewLine2.getId());

        RelativeLayoutLoading = new RelativeLayout(context);
        RelativeLayoutLoading.setLayoutParams(RelativeLayoutLoadingParam);

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
        TextViewTry.setText(getString(R.string.TryAgain));
        TextViewTry.setTextColor(ContextCompat.getColor(context, R.color.BlueGray2));
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
        AndroidNetworking.cancel("FragmentComment");
    }

    private void RetrieveDataFromServer(Context context)
    {
        TextViewTry.setVisibility(View.GONE);
        LoadingViewData.Start();

        AndroidNetworking.post(URLHandler.GetURL(URLHandler.URL.POST_COMMENT_LIST))
        .addBodyParameter("PostID", PostID)
        .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
        .setTag("FragmentComment")
        .build().getAsString(new StringRequestListener()
        {
            @Override
            public void onResponse(String Response)
            {
                RelativeLayoutLoading.setVisibility(View.GONE);

                try
                {
                    JSONObject Result = new JSONObject(Response);

                    if (Result.getInt("Message") == 1000)
                    {
                        JSONArray commentList = new JSONArray(Result.getString("Result"));

                        for (int K = 0; K < commentList.length(); K++)
                        {
                            JSONObject Comment = commentList.getJSONObject(K);
                            CommentList.add(new Struct(Comment.getString("CommentID"), Comment.getString("OwnerID"), Comment.getString("Username"), Comment.getInt("Time"), Comment.getString("Message"), Comment.getInt("LikeCount"), Comment.getBoolean("Like"), Comment.getString("Avatar")));
                        }

                        RecyclerViewCommentAdapter.notifyDataSetChanged();
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
                MiscHandler.Toast(getActivity(), getString(R.string.NoInternet));
                TextViewTry.setVisibility(View.GONE);
                LoadingViewData.Stop();
            }
        });
    }

    private class AdapterComment extends RecyclerView.Adapter<AdapterComment.ViewHolderComment>
    {
        private final int ID_PROFILE = MiscHandler.GenerateViewID();
        private final int ID_USERNAME = MiscHandler.GenerateViewID();
        private final int ID_TIME = MiscHandler.GenerateViewID();
        private final int ID_MESSAGE = MiscHandler.GenerateViewID();
        private final int ID_LIKE = MiscHandler.GenerateViewID();
        private final int ID_SHORTCUT = MiscHandler.GenerateViewID();
        private final int ID_REMOVE = MiscHandler.GenerateViewID();
        private final int ID_LIKE_COUNT = MiscHandler.GenerateViewID();
        private final int ID_VIEW_LINE = MiscHandler.GenerateViewID();

        private final Context context;

        AdapterComment(Context c)
        {
            context = c;
        }

        class ViewHolderComment extends RecyclerView.ViewHolder
        {
            ImageViewCircle ImageViewProfile;
            TextView TextViewUsername;
            TextView TextViewTime;
            TextView TextViewMessage;
            ImageView ImageViewLike;
            ImageView ImageViewShortcut;
            ImageView ImageViewRemove;
            TextView TextViewLikeCount;
            View ViewBlankLine;

            ViewHolderComment(View view, boolean Content)
            {
                super(view);

                if (Content)
                {
                    ImageViewProfile = (ImageViewCircle) view.findViewById(ID_PROFILE);
                    TextViewUsername = (TextView) view.findViewById(ID_USERNAME);
                    TextViewTime = (TextView) view.findViewById(ID_TIME);
                    TextViewMessage = (TextView) view.findViewById(ID_MESSAGE);
                    ImageViewLike = (ImageView) view.findViewById(ID_LIKE);
                    ImageViewShortcut = (ImageView) view.findViewById(ID_SHORTCUT);
                    ImageViewRemove = (ImageView) view.findViewById(ID_REMOVE);
                    TextViewLikeCount = (TextView) view.findViewById(ID_LIKE_COUNT);
                    ViewBlankLine = view.findViewById(ID_VIEW_LINE);
                }
            }
        }

        @Override
        public void onViewRecycled(ViewHolderComment Holder)
        {
            super.onViewRecycled(Holder);

            if (Holder.ImageViewProfile != null)
                Glide.clear(Holder.ImageViewProfile);
        }

        @Override
        public void onBindViewHolder(final ViewHolderComment Holder, int position)
        {
            if (CommentList.get(position) == null)
                return;

            final int Position = Holder.getAdapterPosition();

            Glide.with(FragmentComment.this)
            .load(CommentList.get(Position).Avatar)
            .placeholder(R.color.BlueGray)
            .override(MiscHandler.ToDimension(context, 55), MiscHandler.ToDimension(context, 55))
            .into(Holder.ImageViewProfile);

            String Username = CommentList.get(Position).Username;

            if (Username.length() > 25)
                Username = Username.substring(0, 25) + " ...";

            Holder.TextViewUsername.setText(Username);
            Holder.TextViewTime.setText(MiscHandler.GetTimeName(CommentList.get(Position).Time));
            Holder.TextViewMessage.setText(CommentList.get(Position).Message);

            new TagHandler(Holder.TextViewMessage, getActivity());

            if (CommentList.get(Position).Like)
                Holder.ImageViewLike.setImageResource(R.drawable.ic_like_red);
            else
                Holder.ImageViewLike.setImageResource(R.drawable.ic_like);

            Holder.ImageViewLike.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (CommentList.get(Position).Like)
                    {
                        Holder.ImageViewLike.setImageResource(R.drawable.ic_like);

                        ObjectAnimator Fade = ObjectAnimator.ofFloat(Holder.ImageViewLike, "alpha",  0.1f, 1f);
                        Fade.setDuration(400);

                        AnimatorSet AnimationSet = new AnimatorSet();
                        AnimationSet.play(Fade);
                        AnimationSet.start();

                        int LikeCount = Integer.parseInt(Holder.TextViewLikeCount.getText().toString()) - 1;
                        Holder.TextViewLikeCount.setText(String.valueOf(LikeCount));
                        CommentList.get(Position).DecreaseLike();
                        CommentList.get(Position).SetLike();
                    }
                    else
                    {
                        Holder.ImageViewLike.setImageResource(R.drawable.ic_like_red);

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

                        int LikeCount = Integer.parseInt(Holder.TextViewLikeCount.getText().toString()) + 1;
                        Holder.TextViewLikeCount.setText(String.valueOf(LikeCount));
                        CommentList.get(Position).IncreaseLike();
                        CommentList.get(Position).SetLike();
                    }

                    AndroidNetworking.post(URLHandler.GetURL(URLHandler.URL.POST_COMMENT_LIKE))
                    .addBodyParameter("CommentID", CommentList.get(Position).CommentID)
                    .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
                    .setTag("FragmentComment")
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
                                    if (Result.getBoolean("Like"))
                                        Holder.ImageViewLike.setImageResource(R.drawable.ic_like_red);
                                    else
                                        Holder.ImageViewLike.setImageResource(R.drawable.ic_like);
                                }
                            }
                            catch (Exception e)
                            {
                                // Leave Me Alone
                            }
                        }

                        @Override
                        public void onError(ANError anError) { }
                    });
                }
            });

            if (CommentList.get(Position).OwnerID.equals(SharedHandler.GetString(context, "ID")) || IsOwner)
            {
                Holder.ImageViewRemove.setVisibility(View.VISIBLE);
                Holder.ImageViewRemove.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        LinearLayout Root = new LinearLayout(context);
                        Root.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                        Root.setOrientation(LinearLayout.VERTICAL);

                        TextView TextViewTitle = new TextView(context);
                        TextViewTitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        TextViewTitle.setTextColor(ContextCompat.getColor(context, R.color.Black2));
                        TextViewTitle.setText(getString(R.string.FragmentCommentAreYouSure));
                        TextViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                        TextViewTitle.setPadding(MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10));

                        Root.addView(TextViewTitle);

                        View ViewLine = new View(context);
                        ViewLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1)));
                        ViewLine.setBackgroundResource(R.color.Gray2);

                        Root.addView(ViewLine);

                        LinearLayout LinearLayoutChoice = new LinearLayout(context);
                        LinearLayoutChoice.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        LinearLayoutChoice.setOrientation(LinearLayout.HORIZONTAL);

                        Root.addView(LinearLayoutChoice);

                        TextView TextViewYes = new TextView(context);
                        TextViewYes.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
                        TextViewYes.setTextColor(ContextCompat.getColor(context, R.color.Black4));
                        TextViewYes.setText(getString(R.string.FragmentCommentYes));
                        TextViewYes.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                        TextViewYes.setPadding(MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10));
                        TextViewYes.setGravity(Gravity.CENTER);

                        LinearLayoutChoice.addView(TextViewYes);

                        TextView TextViewNo = new TextView(context);
                        TextViewNo.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
                        TextViewNo.setTextColor(ContextCompat.getColor(context, R.color.Black4));
                        TextViewNo.setText(getString(R.string.FragmentCommentNo));
                        TextViewNo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                        TextViewNo.setPadding(MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10));
                        TextViewNo.setGravity(Gravity.CENTER);

                        LinearLayoutChoice.addView(TextViewNo);

                        final Dialog DialogDelete = new Dialog(getActivity());
                        DialogDelete.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        DialogDelete.setContentView(Root);
                        DialogDelete.show();

                        TextViewYes.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View view)
                            {
                                AndroidNetworking.post(URLHandler.GetURL(URLHandler.URL.POST_COMMENT_DELETE))
                                .addBodyParameter("PostID", PostID)
                                .addBodyParameter("CommentID", CommentList.get(Position).CommentID)
                                .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
                                .setTag("FragmentComment")
                                .build().getAsString(new StringRequestListener()
                                {
                                    @Override
                                    public void onResponse(String Response)
                                    {
                                        try
                                        {
                                            if (new JSONObject(Response).getInt("Message") == 1000)
                                            {
                                                CommentList.remove(Position);
                                                RecyclerViewCommentAdapter.notifyItemRemoved(Position);
                                            }
                                        }
                                        catch (Exception e)
                                        {
                                            // Leave Me Alone
                                        }
                                    }

                                    @Override
                                    public void onError(ANError anError) { }
                                });

                                DialogDelete.dismiss();
                            }
                        });

                        TextViewNo.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View view)
                            {
                                DialogDelete.dismiss();
                            }
                        });
                    }
                });
            }
            else
                Holder.ImageViewRemove.setVisibility(View.GONE);

            Holder.ImageViewShortcut.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    String Message = EditTextComment.getText().toString();
                    Message += "@" + CommentList.get(Position).Username;
                    EditTextComment.setText(Message);
                }
            });

            Holder.TextViewLikeCount.setText(String.valueOf(CommentList.get(Position).LikeCount));

            if (Position == CommentList.size() - 1)
                Holder.ViewBlankLine.setVisibility(View.GONE);
            else
                Holder.ViewBlankLine.setVisibility(View.VISIBLE);
        }

        @Override
        public ViewHolderComment onCreateViewHolder(ViewGroup parent, int ViewType)
        {
            if (ViewType == 0)
            {
                RelativeLayout Root = new RelativeLayout(context);
                Root.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

                RelativeLayout.LayoutParams ImageViewCircleProfileParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 55), MiscHandler.ToDimension(context, 55));
                ImageViewCircleProfileParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));

                ImageViewCircle ImageViewCircleProfile = new ImageViewCircle(context);
                ImageViewCircleProfile.setLayoutParams(ImageViewCircleProfileParam);
                ImageViewCircleProfile.setImageResource(R.color.BlueGray);
                ImageViewCircleProfile.setId(ID_PROFILE);

                Root.addView(ImageViewCircleProfile);

                RelativeLayout.LayoutParams TextViewUsernameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                TextViewUsernameParam.setMargins(0, MiscHandler.ToDimension(context, 15), 0, 0);
                TextViewUsernameParam.addRule(RelativeLayout.RIGHT_OF, ImageViewCircleProfile.getId());

                TextView TextViewUsername = new TextView(context);
                TextViewUsername.setLayoutParams(TextViewUsernameParam);
                TextViewUsername.setTextColor(ContextCompat.getColor(context, R.color.Black));
                TextViewUsername.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                TextViewUsername.setId(ID_USERNAME);

                Root.addView(TextViewUsername);

                RelativeLayout.LayoutParams TextViewTimeParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                TextViewTimeParam.setMargins(MiscHandler.ToDimension(context, 10), 0, MiscHandler.ToDimension(context, 10), 0);
                TextViewTimeParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                TextViewTimeParam.addRule(RelativeLayout.ALIGN_BOTTOM, TextViewUsername.getId());

                TextView TextViewTime = new TextView(context);
                TextViewTime.setLayoutParams(TextViewTimeParam);
                TextViewTime.setTextColor(ContextCompat.getColor(context, R.color.BlueGray2));
                TextViewTime.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                TextViewTime.setId(ID_TIME);

                Root.addView(TextViewTime);

                RelativeLayout.LayoutParams TextViewMessageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                TextViewMessageParam.setMargins(0, MiscHandler.ToDimension(context, 5), MiscHandler.ToDimension(context, 10), 0);
                TextViewMessageParam.addRule(RelativeLayout.RIGHT_OF, ImageViewCircleProfile.getId());
                TextViewMessageParam.addRule(RelativeLayout.BELOW, TextViewUsername.getId());

                TextView TextViewMessage = new TextView(context);
                TextViewMessage.setLayoutParams(TextViewMessageParam);
                TextViewMessage.setTextColor(ContextCompat.getColor(context, R.color.Black3));
                TextViewMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                TextViewMessage.setId(ID_MESSAGE);

                Root.addView(TextViewMessage);

                RelativeLayout.LayoutParams RelativeLayoutToolParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                RelativeLayoutToolParam.addRule(RelativeLayout.RIGHT_OF, ImageViewCircleProfile.getId());
                RelativeLayoutToolParam.addRule(RelativeLayout.BELOW, TextViewMessage.getId());

                RelativeLayout RelativeLayoutTool = new RelativeLayout(context);
                RelativeLayoutTool.setLayoutParams(RelativeLayoutToolParam);
                RelativeLayoutTool.setId(MiscHandler.GenerateViewID());

                Root.addView(RelativeLayoutTool);

                ImageView ImageViewLike = new ImageView(context);
                ImageViewLike.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 30), MiscHandler.ToDimension(context, 30)));
                ImageViewLike.setPadding(MiscHandler.ToDimension(context, 6), MiscHandler.ToDimension(context, 6), MiscHandler.ToDimension(context, 6), MiscHandler.ToDimension(context, 6));
                ImageViewLike.setScaleType(ImageView.ScaleType.FIT_CENTER);
                ImageViewLike.setImageResource(R.drawable.ic_like);
                ImageViewLike.setId(ID_LIKE);

                RelativeLayoutTool.addView(ImageViewLike);

                RelativeLayout.LayoutParams ImageViewShortcutParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 30), MiscHandler.ToDimension(context, 30));
                ImageViewShortcutParam.addRule(RelativeLayout.RIGHT_OF, ImageViewLike.getId());

                ImageView ImageViewShortcut = new ImageView(context);
                ImageViewShortcut.setLayoutParams(ImageViewShortcutParam);
                ImageViewShortcut.setPadding(MiscHandler.ToDimension(context, 4), MiscHandler.ToDimension(context, 4), MiscHandler.ToDimension(context, 4), MiscHandler.ToDimension(context, 4));
                ImageViewShortcut.setScaleType(ImageView.ScaleType.FIT_CENTER);
                ImageViewShortcut.setImageResource(R.drawable.ic_share);
                ImageViewShortcut.setId(ID_SHORTCUT);

                RelativeLayoutTool.addView(ImageViewShortcut);

                RelativeLayout.LayoutParams ImageViewRemoveParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 30), MiscHandler.ToDimension(context, 30));
                ImageViewRemoveParam.addRule(RelativeLayout.RIGHT_OF, ImageViewShortcut.getId());

                ImageView ImageViewRemove = new ImageView(context);
                ImageViewRemove.setLayoutParams(ImageViewRemoveParam);
                ImageViewRemove.setPadding(MiscHandler.ToDimension(context, 3), MiscHandler.ToDimension(context, 3), MiscHandler.ToDimension(context, 3), MiscHandler.ToDimension(context, 3));
                ImageViewRemove.setScaleType(ImageView.ScaleType.FIT_CENTER);
                ImageViewRemove.setImageResource(R.drawable.ic_remove_gray);
                ImageViewRemove.setId(ID_REMOVE);
                ImageViewRemove.setVisibility(View.GONE);

                RelativeLayoutTool.addView(ImageViewRemove);

                RelativeLayout.LayoutParams TextViewLikeCountParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                TextViewLikeCountParam.setMargins(MiscHandler.ToDimension(context, 6), 0, MiscHandler.ToDimension(context, 6), 0);
                TextViewLikeCountParam.addRule(RelativeLayout.RIGHT_OF, ImageViewRemove.getId());
                TextViewLikeCountParam.addRule(RelativeLayout.CENTER_VERTICAL);

                TextView TextViewLikeCount = new TextView(context);
                TextViewLikeCount.setLayoutParams(TextViewLikeCountParam);
                TextViewLikeCount.setTextColor(ContextCompat.getColor(context, R.color.BlueGray2));
                TextViewLikeCount.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                TextViewLikeCount.setId(ID_LIKE_COUNT);

                RelativeLayoutTool.addView(TextViewLikeCount);

                RelativeLayout.LayoutParams TextViewLikeParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                TextViewLikeParam.setMargins(MiscHandler.ToDimension(context, 6), 0, MiscHandler.ToDimension(context, 6), 0);
                TextViewLikeParam.addRule(RelativeLayout.RIGHT_OF, TextViewLikeCount.getId());
                TextViewLikeParam.addRule(RelativeLayout.CENTER_VERTICAL);

                TextView TextViewLike = new TextView(context);
                TextViewLike.setLayoutParams(TextViewLikeParam);
                TextViewLike.setTextColor(ContextCompat.getColor(context, R.color.BlueGray2));
                TextViewLike.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                TextViewLike.setText(getString(R.string.FragmentCommentLike));

                RelativeLayoutTool.addView(TextViewLike);

                RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1));
                ViewLineParam.addRule(RelativeLayout.BELOW, RelativeLayoutTool.getId());

                View ViewLine = new View(context);
                ViewLine.setLayoutParams(ViewLineParam);
                ViewLine.setBackgroundResource(R.color.Gray);
                ViewLine.setId(ID_VIEW_LINE);

                Root.addView(ViewLine);

                return new ViewHolderComment(Root, true);
            }

            LoadingView Loading = new LoadingView(context);
            Loading.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56)));
            Loading.SetShow(true);
            Loading.Start();

            return new ViewHolderComment(Loading, false);
        }

        @Override
        public int getItemCount()
        {
            return CommentList.size();
        }

        @Override
        public int getItemViewType(int position)
        {
            return CommentList.get(position)!= null ? 0 : 1;
        }
    }

    private class Struct
    {
        final String CommentID;
        final String OwnerID;
        final String Username;
        final long Time;
        final String Message;
        int LikeCount;
        boolean Like;
        final String Avatar;

        Struct(String id, String owner, String username, long time, String message, int likeCount, boolean like, String avatar)
        {
            CommentID = id;
            OwnerID = owner;
            Username = username;
            Time = time;
            Message = message;
            LikeCount = likeCount;
            Like = like;
            Avatar = avatar;
        }

        void IncreaseLike() { LikeCount = LikeCount + 1; }
        void DecreaseLike() { LikeCount = LikeCount - 1; }
        void SetLike() { Like = !Like; }
    }
}
