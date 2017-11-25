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

public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private List<PostStruct> PostList = new ArrayList<>();
    private PullToRefreshView PullToRefreshViewMain;
    private final PullToRefreshListener Listener;
    private final FragmentActivity Activity;

    private final int ID_LEVEL = MiscHandler.GenerateViewID();
    private final int ID_NUMBER = MiscHandler.GenerateViewID();
    private final int ID_RATING = MiscHandler.GenerateViewID();
    private final int ID_JOIN = MiscHandler.GenerateViewID();
    private final int ID_POPULAR = MiscHandler.GenerateViewID();
    private final int ID_SCORE = MiscHandler.GenerateViewID();
    private final int ID_MEDAL = MiscHandler.GenerateViewID();
    private final int ID_CLOSE = MiscHandler.GenerateViewID();

    public PostAdapter(FragmentActivity a, List<PostStruct> p, PullToRefreshListener l)
    {
        Listener = l;
        Activity = a;
        PostList = p;
    }

    private class ViewHolderMain extends RecyclerView.ViewHolder
    {
        CircleView CircleViewLevel;
        TextView TextViewNumber;
        LineView LineViewRating;
        CircleView CircleViewJoin;
        CircleView CircleViewPopular;
        CircleView CircleViewScore;
        CircleImageView CircleImageViewMedal;
        ImageView ImageViewClose;

        ViewHolderMain(View v, int viewType)
        {
            super(v);

            if (viewType == 2)
            {
                CircleViewLevel = v.findViewById(ID_LEVEL);
                TextViewNumber = v.findViewById(ID_NUMBER);
                LineViewRating = v.findViewById(ID_RATING);
                CircleViewJoin = v.findViewById(ID_JOIN);
                CircleViewPopular = v.findViewById(ID_POPULAR);
                CircleViewScore = v.findViewById(ID_SCORE);
                CircleImageViewMedal = v.findViewById(ID_MEDAL);
                ImageViewClose = v.findViewById(ID_CLOSE);
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if (viewType == 0)
        {
            PullToRefreshViewMain = new PullToRefreshView(Activity);
            PullToRefreshViewMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            PullToRefreshViewMain.SetPullToRefreshListener(Listener);

            return new ViewHolderMain(PullToRefreshViewMain, viewType);
        }

        if (viewType == 2)
        {
            RelativeLayout RelativeLayoutInfo = new RelativeLayout(Activity);
            RelativeLayoutInfo.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

            View ViewLine = new View(Activity);
            ViewLine.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(Activity, 5)));
            ViewLine.setBackgroundResource(R.color.Gray);
            ViewLine.setId(MiscHandler.GenerateViewID());

            RelativeLayoutInfo.addView(ViewLine);

            RelativeLayout.LayoutParams ImageViewCloseParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(Activity, 48), MiscHandler.ToDimension(Activity, 48));
            ImageViewCloseParam.addRule(RelativeLayout.BELOW, ViewLine.getId());
            ImageViewCloseParam.addRule(MiscHandler.Align("L"));

            ImageView ImageViewClose = new ImageView(Activity);
            ImageViewClose.setLayoutParams(ImageViewCloseParam);
            ImageViewClose.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ImageViewClose.setImageResource(R.drawable.ic_close_gray);
            ImageViewClose.setId(ID_CLOSE);

            RelativeLayoutInfo.addView(ImageViewClose);

            RelativeLayout.LayoutParams TextViewLevelParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewLevelParam.addRule(RelativeLayout.BELOW, ViewLine.getId());
            TextViewLevelParam.setMargins(MiscHandler.ToDimension(Activity, 15), MiscHandler.ToDimension(Activity, 10), MiscHandler.ToDimension(Activity, 15), MiscHandler.ToDimension(Activity, 10));
            TextViewLevelParam.addRule(MiscHandler.Align("R"));

            TextView TextViewLevel = new TextView(Activity, 14, true);
            TextViewLevel.setLayoutParams(TextViewLevelParam);
            TextViewLevel.setText(Activity.getString(R.string.InboxUIUserLevel));
            TextViewLevel.setTextColor(ContextCompat.getColor(Activity, R.color.Gray4));
            TextViewLevel.setId(MiscHandler.GenerateViewID());

            RelativeLayoutInfo.addView(TextViewLevel);

            RelativeLayout.LayoutParams CircleViewLevelParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(Activity, 75), MiscHandler.ToDimension(Activity, 75));
            CircleViewLevelParam.addRule(RelativeLayout.BELOW, TextViewLevel.getId());
            CircleViewLevelParam.setMargins(MiscHandler.ToDimension(Activity, 15), MiscHandler.ToDimension(Activity, 10), MiscHandler.ToDimension(Activity, 15), 0);
            CircleViewLevelParam.addRule(MiscHandler.Align("R"));

            CircleView CircleViewLevel = new CircleView(Activity);
            CircleViewLevel.setLayoutParams(CircleViewLevelParam);
            CircleViewLevel.setId(ID_LEVEL);
            CircleViewLevel.SetMessage("1");
            CircleViewLevel.SetMessageSize(35);
            CircleViewLevel.SetMessageColor(R.color.BlueLight);
            CircleViewLevel.SetStrokeColor(R.color.Gray2);
            CircleViewLevel.SetStrokeWidth(1);
            CircleViewLevel.SetProgressColor(R.color.BlueLight);
            CircleViewLevel.SetProgressWidth(4);
            CircleViewLevel.InvalidateTextPaints();

            RelativeLayoutInfo.addView(CircleViewLevel);

            RelativeLayout.LayoutParams RelativeLayoutRatingParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            RelativeLayoutRatingParam.setMargins(MiscHandler.ToDimension(Activity, MiscHandler.IsRTL() ? 15 : 5), MiscHandler.ToDimension(Activity, 5), MiscHandler.ToDimension(Activity, !MiscHandler.IsRTL() ? 15 : 5), 0);
            RelativeLayoutRatingParam.addRule(MiscHandler.AlignTo("R"), CircleViewLevel.getId());
            RelativeLayoutRatingParam.addRule(RelativeLayout.BELOW, TextViewLevel.getId());

            RelativeLayout RelativeLayoutRating = new RelativeLayout(Activity);
            RelativeLayoutRating.setLayoutParams(RelativeLayoutRatingParam);
            RelativeLayoutRating.setId(MiscHandler.GenerateViewID());

            RelativeLayoutInfo.addView(RelativeLayoutRating);

            RelativeLayout.LayoutParams TextViewRatingParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewRatingParam.addRule(MiscHandler.Align("R"));

            TextView TextViewRating = new TextView(Activity, 14, false);
            TextViewRating.setLayoutParams(TextViewRatingParam);
            TextViewRating.setText(Activity.getString(R.string.InboxUIRating));
            TextViewRating.setTextColor(ContextCompat.getColor(Activity, R.color.BlueLight));

            RelativeLayoutRating.addView(TextViewRating);

            RelativeLayout.LayoutParams TextViewNumberParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewNumberParam.setMargins(0, MiscHandler.ToDimension(Activity, 2), 0, 0);
            TextViewNumberParam.addRule(MiscHandler.Align("L"));

            TextView TextViewNumber = new TextView(Activity, 12, false);
            TextViewNumber.setLayoutParams(TextViewNumberParam);
            TextViewNumber.setText("10/60");
            TextViewNumber.setId(ID_NUMBER);
            TextViewNumber.setTextColor(ContextCompat.getColor(Activity, R.color.Gray4));

            RelativeLayoutRating.addView(TextViewNumber);

            RelativeLayout.LayoutParams LineViewRatingParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(Activity, 10));
            LineViewRatingParam.setMargins(0, MiscHandler.ToDimension(Activity, 3), 0, 0);
            LineViewRatingParam.addRule(MiscHandler.AlignTo("R"), CircleViewLevel.getId());
            LineViewRatingParam.addRule(RelativeLayout.BELOW, TextViewNumber.getId());

            LineView LineViewRating = new LineView(Activity);
            LineViewRating.setLayoutParams(LineViewRatingParam);
            LineViewRating.setId(ID_RATING);
            LineViewRating.SetStrokeColor(R.color.Gray2);
            LineViewRating.SetStrokeWidth(7);
            LineViewRating.SetProgressColor(R.color.BlueLight);
            LineViewRating.SetProgressPercent(33);
            LineViewRating.InvalidateTextPaints();

            RelativeLayoutRating.addView(LineViewRating);

            RelativeLayout.LayoutParams TextViewInfoParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewInfoParam.addRule(RelativeLayout.BELOW, LineViewRating.getId());
            TextViewInfoParam.addRule(MiscHandler.Align("L"));

            TextView TextViewInfo = new TextView(Activity, 12, false);
            TextViewInfo.setLayoutParams(TextViewInfoParam);
            TextViewInfo.setLineSpacing(1.0f, 0.7f);
            TextViewInfo.setText((Activity.getString(R.string.InboxUIInfo) + " " + Activity.getString(R.string.InboxUIInfo2)), TextView.BufferType.SPANNABLE);
            TextViewInfo.setId(MiscHandler.GenerateViewID());
            TextViewInfo.setTextColor(ContextCompat.getColor(Activity, R.color.Gray4));

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
            LinearLayoutMoreParam.setMargins(0, MiscHandler.ToDimension(Activity, 10), 0, 0);
            LinearLayoutMoreParam.addRule(RelativeLayout.BELOW, RelativeLayoutRating.getId());

            final LinearLayout LinearLayoutMore = new LinearLayout(Activity);
            LinearLayoutMore.setLayoutParams(LinearLayoutMoreParam);
            LinearLayoutMore.setId(MiscHandler.GenerateViewID());

            RelativeLayoutInfo.addView(LinearLayoutMore);

            CircleView CircleViewJoin = new CircleView(Activity);
            CircleViewJoin.setLayoutParams(new LinearLayout.LayoutParams(0, MiscHandler.ToDimension(Activity, 60), 1));
            CircleViewJoin.setId(ID_JOIN);
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
            CircleViewPopular.setLayoutParams(new LinearLayout.LayoutParams(0, MiscHandler.ToDimension(Activity, 60), 1));
            CircleViewPopular.setId(ID_POPULAR);
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
            CircleViewScore.setLayoutParams(new LinearLayout.LayoutParams(0, MiscHandler.ToDimension(Activity, 60), 1));
            CircleViewScore.setId(ID_SCORE);
            CircleViewScore.SetMessage("66K");
            CircleViewScore.SetMessageSize(16);
            CircleViewScore.SetMessageColor(R.color.White);
            CircleViewScore.SetStrokeColor(R.color.BlueLight);
            CircleViewScore.SetStrokeWidth(50);
            CircleViewScore.InvalidateTextPaints();

            LinearLayoutMore.addView(CircleViewScore);

            CircleImageView CircleImageViewMedal = new CircleImageView(Activity);
            CircleImageViewMedal.setLayoutParams(new LinearLayout.LayoutParams(0, MiscHandler.ToDimension(Activity, 60), 1));
            CircleImageViewMedal.setId(ID_SCORE);
            CircleImageViewMedal.SetBorderColor(R.color.Gray2);
            CircleImageViewMedal.SetBorderWidth(1);
            CircleImageViewMedal.SetWidthPadding();
            CircleImageViewMedal.setPadding(MiscHandler.ToDimension(Activity, 10), MiscHandler.ToDimension(Activity, 10), MiscHandler.ToDimension(Activity, 10), MiscHandler.ToDimension(Activity, 10));
            CircleImageViewMedal.setImageResource(R.drawable.ic_write_plus);

            LinearLayoutMore.addView(CircleImageViewMedal);

            RelativeLayout.LayoutParams LinearLayoutMore2Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 0);
            LinearLayoutMore2Param.addRule(RelativeLayout.BELOW, LinearLayoutMore.getId());

            final LinearLayout LinearLayoutMore2 = new LinearLayout(Activity);
            LinearLayoutMore2.setLayoutParams(LinearLayoutMore2Param);
            LinearLayoutMore2.setId(MiscHandler.GenerateViewID());

            RelativeLayoutInfo.addView(LinearLayoutMore2);

            TextView TextViewJoin = new TextView(Activity, 14, false);
            TextViewJoin.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            TextViewJoin.setText(Activity.getString(R.string.InboxUIJoin));
            TextViewJoin.setGravity(Gravity.CENTER);
            TextViewJoin.setTextColor(ContextCompat.getColor(Activity, R.color.Gray4));

            LinearLayoutMore2.addView(TextViewJoin);

            TextView TextViewPopular = new TextView(Activity, 14, false);
            TextViewPopular.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            TextViewPopular.setText(Activity.getString(R.string.InboxUIPopular));
            TextViewPopular.setGravity(Gravity.CENTER);
            TextViewPopular.setTextColor(ContextCompat.getColor(Activity, R.color.Gray4));

            LinearLayoutMore2.addView(TextViewPopular);

            TextView TextViewScore = new TextView(Activity, 14, false);
            TextViewScore.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            TextViewScore.setText(Activity.getString(R.string.InboxUIScore));
            TextViewScore.setGravity(Gravity.CENTER);
            TextViewScore.setTextColor(ContextCompat.getColor(Activity, R.color.Gray4));

            LinearLayoutMore2.addView(TextViewScore);

            TextView TextViewMedal = new TextView(Activity, 14, false);
            TextViewMedal.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            TextViewMedal.setText(Activity.getString(R.string.InboxUIMedal));
            TextViewMedal.setGravity(Gravity.CENTER);
            TextViewMedal.setTextColor(ContextCompat.getColor(Activity, R.color.Gray4));

            LinearLayoutMore2.addView(TextViewMedal);

            RelativeLayout.LayoutParams CircleImageViewMoreParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(Activity, 40), MiscHandler.ToDimension(Activity, 40));
            CircleImageViewMoreParam.setMargins(MiscHandler.ToDimension(Activity, 15), -MiscHandler.ToDimension(Activity, 10), MiscHandler.ToDimension(Activity, 15), 0);
            CircleImageViewMoreParam.addRule(RelativeLayout.BELOW, LinearLayoutMore2.getId());
            CircleImageViewMoreParam.addRule(MiscHandler.Align("L"));

            CircleImageView CircleImageViewMore = new CircleImageView(Activity);
            CircleImageViewMore.setLayoutParams(CircleImageViewMoreParam);
            CircleImageViewMore.setId(MiscHandler.GenerateViewID());
            CircleImageViewMore.setImageResource(R.drawable.ic_person_blue);
            CircleImageViewMore.SetBorderColor(R.color.Gray);
            CircleImageViewMore.SetBorderWidth(2);
            CircleImageViewMore.setPadding(MiscHandler.ToDimension(Activity, 2), MiscHandler.ToDimension(Activity, 2), MiscHandler.ToDimension(Activity, 2), MiscHandler.ToDimension(Activity, 2));
            CircleImageViewMore.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    v.setVisibility(View.GONE);

                    ValueAnimator Anim = ValueAnimator.ofInt(LinearLayoutMore.getMeasuredHeight(), MiscHandler.ToDimension(Activity, 100));
                    Anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
                    {
                        @Override
                        public void onAnimationUpdate(ValueAnimator va)
                        {
                            int Height = (int) va.getAnimatedValue();

                            ViewGroup.LayoutParams Param = LinearLayoutMore.getLayoutParams();
                            Param.height = Math.min(Height, MiscHandler.ToDimension(Activity, 80));

                            LinearLayoutMore.setLayoutParams(Param);

                            ViewGroup.LayoutParams Param2 = LinearLayoutMore2.getLayoutParams();
                            Param2.height = Math.min(Height, MiscHandler.ToDimension(Activity, 20));

                            LinearLayoutMore2.setLayoutParams(Param2);
                        }
                    });
                    Anim.setDuration(300);
                    Anim.start();
                }
            });

            RelativeLayoutInfo.addView(CircleImageViewMore);

            RelativeLayout.LayoutParams ViewLine2Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(Activity, 5));
            ViewLine2Param.setMargins(0, MiscHandler.ToDimension(Activity, 9), 0, 0);
            ViewLine2Param.addRule(RelativeLayout.BELOW, LinearLayoutMore2.getId());

            View ViewLine2 = new View(Activity);
            ViewLine2.setLayoutParams(ViewLine2Param);
            ViewLine2.setBackgroundResource(R.color.Gray);

            RelativeLayoutInfo.addView(ViewLine2);
            CircleImageViewMore.bringToFront();

            return new ViewHolderMain(RelativeLayoutInfo, viewType);
        }

        return new ViewHolderMain(new View(Activity), viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {

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
