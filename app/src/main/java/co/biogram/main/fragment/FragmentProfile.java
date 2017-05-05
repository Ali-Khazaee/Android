package co.biogram.main.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import co.biogram.main.App;
import co.biogram.main.R;
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
    private int FrameLayoutID = MiscHandler.GenerateViewID();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        RelativeLayout Root = new RelativeLayout(App.GetContext());
        Root.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        Root.setBackgroundResource(R.color.White);
        Root.setClickable(true);
        Root.setFocusableInTouchMode(true);

        ScrollView ScrollLayout = new ScrollView(App.GetContext());
        ScrollLayout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        ScrollLayout.setVerticalScrollBarEnabled(false);
        ScrollLayout.setHorizontalScrollBarEnabled(false);

        Root.addView(ScrollLayout);

        RelativeLayout RelativeLayoutMain = new RelativeLayout(App.GetContext());
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

        ScrollLayout.addView(RelativeLayoutMain);

        ImageViewCover = new ImageView(App.GetContext());
        ImageViewCover.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.DpToPx(160)));
        ImageViewCover.setScaleType(ImageView.ScaleType.FIT_XY);
        ImageViewCover.setId(MiscHandler.GenerateViewID());
        ImageViewCover.setImageResource(R.color.BlueLight);

        RelativeLayoutMain.addView(ImageViewCover);

        RelativeLayout.LayoutParams ImageViewCircleProfileParam = new RelativeLayout.LayoutParams(MiscHandler.DpToPx(90), MiscHandler.DpToPx(90));
        ImageViewCircleProfileParam.setMargins(MiscHandler.DpToPx(15), MiscHandler.DpToPx(120), MiscHandler.DpToPx(15), 0);

        ImageViewCircleProfile = new ImageViewCircle(App.GetContext());
        ImageViewCircleProfile.SetBorderColor(R.color.White);
        ImageViewCircleProfile.SetBorderWidth(MiscHandler.DpToPx(3));
        ImageViewCircleProfile.setLayoutParams(ImageViewCircleProfileParam);
        ImageViewCircleProfile.setImageResource(R.color.BlueGray);

        RelativeLayoutMain.addView(ImageViewCircleProfile);

        GradientDrawable ShapeEdit = new GradientDrawable();
        ShapeEdit.setShape(GradientDrawable.OVAL);
        ShapeEdit.setColor(Color.WHITE);
        ShapeEdit.setStroke(MiscHandler.DpToPx(2), Color.parseColor("#1f000000"));

        RelativeLayout.LayoutParams ImageButtonEditParam = new RelativeLayout.LayoutParams(MiscHandler.DpToPx(50), MiscHandler.DpToPx(50));
        ImageButtonEditParam.setMargins(MiscHandler.DpToPx(15), MiscHandler.DpToPx(135), MiscHandler.DpToPx(15), 0);
        ImageButtonEditParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        ImageButton ImageButtonEdit = new ImageButton(App.GetContext());
        ImageButtonEdit.setLayoutParams(ImageButtonEditParam);
        ImageButtonEdit.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageButtonEdit.setPadding(MiscHandler.DpToPx(15), MiscHandler.DpToPx(15), MiscHandler.DpToPx(15), MiscHandler.DpToPx(15));
        ImageButtonEdit.setBackground(ShapeEdit);
        ImageButtonEdit.setImageResource(R.drawable.ic_setting_black);
        ImageButtonEdit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getActivity().startActivity(new Intent(App.GetContext(), FragmentProfileEdit.class));
            }
        });

        if (getArguments() != null && !getArguments().getString("ID", "").equals(""))
            if (!getArguments().getString("ID", "").equals(SharedHandler.GetString("ID")))
                ImageButtonEdit.setVisibility(View.GONE);

        RelativeLayoutMain.addView(ImageButtonEdit);

        RelativeLayout.LayoutParams LinearLayoutMain2Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        LinearLayoutMain2Param.addRule(RelativeLayout.BELOW, ImageViewCover.getId());

        LinearLayout LinearLayoutMain2 = new LinearLayout(App.GetContext());
        LinearLayoutMain2.setLayoutParams(LinearLayoutMain2Param);
        LinearLayoutMain2.setOrientation(LinearLayout.VERTICAL);

        RelativeLayoutMain.addView(LinearLayoutMain2);

        View ViewBlankLine = new View(App.GetContext());
        ViewBlankLine.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.DpToPx(5)));
        ViewBlankLine.setBackgroundResource(R.color.Gray);

        LinearLayoutMain2.addView(ViewBlankLine);
        ImageViewCircleProfile.bringToFront();
        ImageButtonEdit.bringToFront();

        TextViewUsername = new TextView(App.GetContext());
        TextViewUsername.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewUsername.setTextColor(ContextCompat.getColor(App.GetContext(), R.color.Black));
        TextViewUsername.setPadding(MiscHandler.DpToPx(15), MiscHandler.DpToPx(45), MiscHandler.DpToPx(15), 0);
        TextViewUsername.setTypeface(null, Typeface.BOLD);
        TextViewUsername.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

        LinearLayoutMain2.addView(TextViewUsername);

        TextViewDescription = new TextView(App.GetContext());
        TextViewDescription.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewDescription.setTextColor(ContextCompat.getColor(App.GetContext(), R.color.Black));
        TextViewDescription.setPadding(MiscHandler.DpToPx(15), 0, MiscHandler.DpToPx(15), 0);
        TextViewDescription.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

        LinearLayoutMain2.addView(TextViewDescription);

        TextViewUrl = new TextView(getActivity());
        TextViewUrl.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewUrl.setTextColor(ContextCompat.getColor(App.GetContext(), R.color.BlueLight));
        TextViewUrl.setLinkTextColor(ContextCompat.getColor(App.GetContext(), R.color.BlueLight));
        TextViewUrl.setPadding(MiscHandler.DpToPx(15), 0, MiscHandler.DpToPx(15), 0);
        TextViewUrl.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        Linkify.addLinks(TextViewUrl, Linkify.ALL);

        LinearLayoutMain2.addView(TextViewUrl);

        LinearLayout LinearLayoutDetails = new LinearLayout(App.GetContext());
        LinearLayoutDetails.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        LinearLayoutDetails.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayoutDetails.setPadding(0, MiscHandler.DpToPx(15), 0, MiscHandler.DpToPx(5));

        LinearLayoutMain2.addView(LinearLayoutDetails);

        RelativeLayout RelativeLayoutPost = new RelativeLayout(App.GetContext());
        RelativeLayoutPost.setLayoutParams(new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT, 1.1f));

        LinearLayoutDetails.addView(RelativeLayoutPost);

        RelativeLayout.LayoutParams TextViewPostCountParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewPostCountParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        TextViewPostCountParam.addRule(RelativeLayout.CENTER_HORIZONTAL);

        TextViewPostCount = new TextView(App.GetContext());
        TextViewPostCount.setLayoutParams(TextViewPostCountParam);
        TextViewPostCount.setTextColor(ContextCompat.getColor(App.GetContext(), R.color.Black));
        TextViewPostCount.setTypeface(null, Typeface.BOLD);
        TextViewPostCount.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        TextViewPostCount.setId(MiscHandler.GenerateViewID());

        RelativeLayoutPost.addView(TextViewPostCount);

        RelativeLayout.LayoutParams TextViewPostParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewPostParam.addRule(RelativeLayout.BELOW, TextViewPostCount.getId());
        TextViewPostParam.addRule(RelativeLayout.CENTER_HORIZONTAL);

        TextView TextViewPost = new TextView(App.GetContext());
        TextViewPost.setLayoutParams(TextViewPostParam);
        TextViewPost.setTextColor(ContextCompat.getColor(App.GetContext(), R.color.Gray5));
        TextViewPost.setText(getString(R.string.FragmentProfilePost));
        TextViewPost.setPadding(MiscHandler.DpToPx(5), MiscHandler.DpToPx(5), MiscHandler.DpToPx(5), MiscHandler.DpToPx(5));
        TextViewPost.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        RelativeLayoutPost.addView(TextViewPost);

        RelativeLayout RelativeLayoutFollower = new RelativeLayout(App.GetContext());
        RelativeLayoutFollower.setLayoutParams(new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT, 1.0f));

        LinearLayoutDetails.addView(RelativeLayoutFollower);

        RelativeLayout.LayoutParams TextViewFollowerCountParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewFollowerCountParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        TextViewFollowerCountParam.addRule(RelativeLayout.CENTER_HORIZONTAL);

        TextViewFollowerCount = new TextView(App.GetContext());
        TextViewFollowerCount.setLayoutParams(TextViewFollowerCountParam);
        TextViewFollowerCount.setTextColor(ContextCompat.getColor(App.GetContext(), R.color.Black));
        TextViewFollowerCount.setTypeface(null, Typeface.BOLD);
        TextViewFollowerCount.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        TextViewFollowerCount.setId(MiscHandler.GenerateViewID());

        RelativeLayoutFollower.addView(TextViewFollowerCount);

        RelativeLayout.LayoutParams TextViewFollowerParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewFollowerParam.addRule(RelativeLayout.BELOW, TextViewFollowerCount.getId());
        TextViewFollowerParam.addRule(RelativeLayout.CENTER_HORIZONTAL);

        TextView TextViewFollower = new TextView(App.GetContext());
        TextViewFollower.setLayoutParams(TextViewFollowerParam);
        TextViewFollower.setTextColor(ContextCompat.getColor(App.GetContext(), R.color.Gray5));
        TextViewFollower.setText(getString(R.string.FragmentProfileFollowers));
        TextViewFollower.setPadding(MiscHandler.DpToPx(5), MiscHandler.DpToPx(5), MiscHandler.DpToPx(5), MiscHandler.DpToPx(5));
        TextViewFollower.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        RelativeLayoutFollower.addView(TextViewFollower);

        RelativeLayout RelativeLayoutFollowing = new RelativeLayout(App.GetContext());
        RelativeLayoutFollowing.setLayoutParams(new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT, 1.0f));

        LinearLayoutDetails.addView(RelativeLayoutFollowing);

        RelativeLayout.LayoutParams TextViewFollowingCountParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewFollowingCountParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        TextViewFollowingCountParam.addRule(RelativeLayout.CENTER_HORIZONTAL);

        TextViewFollowingCount = new TextView(App.GetContext());
        TextViewFollowingCount.setLayoutParams(TextViewFollowingCountParam);
        TextViewFollowingCount.setTextColor(ContextCompat.getColor(App.GetContext(), R.color.Black));
        TextViewFollowingCount.setTypeface(null, Typeface.BOLD);
        TextViewFollowingCount.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        TextViewFollowingCount.setId(MiscHandler.GenerateViewID());

        RelativeLayoutFollowing.addView(TextViewFollowingCount);

        RelativeLayout.LayoutParams TextViewFollowingParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewFollowingParam.addRule(RelativeLayout.BELOW, TextViewFollowingCount.getId());
        TextViewFollowingParam.addRule(RelativeLayout.CENTER_HORIZONTAL);

        TextView TextViewFollowing = new TextView(App.GetContext());
        TextViewFollowing.setLayoutParams(TextViewFollowingParam);
        TextViewFollowing.setTextColor(ContextCompat.getColor(App.GetContext(), R.color.Gray5));
        TextViewFollowing.setText(getString(R.string.FragmentProfileFollowing));
        TextViewFollowing.setPadding(MiscHandler.DpToPx(5), MiscHandler.DpToPx(5), MiscHandler.DpToPx(5), MiscHandler.DpToPx(5));
        TextViewFollowing.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        RelativeLayoutFollowing.addView(TextViewFollowing);

        View ViewBlankLine2 = new View(App.GetContext());
        ViewBlankLine2.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.DpToPx(5)));
        ViewBlankLine2.setBackgroundResource(R.color.Gray);

        LinearLayoutMain2.addView(ViewBlankLine2);

        LinearLayout LinearLayoutTab = new LinearLayout(App.GetContext());
        LinearLayoutTab.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.DpToPx(56)));
        LinearLayoutTab.setBackgroundResource(R.color.White);

        LinearLayoutMain2.addView(LinearLayoutTab);

        RelativeLayout RelativeLayoutTabPost = new RelativeLayout(App.GetContext());
        RelativeLayoutTabPost.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
        RelativeLayoutTabPost.setBackgroundResource(R.color.White);
        RelativeLayoutTabPost.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { ChangeTab(1); } });

        LinearLayoutTab.addView(RelativeLayoutTabPost);

        RelativeLayout.LayoutParams TextViewTabPostParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTabPostParam.addRule(RelativeLayout.CENTER_HORIZONTAL);

        TextViewTabPost = new TextView(App.GetContext());
        TextViewTabPost.setLayoutParams(TextViewTabPostParam);
        TextViewTabPost.setTextColor(ContextCompat.getColor(App.GetContext(), R.color.BlueLight));
        TextViewTabPost.setText("POST");
        TextViewTabPost.setPadding(0, MiscHandler.DpToPx(15), 0, MiscHandler.DpToPx(15));
        TextViewTabPost.setId(MiscHandler.GenerateViewID());
        TextViewTabPost.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

        RelativeLayoutTabPost.addView(TextViewTabPost);

        RelativeLayout.LayoutParams ViewTabPostParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, MiscHandler.DpToPx(2));
        ViewTabPostParam.addRule(RelativeLayout.BELOW, TextViewTabPost.getId());

        ViewTabPost = new View(App.GetContext());
        ViewTabPost.setLayoutParams(ViewTabPostParam);
        ViewTabPost.setBackgroundResource(R.color.BlueLight);

        RelativeLayoutTabPost.addView(ViewTabPost);

        RelativeLayout RelativeLayoutTabComment = new RelativeLayout(App.GetContext());
        RelativeLayoutTabComment.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
        RelativeLayoutTabComment.setBackgroundResource(R.color.White);
        RelativeLayoutTabComment.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { ChangeTab(2); } });

        LinearLayoutTab.addView(RelativeLayoutTabComment);

        RelativeLayout.LayoutParams TextViewTabCommentParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTabCommentParam.addRule(RelativeLayout.CENTER_HORIZONTAL);

        TextViewTabComment = new TextView(App.GetContext());
        TextViewTabComment.setLayoutParams(TextViewTabCommentParam);
        TextViewTabComment.setTextColor(ContextCompat.getColor(App.GetContext(), R.color.BlueLight));
        TextViewTabComment.setText("COMMENT");
        TextViewTabComment.setPadding(0, MiscHandler.DpToPx(15), 0, MiscHandler.DpToPx(15));
        TextViewTabComment.setId(MiscHandler.GenerateViewID());
        TextViewTabComment.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

        RelativeLayoutTabComment.addView(TextViewTabComment);

        RelativeLayout.LayoutParams ViewTabCommentParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, MiscHandler.DpToPx(2));
        ViewTabCommentParam.addRule(RelativeLayout.BELOW, TextViewTabComment.getId());

        ViewTabComment = new View(App.GetContext());
        ViewTabComment.setLayoutParams(ViewTabCommentParam);
        ViewTabComment.setBackgroundResource(R.color.BlueLight);

        RelativeLayoutTabComment.addView(ViewTabComment);

        RelativeLayout RelativeLayoutTabLike = new RelativeLayout(App.GetContext());
        RelativeLayoutTabLike.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
        RelativeLayoutTabLike.setBackgroundResource(R.color.White);
        RelativeLayoutTabLike.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { ChangeTab(3); } });

        LinearLayoutTab.addView(RelativeLayoutTabLike);

        RelativeLayout.LayoutParams TextViewTabLikeParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTabLikeParam.addRule(RelativeLayout.CENTER_HORIZONTAL);

        TextViewTabLike = new TextView(App.GetContext());
        TextViewTabLike.setLayoutParams(TextViewTabLikeParam);
        TextViewTabLike.setTextColor(ContextCompat.getColor(App.GetContext(), R.color.BlueLight));
        TextViewTabLike.setText("LIKE");
        TextViewTabLike.setPadding(0, MiscHandler.DpToPx(15), 0, MiscHandler.DpToPx(15));
        TextViewTabLike.setId(MiscHandler.GenerateViewID());
        TextViewTabLike.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

        RelativeLayoutTabLike.addView(TextViewTabLike);

        RelativeLayout.LayoutParams ViewTabLikeParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, MiscHandler.DpToPx(2));
        ViewTabLikeParam.addRule(RelativeLayout.BELOW, TextViewTabLike.getId());

        ViewTabLike = new View(App.GetContext());
        ViewTabLike.setLayoutParams(ViewTabLikeParam);
        ViewTabLike.setBackgroundResource(R.color.BlueLight);

        RelativeLayoutTabLike.addView(ViewTabLike);

        FrameLayout FrameLayoutTab = new FrameLayout(App.GetContext());
        FrameLayoutTab.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        FrameLayoutTab.setId(FrameLayoutID);

        LinearLayoutMain2.addView(FrameLayoutTab);

        RelativeLayoutLoading = new RelativeLayout(App.GetContext());
        RelativeLayoutLoading.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayoutLoading.setBackgroundResource(R.color.White);

        Root.addView(RelativeLayoutLoading);

        RelativeLayout.LayoutParams LoadingViewDataParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LoadingViewDataParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        LoadingViewData = new LoadingView(App.GetContext());
        LoadingViewData.setLayoutParams(LoadingViewDataParam);
        LoadingViewData.SetColor(R.color.BlueGray2);

        RelativeLayoutLoading.addView(LoadingViewData);

        RelativeLayout.LayoutParams TextViewTryParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTryParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        TextViewTry = new TextView(App.GetContext());
        TextViewTry.setLayoutParams(TextViewTryParam);
        TextViewTry.setTextColor(ContextCompat.getColor(App.GetContext(), R.color.BlueGray2));
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
        RequestHandler.Cancel("FragmentProfile");
    }

    private void ChangeTab(int Tab)
    {
        TextViewTabPost.setTextColor(ContextCompat.getColor(App.GetContext(), R.color.Gray7));
        ViewTabPost.setBackgroundResource(R.color.White);
        TextViewTabComment.setTextColor(ContextCompat.getColor(App.GetContext(), R.color.Gray7));
        ViewTabComment.setBackgroundResource(R.color.White);
        TextViewTabLike.setTextColor(ContextCompat.getColor(App.GetContext(), R.color.Gray7));
        ViewTabLike.setBackgroundResource(R.color.White);

        Fragment fragment = new FragmentProfilePost();

        switch (Tab)
        {
            case 1:
                TextViewTabPost.setTextColor(ContextCompat.getColor(App.GetContext(), R.color.BlueLight));
                ViewTabPost.setBackgroundResource(R.color.BlueLight);
            break;
            case 2:
                fragment = new FragmentProfileComment();
                TextViewTabComment.setTextColor(ContextCompat.getColor(App.GetContext(), R.color.BlueLight));
                ViewTabComment.setBackgroundResource(R.color.BlueLight);
            break;
            case 3:
                fragment = new FragmentProfileLike();
                TextViewTabLike.setTextColor(ContextCompat.getColor(App.GetContext(), R.color.BlueLight));
                ViewTabLike.setBackgroundResource(R.color.BlueLight);
            break;
        }

        getChildFragmentManager().beginTransaction().replace(FrameLayoutID, fragment).commit();
    }

    private void RetrieveDataFromServer()
    {
        TextViewTry.setVisibility(View.GONE);
        LoadingViewData.Start();

        String ID = SharedHandler.GetString("ID");

        if (getArguments() != null && !getArguments().getString("ID", "").equals(""))
            ID = getArguments().getString("ID");

        RequestHandler.Method("POST")
        .Address(URLHandler.GetURL(URLHandler.URL.PROFILE_GET))
        .Header("TOKEN",SharedHandler.GetString("TOKEN"))
        .Param("ID", ID)
        .Tag("FragmentProfile")
        .Build(new RequestHandler.OnCompleteCallBack()
        {
            @Override
            public void OnFinish(String Response, int Status)
            {
                if (Status < 0)
                {
                    MiscHandler.Toast(getString(R.string.GeneralCheckInternet));
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

                        RequestHandler.GetImage(ImageViewCircleProfile, Data.getString("Avatar"), "FragmentProfile", MiscHandler.DpToPx(90), MiscHandler.DpToPx(90), true);
                        RequestHandler.GetImage(ImageViewCover, Data.getString("Cover"), "FragmentProfile", true);

                        TextViewUsername.setText(Data.getString("Username"));
                        TextViewDescription.setText(Data.getString("Description"));
                        TextViewUrl.setText(Data.getString("Link"));
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
