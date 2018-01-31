package co.biogram.main.handler;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
    private ArrayList<PostStruct> PostList = new ArrayList<>();
    private PullToRefreshView RefreshView;
    private FragmentActivity Activity;
    private String Tag;

    // ViewType 1
    private final int ID1_PROFILE = Misc.ViewID();
    private final int ID1_NAME = Misc.ViewID();
    private final int ID1_MEDAL = Misc.ViewID();
    private final int ID1_USERNAME = Misc.ViewID();
    private final int ID1_TIME = Misc.ViewID();
    private final int ID1_MESSAGE = Misc.ViewID();
    private final int ID1_LIKE = Misc.ViewID();
    private final int ID1_LIKE_COUNT = Misc.ViewID();
    private final int ID1_COMMENT = Misc.ViewID();
    private final int ID1_COMMENT_COUNT = Misc.ViewID();
    private final int ID1_OPTION = Misc.ViewID();
    private final int ID1_PERSON1 = Misc.ViewID();
    private final int ID1_PERSON2 = Misc.ViewID();
    private final int ID1_PERSON3 = Misc.ViewID();
    private final int ID1_PERSON4 = Misc.ViewID();
    private final int ID1_IMAGE_LAYOUT = Misc.ViewID();
    private final int ID1_SINGLE = Misc.ViewID();
    private final int ID1_DOUBLE_LAYOUT = Misc.ViewID();
    private final int ID1_DOUBLE1 = Misc.ViewID();
    private final int ID1_DOUBLE2 = Misc.ViewID();
    private final int ID1_TRIPLE_LAYOUT = Misc.ViewID();
    private final int ID1_TRIPLE1 = Misc.ViewID();
    private final int ID1_TRIPLE2 = Misc.ViewID();
    private final int ID1_TRIPLE3 = Misc.ViewID();
    private final int ID1_VIDEO_LAYOUT = Misc.ViewID();
    private final int ID1_VIDEO_IMAGE = Misc.ViewID();
    private final int ID1_VIDEO_DUROTION = Misc.ViewID();
    private final int ID1_VOTE_LAYOUT = Misc.ViewID();
    private final int ID1_VOTE_TYPE1 = Misc.ViewID();
    private final int ID1_VOTE_TYPE1_SEL1 = Misc.ViewID();
    private final int ID1_VOTE_TYPE1_TEXT1 = Misc.ViewID();
    private final int ID1_VOTE_TYPE1_SEL2 = Misc.ViewID();
    private final int ID1_VOTE_TYPE1_TEXT2 = Misc.ViewID();
    private final int ID1_VOTE_TYPE1_SEL3 = Misc.ViewID();
    private final int ID1_VOTE_TYPE1_TEXT3 = Misc.ViewID();
    private final int ID1_VOTE_TYPE1_SEL4 = Misc.ViewID();
    private final int ID1_VOTE_TYPE1_TEXT4 = Misc.ViewID();
    private final int ID1_VOTE_TYPE1_SEL5 = Misc.ViewID();
    private final int ID1_VOTE_TYPE1_TEXT5 = Misc.ViewID();
    private final int ID1_VOTE_TYPE1_SUBMIT = Misc.ViewID();
    private final int ID1_VOTE_TYPE1_RESULT = Misc.ViewID();
    private final int ID1_VOTE_TYPE1_TIME = Misc.ViewID();
    private final int ID1_VOTE_TYPE2 = Misc.ViewID();
    private final int ID1_VOTE_TYPE2_TEXT1 = Misc.ViewID();
    private final int ID1_VOTE_TYPE2_TEXT2 = Misc.ViewID();
    private final int ID1_VOTE_TYPE2_TEXT3 = Misc.ViewID();
    private final int ID1_VOTE_TYPE2_TEXT4 = Misc.ViewID();
    private final int ID1_VOTE_TYPE2_TEXT5 = Misc.ViewID();
    private final int ID1_VOTE_TYPE2_PER1 = Misc.ViewID();
    private final int ID1_VOTE_TYPE2_PER2 = Misc.ViewID();
    private final int ID1_VOTE_TYPE2_PER3 = Misc.ViewID();
    private final int ID1_VOTE_TYPE2_PER4 = Misc.ViewID();
    private final int ID1_VOTE_TYPE2_PER5 = Misc.ViewID();
    private final int ID1_VOTE_TYPE2_CIRCLE = Misc.ViewID();
    private final int ID1_VOTE_TYPE2_RESULT = Misc.ViewID();
    private final int ID1_VOTE_TYPE2_TIME = Misc.ViewID();
















    private final int ID1_FILE_LAYOUT = Misc.ViewID();
    private final int ID1_FILE_DOWNLOAD = Misc.ViewID();
    private final int ID1_FILE_LOADING = Misc.ViewID();
    private final int ID1_FILE_NAME = Misc.ViewID();
    private final int ID1_FILE_DETAIL = Misc.ViewID();
    private final int ID1_VIEW_LINE = Misc.ViewID();

    // ViewType 2
    private final int ID2_LEVEL = Misc.ViewID();
    private final int ID2_NUMBER = Misc.ViewID();
    private final int ID2_RATING = Misc.ViewID();
    private final int ID2_JOIN = Misc.ViewID();
    private final int ID2_POPULAR = Misc.ViewID();
    private final int ID2_POINT = Misc.ViewID();
    private final int ID2_MEDAL = Misc.ViewID();
    private final int ID2_CLOSE = Misc.ViewID();
    private final int ID2_MORE = Misc.ViewID();
    private final int ID2_MORE2 = Misc.ViewID();

    public PostAdapter(FragmentActivity activity, String tag)
    {
        Tag = tag;
        Activity = activity;

        PostList.add(new PostStruct(0));
    }

    class ViewHolderMain extends RecyclerView.ViewHolder
    {
        // ViewType 1
        CircleImageView CircleImageViewProfile;
        TextView TextViewName;
        ImageView ImageViewMedal;
        TextView TextViewUsername;
        TextView TextViewTime;
        TextView TextViewMessage;
        ImageView ImageViewOption;
        ImageView ImageViewLike;
        TextView TextViewLikeCount;
        ImageView ImageViewComment;
        TextView TextViewCommentCount;
        CircleImageView CircleImageViewPerson1;
        CircleImageView CircleImageViewPerson2;
        CircleImageView CircleImageViewPerson3;
        CircleImageView CircleImageViewPerson4;
        View ViewLine;
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
        LinearLayout LinearLayoutVoteType1;
        View ViewType1Sel1;
        View ViewType1Sel2;
        View ViewType1Sel3;
        View ViewType1Sel4;
        View ViewType1Sel5;
        TextView TextViewType1Text1;
        TextView TextViewType1Text2;
        TextView TextViewType1Text3;
        TextView TextViewType1Text4;
        TextView TextViewType1Text5;
        TextView TextViewType1Submit;
        TextView TextViewType1Result;
        TextView TextViewType1Time;
        RelativeLayout RelativeLayoutVoteType2;
        TextView TextViewType2Text1;
        TextView TextViewType2Text2;
        TextView TextViewType2Text3;
        TextView TextViewType2Text4;
        TextView TextViewType2Text5;
        TextView TextViewType2Per1;
        TextView TextViewType2Per2;
        TextView TextViewType2Per3;
        TextView TextViewType2Per4;
        TextView TextViewType2Per5;
        View ViewType2Circle;
        TextView TextViewType2Result;
        TextView TextViewType2Time;
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

        ViewHolderMain(View v, int type)
        {
            super(v);

            if (type == 1)
            {
                CircleImageViewProfile = v.findViewById(ID1_PROFILE);
                TextViewName = v.findViewById(ID1_NAME);
                ImageViewMedal = v.findViewById(ID1_MEDAL);
                TextViewUsername = v.findViewById(ID1_USERNAME);
                TextViewTime = v.findViewById(ID1_TIME);
                TextViewMessage = v.findViewById(ID1_MESSAGE);
                ImageViewOption = v.findViewById(ID1_OPTION);
                CircleImageViewPerson1 = v.findViewById(ID1_PERSON1);
                CircleImageViewPerson2 = v.findViewById(ID1_PERSON2);
                CircleImageViewPerson3 = v.findViewById(ID1_PERSON3);
                CircleImageViewPerson4 = v.findViewById(ID1_PERSON4);
                ImageViewLike = v.findViewById(ID1_LIKE);
                TextViewLikeCount = v.findViewById(ID1_LIKE_COUNT);
                ImageViewComment = v.findViewById(ID1_COMMENT);
                TextViewCommentCount = v.findViewById(ID1_COMMENT_COUNT);
                ViewLine = v.findViewById(ID1_VIEW_LINE);
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
                LinearLayoutVoteType1 = v.findViewById(ID1_VOTE_TYPE1);
                ViewType1Sel1 = v.findViewById(ID1_VOTE_TYPE1_SEL1);
                ViewType1Sel2 = v.findViewById(ID1_VOTE_TYPE1_SEL2);
                ViewType1Sel3 = v.findViewById(ID1_VOTE_TYPE1_SEL3);
                ViewType1Sel4 = v.findViewById(ID1_VOTE_TYPE1_SEL4);
                ViewType1Sel5 = v.findViewById(ID1_VOTE_TYPE1_SEL5);
                TextViewType1Text1 = v.findViewById(ID1_VOTE_TYPE1_TEXT1);
                TextViewType1Text2 = v.findViewById(ID1_VOTE_TYPE1_TEXT2);
                TextViewType1Text3 = v.findViewById(ID1_VOTE_TYPE1_TEXT3);
                TextViewType1Text4 = v.findViewById(ID1_VOTE_TYPE1_TEXT4);
                TextViewType1Text5 = v.findViewById(ID1_VOTE_TYPE1_TEXT5);
                TextViewType1Submit = v.findViewById(ID1_VOTE_TYPE1_SUBMIT);
                TextViewType1Result = v.findViewById(ID1_VOTE_TYPE1_RESULT);
                TextViewType1Time = v.findViewById(ID1_VOTE_TYPE1_TIME);
                RelativeLayoutVoteType2 = v.findViewById(ID1_VOTE_TYPE2);
                TextViewType2Text1 = v.findViewById(ID1_VOTE_TYPE2_TEXT1);
                TextViewType2Text2 = v.findViewById(ID1_VOTE_TYPE2_TEXT2);
                TextViewType2Text3 = v.findViewById(ID1_VOTE_TYPE2_TEXT3);
                TextViewType2Text4 = v.findViewById(ID1_VOTE_TYPE2_TEXT4);
                TextViewType2Text5 = v.findViewById(ID1_VOTE_TYPE2_TEXT5);
                TextViewType2Per1 = v.findViewById(ID1_VOTE_TYPE2_PER1);
                TextViewType2Per2 = v.findViewById(ID1_VOTE_TYPE2_PER2);
                TextViewType2Per3 = v.findViewById(ID1_VOTE_TYPE2_PER3);
                TextViewType2Per4 = v.findViewById(ID1_VOTE_TYPE2_PER4);
                TextViewType2Per5 = v.findViewById(ID1_VOTE_TYPE2_PER5);
                ViewType2Circle = v.findViewById(ID1_VOTE_TYPE2_CIRCLE);
                TextViewType2Result = v.findViewById(ID1_VOTE_TYPE2_RESULT);
                TextViewType2Time = v.findViewById(ID1_VOTE_TYPE2_TIME);
                RelativeLayoutFile = v.findViewById(ID1_FILE_LAYOUT);
                ImageViewFile = v.findViewById(ID1_FILE_DOWNLOAD);
                LoadingViewFile = v.findViewById(ID1_FILE_LOADING);
                TextViewFileName = v.findViewById(ID1_FILE_NAME);
                TextViewFileDetail = v.findViewById(ID1_FILE_DETAIL);
            }
            else if (type == 2)
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

    @Override
    public PostAdapter.ViewHolderMain onCreateViewHolder(ViewGroup vg, int type)
    {
        if (type == 0)
        {
            RefreshView = new PullToRefreshView(Activity);
            RefreshView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 0));
            RefreshView.SetOnRefreshListener(new PullToRefreshView.OnRefreshListener()
            {
                @Override
                public void OnRefresh()
                {
                    int Time = 0 ;

                    if (PostList.size() > 1)
                        Time = PostList.get(1).Time;

                    AndroidNetworking.post(Misc.GetRandomServer("PostListInbox"))
                    .addBodyParameter("Time", String.valueOf(Time))
                    .addHeaders("Token", SharedHandler.GetString(Activity, "Token"))
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
                                    JSONArray ResultList = new JSONArray(Result.getString("Result"));

                                    for (int K = 0; K < ResultList.length(); K++)
                                    {
                                        JSONObject D = ResultList.getJSONObject(K);

                                        PostStruct P = new PostStruct();
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
                                            P.DataType = D.getInt("Type");
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

                                        PostList.add(P);
                                    }

                                    Collections.sort(PostList, new PostSort());

                                    notifyDataSetChanged();
                                }
                            }
                            catch (Exception e)
                            {
                                Misc.Debug("PostAdapter-Refresh: " + e.toString());
                            }

                            RefreshView.SetRefreshComplete();
                        }

                        @Override
                        public void onError(ANError e)
                        {
                            Misc.Debug("PostAdapter-Refresh: " + e.toString());
                            RefreshView.SetRefreshComplete();
                        }
                    });
                }
            });

            return new ViewHolderMain(RefreshView, type);
        }
        else if (type == 1)
        {
            RelativeLayout RelativeLayoutMain = new RelativeLayout(Activity);
            RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

            RelativeLayout.LayoutParams CircleImageViewProfileParam = new RelativeLayout.LayoutParams(Misc.ToDP(50), Misc.ToDP(50));
            CircleImageViewProfileParam.setMargins(Misc.ToDP(8), Misc.ToDP(8), Misc.ToDP(8), Misc.ToDP(8));
            CircleImageViewProfileParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

            CircleImageView CircleImageViewProfile = new CircleImageView(Activity);
            CircleImageViewProfile.setLayoutParams(CircleImageViewProfileParam);
            CircleImageViewProfile.setId(ID1_PROFILE);
            CircleImageViewProfile.SetBorderColor(R.color.Gray2);
            CircleImageViewProfile.SetBorderWidth(1);

            RelativeLayoutMain.addView(CircleImageViewProfile);

            RelativeLayout.LayoutParams TextViewNameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewNameParam.addRule(RelativeLayout.RIGHT_OF, ID1_PROFILE);
            TextViewNameParam.setMargins(0, Misc.ToDP(12), 0, 0);

            TextView TextViewName = new TextView(Activity, 14, true);
            TextViewName.setLayoutParams(TextViewNameParam);
            TextViewName.setTextColor(Misc.Color(R.color.TextWhite));
            TextViewName.setId(ID1_NAME);

            RelativeLayoutMain.addView(TextViewName);

            RelativeLayout.LayoutParams ImageViewMedalParam = new RelativeLayout.LayoutParams(Misc.ToDP(16), Misc.ToDP(16));
            ImageViewMedalParam.addRule(RelativeLayout.RIGHT_OF, ID1_NAME);
            ImageViewMedalParam.setMargins(Misc.ToDP(3), Misc.ToDP(16), 0, 0);

            ImageView ImageViewMedal = new ImageView(Activity);
            ImageViewMedal.setLayoutParams(ImageViewMedalParam);
            ImageViewMedal.setId(ID1_MEDAL);

            RelativeLayoutMain.addView(ImageViewMedal);

            RelativeLayout.LayoutParams TextViewUsernameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewUsernameParam.addRule(RelativeLayout.RIGHT_OF, ID1_PROFILE);
            TextViewUsernameParam.setMargins(0, Misc.ToDP(32), 0, 0);

            TextView TextViewUsername = new TextView(Activity, 14, false);
            TextViewUsername.setLayoutParams(TextViewUsernameParam);
            TextViewUsername.setTextColor(Misc.Color(R.color.Gray4));
            TextViewUsername.setId(ID1_USERNAME);

            RelativeLayoutMain.addView(TextViewUsername);

            RelativeLayout.LayoutParams TextViewTimeParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewTimeParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            TextViewTimeParam.setMargins(0, Misc.ToDP(14), Misc.ToDP(10), 0);

            TextView TextViewTime = new TextView(Activity, 12, false);
            TextViewTime.setLayoutParams(TextViewTimeParam);
            TextViewTime.setTextColor(Misc.Color(R.color.Gray2));
            TextViewTime.setId(ID1_TIME);

            RelativeLayoutMain.addView(TextViewTime);

            RelativeLayout.LayoutParams TextViewMessageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewMessageParam.addRule(RelativeLayout.RIGHT_OF, ID1_PROFILE);
            TextViewMessageParam.addRule(RelativeLayout.BELOW, ID1_USERNAME);
            TextViewMessageParam.setMargins(0, 0, Misc.ToDP(8), 0);

            TextView TextViewMessage = new TextView(Activity, 14, false);
            TextViewMessage.setLayoutParams(TextViewMessageParam);
            TextViewMessage.setTextColor(Misc.Color(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite));
            TextViewMessage.setId(ID1_MESSAGE);

            RelativeLayoutMain.addView(TextViewMessage);

            RelativeLayout.LayoutParams RelativeLayoutContentParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            RelativeLayoutContentParam.addRule(RelativeLayout.RIGHT_OF, ID1_PROFILE);
            RelativeLayoutContentParam.addRule(RelativeLayout.BELOW, ID1_MESSAGE);
            RelativeLayoutContentParam.setMargins(0, 0, Misc.ToDP(5), 0);

            RelativeLayout RelativeLayoutContent = new RelativeLayout(Activity);
            RelativeLayoutContent.setLayoutParams(RelativeLayoutContentParam);
            RelativeLayoutContent.setId(Misc.ViewID());

            RelativeLayoutMain.addView(RelativeLayoutContent);

            RelativeLayout RelativeLayoutImage = new RelativeLayout(Activity);
            RelativeLayoutImage.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(150)));
            RelativeLayoutImage.setVisibility(View.GONE);
            RelativeLayoutImage.setId(ID1_IMAGE_LAYOUT);

            RelativeLayoutContent.addView(RelativeLayoutImage);

            ImageView ImageViewSingle = new ImageView(Activity);
            ImageViewSingle.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            ImageViewSingle.setVisibility(View.GONE);
            ImageViewSingle.setId(ID1_SINGLE);

            RelativeLayoutImage.addView(ImageViewSingle);

            LinearLayout LinearLayoutDouble = new LinearLayout(Activity);
            LinearLayoutDouble.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            LinearLayoutDouble.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayoutDouble.setVisibility(View.GONE);
            LinearLayoutDouble.setId(ID1_DOUBLE_LAYOUT);

            RelativeLayoutImage.addView(LinearLayoutDouble);

            ImageView ImageViewDouble1 = new ImageView(Activity);
            ImageViewDouble1.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1));
            ImageViewDouble1.setId(ID1_DOUBLE1);

            LinearLayoutDouble.addView(ImageViewDouble1);

            View ViewLineDouble = new View(Activity);
            ViewLineDouble.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 0.03f));

            LinearLayoutDouble.addView(ViewLineDouble);

            ImageView ImageViewDouble2 = new ImageView(Activity);
            ImageViewDouble2.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1));
            ImageViewDouble2.setId(ID1_DOUBLE2);

            LinearLayoutDouble.addView(ImageViewDouble2);

            LinearLayout LinearLayoutTriple = new LinearLayout(Activity);
            LinearLayoutTriple.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            LinearLayoutTriple.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayoutTriple.setVisibility(View.GONE);
            LinearLayoutTriple.setId(ID1_TRIPLE_LAYOUT);

            RelativeLayoutImage.addView(LinearLayoutTriple);

            ImageView ImageViewTriple1 = new ImageView(Activity);
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

            ImageView ImageViewTriple2 = new ImageView(Activity);
            ImageViewTriple2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1));
            ImageViewTriple2.setId(ID1_TRIPLE2);

            LinearLayoutDouble2.addView(ImageViewTriple2);

            View ViewLineTriple2 = new View(Activity);
            ViewLineTriple2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.04f));

            LinearLayoutDouble2.addView(ViewLineTriple2);

            ImageView ImageViewTriple3 = new ImageView(Activity);
            ImageViewTriple3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1));
            ImageViewTriple3.setId(ID1_TRIPLE3);

            LinearLayoutDouble2.addView(ImageViewTriple3);

            RelativeLayout RelativeLayoutVideo = new RelativeLayout(Activity);
            RelativeLayoutVideo.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(150)));
            RelativeLayoutVideo.setVisibility(View.GONE);
            RelativeLayoutVideo.setId(ID1_VIDEO_LAYOUT);

            RelativeLayoutContent.addView(RelativeLayoutVideo);

            ImageView ImageViewVideo = new ImageView(Activity);
            ImageViewVideo.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            ImageViewVideo.setId(ID1_VIDEO_IMAGE);

            RelativeLayoutVideo.addView(ImageViewVideo);

            GradientDrawable DrawableVideo = new GradientDrawable();
            DrawableVideo.setColor(Color.parseColor("#65000000"));
            DrawableVideo.setCornerRadius(Misc.ToDP(4));

            RelativeLayout.LayoutParams TextViewDuritonParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewDuritonParam.setMargins(Misc.ToDP(8), 0, Misc.ToDP(8), Misc.ToDP(8));
            TextViewDuritonParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            TextViewDuritonParam.addRule(Misc.Align("L"));

            TextView TextViewDurotion = new TextView(Activity, 12, false);
            TextViewDurotion.setLayoutParams(TextViewDuritonParam);
            TextViewDurotion.setPadding(Misc.ToDP(5), Misc.ToDP(3), Misc.ToDP(5), 0);
            TextViewDurotion.setBackground(DrawableVideo);
            TextViewDurotion.setId(ID1_VIDEO_DUROTION);

            RelativeLayoutVideo.addView(TextViewDurotion);

            RelativeLayout.LayoutParams TextViewVideoParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewVideoParam.setMargins(Misc.ToDP(8), 0, Misc.ToDP(8), Misc.ToDP(8));
            TextViewVideoParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            TextViewVideoParam.addRule(Misc.Align("R"));

            TextView TextViewVideo = new TextView(Activity, 12, false);
            TextViewVideo.setLayoutParams(TextViewVideoParam);
            TextViewVideo.setPadding(Misc.ToDP(5), Misc.ToDP(3), Misc.ToDP(5), 0);
            TextViewVideo.setText(Activity.getString(R.string.PostAdapterVideo));
            TextViewVideo.setBackground(DrawableVideo);

            RelativeLayoutVideo.addView(TextViewVideo);

            GradientDrawable DrawableVote = new GradientDrawable();
            DrawableVote.setStroke(Misc.ToDP(1), Misc.Color(R.color.BlueGray));
            DrawableVote.setCornerRadius(Misc.ToDP(6));

            RelativeLayout RelativeLayoutVote = new RelativeLayout(Activity);
            RelativeLayoutVote.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            RelativeLayoutVote.setPadding(Misc.ToDP(8), Misc.ToDP(8), Misc.ToDP(15), Misc.ToDP(8));
            RelativeLayoutVote.setBackground(DrawableVote);
            RelativeLayoutVote.setVisibility(View.GONE);
            RelativeLayoutVote.setId(ID1_VOTE_LAYOUT);

            RelativeLayoutContent.addView(RelativeLayoutVote);

            {
                LinearLayout LinearLayoutVoteType1 = new LinearLayout(Activity);
                LinearLayoutVoteType1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                LinearLayoutVoteType1.setOrientation(LinearLayout.VERTICAL);
                LinearLayoutVoteType1.setVisibility(View.GONE);
                LinearLayoutVoteType1.setId(ID1_VOTE_TYPE1);

                RelativeLayoutVote.addView(LinearLayoutVoteType1);

                GradientDrawable DrawableSelect = new GradientDrawable();
                DrawableSelect.setStroke(Misc.ToDP(1), Misc.Color(R.color.BlueGray));
                DrawableSelect.setShape(GradientDrawable.OVAL);

                LinearLayout LinearLayoutVote1 = new LinearLayout(Activity);
                LinearLayoutVote1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(25)));
                LinearLayoutVote1.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayoutVote1.setGravity(Gravity.CENTER_VERTICAL);

                LinearLayoutVoteType1.addView(LinearLayoutVote1);

                LinearLayout LinearLayoutVote2 = new LinearLayout(Activity);
                LinearLayoutVote2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(25)));
                LinearLayoutVote2.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayoutVote2.setGravity(Gravity.CENTER_VERTICAL);

                LinearLayoutVoteType1.addView(LinearLayoutVote2);

                LinearLayout LinearLayoutVote3 = new LinearLayout(Activity);
                LinearLayoutVote3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(25)));
                LinearLayoutVote3.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayoutVote3.setGravity(Gravity.CENTER_VERTICAL);

                LinearLayoutVoteType1.addView(LinearLayoutVote3);

                LinearLayout LinearLayoutVote4 = new LinearLayout(Activity);
                LinearLayoutVote4.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(25)));
                LinearLayoutVote4.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayoutVote4.setGravity(Gravity.CENTER_VERTICAL);

                LinearLayoutVoteType1.addView(LinearLayoutVote4);

                LinearLayout LinearLayoutVote5 = new LinearLayout(Activity);
                LinearLayoutVote5.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(25)));
                LinearLayoutVote5.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayoutVote5.setGravity(Gravity.CENTER_VERTICAL);

                LinearLayoutVoteType1.addView(LinearLayoutVote5);

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
                ViewVote3.setVisibility(View.GONE);

                LinearLayoutVote3.addView(ViewVote3);

                View ViewVote4 = new View(Activity);
                ViewVote4.setLayoutParams(new LinearLayout.LayoutParams(Misc.ToDP(16), Misc.ToDP(16)));
                ViewVote4.setBackground(DrawableSelect);
                ViewVote4.setId(ID1_VOTE_TYPE1_SEL4);
                ViewVote4.setVisibility(View.GONE);

                LinearLayoutVote4.addView(ViewVote4);

                View ViewVote5 = new View(Activity);
                ViewVote5.setLayoutParams(new LinearLayout.LayoutParams(Misc.ToDP(16), Misc.ToDP(16)));
                ViewVote5.setBackground(DrawableSelect);
                ViewVote5.setId(ID1_VOTE_TYPE1_SEL5);
                ViewVote5.setVisibility(View.GONE);

                LinearLayoutVote5.addView(ViewVote5);

                TextView TextViewVote1 = new TextView(Activity, 14, false);
                TextViewVote1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(20)));
                TextViewVote1.setTextColor(Misc.Color(R.color.TextWhite));
                TextViewVote1.setPadding(Misc.ToDP(5), 0, 0, 0);
                TextViewVote1.setGravity(Gravity.LEFT);
                TextViewVote1.setId(ID1_VOTE_TYPE1_TEXT1);

                LinearLayoutVote1.addView(TextViewVote1);

                TextView TextViewVote2 = new TextView(Activity, 14, false);
                TextViewVote2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(20)));
                TextViewVote2.setTextColor(Misc.Color(R.color.TextWhite));
                TextViewVote2.setPadding(Misc.ToDP(5), 0, 0, 0);
                TextViewVote2.setGravity(Gravity.LEFT);
                TextViewVote2.setId(ID1_VOTE_TYPE1_TEXT2);

                LinearLayoutVote2.addView(TextViewVote2);

                TextView TextViewVote3 = new TextView(Activity, 14, false);
                TextViewVote3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(20)));
                TextViewVote3.setTextColor(Misc.Color(R.color.TextWhite));
                TextViewVote3.setPadding(Misc.ToDP(5), 0, 0, 0);
                TextViewVote3.setId(ID1_VOTE_TYPE1_TEXT3);
                TextViewVote3.setGravity(Gravity.LEFT);
                TextViewVote3.setVisibility(View.GONE);

                LinearLayoutVote3.addView(TextViewVote3);

                TextView TextViewVote4 = new TextView(Activity, 14, false);
                TextViewVote4.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(20)));
                TextViewVote4.setTextColor(Misc.Color(R.color.TextWhite));
                TextViewVote4.setPadding(Misc.ToDP(5), 0, 0, 0);
                TextViewVote4.setId(ID1_VOTE_TYPE1_TEXT4);
                TextViewVote4.setGravity(Gravity.LEFT);
                TextViewVote4.setVisibility(View.GONE);

                LinearLayoutVote4.addView(TextViewVote4);

                TextView TextViewVote5 = new TextView(Activity, 14, false);
                TextViewVote5.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(20)));
                TextViewVote5.setTextColor(Misc.Color(R.color.TextWhite));
                TextViewVote5.setPadding(Misc.ToDP(5), 0, 0, 0);
                TextViewVote5.setId(ID1_VOTE_TYPE1_TEXT5);
                TextViewVote5.setGravity(Gravity.LEFT);
                TextViewVote5.setVisibility(View.GONE);

                LinearLayoutVote5.addView(TextViewVote5);

                View ViewVoteLine1 = new View(Activity);
                ViewVoteLine1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(8)));
                LinearLayoutVoteType1.addView(ViewVoteLine1);

                View ViewVoteLine2 = new View(Activity);
                ViewVoteLine2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1)));
                ViewVoteLine2.setBackgroundResource(R.color.BlueGray);

                LinearLayoutVoteType1.addView(ViewVoteLine2);

                View ViewVoteLine3 = new View(Activity);
                ViewVoteLine3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(8)));

                LinearLayoutVoteType1.addView(ViewVoteLine3);

                LinearLayout LinearLayoutVoteSubmit = new LinearLayout(Activity);
                LinearLayoutVoteSubmit.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(30)));
                LinearLayoutVoteSubmit.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayoutVoteSubmit.setGravity(Gravity.CENTER_VERTICAL);

                LinearLayoutVoteType1.addView(LinearLayoutVoteSubmit);

                GradientDrawable DrawableVote2 = new GradientDrawable();
                DrawableVote2.setCornerRadius(Misc.ToDP(4));
                DrawableVote2.setColor(Misc.Color(R.color.BlueLight));
                DrawableVote2.setStroke(Misc.ToDP(1), Misc.Color(R.color.BlueLight));

                TextView TextViewVote = new TextView(Activity, 12, false);
                TextViewVote.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, Misc.ToDP(30)));
                TextViewVote.setBackground(DrawableVote2);
                TextViewVote.setPadding(Misc.ToDP(10), Misc.ToDP(5), Misc.ToDP(10), Misc.ToDP(5));
                TextViewVote.setGravity(Gravity.CENTER_VERTICAL);
                TextViewVote.setText(Activity.getString(R.string.PostAdapterVote));
                TextViewVote.setId(ID1_VOTE_TYPE1_SUBMIT);

                LinearLayoutVoteSubmit.addView(TextViewVote);

                TextView TextViewSubmit = new TextView(Activity, 12, false);
                TextViewSubmit.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TextViewSubmit.setTextColor(Misc.Color(R.color.BlueGray2));
                TextViewSubmit.setPadding(Misc.ToDP(10), Misc.ToDP(5), 0, 0);
                TextViewSubmit.setId(ID1_VOTE_TYPE1_RESULT);

                LinearLayoutVoteSubmit.addView(TextViewSubmit);

                TextView TextViewVoteTime = new TextView(Activity, 12, false);
                TextViewVoteTime.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TextViewVoteTime.setTextColor(Misc.Color(R.color.BlueGray2));
                TextViewVoteTime.setPadding(Misc.ToDP(10), Misc.ToDP(5), 0, 0);
                TextViewVoteTime.setId(ID1_VOTE_TYPE1_TIME);

                LinearLayoutVoteSubmit.addView(TextViewVoteTime);
            }

            RelativeLayout RelativeLayoutVoteType2 = new RelativeLayout(Activity);
            RelativeLayoutVoteType2.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            RelativeLayoutVoteType2.setVisibility(View.GONE);
            RelativeLayoutVoteType2.setId(ID1_VOTE_TYPE2);

            RelativeLayoutVote.addView(RelativeLayoutVoteType2);

            RelativeLayout.LayoutParams TextViewPercent1Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, Misc.ToDP(30));
            TextViewPercent1Param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

            TextView TextViewPercent1 = new TextView(Activity, 14, false);
            TextViewPercent1.setLayoutParams(TextViewPercent1Param);
            TextViewPercent1.setTextColor(Misc.Color(R.color.TextWhite));
            TextViewPercent1.setPadding(Misc.ToDP(5), Misc.ToDP(5), 0, 0);
            TextViewPercent1.setGravity(Gravity.CENTER);
            TextViewPercent1.setId(ID1_VOTE_TYPE2_PER1);

            RelativeLayoutVoteType2.addView(TextViewPercent1);

            RelativeLayout.LayoutParams TextViewPercent2Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, Misc.ToDP(30));
            TextViewPercent2Param.addRule(RelativeLayout.BELOW, ID1_VOTE_TYPE2_PER1);
            TextViewPercent2Param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

            TextView TextViewPercent2 = new TextView(Activity, 14, false);
            TextViewPercent2.setLayoutParams(TextViewPercent2Param);
            TextViewPercent2.setTextColor(Misc.Color(R.color.TextWhite));
            TextViewPercent2.setPadding(Misc.ToDP(5), Misc.ToDP(5), 0, 0);
            TextViewPercent2.setGravity(Gravity.CENTER);
            TextViewPercent2.setId(ID1_VOTE_TYPE2_PER2);

            RelativeLayoutVoteType2.addView(TextViewPercent2);

            RelativeLayout.LayoutParams TextViewPercent3Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, Misc.ToDP(30));
            TextViewPercent3Param.addRule(RelativeLayout.BELOW, ID1_VOTE_TYPE2_PER2);
            TextViewPercent3Param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

            TextView TextViewPercent3 = new TextView(Activity, 14, false);
            TextViewPercent3.setLayoutParams(TextViewPercent3Param);
            TextViewPercent3.setTextColor(Misc.Color(R.color.TextWhite));
            TextViewPercent3.setPadding(Misc.ToDP(5), Misc.ToDP(5), 0, 0);
            TextViewPercent3.setGravity(Gravity.CENTER);
            TextViewPercent3.setVisibility(View.GONE);
            TextViewPercent3.setId(ID1_VOTE_TYPE2_PER3);

            RelativeLayoutVoteType2.addView(TextViewPercent3);

            RelativeLayout.LayoutParams TextViewPercent4Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, Misc.ToDP(30));
            TextViewPercent4Param.addRule(RelativeLayout.BELOW, ID1_VOTE_TYPE2_PER3);
            TextViewPercent4Param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

            TextView TextViewPercent4 = new TextView(Activity, 14, false);
            TextViewPercent4.setLayoutParams(TextViewPercent4Param);
            TextViewPercent4.setTextColor(Misc.Color(R.color.TextWhite));
            TextViewPercent4.setPadding(Misc.ToDP(5), Misc.ToDP(5), 0, 0);
            TextViewPercent4.setGravity(Gravity.CENTER);
            TextViewPercent4.setVisibility(View.GONE);
            TextViewPercent4.setId(ID1_VOTE_TYPE2_PER4);

            RelativeLayoutVoteType2.addView(TextViewPercent4);

            RelativeLayout.LayoutParams TextViewPercent5Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, Misc.ToDP(30));
            TextViewPercent5Param.addRule(RelativeLayout.BELOW, ID1_VOTE_TYPE2_PER4);
            TextViewPercent5Param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

            TextView TextViewPercent5 = new TextView(Activity, 14, false);
            TextViewPercent5.setLayoutParams(TextViewPercent5Param);
            TextViewPercent5.setTextColor(Misc.Color(R.color.TextWhite));
            TextViewPercent5.setPadding(Misc.ToDP(5), Misc.ToDP(5), 0, 0);
            TextViewPercent5.setGravity(Gravity.CENTER);
            TextViewPercent5.setVisibility(View.GONE);
            TextViewPercent5.setId(ID1_VOTE_TYPE2_PER5);

            RelativeLayoutVoteType2.addView(TextViewPercent5);

            RelativeLayout.LayoutParams TextViewVote1Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(30));
            TextViewVote1Param.addRule(RelativeLayout.LEFT_OF, ID1_VOTE_TYPE2_PER1);

            TextView TextViewVote1 = new TextView(Activity, 14, false);
            TextViewVote1.setLayoutParams(TextViewVote1Param);
            TextViewVote1.setTextColor(Misc.Color(R.color.TextWhite));
            TextViewVote1.setPadding(Misc.ToDP(20), Misc.ToDP(3), 0, 0);
            TextViewVote1.setGravity(Gravity.LEFT);
            TextViewVote1.setId(ID1_VOTE_TYPE2_TEXT1);

            RelativeLayoutVoteType2.addView(TextViewVote1);

            RelativeLayout.LayoutParams TextViewVote2Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(30));
            TextViewVote2Param.addRule(RelativeLayout.LEFT_OF, ID1_VOTE_TYPE2_PER2);
            TextViewVote2Param.addRule(RelativeLayout.BELOW, ID1_VOTE_TYPE2_TEXT1);

            TextView TextViewVote2 = new TextView(Activity, 14, false);
            TextViewVote2.setLayoutParams(TextViewVote2Param);
            TextViewVote2.setTextColor(Misc.Color(R.color.TextWhite));
            TextViewVote2.setPadding(Misc.ToDP(20), Misc.ToDP(3), 0, 0);
            TextViewVote2.setGravity(Gravity.LEFT);
            TextViewVote2.setId(ID1_VOTE_TYPE2_TEXT2);

            RelativeLayoutVoteType2.addView(TextViewVote2);

            RelativeLayout.LayoutParams TextViewVote3Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(30));
            TextViewVote3Param.addRule(RelativeLayout.LEFT_OF, ID1_VOTE_TYPE2_PER3);
            TextViewVote3Param.addRule(RelativeLayout.BELOW, ID1_VOTE_TYPE2_TEXT2);

            TextView TextViewVote3 = new TextView(Activity, 14, false);
            TextViewVote3.setLayoutParams(TextViewVote3Param);
            TextViewVote3.setTextColor(Misc.Color(R.color.TextWhite));
            TextViewVote3.setPadding(Misc.ToDP(20), Misc.ToDP(3), 0, 0);
            TextViewVote3.setGravity(Gravity.LEFT);
            TextViewVote3.setVisibility(View.GONE);
            TextViewVote3.setId(ID1_VOTE_TYPE2_TEXT3);

            RelativeLayoutVoteType2.addView(TextViewVote3);

            RelativeLayout.LayoutParams TextViewVote4Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(30));
            TextViewVote4Param.addRule(RelativeLayout.LEFT_OF, ID1_VOTE_TYPE2_PER4);
            TextViewVote4Param.addRule(RelativeLayout.BELOW, ID1_VOTE_TYPE2_TEXT3);

            TextView TextViewVote4 = new TextView(Activity, 14, false);
            TextViewVote4.setLayoutParams(TextViewVote4Param);
            TextViewVote4.setTextColor(Misc.Color(R.color.TextWhite));
            TextViewVote4.setPadding(Misc.ToDP(20), Misc.ToDP(3), 0, 0);
            TextViewVote4.setGravity(Gravity.LEFT);
            TextViewVote4.setVisibility(View.GONE);
            TextViewVote4.setId(ID1_VOTE_TYPE2_TEXT4);

            RelativeLayoutVoteType2.addView(TextViewVote4);

            RelativeLayout.LayoutParams TextViewVote5Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(30));
            TextViewVote5Param.addRule(RelativeLayout.LEFT_OF, ID1_VOTE_TYPE2_PER5);
            TextViewVote5Param.addRule(RelativeLayout.BELOW, ID1_VOTE_TYPE2_TEXT4);

            TextView TextViewVote5 = new TextView(Activity, 14, false);
            TextViewVote5.setLayoutParams(TextViewVote5Param);
            TextViewVote5.setTextColor(Misc.Color(R.color.TextWhite));
            TextViewVote5.setPadding(Misc.ToDP(20), Misc.ToDP(3), 0, 0);
            TextViewVote5.setGravity(Gravity.LEFT);
            TextViewVote5.setVisibility(View.GONE);
            TextViewVote5.setId(ID1_VOTE_TYPE2_TEXT5);

            RelativeLayoutVoteType2.addView(TextViewVote5);

            GradientDrawable DrawableVote3 = new GradientDrawable();
            DrawableVote3.setColor(Misc.Color(R.color.BlueGray2));
            DrawableVote3.setCornerRadius(Misc.ToDP(100));

            View ViewVoteCircle = new View(Activity);
            ViewVoteCircle.setLayoutParams(new RelativeLayout.LayoutParams(Misc.ToDP(6), Misc.ToDP(6)));
            ViewVoteCircle.setBackground(DrawableVote3);
            ViewVoteCircle.setId(ID1_VOTE_TYPE2_CIRCLE);

            RelativeLayoutVoteType2.addView(ViewVoteCircle);

            RelativeLayout.LayoutParams ViewVoteParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1));
            ViewVoteParam.addRule(RelativeLayout.BELOW, ID1_VOTE_TYPE2_TEXT5);
            ViewVoteParam.setMargins(0, Misc.ToDP(5), 0, Misc.ToDP(5));

            View ViewVote = new View(Activity);
            ViewVote.setLayoutParams(ViewVoteParam);
            ViewVote.setBackgroundResource(R.color.BlueGray);
            ViewVote.setId(Misc.ViewID());

            RelativeLayoutVoteType2.addView(ViewVote);

            RelativeLayout.LayoutParams LinearLayoutVoteSubmitParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(30));
            LinearLayoutVoteSubmitParam.addRule(RelativeLayout.BELOW, ViewVote.getId());

            LinearLayout LinearLayoutVoteSubmit = new LinearLayout(Activity);
            LinearLayoutVoteSubmit.setLayoutParams(LinearLayoutVoteSubmitParam);
            LinearLayoutVoteSubmit.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayoutVoteSubmit.setGravity(Gravity.CENTER_VERTICAL);

            RelativeLayoutVoteType2.addView(LinearLayoutVoteSubmit);

            TextView TextViewSubmit = new TextView(Activity, 12, false);
            TextViewSubmit.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            TextViewSubmit.setTextColor(Misc.Color(R.color.BlueGray2));
            TextViewSubmit.setPadding(Misc.ToDP(10), Misc.ToDP(5), 0, 0);
            TextViewSubmit.setId(ID1_VOTE_TYPE2_RESULT);

            LinearLayoutVoteSubmit.addView(TextViewSubmit);

            TextView TextViewVoteTime = new TextView(Activity, 12, false);
            TextViewVoteTime.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            TextViewVoteTime.setTextColor(Misc.Color(R.color.BlueGray2));
            TextViewVoteTime.setPadding(Misc.ToDP(10), Misc.ToDP(5), 0, 0);
            TextViewVoteTime.setId(ID1_VOTE_TYPE2_TIME);

            LinearLayoutVoteSubmit.addView(TextViewVoteTime);

            RelativeLayout RelativeLayoutFile = new RelativeLayout(Activity);
            RelativeLayoutFile.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(70)));
            RelativeLayoutFile.setPadding(0, Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10));
            RelativeLayoutFile.setVisibility(View.GONE);
            RelativeLayoutFile.setId(ID1_FILE_LAYOUT);

            RelativeLayoutContent.addView(RelativeLayoutFile);

            RelativeLayout.LayoutParams ImageViewFileParam = new RelativeLayout.LayoutParams(Misc.ToDP(50), Misc.ToDP(50));
            ImageViewFileParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

            GradientDrawable DrawableFile = new GradientDrawable();
            DrawableFile.setCornerRadius(Misc.ToDP(4));
            DrawableFile.setColor(Misc.Color(R.color.BlueLight));

            ImageView ImageViewFile = new ImageView(Activity);
            ImageViewFile.setLayoutParams(ImageViewFileParam);
            ImageViewFile.setPadding(Misc.ToDP(12), Misc.ToDP(12), Misc.ToDP(12), Misc.ToDP(12));
            ImageViewFile.setImageResource(R.drawable.download_white);
            ImageViewFile.setId(Misc.ViewID());
            ImageViewFile.setBackground(DrawableFile);
            ImageViewFile.setId(ID1_FILE_DOWNLOAD);

            RelativeLayoutFile.addView(ImageViewFile);

            RelativeLayout.LayoutParams LoadingViewParam = new RelativeLayout.LayoutParams(Misc.ToDP(50), Misc.ToDP(50));
            LoadingViewParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

            LoadingView LoadingViewFile = new LoadingView(Activity);
            LoadingViewFile.setLayoutParams(LoadingViewParam);
            LoadingViewFile.setBackground(DrawableFile);
            LoadingViewFile.SetColor(R.color.TextDark);
            LoadingViewFile.SetScale(1.90f);
            LoadingViewFile.SetSize(5);
            LoadingViewFile.setVisibility(View.VISIBLE);
            LoadingViewFile.setId(ID1_FILE_LOADING);
            LoadingViewFile.Start();

            RelativeLayoutFile.addView(LoadingViewFile);

            RelativeLayout.LayoutParams TextViewFileNameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewFileNameParam.addRule(RelativeLayout.RIGHT_OF, ID1_FILE_DOWNLOAD);
            TextViewFileNameParam.setMargins(Misc.ToDP(8), Misc.ToDP(2), 0, 0);

            TextView TextViewFileName = new TextView(Activity, 14, false);
            TextViewFileName.setLayoutParams(TextViewFileNameParam);
            TextViewFileName.setTextColor(Misc.Color(R.color.TextWhite));
            TextViewFileName.setId(ID1_FILE_NAME);

            RelativeLayoutFile.addView(TextViewFileName);

            RelativeLayout.LayoutParams TextViewFileDetailParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewFileDetailParam.addRule(RelativeLayout.RIGHT_OF, ID1_FILE_DOWNLOAD);
            TextViewFileDetailParam.addRule(RelativeLayout.BELOW, ID1_FILE_NAME);
            TextViewFileDetailParam.setMargins(Misc.ToDP(8),0, 0, 0);

            TextView TextViewFileDetail = new TextView(Activity, 12, false);
            TextViewFileDetail.setLayoutParams(TextViewFileDetailParam);
            TextViewFileDetail.setTextColor(Misc.Color(R.color.BlueGray2));
            TextViewFileDetail.setId(ID1_FILE_DETAIL);

            RelativeLayoutFile.addView(TextViewFileDetail);

            RelativeLayout.LayoutParams RelativeLayoutToolParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(40));
            RelativeLayoutToolParam.addRule(RelativeLayout.BELOW, RelativeLayoutContent.getId());
            RelativeLayoutToolParam.addRule(RelativeLayout.RIGHT_OF, ID1_PROFILE);

            RelativeLayout RelativeLayoutTool = new RelativeLayout(Activity);
            RelativeLayoutTool.setLayoutParams(RelativeLayoutToolParam);

            RelativeLayoutMain.addView(RelativeLayoutTool);

            RelativeLayout.LayoutParams ImageViewOptionParam = new RelativeLayout.LayoutParams(Misc.ToDP(32), RelativeLayout.LayoutParams.MATCH_PARENT);
            ImageViewOptionParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

            ImageView ImageViewOption = new ImageView(Activity);
            ImageViewOption.setLayoutParams(ImageViewOptionParam);
            ImageViewOption.setPadding(Misc.ToDP(4), Misc.ToDP(4), Misc.ToDP(4), Misc.ToDP(4));
            ImageViewOption.setImageResource(R.drawable._inbox_option);
            ImageViewOption.setId(ID1_OPTION);

            RelativeLayoutTool.addView(ImageViewOption);

            RelativeLayout.LayoutParams TextViewLikeParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            TextViewLikeParam.addRule(RelativeLayout.LEFT_OF, ID1_OPTION);

            TextView TextViewLike = new TextView(Activity, 12, false);
            TextViewLike.setLayoutParams(TextViewLikeParam);
            TextViewLike.setTextColor(Misc.Color(R.color.BlueGray2));
            TextViewLike.setPadding(Misc.ToDP(10), Misc.ToDP(4), Misc.ToDP(10), 0);
            TextViewLike.setGravity(Gravity.CENTER_VERTICAL);
            TextViewLike.setId(ID1_LIKE_COUNT);

            RelativeLayoutTool.addView(TextViewLike);

            RelativeLayout.LayoutParams ImageViewLikeParam = new RelativeLayout.LayoutParams(Misc.ToDP(32), RelativeLayout.LayoutParams.MATCH_PARENT);
            ImageViewLikeParam.addRule(RelativeLayout.LEFT_OF, ID1_LIKE_COUNT);

            ImageView ImageViewLike = new ImageView(Activity);
            ImageViewLike.setLayoutParams(ImageViewLikeParam);
            ImageViewLike.setPadding(Misc.ToDP(3), Misc.ToDP(3), Misc.ToDP(3), Misc.ToDP(3));
            ImageViewLike.setImageResource(R.drawable._inbox_like);
            ImageViewLike.setId(ID1_LIKE);

            RelativeLayoutTool.addView(ImageViewLike);

            RelativeLayout.LayoutParams TextViewCommentParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            TextViewCommentParam.addRule(RelativeLayout.LEFT_OF, ID1_LIKE);

            TextView TextViewComment = new TextView(Activity, 12, false);
            TextViewComment.setLayoutParams(TextViewCommentParam);
            TextViewComment.setTextColor(Misc.Color(R.color.BlueGray2));
            TextViewComment.setPadding(Misc.ToDP(10), Misc.ToDP(5), Misc.ToDP(10), 0);
            TextViewComment.setGravity(Gravity.CENTER_VERTICAL);
            TextViewComment.setId(ID1_COMMENT_COUNT);

            RelativeLayoutTool.addView(TextViewComment);

            RelativeLayout.LayoutParams ImageViewCommentParam = new RelativeLayout.LayoutParams(Misc.ToDP(32), RelativeLayout.LayoutParams.MATCH_PARENT);
            ImageViewCommentParam.addRule(RelativeLayout.LEFT_OF, ID1_COMMENT_COUNT);

            ImageView ImageViewComment = new ImageView(Activity);
            ImageViewComment.setLayoutParams(ImageViewCommentParam);
            ImageViewComment.setPadding(Misc.ToDP(3), Misc.ToDP(3), Misc.ToDP(3), Misc.ToDP(3));
            ImageViewComment.setImageResource(R.drawable._inbox_comment);
            ImageViewComment.setId(ID1_COMMENT);

            RelativeLayoutTool.addView(ImageViewComment);

            RelativeLayout.LayoutParams CircleImageViewPerson1Param = new RelativeLayout.LayoutParams(Misc.ToDP(24), Misc.ToDP(24));
            CircleImageViewPerson1Param.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            CircleImageViewPerson1Param.addRule(RelativeLayout.CENTER_VERTICAL);

            CircleImageView CircleImageViewPerson = new CircleImageView(Activity);
            CircleImageViewPerson.setLayoutParams(CircleImageViewPerson1Param);
            CircleImageViewPerson.setId(ID1_PERSON1);

            RelativeLayoutTool.addView(CircleImageViewPerson);

            RelativeLayout.LayoutParams CircleImageViewPerson2Param = new RelativeLayout.LayoutParams(Misc.ToDP(24), Misc.ToDP(24));
            CircleImageViewPerson2Param.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            CircleImageViewPerson2Param.addRule(RelativeLayout.CENTER_VERTICAL);
            CircleImageViewPerson2Param.setMargins(Misc.ToDP(30), 0, 0, 0);

            CircleImageView CircleImageViewPerson2 = new CircleImageView(Activity);
            CircleImageViewPerson2.setLayoutParams(CircleImageViewPerson2Param);
            CircleImageViewPerson2.setId(ID1_PERSON2);

            RelativeLayoutTool.addView(CircleImageViewPerson2);

            RelativeLayout.LayoutParams CircleImageViewPerson3Param = new RelativeLayout.LayoutParams(Misc.ToDP(24), Misc.ToDP(24));
            CircleImageViewPerson3Param.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            CircleImageViewPerson3Param.addRule(RelativeLayout.CENTER_VERTICAL);
            CircleImageViewPerson3Param.setMargins(Misc.ToDP(60), 0, 0, 0);

            CircleImageView CircleImageViewPerson3 = new CircleImageView(Activity);
            CircleImageViewPerson3.setLayoutParams(CircleImageViewPerson3Param);
            CircleImageViewPerson3.setId(ID1_PERSON3);

            RelativeLayoutTool.addView(CircleImageViewPerson3);

            RelativeLayout.LayoutParams CircleImageViewPerson4Param = new RelativeLayout.LayoutParams(Misc.ToDP(24), Misc.ToDP(24));
            CircleImageViewPerson4Param.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            CircleImageViewPerson4Param.addRule(RelativeLayout.CENTER_VERTICAL);
            CircleImageViewPerson4Param.setMargins(Misc.ToDP(90), 0, 0, 0);

            CircleImageView CircleImageViewPerson4 = new CircleImageView(Activity);
            CircleImageViewPerson4.setLayoutParams(CircleImageViewPerson4Param);
            CircleImageViewPerson4.setId(ID1_PERSON4);

            RelativeLayoutTool.addView(CircleImageViewPerson4);

















            RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1));
            ViewLineParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            ViewLineParam.setMargins(0, Misc.ToDP(5), 0, 0);

            View ViewLine = new View(Activity);
            ViewLine.setLayoutParams(ViewLineParam);
            ViewLine.setBackgroundResource(Misc.IsDark() ? R.color.LineDark : R.color.LineWhite);
            ViewLine.setId(ID1_VIEW_LINE);

            RelativeLayoutMain.addView(ViewLine);

            return new ViewHolderMain(RelativeLayoutMain, type);
        }
        else if (type == 2)
        {
            RelativeLayout RelativeLayoutInfo = new RelativeLayout(Activity);
            RelativeLayoutInfo.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

            View ViewLine = new View(Activity);
            ViewLine.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(5)));
            ViewLine.setBackgroundResource(Misc.IsDark() ? R.color.LineDark : R.color.Gray);
            ViewLine.setId(Misc.ViewID());

            RelativeLayoutInfo.addView(ViewLine);

            RelativeLayout.LayoutParams ImageViewCloseParam = new RelativeLayout.LayoutParams(Misc.ToDP(48), Misc.ToDP(48));
            ImageViewCloseParam.addRule(RelativeLayout.BELOW, ViewLine.getId());
            ImageViewCloseParam.addRule(Misc.Align("L"));

            ImageView ImageViewClose = new ImageView(Activity);
            ImageViewClose.setLayoutParams(ImageViewCloseParam);
            ImageViewClose.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ImageViewClose.setImageResource(Misc.IsDark() ? R.drawable.option_white : R.drawable.option_bluegray);
            ImageViewClose.setPadding(Misc.ToDP(8), Misc.ToDP(8), Misc.ToDP(8) , Misc.ToDP(8));
            ImageViewClose.setId(ID2_CLOSE);

            RelativeLayoutInfo.addView(ImageViewClose);

            RelativeLayout.LayoutParams TextViewLevelParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewLevelParam.addRule(RelativeLayout.BELOW, ViewLine.getId());
            TextViewLevelParam.setMargins(Misc.ToDP(15), Misc.ToDP(10), Misc.ToDP(15), Misc.ToDP(10));
            TextViewLevelParam.addRule(Misc.Align("R"));

            TextView TextViewLevel = new TextView(Activity, 14, true);
            TextViewLevel.setLayoutParams(TextViewLevelParam);
            TextViewLevel.setText(Activity.getString(R.string.InboxUIUserLevel));
            TextViewLevel.setTextColor(Misc.Color(Misc.IsDark() ? R.color.TextDark : R.color.Gray4));
            TextViewLevel.setId(Misc.ViewID());

            RelativeLayoutInfo.addView(TextViewLevel);

            RelativeLayout.LayoutParams CircleViewLevelParam = new RelativeLayout.LayoutParams(Misc.ToDP(75), Misc.ToDP(75));
            CircleViewLevelParam.addRule(RelativeLayout.BELOW, TextViewLevel.getId());
            CircleViewLevelParam.setMargins(Misc.ToDP(15), Misc.ToDP(10), Misc.ToDP(15), 0);
            CircleViewLevelParam.addRule(Misc.Align("R"));

            CircleView CircleViewLevel = new CircleView(Activity);
            CircleViewLevel.setLayoutParams(CircleViewLevelParam);
            CircleViewLevel.setId(ID2_LEVEL);
            CircleViewLevel.SetMessage("1");
            CircleViewLevel.SetMessageSize(35);
            CircleViewLevel.SetMessageColor(R.color.BlueLight);
            CircleViewLevel.SetStrokeColor(Misc.IsDark() ? R.color.TextDark : R.color.Gray2);
            CircleViewLevel.SetStrokeWidth(1);
            CircleViewLevel.SetProgressColor(R.color.BlueLight);
            CircleViewLevel.SetProgressWidth(4);
            CircleViewLevel.SetProgressPercentage(18);
            CircleViewLevel.InvalidateTextPaints();

            RelativeLayoutInfo.addView(CircleViewLevel);

            RelativeLayout.LayoutParams RelativeLayoutRatingParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            RelativeLayoutRatingParam.setMargins(Misc.ToDP(Misc.IsRTL() ? 15 : 5), Misc.ToDP(5), Misc.ToDP(!Misc.IsRTL() ? 15 : 5), 0);
            RelativeLayoutRatingParam.addRule(Misc.AlignTo("R"), CircleViewLevel.getId());
            RelativeLayoutRatingParam.addRule(RelativeLayout.BELOW, TextViewLevel.getId());

            RelativeLayout RelativeLayoutRating = new RelativeLayout(Activity);
            RelativeLayoutRating.setLayoutParams(RelativeLayoutRatingParam);
            RelativeLayoutRating.setId(Misc.ViewID());

            RelativeLayoutInfo.addView(RelativeLayoutRating);

            RelativeLayout.LayoutParams TextViewRatingParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewRatingParam.addRule(Misc.Align("R"));

            TextView TextViewRating = new TextView(Activity, 14, false);
            TextViewRating.setLayoutParams(TextViewRatingParam);
            TextViewRating.setText(Activity.getString(R.string.InboxUIRating));
            TextViewRating.setTextColor(Misc.Color(R.color.BlueLight));

            RelativeLayoutRating.addView(TextViewRating);

            RelativeLayout.LayoutParams TextViewNumberParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewNumberParam.setMargins(0, Misc.ToDP(2), 0, 0);
            TextViewNumberParam.addRule(Misc.Align("L"));

            TextView TextViewNumber = new TextView(Activity, 12, false);
            TextViewNumber.setLayoutParams(TextViewNumberParam);
            TextViewNumber.setText(("10/60"));
            TextViewNumber.setId(ID2_NUMBER);
            TextViewNumber.setTextColor(Misc.Color(Misc.IsDark() ? R.color.TextDark :  R.color.Gray4));

            RelativeLayoutRating.addView(TextViewNumber);

            RelativeLayout.LayoutParams LineViewRatingParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(10));
            LineViewRatingParam.setMargins(0, Misc.ToDP(3), 0, 0);
            LineViewRatingParam.addRule(Misc.AlignTo("R"), CircleViewLevel.getId());
            LineViewRatingParam.addRule(RelativeLayout.BELOW, TextViewNumber.getId());

            LineView LineViewRating = new LineView(Activity);
            LineViewRating.setLayoutParams(LineViewRatingParam);
            LineViewRating.setId(ID2_RATING);
            LineViewRating.SetStrokeColor(Misc.IsDark() ? R.color.TextDark : R.color.Gray2);
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
            TextViewInfo.setId(Misc.ViewID());
            TextViewInfo.setTextColor(Misc.Color(Misc.IsDark() ? R.color.TextDark : R.color.Gray4));

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
                    t.setColor(Misc.Color(R.color.BlueLight));
                    t.setUnderlineText(false);
                }
            };
            Span.setSpan(ClickableSpanMessage, Activity.getString(R.string.InboxUIInfo).length() + 1, Span.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            RelativeLayoutRating.addView(TextViewInfo);

            RelativeLayout.LayoutParams LinearLayoutMoreParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 0);
            LinearLayoutMoreParam.setMargins(0, Misc.ToDP(5), 0, 0);
            LinearLayoutMoreParam.addRule(RelativeLayout.BELOW, RelativeLayoutRating.getId());

            LinearLayout LinearLayoutMore = new LinearLayout(Activity);
            LinearLayoutMore.setLayoutParams(LinearLayoutMoreParam);
            LinearLayoutMore.setId(ID2_MORE);

            RelativeLayoutInfo.addView(LinearLayoutMore);

            CircleView CircleViewJoin = new CircleView(Activity);
            CircleViewJoin.setLayoutParams(new LinearLayout.LayoutParams(0, Misc.ToDP(60), 1));
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
            CircleViewPopular.setLayoutParams(new LinearLayout.LayoutParams(0, Misc.ToDP(60), 1));
            CircleViewPopular.setId(ID2_POPULAR);
            CircleViewPopular.SetMessage("4.5");
            CircleViewPopular.SetMessageSize(17);
            CircleViewPopular.SetMessageBold();
            CircleViewPopular.SetSubMessageSpace(14);
            CircleViewPopular.SetMessageColor(R.color.BlueLight);
            CircleViewPopular.SetStrokeColor(R.color.BlueLight);
            CircleViewPopular.SetStrokeWidth(1);
            CircleViewPopular.SetBitmap(R.drawable.popular_blue, 24);
            CircleViewPopular.InvalidateTextPaints();

            LinearLayoutMore.addView(CircleViewPopular);

            CircleView CircleViewPoint = new CircleView(Activity);
            CircleViewPoint.setLayoutParams(new LinearLayout.LayoutParams(0, Misc.ToDP(60), 1));
            CircleViewPoint.setId(ID2_POINT);
            CircleViewPoint.SetMessage("66K");
            CircleViewPoint.SetMessageSize(16);
            CircleViewPoint.SetMessageColor(R.color.TextDark);
            CircleViewPoint.SetStrokeColor(R.color.BlueLight);
            CircleViewPoint.SetStrokeWidth(50);
            CircleViewPoint.InvalidateTextPaints();

            LinearLayoutMore.addView(CircleViewPoint);

            CircleImageView CircleImageViewMedal = new CircleImageView(Activity);
            CircleImageViewMedal.setLayoutParams(new LinearLayout.LayoutParams(0, Misc.ToDP(60), 1));
            CircleImageViewMedal.setId(ID2_POINT);
            CircleImageViewMedal.SetBorderColor(Misc.IsDark() ? R.color.TextDark : R.color.Gray2);
            CircleImageViewMedal.SetBorderWidth(1);
            CircleImageViewMedal.SetWidthPadding();
            CircleImageViewMedal.setPadding(Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10), Misc.ToDP(10));
            CircleImageViewMedal.setImageResource(R.drawable.write_plus_blue);

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
            TextViewJoin.setTextColor(Misc.Color(Misc.IsDark() ? R.color.TextDark :R.color.Gray4));

            LinearLayoutMore2.addView(TextViewJoin);

            TextView TextViewPopular = new TextView(Activity, 14, false);
            TextViewPopular.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            TextViewPopular.setText(Activity.getString(R.string.InboxUIPopular));
            TextViewPopular.setGravity(Gravity.CENTER);
            TextViewPopular.setTextColor(Misc.Color(Misc.IsDark() ? R.color.TextDark : R.color.Gray4));

            LinearLayoutMore2.addView(TextViewPopular);

            TextView TextViewPoint = new TextView(Activity, 14, false);
            TextViewPoint.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            TextViewPoint.setText(Activity.getString(R.string.InboxUIPoint));
            TextViewPoint.setGravity(Gravity.CENTER);
            TextViewPoint.setTextColor(Misc.Color(Misc.IsDark() ? R.color.TextDark :  R.color.Gray4));

            LinearLayoutMore2.addView(TextViewPoint);

            TextView TextViewMedal = new TextView(Activity, 14, false);
            TextViewMedal.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            TextViewMedal.setText(Activity.getString(R.string.InboxUIMedal));
            TextViewMedal.setGravity(Gravity.CENTER);
            TextViewMedal.setTextColor(Misc.Color(Misc.IsDark() ? R.color.TextDark :  R.color.Gray4));

            LinearLayoutMore2.addView(TextViewMedal);

            RelativeLayout.LayoutParams ViewLine2Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(5));
            ViewLine2Param.setMargins(0, Misc.ToDP(9), 0, 0);
            ViewLine2Param.addRule(RelativeLayout.BELOW, LinearLayoutMore2.getId());

            View ViewLine2 = new View(Activity);
            ViewLine2.setLayoutParams(ViewLine2Param);
            ViewLine2.setBackgroundResource(Misc.IsDark() ? R.color.LineDark : R.color.Gray);

            RelativeLayoutInfo.addView(ViewLine2);

            return new ViewHolderMain(RelativeLayoutInfo, type);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(final ViewHolderMain Holder, int position)
    {
        final int Position = Holder.getAdapterPosition();

        if (Holder.getItemViewType() == 1)
        {
            try
            {
                GlideApp.with(Activity).load(PostList.get(Position).Profile).placeholder(R.drawable._general_avatar).into(Holder.CircleImageViewProfile);
                Holder.TextViewName.setText(PostList.get(Position).Name);

                if (PostList.get(Position).Medal == null || PostList.get(Position).Medal.isEmpty())
                    Holder.ImageViewMedal.setVisibility(View.GONE);
                else
                {
                    Holder.ImageViewMedal.setVisibility(View.VISIBLE);
                    GlideApp.with(Activity).load(PostList.get(Position).Medal).into(Holder.ImageViewMedal);
                }

                Holder.TextViewUsername.setText(("@" + PostList.get(Position).Username));
                Holder.TextViewTime.setText(Misc.TimeAgo(PostList.get(Position).Time));

                if (PostList.get(Position).Message == null || PostList.get(Position).Message.isEmpty())
                    Holder.TextViewMessage.setVisibility(View.GONE);
                else
                {
                    Holder.TextViewMessage.setVisibility(View.VISIBLE);
                    Holder.TextViewMessage.setText(PostList.get(Position).Message);
                    TagHandler.Show(Holder.TextViewMessage);
                }

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
                            {
                                final String URL1 = URL.get(0).toString();

                                Holder.ImageViewSingle.setVisibility(View.VISIBLE);
                                Holder.ImageViewSingle.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { Activity.GetManager().OpenView(new ImagePreviewUI(URL1), R.id.ContainerFull, "ImagePreviewUI");  } });

                                GlideApp.with(Activity).load(URL1).placeholder(R.color.BlueGray2).transforms(new CenterCrop(), new RoundedCorners(Misc.ToDP(6))).into(Holder.ImageViewSingle);
                            }
                            break;
                            case 2:
                            {
                                final String URL1 = URL.get(0).toString();
                                final String URL2 = URL.get(1).toString();

                                Holder.LinearLayoutDouble.setVisibility(View.VISIBLE);
                                Holder.ImageViewDouble1.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { Activity.GetManager().OpenView(new ImagePreviewUI(URL1, URL2), R.id.ContainerFull, "ImagePreviewUI"); } });
                                Holder.ImageViewDouble2.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { Activity.GetManager().OpenView(new ImagePreviewUI(URL2, URL1), R.id.ContainerFull, "ImagePreviewUI"); } });

                                GlideApp.with(Activity).load(URL1).placeholder(R.color.BlueGray2).transforms(new CenterCrop(), new RoundedCorners(Misc.ToDP(6))).into(Holder.ImageViewDouble1);
                                GlideApp.with(Activity).load(URL2).placeholder(R.color.BlueGray2).transforms(new CenterCrop(), new RoundedCorners(Misc.ToDP(6))).into(Holder.ImageViewDouble2);
                            }
                            break;
                            case 3:
                            {
                                final String URL1 = URL.get(0).toString();
                                final String URL2 = URL.get(1).toString();
                                final String URL3 = URL.get(2).toString();

                                Holder.LinearLayoutTriple.setVisibility(View.VISIBLE);
                                Holder.ImageViewTriple1.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { Activity.GetManager().OpenView(new ImagePreviewUI(URL1, URL2, URL3), R.id.ContainerFull, "ImagePreviewUI"); } });
                                Holder.ImageViewTriple2.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { Activity.GetManager().OpenView(new ImagePreviewUI(URL2, URL3, URL1), R.id.ContainerFull, "ImagePreviewUI"); } });
                                Holder.ImageViewTriple3.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { Activity.GetManager().OpenView(new ImagePreviewUI(URL3, URL1, URL2), R.id.ContainerFull, "ImagePreviewUI"); } });

                                GlideApp.with(Activity).load(URL1).placeholder(R.color.BlueGray2).transforms(new CenterCrop(), new RoundedCorners(Misc.ToDP(6))).into(Holder.ImageViewTriple1);
                                GlideApp.with(Activity).load(URL2).placeholder(R.color.BlueGray2).transforms(new CenterCrop(), new RoundedCorners(Misc.ToDP(6))).into(Holder.ImageViewTriple2);
                                GlideApp.with(Activity).load(URL3).placeholder(R.color.BlueGray2).transforms(new CenterCrop(), new RoundedCorners(Misc.ToDP(6))).into(Holder.ImageViewTriple3);
                            }
                            break;
                        }
                    }
                    break;
                    case 2:
                    {
                        JSONObject Video = new JSONObject(PostList.get(Position).Data);

                        final String URL = Video.getString("URL");

                        Holder.RelativeLayoutVideo.setVisibility(View.VISIBLE);
                        Holder.ImageViewVideo.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { Activity.GetManager().OpenView(new VideoPreviewUI(URL, false), R.id.ContainerFull, "VideoPreviewUI"); } });

                        GlideApp.with(Activity).load(URL.substring(0, URL.length() - 3) + "png").placeholder(R.color.BlueGray2).transforms(new CenterCrop()).into(Holder.ImageViewVideo);

                        int Time = Integer.parseInt(Video.getString("Duration")) / 1000;
                        int Min = Time / 60;
                        int Sec = Time - (Min * 60);

                        Holder.TextViewDurotion.setText((String.valueOf(Min) + ":" + String.valueOf(Sec)));
                    }
                    break;
                    case 3:
                    {
                        Holder.RelativeLayoutVote.setVisibility(View.VISIBLE);
                        Holder.RelativeLayoutVoteType2.setVisibility(View.GONE);
                        Holder.LinearLayoutVoteType1.setVisibility(View.GONE);

                        JSONObject Vote = new JSONObject(PostList.get(Position).Data);

                        // TODO
                        // TODO
                        // TODO
                        // TODO
                        // TODO

                        if (Vote.isNull("Vote"))
                        {
                            Holder.LinearLayoutVoteType1.setVisibility(View.VISIBLE);

                            Holder.TextViewType1Text1.setText("sss");
                            Holder.TextViewType1Text2.setText("sss");
                        }
                        else
                        {
                            Holder.RelativeLayoutVoteType2.setVisibility(View.VISIBLE);
                        }
                    }
                    break;
                    case 4:
                        JSONObject FileJSON = new JSONObject(PostList.get(Position).Data);

                        String Name = FileJSON.getString("Name").length() <= 15 ? FileJSON.getString("Name") : FileJSON.getString("Name").substring(0, Math.min(FileJSON.getString("Name").length(), 15)) + "...";
                        String Details =  new DecimalFormat("####.##").format(((double) FileJSON.getInt("Size") / 1024 / 1024)) + " MB / " + FileJSON.getString("Ext").toUpperCase().substring(1);

                        Holder.RelativeLayoutFile.setVisibility(View.VISIBLE);
                        Holder.ImageViewFile.setVisibility(View.VISIBLE);
                        Holder.TextViewFileName.setText(Name);
                        Holder.TextViewFileDetail.setText(Details);
                    break;
                }

                GlideApp.with(Activity).load(PostList.get(Position).Profile).into(Holder.CircleImageViewPerson1);
                GlideApp.with(Activity).load(PostList.get(Position).Profile).into(Holder.CircleImageViewPerson2);
                GlideApp.with(Activity).load(PostList.get(Position).Profile).into(Holder.CircleImageViewPerson3);
                GlideApp.with(Activity).load(PostList.get(Position).Profile).into(Holder.CircleImageViewPerson4);

                if (PostList.get(Position).IsLike)
                {
                    Holder.TextViewLikeCount.setTextColor(Misc.Color(R.color.RedLike));
                    GlideApp.with(Activity).load(R.drawable.like_red).into(Holder.ImageViewLike);
                }
                else
                {
                    Holder.TextViewLikeCount.setTextColor(Misc.Color(R.color.BlueGray2));
                    GlideApp.with(Activity).load(R.drawable.like_bluegray).into(Holder.ImageViewLike);
                }

                Holder.ImageViewLike.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (PostList.get(Position).IsLike)
                        {
                            Holder.TextViewLikeCount.setTextColor(Misc.Color(R.color.BlueGray2));
                            Holder.ImageViewLike.setImageResource(R.drawable.like_bluegray);

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
                            Holder.TextViewLikeCount.setTextColor(Misc.Color(R.color.RedLike));
                            Holder.ImageViewLike.setImageResource(R.drawable.like_red);

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

                if (!PostList.get(Position).IsComment)
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

                if (Position == PostList.size() - 1)
                    Holder.ViewLine.setVisibility(View.GONE);
                else
                    Holder.ViewLine.setVisibility(View.VISIBLE);
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
                        ValueAnimator Anim = ValueAnimator.ofInt(Holder.LinearLayoutMore.getMeasuredHeight(), Misc.ToDP(90));
                        Anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
                        {
                            @Override
                            public void onAnimationUpdate(ValueAnimator va)
                            {
                                int Height = (int) va.getAnimatedValue();

                                ViewGroup.LayoutParams Param = Holder.LinearLayoutMore.getLayoutParams();
                                Param.height = Math.min(Height, Misc.ToDP(65));

                                Holder.LinearLayoutMore.setLayoutParams(Param);

                                ViewGroup.LayoutParams Param2 = Holder.LinearLayoutMore2.getLayoutParams();
                                Param2.height = Math.min(Height, Misc.ToDP(20));

                                Holder.LinearLayoutMore2.setLayoutParams(Param2);
                            }
                        });
                        Anim.setDuration(300);
                        Anim.start();

                        IsMore = false;
                        Holder.ImageViewClose.setImageResource(Misc.IsDark() ? R.drawable.close_white : R.drawable.close_gray);
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

    public void OnTouchEvent(MotionEvent e)
    {
        if (RefreshView != null)
            RefreshView.onTouchEvent(e);
    }

    public void Update()
    {
        AndroidNetworking.post(Misc.GetRandomServer("PostListInbox"))
        .addBodyParameter("Skip", String.valueOf(GetSize()))
        .addHeaders("Token", SharedHandler.GetString(Activity, "Token"))
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
                        JSONArray ResultList = new JSONArray(Result.getString("Result"));

                        for (int K = 0; K < ResultList.length(); K++)
                        {
                            JSONObject D = ResultList.getJSONObject(K);

                            PostStruct P = new PostStruct();
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
                                P.DataType = D.getInt("Type");
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

                            PostList.add(P);
                        }

                        Collections.sort(PostList, new PostSort());

                        notifyDataSetChanged();
                    }
                }
                catch (Exception e)
                {
                    Misc.Debug("PostAdapter-Update: " + e.toString());
                }
            }

            @Override
            public void onError(ANError e)
            {
                Misc.Debug("PostAdapter-Update: " + e.toString());
            }
        });
    }

    public void Update(JSONObject D)
    {
        try
        {
            PostStruct P = new PostStruct();
            P.ID = D.getString("_id");
            P.Profile = SharedHandler.GetString(Activity, "Avatar");
            P.Name = SharedHandler.GetString(Activity, "Name");
            P.Medal = SharedHandler.GetString(Activity, "Medal");
            P.Username = SharedHandler.GetString(Activity, "Username");
            P.Time = D.getInt("Time");

            if (!D.isNull("Message"))
                P.Message = D.getString("Message");

            if (D.getInt("Type") != 0)
            {
                P.DataType = D.getInt("Type");
                P.Data = D.getString("Data");
            }

            P.Owner = D.getString("Owner");
            P.View = 0;
            P.Category = D.getInt("Category");
            P.LikeCount = 0;
            P.CommentCount = 0;
            P.IsLike = false;
            P.IsFollow = false;
            P.IsComment = false;
            P.IsBookmark = false;

            PostList.add(1, P);
        }
        catch (Exception e)
        {
            Misc.Debug("PostAdapter-Update2: " + e.toString());
        }
    }

    private int GetSize()
    {
        int Size = 0;

        for (PostStruct P : PostList)
            if (P.ViewType == 1)
                Size++;

        return Size;
    }

    private class PostStruct
    {
        String ID;
        String Profile;
        String Name;
        String Medal;
        String Username;
        String Owner;
        int Time;
        String Message;
        int DataType; // 0: Message 1: Image 2: Video 3: Vote 4: File
        String Data;
        int View;
        int Category;
        int LikeCount;
        int CommentCount;
        boolean IsLike;
        boolean IsFollow;
        boolean IsComment;
        boolean IsBookmark;
        String Person1ID;
        String Person1Avatar;
        String Person2ID;
        String Person2Avatar;
        String Person3ID;
        String Person3Avatar;
        String Person4ID;
        String Person4Avatar;

        int ViewType = 1; // 0: Pull 1: Post 2: Level 3: Suggestion 4: Loading

        PostStruct() { }
        PostStruct(int v) { ViewType = v; }

        void RevLike() { IsLike = !IsLike; }
        void InsLike() { LikeCount++; }
        void DesLike() { LikeCount--; }
    }

    private class PostSort implements Comparator<PostStruct>
    {
        @Override
        public int compare(PostStruct P1, PostStruct P2)
        {
            if (P1.ViewType == 0 || P2.ViewType == 0)
                return 0;

            return P1.Time > P2.Time ? -1 : (P1.Time < P2.Time) ? 1 : 0;
        }
    }
}
