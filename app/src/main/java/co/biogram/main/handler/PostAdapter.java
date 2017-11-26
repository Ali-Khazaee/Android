package co.biogram.main.handler;

import android.animation.ValueAnimator;
import android.content.Intent;
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
    private final int ID1_CHAT = Misc.GenerateViewID();

    // ViewType 2
    private final int ID2_LEVEL = Misc.GenerateViewID();
    private final int ID2_NUMBER = Misc.GenerateViewID();
    private final int ID2_RATING = Misc.GenerateViewID();
    private final int ID2_JOIN = Misc.GenerateViewID();
    private final int ID2_POPULAR = Misc.GenerateViewID();
    private final int ID2_SCORE = Misc.GenerateViewID();
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
        // ViewType 2
        CircleView CircleViewLevel;
        TextView TextViewNumber;
        LineView LineViewRating;
        CircleView CircleViewJoin;
        CircleView CircleViewPopular;
        CircleView CircleViewScore;
        CircleImageView CircleImageViewMedal;
        ImageView ImageViewClose;
        LinearLayout LinearLayoutMore;
        LinearLayout LinearLayoutMore2;

        ViewHolderMain(View v, int viewType)
        {
            super(v);

            if (viewType == 1)
            {

            }
            else if (viewType == 2)
            {
                CircleViewLevel = v.findViewById(ID2_LEVEL);
                TextViewNumber = v.findViewById(ID2_NUMBER);
                LineViewRating = v.findViewById(ID2_RATING);
                CircleViewJoin = v.findViewById(ID2_JOIN);
                CircleViewPopular = v.findViewById(ID2_POPULAR);
                CircleViewScore = v.findViewById(ID2_SCORE);
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



            RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(Activity, 1));
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
            LinearLayoutMoreParam.setMargins(0, Misc.ToDP(Activity, 10), 0, 0);
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
            CircleViewPopular.SetBitmap(R.drawable.ic_launcher);
            CircleViewPopular.InvalidateTextPaints();

            LinearLayoutMore.addView(CircleViewPopular);

            CircleView CircleViewScore = new CircleView(Activity);
            CircleViewScore.setLayoutParams(new LinearLayout.LayoutParams(0, Misc.ToDP(Activity, 60), 1));
            CircleViewScore.setId(ID2_SCORE);
            CircleViewScore.SetMessage("66K");
            CircleViewScore.SetMessageSize(16);
            CircleViewScore.SetMessageColor(R.color.White);
            CircleViewScore.SetStrokeColor(R.color.BlueLight);
            CircleViewScore.SetStrokeWidth(50);
            CircleViewScore.InvalidateTextPaints();

            LinearLayoutMore.addView(CircleViewScore);

            CircleImageView CircleImageViewMedal = new CircleImageView(Activity);
            CircleImageViewMedal.setLayoutParams(new LinearLayout.LayoutParams(0, Misc.ToDP(Activity, 60), 1));
            CircleImageViewMedal.setId(ID2_SCORE);
            CircleImageViewMedal.SetBorderColor(Misc.IsDark(Activity) ? R.color.White : R.color.Gray2);
            CircleImageViewMedal.SetBorderWidth(1);
            CircleImageViewMedal.SetWidthPadding();
            CircleImageViewMedal.setPadding(Misc.ToDP(Activity, 10), Misc.ToDP(Activity, 10), Misc.ToDP(Activity, 10), Misc.ToDP(Activity, 10));
            CircleImageViewMedal.setImageResource(R.drawable.ic_write_plus);

            LinearLayoutMore.addView(CircleImageViewMedal);

            RelativeLayout.LayoutParams LinearLayoutMore2Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 0);
            LinearLayoutMore2Param.addRule(RelativeLayout.BELOW, LinearLayoutMore.getId());

            final LinearLayout LinearLayoutMore2 = new LinearLayout(Activity);
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

            TextView TextViewScore = new TextView(Activity, 14, false);
            TextViewScore.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            TextViewScore.setText(Activity.getString(R.string.InboxUIScore));
            TextViewScore.setGravity(Gravity.CENTER);
            TextViewScore.setTextColor(Misc.IsDark(Activity) ? ContextCompat.getColor(Activity, R.color.White) : ContextCompat.getColor(Activity, R.color.Gray4));

            LinearLayoutMore2.addView(TextViewScore);

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

        if (Holder.getItemViewType() == 2)
        {
            Holder.ImageViewClose.setOnClickListener(new View.OnClickListener()
            {
                boolean IsMore = true;

                @Override
                public void onClick(View v)
                {
                    if (IsMore)
                    {
                        ValueAnimator Anim = ValueAnimator.ofInt(Holder.LinearLayoutMore.getMeasuredHeight(), Misc.ToDP(Activity, 100));
                        Anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
                        {
                            @Override
                            public void onAnimationUpdate(ValueAnimator va)
                            {
                                int Height = (int) va.getAnimatedValue();

                                ViewGroup.LayoutParams Param = Holder.LinearLayoutMore.getLayoutParams();
                                Param.height = Math.min(Height, Misc.ToDP(Activity, 80));

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
        int ViewType;

        public PostStruct(int viewType)
        {
            ViewType = viewType;
        }
    }

    public interface PullToRefreshListener
    {
        void OnRefresh();
    }
}
