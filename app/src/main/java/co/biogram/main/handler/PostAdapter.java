package co.biogram.main.handler;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Dialog;
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
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import co.biogram.main.R;
import co.biogram.main.fragment.FragmentActivity;
import co.biogram.main.ui.general.ImagePreviewUI;
import co.biogram.main.ui.general.VideoPreviewUI;
import co.biogram.main.ui.view.CircleImageView;
import co.biogram.main.ui.view.CircleView;
import co.biogram.main.ui.view.LineView;
import co.biogram.main.ui.view.LoadingView;
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
    private final int ID1_LIKE_COUNT = Misc.GenerateViewID();
    private final int ID1_COMMENT = Misc.GenerateViewID();
    private final int ID1_COMMENT_COUNT = Misc.GenerateViewID();
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
    private final int ID1_VOTE = Misc.GenerateViewID();
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
    private final int ID1_VOTE_CIRCLE = Misc.GenerateViewID();
    private final int ID1_VOTE_RESULT = Misc.GenerateViewID();
    private final int ID1_FILE_LAYOUT = Misc.GenerateViewID();
    private final int ID1_FILE_DOWNLOAD = Misc.GenerateViewID();
    private final int ID1_FILE_LOADING = Misc.GenerateViewID();
    private final int ID1_FILE_NAME = Misc.GenerateViewID();
    private final int ID1_FILE_DETAIL = Misc.GenerateViewID();

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
        ImageView ImageViewMedal;
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
        TextView TextViewVote;
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
        View VoteCircle;
        ImageView ImageViewLike;
        TextView TextViewLikeCount;
        ImageView ImageViewReplay;
        ImageView ImageViewComment;
        TextView TextViewCommentCount;
        ImageView ImageViewForward;
        RelativeLayout RelativeLayoutFile;
        ImageView ImageViewFile;
        LoadingView LoadingViewFile;
        TextView TextViewFileName;
        TextView TextViewFileDetail;

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
                ImageViewMedal = v.findViewById(ID1_MEDAL);
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
                TextViewVote = v.findViewById(ID1_VOTE);
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
                VoteCircle = v.findViewById(ID1_VOTE_CIRCLE);
                ImageViewLike = v.findViewById(ID1_LIKE);
                TextViewLikeCount = v.findViewById(ID1_LIKE_COUNT);
                ImageViewReplay = v.findViewById(ID1_REPLAY);
                ImageViewComment = v.findViewById(ID1_COMMENT);
                TextViewCommentCount = v.findViewById(ID1_COMMENT_COUNT);
                ImageViewForward = v.findViewById(ID1_FORWARD);
                RelativeLayoutFile = v.findViewById(ID1_FILE_LAYOUT);
                ImageViewFile = v.findViewById(ID1_FILE_DOWNLOAD);
                LoadingViewFile = v.findViewById(ID1_FILE_LOADING);
                TextViewFileName = v.findViewById(ID1_FILE_NAME);
                TextViewFileDetail = v.findViewById(ID1_FILE_DETAIL);
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

            LinearLayoutProfile.addView(TextViewName);

            ImageView ImageiewMedal = new ImageView(Activity);
            ImageiewMedal.setLayoutParams(new RelativeLayout.LayoutParams(Misc.ToDP(Activity, 24), Misc.ToDP(Activity, 24)));
            ImageiewMedal.setId(ID1_MEDAL);

            LinearLayoutProfile.addView(ImageiewMedal);

            TextView TextViewUsername = new TextView(Activity, 14, false);
            TextViewUsername.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            TextViewUsername.setTextColor(ContextCompat.getColor(Activity, R.color.Gray4));
            TextViewUsername.setId(ID1_USERNAME);
            TextViewUsername.setLineSpacing(1.0f, 3.0f);

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

            RelativeLayoutMain.addView(TextViewTime);

            RelativeLayout.LayoutParams TextViewMessageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewMessageParam.addRule(RelativeLayout.RIGHT_OF, ID1_PROFILE);
            TextViewMessageParam.addRule(RelativeLayout.BELOW, ID1_TIME);

            TextView TextViewMessage = new TextView(Activity, 14, false);
            TextViewMessage.setLayoutParams(TextViewMessageParam);
            TextViewMessage.setTextColor(Misc.IsDark(Activity) ? ContextCompat.getColor(Activity, R.color.White) : ContextCompat.getColor(Activity, R.color.Black));
            TextViewMessage.setPadding(0, 0, Misc.ToDP(Activity, 8), 0);
            TextViewMessage.setId(ID1_MESSAGE);

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
            ViewLineTriple2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.04f));
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
            TextViewVideo.setText(Activity.getString(R.string.PostAdapterVideo));

            RelativeLayoutVideo.addView(TextViewVideo);

            GradientDrawable DrawableRounded = new GradientDrawable();
            DrawableRounded.setCornerRadius(Misc.ToDP(Activity, 4));
            DrawableRounded.setStroke(Misc.ToDP(Activity, 1), ContextCompat.getColor(Activity, R.color.BlueGray));

            RelativeLayout RelativeLayoutVote = new RelativeLayout(Activity);
            RelativeLayoutVote.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            RelativeLayoutVote.setPadding(Misc.ToDP(Activity, 8), Misc.ToDP(Activity, 8), Misc.ToDP(Activity, 10), Misc.ToDP(Activity, 8));
            RelativeLayoutVote.setBackground(DrawableRounded);
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

            GradientDrawable DrawableVote3 = new GradientDrawable();
            DrawableVote3.setCornerRadius(Misc.ToDP(Activity, 100));
            DrawableVote3.setColor(ContextCompat.getColor(Activity, R.color.Black));

            View ViewVoteCircle = new View(Activity);
            ViewVoteCircle.setLayoutParams(new RelativeLayout.LayoutParams(Misc.ToDP(Activity, 6), Misc.ToDP(Activity, 6)));
            ViewVoteCircle.setBackground(DrawableVote3);
            ViewVoteCircle.setVisibility(View.GONE);
            ViewVoteCircle.setId(ID1_VOTE_CIRCLE);

            RelativeLayoutVote.addView(ViewVoteCircle);

            RelativeLayout.LayoutParams ViewVoteParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(Activity, 1));
            ViewVoteParam.setMargins(0, Misc.ToDP(Activity, 15), 0, 0);
            ViewVoteParam.addRule(RelativeLayout.BELOW, ID1_VOTE5);

            View ViewVote = new View(Activity);
            ViewVote.setLayoutParams(ViewVoteParam);
            ViewVote.setBackgroundResource(R.color.BlueGray);
            ViewVote.setId(Misc.GenerateViewID());

            RelativeLayoutVote.addView(ViewVote);

            RelativeLayout.LayoutParams TextViewVoteParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, Misc.ToDP(Activity, 30));
            TextViewVoteParam.setMargins(0, Misc.ToDP(Activity, 8), 0, 0);
            TextViewVoteParam.addRule(RelativeLayout.BELOW, ViewVote.getId());

            GradientDrawable DrawableVote2 = new GradientDrawable();
            DrawableVote2.setCornerRadius(Misc.ToDP(Activity, 2));
            DrawableVote2.setColor(ContextCompat.getColor(Activity, R.color.BlueLight));
            DrawableVote2.setStroke(Misc.ToDP(Activity, 1), ContextCompat.getColor(Activity, R.color.BlueLight));

            TextView TextViewVote = new TextView(Activity, 14, false);
            TextViewVote.setLayoutParams(TextViewVoteParam);
            TextViewVote.setTextColor(ContextCompat.getColor(Activity, R.color.White));
            TextViewVote.setBackground(DrawableVote2);
            TextViewVote.setPadding(Misc.ToDP(Activity, 10), Misc.ToDP(Activity, 5), Misc.ToDP(Activity, 10), Misc.ToDP(Activity, 5));
            TextViewVote.setGravity(Gravity.CENTER_VERTICAL);
            TextViewVote.setText(Activity.getString(R.string.PostAdapterVote));
            TextViewVote.setId(ID1_VOTE);

            RelativeLayoutVote.addView(TextViewVote);

            RelativeLayout.LayoutParams TextViewResultParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewResultParam.addRule(Misc.AlignTo("R"), TextViewVote.getId());
            TextViewResultParam.addRule(RelativeLayout.BELOW, ViewVote.getId());

            TextView TextViewResult = new TextView(Activity, 14, false);
            TextViewResult.setLayoutParams(TextViewResultParam);
            TextViewResult.setTextColor(ContextCompat.getColor(Activity, R.color.BlueGray2));
            TextViewResult.setPadding(Misc.ToDP(Activity, 5), Misc.ToDP(Activity, 14), 0, 0);
            TextViewResult.setGravity(Gravity.CENTER_VERTICAL);
            TextViewResult.setId(ID1_VOTE_RESULT);

            RelativeLayoutVote.addView(TextViewResult);

            RelativeLayout RelativeLayoutFile = new RelativeLayout(Activity);
            RelativeLayoutFile.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(Activity, 70)));
            RelativeLayoutFile.setPadding(0, Misc.ToDP(Activity, 10), Misc.ToDP(Activity, 10), Misc.ToDP(Activity, 10));
            RelativeLayoutFile.setVisibility(View.GONE);
            RelativeLayoutFile.setId(ID1_FILE_LAYOUT);

            RelativeLayoutContent.addView(RelativeLayoutFile);

            RelativeLayout.LayoutParams ImageViewFileParam = new RelativeLayout.LayoutParams(Misc.ToDP(Activity, 50), Misc.ToDP(Activity, 50));
            ImageViewFileParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

            GradientDrawable DrawableFile = new GradientDrawable();
            DrawableFile.setCornerRadius(Misc.ToDP(Activity, 4));
            DrawableFile.setColor(ContextCompat.getColor(Activity, R.color.BlueLight));

            ImageView ImageViewFile = new ImageView(Activity);
            ImageViewFile.setLayoutParams(ImageViewFileParam);
            ImageViewFile.setPadding(Misc.ToDP(Activity, 12), Misc.ToDP(Activity, 12), Misc.ToDP(Activity, 12), Misc.ToDP(Activity, 12));
            ImageViewFile.setImageResource(R.drawable.ic_download_white);
            ImageViewFile.setId(Misc.GenerateViewID());
            ImageViewFile.setBackground(DrawableFile);
            ImageViewFile.setId(ID1_FILE_DOWNLOAD);

            RelativeLayoutFile.addView(ImageViewFile);

            RelativeLayout.LayoutParams LoadingViewParam = new RelativeLayout.LayoutParams(Misc.ToDP(Activity, 50), Misc.ToDP(Activity, 50));
            LoadingViewParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

            LoadingView LoadingViewFile = new LoadingView(Activity);
            LoadingViewFile.setLayoutParams(ImageViewFileParam);
            LoadingViewFile.setBackground(DrawableFile);
            LoadingViewFile.SetColor(R.color.White);
            LoadingViewFile.SetScale(1.90f);
            LoadingViewFile.SetSize(5);
            LoadingViewFile.setVisibility(View.VISIBLE);
            LoadingViewFile.setId(ID1_FILE_LOADING);
            LoadingViewFile.Start();

            RelativeLayoutFile.addView(LoadingViewFile);

            RelativeLayout.LayoutParams TextViewFileNameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewFileNameParam.addRule(RelativeLayout.RIGHT_OF, ID1_FILE_DOWNLOAD);
            TextViewFileNameParam.setMargins(Misc.ToDP(Activity, 8), Misc.ToDP(Activity, 2), 0, 0);

            TextView TextViewFileName = new TextView(Activity, 14, false);
            TextViewFileName.setLayoutParams(TextViewFileNameParam);
            TextViewFileName.setTextColor(ContextCompat.getColor(Activity, R.color.Black));
            TextViewFileName.setId(ID1_FILE_NAME);

            RelativeLayoutFile.addView(TextViewFileName);

            RelativeLayout.LayoutParams TextViewFileDetailParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewFileDetailParam.addRule(RelativeLayout.RIGHT_OF, ID1_FILE_DOWNLOAD);
            TextViewFileDetailParam.addRule(RelativeLayout.BELOW, ID1_FILE_NAME);
            TextViewFileDetailParam.setMargins(Misc.ToDP(Activity, 8),0, 0, 0);

            TextView TextViewFileDetail = new TextView(Activity, 12, false);
            TextViewFileDetail.setLayoutParams(TextViewFileDetailParam);
            TextViewFileDetail.setTextColor(ContextCompat.getColor(Activity, R.color.BlueGray2));
            TextViewFileDetail.setId(ID1_FILE_DETAIL);

            RelativeLayoutFile.addView(TextViewFileDetail);







            RelativeLayout.LayoutParams RelativeLayoutToolParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(Activity, 40));
            RelativeLayoutToolParam.addRule(RelativeLayout.BELOW, RelativeLayoutContent.getId());
            RelativeLayoutToolParam.addRule(RelativeLayout.RIGHT_OF, ID1_PROFILE);

            RelativeLayout RelativeLayoutTool = new RelativeLayout(Activity);
            RelativeLayoutTool.setLayoutParams(RelativeLayoutToolParam);

            RelativeLayoutMain.addView(RelativeLayoutTool);

            RelativeLayout.LayoutParams ImageViewLikeParam = new RelativeLayout.LayoutParams(Misc.ToDP(Activity, 32), RelativeLayout.LayoutParams.MATCH_PARENT);
            ImageViewLikeParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

            ImageView ImageViewLike = new ImageView(Activity);
            ImageViewLike.setLayoutParams(ImageViewLikeParam);
            ImageViewLike.setPadding(Misc.ToDP(Activity, 3), Misc.ToDP(Activity, 3), Misc.ToDP(Activity, 3), Misc.ToDP(Activity, 3));
            ImageViewLike.setImageResource(R.drawable.ic_like);
            ImageViewLike.setId(ID1_LIKE);

            RelativeLayoutTool.addView(ImageViewLike);

            RelativeLayout.LayoutParams TextViewLikeParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            TextViewLikeParam.setMargins(0, 0, Misc.ToDP(Activity, 30), 0);
            TextViewLikeParam.addRule(RelativeLayout.RIGHT_OF, ID1_LIKE);

            TextView TextViewLike = new TextView(Activity, 12, false);
            TextViewLike.setLayoutParams(TextViewLikeParam);
            TextViewLike.setTextColor(ContextCompat.getColor(Activity, R.color.BlueGray2));
            TextViewLike.setPadding(0, Misc.ToDP(Activity, 4), 0, 0);
            TextViewLike.setGravity(Gravity.CENTER_VERTICAL);
            TextViewLike.setId(ID1_LIKE_COUNT);

            RelativeLayoutTool.addView(TextViewLike);

            RelativeLayout.LayoutParams ImageViewReplayParam = new RelativeLayout.LayoutParams(Misc.ToDP(Activity, 32), RelativeLayout.LayoutParams.MATCH_PARENT);
            ImageViewReplayParam.setMargins(0, 0, Misc.ToDP(Activity, 30), 0);
            ImageViewReplayParam.addRule(RelativeLayout.RIGHT_OF, ID1_LIKE_COUNT);

            ImageView ImageViewReplay = new ImageView(Activity);
            ImageViewReplay.setLayoutParams(ImageViewReplayParam);
            ImageViewReplay.setPadding(Misc.ToDP(Activity, 4), Misc.ToDP(Activity, 4), Misc.ToDP(Activity, 4), Misc.ToDP(Activity, 4));
            ImageViewReplay.setImageResource(R.drawable.ic_replay);
            ImageViewReplay.setId(ID1_REPLAY);

            RelativeLayoutTool.addView(ImageViewReplay);

            RelativeLayout.LayoutParams ImageViewCommentParam = new RelativeLayout.LayoutParams(Misc.ToDP(Activity, 32), RelativeLayout.LayoutParams.MATCH_PARENT);
            ImageViewCommentParam.addRule(RelativeLayout.RIGHT_OF, ID1_REPLAY);

            ImageView ImageViewComment = new ImageView(Activity);
            ImageViewComment.setLayoutParams(ImageViewCommentParam);
            ImageViewComment.setPadding(Misc.ToDP(Activity, 3), Misc.ToDP(Activity, 3), Misc.ToDP(Activity, 3), Misc.ToDP(Activity, 3));
            ImageViewComment.setImageResource(R.drawable.ic_comment);
            ImageViewComment.setId(ID1_COMMENT);

            RelativeLayoutTool.addView(ImageViewComment);

            RelativeLayout.LayoutParams TextViewCommentParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            TextViewCommentParam.setMargins(0, 0, Misc.ToDP(Activity, 30), 0);
            TextViewCommentParam.addRule(RelativeLayout.RIGHT_OF, ID1_COMMENT);

            TextView TextViewComment = new TextView(Activity, 12, false);
            TextViewComment.setLayoutParams(TextViewCommentParam);
            TextViewComment.setTextColor(ContextCompat.getColor(Activity, R.color.BlueGray2));
            TextViewComment.setPadding(0, Misc.ToDP(Activity, 5), 0, 0);
            TextViewComment.setGravity(Gravity.CENTER_VERTICAL);
            TextViewComment.setId(ID1_COMMENT_COUNT);

            RelativeLayoutTool.addView(TextViewComment);

            RelativeLayout.LayoutParams ImageViewForwardParam = new RelativeLayout.LayoutParams(Misc.ToDP(Activity, 32), RelativeLayout.LayoutParams.MATCH_PARENT);
            ImageViewForwardParam.addRule(RelativeLayout.RIGHT_OF, ID1_COMMENT_COUNT);

            ImageView ImageViewForward = new ImageView(Activity);
            ImageViewForward.setLayoutParams(ImageViewForwardParam);
            ImageViewForward.setPadding(Misc.ToDP(Activity, 4), Misc.ToDP(Activity, 4), Misc.ToDP(Activity, 4), Misc.ToDP(Activity, 4));
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
            try
            {
                GlideApp.with(Activity).load(PostList.get(Position).Profile).placeholder(R.color.BlueGray2).into(Holder.CircleImageViewProfile);
                Holder.TextViewName.setText(PostList.get(Position).Name);
                GlideApp.with(Activity).load(PostList.get(Position).Medal).into(Holder.ImageViewMedal);
                Holder.TextViewUsername.setText(PostList.get(Position).Username);
                Holder.TextViewTime.setText(Misc.GetTime(PostList.get(Position).Time));
                Holder.TextViewMessage.setText(PostList.get(Position).Message);
                TagHandler.Show(Holder.TextViewMessage);

                Holder.RelativeLayoutVote.setVisibility(View.GONE);
                Holder.RelativeLayoutVideo.setVisibility(View.GONE);
                Holder.RelativeLayoutImage.setVisibility(View.GONE);
                Holder.RelativeLayoutFile.setVisibility(View.GONE);

                switch (PostList.get(Position).DataType)
                {
                    case 1:
                    {
                        Holder.RelativeLayoutImage.setVisibility(View.VISIBLE);
                        Holder.ImageViewSingle.setVisibility(View.GONE);
                        Holder.LinearLayoutDouble.setVisibility(View.GONE);
                        Holder.LinearLayoutTriple.setVisibility(View.GONE);

                        JSONArray URL = new JSONArray(PostList.get(Position).Data);

                        switch (URL.length())
                        {
                            case 1:
                                final String URL1 = URL.get(0).toString();
                                Holder.ImageViewSingle.setVisibility(View.VISIBLE);
                                GlideApp.with(Activity).load(URL1).placeholder(R.color.BlueGray2).transforms(new CenterCrop(), new RoundedCorners(Misc.ToDP(Activity, 3))).into(Holder.ImageViewSingle);
                                Holder.ImageViewSingle.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { Activity.GetManager().OpenView(new ImagePreviewUI(URL1), R.id.ContainerFull, "ImagePreviewUI");  } });
                            break;
                            case 2:
                                final String URLD1 = URL.get(0).toString();
                                final String URLD2 = URL.get(1).toString();
                                Holder.LinearLayoutDouble.setVisibility(View.VISIBLE);
                                GlideApp.with(Activity).load(URLD1).placeholder(R.color.BlueGray2).transforms(new CenterCrop(), new RoundedCorners(Misc.ToDP(Activity, 3))).into(Holder.ImageViewDouble1);
                                Holder.ImageViewDouble1.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { Activity.GetManager().OpenView(new ImagePreviewUI(URLD1, URLD2), R.id.ContainerFull, "ImagePreviewUI"); } });
                                GlideApp.with(Activity).load(URLD2).placeholder(R.color.BlueGray2).transforms(new CenterCrop(), new RoundedCorners(Misc.ToDP(Activity, 3))).into(Holder.ImageViewDouble2);
                                Holder.ImageViewDouble2.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { Activity.GetManager().OpenView(new ImagePreviewUI(URLD2, URLD1), R.id.ContainerFull, "ImagePreviewUI"); } });
                                break;
                            case 3:
                                final String URLT1 = URL.get(0).toString();
                                final String URLT2 = URL.get(1).toString();
                                final String URLT3 = URL.get(2).toString();
                                Holder.LinearLayoutTriple.setVisibility(View.VISIBLE);
                                GlideApp.with(Activity).load(URLT1).placeholder(R.color.BlueGray2).transforms(new CenterCrop(), new RoundedCorners(Misc.ToDP(Activity, 3))).into(Holder.ImageViewTriple1);
                                Holder.ImageViewTriple1.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { Activity.GetManager().OpenView(new ImagePreviewUI(URLT1, URLT2, URLT3), R.id.ContainerFull, "ImagePreviewUI"); } });
                                GlideApp.with(Activity).load(URLT2).placeholder(R.color.BlueGray2).transforms(new CenterCrop(), new RoundedCorners(Misc.ToDP(Activity, 3))).into(Holder.ImageViewTriple2);
                                Holder.ImageViewTriple2.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { Activity.GetManager().OpenView(new ImagePreviewUI(URLT2, URLT3, URLT1), R.id.ContainerFull, "ImagePreviewUI"); } });
                                GlideApp.with(Activity).load(URLT3).placeholder(R.color.BlueGray2).transforms(new CenterCrop(), new RoundedCorners(Misc.ToDP(Activity, 3))).into(Holder.ImageViewTriple3);
                                Holder.ImageViewTriple3.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { Activity.GetManager().OpenView(new ImagePreviewUI(URLT3, URLT1, URLT2), R.id.ContainerFull, "ImagePreviewUI"); } });
                            break;
                        }
                    }
                    break;
                    case 2:
                    {
                        JSONArray URL = new JSONArray(PostList.get(Position).Data);
                        final String VideoURL = URL.get(2).toString();

                        Holder.RelativeLayoutVideo.setVisibility(View.VISIBLE);
                        GlideApp.with(Activity).load(URL.get(0).toString()).placeholder(R.color.BlueGray2).transforms(new CenterCrop(), new RoundedCorners(Misc.ToDP(Activity, 3))).into(Holder.ImageViewVideo);
                        Holder.ImageViewVideo.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { Activity.GetManager().OpenView(new VideoPreviewUI(VideoURL, false), R.id.ContainerFull, "VideoPreviewUI"); } });

                        int Time = Integer.parseInt(URL.get(1).toString());
                        int Min = Time / 60;
                        int Sec = Time - (Min * 60);

                        Holder.TextViewDurotion.setText((String.valueOf(Min) + ":" + String.valueOf(Sec)));
                    }
                    break;
                    case 3:
                    {
                        final JSONObject Vote = new JSONObject(PostList.get(Position).Data);

                        Holder.RelativeLayoutVote.setVisibility(View.VISIBLE);
                        Holder.TextViewResult.setText((Vote.getString("Total") + " " + Activity.getString(R.string.PostAdapterVotes)));

                        int Total = Integer.parseInt(Vote.getString("Total"));
                        int Per;

                        if (!Vote.isNull("V1"))
                        {
                            Per = Vote.getInt("V1V") * 100 / Total;
                            Holder.TextViewVote1.setVisibility(View.VISIBLE);
                            Holder.TextViewVote1.setText(Vote.getString("V1"));
                            Holder.TextViewVote1.FillBackground(Per);
                            Holder.TextViewPercent1.setVisibility(View.VISIBLE);
                            Holder.TextViewPercent1.setText((String.valueOf(Per) + "%"));
                        }

                        if (!Vote.isNull("V2"))
                        {
                            Per = Vote.getInt("V2V") * 100 / Total;
                            Holder.TextViewVote2.setVisibility(View.VISIBLE);
                            Holder.TextViewVote2.setText(Vote.getString("V2"));
                            Holder.TextViewVote2.FillBackground(Per);
                            Holder.TextViewPercent2.setVisibility(View.VISIBLE);
                            Holder.TextViewPercent2.setText((String.valueOf(Per) + "%"));
                        }

                        if (!Vote.isNull("V3"))
                        {
                            Per = Vote.getInt("V3V") * 100 / Total;
                            Holder.TextViewVote3.setVisibility(View.VISIBLE);
                            Holder.TextViewVote3.setText(Vote.getString("V3"));
                            Holder.TextViewVote3.FillBackground(Per);
                            Holder.TextViewPercent3.setVisibility(View.VISIBLE);
                            Holder.TextViewPercent3.setText((String.valueOf(Per) + "%"));
                        }

                        if (!Vote.isNull("V4"))
                        {
                            Per = Vote.getInt("V4V") * 100 / Total;
                            Holder.TextViewVote4.setVisibility(View.VISIBLE);
                            Holder.TextViewVote4.setText(Vote.getString("V4"));
                            Holder.TextViewVote4.FillBackground(Per);
                            Holder.TextViewPercent4.setVisibility(View.VISIBLE);
                            Holder.TextViewPercent4.setText((String.valueOf(Per) + "%"));
                        }

                        if (!Vote.isNull("V5"))
                        {
                            Per = Vote.getInt("V5V") * 100 / Total;
                            Holder.TextViewVote5.setVisibility(View.VISIBLE);
                            Holder.TextViewVote5.setText(Vote.getString("V5"));
                            Holder.TextViewVote5.FillBackground(Per);
                            Holder.TextViewPercent5.setVisibility(View.VISIBLE);
                            Holder.TextViewPercent5.setText((String.valueOf(Per) + "%"));
                        }

                        if (Vote.getInt("Vote") > 0)
                        {
                            int ID = 0;
                            int Top = 17;

                            switch (Vote.getInt("Vote"))
                            {
                                case 1: Top = 12; break;
                                case 2: ID = Holder.TextViewVote1.getId(); break;
                                case 3: ID = Holder.TextViewVote2.getId(); break;
                                case 4: ID = Holder.TextViewVote3.getId(); break;
                                case 5: ID = Holder.TextViewVote4.getId(); break;
                            }

                            RelativeLayout.LayoutParams Param = (RelativeLayout.LayoutParams) Holder.VoteCircle.getLayoutParams();
                            Param.setMargins(0, Misc.ToDP(Activity, Top), Misc.ToDP(Activity, 10), 0);
                            Param.addRule(RelativeLayout.LEFT_OF, Holder.TextViewPercent1.getId());

                            if (ID != 0)
                                Param.addRule(RelativeLayout.BELOW, ID);

                            Holder.VoteCircle.setLayoutParams(Param);
                            Holder.VoteCircle.setVisibility(View.VISIBLE);
                            Holder.VoteCircle.invalidate();
                        }

                        Holder.TextViewVote.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View view)
                            {
                                final Dialog DialogVote = new Dialog(Activity);
                                DialogVote.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                DialogVote.setCancelable(true);

                                LinearLayout LinearLayoutVote = new LinearLayout(Activity);
                                LinearLayoutVote.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                                LinearLayoutVote.setOrientation(LinearLayout.VERTICAL);

                                RelativeLayout RelativeLayoutHeader = new RelativeLayout(Activity);
                                RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(Activity, 56)));

                                LinearLayoutVote.addView(RelativeLayoutHeader);

                                RelativeLayout.LayoutParams TextViewTitleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, Misc.ToDP(Activity, 56));
                                TextViewTitleParam.addRule(Misc.Align("R"));

                                TextView TextViewTitle = new TextView(Activity, 16, false);
                                TextViewTitle.setLayoutParams(TextViewTitleParam);
                                TextViewTitle.setTextColor(ContextCompat.getColor(Activity, R.color.Black));
                                TextViewTitle.setText(Activity.getString(R.string.PostAdapterVoteChoice));
                                TextViewTitle.setPadding(Misc.ToDP(Activity, 15), 0, Misc.ToDP(Activity, 15), 0);
                                TextViewTitle.setGravity(Gravity.CENTER_VERTICAL);

                                RelativeLayoutHeader.addView(TextViewTitle);

                                RelativeLayout.LayoutParams ImageViewCloseParam = new RelativeLayout.LayoutParams(Misc.ToDP(Activity, 56), Misc.ToDP(Activity, 56));
                                ImageViewCloseParam.addRule(Misc.Align("L"));

                                ImageView ImageViewClose = new ImageView(Activity);
                                ImageViewClose.setLayoutParams(ImageViewCloseParam);
                                ImageViewClose.setImageResource(R.drawable.ic_close_blue);
                                ImageViewClose.setPadding(Misc.ToDP(Activity, 6), Misc.ToDP(Activity, 6), Misc.ToDP(Activity, 6), Misc.ToDP(Activity, 6));
                                ImageViewClose.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { DialogVote.dismiss(); } });

                                RelativeLayoutHeader.addView(ImageViewClose);

                                View ViewLine = new View(Activity);
                                ViewLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(Activity, 1)));
                                ViewLine.setBackgroundResource(R.color.Gray2);

                                LinearLayoutVote.addView(ViewLine);

                                try
                                {
                                    RelativeLayout.LayoutParams TextViewVoteParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(Activity, 56));
                                    TextViewVoteParam.addRule(Misc.Align("R"));

                                    if (!Vote.isNull("V1"))
                                    {
                                        TextView TextViewVote1 = new TextView(Activity, 16, false);
                                        TextViewVote1.setLayoutParams(TextViewVoteParam);
                                        TextViewVote1.setTextColor(ContextCompat.getColor(Activity, R.color.Black));
                                        TextViewVote1.setText(Vote.getString("V1"));
                                        TextViewVote1.setPadding(Misc.ToDP(Activity, 15), 0, Misc.ToDP(Activity, 15), 0);
                                        TextViewVote1.setGravity(Misc.Gravity("L") | Gravity.CENTER_VERTICAL);
                                        TextViewVote1.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { DialogVote.dismiss(); Misc.ChangeLanguage(Activity, "en"); } });

                                        if (Vote.getInt("Vote") == 1)
                                            TextViewVote1.setBackgroundResource(R.color.BlueGray);

                                        LinearLayoutVote.addView(TextViewVote1);
                                    }

                                    if (!Vote.isNull("V2"))
                                    {
                                        View ViewVote1 = new View(Activity);
                                        ViewVote1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(Activity, 1)));
                                        ViewVote1.setBackgroundResource(R.color.Gray);

                                        LinearLayoutVote.addView(ViewVote1);

                                        TextView TextViewVote2 = new TextView(Activity, 16, false);
                                        TextViewVote2.setLayoutParams(TextViewVoteParam);
                                        TextViewVote2.setTextColor(ContextCompat.getColor(Activity, R.color.Black));
                                        TextViewVote2.setText(Vote.getString("V2"));
                                        TextViewVote2.setPadding(Misc.ToDP(Activity, 15), 0, Misc.ToDP(Activity, 15), 0);
                                        TextViewVote2.setGravity(Misc.Gravity("L") | Gravity.CENTER_VERTICAL);
                                        TextViewVote2.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { DialogVote.dismiss(); Misc.ChangeLanguage(Activity, "en"); } });

                                        if (Vote.getInt("Vote") == 2)
                                            TextViewVote2.setBackgroundResource(R.color.BlueGray);

                                        LinearLayoutVote.addView(TextViewVote2);
                                    }

                                    if (!Vote.isNull("V3"))
                                    {
                                        View ViewVote2 = new View(Activity);
                                        ViewVote2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(Activity, 1)));
                                        ViewVote2.setBackgroundResource(R.color.Gray);

                                        LinearLayoutVote.addView(ViewVote2);

                                        TextView TextViewVote3 = new TextView(Activity, 16, false);
                                        TextViewVote3.setLayoutParams(TextViewVoteParam);
                                        TextViewVote3.setTextColor(ContextCompat.getColor(Activity, R.color.Black));
                                        TextViewVote3.setText(Vote.getString("V3"));
                                        TextViewVote3.setPadding(Misc.ToDP(Activity, 15), 0, Misc.ToDP(Activity, 15), 0);
                                        TextViewVote3.setGravity(Misc.Gravity("L") | Gravity.CENTER_VERTICAL);
                                        TextViewVote3.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { DialogVote.dismiss(); Misc.ChangeLanguage(Activity, "en"); } });

                                        if (Vote.getInt("Vote") == 3)
                                            TextViewVote3.setBackgroundResource(R.color.BlueGray);

                                        LinearLayoutVote.addView(TextViewVote3);
                                    }

                                    if (!Vote.isNull("V4"))
                                    {
                                        View ViewVote3 = new View(Activity);
                                        ViewVote3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(Activity, 1)));
                                        ViewVote3.setBackgroundResource(R.color.Gray);

                                        LinearLayoutVote.addView(ViewVote3);

                                        TextView TextViewVote4 = new TextView(Activity, 16, false);
                                        TextViewVote4.setLayoutParams(TextViewVoteParam);
                                        TextViewVote4.setTextColor(ContextCompat.getColor(Activity, R.color.Black));
                                        TextViewVote4.setText(Vote.getString("V4"));
                                        TextViewVote4.setPadding(Misc.ToDP(Activity, 15), 0, Misc.ToDP(Activity, 15), 0);
                                        TextViewVote4.setGravity(Misc.Gravity("L") | Gravity.CENTER_VERTICAL);
                                        TextViewVote4.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { DialogVote.dismiss(); Misc.ChangeLanguage(Activity, "en"); } });

                                        if (Vote.getInt("Vote") == 4)
                                            TextViewVote4.setBackgroundResource(R.color.BlueGray);

                                        LinearLayoutVote.addView(TextViewVote4);
                                    }

                                    if (!Vote.isNull("V5"))
                                    {
                                        View ViewVote4 = new View(Activity);
                                        ViewVote4.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(Activity, 1)));
                                        ViewVote4.setBackgroundResource(R.color.Gray);

                                        LinearLayoutVote.addView(ViewVote4);

                                        TextView TextViewVote5 = new TextView(Activity, 16, false);
                                        TextViewVote5.setLayoutParams(TextViewVoteParam);
                                        TextViewVote5.setTextColor(ContextCompat.getColor(Activity, R.color.Black));
                                        TextViewVote5.setText(Vote.getString("V5"));
                                        TextViewVote5.setPadding(Misc.ToDP(Activity, 15), 0, Misc.ToDP(Activity, 15), 0);
                                        TextViewVote5.setGravity(Misc.Gravity("L") | Gravity.CENTER_VERTICAL);
                                        TextViewVote5.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { DialogVote.dismiss(); Misc.ChangeLanguage(Activity, "en"); } });

                                        if (Vote.getInt("Vote") == 5)
                                            TextViewVote5.setBackgroundResource(R.color.BlueGray);

                                        LinearLayoutVote.addView(TextViewVote5);
                                    }
                                }
                                catch (Exception e)
                                {
                                    Misc.Debug("PostAdapter-Vote: " + e.toString());
                                }

                                DialogVote.setContentView(LinearLayoutVote);
                                DialogVote.show();
                            }
                        });
                    }
                    break;
                    case 4:
                        JSONObject FileJSON = new JSONObject(PostList.get(Position).Data);

                        Holder.RelativeLayoutFile.setVisibility(View.VISIBLE);
                        Holder.ImageViewFile.setVisibility(View.VISIBLE);
                        Holder.TextViewFileName.setText(FileJSON.getString("Name"));
                        Holder.TextViewFileDetail.setText(FileJSON.getString("Detail"));
                    break;
                }

                if (PostList.get(Position).IsLike)
                {
                    Holder.TextViewLikeCount.setTextColor(ContextCompat.getColor(Activity, R.color.RedLike));
                    GlideApp.with(Activity).load(R.drawable.ic_like_red).into(Holder.ImageViewLike);
                }
                else
                {
                    Holder.TextViewLikeCount.setTextColor(ContextCompat.getColor(Activity, R.color.BlueGray));
                    GlideApp.with(Activity).load(R.drawable.ic_like).into(Holder.ImageViewLike);
                }

                Holder.ImageViewLike.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (PostList.get(Position).IsLike)
                        {
                            Holder.TextViewLikeCount.setTextColor(ContextCompat.getColor(Activity, R.color.BlueGray));
                            Holder.ImageViewLike.setImageResource(R.drawable.ic_like);

                            ObjectAnimator Fade = ObjectAnimator.ofFloat(Holder.ImageViewLike, "alpha",  0.1f, 1f);
                            Fade.setDuration(200);

                            AnimatorSet AnimationSet = new AnimatorSet();
                            AnimationSet.play(Fade);
                            AnimationSet.start();

                            PostList.get(Position).DesLike();
                            PostList.get(Position).RevLike();

                            Holder.TextViewLikeCount.setText(String.valueOf(PostList.get(Position).LikeCount));
                        }
                        else
                        {
                            Holder.TextViewLikeCount.setTextColor(ContextCompat.getColor(Activity, R.color.RedLike));
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

                            PostList.get(Position).InsLike();
                            PostList.get(Position).RevLike();

                            Holder.TextViewLikeCount.setText(String.valueOf(PostList.get(Position).LikeCount));
                        }

                        // Todo Create API
                        /*AndroidNetworking.post(MiscHandler.GetRandomServer("PostLike"))
                                .addBodyParameter("PostID", PostList.get(Position).PostID)
                                .addHeaders("TOKEN", SharedHandler.GetString(Activity, "TOKEN"))
                                .setTag(Tag)
                                .build()
                                .getAsString(null);*/
                    }
                });

                Holder.TextViewLikeCount.setText(String.valueOf(PostList.get(Position).LikeCount));
                Holder.TextViewLikeCount.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) {  } }); // TODO Open Likes

                if (PostList.get(Position).IsComment)
                {
                    Holder.ImageViewComment.setVisibility(View.VISIBLE);
                    Holder.TextViewCommentCount.setVisibility(View.VISIBLE);
                    Holder.TextViewCommentCount.setText(String.valueOf(PostList.get(Position).CommentCount));
                    Holder.TextViewCommentCount.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) {  } });  // TODO Open Comment
                    Holder.ImageViewComment.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) {  } });  // TODO Open Comment
                }
                else
                {
                    Holder.ImageViewComment.setVisibility(View.GONE);
                    Holder.TextViewCommentCount.setVisibility(View.GONE);
                }
            }
            catch (Exception e)
            {
                Misc.Debug("PostAdapter-ViewType1: " + e.toString());
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
        String Profile;
        String Name;
        String Medal;
        String Username;
        int Time;
        String Message;
        int DataType; // 0: Message 1: Image 2: Video 3: Vote 4: File
        String Data;
        int ViewType = 1; // 0: Pull 1: Post 2: Level 3: Suggestion
        boolean IsLike;
        int LikeCount;
        boolean IsComment;
        int CommentCount;
        boolean IsDownloading;

        public PostStruct(String profile, String name, String medal, String username, int time, String message, int dataType, String data, boolean isLike, int likeCount, boolean isComment, int commentCount)
        {
            Profile = profile;
            Name = name;
            Medal = medal;
            Username = username;
            Time = time;
            Message = message;
            DataType = dataType;
            Data = data;
            IsLike = isLike;
            LikeCount = likeCount;
            IsComment = isComment;
            CommentCount = commentCount;
        }

        public PostStruct(int viewType)
        {
            ViewType = viewType;
        }

        void RevLike()
        {
            IsLike = !IsLike;
        }

        void InsLike()
        {
            LikeCount++;
        }

        void DesLike()
        {
            LikeCount--;
        }
    }

    public interface PullToRefreshListener
    {
        void OnRefresh();
    }
}
