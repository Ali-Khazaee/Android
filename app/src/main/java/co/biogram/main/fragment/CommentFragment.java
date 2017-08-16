package co.biogram.main.fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import android.view.inputmethod.InputMethodManager;
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
import co.biogram.main.misc.LoadingView;
import co.biogram.main.misc.ImageViewCircle;
import co.biogram.main.misc.RecyclerViewScroll;

public class CommentFragment extends Fragment
{
    private final List<StructComment> CommentList = new ArrayList<>();
    private RecyclerViewScroll RecyclerViewScrollMain;
    private EditText EditTextComment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final Context context = getActivity();
        final String PostID = getArguments().getString("PostID", "");
        final RecyclerView RecyclerViewMain = new RecyclerView(context);
        final AdapterMain Adapter = new AdapterMain(context, getArguments().getString("OwnerID", "").equals(SharedHandler.GetString(context, "ID")), PostID);

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
        TextViewTitle.setText(getString(R.string.CommentFragment));
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

        RelativeLayoutMain.addView(ViewLine);

        RelativeLayout.LayoutParams RelativeLayoutBottomParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56));
        RelativeLayoutBottomParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        RelativeLayout RelativeLayoutBottom = new RelativeLayout(context);
        RelativeLayoutBottom.setLayoutParams(RelativeLayoutBottomParam);
        RelativeLayoutBottom.setBackgroundResource(R.color.White5);
        RelativeLayoutBottom.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain.addView(RelativeLayoutBottom);

        RelativeLayout.LayoutParams RelativeLayoutSendParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), MiscHandler.ToDimension(context, 56));
        RelativeLayoutSendParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        RelativeLayout RelativeLayoutSend = new RelativeLayout(context);
        RelativeLayoutSend.setLayoutParams(RelativeLayoutSendParam);
        RelativeLayoutSend.setId(MiscHandler.GenerateViewID());

        RelativeLayoutBottom.addView(RelativeLayoutSend);

        RelativeLayout.LayoutParams LoadingViewSendParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        LoadingViewSendParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        final LoadingView LoadingViewSend = new LoadingView(context);
        LoadingViewSend.setLayoutParams(LoadingViewSendParam);
        LoadingViewSend.SetColor(R.color.BlueLight);

        RelativeLayoutSend.addView(LoadingViewSend);

        final ImageView ImageViewSend = new ImageView(context);
        ImageViewSend.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), MiscHandler.ToDimension(context, 56)));
        ImageViewSend.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewSend.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
        ImageViewSend.setImageResource(R.drawable.ic_send_gray);
        ImageViewSend.setOnClickListener(new View.OnClickListener()
        {
            private boolean IsSending = false;

            @Override
            public void onClick(View view)
            {
                if (IsSending || EditTextComment.getText().toString().equals(""))
                    return;

                ImageViewSend.setVisibility(View.GONE);
                LoadingViewSend.Start();
                IsSending = true;

                AndroidNetworking.post(MiscHandler.GetRandomServer("PostComment"))
                .addBodyParameter("Message", EditTextComment.getText().toString())
                .addBodyParameter("PostID", PostID)
                .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
                .setTag("CommentFragment")
                .build()
                .getAsString(new StringRequestListener()
                {
                    @Override
                    public void onResponse(String Response)
                    {
                        try
                        {
                            JSONObject Result = new JSONObject(Response);

                            if (Result.getInt("Message") == 1000)
                            {
                                StructComment StructComment = new StructComment();
                                StructComment.CommentID = Result.getString("CommentID");
                                StructComment.OwnerID = SharedHandler.GetString(context, "ID");
                                StructComment.Username = SharedHandler.GetString(context, "Username");
                                StructComment.Time = (System.currentTimeMillis() / 1000) - 2;
                                StructComment.Message = EditTextComment.getText().toString();
                                StructComment.LikeCount = 0;
                                StructComment.Like = false;
                                StructComment.Avatar = SharedHandler.GetString(context, "Avatar");

                                CommentList.add(0, StructComment);
                                Adapter.notifyDataSetChanged();
                                RecyclerViewMain.scrollToPosition(0);
                            }
                        }
                        catch (Exception e)
                        {
                            MiscHandler.Debug("CommentFragment-CommentSent: " + e.toString());
                        }

                        IsSending = false;
                        LoadingViewSend.Stop();
                        EditTextComment.setText("");
                        ImageViewSend.setVisibility(View.VISIBLE);
                        ImageViewSend.setImageResource(R.drawable.ic_send_gray);
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

        RelativeLayout.LayoutParams EditTextCommentParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        EditTextCommentParam.addRule(RelativeLayout.LEFT_OF, RelativeLayoutSend.getId());

        EditTextComment = new EditText(context);
        EditTextComment.setLayoutParams(EditTextCommentParam);
        EditTextComment.setHint(R.string.CommentFragmentHint);
        EditTextComment.setPadding(MiscHandler.ToDimension(context, 15), 0, MiscHandler.ToDimension(context, 15), 0);
        EditTextComment.setId(MiscHandler.GenerateViewID());
        EditTextComment.setBackground(null);
        EditTextComment.requestFocus();
        EditTextComment.setTextColor(ContextCompat.getColor(context, R.color.Black));
        EditTextComment.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        EditTextComment.setFilters(new InputFilter[] { new InputFilter.LengthFilter(150) });
        EditTextComment.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        EditTextComment.setHintTextColor(ContextCompat.getColor(context, R.color.Gray2));
        EditTextComment.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2)
            {
                if (s.length() == 0)
                    ImageViewSend.setImageResource(R.drawable.ic_send_gray);
                else
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

        RelativeLayoutMain.addView(ViewLine2);

        RelativeLayout.LayoutParams RecyclerViewMainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        RecyclerViewMainParam.addRule(RelativeLayout.BELOW, ViewLine.getId());
        RecyclerViewMainParam.addRule(RelativeLayout.ABOVE, ViewLine2.getId());

        LinearLayoutManager LinearLayoutManagerComment = new LinearLayoutManager(context);

        RecyclerViewScrollMain = new RecyclerViewScroll(LinearLayoutManagerComment)
        {
            @Override
            public void OnLoadMore()
            {
                CommentList.add(null);
                Adapter.notifyDataSetChanged();

                AndroidNetworking.post(MiscHandler.GetRandomServer("PostCommentList"))
                .addBodyParameter("Skip", String.valueOf(CommentList.size() - 1))
                .addBodyParameter("PostID", PostID)
                .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
                .setTag("CommentFragment")
                .build()
                .getAsString(new StringRequestListener()
                {
                    @Override
                    public void onResponse(String Response)
                    {
                        CommentList.remove(CommentList.size() - 1);

                        try
                        {
                            JSONObject Result = new JSONObject(Response);

                            if (Result.getInt("Message") == 1000 && !Result.getString("Result").equals(""))
                            {
                                JSONArray ResultList = new JSONArray(Result.getString("Result"));

                                for (int K = 0; K < ResultList.length(); K++)
                                {
                                    JSONObject Comment = ResultList.getJSONObject(K);

                                    StructComment StructComment = new StructComment();
                                    StructComment.CommentID = Comment.getString("CommentID");
                                    StructComment.OwnerID = Comment.getString("OwnerID");
                                    StructComment.Username = Comment.getString("Username");
                                    StructComment.Time = Comment.getInt("Time");
                                    StructComment.Message = Comment.getString("Message");
                                    StructComment.LikeCount = Comment.getInt("LikeCount");
                                    StructComment.Like = Comment.getBoolean("Like");
                                    StructComment.Avatar = Comment.getString("Avatar");

                                    CommentList.add(StructComment);
                                }

                                Adapter.notifyDataSetChanged();
                            }
                        }
                        catch (Exception e)
                        {
                            RecyclerViewScrollMain.ResetLoading(false);
                            MiscHandler.Debug("CommentFragment-RequestMore: " + e.toString());
                        }
                    }

                    @Override
                    public void onError(ANError error)
                    {
                        CommentList.remove(CommentList.size() - 1);
                        Adapter.notifyDataSetChanged();
                        RecyclerViewScrollMain.ResetLoading(false);

                        MiscHandler.Toast(context, getString(R.string.NoInternet));
                    }
                });
            }
        };

        RecyclerViewMain.setLayoutParams(RecyclerViewMainParam);
        RecyclerViewMain.setLayoutManager(LinearLayoutManagerComment);
        RecyclerViewMain.setAdapter(Adapter);
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

        RelativeLayout.LayoutParams TextViewTryAgainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTryAgainParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        final TextView TextViewTryAgain = new TextView(context);
        TextViewTryAgain.setLayoutParams(TextViewTryAgainParam);
        TextViewTryAgain.setText(getString(R.string.TryAgain));
        TextViewTryAgain.setTextColor(ContextCompat.getColor(context, R.color.BlueGray2));
        TextViewTryAgain.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        TextViewTryAgain.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { RetrieveDataFromServer(context, PostID, Adapter, RelativeLayoutLoading, LoadingViewMain, TextViewTryAgain); } });

        RelativeLayoutLoading.addView(TextViewTryAgain);

        RetrieveDataFromServer(context, PostID, Adapter, RelativeLayoutLoading, LoadingViewMain, TextViewTryAgain);

        return RelativeLayoutMain;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        AndroidNetworking.forceCancel("CommentFragment");
    }

    private void RetrieveDataFromServer(final Context context, String PostID, final AdapterMain Adapter, final RelativeLayout RelativeLayoutLoading, final LoadingView LoadingViewMain, final TextView TextViewTryAgain)
    {
        TextViewTryAgain.setVisibility(View.GONE);
        LoadingViewMain.Start();

        AndroidNetworking.post(MiscHandler.GetRandomServer("PostCommentList"))
        .addBodyParameter("PostID", PostID)
        .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
        .setTag("CommentFragment")
        .build()
        .getAsString(new StringRequestListener()
        {
            @Override
            public void onResponse(String Response)
            {
                try
                {
                    JSONObject Result = new JSONObject(Response);

                    if (Result.getInt("Message") == 1000)
                    {
                        JSONArray ResultList = new JSONArray(Result.getString("Result"));

                        for (int K = 0; K < ResultList.length(); K++)
                        {
                            JSONObject Comment = ResultList.getJSONObject(K);

                            StructComment StructComment = new StructComment();
                            StructComment.CommentID = Comment.getString("CommentID");
                            StructComment.OwnerID = Comment.getString("OwnerID");
                            StructComment.Username = Comment.getString("Username");
                            StructComment.Time = Comment.getInt("Time");
                            StructComment.Message = Comment.getString("Message");
                            StructComment.LikeCount = Comment.getInt("LikeCount");
                            StructComment.Like = Comment.getBoolean("Like");
                            StructComment.Avatar = Comment.getString("Avatar");

                            CommentList.add(StructComment);
                        }

                        Adapter.notifyDataSetChanged();
                    }
                }
                catch (Exception e)
                {
                    MiscHandler.Debug("CommentFragment-RequestSent: " + e.toString());
                    RecyclerViewScrollMain.ResetLoading(false);
                }

                InputMethodManager IMM = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                IMM.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);

                LoadingViewMain.Stop();
                RelativeLayoutLoading.setVisibility(View.GONE);
            }

            @Override
            public void onError(ANError error)
            {
                LoadingViewMain.Stop();
                TextViewTryAgain.setVisibility(View.VISIBLE);
                RecyclerViewScrollMain.ResetLoading(false);

                MiscHandler.Toast(context, getString(R.string.NoInternet));
            }
        });
    }

    private class AdapterMain extends RecyclerView.Adapter<AdapterMain.ViewHolderComment>
    {
        private final int ID_Profile = MiscHandler.GenerateViewID();
        private final int ID_Username = MiscHandler.GenerateViewID();
        private final int ID_Time = MiscHandler.GenerateViewID();
        private final int ID_Message = MiscHandler.GenerateViewID();
        private final int ID_Like = MiscHandler.GenerateViewID();
        private final int ID_ShortCut = MiscHandler.GenerateViewID();
        private final int ID_Remove = MiscHandler.GenerateViewID();
        private final int ID_LikeText = MiscHandler.GenerateViewID();
        private final int ID_LikeCount = MiscHandler.GenerateViewID();
        private final int ID_Line = MiscHandler.GenerateViewID();

        private final String PostID;
        private final boolean IsOwner;
        private final Context context;

        AdapterMain(Context c, boolean isOwner, String postID)
        {
            PostID = postID;
            IsOwner = isOwner;
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
            TextView TextViewLike;
            TextView TextViewLikeCount;
            View ViewLine;

            ViewHolderComment(View view, boolean Content)
            {
                super(view);

                if (Content)
                {
                    ImageViewProfile = (ImageViewCircle) view.findViewById(ID_Profile);
                    TextViewUsername = (TextView) view.findViewById(ID_Username);
                    TextViewTime = (TextView) view.findViewById(ID_Time);
                    TextViewMessage = (TextView) view.findViewById(ID_Message);
                    ImageViewLike = (ImageView) view.findViewById(ID_Like);
                    ImageViewShortcut = (ImageView) view.findViewById(ID_ShortCut);
                    ImageViewRemove = (ImageView) view.findViewById(ID_Remove);
                    TextViewLike = (TextView) view.findViewById(ID_LikeText);
                    TextViewLikeCount = (TextView) view.findViewById(ID_LikeCount);
                    ViewLine = view.findViewById(ID_Line);
                }
            }
        }

        @Override
        public void onBindViewHolder(final ViewHolderComment Holder, int position)
        {
            if (getItemViewType(position) == 1)
                return;

            final int Position = Holder.getAdapterPosition();

            Glide.with(context)
            .load(CommentList.get(Position).Avatar)
            .placeholder(R.color.BlueGray)
            .override(MiscHandler.ToDimension(context, 40), MiscHandler.ToDimension(context, 40))
            .dontAnimate()
            .into(Holder.ImageViewProfile);

            Holder.ImageViewProfile.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("Username", CommentList.get(Position).Username);

                    Fragment fragment = new ProfileFragment();
                    fragment.setArguments(bundle);

                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.ActivityMainFullContainer, fragment).addToBackStack("FragmentProfile").commit();
                }
            });

            String Username = CommentList.get(Position).Username;

            if (Username.length() > 25)
                Username = Username.substring(0, 30) + "...";

            Holder.TextViewUsername.setText(Username);
            Holder.TextViewUsername.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("Username", CommentList.get(Position).Username);

                    Fragment fragment = new ProfileFragment();
                    fragment.setArguments(bundle);

                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.ActivityMainFullContainer, fragment).addToBackStack("FragmentProfile").commit();
                }
            });

            Holder.TextViewTime.setText(MiscHandler.GetTimeName(CommentList.get(Position).Time));
            Holder.TextViewMessage.setText(CommentList.get(Position).Message);

            new TagHandler(Holder.TextViewMessage, getActivity());

            if (CommentList.get(Position).Like)
                Holder.ImageViewLike.setImageResource(R.drawable.ic_like_red);
            else
                Holder.ImageViewLike.setImageResource(R.drawable.ic_like);

            if (CommentList.get(Position).LikeCount == 0)
            {
                Holder.TextViewLike.setVisibility(View.GONE);
                Holder.TextViewLikeCount.setVisibility(View.GONE);
            }
            else
            {
                Holder.TextViewLike.setVisibility(View.VISIBLE);
                Holder.TextViewLikeCount.setVisibility(View.VISIBLE);
            }

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

                        CommentList.get(Position).LikeDecrease();
                        CommentList.get(Position).LikeReverse();

                        Holder.TextViewLikeCount.setText(String.valueOf(CommentList.get(Position).LikeCount));

                        if (CommentList.get(Position).LikeCount == 0)
                        {
                            Holder.TextViewLike.setVisibility(View.GONE);
                            Holder.TextViewLikeCount.setVisibility(View.GONE);
                        }
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

                        CommentList.get(Position).LikeIncrease();
                        CommentList.get(Position).LikeReverse();

                        Holder.TextViewLikeCount.setText(String.valueOf(CommentList.get(Position).LikeCount));

                        if (CommentList.get(Position).LikeCount != 0)
                        {
                            Holder.TextViewLike.setVisibility(View.VISIBLE);
                            Holder.TextViewLikeCount.setVisibility(View.VISIBLE);
                        }
                    }

                    AndroidNetworking.post(MiscHandler.GetRandomServer("PostCommentLike"))
                    .addBodyParameter("CommentID", CommentList.get(Position).CommentID)
                    .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
                    .setTag("CommentFragment")
                    .build()
                    .getAsString(new StringRequestListener()
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
                                MiscHandler.Debug("CommentFragment-RequestLike: " + e.toString());
                            }
                        }

                        @Override
                        public void onError(ANError anError) { }
                    });
                }
            });

            Holder.ImageViewRemove.setVisibility(View.GONE);

            if (CommentList.get(Position).OwnerID.equals(SharedHandler.GetString(context, "ID")) || IsOwner)
            {
                Holder.ImageViewRemove.setVisibility(View.VISIBLE);
                Holder.ImageViewRemove.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        LinearLayout LinearLayoutMain = new LinearLayout(context);
                        LinearLayoutMain.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                        LinearLayoutMain.setOrientation(LinearLayout.VERTICAL);

                        TextView TextViewTitle = new TextView(context);
                        TextViewTitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        TextViewTitle.setTextColor(ContextCompat.getColor(context, R.color.Black2));
                        TextViewTitle.setText(getString(R.string.CommentFragmentAreYouSure));
                        TextViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                        TextViewTitle.setPadding(MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10));

                        LinearLayoutMain.addView(TextViewTitle);

                        View ViewLine = new View(context);
                        ViewLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1)));
                        ViewLine.setBackgroundResource(R.color.Gray2);

                        LinearLayoutMain.addView(ViewLine);

                        LinearLayout LinearLayoutChoice = new LinearLayout(context);
                        LinearLayoutChoice.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        LinearLayoutChoice.setOrientation(LinearLayout.HORIZONTAL);

                        LinearLayoutMain.addView(LinearLayoutChoice);

                        TextView TextViewYes = new TextView(context);
                        TextViewYes.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
                        TextViewYes.setTextColor(ContextCompat.getColor(context, R.color.Black4));
                        TextViewYes.setText(getString(R.string.CommentFragmentYes));
                        TextViewYes.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                        TextViewYes.setPadding(MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10));
                        TextViewYes.setGravity(Gravity.CENTER);

                        LinearLayoutChoice.addView(TextViewYes);

                        TextView TextViewNo = new TextView(context);
                        TextViewNo.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
                        TextViewNo.setTextColor(ContextCompat.getColor(context, R.color.Black4));
                        TextViewNo.setText(getString(R.string.CommentFragmentNo));
                        TextViewNo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                        TextViewNo.setPadding(MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10));
                        TextViewNo.setGravity(Gravity.CENTER);

                        LinearLayoutChoice.addView(TextViewNo);

                        final Dialog DialogDelete = new Dialog(getActivity());
                        DialogDelete.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        DialogDelete.setContentView(LinearLayoutMain);
                        DialogDelete.show();

                        TextViewYes.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View view)
                            {
                                AndroidNetworking.post(MiscHandler.GetRandomServer("PostCommentDelete"))
                                .addBodyParameter("PostID", PostID)
                                .addBodyParameter("CommentID", CommentList.get(Position).CommentID)
                                .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
                                .setTag("CommentFragment")
                                .build()
                                .getAsString(new StringRequestListener()
                                {
                                    @Override
                                    public void onResponse(String Response)
                                    {
                                        try
                                        {
                                            if (new JSONObject(Response).getInt("Message") == 1000)
                                            {
                                                CommentList.remove(Position);
                                                notifyDataSetChanged();
                                            }
                                        }
                                        catch (Exception e)
                                        {
                                            MiscHandler.Debug("CommentFragment-RequestDelete: " + e.toString());
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
                Holder.ViewLine.setVisibility(View.GONE);
            else
                Holder.ViewLine.setVisibility(View.VISIBLE);
        }

        @Override
        public ViewHolderComment onCreateViewHolder(ViewGroup parent, int ViewType)
        {
            if (ViewType == 0)
            {
                RelativeLayout RelativeLayoutMain = new RelativeLayout(context);
                RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

                RelativeLayout.LayoutParams ImageViewCircleProfileParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 40), MiscHandler.ToDimension(context, 40));
                ImageViewCircleProfileParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));

                ImageViewCircle ImageViewCircleProfile = new ImageViewCircle(context);
                ImageViewCircleProfile.setLayoutParams(ImageViewCircleProfileParam);
                ImageViewCircleProfile.setId(ID_Profile);

                RelativeLayoutMain.addView(ImageViewCircleProfile);

                RelativeLayout.LayoutParams TextViewUsernameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                TextViewUsernameParam.setMargins(0, MiscHandler.ToDimension(context, 15), 0, 0);
                TextViewUsernameParam.addRule(RelativeLayout.RIGHT_OF, ImageViewCircleProfile.getId());

                TextView TextViewUsername = new TextView(context);
                TextViewUsername.setLayoutParams(TextViewUsernameParam);
                TextViewUsername.setTextColor(ContextCompat.getColor(context, R.color.Black));
                TextViewUsername.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                TextViewUsername.setId(ID_Username);

                RelativeLayoutMain.addView(TextViewUsername);

                RelativeLayout.LayoutParams TextViewTimeParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                TextViewTimeParam.setMargins(MiscHandler.ToDimension(context, 10), 0, MiscHandler.ToDimension(context, 10), 0);
                TextViewTimeParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                TextViewTimeParam.addRule(RelativeLayout.ALIGN_BOTTOM, TextViewUsername.getId());

                TextView TextViewTime = new TextView(context);
                TextViewTime.setLayoutParams(TextViewTimeParam);
                TextViewTime.setTextColor(ContextCompat.getColor(context, R.color.BlueGray2));
                TextViewTime.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                TextViewTime.setId(ID_Time);

                RelativeLayoutMain.addView(TextViewTime);

                RelativeLayout.LayoutParams TextViewMessageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                TextViewMessageParam.setMargins(0, MiscHandler.ToDimension(context, 1), MiscHandler.ToDimension(context, 10), 0);
                TextViewMessageParam.addRule(RelativeLayout.RIGHT_OF, ImageViewCircleProfile.getId());
                TextViewMessageParam.addRule(RelativeLayout.BELOW, TextViewUsername.getId());

                TextView TextViewMessage = new TextView(context);
                TextViewMessage.setLayoutParams(TextViewMessageParam);
                TextViewMessage.setTextColor(ContextCompat.getColor(context, R.color.Black3));
                TextViewMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                TextViewMessage.setId(ID_Message);

                RelativeLayoutMain.addView(TextViewMessage);

                RelativeLayout.LayoutParams RelativeLayoutToolParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                RelativeLayoutToolParam.addRule(RelativeLayout.RIGHT_OF, ImageViewCircleProfile.getId());
                RelativeLayoutToolParam.addRule(RelativeLayout.BELOW, TextViewMessage.getId());

                RelativeLayout RelativeLayoutTool = new RelativeLayout(context);
                RelativeLayoutTool.setLayoutParams(RelativeLayoutToolParam);
                RelativeLayoutTool.setId(MiscHandler.GenerateViewID());

                RelativeLayoutMain.addView(RelativeLayoutTool);

                ImageView ImageViewLike = new ImageView(context);
                ImageViewLike.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 30), MiscHandler.ToDimension(context, 30)));
                ImageViewLike.setPadding(MiscHandler.ToDimension(context, 6), MiscHandler.ToDimension(context, 6), MiscHandler.ToDimension(context, 6), MiscHandler.ToDimension(context, 6));
                ImageViewLike.setScaleType(ImageView.ScaleType.FIT_CENTER);
                ImageViewLike.setImageResource(R.drawable.ic_like);
                ImageViewLike.setId(ID_Like);

                RelativeLayoutTool.addView(ImageViewLike);

                RelativeLayout.LayoutParams ImageViewShortcutParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 30), MiscHandler.ToDimension(context, 30));
                ImageViewShortcutParam.addRule(RelativeLayout.RIGHT_OF, ImageViewLike.getId());

                ImageView ImageViewShortcut = new ImageView(context);
                ImageViewShortcut.setLayoutParams(ImageViewShortcutParam);
                ImageViewShortcut.setPadding(MiscHandler.ToDimension(context, 4), MiscHandler.ToDimension(context, 4), MiscHandler.ToDimension(context, 4), MiscHandler.ToDimension(context, 4));
                ImageViewShortcut.setScaleType(ImageView.ScaleType.FIT_CENTER);
                ImageViewShortcut.setImageResource(R.drawable.ic_share);
                ImageViewShortcut.setId(ID_ShortCut);

                RelativeLayoutTool.addView(ImageViewShortcut);

                RelativeLayout.LayoutParams ImageViewRemoveParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 30), MiscHandler.ToDimension(context, 30));
                ImageViewRemoveParam.addRule(RelativeLayout.RIGHT_OF, ImageViewShortcut.getId());

                ImageView ImageViewRemove = new ImageView(context);
                ImageViewRemove.setLayoutParams(ImageViewRemoveParam);
                ImageViewRemove.setPadding(MiscHandler.ToDimension(context, 3), MiscHandler.ToDimension(context, 3), MiscHandler.ToDimension(context, 3), MiscHandler.ToDimension(context, 3));
                ImageViewRemove.setScaleType(ImageView.ScaleType.FIT_CENTER);
                ImageViewRemove.setImageResource(R.drawable.ic_remove_gray);
                ImageViewRemove.setId(ID_Remove);
                ImageViewRemove.setVisibility(View.GONE);

                RelativeLayoutTool.addView(ImageViewRemove);

                RelativeLayout.LayoutParams TextViewLikeParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                TextViewLikeParam.setMargins(MiscHandler.ToDimension(context, 10), 0, MiscHandler.ToDimension(context, 10), 0);
                TextViewLikeParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                TextViewLikeParam.addRule(RelativeLayout.CENTER_VERTICAL);

                TextView TextViewLike = new TextView(context);
                TextViewLike.setLayoutParams(TextViewLikeParam);
                TextViewLike.setTextColor(ContextCompat.getColor(context, R.color.BlueGray2));
                TextViewLike.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                TextViewLike.setText(getString(R.string.CommentFragmentLike));
                TextViewLike.setTypeface(null, Typeface.BOLD);
                TextViewLike.setId(ID_LikeText);

                RelativeLayoutTool.addView(TextViewLike);

                RelativeLayout.LayoutParams TextViewLikeCountParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                TextViewLikeCountParam.addRule(RelativeLayout.LEFT_OF, TextViewLike.getId());
                TextViewLikeCountParam.addRule(RelativeLayout.CENTER_VERTICAL);

                TextView TextViewLikeCount = new TextView(context);
                TextViewLikeCount.setLayoutParams(TextViewLikeCountParam);
                TextViewLikeCount.setTextColor(ContextCompat.getColor(context, R.color.BlueGray2));
                TextViewLikeCount.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                TextViewLikeCount.setTypeface(null, Typeface.BOLD);
                TextViewLikeCount.setId(ID_LikeCount);

                RelativeLayoutTool.addView(TextViewLikeCount);

                RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1));
                ViewLineParam.addRule(RelativeLayout.BELOW, RelativeLayoutTool.getId());

                View ViewLine = new View(context);
                ViewLine.setLayoutParams(ViewLineParam);
                ViewLine.setBackgroundResource(R.color.Gray);
                ViewLine.setId(ID_Line);

                RelativeLayoutMain.addView(ViewLine);

                return new ViewHolderComment(RelativeLayoutMain, true);
            }

            LoadingView LoadingViewMain = new LoadingView(context);
            LoadingViewMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56)));
            LoadingViewMain.Start();

            return new ViewHolderComment(LoadingViewMain, false);
        }

        @Override
        public int getItemCount()
        {
            return CommentList.size();
        }

        @Override
        public int getItemViewType(int position)
        {
            return CommentList.get(position) != null ? 0 : 1;
        }
    }

    private class StructComment
    {
        String CommentID;
        String OwnerID;
        String Username;
        long Time;
        String Message;
        int LikeCount;
        boolean Like;
        String Avatar;

        void LikeIncrease() { LikeCount = LikeCount + 1; }
        void LikeDecrease() { LikeCount = LikeCount - 1; }
        void LikeReverse() { Like = !Like; }
    }
}
