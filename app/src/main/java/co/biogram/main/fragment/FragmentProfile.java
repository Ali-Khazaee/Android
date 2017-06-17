package co.biogram.main.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.text.util.Linkify;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
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
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import org.json.JSONObject;

import co.biogram.main.R;
import co.biogram.main.activity.ActivityProfileEdit;
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
    private String PostCount;
    private String CommentCount;
    private String LikeCount;
    private boolean Self = false;

    private ImageView ImageViewBack;
    private ImageView ImageViewCover;
    private TextView TextViewDetailTool;
    private TextView TextViewUsernameTool;
    private ImageView ImageViewCoverLayer;
    private ImageViewCircle ImageViewCircleProfile;

    private GradientDrawable ShapeFollowWhite;
    private GradientDrawable ShapeFollowBlue;

    private ImageView ImageViewEdit;
    private LoadingView LoadingViewFollow;
    private ImageView ImageViewFollow;

    private TextView TextViewUsername;
    private TextView TextViewDescription;
    private TextView TextViewUrl;
    private TextView TextViewLocation;
    private LinearLayout LinearLayoutLocation;
    private LinearLayout LinearLayoutUrl;
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
        Root.setBackgroundResource(R.color.White);

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
                    Fade.setDuration(200);

                    AnimationSet FadeAnim = new AnimationSet(true);
                    FadeAnim.addAnimation(Fade);
                    FadeAnim.setAnimationListener(new Animation.AnimationListener()
                    {
                        @Override public void onAnimationStart(Animation animation) { }
                        @Override public void onAnimationRepeat(Animation animation) { }
                        @Override public void onAnimationEnd(Animation animation) { ImageViewCircleProfile.setVisibility(View.INVISIBLE); }
                    });

                    ImageViewCircleProfile.startAnimation(FadeAnim);

                    if (Self)
                    {
                        ScaleAnimation Fade2 = new ScaleAnimation(1, 0, 1, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                        Fade2.setDuration(200);

                        AnimationSet FadeAnim2 = new AnimationSet(true);
                        FadeAnim2.addAnimation(Fade2);
                        FadeAnim2.setAnimationListener(new Animation.AnimationListener()
                        {
                            @Override public void onAnimationStart(Animation animation) { }
                            @Override public void onAnimationRepeat(Animation animation) { }
                            @Override public void onAnimationEnd(Animation animation) { ImageViewEdit.setVisibility(View.INVISIBLE); }
                        });

                        ImageViewEdit.startAnimation(FadeAnim2);
                    }
                    else
                    {
                        ScaleAnimation Fade2 = new ScaleAnimation(1, 0, 1, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                        Fade2.setDuration(200);

                        AnimationSet FadeAnim2 = new AnimationSet(true);
                        FadeAnim2.addAnimation(Fade2);
                        FadeAnim2.setAnimationListener(new Animation.AnimationListener()
                        {
                            @Override public void onAnimationStart(Animation animation) { }
                            @Override public void onAnimationRepeat(Animation animation) { }
                            @Override public void onAnimationEnd(Animation animation) { LoadingViewFollow.setVisibility(View.INVISIBLE); }
                        });

                        AnimationSet FadeAnim3 = new AnimationSet(true);
                        FadeAnim3.addAnimation(Fade2);
                        FadeAnim3.setAnimationListener(new Animation.AnimationListener()
                        {
                            @Override public void onAnimationStart(Animation animation) { }
                            @Override public void onAnimationRepeat(Animation animation) { }
                            @Override public void onAnimationEnd(Animation animation) { ImageViewFollow.setVisibility(View.INVISIBLE); }
                        });

                        LoadingViewFollow.startAnimation(FadeAnim2);
                        ImageViewFollow.startAnimation(FadeAnim3);
                    }
                }
                else if (Hidden && Off < Total / 1.25)
                {
                    Hidden = false;

                    ImageViewCircleProfile.setVisibility(View.VISIBLE);

                    ScaleAnimation Fade = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    Fade.setDuration(300);

                    AnimationSet FadeAnim = new AnimationSet(true);
                    FadeAnim.addAnimation(Fade);

                    ImageViewCircleProfile.setAnimation(FadeAnim);

                    if (Self)
                    {
                        ImageViewEdit.setVisibility(View.VISIBLE);

                        ScaleAnimation Fade2 = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                        Fade2.setDuration(200);

                        AnimationSet FadeAnim2 = new AnimationSet(true);
                        FadeAnim2.addAnimation(Fade2);

                        ImageViewEdit.startAnimation(FadeAnim2);
                    }
                    else
                    {
                        LoadingViewFollow.setVisibility(View.VISIBLE);
                        ImageViewFollow.setVisibility(View.VISIBLE);

                        ScaleAnimation Fade2 = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                        Fade2.setDuration(200);

                        AnimationSet FadeAnim2 = new AnimationSet(true);
                        FadeAnim2.addAnimation(Fade2);

                        LoadingViewFollow.startAnimation(FadeAnim2);
                        ImageViewFollow.startAnimation(FadeAnim2);
                    }
                }
            }
        });

        if (Build.VERSION.SDK_INT > 20)
            AppBar.setOutlineProvider(null);

        Root.addView(AppBar);

        CollapsingToolbarLayout Collapsing = new CollapsingToolbarLayout(context)
        {
            private boolean Hidden = false;

            @Override
            public void setScrimsShown(boolean shown, boolean animate)
            {
                super.setScrimsShown(shown, animate);

                if (shown && Hidden)
                {
                    Hidden =  false;

                    ImageViewCoverLayer.setVisibility(View.VISIBLE);
                }
                else if (!shown && !Hidden)
                {
                    Hidden = true;

                    ImageViewCoverLayer.setVisibility(View.GONE);
                }
            }
        };
        Collapsing.setLayoutParams(new AppBarLayout.LayoutParams(AppBarLayout.LayoutParams.MATCH_PARENT, AppBarLayout.LayoutParams.MATCH_PARENT));
        Collapsing.setScrimVisibleHeightTrigger(MiscHandler.ToDimension(context, 57));

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

        ImageViewCoverLayer = new ImageView(context);
        ImageViewCoverLayer.setLayoutParams(new CollapsingToolbarLayout.LayoutParams(CollapsingToolbarLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 160)));
        ImageViewCoverLayer.setScaleType(ImageView.ScaleType.FIT_XY);
        ImageViewCoverLayer.setVisibility(View.GONE);

        CollapsingToolbarLayout.LayoutParams ImageViewCoverLayerParam = (CollapsingToolbarLayout.LayoutParams) ImageViewCoverLayer.getLayoutParams();
        ImageViewCoverLayerParam.setCollapseMode(CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PARALLAX);

        ImageViewCoverLayer.setLayoutParams(ImageViewCoverLayerParam);
        ImageViewCoverLayer.requestLayout();

        Collapsing.addView(ImageViewCoverLayer);

        final ImageView ImageViewCoverLayer2 = new ImageView(context);
        ImageViewCoverLayer2.setLayoutParams(new CollapsingToolbarLayout.LayoutParams(CollapsingToolbarLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 160)));
        ImageViewCoverLayer2.setBackgroundColor(Color.parseColor("#50000000"));
        ImageViewCoverLayer2.setVisibility(View.GONE);

        Collapsing.addView(ImageViewCoverLayer2);

        Toolbar ToolBar = new Toolbar(context);
        ToolBar.setLayoutParams(new CollapsingToolbarLayout.LayoutParams(CollapsingToolbarLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56)));
        ToolBar.setContentInsetsAbsolute(0, 0);

        CollapsingToolbarLayout.LayoutParams ToolBarParam = (CollapsingToolbarLayout.LayoutParams) ToolBar.getLayoutParams();
        ToolBarParam.setCollapseMode(CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PIN);

        ToolBar.setLayoutParams(ToolBarParam);
        ToolBar.requestLayout();

        Collapsing.addView(ToolBar);

        RelativeLayout RelativeTool = new RelativeLayout(context);
        RelativeTool.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

        ToolBar.addView(RelativeTool);

        ImageViewBack = new ImageView(context);
        ImageViewBack.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), RelativeLayout.LayoutParams.MATCH_PARENT));
        ImageViewBack.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewBack.setPadding(MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12));
        ImageViewBack.setImageResource(R.drawable.ic_back_white);
        ImageViewBack.setId(MiscHandler.GenerateViewID());
        ImageViewBack.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { getActivity().onBackPressed(); } });

        RelativeTool.addView(ImageViewBack);

        RelativeLayout.LayoutParams ImageViewMoreParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), RelativeLayout.LayoutParams.MATCH_PARENT);
        ImageViewMoreParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        ImageView ImageViewMore = new ImageView(context);
        ImageViewMore.setLayoutParams(ImageViewMoreParam);
        ImageViewMore.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewMore.setPadding(MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12));
        ImageViewMore.setImageResource(R.drawable.ic_back_white);
        ImageViewMore.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (Self)
                {

                }
                else
                {
                    final Dialog DialogMore = new Dialog(getActivity());
                    DialogMore.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    DialogMore.setCancelable(true);

                    LinearLayout Root = new LinearLayout(context);
                    Root.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    Root.setBackgroundResource(R.color.White);
                    Root.setOrientation(LinearLayout.VERTICAL);

                    TextView TextViewReport = new TextView(context);
                    TextViewReport.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    TextViewReport.setTextColor(ContextCompat.getColor(context, R.color.Black));
                    TextViewReport.setText(getString(R.string.FragmentProfileReport));
                    TextViewReport.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
                    TextViewReport.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                    TextViewReport.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View view)
                        {
                            DialogMore.dismiss();
                            MiscHandler.Toast(context, getString(R.string.Soon));
                        }
                    });

                    Root.addView(TextViewReport);

                    DialogMore.setContentView(Root);
                    DialogMore.show();
                }
            }
        });

        RelativeTool.addView(ImageViewMore);

        RelativeLayout.LayoutParams TextViewUsernameToolParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewUsernameToolParam.addRule(RelativeLayout.RIGHT_OF, ImageViewBack.getId());
        TextViewUsernameToolParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        TextViewUsernameToolParam.setMargins(0, MiscHandler.ToDimension(context, 5), 0, 0);

        TextViewUsernameTool = new TextView(context);
        TextViewUsernameTool.setLayoutParams(TextViewUsernameToolParam);
        TextViewUsernameTool.setTextColor(ContextCompat.getColor(context, R.color.White));
        TextViewUsernameTool.setTypeface(null, Typeface.BOLD);
        TextViewUsernameTool.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        TextViewUsernameTool.setId(MiscHandler.GenerateViewID());
        TextViewUsernameTool.setVisibility(View.GONE);

        RelativeTool.addView(TextViewUsernameTool);

        RelativeLayout.LayoutParams TextViewDetailToolParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewDetailToolParam.addRule(RelativeLayout.RIGHT_OF, ImageViewBack.getId());
        TextViewDetailToolParam.addRule(RelativeLayout.BELOW, TextViewUsernameTool.getId());

        TextViewDetailTool = new TextView(context);
        TextViewDetailTool.setLayoutParams(TextViewDetailToolParam);
        TextViewDetailTool.setTextColor(ContextCompat.getColor(context, R.color.White5));
        TextViewDetailTool.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        TextViewDetailTool.setVisibility(View.GONE);

        RelativeTool.addView(TextViewDetailTool);

        ScrollViewSticky ScrollMain = new ScrollViewSticky(context);
        ScrollMain.setLayoutParams(new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT));
        ScrollMain.setOnScrollChangeListener(new ScrollViewSticky.OnScrollChangeListener()
        {
            private boolean Hidden = false;

            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY)
            {
                Rect rect = new Rect();
                v.getHitRect(rect);

                if (TextViewUsername != null)
                {
                    if (Hidden && !TextViewUsername.getLocalVisibleRect(rect))
                    {
                        Hidden = false;

                        Animation Fade = new AlphaAnimation(0, 1);
                        Fade.setDuration(500);

                        AnimationSet animation = new AnimationSet(false);
                        animation.addAnimation(Fade);

                        ImageViewCoverLayer2.setAnimation(animation);
                        TextViewDetailTool.setAnimation(animation);
                        TextViewUsernameTool.setAnimation(animation);

                        ImageViewCoverLayer2.setVisibility(View.VISIBLE);
                        TextViewDetailTool.setVisibility(View.VISIBLE);
                        TextViewUsernameTool.setVisibility(View.VISIBLE);
                    }
                    else if (!Hidden && TextViewUsername.getLocalVisibleRect(rect))
                    {
                        Hidden = true;

                        Animation Fade = new AlphaAnimation(1, 0);
                        Fade.setDuration(500);

                        AnimationSet animation = new AnimationSet(false);
                        animation.addAnimation(Fade);

                        ImageViewCoverLayer2.setAnimation(animation);
                        TextViewDetailTool.setAnimation(animation);
                        TextViewUsernameTool.setAnimation(animation);

                        ImageViewCoverLayer2.setVisibility(View.GONE);
                        TextViewDetailTool.setVisibility(View.GONE);
                        TextViewUsernameTool.setVisibility(View.GONE);
                    }
                }
            }
        });

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
        LinearData.setBackgroundResource(R.color.White);
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
        TextViewDescription.setTextColor(ContextCompat.getColor(context, R.color.Black4));
        TextViewDescription.setPadding(MiscHandler.ToDimension(context, 15), 0, MiscHandler.ToDimension(context, 15), 0);
        TextViewDescription.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        TextViewDescription.setLineSpacing(5, 1);
        TextViewDescription.setVisibility(View.GONE);

        LinearData.addView(TextViewDescription);

        LinearLayoutUrl = new LinearLayout(context);
        LinearLayoutUrl.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        LinearLayoutUrl.setPadding(MiscHandler.ToDimension(context, 10), 0, 0, 0);
        LinearLayoutUrl.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayoutUrl.setVisibility(View.GONE);

        LinearData.addView(LinearLayoutUrl);

        ImageView ImageViewUrl = new ImageView(context);
        ImageViewUrl.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 24), MiscHandler.ToDimension(context, 24)));
        ImageViewUrl.setImageResource(R.drawable.ic_arrow_down_blue);

        LinearLayoutUrl.addView(ImageViewUrl);

        TextViewUrl = new TextView(getActivity());
        TextViewUrl.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewUrl.setTextColor(ContextCompat.getColor(context, R.color.BlueLight));
        TextViewUrl.setLinkTextColor(ContextCompat.getColor(context, R.color.BlueLight));
        TextViewUrl.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        Linkify.addLinks(TextViewUrl, Linkify.ALL);

        LinearLayoutUrl.addView(TextViewUrl);

        LinearLayoutLocation = new LinearLayout(context);
        LinearLayoutLocation.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        LinearLayoutLocation.setPadding(MiscHandler.ToDimension(context, 10), 0, 0, 0);
        LinearLayoutLocation.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayoutLocation.setVisibility(View.GONE);

        LinearData.addView(LinearLayoutLocation);

        ImageView ImageViewLocation = new ImageView(context);
        ImageViewLocation.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 24), MiscHandler.ToDimension(context, 24)));
        ImageViewLocation.setImageResource(R.drawable.ic_arrow_down_blue);

        LinearLayoutLocation.addView(ImageViewLocation);

        TextViewLocation = new TextView(getActivity());
        TextViewLocation.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewLocation.setTextColor(ContextCompat.getColor(context, R.color.Gray5));
        TextViewLocation.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        LinearLayoutLocation.addView(TextViewLocation);

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

        LinearLayout LinearTab2 = new LinearLayout(context);
        LinearTab2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 57)));
        LinearTab2.setOrientation(LinearLayout.VERTICAL);
        LinearTab2.setTag("sticky");

        LinearData.addView(LinearTab2);

        LinearLayout LinearTab = new LinearLayout(context);
        LinearTab.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56)));

        LinearTab2.addView(LinearTab);

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
        TextViewTabPost.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        TextViewTabPost.setTypeface(null, Typeface.BOLD);

        RelativeTabPost.addView(TextViewTabPost);

        RelativeLayout.LayoutParams ViewTabPostParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, MiscHandler.ToDimension(context, 2));
        ViewTabPostParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

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
        TextViewTabComment.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        TextViewTabComment.setTypeface(null, Typeface.BOLD);

        RelativeLayoutTabComment.addView(TextViewTabComment);

        RelativeLayout.LayoutParams ViewTabCommentParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, MiscHandler.ToDimension(context, 2));
        ViewTabCommentParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        ViewTabComment = new View(context);
        ViewTabComment.setLayoutParams(ViewTabCommentParam);
        ViewTabComment.setBackgroundResource(R.color.BlueLight);

        RelativeLayoutTabComment.addView(ViewTabComment);

        RelativeLayout RelativeLayoutTabLike = new RelativeLayout(context);
        RelativeLayoutTabLike.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
        RelativeLayoutTabLike.setBackgroundResource(R.color.White);
        RelativeLayoutTabLike.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { ChangeTab(context, 3); } });

        LinearTab.addView(RelativeLayoutTabLike);

        View ViewLine3 = new View(context);
        ViewLine3.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1)));
        ViewLine3.setBackgroundResource(R.color.Gray);

        LinearTab2.addView(ViewLine3);

        RelativeLayout.LayoutParams TextViewTabLikeParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTabLikeParam.addRule(RelativeLayout.CENTER_HORIZONTAL);

        TextViewTabLike = new TextView(context);
        TextViewTabLike.setLayoutParams(TextViewTabLikeParam);
        TextViewTabLike.setTextColor(ContextCompat.getColor(context, R.color.BlueLight));
        TextViewTabLike.setText(getString(R.string.FragmentProfileLike));
        TextViewTabLike.setPadding(0, MiscHandler.ToDimension(context, 15), 0, MiscHandler.ToDimension(context, 15));
        TextViewTabLike.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        TextViewTabLike.setTypeface(null, Typeface.BOLD);

        RelativeLayoutTabLike.addView(TextViewTabLike);

        RelativeLayout.LayoutParams ViewTabLikeParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, MiscHandler.ToDimension(context, 2));
        ViewTabLikeParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        ViewTabLike = new View(context);
        ViewTabLike.setLayoutParams(ViewTabLikeParam);
        ViewTabLike.setBackgroundResource(R.color.BlueLight);

        RelativeLayoutTabLike.addView(ViewTabLike);

        RelativeLayout.LayoutParams ViewLine3Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1));
        ViewLine3Param.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        FrameLayout FrameTab = new FrameLayout(context);
        FrameTab.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        FrameTab.setId(FrameLayoutID);

        LinearData.addView(FrameTab);

        ImageViewCircleProfile = new ImageViewCircle(context);
        ImageViewCircleProfile.SetBorderWidth(MiscHandler.ToDimension(context, 3));
        ImageViewCircleProfile.setLayoutParams(new CoordinatorLayout.LayoutParams(MiscHandler.ToDimension(context, 90), MiscHandler.ToDimension(context, 90)));
        ImageViewCircleProfile.setImageResource(R.color.BlueGray);

        CoordinatorLayout.LayoutParams ImageViewCircleProfileParam = (CoordinatorLayout.LayoutParams) ImageViewCircleProfile.getLayoutParams();
        ImageViewCircleProfileParam.anchorGravity = Gravity.BOTTOM | Gravity.START;
        ImageViewCircleProfileParam.leftMargin = MiscHandler.ToDimension(context, 10);
        ImageViewCircleProfileParam.setAnchorId(AppBar.getId());

        ImageViewCircleProfile.setLayoutParams(ImageViewCircleProfileParam);
        ImageViewCircleProfile.requestLayout();

        Root.addView(ImageViewCircleProfile);

        ShapeFollowWhite = new GradientDrawable();
        ShapeFollowWhite.setShape(GradientDrawable.OVAL);
        ShapeFollowWhite.setColor(Color.WHITE);
        ShapeFollowWhite.setStroke(MiscHandler.ToDimension(context, 2), Color.parseColor("#09000000"));

        ShapeFollowBlue = new GradientDrawable();
        ShapeFollowBlue.setShape(GradientDrawable.OVAL);
        ShapeFollowBlue.setColor(Color.parseColor("#1da1f2"));
        ShapeFollowBlue.setStroke(MiscHandler.ToDimension(context, 2), Color.parseColor("#09000000"));

        ImageViewEdit = new ImageView(context);
        ImageViewEdit.setLayoutParams(new CoordinatorLayout.LayoutParams(MiscHandler.ToDimension(context, 50), MiscHandler.ToDimension(context, 50)));
        ImageViewEdit.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewEdit.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
        ImageViewEdit.setBackground(ShapeFollowWhite);
        ImageViewEdit.setImageResource(R.drawable.ic_setting_black);
        ImageViewEdit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getActivity().startActivity(new Intent(context, ActivityProfileEdit.class));
                getActivity().finish();
            }
        });

        CoordinatorLayout.LayoutParams ImageViewEditParam = (CoordinatorLayout.LayoutParams) ImageViewEdit.getLayoutParams();
        ImageViewEditParam.anchorGravity = Gravity.BOTTOM | Gravity.END;
        ImageViewEditParam.rightMargin = MiscHandler.ToDimension(context, 10);
        ImageViewEditParam.setAnchorId(AppBar.getId());

        ImageViewEdit.setLayoutParams(ImageViewEditParam);
        ImageViewEdit.requestLayout();

        Root.addView(ImageViewEdit);

        LoadingViewFollow = new LoadingView(context);
        LoadingViewFollow.setLayoutParams(new CoordinatorLayout.LayoutParams(MiscHandler.ToDimension(context, 50), MiscHandler.ToDimension(context, 50)));
        LoadingViewFollow.setVisibility(View.GONE);
        LoadingViewFollow.setBackground(ShapeFollowWhite);
        LoadingViewFollow.SetColor(R.color.BlueLight);
        LoadingViewFollow.SetSize(4);

        CoordinatorLayout.LayoutParams LoadingViewFollowParam = (CoordinatorLayout.LayoutParams) LoadingViewFollow.getLayoutParams();
        LoadingViewFollowParam.anchorGravity = Gravity.BOTTOM | Gravity.END;
        LoadingViewFollowParam.rightMargin = MiscHandler.ToDimension(context, 10);
        LoadingViewFollowParam.setAnchorId(AppBar.getId());

        LoadingViewFollow.setLayoutParams(LoadingViewFollowParam);
        LoadingViewFollow.requestLayout();

        Root.addView(LoadingViewFollow);

        ImageViewFollow = new ImageView(context);
        ImageViewFollow.setLayoutParams(new CoordinatorLayout.LayoutParams(MiscHandler.ToDimension(context, 50), MiscHandler.ToDimension(context, 50)));
        ImageViewFollow.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewFollow.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
        ImageViewFollow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ImageViewFollow.setVisibility(View.GONE);
                LoadingViewFollow.Start();

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

        CoordinatorLayout.LayoutParams ImageViewFollowParam = (CoordinatorLayout.LayoutParams) ImageViewFollow.getLayoutParams();
        ImageViewFollowParam.anchorGravity = Gravity.BOTTOM | Gravity.END;
        ImageViewFollowParam.rightMargin = MiscHandler.ToDimension(context, 10);
        ImageViewFollowParam.setAnchorId(AppBar.getId());

        ImageViewFollow.setLayoutParams(ImageViewFollowParam);
        ImageViewFollow.requestLayout();

        Root.addView(ImageViewFollow);

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
        TextViewTry.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { RetrieveDataFromServer(context); } });

        RelativeLayoutLoading.addView(TextViewTry);

        RetrieveDataFromServer(context);

        return Root;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        AndroidNetworking.cancel("FragmentProfile");
    }

    private void ChangeTab(Context context, int tab)
    {
        switch (tab)
        {
            case 1: TextViewDetailTool.setText((PostCount + " " + getString(R.string.FragmentProfilePostCount))); break;
            case 2: TextViewDetailTool.setText((CommentCount + " " + getString(R.string.FragmentProfileCommentCount))); break;
            case 3: TextViewDetailTool.setText((LikeCount + " " + getString(R.string.FragmentProfileLikeCount))); break;
        }

        TextViewTabPost.setTextColor(ContextCompat.getColor(context, R.color.Gray7));
        ViewTabPost.setBackgroundResource(R.color.White);
        TextViewTabComment.setTextColor(ContextCompat.getColor(context, R.color.Gray7));
        ViewTabComment.setBackgroundResource(R.color.White);
        TextViewTabLike.setTextColor(ContextCompat.getColor(context, R.color.Gray7));
        ViewTabLike.setBackgroundResource(R.color.White);

        Fragment SelectedFragment = new FragmentProfilePost();

        switch (tab)
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

        Bundle bundle = new Bundle();
        bundle.putString("Username", Username);

        SelectedFragment.setArguments(bundle);

        getChildFragmentManager().beginTransaction().replace(FrameLayoutID, SelectedFragment).commit();
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

                        TextViewUsernameTool.setText(Data.getString("Username"));

                        if (Data.getBoolean("Self"))
                        {
                            Self = true;
                            ImageViewBack.setVisibility(View.GONE);
                            SharedHandler.SetString(context, "Avatar", Data.getString("Avatar"));
                            SharedHandler.SetString(context, "Username", Data.getString("Username"));

                            RelativeLayout.LayoutParams TextViewUsernameToolParam = (RelativeLayout.LayoutParams) TextViewUsernameTool.getLayoutParams();
                            TextViewUsernameToolParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 5), 0, 0);

                            TextViewUsernameTool.setLayoutParams(TextViewUsernameToolParam);
                            TextViewUsernameTool.requestLayout();

                            RelativeLayout.LayoutParams TextViewDetailToolParam = (RelativeLayout.LayoutParams) TextViewDetailTool.getLayoutParams();
                            TextViewDetailToolParam.setMargins(MiscHandler.ToDimension(context, 15), 0, 0, 0);

                            TextViewDetailTool.setLayoutParams(TextViewDetailToolParam);
                            TextViewDetailTool.requestLayout();
                        }
                        else
                        {
                            ImageViewEdit.setVisibility(View.GONE);
                            LoadingViewFollow.setVisibility(View.VISIBLE);
                            ImageViewFollow.setVisibility(View.VISIBLE);

                            if (Data.getBoolean("Follow"))
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
                        {
                            Glide.with(context).load(Data.getString("Cover")).asBitmap().placeholder(R.color.BlueLight).dontAnimate().into(new SimpleTarget<Bitmap>()
                            {
                                @Override
                                public void onResourceReady(Bitmap bitmap, GlideAnimation anim)
                                {
                                    ImageViewCover.setImageBitmap(bitmap);
                                    ImageViewCoverLayer.setImageBitmap(MiscHandler.Blurry(bitmap, 25));
                                }
                            });
                        }

                        TextViewUsername.setText(Data.getString("Username"));

                        if (!Data.getString("Description").equals(""))
                        {
                            TextViewDescription.setText(Data.getString("Description"));
                            TextViewDescription.setVisibility(View.VISIBLE);
                        }

                        if (!Data.getString("Link").equals(""))
                        {
                            TextViewUrl.setText(Data.getString("Link"));
                            LinearLayoutUrl.setVisibility(View.VISIBLE);
                        }

                        if (!Data.getString("Location").equals(""))
                        {
                            TextViewLocation.setText(Data.getString("Location"));
                            LinearLayoutLocation.setVisibility(View.VISIBLE);
                        }

                        TextViewPostCount.setText(Data.getString("Post"));
                        TextViewFollowingCount.setText(Data.getString("Following"));
                        TextViewFollowerCount.setText(Data.getString("Follower"));

                        PostCount = Data.getString("Post");
                        CommentCount = Data.getString("Comment");
                        LikeCount = Data.getString("Like");
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
