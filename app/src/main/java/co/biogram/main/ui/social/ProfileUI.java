package co.biogram.main.ui.social;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.json.JSONObject;

import co.biogram.main.R;
import co.biogram.main.fragment.FragmentView;
import co.biogram.main.handler.DBHandler;
import co.biogram.main.handler.Misc;
import co.biogram.main.handler.SharedHandler;
import co.biogram.main.ui.view.CircleImageView;
import co.biogram.main.ui.view.StickyScrollView;
import co.biogram.main.ui.view.TextView;

public class ProfileUI extends FragmentView
{
    public ProfileUI()
    {

    }

    public ProfileUI(String username)
    {

    }

    private TextView TextViewName;
    private TextView TextViewTitle;
    private CircleImageView CircleImageViewProfile;
    private TextView TextViewUsername;
    private TextView TextViewType;
    private TextView TextViewProfileCount;
    private TextView TextViewFollowingCount;
    private TextView TextViewFollowerCount;
    private TextView TextViewPostCount;
    private TextView TextViewLevel2;
    private TextView TextViewCash2;
    private TextView TextViewRating2;
    private TextView TextViewRating3;
    private TextView TextViewBadge3;
    private TextView TextViewAboutMe;
    private TextView TextViewLink;
    private LinearLayout LinearLayoutLink;
    private LinearLayout LinearLayoutLocation;
    private TextView TextViewLocation;

    @Override
    public void OnCreate()
    {
        RelativeLayout RelativeLayoutMain = new RelativeLayout(Activity);
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayoutMain.setBackgroundResource(Misc.IsDark() ? R.color.GroundDark : R.color.GroundWhite);
        RelativeLayoutMain.setClickable(true);

        RelativeLayout RelativeLayoutHeader = new RelativeLayout(Activity);
        RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56)));
        RelativeLayoutHeader.setBackgroundResource(Misc.IsDark() ? R.color.ActionBarDark : R.color.ActionBarWhite);
        RelativeLayoutHeader.setId(Misc.ViewID());

        RelativeLayoutMain.addView(RelativeLayoutHeader);

        RelativeLayout.LayoutParams TextViewTitleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTitleParam.setMargins(Misc.ToDP(15), 0, Misc.ToDP(15), 0);
        TextViewTitleParam.addRule(RelativeLayout.CENTER_VERTICAL);
        TextViewTitleParam.addRule(Misc.Align("R"));

        TextViewTitle = new TextView(Activity, 16, true);
        TextViewTitle.setLayoutParams(TextViewTitleParam);
        TextViewTitle.SetColor(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite);
        TextViewTitle.setPadding(0, Misc.ToDP(6), 0, 0);
        TextViewTitle.setText(Misc.String(R.string.ProfileUI));

        RelativeLayoutHeader.addView(TextViewTitle);

        RelativeLayout.LayoutParams ImageViewSettingParam = new RelativeLayout.LayoutParams(Misc.ToDP(56), Misc.ToDP(56));
        ImageViewSettingParam.addRule(Misc.Align("L"));

        ImageView ImageViewSetting = new ImageView(Activity);
        ImageViewSetting.setLayoutParams(ImageViewSettingParam);
        ImageViewSetting.setId(Misc.ViewID());
        ImageViewSetting.setImageResource(R.drawable._inbox_search);
        ImageViewSetting.setPadding(Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15));
        ImageViewSetting.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { /* TODO Open Setting */  } });

        RelativeLayoutHeader.addView(ImageViewSetting);

        RelativeLayout.LayoutParams ImageViewWriteParam = new RelativeLayout.LayoutParams(Misc.ToDP(56), Misc.ToDP(56));
        ImageViewWriteParam.addRule(Misc.AlignTo("L"), ImageViewSetting.getId());

        ImageView ImageViewProfile = new ImageView(Activity);
        ImageViewProfile.setLayoutParams(ImageViewWriteParam);
        ImageViewProfile.setImageResource(R.drawable._inbox_write);
        ImageViewProfile.setPadding(Misc.ToDP(5), Misc.ToDP(5), Misc.ToDP(5), Misc.ToDP(5));
        ImageViewProfile.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { /* TODO Open Profile */  } });

        RelativeLayoutHeader.addView(ImageViewProfile);

        RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1));
        ViewLineParam.addRule(RelativeLayout.BELOW, RelativeLayoutHeader.getId());

        View ViewLine = new View(Activity);
        ViewLine.setLayoutParams(ViewLineParam);
        ViewLine.setBackgroundResource(Misc.IsDark() ? R.color.LineDark : R.color.LineWhite);
        ViewLine.setId(Misc.ViewID());

        RelativeLayoutMain.addView(ViewLine);

        RelativeLayout.LayoutParams ScrollViewMainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        ScrollViewMainParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

        StickyScrollView ScrollViewMain = new StickyScrollView(Activity);
        ScrollViewMain.setLayoutParams(ScrollViewMainParam);

        RelativeLayoutMain.addView(ScrollViewMain);

        RelativeLayout RelativeLayoutScroll = new RelativeLayout(Activity);
        RelativeLayoutScroll.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

        ScrollViewMain.addView(RelativeLayoutScroll);

        RelativeLayout.LayoutParams CircleImageViewProfileParam = new RelativeLayout.LayoutParams(Misc.ToDP(65), Misc.ToDP(65));
        CircleImageViewProfileParam.setMargins(Misc.ToDP(15), Misc.ToDP(20), Misc.ToDP(15), 0);
        CircleImageViewProfileParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

        CircleImageViewProfile = new CircleImageView(Activity);
        CircleImageViewProfile.setLayoutParams(CircleImageViewProfileParam);
        CircleImageViewProfile.SetBorderColor(R.color.LineWhite);
        CircleImageViewProfile.SetCircleBackgroundColor(R.color.Gray);
        CircleImageViewProfile.setImageResource(R.drawable._profile_avatar);
        CircleImageViewProfile.setId(Misc.ViewID());
        CircleImageViewProfile.SetBorderWidth(1);

        RelativeLayoutScroll.addView(CircleImageViewProfile);

        RelativeLayout.LayoutParams TextViewNameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewNameParam.addRule(RelativeLayout.RIGHT_OF, CircleImageViewProfile.getId());
        TextViewNameParam.setMargins(0, Misc.ToDP(20), 0, 0);

        TextViewName = new TextView(Activity, 14, true);
        TextViewName.setLayoutParams(TextViewNameParam);
        TextViewName.SetColor(R.color.TextWhite);

        RelativeLayoutScroll.addView(TextViewName);

        RelativeLayout.LayoutParams TextViewUsernameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewUsernameParam.addRule(RelativeLayout.RIGHT_OF,  CircleImageViewProfile.getId());
        TextViewUsernameParam.setMargins(0, Misc.ToDP(39), 0, 0);

        TextViewUsername = new TextView(Activity, 14, false);
        TextViewUsername.setLayoutParams(TextViewUsernameParam);
        TextViewUsername.SetColor(R.color.Gray);

        RelativeLayoutScroll.addView(TextViewUsername);

        RelativeLayout.LayoutParams LinearLayoutTypeParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LinearLayoutTypeParam.addRule(RelativeLayout.RIGHT_OF,  CircleImageViewProfile.getId());
        LinearLayoutTypeParam.setMargins(0, Misc.ToDP(61), 0, 0);

        LinearLayout LinearLayoutType = new LinearLayout(Activity);
        LinearLayoutType.setLayoutParams(LinearLayoutTypeParam);
        LinearLayoutType.setOrientation(LinearLayout.HORIZONTAL);

        RelativeLayoutScroll.addView(LinearLayoutType);

        ImageView ImageViewType = new ImageView(Activity);
        ImageViewType.setLayoutParams(new RelativeLayout.LayoutParams(Misc.ToDP(24), Misc.ToDP(24)));
        ImageViewType.setImageResource(R.drawable._general_like);

        LinearLayoutType.addView(ImageViewType);

        TextViewType = new TextView(Activity, 14, false);
        TextViewType.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewType.SetColor(R.color.Primary);

        LinearLayoutType.addView(TextViewType);

        RelativeLayout.LayoutParams LinearLayoutEditParam = new RelativeLayout.LayoutParams(Misc.ToDP(85), Misc.ToDP(45));
        LinearLayoutEditParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        LinearLayoutEditParam.setMargins(0, Misc.ToDP(30), Misc.ToDP(15), 0);

        GradientDrawable DrawableEdit = new GradientDrawable();
        DrawableEdit.setColor(Misc.Color(R.color.Primary));
        DrawableEdit.setCornerRadius(Misc.ToDP(25));

        LinearLayout LinearLayoutEdit = new LinearLayout(Activity);
        LinearLayoutEdit.setLayoutParams(LinearLayoutEditParam);
        LinearLayoutEdit.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayoutEdit.setGravity(Gravity.CENTER);
        LinearLayoutEdit.setBackground(DrawableEdit);

        RelativeLayoutScroll.addView(LinearLayoutEdit);

        ImageView ImageViewEdit = new ImageView(Activity);
        ImageViewEdit.setLayoutParams(new RelativeLayout.LayoutParams(Misc.ToDP(32), Misc.ToDP(32)));
        ImageViewEdit.setImageResource(R.drawable._profile_edit);

        LinearLayoutEdit.addView(ImageViewEdit);

        TextView TextViewEdit = new TextView(Activity, 14, false);
        TextViewEdit.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewEdit.SetColor(R.color.TextDark);
        TextViewEdit.setPadding(Misc.ToDP(5), Misc.ToDP(5), Misc.ToDP(10), 0);
        TextViewEdit.setText(Activity.getString(R.string.ProfileUIEdit));

        LinearLayoutEdit.addView(TextViewEdit);

        RelativeLayout.LayoutParams ViewLine2Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1));
        ViewLine2Param.addRule(RelativeLayout.BELOW, CircleImageViewProfile.getId());
        ViewLine2Param.setMargins(0, Misc.ToDP(20), 0, Misc.ToDP(20));

        View ViewLine2 = new View(Activity);
        ViewLine2.setLayoutParams(ViewLine2Param);
        ViewLine2.setBackgroundResource(R.color.LineWhite);
        ViewLine2.setId(Misc.ViewID());

        RelativeLayoutScroll.addView(ViewLine2);

        RelativeLayout.LayoutParams LinearLayoutCountParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56));
        LinearLayoutCountParam.addRule(RelativeLayout.BELOW, ViewLine2.getId());

        LinearLayout LinearLayoutCount = new LinearLayout(Activity);
        LinearLayoutCount.setLayoutParams(LinearLayoutCountParam);
        LinearLayoutCount.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayoutCount.setGravity(Gravity.CENTER);
        LinearLayoutCount.setId(Misc.ViewID());

        RelativeLayoutScroll.addView(LinearLayoutCount);

        LinearLayout LinearLayoutPost = new LinearLayout(Activity);
        LinearLayoutPost.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 0.8f));
        LinearLayoutPost.setOrientation(LinearLayout.VERTICAL);
        LinearLayoutPost.setGravity(Gravity.CENTER);

        LinearLayoutCount.addView(LinearLayoutPost);

        TextViewPostCount = new TextView(Activity, 14, true);
        TextViewPostCount.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewPostCount.SetColor(R.color.TextWhite);
        TextViewPostCount.setPadding(0, Misc.ToDP(5), 0, 0);

        LinearLayoutPost.addView(TextViewPostCount);

        TextView TextViewPost = new TextView(Activity, 14, true);
        TextViewPost.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewPost.SetColor(R.color.Gray);
        TextViewPost.setPadding(0, Misc.ToDP(5), 0, 0);
        TextViewPost.setText(Activity.getString(R.string.ProfileUIPost));

        LinearLayoutPost.addView(TextViewPost);

        LinearLayout LinearLayoutFollower = new LinearLayout(Activity);
        LinearLayoutFollower.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 0.8f));
        LinearLayoutFollower.setOrientation(LinearLayout.VERTICAL);
        LinearLayoutFollower.setGravity(Gravity.CENTER);

        LinearLayoutCount.addView(LinearLayoutFollower);

        TextViewFollowerCount = new TextView(Activity, 14, true);
        TextViewFollowerCount.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewFollowerCount.SetColor(R.color.TextWhite);
        TextViewFollowerCount.setPadding(0, Misc.ToDP(5), 0, 0);

        LinearLayoutFollower.addView(TextViewFollowerCount);

        TextView TextViewFollower = new TextView(Activity, 14, true);
        TextViewFollower.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewFollower.SetColor(R.color.Gray);
        TextViewFollower.setPadding(0, Misc.ToDP(5), 0, 0);
        TextViewFollower.setText(Activity.getString(R.string.ProfileUIFollower));

        LinearLayoutFollower.addView(TextViewFollower);

        LinearLayout LinearLayoutFollowing = new LinearLayout(Activity);
        LinearLayoutFollowing.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 0.8f));
        LinearLayoutFollowing.setOrientation(LinearLayout.VERTICAL);
        LinearLayoutFollowing.setGravity(Gravity.CENTER);

        LinearLayoutCount.addView(LinearLayoutFollowing);

        TextViewFollowingCount = new TextView(Activity, 14, true);
        TextViewFollowingCount.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewFollowingCount.SetColor(R.color.TextWhite);
        TextViewFollowingCount.setPadding(0, Misc.ToDP(5), 0, 0);

        LinearLayoutFollowing.addView(TextViewFollowingCount);

        TextView TextViewFollowing = new TextView(Activity, 14, true);
        TextViewFollowing.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewFollowing.SetColor(R.color.Gray);
        TextViewFollowing.setPadding(0, Misc.ToDP(5), 0, 0);
        TextViewFollowing.setText(Activity.getString(R.string.ProfileUIFollowing));

        LinearLayoutFollowing.addView(TextViewFollowing);

        LinearLayout LinearLayoutProfile = new LinearLayout(Activity);
        LinearLayoutProfile.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
        LinearLayoutProfile.setOrientation(LinearLayout.VERTICAL);
        LinearLayoutProfile.setGravity(Gravity.CENTER);

        LinearLayoutCount.addView(LinearLayoutProfile);

        TextViewProfileCount = new TextView(Activity, 14, true);
        TextViewProfileCount.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewProfileCount.SetColor(R.color.TextWhite);
        TextViewProfileCount.setPadding(0, Misc.ToDP(5), 0, 0);

        LinearLayoutProfile.addView(TextViewProfileCount);

        TextView TextViewProfile = new TextView(Activity, 14, true);
        TextViewProfile.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewProfile.SetColor(R.color.Gray);
        TextViewProfile.setPadding(0, Misc.ToDP(5), 0, 0);
        TextViewProfile.setText(Activity.getString(R.string.ProfileUIProfileView));

        LinearLayoutProfile.addView(TextViewProfile);

        RelativeLayout.LayoutParams ViewLine3Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1));
        ViewLine3Param.addRule(RelativeLayout.BELOW, LinearLayoutCount.getId());
        ViewLine3Param.setMargins(0, Misc.ToDP(20), 0, Misc.ToDP(20));

        View ViewLine3 = new View(Activity);
        ViewLine3.setLayoutParams(ViewLine3Param);
        ViewLine3.setBackgroundResource(R.color.LineWhite);
        ViewLine3.setId(Misc.ViewID());

        RelativeLayoutScroll.addView(ViewLine3);

        RelativeLayout.LayoutParams TextViewPropertyParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewPropertyParam.addRule(RelativeLayout.BELOW, ViewLine3.getId());
        TextViewPropertyParam.setMargins(Misc.ToDP(20), 0, Misc.ToDP(20), Misc.ToDP(10));

        TextView TextViewProperty = new TextView(Activity, 16, true);
        TextViewProperty.setLayoutParams(TextViewPropertyParam);
        TextViewProperty.SetColor(R.color.TextWhite);
        TextViewProperty.setId(Misc.ViewID());
        TextViewProperty.setText(Activity.getString(R.string.ProfileUIProperty));

        RelativeLayoutScroll.addView(TextViewProperty);

        RelativeLayout.LayoutParams TextViewLevelParam = new RelativeLayout.LayoutParams(Misc.ToDP(150), RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewLevelParam.setMargins(Misc.ToDP(20), 0, Misc.ToDP(20), Misc.ToDP(5));
        TextViewLevelParam.addRule(RelativeLayout.BELOW, TextViewProperty.getId());

        TextView TextViewLevel = new TextView(Activity, 14, true);
        TextViewLevel.setLayoutParams(TextViewLevelParam);
        TextViewLevel.setId(Misc.ViewID());
        TextViewLevel.SetColor(R.color.Gray);
        TextViewLevel.setText(Activity.getString(R.string.ProfileUILevel));

        RelativeLayoutScroll.addView(TextViewLevel);

        RelativeLayout.LayoutParams RelativeLayoutLevelParam = new RelativeLayout.LayoutParams(Misc.ToDP(150), Misc.ToDP(45));
        RelativeLayoutLevelParam.setMargins(Misc.ToDP(20), 0, Misc.ToDP(20), Misc.ToDP(10));
        RelativeLayoutLevelParam.addRule(RelativeLayout.BELOW, TextViewLevel.getId());

        GradientDrawable DrawableLevel = new GradientDrawable();
        DrawableLevel.setColor(Color.parseColor("#01c09e"));
        DrawableLevel.setCornerRadius(Misc.ToDP(8));

        RelativeLayout RelativeLayoutLevel = new RelativeLayout(Activity);
        RelativeLayoutLevel.setLayoutParams(RelativeLayoutLevelParam);
        RelativeLayoutLevel.setBackground(DrawableLevel);
        RelativeLayoutLevel.setId(Misc.ViewID());

        RelativeLayoutScroll.addView(RelativeLayoutLevel);

        RelativeLayout.LayoutParams ImageViewLevelParam = new RelativeLayout.LayoutParams(Misc.ToDP(32), Misc.ToDP(32));
        ImageViewLevelParam.addRule(RelativeLayout.CENTER_VERTICAL);

        ImageView ImageViewLevel = new ImageView(Activity);
        ImageViewLevel.setLayoutParams(ImageViewLevelParam);
        ImageViewLevel.setImageResource(R.drawable._profile_level);
        ImageViewLevel.setPadding(Misc.ToDP(3), Misc.ToDP(3), Misc.ToDP(3), Misc.ToDP(3));
        ImageViewLevel.setId(Misc.ViewID());

        RelativeLayoutLevel.addView(ImageViewLevel);

        RelativeLayout.LayoutParams TextViewLevel2Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewLevel2Param.addRule(RelativeLayout.RIGHT_OF, ImageViewLevel.getId());
        TextViewLevel2Param.addRule(RelativeLayout.CENTER_VERTICAL);

        TextViewLevel2 = new TextView(Activity, 14, true);
        TextViewLevel2.setLayoutParams(TextViewLevel2Param);
        TextViewLevel2.SetColor(R.color.TextDark);
        TextViewLevel2.setPadding(Misc.ToDP(5), Misc.ToDP(5), Misc.ToDP(10), 0);

        RelativeLayoutLevel.addView(TextViewLevel2);

        RelativeLayout.LayoutParams ImageViewLevel2Param = new RelativeLayout.LayoutParams(Misc.ToDP(32), Misc.ToDP(32));
        ImageViewLevel2Param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        ImageViewLevel2Param.addRule(RelativeLayout.CENTER_VERTICAL);

        ImageView ImageViewLevel2 = new ImageView(Activity);
        ImageViewLevel2.setLayoutParams(ImageViewLevel2Param);
        ImageViewLevel2.setImageResource(R.drawable.back_white_rtl);

        RelativeLayoutLevel.addView(ImageViewLevel2);

        RelativeLayout.LayoutParams TextViewCashParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewCashParam.addRule(RelativeLayout.BELOW, TextViewProperty.getId());
        TextViewCashParam.addRule(RelativeLayout.RIGHT_OF, TextViewLevel.getId());

        TextView TextViewCash = new TextView(Activity, 14, true);
        TextViewCash.setLayoutParams(TextViewCashParam);
        TextViewCash.SetColor(R.color.Gray);
        TextViewCash.setPadding(Misc.ToDP(5), Misc.ToDP(5), Misc.ToDP(10), 0);
        TextViewCash.setText(Activity.getString(R.string.ProfileUICash));

        RelativeLayoutScroll.addView(TextViewCash);

        RelativeLayout.LayoutParams RelativeLayoutCashParam =  new RelativeLayout.LayoutParams(Misc.ToDP(150), Misc.ToDP(45));
        RelativeLayoutCashParam.setMargins(Misc.ToDP(20), 0, Misc.ToDP(20), Misc.ToDP(10));
        RelativeLayoutCashParam.addRule(RelativeLayout.BELOW, TextViewLevel.getId());
        RelativeLayoutCashParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        GradientDrawable DrawableCash = new GradientDrawable();
        DrawableCash.setColor(Color.parseColor("#ef9c00"));
        DrawableCash.setCornerRadius(Misc.ToDP(8));

        RelativeLayout RelativeLayoutCash = new RelativeLayout(Activity);
        RelativeLayoutCash.setLayoutParams(RelativeLayoutCashParam);
        RelativeLayoutCash.setBackground(DrawableCash);

        RelativeLayoutScroll.addView(RelativeLayoutCash);

        RelativeLayout.LayoutParams ImageViewCashParam = new RelativeLayout.LayoutParams(Misc.ToDP(32), Misc.ToDP(32));
        ImageViewCashParam.addRule(RelativeLayout.CENTER_VERTICAL);

        ImageView ImageViewCash = new ImageView(Activity);
        ImageViewCash.setLayoutParams(ImageViewCashParam);
        ImageViewCash.setImageResource(R.drawable._profile_cash);
        ImageViewCash.setId(Misc.ViewID());

        RelativeLayoutCash.addView(ImageViewCash);

        RelativeLayout.LayoutParams TextViewCash2Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewCash2Param.addRule(RelativeLayout.RIGHT_OF, ImageViewCash.getId());
        TextViewCash2Param.addRule(RelativeLayout.CENTER_VERTICAL);

        TextViewCash2 = new TextView(Activity, 14, false);
        TextViewCash2.setLayoutParams(TextViewCash2Param);
        TextViewCash2.SetColor(R.color.TextDark);
        TextViewCash2.setPadding(Misc.ToDP(5), Misc.ToDP(5), Misc.ToDP(10), 0);

        RelativeLayoutCash.addView(TextViewCash2);

        RelativeLayout.LayoutParams ImageViewCash2Param = new RelativeLayout.LayoutParams(Misc.ToDP(32), Misc.ToDP(32));
        ImageViewCash2Param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        ImageViewCash2Param.addRule(RelativeLayout.CENTER_VERTICAL);

        ImageView ImageViewCash2 = new ImageView(Activity);
        ImageViewCash2.setLayoutParams(ImageViewCash2Param);
        ImageViewCash2.setImageResource(R.drawable.back_white_rtl);

        RelativeLayoutCash.addView(ImageViewCash2);

        RelativeLayout.LayoutParams TextViewRatingParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewRatingParam.addRule(RelativeLayout.BELOW, RelativeLayoutLevel.getId());
        TextViewRatingParam.setMargins(Misc.ToDP(20), 0, Misc.ToDP(8), Misc.ToDP(5));

        TextView TextViewRating = new TextView(Activity, 14, true);
        TextViewRating.setLayoutParams(TextViewRatingParam);
        TextViewRating.setId(Misc.ViewID());
        TextViewRating.SetColor(R.color.Gray);
        TextViewRating.setText(Activity.getString(R.string.ProfileUIRating));

        RelativeLayoutScroll.addView(TextViewRating);

        RelativeLayout.LayoutParams TextViewRating3Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewRating3Param.addRule(RelativeLayout.BELOW, RelativeLayoutLevel.getId());
        TextViewRating3Param.addRule(RelativeLayout.RIGHT_OF, TextViewRating.getId());
        TextViewRating3Param.setMargins(0, Misc.ToDP(3), 0, 0);

        TextViewRating3 = new TextView(Activity, 12, false);
        TextViewRating3.setLayoutParams(TextViewRating3Param);
        TextViewRating3.SetColor(R.color.Gray);

        RelativeLayoutScroll.addView(TextViewRating3);

        RelativeLayout.LayoutParams RelativeLayoutRatingParam =  new RelativeLayout.LayoutParams(Misc.ToDP(150), Misc.ToDP(45));
        RelativeLayoutRatingParam.setMargins(Misc.ToDP(20), 0, Misc.ToDP(20), Misc.ToDP(10));
        RelativeLayoutRatingParam.addRule(RelativeLayout.BELOW, TextViewRating.getId());

        GradientDrawable DrawableRating = new GradientDrawable();
        DrawableRating.setStroke(Misc.ToDP(2), Misc.Color(R.color.Primary));
        DrawableRating.setCornerRadius(Misc.ToDP(8));

        RelativeLayout RelativeLayoutRating = new RelativeLayout(Activity);
        RelativeLayoutRating.setLayoutParams(RelativeLayoutRatingParam);
        RelativeLayoutRating.setBackground(DrawableRating);

        RelativeLayoutScroll.addView(RelativeLayoutRating);

        RelativeLayout.LayoutParams TextViewRating2Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewRating2Param.addRule(RelativeLayout.CENTER_VERTICAL);

        TextViewRating2 = new TextView(Activity, 16, true);
        TextViewRating2.setLayoutParams(TextViewRating2Param);
        TextViewRating2.SetColor(R.color.TextWhite);
        TextViewRating2.setPadding(Misc.ToDP(15), Misc.ToDP(5), Misc.ToDP(30), 0);
        TextViewRating2.setId(Misc.ViewID());

        RelativeLayoutRating.addView(TextViewRating2);

        RelativeLayout.LayoutParams ImageViewRatingParam = new RelativeLayout.LayoutParams(Misc.ToDP(32), Misc.ToDP(32));
        ImageViewRatingParam.addRule(RelativeLayout.RIGHT_OF, TextViewRating2.getId());
        ImageViewRatingParam.addRule(RelativeLayout.CENTER_VERTICAL);

        ImageView ImageViewRating = new ImageView(Activity);
        ImageViewRating.setLayoutParams(ImageViewRatingParam);
        ImageViewRating.setImageResource(R.drawable._profile_rating);

        RelativeLayoutRating.addView(ImageViewRating);

        RelativeLayout.LayoutParams ImageViewRating2Param = new RelativeLayout.LayoutParams(Misc.ToDP(32), Misc.ToDP(32));
        ImageViewRating2Param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        ImageViewRating2Param.addRule(RelativeLayout.CENTER_VERTICAL);

        ImageView ImageViewRating2 = new ImageView(Activity);
        ImageViewRating2.setLayoutParams(ImageViewRating2Param);
        ImageViewRating2.setImageResource(R.drawable.back_blue_rtl);

        RelativeLayoutRating.addView(ImageViewRating2);

        RelativeLayout.LayoutParams TextViewBadgeParam = new RelativeLayout.LayoutParams(Misc.ToDP(150), RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewBadgeParam.addRule(RelativeLayout.BELOW, RelativeLayoutLevel.getId());
        TextViewBadgeParam.addRule(RelativeLayout.BELOW, RelativeLayoutLevel.getId());
        TextViewBadgeParam.addRule(RelativeLayout.RIGHT_OF, TextViewLevel.getId());

        TextView TextViewBadge = new TextView(Activity, 14, true);
        TextViewBadge.setLayoutParams(TextViewBadgeParam);
        TextViewBadge.setId(Misc.ViewID());
        TextViewBadge.SetColor(R.color.Gray);
        TextViewBadge.setPadding(Misc.ToDP(5), Misc.ToDP(5), Misc.ToDP(10), 0);
        TextViewBadge.setText(Activity.getString(R.string.ProfileUIBadge));

        RelativeLayoutScroll.addView(TextViewBadge);

        RelativeLayout.LayoutParams RelativeLayoutBadgeParam =  new RelativeLayout.LayoutParams(Misc.ToDP(150), Misc.ToDP(45));
        RelativeLayoutBadgeParam.setMargins(Misc.ToDP(20), 0, Misc.ToDP(20), Misc.ToDP(10));
        RelativeLayoutBadgeParam.addRule(RelativeLayout.BELOW, TextViewBadge.getId());
        RelativeLayoutBadgeParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        GradientDrawable DrawableBadge = new GradientDrawable();
        DrawableBadge.setStroke(Misc.ToDP(2), Misc.Color(R.color.Primary));
        DrawableBadge.setCornerRadius(Misc.ToDP(8));

        RelativeLayout RelativeLayoutBadge = new RelativeLayout(Activity);
        RelativeLayoutBadge.setLayoutParams(RelativeLayoutBadgeParam);
        RelativeLayoutBadge.setBackground(DrawableBadge);
        RelativeLayoutBadge.setId(Misc.ViewID());

        RelativeLayoutScroll.addView(RelativeLayoutBadge);

        RelativeLayout.LayoutParams TextViewBadge2Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewBadge2Param.addRule(RelativeLayout.CENTER_VERTICAL);

        TextView TextViewBadge2 = new TextView(Activity, 14, false);
        TextViewBadge2.setLayoutParams(TextViewBadge2Param);
        TextViewBadge2.SetColor(R.color.Primary);
        TextViewBadge2.setPadding(Misc.ToDP(15), Misc.ToDP(5), 0, 0);
        TextViewBadge2.setId(Misc.ViewID());
        TextViewBadge2.setText(Activity.getString(R.string.ProfileUIBadge));

        RelativeLayoutBadge.addView(TextViewBadge2);

        RelativeLayout.LayoutParams TextViewBadge3Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewBadge3Param.addRule(RelativeLayout.RIGHT_OF, TextViewBadge2.getId());
        TextViewBadge3Param.addRule(RelativeLayout.CENTER_VERTICAL);

        TextViewBadge3 = new TextView(Activity, 14, false);
        TextViewBadge3.setLayoutParams(TextViewBadge3Param);
        TextViewBadge3.SetColor(R.color.TextWhite);
        TextViewBadge3.setPadding(Misc.ToDP(15), Misc.ToDP(5), Misc.ToDP(15), 0);

        RelativeLayoutBadge.addView(TextViewBadge3);

        RelativeLayout.LayoutParams ImageViewBadge2Param = new RelativeLayout.LayoutParams(Misc.ToDP(32), Misc.ToDP(32));
        ImageViewBadge2Param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        ImageViewBadge2Param.addRule(RelativeLayout.CENTER_VERTICAL);

        ImageView ImageViewBadge2 = new ImageView(Activity);
        ImageViewBadge2.setLayoutParams(ImageViewBadge2Param);
        ImageViewBadge2.setImageResource(R.drawable.back_blue_rtl);

        RelativeLayoutBadge.addView(ImageViewBadge2);

        RelativeLayout.LayoutParams ViewLine4Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1));
        ViewLine4Param.addRule(RelativeLayout.BELOW, RelativeLayoutBadge.getId());
        ViewLine4Param.setMargins(0, Misc.ToDP(20), 0, Misc.ToDP(20));

        View ViewLine4 = new View(Activity);
        ViewLine4.setLayoutParams(ViewLine4Param);
        ViewLine4.setBackgroundResource(R.color.LineWhite);
        ViewLine4.setId(Misc.ViewID());

        RelativeLayoutScroll.addView(ViewLine4);

        RelativeLayout.LayoutParams TextViewAboutParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewAboutParam.addRule(RelativeLayout.BELOW, ViewLine4.getId());
        TextViewAboutParam.setMargins(Misc.ToDP(20), 0, Misc.ToDP(20), 0);

        TextView TextViewAbout = new TextView(Activity, 16, true);
        TextViewAbout.setLayoutParams(TextViewAboutParam);
        TextViewAbout.SetColor(R.color.TextWhite);
        TextViewAbout.setId(Misc.ViewID());
        TextViewAbout.setText(Activity.getString(R.string.ProfileUIAbout));

        RelativeLayoutScroll.addView(TextViewAbout);

        RelativeLayout.LayoutParams TextViewAboutMeParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewAboutMeParam.addRule(RelativeLayout.BELOW, TextViewAbout.getId());
        TextViewAboutMeParam.setMargins(Misc.ToDP(20), 0, Misc.ToDP(20), Misc.ToDP(5));

        TextViewAboutMe = new TextView(Activity, 14, false);
        TextViewAboutMe.setLayoutParams(TextViewAboutMeParam);
        TextViewAboutMe.SetColor(R.color.Gray);
        TextViewAboutMe.setId(Misc.ViewID());

        RelativeLayoutScroll.addView(TextViewAboutMe);

        RelativeLayout.LayoutParams LinearLayoutLinkParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LinearLayoutLinkParam.addRule(RelativeLayout.BELOW, TextViewAboutMe.getId());
        LinearLayoutLinkParam.setMargins(Misc.ToDP(20), 0, Misc.ToDP(20), Misc.ToDP(5));

        LinearLayoutLink = new LinearLayout(Activity);
        LinearLayoutLink.setLayoutParams(LinearLayoutLinkParam);
        LinearLayoutLink.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayoutLink.setId(Misc.ViewID());

        RelativeLayoutScroll.addView(LinearLayoutLink);

        ImageView ImageViewLink = new ImageView(Activity);
        ImageViewLink.setLayoutParams(new RelativeLayout.LayoutParams(Misc.ToDP(32), Misc.ToDP(32)));
        ImageViewLink.setImageResource(R.drawable._category_artist_black);
        ImageViewLink.setPadding(Misc.ToDP(3), Misc.ToDP(3), Misc.ToDP(3), Misc.ToDP(3));

        LinearLayoutLink.addView(ImageViewLink);

        TextViewLink = new TextView(Activity, 14, false);
        TextViewLink.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewLink.SetColor(R.color.Primary);
        TextViewLink.setPadding(Misc.ToDP(5), Misc.ToDP(5), Misc.ToDP(10), 0);
        TextViewLink.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });

        LinearLayoutLink.addView(TextViewLink);

        RelativeLayout.LayoutParams LinearLayoutLocationParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LinearLayoutLocationParam.addRule(RelativeLayout.BELOW, LinearLayoutLink.getId());
        LinearLayoutLocationParam.setMargins(Misc.ToDP(20), 0, Misc.ToDP(20), Misc.ToDP(5));

        LinearLayoutLocation = new LinearLayout(Activity);
        LinearLayoutLocation.setLayoutParams(LinearLayoutLocationParam);
        LinearLayoutLocation.setOrientation(LinearLayout.HORIZONTAL);

        RelativeLayoutScroll.addView(LinearLayoutLocation);

        ImageView ImageViewLocation = new ImageView(Activity);
        ImageViewLocation.setLayoutParams(new RelativeLayout.LayoutParams(Misc.ToDP(32), Misc.ToDP(32)));
        ImageViewLocation.setImageResource(R.drawable._category_artist_black);
        ImageViewLocation.setPadding(Misc.ToDP(3), Misc.ToDP(3), Misc.ToDP(3), Misc.ToDP(3));

        LinearLayoutLocation.addView(ImageViewLocation);

        TextViewLocation = new TextView(Activity, 14, false);
        TextViewLocation.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewLocation.SetColor(R.color.Gray);
        TextViewLocation.setPadding(Misc.ToDP(5), Misc.ToDP(5), Misc.ToDP(10), 0);

        LinearLayoutLocation.addView(TextViewLocation);


        final DBHandler DB = new DBHandler(Activity);
        Cursor cursor = DB.InboxPost(Size);

        while (cursor.moveToNext())
        {
            P.ID = cursor.getString(cursor.getColumnIndex(DBHandler.INBOX_POST_ID));
            P.Profile = cursor.getString(cursor.getColumnIndex(DBHandler.INBOX_POST_PROFILE));
            P.Name = cursor.getString(cursor.getColumnIndex(DBHandler.INBOX_POST_NAME));
            P.Medal = cursor.getString(cursor.getColumnIndex(DBHandler.INBOX_POST_MEDAL));
            P.Username = cursor.getString(cursor.getColumnIndex(DBHandler.INBOX_POST_USERNAME));
            P.Time = cursor.getInt(cursor.getColumnIndex(DBHandler.INBOX_POST_TIME));
            P.Message = cursor.getString(cursor.getColumnIndex(DBHandler.INBOX_POST_MESSAGE));
            P.Type = cursor.getInt(cursor.getColumnIndex(DBHandler.INBOX_POST_TYPE));
            P.Data = cursor.getString(cursor.getColumnIndex(DBHandler.INBOX_POST_DATA));
            P.Owner = cursor.getString(cursor.getColumnIndex(DBHandler.INBOX_POST_OWNER));
            P.View = cursor.getInt(cursor.getColumnIndex(DBHandler.INBOX_POST_VIEW));
            P.Category = cursor.getInt(cursor.getColumnIndex(DBHandler.INBOX_POST_CATEGORY));
            P.LikeCount = cursor.getInt(cursor.getColumnIndex(DBHandler.INBOX_POST_LIKECOUNT));
            P.CommentCount = cursor.getInt(cursor.getColumnIndex(DBHandler.INBOX_POST_COMMENTCOUNT));
            P.IsLike = cursor.getInt(cursor.getColumnIndex(DBHandler.INBOX_POST_LIKE)) == 1;
            P.IsFollow = cursor.getInt(cursor.getColumnIndex(DBHandler.INBOX_POST_FOLLOW)) == 1;
            P.IsComment = cursor.getInt(cursor.getColumnIndex(DBHandler.INBOX_POST_COMMENT)) == 1;
            P.IsBookmark = cursor.getInt(cursor.getColumnIndex(DBHandler.INBOX_POST_BOOKMARK)) == 1;
            P.Person1ID = cursor.getString(cursor.getColumnIndex(DBHandler.INBOX_POST_I1));
            P.Person1Avatar = cursor.getString(cursor.getColumnIndex(DBHandler.INBOX_POST_I1P));
            P.Person2ID = cursor.getString(cursor.getColumnIndex(DBHandler.INBOX_POST_I2));
            P.Person2Avatar = cursor.getString(cursor.getColumnIndex(DBHandler.INBOX_POST_I2P));
            P.Person3ID = cursor.getString(cursor.getColumnIndex(DBHandler.INBOX_POST_I3));
            P.Person3Avatar = cursor.getString(cursor.getColumnIndex(DBHandler.INBOX_POST_I3P));
            P.Person4ID = cursor.getString(cursor.getColumnIndex(DBHandler.INBOX_POST_I4));
            P.Person4Avatar = cursor.getString(cursor.getColumnIndex(DBHandler.INBOX_POST_I4P));
        }

        cursor.close();

        AndroidNetworking.post(Misc.GetRandomServer("PostDeleteCheck"))
        .addBodyParameter("List", ID2)
        .addHeaders("Token", SharedHandler.GetString( "Token"))
        .setTag(Tag)
        .build()
        .getAsString(new StringRequestListener()
        {
            @Override
            public void onResponse(String Response)
            {
                try
                {
                    JSONObject Result = new JSONObject(Response);

                    if (Result.getInt("Message") == 0)
                    {

                    }
                }
                catch (Exception e)
                {
                    Misc.Debug("PostAdapter-Delete: " + e.toString());
                }
            }

            @Override public void onError(ANError e) { }
        });




        TextViewName.setText("ali khazaee");
        TextViewUsername.setText("@ali.khazaee");
        TextViewType.setText("Normal");
        TextViewProfileCount.setText("9812");
        TextViewFollowingCount.setText("1862");
        TextViewFollowerCount.setText("53.2K");
        TextViewPostCount.setText("10");
        TextViewLevel2.setText("Lv 3");
        TextViewCash2.setText("1800000 T");
        TextViewRating2.setText("3.5");
        TextViewBadge3.setText("none");
        TextViewAboutMe.setText("Salam Sosis, Ali Hastam Mohandese Petro Shimie Karaj Tahte Lisanse ORoPa xD, Biiiiiiiiiiiiiiiiiiiiib, ye QQ Miad Jolo Az ghast K Maaaan Begam on Weed o Bede Pass Man ye Daaam Begiram ");
        TextViewLink.setText("http://google.com/");
        TextViewLocation.setText("Karaj - Tehran");
        TextViewRating3.setText("12.3K Rates");

        ViewMain = RelativeLayoutMain;
    }
}
