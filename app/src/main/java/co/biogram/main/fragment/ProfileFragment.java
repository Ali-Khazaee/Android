package co.biogram.main.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import co.biogram.main.R;
import co.biogram.main.handler.MiscHandler;
import co.biogram.main.handler.SharedHandler;
import co.biogram.main.misc.AdapterPost;
import co.biogram.main.misc.ImageViewCircle;
import co.biogram.main.misc.LinearLayoutManager2;
import co.biogram.main.misc.LoadingView;
import co.biogram.main.misc.ScrollViewSticky;

public class ProfileFragment extends Fragment
{
    private final List<AdapterPost.Struct> PostListLike = new ArrayList<>();
    private final List<AdapterPost.Struct> PostList = new ArrayList<>();
    private final List<StructComment> CommentList = new ArrayList<>();

    private AdapterComment CommentAdapter;
    private AdapterPost PostAdapter;
    private AdapterPost LikeAdapter;

    private int CurrentTab = 0;

    private String PostCount;
    private String CommentCount;
    private String LikeCount;

    private boolean IsPrivate = false;
    private boolean IsLoading = false;

    private TextView TextViewTabPost;
    private View ViewTabPost;
    private TextView TextViewTabComment;
    private View ViewTabComment;
    private TextView TextViewTabLike;
    private View ViewTabLike;
    private TextView TextViewUsername;
    private TextView TextViewDescription;
    private TextView TextViewUrl;
    private TextView TextViewLocation;
    private LinearLayout LinearLayoutLocation;
    private LinearLayout LinearLayoutUrl;
    private TextView TextViewPostCount;
    private TextView TextViewFollowerCount;
    private TextView TextViewFollowingCount;
    private ImageView ImageViewEdit;
    private LoadingView LoadingViewFollow;
    private ImageView ImageViewFollow;
    private ImageView ImageViewCover;
    private TextView TextViewDetailTool;
    private TextView TextViewUsernameTool;
    private ImageView ImageViewCoverLayer;
    private ImageViewCircle ImageViewCircleProfile;
    private RecyclerView RecyclerViewMain;

    private GradientDrawable ShapeFollowWhite;
    private GradientDrawable ShapeFollowBlue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        MiscHandler.HideSoftKey(getActivity());

        final Context context = getActivity();
        final String Username;

        if (getArguments() != null && !getArguments().getString("Username", "").equals(""))
            Username = getArguments().getString("Username");
        else
            Username = SharedHandler.GetString(context, "Username");

        CoordinatorLayout CoordinatorLayoutMain = new CoordinatorLayout(context);
        CoordinatorLayoutMain.setLayoutParams(new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.MATCH_PARENT));
        CoordinatorLayoutMain.setBackgroundResource(R.color.White);
        CoordinatorLayoutMain.setClickable(true);

        AppBarLayout AppBarLayoutMain = new AppBarLayout(context);
        AppBarLayoutMain.setLayoutParams(new AppBarLayout.LayoutParams(AppBarLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 160)));
        AppBarLayoutMain.setId(MiscHandler.GenerateViewID());
        AppBarLayoutMain.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener()
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

                    ScaleAnimation Fade2 = new ScaleAnimation(1, 0, 1, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    Fade2.setDuration(200);

                    if (IsPrivate)
                    {
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

                    ScaleAnimation Fade2 = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    Fade2.setDuration(200);

                    if (IsPrivate)
                    {
                        ImageViewEdit.setVisibility(View.VISIBLE);

                        AnimationSet FadeAnim2 = new AnimationSet(true);
                        FadeAnim2.addAnimation(Fade2);

                        ImageViewEdit.startAnimation(FadeAnim2);
                    }
                    else
                    {
                        LoadingViewFollow.setVisibility(View.VISIBLE);
                        ImageViewFollow.setVisibility(View.VISIBLE);

                        AnimationSet FadeAnim2 = new AnimationSet(true);
                        FadeAnim2.addAnimation(Fade2);

                        LoadingViewFollow.startAnimation(FadeAnim2);
                        ImageViewFollow.startAnimation(FadeAnim2);
                    }
                }
            }
        });

        if (Build.VERSION.SDK_INT > 20)
            AppBarLayoutMain.setOutlineProvider(null);

        CoordinatorLayoutMain.addView(AppBarLayoutMain);

        CollapsingToolbarLayout CollapsingToolbarLayoutMain = new CollapsingToolbarLayout(context)
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
        CollapsingToolbarLayoutMain.setLayoutParams(new AppBarLayout.LayoutParams(AppBarLayout.LayoutParams.MATCH_PARENT, AppBarLayout.LayoutParams.MATCH_PARENT));
        CollapsingToolbarLayoutMain.setScrimVisibleHeightTrigger(MiscHandler.ToDimension(context, 57));

        AppBarLayout.LayoutParams CollapsingToolbarLayoutMainParam = (AppBarLayout.LayoutParams) CollapsingToolbarLayoutMain.getLayoutParams();
        CollapsingToolbarLayoutMainParam.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);

        AppBarLayoutMain.addView(CollapsingToolbarLayoutMain);

        CollapsingToolbarLayout.LayoutParams ImageViewCoverParam = new CollapsingToolbarLayout.LayoutParams(CollapsingToolbarLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 160));
        ImageViewCoverParam.setCollapseMode(CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PARALLAX);

        ImageViewCover = new ImageView(context);
        ImageViewCover.setLayoutParams(ImageViewCoverParam);
        ImageViewCover.setScaleType(ImageView.ScaleType.FIT_XY);
        ImageViewCover.setImageResource(R.color.BlueLight2);

        CollapsingToolbarLayoutMain.addView(ImageViewCover);

        CollapsingToolbarLayout.LayoutParams ImageViewCoverLayerParam = new CollapsingToolbarLayout.LayoutParams(CollapsingToolbarLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 160));
        ImageViewCoverLayerParam.setCollapseMode(CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PARALLAX);

        ImageViewCoverLayer = new ImageView(context);
        ImageViewCoverLayer.setLayoutParams(ImageViewCoverLayerParam);
        ImageViewCoverLayer.setScaleType(ImageView.ScaleType.FIT_XY);
        ImageViewCoverLayer.setVisibility(View.GONE);

        CollapsingToolbarLayoutMain.addView(ImageViewCoverLayer);

        final ImageView ImageViewCoverLayer2 = new ImageView(context);
        ImageViewCoverLayer2.setLayoutParams(new CollapsingToolbarLayout.LayoutParams(CollapsingToolbarLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 160)));
        ImageViewCoverLayer2.setBackgroundColor(Color.parseColor("#50000000"));
        ImageViewCoverLayer2.setVisibility(View.GONE);

        CollapsingToolbarLayoutMain.addView(ImageViewCoverLayer2);

        CollapsingToolbarLayout.LayoutParams ToolbarMainParam = new CollapsingToolbarLayout.LayoutParams(CollapsingToolbarLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56));
        ToolbarMainParam.setCollapseMode(CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PIN);

        Toolbar ToolbarMain = new Toolbar(context);
        ToolbarMain.setLayoutParams(ToolbarMainParam);
        ToolbarMain.setContentInsetsAbsolute(0, 0);

        CollapsingToolbarLayoutMain.addView(ToolbarMain);

        RelativeLayout RelativeLayoutToolBar = new RelativeLayout(context);
        RelativeLayoutToolBar.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

        ToolbarMain.addView(RelativeLayoutToolBar);

        ImageView ImageViewBack = new ImageView(context);
        ImageViewBack.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), RelativeLayout.LayoutParams.MATCH_PARENT));
        ImageViewBack.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewBack.setPadding(MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12));
        ImageViewBack.setImageResource(R.drawable.ic_back_white);
        ImageViewBack.setId(MiscHandler.GenerateViewID());
        ImageViewBack.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { getActivity().onBackPressed(); } });

        if (getArguments() != null && getArguments().getBoolean("HideBack", false))
            ImageViewBack.setVisibility(View.GONE);

        RelativeLayoutToolBar.addView(ImageViewBack);

        RelativeLayout.LayoutParams ImageViewMoreParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), RelativeLayout.LayoutParams.MATCH_PARENT);
        ImageViewMoreParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        ImageView ImageViewMore = new ImageView(context);
        ImageViewMore.setLayoutParams(ImageViewMoreParam);
        ImageViewMore.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewMore.setPadding(MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12));
        ImageViewMore.setImageResource(R.drawable.ic_more_white);
        ImageViewMore.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (!IsPrivate)
                {
                    final Dialog DialogMore = new Dialog(getActivity());
                    DialogMore.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    DialogMore.setCancelable(true);

                    LinearLayout LinearLayoutMain = new LinearLayout(context);
                    LinearLayoutMain.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    LinearLayoutMain.setBackgroundResource(R.color.White);
                    LinearLayoutMain.setOrientation(LinearLayout.VERTICAL);

                    TextView TextViewReport = new TextView(context);
                    TextViewReport.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    TextViewReport.setTextColor(ContextCompat.getColor(context, R.color.Black));
                    TextViewReport.setText(getString(R.string.ProfileFragmentReport));
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

                    LinearLayoutMain.addView(TextViewReport);

                    DialogMore.setContentView(LinearLayoutMain);
                    DialogMore.show();
                    return;
                }

                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.MainActivityFullContainer, new SettingFragment()).addToBackStack("SettingFragment").commitAllowingStateLoss();
            }
        });

        RelativeLayoutToolBar.addView(ImageViewMore);

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

        RelativeLayoutToolBar.addView(TextViewUsernameTool);

        RelativeLayout.LayoutParams TextViewDetailToolParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewDetailToolParam.addRule(RelativeLayout.RIGHT_OF, ImageViewBack.getId());
        TextViewDetailToolParam.addRule(RelativeLayout.BELOW, TextViewUsernameTool.getId());

        TextViewDetailTool = new TextView(context);
        TextViewDetailTool.setLayoutParams(TextViewDetailToolParam);
        TextViewDetailTool.setTextColor(ContextCompat.getColor(context, R.color.White5));
        TextViewDetailTool.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        TextViewDetailTool.setVisibility(View.GONE);

        RelativeLayoutToolBar.addView(TextViewDetailTool);

        ScrollViewSticky ScrollViewStickyMain = new ScrollViewSticky(context);
        ScrollViewStickyMain.setLayoutParams(new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.MATCH_PARENT));
        ScrollViewStickyMain.setFillViewport(true);
        ScrollViewStickyMain.setOnScrollChangeListener(new ScrollViewSticky.OnScrollChangeListener()
        {
            private boolean Hidden = false;

            @Override
            public void onScrollChange(NestedScrollView view, int X, int Y, int OldX, int OldY)
            {
                if (view.getChildAt(view.getChildCount() - 1) != null)
                {
                    if ((Y >= (view.getChildAt(view.getChildCount() - 1).getMeasuredHeight() - view.getMeasuredHeight())) && Y > OldY)
                    {
                        if (!IsLoading)
                        {
                            IsLoading = true;
                            RetrieveDataFor(context, Username);
                        }
                    }
                }

                Rect rect = new Rect();
                view.getHitRect(rect);

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

        CoordinatorLayout.LayoutParams ScrollViewStickyMainParam = (CoordinatorLayout.LayoutParams) ScrollViewStickyMain.getLayoutParams();
        ScrollViewStickyMainParam.setBehavior(new AppBarLayout.ScrollingViewBehavior());

        CoordinatorLayoutMain.addView(ScrollViewStickyMain);

        RelativeLayout RelativeLayoutMain = new RelativeLayout(context);
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

        ScrollViewStickyMain.addView(RelativeLayoutMain);

        View ViewLine = new View(context);
        ViewLine.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 5)));
        ViewLine.setBackgroundResource(R.color.Gray);
        ViewLine.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain.addView(ViewLine);

        RelativeLayout.LayoutParams LinearLayoutMainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        LinearLayoutMainParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

        LinearLayout LinearLayoutMain = new LinearLayout(context);
        LinearLayoutMain.setLayoutParams(LinearLayoutMainParam);
        LinearLayoutMain.setBackgroundResource(R.color.White);
        LinearLayoutMain.setId(MiscHandler.GenerateViewID());
        LinearLayoutMain.setOrientation(LinearLayout.VERTICAL);

        RelativeLayoutMain.addView(LinearLayoutMain);

        TextViewUsername = new TextView(context);
        TextViewUsername.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewUsername.setTextColor(ContextCompat.getColor(context, R.color.Black));
        TextViewUsername.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 45), MiscHandler.ToDimension(context, 15), 0);
        TextViewUsername.setTypeface(null, Typeface.BOLD);
        TextViewUsername.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

        LinearLayoutMain.addView(TextViewUsername);

        TextViewDescription = new TextView(context);
        TextViewDescription.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewDescription.setTextColor(ContextCompat.getColor(context, R.color.Black4));
        TextViewDescription.setPadding(MiscHandler.ToDimension(context, 15), 0, MiscHandler.ToDimension(context, 15), 0);
        TextViewDescription.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        TextViewDescription.setLineSpacing(5, 1);
        TextViewDescription.setVisibility(View.GONE);

        LinearLayoutMain.addView(TextViewDescription);

        LinearLayoutUrl = new LinearLayout(context);
        LinearLayoutUrl.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        LinearLayoutUrl.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 5), 0, 0);
        LinearLayoutUrl.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayoutUrl.setVisibility(View.GONE);

        LinearLayoutMain.addView(LinearLayoutUrl);

        ImageView ImageViewUrl = new ImageView(context);
        ImageViewUrl.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 24), MiscHandler.ToDimension(context, 24)));
        ImageViewUrl.setImageResource(R.drawable.ic_link_black);

        LinearLayoutUrl.addView(ImageViewUrl);

        TextViewUrl = new TextView(getActivity());
        TextViewUrl.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewUrl.setTextColor(ContextCompat.getColor(context, R.color.BlueLight));
        TextViewUrl.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        TextViewUrl.setPadding(MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 1), 0, 0);
        TextViewUrl.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(TextViewUrl.getText().toString()));
                startActivity(i);
            }
        });

        LinearLayoutUrl.addView(TextViewUrl);

        LinearLayoutLocation = new LinearLayout(context);
        LinearLayoutLocation.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        LinearLayoutLocation.setPadding(MiscHandler.ToDimension(context, 15), 0, 0, 0);
        LinearLayoutLocation.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayoutLocation.setVisibility(View.GONE);

        LinearLayoutMain.addView(LinearLayoutLocation);

        ImageView ImageViewLocation = new ImageView(context);
        ImageViewLocation.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 24), MiscHandler.ToDimension(context, 24)));
        ImageViewLocation.setImageResource(R.drawable.ic_location_black);
        ImageViewLocation.setPadding(MiscHandler.ToDimension(context, 2), MiscHandler.ToDimension(context, 2), MiscHandler.ToDimension(context, 2), MiscHandler.ToDimension(context, 2));

        LinearLayoutLocation.addView(ImageViewLocation);

        TextViewLocation = new TextView(getActivity());
        TextViewLocation.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewLocation.setTextColor(ContextCompat.getColor(context, R.color.Gray5));
        TextViewLocation.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        TextViewLocation.setPadding(MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 1), 0, 0);

        LinearLayoutLocation.addView(TextViewLocation);

        LinearLayout LinearLayoutDetails = new LinearLayout(context);
        LinearLayoutDetails.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        LinearLayoutDetails.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayoutDetails.setPadding(0, MiscHandler.ToDimension(context, 15), 0, MiscHandler.ToDimension(context, 5));

        LinearLayoutMain.addView(LinearLayoutDetails);

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
        TextViewPost.setText(getString(R.string.ProfileFragmentPost));
        TextViewPost.setPadding(MiscHandler.ToDimension(context, 5), MiscHandler.ToDimension(context, 5), MiscHandler.ToDimension(context, 5), MiscHandler.ToDimension(context, 5));
        TextViewPost.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        RelativeLayoutPost.addView(TextViewPost);

        RelativeLayout RelativeLayoutFollower = new RelativeLayout(context);
        RelativeLayoutFollower.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
        RelativeLayoutFollower.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Bundle bundle = new Bundle();
                bundle.putString("Username", Username);

                Fragment fragment = new FollowersFragment();
                fragment.setArguments(bundle);

                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.MainActivityFullContainer, fragment).addToBackStack("FollowersFragment").commitAllowingStateLoss();
            }
        });

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
        TextViewFollower.setText(getString(R.string.ProfileFragmentFollowers));
        TextViewFollower.setPadding(MiscHandler.ToDimension(context, 5), MiscHandler.ToDimension(context, 5), MiscHandler.ToDimension(context, 5), MiscHandler.ToDimension(context, 5));
        TextViewFollower.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        RelativeLayoutFollower.addView(TextViewFollower);

        RelativeLayout RelativeLayoutFollowing = new RelativeLayout(context);
        RelativeLayoutFollowing.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
        RelativeLayoutFollowing.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Bundle bundle = new Bundle();
                bundle.putString("Username", Username);

                Fragment fragment = new FollowingFragment();
                fragment.setArguments(bundle);

                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.MainActivityFullContainer, fragment).addToBackStack("FollowingFragment").commitAllowingStateLoss();
            }
        });

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
        TextViewFollowing.setText(getString(R.string.ProfileFragmentFollowing));
        TextViewFollowing.setPadding(MiscHandler.ToDimension(context, 5), MiscHandler.ToDimension(context, 5), MiscHandler.ToDimension(context, 5), MiscHandler.ToDimension(context, 5));
        TextViewFollowing.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        RelativeLayoutFollowing.addView(TextViewFollowing);

        View ViewLine2 = new View(context);
        ViewLine2.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 5)));
        ViewLine2.setBackgroundResource(R.color.Gray);

        LinearLayoutMain.addView(ViewLine2);

        LinearLayout LinearLayoutTab = new LinearLayout(context);
        LinearLayoutTab.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 57)));
        LinearLayoutTab.setOrientation(LinearLayout.VERTICAL);
        LinearLayoutTab.setTag("sticky");

        LinearLayoutMain.addView(LinearLayoutTab);

        LinearLayout LinearLayoutTab2 = new LinearLayout(context);
        LinearLayoutTab2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56)));

        LinearLayoutTab.addView(LinearLayoutTab2);

        RelativeLayout RelativeLayoutTabPost = new RelativeLayout(context);
        RelativeLayoutTabPost.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
        RelativeLayoutTabPost.setBackgroundResource(R.color.White);
        RelativeLayoutTabPost.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { ChangeTab(context, 1, Username); } });

        LinearLayoutTab2.addView(RelativeLayoutTabPost);

        RelativeLayout.LayoutParams TextViewTabPostParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTabPostParam.addRule(RelativeLayout.CENTER_HORIZONTAL);

        TextViewTabPost = new TextView(context);
        TextViewTabPost.setLayoutParams(TextViewTabPostParam);
        TextViewTabPost.setTextColor(ContextCompat.getColor(context, R.color.BlueLight));
        TextViewTabPost.setText(getString(R.string.ProfileFragmentPost2));
        TextViewTabPost.setPadding(0, MiscHandler.ToDimension(context, 15), 0, MiscHandler.ToDimension(context, 15));
        TextViewTabPost.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        TextViewTabPost.setTypeface(null, Typeface.BOLD);

        RelativeLayoutTabPost.addView(TextViewTabPost);

        RelativeLayout.LayoutParams ViewTabPostParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, MiscHandler.ToDimension(context, 2));
        ViewTabPostParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        ViewTabPost = new View(context);
        ViewTabPost.setLayoutParams(ViewTabPostParam);
        ViewTabPost.setBackgroundResource(R.color.BlueLight);

        RelativeLayoutTabPost.addView(ViewTabPost);

        RelativeLayout RelativeLayoutTabComment = new RelativeLayout(context);
        RelativeLayoutTabComment.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
        RelativeLayoutTabComment.setBackgroundResource(R.color.White);
        RelativeLayoutTabComment.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { ChangeTab(context, 2, Username); } });

        LinearLayoutTab2.addView(RelativeLayoutTabComment);

        RelativeLayout.LayoutParams TextViewTabCommentParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTabCommentParam.addRule(RelativeLayout.CENTER_HORIZONTAL);

        TextViewTabComment = new TextView(context);
        TextViewTabComment.setLayoutParams(TextViewTabCommentParam);
        TextViewTabComment.setTextColor(ContextCompat.getColor(context, R.color.BlueLight));
        TextViewTabComment.setText(getString(R.string.ProfileFragmentComment));
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
        RelativeLayoutTabLike.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { ChangeTab(context, 3, Username); } });

        LinearLayoutTab2.addView(RelativeLayoutTabLike);

        RelativeLayout.LayoutParams TextViewTabLikeParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTabLikeParam.addRule(RelativeLayout.CENTER_HORIZONTAL);

        TextViewTabLike = new TextView(context);
        TextViewTabLike.setLayoutParams(TextViewTabLikeParam);
        TextViewTabLike.setTextColor(ContextCompat.getColor(context, R.color.BlueLight));
        TextViewTabLike.setText(getString(R.string.ProfileFragmentLike));
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

        View ViewLine3 = new View(context);
        ViewLine3.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1)));
        ViewLine3.setBackgroundResource(R.color.Gray);

        LinearLayoutTab.addView(ViewLine3);

        RecyclerViewMain = new RecyclerView(context);
        RecyclerViewMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RecyclerViewMain.setLayoutManager(new LinearLayoutManager2(context));
        RecyclerViewMain.setNestedScrollingEnabled(false);

        LinearLayoutMain.addView(RecyclerViewMain);

        CoordinatorLayout.LayoutParams ImageViewCircleProfileParam = new CoordinatorLayout.LayoutParams(MiscHandler.ToDimension(context, 90), MiscHandler.ToDimension(context, 90));
        ImageViewCircleProfileParam.anchorGravity = Gravity.BOTTOM | Gravity.START;
        ImageViewCircleProfileParam.leftMargin = MiscHandler.ToDimension(context, 10);
        ImageViewCircleProfileParam.setAnchorId(AppBarLayoutMain.getId());

        ImageViewCircleProfile = new ImageViewCircle(context);
        ImageViewCircleProfile.SetBorderWidth(MiscHandler.ToDimension(context, 3));
        ImageViewCircleProfile.setLayoutParams(ImageViewCircleProfileParam);
        ImageViewCircleProfile.setImageResource(R.color.BlueGray);

        CoordinatorLayoutMain.addView(ImageViewCircleProfile);

        CoordinatorLayout.LayoutParams RelativeLayoutFABParam = new CoordinatorLayout.LayoutParams(MiscHandler.ToDimension(context, 50), MiscHandler.ToDimension(context, 50));
        RelativeLayoutFABParam.anchorGravity = Gravity.BOTTOM | Gravity.END;
        RelativeLayoutFABParam.rightMargin = MiscHandler.ToDimension(context, 10);
        RelativeLayoutFABParam.setAnchorId(AppBarLayoutMain.getId());

        ShapeFollowWhite = new GradientDrawable();
        ShapeFollowWhite.setShape(GradientDrawable.OVAL);
        ShapeFollowWhite.setColor(Color.WHITE);
        ShapeFollowWhite.setStroke(MiscHandler.ToDimension(context, 2), Color.parseColor("#09000000"));

        ShapeFollowBlue = new GradientDrawable();
        ShapeFollowBlue.setShape(GradientDrawable.OVAL);
        ShapeFollowBlue.setColor(Color.parseColor("#1da1f2"));
        ShapeFollowBlue.setStroke(MiscHandler.ToDimension(context, 2), Color.parseColor("#09000000"));

        RelativeLayout RelativeLayoutFAB = new RelativeLayout(context);
        RelativeLayoutFAB.setLayoutParams(RelativeLayoutFABParam);
        RelativeLayoutFAB.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (IsPrivate)
                {
                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.MainActivityFullContainer, new EditFragment(), "EditFragment").addToBackStack("EditFragment").commitAllowingStateLoss();
                    return;
                }

                ImageViewFollow.setVisibility(View.GONE);
                LoadingViewFollow.Start();

                AndroidNetworking.post(MiscHandler.GetRandomServer("Follow"))
                .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
                .addBodyParameter("Username", Username)
                .setTag("ProfileFragment")
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
                                    MiscHandler.Toast(context, getString(R.string.ProfileFragmentFollow));
                                }
                                else
                                {
                                    ImageViewFollow.setImageResource(R.drawable.ic_follow);
                                    ImageViewFollow.setBackground(ShapeFollowBlue);
                                    MiscHandler.Toast(context, getString(R.string.ProfileFragmentUnFollow));
                                }
                            }
                        }
                        catch (Exception e)
                        {
                            MiscHandler.Debug("ProfileFragment-RequestFollow: " + e.toString());
                        }

                        LoadingViewFollow.Stop();
                        ImageViewFollow.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(ANError anError)
                    {
                        LoadingViewFollow.Stop();
                        ImageViewFollow.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

        CoordinatorLayoutMain.addView(RelativeLayoutFAB);

        ImageViewEdit = new ImageView(context);
        ImageViewEdit.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 50), MiscHandler.ToDimension(context, 50)));
        ImageViewEdit.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewEdit.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
        ImageViewEdit.setBackground(ShapeFollowWhite);
        ImageViewEdit.setImageResource(R.drawable.ic_setting_black);

        RelativeLayoutFAB.addView(ImageViewEdit);

        LoadingViewFollow = new LoadingView(context);
        LoadingViewFollow.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 50), MiscHandler.ToDimension(context, 50)));
        LoadingViewFollow.setVisibility(View.GONE);
        LoadingViewFollow.setBackground(ShapeFollowWhite);
        LoadingViewFollow.SetColor(R.color.BlueLight);
        LoadingViewFollow.SetSize(4);

        RelativeLayoutFAB.addView(LoadingViewFollow);

        ImageViewFollow = new ImageView(context);
        ImageViewFollow.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 50), MiscHandler.ToDimension(context, 50)));
        ImageViewFollow.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewFollow.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));

        RelativeLayoutFAB.addView(ImageViewFollow);

        final RelativeLayout RelativeLayoutLoading = new RelativeLayout(context);
        RelativeLayoutLoading.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayoutLoading.setBackgroundResource(R.color.White);
        RelativeLayoutLoading.setClickable(true);

        CoordinatorLayoutMain.addView(RelativeLayoutLoading);

        RelativeLayout.LayoutParams LoadingViewDataParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), MiscHandler.ToDimension(context, 56));
        LoadingViewDataParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        final LoadingView LoadingViewMain = new LoadingView(context);
        LoadingViewMain.setLayoutParams(LoadingViewDataParam);

        RelativeLayoutLoading.addView(LoadingViewMain);

        RelativeLayout.LayoutParams TextViewTryAgainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTryAgainParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        final TextView TextViewTryAgain = new TextView(context);
        TextViewTryAgain.setLayoutParams(TextViewTryAgainParam);
        TextViewTryAgain.setTextColor(ContextCompat.getColor(context, R.color.BlueGray2));
        TextViewTryAgain.setText(getString(R.string.TryAgain));
        TextViewTryAgain.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        TextViewTryAgain.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { RetrieveDataFromServer(context, Username, RelativeLayoutLoading, LoadingViewMain, TextViewTryAgain); } });

        RelativeLayoutLoading.addView(TextViewTryAgain);

        RetrieveDataFromServer(context, Username, RelativeLayoutLoading, LoadingViewMain, TextViewTryAgain);

        PostAdapter = new AdapterPost(getActivity(), PostList, "ProfileFragment");
        CommentAdapter = new AdapterComment(context);
        LikeAdapter = new AdapterPost(getActivity(), PostListLike, "ProfileFragment");

        return CoordinatorLayoutMain;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        AndroidNetworking.forceCancel("ProfileFragment");
    }

    public void Update(final Context context, final String Username)
    {
        AndroidNetworking.post(MiscHandler.GetRandomServer("ProfileGet"))
        .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
        .addBodyParameter("Username", Username)
        .setTag("ProfileFragment")
        .build()
        .getAsString(new StringRequestListener()
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
                            IsPrivate = true;
                            SharedHandler.SetString(context, "Avatar", Data.getString("Avatar"));
                            SharedHandler.SetString(context, "Username", Data.getString("Username"));

                            RelativeLayout.LayoutParams TextViewUsernameToolParam = (RelativeLayout.LayoutParams) TextViewUsernameTool.getLayoutParams();
                            TextViewUsernameToolParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 5), 0, 0);

                            RelativeLayout.LayoutParams TextViewDetailToolParam = (RelativeLayout.LayoutParams) TextViewDetailTool.getLayoutParams();
                            TextViewDetailToolParam.setMargins(MiscHandler.ToDimension(context, 15), 0, 0, 0);
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
                        {
                            Glide.with(context)
                            .load(Data.getString("Avatar"))
                            .placeholder(R.color.BlueGray)
                            .dontAnimate()
                            .into(ImageViewCircleProfile);
                        }

                        if (!Data.getString("Cover").equals(""))
                        {
                            Glide.with(context)
                            .load(Data.getString("Cover"))
                            .asBitmap()
                            .placeholder(R.color.BlueLight)
                            .dontAnimate()
                            .into(new SimpleTarget<Bitmap>()
                            {
                                @Override
                                public void onResourceReady(Bitmap bitmap, GlideAnimation anim)
                                {
                                    ImageViewCover.setImageBitmap(bitmap);
                                    ImageViewCoverLayer.setImageBitmap(MiscHandler.Blurry(bitmap));
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
                }
                catch (Exception e)
                {
                    MiscHandler.Debug("ProfileFragment-RequestNew: " + e.toString());
                }
            }

            @Override public void onError(ANError anError) { }
        });
    }

    private void RetrieveDataFromServer(final Context context, final String Username, final RelativeLayout RelativeLayoutLoading, final LoadingView LoadingViewMain, final TextView TextViewTryAgain)
    {
        TextViewTryAgain.setVisibility(View.GONE);
        LoadingViewMain.Start();

        AndroidNetworking.post(MiscHandler.GetRandomServer("ProfileGet"))
        .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
        .addBodyParameter("Username", Username)
        .setTag("ProfileFragment")
        .build()
        .getAsString(new StringRequestListener()
        {
            @Override
            public void onResponse(String Response)
            {MiscHandler.Debug("QQ: " + Response);
                try
                {
                    JSONObject Result = new JSONObject(Response);

                    if (Result.getInt("Message") == 1000 && !Result.getString("Result").equals(""))
                    {
                        JSONObject Data = new JSONObject(Result.getString("Result"));

                        TextViewUsernameTool.setText(Data.getString("Username"));

                        if (Data.getBoolean("Self"))
                        {
                            IsPrivate = true;
                            SharedHandler.SetString(context, "Avatar", Data.getString("Avatar"));
                            SharedHandler.SetString(context, "Username", Data.getString("Username"));

                            RelativeLayout.LayoutParams TextViewUsernameToolParam = (RelativeLayout.LayoutParams) TextViewUsernameTool.getLayoutParams();
                            TextViewUsernameToolParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 5), 0, 0);

                            RelativeLayout.LayoutParams TextViewDetailToolParam = (RelativeLayout.LayoutParams) TextViewDetailTool.getLayoutParams();
                            TextViewDetailToolParam.setMargins(MiscHandler.ToDimension(context, 15), 0, 0, 0);
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
                        {
                            Glide.with(context)
                            .load(Data.getString("Avatar"))
                            .placeholder(R.color.BlueGray)
                            .dontAnimate()
                            .into(ImageViewCircleProfile);
                        }

                        if (!Data.getString("Cover").equals(""))
                        {
                            Glide.with(context)
                            .load(Data.getString("Cover"))
                            .asBitmap()
                            .placeholder(R.color.BlueLight)
                            .dontAnimate()
                            .into(new SimpleTarget<Bitmap>()
                            {
                                @Override
                                public void onResourceReady(Bitmap bitmap, GlideAnimation anim)
                                {
                                    ImageViewCover.setImageBitmap(bitmap);
                                    ImageViewCoverLayer.setImageBitmap(MiscHandler.Blurry(bitmap));
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
                }
                catch (Exception e)
                {
                    MiscHandler.Debug("ProfileFragment-RequestNew: " + e.toString());
                }

                if (isVisible())
                    ChangeTab(context, 1, Username);

                LoadingViewMain.Stop();
                TextViewTryAgain.setVisibility(View.GONE);
                RelativeLayoutLoading.setVisibility(View.GONE);
            }

            @Override
            public void onError(ANError anError)
            {
                LoadingViewMain.Stop();
                TextViewTryAgain.setVisibility(View.VISIBLE);
            }
        });
    }

    private void ChangeTab(Context context, int tab, String username)
    {
        switch (tab)
        {
            case 1: TextViewDetailTool.setText((PostCount + " " + getString(R.string.ProfileFragmentPostCount))); break;
            case 2: TextViewDetailTool.setText((CommentCount + " " + getString(R.string.ProfileFragmentCommentCount))); break;
            case 3: TextViewDetailTool.setText((LikeCount + " " + getString(R.string.ProfileFragmentLikeCount))); break;
        }

        CurrentTab = tab;

        TextViewTabPost.setTextColor(ContextCompat.getColor(context, R.color.Gray7));
        ViewTabPost.setBackgroundResource(R.color.White);
        TextViewTabComment.setTextColor(ContextCompat.getColor(context, R.color.Gray7));
        ViewTabComment.setBackgroundResource(R.color.White);
        TextViewTabLike.setTextColor(ContextCompat.getColor(context, R.color.Gray7));
        ViewTabLike.setBackgroundResource(R.color.White);

        switch (tab)
        {
            case 1:
                RecyclerViewMain.setAdapter(PostAdapter);
                TextViewTabPost.setTextColor(ContextCompat.getColor(context, R.color.BlueLight));
                ViewTabPost.setBackgroundResource(R.color.BlueLight);
            break;
            case 2:
                RecyclerViewMain.setAdapter(CommentAdapter);
                TextViewTabComment.setTextColor(ContextCompat.getColor(context, R.color.BlueLight));
                ViewTabComment.setBackgroundResource(R.color.BlueLight);
            break;
            case 3:
                RecyclerViewMain.setAdapter(LikeAdapter);
                TextViewTabLike.setTextColor(ContextCompat.getColor(context, R.color.BlueLight));
                ViewTabLike.setBackgroundResource(R.color.BlueLight);
            break;
        }

        RetrieveDataFor(context, username);
    }

    private void RetrieveDataFor(Context context, String Username)
    {
        switch (CurrentTab)
        {
            case 1:
            {
                PostList.add(null);
                PostAdapter.notifyDataSetChanged();

                AndroidNetworking.post(MiscHandler.GetRandomServer("ProfilePostGet"))
                .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
                .addBodyParameter("Username", Username)
                .addBodyParameter("Skip", String.valueOf(PostList.size() - 1))
                .setTag("ProfileFragment")
                .build()
                .getAsString(new StringRequestListener()
                {
                    @Override
                    public void onResponse(String Response)
                    {
                        PostList.remove(PostList.size() - 1);

                        try
                        {
                            JSONObject Result = new JSONObject(Response);

                            if (Result.getInt("Message") == 1000 && !Result.getString("Result").equals(""))
                            {
                                JSONArray ResultList = new JSONArray(Result.getString("Result"));

                                for (int K = 0; K < ResultList.length(); K++)
                                {
                                    JSONObject Post = ResultList.getJSONObject(K);

                                    AdapterPost.Struct PostStruct = new AdapterPost.Struct();
                                    PostStruct.PostID = Post.getString("PostID");
                                    PostStruct.OwnerID = Post.getString("OwnerID");
                                    PostStruct.Type = Post.getInt("Type");
                                    PostStruct.Category = Post.getInt("Category");
                                    PostStruct.Time = Post.getInt("Time");
                                    PostStruct.Comment = Post.getBoolean("Comment");
                                    PostStruct.Message = Post.getString("Message");
                                    PostStruct.Data = Post.getString("Data");
                                    PostStruct.Username = Post.getString("Username");
                                    PostStruct.Avatar = Post.getString("Avatar");
                                    PostStruct.Like = Post.getBoolean("Like");
                                    PostStruct.LikeCount = Post.getInt("LikeCount");
                                    PostStruct.CommentCount = Post.getInt("CommentCount");
                                    PostStruct.BookMark = Post.getBoolean("BookMark");
                                    PostStruct.Follow = Post.getBoolean("Follow");

                                    PostList.add(PostStruct);
                                }
                            }
                        }
                        catch (Exception e)
                        {
                            MiscHandler.Debug("ProfileFragment-RequestPost: " + e.toString());
                        }

                        IsLoading = false;
                        PostAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(ANError anError)
                    {
                        IsLoading = false;
                        PostList.remove(PostList.size() - 1);
                        PostAdapter.notifyDataSetChanged();
                    }
                });
            }
            break;
            case 2:
            {
                CommentList.add(null);
                CommentAdapter.notifyDataSetChanged();

                AndroidNetworking.post(MiscHandler.GetRandomServer("ProfileCommentGet"))
                .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
                .addBodyParameter("Username", Username)
                .addBodyParameter("Skip", String.valueOf(CommentList.size() - 1))
                .setTag("ProfileFragment")
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

                                for (int I = 0; I < ResultList.length(); I++)
                                {
                                    JSONObject Comment = ResultList.getJSONObject(I);

                                    StructComment commentStruct = new StructComment();
                                    commentStruct.PostID = Comment.getString("PostID");
                                    commentStruct.Username = Comment.getString("Username");
                                    commentStruct.Avatar = Comment.getString("Avatar");
                                    commentStruct.Target = Comment.getString("Target");
                                    commentStruct.Comment = Comment.getString("Comment");
                                    commentStruct.Time = Comment.getInt("Time");

                                    CommentList.add(commentStruct);
                                }
                            }
                        }
                        catch (Exception e)
                        {
                            MiscHandler.Debug("ProfileFragment-RequestComment: " + e.toString());
                        }

                        IsLoading = false;
                        CommentAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(ANError anError)
                    {
                        IsLoading = false;
                        CommentList.remove(CommentList.size() - 1);
                        CommentAdapter.notifyDataSetChanged();
                    }
                });
            }
            break;
            case 3:
            {
                PostListLike.add(null);
                LikeAdapter.notifyDataSetChanged();

                AndroidNetworking.post(MiscHandler.GetRandomServer("ProfileLikeGet"))
                .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
                .addBodyParameter("Username", Username)
                .addBodyParameter("Skip", String.valueOf(PostListLike.size() - 1))
                .setTag("ProfileFragment")
                .build()
                .getAsString(new StringRequestListener()
                {
                    @Override
                    public void onResponse(String Response)
                    {
                        PostListLike.remove(PostListLike.size() -1);

                        try
                        {
                            JSONObject Result = new JSONObject(Response);

                            if (Result.getInt("Message") == 1000 && !Result.getString("Result").equals(""))
                            {
                                JSONArray ResultList = new JSONArray(Result.getString("Result"));

                                for (int K = 0; K < ResultList.length(); K++)
                                {
                                    JSONObject Post = ResultList.getJSONObject(K);

                                    AdapterPost.Struct PostStruct = new AdapterPost.Struct();
                                    PostStruct.PostID = Post.getString("PostID");
                                    PostStruct.OwnerID = Post.getString("OwnerID");
                                    PostStruct.Type = Post.getInt("Type");
                                    PostStruct.Category = Post.getInt("Category");
                                    PostStruct.Time = Post.getInt("Time");
                                    PostStruct.Comment = Post.getBoolean("Comment");
                                    PostStruct.Message = Post.getString("Message");
                                    PostStruct.Data = Post.getString("Data");
                                    PostStruct.Username = Post.getString("Username");
                                    PostStruct.Avatar = Post.getString("Avatar");
                                    PostStruct.Like = Post.getBoolean("Like");
                                    PostStruct.LikeCount = Post.getInt("LikeCount");
                                    PostStruct.CommentCount = Post.getInt("CommentCount");
                                    PostStruct.BookMark = Post.getBoolean("BookMark");
                                    PostStruct.Follow = Post.getBoolean("Follow");

                                    PostListLike.add(PostStruct);
                                }
                            }
                        }
                        catch (Exception e)
                        {
                            MiscHandler.Debug("ProfileFragment-RequestLike: " + e.toString());
                        }

                        IsLoading = false;
                        LikeAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(ANError anError)
                    {
                        IsLoading = false;
                        PostListLike.remove(PostListLike.size() -1);
                        LikeAdapter.notifyDataSetChanged();
                    }
                });
            }
            break;
        }
    }

    private class AdapterComment extends RecyclerView.Adapter<AdapterComment.ViewHolderComment>
    {
        private final int ID_MAIN = MiscHandler.GenerateViewID();
        private final int ID_PROFILE = MiscHandler.GenerateViewID();
        private final int ID_MESSAGE = MiscHandler.GenerateViewID();
        private final int ID_MORE = MiscHandler.GenerateViewID();
        private final int ID_COMMENT = MiscHandler.GenerateViewID();
        private final int ID_TIME = MiscHandler.GenerateViewID();
        private final int ID_LINE = MiscHandler.GenerateViewID();

        private final Context context;

        AdapterComment(Context c)
        {
            context = c;
        }

        class ViewHolderComment extends RecyclerView.ViewHolder
        {
            private RelativeLayout RelativeLayoutMain;
            private ImageViewCircle ImageViewCircleProfile;
            private TextView TextViewMessage;
            private ImageView ImageViewMore;
            private TextView TextViewTime;
            private TextView TextViewComment;
            private View ViewLine;

            ViewHolderComment(View view, boolean Content)
            {
                super(view);

                if (Content)
                {
                    RelativeLayoutMain = (RelativeLayout) view.findViewById(ID_MAIN);
                    ImageViewCircleProfile = (ImageViewCircle) view.findViewById(ID_PROFILE);
                    TextViewMessage = (TextView) view.findViewById(ID_MESSAGE);
                    ImageViewMore = (ImageView) view.findViewById(ID_MORE);
                    TextViewTime = (TextView) view.findViewById(ID_TIME);
                    TextViewComment = (TextView) view.findViewById(ID_COMMENT);
                    ViewLine = view.findViewById(ID_LINE);
                }
            }
        }

        @Override
        public void onBindViewHolder(final ViewHolderComment Holder, int position)
        {
            if (getItemViewType(position) != 0)
                return;

            final int Position = Holder.getAdapterPosition();

            Holder.RelativeLayoutMain.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("PostID", CommentList.get(Position).PostID);

                    Fragment fragment = new PostFragment();
                    fragment.setArguments(bundle);

                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.MainActivityFullContainer, fragment).addToBackStack("PostFragment").commitAllowingStateLoss();
                }
            });

            Glide.with(context)
            .load(CommentList.get(Position).Avatar)
            .placeholder(R.color.BlueGray)
            .dontAnimate()
            .into(Holder.ImageViewCircleProfile);

            Holder.ImageViewCircleProfile.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (SharedHandler.GetString(context, "Username").equals(CommentList.get(Position).Username))
                        return;

                    Bundle bundle = new Bundle();
                    bundle.putString("Username", CommentList.get(Position).Target);

                    Fragment fragment = new ProfileFragment();
                    fragment.setArguments(bundle);

                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.MainActivityFullContainer, fragment).addToBackStack("ProfileFragment").commitAllowingStateLoss();
                }
            });

            String Message = CommentList.get(Position).Username + " " + getString(R.string.ProfileFragmentCommentMessage) + " " + CommentList.get(Position).Target;
            int UsernameLength = CommentList.get(Position).Username .length();

            SpannableString SpanMessage = new SpannableString(Message);
            SpanMessage.setSpan(new StyleSpan(Typeface.BOLD), SpanMessage.length() - CommentList.get(Position).Target.length(), SpanMessage.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            SpanMessage.setSpan(new StyleSpan(Typeface.BOLD), 0, UsernameLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            Holder.TextViewMessage.setText(SpanMessage);

            Holder.TextViewTime.setText(MiscHandler.GetTimeName(CommentList.get(Position).Time));

            Holder.ImageViewMore.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    PopupMenu PopMenu = new PopupMenu(context, Holder.ImageViewMore);
                    PopMenu.getMenu().add(getString(R.string.ProfileFragmentCommentReport));
                    PopMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
                    {
                        @Override
                        public boolean onMenuItemClick(MenuItem item)
                        {
                            return false;
                        }
                    });
                    PopMenu.show();
                }
            });

            String Comment = CommentList.get(Position).Comment;

            if (Comment.length() > 20)
                Comment = Comment.substring(0, 20) + " ...";

            Holder.TextViewComment.setText(("\" " + Comment + " \""));

            if (Position == CommentList.size() - 1)
                Holder.ViewLine.setVisibility(View.GONE);
            else
                Holder.ViewLine.setVisibility(View.VISIBLE);
        }

        @Override
        public ViewHolderComment onCreateViewHolder(ViewGroup parent, int ViewType)
        {
            if (ViewType == 2)
            {
                RelativeLayout RelativeLayoutMain = new RelativeLayout(context);
                RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

                RelativeLayout.LayoutParams ImageViewCommentParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 80), MiscHandler.ToDimension(context, 80));
                ImageViewCommentParam.setMargins(0, MiscHandler.ToDimension(context, 30), 0, MiscHandler.ToDimension(context, 10));
                ImageViewCommentParam.addRule(RelativeLayout.CENTER_HORIZONTAL);

                ImageView ImageViewComment = new ImageView(context);
                ImageViewComment.setLayoutParams(ImageViewCommentParam);
                ImageViewComment.setImageResource(R.drawable.ic_comment_gray);
                ImageViewComment.setId(MiscHandler.GenerateViewID());

                RelativeLayoutMain.addView(ImageViewComment);

                RelativeLayout.LayoutParams TextViewMessageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                TextViewMessageParam.addRule(RelativeLayout.BELOW, ImageViewComment.getId());
                TextViewMessageParam.addRule(RelativeLayout.CENTER_HORIZONTAL);

                TextView TextViewMessage = new TextView(context);
                TextViewMessage.setLayoutParams(TextViewMessageParam);
                TextViewMessage.setTextColor(ContextCompat.getColor(context, R.color.Gray2));
                TextViewMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                TextViewMessage.setText(getString(R.string.ProfileFragmentCommentEmpty));

                RelativeLayoutMain.addView(TextViewMessage);

                return new ViewHolderComment(RelativeLayoutMain, false);
            }

            if (ViewType == 0)
            {
                RelativeLayout RelativeLayoutMain = new RelativeLayout(context);
                RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                RelativeLayoutMain.setId(ID_MAIN);

                RelativeLayout.LayoutParams ImageViewCircleProfileParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 50), MiscHandler.ToDimension(context, 50));
                ImageViewCircleProfileParam.setMargins(MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10));

                ImageViewCircle ImageViewCircleProfile = new ImageViewCircle(context);
                ImageViewCircleProfile.SetBorderWidth(MiscHandler.ToDimension(context, 3));
                ImageViewCircleProfile.setLayoutParams(ImageViewCircleProfileParam);
                ImageViewCircleProfile.setImageResource(R.color.BlueGray);
                ImageViewCircleProfile.setId(ID_PROFILE);

                RelativeLayoutMain.addView(ImageViewCircleProfile);

                RelativeLayout.LayoutParams TextViewMessageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                TextViewMessageParam.setMargins(0, MiscHandler.ToDimension(context, 15), 0, 0);
                TextViewMessageParam.addRule(RelativeLayout.RIGHT_OF, ID_PROFILE);
                TextViewMessageParam.addRule(RelativeLayout.LEFT_OF, ID_TIME);

                TextView TextViewMessage = new TextView(context);
                TextViewMessage.setLayoutParams(TextViewMessageParam);
                TextViewMessage.setTextColor(ContextCompat.getColor(context, R.color.Black4));
                TextViewMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                TextViewMessage.setId(ID_MESSAGE);

                RelativeLayoutMain.addView(TextViewMessage);

                RelativeLayout.LayoutParams ImageViewMoreParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 32), MiscHandler.ToDimension(context, 32));
                ImageViewMoreParam.setMargins(0, MiscHandler.ToDimension(context, 10), 0, 0);
                ImageViewMoreParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

                ImageView ImageViewMore = new ImageView(context);
                ImageViewMore.setLayoutParams(ImageViewMoreParam);
                ImageViewMore.setPadding(MiscHandler.ToDimension(context, 1), MiscHandler.ToDimension(context, 1), MiscHandler.ToDimension(context, 1), MiscHandler.ToDimension(context, 1));
                ImageViewMore.setImageResource(R.drawable.ic_option);
                ImageViewMore.setId(ID_MORE);

                RelativeLayoutMain.addView(ImageViewMore);

                RelativeLayout.LayoutParams TextViewTimeParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                TextViewTimeParam.setMargins(0, MiscHandler.ToDimension(context, 15), 0, 0);
                TextViewTimeParam.addRule(RelativeLayout.LEFT_OF, ID_MORE);

                TextView TextViewTime = new TextView(context);
                TextViewTime.setLayoutParams(TextViewTimeParam);
                TextViewTime.setTextColor(ContextCompat.getColor(context, R.color.Gray3));
                TextViewTime.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                TextViewTime.setId(ID_TIME);

                RelativeLayoutMain.addView(TextViewTime);

                RelativeLayout.LayoutParams TextViewCommentParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                TextViewCommentParam.addRule(RelativeLayout.RIGHT_OF, ID_PROFILE);
                TextViewCommentParam.addRule(RelativeLayout.BELOW, ID_MESSAGE);

                TextView TextViewComment = new TextView(context);
                TextViewComment.setLayoutParams(TextViewCommentParam);
                TextViewComment.setTextColor(ContextCompat.getColor(context, R.color.Gray4));
                TextViewComment.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                TextViewComment.setId(ID_COMMENT);

                RelativeLayoutMain.addView(TextViewComment);

                RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1));
                ViewLineParam.setMargins(0, MiscHandler.ToDimension(context, 5), 0, MiscHandler.ToDimension(context, 5));
                ViewLineParam.addRule(RelativeLayout.BELOW, ID_COMMENT);

                TextView ViewLine = new TextView(context);
                ViewLine.setLayoutParams(ViewLineParam);
                ViewLine.setBackgroundResource(R.color.Gray);
                ViewLine.setId(ID_LINE);

                RelativeLayoutMain.addView(ViewLine);

                return new ViewHolderComment(RelativeLayoutMain, true);
            }

            LoadingView LoadingViewMain = new LoadingView(context);
            LoadingViewMain.setLayoutParams(new LoadingView.LayoutParams(LoadingView.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56)));
            LoadingViewMain.Start();

            return new ViewHolderComment(LoadingViewMain, false);
        }

        @Override
        public int getItemCount()
        {
            if (CommentList.size() == 0)
                return 1;

            return CommentList.size();
        }

        @Override
        public int getItemViewType(int position)
        {
            if (CommentList.size() == 0)
                return 2;

            return CommentList.get(position) == null ? 1 : 0;
        }
    }

    private class StructComment
    {
        private String PostID;
        private String Username;
        private String Avatar;
        private String Target;
        private String Comment;
        private int Time;
    }
}
