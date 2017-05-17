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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import co.biogram.main.R;
import co.biogram.main.handler.MiscHandler;
import co.biogram.main.handler.RequestHandler;
import co.biogram.main.handler.SharedHandler;
import co.biogram.main.handler.TagHandler;
import co.biogram.main.handler.URLHandler;
import co.biogram.main.misc.LoadingView;
import co.biogram.main.misc.ImageViewCircle;

public class FragmentComment extends Fragment
{
    private TextView TextViewTryAgain;
    private LoadingView LoadingViewRoot;
    private RelativeLayout RelativeLayoutLoading;

    private String PostID = "";
    private boolean IsOwner = false;
    private boolean IsSending = false;
    private boolean IsLoadingTop = false;
    private boolean IsLoadingBottom = false;
    private EditText EditTextComment;
    private AdapterComment RecyclerViewCommentAdapter;
    private List<Struct> CommentList = new ArrayList<>();

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
        RelativeLayoutSend.setBackgroundResource(R.color.White5);
        RelativeLayoutSend.setId(MiscHandler.GenerateViewID());

        RelativeLayoutBottom.addView(RelativeLayoutSend);

        final ImageView ImageViewSend = new ImageView(context);
        ImageViewSend.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), MiscHandler.ToDimension(context, 56)));
        ImageViewSend.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewSend.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
        ImageViewSend.setImageResource(R.drawable.ic_send_gray);

        RelativeLayoutSend.addView(ImageViewSend);

        RelativeLayout.LayoutParams LoadingViewSendParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        LoadingViewSendParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        LoadingView LoadingViewSend = new LoadingView(context);
        LoadingViewSend.setLayoutParams(LoadingViewSendParam);
        LoadingViewSend.SetColor(R.color.BlueLight);

        RelativeLayoutSend.addView(LoadingViewSend);

        RelativeLayout.LayoutParams EditTextCommentParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        EditTextCommentParam.addRule(RelativeLayout.LEFT_OF, RelativeLayoutSend.getId());

        EditText EditTextComment = new EditText(context);
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
        ViewLineParam.addRule(RelativeLayout.ABOVE, RelativeLayoutBottom.getId());

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

            }
        });

        Root.addView(SwipeRefreshLayoutComment);

        RecyclerView RecyclerViewComment = new RecyclerView(context);
        RecyclerViewComment.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RecyclerViewComment.setLayoutManager(new LinearLayoutManager(context));
        //RecyclerViewMoment.setAdapter(PostAdapter);
        RecyclerViewComment.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView View, int DX, int DY)
            {

            }
        });

        SwipeRefreshLayoutComment.addView(RecyclerViewComment);

        return Root;


























        /*
        final RecyclerView RecyclerViewComment = (RecyclerView) Root.findViewById(R.id.RecyclerViewComment);
        RecyclerViewCommentAdapter = new AdapterComment(getActivity());

        RecyclerViewComment.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerViewComment.setAdapter(RecyclerViewCommentAdapter);
        RecyclerViewComment.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView View, int dx, int DY)
            {
                if (DY <= 0)
                    return;

                if (((LinearLayoutManager) View.getLayoutManager()).findLastVisibleItemPosition() + 2 > View.getAdapter().getItemCount() && !IsLoadingBottom)
                {
                    CommentList.add(null);
                    IsLoadingBottom = true;
                    RecyclerViewCommentAdapter.notifyItemInserted(CommentList.size());

                    AndroidNetworking.post(URLHandler.GetURL(URLHandler.URL.POST_COMMENT_LIST))
                    .addBodyParameter("PostID", PostID)
                    .addBodyParameter("Skip", String.valueOf(CommentList.size()))
                    .addHeaders("TOKEN", SharedHandler.GetString("TOKEN"))
                    .setTag("FragmentComment").build().getAsString(new StringRequestListener()
                    {
                        @Override
                        public void onResponse(String Response)
                        {
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

                            IsLoadingBottom = false;
                        }

                        @Override
                        public void onError(ANError error)
                        {
                            CommentList.remove(CommentList.size() - 1);
                            RecyclerViewCommentAdapter.notifyItemRemoved(CommentList.size());
                            IsLoadingBottom = false;
                        }
                    });
                }
            }
        });

        final SwipeRefreshLayout SwipeRefreshLayoutComment = (SwipeRefreshLayout) Root.findViewById(R.id.SwipeRefreshLayoutComment);
        SwipeRefreshLayoutComment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                if (IsLoadingTop || CommentList.size() <= 0)
                    return;

                IsLoadingTop = true;
                SwipeRefreshLayoutComment.setEnabled(false);
                SwipeRefreshLayoutComment.setRefreshing(false);

                AndroidNetworking.post(URLHandler.GetURL(URLHandler.URL.POST_COMMENT_LIST))
                .addBodyParameter("PostID", PostID)
                .addBodyParameter("CommentTime", String.valueOf(CommentList.get(0).Time))
                .addHeaders("TOKEN", SharedHandler.GetString("TOKEN"))
                .setTag("FragmentComment").build().getAsString(new StringRequestListener()
                {
                    @Override
                    public void onResponse(String Response)
                    {
                        try
                        {
                            JSONObject Result = new JSONObject(Response);

                            if (Result.getInt("Message") == 1000 && !Result.getString("Result").equals(""))
                            {
                                JSONArray CommentArray = new JSONArray(Result.getString("Result"));

                                for (int K = 0; K < CommentArray.length(); K++)
                                {
                                    JSONObject Comment = CommentArray.getJSONObject(K);
                                    CommentList.add(0, new Struct(Comment.getString("CommentID"), Comment.getString("OwnerID"), Comment.getString("Username"), Comment.getInt("Time"), Comment.getString("Message"), Comment.getInt("LikeCount"), Comment.getBoolean("Like"), Comment.getString("Avatar")));
                                }

                                RecyclerViewCommentAdapter.notifyDataSetChanged();
                            }
                        }
                        catch (Exception e)
                        {
                            // Leave Me Alone
                        }

                        IsLoadingTop = false;
                        SwipeRefreshLayoutComment.setEnabled(true);
                    }

                    @Override
                    public void onError(ANError error)
                    {
                        IsLoadingTop = false;
                        SwipeRefreshLayoutComment.setEnabled(true);
                    }
                });
            }
        });

        final LoadingView LoadingViewComment = (LoadingView) Root.findViewById(R.id.LoadingViewComment);
        final ImageView ImageViewSend = (ImageView) Root.findViewById(R.id.ImageViewSend);
        EditTextComment = (EditText) Root.findViewById(R.id.EditTextComment);

        ImageViewSend.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (IsSending || EditTextComment.getText().toString().equals(""))
                    return;

                IsSending = true;
                LoadingViewComment.Start();
                ImageViewSend.setVisibility(View.GONE);

                AndroidNetworking.post(URLHandler.GetURL(URLHandler.URL.POST_COMMENT))
                .addBodyParameter("PostID", PostID)
                .addBodyParameter("Message", EditTextComment.getText().toString())
                .addHeaders("TOKEN", SharedHandler.GetString("TOKEN"))
                .setTag("FragmentComment").build().getAsString(new StringRequestListener()
                {
                    @Override
                    public void onResponse(String Response)
                    {
                        try
                        {
                            ImageViewSend.setVisibility(View.VISIBLE);
                            ImageViewSend.setImageResource(R.drawable.ic_send_blue);
                            JSONObject Result = new JSONObject(Response);

                            if (Result.getInt("Message") == 1000)
                            {
                                RecyclerViewComment.scrollToPosition(0);
                                CommentList.add(0, new Struct(Result.getString("CommentID"), SharedHandler.GetString("ID"), SharedHandler.GetString("Username"), (System.currentTimeMillis() + 50000) , EditTextComment.getText().toString(), 0, false, SharedHandler.GetString("Avatar")));
                                RecyclerViewCommentAdapter.notifyDataSetChanged();
                                ImageViewSend.setImageResource(R.drawable.ic_send_gray);
                                EditTextComment.setText("");
                            }
                        }
                        catch (Exception e)
                        {
                            // Leave Me Alone
                        }

                        IsSending = false;
                        LoadingViewComment.Stop();
                    }

                    @Override
                    public void onError(ANError error)
                    {
                        IsSending = false;
                        LoadingViewComment.Stop();
                        ImageViewSend.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

       */
    }

    @Override
    public void onPause()
    {
        super.onPause();
        RequestHandler.Core().Cancel("FragmentComment");
    }

    private void RetrieveDataFromServer()
    {
        TextViewTryAgain.setVisibility(View.GONE);
        RelativeLayoutLoading.setVisibility(View.VISIBLE);
        LoadingViewRoot.Start();

        AndroidNetworking.post(URLHandler.GetURL(URLHandler.URL.POST_COMMENT_LIST))
        .addBodyParameter("PostID", PostID)
        .addHeaders("TOKEN", SharedHandler.GetString("TOKEN"))
        .setTag("FragmentComment").build().getAsString(new StringRequestListener()
        {
            @Override
            public void onResponse(String Response)
            {
                try
                {
                    JSONObject Result = new JSONObject(Response);

                    if (Result.getInt("Message") == 1000)
                    {
                        JSONArray List = new JSONArray(Result.getString("Result"));

                        for (int K = 0; K < List.length(); K++)
                        {
                            JSONObject Comment = List.getJSONObject(K);
                            CommentList.add(new Struct(Comment.getString("CommentID"), Comment.getString("OwnerID"), Comment.getString("Username"), Comment.getInt("Time"), Comment.getString("Message"), Comment.getInt("LikeCount"), Comment.getBoolean("Like"), Comment.getString("Avatar")));
                        }

                        RecyclerViewCommentAdapter.notifyDataSetChanged();
                    }
                }
                catch (Exception e)
                {
                    // Leave Me Alone
                }

                TextViewTryAgain.setVisibility(View.GONE);
                RelativeLayoutLoading.setVisibility(View.GONE);
                LoadingViewRoot.Stop();
            }

            @Override
            public void onError(ANError error)
            {
                TextViewTryAgain.setVisibility(View.VISIBLE);
                RelativeLayoutLoading.setVisibility(View.VISIBLE);
                LoadingViewRoot.Stop();

                MiscHandler.Toast(getActivity(), getString(R.string.GeneralCheckInternet));
            }
        });
    }

    private class AdapterComment extends RecyclerView.Adapter<AdapterComment.ViewHolderComment>
    {
        private Context context;

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
                    ImageViewProfile = (ImageViewCircle) view.findViewById(R.id.ImageViewProfile);
                    TextViewUsername = (TextView) view.findViewById(R.id.TextViewUsername);
                    TextViewTime = (TextView) view.findViewById(R.id.TextViewTime);
                    TextViewMessage = (TextView) view.findViewById(R.id.TextViewMessage);
                    ImageViewLike = (ImageView) view.findViewById(R.id.ImageViewLike);
                    ImageViewShortcut = (ImageView) view.findViewById(R.id.ImageViewShortcut);
                    ImageViewRemove = (ImageView) view.findViewById(R.id.ImageViewRemove);
                    TextViewLikeCount = (TextView) view.findViewById(R.id.TextViewLikeCount);
                    ViewBlankLine = view.findViewById(R.id.ViewBlankLine);
                }
            }
        }

        @Override
        public void onBindViewHolder(final ViewHolderComment Holder, int position)
        {
            if (CommentList.get(position) == null)
                return;

            final int Position = Holder.getAdapterPosition();

            RequestHandler.Core().LoadImage(Holder.ImageViewProfile, CommentList.get(Position).Avatar, "FragmentComment", MiscHandler.ToDimension(55), MiscHandler.ToDimension(55), true);

            String Username = CommentList.get(Position).Username;

            if (Username.length() > 20)
                Username = Username.substring(0, 20) + " ...";

            Holder.TextViewUsername.setText(Username);
            Holder.TextViewTime.setText(MiscHandler.GetTimeName(CommentList.get(Position).Time));
            Holder.TextViewMessage.setText(CommentList.get(Position).Message);

            new TagHandler(Holder.TextViewMessage, new TagHandler.OnTagClickListener()
            {
                @Override
                public void OnTagClicked(String Tag, int Type)
                {
                    MiscHandler.Toast(getActivity(), Tag);
                }
            });

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
                        Fade.setDuration(800);

                        AnimatorSet AnimationSet = new AnimatorSet();
                        AnimationSet.play(Fade);
                        AnimationSet.start();

                        int LikeCount = Integer.parseInt(Holder.TextViewLikeCount.getText().toString()) - 1;
                        Holder.TextViewLikeCount.setText(String.valueOf(LikeCount));
                        CommentList.get(Position).DecreaseLike();
                        CommentList.get(Position).SetLike();

                        AndroidNetworking.post(URLHandler.GetURL(URLHandler.URL.POST_COMMENT_LIKE))
                        .addBodyParameter("CommentID", CommentList.get(Position).CommentID)
                        .addHeaders("TOKEN", SharedHandler.GetString("TOKEN"))
                        .setTag("FragmentComment").build().getAsString(null);
                    }
                    else
                    {
                        Holder.ImageViewLike.setImageResource(R.drawable.ic_like_red);

                        ObjectAnimator SizeX = ObjectAnimator.ofFloat(Holder.ImageViewLike, "scaleX", 1.5f);
                        SizeX.setDuration(400);

                        ObjectAnimator SizeY = ObjectAnimator.ofFloat(Holder.ImageViewLike, "scaleY", 1.5f);
                        SizeY.setDuration(400);

                        ObjectAnimator Fade = ObjectAnimator.ofFloat(Holder.ImageViewLike, "alpha",  0.1f, 1f);
                        Fade.setDuration(800);

                        ObjectAnimator SizeX2 = ObjectAnimator.ofFloat(Holder.ImageViewLike, "scaleX", 1f);
                        SizeX2.setDuration(400);
                        SizeX2.setStartDelay(400);

                        ObjectAnimator SizeY2 = ObjectAnimator.ofFloat(Holder.ImageViewLike, "scaleY", 1f);
                        SizeY2.setDuration(400);
                        SizeY2.setStartDelay(400);

                        AnimatorSet AnimationSet = new AnimatorSet();
                        AnimationSet.playTogether(SizeX, SizeY, Fade, SizeX2, SizeY2);
                        AnimationSet.start();

                        int LikeCount = Integer.parseInt(Holder.TextViewLikeCount.getText().toString()) + 1;
                        Holder.TextViewLikeCount.setText(String.valueOf(LikeCount));
                        CommentList.get(Position).IncreaseLike();
                        CommentList.get(Position).SetLike();

                        AndroidNetworking.post(URLHandler.GetURL(URLHandler.URL.POST_COMMENT_LIKE))
                        .addBodyParameter("CommentID", CommentList.get(Position).CommentID)
                        .addHeaders("TOKEN", SharedHandler.GetString("TOKEN"))
                        .setTag("FragmentComment").build().getAsString(null);
                    }
                }
            });

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

            if (CommentList.get(Position).OwnerID.equals(SharedHandler.GetString("ID")) || IsOwner)
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

                        TextView Title = new TextView(context);
                        Title.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        Title.setTextColor(ContextCompat.getColor(context, R.color.Black2));
                        Title.setText(getString(R.string.FragmentCommentAreYouSure));
                        Title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                        Title.setPadding(MiscHandler.ToDimension(10), MiscHandler.ToDimension(10), MiscHandler.ToDimension(10), MiscHandler.ToDimension(10));

                        Root.addView(Title);

                        View Line = new View(context);
                        Line.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(1)));
                        Line.setBackgroundColor(ContextCompat.getColor(context, R.color.Gray2));

                        Root.addView(Line);

                        LinearLayout Linear = new LinearLayout(context);
                        Linear.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        Linear.setOrientation(LinearLayout.HORIZONTAL);

                        Root.addView(Linear);

                        TextView Yes = new TextView(context);
                        Yes.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
                        Yes.setTextColor(ContextCompat.getColor(context, R.color.Black4));
                        Yes.setText(getString(R.string.FragmentCommentYes));
                        Yes.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                        Yes.setPadding(MiscHandler.ToDimension(10), MiscHandler.ToDimension(10), MiscHandler.ToDimension(10), MiscHandler.ToDimension(10));
                        Yes.setGravity(Gravity.CENTER);

                        Linear.addView(Yes);

                        TextView No = new TextView(context);
                        No.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
                        No.setTextColor(ContextCompat.getColor(context, R.color.Black4));
                        No.setText(getString(R.string.FragmentCommentNo));
                        No.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                        No.setPadding(MiscHandler.ToDimension(10), MiscHandler.ToDimension(10), MiscHandler.ToDimension(10), MiscHandler.ToDimension(10));
                        No.setGravity(Gravity.CENTER);

                        Linear.addView(No);

                        final Dialog DialogDelete = new Dialog(getActivity());
                        DialogDelete.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        DialogDelete.setContentView(Root);
                        DialogDelete.show();

                        Yes.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View view)
                            {
                                AndroidNetworking.post(URLHandler.GetURL(URLHandler.URL.POST_COMMENT_DELETE)).addBodyParameter("PostID", PostID).addBodyParameter("CommentID", CommentList.get(Position).CommentID).addHeaders("TOKEN", SharedHandler.GetString("TOKEN")).setTag("FragmentComment").build().getAsString(new StringRequestListener()
                                {
                                    @Override
                                    public void onResponse(String Response)
                                    {
                                        try
                                        {
                                            if (new JSONObject(Response).getInt("Message") == 1000)
                                            {
                                                CommentList.remove(Position);
                                                RecyclerViewCommentAdapter.notifyDataSetChanged();
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

                        No.setOnClickListener(new View.OnClickListener()
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

            Holder.TextViewLikeCount.setText(String.valueOf(CommentList.get(Position).LikeCount));

            if (Position == CommentList.size() - 1)
                Holder.ViewBlankLine.setVisibility(View.GONE);
            else
                Holder.ViewBlankLine.setVisibility(View.VISIBLE);
        }

        @Override
        public int getItemCount()
        {
            return CommentList.size();
        }

        @Override
        public ViewHolderComment onCreateViewHolder(ViewGroup parent, int ViewType)
        {
            View ItemView;

            if (ViewType == 0)
            {
                ItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_comment_row, parent, false);
                return new ViewHolderComment(ItemView, true);
            }

            LinearLayout Root = new LinearLayout(context);
            Root.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(56)));
            Root.setGravity(Gravity.CENTER);

            LoadingView Loading = new LoadingView(context);
            Loading.Start();

            Root.addView(Loading);

            return new ViewHolderComment(Root, false);
        }

        @Override
        public int getItemViewType(int position)
        {
            return CommentList.get(position)!= null ? 0 : 1;
        }
    }

    private class Struct
    {
        String CommentID;
        String OwnerID;
        String Username;
        long Time;
        String Message;
        int LikeCount;
        boolean Like;
        String Avatar;

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
