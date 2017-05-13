package co.biogram.main.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.text.util.Linkify;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONObject;

import co.biogram.main.R;
import co.biogram.main.activity.ActivityProfileEdit;
import co.biogram.main.handler.MiscHandler;
import co.biogram.main.handler.RequestHandler;
import co.biogram.main.handler.SharedHandler;
import co.biogram.main.handler.URLHandler;
import co.biogram.main.misc.ImageViewCircle;
import co.biogram.main.misc.LoadingView;

public class FragmentProfile extends Fragment
{
    private RelativeLayout RelativeLayoutLoading;
    private LoadingView LoadingViewData;
    private TextView TextViewTry;

    private ImageView ImageViewCover;
    private ImageViewCircle ImageViewCircleProfile;

    private TextView TextViewUsername;
    private TextView TextViewDescription;
    private TextView TextViewUrl;
    private TextView TextViewPostCount;
    private TextView TextViewFollowerCount;
    private TextView TextViewFollowingCount;

    private TextView TextViewTabPost;
    private View ViewTabPost;
    private TextView TextViewTabComment;
    private View ViewTabComment;
    private TextView TextViewTabLike;
    private View ViewTabLike;

    private final int FrameLayoutID = MiscHandler.GenerateViewID();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final Context context = getActivity();

        RelativeLayout Root = new RelativeLayout(context);
        Root.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        Root.setBackgroundResource(R.color.White);
        Root.setClickable(true);
        Root.setFocusableInTouchMode(true);

        ScrollView ScrollLayout = new ScrollView(context);
        ScrollLayout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        ScrollLayout.setVerticalScrollBarEnabled(false);
        ScrollLayout.setHorizontalScrollBarEnabled(false);

        Root.addView(ScrollLayout);

        RelativeLayout RelativeLayoutMain = new RelativeLayout(context);
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

        ScrollLayout.addView(RelativeLayoutMain);

        ImageViewCover = new ImageView(context);
        ImageViewCover.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 160)));
        ImageViewCover.setScaleType(ImageView.ScaleType.FIT_XY);
        ImageViewCover.setId(MiscHandler.GenerateViewID());
        ImageViewCover.setImageResource(R.color.BlueLight);

        RelativeLayoutMain.addView(ImageViewCover);

        RelativeLayout.LayoutParams ImageViewCircleProfileParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 90), MiscHandler.ToDimension(context, 90));
        ImageViewCircleProfileParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 120), MiscHandler.ToDimension(context, 15), 0);

        ImageViewCircleProfile = new ImageViewCircle(context);
        ImageViewCircleProfile.SetBorderWidth(MiscHandler.ToDimension(context, 3));
        ImageViewCircleProfile.setLayoutParams(ImageViewCircleProfileParam);
        ImageViewCircleProfile.setImageResource(R.color.BlueGray);

        RelativeLayoutMain.addView(ImageViewCircleProfile);

        GradientDrawable ShapeEdit = new GradientDrawable();
        ShapeEdit.setShape(GradientDrawable.OVAL);
        ShapeEdit.setColor(Color.WHITE);
        ShapeEdit.setStroke(MiscHandler.ToDimension(context, 2), Color.parseColor("#1f000000"));

        RelativeLayout.LayoutParams ImageButtonEditParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 50), MiscHandler.ToDimension(context, 50));
        ImageButtonEditParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 135), MiscHandler.ToDimension(context, 15), 0);
        ImageButtonEditParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        ImageButton ImageButtonEdit = new ImageButton(context);
        ImageButtonEdit.setLayoutParams(ImageButtonEditParam);
        ImageButtonEdit.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageButtonEdit.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
        ImageButtonEdit.setBackground(ShapeEdit);
        ImageButtonEdit.setImageResource(R.drawable.ic_setting_black);
        ImageButtonEdit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getActivity().startActivity(new Intent(context, ActivityProfileEdit.class));
                getActivity().finish();
            }
        });

        if (getArguments() != null && !getArguments().getString("ID", "").equals(""))
            if (!getArguments().getString("ID", "").equals(SharedHandler.GetString(context, "ID")))
                ImageButtonEdit.setVisibility(View.GONE);

        RelativeLayoutMain.addView(ImageButtonEdit);

        RelativeLayout.LayoutParams LinearLayoutMain2Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        LinearLayoutMain2Param.addRule(RelativeLayout.BELOW, ImageViewCover.getId());

        LinearLayout LinearLayoutMain2 = new LinearLayout(context);
        LinearLayoutMain2.setLayoutParams(LinearLayoutMain2Param);
        LinearLayoutMain2.setOrientation(LinearLayout.VERTICAL);

        RelativeLayoutMain.addView(LinearLayoutMain2);

        View ViewBlankLine = new View(context);
        ViewBlankLine.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 5)));
        ViewBlankLine.setBackgroundResource(R.color.Gray);

        LinearLayoutMain2.addView(ViewBlankLine);
        ImageViewCircleProfile.bringToFront();
        ImageButtonEdit.bringToFront();

        TextViewUsername = new TextView(context);
        TextViewUsername.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewUsername.setTextColor(ContextCompat.getColor(context, R.color.Black));
        TextViewUsername.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 45), MiscHandler.ToDimension(context, 15), 0);
        TextViewUsername.setTypeface(null, Typeface.BOLD);
        TextViewUsername.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

        LinearLayoutMain2.addView(TextViewUsername);

        TextViewDescription = new TextView(context);
        TextViewDescription.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewDescription.setTextColor(ContextCompat.getColor(context, R.color.Black));
        TextViewDescription.setPadding(MiscHandler.ToDimension(context, 15), 0, MiscHandler.ToDimension(context, 15), 0);
        TextViewDescription.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        TextViewDescription.setVisibility(View.GONE);

        LinearLayoutMain2.addView(TextViewDescription);

        TextViewUrl = new TextView(getActivity());
        TextViewUrl.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewUrl.setTextColor(ContextCompat.getColor(context, R.color.BlueLight));
        TextViewUrl.setLinkTextColor(ContextCompat.getColor(context, R.color.BlueLight));
        TextViewUrl.setPadding(MiscHandler.ToDimension(context, 15), 0, MiscHandler.ToDimension(context, 15), 0);
        TextViewUrl.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        TextViewUrl.setVisibility(View.GONE);
        Linkify.addLinks(TextViewUrl, Linkify.ALL);

        LinearLayoutMain2.addView(TextViewUrl);

        LinearLayout LinearLayoutDetails = new LinearLayout(context);
        LinearLayoutDetails.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        LinearLayoutDetails.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayoutDetails.setPadding(0, MiscHandler.ToDimension(context, 15), 0, MiscHandler.ToDimension(context, 5));

        LinearLayoutMain2.addView(LinearLayoutDetails);

        RelativeLayout RelativeLayoutPost = new RelativeLayout(context);
        RelativeLayoutPost.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.1f));

        LinearLayoutDetails.addView(RelativeLayoutPost);

        RelativeLayout.LayoutParams TextViewPostCountParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewPostCountParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        TextViewPostCountParam.addRule(RelativeLayout.CENTER_HORIZONTAL);

        TextViewPostCount = new TextView(context);
        TextViewPostCount.setLayoutParams(TextViewPostCountParam);
        TextViewPostCount.setTextColor(ContextCompat.getColor(context, R.color.Black));
        TextViewPostCount.setTypeface(null, Typeface.BOLD);
        TextViewPostCount.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        TextViewPostCount.setId(MiscHandler.GenerateViewID());

        RelativeLayoutPost.addView(TextViewPostCount);

        RelativeLayout.LayoutParams TextViewPostParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewPostParam.addRule(RelativeLayout.BELOW, TextViewPostCount.getId());
        TextViewPostParam.addRule(RelativeLayout.CENTER_HORIZONTAL);

        TextView TextViewPost = new TextView(context);
        TextViewPost.setLayoutParams(TextViewPostParam);
        TextViewPost.setTextColor(ContextCompat.getColor(context, R.color.Gray5));
        TextViewPost.setText(getString(R.string.FragmentProfilePost));
        TextViewPost.setPadding(MiscHandler.ToDimension(context, 5), MiscHandler.ToDimension(context, 5), MiscHandler.ToDimension(context, 5), MiscHandler.ToDimension(context, 5));
        TextViewPost.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        RelativeLayoutPost.addView(TextViewPost);

        RelativeLayout RelativeLayoutFollower = new RelativeLayout(context);
        RelativeLayoutFollower.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));

        LinearLayoutDetails.addView(RelativeLayoutFollower);

        RelativeLayout.LayoutParams TextViewFollowerCountParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewFollowerCountParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        TextViewFollowerCountParam.addRule(RelativeLayout.CENTER_HORIZONTAL);

        TextViewFollowerCount = new TextView(context);
        TextViewFollowerCount.setLayoutParams(TextViewFollowerCountParam);
        TextViewFollowerCount.setTextColor(ContextCompat.getColor(context, R.color.Black));
        TextViewFollowerCount.setTypeface(null, Typeface.BOLD);
        TextViewFollowerCount.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        TextViewFollowerCount.setId(MiscHandler.GenerateViewID());

        RelativeLayoutFollower.addView(TextViewFollowerCount);

        RelativeLayout.LayoutParams TextViewFollowerParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewFollowerParam.addRule(RelativeLayout.BELOW, TextViewFollowerCount.getId());
        TextViewFollowerParam.addRule(RelativeLayout.CENTER_HORIZONTAL);

        TextView TextViewFollower = new TextView(context);
        TextViewFollower.setLayoutParams(TextViewFollowerParam);
        TextViewFollower.setTextColor(ContextCompat.getColor(context, R.color.Gray5));
        TextViewFollower.setText(getString(R.string.FragmentProfileFollowers));
        TextViewFollower.setPadding(MiscHandler.ToDimension(context, 5), MiscHandler.ToDimension(context, 5), MiscHandler.ToDimension(context, 5), MiscHandler.ToDimension(context, 5));
        TextViewFollower.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        RelativeLayoutFollower.addView(TextViewFollower);

        RelativeLayout RelativeLayoutFollowing = new RelativeLayout(context);
        RelativeLayoutFollowing.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));

        LinearLayoutDetails.addView(RelativeLayoutFollowing);

        RelativeLayout.LayoutParams TextViewFollowingCountParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewFollowingCountParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        TextViewFollowingCountParam.addRule(RelativeLayout.CENTER_HORIZONTAL);

        TextViewFollowingCount = new TextView(context);
        TextViewFollowingCount.setLayoutParams(TextViewFollowingCountParam);
        TextViewFollowingCount.setTextColor(ContextCompat.getColor(context, R.color.Black));
        TextViewFollowingCount.setTypeface(null, Typeface.BOLD);
        TextViewFollowingCount.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        TextViewFollowingCount.setId(MiscHandler.GenerateViewID());

        RelativeLayoutFollowing.addView(TextViewFollowingCount);

        RelativeLayout.LayoutParams TextViewFollowingParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewFollowingParam.addRule(RelativeLayout.BELOW, TextViewFollowingCount.getId());
        TextViewFollowingParam.addRule(RelativeLayout.CENTER_HORIZONTAL);

        TextView TextViewFollowing = new TextView(context);
        TextViewFollowing.setLayoutParams(TextViewFollowingParam);
        TextViewFollowing.setTextColor(ContextCompat.getColor(context, R.color.Gray5));
        TextViewFollowing.setText(getString(R.string.FragmentProfileFollowing));
        TextViewFollowing.setPadding(MiscHandler.ToDimension(context, 5), MiscHandler.ToDimension(context, 5), MiscHandler.ToDimension(context, 5), MiscHandler.ToDimension(context, 5));
        TextViewFollowing.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        RelativeLayoutFollowing.addView(TextViewFollowing);

        View ViewBlankLine2 = new View(context);
        ViewBlankLine2.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 5)));
        ViewBlankLine2.setBackgroundResource(R.color.Gray);

        LinearLayoutMain2.addView(ViewBlankLine2);

        LinearLayout LinearLayoutTab = new LinearLayout(context);
        LinearLayoutTab.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56)));
        LinearLayoutTab.setBackgroundResource(R.color.White);

        LinearLayoutMain2.addView(LinearLayoutTab);

        RelativeLayout RelativeLayoutTabPost = new RelativeLayout(context);
        RelativeLayoutTabPost.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
        RelativeLayoutTabPost.setBackgroundResource(R.color.White);
        RelativeLayoutTabPost.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { ChangeTab(1); } });

        LinearLayoutTab.addView(RelativeLayoutTabPost);

        RelativeLayout.LayoutParams TextViewTabPostParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTabPostParam.addRule(RelativeLayout.CENTER_HORIZONTAL);

        TextViewTabPost = new TextView(context);
        TextViewTabPost.setLayoutParams(TextViewTabPostParam);
        TextViewTabPost.setTextColor(ContextCompat.getColor(context, R.color.BlueLight));
        TextViewTabPost.setText(getString(R.string.FragmentProfilePost2));
        TextViewTabPost.setPadding(0, MiscHandler.ToDimension(context, 15), 0, MiscHandler.ToDimension(context, 15));
        TextViewTabPost.setId(MiscHandler.GenerateViewID());
        TextViewTabPost.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

        RelativeLayoutTabPost.addView(TextViewTabPost);

        RelativeLayout.LayoutParams ViewTabPostParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, MiscHandler.ToDimension(context, 2));
        ViewTabPostParam.addRule(RelativeLayout.BELOW, TextViewTabPost.getId());

        ViewTabPost = new View(context);
        ViewTabPost.setLayoutParams(ViewTabPostParam);
        ViewTabPost.setBackgroundResource(R.color.BlueLight);

        RelativeLayoutTabPost.addView(ViewTabPost);

        RelativeLayout RelativeLayoutTabComment = new RelativeLayout(context);
        RelativeLayoutTabComment.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
        RelativeLayoutTabComment.setBackgroundResource(R.color.White);
        RelativeLayoutTabComment.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { ChangeTab(2); } });

        LinearLayoutTab.addView(RelativeLayoutTabComment);

        RelativeLayout.LayoutParams TextViewTabCommentParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTabCommentParam.addRule(RelativeLayout.CENTER_HORIZONTAL);

        TextViewTabComment = new TextView(context);
        TextViewTabComment.setLayoutParams(TextViewTabCommentParam);
        TextViewTabComment.setTextColor(ContextCompat.getColor(context, R.color.BlueLight));
        TextViewTabComment.setText(getString(R.string.FragmentProfileComment));
        TextViewTabComment.setPadding(0, MiscHandler.ToDimension(context, 15), 0, MiscHandler.ToDimension(context, 15));
        TextViewTabComment.setId(MiscHandler.GenerateViewID());
        TextViewTabComment.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

        RelativeLayoutTabComment.addView(TextViewTabComment);

        RelativeLayout.LayoutParams ViewTabCommentParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, MiscHandler.ToDimension(context, 2));
        ViewTabCommentParam.addRule(RelativeLayout.BELOW, TextViewTabComment.getId());

        ViewTabComment = new View(context);
        ViewTabComment.setLayoutParams(ViewTabCommentParam);
        ViewTabComment.setBackgroundResource(R.color.BlueLight);

        RelativeLayoutTabComment.addView(ViewTabComment);

        RelativeLayout RelativeLayoutTabLike = new RelativeLayout(context);
        RelativeLayoutTabLike.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
        RelativeLayoutTabLike.setBackgroundResource(R.color.White);
        RelativeLayoutTabLike.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { ChangeTab(3); } });

        LinearLayoutTab.addView(RelativeLayoutTabLike);

        RelativeLayout.LayoutParams TextViewTabLikeParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTabLikeParam.addRule(RelativeLayout.CENTER_HORIZONTAL);

        TextViewTabLike = new TextView(context);
        TextViewTabLike.setLayoutParams(TextViewTabLikeParam);
        TextViewTabLike.setTextColor(ContextCompat.getColor(context, R.color.BlueLight));
        TextViewTabLike.setText(getString(R.string.FragmentProfileLike));
        TextViewTabLike.setPadding(0, MiscHandler.ToDimension(context, 15), 0, MiscHandler.ToDimension(context, 15));
        TextViewTabLike.setId(MiscHandler.GenerateViewID());
        TextViewTabLike.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

        RelativeLayoutTabLike.addView(TextViewTabLike);

        RelativeLayout.LayoutParams ViewTabLikeParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, MiscHandler.ToDimension(context, 2));
        ViewTabLikeParam.addRule(RelativeLayout.BELOW, TextViewTabLike.getId());

        ViewTabLike = new View(context);
        ViewTabLike.setLayoutParams(ViewTabLikeParam);
        ViewTabLike.setBackgroundResource(R.color.BlueLight);

        RelativeLayoutTabLike.addView(ViewTabLike);

        FrameLayout FrameLayoutTab = new FrameLayout(context);
        FrameLayoutTab.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        FrameLayoutTab.setId(FrameLayoutID);

        LinearLayoutMain2.addView(FrameLayoutTab);

        RelativeLayoutLoading = new RelativeLayout(context);
        RelativeLayoutLoading.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayoutLoading.setBackgroundResource(R.color.White);
        RelativeLayoutLoading.setClickable(true);

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
        TextViewTry.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { RetrieveDataFromServer(); } });

        RelativeLayoutLoading.addView(TextViewTry);

        RetrieveDataFromServer();

        return Root;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        RequestHandler.Core().Cancel("FragmentProfile");
    }

    private void ChangeTab(int Tab)
    {
        Context context = getActivity();

        TextViewTabPost.setTextColor(ContextCompat.getColor(context, R.color.Gray7));
        ViewTabPost.setBackgroundResource(R.color.White);
        TextViewTabComment.setTextColor(ContextCompat.getColor(context, R.color.Gray7));
        ViewTabComment.setBackgroundResource(R.color.White);
        TextViewTabLike.setTextColor(ContextCompat.getColor(context, R.color.Gray7));
        ViewTabLike.setBackgroundResource(R.color.White);

        Fragment SelectedFragment = new FragmentProfilePost();

        switch (Tab)
        {
            case 1:
                TextViewTabPost.setTextColor(ContextCompat.getColor(context, R.color.BlueLight));
                ViewTabPost.setBackgroundResource(R.color.BlueLight);
            break;
            case 2:
                SelectedFragment = new FragmentProfileComment();
                TextViewTabComment.setTextColor(ContextCompat.getColor(context, R.color.BlueLight));
                ViewTabComment.setBackgroundResource(R.color.BlueLight);
            break;
            case 3:
                SelectedFragment = new FragmentProfileLike();
                TextViewTabLike.setTextColor(ContextCompat.getColor(context, R.color.BlueLight));
                ViewTabLike.setBackgroundResource(R.color.BlueLight);
            break;
        }

        FragmentManager FragManager = getChildFragmentManager();
        Fragment FoundFragment = FragManager.findFragmentByTag(SelectedFragment.getClass().getSimpleName());

        if (FoundFragment != null)
        {
            for (Fragment Frag : FragManager.getFragments())
            {
                if (Frag != null && Frag != FoundFragment)
                {
                    FragManager.beginTransaction().hide(Frag).commit();
                }
            }

            FragManager.beginTransaction().show(FoundFragment).commit();
            return;
        }

        FragManager.beginTransaction().add(FrameLayoutID, SelectedFragment, SelectedFragment.getClass().getSimpleName()).commit();
    }

    private void RetrieveDataFromServer()
    {
        final Context context = getActivity();

        TextViewTry.setVisibility(View.GONE);
        LoadingViewData.Start();

        String ID = SharedHandler.GetString(context, "ID");

        if (getArguments() != null && !getArguments().getString("ID", "").equals(""))
            ID = getArguments().getString("ID");

        RequestHandler.Core().Method("POST")
        .Address(URLHandler.GetURL(URLHandler.URL.PROFILE_GET))
        .Header("TOKEN",SharedHandler.GetString(context, "TOKEN"))
        .Param("ID", ID)
        .Tag("FragmentProfile")
        .Build(new RequestHandler.OnCompleteCallBack()
        {
            @Override
            public void OnFinish(String Response, int Status)
            {
                if (Status != 200)
                {
                    MiscHandler.Toast(context, getString(R.string.NoInternet));
                    TextViewTry.setVisibility(View.VISIBLE);
                    LoadingViewData.Stop();
                    return;
                }

                try
                {
                    JSONObject Result = new JSONObject(Response);

                    if (Result.getInt("Message") == 1000 && !Result.getString("Result").equals(""))
                    {
                        JSONObject Data = new JSONObject(Result.getString("Result"));

                        RequestHandler.Core().LoadImage(ImageViewCircleProfile, Data.getString("Avatar"), "FragmentProfile", MiscHandler.ToDimension(context, 90), MiscHandler.ToDimension(context, 90), true);
                        RequestHandler.Core().LoadImage(ImageViewCover, Data.getString("Cover"), "FragmentProfile", true);

                        TextViewUsername.setText(Data.getString("Username"));

                        if (!Data.getString("Description").equals(""))
                        {
                            TextViewDescription.setText(Data.getString("Description"));
                            TextViewDescription.setVisibility(View.VISIBLE);
                        }

                        if (!Data.getString("Link").equals(""))
                        {
                            TextViewUrl.setText(Data.getString("Link"));
                            TextViewUrl.setVisibility(View.VISIBLE);
                        }

                        TextViewPostCount.setText(Data.getString("Post"));
                        TextViewFollowingCount.setText(Data.getString("Following"));
                        TextViewFollowerCount.setText(Data.getString("Follower"));
                    }

                    ChangeTab(1);
                }
                catch (Exception e)
                {
                    // Leave Me Alone
                }

                RelativeLayoutLoading.setVisibility(View.GONE);
                TextViewTry.setVisibility(View.GONE);
                LoadingViewData.Stop();
            }
        });
    }
}
