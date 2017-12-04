package co.biogram.main.handler;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.util.ArrayList;
import java.util.List;

import co.biogram.main.R;
import co.biogram.main.fragment.FragmentActivity;
import co.biogram.main.ui.view.CircleImageView;
import co.biogram.main.ui.view.CircleView;
import co.biogram.main.ui.view.LineView;
import co.biogram.main.ui.view.PullToRefreshView;
import co.biogram.main.ui.view.TextView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolderMain>
{
    private List<PostStruct> PostList = new ArrayList<>();
    private PullToRefreshView PullToRefreshViewMain;
    private final PullToRefreshListener Listener;
    private final FragmentActivity Activity;

    // ViewType 1
    private final int ID1_PROFILE = Misc.GenerateViewID();
    private final int ID1_NAME = Misc.GenerateViewID();
    private final int ID1_MEDAL = Misc.GenerateViewID();
    private final int ID1_USERNAME = Misc.GenerateViewID();
    private final int ID1_TIME = Misc.GenerateViewID();
    private final int ID1_OPTION = Misc.GenerateViewID();
    private final int ID1_MESSAGE = Misc.GenerateViewID();
    private final int ID1_LIKE = Misc.GenerateViewID();
    private final int ID1_COMMENT = Misc.GenerateViewID();
    private final int ID1_REPLAY = Misc.GenerateViewID();
    private final int ID1_FORWARD = Misc.GenerateViewID();
    private final int ID1_IMAGE_LAYOUT = Misc.GenerateViewID();
    private final int ID1_SINGLE = Misc.GenerateViewID();
    private final int ID1_DOUBLE_LAYOUT = Misc.GenerateViewID();
    private final int ID1_DOUBLE1 = Misc.GenerateViewID();
    private final int ID1_DOUBLE2 = Misc.GenerateViewID();
    private final int ID1_TRIPLE_LAYOUT = Misc.GenerateViewID();
    private final int ID1_TRIPLE1 = Misc.GenerateViewID();
    private final int ID1_TRIPLE2 = Misc.GenerateViewID();
    private final int ID1_TRIPLE3 = Misc.GenerateViewID();
    private final int ID1_VIDEO_LAYOUT = Misc.GenerateViewID();
    private final int ID1_VIDEO_IMAGE = Misc.GenerateViewID();
    private final int ID1_VIDEO_DUROTION = Misc.GenerateViewID();
    private final int ID1_VOTE_LAYOUT = Misc.GenerateViewID();
    private final int ID1_VOTE1 = Misc.GenerateViewID();
    private final int ID1_VOTE_PER1 = Misc.GenerateViewID();
    private final int ID1_VOTE2 = Misc.GenerateViewID();
    private final int ID1_VOTE_PER2 = Misc.GenerateViewID();
    private final int ID1_VOTE3 = Misc.GenerateViewID();
    private final int ID1_VOTE_PER3 = Misc.GenerateViewID();
    private final int ID1_VOTE4 = Misc.GenerateViewID();
    private final int ID1_VOTE_PER4 = Misc.GenerateViewID();
    private final int ID1_VOTE5 = Misc.GenerateViewID();
    private final int ID1_VOTE_PER5 = Misc.GenerateViewID();
    private final int ID1_VOTE_RESULT = Misc.GenerateViewID();

    // ViewType 2
    private final int ID2_LEVEL = Misc.GenerateViewID();
    private final int ID2_NUMBER = Misc.GenerateViewID();
    private final int ID2_RATING = Misc.GenerateViewID();
    private final int ID2_JOIN = Misc.GenerateViewID();
    private final int ID2_POPULAR = Misc.GenerateViewID();
    private final int ID2_POINT = Misc.GenerateViewID();
    private final int ID2_MEDAL = Misc.GenerateViewID();
    private final int ID2_CLOSE = Misc.GenerateViewID();
    private final int ID2_MORE = Misc.GenerateViewID();
    private final int ID2_MORE2 = Misc.GenerateViewID();

    public PostAdapter(FragmentActivity a, List<PostStruct> p, PullToRefreshListener l)
    {
        Listener = l;
        Activity = a;
        PostList = p;
    }

    class ViewHolderMain extends RecyclerView.ViewHolder
    {
        // ViewType 1
        CircleImageView CircleImageViewProfile;
        TextView TextViewName;
        ImageView ImageiewMedal;
        TextView TextViewUsername;
        ImageView ImageiewOption;
        TextView TextViewTime;
        TextView TextViewMessage;
        RelativeLayout RelativeLayoutImage;
        ImageView ImageViewSingle;
        LinearLayout LinearLayoutDouble;
        ImageView ImageViewDouble1;
        ImageView ImageViewDouble2;
        LinearLayout LinearLayoutTriple;
        ImageView ImageViewTriple1;
        ImageView ImageViewTriple2;
        ImageView ImageViewTriple3;
        RelativeLayout RelativeLayoutVideo;
        ImageView ImageViewVideo;
        TextView TextViewDurotion;
        RelativeLayout RelativeLayoutVote;
        TextView TextViewVote1;
        TextView TextViewPercent1;
        TextView TextViewVote2;
        TextView TextViewPercent2;
        TextView TextViewVote3;
        TextView TextViewPercent3;
        TextView TextViewVote4;
        TextView TextViewPercent4;
        TextView TextViewVote5;
        TextView TextViewPercent5;
        TextView TextViewResult;

        // ViewType 2
        CircleView CircleViewLevel;
        TextView TextViewNumber;
        LineView LineViewRating;
        CircleView CircleViewJoin;
        CircleView CircleViewPopular;
        CircleView CircleViewPoint;
        CircleImageView CircleImageViewMedal;
        ImageView ImageViewClose;
        LinearLayout LinearLayoutMore;
        LinearLayout LinearLayoutMore2;

        ViewHolderMain(View v, int viewType)
        {
            super(v);

            if (viewType == 1)
            {
                CircleImageViewProfile = v.findViewById(ID1_PROFILE);
                TextViewName = v.findViewById(ID1_NAME);
                ImageiewMedal = v.findViewById(ID1_MEDAL);
                TextViewUsername = v.findViewById(ID1_USERNAME);
                ImageiewOption = v.findViewById(ID1_OPTION);
                TextViewTime = v.findViewById(ID1_TIME);
                TextViewMessage = v.findViewById(ID1_MESSAGE);
                RelativeLayoutImage = v.findViewById(ID1_IMAGE_LAYOUT);
                ImageViewSingle = v.findViewById(ID1_SINGLE);
                LinearLayoutDouble = v.findViewById(ID1_DOUBLE_LAYOUT);
                ImageViewDouble1 = v.findViewById(ID1_DOUBLE1);
                ImageViewDouble2 = v.findViewById(ID1_DOUBLE2);
                LinearLayoutTriple = v.findViewById(ID1_TRIPLE_LAYOUT);
                ImageViewTriple1 = v.findViewById(ID1_TRIPLE1);
                ImageViewTriple2 = v.findViewById(ID1_TRIPLE2);
                ImageViewTriple3 = v.findViewById(ID1_TRIPLE3);
                RelativeLayoutVideo = v.findViewById(ID1_VIDEO_LAYOUT);
                ImageViewVideo = v.findViewById(ID1_VIDEO_IMAGE);
                TextViewDurotion = v.findViewById(ID1_VIDEO_DUROTION);
                RelativeLayoutVote = v.findViewById(ID1_VOTE_LAYOUT);
                TextViewVote1 = v.findViewById(ID1_VOTE1);
                TextViewPercent1 = v.findViewById(ID1_VOTE_PER1);
                TextViewVote2 = v.findViewById(ID1_VOTE2);
                TextViewPercent2 = v.findViewById(ID1_VOTE_PER2);
                TextViewVote3 = v.findViewById(ID1_VOTE3);
                TextViewPercent3 = v.findViewById(ID1_VOTE_PER3);
                TextViewVote4 = v.findViewById(ID1_VOTE4);
                TextViewPercent4 = v.findViewById(ID1_VOTE_PER4);
                TextViewVote5 = v.findViewById(ID1_VOTE5);
                TextViewPercent5 = v.findViewById(ID1_VOTE_PER5);
                TextViewResult = v.findViewById(ID1_VOTE_RESULT);
            }
            else if (viewType == 2)
            {
                CircleViewLevel = v.findViewById(ID2_LEVEL);
                TextViewNumber = v.findViewById(ID2_NUMBER);
                LineViewRating = v.findViewById(ID2_RATING);
                CircleViewJoin = v.findViewById(ID2_JOIN);
                CircleViewPopular = v.findViewById(ID2_POPULAR);
                CircleViewPoint = v.findViewById(ID2_POINT);
                CircleImageViewMedal = v.findViewById(ID2_MEDAL);
                ImageViewClose = v.findViewById(ID2_CLOSE);
                LinearLayoutMore = v.findViewById(ID2_MORE);
                LinearLayoutMore2 = v.findViewById(ID2_MORE2);
            }
        }
    }

    public View GetPullToRefreshView()
    {
        return PullToRefreshViewMain;
    }

    public void SetRefreshComplete()
    {
        PullToRefreshViewMain.SetRefreshComplete();
    }

    @Override
    public PostAdapter.ViewHolderMain onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if (viewType == 0)
        {
            PullToRefreshViewMain = new PullToRefreshView(Activity);
            PullToRefreshViewMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            PullToRefreshViewMain.SetPullToRefreshListener(Listener);

            return new ViewHolderMain(PullToRefreshViewMain, viewType);
        }
        else if (viewType == 1)
        {
            RelativeLayout RelativeLayoutMain = new RelativeLayout(Activity);
            RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

            RelativeLayout.LayoutParams CircleImageViewProfileParam = new RelativeLayout.LayoutParams(Misc.ToDP(Activity, 50), Misc.ToDP(Activity, 50));
            CircleImageViewProfileParam.setMargins(Misc.ToDP(Activity, 8), Misc.ToDP(Activity, 8), Misc.ToDP(Activity, 8), Misc.ToDP(Activity, 8));
            CircleImageViewProfileParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

            CircleImageView CircleImageViewProfile = new CircleImageView(Activity);
            CircleImageViewProfile.setLayoutParams(CircleImageViewProfileParam);
            CircleImageViewProfile.setId(ID1_PROFILE);
            CircleImageViewProfile.SetBorderColor(R.color.Gray2);
            CircleImageViewProfile.SetBorderWidth(1);
            CircleImageViewProfile.setImageResource(R.drawable.ic_profile_blue);

            RelativeLayoutMain.addView(CircleImageViewProfile);

            RelativeLayout.LayoutParams LinearLayoutProfileParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(Activity, 24));
            LinearLayoutProfileParam.setMargins(0, Misc.ToDP(Activity, 12), 0, 0);
            LinearLayoutProfileParam.addRule(RelativeLayout.RIGHT_OF, ID1_PROFILE);
            LinearLayoutProfileParam.addRule(RelativeLayout.LEFT_OF, ID1_TIME);

            LinearLayout LinearLayoutProfile = new LinearLayout(Activity);
            LinearLayoutProfile.setLayoutParams(LinearLayoutProfileParam);
            LinearLayoutProfile.setOrientation(LinearLayout.HORIZONTAL);

            RelativeLayoutMain.addView(LinearLayoutProfile);

            TextView TextViewName = new TextView(Activity, 14, true);
            TextViewName.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            TextViewName.setTextColor(ContextCompat.getColor(Activity, R.color.Black));
            TextViewName.setId(ID1_NAME);
            TextViewName.setText(("ali blog"));

            LinearLayoutProfile.addView(TextViewName);

            ImageView ImageiewMedal = new ImageView(Activity);
            ImageiewMedal.setLayoutParams(new RelativeLayout.LayoutParams(Misc.ToDP(Activity, 24), Misc.ToDP(Activity, 24)));
            ImageiewMedal.setId(ID1_MEDAL);
            ImageiewMedal.setImageResource(R.drawable.ic_profile_blue);

            LinearLayoutProfile.addView(ImageiewMedal);

            TextView TextViewUsername = new TextView(Activity, 14, false);
            TextViewUsername.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            TextViewUsername.setTextColor(ContextCompat.getColor(Activity, R.color.Gray4));
            TextViewUsername.setId(ID1_USERNAME);
            TextViewUsername.setLineSpacing(1.0f, 3.0f);
            TextViewUsername.setText(("usernamealiusernamealiusernameali"));

            LinearLayoutProfile.addView(TextViewUsername);

            RelativeLayout.LayoutParams ImageiewOptionParam = new RelativeLayout.LayoutParams(Misc.ToDP(Activity, 32), Misc.ToDP(Activity, 32));
            ImageiewOptionParam.setMargins(0, Misc.ToDP(Activity, 6), 0, 0);
            ImageiewOptionParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

            ImageView ImageiewOption = new ImageView(Activity);
            ImageiewOption.setLayoutParams(ImageiewOptionParam);
            ImageiewOption.setId(ID1_OPTION);
            ImageiewOption.setPadding(Misc.ToDP(Activity, 5), Misc.ToDP(Activity, 5), Misc.ToDP(Activity, 5),  Misc.ToDP(Activity, 5));
            ImageiewOption.setImageResource(R.drawable.ic_option_gray);

            RelativeLayoutMain.addView(ImageiewOption);

            RelativeLayout.LayoutParams TextViewTimeParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewTimeParam.setMargins(0, Misc.ToDP(Activity, 14), 0, 0);
            TextViewTimeParam.addRule(RelativeLayout.LEFT_OF, ID1_OPTION);

            TextView TextViewTime = new TextView(Activity, 12, false);
            TextViewTime.setLayoutParams(TextViewTimeParam);
            TextViewTime.setTextColor(ContextCompat.getColor(Activity, R.color.Gray2));
            TextViewTime.setId(ID1_TIME);
            TextViewTime.setText(("1h"));

            RelativeLayoutMain.addView(TextViewTime);

            RelativeLayout.LayoutParams TextViewMessageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewMessageParam.addRule(RelativeLayout.RIGHT_OF, ID1_PROFILE);
            TextViewMessageParam.addRule(RelativeLayout.BELOW, ID1_TIME);

            TextView TextViewMessage = new TextView(Activity, 14, false);
            TextViewMessage.setLayoutParams(TextViewMessageParam);
            TextViewMessage.setTextColor(Misc.IsDark(Activity) ? ContextCompat.getColor(Activity, R.color.White) : ContextCompat.getColor(Activity, R.color.Black));
            TextViewMessage.setId(ID1_MESSAGE);
            TextViewMessage.setText(("salam khobi khoshi ? چه میکنیsalam khobi khoshi ? چه میکنیsalam khobi khoshi ? چه میکنیsalam khobi khoshi ? چه میکنیsalam khobi khoshi ? چه میکنی #Salam"));

            RelativeLayoutMain.addView(TextViewMessage);

            RelativeLayout.LayoutParams RelativeLayoutContentParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            RelativeLayoutContentParam.setMargins(0, 0, Misc.ToDP(Activity, 5), 0);
            RelativeLayoutContentParam.addRule(RelativeLayout.RIGHT_OF, ID1_PROFILE);
            RelativeLayoutContentParam.addRule(RelativeLayout.BELOW, ID1_MESSAGE);

            RelativeLayout RelativeLayoutContent = new RelativeLayout(Activity);
            RelativeLayoutContent.setLayoutParams(RelativeLayoutContentParam);
            RelativeLayoutContent.setId(Misc.GenerateViewID());

            RelativeLayoutMain.addView(RelativeLayoutContent);

            RelativeLayout RelativeLayoutImage = new RelativeLayout(Activity);
            RelativeLayoutImage.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(Activity, 150)));
            RelativeLayoutImage.setId(ID1_IMAGE_LAYOUT);
            RelativeLayoutImage.setVisibility(View.GONE);

            RelativeLayoutContent.addView(RelativeLayoutImage);

            ImageView ImageiewSingle = new ImageView(Activity);
            ImageiewSingle.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            ImageiewSingle.setId(ID1_SINGLE);
            ImageiewSingle.setVisibility(View.GONE);

            RelativeLayoutImage.addView(ImageiewSingle);

            LinearLayout LinearLayoutDouble = new LinearLayout(Activity);
            LinearLayoutDouble.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            LinearLayoutDouble.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayoutDouble.setVisibility(View.GONE);
            LinearLayoutDouble.setId(ID1_DOUBLE_LAYOUT);

            RelativeLayoutImage.addView(LinearLayoutDouble);

            ImageView ImageiewDouble1 = new ImageView(Activity);
            ImageiewDouble1.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1));
            ImageiewDouble1.setId(ID1_DOUBLE1);

            LinearLayoutDouble.addView(ImageiewDouble1);

            View ViewLineDouble = new View(Activity);
            ViewLineDouble.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 0.03f));
            ViewLineDouble.setBackgroundResource(Misc.IsDark(Activity) ? R.color.GroundDark : R.color.White);

            LinearLayoutDouble.addView(ViewLineDouble);

            ImageView ImageiewDouble2 = new ImageView(Activity);
            ImageiewDouble2.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1));
            ImageiewDouble2.setId(ID1_DOUBLE2);

            LinearLayoutDouble.addView(ImageiewDouble2);

            LinearLayout LinearLayoutTriple = new LinearLayout(Activity);
            LinearLayoutTriple.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            LinearLayoutTriple.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayoutTriple.setVisibility(View.GONE);
            LinearLayoutTriple.setId(ID1_TRIPLE_LAYOUT);

            RelativeLayoutImage.addView(LinearLayoutTriple);

            ImageView ImageiewTriple1 = new ImageView(Activity);
            ImageiewTriple1.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1));
            ImageiewTriple1.setId(ID1_TRIPLE1);

            LinearLayoutTriple.addView(ImageiewTriple1);

            View ViewLineTriple = new View(Activity);
            ViewLineTriple.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 0.02f));
            ViewLineTriple.setBackgroundResource(Misc.IsDark(Activity) ? R.color.GroundDark : R.color.White);

            LinearLayoutTriple.addView(ViewLineTriple);

            LinearLayout LinearLayoutDouble2 = new LinearLayout(Activity);
            LinearLayoutDouble2.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1));
            LinearLayoutDouble2.setOrientation(LinearLayout.VERTICAL);

            LinearLayoutTriple.addView(LinearLayoutDouble2);

            ImageView ImageiewTriple2 = new ImageView(Activity);
            ImageiewTriple2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1));
            ImageiewTriple2.setId(ID1_TRIPLE2);

            LinearLayoutDouble2.addView(ImageiewTriple2);

            View ViewLineTriple2 = new View(Activity);
            ViewLineTriple2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.03f));
            ViewLineTriple2.setBackgroundResource(Misc.IsDark(Activity) ? R.color.GroundDark : R.color.White);

            LinearLayoutDouble2.addView(ViewLineTriple2);

            ImageView ImageiewTriple3 = new ImageView(Activity);
            ImageiewTriple3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1));
            ImageiewTriple3.setId(ID1_TRIPLE3);

            LinearLayoutDouble2.addView(ImageiewTriple3);

            RelativeLayout RelativeLayoutVideo = new RelativeLayout(Activity);
            RelativeLayoutVideo.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(Activity, 150)));
            RelativeLayoutVideo.setVisibility(View.GONE);
            RelativeLayoutVideo.setId(ID1_VIDEO_LAYOUT);

            RelativeLayoutContent.addView(RelativeLayoutVideo);

            ImageView ImageViewVideo = new ImageView(Activity);
            ImageViewVideo.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            ImageViewVideo.setBackgroundResource(R.color.BlueGray2);
            ImageViewVideo.setId(ID1_VIDEO_IMAGE);

            RelativeLayoutVideo.addView(ImageViewVideo);

            GradientDrawable DrawableVideo = new GradientDrawable();
            DrawableVideo.setColor(Color.parseColor("#65000000"));
            DrawableVideo.setCornerRadius(Misc.ToDP(Activity, 4));

            RelativeLayout.LayoutParams TextViewDuritonParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewDuritonParam.setMargins(Misc.ToDP(Activity, 8), 0, Misc.ToDP(Activity, 8), Misc.ToDP(Activity, 8));
            TextViewDuritonParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            TextViewDuritonParam.addRule(Misc.Align("L"));

            TextView TextViewDurotion = new TextView(Activity, 12, false);
            TextViewDurotion.setLayoutParams(TextViewDuritonParam);
            TextViewDurotion.setTextColor(ContextCompat.getColor(Activity, R.color.White));
            TextViewDurotion.setBackground(DrawableVideo);
            TextViewDurotion.setPadding(Misc.ToDP(Activity, 3), Misc.ToDP(Activity, 3), Misc.ToDP(Activity, 3), 0);
            TextViewDurotion.setId(ID1_VIDEO_DUROTION);
            TextViewDurotion.setText(("1:32"));

            RelativeLayoutVideo.addView(TextViewDurotion);

            RelativeLayout.LayoutParams TextViewVideoParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewVideoParam.setMargins(Misc.ToDP(Activity, 8), 0, Misc.ToDP(Activity, 8), Misc.ToDP(Activity, 8));
            TextViewVideoParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            TextViewVideoParam.addRule(Misc.Align("R"));

            TextView TextViewVideo = new TextView(Activity, 12, false);
            TextViewVideo.setLayoutParams(TextViewVideoParam);
            TextViewVideo.setTextColor(ContextCompat.getColor(Activity, R.color.White));
            TextViewVideo.setPadding(Misc.ToDP(Activity, 3), Misc.ToDP(Activity, 3), Misc.ToDP(Activity, 3), 0);
            TextViewVideo.setBackground(DrawableVideo);
            TextViewVideo.setText(("Video"));

            RelativeLayoutVideo.addView(TextViewVideo);

            GradientDrawable DrawableVote = new GradientDrawable();
            DrawableVote.setCornerRadius(Misc.ToDP(Activity, 4));
            DrawableVote.setStroke(Misc.ToDP(Activity, 1), ContextCompat.getColor(Activity, R.color.BlueGray));

            RelativeLayout RelativeLayoutVote = new RelativeLayout(Activity);
            RelativeLayoutVote.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            RelativeLayoutVote.setPadding(Misc.ToDP(Activity, 8), Misc.ToDP(Activity, 8), Misc.ToDP(Activity, 10), Misc.ToDP(Activity, 8));
            RelativeLayoutVote.setBackground(DrawableVote);
            RelativeLayoutVote.setVisibility(View.GONE);
            RelativeLayoutVote.setId(ID1_VOTE_LAYOUT);

            RelativeLayoutContent.addView(RelativeLayoutVote);

            RelativeLayout.LayoutParams TextViewVote1Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(Activity, 30));
            TextViewVote1Param.addRule(RelativeLayout.LEFT_OF, ID1_VOTE_PER1);

            TextView TextViewVote1 = new TextView(Activity, 14, false);
            TextViewVote1.setLayoutParams(TextViewVote1Param);
            TextViewVote1.setTextColor(ContextCompat.getColor(Activity, R.color.Black));
            TextViewVote1.setPadding(Misc.ToDP(Activity, 3), Misc.ToDP(Activity, 3), 0, 0);
            TextViewVote1.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
            TextViewVote1.setVisibility(View.GONE);
            TextViewVote1.setId(ID1_VOTE1);

            RelativeLayoutVote.addView(TextViewVote1);

            RelativeLayout.LayoutParams TextViewPercent1Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, Misc.ToDP(Activity, 30));
            TextViewPercent1Param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

            TextView TextViewPercent1 = new TextView(Activity, 14, false);
            TextViewPercent1.setLayoutParams(TextViewPercent1Param);
            TextViewPercent1.setTextColor(ContextCompat.getColor(Activity, R.color.Black));
            TextViewPercent1.setPadding(Misc.ToDP(Activity, 5), Misc.ToDP(Activity, 5), 0, 0);
            TextViewPercent1.setGravity(Gravity.CENTER_VERTICAL);
            TextViewPercent1.setVisibility(View.GONE);
            TextViewPercent1.setId(ID1_VOTE_PER1);

            RelativeLayoutVote.addView(TextViewPercent1);

            RelativeLayout.LayoutParams TextViewVote2Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(Activity, 30));
            TextViewVote2Param.setMargins(0, Misc.ToDP(Activity, 5), 0, 0);
            TextViewVote2Param.addRule(RelativeLayout.LEFT_OF, ID1_VOTE_PER2);
            TextViewVote2Param.addRule(RelativeLayout.BELOW, ID1_VOTE1);

            TextView TextViewVote2 = new TextView(Activity, 14, false);
            TextViewVote2.setLayoutParams(TextViewVote2Param);
            TextViewVote2.setTextColor(ContextCompat.getColor(Activity, R.color.Black));
            TextViewVote2.setPadding(Misc.ToDP(Activity, 3), Misc.ToDP(Activity, 3), 0, 0);
            TextViewVote2.setGravity(Gravity.CENTER_VERTICAL);
            TextViewVote2.setVisibility(View.GONE);
            TextViewVote2.setId(ID1_VOTE2);

            RelativeLayoutVote.addView(TextViewVote2);

            RelativeLayout.LayoutParams TextViewPercent2Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, Misc.ToDP(Activity, 30));
            TextViewPercent2Param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            TextViewPercent2Param.addRule(RelativeLayout.BELOW, ID1_VOTE1);

            TextView TextViewPercent2 = new TextView(Activity, 14, false);
            TextViewPercent2.setLayoutParams(TextViewPercent2Param);
            TextViewPercent2.setTextColor(ContextCompat.getColor(Activity, R.color.Black));
            TextViewPercent2.setPadding(Misc.ToDP(Activity, 5), Misc.ToDP(Activity, 10), 0, 0);
            TextViewPercent2.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
            TextViewPercent2.setVisibility(View.GONE);
            TextViewPercent2.setId(ID1_VOTE_PER2);

            RelativeLayoutVote.addView(TextViewPercent2);

            RelativeLayout.LayoutParams TextViewVote3Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(Activity, 30));
            TextViewVote3Param.setMargins(0, Misc.ToDP(Activity, 5), 0, 0);
            TextViewVote3Param.addRule(RelativeLayout.LEFT_OF, ID1_VOTE_PER3);
            TextViewVote3Param.addRule(RelativeLayout.BELOW, ID1_VOTE2);

            TextView TextViewVote3 = new TextView(Activity, 14, false);
            TextViewVote3.setLayoutParams(TextViewVote3Param);
            TextViewVote3.setTextColor(ContextCompat.getColor(Activity, R.color.Black));
            TextViewVote3.setPadding(Misc.ToDP(Activity, 3), Misc.ToDP(Activity, 3), 0, 0);
            TextViewVote3.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
            TextViewVote3.setVisibility(View.GONE);
            TextViewVote3.setId(ID1_VOTE3);

            RelativeLayoutVote.addView(TextViewVote3);

            RelativeLayout.LayoutParams TextViewPercent3Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, Misc.ToDP(Activity, 30));
            TextViewPercent3Param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            TextViewPercent3Param.addRule(RelativeLayout.BELOW, ID1_VOTE2);

            TextView TextViewPercent3 = new TextView(Activity, 14, false);
            TextViewPercent3.setLayoutParams(TextViewPercent3Param);
            TextViewPercent3.setTextColor(ContextCompat.getColor(Activity, R.color.Black));
            TextViewPercent3.setPadding(Misc.ToDP(Activity, 5), Misc.ToDP(Activity, 10), 0, 0);
            TextViewPercent3.setGravity(Gravity.CENTER_VERTICAL);
            TextViewPercent3.setVisibility(View.GONE);
            TextViewPercent3.setId(ID1_VOTE_PER3);

            RelativeLayoutVote.addView(TextViewPercent3);

            RelativeLayout.LayoutParams TextViewVote4Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(Activity, 30));
            TextViewVote4Param.setMargins(0, Misc.ToDP(Activity, 5), 0, 0);
            TextViewVote4Param.addRule(RelativeLayout.LEFT_OF, ID1_VOTE_PER4);
            TextViewVote4Param.addRule(RelativeLayout.BELOW, ID1_VOTE3);

            TextView TextViewVote4 = new TextView(Activity, 14, false);
            TextViewVote4.setLayoutParams(TextViewVote4Param);
            TextViewVote4.setTextColor(ContextCompat.getColor(Activity, R.color.Black));
            TextViewVote4.setPadding(Misc.ToDP(Activity, 3), Misc.ToDP(Activity, 3), 0, 0);
            TextViewVote4.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
            TextViewVote4.setVisibility(View.GONE);
            TextViewVote4.setId(ID1_VOTE4);

            RelativeLayoutVote.addView(TextViewVote4);

            RelativeLayout.LayoutParams TextViewPercent4Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, Misc.ToDP(Activity, 30));
            TextViewPercent4Param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            TextViewPercent4Param.addRule(RelativeLayout.BELOW, ID1_VOTE3);

            TextView TextViewPercent4 = new TextView(Activity, 14, false);
            TextViewPercent4.setLayoutParams(TextViewPercent4Param);
            TextViewPercent4.setTextColor(ContextCompat.getColor(Activity, R.color.Black));
            TextViewPercent4.setPadding(Misc.ToDP(Activity, 5), Misc.ToDP(Activity, 10), 0, 0);
            TextViewPercent4.setGravity(Gravity.CENTER_VERTICAL);
            TextViewPercent4.setVisibility(View.GONE);
            TextViewPercent4.setId(ID1_VOTE_PER4);

            RelativeLayoutVote.addView(TextViewPercent4);

            RelativeLayout.LayoutParams TextViewVote5Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(Activity, 30));
            TextViewVote5Param.setMargins(0, Misc.ToDP(Activity, 5), 0, 0);
            TextViewVote5Param.addRule(RelativeLayout.LEFT_OF, ID1_VOTE_PER5);
            TextViewVote5Param.addRule(RelativeLayout.BELOW, ID1_VOTE4);

            TextView TextViewVote5 = new TextView(Activity, 14, false);
            TextViewVote5.setLayoutParams(TextViewVote5Param);
            TextViewVote5.setTextColor(ContextCompat.getColor(Activity, R.color.Black));
            TextViewVote5.setPadding(Misc.ToDP(Activity, 3), Misc.ToDP(Activity, 3), 0, 0);
            TextViewVote5.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
            TextViewVote5.setVisibility(View.GONE);
            TextViewVote5.setId(ID1_VOTE5);

            RelativeLayoutVote.addView(TextViewVote5);

            RelativeLayout.LayoutParams TextViewPercent5Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, Misc.ToDP(Activity, 30));
            TextViewPercent5Param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            TextViewPercent5Param.addRule(RelativeLayout.BELOW, ID1_VOTE4);

            TextView TextViewPercent5 = new TextView(Activity, 14, false);
            TextViewPercent5.setLayoutParams(TextViewPercent5Param);
            TextViewPercent5.setTextColor(ContextCompat.getColor(Activity, R.color.Black));
            TextViewPercent5.setPadding(Misc.ToDP(Activity, 5), Misc.ToDP(Activity, 10), 0, 0);
            TextViewPercent5.setGravity(Gravity.CENTER_VERTICAL);
            TextViewPercent5.setVisibility(View.GONE);
            TextViewPercent5.setId(ID1_VOTE_PER5);

            RelativeLayoutVote.addView(TextViewPercent5);

            RelativeLayout.LayoutParams ViewVoteParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(Activity, 1));
            ViewVoteParam.setMargins(0, Misc.ToDP(Activity, 15), 0, 0);
            ViewVoteParam.addRule(RelativeLayout.BELOW, ID1_VOTE5);

            View ViewVote = new View(Activity);
            ViewVote.setLayoutParams(ViewVoteParam);
            ViewVote.setBackgroundResource(R.color.BlueGray);
            ViewVote.setId(Misc.GenerateViewID());

            RelativeLayoutVote.addView(ViewVote);

            RelativeLayout.LayoutParams TextViewResultParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, Misc.ToDP(Activity, 30));
            TextViewResultParam.addRule(RelativeLayout.BELOW, ViewVote.getId());

            TextView TextViewResult = new TextView(Activity, 14, false);
            TextViewResult.setLayoutParams(TextViewResultParam);
            TextViewResult.setTextColor(ContextCompat.getColor(Activity, R.color.BlueGray2));
            TextViewResult.setPadding(Misc.ToDP(Activity, 5), Misc.ToDP(Activity, 10), 0, 0);
            TextViewResult.setGravity(Gravity.CENTER_VERTICAL);
            TextViewResult.setId(ID1_VOTE_RESULT);

            RelativeLayoutVote.addView(TextViewResult);

            RelativeLayout.LayoutParams RelativeLayoutToolParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(Activity, 40));
            RelativeLayoutToolParam.addRule(RelativeLayout.BELOW, RelativeLayoutContent.getId());
            RelativeLayoutToolParam.addRule(RelativeLayout.RIGHT_OF, ID1_PROFILE);

            RelativeLayout RelativeLayoutTool = new RelativeLayout(Activity);
            RelativeLayoutTool.setLayoutParams(RelativeLayoutToolParam);

            RelativeLayoutMain.addView(RelativeLayoutTool);

            RelativeLayout.LayoutParams ImageViewLikeParam = new RelativeLayout.LayoutParams(Misc.ToDP(Activity, 40), RelativeLayout.LayoutParams.MATCH_PARENT);
            ImageViewLikeParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

            ImageView ImageViewLike = new ImageView(Activity);
            ImageViewLike.setLayoutParams(ImageViewLikeParam);
            ImageViewLike.setPadding(Misc.ToDP(Activity, 8), Misc.ToDP(Activity, 8), Misc.ToDP(Activity, 8), Misc.ToDP(Activity, 8));
            ImageViewLike.setImageResource(R.drawable.ic_like);
            ImageViewLike.setId(ID1_LIKE);

            RelativeLayoutTool.addView(ImageViewLike);

            RelativeLayout.LayoutParams ImageViewCommentParam = new RelativeLayout.LayoutParams(Misc.ToDP(Activity, 40), RelativeLayout.LayoutParams.MATCH_PARENT);
            ImageViewCommentParam.setMargins(Misc.ToDP(Activity, 40), 0, 0, 0);
            ImageViewCommentParam.addRule(RelativeLayout.RIGHT_OF, ID1_LIKE);

            ImageView ImageViewComment = new ImageView(Activity);
            ImageViewComment.setLayoutParams(ImageViewCommentParam);
            ImageViewComment.setPadding(Misc.ToDP(Activity, 8), Misc.ToDP(Activity, 8), Misc.ToDP(Activity, 8), Misc.ToDP(Activity, 8));
            ImageViewComment.setImageResource(R.drawable.ic_comment);
            ImageViewComment.setId(ID1_COMMENT);

            RelativeLayoutTool.addView(ImageViewComment);

            RelativeLayout.LayoutParams ImageViewReplayParam = new RelativeLayout.LayoutParams(Misc.ToDP(Activity, 40), RelativeLayout.LayoutParams.MATCH_PARENT);
            ImageViewReplayParam.setMargins(0, 0, Misc.ToDP(Activity, 40), 0);
            ImageViewReplayParam.addRule(RelativeLayout.LEFT_OF, ID1_FORWARD);

            ImageView ImageViewReplay = new ImageView(Activity);
            ImageViewReplay.setLayoutParams(ImageViewReplayParam);
            ImageViewReplay.setPadding(Misc.ToDP(Activity, 8), Misc.ToDP(Activity, 8), Misc.ToDP(Activity, 8), Misc.ToDP(Activity, 8));
            ImageViewReplay.setImageResource(R.drawable.ic_replay);
            ImageViewReplay.setId(ID1_REPLAY);

            RelativeLayoutTool.addView(ImageViewReplay);

            RelativeLayout.LayoutParams ImageViewForwardParam = new RelativeLayout.LayoutParams(Misc.ToDP(Activity, 40), RelativeLayout.LayoutParams.MATCH_PARENT);
            ImageViewForwardParam.setMargins(0, 0, Misc.ToDP(Activity, 10), 0);
            ImageViewForwardParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

            ImageView ImageViewForward = new ImageView(Activity);
            ImageViewForward.setLayoutParams(ImageViewForwardParam);
            ImageViewForward.setPadding(Misc.ToDP(Activity, 8), Misc.ToDP(Activity, 8), Misc.ToDP(Activity, 8), Misc.ToDP(Activity, 8));
            ImageViewForward.setImageResource(R.drawable.ic_chat);
            ImageViewForward.setId(ID1_FORWARD);

            RelativeLayoutTool.addView(ImageViewForward);

            RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(Activity, 1));
            ViewLineParam.setMargins(0, Misc.ToDP(Activity, 5), 0, 0);
            ViewLineParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

            View ViewLine = new View(Activity);
            ViewLine.setLayoutParams(ViewLineParam);
            ViewLine.setBackgroundResource(R.color.Gray);

            RelativeLayoutMain.addView(ViewLine);

            return new ViewHolderMain(RelativeLayoutMain, viewType);
        }
        else if (viewType == 2)
        {
            RelativeLayout RelativeLayoutInfo = new RelativeLayout(Activity);
            RelativeLayoutInfo.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

            View ViewLine = new View(Activity);
            ViewLine.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(Activity, 5)));
            ViewLine.setBackgroundResource(Misc.IsDark(Activity) ? R.color.LineDark : R.color.Gray);
            ViewLine.setId(Misc.GenerateViewID());

            RelativeLayoutInfo.addView(ViewLine);

            RelativeLayout.LayoutParams ImageViewCloseParam = new RelativeLayout.LayoutParams(Misc.ToDP(Activity, 48), Misc.ToDP(Activity, 48));
            ImageViewCloseParam.addRule(RelativeLayout.BELOW, ViewLine.getId());
            ImageViewCloseParam.addRule(Misc.Align("L"));

            ImageView ImageViewClose = new ImageView(Activity);
            ImageViewClose.setLayoutParams(ImageViewCloseParam);
            ImageViewClose.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ImageViewClose.setImageResource(Misc.IsDark(Activity) ? R.drawable.ic_option_white : R.drawable.ic_option_gray);
            ImageViewClose.setPadding(Misc.ToDP(Activity, 8), Misc.ToDP(Activity, 8), Misc.ToDP(Activity, 8) , Misc.ToDP(Activity, 8));
            ImageViewClose.setId(ID2_CLOSE);

            RelativeLayoutInfo.addView(ImageViewClose);

            RelativeLayout.LayoutParams TextViewLevelParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewLevelParam.addRule(RelativeLayout.BELOW, ViewLine.getId());
            TextViewLevelParam.setMargins(Misc.ToDP(Activity, 15), Misc.ToDP(Activity, 10), Misc.ToDP(Activity, 15), Misc.ToDP(Activity, 10));
            TextViewLevelParam.addRule(Misc.Align("R"));

            TextView TextViewLevel = new TextView(Activity, 14, true);
            TextViewLevel.setLayoutParams(TextViewLevelParam);
            TextViewLevel.setText(Activity.getString(R.string.InboxUIUserLevel));
            TextViewLevel.setTextColor(Misc.IsDark(Activity) ? ContextCompat.getColor(Activity, R.color.White) : ContextCompat.getColor(Activity, R.color.Gray4));
            TextViewLevel.setId(Misc.GenerateViewID());

            RelativeLayoutInfo.addView(TextViewLevel);

            RelativeLayout.LayoutParams CircleViewLevelParam = new RelativeLayout.LayoutParams(Misc.ToDP(Activity, 75), Misc.ToDP(Activity, 75));
            CircleViewLevelParam.addRule(RelativeLayout.BELOW, TextViewLevel.getId());
            CircleViewLevelParam.setMargins(Misc.ToDP(Activity, 15), Misc.ToDP(Activity, 10), Misc.ToDP(Activity, 15), 0);
            CircleViewLevelParam.addRule(Misc.Align("R"));

            CircleView CircleViewLevel = new CircleView(Activity);
            CircleViewLevel.setLayoutParams(CircleViewLevelParam);
            CircleViewLevel.setId(ID2_LEVEL);
            CircleViewLevel.SetMessage("1");
            CircleViewLevel.SetMessageSize(35);
            CircleViewLevel.SetMessageColor(R.color.BlueLight);
            CircleViewLevel.SetStrokeColor(Misc.IsDark(Activity) ? R.color.White : R.color.Gray2);
            CircleViewLevel.SetStrokeWidth(1);
            CircleViewLevel.SetProgressColor(R.color.BlueLight);
            CircleViewLevel.SetProgressWidth(4);
            CircleViewLevel.SetProgressPercentage(18);
            CircleViewLevel.InvalidateTextPaints();

            RelativeLayoutInfo.addView(CircleViewLevel);

            RelativeLayout.LayoutParams RelativeLayoutRatingParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            RelativeLayoutRatingParam.setMargins(Misc.ToDP(Activity, Misc.IsRTL() ? 15 : 5), Misc.ToDP(Activity, 5), Misc.ToDP(Activity, !Misc.IsRTL() ? 15 : 5), 0);
            RelativeLayoutRatingParam.addRule(Misc.AlignTo("R"), CircleViewLevel.getId());
            RelativeLayoutRatingParam.addRule(RelativeLayout.BELOW, TextViewLevel.getId());

            RelativeLayout RelativeLayoutRating = new RelativeLayout(Activity);
            RelativeLayoutRating.setLayoutParams(RelativeLayoutRatingParam);
            RelativeLayoutRating.setId(Misc.GenerateViewID());

            RelativeLayoutInfo.addView(RelativeLayoutRating);

            RelativeLayout.LayoutParams TextViewRatingParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewRatingParam.addRule(Misc.Align("R"));

            TextView TextViewRating = new TextView(Activity, 14, false);
            TextViewRating.setLayoutParams(TextViewRatingParam);
            TextViewRating.setText(Activity.getString(R.string.InboxUIRating));
            TextViewRating.setTextColor(ContextCompat.getColor(Activity, R.color.BlueLight));

            RelativeLayoutRating.addView(TextViewRating);

            RelativeLayout.LayoutParams TextViewNumberParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewNumberParam.setMargins(0, Misc.ToDP(Activity, 2), 0, 0);
            TextViewNumberParam.addRule(Misc.Align("L"));

            TextView TextViewNumber = new TextView(Activity, 12, false);
            TextViewNumber.setLayoutParams(TextViewNumberParam);
            TextViewNumber.setText(("10/60"));
            TextViewNumber.setId(ID2_NUMBER);
            TextViewNumber.setTextColor(Misc.IsDark(Activity) ? ContextCompat.getColor(Activity, R.color.White) : ContextCompat.getColor(Activity, R.color.Gray4));

            RelativeLayoutRating.addView(TextViewNumber);

            RelativeLayout.LayoutParams LineViewRatingParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(Activity, 10));
            LineViewRatingParam.setMargins(0, Misc.ToDP(Activity, 3), 0, 0);
            LineViewRatingParam.addRule(Misc.AlignTo("R"), CircleViewLevel.getId());
            LineViewRatingParam.addRule(RelativeLayout.BELOW, TextViewNumber.getId());

            LineView LineViewRating = new LineView(Activity);
            LineViewRating.setLayoutParams(LineViewRatingParam);
            LineViewRating.setId(ID2_RATING);
            LineViewRating.SetStrokeColor(Misc.IsDark(Activity) ? R.color.White : R.color.Gray2);
            LineViewRating.SetStrokeWidth(7);
            LineViewRating.SetProgressColor(R.color.BlueLight);
            LineViewRating.SetProgressPercent(33);
            LineViewRating.InvalidateTextPaints();

            RelativeLayoutRating.addView(LineViewRating);

            RelativeLayout.LayoutParams TextViewInfoParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewInfoParam.addRule(RelativeLayout.BELOW, LineViewRating.getId());
            TextViewInfoParam.addRule(Misc.Align("L"));

            TextView TextViewInfo = new TextView(Activity, 12, false);
            TextViewInfo.setLayoutParams(TextViewInfoParam);
            TextViewInfo.setLineSpacing(1.0f, 0.7f);
            TextViewInfo.setText((Activity.getString(R.string.InboxUIInfo) + " " + Activity.getString(R.string.InboxUIInfo2)), TextView.BufferType.SPANNABLE);
            TextViewInfo.setId(Misc.GenerateViewID());
            TextViewInfo.setTextColor(Misc.IsDark(Activity) ? ContextCompat.getColor(Activity, R.color.White) : ContextCompat.getColor(Activity, R.color.Gray4));

            Spannable Span = (Spannable) TextViewInfo.getText();
            ClickableSpan ClickableSpanMessage = new ClickableSpan()
            {
                @Override
                public void onClick(View v)
                {
                    Activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://biogram.co")));
                }

                @Override
                public void updateDrawState(TextPaint t)
                {
                    super.updateDrawState(t);
                    t.setColor(ContextCompat.getColor(Activity, R.color.BlueLight));
                    t.setUnderlineText(false);
                }
            };
            Span.setSpan(ClickableSpanMessage, Activity.getString(R.string.InboxUIInfo).length() + 1, Span.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            RelativeLayoutRating.addView(TextViewInfo);

            RelativeLayout.LayoutParams LinearLayoutMoreParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 0);
            LinearLayoutMoreParam.setMargins(0, Misc.ToDP(Activity, 5), 0, 0);
            LinearLayoutMoreParam.addRule(RelativeLayout.BELOW, RelativeLayoutRating.getId());

            LinearLayout LinearLayoutMore = new LinearLayout(Activity);
            LinearLayoutMore.setLayoutParams(LinearLayoutMoreParam);
            LinearLayoutMore.setId(ID2_MORE);

            RelativeLayoutInfo.addView(LinearLayoutMore);

            CircleView CircleViewJoin = new CircleView(Activity);
            CircleViewJoin.setLayoutParams(new LinearLayout.LayoutParams(0, Misc.ToDP(Activity, 60), 1));
            CircleViewJoin.setId(ID2_JOIN);
            CircleViewJoin.SetMessage("2");
            CircleViewJoin.SetMessageSize(17);
            CircleViewJoin.SetMessageBold();
            CircleViewJoin.SetMessageColor(R.color.BlueLight);
            CircleViewJoin.SetStrokeColor(R.color.BlueLight);
            CircleViewJoin.SetStrokeWidth(1);
            CircleViewJoin.SetSubMessage("Month");
            CircleViewJoin.SetSubMessageSize(10);
            CircleViewJoin.SetSubMessageSpace(12);
            CircleViewJoin.SetSubMessageColor(R.color.BlueLight);
            CircleViewJoin.InvalidateTextPaints();

            LinearLayoutMore.addView(CircleViewJoin);

            CircleView CircleViewPopular = new CircleView(Activity);
            CircleViewPopular.setLayoutParams(new LinearLayout.LayoutParams(0, Misc.ToDP(Activity, 60), 1));
            CircleViewPopular.setId(ID2_POPULAR);
            CircleViewPopular.SetMessage("4.5");
            CircleViewPopular.SetMessageSize(17);
            CircleViewPopular.SetMessageBold();
            CircleViewPopular.SetSubMessageSpace(14);
            CircleViewPopular.SetMessageColor(R.color.BlueLight);
            CircleViewPopular.SetStrokeColor(R.color.BlueLight);
            CircleViewPopular.SetStrokeWidth(1);
            CircleViewPopular.SetBitmap(R.drawable.ic_popular_blue, 24);
            CircleViewPopular.InvalidateTextPaints();

            LinearLayoutMore.addView(CircleViewPopular);

            CircleView CircleViewPoint = new CircleView(Activity);
            CircleViewPoint.setLayoutParams(new LinearLayout.LayoutParams(0, Misc.ToDP(Activity, 60), 1));
            CircleViewPoint.setId(ID2_POINT);
            CircleViewPoint.SetMessage("66K");
            CircleViewPoint.SetMessageSize(16);
            CircleViewPoint.SetMessageColor(R.color.White);
            CircleViewPoint.SetStrokeColor(R.color.BlueLight);
            CircleViewPoint.SetStrokeWidth(50);
            CircleViewPoint.InvalidateTextPaints();

            LinearLayoutMore.addView(CircleViewPoint);

            CircleImageView CircleImageViewMedal = new CircleImageView(Activity);
            CircleImageViewMedal.setLayoutParams(new LinearLayout.LayoutParams(0, Misc.ToDP(Activity, 60), 1));
            CircleImageViewMedal.setId(ID2_POINT);
            CircleImageViewMedal.SetBorderColor(Misc.IsDark(Activity) ? R.color.White : R.color.Gray2);
            CircleImageViewMedal.SetBorderWidth(1);
            CircleImageViewMedal.SetWidthPadding();
            CircleImageViewMedal.setPadding(Misc.ToDP(Activity, 10), Misc.ToDP(Activity, 10), Misc.ToDP(Activity, 10), Misc.ToDP(Activity, 10));
            CircleImageViewMedal.setImageResource(R.drawable.ic_write_plus);

            LinearLayoutMore.addView(CircleImageViewMedal);

            RelativeLayout.LayoutParams LinearLayoutMore2Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 0);
            LinearLayoutMore2Param.addRule(RelativeLayout.BELOW, LinearLayoutMore.getId());

            LinearLayout LinearLayoutMore2 = new LinearLayout(Activity);
            LinearLayoutMore2.setLayoutParams(LinearLayoutMore2Param);
            LinearLayoutMore2.setId(ID2_MORE2);

            RelativeLayoutInfo.addView(LinearLayoutMore2);

            TextView TextViewJoin = new TextView(Activity, 14, false);
            TextViewJoin.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            TextViewJoin.setText(Activity.getString(R.string.InboxUIJoin));
            TextViewJoin.setGravity(Gravity.CENTER);
            TextViewJoin.setTextColor(Misc.IsDark(Activity) ? ContextCompat.getColor(Activity, R.color.White) : ContextCompat.getColor(Activity, R.color.Gray4));

            LinearLayoutMore2.addView(TextViewJoin);

            TextView TextViewPopular = new TextView(Activity, 14, false);
            TextViewPopular.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            TextViewPopular.setText(Activity.getString(R.string.InboxUIPopular));
            TextViewPopular.setGravity(Gravity.CENTER);
            TextViewPopular.setTextColor(Misc.IsDark(Activity) ? ContextCompat.getColor(Activity, R.color.White) : ContextCompat.getColor(Activity, R.color.Gray4));

            LinearLayoutMore2.addView(TextViewPopular);

            TextView TextViewPoint = new TextView(Activity, 14, false);
            TextViewPoint.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            TextViewPoint.setText(Activity.getString(R.string.InboxUIPoint));
            TextViewPoint.setGravity(Gravity.CENTER);
            TextViewPoint.setTextColor(Misc.IsDark(Activity) ? ContextCompat.getColor(Activity, R.color.White) : ContextCompat.getColor(Activity, R.color.Gray4));

            LinearLayoutMore2.addView(TextViewPoint);

            TextView TextViewMedal = new TextView(Activity, 14, false);
            TextViewMedal.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            TextViewMedal.setText(Activity.getString(R.string.InboxUIMedal));
            TextViewMedal.setGravity(Gravity.CENTER);
            TextViewMedal.setTextColor(Misc.IsDark(Activity) ? ContextCompat.getColor(Activity, R.color.White) : ContextCompat.getColor(Activity, R.color.Gray4));

            LinearLayoutMore2.addView(TextViewMedal);

            RelativeLayout.LayoutParams ViewLine2Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(Activity, 5));
            ViewLine2Param.setMargins(0, Misc.ToDP(Activity, 9), 0, 0);
            ViewLine2Param.addRule(RelativeLayout.BELOW, LinearLayoutMore2.getId());

            View ViewLine2 = new View(Activity);
            ViewLine2.setLayoutParams(ViewLine2Param);
            ViewLine2.setBackgroundResource(Misc.IsDark(Activity) ? R.color.LineDark : R.color.Gray);

            RelativeLayoutInfo.addView(ViewLine2);

            return new ViewHolderMain(RelativeLayoutInfo, viewType);
        }

        return new ViewHolderMain(new View(Activity), viewType);
    }

    @Override
    public void onBindViewHolder(final PostAdapter.ViewHolderMain Holder, int position)
    {
        final int Position = Holder.getAdapterPosition();

        if (Holder.getItemViewType() == 1)
        {
            switch (Position)
            {
                case 2:
                    Holder.RelativeLayoutVote.setVisibility(View.VISIBLE);
                    Holder.TextViewVote1.setVisibility(View.VISIBLE);
                    Holder.TextViewVote1.setText("ali khobe");
                    Holder.TextViewVote1.FillBackground(46);
                    Holder.TextViewPercent1.setVisibility(View.VISIBLE);
                    Holder.TextViewPercent1.setText("46%");

                    Holder.TextViewVote2.setVisibility(View.VISIBLE);
                    Holder.TextViewVote2.setText("ali bad e");
                    Holder.TextViewVote2.FillBackground(61);
                    Holder.TextViewPercent2.setVisibility(View.VISIBLE);
                    Holder.TextViewPercent2.setText("61%");

                    Holder.TextViewVote3.setVisibility(View.VISIBLE);
                    Holder.TextViewVote3.setText("ali bad e");
                    Holder.TextViewVote3.FillBackground(100);
                    Holder.TextViewPercent3.setVisibility(View.VISIBLE);
                    Holder.TextViewPercent3.setText("100%");

                    Holder.TextViewVote4.setVisibility(View.VISIBLE);
                    Holder.TextViewVote4.setText("ali bad e");
                    Holder.TextViewVote4.FillBackground(18);
                    Holder.TextViewPercent4.setVisibility(View.VISIBLE);
                    Holder.TextViewPercent4.setText("18%");

                    Holder.TextViewVote5.setVisibility(View.VISIBLE);
                    Holder.TextViewVote5.setText("ali bad e");
                    Holder.TextViewVote5.FillBackground(37);
                    Holder.TextViewPercent5.setVisibility(View.VISIBLE);
                    Holder.TextViewPercent5.setText("37%");

                    Holder.TextViewResult.setText("5125 Votes");
                    break;
                case 3:
                    Holder.RelativeLayoutImage.setVisibility(View.VISIBLE);
                    Holder.ImageViewSingle.setVisibility(View.VISIBLE);
                    GlideApp.with(Activity)
                            .load("http://webneel.com/sites/default/files/images/blog/t-natuwal.jpg")
                            .placeholder(R.color.BlueGray2)
                            .transforms(new CenterCrop(), new RoundedCorners(Misc.ToDP(Activity, 3)))
                            .into(Holder.ImageViewSingle);
                    break;
                case 4:
                    Holder.RelativeLayoutImage.setVisibility(View.VISIBLE);
                    Holder.LinearLayoutDouble.setVisibility(View.VISIBLE);
                    GlideApp.with(Activity)
                            .load("http://webneel.com/wallpaper/sites/default/files/images/04-2013/mediterranean-beach-wallpaper.preview.jpg")
                            .placeholder(R.color.BlueGray2)
                            .transforms(new CenterCrop(), new RoundedCorners(Misc.ToDP(Activity, 3)))
                            .into(Holder.ImageViewDouble1);
                    GlideApp.with(Activity)
                            .load("http://webneel.com/wallpaper/sites/default/files/images/04-2013/tropical-beach-wallpaper.preview.jpg")
                            .placeholder(R.color.BlueGray2)
                            .transforms(new CenterCrop(), new RoundedCorners(Misc.ToDP(Activity, 3)))
                            .into(Holder.ImageViewDouble2);
                    break;
                case 5:
                    Holder.RelativeLayoutImage.setVisibility(View.VISIBLE);
                    Holder.LinearLayoutTriple.setVisibility(View.VISIBLE);
                    GlideApp.with(Activity)
                            .load("http://webneel.com/sites/default/files/images/blog/t-natuwal.jpg")
                            .placeholder(R.color.BlueGray2)
                            .transforms(new CenterCrop(), new RoundedCorners(Misc.ToDP(Activity, 3)))
                            .into(Holder.ImageViewTriple1);
                    GlideApp.with(Activity)
                            .load("http://webneel.com/wallpaper/sites/default1/files/images/04-2013/mediterranean-beach-wallpaper.preview.jpg")
                            .placeholder(R.color.BlueGray2)
                            .transforms(new CenterCrop(), new RoundedCorners(Misc.ToDP(Activity, 3)))
                            .into(Holder.ImageViewTriple2);
                    GlideApp.with(Activity)
                            .load("http://webneel.com/wallpaper/sites/defaul1t/files/images/04-2013/tropical-beach-wallpaper.preview.jpg")
                            .placeholder(R.color.BlueGray2)
                            .transforms(new CenterCrop(), new RoundedCorners(Misc.ToDP(Activity, 3)))
                            .into(Holder.ImageViewTriple3);
                    break;
                case 6:
                    Holder.RelativeLayoutImage.setVisibility(View.VISIBLE);
                    Holder.ImageViewSingle.setVisibility(View.VISIBLE);
                    GlideApp.with(Activity)
                            .load("http://webneel.com/sites/default1/files/images/blog/t-natuwal.jpg")
                            .placeholder(R.color.BlueGray2)
                            .transforms(new CenterCrop(), new RoundedCorners(Misc.ToDP(Activity, 3)))
                            .into(Holder.ImageViewSingle);
                    break;
                case 7:
                    Holder.RelativeLayoutVideo.setVisibility(View.VISIBLE);
                    GlideApp.with(Activity)
                            .load("http://webneel.com/sites/default/files/images/blog/t-natuwal.jpg")
                            .placeholder(R.color.BlueGray2)
                            .transforms(new CenterCrop(), new RoundedCorners(Misc.ToDP(Activity, 3)))
                            .into(Holder.ImageViewVideo);
                    break;
            }
        }
        else if (Holder.getItemViewType() == 2)
        {
            Holder.ImageViewClose.setOnClickListener(new View.OnClickListener()
            {
                boolean IsMore = true;

                @Override
                public void onClick(View v)
                {
                    if (IsMore)
                    {
                        ValueAnimator Anim = ValueAnimator.ofInt(Holder.LinearLayoutMore.getMeasuredHeight(), Misc.ToDP(Activity, 90));
                        Anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
                        {
                            @Override
                            public void onAnimationUpdate(ValueAnimator va)
                            {
                                int Height = (int) va.getAnimatedValue();

                                ViewGroup.LayoutParams Param = Holder.LinearLayoutMore.getLayoutParams();
                                Param.height = Math.min(Height, Misc.ToDP(Activity, 65));

                                Holder.LinearLayoutMore.setLayoutParams(Param);

                                ViewGroup.LayoutParams Param2 = Holder.LinearLayoutMore2.getLayoutParams();
                                Param2.height = Math.min(Height, Misc.ToDP(Activity, 20));

                                Holder.LinearLayoutMore2.setLayoutParams(Param2);
                            }
                        });
                        Anim.setDuration(300);
                        Anim.start();

                        IsMore = false;
                        Holder.ImageViewClose.setImageResource(Misc.IsDark(Activity) ? R.drawable.ic_close_white : R.drawable.ic_close_gray);
                        Holder.ImageViewClose.setPadding(0, 0, 0, 0);
                    }
                    else
                    {
                        PostList.remove(Position);
                        notifyDataSetChanged();
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int Position)
    {
        return PostList.get(Position).ViewType;
    }

    @Override
    public int getItemCount()
    {
        return PostList.size();
    }


    public static class PostStruct
    {
        int DataType; // 1: Image 2: Video 3: Vote 4: Music
        int ViewType;

        public PostStruct(int dataType, int viewType)
        {
            DataType = dataType;
            ViewType = viewType;
        }
    }

    public interface PullToRefreshListener
    {
        void OnRefresh();
    }
}
