package co.biogram.main.handler;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.androidnetworking.interfaces.DownloadProgressListener;
import com.androidnetworking.interfaces.StringRequestListener;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import co.biogram.main.R;
import co.biogram.main.fragment.FragmentActivity;
import co.biogram.main.ui.general.ImagePreviewUI;
import co.biogram.main.ui.general.VideoPreviewUI;
import co.biogram.main.ui.social.CommentUI;
import co.biogram.main.ui.social.LikeUI;
import co.biogram.main.ui.view.CircleImageView;
import co.biogram.main.ui.view.PullToRefreshView;
import co.biogram.main.ui.view.TextView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolderMain>
{
    private ArrayList<PostStruct> PostList = new ArrayList<>();
    private GradientDrawable DrawableBorder;
    private PullToRefreshView RefreshView;
    private FragmentActivity Activity;
    private DBHandler DB;
    private String Tag;

    public PostAdapter(FragmentActivity activity, String tag)
    {
        Tag = tag;
        Activity = activity;

        PostList.add(new PostStruct(0));

        DrawableBorder = new GradientDrawable();
        DrawableBorder.setStroke(Misc.ToDP(1), Misc.Color(R.color.LineWhite));
        DrawableBorder.setCornerRadius(Misc.ToDP(6));

        DB = new DBHandler(Activity);
    }

    private int ID1_MAIN = Misc.ViewID();
    private int ID1_PROFILE = Misc.ViewID();
    private int ID1_NAME = Misc.ViewID();
    private int ID1_MEDAL = Misc.ViewID();
    private int ID1_USERNAME = Misc.ViewID();
    private int ID1_TIME = Misc.ViewID();
    private int ID1_MESSAGE = Misc.ViewID();
    private int ID1_LIKE = Misc.ViewID();
    private int ID1_LIKE_COUNT = Misc.ViewID();
    private int ID1_COMMENT = Misc.ViewID();
    private int ID1_COMMENT_COUNT = Misc.ViewID();
    private int ID1_OPTION = Misc.ViewID();
    private int ID1_PERSON1 = Misc.ViewID();
    private int ID1_PERSON2 = Misc.ViewID();
    private int ID1_PERSON3 = Misc.ViewID();
    private int ID1_PERSON4 = Misc.ViewID();
    private int ID1_IMAGE_LAYOUT = Misc.ViewID();
    private int ID1_SINGLE = Misc.ViewID();
    private int ID1_DOUBLE_LAYOUT = Misc.ViewID();
    private int ID1_DOUBLE1 = Misc.ViewID();
    private int ID1_DOUBLE2 = Misc.ViewID();
    private int ID1_TRIPLE_LAYOUT = Misc.ViewID();
    private int ID1_TRIPLE1 = Misc.ViewID();
    private int ID1_TRIPLE2 = Misc.ViewID();
    private int ID1_TRIPLE3 = Misc.ViewID();
    private int ID1_VIDEO_LAYOUT = Misc.ViewID();
    private int ID1_VIDEO_IMAGE = Misc.ViewID();
    private int ID1_VIDEO_DUROTION = Misc.ViewID();
    private int ID1_VOTE_LAYOUT = Misc.ViewID();
    private int ID1_VOTE_TYPE1 = Misc.ViewID();
    private int ID1_VOTE_TYPE1_LIN1 = Misc.ViewID();
    private int ID1_VOTE_TYPE1_LIN2 = Misc.ViewID();
    private int ID1_VOTE_TYPE1_LIN3 = Misc.ViewID();
    private int ID1_VOTE_TYPE1_LIN4 = Misc.ViewID();
    private int ID1_VOTE_TYPE1_LIN5 = Misc.ViewID();
    private int ID1_VOTE_TYPE1_SEL1 = Misc.ViewID();
    private int ID1_VOTE_TYPE1_TEXT1 = Misc.ViewID();
    private int ID1_VOTE_TYPE1_SEL2 = Misc.ViewID();
    private int ID1_VOTE_TYPE1_TEXT2 = Misc.ViewID();
    private int ID1_VOTE_TYPE1_SEL3 = Misc.ViewID();
    private int ID1_VOTE_TYPE1_TEXT3 = Misc.ViewID();
    private int ID1_VOTE_TYPE1_SEL4 = Misc.ViewID();
    private int ID1_VOTE_TYPE1_TEXT4 = Misc.ViewID();
    private int ID1_VOTE_TYPE1_SEL5 = Misc.ViewID();
    private int ID1_VOTE_TYPE1_TEXT5 = Misc.ViewID();
    private int ID1_VOTE_TYPE1_SUBMIT = Misc.ViewID();
    private int ID1_VOTE_TYPE1_RESULT = Misc.ViewID();
    private int ID1_VOTE_TYPE1_TIME = Misc.ViewID();
    private int ID1_VOTE_TYPE2 = Misc.ViewID();
    private int ID1_VOTE_TYPE2_TEXT1 = Misc.ViewID();
    private int ID1_VOTE_TYPE2_TEXT2 = Misc.ViewID();
    private int ID1_VOTE_TYPE2_TEXT3 = Misc.ViewID();
    private int ID1_VOTE_TYPE2_TEXT4 = Misc.ViewID();
    private int ID1_VOTE_TYPE2_TEXT5 = Misc.ViewID();
    private int ID1_VOTE_TYPE2_PER1 = Misc.ViewID();
    private int ID1_VOTE_TYPE2_PER2 = Misc.ViewID();
    private int ID1_VOTE_TYPE2_PER3 = Misc.ViewID();
    private int ID1_VOTE_TYPE2_PER4 = Misc.ViewID();
    private int ID1_VOTE_TYPE2_PER5 = Misc.ViewID();
    private int ID1_VOTE_TYPE2_SELECT = Misc.ViewID();
    private int ID1_VOTE_TYPE2_RESULT = Misc.ViewID();
    private int ID1_VOTE_TYPE2_TIME = Misc.ViewID();
    private int ID1_FILE_LAYOUT = Misc.ViewID();
    private int ID1_FILE_IMAGE = Misc.ViewID();
    private int ID1_FILE_TEXT = Misc.ViewID();
    private int ID1_FILE_NAME = Misc.ViewID();
    private int ID1_FILE_DETAIL = Misc.ViewID();
    private int ID1_VIEW_LINE = Misc.ViewID();

    class ViewHolderMain extends RecyclerView.ViewHolder
    {
        RelativeLayout RelativeLayoutMain;
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
        LinearLayout LinearLayoutVoteLin1;
        LinearLayout LinearLayoutVoteLin2;
        LinearLayout LinearLayoutVoteLin3;
        LinearLayout LinearLayoutVoteLin4;
        LinearLayout LinearLayoutVoteLin5;
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
        ImageView ImageViewType2Select;
        TextView TextViewType2Result;
        TextView TextViewType2Time;
        RelativeLayout RelativeLayoutFile;
        ImageView ImageViewFile;
        TextView TextViewFile;
        TextView TextViewFileName;
        TextView TextViewFileDetail;

        ViewHolderMain(View v, int type)
        {
            super(v);

            if (type == 1)
            {
                RelativeLayoutMain = v.findViewById(ID1_MAIN);
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
                LinearLayoutVoteLin1 = v.findViewById(ID1_VOTE_TYPE1_LIN1);
                LinearLayoutVoteLin2 = v.findViewById(ID1_VOTE_TYPE1_LIN2);
                LinearLayoutVoteLin3 = v.findViewById(ID1_VOTE_TYPE1_LIN3);
                LinearLayoutVoteLin4 = v.findViewById(ID1_VOTE_TYPE1_LIN4);
                LinearLayoutVoteLin5 = v.findViewById(ID1_VOTE_TYPE1_LIN5);
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
                ImageViewType2Select = v.findViewById(ID1_VOTE_TYPE2_SELECT);
                TextViewType2Result = v.findViewById(ID1_VOTE_TYPE2_RESULT);
                TextViewType2Time = v.findViewById(ID1_VOTE_TYPE2_TIME);
                RelativeLayoutFile = v.findViewById(ID1_FILE_LAYOUT);
                ImageViewFile = v.findViewById(ID1_FILE_IMAGE);
                TextViewFile = v.findViewById(ID1_FILE_TEXT);
                TextViewFileName = v.findViewById(ID1_FILE_NAME);
                TextViewFileDetail = v.findViewById(ID1_FILE_DETAIL);
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
                    int Time = 0;

                    if (PostList.size() > 1)
                        Time = PostList.get(1).Time;

                    if (Time == 0)
                    {
                        Update();
                        return;
                    }

                    AndroidNetworking.post(Misc.GetRandomServer("PostListInbox"))
                    .addBodyParameter("Time", String.valueOf(Time))
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

                                if (Result.getInt("Message") == 0 && !Result.isNull("Result"))
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

                                        PostList.add(P);

                                        DB.InboxUpdate(P);
                                    }

                                    OnChanged();
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
                            RefreshView.SetRefreshComplete();
                        }
                    });
                }
            });

            return new ViewHolderMain(RefreshView, type);
        }
        else if (type == 1)
        {
            StateListDrawable StatePress = new StateListDrawable();
            StatePress.addState(new int[] { android.R.attr.state_pressed }, new ColorDrawable(Color.parseColor("#b0eeeeee")));

            RelativeLayout RelativeLayoutMain = new RelativeLayout(Activity);
            RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            RelativeLayoutMain.setBackground(StatePress);
            RelativeLayoutMain.setId(ID1_MAIN);

            RelativeLayout.LayoutParams CircleImageViewProfileParam = new RelativeLayout.LayoutParams(Misc.ToDP(50), Misc.ToDP(50));
            CircleImageViewProfileParam.setMargins(Misc.ToDP(8), Misc.ToDP(8), Misc.ToDP(8), Misc.ToDP(8));
            CircleImageViewProfileParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

            CircleImageView CircleImageViewProfile = new CircleImageView(Activity);
            CircleImageViewProfile.setLayoutParams(CircleImageViewProfileParam);
            CircleImageViewProfile.SetBorderColor(R.color.LineWhite);
            CircleImageViewProfile.setId(ID1_PROFILE);
            CircleImageViewProfile.SetBorderWidth(1);

            RelativeLayoutMain.addView(CircleImageViewProfile);

            RelativeLayout.LayoutParams TextViewNameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewNameParam.addRule(RelativeLayout.RIGHT_OF, ID1_PROFILE);
            TextViewNameParam.setMargins(0, Misc.ToDP(12), 0, 0);

            TextView TextViewName = new TextView(Activity, 14, true);
            TextViewName.setLayoutParams(TextViewNameParam);
            TextViewName.SetColor(R.color.TextWhite);
            TextViewName.setId(ID1_NAME);

            RelativeLayoutMain.addView(TextViewName);

            RelativeLayout.LayoutParams ImageViewMedalParam = new RelativeLayout.LayoutParams(Misc.ToDP(16), Misc.ToDP(16));
            ImageViewMedalParam.setMargins(Misc.ToDP(3), Misc.ToDP(16), 0, 0);
            ImageViewMedalParam.addRule(RelativeLayout.RIGHT_OF, ID1_NAME);

            ImageView ImageViewMedal = new ImageView(Activity);
            ImageViewMedal.setLayoutParams(ImageViewMedalParam);
            ImageViewMedal.setId(ID1_MEDAL);

            RelativeLayoutMain.addView(ImageViewMedal);

            RelativeLayout.LayoutParams TextViewUsernameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewUsernameParam.addRule(RelativeLayout.RIGHT_OF, ID1_PROFILE);
            TextViewUsernameParam.setMargins(0, Misc.ToDP(32), 0, 0);

            TextView TextViewUsername = new TextView(Activity, 14, false);
            TextViewUsername.setLayoutParams(TextViewUsernameParam);
            TextViewUsername.SetColor(R.color.Gray);
            TextViewUsername.setId(ID1_USERNAME);

            RelativeLayoutMain.addView(TextViewUsername);

            RelativeLayout.LayoutParams TextViewTimeParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewTimeParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            TextViewTimeParam.setMargins(0, Misc.ToDP(14), Misc.ToDP(10), 0);

            TextView TextViewTime = new TextView(Activity, 12, false);
            TextViewTime.setLayoutParams(TextViewTimeParam);
            TextViewTime.SetColor(R.color.Gray);
            TextViewTime.setId(ID1_TIME);

            RelativeLayoutMain.addView(TextViewTime);

            RelativeLayout.LayoutParams TextViewMessageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewMessageParam.addRule(RelativeLayout.RIGHT_OF, ID1_PROFILE);
            TextViewMessageParam.addRule(RelativeLayout.BELOW, ID1_USERNAME);
            TextViewMessageParam.setMargins(0, 0, Misc.ToDP(10), 0);

            TextView TextViewMessage = new TextView(Activity, 14, false);
            TextViewMessage.setLayoutParams(TextViewMessageParam);
            TextViewMessage.SetColor(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite);
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

            RelativeLayout.LayoutParams ImageViewPlayParam = new RelativeLayout.LayoutParams(Misc.ToDP(48), Misc.ToDP(48));
            ImageViewPlayParam.addRule(RelativeLayout.CENTER_IN_PARENT);

            ImageView ImageViewPlay = new ImageView(Activity);
            ImageViewPlay.setLayoutParams(ImageViewPlayParam);
            ImageViewPlay.setImageResource(R.drawable._general_play);

            RelativeLayoutVideo.addView(ImageViewPlay);

            GradientDrawable DrawableVideo = new GradientDrawable();
            DrawableVideo.setColor(Color.parseColor("#65000000"));
            DrawableVideo.setCornerRadius(Misc.ToDP(4));
            DrawableVideo.mutate();

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
            TextViewVideo.setText(Misc.String(R.string.PostAdapterVideo));
            TextViewVideo.setBackground(DrawableVideo);

            RelativeLayoutVideo.addView(TextViewVideo);

            RelativeLayout.LayoutParams RelativeLayoutVoteParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            RelativeLayoutVoteParam.setMargins(0, 0, Misc.ToDP(5), 0);

            RelativeLayout RelativeLayoutVote = new RelativeLayout(Activity);
            RelativeLayoutVote.setLayoutParams(RelativeLayoutVoteParam);
            RelativeLayoutVote.setPadding(Misc.ToDP(8), Misc.ToDP(8), Misc.ToDP(8), Misc.ToDP(8));
            RelativeLayoutVote.setBackground(DrawableBorder);
            RelativeLayoutVote.setVisibility(View.GONE);
            RelativeLayoutVote.setId(ID1_VOTE_LAYOUT);

            RelativeLayoutContent.addView(RelativeLayoutVote);

            {
                LinearLayout LinearLayoutType1 = new LinearLayout(Activity);
                LinearLayoutType1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                LinearLayoutType1.setOrientation(LinearLayout.VERTICAL);
                LinearLayoutType1.setVisibility(View.GONE);
                LinearLayoutType1.setId(ID1_VOTE_TYPE1);

                RelativeLayoutVote.addView(LinearLayoutType1);

                LinearLayout LinearLayoutVote1 = new LinearLayout(Activity);
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
            }

            {
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
                ViewVote.setId(Misc.ViewID());

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
            }

            RelativeLayout.LayoutParams RelativeLayoutFileParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(70));
            RelativeLayoutFileParam.setMargins(0, Misc.ToDP(5), Misc.ToDP(5), Misc.ToDP(5));

            RelativeLayout RelativeLayoutFile = new RelativeLayout(Activity);
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
            RelativeLayoutFile2.setId(Misc.ViewID());

            RelativeLayoutFile.addView(RelativeLayoutFile2);

            ImageView ImageViewFile = new ImageView(Activity);
            ImageViewFile.setLayoutParams(new RelativeLayout.LayoutParams(Misc.ToDP(50), Misc.ToDP(50)));
            ImageViewFile.setPadding(Misc.ToDP(14), Misc.ToDP(14), Misc.ToDP(14), Misc.ToDP(14));
            ImageViewFile.setImageResource(R.drawable._general_download);
            ImageViewFile.setId(ID1_FILE_IMAGE);

            RelativeLayoutFile2.addView(ImageViewFile);

            TextView TextViewFile = new TextView(Activity, 12, true);
            TextViewFile.setLayoutParams(new RelativeLayout.LayoutParams(Misc.ToDP(50), Misc.ToDP(50)));
            TextViewFile.setGravity(Gravity.CENTER);
            TextViewFile.setPadding(0, Misc.ToDP(5), 0, 0);
            TextViewFile.setTextColor(Color.WHITE);
            TextViewFile.setId(ID1_FILE_TEXT);

            RelativeLayoutFile2.addView(TextViewFile);

            RelativeLayout.LayoutParams TextViewFileNameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewFileNameParam.addRule(RelativeLayout.RIGHT_OF, RelativeLayoutFile2.getId());
            TextViewFileNameParam.setMargins(Misc.ToDP(8), Misc.ToDP(2), 0, 0);

            TextView TextViewFileName = new TextView(Activity, 14, false);
            TextViewFileName.setLayoutParams(TextViewFileNameParam);
            TextViewFileName.SetColor(R.color.TextWhite);
            TextViewFileName.setId(ID1_FILE_NAME);

            RelativeLayoutFile.addView(TextViewFileName);

            RelativeLayout.LayoutParams TextViewFileDetailParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewFileDetailParam.addRule(RelativeLayout.RIGHT_OF, RelativeLayoutFile2.getId());
            TextViewFileDetailParam.addRule(RelativeLayout.BELOW, ID1_FILE_NAME);
            TextViewFileDetailParam.setMargins(Misc.ToDP(8),0, 0, 0);

            TextView TextViewFileDetail = new TextView(Activity, 12, false);
            TextViewFileDetail.setLayoutParams(TextViewFileDetailParam);
            TextViewFileDetail.SetColor(R.color.Gray);
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
            TextViewLike.setPadding(Misc.ToDP(5), Misc.ToDP(4), Misc.ToDP(5), 0);
            TextViewLike.setGravity(Gravity.CENTER_VERTICAL);
            TextViewLike.SetColor(R.color.Gray);
            TextViewLike.setId(ID1_LIKE_COUNT);

            RelativeLayoutTool.addView(TextViewLike);

            RelativeLayout.LayoutParams ImageViewLikeParam = new RelativeLayout.LayoutParams(Misc.ToDP(32), RelativeLayout.LayoutParams.MATCH_PARENT);
            ImageViewLikeParam.addRule(RelativeLayout.LEFT_OF, ID1_LIKE_COUNT);

            ImageView ImageViewLike = new ImageView(Activity);
            ImageViewLike.setLayoutParams(ImageViewLikeParam);
            ImageViewLike.setPadding(Misc.ToDP(3), Misc.ToDP(3), Misc.ToDP(3), Misc.ToDP(3));
            ImageViewLike.setImageResource(R.drawable._general_like);
            ImageViewLike.setId(ID1_LIKE);

            RelativeLayoutTool.addView(ImageViewLike);

            RelativeLayout.LayoutParams TextViewCommentParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            TextViewCommentParam.addRule(RelativeLayout.LEFT_OF, ID1_LIKE);

            TextView TextViewComment = new TextView(Activity, 12, false);
            TextViewComment.setLayoutParams(TextViewCommentParam);
            TextViewComment.setPadding(Misc.ToDP(5), Misc.ToDP(4), Misc.ToDP(5), 0);
            TextViewComment.setGravity(Gravity.CENTER_VERTICAL);
            TextViewComment.SetColor(R.color.Gray);
            TextViewComment.setId(ID1_COMMENT_COUNT);

            RelativeLayoutTool.addView(TextViewComment);

            RelativeLayout.LayoutParams ImageViewCommentParam = new RelativeLayout.LayoutParams(Misc.ToDP(32), RelativeLayout.LayoutParams.MATCH_PARENT);
            ImageViewCommentParam.addRule(RelativeLayout.LEFT_OF, ID1_COMMENT_COUNT);

            ImageView ImageViewComment = new ImageView(Activity);
            ImageViewComment.setLayoutParams(ImageViewCommentParam);
            ImageViewComment.setPadding(Misc.ToDP(4), Misc.ToDP(4), Misc.ToDP(4), Misc.ToDP(4));
            ImageViewComment.setImageResource(R.drawable._inbox_comment);
            ImageViewComment.setId(ID1_COMMENT);

            RelativeLayoutTool.addView(ImageViewComment);

            RelativeLayout.LayoutParams CircleImageViewPerson1Param = new RelativeLayout.LayoutParams(Misc.ToDP(24), Misc.ToDP(24));
            CircleImageViewPerson1Param.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            CircleImageViewPerson1Param.addRule(RelativeLayout.CENTER_VERTICAL);

            CircleImageView CircleImageViewPerson = new CircleImageView(Activity);
            CircleImageViewPerson.setLayoutParams(CircleImageViewPerson1Param);
            CircleImageViewPerson.setId(ID1_PERSON1);
            CircleImageViewPerson.SetBorderWidth(Misc.ToDP(1));
            CircleImageViewPerson.SetBorderColor(R.color.LineWhite);

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
                Holder.RelativeLayoutMain.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        // TODO Open Post
                    }
                });

                GlideApp.with(Activity).load(PostList.get(Position).Profile).placeholder(R.drawable._general_avatar).into(Holder.CircleImageViewProfile);
                Holder.CircleImageViewProfile.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        // TODO Open Profile
                    }
                });

                Holder.TextViewName.setText(PostList.get(Position).Name);
                Holder.TextViewName.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        // TODO Open Profile
                    }
                });

                if (PostList.get(Position).Medal == null || PostList.get(Position).Medal.isEmpty())
                    Holder.ImageViewMedal.setVisibility(View.GONE);
                else
                {
                    Holder.ImageViewMedal.setVisibility(View.VISIBLE);
                    GlideApp.with(Activity).load(PostList.get(Position).Medal).into(Holder.ImageViewMedal);
                }

                Holder.TextViewUsername.setText(("@" + PostList.get(Position).Username));
                Holder.TextViewUsername.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        // TODO Open Profile
                    }
                });

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

                switch (PostList.get(Position).Type)
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
                                Holder.ImageViewSingle.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { Activity.GetManager().OpenView(new ImagePreviewUI(URL1, true), R.id.ContainerFull, "ImagePreviewUI"); } });

                                GlideApp.with(Activity).load(URL1).placeholder(R.color.LineWhite).transforms(new CenterCrop(), new RoundedCorners(Misc.ToDP(6))).into(Holder.ImageViewSingle);
                            }
                            break;
                            case 2:
                            {
                                final String URL1 = URL.get(0).toString();
                                final String URL2 = URL.get(1).toString();

                                Holder.LinearLayoutDouble.setVisibility(View.VISIBLE);

                                Holder.ImageViewDouble1.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { Activity.GetManager().OpenView(new ImagePreviewUI(URL1, URL2, true), R.id.ContainerFull, "ImagePreviewUI"); } });
                                Holder.ImageViewDouble2.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { Activity.GetManager().OpenView(new ImagePreviewUI(URL2, URL1, true), R.id.ContainerFull, "ImagePreviewUI"); } });

                                GlideApp.with(Activity).load(URL1).placeholder(R.color.LineWhite).transforms(new CenterCrop(), new RoundedCorners(Misc.ToDP(6))).into(Holder.ImageViewDouble1);
                                GlideApp.with(Activity).load(URL2).placeholder(R.color.LineWhite).transforms(new CenterCrop(), new RoundedCorners(Misc.ToDP(6))).into(Holder.ImageViewDouble2);
                            }
                            break;
                            case 3:
                            {
                                final String URL1 = URL.get(0).toString();
                                final String URL2 = URL.get(1).toString();
                                final String URL3 = URL.get(2).toString();

                                Holder.LinearLayoutTriple.setVisibility(View.VISIBLE);

                                Holder.ImageViewTriple1.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { Activity.GetManager().OpenView(new ImagePreviewUI(URL1, URL2, URL3, true), R.id.ContainerFull, "ImagePreviewUI"); } });
                                Holder.ImageViewTriple2.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { Activity.GetManager().OpenView(new ImagePreviewUI(URL2, URL3, URL1, true), R.id.ContainerFull, "ImagePreviewUI"); } });
                                Holder.ImageViewTriple3.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { Activity.GetManager().OpenView(new ImagePreviewUI(URL3, URL1, URL2, true), R.id.ContainerFull, "ImagePreviewUI"); } });

                                GlideApp.with(Activity).load(URL1).placeholder(R.color.LineWhite).transforms(new CenterCrop(), new RoundedCorners(Misc.ToDP(6))).into(Holder.ImageViewTriple1);
                                GlideApp.with(Activity).load(URL2).placeholder(R.color.LineWhite).transforms(new CenterCrop(), new RoundedCorners(Misc.ToDP(6))).into(Holder.ImageViewTriple2);
                                GlideApp.with(Activity).load(URL3).placeholder(R.color.LineWhite).transforms(new CenterCrop(), new RoundedCorners(Misc.ToDP(6))).into(Holder.ImageViewTriple3);
                            }
                            break;
                        }
                    }
                    break;
                    case 2:
                    {
                        JSONObject Video = new JSONObject(PostList.get(Position).Data);

                        final String URL = Video.getString("URL");

                        // TODO Add Video Size

                        Holder.RelativeLayoutVideo.setVisibility(View.VISIBLE);

                        Holder.ImageViewVideo.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { Activity.GetManager().OpenView(new VideoPreviewUI(URL, false, true), R.id.ContainerFull, "VideoPreviewUI"); } });

                        GlideApp.with(Activity).load(URL.substring(0, URL.length() - 3) + "png").placeholder(R.color.Gray).transforms(new CenterCrop()).into(Holder.ImageViewVideo);

                        int Time = Integer.parseInt(Video.getString("Duration")) / 1000;
                        int Min = Time / 60;
                        int Sec = Time - (Min * 60);

                        Holder.TextViewDurotion.setText(((Min < 10 ?  "0" : "") + String.valueOf(Min) + ":" + (Sec < 10 ?  "0" : "") + String.valueOf(Sec)));
                    }
                    break;
                    case 3:
                    {
                        Holder.RelativeLayoutVote.setVisibility(View.VISIBLE);
                        Holder.LinearLayoutVoteType1.setVisibility(View.GONE);
                        Holder.RelativeLayoutVoteType2.setVisibility(View.GONE);

                        final JSONObject Vote = new JSONObject(PostList.get(Position).Data);

                        if (Vote.isNull("Vote"))
                        {
                            Holder.LinearLayoutVoteType1.setVisibility(View.VISIBLE);

                            Holder.TextViewType1Text1.setText(Vote.getString("Vote1"));
                            Holder.TextViewType1Text2.setText(Vote.getString("Vote2"));

                            if (Vote.isNull("Vote3"))
                                Holder.LinearLayoutVoteLin3.setVisibility(View.GONE);
                            else
                            {
                                Holder.LinearLayoutVoteLin3.setVisibility(View.VISIBLE);
                                Holder.TextViewType1Text3.setText(Vote.getString("Vote3"));
                            }

                            if (Vote.isNull("Vote4"))
                                Holder.LinearLayoutVoteLin4.setVisibility(View.GONE);
                            else
                            {
                                Holder.LinearLayoutVoteLin4.setVisibility(View.VISIBLE);
                                Holder.TextViewType1Text4.setText(Vote.getString("Vote4"));
                            }

                            if (Vote.isNull("Vote5"))
                                Holder.LinearLayoutVoteLin5.setVisibility(View.GONE);
                            else
                            {
                                Holder.LinearLayoutVoteLin5.setVisibility(View.VISIBLE);
                                Holder.TextViewType1Text5.setText(Vote.getString("Vote5"));
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

                            Holder.TextViewType1Result.setText((Total + " " + Misc.String(R.string.PostAdapterVotes)));
                            Holder.TextViewType1Time.setText(Misc.TimeLeft(Vote.getInt("Time")));

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

                                    Holder.ViewType1Sel1.setBackground(Select);
                                    Holder.ViewType1Sel2.setBackground(Select);
                                    Holder.ViewType1Sel3.setBackground(Select);
                                    Holder.ViewType1Sel4.setBackground(Select);
                                    Holder.ViewType1Sel5.setBackground(Select);

                                    Holder.ViewType1Sel1.setTag("0");
                                    Holder.ViewType1Sel2.setTag("0");
                                    Holder.ViewType1Sel3.setTag("0");
                                    Holder.ViewType1Sel4.setTag("0");
                                    Holder.ViewType1Sel5.setTag("0");

                                    if (v.getId() == ID1_VOTE_TYPE1_LIN1)
                                    {
                                        Holder.ViewType1Sel1.setBackground(Layer);
                                        Holder.ViewType1Sel1.setTag("1");
                                    }
                                    else if (v.getId() == ID1_VOTE_TYPE1_LIN2)
                                    {
                                        Holder.ViewType1Sel2.setBackground(Layer);
                                        Holder.ViewType1Sel2.setTag("1");
                                    }
                                    else if (v.getId() == ID1_VOTE_TYPE1_LIN3)
                                    {
                                        Holder.ViewType1Sel3.setBackground(Layer);
                                        Holder.ViewType1Sel3.setTag("1");
                                    }
                                    else if (v.getId() == ID1_VOTE_TYPE1_LIN4)
                                    {
                                        Holder.ViewType1Sel4.setBackground(Layer);
                                        Holder.ViewType1Sel4.setTag("1");
                                    }
                                    else if (v.getId() == ID1_VOTE_TYPE1_LIN5)
                                    {
                                        Holder.ViewType1Sel5.setBackground(Layer);
                                        Holder.ViewType1Sel5.setTag("1");
                                    }
                                }
                            };

                            Holder.LinearLayoutVoteLin1.setOnClickListener(VoteSelection);
                            Holder.LinearLayoutVoteLin2.setOnClickListener(VoteSelection);
                            Holder.LinearLayoutVoteLin3.setOnClickListener(VoteSelection);
                            Holder.LinearLayoutVoteLin4.setOnClickListener(VoteSelection);
                            Holder.LinearLayoutVoteLin5.setOnClickListener(VoteSelection);

                            Holder.TextViewType1Submit.setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {
                                    int S = 0;

                                    if (Holder.ViewType1Sel1.getTag() == "1")
                                        S = 1;
                                    else if (Holder.ViewType1Sel2.getTag() == "1")
                                        S = 2;
                                    else if (Holder.ViewType1Sel3.getTag() == "1")
                                        S = 3;
                                    else if (Holder.ViewType1Sel4.getTag() == "1")
                                        S = 4;
                                    else if (Holder.ViewType1Sel5.getTag() == "1")
                                        S = 5;

                                    if (S == 0)
                                        return;

                                    final int Sel = S;

                                    AndroidNetworking.post(Misc.GetRandomServer("PostVote"))
                                    .addBodyParameter("Post", PostList.get(Position).ID)
                                    .addBodyParameter("Vote", String.valueOf(Sel))
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

                                                    PostList.get(Position).Data = Data.toString();

                                                    notifyDataSetChanged();
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
                            Holder.RelativeLayoutVoteType2.setVisibility(View.VISIBLE);

                            int C1 = Vote.isNull("Count1") ? 0 : Vote.getInt("Count1");
                            int C2 = Vote.isNull("Count2") ? 0 : Vote.getInt("Count2");
                            int C3 = Vote.isNull("Count3") ? 0 : Vote.getInt("Count3");
                            int C4 = Vote.isNull("Count4") ? 0 : Vote.getInt("Count4");
                            int C5 = Vote.isNull("Count5") ? 0 : Vote.getInt("Count5");

                            int Total = C1 + C2 + C3 + C4 + C5;

                            Holder.TextViewType2Text1.setText(Vote.getString("Vote1"));
                            Holder.TextViewType2Text1.FillBackground(C1 == 0 ? 0 : C1 * 100 / Total);
                            Holder.TextViewType2Per1.setText((C1 == 0 ? "0%" : String.valueOf(C1 * 100 / Total) + "%"));

                            Holder.TextViewType2Text2.setText(Vote.getString("Vote2"));
                            Holder.TextViewType2Text2.FillBackground(C2 == 0 ? 0 : C2 * 100 / Total);
                            Holder.TextViewType2Per2.setText((C2 == 0 ? "0%" : String.valueOf(C2 * 100 / Total) + "%"));

                            if (Vote.isNull("Vote3"))
                            {
                                Holder.TextViewType2Per3.setVisibility(View.GONE);
                                Holder.TextViewType2Text3.setVisibility(View.GONE);
                            }
                            else
                            {
                                Holder.TextViewType2Per3.setVisibility(View.VISIBLE);
                                Holder.TextViewType2Per3.setText((C3 == 0 ? "0%" : String.valueOf(C3 * 100 / Total) + "%"));

                                Holder.TextViewType2Text3.setVisibility(View.VISIBLE);
                                Holder.TextViewType2Text3.setText(Vote.getString("Vote3"));
                                Holder.TextViewType2Text3.FillBackground(C3 == 0 ? 0 : C3 * 100 / Total);
                            }

                            if (Vote.isNull("Vote4"))
                            {
                                Holder.TextViewType2Per4.setVisibility(View.GONE);
                                Holder.TextViewType2Text4.setVisibility(View.GONE);
                            }
                            else
                            {
                                Holder.TextViewType2Per4.setVisibility(View.VISIBLE);
                                Holder.TextViewType2Per4.setText((C4 == 0 ? "0%" : String.valueOf(C4 * 100 / Total) + "%"));

                                Holder.TextViewType2Text4.setVisibility(View.VISIBLE);
                                Holder.TextViewType2Text4.setText(Vote.getString("Vote4"));
                                Holder.TextViewType2Text4.FillBackground(C4 == 0 ? 0 : C4 * 100 / Total);
                            }

                            if (Vote.isNull("Vote5"))
                            {
                                Holder.TextViewType2Per5.setVisibility(View.GONE);
                                Holder.TextViewType2Text5.setVisibility(View.GONE);
                            }
                            else
                            {
                                Holder.TextViewType2Per5.setVisibility(View.VISIBLE);
                                Holder.TextViewType2Per5.setText((C5 == 0 ? "0%" : String.valueOf(C5 * 100 / Total) + "%"));

                                Holder.TextViewType2Text5.setVisibility(View.VISIBLE);
                                Holder.TextViewType2Text5.setText(Vote.getString("Vote5"));
                                Holder.TextViewType2Text5.FillBackground(C5 == 0 ? 0 : C5 * 100 / Total);
                            }

                            int ID = 0;
                            Holder.ImageViewType2Select.setVisibility(View.VISIBLE);

                            switch (Vote.getInt("Vote"))
                            {
                                case 0: Holder.ImageViewType2Select.setVisibility(View.GONE); break;
                                case 2: ID = Holder.TextViewType2Text1.getId(); break;
                                case 3: ID = Holder.TextViewType2Text2.getId(); break;
                                case 4: ID = Holder.TextViewType2Text3.getId(); break;
                                case 5: ID = Holder.TextViewType2Text5.getId(); break;
                            }

                            RelativeLayout.LayoutParams Param = (RelativeLayout.LayoutParams) Holder.ImageViewType2Select.getLayoutParams();
                            Param.setMargins(-Misc.ToDP(10), 0, Misc.ToDP(10), 0);

                            if (ID != 0)
                                Param.addRule(RelativeLayout.BELOW, ID);

                            Holder.ImageViewType2Select.setLayoutParams(Param);
                            Holder.TextViewType2Result.setText((Total + " " + Misc.String(R.string.PostAdapterVotes)));
                            Holder.TextViewType2Time.setText(Misc.TimeLeft(Vote.getInt("Time")));
                        }
                    }
                    break;
                    case 4:
                    {
                        final JSONObject file = new JSONObject(PostList.get(Position).Data);
                        final String URL = file.getString("URL");
                        final String FileName = file.getString("Name");

                        String Name = file.getString("Name").length() <= 15 ? file.getString("Name") : file.getString("Name").substring(0, Math.min(file.getString("Name").length(), 15)) + "...";
                        String Details = new DecimalFormat("####.##").format(((double) file.getInt("Size") / 1024 / 1024)) + " MB / " + file.getString("Ext").toUpperCase().substring(1);

                        Holder.RelativeLayoutFile.setVisibility(View.VISIBLE);
                        Holder.TextViewFileName.setText(Name);
                        Holder.TextViewFileDetail.setText(Details);

                        final File Download = new File(CacheHandler.Dir(CacheHandler.DOWNLOAD), FileName);

                        if (PostList.get(Position).IsDownloading)
                        {
                            Holder.ImageViewFile.setVisibility(View.GONE);
                            Holder.TextViewFile.setVisibility(View.VISIBLE);
                        }
                        else if (Download.exists())
                        {
                            Holder.TextViewFile.setVisibility(View.GONE);
                            Holder.ImageViewFile.setVisibility(View.VISIBLE);
                            Holder.ImageViewFile.setImageResource(R.drawable._inbox_downloaded);
                            Holder.ImageViewFile.setPadding(Misc.ToDP(7), Misc.ToDP(7), Misc.ToDP(7), Misc.ToDP(7));
                        }
                        else
                        {
                            Holder.TextViewFile.setVisibility(View.GONE);
                            Holder.ImageViewFile.setVisibility(View.VISIBLE);
                            Holder.ImageViewFile.setImageResource(R.drawable._general_download);
                            Holder.ImageViewFile.setPadding(Misc.ToDP(14), Misc.ToDP(14), Misc.ToDP(14), Misc.ToDP(14));
                        }

                        Holder.RelativeLayoutFile.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View view)
                            {
                                if (PostList.get(Position).IsDownloading)
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
                                            AndroidNetworking.forceCancel(PostList.get(Position).ID);

                                            Holder.TextViewFile.setVisibility(View.GONE);
                                            Holder.ImageViewFile.setVisibility(View.VISIBLE);

                                            PostList.get(Position).IsDownloading = false;

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
                                            I.setDataAndType(URI, "*/*");

                                        I.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                        Activity.startActivity(I);
                                    }
                                    catch (Exception e)
                                    {
                                        Misc.Toast(Misc.String(R.string.PostAdapterNoHandle));
                                    }

                                    return;
                                }

                                PostList.get(Position).IsDownloading = true;

                                Holder.ImageViewFile.setVisibility(View.GONE);
                                Holder.TextViewFile.setVisibility(View.VISIBLE);
                                Holder.TextViewFile.setText("0%");

                                AndroidNetworking.download(URL, CacheHandler.Dir(CacheHandler.DOWNLOAD).getAbsolutePath(), FileName)
                                .setPriority(Priority.MEDIUM)
                                .setTag(PostList.get(Position).ID)
                                .build()
                                .setDownloadProgressListener(new DownloadProgressListener()
                                {
                                    @Override
                                    public void onProgress(long D, long T)
                                    {
                                        Holder.TextViewFile.setText((String.valueOf(D * 100 / T) + "%"));
                                    }
                                })
                                .startDownload(new DownloadListener()
                                {
                                    @Override
                                    public void onDownloadComplete()
                                    {
                                        PostList.get(Position).IsDownloading = false;

                                        Holder.TextViewFile.setVisibility(View.GONE);
                                        Holder.ImageViewFile.setVisibility(View.VISIBLE);
                                        Holder.ImageViewFile.setImageResource(R.drawable._inbox_downloaded);
                                        Holder.ImageViewFile.setPadding(Misc.ToDP(7), Misc.ToDP(7), Misc.ToDP(7), Misc.ToDP(7));
                                    }

                                    @Override
                                    public void onError(ANError e)
                                    {
                                        PostList.get(Position).IsDownloading = false;

                                        Holder.TextViewFile.setVisibility(View.GONE);
                                        Holder.ImageViewFile.setVisibility(View.VISIBLE);
                                        Holder.ImageViewFile.setImageResource(R.drawable._general_download);
                                    }
                                });
                            }
                        });
                    }
                    break;
                }

                if (PostList.get(Position).Person1Avatar != null && !PostList.get(Position).Person1Avatar.isEmpty())
                {
                    GlideApp.with(Activity).load(PostList.get(Position).Person1Avatar).into(Holder.CircleImageViewPerson1);
                    Holder.CircleImageViewPerson1.setVisibility(View.VISIBLE);
                    Holder.CircleImageViewPerson1.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View view)
                        {
                            // TODO Open Profile
                        }
                    });
                }
                else
                    Holder.CircleImageViewPerson1.setVisibility(View.GONE);

                if (PostList.get(Position).Person2Avatar != null && !PostList.get(Position).Person2Avatar.isEmpty())
                {
                    GlideApp.with(Activity).load(PostList.get(Position).Person2Avatar).into(Holder.CircleImageViewPerson2);
                    Holder.CircleImageViewPerson2.setVisibility(View.VISIBLE);
                    Holder.CircleImageViewPerson2.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View view)
                        {
                            // TODO Open Profile
                        }
                    });
                }
                else
                    Holder.CircleImageViewPerson2.setVisibility(View.GONE);

                if (PostList.get(Position).Person3Avatar != null && !PostList.get(Position).Person3Avatar.isEmpty())
                {
                    GlideApp.with(Activity).load(PostList.get(Position).Person3Avatar).into(Holder.CircleImageViewPerson3);
                    Holder.CircleImageViewPerson3.setVisibility(View.VISIBLE);
                    Holder.CircleImageViewPerson3.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View view)
                        {
                            // TODO Open Profile
                        }
                    });
                }
                else
                    Holder.CircleImageViewPerson3.setVisibility(View.GONE);

                if (PostList.get(Position).Person4Avatar != null && !PostList.get(Position).Person4Avatar.isEmpty())
                {
                    GlideApp.with(Activity).load(PostList.get(Position).Person4Avatar).into(Holder.CircleImageViewPerson4);
                    Holder.CircleImageViewPerson4.setVisibility(View.VISIBLE);
                    Holder.CircleImageViewPerson4.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View view)
                        {
                            // TODO Open Profile
                        }
                    });
                }
                else
                    Holder.CircleImageViewPerson4.setVisibility(View.GONE);

                if (PostList.get(Position).IsLike)
                {
                    Holder.TextViewLikeCount.SetColor(R.color.Red);
                    GlideApp.with(Activity).load(R.drawable._general_like_red).into(Holder.ImageViewLike);
                }
                else
                {
                    Holder.TextViewLikeCount.SetColor(R.color.Gray);
                    GlideApp.with(Activity).load(R.drawable._general_like).into(Holder.ImageViewLike);
                }

                Holder.ImageViewLike.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (PostList.get(Position).IsLike)
                        {
                            Holder.TextViewLikeCount.SetColor(R.color.Gray);
                            Holder.ImageViewLike.setImageResource(R.drawable._general_like);

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
                            Holder.TextViewLikeCount.SetColor(R.color.Red);
                            Holder.ImageViewLike.setImageResource(R.drawable._general_like_red);

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

                        AndroidNetworking.post(Misc.GetRandomServer("PostLike"))
                        .addBodyParameter("PostID", PostList.get(Position).ID)
                        .addHeaders("Token", SharedHandler.GetString( "Token"))
                        .setTag(Tag)
                        .build()
                        .getAsString(null);
                    }
                });

                Holder.TextViewLikeCount.setText(String.valueOf(PostList.get(Position).LikeCount));
                Holder.TextViewLikeCount.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { Activity.GetManager().OpenView(new LikeUI(PostList.get(Position).ID, false), R.id.ContainerFull, "LikeUI"); } });

                if (PostList.get(Position).IsComment)
                {
                    Holder.ImageViewComment.setVisibility(View.GONE);
                    Holder.TextViewCommentCount.setVisibility(View.GONE);
                }
                else
                {
                    Holder.ImageViewComment.setVisibility(View.VISIBLE);
                    Holder.ImageViewComment.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { Activity.GetManager().OpenView(new CommentUI(PostList, Position), R.id.ContainerFull, "CommentUI"); } });

                    Holder.TextViewCommentCount.setVisibility(View.VISIBLE);
                    Holder.TextViewCommentCount.setText(String.valueOf(PostList.get(Position).CommentCount));
                    Holder.TextViewCommentCount.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { Activity.GetManager().OpenView(new CommentUI(PostList, Position), R.id.ContainerFull, "CommentUI"); } });
                }

                Holder.ImageViewOption.setOnClickListener(new View.OnClickListener()
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

                        if (PostList.get(Position).Owner.equals(SharedHandler.GetString( "ID")))
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
                                    .addBodyParameter("PostID", PostList.get(Position).ID)
                                    .addHeaders("Token", SharedHandler.GetString( "Token"))
                                    .setTag(Tag)
                                    .build()
                                    .getAsString(new StringRequestListener()
                                    {
                                        @Override
                                        public void onResponse(String e)
                                        {
                                            DB.InboxDelete(PostList.get(Position).ID);
                                            Misc.Toast(Activity.getString(R.string.PostAdapterPostDeleted));
                                            PostList.remove(Position);
                                            notifyDataSetChanged();
                                        }

                                        @Override public void onError(ANError e) { }
                                    });

                                    DialogOption.dismiss();
                                }
                            });

                            LinearLayoutMain.addView(TextViewDelete);

                            if (PostList.get(Position).Time < ((System.currentTimeMillis() / 1000) + 172800))
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
                                        EditTextMessage.setText(PostList.get(Position).Message);
                                        EditTextMessage.setHint(R.string.PostAdapterOptionEditMessage);
                                        EditTextMessage.setHintTextColor(Misc.Color(R.color.Gray));
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
                                                .addBodyParameter("PostID", PostList.get(Position).ID)
                                                .addBodyParameter("Message", EditTextMessage.getText().toString())
                                                .addHeaders("Token", SharedHandler.GetString("Token"))
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
                                                                PostList.get(Position).Message = Result.getString("Text");

                                                                if (PostList.get(Position).Message == null || PostList.get(Position).Message.isEmpty())
                                                                    Holder.TextViewMessage.setVisibility(View.GONE);
                                                                else
                                                                {
                                                                    Holder.TextViewMessage.setVisibility(View.VISIBLE);
                                                                    Holder.TextViewMessage.setText(PostList.get(Position).Message);

                                                                    TagHandler.Show(Holder.TextViewMessage);
                                                                }

                                                                DB.InboxMessage(PostList.get(Position).ID, PostList.get(Position).Message);

                                                                notifyDataSetChanged();
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
                            TextViewFollow.setText(Misc.String(PostList.get(Position).IsFollow ? R.string.PostAdapterOptionUnfollow : R.string.PostAdapterOptionFollow));
                            TextViewFollow.setGravity(Gravity.CENTER_VERTICAL);
                            TextViewFollow.setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {
                                    AndroidNetworking.post(Misc.GetRandomServer("ProfileFollow"))
                                    .addBodyParameter("Username", PostList.get(Position).Username)
                                    .addHeaders("Token", SharedHandler.GetString( "Token"))
                                    .setTag(Tag)
                                    .build()
                                    .getAsString(new StringRequestListener()
                                    {
                                        @Override
                                        public void onResponse(String e)
                                        {
                                            PostList.get(Position).IsFollow = !PostList.get(Position).IsFollow;
                                            Misc.Toast(PostList.get(Position).Username + " " + (PostList.get(Position).IsFollow ? Activity.getString(R.string.PostAdapterUserFollowed) : Activity.getString(R.string.PostAdapterUserFollowed)));
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

                        if (!PostList.get(Position).Owner.equals(SharedHandler.GetString( "ID")))
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
                                ClipData clip = ClipData.newPlainText("InboxMessage:"+ PostList.get(Position).ID, PostList.get(Position).Message);

                                if (clipboard != null)
                                    clipboard.setPrimaryClip(clip);

                                Misc.Toast(Misc.String(R.string.PostAdapterOptionCopy2));
                                DialogOption.dismiss();
                            }
                        });

                        View ViewLine4 = new View(Activity);
                        ViewLine4.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1)));
                        ViewLine4.setBackgroundResource(R.color.LineWhite);

                        if (PostList.get(Position).Message != null && !PostList.get(Position).Message.isEmpty())
                        {

                            LinearLayoutMain.addView(TextViewCopy);
                            LinearLayoutMain.addView(ViewLine4);
                        }

                        TextView TextViewBookmark = new TextView(Activity, 14, false);
                        TextViewBookmark.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56)));
                        TextViewBookmark.SetColor(Misc.IsDark() ? R.color.TextDark : R.color.TextWhite);
                        TextViewBookmark.setPadding(Misc.ToDP(15), 0, Misc.ToDP(15), 0);
                        TextViewBookmark.setText(Misc.String(PostList.get(Position).IsBookmark ? R.string.PostAdapterOptionUnbookmark : R.string.PostAdapterOptionBookmark));
                        TextViewBookmark.setGravity(Gravity.CENTER_VERTICAL);
                        TextViewBookmark.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                AndroidNetworking.post(Misc.GetRandomServer("PostBookmark"))
                                .addBodyParameter("PostID", PostList.get(Position).ID)
                                .addHeaders("Token", SharedHandler.GetString( "Token"))
                                .setTag(Tag)
                                .build()
                                .getAsString(new StringRequestListener()
                                {
                                    @Override
                                    public void onResponse(String e)
                                    {
                                        PostList.get(Position).IsBookmark = !PostList.get(Position).IsBookmark;
                                        Misc.Toast(Activity.getString(R.string.PostAdapterPostBookmark));
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
                                String Message = PostList.get(Position).Name + " " + Activity.getString(R.string.PostAdapterPostShare1) + "\n";
                                Message += PostList.get(Position).Message == null ? "" : PostList.get(Position).Message;
                                Message += "\n" +  Activity.getString(R.string.PostAdapterPostShare1) + "\nhttp://biogram.co/post/" + PostList.get(Position).ID;

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

    private void OnChanged()
    {
        Collections.sort(PostList, new PostSort());

        notifyDataSetChanged();
    }

    public void OnTouchEvent(MotionEvent e)
    {
        if (RefreshView != null)
            RefreshView.onTouchEvent(e);
    }

    public void Update()
    {
        int Size = GetSize();
        Cursor cursor = DB.InboxPost(Size);
        StringBuilder ID = new StringBuilder();

        while (cursor.moveToNext())
        {
            PostStruct P = new PostStruct();
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
            PostList.add(P);

            ID.append(P.ID).append(",");
        }

        cursor.close();
        OnChanged();

        String ID2 = ID.toString();

        if (ID2.length() > 5 && ID2.charAt(ID2.length() - 1) == ',')
            ID2 = ID2.substring(0, ID2.length() - 1);

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

                    if (Result.getInt("Message") == 0 && !Result.isNull("Result"))
                    {
                        JSONArray ResultList = new JSONArray(Result.getString("Result"));

                        for (int I = 0; I < ResultList.length(); I++)
                        {
                            String ID = ResultList.getJSONObject(I).getString("ID");

                            DB.InboxDelete(ID);

                            Iterator itr = PostList.iterator();

                            while (itr.hasNext())
                            {
                                PostStruct Post = (PostStruct) itr.next();

                                if (ID.equals(Post.ID))
                                    itr.remove();
                            }
                        }

                        OnChanged();
                    }
                }
                catch (Exception e)
                {
                    Misc.Debug("PostAdapter-Delete: " + e.toString());
                }
            }

            @Override public void onError(ANError e) { }
        });

        AndroidNetworking.post(Misc.GetRandomServer("PostListInbox"))
        .addBodyParameter("Skip", String.valueOf(Size))
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

                    if (Result.getInt("Message") == 0 && !Result.isNull("Result"))
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

                            int I = PostList.indexOf(P);

                            if (I != -1)
                                PostList.set(I, P);

                            DB.InboxUpdate(P);
                        }

                        OnChanged();
                    }
                }
                catch (Exception e)
                {
                    Misc.Debug("PostAdapter-Update: " + e.toString());
                }
            }

            @Override public void onError(ANError e) { }
        });
    }

    public void Insert(JSONObject D)
    {
        try
        {
            PostStruct P = new PostStruct();
            P.ID = D.getString("_id");
            P.Profile = SharedHandler.GetString("Avatar");
            P.Name = SharedHandler.GetString("Name");
            P.Medal = SharedHandler.GetString("Medal");
            P.Username = SharedHandler.GetString("Username");
            P.Time = D.getInt("Time");

            if (!D.isNull("Message"))
                P.Message = D.getString("Message");

            if (D.getInt("Type") != 0)
            {
                P.Type = D.getInt("Type");
                P.Data = D.getString("Data");
            }

            P.Owner = D.getString("Owner");
            P.Category = D.getInt("Category");
            P.LikeCount = 0;
            P.CommentCount = 0;

            PostList.add(P);
            DB.InboxUpdate(P);

            OnChanged();
        }
        catch (Exception e)
        {
            Misc.Debug("PostAdapter-Insert: " + e.toString());
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

    public class PostStruct
    {
        public String ID;
        String Profile;
        String Name;
        String Medal;
        String Username;
        public String Owner;
        int Time;
        String Message;
        int Type; // 0: Message 1: Image 2: Video 3: Vote 4: File
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

        int ViewType = 1; // 0: Pull 1: Post
        boolean IsDownloading = false;

        PostStruct() { }
        PostStruct(int v) { ViewType = v; }

        void RevLike() { IsLike = !IsLike; }

        void InsLike()
        {
            LikeCount++;
            DB.InboxLike(ID, true);
        }

        void DesLike()
        {
            LikeCount--;
            DB.InboxLike(ID, false);
        }

        public void InsComment()
        {
            CommentCount++;
            DB.InboxComment(ID, true);
        }

        public void DesComment()
        {
            CommentCount--;
            DB.InboxComment(ID, false);
        }
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
