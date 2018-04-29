package co.biogram.main.ui.social;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.androidnetworking.AndroidNetworking;

import co.biogram.main.R;
import co.biogram.main.fragment.FragmentView;
import co.biogram.main.handler.Misc;
import co.biogram.main.ui.view.TextView;
import de.hdodenhof.circleimageview.CircleImageView;

public class PostUI extends FragmentView
{
    private int ID1_PROFILE = Misc.generateViewId();
    private int ID1_NAME = Misc.generateViewId();
    private int ID1_MEDAL = Misc.generateViewId();
    private int ID1_USERNAME = Misc.generateViewId();
    private int ID1_TIME = Misc.generateViewId();
    private int ID1_MESSAGE = Misc.generateViewId();
    private int ID1_LIKE = Misc.generateViewId();
    private int ID1_LIKE_COUNT = Misc.generateViewId();
    private int ID1_COMMENT = Misc.generateViewId();
    private int ID1_COMMENT_COUNT = Misc.generateViewId();
    private int ID1_OPTION = Misc.generateViewId();
    private int ID1_PERSON1 = Misc.generateViewId();
    private int ID1_PERSON2 = Misc.generateViewId();
    private int ID1_PERSON3 = Misc.generateViewId();
    private int ID1_PERSON4 = Misc.generateViewId();
    private int ID1_IMAGE_LAYOUT = Misc.generateViewId();
    private int ID1_SINGLE = Misc.generateViewId();
    private int ID1_DOUBLE_LAYOUT = Misc.generateViewId();
    private int ID1_DOUBLE1 = Misc.generateViewId();
    private int ID1_DOUBLE2 = Misc.generateViewId();
    private int ID1_TRIPLE_LAYOUT = Misc.generateViewId();
    private int ID1_TRIPLE1 = Misc.generateViewId();
    private int ID1_TRIPLE2 = Misc.generateViewId();
    private int ID1_TRIPLE3 = Misc.generateViewId();
    private int ID1_VIDEO_LAYOUT = Misc.generateViewId();
    private int ID1_VIDEO_IMAGE = Misc.generateViewId();
    private int ID1_VIDEO_DURATION = Misc.generateViewId();
    private int ID1_VOTE_LAYOUT = Misc.generateViewId();
    private int ID1_VOTE_TYPE1 = Misc.generateViewId();
    private int ID1_VOTE_TYPE1_LIN1 = Misc.generateViewId();
    private int ID1_VOTE_TYPE1_LIN2 = Misc.generateViewId();
    private int ID1_VOTE_TYPE1_LIN3 = Misc.generateViewId();
    private int ID1_VOTE_TYPE1_LIN4 = Misc.generateViewId();
    private int ID1_VOTE_TYPE1_LIN5 = Misc.generateViewId();
    private int ID1_VOTE_TYPE1_SEL1 = Misc.generateViewId();
    private int ID1_VOTE_TYPE1_TEXT1 = Misc.generateViewId();
    private int ID1_VOTE_TYPE1_SEL2 = Misc.generateViewId();
    private int ID1_VOTE_TYPE1_TEXT2 = Misc.generateViewId();
    private int ID1_VOTE_TYPE1_SEL3 = Misc.generateViewId();
    private int ID1_VOTE_TYPE1_TEXT3 = Misc.generateViewId();
    private int ID1_VOTE_TYPE1_SEL4 = Misc.generateViewId();
    private int ID1_VOTE_TYPE1_TEXT4 = Misc.generateViewId();
    private int ID1_VOTE_TYPE1_SEL5 = Misc.generateViewId();
    private int ID1_VOTE_TYPE1_TEXT5 = Misc.generateViewId();
    private int ID1_VOTE_TYPE1_SUBMIT = Misc.generateViewId();
    private int ID1_VOTE_TYPE1_RESULT = Misc.generateViewId();
    private int ID1_VOTE_TYPE1_TIME = Misc.generateViewId();
    private int ID1_VOTE_TYPE2 = Misc.generateViewId();
    private int ID1_VOTE_TYPE2_TEXT1 = Misc.generateViewId();
    private int ID1_VOTE_TYPE2_TEXT2 = Misc.generateViewId();
    private int ID1_VOTE_TYPE2_TEXT3 = Misc.generateViewId();
    private int ID1_VOTE_TYPE2_TEXT4 = Misc.generateViewId();
    private int ID1_VOTE_TYPE2_TEXT5 = Misc.generateViewId();
    private int ID1_VOTE_TYPE2_PER1 = Misc.generateViewId();
    private int ID1_VOTE_TYPE2_PER2 = Misc.generateViewId();
    private int ID1_VOTE_TYPE2_PER3 = Misc.generateViewId();
    private int ID1_VOTE_TYPE2_PER4 = Misc.generateViewId();
    private int ID1_VOTE_TYPE2_PER5 = Misc.generateViewId();
    private int ID1_VOTE_TYPE2_SELECT = Misc.generateViewId();
    private int ID1_VOTE_TYPE2_RESULT = Misc.generateViewId();
    private int ID1_VOTE_TYPE2_TIME = Misc.generateViewId();
    private int ID1_FILE_LAYOUT = Misc.generateViewId();
    private int ID1_FILE_IMAGE = Misc.generateViewId();
    private int ID1_FILE_TEXT = Misc.generateViewId();
    private int ID1_FILE_NAME = Misc.generateViewId();
    private int ID1_FILE_DETAIL = Misc.generateViewId();
    private int ID1_VIEW_LINE = Misc.generateViewId();

    private String ID;

    public PostUI(String id)
    {
        ID = id;
    }

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
        RelativeLayoutHeader.setId(Misc.generateViewId());

        RelativeLayoutMain.addView(RelativeLayoutHeader);

        RelativeLayout.LayoutParams ImageViewBackParam = new RelativeLayout.LayoutParams(Misc.ToDP(56), Misc.ToDP(56));
        ImageViewBackParam.addRule(Misc.Align("R"));

        ImageView ImageViewBack = new ImageView(Activity);
        ImageViewBack.setLayoutParams(ImageViewBackParam);
        ImageViewBack.setPadding(Misc.ToDP(13), Misc.ToDP(13), Misc.ToDP(13), Misc.ToDP(13));
        ImageViewBack.setImageResource(Misc.IsRTL() ? R.drawable.z_general_back_blue : R.drawable.z_general_back_blue);
        ImageViewBack.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { Activity.onBackPressed(); } });
        ImageViewBack.setId(Misc.generateViewId());

        RelativeLayoutHeader.addView(ImageViewBack);

        RelativeLayout.LayoutParams TextViewTitleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTitleParam.addRule(Misc.AlignTo("R"), ImageViewBack.getId());
        TextViewTitleParam.addRule(RelativeLayout.CENTER_VERTICAL);

        TextView TextViewTitle = new TextView(Activity, 16, true);
        TextViewTitle.setLayoutParams(TextViewTitleParam);
        TextViewTitle.SetColor(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite);
        TextViewTitle.setText(Misc.String(R.string.PostUI));
        TextViewTitle.setPadding(0, Misc.ToDP(6), 0, 0);

        RelativeLayoutHeader.addView(TextViewTitle);

        RelativeLayout.LayoutParams ImageViewWorldParam = new RelativeLayout.LayoutParams(Misc.ToDP(56), Misc.ToDP(56));
        ImageViewWorldParam.addRule(Misc.Align("L"));

        ImageView ImageViewWorld = new ImageView(Activity);
        ImageViewWorld.setLayoutParams(ImageViewWorldParam);
        ImageViewWorld.setPadding(Misc.ToDP(13), Misc.ToDP(13), Misc.ToDP(13), Misc.ToDP(13));
        ImageViewWorld.setImageResource(R.drawable._write_global_gray);

        RelativeLayoutHeader.addView(ImageViewWorld);

        RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1));
        ViewLineParam.addRule(RelativeLayout.BELOW, RelativeLayoutHeader.getId());

        View ViewLine = new View(Activity);
        ViewLine.setLayoutParams(ViewLineParam);
        ViewLine.setBackgroundResource(Misc.IsDark() ? R.color.LineDark : R.color.LineWhite);
        ViewLine.setId(Misc.generateViewId());

        RelativeLayoutMain.addView(ViewLine);

        RelativeLayout.LayoutParams ScrollViewMainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        ScrollViewMainParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

        ScrollView ScrollViewMain = new ScrollView(Activity);
        ScrollViewMain.setLayoutParams(ScrollViewMainParam);
        ScrollViewMain.setFillViewport(true);

        RelativeLayoutMain.addView(ScrollViewMain);

        RelativeLayout RelativeLayoutScroll = new RelativeLayout(Activity);
        RelativeLayoutScroll.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

        ScrollViewMain.addView(RelativeLayoutScroll);

        RelativeLayout.LayoutParams CircleImageViewProfileParam = new RelativeLayout.LayoutParams(Misc.ToDP(50), Misc.ToDP(50));
        CircleImageViewProfileParam.setMargins(Misc.ToDP(8), Misc.ToDP(8), Misc.ToDP(8), Misc.ToDP(8));
        CircleImageViewProfileParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

        final CircleImageView CircleImageViewProfile = new CircleImageView(Activity);
        CircleImageViewProfile.setLayoutParams(CircleImageViewProfileParam);
        //CircleImageViewProfile.SetBorderColor(R.color.LineWhite);
        CircleImageViewProfile.setId(ID1_PROFILE);
        //CircleImageViewProfile.SetBorderWidth(1);

        RelativeLayoutScroll.addView(CircleImageViewProfile);

        RelativeLayout.LayoutParams TextViewNameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewNameParam.addRule(RelativeLayout.RIGHT_OF, ID1_PROFILE);
        TextViewNameParam.setMargins(0, Misc.ToDP(12), 0, 0);

        final TextView TextViewName = new TextView(Activity, 14, true);
        TextViewName.setLayoutParams(TextViewNameParam);
        TextViewName.SetColor(R.color.TextWhite);
        TextViewName.setId(ID1_NAME);

        RelativeLayoutScroll.addView(TextViewName);

        RelativeLayout.LayoutParams ImageViewMedalParam = new RelativeLayout.LayoutParams(Misc.ToDP(16), Misc.ToDP(16));
        ImageViewMedalParam.setMargins(Misc.ToDP(3), Misc.ToDP(16), 0, 0);
        ImageViewMedalParam.addRule(RelativeLayout.RIGHT_OF, ID1_NAME);

        final ImageView ImageViewMedal = new ImageView(Activity);
        ImageViewMedal.setLayoutParams(ImageViewMedalParam);
        ImageViewMedal.setId(ID1_MEDAL);

        RelativeLayoutScroll.addView(ImageViewMedal);

        RelativeLayout.LayoutParams TextViewUsernameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewUsernameParam.addRule(RelativeLayout.RIGHT_OF, ID1_PROFILE);
        TextViewUsernameParam.setMargins(0, Misc.ToDP(32), 0, 0);

        final TextView TextViewUsername = new TextView(Activity, 14, false);
        TextViewUsername.setLayoutParams(TextViewUsernameParam);
        TextViewUsername.SetColor(R.color.Gray);
        TextViewUsername.setId(ID1_USERNAME);

        RelativeLayoutScroll.addView(TextViewUsername);

        RelativeLayout.LayoutParams TextViewTimeParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTimeParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        TextViewTimeParam.setMargins(0, Misc.ToDP(14), Misc.ToDP(10), 0);

        final TextView TextViewTime = new TextView(Activity, 12, false);
        TextViewTime.setLayoutParams(TextViewTimeParam);
        TextViewTime.SetColor(R.color.Gray);
        TextViewTime.setId(ID1_TIME);

        RelativeLayoutScroll.addView(TextViewTime);

        RelativeLayout.LayoutParams TextViewMessageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewMessageParam.addRule(RelativeLayout.RIGHT_OF, ID1_PROFILE);
        TextViewMessageParam.addRule(RelativeLayout.BELOW, ID1_USERNAME);
        TextViewMessageParam.setMargins(0, 0, Misc.ToDP(10), 0);

        final TextView TextViewMessage = new TextView(Activity, 14, false);
        TextViewMessage.setLayoutParams(TextViewMessageParam);
        TextViewMessage.SetColor(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite);
        TextViewMessage.setId(ID1_MESSAGE);

        RelativeLayoutScroll.addView(TextViewMessage);

        RelativeLayout.LayoutParams RelativeLayoutContentParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayoutContentParam.addRule(RelativeLayout.RIGHT_OF, ID1_PROFILE);
        RelativeLayoutContentParam.addRule(RelativeLayout.BELOW, ID1_MESSAGE);
        RelativeLayoutContentParam.setMargins(0, 0, Misc.ToDP(5), 0);

        RelativeLayout RelativeLayoutContent = new RelativeLayout(Activity);
        RelativeLayoutContent.setLayoutParams(RelativeLayoutContentParam);
        RelativeLayoutContent.setId(Misc.generateViewId());

        RelativeLayoutScroll.addView(RelativeLayoutContent);

        final RelativeLayout RelativeLayoutImage = new RelativeLayout(Activity);
        RelativeLayoutImage.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(150)));
        RelativeLayoutImage.setVisibility(View.GONE);
        RelativeLayoutImage.setId(ID1_IMAGE_LAYOUT);

        RelativeLayoutContent.addView(RelativeLayoutImage);

        final ImageView ImageViewSingle = new ImageView(Activity);
        ImageViewSingle.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        ImageViewSingle.setVisibility(View.GONE);
        ImageViewSingle.setId(ID1_SINGLE);

        RelativeLayoutImage.addView(ImageViewSingle);

        final LinearLayout LinearLayoutDouble = new LinearLayout(Activity);
        LinearLayoutDouble.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        LinearLayoutDouble.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayoutDouble.setVisibility(View.GONE);
        LinearLayoutDouble.setId(ID1_DOUBLE_LAYOUT);

        RelativeLayoutImage.addView(LinearLayoutDouble);

        final ImageView ImageViewDouble1 = new ImageView(Activity);
        ImageViewDouble1.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1));
        ImageViewDouble1.setId(ID1_DOUBLE1);

        LinearLayoutDouble.addView(ImageViewDouble1);

        View ViewLineDouble = new View(Activity);
        ViewLineDouble.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 0.03f));

        LinearLayoutDouble.addView(ViewLineDouble);

        final ImageView ImageViewDouble2 = new ImageView(Activity);
        ImageViewDouble2.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1));
        ImageViewDouble2.setId(ID1_DOUBLE2);

        LinearLayoutDouble.addView(ImageViewDouble2);

        final LinearLayout LinearLayoutTriple = new LinearLayout(Activity);
        LinearLayoutTriple.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        LinearLayoutTriple.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayoutTriple.setVisibility(View.GONE);
        LinearLayoutTriple.setId(ID1_TRIPLE_LAYOUT);

        RelativeLayoutImage.addView(LinearLayoutTriple);

        final ImageView ImageViewTriple1 = new ImageView(Activity);
        ImageViewTriple1.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1));
        ImageViewTriple1.setId(ID1_TRIPLE1);

        LinearLayoutTriple.addView(ImageViewTriple1);

        View ViewLineTriple = new View(Activity);
        ViewLineTriple.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 0.02f));

        LinearLayoutTriple.addView(ViewLineTriple);

        LinearLayout LinearLayoutDouble2 = new LinearLayout(Activity);
        LinearLayoutDouble2.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1));
        LinearLayoutDouble2.setOrientation(LinearLayout.VERTICAL);

        LinearLayoutTriple.addView(LinearLayoutDouble2);

        final ImageView ImageViewTriple2 = new ImageView(Activity);
        ImageViewTriple2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1));
        ImageViewTriple2.setId(ID1_TRIPLE2);

        LinearLayoutDouble2.addView(ImageViewTriple2);

        View ViewLineTriple2 = new View(Activity);
        ViewLineTriple2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.04f));

        LinearLayoutDouble2.addView(ViewLineTriple2);

        final ImageView ImageViewTriple3 = new ImageView(Activity);
        ImageViewTriple3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1));
        ImageViewTriple3.setId(ID1_TRIPLE3);

        LinearLayoutDouble2.addView(ImageViewTriple3);

        final RelativeLayout RelativeLayoutVideo = new RelativeLayout(Activity);
        RelativeLayoutVideo.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(150)));
        RelativeLayoutVideo.setVisibility(View.GONE);
        RelativeLayoutVideo.setId(ID1_VIDEO_LAYOUT);

        RelativeLayoutContent.addView(RelativeLayoutVideo);

        final ImageView ImageViewVideo = new ImageView(Activity);
        ImageViewVideo.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        ImageViewVideo.setId(ID1_VIDEO_IMAGE);

        RelativeLayoutVideo.addView(ImageViewVideo);

        RelativeLayout.LayoutParams ImageViewPlayParam = new RelativeLayout.LayoutParams(Misc.ToDP(48), Misc.ToDP(48));
        ImageViewPlayParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        ImageView ImageViewPlay = new ImageView(Activity);
        ImageViewPlay.setLayoutParams(ImageViewPlayParam);
        ImageViewPlay.setImageResource(R.drawable._general_play);

        RelativeLayoutVideo.addView(ImageViewPlay);

        GradientDrawable DrawableVideo = new GradientDrawable();
        DrawableVideo.setColor(Color.parseColor("#65000000"));
        DrawableVideo.setCornerRadius(Misc.ToDP(4));

        RelativeLayout.LayoutParams TextViewDuritonParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewDuritonParam.setMargins(Misc.ToDP(8), 0, Misc.ToDP(8), Misc.ToDP(8));
        TextViewDuritonParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        TextViewDuritonParam.addRule(Misc.Align("L"));

        final TextView TextViewDuration = new TextView(Activity, 12, false);
        TextViewDuration.setLayoutParams(TextViewDuritonParam);
        TextViewDuration.setPadding(Misc.ToDP(5), Misc.ToDP(3), Misc.ToDP(5), 0);
        TextViewDuration.setBackground(DrawableVideo);
        TextViewDuration.setId(ID1_VIDEO_DURATION);

        RelativeLayoutVideo.addView(TextViewDuration);

        RelativeLayout.LayoutParams TextViewVideoParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewVideoParam.setMargins(Misc.ToDP(8), 0, Misc.ToDP(8), Misc.ToDP(8));
        TextViewVideoParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        TextViewVideoParam.addRule(Misc.Align("R"));

        TextView TextViewVideo = new TextView(Activity, 12, false);
        TextViewVideo.setLayoutParams(TextViewVideoParam);
        TextViewVideo.setPadding(Misc.ToDP(5), Misc.ToDP(3), Misc.ToDP(5), 0);
        TextViewVideo.setText(Misc.String(R.string.PostAdapterVideo));
        TextViewVideo.setBackground(DrawableVideo);

        RelativeLayoutVideo.addView(TextViewVideo);

        RelativeLayout.LayoutParams RelativeLayoutVoteParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayoutVoteParam.setMargins(0, 0, Misc.ToDP(5), 0);

        GradientDrawable DrawableBorder = new GradientDrawable();
        DrawableBorder.setStroke(Misc.ToDP(1), Misc.Color(R.color.LineWhite));
        DrawableBorder.setCornerRadius(Misc.ToDP(6));

        final RelativeLayout RelativeLayoutVote = new RelativeLayout(Activity);
        RelativeLayoutVote.setLayoutParams(RelativeLayoutVoteParam);
        RelativeLayoutVote.setPadding(Misc.ToDP(8), Misc.ToDP(8), Misc.ToDP(8), Misc.ToDP(8));
        RelativeLayoutVote.setBackground(DrawableBorder);
        RelativeLayoutVote.setVisibility(View.GONE);
        RelativeLayoutVote.setId(ID1_VOTE_LAYOUT);

        RelativeLayoutContent.addView(RelativeLayoutVote);

        LinearLayout LinearLayoutType1 = new LinearLayout(Activity);
        LinearLayoutType1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        LinearLayoutType1.setOrientation(LinearLayout.VERTICAL);
        LinearLayoutType1.setVisibility(View.GONE);
        LinearLayoutType1.setId(ID1_VOTE_TYPE1);

        RelativeLayoutVote.addView(LinearLayoutType1);

        final LinearLayout LinearLayoutVote1 = new LinearLayout(Activity);
        LinearLayoutVote1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(30)));
        LinearLayoutVote1.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayoutVote1.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayoutVote1.setId(ID1_VOTE_TYPE1_LIN1);

        LinearLayoutType1.addView(LinearLayoutVote1);

        LinearLayout LinearLayoutVote2 = new LinearLayout(Activity);
        LinearLayoutVote2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(30)));
        LinearLayoutVote2.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayoutVote2.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayoutVote2.setId(ID1_VOTE_TYPE1_LIN2);

        LinearLayoutType1.addView(LinearLayoutVote2);

        LinearLayout LinearLayoutVote3 = new LinearLayout(Activity);
        LinearLayoutVote3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(30)));
        LinearLayoutVote3.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayoutVote3.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayoutVote3.setVisibility(View.GONE);
        LinearLayoutVote3.setId(ID1_VOTE_TYPE1_LIN3);

        LinearLayoutType1.addView(LinearLayoutVote3);

        LinearLayout LinearLayoutVote4 = new LinearLayout(Activity);
        LinearLayoutVote4.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(30)));
        LinearLayoutVote4.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayoutVote4.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayoutVote4.setVisibility(View.GONE);
        LinearLayoutVote4.setId(ID1_VOTE_TYPE1_LIN4);

        LinearLayoutType1.addView(LinearLayoutVote4);

        LinearLayout LinearLayoutVote5 = new LinearLayout(Activity);
        LinearLayoutVote5.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(30)));
        LinearLayoutVote5.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayoutVote5.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayoutVote5.setVisibility(View.GONE);
        LinearLayoutVote5.setId(ID1_VOTE_TYPE1_LIN5);

        LinearLayoutType1.addView(LinearLayoutVote5);

        GradientDrawable DrawableSelect = new GradientDrawable();
        DrawableSelect.setStroke(Misc.ToDP(1), Misc.Color(R.color.Gray));
        DrawableSelect.setShape(GradientDrawable.OVAL);

        View ViewVote1 = new View(Activity);
        ViewVote1.setLayoutParams(new LinearLayout.LayoutParams(Misc.ToDP(16), Misc.ToDP(16)));
        ViewVote1.setBackground(DrawableSelect);
        ViewVote1.setId(ID1_VOTE_TYPE1_SEL1);

        LinearLayoutVote1.addView(ViewVote1);

        View ViewVote2 = new View(Activity);
        ViewVote2.setLayoutParams(new LinearLayout.LayoutParams(Misc.ToDP(16), Misc.ToDP(16)));
        ViewVote2.setBackground(DrawableSelect);
        ViewVote2.setId(ID1_VOTE_TYPE1_SEL2);

        LinearLayoutVote2.addView(ViewVote2);

        View ViewVote3 = new View(Activity);
        ViewVote3.setLayoutParams(new LinearLayout.LayoutParams(Misc.ToDP(16), Misc.ToDP(16)));
        ViewVote3.setBackground(DrawableSelect);
        ViewVote3.setId(ID1_VOTE_TYPE1_SEL3);

        LinearLayoutVote3.addView(ViewVote3);

        View ViewVote4 = new View(Activity);
        ViewVote4.setLayoutParams(new LinearLayout.LayoutParams(Misc.ToDP(16), Misc.ToDP(16)));
        ViewVote4.setBackground(DrawableSelect);
        ViewVote4.setId(ID1_VOTE_TYPE1_SEL4);

        LinearLayoutVote4.addView(ViewVote4);

        View ViewVote5 = new View(Activity);
        ViewVote5.setLayoutParams(new LinearLayout.LayoutParams(Misc.ToDP(16), Misc.ToDP(16)));
        ViewVote5.setBackground(DrawableSelect);
        ViewVote5.setId(ID1_VOTE_TYPE1_SEL5);

        LinearLayoutVote5.addView(ViewVote5);

        TextView TextViewVote1 = new TextView(Activity, 14, false);
        TextViewVote1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(20)));
        TextViewVote1.SetColor(R.color.TextWhite);
        TextViewVote1.setPadding(Misc.ToDP(5), 0, 0, 0);
        TextViewVote1.setGravity(Gravity.LEFT);
        TextViewVote1.setId(ID1_VOTE_TYPE1_TEXT1);

        LinearLayoutVote1.addView(TextViewVote1);

        TextView TextViewVote2 = new TextView(Activity, 14, false);
        TextViewVote2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(20)));
        TextViewVote2.SetColor(R.color.TextWhite);
        TextViewVote2.setPadding(Misc.ToDP(5), 0, 0, 0);
        TextViewVote2.setGravity(Gravity.LEFT);
        TextViewVote2.setId(ID1_VOTE_TYPE1_TEXT2);

        LinearLayoutVote2.addView(TextViewVote2);

        TextView TextViewVote3 = new TextView(Activity, 14, false);
        TextViewVote3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(20)));
        TextViewVote3.SetColor(R.color.TextWhite);
        TextViewVote3.setPadding(Misc.ToDP(5), 0, 0, 0);
        TextViewVote3.setId(ID1_VOTE_TYPE1_TEXT3);
        TextViewVote3.setGravity(Gravity.LEFT);

        LinearLayoutVote3.addView(TextViewVote3);

        TextView TextViewVote4 = new TextView(Activity, 14, false);
        TextViewVote4.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(20)));
        TextViewVote4.SetColor(R.color.TextWhite);
        TextViewVote4.setPadding(Misc.ToDP(5), 0, 0, 0);
        TextViewVote4.setId(ID1_VOTE_TYPE1_TEXT4);
        TextViewVote4.setGravity(Gravity.LEFT);

        LinearLayoutVote4.addView(TextViewVote4);

        TextView TextViewVote5 = new TextView(Activity, 14, false);
        TextViewVote5.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(20)));
        TextViewVote5.SetColor(R.color.TextWhite);
        TextViewVote5.setPadding(Misc.ToDP(5), 0, 0, 0);
        TextViewVote5.setId(ID1_VOTE_TYPE1_TEXT5);
        TextViewVote5.setGravity(Gravity.LEFT);

        LinearLayoutVote5.addView(TextViewVote5);

        View ViewVoteLine1 = new View(Activity);
        ViewVoteLine1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(8)));

        LinearLayoutType1.addView(ViewVoteLine1);

        View ViewVoteLine2 = new View(Activity);
        ViewVoteLine2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1)));
        ViewVoteLine2.setBackgroundResource(R.color.LineWhite);

        LinearLayoutType1.addView(ViewVoteLine2);

        View ViewVoteLine3 = new View(Activity);
        ViewVoteLine3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(8)));

        LinearLayoutType1.addView(ViewVoteLine3);

        LinearLayout LinearLayoutResult = new LinearLayout(Activity);
        LinearLayoutResult.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(30)));
        LinearLayoutResult.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayoutResult.setGravity(Gravity.CENTER_VERTICAL);

        LinearLayoutType1.addView(LinearLayoutResult);

        GradientDrawable DrawableSubmit = new GradientDrawable();
        DrawableSubmit.setColor(Misc.Color(R.color.Primary));
        DrawableSubmit.setCornerRadius(Misc.ToDP(4));

        TextView TextViewSubmit = new TextView(Activity, 12, true);
        TextViewSubmit.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, Misc.ToDP(30)));
        TextViewSubmit.setBackground(DrawableSubmit);
        TextViewSubmit.setPadding(Misc.ToDP(10), Misc.ToDP(5), Misc.ToDP(10), Misc.ToDP(5));
        TextViewSubmit.setGravity(Gravity.CENTER_VERTICAL);
        TextViewSubmit.setText(Misc.String(R.string.PostAdapterSubmit));
        TextViewSubmit.setId(ID1_VOTE_TYPE1_SUBMIT);

        LinearLayoutResult.addView(TextViewSubmit);

        TextView TextViewResult = new TextView(Activity, 12, false);
        TextViewResult.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        TextViewResult.setPadding(Misc.ToDP(10), Misc.ToDP(5), 0, 0);
        TextViewResult.setId(ID1_VOTE_TYPE1_RESULT);
        TextViewResult.SetColor(R.color.Gray);

        LinearLayoutResult.addView(TextViewResult);

        TextView TextViewVoteTime = new TextView(Activity, 12, false);
        TextViewVoteTime.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        TextViewVoteTime.setPadding(Misc.ToDP(10), Misc.ToDP(5), 0, 0);
        TextViewVoteTime.setId(ID1_VOTE_TYPE1_TIME);
        TextViewVoteTime.SetColor(R.color.Gray);

        LinearLayoutResult.addView(TextViewVoteTime);












        RelativeLayout RelativeLayoutType2 = new RelativeLayout(Activity);
        RelativeLayoutType2.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        RelativeLayoutType2.setVisibility(View.GONE);
        RelativeLayoutType2.setId(ID1_VOTE_TYPE2);

        RelativeLayoutVote.addView(RelativeLayoutType2);

        RelativeLayout.LayoutParams TextViewPercent1Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, Misc.ToDP(30));
        TextViewPercent1Param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        TextView TextViewPercent1 = new TextView(Activity, 14, false);
        TextViewPercent1.setLayoutParams(TextViewPercent1Param);
        TextViewPercent1.SetColor(R.color.TextWhite);
        TextViewPercent1.setPadding(Misc.ToDP(5), Misc.ToDP(7), 0, 0);
        TextViewPercent1.setGravity(Gravity.CENTER);
        TextViewPercent1.setId(ID1_VOTE_TYPE2_PER1);

        RelativeLayoutType2.addView(TextViewPercent1);

        RelativeLayout.LayoutParams TextViewPercent2Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, Misc.ToDP(30));
        TextViewPercent2Param.addRule(RelativeLayout.BELOW, ID1_VOTE_TYPE2_PER1);
        TextViewPercent2Param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        TextView TextViewPercent2 = new TextView(Activity, 14, false);
        TextViewPercent2.setLayoutParams(TextViewPercent2Param);
        TextViewPercent2.SetColor(R.color.TextWhite);
        TextViewPercent2.setPadding(Misc.ToDP(5), Misc.ToDP(7), 0, 0);
        TextViewPercent2.setGravity(Gravity.CENTER);
        TextViewPercent2.setId(ID1_VOTE_TYPE2_PER2);

        RelativeLayoutType2.addView(TextViewPercent2);

        RelativeLayout.LayoutParams TextViewPercent3Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, Misc.ToDP(30));
        TextViewPercent3Param.addRule(RelativeLayout.BELOW, ID1_VOTE_TYPE2_PER2);
        TextViewPercent3Param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        TextView TextViewPercent3 = new TextView(Activity, 14, false);
        TextViewPercent3.setLayoutParams(TextViewPercent3Param);
        TextViewPercent3.SetColor(R.color.TextWhite);
        TextViewPercent3.setPadding(Misc.ToDP(5), Misc.ToDP(7), 0, 0);
        TextViewPercent3.setGravity(Gravity.CENTER);
        TextViewPercent3.setVisibility(View.GONE);
        TextViewPercent3.setId(ID1_VOTE_TYPE2_PER3);

        RelativeLayoutType2.addView(TextViewPercent3);

        RelativeLayout.LayoutParams TextViewPercent4Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, Misc.ToDP(30));
        TextViewPercent4Param.addRule(RelativeLayout.BELOW, ID1_VOTE_TYPE2_PER3);
        TextViewPercent4Param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        TextView TextViewPercent4 = new TextView(Activity, 14, false);
        TextViewPercent4.setLayoutParams(TextViewPercent4Param);
        TextViewPercent4.SetColor(R.color.TextWhite);
        TextViewPercent4.setPadding(Misc.ToDP(5), Misc.ToDP(7), 0, 0);
        TextViewPercent4.setGravity(Gravity.CENTER);
        TextViewPercent4.setVisibility(View.GONE);
        TextViewPercent4.setId(ID1_VOTE_TYPE2_PER4);

        RelativeLayoutType2.addView(TextViewPercent4);

        RelativeLayout.LayoutParams TextViewPercent5Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, Misc.ToDP(30));
        TextViewPercent5Param.addRule(RelativeLayout.BELOW, ID1_VOTE_TYPE2_PER4);
        TextViewPercent5Param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        TextView TextViewPercent5 = new TextView(Activity, 14, false);
        TextViewPercent5.setLayoutParams(TextViewPercent5Param);
        TextViewPercent5.SetColor(R.color.TextWhite);
        TextViewPercent5.setPadding(Misc.ToDP(5), Misc.ToDP(7), 0, 0);
        TextViewPercent5.setGravity(Gravity.CENTER);
        TextViewPercent5.setVisibility(View.GONE);
        TextViewPercent5.setId(ID1_VOTE_TYPE2_PER5);

        RelativeLayoutType2.addView(TextViewPercent5);
/*
        RelativeLayout.LayoutParams TextViewVote1Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(25));
        TextViewVote1Param.addRule(RelativeLayout.LEFT_OF, ID1_VOTE_TYPE2_PER1);
        TextViewVote1Param.setMargins(Misc.ToDP(17), Misc.ToDP(5), 0, 0);

        TextView TextViewVote1 = new TextView(Activity, 14, false);
        TextViewVote1.setLayoutParams(TextViewVote1Param);
        TextViewVote1.SetColor(R.color.TextWhite);
        TextViewVote1.setPadding(Misc.ToDP(3), Misc.ToDP(1), 0, 0);
        TextViewVote1.setGravity(Gravity.LEFT);
        TextViewVote1.setId(ID1_VOTE_TYPE2_TEXT1);

        RelativeLayoutType2.addView(TextViewVote1);

        RelativeLayout.LayoutParams TextViewVote2Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(25));
        TextViewVote2Param.addRule(RelativeLayout.LEFT_OF, ID1_VOTE_TYPE2_PER2);
        TextViewVote2Param.addRule(RelativeLayout.BELOW, ID1_VOTE_TYPE2_TEXT1);
        TextViewVote2Param.setMargins(Misc.ToDP(17), Misc.ToDP(5), 0, 0);

        TextView TextViewVote2 = new TextView(Activity, 14, false);
        TextViewVote2.setLayoutParams(TextViewVote2Param);
        TextViewVote2.SetColor(R.color.TextWhite);
        TextViewVote2.setPadding(Misc.ToDP(3), Misc.ToDP(1), 0, 0);
        TextViewVote2.setGravity(Gravity.LEFT);
        TextViewVote2.setId(ID1_VOTE_TYPE2_TEXT2);

        RelativeLayoutType2.addView(TextViewVote2);

        RelativeLayout.LayoutParams TextViewVote3Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(25));
        TextViewVote3Param.addRule(RelativeLayout.LEFT_OF, ID1_VOTE_TYPE2_PER3);
        TextViewVote3Param.addRule(RelativeLayout.BELOW, ID1_VOTE_TYPE2_TEXT2);
        TextViewVote3Param.setMargins(Misc.ToDP(17), Misc.ToDP(5), 0, 0);

        TextView TextViewVote3 = new TextView(Activity, 14, false);
        TextViewVote3.setLayoutParams(TextViewVote3Param);
        TextViewVote3.SetColor(R.color.TextWhite);
        TextViewVote3.setPadding(Misc.ToDP(3), Misc.ToDP(1), 0, 0);
        TextViewVote3.setGravity(Gravity.LEFT);
        TextViewVote3.setVisibility(View.GONE);
        TextViewVote3.setId(ID1_VOTE_TYPE2_TEXT3);

        RelativeLayoutType2.addView(TextViewVote3);

        RelativeLayout.LayoutParams TextViewVote4Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(25));
        TextViewVote4Param.addRule(RelativeLayout.LEFT_OF, ID1_VOTE_TYPE2_PER4);
        TextViewVote4Param.addRule(RelativeLayout.BELOW, ID1_VOTE_TYPE2_TEXT3);
        TextViewVote4Param.setMargins(Misc.ToDP(17), Misc.ToDP(5), 0, 0);

        TextView TextViewVote4 = new TextView(Activity, 14, false);
        TextViewVote4.setLayoutParams(TextViewVote4Param);
        TextViewVote4.SetColor(R.color.TextWhite);
        TextViewVote4.setPadding(Misc.ToDP(3), Misc.ToDP(1), 0, 0);
        TextViewVote4.setGravity(Gravity.LEFT);
        TextViewVote4.setVisibility(View.GONE);
        TextViewVote4.setId(ID1_VOTE_TYPE2_TEXT4);

        RelativeLayoutType2.addView(TextViewVote4);

        RelativeLayout.LayoutParams TextViewVote5Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(25));
        TextViewVote5Param.addRule(RelativeLayout.LEFT_OF, ID1_VOTE_TYPE2_PER5);
        TextViewVote5Param.addRule(RelativeLayout.BELOW, ID1_VOTE_TYPE2_TEXT4);
        TextViewVote5Param.setMargins(Misc.ToDP(17), Misc.ToDP(5), 0, 0);

        TextView TextViewVote5 = new TextView(Activity, 14, false);
        TextViewVote5.setLayoutParams(TextViewVote5Param);
        TextViewVote5.SetColor(R.color.TextWhite);
        TextViewVote5.setPadding(Misc.ToDP(3), Misc.ToDP(1), 0, 0);
        TextViewVote5.setGravity(Gravity.LEFT);
        TextViewVote5.setVisibility(View.GONE);
        TextViewVote5.setId(ID1_VOTE_TYPE2_TEXT5);

        RelativeLayoutType2.addView(TextViewVote5);

        ImageView ImageViewSelect = new ImageView(Activity);
        ImageViewSelect.setLayoutParams(new RelativeLayout.LayoutParams(Misc.ToDP(32), Misc.ToDP(32)));
        ImageViewSelect.setImageResource(R.drawable._inbox_selection);
        ImageViewSelect.setId(ID1_VOTE_TYPE2_SELECT);

        RelativeLayoutType2.addView(ImageViewSelect);

        RelativeLayout.LayoutParams ViewVoteParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1));
        ViewVoteParam.addRule(RelativeLayout.BELOW, ID1_VOTE_TYPE2_TEXT5);
        ViewVoteParam.setMargins(0, Misc.ToDP(5), 0, Misc.ToDP(5));

        View ViewVote = new View(Activity);
        ViewVote.setLayoutParams(ViewVoteParam);
        ViewVote.setBackgroundResource(R.color.LineWhite);
        ViewVote.setId(Misc.generateViewId());

        RelativeLayoutType2.addView(ViewVote);

        RelativeLayout.LayoutParams LinearLayoutVoteSubmitParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(30));
        LinearLayoutVoteSubmitParam.addRule(RelativeLayout.BELOW, ViewVote.getId());

        LinearLayout LinearLayoutResult = new LinearLayout(Activity);
        LinearLayoutResult.setLayoutParams(LinearLayoutVoteSubmitParam);
        LinearLayoutResult.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayoutResult.setGravity(Gravity.CENTER_VERTICAL);

        RelativeLayoutType2.addView(LinearLayoutResult);

        TextView TextViewSubmit = new TextView(Activity, 12, false);
        TextViewSubmit.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        TextViewSubmit.setPadding(Misc.ToDP(10), Misc.ToDP(5), 0, 0);
        TextViewSubmit.setId(ID1_VOTE_TYPE2_RESULT);
        TextViewSubmit.SetColor(R.color.Gray);

        LinearLayoutResult.addView(TextViewSubmit);

        TextView TextViewVoteTime = new TextView(Activity, 12, false);
        TextViewVoteTime.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        TextViewVoteTime.setPadding(Misc.ToDP(10), Misc.ToDP(5), 0, 0);
        TextViewVoteTime.setId(ID1_VOTE_TYPE2_TIME);
        TextViewVoteTime.SetColor(R.color.Gray);

        LinearLayoutResult.addView(TextViewVoteTime);

        RelativeLayout.LayoutParams RelativeLayoutFileParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(70));
        RelativeLayoutFileParam.setMargins(0, Misc.ToDP(5), Misc.ToDP(5), Misc.ToDP(5));

        final RelativeLayout RelativeLayoutFile = new RelativeLayout(Activity);
        RelativeLayoutFile.setLayoutParams(RelativeLayoutFileParam);
        RelativeLayoutFile.setPadding(Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10));
        RelativeLayoutFile.setBackground(DrawableBorder);
        RelativeLayoutFile.setVisibility(View.GONE);
        RelativeLayoutFile.setId(ID1_FILE_LAYOUT);

        RelativeLayoutContent.addView(RelativeLayoutFile);

        RelativeLayout.LayoutParams RelativeLayoutFile2Param = new RelativeLayout.LayoutParams(Misc.ToDP(50), Misc.ToDP(50));
        RelativeLayoutFile2Param.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

        GradientDrawable DrawableFile = new GradientDrawable();
        DrawableFile.setColor(Misc.Color(R.color.Primary));
        DrawableFile.setShape(GradientDrawable.OVAL);

        RelativeLayout RelativeLayoutFile2 = new RelativeLayout(Activity);
        RelativeLayoutFile2.setLayoutParams(RelativeLayoutFile2Param);
        RelativeLayoutFile2.setBackground(DrawableFile);
        RelativeLayoutFile2.setId(Misc.generateViewId());

        RelativeLayoutFile.addView(RelativeLayoutFile2);

        final ImageView ImageViewFile = new ImageView(Activity);
        ImageViewFile.setLayoutParams(new RelativeLayout.LayoutParams(Misc.ToDP(50), Misc.ToDP(50)));
        ImageViewFile.setPadding(Misc.ToDP(14), Misc.ToDP(14), Misc.ToDP(14), Misc.ToDP(14));
        ImageViewFile.setImageResource(R.drawable._general_download);
        ImageViewFile.setId(ID1_FILE_IMAGE);

        RelativeLayoutFile2.addView(ImageViewFile);

        final TextView TextViewFile = new TextView(Activity, 12, true);
        TextViewFile.setLayoutParams(new RelativeLayout.LayoutParams(Misc.ToDP(50), Misc.ToDP(50)));
        TextViewFile.setGravity(Gravity.CENTER);
        TextViewFile.setPadding(0, Misc.ToDP(5), 0, 0);
        TextViewFile.setTextColor(Color.WHITE);
        TextViewFile.setId(ID1_FILE_TEXT);

        RelativeLayoutFile2.addView(TextViewFile);

        RelativeLayout.LayoutParams TextViewFileNameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewFileNameParam.addRule(RelativeLayout.RIGHT_OF, RelativeLayoutFile2.getId());
        TextViewFileNameParam.setMargins(Misc.ToDP(8), Misc.ToDP(2), 0, 0);

        final TextView TextViewFileName = new TextView(Activity, 14, false);
        TextViewFileName.setLayoutParams(TextViewFileNameParam);
        TextViewFileName.SetColor(R.color.TextWhite);
        TextViewFileName.setId(ID1_FILE_NAME);

        RelativeLayoutFile.addView(TextViewFileName);

        RelativeLayout.LayoutParams TextViewFileDetailParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewFileDetailParam.addRule(RelativeLayout.RIGHT_OF, RelativeLayoutFile2.getId());
        TextViewFileDetailParam.addRule(RelativeLayout.BELOW, ID1_FILE_NAME);
        TextViewFileDetailParam.setMargins(Misc.ToDP(8),0, 0, 0);

        final TextView TextViewFileDetail = new TextView(Activity, 12, false);
        TextViewFileDetail.setLayoutParams(TextViewFileDetailParam);
        TextViewFileDetail.SetColor(R.color.Gray);
        TextViewFileDetail.setId(ID1_FILE_DETAIL);

        RelativeLayoutFile.addView(TextViewFileDetail);

        RelativeLayout.LayoutParams RelativeLayoutToolParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(40));
        RelativeLayoutToolParam.addRule(RelativeLayout.BELOW, RelativeLayoutContent.getId());
        RelativeLayoutToolParam.addRule(RelativeLayout.RIGHT_OF, ID1_PROFILE);

        RelativeLayout RelativeLayoutTool = new RelativeLayout(Activity);
        RelativeLayoutTool.setLayoutParams(RelativeLayoutToolParam);

        RelativeLayoutScroll.addView(RelativeLayoutTool);

        RelativeLayout.LayoutParams ImageViewOptionParam = new RelativeLayout.LayoutParams(Misc.ToDP(32), RelativeLayout.LayoutParams.MATCH_PARENT);
        ImageViewOptionParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        final ImageView ImageViewOption = new ImageView(Activity);
        ImageViewOption.setLayoutParams(ImageViewOptionParam);
        ImageViewOption.setPadding(Misc.ToDP(4), Misc.ToDP(4), Misc.ToDP(4), Misc.ToDP(4));
        ImageViewOption.setImageResource(R.drawable._inbox_option);
        ImageViewOption.setId(ID1_OPTION);

        RelativeLayoutTool.addView(ImageViewOption);

        RelativeLayout.LayoutParams TextViewLikeParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        TextViewLikeParam.addRule(RelativeLayout.LEFT_OF, ID1_OPTION);

        final TextView TextViewLike = new TextView(Activity, 12, false);
        TextViewLike.setLayoutParams(TextViewLikeParam);
        TextViewLike.setPadding(Misc.ToDP(5), Misc.ToDP(4), Misc.ToDP(5), 0);
        TextViewLike.setGravity(Gravity.CENTER_VERTICAL);
        TextViewLike.SetColor(R.color.Gray);
        TextViewLike.setId(ID1_LIKE_COUNT);

        RelativeLayoutTool.addView(TextViewLike);

        RelativeLayout.LayoutParams ImageViewLikeParam = new RelativeLayout.LayoutParams(Misc.ToDP(32), RelativeLayout.LayoutParams.MATCH_PARENT);
        ImageViewLikeParam.addRule(RelativeLayout.LEFT_OF, ID1_LIKE_COUNT);

        final ImageView ImageViewLike = new ImageView(Activity);
        ImageViewLike.setLayoutParams(ImageViewLikeParam);
        ImageViewLike.setPadding(Misc.ToDP(3), Misc.ToDP(3), Misc.ToDP(3), Misc.ToDP(3));
        ImageViewLike.setImageResource(R.drawable._general_like);
        ImageViewLike.setId(ID1_LIKE);

        RelativeLayoutTool.addView(ImageViewLike);

        RelativeLayout.LayoutParams TextViewCommentParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        TextViewCommentParam.addRule(RelativeLayout.LEFT_OF, ID1_LIKE);

        final TextView TextViewComment = new TextView(Activity, 12, false);
        TextViewComment.setLayoutParams(TextViewCommentParam);
        TextViewComment.setPadding(Misc.ToDP(5), Misc.ToDP(4), Misc.ToDP(5), 0);
        TextViewComment.setGravity(Gravity.CENTER_VERTICAL);
        TextViewComment.SetColor(R.color.Gray);
        TextViewComment.setId(ID1_COMMENT_COUNT);

        RelativeLayoutTool.addView(TextViewComment);

        RelativeLayout.LayoutParams ImageViewCommentParam = new RelativeLayout.LayoutParams(Misc.ToDP(32), RelativeLayout.LayoutParams.MATCH_PARENT);
        ImageViewCommentParam.addRule(RelativeLayout.LEFT_OF, ID1_COMMENT_COUNT);

        final ImageView ImageViewComment = new ImageView(Activity);
        ImageViewComment.setLayoutParams(ImageViewCommentParam);
        ImageViewComment.setPadding(Misc.ToDP(4), Misc.ToDP(4), Misc.ToDP(4), Misc.ToDP(4));
        ImageViewComment.setImageResource(R.drawable._inbox_comment);
        ImageViewComment.setId(ID1_COMMENT);

        RelativeLayoutTool.addView(ImageViewComment);

        RelativeLayout.LayoutParams CircleImageViewPerson1Param = new RelativeLayout.LayoutParams(Misc.ToDP(24), Misc.ToDP(24));
        CircleImageViewPerson1Param.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        CircleImageViewPerson1Param.addRule(RelativeLayout.CENTER_VERTICAL);

        final CircleImageView CircleImageViewPerson1 = new CircleImageView(Activity);
        CircleImageViewPerson1.setLayoutParams(CircleImageViewPerson1Param);
        CircleImageViewPerson1.setId(ID1_PERSON1);
        CircleImageViewPerson1.SetBorderWidth(Misc.ToDP(1));
        CircleImageViewPerson1.SetBorderColor(R.color.LineWhite);

        RelativeLayoutTool.addView(CircleImageViewPerson1);

        RelativeLayout.LayoutParams CircleImageViewPerson2Param = new RelativeLayout.LayoutParams(Misc.ToDP(24), Misc.ToDP(24));
        CircleImageViewPerson2Param.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        CircleImageViewPerson2Param.addRule(RelativeLayout.CENTER_VERTICAL);
        CircleImageViewPerson2Param.setMargins(Misc.ToDP(30), 0, 0, 0);

        final CircleImageView CircleImageViewPerson2 = new CircleImageView(Activity);
        CircleImageViewPerson2.setLayoutParams(CircleImageViewPerson2Param);
        CircleImageViewPerson2.setId(ID1_PERSON2);

        RelativeLayoutTool.addView(CircleImageViewPerson2);

        RelativeLayout.LayoutParams CircleImageViewPerson3Param = new RelativeLayout.LayoutParams(Misc.ToDP(24), Misc.ToDP(24));
        CircleImageViewPerson3Param.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        CircleImageViewPerson3Param.addRule(RelativeLayout.CENTER_VERTICAL);
        CircleImageViewPerson3Param.setMargins(Misc.ToDP(60), 0, 0, 0);

        final CircleImageView CircleImageViewPerson3 = new CircleImageView(Activity);
        CircleImageViewPerson3.setLayoutParams(CircleImageViewPerson3Param);
        CircleImageViewPerson3.setId(ID1_PERSON3);

        RelativeLayoutTool.addView(CircleImageViewPerson3);

        RelativeLayout.LayoutParams CircleImageViewPerson4Param = new RelativeLayout.LayoutParams(Misc.ToDP(24), Misc.ToDP(24));
        CircleImageViewPerson4Param.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        CircleImageViewPerson4Param.addRule(RelativeLayout.CENTER_VERTICAL);
        CircleImageViewPerson4Param.setMargins(Misc.ToDP(90), 0, 0, 0);

        final CircleImageView CircleImageViewPerson4 = new CircleImageView(Activity);
        CircleImageViewPerson4.setLayoutParams(CircleImageViewPerson4Param);
        CircleImageViewPerson4.setId(ID1_PERSON4);

        RelativeLayoutTool.addView(CircleImageViewPerson4);

        RelativeLayout.LayoutParams ViewLine2Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1));
        ViewLine2Param.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        ViewLine2Param.setMargins(0, Misc.ToDP(5), 0, 0);

        View ViewLine2 = new View(Activity);
        ViewLine2.setLayoutParams(ViewLine2Param);
        ViewLine2.setBackgroundResource(Misc.IsDark() ? R.color.LineDark : R.color.LineWhite);
        ViewLine2.setId(ID1_VIEW_LINE);

        RelativeLayoutScroll.addView(ViewLine2);

        RelativeLayout.LayoutParams LoadingViewMainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        LoadingViewMainParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        final LoadingView LoadingViewMain = new LoadingView(Activity);
        LoadingViewMain.setLayoutParams(LoadingViewMainParam);
        LoadingViewMain.setBackgroundResource(R.color.GroundWhite);
        LoadingViewMain.Start();

        RelativeLayoutScroll.addView(LoadingViewMain);

        final DBHandler DB = new DBHandler(Activity);
        Cursor cursor = DB.PostDetail(ID);

        while (cursor.moveToNext())
        {

            LoadingViewMain.Stop();
            LoadingViewMain.setVisibility(View.GONE);
        }

        cursor.close();

        AndroidNetworking.post(Misc.GetRandomServer("PostDetail"))
        .addQueryParameter("PostID", ID)
        .addHeaders("Token", Misc.GetString( "Token"))
        .setTag("Profile_UI")
        .build()
        .getAsString(new StringRequestListener()
        {
            @Override
            public void onResponse(String Response)
            {
                try
                {
                    JSONObject D = new JSONObject(Response);

                    if (D.getInt("Message") == 0)
                    {
                        final PostAdapter.PostStruct P = new PostAdapter.PostStruct();
                        P.ID = D.getString("ID");

                        if (!D.isNull("Profile"))
                            P.Profile = D.getString("Profile");

                        P.Name = D.getString("Name");

                        if (!D.isNull("Medal"))
                            P.Medal = D.getString("Medal");

                        P.Username = D.getString("Username");
                        P.Time = D.getInt("Time");

                        if (!D.isNull("Message"))
                            P.Message = D.getString("Message");

                        if (D.getInt("Type") != 0)
                        {
                            P.Type = D.getInt("Type");
                            P.Data = D.getString("Data");
                        }

                        P.Owner = D.getString("Owner");

                        if (!D.isNull("View"))
                            P.View = D.getInt("View");

                        P.Category = D.getInt("Category");
                        P.LikeCount = D.getInt("LikeCount");
                        P.CommentCount = D.getInt("CommentCount");
                        P.IsLike = D.getInt("Like") != 0;
                        P.IsFollow = D.getInt("Follow") != 0;

                        if (!D.isNull("Comment"))
                            P.IsComment = D.getInt("Comment") != 0;

                        P.IsBookmark = D.getInt("Bookmark") != 0;

                        if (!D.isNull("I1"))
                        {
                            P.Person1ID = D.getString("I1");
                            P.Person1Avatar = D.getString("I1P");
                        }

                        if (!D.isNull("I2"))
                        {
                            P.Person2ID = D.getString("I2");
                            P.Person2Avatar = D.getString("I2P");
                        }

                        if (!D.isNull("I3"))
                        {
                            P.Person3ID = D.getString("I3");
                            P.Person3Avatar = D.getString("I3P");
                        }

                        if (!D.isNull("I4"))
                        {
                            P.Person4ID = D.getString("I4");
                            P.Person4Avatar = D.getString("I4P");
                        }

                        DB.InboxUpdate(P);


                        GlideApp.with(Activity).load(P.Profile).placeholder(R.drawable._general_avatar).into(CircleImageViewProfile);
                        CircleImageViewProfile.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                // TODO Open Profile
                            }
                        });

                        TextViewName.setText(P.Name);
                        TextViewName.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                // TODO Open Profile
                            }
                        });

                        if (P.Medal == null || P.Medal.isEmpty())
                            ImageViewMedal.setVisibility(View.GONE);
                        else
                        {
                            ImageViewMedal.setVisibility(View.VISIBLE);
                            GlideApp.with(Activity).load(P.Medal).into(ImageViewMedal);
                        }

                        TextViewUsername.setText(("@" + P.Username));
                        TextViewUsername.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                // TODO Open Profile
                            }
                        });

                        TextViewTime.setText(Misc.TimeAgo(P.Time));

                        if (P.Message == null || P.Message.isEmpty())
                            TextViewMessage.setVisibility(View.GONE);
                        else
                        {
                            TextViewMessage.setVisibility(View.VISIBLE);
                            TextViewMessage.setText(P.Message);

                            TagHandler.Show(TextViewMessage);
                        }

                        RelativeLayoutVote.setVisibility(View.GONE);
                        RelativeLayoutVideo.setVisibility(View.GONE);
                        RelativeLayoutImage.setVisibility(View.GONE);
                        RelativeLayoutFile.setVisibility(View.GONE);

                        switch (P.Type)
                        {
                            case 1:
                            {
                                RelativeLayoutImage.setVisibility(View.VISIBLE);
                                ImageViewSingle.setVisibility(View.GONE);
                                LinearLayoutDouble.setVisibility(View.GONE);
                                LinearLayoutTriple.setVisibility(View.GONE);

                                JSONArray URL = new JSONArray(P.Data);

                                switch (URL.length())
                                {
                                    case 1:
                                    {
                                        final String URL1 = URL.get(0).toString();

                                        ImageViewSingle.setVisibility(View.VISIBLE);
                                        ImageViewSingle.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { Activity.GetManager().OpenView(new ImagePreviewUI(URL1, true), R.id.ContainerFull, "ImagePreviewUI"); } });

                                        GlideApp.with(Activity).load(URL1).placeholder(R.color.LineWhite).transforms(new CenterCrop(), new RoundedCorners(Misc.ToDP(6))).into(ImageViewSingle);
                                    }
                                    break;
                                    case 2:
                                    {
                                        final String URL1 = URL.get(0).toString();
                                        final String URL2 = URL.get(1).toString();

                                        LinearLayoutDouble.setVisibility(View.VISIBLE);

                                        ImageViewDouble1.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { Activity.GetManager().OpenView(new ImagePreviewUI(URL1, URL2, true), R.id.ContainerFull, "ImagePreviewUI"); } });
                                        ImageViewDouble2.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { Activity.GetManager().OpenView(new ImagePreviewUI(URL2, URL1, true), R.id.ContainerFull, "ImagePreviewUI"); } });

                                        GlideApp.with(Activity).load(URL1).placeholder(R.color.LineWhite).transforms(new CenterCrop(), new RoundedCorners(Misc.ToDP(6))).into(ImageViewDouble1);
                                        GlideApp.with(Activity).load(URL2).placeholder(R.color.LineWhite).transforms(new CenterCrop(), new RoundedCorners(Misc.ToDP(6))).into(ImageViewDouble2);
                                    }
                                    break;
                                    case 3:
                                    {
                                        final String URL1 = URL.get(0).toString();
                                        final String URL2 = URL.get(1).toString();
                                        final String URL3 = URL.get(2).toString();

                                        LinearLayoutTriple.setVisibility(View.VISIBLE);

                                        ImageViewTriple1.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { Activity.GetManager().OpenView(new ImagePreviewUI(URL1, URL2, URL3, true), R.id.ContainerFull, "ImagePreviewUI"); } });
                                        ImageViewTriple2.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { Activity.GetManager().OpenView(new ImagePreviewUI(URL2, URL3, URL1, true), R.id.ContainerFull, "ImagePreviewUI"); } });
                                        ImageViewTriple3.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { Activity.GetManager().OpenView(new ImagePreviewUI(URL3, URL1, URL2, true), R.id.ContainerFull, "ImagePreviewUI"); } });

                                        GlideApp.with(Activity).load(URL1).placeholder(R.color.LineWhite).transforms(new CenterCrop(), new RoundedCorners(Misc.ToDP(6))).into(ImageViewTriple1);
                                        GlideApp.with(Activity).load(URL2).placeholder(R.color.LineWhite).transforms(new CenterCrop(), new RoundedCorners(Misc.ToDP(6))).into(ImageViewTriple2);
                                        GlideApp.with(Activity).load(URL3).placeholder(R.color.LineWhite).transforms(new CenterCrop(), new RoundedCorners(Misc.ToDP(6))).into(ImageViewTriple3);
                                    }
                                    break;
                                }
                            }
                            break;
                            case 2:
                            {
                                JSONObject Video = new JSONObject(P.Data);

                                final String URL = Video.getString("URL");

                                // TODO Add Video Size

                                RelativeLayoutVideo.setVisibility(View.VISIBLE);

                                ImageViewVideo.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { Activity.GetManager().OpenView(new VideoPreviewUI(URL, false, true), R.id.ContainerFull, "VideoPreviewUI"); } });

                                GlideApp.with(Activity).load(URL.substring(0, URL.length() - 3) + "png").placeholder(R.color.Gray).transforms(new CenterCrop()).into(ImageViewVideo);

                                int Time = Integer.parseInt(Video.getString("Duration")) / 1000;
                                int Min = Time / 60;
                                int Sec = Time - (Min * 60);

                                TextViewDuration.setText(((Min < 10 ?  "0" : "") + String.valueOf(Min) + ":" + (Sec < 10 ?  "0" : "") + String.valueOf(Sec)));
                            }
                            break;
                            case 3:
                            {
                                RelativeLayoutVote.setVisibility(View.VISIBLE);
                                LinearLayoutVote1.setVisibility(View.GONE);
                                RelativeLayoutVoteType2.setVisibility(View.GONE);

                                final JSONObject Vote = new JSONObject(P.Data);

                                if (Vote.isNull("Vote"))
                                {
                                    LinearLayoutVote1.setVisibility(View.VISIBLE);

                                    TextViewType1Text1.setText(Vote.getString("Vote1"));
                                    TextViewType1Text2.setText(Vote.getString("Vote2"));

                                    if (Vote.isNull("Vote3"))
                                        LinearLayoutVoteLin3.setVisibility(View.GONE);
                                    else
                                    {
                                        LinearLayoutVoteLin3.setVisibility(View.VISIBLE);
                                        TextViewType1Text3.setText(Vote.getString("Vote3"));
                                    }

                                    if (Vote.isNull("Vote4"))
                                        LinearLayoutVoteLin4.setVisibility(View.GONE);
                                    else
                                    {
                                        LinearLayoutVoteLin4.setVisibility(View.VISIBLE);
                                        TextViewType1Text4.setText(Vote.getString("Vote4"));
                                    }

                                    if (Vote.isNull("Vote5"))
                                        LinearLayoutVoteLin5.setVisibility(View.GONE);
                                    else
                                    {
                                        LinearLayoutVoteLin5.setVisibility(View.VISIBLE);
                                        TextViewType1Text5.setText(Vote.getString("Vote5"));
                                    }

                                    int Total = 0;

                                    if (!Vote.isNull("Count1"))
                                        Total += Vote.getInt("Count1");

                                    if (!Vote.isNull("Count2"))
                                        Total += Vote.getInt("Count2");

                                    if (!Vote.isNull("Count3"))
                                        Total += Vote.getInt("Count3");

                                    if (!Vote.isNull("Count4"))
                                        Total += Vote.getInt("Count4");

                                    if (!Vote.isNull("Count5"))
                                        Total += Vote.getInt("Count5");

                                    TextViewType1Result.setText((Total + " " + Misc.String(R.string.PostAdapterVotes)));
                                    TextViewType1Time.setText(Misc.TimeLeft(Vote.getInt("Time")));

                                    View.OnClickListener VoteSelection = new View.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(View v)
                                        {
                                            GradientDrawable Select = new GradientDrawable();
                                            Select.setStroke(Misc.ToDP(1), Misc.Color(R.color.Gray));
                                            Select.setShape(GradientDrawable.OVAL);

                                            GradientDrawable Circle = new GradientDrawable();
                                            Circle.setStroke(Misc.ToDP(7), Color.TRANSPARENT);
                                            Circle.setShape(GradientDrawable.OVAL);
                                            Circle.setColor(Color.BLACK);

                                            LayerDrawable Layer = new LayerDrawable(new Drawable[] { Select, Circle });

                                            ViewType1Sel1.setBackground(Select);
                                            ViewType1Sel2.setBackground(Select);
                                            ViewType1Sel3.setBackground(Select);
                                            ViewType1Sel4.setBackground(Select);
                                            ViewType1Sel5.setBackground(Select);

                                            ViewType1Sel1.setTag("0");
                                            ViewType1Sel2.setTag("0");
                                            ViewType1Sel3.setTag("0");
                                            ViewType1Sel4.setTag("0");
                                            ViewType1Sel5.setTag("0");

                                            if (v.getId() == ID1_VOTE_TYPE1_LIN1)
                                            {
                                                ViewType1Sel1.setBackground(Layer);
                                                ViewType1Sel1.setTag("1");
                                            }
                                            else if (v.getId() == ID1_VOTE_TYPE1_LIN2)
                                            {
                                                ViewType1Sel2.setBackground(Layer);
                                                ViewType1Sel2.setTag("1");
                                            }
                                            else if (v.getId() == ID1_VOTE_TYPE1_LIN3)
                                            {
                                                ViewType1Sel3.setBackground(Layer);
                                                ViewType1Sel3.setTag("1");
                                            }
                                            else if (v.getId() == ID1_VOTE_TYPE1_LIN4)
                                            {
                                                ViewType1Sel4.setBackground(Layer);
                                                ViewType1Sel4.setTag("1");
                                            }
                                            else if (v.getId() == ID1_VOTE_TYPE1_LIN5)
                                            {
                                                ViewType1Sel5.setBackground(Layer);
                                                ViewType1Sel5.setTag("1");
                                            }
                                        }
                                    };

                                    LinearLayoutVoteLin1.setOnClickListener(VoteSelection);
                                    LinearLayoutVoteLin2.setOnClickListener(VoteSelection);
                                    LinearLayoutVoteLin3.setOnClickListener(VoteSelection);
                                    LinearLayoutVoteLin4.setOnClickListener(VoteSelection);
                                    LinearLayoutVoteLin5.setOnClickListener(VoteSelection);

                                    TextViewType1Submit.setOnClickListener(new View.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(View v)
                                        {
                                            int S = 0;

                                            if (ViewType1Sel1.getTag() == "1")
                                                S = 1;
                                            else if (ViewType1Sel2.getTag() == "1")
                                                S = 2;
                                            else if (ViewType1Sel3.getTag() == "1")
                                                S = 3;
                                            else if (ViewType1Sel4.getTag() == "1")
                                                S = 4;
                                            else if (ViewType1Sel5.getTag() == "1")
                                                S = 5;

                                            if (S == 0)
                                                return;

                                            final int Sel = S;

                                            AndroidNetworking.post(Misc.GetRandomServer("PostVote"))
                                            .addBodyParameter("Post", P.ID)
                                            .addBodyParameter("Vote", String.valueOf(Sel))
                                            .addHeaders("Token", Misc.GetString( "Token"))
                                            .setTag("PostUI")
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
                                                            JSONObject Data = new JSONObject();

                                                            Data.put("Vote" , Sel);
                                                            Data.put("Count1" , Result.getInt("Count1"));
                                                            Data.put("Count2" , Result.getInt("Count2"));

                                                            if (!Result.isNull("Count3"))
                                                                Data.put("Count3" , Result.getString("Count3"));

                                                            if (!Result.isNull("Count4"))
                                                                Data.put("Count4" , Result.getString("Count4"));

                                                            if (!Result.isNull("Count5"))
                                                                Data.put("Count5" , Result.getString("Count5"));

                                                            Data.put("Vote1" , Vote.getString("Vote1"));
                                                            Data.put("Vote2" , Vote.getString("Vote2"));

                                                            if (!Vote.isNull("Vote3"))
                                                                Data.put("Vote3" , Vote.getString("Vote3"));

                                                            if (!Vote.isNull("Vote4"))
                                                                Data.put("Vote4" , Vote.getString("Vote4"));

                                                            if (!Vote.isNull("Vote5"))
                                                                Data.put("Vote5" , Vote.getString("Vote5"));

                                                            P.Data = Data.toString();

                                                            // TODO Notify Change
                                                        }
                                                    }
                                                    catch (Exception e)
                                                    {
                                                        Misc.Debug("PostAdapter-Vote: " + e.toString());
                                                    }
                                                }

                                                @Override public void onError(ANError e) { }
                                            });
                                        }
                                    });
                                }
                                else
                                {
                                    RelativeLayoutVoteType2.setVisibility(View.VISIBLE);

                                    int C1 = Vote.isNull("Count1") ? 0 : Vote.getInt("Count1");
                                    int C2 = Vote.isNull("Count2") ? 0 : Vote.getInt("Count2");
                                    int C3 = Vote.isNull("Count3") ? 0 : Vote.getInt("Count3");
                                    int C4 = Vote.isNull("Count4") ? 0 : Vote.getInt("Count4");
                                    int C5 = Vote.isNull("Count5") ? 0 : Vote.getInt("Count5");

                                    int Total = C1 + C2 + C3 + C4 + C5;

                                    TextViewType2Text1.setText(Vote.getString("Vote1"));
                                    TextViewType2Text1.FillBackground(C1 == 0 ? 0 : C1 * 100 / Total);
                                    TextViewType2Per1.setText((C1 == 0 ? "0%" : String.valueOf(C1 * 100 / Total) + "%"));

                                    TextViewType2Text2.setText(Vote.getString("Vote2"));
                                    TextViewType2Text2.FillBackground(C2 == 0 ? 0 : C2 * 100 / Total);
                                    TextViewType2Per2.setText((C2 == 0 ? "0%" : String.valueOf(C2 * 100 / Total) + "%"));

                                    if (Vote.isNull("Vote3"))
                                    {
                                        TextViewType2Per3.setVisibility(View.GONE);
                                        TextViewType2Text3.setVisibility(View.GONE);
                                    }
                                    else
                                    {
                                        TextViewType2Per3.setVisibility(View.VISIBLE);
                                        TextViewType2Per3.setText((C3 == 0 ? "0%" : String.valueOf(C3 * 100 / Total) + "%"));

                                        TextViewType2Text3.setVisibility(View.VISIBLE);
                                        TextViewType2Text3.setText(Vote.getString("Vote3"));
                                        TextViewType2Text3.FillBackground(C3 == 0 ? 0 : C3 * 100 / Total);
                                    }

                                    if (Vote.isNull("Vote4"))
                                    {
                                        TextViewType2Per4.setVisibility(View.GONE);
                                        TextViewType2Text4.setVisibility(View.GONE);
                                    }
                                    else
                                    {
                                        TextViewType2Per4.setVisibility(View.VISIBLE);
                                        TextViewType2Per4.setText((C4 == 0 ? "0%" : String.valueOf(C4 * 100 / Total) + "%"));

                                        TextViewType2Text4.setVisibility(View.VISIBLE);
                                        TextViewType2Text4.setText(Vote.getString("Vote4"));
                                        TextViewType2Text4.FillBackground(C4 == 0 ? 0 : C4 * 100 / Total);
                                    }

                                    if (Vote.isNull("Vote5"))
                                    {
                                        TextViewType2Per5.setVisibility(View.GONE);
                                        TextViewType2Text5.setVisibility(View.GONE);
                                    }
                                    else
                                    {
                                        TextViewType2Per5.setVisibility(View.VISIBLE);
                                        TextViewType2Per5.setText((C5 == 0 ? "0%" : String.valueOf(C5 * 100 / Total) + "%"));

                                        TextViewType2Text5.setVisibility(View.VISIBLE);
                                        TextViewType2Text5.setText(Vote.getString("Vote5"));
                                        TextViewType2Text5.FillBackground(C5 == 0 ? 0 : C5 * 100 / Total);
                                    }

                                    int ID = 0;
                                    ImageViewType2Select.setVisibility(View.VISIBLE);

                                    switch (Vote.getInt("Vote"))
                                    {
                                        case 0: ImageViewType2Select.setVisibility(View.GONE); break;
                                        case 2: ID = TextViewType2Text1.getId(); break;
                                        case 3: ID = TextViewType2Text2.getId(); break;
                                        case 4: ID = TextViewType2Text3.getId(); break;
                                        case 5: ID = TextViewType2Text5.getId(); break;
                                    }

                                    RelativeLayout.LayoutParams Param = (RelativeLayout.LayoutParams) ImageViewType2Select.getLayoutParams();
                                    Param.setMargins(-Misc.ToDP(10), 0, Misc.ToDP(10), 0);

                                    if (ID != 0)
                                        Param.addRule(RelativeLayout.BELOW, ID);

                                    ImageViewType2Select.setLayoutParams(Param);
                                    TextViewType2Result.setText((Total + " " + Misc.String(R.string.PostAdapterVotes)));
                                    TextViewType2Time.setText(Misc.TimeLeft(Vote.getInt("Time")));
                                }
                            }
                            break;
                            case 4:
                            {
                                final JSONObject file = new JSONObject(P.Data);
                                final String URL = file.getString("URL");
                                final String FileName = file.getString("Name");

                                String Name = file.getString("Name").length() <= 15 ? file.getString("Name") : file.getString("Name").substring(0, Math.min(file.getString("Name").length(), 15)) + "...";
                                String Details = new DecimalFormat("####.##").format(((double) file.getInt("Size") / 1024 / 1024)) + " MB / " + file.getString("Ext").toUpperCase().substring(1);

                                RelativeLayoutFile.setVisibility(View.VISIBLE);
                                TextViewFileName.setText(Name);
                                TextViewFileDetail.setText(Details);

                                final File Download = new File(CacheHandler.Dir(CacheHandler.DIR_DOWNLOAD), FileName);

                                if (P.IsDownloading)
                                {
                                    ImageViewFile.setVisibility(View.GONE);
                                    TextViewFile.setVisibility(View.VISIBLE);
                                }
                                else if (Download.exists())
                                {
                                    TextViewFile.setVisibility(View.GONE);
                                    ImageViewFile.setVisibility(View.VISIBLE);
                                    ImageViewFile.setImageResource(R.drawable._inbox_downloaded);
                                    ImageViewFile.setPadding(Misc.ToDP(7), Misc.ToDP(7), Misc.ToDP(7), Misc.ToDP(7));
                                }
                                else
                                {
                                    TextViewFile.setVisibility(View.GONE);
                                    ImageViewFile.setVisibility(View.VISIBLE);
                                    ImageViewFile.setImageResource(R.drawable._general_download);
                                    ImageViewFile.setPadding(Misc.ToDP(14), Misc.ToDP(14), Misc.ToDP(14), Misc.ToDP(14));
                                }

                                RelativeLayoutFile.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View view)
                                    {
                                        if (P.IsDownloading)
                                        {
                                            final Dialog DialogCancel = new Dialog(Activity);
                                            DialogCancel.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                            DialogCancel.setCancelable(true);

                                            LinearLayout LinearLayoutMain = new LinearLayout(Activity);
                                            LinearLayoutMain.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                                            LinearLayoutMain.setBackgroundResource(Misc.IsDark() ? R.color.GroundDark : R.color.GroundWhite);
                                            LinearLayoutMain.setOrientation(LinearLayout.VERTICAL);

                                            TextView TextViewTitle = new TextView(Activity, 14, false);
                                            TextViewTitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56)));
                                            TextViewTitle.SetColor(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite);
                                            TextViewTitle.setPadding(Misc.ToDP(15), 0, Misc.ToDP(15), 0);
                                            TextViewTitle.setText(Misc.String(R.string.PostAdapterCancel));
                                            TextViewTitle.setGravity(Gravity.CENTER_VERTICAL);

                                            LinearLayoutMain.addView(TextViewTitle);

                                            View ViewLine = new View(Activity);
                                            ViewLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1)));
                                            ViewLine.setBackgroundResource(R.color.LineWhite);

                                            LinearLayoutMain.addView(ViewLine);

                                            LinearLayout LinearLayoutButton = new LinearLayout(Activity);
                                            LinearLayoutButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56)));
                                            LinearLayoutButton.setOrientation(LinearLayout.HORIZONTAL);

                                            LinearLayoutMain.addView(LinearLayoutButton);

                                            TextView TextViewYes = new TextView(Activity, 14, false);
                                            TextViewYes.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
                                            TextViewYes.SetColor(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite);
                                            TextViewYes.setText(Misc.String(R.string.PostAdapterYes));
                                            TextViewYes.setGravity(Gravity.CENTER);
                                            TextViewYes.setOnClickListener(new View.OnClickListener()
                                            {
                                                @Override
                                                public void onClick(View view)
                                                {
                                                    AndroidNetworking.forceCancel(P.ID);

                                                    TextViewFile.setVisibility(View.GONE);
                                                    ImageViewFile.setVisibility(View.VISIBLE);

                                                    P.IsDownloading = false;

                                                    DialogCancel.dismiss();
                                                }
                                            });

                                            TextView TextViewNo = new TextView(Activity, 14, false);
                                            TextViewNo.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
                                            TextViewNo.SetColor(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite);
                                            TextViewNo.setText(Misc.String(R.string.PostAdapterNo));
                                            TextViewNo.setGravity(Gravity.CENTER);
                                            TextViewNo.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { DialogCancel.dismiss(); } });

                                            if (Misc.IsRTL())
                                            {
                                                LinearLayoutButton.addView(TextViewNo);
                                                LinearLayoutButton.addView(TextViewYes);
                                            }
                                            else
                                            {
                                                LinearLayoutButton.addView(TextViewYes);
                                                LinearLayoutButton.addView(TextViewNo);
                                            }

                                            DialogCancel.setContentView(LinearLayoutMain);
                                            DialogCancel.show();
                                            return;
                                        }
                                        else if (Download.exists())
                                        {
                                            try
                                            {
                                                Uri URI = Uri.fromFile(Download);
                                                String URL = Download.getAbsolutePath();
                                                Intent I = new Intent(Intent.ACTION_VIEW);

                                                if (URL.contains(".doc") || URL.contains(".docx"))
                                                    I.setDataAndType(URI, "application/msword");
                                                else if (URL.contains(".pdf"))
                                                    I.setDataAndType(URI, "application/pdf");
                                                else if (URL.contains(".ppt") || URL.contains(".pptx"))
                                                    I.setDataAndType(URI, "application/vnd.ms-powerpoint");
                                                else if (URL.contains(".xls") ||URL.contains(".xlsx"))
                                                    I.setDataAndType(URI, "application/vnd.ms-excel");
                                                else if (URL.contains(".zip") || URL.contains(".rar"))
                                                    I.setDataAndType(URI, "application/x-wav");
                                                else if (URL.contains(".rtf"))
                                                    I.setDataAndType(URI, "application/rtf");
                                                else if (URL.contains(".wav") || URL.contains(".mp3"))
                                                    I.setDataAndType(URI, "audio/x-wav");
                                                else if (URL.contains(".gif"))
                                                    I.setDataAndType(URI, "image/gif");
                                                else if (URL.contains(".jpg") || URL.contains(".jpeg") || URL.contains(".png"))
                                                    I.setDataAndType(URI, "image/jpeg");
                                                else if (URL.contains(".txt"))
                                                    I.setDataAndType(URI, "text/plain");
                                                else if (URL.contains(".3gp") || URL.contains(".mpg") || URL.contains(".mpeg") || URL.contains(".mpe") || URL.contains(".mp4") || URL.contains(".avi"))
                                                    I.setDataAndType(URI, "video/*");
                                                else
                                                    I.setDataAndType(URI, "* / *");

                                                I.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                                Activity.startActivity(I);
                                            }
                                            catch (Exception e)
                                            {
                                                Misc.ToastOld(Misc.String(R.string.PostAdapterNoHandle));
                                            }

                                            return;
                                        }

                                        P.IsDownloading = true;

                                        ImageViewFile.setVisibility(View.GONE);
                                        TextViewFile.setVisibility(View.VISIBLE);
                                        TextViewFile.setText("0%");

                                        AndroidNetworking.download(URL, CacheHandler.Dir(CacheHandler.DIR_DOWNLOAD).getAbsolutePath(), FileName)
                                        .setPriority(Priority.MEDIUM)
                                        .setTag(P.ID)
                                        .build()
                                        .setDownloadProgressListener(new DownloadProgressListener()
                                        {
                                            @Override
                                            public void onProgress(long D, long T)
                                            {
                                                TextViewFile.setText((String.valueOf(D * 100 / T) + "%"));
                                            }
                                        })
                                        .startDownload(new DownloadListener()
                                        {
                                            @Override
                                            public void onDownloadComplete()
                                            {
                                                P.IsDownloading = false;

                                                TextViewFile.setVisibility(View.GONE);
                                                ImageViewFile.setVisibility(View.VISIBLE);
                                                ImageViewFile.setImageResource(R.drawable._inbox_downloaded);
                                                ImageViewFile.setPadding(Misc.ToDP(7), Misc.ToDP(7), Misc.ToDP(7), Misc.ToDP(7));
                                            }

                                            @Override
                                            public void onError(ANError e)
                                            {
                                                P.IsDownloading = false;

                                                TextViewFile.setVisibility(View.GONE);
                                                ImageViewFile.setVisibility(View.VISIBLE);
                                                ImageViewFile.setImageResource(R.drawable._general_download);
                                            }
                                        });
                                    }
                                });
                            }
                            break;
                        }

                        if (P.Person1Avatar != null && !P.Person1Avatar.isEmpty())
                        {
                            GlideApp.with(Activity).load(P.Person1Avatar).into(CircleImageViewPerson1);
                            CircleImageViewPerson1.setVisibility(View.VISIBLE);
                            CircleImageViewPerson1.setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View view)
                                {
                                    // TODO Open Profile
                                }
                            });
                        }
                        else
                            CircleImageViewPerson1.setVisibility(View.GONE);

                        if (P.Person2Avatar != null && !P.Person2Avatar.isEmpty())
                        {
                            GlideApp.with(Activity).load(P.Person2Avatar).into(CircleImageViewPerson2);
                            CircleImageViewPerson2.setVisibility(View.VISIBLE);
                            CircleImageViewPerson2.setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View view)
                                {
                                    // TODO Open Profile
                                }
                            });
                        }
                        else
                            CircleImageViewPerson2.setVisibility(View.GONE);

                        if (P.Person3Avatar != null && !P.Person3Avatar.isEmpty())
                        {
                            GlideApp.with(Activity).load(P.Person3Avatar).into(CircleImageViewPerson3);
                            CircleImageViewPerson3.setVisibility(View.VISIBLE);
                            CircleImageViewPerson3.setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View view)
                                {
                                    // TODO Open Profile
                                }
                            });
                        }
                        else
                            CircleImageViewPerson3.setVisibility(View.GONE);

                        if (P.Person4Avatar != null && !P.Person4Avatar.isEmpty())
                        {
                            GlideApp.with(Activity).load(P.Person4Avatar).into(CircleImageViewPerson4);
                            CircleImageViewPerson4.setVisibility(View.VISIBLE);
                            CircleImageViewPerson4.setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View view)
                                {
                                    // TODO Open Profile
                                }
                            });
                        }
                        else
                            CircleImageViewPerson4.setVisibility(View.GONE);

                        if (P.IsLike)
                        {
                            TextViewLike.SetColor(R.color.Red);
                            GlideApp.with(Activity).load(R.drawable._general_like_red).into(ImageViewLike);
                        }
                        else
                        {
                            TextViewLike.SetColor(R.color.Gray);
                            GlideApp.with(Activity).load(R.drawable._general_like).into(ImageViewLike);
                        }

                        ImageViewLike.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                if (P.IsLike)
                                {
                                    TextViewLike.SetColor(R.color.Gray);
                                    ImageViewLike.setImageResource(R.drawable._general_like);

                                    ObjectAnimator Fade = ObjectAnimator.ofFloat(ImageViewLike, "alpha",  0.1f, 1f);
                                    Fade.setDuration(200);

                                    AnimatorSet AnimationSet = new AnimatorSet();
                                    AnimationSet.play(Fade);
                                    AnimationSet.start();

                                    P.DesLike();
                                    P.RevLike();

                                    TextViewLike.setText(String.valueOf(P.LikeCount));
                                }
                                else
                                {
                                    TextViewLike.SetColor(R.color.Red);
                                    ImageViewLike.setImageResource(R.drawable._general_like_red);

                                    ObjectAnimator SizeX = ObjectAnimator.ofFloat(ImageViewLike, "scaleX", 1.5f);
                                    SizeX.setDuration(200);

                                    ObjectAnimator SizeY = ObjectAnimator.ofFloat(ImageViewLike, "scaleY", 1.5f);
                                    SizeY.setDuration(200);

                                    ObjectAnimator Fade = ObjectAnimator.ofFloat(ImageViewLike, "alpha",  0.1f, 1f);
                                    Fade.setDuration(400);

                                    ObjectAnimator SizeX2 = ObjectAnimator.ofFloat(ImageViewLike, "scaleX", 1f);
                                    SizeX2.setDuration(200);
                                    SizeX2.setStartDelay(200);

                                    ObjectAnimator SizeY2 = ObjectAnimator.ofFloat(ImageViewLike, "scaleY", 1f);
                                    SizeY2.setDuration(200);
                                    SizeY2.setStartDelay(200);

                                    AnimatorSet AnimationSet = new AnimatorSet();
                                    AnimationSet.playTogether(SizeX, SizeY, Fade, SizeX2, SizeY2);
                                    AnimationSet.start();

                                    P.InsLike();
                                    P.RevLike();

                                    TextViewLike.setText(String.valueOf(P.LikeCount));
                                }

                                AndroidNetworking.post(Misc.GetRandomServer("PostLike"))
                                .addBodyParameter("PostID", P.ID)
                                .addHeaders("Token", Misc.GetString( "Token"))
                                .setTag("PostUI")
                                .build()
                                .getAsString(null);
                            }
                        });

                        TextViewLike.setText(String.valueOf(P.LikeCount));
                        TextViewLike.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { Activity.GetManager().OpenView(new LikeUI(P.ID, false), R.id.ContainerFull, "LikeUI"); } });

                        if (P.IsComment)
                        {
                            ImageViewComment.setVisibility(View.GONE);
                            TextViewComment.setVisibility(View.GONE);
                        }
                        else
                        {
                            ImageViewComment.setVisibility(View.VISIBLE);
                            ImageViewComment.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { Activity.GetManager().OpenView(new CommentUI(P), R.id.ContainerFull, "CommentUI"); } });

                            TextViewComment.setVisibility(View.VISIBLE);
                            TextViewComment.setText(String.valueOf(P.CommentCount));
                            TextViewComment.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { Activity.GetManager().OpenView(new CommentUI(P), R.id.ContainerFull, "CommentUI"); } });
                        }

                        ImageViewOption.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View view)
                            {
                                final Dialog DialogOption = new Dialog(Activity);
                                DialogOption.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                DialogOption.setCancelable(true);

                                LinearLayout LinearLayoutMain = new LinearLayout(Activity);
                                LinearLayoutMain.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                                LinearLayoutMain.setBackgroundResource(Misc.IsDark() ? R.color.GroundDark : R.color.GroundWhite);
                                LinearLayoutMain.setOrientation(LinearLayout.VERTICAL);

                                if (P.Owner.equals(SharedHandler.GetString( "ID")))
                                {
                                    TextView TextViewDelete = new TextView(Activity, 14, false);
                                    TextViewDelete.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56)));
                                    TextViewDelete.SetColor(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite);
                                    TextViewDelete.setPadding(Misc.ToDP(15), 0, Misc.ToDP(15), 0);
                                    TextViewDelete.setText(Misc.String(R.string.PostAdapterOptionDelete));
                                    TextViewDelete.setGravity(Gravity.CENTER_VERTICAL);
                                    TextViewDelete.setOnClickListener(new View.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(View v)
                                        {
                                            AndroidNetworking.post(Misc.GetRandomServer("PostDelete"))
                                            .addBodyParameter("PostID", P.ID)
                                            .addHeaders("Token", SharedHandler.GetString( "Token"))
                                            .setTag("PostUI")
                                            .build()
                                            .getAsString(new StringRequestListener()
                                            {
                                                @Override
                                                public void onResponse(String e)
                                                {
                                                    DB.InboxDelete(P.ID);
                                                    Misc.ToastOld(Activity.getString(R.string.PostAdapterPostDeleted));
                                                    Activity.onBackPressed();
                                                }

                                                @Override public void onError(ANError e) { }
                                            });

                                            DialogOption.dismiss();
                                        }
                                    });

                                    LinearLayoutMain.addView(TextViewDelete);

                                    if ((P.Time + 172800) > ((System.currentTimeMillis() / 1000)))
                                    {
                                        View ViewLine = new View(Activity);
                                        ViewLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1)));
                                        ViewLine.setBackgroundResource(R.color.LineWhite);

                                        LinearLayoutMain.addView(ViewLine);

                                        TextView TextViewEdit = new TextView(Activity, 14, false);
                                        TextViewEdit.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56)));
                                        TextViewEdit.SetColor(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite);
                                        TextViewEdit.setPadding(Misc.ToDP(15), 0, Misc.ToDP(15), 0);
                                        TextViewEdit.setText(Misc.String(R.string.PostAdapterOptionEdit));
                                        TextViewEdit.setGravity(Gravity.CENTER_VERTICAL);
                                        TextViewEdit.setOnClickListener(new View.OnClickListener()
                                        {
                                            @Override
                                            public void onClick(View v)
                                            {
                                                DialogOption.dismiss();

                                                final Dialog DialogEdit = new Dialog(Activity);
                                                DialogEdit.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                                DialogEdit.setCancelable(true);

                                                LinearLayout LinearLayoutMain = new LinearLayout(Activity);
                                                LinearLayoutMain.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                                                LinearLayoutMain.setBackgroundResource(Misc.IsDark() ? R.color.GroundDark : R.color.GroundWhite);
                                                LinearLayoutMain.setOrientation(LinearLayout.VERTICAL);

                                                TextView TextViewTitle = new TextView(Activity, 14, true);
                                                TextViewTitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56)));
                                                TextViewTitle.SetColor(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite);
                                                TextViewTitle.setPadding(Misc.ToDP(15), 0, Misc.ToDP(15), 0);
                                                TextViewTitle.setText(Misc.String(R.string.PostAdapterOptionEditTitle));
                                                TextViewTitle.setGravity(Gravity.CENTER_VERTICAL);

                                                LinearLayoutMain.addView(TextViewTitle);

                                                View ViewLine = new View(Activity);
                                                ViewLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1)));
                                                ViewLine.setBackgroundResource(R.color.LineWhite);

                                                LinearLayoutMain.addView(ViewLine);

                                                final EditText EditTextMessage = new EditText(Activity);
                                                EditTextMessage.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                                EditTextMessage.setPadding(Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10));
                                                EditTextMessage.setMinHeight(Misc.ToDP(56));
                                                EditTextMessage.setBackground(null);
                                                EditTextMessage.setText(P.Message);
                                                EditTextMessage.setHint(R.string.PostAdapterOptionEditMessage);
                                                EditTextMessage.setHintTextColor(Misc.Color(R.color.Gray));
                                                EditTextMessage.setSelection(EditTextMessage.getText().length());
                                                EditTextMessage.setTextColor(Misc.Color(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite));
                                                EditTextMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                                                EditTextMessage.setFilters(new InputFilter[] { new InputFilter.LengthFilter(300) });

                                                LinearLayoutMain.addView(EditTextMessage);

                                                View ViewLine2 = new View(Activity);
                                                ViewLine2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1)));
                                                ViewLine2.setBackgroundResource(R.color.LineWhite);

                                                LinearLayoutMain.addView(ViewLine2);

                                                TextView TextViewSubmit = new TextView(Activity, 14, true);
                                                TextViewSubmit.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56)));
                                                TextViewSubmit.SetColor(R.color.Primary);
                                                TextViewSubmit.setText(Misc.String(R.string.PostAdapterOptionEditSubmit));
                                                TextViewSubmit.setGravity(Gravity.CENTER);
                                                TextViewSubmit.setOnClickListener(new View.OnClickListener()
                                                {
                                                    @Override
                                                    public void onClick(View view)
                                                    {
                                                        AndroidNetworking.post(Misc.GetRandomServer("PostEdit"))
                                                        .addBodyParameter("PostID", P.ID)
                                                        .addBodyParameter("Message", EditTextMessage.getText().toString())
                                                        .addHeaders("Token", SharedHandler.GetString("Token"))
                                                        .setTag("PostUI")
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
                                                                        P.Message = Result.getString("Text");

                                                                        if (P.Message == null || P.Message.isEmpty())
                                                                            TextViewMessage.setVisibility(View.GONE);
                                                                        else
                                                                        {
                                                                            TextViewMessage.setVisibility(View.VISIBLE);
                                                                            TextViewMessage.setText(P.Message);

                                                                            TagHandler.Show(TextViewMessage);
                                                                        }

                                                                        DB.InboxMessage(P.ID, P.Message);

                                                                        // TODO Notify View Change
                                                                    }
                                                                }
                                                                catch (Exception e)
                                                                {
                                                                    Misc.Debug("PostAdapter-Edit: " + e.toString());
                                                                }
                                                            }

                                                            @Override public void onError(ANError e) { }
                                                        });

                                                        DialogEdit.dismiss();
                                                    }
                                                });

                                                LinearLayoutMain.addView(TextViewSubmit);

                                                DialogEdit.setContentView(LinearLayoutMain);
                                                DialogEdit.show();
                                            }
                                        });

                                        LinearLayoutMain.addView(TextViewEdit);
                                    }
                                }
                                else
                                {
                                    TextView TextViewFollow = new TextView(Activity, 14, false);
                                    TextViewFollow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56)));
                                    TextViewFollow.SetColor(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite);
                                    TextViewFollow.setPadding(Misc.ToDP(15), 0, Misc.ToDP(15), 0);
                                    TextViewFollow.setText(Misc.String(P.IsFollow ? R.string.PostAdapterOptionUnfollow : R.string.PostAdapterOptionFollow));
                                    TextViewFollow.setGravity(Gravity.CENTER_VERTICAL);
                                    TextViewFollow.setOnClickListener(new View.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(View v)
                                        {
                                            AndroidNetworking.post(Misc.GetRandomServer("ProfileFollow"))
                                            .addBodyParameter("Username", P.Username)
                                            .addHeaders("Token", SharedHandler.GetString( "Token"))
                                            .setTag("PostUI")
                                            .build()
                                            .getAsString(new StringRequestListener()
                                            {
                                                @Override
                                                public void onResponse(String e)
                                                {
                                                    P.IsFollow = !P.IsFollow;
                                                    Misc.ToastOld(P.Username + " " + (P.IsFollow ? Activity.getString(R.string.PostAdapterUserFollowed) : Activity.getString(R.string.PostAdapterUserFollowed)));
                                                }

                                                @Override public void onError(ANError e) { }
                                            });

                                            DialogOption.dismiss();
                                        }
                                    });

                                    LinearLayoutMain.addView(TextViewFollow);
                                }

                                View ViewLine = new View(Activity);
                                ViewLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1)));
                                ViewLine.setBackgroundResource(R.color.LineWhite);

                                LinearLayoutMain.addView(ViewLine);

                                TextView TextViewReport = new TextView(Activity, 14, false);
                                TextViewReport.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56)));
                                TextViewReport.SetColor(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite);
                                TextViewReport.setPadding(Misc.ToDP(15), 0, Misc.ToDP(15), 0);
                                TextViewReport.setText(Misc.String(R.string.PostAdapterOptionReport));
                                TextViewReport.setGravity(Gravity.CENTER_VERTICAL);
                                TextViewReport.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        // TODO Safe Report
                                    }
                                });

                                View ViewLine2 = new View(Activity);
                                ViewLine2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1)));
                                ViewLine2.setBackgroundResource(R.color.LineWhite);

                                if (!P.Owner.equals(SharedHandler.GetString( "ID")))
                                {
                                    LinearLayoutMain.addView(TextViewReport);
                                    LinearLayoutMain.addView(ViewLine2);
                                }

                                TextView TextViewCopy = new TextView(Activity, 14, false);
                                TextViewCopy.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56)));
                                TextViewCopy.SetColor(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite);
                                TextViewCopy.setPadding(Misc.ToDP(15), 0, Misc.ToDP(15), 0);
                                TextViewCopy.setText(Misc.String(R.string.PostAdapterOptionCopy));
                                TextViewCopy.setGravity(Gravity.CENTER_VERTICAL);
                                TextViewCopy.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        ClipboardManager clipboard = (ClipboardManager) Activity.getSystemService(Context.CLIPBOARD_SERVICE);
                                        ClipData clip = ClipData.newPlainText("InboxMessage:"+ P.ID, P.Message);

                                        if (clipboard != null)
                                            clipboard.setPrimaryClip(clip);

                                        Misc.ToastOld(Misc.String(R.string.PostAdapterOptionCopy2));
                                        DialogOption.dismiss();
                                    }
                                });

                                View ViewLine4 = new View(Activity);
                                ViewLine4.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1)));
                                ViewLine4.setBackgroundResource(R.color.LineWhite);

                                if (P.Message != null && !P.Message.isEmpty())
                                {

                                    LinearLayoutMain.addView(TextViewCopy);
                                    LinearLayoutMain.addView(ViewLine4);
                                }

                                TextView TextViewBookmark = new TextView(Activity, 14, false);
                                TextViewBookmark.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56)));
                                TextViewBookmark.SetColor(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite);
                                TextViewBookmark.setPadding(Misc.ToDP(15), 0, Misc.ToDP(15), 0);
                                TextViewBookmark.setText(Misc.String(P.IsBookmark ? R.string.PostAdapterOptionUnbookmark : R.string.PostAdapterOptionBookmark));
                                TextViewBookmark.setGravity(Gravity.CENTER_VERTICAL);
                                TextViewBookmark.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        AndroidNetworking.post(Misc.GetRandomServer("PostBookmark"))
                                        .addBodyParameter("PostID", P.ID)
                                        .addHeaders("Token", SharedHandler.GetString( "Token"))
                                        .setTag("PostUI")
                                        .build()
                                        .getAsString(new StringRequestListener()
                                        {
                                            @Override
                                            public void onResponse(String e)
                                            {
                                                P.IsBookmark = !P.IsBookmark;
                                                Misc.ToastOld(Activity.getString(R.string.PostAdapterPostBookmark));
                                            }

                                            @Override public void onError(ANError e) { }
                                        });

                                        DialogOption.dismiss();
                                    }
                                });

                                LinearLayoutMain.addView(TextViewBookmark);

                                View ViewLine5 = new View(Activity);
                                ViewLine5.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1)));
                                ViewLine5.setBackgroundResource(R.color.LineWhite);

                                LinearLayoutMain.addView(ViewLine5);

                                TextView TextViewShare = new TextView(Activity, 14, false);
                                TextViewShare.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56)));
                                TextViewShare.SetColor(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite);
                                TextViewShare.setPadding(Misc.ToDP(15), 0, Misc.ToDP(15), 0);
                                TextViewShare.setText(Misc.String(R.string.PostAdapterOptionShare));
                                TextViewShare.setGravity(Gravity.CENTER_VERTICAL);
                                TextViewShare.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        String Message = P.Name + " " + Activity.getString(R.string.PostAdapterPostShare1) + "\n";
                                        Message += P.Message == null ? "" : P.Message;
                                        Message += "\n" +  Activity.getString(R.string.PostAdapterPostShare1) + "\nhttp://biogram.co/post/" + P.ID;

                                        Intent I = new Intent();
                                        I.setAction(Intent.ACTION_SEND);
                                        I.putExtra(Intent.EXTRA_TEXT, Message);
                                        I.setType("text/plain");
                                        Activity.startActivity(I);

                                        DialogOption.dismiss();
                                    }
                                });

                                LinearLayoutMain.addView(TextViewShare);

                                DialogOption.setContentView(LinearLayoutMain);
                                DialogOption.show();
                            }
                        });
                    }
                }
                catch (Exception e)
                {
                    Misc.Debug("PostUI: " + e.toString());
                }

                LoadingViewMain.Stop();
                LoadingViewMain.setVisibility(View.GONE);
            }

            @Override
            public void onError(ANError e)
            {
                LoadingViewMain.Stop();
                LoadingViewMain.setVisibility(View.GONE);
            }
        });*/

        ViewMain = RelativeLayoutMain;
    }

    @Override
    public void OnPause()
    {
        AndroidNetworking.forceCancel("WriteUI");
    }
}
