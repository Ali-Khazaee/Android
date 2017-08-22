package co.biogram.main.fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.Bidi;

import co.biogram.main.R;
import co.biogram.main.handler.MiscHandler;
import co.biogram.main.handler.SharedHandler;
import co.biogram.main.handler.TagHandler;
import co.biogram.main.misc.LoadingView;
import co.biogram.main.misc.ImageViewCircle;
import co.biogram.main.misc.TextCrawler;

public class PostFragment extends Fragment
{
    private ImageViewCircle ImageViewCircleProfile;
    private TextView TextViewUsername;
    private TextView TextViewTime;
    private TextView TextViewMessage;
    private LinearLayout LinearLayoutContentSingle;
    private ImageView ImageViewSingle;
    private LinearLayout LinearLayoutContentDouble;
    private ImageView ImageViewDouble1;
    private ImageView ImageViewDouble2;
    private LinearLayout LinearLayoutContentTriple;
    private ImageView ImageViewTriple1;
    private ImageView ImageViewTriple2;
    private ImageView ImageViewTriple3;
    private RelativeLayout RelativeLayoutVideo;
    private ImageView ImageViewVideo;
    private RelativeLayout RelativeLayoutContentLink;
    private LoadingView LoadingViewLink;
    private TextView TextViewTryLink;
    private TextView TextViewWebsiteLink;
    private TextView TextViewDescriptionLink;
    private ImageView ImageViewFavLink;
    private TextView TextViewLikeCount;
    private TextView TextViewCommentCount;

    private ImageView ImageViewOption;
    private ImageView ImageViewBookMark;
    private ImageView ImageViewLike;

    private String OwnerID = "";
    private final String Username = "";
    private boolean IsLike = false;
    private boolean IsComment = false;
    private boolean IsBookmark = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final String PostID;
        final Context context = getActivity();

        if (getArguments() != null)
            PostID = getArguments().getString("PostID", "");
        else
            PostID = "";

        RelativeLayout RelativeLayoutMain = new RelativeLayout(context);
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayoutMain.setBackgroundResource(R.color.White);
        RelativeLayoutMain.setClickable(true);

        RelativeLayout RelativeLayoutHeader = new RelativeLayout(context);
        RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56)));
        RelativeLayoutHeader.setBackgroundResource(R.color.White5);
        RelativeLayoutHeader.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain.addView(RelativeLayoutHeader);

        ImageView ImageViewBack = new ImageView(context);
        ImageViewBack.setPadding(MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12));
        ImageViewBack.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewBack.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), MiscHandler.ToDimension(context, 56)));
        ImageViewBack.setImageResource(R.drawable.ic_back_blue);
        ImageViewBack.setId(MiscHandler.GenerateViewID());
        ImageViewBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                getActivity().onBackPressed();
            }
        });

        RelativeLayoutHeader.addView(ImageViewBack);

        RelativeLayout.LayoutParams TextViewTitleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTitleParam.addRule(RelativeLayout.RIGHT_OF, ImageViewBack.getId());
        TextViewTitleParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        TextView TextViewTitle = new TextView(context);
        TextViewTitle.setLayoutParams(TextViewTitleParam);
        TextViewTitle.setTextColor(ContextCompat.getColor(context, R.color.Black));
        TextViewTitle.setText(getString(R.string.PostFragment));
        TextViewTitle.setTypeface(null, Typeface.BOLD);
        TextViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

        RelativeLayoutHeader.addView(TextViewTitle);

        RelativeLayout.LayoutParams ImageViewOptionParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), MiscHandler.ToDimension(context, 56));
        ImageViewOptionParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        ImageViewOption = new ImageView(context);
        ImageViewOption.setPadding(MiscHandler.ToDimension(context, 14), MiscHandler.ToDimension(context, 14), MiscHandler.ToDimension(context, 14), MiscHandler.ToDimension(context, 14));
        ImageViewOption.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewOption.setLayoutParams(ImageViewOptionParam);
        ImageViewOption.setImageResource(R.drawable.ic_option_black);
        ImageViewOption.setVisibility(View.GONE);
        ImageViewOption.setId(MiscHandler.GenerateViewID());
        ImageViewOption.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                final Dialog DialogOption = new Dialog(getActivity());
                DialogOption.requestWindowFeature(Window.FEATURE_NO_TITLE);
                DialogOption.setCancelable(true);

                LinearLayout LinearLayoutMain = new LinearLayout(context);
                LinearLayoutMain.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                LinearLayoutMain.setBackgroundResource(R.color.White);
                LinearLayoutMain.setOrientation(LinearLayout.VERTICAL);

                final TextView TextViewFollow = new TextView(context);
                TextViewFollow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TextViewFollow.setTextColor(ContextCompat.getColor(context, R.color.Black));
                TextViewFollow.setText(getString(R.string.PostFragmentFollow));
                TextViewFollow.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
                TextViewFollow.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                TextViewFollow.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        AndroidNetworking.post(MiscHandler.GetRandomServer("Follow"))
                        .addBodyParameter("Username", Username)
                        .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
                        .setTag("PostFragment")
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
                                            TextViewFollow.setText(getString(R.string.PostFragmentUnfollow));
                                        else
                                            TextViewFollow.setText(getString(R.string.PostFragmentFollow));

                                        MiscHandler.Toast(context, getString(R.string.PostFragmentFollowRequest));
                                    }
                                }
                                catch (Exception e)
                                {
                                    MiscHandler.Debug("PostFragment-RequestFollow: " + e.toString());
                                }
                            }

                            @Override
                            public void onError(ANError anError) { }
                        });

                        DialogOption.dismiss();
                    }
                });

                LinearLayoutMain.addView(TextViewFollow);

                View ViewFollowLine = new View(context);
                ViewFollowLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1)));
                ViewFollowLine.setBackgroundResource(R.color.Gray1);

                LinearLayoutMain.addView(ViewFollowLine);

                final TextView TextViewTurn = new TextView(context);
                TextViewTurn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TextViewTurn.setTextColor(ContextCompat.getColor(context, R.color.Black));
                TextViewTurn.setVisibility(View.GONE);
                TextViewTurn.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
                TextViewTurn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                TextViewTurn.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        AndroidNetworking.post(MiscHandler.GetRandomServer("PostTurnComment"))
                        .addBodyParameter("PostID", PostID)
                        .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
                        .setTag("PostFragment")
                        .build()
                        .getAsString(new StringRequestListener()
                        {
                            @Override
                            public void onResponse(String Response)
                            {
                                try
                                {
                                    if (new JSONObject(Response).getInt("Message") == 1000)
                                        IsComment = !IsComment;
                                }
                                catch (Exception e)
                                {
                                    MiscHandler.Debug("PostFragment-RequestTurn: " + e.toString());
                                }
                            }

                            @Override
                            public void onError(ANError anError) { }
                        });

                        DialogOption.dismiss();
                    }
                });

                if (IsComment)
                    TextViewTurn.setText(getString(R.string.PostFragmentTurnOn));
                else
                    TextViewTurn.setText(getString(R.string.PostFragmentTurnOff));

                LinearLayoutMain.addView(TextViewTurn);

                View ViewTurnLine = new View(context);
                ViewTurnLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1)));
                ViewTurnLine.setBackgroundResource(R.color.Gray1);
                ViewTurnLine.setVisibility(View.GONE);

                LinearLayoutMain.addView(ViewTurnLine);

                TextView TextViewCopy = new TextView(context);
                TextViewCopy.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TextViewCopy.setTextColor(ContextCompat.getColor(context, R.color.Black));
                TextViewCopy.setText(getString(R.string.PostFragmentCopy));
                TextViewCopy.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
                TextViewCopy.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                TextViewCopy.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        ClipboardManager ClipBoard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData Clip = ClipData.newPlainText(PostID, TextViewMessage.getText().toString());
                        ClipBoard.setPrimaryClip(Clip);

                        MiscHandler.Toast(context, getString(R.string.PostFragmentClipboard));
                        DialogOption.dismiss();
                    }
                });

                LinearLayoutMain.addView(TextViewCopy);

                View ViewCopyLine = new View(context);
                ViewCopyLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1)));
                ViewCopyLine.setBackgroundResource(R.color.Gray1);

                LinearLayoutMain.addView(ViewCopyLine);

                TextView TextViewBlock = new TextView(context);
                TextViewBlock.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TextViewBlock.setTextColor(ContextCompat.getColor(context, R.color.Black));
                TextViewBlock.setText(getString(R.string.PostFragmentBlock));
                TextViewBlock.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
                TextViewBlock.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                TextViewBlock.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        DialogOption.dismiss();
                        MiscHandler.Toast(context, getString(R.string.Soon));
                    }
                });

                LinearLayoutMain.addView(TextViewBlock);

                View ViewBlockLine = new View(context);
                ViewBlockLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1)));
                ViewBlockLine.setBackgroundResource(R.color.Gray1);

                LinearLayoutMain.addView(ViewBlockLine);

                TextView TextViewDelete = new TextView(context);
                TextViewDelete.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TextViewDelete.setTextColor(ContextCompat.getColor(context, R.color.Black));
                TextViewDelete.setText(getString(R.string.PostFragmentDelete));
                TextViewDelete.setVisibility(View.GONE);
                TextViewDelete.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
                TextViewDelete.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                TextViewDelete.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        AndroidNetworking.post(MiscHandler.GetRandomServer("PostDelete"))
                        .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
                        .addBodyParameter("PostID", PostID)
                        .setTag("PostFragment")
                        .build()
                        .getAsString(new StringRequestListener()
                        {
                            @Override
                            public void onResponse(String Response)
                            {
                                try
                                {
                                    if (new JSONObject(Response).getInt("Message") == 1000)
                                        getActivity().onBackPressed();
                                }
                                catch (Exception e)
                                {
                                    MiscHandler.Debug("PostFragment-RequestDelete: " + e.toString());
                                }
                            }

                            @Override
                            public void onError(ANError anError) { }
                        });

                        DialogOption.dismiss();
                    }
                });

                LinearLayoutMain.addView(TextViewDelete);

                View ViewDeleteLine = new View(context);
                ViewDeleteLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1)));
                ViewDeleteLine.setBackgroundResource(R.color.Gray1);
                ViewDeleteLine.setVisibility(View.GONE);

                LinearLayoutMain.addView(ViewDeleteLine);

                TextView TextViewReport = new TextView(context);
                TextViewReport.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TextViewReport.setTextColor(ContextCompat.getColor(context, R.color.Black));
                TextViewReport.setText(getString(R.string.PostFragmentReport));
                TextViewReport.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
                TextViewReport.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                TextViewReport.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        AndroidNetworking.post(MiscHandler.GetRandomServer("PostReport"))
                        .addBodyParameter("PostID", PostID)
                        .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
                        .setTag("PostFragment")
                        .build()
                        .getAsString(new StringRequestListener()
                        {
                            @Override
                            public void onResponse(String Response)
                            {
                                try
                                {
                                    if (new JSONObject(Response).getInt("Message") == 1000)
                                        MiscHandler.Toast(context, getString(R.string.PostFragmentReportSent));
                                }
                                catch (Exception e)
                                {
                                    MiscHandler.Debug("PostFragment-RequestReport: " + e.toString());
                                }
                            }

                            @Override
                            public void onError(ANError anError) { }
                        });

                        DialogOption.dismiss();
                    }
                });

                LinearLayoutMain.addView(TextViewReport);

                if (OwnerID.equals(SharedHandler.GetString(context, "ID")))
                {
                    TextViewFollow.setVisibility(View.GONE);
                    ViewFollowLine.setVisibility(View.GONE);

                    TextViewTurn.setVisibility(View.VISIBLE);
                    ViewTurnLine.setVisibility(View.VISIBLE);

                    TextViewBlock.setVisibility(View.GONE);
                    ViewBlockLine.setVisibility(View.GONE);

                    TextViewDelete.setVisibility(View.VISIBLE);
                    ViewDeleteLine.setVisibility(View.VISIBLE);

                    TextViewReport.setVisibility(View.GONE);
                }

                DialogOption.setContentView(LinearLayoutMain);
                DialogOption.show();
            }
        });

        RelativeLayoutHeader.addView(ImageViewOption);

        RelativeLayout.LayoutParams BookMarkParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), MiscHandler.ToDimension(context, 56));
        BookMarkParam.addRule(RelativeLayout.LEFT_OF, ImageViewOption.getId());

        ImageViewBookMark = new ImageView(context);
        ImageViewBookMark.setPadding(MiscHandler.ToDimension(context, 16), MiscHandler.ToDimension(context, 16), MiscHandler.ToDimension(context, 16), MiscHandler.ToDimension(context, 16));
        ImageViewBookMark.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewBookMark.setLayoutParams(BookMarkParam);
        ImageViewBookMark.setImageResource(R.drawable.ic_bookmark_black);
        ImageViewBookMark.setVisibility(View.GONE);
        ImageViewBookMark.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (IsBookmark)
                {
                    ImageViewBookMark.setImageResource(R.drawable.ic_bookmark_black);

                    ObjectAnimator Fade = ObjectAnimator.ofFloat(ImageViewBookMark, "alpha",  0.1f, 1f);
                    Fade.setDuration(400);

                    AnimatorSet AnimationSet = new AnimatorSet();
                    AnimationSet.play(Fade);
                    AnimationSet.start();

                    IsBookmark = false;
                }
                else
                {
                    ImageViewBookMark.setImageResource(R.drawable.ic_bookmark_black2);

                    ObjectAnimator SizeX = ObjectAnimator.ofFloat(ImageViewBookMark, "scaleX", 1.5f);
                    SizeX.setDuration(200);

                    ObjectAnimator SizeY = ObjectAnimator.ofFloat(ImageViewBookMark, "scaleY", 1.5f);
                    SizeY.setDuration(200);

                    ObjectAnimator Fade = ObjectAnimator.ofFloat(ImageViewBookMark, "alpha",  0.1f, 1f);
                    Fade.setDuration(400);

                    ObjectAnimator SizeX2 = ObjectAnimator.ofFloat(ImageViewBookMark, "scaleX", 1f);
                    SizeX2.setDuration(200);
                    SizeX2.setStartDelay(200);

                    ObjectAnimator SizeY2 = ObjectAnimator.ofFloat(ImageViewBookMark, "scaleY", 1f);
                    SizeY2.setDuration(200);
                    SizeY2.setStartDelay(200);

                    AnimatorSet AnimationSet = new AnimatorSet();
                    AnimationSet.playTogether(SizeX, SizeY, Fade, SizeX2, SizeY2);
                    AnimationSet.start();

                    IsBookmark = true;
                }

                AndroidNetworking.post(MiscHandler.GetRandomServer("PostBookmark"))
                .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
                .addBodyParameter("PostID", PostID)
                .setTag("PostFragment")
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
                                if (Result.getBoolean("Bookmark"))
                                    ImageViewBookMark.setImageResource(R.drawable.ic_bookmark_black2);
                                else
                                    ImageViewBookMark.setImageResource(R.drawable.ic_bookmark_black);
                            }
                        }
                        catch (Exception e)
                        {
                            MiscHandler.Debug("PostFragment-RequestBookmark: " + e.toString());
                        }
                    }

                    @Override
                    public void onError(ANError anError) { }
                });
            }
        });

        RelativeLayoutHeader.addView(ImageViewBookMark);

        RelativeLayout.LayoutParams LineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1));
        LineParam.addRule(RelativeLayout.BELOW, RelativeLayoutHeader.getId());

        View ViewLine = new View(context);
        ViewLine.setLayoutParams(LineParam);
        ViewLine.setBackgroundResource(R.color.Gray2);
        ViewLine.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain.addView(ViewLine);

        RelativeLayout.LayoutParams ScrollParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        ScrollParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

        ScrollView ScrollViewMain = new ScrollView(context);
        ScrollViewMain.setLayoutParams(ScrollParam);
        ScrollViewMain.setFillViewport(true);

        RelativeLayoutMain.addView(ScrollViewMain);

        RelativeLayout RelativeLayoutMain2 = new RelativeLayout(context);
        RelativeLayoutMain2.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

        ScrollViewMain.addView(RelativeLayoutMain2);

        RelativeLayout.LayoutParams ImageViewCircleProfileParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 55), MiscHandler.ToDimension(context, 55));
        ImageViewCircleProfileParam.setMargins(MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 10));

        ImageViewCircleProfile = new ImageViewCircle(context);
        ImageViewCircleProfile.setLayoutParams(ImageViewCircleProfileParam);
        ImageViewCircleProfile.setImageResource(R.color.BlueGray);
        ImageViewCircleProfile.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain2.addView(ImageViewCircleProfile);

        RelativeLayout.LayoutParams TextViewUsernameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewUsernameParam.addRule(RelativeLayout.RIGHT_OF, ImageViewCircleProfile.getId());
        TextViewUsernameParam.setMargins(0, MiscHandler.ToDimension(context, 14), 0, 0);

        TextViewUsername = new TextView(context);
        TextViewUsername.setLayoutParams(TextViewUsernameParam);
        TextViewUsername.setTextColor(ContextCompat.getColor(context, R.color.Black));
        TextViewUsername.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        TextViewUsername.setId(MiscHandler.GenerateViewID());
        TextViewUsername.setTypeface(null, Typeface.BOLD);

        RelativeLayoutMain2.addView(TextViewUsername);

        RelativeLayout.LayoutParams TextViewTimeParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTimeParam.addRule(RelativeLayout.BELOW, TextViewUsername.getId());
        TextViewTimeParam.addRule(RelativeLayout.RIGHT_OF, ImageViewCircleProfile.getId());

        TextViewTime = new TextView(context);
        TextViewTime.setLayoutParams(TextViewTimeParam);
        TextViewTime.setTextColor(ContextCompat.getColor(context, R.color.Gray4));
        TextViewTime.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        RelativeLayoutMain2.addView(TextViewTime);

        RelativeLayout.LayoutParams TextViewMessageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewMessageParam.addRule(RelativeLayout.BELOW, ImageViewCircleProfile.getId());
        TextViewMessageParam.setMargins(MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 5), MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 5));

        TextViewMessage = new TextView(context);
        TextViewMessage.setLayoutParams(TextViewMessageParam);
        TextViewMessage.setTextColor(ContextCompat.getColor(context, R.color.Black));
        TextViewMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        TextViewMessage.setId(MiscHandler.GenerateViewID());
        TextViewMessage.setLineSpacing(1f, 1.25f);
        TextViewMessage.setVisibility(View.GONE);

        RelativeLayoutMain2.addView(TextViewMessage);

        RelativeLayout.LayoutParams RelativeLayoutContentParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayoutContentParam.setMargins(MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 10), 0);
        RelativeLayoutContentParam.addRule(RelativeLayout.BELOW, TextViewMessage.getId());

        RelativeLayout RelativeLayoutContent = new RelativeLayout(context);
        RelativeLayoutContent.setLayoutParams(RelativeLayoutContentParam);
        RelativeLayoutContent.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain2.addView(RelativeLayoutContent);

        LinearLayoutContentSingle = new LinearLayout(context);
        LinearLayoutContentSingle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        LinearLayoutContentSingle.setVisibility(View.GONE);

        RelativeLayoutContent.addView(LinearLayoutContentSingle);

        ImageViewSingle = new ImageView(context);
        ImageViewSingle.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 200)));
        ImageViewSingle.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ImageViewSingle.setBackgroundResource(R.color.BlueGray);

        LinearLayoutContentSingle.addView(ImageViewSingle);

        LinearLayoutContentDouble = new LinearLayout(context);
        LinearLayoutContentDouble.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        LinearLayoutContentDouble.setVisibility(View.GONE);

        RelativeLayoutContent.addView(LinearLayoutContentDouble);

        ImageViewDouble1 = new ImageView(context);
        ImageViewDouble1.setLayoutParams(new LinearLayout.LayoutParams(0, MiscHandler.ToDimension(context, 200), 1f));
        ImageViewDouble1.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ImageViewDouble1.setBackgroundResource(R.color.BlueGray);

        LinearLayoutContentDouble.addView(ImageViewDouble1);

        View ViewLineDouble = new View(context);
        ViewLineDouble.setLayoutParams(new LinearLayout.LayoutParams(0, RelativeLayout.LayoutParams.MATCH_PARENT, 0.05f));

        LinearLayoutContentDouble.addView(ViewLineDouble);

        ImageViewDouble2 = new ImageView(context);
        ImageViewDouble2.setLayoutParams(new LinearLayout.LayoutParams(0, MiscHandler.ToDimension(context, 200), 1f));
        ImageViewDouble2.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ImageViewDouble2.setBackgroundResource(R.color.BlueGray);

        LinearLayoutContentDouble.addView(ImageViewDouble2);

        LinearLayoutContentTriple = new LinearLayout(context);
        LinearLayoutContentTriple.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        LinearLayoutContentTriple.setVisibility(View.GONE);

        RelativeLayoutContent.addView(LinearLayoutContentTriple);

        ImageViewTriple1 = new ImageView(context);
        ImageViewTriple1.setLayoutParams(new LinearLayout.LayoutParams(0, MiscHandler.ToDimension(context, 200), 1f));
        ImageViewTriple1.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ImageViewTriple1.setBackgroundResource(R.color.BlueGray);

        LinearLayoutContentTriple.addView(ImageViewTriple1);

        View ViewLineTriple = new View(context);
        ViewLineTriple.setLayoutParams(new LinearLayout.LayoutParams(0, RelativeLayout.LayoutParams.MATCH_PARENT, 0.05f));

        LinearLayoutContentTriple.addView(ViewLineTriple);

        RelativeLayout RelativeLayoutTripleLayout = new RelativeLayout(context);
        RelativeLayoutTripleLayout.setLayoutParams(new LinearLayout.LayoutParams(0, MiscHandler.ToDimension(context, 200), 1f));

        LinearLayoutContentTriple.addView(RelativeLayoutTripleLayout);

        ImageViewTriple2 = new ImageView(context);
        ImageViewTriple2.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 97)));
        ImageViewTriple2.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ImageViewTriple2.setBackgroundResource(R.color.BlueGray);
        ImageViewTriple2.setId(MiscHandler.GenerateViewID());

        RelativeLayoutTripleLayout.addView(ImageViewTriple2);

        RelativeLayout.LayoutParams ViewLineTriple2Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 6));
        ViewLineTriple2Param.addRule(RelativeLayout.BELOW, ImageViewTriple2.getId());

        View ViewLineTriple2 = new View(context);
        ViewLineTriple2.setLayoutParams(ViewLineTriple2Param);
        ViewLineTriple2.setId(MiscHandler.GenerateViewID());

        RelativeLayoutTripleLayout.addView(ViewLineTriple2);

        RelativeLayout.LayoutParams ImageViewTriple3Triple3Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 97));
        ImageViewTriple3Triple3Param.addRule(RelativeLayout.BELOW, ViewLineTriple2.getId());

        ImageViewTriple3 = new ImageView(context);
        ImageViewTriple3.setLayoutParams(ImageViewTriple3Triple3Param);
        ImageViewTriple3.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ImageViewTriple3.setBackgroundResource(R.color.BlueGray);

        RelativeLayoutTripleLayout.addView(ImageViewTriple3);

        RelativeLayoutVideo = new RelativeLayout(context);
        RelativeLayoutVideo.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 180)));
        RelativeLayoutVideo.setBackgroundResource(R.color.Black);
        RelativeLayoutVideo.setVisibility(View.GONE);

        RelativeLayoutContent.addView(RelativeLayoutVideo);

        ImageViewVideo = new ImageView(context);
        ImageViewVideo.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        ImageViewVideo.setScaleType(ImageView.ScaleType.CENTER_CROP);

        RelativeLayoutVideo.addView(ImageViewVideo);

        RelativeLayout.LayoutParams ImageViewPlayParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 50), MiscHandler.ToDimension(context, 50));
        ImageViewPlayParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        ImageView ImageViewPlay = new ImageView(context);
        ImageViewPlay.setLayoutParams(ImageViewPlayParam);
        ImageViewPlay.setScaleType(ImageView.ScaleType.FIT_XY);
        ImageViewPlay.setImageResource(R.drawable.ic_play);

        RelativeLayoutVideo.addView(ImageViewPlay);

        GradientDrawable ShapeLink = new GradientDrawable();
        ShapeLink.setStroke(MiscHandler.ToDimension(context, 1), ContextCompat.getColor(context, R.color.BlueGray));

        RelativeLayoutContentLink = new RelativeLayout(context);
        RelativeLayoutContentLink.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        RelativeLayoutContentLink.setPadding(MiscHandler.ToDimension(context, 1), MiscHandler.ToDimension(context, 1), MiscHandler.ToDimension(context, 1), MiscHandler.ToDimension(context, 1));
        RelativeLayoutContentLink.setBackground(ShapeLink);
        RelativeLayoutContentLink.setMinimumHeight(MiscHandler.ToDimension(context, 56));
        RelativeLayoutContentLink.setVisibility(View.GONE);

        RelativeLayoutContent.addView(RelativeLayoutContentLink);

        RelativeLayout.LayoutParams LoadingViewLinkParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), MiscHandler.ToDimension(context, 56));
        LoadingViewLinkParam.addRule(RelativeLayout.CENTER_HORIZONTAL);
        LoadingViewLinkParam.addRule(RelativeLayout.CENTER_VERTICAL);

        LoadingViewLink = new LoadingView(context);
        LoadingViewLink.setLayoutParams(LoadingViewLinkParam);

        RelativeLayoutContentLink.addView(LoadingViewLink);

        RelativeLayout.LayoutParams TextViewTryLinkParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTryLinkParam.addRule(RelativeLayout.CENTER_HORIZONTAL);
        TextViewTryLinkParam.addRule(RelativeLayout.CENTER_VERTICAL);

        TextViewTryLink = new TextView(context);
        TextViewTryLink.setLayoutParams(TextViewTryLinkParam);
        TextViewTryLink.setTextColor(ContextCompat.getColor(context, R.color.Black));
        TextViewTryLink.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        TextViewTryLink.setTypeface(null, Typeface.BOLD);
        TextViewTryLink.setText(getString(R.string.TryAgain));
        TextViewTryLink.setVisibility(View.GONE);

        RelativeLayoutContentLink.addView(TextViewTryLink);

        RelativeLayout.LayoutParams TextViewWebsiteLinkParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewWebsiteLinkParam.setMargins(MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 5), MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 5));

        TextViewWebsiteLink = new TextView(context);
        TextViewWebsiteLink.setLayoutParams(TextViewWebsiteLinkParam);
        TextViewWebsiteLink.setTextColor(ContextCompat.getColor(context, R.color.BlueLight));
        TextViewWebsiteLink.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        TextViewWebsiteLink.setId(MiscHandler.GenerateViewID());

        RelativeLayoutContentLink.addView(TextViewWebsiteLink);

        RelativeLayout.LayoutParams TextViewDescriptionLinkParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewDescriptionLinkParam.setMargins(MiscHandler.ToDimension(context, 10), 0, MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 5));
        TextViewDescriptionLinkParam.addRule(RelativeLayout.BELOW, TextViewWebsiteLink.getId());

        TextViewDescriptionLink = new TextView(context);
        TextViewDescriptionLink.setLayoutParams(TextViewDescriptionLinkParam);
        TextViewDescriptionLink.setTextColor(ContextCompat.getColor(context, R.color.Gray3));
        TextViewDescriptionLink.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        TextViewDescriptionLink.setId(MiscHandler.GenerateViewID());

        RelativeLayoutContentLink.addView(TextViewDescriptionLink);

        RelativeLayout.LayoutParams ImageViewFavLinkParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        ImageViewFavLinkParam.addRule(RelativeLayout.BELOW, TextViewDescriptionLink.getId());

        ImageViewFavLink = new ImageView(context);
        ImageViewFavLink.setLayoutParams(ImageViewFavLinkParam);
        ImageViewFavLink.setScaleType(ImageView.ScaleType.FIT_XY);
        ImageViewFavLink.setMaxHeight(MiscHandler.ToDimension(context, 300));
        ImageViewFavLink.setAdjustViewBounds(true);

        RelativeLayoutContentLink.addView(ImageViewFavLink);

        RelativeLayout.LayoutParams LinearLayoutInfoParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LinearLayoutInfoParam.addRule(RelativeLayout.BELOW, RelativeLayoutContent.getId());

        LinearLayout LinearLayoutInfo = new LinearLayout(context);
        LinearLayoutInfo.setLayoutParams(LinearLayoutInfoParam);
        LinearLayoutInfo.setId(MiscHandler.GenerateViewID());
        LinearLayoutInfo.setPadding(0, MiscHandler.ToDimension(context, 15), 0, MiscHandler.ToDimension(context, 15));

        RelativeLayoutMain2.addView(LinearLayoutInfo);

        TextViewLikeCount = new TextView(context);
        TextViewLikeCount.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewLikeCount.setTextColor(ContextCompat.getColor(context, R.color.Black));
        TextViewLikeCount.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        TextViewLikeCount.setTypeface(null, Typeface.BOLD);
        TextViewLikeCount.setPadding(MiscHandler.ToDimension(context, 15), 0, 0, 0);
        TextViewLikeCount.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Bundle bundle = new Bundle();
                bundle.putString("PostID", PostID);

                Fragment fragment = new LikeFragment();
                fragment.setArguments(bundle);

                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.MainActivityFullContainer, fragment).addToBackStack("LikeFragment").commit();
            }
        });

        LinearLayoutInfo.addView(TextViewLikeCount);

        TextView TextViewLike = new TextView(context);
        TextViewLike.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewLike.setTextColor(ContextCompat.getColor(context, R.color.BlueGray2));
        TextViewLike.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        TextViewLike.setTypeface(null, Typeface.BOLD);
        TextViewLike.setText(getString(R.string.PostFragmentLike));
        TextViewLike.setPadding(MiscHandler.ToDimension(context, 5), 0, 0, 0);
        TextViewLike.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Bundle bundle = new Bundle();
                bundle.putString("PostID", PostID);

                Fragment fragment = new LikeFragment();
                fragment.setArguments(bundle);

                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.MainActivityFullContainer, fragment).addToBackStack("LikeFragment").commit();
            }
        });

        LinearLayoutInfo.addView(TextViewLike);

        TextViewCommentCount = new TextView(context);
        TextViewCommentCount.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewCommentCount.setTextColor(ContextCompat.getColor(context, R.color.Black));
        TextViewCommentCount.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        TextViewCommentCount.setTypeface(null, Typeface.BOLD);
        TextViewCommentCount.setPadding(MiscHandler.ToDimension(context, 15), 0, 0, 0);
        TextViewCommentCount.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (IsComment)
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("PostID", PostID);
                    bundle.putString("OwnerID", OwnerID);

                    Fragment fragment = new CommentFragment();
                    fragment.setArguments(bundle);

                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.MainActivityFullContainer, fragment).addToBackStack("CommentFragment").commit();
                    return;
                }

                MiscHandler.Toast(context, getString(R.string.PostFragmentCommentDisable));
            }
        });

        LinearLayoutInfo.addView(TextViewCommentCount);

        TextView TextViewComment = new TextView(context);
        TextViewComment.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewComment.setTextColor(ContextCompat.getColor(context, R.color.BlueGray2));
        TextViewComment.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        TextViewComment.setTypeface(null, Typeface.BOLD);
        TextViewComment.setText(getString(R.string.PostFragmentComment));
        TextViewComment.setPadding(MiscHandler.ToDimension(context, 5), 0, 0, 0);
        TextViewComment.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (IsComment)
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("PostID", PostID);
                    bundle.putString("OwnerID", OwnerID);

                    Fragment fragment = new CommentFragment();
                    fragment.setArguments(bundle);

                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.MainActivityFullContainer, fragment).addToBackStack("CommentFragment").commit();
                    return;
                }

                MiscHandler.Toast(context, getString(R.string.PostFragmentCommentDisable));
            }
        });

        LinearLayoutInfo.addView(TextViewComment);

        RelativeLayout.LayoutParams ViewLine2Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1));
        ViewLine2Param.addRule(RelativeLayout.BELOW, LinearLayoutInfo.getId());

        View ViewLine2 = new View(context);
        ViewLine2.setLayoutParams(ViewLine2Param);
        ViewLine2.setBackgroundResource(R.color.Gray);
        ViewLine2.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain2.addView(ViewLine2);

        RelativeLayout.LayoutParams LinearLayoutToolParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LinearLayoutToolParam.addRule(RelativeLayout.BELOW, ViewLine2.getId());

        LinearLayout LinearLayoutTool = new LinearLayout(context);
        LinearLayoutTool.setLayoutParams(LinearLayoutToolParam);
        LinearLayoutTool.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain2.addView(LinearLayoutTool);

        ImageViewLike = new ImageView(context);
        ImageViewLike.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
        ImageViewLike.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewLike.setLayoutParams(new LinearLayout.LayoutParams(MiscHandler.ToDimension(context, 56), MiscHandler.ToDimension(context, 56), 1f));
        ImageViewLike.setImageResource(R.drawable.ic_like);
        ImageViewLike.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (IsLike)
                {
                    ImageViewLike.setImageResource(R.drawable.ic_like);

                    ObjectAnimator Fade = ObjectAnimator.ofFloat(ImageViewLike, "alpha",  0.1f, 1f);
                    Fade.setDuration(400);

                    AnimatorSet AnimationSet = new AnimatorSet();
                    AnimationSet.play(Fade);
                    AnimationSet.start();

                    IsLike = false;
                    TextViewLikeCount.setText(String.valueOf(Integer.parseInt(TextViewLikeCount.getText().toString()) - 1));
                }
                else
                {
                    ImageViewLike.setImageResource(R.drawable.ic_like_red);

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

                    IsLike = true;
                    TextViewLikeCount.setText(String.valueOf(Integer.parseInt(TextViewLikeCount.getText().toString()) + 1));
                }

                AndroidNetworking.post(MiscHandler.GetRandomServer("PostLike"))
                .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
                .addBodyParameter("PostID", PostID)
                .setTag("PostFragment")
                .build()
                .getAsString(null);
            }
        });

        LinearLayoutTool.addView(ImageViewLike);

        ImageView ImageViewComment = new ImageView(context);
        ImageViewComment.setPadding(MiscHandler.ToDimension(context, 14), MiscHandler.ToDimension(context, 14), MiscHandler.ToDimension(context, 14), MiscHandler.ToDimension(context, 14));
        ImageViewComment.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewComment.setLayoutParams(new LinearLayout.LayoutParams(MiscHandler.ToDimension(context, 56), MiscHandler.ToDimension(context, 56), 1f));
        ImageViewComment.setImageResource(R.drawable.ic_comment);
        ImageViewComment.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (IsComment)
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("PostID", PostID);
                    bundle.putString("OwnerID", OwnerID);

                    Fragment fragment = new CommentFragment();
                    fragment.setArguments(bundle);

                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.MainActivityFullContainer, fragment).addToBackStack("CommentFragment").commit();
                    return;
                }

                MiscHandler.Toast(context, getString(R.string.PostFragmentCommentDisable));
            }
        });

        LinearLayoutTool.addView(ImageViewComment);

        ImageView ImageViewShare = new ImageView(context);
        ImageViewShare.setPadding(MiscHandler.ToDimension(context, 14), MiscHandler.ToDimension(context, 14), MiscHandler.ToDimension(context, 14), MiscHandler.ToDimension(context, 14));
        ImageViewShare.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewShare.setLayoutParams(new LinearLayout.LayoutParams(MiscHandler.ToDimension(context, 56), MiscHandler.ToDimension(context, 56), 1f));
        ImageViewShare.setImageResource(R.drawable.ic_share);
        ImageViewShare.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent SendIntent = new Intent();
                SendIntent.setAction(Intent.ACTION_SEND);
                SendIntent.putExtra(Intent.EXTRA_TEXT, TextViewMessage.getText().toString() + "\n http://biogram.co/" + PostID);
                SendIntent.setType("text/plain");
                getActivity().startActivity(Intent.createChooser(SendIntent, getString(R.string.PostFragmentChoose)));
            }
        });

        LinearLayoutTool.addView(ImageViewShare);

        RelativeLayout.LayoutParams ViewLine3Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1));
        ViewLine3Param.addRule(RelativeLayout.BELOW, LinearLayoutTool.getId());
        ViewLine3Param.setMargins(0, 0, 0, MiscHandler.ToDimension(context, 25));

        View ViewLine3 = new View(context);
        ViewLine3.setLayoutParams(ViewLine3Param);
        ViewLine3.setBackgroundResource(R.color.Gray);
        ViewLine3.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain2.addView(ViewLine3);

        RelativeLayout.LayoutParams RelativeLayoutLoadingParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        RelativeLayoutLoadingParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

        final RelativeLayout RelativeLayoutLoading = new RelativeLayout(context);
        RelativeLayoutLoading.setLayoutParams(RelativeLayoutLoadingParam);
        RelativeLayoutLoading.setBackgroundResource(R.color.White);
        RelativeLayoutLoading.setClickable(true);

        RelativeLayoutMain2.addView(RelativeLayoutLoading);

        RelativeLayout.LayoutParams LoadingViewMainParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), MiscHandler.ToDimension(context, 56));
        LoadingViewMainParam.addRule(RelativeLayout.CENTER_HORIZONTAL);
        LoadingViewMainParam.addRule(RelativeLayout.CENTER_VERTICAL);

        final LoadingView LoadingViewMain = new LoadingView(context);
        LoadingViewMain.setLayoutParams(LoadingViewMainParam);

        RelativeLayoutLoading.addView(LoadingViewMain);

        RelativeLayout.LayoutParams TextViewTryAgainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTryAgainParam.addRule(RelativeLayout.CENTER_HORIZONTAL);
        TextViewTryAgainParam.addRule(RelativeLayout.CENTER_VERTICAL);

        final TextView TextViewTryAgain = new TextView(context);
        TextViewTryAgain.setLayoutParams(TextViewTryAgainParam);
        TextViewTryAgain.setTextColor(ContextCompat.getColor(context, R.color.Black));
        TextViewTryAgain.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        TextViewTryAgain.setTypeface(null, Typeface.BOLD);
        TextViewTryAgain.setText(getString(R.string.TryAgain));
        TextViewTryAgain.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                RetrieveDataFromServer(context, PostID, RelativeLayoutLoading, LoadingViewMain, TextViewTryAgain);
            }
        });

        RelativeLayoutLoading.addView(TextViewTryAgain);

        RetrieveDataFromServer(context, PostID, RelativeLayoutLoading, LoadingViewMain, TextViewTryAgain);

        return RelativeLayoutMain;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        AndroidNetworking.forceCancel("PostFragment");
    }

    private void RetrieveDataFromServer(final Context context, String PostID, final RelativeLayout RelativeLayoutLoading, final LoadingView LoadingViewMain, final TextView TextViewTryAgain)
    {
        TextViewTryAgain.setVisibility(View.GONE);
        LoadingViewMain.Start();

        AndroidNetworking.post(MiscHandler.GetRandomServer("PostDetails"))
        .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
        .addBodyParameter("PostID", PostID)
        .setTag("PostFragment")
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
                        Result = new JSONObject(Result.getString("Result"));

                        if (!Result.getString("Avatar").equals(""))
                            Glide.with(context)
                            .load(Result.getString("Avatar"))
                            .placeholder(R.color.BlueGray)
                            .override(MiscHandler.ToDimension(context, 55), MiscHandler.ToDimension(context, 55))
                            .dontAnimate()
                            .into(ImageViewCircleProfile);

                        final String Username = Result.getString("Username");

                        ImageViewCircleProfile.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                if (SharedHandler.GetString(context, "Username").equals(Username))
                                    return;

                                Bundle bundle = new Bundle();
                                bundle.putString("Username", Username);

                                Fragment fragment = new ProfileFragment();
                                fragment.setArguments(bundle);

                                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.MainActivityFullContainer, fragment).addToBackStack("ProfileFragment").commit();
                            }
                        });

                        TextViewUsername.setText(Username);
                        TextViewTime.setText(MiscHandler.GetTimeName(Result.getLong("Time")));

                        if (!Result.getString("Message").equals(""))
                        {
                            if (new Bidi(Result.getString("Message"), Bidi.DIRECTION_DEFAULT_LEFT_TO_RIGHT).getBaseLevel() == 0)
                            {
                                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) TextViewMessage.getLayoutParams();
                                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                                TextViewMessage.setLayoutParams(params);
                            }
                            else
                            {
                                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) TextViewMessage.getLayoutParams();
                                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                                TextViewMessage.setLayoutParams(params);
                            }

                            TextViewMessage.setVisibility(View.VISIBLE);
                            TextViewMessage.setText(Result.getString("Message"));

                            new TagHandler(TextViewMessage, getActivity());
                        }

                        if (Result.getInt("Type") == 1)
                        {
                            try
                            {
                                final JSONArray URL = new JSONArray(Result.getString("Data"));

                                switch (URL.length())
                                {
                                    case 1:
                                        LinearLayoutContentSingle.setVisibility(View.VISIBLE);
                                        ImageViewSingle.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { try { OpenPreviewImage(URL.get(0).toString(), null, null); } catch (Exception e) { /* Leave Me Alone */ } } });
                                        Glide.with(context).load(URL.get(0).toString()).dontAnimate().into(ImageViewSingle);
                                    break;
                                    case 2:
                                        LinearLayoutContentDouble.setVisibility(View.VISIBLE);
                                        ImageViewDouble1.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { try { OpenPreviewImage(URL.get(0).toString(), URL.get(1).toString(), null); } catch (Exception e) { /* Leave Me Alone */ } } });
                                        ImageViewDouble2.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { try { OpenPreviewImage(URL.get(1).toString(), URL.get(0).toString(), null); } catch (Exception e) { /* Leave Me Alone */ } } });
                                        Glide.with(context).load(URL.get(0).toString()).dontAnimate().into(ImageViewDouble1);
                                        Glide.with(context).load(URL.get(1).toString()).dontAnimate().into(ImageViewDouble2);
                                    break;
                                    case 3:
                                        LinearLayoutContentTriple.setVisibility(View.VISIBLE);
                                        ImageViewTriple1.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { try { OpenPreviewImage(URL.get(0).toString(), URL.get(1).toString(), URL.get(2).toString()); } catch (Exception e) { /* Leave Me Alone */ } } });
                                        ImageViewTriple2.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { try { OpenPreviewImage(URL.get(1).toString(), URL.get(2).toString(), URL.get(0).toString()); } catch (Exception e) { /* Leave Me Alone */ } } });
                                        ImageViewTriple3.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { try { OpenPreviewImage(URL.get(2).toString(), URL.get(0).toString(), URL.get(1).toString()); } catch (Exception e) { /* Leave Me Alone */ } } });
                                        Glide.with(context).load(URL.get(0).toString()).dontAnimate().into(ImageViewTriple1);
                                        Glide.with(context).load(URL.get(1).toString()).dontAnimate().into(ImageViewTriple2);
                                        Glide.with(context).load(URL.get(2).toString()).dontAnimate().into(ImageViewTriple3);
                                    break;
                                }
                            }
                            catch (Exception e)
                            {
                                MiscHandler.Debug("PostFragment-RequestStart3: " + e.toString());
                            }
                        }
                        else if (Result.getInt("Type") == 2)
                        {
                            JSONArray URL = new JSONArray(Result.getString("Data"));

                            final String VideoUrl = URL.get(0).toString();

                            RelativeLayoutVideo.setVisibility(View.VISIBLE);
                            ImageViewVideo.setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("VideoURL", VideoUrl);

                                    Fragment fragment = new VideoPreviewFragment();
                                    fragment.setArguments(bundle);

                                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.MainActivityFullContainer, fragment).addToBackStack("VideoPreviewFragment").commit();
                                }
                            });

                            MiscHandler.CreateVideoThumbnail(URL.get(0).toString(), getActivity(), ImageViewVideo);
                        }
                        else if (Result.getInt("Type") == 3)
                        {
                            RelativeLayoutContentLink.setVisibility(View.VISIBLE);
                            LoadingViewLink.Start();

                            try
                            {
                                JSONArray URL = new JSONArray(Result.getString("Data"));

                                final TextCrawler Request = new TextCrawler(context, URL.get(0).toString(), "PostFragment", new TextCrawler.TextCrawlerCallBack()
                                {
                                    @Override
                                    public void OnCompleted(TextCrawler.URLContent Content)
                                    {
                                        if (new Bidi(Content.Title, Bidi.DIRECTION_DEFAULT_LEFT_TO_RIGHT).getBaseLevel() == 0)
                                        {
                                            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) TextViewWebsiteLink.getLayoutParams();
                                            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                                            TextViewWebsiteLink.setLayoutParams(params);
                                        }
                                        else
                                        {
                                            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) TextViewWebsiteLink.getLayoutParams();
                                            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                                            TextViewWebsiteLink.setLayoutParams(params);
                                        }

                                        if (new Bidi(Content.Title, Bidi.DIRECTION_DEFAULT_LEFT_TO_RIGHT).getBaseLevel() == 0)
                                        {
                                            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) TextViewDescriptionLink.getLayoutParams();
                                            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                                            TextViewDescriptionLink.setLayoutParams(params);
                                        }
                                        else
                                        {
                                            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) TextViewDescriptionLink.getLayoutParams();
                                            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                                            TextViewDescriptionLink.setLayoutParams(params);
                                        }

                                        TextViewWebsiteLink.setText(Content.Title);
                                        TextViewDescriptionLink.setText(Content.Description);
                                        LoadingViewLink.Stop();
                                        ImageViewFavLink.setVisibility(View.VISIBLE);

                                        Glide.with(context).load(Content.Image).dontAnimate().into(ImageViewFavLink);
                                    }

                                    @Override
                                    public void OnFailed()
                                    {
                                        LoadingViewLink.Stop();
                                        TextViewTryLink.setVisibility(View.VISIBLE);
                                    }
                                });

                                TextViewTryLink.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Request.Start();
                                        LoadingViewLink.Start();
                                    }
                                });

                                Request.Start();
                            }
                            catch (Exception e)
                            {
                                MiscHandler.Debug("PostFragment-RequestStart2: " + e.toString());
                            }
                        }

                        TextViewLikeCount.setText(String.valueOf(Result.getInt("LikeCount")));
                        TextViewCommentCount.setText(String.valueOf(Result.getInt("CommentCount")));

                        OwnerID = Result.getString("OwnerID");
                        IsLike = Result.getBoolean("Like");
                        IsComment = Result.getBoolean("Comment");
                        IsBookmark = Result.getBoolean("BookMark");

                        if (IsLike)
                            ImageViewLike.setImageResource(R.drawable.ic_like_red);

                        if (IsBookmark)
                            ImageViewBookMark.setImageResource(R.drawable.ic_bookmark_black2);

                        ImageViewOption.setVisibility(View.VISIBLE);
                        ImageViewBookMark.setVisibility(View.VISIBLE);
                    }
                }
                catch (Exception e)
                {
                    MiscHandler.Debug("PostFragment-RequestStart: " + e.toString());
                }

                LoadingViewMain.Stop();
                TextViewTryAgain.setVisibility(View.GONE);
                RelativeLayoutLoading.setVisibility(View.GONE);
            }

            @Override
            public void onError(ANError anError)
            {
                LoadingViewMain.Stop();
                TextViewTryAgain.setVisibility(View.GONE);
            }
        });
    }

    private void OpenPreviewImage(String URL, String URL2, String URL3)
    {
        Bundle bundle = new Bundle();
        bundle.putString("URL", URL);

        if (URL2 != null && !URL2.equals(""))
            bundle.putString("URL2", URL2);

        if (URL3 != null && !URL3.equals(""))
            bundle.putString("URL3", URL3);

        Fragment fragment = new ImagePreviewFragment();
        fragment.setArguments(bundle);

        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.MainActivityFullContainer, fragment).addToBackStack("ImagePreviewFragment").commit();
    }
}