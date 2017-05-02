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
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.Bidi;

import co.biogram.main.App;
import co.biogram.main.R;
import co.biogram.main.handler.MiscHandler;
import co.biogram.main.handler.SharedHandler;
import co.biogram.main.handler.TagHandler;
import co.biogram.main.handler.URLHandler;
import co.biogram.main.misc.LoadingView;
import co.biogram.main.misc.ImageViewCircle;
import co.biogram.main.misc.TextCrawler;

public class FragmentPostDetails extends Fragment
{
    private LoadingView LoadingPage;
    private RelativeLayout LoadPage;
    private TextView TryPage;

    private ImageViewCircle Profile;
    private TextView Username;
    private TextView Time;
    private TextView Message;
    private LinearLayout ContentSingle;
    private ImageView Single;
    private LinearLayout ContentDouble;
    private ImageView Double1;
    private ImageView Double2;
    private LinearLayout ContentTriple;
    private ImageView Triple1;
    private ImageView Triple2;
    private ImageView Triple3;
    private RelativeLayout ContentLink;
    private LoadingView Loading;
    private TextView Try;
    private TextView Website;
    private TextView Description;
    private ImageView Fav;
    private TextView LikeCount;
    private TextView CommentCount;

    private ImageView Option;
    private ImageView BookMark;
    private ImageView IconLike;

    private String PostID = "";
    private String OwnerID = "";
    private boolean IsLike = false;
    private boolean IsComment = false;
    private boolean IsBookMark = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        RelativeLayout Root = new RelativeLayout(App.GetContext());
        Root.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        Root.setBackgroundColor(ContextCompat.getColor(App.GetContext(), R.color.White));

        RelativeLayout Header = new RelativeLayout(App.GetContext());
        Header.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.DpToPx(56)));
        Header.setBackgroundColor(ContextCompat.getColor(App.GetContext(), R.color.White5));
        Header.setId(MiscHandler.GenerateViewID());

        Root.addView(Header);

        ImageView Back = new ImageView(App.GetContext());
        Back.setPadding(MiscHandler.DpToPx(12), MiscHandler.DpToPx(12), MiscHandler.DpToPx(12), MiscHandler.DpToPx(12));
        Back.setScaleType(ImageView.ScaleType.FIT_CENTER);
        Back.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.DpToPx(56), MiscHandler.DpToPx(56)));
        Back.setImageResource(R.drawable.ic_back_blue);
        Back.setId(MiscHandler.GenerateViewID());
        Back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                getActivity().getSupportFragmentManager().beginTransaction().remove(FragmentPostDetails.this).commit();
            }
        });

        Header.addView(Back);

        RelativeLayout.LayoutParams TitleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TitleParam.addRule(RelativeLayout.RIGHT_OF, Back.getId());
        TitleParam.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

        TextView Title = new TextView(App.GetContext());
        Title.setLayoutParams(TitleParam);
        Title.setTextColor(ContextCompat.getColor(App.GetContext(), R.color.Black));
        Title.setText(getString(R.string.PostDetailsTitle));
        Title.setTypeface(null, Typeface.BOLD);
        Title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

        Header.addView(Title);

        RelativeLayout.LayoutParams OptionParam = new RelativeLayout.LayoutParams(MiscHandler.DpToPx(56), MiscHandler.DpToPx(56));
        OptionParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);

        Option = new ImageView(App.GetContext());
        Option.setPadding(MiscHandler.DpToPx(14), MiscHandler.DpToPx(14), MiscHandler.DpToPx(14), MiscHandler.DpToPx(14));
        Option.setScaleType(ImageView.ScaleType.FIT_CENTER);
        Option.setLayoutParams(OptionParam);
        Option.setImageResource(R.drawable.ic_option_black);
        Option.setId(MiscHandler.GenerateViewID());
        Option.setVisibility(View.GONE);
        Option.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                final Dialog DialogOption = new Dialog(getActivity());
                DialogOption.requestWindowFeature(Window.FEATURE_NO_TITLE);
                DialogOption.setCancelable(true);

                LinearLayout Root = new LinearLayout(App.GetContext());
                Root.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                Root.setBackgroundColor(ContextCompat.getColor(App.GetContext(), R.color.White));
                Root.setOrientation(LinearLayout.VERTICAL);

                TextView Follow = new TextView(App.GetContext());
                Follow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                Follow.setTextColor(ContextCompat.getColor(App.GetContext(), R.color.Black));
                Follow.setText(getString(R.string.AdapterPostFollow));
                Follow.setPadding(MiscHandler.DpToPx(15), MiscHandler.DpToPx(15), MiscHandler.DpToPx(15), MiscHandler.DpToPx(15));
                Follow.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                Follow.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        DialogOption.dismiss();
                        MiscHandler.Toast(getString(R.string.GeneralSoon));
                    }
                });

                Root.addView(Follow);

                View FollowLine = new View(App.GetContext());
                FollowLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MiscHandler.DpToPx(1)));
                FollowLine.setBackgroundColor(ContextCompat.getColor(App.GetContext(), R.color.Gray1));

                Root.addView(FollowLine);

                final TextView Turn = new TextView(App.GetContext());
                Turn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                Turn.setTextColor(ContextCompat.getColor(App.GetContext(), R.color.Black));
                Turn.setVisibility(View.GONE);
                Turn.setPadding(MiscHandler.DpToPx(15), MiscHandler.DpToPx(15), MiscHandler.DpToPx(15), MiscHandler.DpToPx(15));
                Turn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                Turn.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        AndroidNetworking.post(URLHandler.GetURL(URLHandler.URL.POST_TURN_COMMENT))
                        .addBodyParameter("PostID",PostID)
                        .addHeaders("TOKEN", SharedHandler.GetString("TOKEN"))
                        .setTag("FragmentPostDetails").build().getAsString(new StringRequestListener()
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
                                    // Leave Me Alone
                                }
                            }

                            @Override
                            public void onError(ANError e) { }
                        });

                        DialogOption.dismiss();
                    }
                });

                if (IsComment)
                    Turn.setText(getString(R.string.AdapterPostTurnOff));
                else
                    Turn.setText(getString(R.string.AdapterPostTurnOn));

                Root.addView(Turn);

                View TurnLine = new View(App.GetContext());
                TurnLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MiscHandler.DpToPx(1)));
                TurnLine.setBackgroundColor(ContextCompat.getColor(App.GetContext(), R.color.Gray1));
                TurnLine.setVisibility(View.GONE);

                Root.addView(TurnLine);

                TextView Copy = new TextView(App.GetContext());
                Copy.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                Copy.setTextColor(ContextCompat.getColor(App.GetContext(), R.color.Black));
                Copy.setText(getString(R.string.AdapterPostCopy));
                Copy.setPadding(MiscHandler.DpToPx(15), MiscHandler.DpToPx(15), MiscHandler.DpToPx(15), MiscHandler.DpToPx(15));
                Copy.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                Copy.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText(PostID, Message.getText().toString());
                        clipboard.setPrimaryClip(clip);

                        MiscHandler.Toast(getString(R.string.AdapterPostClipboard));
                        DialogOption.dismiss();
                    }
                });

                Root.addView(Copy);

                View CopyLine = new View(App.GetContext());
                CopyLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MiscHandler.DpToPx(1)));
                CopyLine.setBackgroundColor(ContextCompat.getColor(App.GetContext(), R.color.Gray1));

                Root.addView(CopyLine);

                TextView Block = new TextView(App.GetContext());
                Block.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                Block.setTextColor(ContextCompat.getColor(App.GetContext(), R.color.Black));
                Block.setText(getString(R.string.AdapterPostBlock));
                Block.setPadding(MiscHandler.DpToPx(15), MiscHandler.DpToPx(15), MiscHandler.DpToPx(15), MiscHandler.DpToPx(15));
                Block.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                Block.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        DialogOption.dismiss();
                        MiscHandler.Toast(getString(R.string.GeneralSoon));
                    }
                });

                Root.addView(Block);

                View BlockLine = new View(App.GetContext());
                BlockLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MiscHandler.DpToPx(1)));
                BlockLine.setBackgroundColor(ContextCompat.getColor(App.GetContext(), R.color.Gray1));

                Root.addView(BlockLine);

                TextView Delete = new TextView(App.GetContext());
                Delete.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                Delete.setTextColor(ContextCompat.getColor(App.GetContext(), R.color.Black));
                Delete.setText(getString(R.string.AdapterPostDelete));
                Delete.setVisibility(View.GONE);
                Delete.setPadding(MiscHandler.DpToPx(15), MiscHandler.DpToPx(15), MiscHandler.DpToPx(15), MiscHandler.DpToPx(15));
                Delete.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                Delete.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        AndroidNetworking.post(URLHandler.GetURL(URLHandler.URL.POST_DELETE))
                        .addBodyParameter("PostID", PostID)
                        .addHeaders("TOKEN", SharedHandler.GetString("TOKEN"))
                        .setTag("FragmentPostDetails").build().getAsString(new StringRequestListener()
                        {
                            @Override
                            public void onResponse(String Response)
                            {
                                try
                                {
                                    if (new JSONObject(Response).getInt("Message") == 1000)
                                        getActivity().getSupportFragmentManager().beginTransaction().remove(FragmentPostDetails.this).commit();
                                }
                                catch (Exception e)
                                {
                                    // Leave Me Alone
                                }
                            }

                            @Override
                            public void onError(ANError e) { }
                        });

                        DialogOption.dismiss();
                    }
                });

                Root.addView(Delete);

                View DeleteLine = new View(App.GetContext());
                DeleteLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MiscHandler.DpToPx(1)));
                DeleteLine.setBackgroundColor(ContextCompat.getColor(App.GetContext(), R.color.Gray1));
                DeleteLine.setVisibility(View.GONE);

                Root.addView(DeleteLine);

                TextView Report = new TextView(App.GetContext());
                Report.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                Report.setTextColor(ContextCompat.getColor(App.GetContext(), R.color.Black));
                Report.setText(getString(R.string.AdapterPostReport));
                Report.setPadding(MiscHandler.DpToPx(15), MiscHandler.DpToPx(15), MiscHandler.DpToPx(15), MiscHandler.DpToPx(15));
                Report.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                Report.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        DialogOption.dismiss();
                        MiscHandler.Toast(getString(R.string.GeneralSoon));
                    }
                });

                Root.addView(Report);

                if (OwnerID.equals(SharedHandler.GetString("ID")))
                {
                    Follow.setVisibility(View.GONE);
                    FollowLine.setVisibility(View.GONE);

                    Turn.setVisibility(View.VISIBLE);
                    TurnLine.setVisibility(View.VISIBLE);

                    Block.setVisibility(View.GONE);
                    BlockLine.setVisibility(View.GONE);

                    Delete.setVisibility(View.VISIBLE);
                    DeleteLine.setVisibility(View.VISIBLE);

                    Report.setVisibility(View.GONE);
                }

                DialogOption.setContentView(Root);
                DialogOption.show();
            }
        });

        Header.addView(Option);

        RelativeLayout.LayoutParams BookMarkParam = new RelativeLayout.LayoutParams(MiscHandler.DpToPx(56), MiscHandler.DpToPx(56));
        BookMarkParam.addRule(RelativeLayout.LEFT_OF, Option.getId());

        BookMark = new ImageView(App.GetContext());
        BookMark.setPadding(MiscHandler.DpToPx(16), MiscHandler.DpToPx(16), MiscHandler.DpToPx(16), MiscHandler.DpToPx(16));
        BookMark.setScaleType(ImageView.ScaleType.FIT_CENTER);
        BookMark.setLayoutParams(BookMarkParam);
        BookMark.setImageResource(R.drawable.ic_bookmark_black);
        BookMark.setVisibility(View.GONE);
        BookMark.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AndroidNetworking.post(URLHandler.GetURL(URLHandler.URL.POST_BOOKMARK))
                .addBodyParameter("PostID", PostID)
                .addHeaders("TOKEN", SharedHandler.GetString("TOKEN"))
                .setTag("FragmentPostDetails").build().getAsString(new StringRequestListener()
                {
                    @Override
                    public void onResponse(String Response)
                    {
                        try
                        {
                            if (new JSONObject(Response).getInt("Message") == 1000)
                            {
                                IsBookMark = !IsBookMark;

                                if (IsBookMark)
                                    BookMark.setImageResource(R.drawable.ic_bookmark_black2);
                                else
                                    BookMark.setImageResource(R.drawable.ic_bookmark_black);
                            }
                        }
                        catch (Exception e)
                        {
                            // Leave Me Alone
                        }
                    }

                    @Override
                    public void onError(ANError e) { }
                });
            }
        });

        Header.addView(BookMark);

        RelativeLayout.LayoutParams LineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.DpToPx(1));
        LineParam.addRule(RelativeLayout.BELOW, Header.getId());

        View Line = new View(App.GetContext());
        Line.setLayoutParams(LineParam);
        Line.setBackgroundColor(ContextCompat.getColor(App.GetContext(), R.color.Gray2));
        Line.setId(MiscHandler.GenerateViewID());

        Root.addView(Line);

        RelativeLayout.LayoutParams ScrollParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        ScrollParam.addRule(RelativeLayout.BELOW, Line.getId());

        ScrollView Scroll = new ScrollView(App.GetContext());
        Scroll.setLayoutParams(ScrollParam);
        Scroll.setFillViewport(true);
        Scroll.setVerticalScrollBarEnabled(false);
        Scroll.setHorizontalScrollBarEnabled(false);

        Root.addView(Scroll);

        RelativeLayout Main = new RelativeLayout(App.GetContext());
        Main.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

        Scroll.addView(Main);

        RelativeLayout.LayoutParams ProfileParam = new RelativeLayout.LayoutParams(MiscHandler.DpToPx(55), MiscHandler.DpToPx(55));
        ProfileParam.setMargins(MiscHandler.DpToPx(10), MiscHandler.DpToPx(10), MiscHandler.DpToPx(10), MiscHandler.DpToPx(10));

        Profile = new ImageViewCircle(App.GetContext());
        Profile.setLayoutParams(ProfileParam);
        Profile.setImageResource(R.color.BlueGray);
        Profile.setId(MiscHandler.GenerateViewID());

        Main.addView(Profile);

        RelativeLayout.LayoutParams UsernameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        UsernameParam.addRule(RelativeLayout.RIGHT_OF, Profile.getId());
        UsernameParam.setMargins(0, MiscHandler.DpToPx(14), 0, 0);

        Username = new TextView(App.GetContext());
        Username.setLayoutParams(UsernameParam);
        Username.setTextColor(ContextCompat.getColor(App.GetContext(), R.color.Black));
        Username.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        Username.setId(MiscHandler.GenerateViewID());
        Username.setTypeface(null, Typeface.BOLD);

        Main.addView(Username);

        RelativeLayout.LayoutParams TimeParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TimeParam.addRule(RelativeLayout.BELOW, Username.getId());
        TimeParam.addRule(RelativeLayout.RIGHT_OF, Profile.getId());

        Time = new TextView(App.GetContext());
        Time.setLayoutParams(TimeParam);
        Time.setTextColor(ContextCompat.getColor(App.GetContext(), R.color.Gray4));
        Time.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        Main.addView(Time);

        RelativeLayout.LayoutParams MessageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        MessageParam.addRule(RelativeLayout.BELOW, Profile.getId());
        MessageParam.setMargins(MiscHandler.DpToPx(10), MiscHandler.DpToPx(5), MiscHandler.DpToPx(10), MiscHandler.DpToPx(5));

        Message = new TextView(App.GetContext());
        Message.setLayoutParams(MessageParam);
        Message.setTextColor(ContextCompat.getColor(App.GetContext(), R.color.Black));
        Message.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        Message.setId(MiscHandler.GenerateViewID());
        Message.setLineSpacing(1f, 1.25f);
        Message.setVisibility(View.GONE);

        Main.addView(Message);

        RelativeLayout.LayoutParams ContentParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        ContentParam.setMargins(MiscHandler.DpToPx(10), MiscHandler.DpToPx(15), MiscHandler.DpToPx(10), 0);
        ContentParam.addRule(RelativeLayout.BELOW, Message.getId());

        RelativeLayout Content = new RelativeLayout(App.GetContext());
        Content.setLayoutParams(ContentParam);
        Content.setId(MiscHandler.GenerateViewID());

        Main.addView(Content);

        ContentSingle = new LinearLayout(App.GetContext());
        ContentSingle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        ContentSingle.setVisibility(View.GONE);

        Content.addView(ContentSingle);

        Single = new ImageView(App.GetContext());
        Single.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.DpToPx(200)));
        Single.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Single.setBackgroundColor(ContextCompat.getColor(App.GetContext(), R.color.BlueGray));

        ContentSingle.addView(Single);

        ContentDouble = new LinearLayout(App.GetContext());
        ContentDouble.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        ContentDouble.setVisibility(View.GONE);

        Content.addView(ContentDouble);

        Double1 = new ImageView(App.GetContext());
        Double1.setLayoutParams(new LinearLayout.LayoutParams(0, MiscHandler.DpToPx(200), 1f));
        Double1.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Double1.setBackgroundColor(ContextCompat.getColor(App.GetContext(), R.color.BlueGray));

        ContentDouble.addView(Double1);

        View LineDouble = new View(App.GetContext());
        LineDouble.setLayoutParams(new LinearLayout.LayoutParams(0, RelativeLayout.LayoutParams.MATCH_PARENT, 0.05f));

        ContentDouble.addView(LineDouble);

        Double2 = new ImageView(App.GetContext());
        Double2.setLayoutParams(new LinearLayout.LayoutParams(0, MiscHandler.DpToPx(200), 1f));
        Double2.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Double2.setBackgroundColor(ContextCompat.getColor(App.GetContext(), R.color.BlueGray));

        ContentDouble.addView(Double2);

        ContentTriple = new LinearLayout(App.GetContext());
        ContentTriple.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        ContentTriple.setVisibility(View.GONE);

        Content.addView(ContentTriple);

        Triple1 = new ImageView(App.GetContext());
        Triple1.setLayoutParams(new LinearLayout.LayoutParams(0, MiscHandler.DpToPx(200), 1f));
        Triple1.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Triple1.setBackgroundColor(ContextCompat.getColor(App.GetContext(), R.color.BlueGray));

        ContentTriple.addView(Triple1);

        View LineTriple = new View(App.GetContext());
        LineTriple.setLayoutParams(new LinearLayout.LayoutParams(0, RelativeLayout.LayoutParams.MATCH_PARENT, 0.05f));

        ContentTriple.addView(LineTriple);

        RelativeLayout TripleLayout = new RelativeLayout(App.GetContext());
        TripleLayout.setLayoutParams(new LinearLayout.LayoutParams(0, MiscHandler.DpToPx(200), 1f));

        ContentTriple.addView(TripleLayout);

        Triple2 = new ImageView(App.GetContext());
        Triple2.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.DpToPx(97)));
        Triple2.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Triple2.setBackgroundColor(ContextCompat.getColor(App.GetContext(), R.color.BlueGray));
        Triple2.setId(MiscHandler.GenerateViewID());

        TripleLayout.addView(Triple2);

        RelativeLayout.LayoutParams LineTriple2Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.DpToPx(6));
        LineTriple2Param.addRule(RelativeLayout.BELOW, Triple2.getId());

        View LineTriple2 = new View(App.GetContext());
        LineTriple2.setLayoutParams(LineTriple2Param);
        LineTriple2.setId(MiscHandler.GenerateViewID());

        TripleLayout.addView(LineTriple2);

        RelativeLayout.LayoutParams Triple3Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.DpToPx(97));
        Triple3Param.addRule(RelativeLayout.BELOW, LineTriple2.getId());

        Triple3 = new ImageView(App.GetContext());
        Triple3.setLayoutParams(Triple3Param);
        Triple3.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Triple3.setBackgroundColor(ContextCompat.getColor(App.GetContext(), R.color.BlueGray));

        TripleLayout.addView(Triple3);

        GradientDrawable ShapeLink = new GradientDrawable();
        ShapeLink.setStroke(MiscHandler.DpToPx(1), ContextCompat.getColor(App.GetContext(), R.color.BlueGray));

        ContentLink = new RelativeLayout(App.GetContext());
        ContentLink.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        ContentLink.setPadding(MiscHandler.DpToPx(1), MiscHandler.DpToPx(1), MiscHandler.DpToPx(1), MiscHandler.DpToPx(1));
        ContentLink.setBackground(ShapeLink);
        ContentLink.setMinimumHeight(MiscHandler.DpToPx(56));
        ContentLink.setVisibility(View.GONE);

        Content.addView(ContentLink);

        RelativeLayout.LayoutParams LoadingParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LoadingParam.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        LoadingParam.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);

        Loading = new LoadingView(App.GetContext());
        Loading.setLayoutParams(LoadingParam);
        Loading.SetColor(R.color.BlueGray2);

        ContentLink.addView(Loading);

        RelativeLayout.LayoutParams TryParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TryParam.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        TryParam.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);

        Try = new TextView(App.GetContext());
        Try.setLayoutParams(TryParam);
        Try.setTextColor(ContextCompat.getColor(App.GetContext(), R.color.Black));
        Try.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        Try.setTypeface(null, Typeface.BOLD);
        Try.setText(getString(R.string.GeneralTryAgain));
        Try.setVisibility(View.GONE);

        ContentLink.addView(Try);

        RelativeLayout.LayoutParams TitleParam2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TitleParam2.setMargins(MiscHandler.DpToPx(10), MiscHandler.DpToPx(5), MiscHandler.DpToPx(10), MiscHandler.DpToPx(5));

        Website = new TextView(App.GetContext());
        Website.setLayoutParams(TitleParam2);
        Website.setTextColor(ContextCompat.getColor(App.GetContext(), R.color.BlueLight));
        Website.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        Website.setId(MiscHandler.GenerateViewID());

        ContentLink.addView(Website);

        RelativeLayout.LayoutParams DescriptionParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        DescriptionParam.setMargins(MiscHandler.DpToPx(10), 0, MiscHandler.DpToPx(10), MiscHandler.DpToPx(5));
        DescriptionParam.addRule(RelativeLayout.BELOW, Website.getId());

        Description = new TextView(App.GetContext());
        Description.setLayoutParams(DescriptionParam);
        Description.setTextColor(ContextCompat.getColor(App.GetContext(), R.color.Gray3));
        Description.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        Description.setId(MiscHandler.GenerateViewID());

        ContentLink.addView(Description);

        RelativeLayout.LayoutParams FavParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        FavParam.addRule(RelativeLayout.BELOW, Description.getId());

        Fav = new ImageView(App.GetContext());
        Fav.setLayoutParams(FavParam);
        Fav.setScaleType(ImageView.ScaleType.FIT_XY);
        Fav.setMaxHeight(MiscHandler.DpToPx(300));
        Fav.setAdjustViewBounds(true);

        ContentLink.addView(Fav);

        RelativeLayout.LayoutParams InfoParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        InfoParam.addRule(RelativeLayout.BELOW, Content.getId());

        LinearLayout Info = new LinearLayout(App.GetContext());
        Info.setLayoutParams(InfoParam);
        Info.setId(MiscHandler.GenerateViewID());
        Info.setPadding(0, MiscHandler.DpToPx(15), 0, MiscHandler.DpToPx(15));

        Main.addView(Info);

        LikeCount = new TextView(App.GetContext());
        LikeCount.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        LikeCount.setTextColor(ContextCompat.getColor(App.GetContext(), R.color.Black));
        LikeCount.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        LikeCount.setTypeface(null, Typeface.BOLD);
        LikeCount.setPadding(MiscHandler.DpToPx(15), 0, 0, 0);
        LikeCount.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Bundle bundle = new Bundle();
                bundle.putString("PostID", PostID);

                Fragment fragment = new FragmentLike();
                fragment.setArguments(bundle);

                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.FullScreenContainer, fragment).addToBackStack("FragmentLike").commit();
            }
        });

        Info.addView(LikeCount);

        TextView Like = new TextView(App.GetContext());
        Like.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        Like.setTextColor(ContextCompat.getColor(App.GetContext(), R.color.BlueGray2));
        Like.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        Like.setTypeface(null, Typeface.BOLD);
        Like.setText(getString(R.string.PostDetailsLike));
        Like.setPadding(MiscHandler.DpToPx(5), 0, 0, 0);
        Like.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Bundle bundle = new Bundle();
                bundle.putString("PostID", PostID);

                Fragment fragment = new FragmentLike();
                fragment.setArguments(bundle);

                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.FullScreenContainer, fragment).addToBackStack("FragmentLike").commit();
            }
        });

        Info.addView(Like);

        CommentCount = new TextView(App.GetContext());
        CommentCount.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        CommentCount.setTextColor(ContextCompat.getColor(App.GetContext(), R.color.Black));
        CommentCount.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        CommentCount.setTypeface(null, Typeface.BOLD);
        CommentCount.setPadding(MiscHandler.DpToPx(15), 0, 0, 0);
        CommentCount.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (IsComment)
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("PostID", PostID);
                    bundle.putString("OwnerID", OwnerID);

                    Fragment fragment = new FragmentComment();
                    fragment.setArguments(bundle);

                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.FullScreenContainer, fragment).addToBackStack("FragmentComment").commit();
                    return;
                }

                MiscHandler.Toast(getString(R.string.AdapterPostComment));
            }
        });

        Info.addView(CommentCount);

        TextView Comment = new TextView(App.GetContext());
        Comment.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        Comment.setTextColor(ContextCompat.getColor(App.GetContext(), R.color.BlueGray2));
        Comment.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        Comment.setTypeface(null, Typeface.BOLD);
        Comment.setText(getString(R.string.PostDetailsComment));
        Comment.setPadding(MiscHandler.DpToPx(5), 0, 0, 0);
        Comment.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (IsComment)
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("PostID", PostID);
                    bundle.putString("OwnerID", OwnerID);

                    Fragment fragment = new FragmentComment();
                    fragment.setArguments(bundle);

                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.FullScreenContainer, fragment).addToBackStack("FragmentComment").commit();
                    return;
                }

                MiscHandler.Toast(getString(R.string.AdapterPostComment));
            }
        });

        Info.addView(Comment);

        RelativeLayout.LayoutParams Line2Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.DpToPx(1));
        Line2Param.addRule(RelativeLayout.BELOW, Info.getId());

        View Line2 = new View(App.GetContext());
        Line2.setLayoutParams(Line2Param);
        Line2.setBackgroundColor(ContextCompat.getColor(App.GetContext(), R.color.Gray));
        Line2.setId(MiscHandler.GenerateViewID());

        Main.addView(Line2);

        RelativeLayout.LayoutParams ToolParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        ToolParam.addRule(RelativeLayout.BELOW, Line2.getId());

        LinearLayout Tool = new LinearLayout(App.GetContext());
        Tool.setLayoutParams(ToolParam);
        Tool.setId(MiscHandler.GenerateViewID());

        Main.addView(Tool);

        IconLike = new ImageView(App.GetContext());
        IconLike.setPadding(MiscHandler.DpToPx(15), MiscHandler.DpToPx(15), MiscHandler.DpToPx(15), MiscHandler.DpToPx(15));
        IconLike.setScaleType(ImageView.ScaleType.FIT_CENTER);
        IconLike.setLayoutParams(new LinearLayout.LayoutParams(MiscHandler.DpToPx(56), MiscHandler.DpToPx(56), 1f));
        IconLike.setImageResource(R.drawable.ic_like);
        IconLike.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (IsLike)
                {
                    IconLike.setImageResource(R.drawable.ic_like);

                    ObjectAnimator Fade = ObjectAnimator.ofFloat(IconLike, "alpha",  0.1f, 1f);
                    Fade.setDuration(400);

                    AnimatorSet AnimationSet = new AnimatorSet();
                    AnimationSet.play(Fade);
                    AnimationSet.start();

                    IsLike = false;
                    LikeCount.setText(String.valueOf(Integer.parseInt(LikeCount.getText().toString()) - 1));

                    AndroidNetworking.post(URLHandler.GetURL(URLHandler.URL.POST_LIKE))
                    .addBodyParameter("PostID", PostID)
                    .addHeaders("TOKEN", SharedHandler.GetString("TOKEN"))
                    .setTag("FragmentPostDetails").build().getAsString(null);
                }
                else
                {
                    IconLike.setImageResource(R.drawable.ic_like_red);

                    ObjectAnimator SizeX = ObjectAnimator.ofFloat(IconLike, "scaleX", 1.5f);
                    SizeX.setDuration(200);

                    ObjectAnimator SizeY = ObjectAnimator.ofFloat(IconLike, "scaleY", 1.5f);
                    SizeY.setDuration(200);

                    ObjectAnimator Fade = ObjectAnimator.ofFloat(IconLike, "alpha",  0.1f, 1f);
                    Fade.setDuration(400);

                    ObjectAnimator SizeX2 = ObjectAnimator.ofFloat(IconLike, "scaleX", 1f);
                    SizeX2.setDuration(200);
                    SizeX2.setStartDelay(200);

                    ObjectAnimator SizeY2 = ObjectAnimator.ofFloat(IconLike, "scaleY", 1f);
                    SizeY2.setDuration(200);
                    SizeY2.setStartDelay(200);

                    AnimatorSet AnimationSet = new AnimatorSet();
                    AnimationSet.playTogether(SizeX, SizeY, Fade, SizeX2, SizeY2);
                    AnimationSet.start();

                    IsLike = true;
                    LikeCount.setText(String.valueOf(Integer.parseInt(LikeCount.getText().toString()) + 1));

                    AndroidNetworking.post(URLHandler.GetURL(URLHandler.URL.POST_LIKE))
                    .addBodyParameter("PostID", PostID)
                    .addHeaders("TOKEN", SharedHandler.GetString("TOKEN"))
                    .setTag("FragmentPostDetails").build().getAsString(null);
                }
            }
        });

        Tool.addView(IconLike);

        ImageView IconComment = new ImageView(App.GetContext());
        IconComment.setPadding(MiscHandler.DpToPx(14), MiscHandler.DpToPx(14), MiscHandler.DpToPx(14), MiscHandler.DpToPx(14));
        IconComment.setScaleType(ImageView.ScaleType.FIT_CENTER);
        IconComment.setLayoutParams(new LinearLayout.LayoutParams(MiscHandler.DpToPx(56), MiscHandler.DpToPx(56), 1f));
        IconComment.setImageResource(R.drawable.ic_comment);
        IconComment.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (IsComment)
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("PostID", PostID);
                    bundle.putString("OwnerID", OwnerID);

                    Fragment fragment = new FragmentComment();
                    fragment.setArguments(bundle);

                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.FullScreenContainer, fragment).addToBackStack("FragmentComment").commit();
                    return;
                }

                MiscHandler.Toast(getString(R.string.AdapterPostComment));
            }
        });

        Tool.addView(IconComment);

        ImageView IconShare = new ImageView(App.GetContext());
        IconShare.setPadding(MiscHandler.DpToPx(14), MiscHandler.DpToPx(14), MiscHandler.DpToPx(14), MiscHandler.DpToPx(14));
        IconShare.setScaleType(ImageView.ScaleType.FIT_CENTER);
        IconShare.setLayoutParams(new LinearLayout.LayoutParams(MiscHandler.DpToPx(56), MiscHandler.DpToPx(56), 1f));
        IconShare.setImageResource(R.drawable.ic_share);
        IconShare.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent SendIntent = new Intent();
                SendIntent.setAction(Intent.ACTION_SEND);
                SendIntent.putExtra(Intent.EXTRA_TEXT, Message + "\n http://BioGram.Co/");
                SendIntent.setType("text/plain");
                getActivity().startActivity(Intent.createChooser(SendIntent, getString(R.string.AdapterPostChoice)));
            }
        });

        Tool.addView(IconShare);

        RelativeLayout.LayoutParams Line3Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.DpToPx(1));
        Line3Param.addRule(RelativeLayout.BELOW, Tool.getId());
        Line3Param.setMargins(0, 0, 0, MiscHandler.DpToPx(25));

        View Line3 = new View(App.GetContext());
        Line3.setLayoutParams(Line3Param);
        Line3.setBackgroundColor(ContextCompat.getColor(App.GetContext(), R.color.Gray));
        Line3.setId(MiscHandler.GenerateViewID());

        Main.addView(Line3);

        RelativeLayout.LayoutParams LoadParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        LoadParam.addRule(RelativeLayout.BELOW, Line.getId());

        LoadPage = new RelativeLayout(App.GetContext());
        LoadPage.setLayoutParams(LoadParam);
        LoadPage.setBackgroundColor(ContextCompat.getColor(App.GetContext(), R.color.White));
        LoadPage.setVisibility(View.VISIBLE);

        Root.addView(LoadPage);

        RelativeLayout.LayoutParams LoadingPageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LoadingPageParam.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        LoadingPageParam.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);

        LoadingPage = new LoadingView(App.GetContext());
        LoadingPage.setLayoutParams(LoadingPageParam);
        LoadingPage.SetColor(R.color.BlueGray2);

        Root.addView(LoadingPage);

        RelativeLayout.LayoutParams TryPageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TryPageParam.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        TryPageParam.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);

        TryPage = new TextView(App.GetContext());
        TryPage.setLayoutParams(TryPageParam);
        TryPage.setTextColor(ContextCompat.getColor(App.GetContext(), R.color.Black));
        TryPage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        TryPage.setTypeface(null, Typeface.BOLD);
        TryPage.setText(getString(R.string.GeneralTryAgain));
        TryPage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                RetrieveDataFromServer();
            }
        });

        Root.addView(TryPage);

        RetrieveDataFromServer();

        return Root;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        AndroidNetworking.cancel("FragmentPostDetails");
    }

    private void RetrieveDataFromServer()
    {
        LoadingPage.Start();
        TryPage.setVisibility(View.GONE);

        AndroidNetworking.post(URLHandler.GetURL(URLHandler.URL.POST_DETAILS))
        .addBodyParameter("PostID", ((getArguments() == null) ? "" : getArguments().getString("PostID", "")))
        .addHeaders("TOKEN", SharedHandler.GetString("TOKEN"))
        .setTag("FragmentPostDetails").build().getAsString(new StringRequestListener()
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
                            MiscHandler.LoadImage(Profile, "FragmentPostDetails", Result.getString("Avatar"), MiscHandler.DpToPx(55), MiscHandler.DpToPx(55));

                        Username.setText(Result.getString("Username"));
                        Time.setText(MiscHandler.GetTime(Result.getLong("Time")));

                        if (!Result.getString("Message").equals(""))
                        {
                            if (new Bidi(Result.getString("Message"), Bidi.DIRECTION_DEFAULT_LEFT_TO_RIGHT).getBaseLevel() == 0)
                            {
                                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) Message.getLayoutParams();
                                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                                Message.setLayoutParams(params);
                            }
                            else
                            {
                                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) Message.getLayoutParams();
                                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                                Message.setLayoutParams(params);
                            }

                            Message.setVisibility(View.VISIBLE);
                            Message.setText(Result.getString("Message"));

                            new TagHandler(Message, new TagHandler.OnTagClickListener()
                            {
                                @Override
                                public void OnTagClicked(String Tag, int Type)
                                {
                                    MiscHandler.Toast(Tag);
                                }
                            });
                        }

                        if (Result.getInt("Type") == 1)
                        {
                            try
                            {
                                final JSONArray URL = new JSONArray(Result.getString("Data"));

                                switch (URL.length())
                                {
                                    case 1:
                                        ContentSingle.setVisibility(View.VISIBLE);
                                        Single.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { try { OpenPreviewImage(URL.get(0).toString(), null, null); } catch (Exception e) { /* Leave Me Alone */ } } });
                                        MiscHandler.LoadImage(Single, "FragmentPostDetails", URL.get(0).toString());
                                        break;
                                    case 2:
                                        ContentDouble.setVisibility(View.VISIBLE);
                                        Double1.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { try { OpenPreviewImage(URL.get(0).toString(), URL.get(1).toString(), null); } catch (Exception e) { /* Leave Me Alone */ } } });
                                        Double2.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { try { OpenPreviewImage(URL.get(1).toString(), URL.get(0).toString(), null); } catch (Exception e) { /* Leave Me Alone */ } } });
                                        MiscHandler.LoadImage(Double1, "FragmentPostDetails", URL.get(0).toString());
                                        MiscHandler.LoadImage(Double2, "FragmentPostDetails", URL.get(1).toString());
                                        break;
                                    case 3:
                                        ContentTriple.setVisibility(View.VISIBLE);
                                        Triple1.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { try { OpenPreviewImage(URL.get(0).toString(), URL.get(1).toString(), URL.get(2).toString()); } catch (Exception e) { /* Leave Me Alone */ } } });
                                        Triple2.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { try { OpenPreviewImage(URL.get(1).toString(), URL.get(2).toString(), URL.get(0).toString()); } catch (Exception e) { /* Leave Me Alone */ } } });
                                        Triple3.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { try { OpenPreviewImage(URL.get(2).toString(), URL.get(0).toString(), URL.get(1).toString()); } catch (Exception e) { /* Leave Me Alone */ } } });
                                        MiscHandler.LoadImage(Triple1, "FragmentPostDetails", URL.get(0).toString());
                                        MiscHandler.LoadImage(Triple2, "FragmentPostDetails", URL.get(1).toString());
                                        MiscHandler.LoadImage(Triple3, "FragmentPostDetails", URL.get(2).toString());
                                        break;
                                }
                            }
                            catch (Exception e)
                            {
                                // Leave Me Alone
                            }
                        }
                        else if (Result.getInt("Type") == 2)
                        {
                            // Fill Me Later
                            Result.getInt("Type");
                        }
                        else if (Result.getInt("Type") == 3)
                        {
                            ContentLink.setVisibility(View.VISIBLE);
                            Loading.Start();

                            try
                            {
                                JSONArray URL = new JSONArray(Result.getString("Data"));

                                final TextCrawler Request = new TextCrawler(URL.get(0).toString(), "FragmentPostDetails", new TextCrawler.TextCrawlerCallBack()
                                {
                                    @Override
                                    public void OnCompleted(TextCrawler.URLContent Content)
                                    {
                                        if (new Bidi(Content.Title, Bidi.DIRECTION_DEFAULT_LEFT_TO_RIGHT).getBaseLevel() == 0)
                                        {
                                            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) Website.getLayoutParams();
                                            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                                            Website.setLayoutParams(params);
                                        }
                                        else
                                        {
                                            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) Website.getLayoutParams();
                                            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                                            Website.setLayoutParams(params);
                                        }

                                        if (new Bidi(Content.Title, Bidi.DIRECTION_DEFAULT_LEFT_TO_RIGHT).getBaseLevel() == 0)
                                        {
                                            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) Description.getLayoutParams();
                                            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                                            Description.setLayoutParams(params);
                                        }
                                        else
                                        {
                                            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) Description.getLayoutParams();
                                            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                                            Description.setLayoutParams(params);
                                        }

                                        Website.setText(Content.Title);
                                        Description.setText(Content.Description);
                                        Loading.Stop();
                                        Fav.setVisibility(View.VISIBLE);

                                        MiscHandler.LoadImage(Fav, "FragmentPostDetails", Content.Image);
                                    }

                                    @Override
                                    public void OnFailed()
                                    {
                                        Loading.Stop();
                                        Try.setVisibility(View.VISIBLE);
                                    }
                                });

                                Try.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        Request.Start();
                                        Loading.Start();
                                    }
                                });

                                Request.Start();
                            }
                            catch (Exception e)
                            {
                                // Leave Me Alone
                            }
                        }

                        LikeCount.setText(String.valueOf(Result.getInt("LikeCount")));
                        CommentCount.setText(String.valueOf(Result.getInt("CommentCount")));

                        PostID = Result.getString("PostID");
                        OwnerID = Result.getString("OwnerID");
                        IsLike = Result.getBoolean("Like");
                        IsComment = Result.getBoolean("Comment");
                        IsBookMark = Result.getBoolean("BookMark");

                        if (IsLike)
                            IconLike.setImageResource(R.drawable.ic_like_red);

                        if (IsBookMark)
                            BookMark.setImageResource(R.drawable.ic_bookmark_black2);

                        Option.setVisibility(View.VISIBLE);
                        BookMark.setVisibility(View.VISIBLE);
                    }
                }
                catch (Exception e)
                {
                    // Leave Me Alone
                }

                LoadingPage.Stop();
                TryPage.setVisibility(View.GONE);
                LoadPage.setVisibility(View.GONE);
            }

            @Override
            public void onError(ANError e)
            {
                LoadingPage.Stop();
                TryPage.setVisibility(View.VISIBLE);
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

        Fragment fragment = new FragmentImagePreview();
        fragment.setArguments(bundle);

        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.FullScreenContainer, fragment).addToBackStack("FragmentImagePreview").commit();
    }
}