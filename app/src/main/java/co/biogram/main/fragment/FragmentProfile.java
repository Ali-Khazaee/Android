package co.biogram.main.fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.text.util.Linkify;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.bumptech.glide.Glide;

import org.json.JSONObject;

import co.biogram.main.R;
import co.biogram.main.activity.ActivityProfile;
import co.biogram.main.handler.MiscHandler;
import co.biogram.main.handler.SharedHandler;
import co.biogram.main.handler.URLHandler;
import co.biogram.main.misc.ImageViewCircle;
import co.biogram.main.misc.LoadingView;
import co.biogram.main.misc.ScrollViewSticky;

public class FragmentProfile extends Fragment
{
    private RelativeLayout RelativeLayoutLoading;
    private LoadingView LoadingViewData;
    private TextView TextViewTry;

    private String Username;

    private ImageView ImageViewCover;
    private ImageViewCircle ImageViewCircleProfile;

    private GradientDrawable ShapeFollowWhite;
    private GradientDrawable ShapeFollowBlue;

    private ImageView ImageViewEdit;
    private ImageView ImageViewFollow;

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

        MiscHandler.HideSoftKey(getActivity());

        Username = SharedHandler.GetString(context, "Username");

        if (getArguments() != null && !getArguments().getString("Username", "").equals(""))
            Username = getArguments().getString("Username");

        CoordinatorLayout Root = new CoordinatorLayout(context);
        Root.setLayoutParams(new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.MATCH_PARENT));

        AppBarLayout AppBar = new AppBarLayout(context);
        AppBar.setLayoutParams(new AppBarLayout.LayoutParams(AppBarLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 160)));
        AppBar.setId(MiscHandler.GenerateViewID());
        AppBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener()
        {
            private boolean Hidden = false;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset)
            {
                int Off = Math.abs(verticalOffset);
                int Total = appBarLayout.getTotalScrollRange();
                float Ratio = Math.max(((float) (Total - Off) / Total), 0.75f);

                ImageViewCircleProfile.setScaleX(Ratio);
                ImageViewCircleProfile.setScaleY(Ratio);

                if (!Hidden && Off >= Total)
                {
                    Hidden = true;

                    ScaleAnimation Fade = new ScaleAnimation(0.75f, 0, 0.75f, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    Fade.setDuration(300);

                    AnimationSet FadeAnim = new AnimationSet(true);
                    FadeAnim.addAnimation(Fade);
                    FadeAnim.setAnimationListener(new Animation.AnimationListener()
                    {
                        @Override public void onAnimationStart(Animation animation) { }
                        @Override public void onAnimationRepeat(Animation animation) { }
                        @Override public void onAnimationEnd(Animation animation) { ImageViewCircleProfile.setVisibility(View.INVISIBLE); }
                    });
                    ImageViewCircleProfile.startAnimation(FadeAnim);

                }
                else if (Hidden && Off < Total / 2)
                {
                    Hidden = false;

                    ImageViewCircleProfile.setVisibility(View.VISIBLE);

                    ScaleAnimation Fade = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    Fade.setDuration(300);

                    AnimationSet FadeAnim = new AnimationSet(true);
                    FadeAnim.addAnimation(Fade);
                    ImageViewCircleProfile.setAnimation(FadeAnim);
                }
            }
        });
        // noinspection all
        AppBar.setElevation(0);
        // noinspection all
        AppBar.setTargetElevation(0);

        Root.addView(AppBar);

        CollapsingToolbarLayout Collapsing = new CollapsingToolbarLayout(context);
        Collapsing.setLayoutParams(new AppBarLayout.LayoutParams(AppBarLayout.LayoutParams.MATCH_PARENT, AppBarLayout.LayoutParams.MATCH_PARENT));
        Collapsing.setContentScrimResource(R.color.White);

        AppBarLayout.LayoutParams CollapsingParam = (AppBarLayout.LayoutParams) Collapsing.getLayoutParams();
        CollapsingParam.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);

        AppBar.addView(Collapsing);

        ImageViewCover = new ImageView(context);
        ImageViewCover.setLayoutParams(new CollapsingToolbarLayout.LayoutParams(CollapsingToolbarLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 160)));
        ImageViewCover.setScaleType(ImageView.ScaleType.FIT_XY);
        ImageViewCover.setImageResource(R.color.BlueLight);

        CollapsingToolbarLayout.LayoutParams ImageViewCoverParam = (CollapsingToolbarLayout.LayoutParams) ImageViewCover.getLayoutParams();
        ImageViewCoverParam.setCollapseMode(CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PARALLAX);

        ImageViewCover.setLayoutParams(ImageViewCoverParam);
        ImageViewCover.requestLayout();

        Collapsing.addView(ImageViewCover);

        Toolbar ToolBar = new Toolbar(context);
        ToolBar.setLayoutParams(new CollapsingToolbarLayout.LayoutParams(CollapsingToolbarLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56)));
        ToolBar.setContentInsetsAbsolute(0, 0);

        CollapsingToolbarLayout.LayoutParams ToolBarParam = (CollapsingToolbarLayout.LayoutParams) ToolBar.getLayoutParams();
        ToolBarParam.setCollapseMode(CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PIN);

        ToolBar.setLayoutParams(ToolBarParam);
        ToolBar.requestLayout();

        Collapsing.addView(ToolBar);

        ImageView ImageViewBack = new ImageView(context);
        ImageViewBack.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), RelativeLayout.LayoutParams.MATCH_PARENT));
        ImageViewBack.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewBack.setPadding(MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12));
        ImageViewBack.setImageResource(R.drawable.ic_back_blue);
        ImageViewBack.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { } });

        ToolBar.addView(ImageViewBack);

        ScrollViewSticky ScrollMain = new ScrollViewSticky(context);
        ScrollMain.setLayoutParams(new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.MATCH_PARENT));

        CoordinatorLayout.LayoutParams ScrollMainParam = (CoordinatorLayout.LayoutParams) ScrollMain.getLayoutParams();
        ScrollMainParam.setBehavior(new AppBarLayout.ScrollingViewBehavior());

        ScrollMain.requestLayout();

        Root.addView(ScrollMain);

        RelativeLayout Main = new RelativeLayout(context);
        Main.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

        ScrollMain.addView(Main);

        View ViewLine = new View(context);
        ViewLine.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 5)));
        ViewLine.setBackgroundResource(R.color.Gray);
        ViewLine.setId(MiscHandler.GenerateViewID());

        Main.addView(ViewLine);

        RelativeLayout.LayoutParams LinearDataParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        LinearDataParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

        LinearLayout LinearData = new LinearLayout(context);
        LinearData.setLayoutParams(LinearDataParam);
        LinearData.setOrientation(LinearLayout.VERTICAL);

        Main.addView(LinearData);

        TextViewUsername = new TextView(context);
        TextViewUsername.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewUsername.setTextColor(ContextCompat.getColor(context, R.color.Black));
        TextViewUsername.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 45), MiscHandler.ToDimension(context, 15), 0);
        TextViewUsername.setTypeface(null, Typeface.BOLD);
        TextViewUsername.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

        LinearData.addView(TextViewUsername);

        TextViewDescription = new TextView(context);
        TextViewDescription.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewDescription.setTextColor(ContextCompat.getColor(context, R.color.Black));
        TextViewDescription.setPadding(MiscHandler.ToDimension(context, 15), 0, MiscHandler.ToDimension(context, 15), 0);
        TextViewDescription.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        TextViewDescription.setVisibility(View.GONE);

        LinearData.addView(TextViewDescription);

        TextViewUrl = new TextView(getActivity());
        TextViewUrl.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewUrl.setTextColor(ContextCompat.getColor(context, R.color.BlueLight));
        TextViewUrl.setLinkTextColor(ContextCompat.getColor(context, R.color.BlueLight));
        TextViewUrl.setPadding(MiscHandler.ToDimension(context, 15), 0, MiscHandler.ToDimension(context, 15), 0);
        TextViewUrl.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        TextViewUrl.setVisibility(View.GONE);
        Linkify.addLinks(TextViewUrl, Linkify.ALL);

        LinearData.addView(TextViewUrl);

        LinearLayout LinearDetails = new LinearLayout(context);
        LinearDetails.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        LinearDetails.setOrientation(LinearLayout.HORIZONTAL);
        LinearDetails.setPadding(0, MiscHandler.ToDimension(context, 15), 0, MiscHandler.ToDimension(context, 5));

        LinearData.addView(LinearDetails);

        RelativeLayout RelativePost = new RelativeLayout(context);
        RelativePost.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.1f));

        LinearDetails.addView(RelativePost);

        RelativeLayout.LayoutParams TextViewPostCountParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewPostCountParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        TextViewPostCountParam.addRule(RelativeLayout.CENTER_HORIZONTAL);

        TextViewPostCount = new TextView(context);
        TextViewPostCount.setLayoutParams(TextViewPostCountParam);
        TextViewPostCount.setTextColor(ContextCompat.getColor(context, R.color.Black));
        TextViewPostCount.setTypeface(null, Typeface.BOLD);
        TextViewPostCount.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        TextViewPostCount.setId(MiscHandler.GenerateViewID());

        RelativePost.addView(TextViewPostCount);

        RelativeLayout.LayoutParams TextViewPostParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewPostParam.addRule(RelativeLayout.BELOW, TextViewPostCount.getId());
        TextViewPostParam.addRule(RelativeLayout.CENTER_HORIZONTAL);

        TextView TextViewPost = new TextView(context);
        TextViewPost.setLayoutParams(TextViewPostParam);
        TextViewPost.setTextColor(ContextCompat.getColor(context, R.color.Gray5));
        TextViewPost.setText(getString(R.string.FragmentProfilePost));
        TextViewPost.setPadding(MiscHandler.ToDimension(context, 5), MiscHandler.ToDimension(context, 5), MiscHandler.ToDimension(context, 5), MiscHandler.ToDimension(context, 5));
        TextViewPost.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        RelativePost.addView(TextViewPost);

        RelativeLayout RelativeFollower = new RelativeLayout(context);
        RelativeFollower.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
        RelativeFollower.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Bundle bundle = new Bundle();
                bundle.putString("Username", Username);

                Fragment fragment = new FragmentFollowers();
                fragment.setArguments(bundle);

                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.ActivityMainFullContainer, fragment).addToBackStack("FragmentFollowers").commit();
            }
        });

        LinearDetails.addView(RelativeFollower);

        RelativeLayout.LayoutParams TextViewFollowerCountParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewFollowerCountParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        TextViewFollowerCountParam.addRule(RelativeLayout.CENTER_HORIZONTAL);

        TextViewFollowerCount = new TextView(context);
        TextViewFollowerCount.setLayoutParams(TextViewFollowerCountParam);
        TextViewFollowerCount.setTextColor(ContextCompat.getColor(context, R.color.Black));
        TextViewFollowerCount.setTypeface(null, Typeface.BOLD);
        TextViewFollowerCount.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        TextViewFollowerCount.setId(MiscHandler.GenerateViewID());

        RelativeFollower.addView(TextViewFollowerCount);

        RelativeLayout.LayoutParams TextViewFollowerParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewFollowerParam.addRule(RelativeLayout.BELOW, TextViewFollowerCount.getId());
        TextViewFollowerParam.addRule(RelativeLayout.CENTER_HORIZONTAL);

        TextView TextViewFollower = new TextView(context);
        TextViewFollower.setLayoutParams(TextViewFollowerParam);
        TextViewFollower.setTextColor(ContextCompat.getColor(context, R.color.Gray5));
        TextViewFollower.setText(getString(R.string.FragmentProfileFollowers));
        TextViewFollower.setPadding(MiscHandler.ToDimension(context, 5), MiscHandler.ToDimension(context, 5), MiscHandler.ToDimension(context, 5), MiscHandler.ToDimension(context, 5));
        TextViewFollower.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        RelativeFollower.addView(TextViewFollower);

        RelativeLayout RelativeFollowing = new RelativeLayout(context);
        RelativeFollowing.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
        RelativeFollowing.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Bundle bundle = new Bundle();
                bundle.putString("Username", Username);

                Fragment fragment = new FragmentFollowing();
                fragment.setArguments(bundle);

                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.ActivityMainFullContainer, fragment).addToBackStack("FragmentFollowing").commit();
            }
        });

        LinearDetails.addView(RelativeFollowing);

        RelativeLayout.LayoutParams TextViewFollowingCountParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewFollowingCountParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        TextViewFollowingCountParam.addRule(RelativeLayout.CENTER_HORIZONTAL);

        TextViewFollowingCount = new TextView(context);
        TextViewFollowingCount.setLayoutParams(TextViewFollowingCountParam);
        TextViewFollowingCount.setTextColor(ContextCompat.getColor(context, R.color.Black));
        TextViewFollowingCount.setTypeface(null, Typeface.BOLD);
        TextViewFollowingCount.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        TextViewFollowingCount.setId(MiscHandler.GenerateViewID());

        RelativeFollowing.addView(TextViewFollowingCount);

        RelativeLayout.LayoutParams TextViewFollowingParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewFollowingParam.addRule(RelativeLayout.BELOW, TextViewFollowingCount.getId());
        TextViewFollowingParam.addRule(RelativeLayout.CENTER_HORIZONTAL);

        TextView TextViewFollowing = new TextView(context);
        TextViewFollowing.setLayoutParams(TextViewFollowingParam);
        TextViewFollowing.setTextColor(ContextCompat.getColor(context, R.color.Gray5));
        TextViewFollowing.setText(getString(R.string.FragmentProfileFollowing));
        TextViewFollowing.setPadding(MiscHandler.ToDimension(context, 5), MiscHandler.ToDimension(context, 5), MiscHandler.ToDimension(context, 5), MiscHandler.ToDimension(context, 5));
        TextViewFollowing.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        RelativeFollowing.addView(TextViewFollowing);

        View ViewLine2 = new View(context);
        ViewLine2.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 5)));
        ViewLine2.setBackgroundResource(R.color.Gray);

        LinearData.addView(ViewLine2);

        LinearLayout LinearTab = new LinearLayout(context);
        LinearTab.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56)));
        LinearTab.setTag("sticky");

        LinearData.addView(LinearTab);

        RelativeLayout RelativeTabPost = new RelativeLayout(context);
        RelativeTabPost.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
        RelativeTabPost.setBackgroundResource(R.color.White);
        RelativeTabPost.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { ChangeTab(context, 1); } });

        LinearTab.addView(RelativeTabPost);

        RelativeLayout.LayoutParams TextViewTabPostParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTabPostParam.addRule(RelativeLayout.CENTER_HORIZONTAL);

        TextViewTabPost = new TextView(context);
        TextViewTabPost.setLayoutParams(TextViewTabPostParam);
        TextViewTabPost.setTextColor(ContextCompat.getColor(context, R.color.BlueLight));
        TextViewTabPost.setText(getString(R.string.FragmentProfilePost2));
        TextViewTabPost.setPadding(0, MiscHandler.ToDimension(context, 15), 0, MiscHandler.ToDimension(context, 15));
        TextViewTabPost.setId(MiscHandler.GenerateViewID());
        TextViewTabPost.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

        RelativeTabPost.addView(TextViewTabPost);

        RelativeLayout.LayoutParams ViewTabPostParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, MiscHandler.ToDimension(context, 2));
        ViewTabPostParam.addRule(RelativeLayout.BELOW, TextViewTabPost.getId());

        ViewTabPost = new View(context);
        ViewTabPost.setLayoutParams(ViewTabPostParam);
        ViewTabPost.setBackgroundResource(R.color.BlueLight);

        RelativeTabPost.addView(ViewTabPost);

        RelativeLayout RelativeLayoutTabComment = new RelativeLayout(context);
        RelativeLayoutTabComment.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
        RelativeLayoutTabComment.setBackgroundResource(R.color.White);
        RelativeLayoutTabComment.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { ChangeTab(context, 2); } });

        LinearTab.addView(RelativeLayoutTabComment);

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
        RelativeLayoutTabLike.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { ChangeTab(context, 3); } });

        LinearTab.addView(RelativeLayoutTabLike);

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

        FrameLayout FrameTab = new FrameLayout(context);
        FrameTab.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        FrameTab.setId(FrameLayoutID);

        LinearData.addView(FrameTab);

        ImageViewCircleProfile = new ImageViewCircle(context);
        ImageViewCircleProfile.SetBorderWidth(MiscHandler.ToDimension(context, 3));
        ImageViewCircleProfile.setLayoutParams(new CoordinatorLayout.LayoutParams(MiscHandler.ToDimension(context, 90), MiscHandler.ToDimension(context, 90)));
        ImageViewCircleProfile.setImageResource(R.color.BlueGray);

        CoordinatorLayout.LayoutParams ImageViewCircleProfileParam2 = (CoordinatorLayout.LayoutParams) ImageViewCircleProfile.getLayoutParams();
        ImageViewCircleProfileParam2.anchorGravity = Gravity.BOTTOM | Gravity.START;
        ImageViewCircleProfileParam2.leftMargin = MiscHandler.ToDimension(context, 10);
        ImageViewCircleProfileParam2.setAnchorId(AppBar.getId());

        ImageViewCircleProfile.setLayoutParams(ImageViewCircleProfileParam2);
        ImageViewCircleProfile.requestLayout();

        Root.addView(ImageViewCircleProfile);


























        /*RelativeLayout.LayoutParams ImageViewEditParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 50), MiscHandler.ToDimension(context, 50));
        ImageViewEditParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 135), MiscHandler.ToDimension(context, 15), 0);
        ImageViewEditParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        ShapeFollowWhite = new GradientDrawable();
        ShapeFollowWhite.setShape(GradientDrawable.OVAL);
        ShapeFollowWhite.setColor(Color.WHITE);
        ShapeFollowWhite.setStroke(MiscHandler.ToDimension(context, 2), Color.parseColor("#09000000"));

        ImageViewEdit = new ImageView(context);
        ImageViewEdit.setLayoutParams(ImageViewEditParam);
        ImageViewEdit.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewEdit.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
        ImageViewEdit.setBackground(ShapeFollowWhite);
        ImageViewEdit.setImageResource(R.drawable.ic_setting_black);
        ImageViewEdit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getActivity().startActivity(new Intent(context, ActivityProfile.class));
                getActivity().finish();
            }
        });

        Main.addView(ImageViewEdit);

        RelativeLayout.LayoutParams LoadingViewFollowParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 50), MiscHandler.ToDimension(context, 50));
        LoadingViewFollowParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 135), MiscHandler.ToDimension(context, 15), 0);
        LoadingViewFollowParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        final LoadingView LoadingViewFollow = new LoadingView(context);
        LoadingViewFollow.setLayoutParams(LoadingViewFollowParam);
        LoadingViewFollow.setBackground(ShapeFollowWhite);
        LoadingViewFollow.SetColor(R.color.BlueLight);
        LoadingViewFollow.SetSize(4);

        Main.addView(LoadingViewFollow);

        RelativeLayout.LayoutParams ImageButtonEditParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 50), MiscHandler.ToDimension(context, 50));
        ImageButtonEditParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 135), MiscHandler.ToDimension(context, 15), 0);
        ImageButtonEditParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        ShapeFollowBlue = new GradientDrawable();
        ShapeFollowBlue.setShape(GradientDrawable.OVAL);
        ShapeFollowBlue.setColor(Color.parseColor("#1da1f2"));
        ShapeFollowBlue.setStroke(MiscHandler.ToDimension(context, 2), Color.parseColor("#09000000"));

        ImageViewFollow = new ImageView(context);
        ImageViewFollow.setLayoutParams(ImageButtonEditParam);
        ImageViewFollow.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewFollow.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
        ImageViewFollow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ImageViewFollow.setVisibility(View.GONE);
                LoadingViewFollow.Start();

                ObjectAnimator SizeX = ObjectAnimator.ofFloat(ImageViewFollow, "scaleX", 1.5f);
                SizeX.setDuration(200);

                ObjectAnimator SizeY = ObjectAnimator.ofFloat(ImageViewFollow, "scaleY", 1.5f);
                SizeY.setDuration(200);

                ObjectAnimator Fade = ObjectAnimator.ofFloat(ImageViewFollow, "alpha",  0.1f, 1f);
                Fade.setDuration(400);

                ObjectAnimator SizeX2 = ObjectAnimator.ofFloat(ImageViewFollow, "scaleX", 1f);
                SizeX2.setDuration(200);
                SizeX2.setStartDelay(200);

                ObjectAnimator SizeY2 = ObjectAnimator.ofFloat(ImageViewFollow, "scaleY", 1f);
                SizeY2.setDuration(200);
                SizeY2.setStartDelay(200);

                AnimatorSet AnimationSet = new AnimatorSet();
                AnimationSet.playTogether(SizeX, SizeY, Fade, SizeX2, SizeY2);
                AnimationSet.start();

                AndroidNetworking.post(URLHandler.GetURL(URLHandler.URL.FOLLOW))
                .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
                .addBodyParameter("Username", Username)
                .setTag("FragmentProfile")
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
                                if (Result.getBoolean("Follow"))
                                {
                                    ImageViewFollow.setImageResource(R.drawable.ic_follow_block);
                                    ImageViewFollow.setBackground(ShapeFollowWhite);
                                    MiscHandler.Toast(context, getString(R.string.FragmentProfileFollow));
                                }
                                else
                                {
                                    ImageViewFollow.setImageResource(R.drawable.ic_follow);
                                    ImageViewFollow.setBackground(ShapeFollowBlue);
                                    MiscHandler.Toast(context, getString(R.string.FragmentProfileUnFollow));
                                }
                            }

                            LoadingViewFollow.Stop();
                            ImageViewFollow.setVisibility(View.VISIBLE);

                        }
                        catch (Exception e)
                        {
                            // Leave Me Alone
                        }
                    }

                    @Override
                    public void onError(ANError anError)
                    {
                        MiscHandler.Toast(context, getString(R.string.NoInternet));
                    }
                });
            }
        });

        Main.addView(ImageViewFollow);











        RelativeLayoutLoading = new RelativeLayout(context);
        RelativeLayoutLoading.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayoutLoading.setBackgroundResource(R.color.White);
        RelativeLayoutLoading.setClickable(true);

        Main.addView(RelativeLayoutLoading);

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
        TextViewTry.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { RetrieveDataFromServer(context); } });

        RelativeLayoutLoading.addView(TextViewTry);

        RetrieveDataFromServer(context);*/

        return Root;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        AndroidNetworking.cancel("FragmentProfile");
    }

    private void ChangeTab(Context context, int Tab)
    {
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

        FragManager.beginTransaction().replace(FrameLayoutID, SelectedFragment, SelectedFragment.getClass().getSimpleName()).commit();
    }

    private void RetrieveDataFromServer(final Context context)
    {
        TextViewTry.setVisibility(View.GONE);
        LoadingViewData.Start();

        AndroidNetworking.post(URLHandler.GetURL(URLHandler.URL.PROFILE_GET))
        .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
        .addBodyParameter("Username", Username)
        .setTag("FragmentProfile")
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
                        JSONObject Data = new JSONObject(Result.getString("Result"));

                        if (Result.getBoolean("Self"))
                        {
                            SharedHandler.SetString(context, "Avatar", Data.getString("Avatar"));
                            SharedHandler.SetString(context, "Username", Data.getString("Username"));
                        }
                        else
                        {
                            ImageViewEdit.setVisibility(View.GONE);
                            ImageViewFollow.setVisibility(View.VISIBLE);

                            if (Result.getBoolean("Follow"))
                            {
                                ImageViewFollow.setImageResource(R.drawable.ic_follow_block);
                                ImageViewFollow.setBackground(ShapeFollowWhite);
                            }
                            else
                            {
                                ImageViewFollow.setImageResource(R.drawable.ic_follow);
                                ImageViewFollow.setBackground(ShapeFollowBlue);
                            }
                        }

                        if (!Data.getString("Avatar").equals(""))
                            Glide.with(context).load(Data.getString("Avatar")).placeholder(R.color.BlueGray).override(MiscHandler.ToDimension(context, 90), MiscHandler.ToDimension(context, 90)).dontAnimate().into(ImageViewCircleProfile);

                        if (!Data.getString("Cover").equals(""))
                            Glide.with(context).load(Data.getString("Cover")).placeholder(R.color.BlueLight).dontAnimate().into(ImageViewCover);

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

                    ChangeTab(context, 1);
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
                MiscHandler.Toast(context, getString(R.string.NoInternet));
                TextViewTry.setVisibility(View.VISIBLE);
                LoadingViewData.Stop();
            }
        });
    }
}
