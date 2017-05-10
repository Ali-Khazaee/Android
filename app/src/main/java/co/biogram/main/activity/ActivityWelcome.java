package co.biogram.main.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Selection;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONObject;

import co.biogram.main.BuildConfig;
import co.biogram.main.R;
import co.biogram.main.handler.MiscHandler;
import co.biogram.main.handler.RequestHandler;
import co.biogram.main.handler.SharedHandler;
import co.biogram.main.handler.URLHandler;
import co.biogram.main.misc.LoadingView;

public class ActivityWelcome extends FragmentActivity
{
    private String Username;
    private String Password;

    private GoogleApiClient GoogleClient;
    private int FrameLayoutContainerID = MiscHandler.GenerateViewID();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Context context = this;

        if (SharedHandler.GetBoolean(context, "IsLogin"))
        {
            startActivity(new Intent(ActivityWelcome.this, ActivityMain.class));
            finish();
            return;
        }

        if (Build.VERSION.SDK_INT > 20)
            getWindow().setStatusBarColor(ContextCompat.getColor(context, R.color.BlueLight));

        ScrollView Root = new ScrollView(context);
        Root.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        Root.setBackgroundResource(R.color.White);
        Root.setHorizontalScrollBarEnabled(false);
        Root.setVerticalScrollBarEnabled(false);
        Root.setFillViewport(true);

        RelativeLayout RelativeLayoutMain = new RelativeLayout(context);
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

        Root.addView(RelativeLayoutMain);

        View ViewHeader = new View(context);
        ViewHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 170)));
        ViewHeader.setBackgroundResource(R.color.BlueLight);
        ViewHeader.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain.addView(ViewHeader);

        LinearLayout LinearLayoutHeader = new LinearLayout(context);
        LinearLayoutHeader.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 170)));
        LinearLayoutHeader.setOrientation(LinearLayout.VERTICAL);
        LinearLayoutHeader.setGravity(Gravity.CENTER);

        RelativeLayoutMain.addView(LinearLayoutHeader);

        ImageView ImageViewHeader = new ImageView(context);
        ImageViewHeader.setLayoutParams(new LinearLayout.LayoutParams(MiscHandler.ToDimension(context, 150), MiscHandler.ToDimension(context, 65)));
        ImageViewHeader.setImageResource(R.drawable.ic_logo);

        LinearLayoutHeader.addView(ImageViewHeader);

        LinearLayout.LayoutParams TextViewHeaderParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        TextViewHeaderParam.setMargins(0, MiscHandler.ToDimension(context, 10), 0, 0);

        TextView TextViewHeader = new TextView(context);
        TextViewHeader.setLayoutParams(TextViewHeaderParam);
        TextViewHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        TextViewHeader.setText(getString(R.string.ActivityWelcomeHeader));
        TextViewHeader.setTextColor(ContextCompat.getColor(context, R.color.White));

        LinearLayoutHeader.addView(TextViewHeader);

        LinearLayout.LayoutParams TextViewHeader2Param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        TextViewHeader2Param.setMargins(0, MiscHandler.ToDimension(context, 5), 0, 0);

        TextView TextViewHeader2 = new TextView(context);
        TextViewHeader2.setLayoutParams(TextViewHeader2Param);
        TextViewHeader2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        TextViewHeader2.setText(getString(R.string.ActivityWelcomeHeader2));
        TextViewHeader2.setTextColor(ContextCompat.getColor(context, R.color.White));

        LinearLayoutHeader.addView(TextViewHeader2);

        RelativeLayout.LayoutParams ButtonSignUpParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 270), MiscHandler.ToDimension(context, 45));
        ButtonSignUpParam.setMargins(0, MiscHandler.ToDimension(context, 30), 0, 0);
        ButtonSignUpParam.addRule(RelativeLayout.BELOW, ViewHeader.getId());
        ButtonSignUpParam.addRule(RelativeLayout.CENTER_HORIZONTAL);

        GradientDrawable ShapeButtonSignUp = new GradientDrawable();
        ShapeButtonSignUp.setColor(ContextCompat.getColor(context, R.color.BlueLight));
        ShapeButtonSignUp.setCornerRadius(MiscHandler.ToDimension(context, 7));

        Button ButtonSignUp = new Button(context, null, android.R.attr.borderlessButtonStyle);
        ButtonSignUp.setLayoutParams(ButtonSignUpParam);
        ButtonSignUp.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        ButtonSignUp.setTextColor(ContextCompat.getColor(context, R.color.White));
        ButtonSignUp.setText(getString(R.string.ActivityWelcomeSignUp));
        ButtonSignUp.setId(MiscHandler.GenerateViewID());
        ButtonSignUp.setBackground(ShapeButtonSignUp);
        ButtonSignUp.setAllCaps(false);
        ButtonSignUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getSupportFragmentManager().beginTransaction().replace(FrameLayoutContainerID, new FragmentSignUpUsername()).addToBackStack("FragmentSignUpUsername").commit();
            }
        });

        RelativeLayoutMain.addView(ButtonSignUp);

        RelativeLayout.LayoutParams RelativeLayoutORParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 45));
        RelativeLayoutORParam.setMargins(0, MiscHandler.ToDimension(context, 20), 0, MiscHandler.ToDimension(context, 5));
        RelativeLayoutORParam.addRule(RelativeLayout.BELOW, ButtonSignUp.getId());

        RelativeLayout RelativeLayoutOR = new RelativeLayout(context);
        RelativeLayoutOR.setLayoutParams(RelativeLayoutORParam);
        RelativeLayoutOR.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain.addView(RelativeLayoutOR);

        RelativeLayout.LayoutParams TextViewORParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 40), RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewORParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        TextView TextViewOR = new TextView(context);
        TextViewOR.setLayoutParams(TextViewORParam);
        TextViewOR.setTypeface(null, Typeface.BOLD);
        TextViewOR.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        TextViewOR.setText(getString(R.string.ActivityWelcomeOR));
        TextViewOR.setId(MiscHandler.GenerateViewID());
        TextViewOR.setGravity(Gravity.CENTER);
        TextViewOR.setTextColor(ContextCompat.getColor(context, R.color.BlueGray));

        RelativeLayoutOR.addView(TextViewOR);

        RelativeLayout.LayoutParams ViewOrLine1Param = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 115), MiscHandler.ToDimension(context, 1));
        ViewOrLine1Param.addRule(RelativeLayout.RIGHT_OF, TextViewOR.getId());
        ViewOrLine1Param.addRule(RelativeLayout.CENTER_VERTICAL);

        View ViewOrLine1 = new View(context);
        ViewOrLine1.setLayoutParams(ViewOrLine1Param);
        ViewOrLine1.setBackgroundResource(R.color.BlueGray);

        RelativeLayoutOR.addView(ViewOrLine1);

        RelativeLayout.LayoutParams ViewOrLine2Param = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 115), MiscHandler.ToDimension(context, 1));
        ViewOrLine2Param.addRule(RelativeLayout.LEFT_OF, TextViewOR.getId());
        ViewOrLine2Param.addRule(RelativeLayout.CENTER_VERTICAL);

        View ViewOrLine2 = new View(context);
        ViewOrLine2.setLayoutParams(ViewOrLine2Param);
        ViewOrLine2.setBackgroundResource(R.color.BlueGray);

        RelativeLayoutOR.addView(ViewOrLine2);

        RelativeLayout.LayoutParams LinearLayoutGoogleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LinearLayoutGoogleParam.addRule(RelativeLayout.BELOW, RelativeLayoutOR.getId());
        LinearLayoutGoogleParam.addRule(RelativeLayout.CENTER_HORIZONTAL);

        LinearLayout LinearLayoutGoogle = new LinearLayout(context);
        LinearLayoutGoogle.setLayoutParams(LinearLayoutGoogleParam);
        LinearLayoutGoogle.setGravity(Gravity.CENTER);
        LinearLayoutGoogle.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayoutGoogle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivityForResult(Auth.GoogleSignInApi.getSignInIntent(GoogleClient), 100);
            }
        });

        RelativeLayoutMain.addView(LinearLayoutGoogle);

        LinearLayout.LayoutParams ImageViewGoogleParam = new LinearLayout.LayoutParams(MiscHandler.ToDimension(context, 30), MiscHandler.ToDimension(context, 30));
        ImageViewGoogleParam.setMargins(MiscHandler.ToDimension(5), 0, MiscHandler.ToDimension(5), 0);

        ImageView ImageViewGoogle = new ImageView(context);
        ImageViewGoogle.setLayoutParams(ImageViewGoogleParam);
        ImageViewGoogle.setBackgroundResource(R.drawable.ic_google);

        LinearLayoutGoogle.addView(ImageViewGoogle);

        TextView TextViewGoogle = new TextView(context);
        TextViewGoogle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        TextViewGoogle.setTextColor(ContextCompat.getColor(context, R.color.BlueLight));
        TextViewGoogle.setTypeface(null, Typeface.BOLD);
        TextViewGoogle.setText(getString(R.string.ActivityWelcomeSignInGoogle));
        TextViewGoogle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

        LinearLayoutGoogle.addView(TextViewGoogle);

        RelativeLayout.LayoutParams LinearLayoutSignInParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56));
        LinearLayoutSignInParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        LinearLayout LinearLayoutSignIn = new LinearLayout(context);
        LinearLayoutSignIn.setLayoutParams(LinearLayoutSignInParam);
        LinearLayoutSignIn.setBackgroundResource(R.color.White5);
        LinearLayoutSignIn.setGravity(Gravity.CENTER);
        LinearLayoutSignIn.setId(MiscHandler.GenerateViewID());
        LinearLayoutSignIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getSupportFragmentManager().beginTransaction().replace(FrameLayoutContainerID, new SignInFragment()).commit();
            }
        });

        RelativeLayoutMain.addView(LinearLayoutSignIn);

        TextView TextViewSignIn = new TextView(context);
        TextViewSignIn.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewSignIn.setTextColor(ContextCompat.getColor(context, R.color.Black));
        TextViewSignIn.setText(getString(R.string.ActivityWelcomeSignIn));
        TextViewSignIn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

        LinearLayoutSignIn.addView(TextViewSignIn);

        LinearLayout.LayoutParams TextViewSignIn2Param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        TextViewSignIn2Param.setMargins(MiscHandler.ToDimension(context, 5), 0, MiscHandler.ToDimension(context, 5), 0);

        TextView TextViewSignIn2 = new TextView(context);
        TextViewSignIn2.setLayoutParams(TextViewSignIn2Param);
        TextViewSignIn2.setTextColor(ContextCompat.getColor(context, R.color.BlueLight));
        TextViewSignIn2.setText(getString(R.string.ActivityWelcomeSignIn2));
        TextViewSignIn2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

        LinearLayoutSignIn.addView(TextViewSignIn2);

        RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1));
        ViewLineParam.addRule(RelativeLayout.ABOVE, LinearLayoutSignIn.getId());

        View ViewLine = new View(context);
        ViewLine.setLayoutParams(ViewLineParam);
        ViewLine.setBackgroundResource(R.color.Gray2);
        ViewLine.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain.addView(ViewLine);

        RelativeLayout.LayoutParams TextViewTerm2Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTerm2Param.addRule(RelativeLayout.ABOVE, ViewLine.getId());
        TextViewTerm2Param.addRule(RelativeLayout.CENTER_HORIZONTAL);
        TextViewTerm2Param.setMargins(0, 0, 0, MiscHandler.ToDimension(context, 20));

        TextView TextViewTerm2 = new TextView(context);
        TextViewTerm2.setLayoutParams(TextViewTerm2Param);
        TextViewTerm2.setTextColor(ContextCompat.getColor(context, R.color.BlueGray));
        TextViewTerm2.setText(getString(R.string.ActivityWelcomeTerm2));
        TextViewTerm2.setTypeface(null, Typeface.BOLD);
        TextViewTerm2.setId(MiscHandler.GenerateViewID());
        TextViewTerm2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        TextViewTerm2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com")));
            }
        });

        RelativeLayoutMain.addView(TextViewTerm2);

        RelativeLayout.LayoutParams TextViewTermParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTermParam.addRule(RelativeLayout.ABOVE, TextViewTerm2.getId());
        TextViewTermParam.addRule(RelativeLayout.CENTER_HORIZONTAL);

        TextView TextViewTerm = new TextView(context);
        TextViewTerm.setLayoutParams(TextViewTermParam);
        TextViewTerm.setTextColor(ContextCompat.getColor(context, R.color.BlueGray));
        TextViewTerm.setText(getString(R.string.ActivityWelcomeTerm));
        TextViewTerm.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

        RelativeLayoutMain.addView(TextViewTerm);

        FrameLayout FrameLayoutContainer = new FrameLayout(context);
        FrameLayoutContainer.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        FrameLayoutContainer.setId(FrameLayoutContainerID);

        RelativeLayoutMain.addView(FrameLayoutContainer);

        setContentView(Root);

        GoogleSignInOptions GoogleOption = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestProfile().requestEmail().requestId().requestIdToken("590625045379-9pgbc6r8v0794rij59jj50o1gp6ijnvl.apps.googleusercontent.com").build();
        GoogleClient = new GoogleApiClient.Builder(this).enableAutoManage(this, null).addApi(Auth.GOOGLE_SIGN_IN_API, GoogleOption).build();

        if (GoogleClient.isConnected())
            Auth.GoogleSignInApi.signOut(GoogleClient);
    }

    @Override
    public void onActivityResult(int RequestCode, int ResultCode, Intent Data)
    {
        if (RequestCode == 100)
        {
            GoogleSignInResult Result = Auth.GoogleSignInApi.getSignInResultFromIntent(Data);

            if (Result.isSuccess())
            {
                GoogleSignInAccount Acc = Result.getSignInAccount();

                if (Acc != null)
                {
                    RequestHandler.Core().Method("POST")
                    .Address(URLHandler.GetURL(URLHandler.URL.SIGN_IN_GOOGLE))
                    .Param("Token", Acc.getIdToken())
                    .Param("Session", GenerateSession())
                    .Tag("ActivityWelcome")
                    .Build(new RequestHandler.OnCompleteCallBack()
                    {
                        @Override
                        public void OnFinish(String Response, int Status)
                        {
                            Context context = ActivityWelcome.this;

                            if (Status != 200)
                            {
                                MiscHandler.Toast(context, getString(R.string.NoInternet));
                                return;
                            }

                            try
                            {
                                JSONObject Result = new JSONObject(Response);

                                if (Result.getInt("Message") == 1000)
                                {
                                    SharedHandler.SetBoolean(context, "IsLogin", true);
                                    SharedHandler.SetString(context, "TOKEN", Result.getString("Token"));
                                    SharedHandler.SetString(context, "ID", Result.getString("AccountID"));
                                    SharedHandler.SetString(context, "Username", Result.getString("AccountID")); // TODO ino Check Kon Samte Server Bebin Hamin Tori e Dorost e Ya na
                                    SharedHandler.SetString(context, "Avatar", Result.getString("Avatar"));

                                    // TODO Fragment e Entekhabe Profile o Bezar Inja
                                    return;
                                }

                                MiscHandler.Toast(context, getString(R.string.ActivityWelcomeGoogleError));
                            }
                            catch (Exception e)
                            {
                                // Leave Me Alone
                            }
                        }
                    });
                }

                if (GoogleClient.isConnected())
                    Auth.GoogleSignInApi.signOut(GoogleClient);
            }
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();
        GoogleClient.disconnect();
        MiscHandler.HideSoftKey(this);
        RequestHandler.Core().Cancel("ActivityWelcome");
    }

    public static class FragmentSignUpUsername extends Fragment
    {
        private InputMethodManager IMM;
        private EditText EditTextUsername;

        private int LastDifferenceHeight = 0;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup Parent, Bundle savedInstanceState)
        {
            final Context context = getActivity();

            final ScrollView Root = new ScrollView(context);
            Root.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            Root.setBackgroundResource(R.color.White);
            Root.setHorizontalScrollBarEnabled(false);
            Root.setVerticalScrollBarEnabled(false);
            Root.setFillViewport(true);
            Root.setClickable(true);
            Root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
            {
                @Override
                public void onGlobalLayout()
                {
                    Rect rect = new Rect();
                    Root.getWindowVisibleDisplayFrame(rect);

                    int ScreenHeight = Root.getHeight();
                    int DifferenceHeight = ScreenHeight - (rect.bottom - rect.top);

                    if (DifferenceHeight > ScreenHeight / 3 && DifferenceHeight != LastDifferenceHeight)
                    {
                        Root.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, ScreenHeight - DifferenceHeight));
                        LastDifferenceHeight = DifferenceHeight;
                    }
                    else if (DifferenceHeight != LastDifferenceHeight)
                    {
                        Root.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, ScreenHeight));
                        LastDifferenceHeight = DifferenceHeight;
                    }
                }
            });

            RelativeLayout RelativeLayoutMain = new RelativeLayout(context);
            RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

            Root.addView(RelativeLayoutMain);

            RelativeLayout RelativeLayoutHeader = new RelativeLayout(context);
            RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56)));
            RelativeLayoutHeader.setBackgroundResource(R.color.BlueLight);
            RelativeLayoutHeader.setId(MiscHandler.GenerateViewID());

            RelativeLayoutMain.addView(RelativeLayoutHeader);

            ImageView ImageViewBack = new ImageView(context);
            ImageViewBack.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), RelativeLayout.LayoutParams.MATCH_PARENT));
            ImageViewBack.setScaleType(ImageView.ScaleType.FIT_XY);
            ImageViewBack.setId(MiscHandler.GenerateViewID());
            ImageViewBack.setImageResource(R.drawable.ic_back_white);
            ImageViewBack.setPadding(MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12));
            ImageViewBack.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    MiscHandler.HideSoftKey(getActivity());
                    getActivity().getSupportFragmentManager().beginTransaction().remove(FragmentSignUpUsername.this).commit();
                }
            });

            RelativeLayoutHeader.addView(ImageViewBack);

            RelativeLayout.LayoutParams TextViewHeaderParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewHeaderParam.addRule(RelativeLayout.RIGHT_OF, ImageViewBack.getId());
            TextViewHeaderParam.addRule(RelativeLayout.CENTER_VERTICAL);

            TextView TextViewHeader = new TextView(context);
            TextViewHeader.setLayoutParams(TextViewHeaderParam);
            TextViewHeader.setTextColor(ContextCompat.getColor(context, R.color.White));
            TextViewHeader.setText(getString(R.string.FragmentSignUpUsernameTitle));
            TextViewHeader.setTypeface(null, Typeface.BOLD);
            TextViewHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

            RelativeLayoutHeader.addView(TextViewHeader);

            RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1));
            ViewLineParam.addRule(RelativeLayout.BELOW, RelativeLayoutHeader.getId());

            View ViewLine = new View(context);
            ViewLine.setLayoutParams(ViewLineParam);
            ViewLine.setBackgroundResource(R.color.Gray2);
            ViewLine.setId(MiscHandler.GenerateViewID());

            RelativeLayoutMain.addView(ViewLine);

            RelativeLayout.LayoutParams TextViewUsernameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewUsernameParam.addRule(RelativeLayout.BELOW, ViewLine.getId());
            TextViewUsernameParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));

            TextView TextViewUsername = new TextView(context);
            TextViewUsername.setLayoutParams(TextViewUsernameParam);
            TextViewUsername.setTextColor(ContextCompat.getColor(context, R.color.Gray4));
            TextViewUsername.setText(getString(R.string.FragmentSignUpUsername));
            TextViewUsername.setTypeface(null, Typeface.BOLD);
            TextViewUsername.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            TextViewUsername.setId(MiscHandler.GenerateViewID());

            RelativeLayoutMain.addView(TextViewUsername);

            RelativeLayout.LayoutParams EditTextUsernameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            EditTextUsernameParam.addRule(RelativeLayout.BELOW, TextViewUsername.getId());
            EditTextUsernameParam.setMargins(MiscHandler.ToDimension(context, 10), 0, MiscHandler.ToDimension(context, 10), 0);

            EditTextUsername = new EditText(new ContextThemeWrapper(context, R.style.GeneralEditTextTheme));
            EditTextUsername.setLayoutParams(EditTextUsernameParam);
            EditTextUsername.setMaxLines(1);
            EditTextUsername.setText("@A");
            EditTextUsername.setId(MiscHandler.GenerateViewID());
            EditTextUsername.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            EditTextUsername.setFilters(new InputFilter[]
            {
                new InputFilter.LengthFilter(32), new InputFilter()
                {
                    @Override
                    public CharSequence filter(CharSequence Source, int Start, int End, Spanned Dest, int DestStart, int DestEnd)
                    {
                        if (End > Start)
                        {
                            char[] AllowChar = new char[] {'a', 'b', 'c', 'd', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '.', '_', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

                            for (int I = Start; I < End; I++)
                            {
                                if (!new String(AllowChar).contains(String.valueOf(Source.charAt(I))))
                                {
                                    return "";
                                }
                            }
                        }

                        return null;
                    }
                },
                new InputFilter()
                {
                    @Override
                    public CharSequence filter(final CharSequence source, final int start, final int end, final Spanned dest, final int dstart, final int dend)
                    {
                        final int newStart = Math.max(2, dstart);
                        final int newEnd = Math.max(2, dend);

                        if (newStart != dstart || newEnd != dend)
                        {
                            final SpannableStringBuilder builder = new SpannableStringBuilder(dest);
                            builder.replace(newStart, newEnd, source);

                            if (source instanceof Spanned)
                            {
                                TextUtils.copySpansFrom((Spanned) source, 0, source.length(), null, builder, newStart);
                            }

                            Selection.setSelection(builder, newStart + source.length());
                            return builder;
                        }
                        else
                        {
                            return null;
                        }
                    }
                }
            });
            EditTextUsername.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            EditTextUsername.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.BlueLight), PorterDuff.Mode.SRC_ATOP);
            EditTextUsername.requestFocus();
            EditTextUsername.addTextChangedListener(new TextWatcher()
            {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                @Override
                public void afterTextChanged(Editable s) { }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count)
                {
                    //if (s.length() > 2)
                        //ButtonNext.setEnabled(true);
                    //else
                        //ButtonNext.setEnabled(false);
                }
            });

            IMM = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            IMM.showSoftInput(EditTextUsername, 0);
            IMM.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);

            RelativeLayoutMain.addView(EditTextUsername);
























            return Root;
/*

            final LoadingView LoadingViewUsername = (LoadingView) RootView.findViewById(R.id.LoadingViewUsername);
            final Button ButtonNext = (Button) RootView.findViewById(R.id.ButtonNext);

            ButtonNext.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(final View v)
                {
                    LoadingViewUsername.Start();
                    ButtonNext.setVisibility(View.INVISIBLE);

                    AndroidNetworking.post(URLHandler.GetURL(URLHandler.URL.USERNAME_IS_AVAILABLE)).addBodyParameter("Username", EditTextUsername.getText().toString()).setTag("SignUpUsernameFragment").build().getAsString(new StringRequestListener()
                    {
                        @Override
                        public void onResponse(String Response)
                        {
                            try
                            {
                                if (new JSONObject(Response).getInt("Message") == 1000)
                                {
                                    //Username = EditTextUsername.getText().toString();
                                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.FrameLayoutContainer, new SignUpPasswordFragment()).addToBackStack("SignUpUsernameFragment").commit();
                                }
                                else
                                    MiscHandler.Toast(context, getString(R.string.ActivityWelcomeFragmentUsernameError));
                            }
                            catch (Exception e)
                            {
                                // Leave Me Alone
                            }

                            LoadingViewUsername.Stop();
                            ButtonNext.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError(ANError error)
                        {
                            LoadingViewUsername.Stop();
                            ButtonNext.setVisibility(View.VISIBLE);
                            MiscHandler.Toast(context, getString(R.string.GeneralCheckInternet));
                        }
                    });
                }
            });

            RootView.findViewById(R.id.TextViewPrivacy).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com")));
                }
            });

            */
        }

        @Override
        public void onResume()
        {
            super.onResume();

            if (IMM != null && EditTextUsername != null)
            {
                EditTextUsername.requestFocus();
                IMM.showSoftInput(EditTextUsername, 0);
                IMM.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        }

        @Override
        public void onPause()
        {
            super.onPause();
            RequestHandler.Core().Cancel("FragmentSignUpUsername");
        }
    }

    public static class SignUpPasswordFragment extends Fragment
    {
        private InputMethodManager IMM;
        private EditText EditTextPassword;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup Parent, Bundle savedInstanceState)
        {
            View RootView = inflater.inflate(R.layout.activity_welcome_fragment_password, Parent, false);

            RootView.findViewById(R.id.ImageViewBack).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    MiscHandler.HideSoftKey(getActivity());
                    getActivity().onBackPressed();
                }
            });

            final Button ButtonNext = (Button) RootView.findViewById(R.id.ButtonNext);
            ButtonNext.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    //Password = EditTextPassword.getText().toString();
                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.FrameLayoutContainer, new SignUpEmailFragment()).addToBackStack("SignUpPasswordFragment").commit();
                }
            });

            RootView.findViewById(R.id.TextViewPrivacy).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com")));
                }
            });

            EditTextPassword = (EditText) RootView.findViewById(R.id.EditTextPassword);
            EditTextPassword.requestFocus();
            EditTextPassword.addTextChangedListener(new TextWatcher()
            {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                @Override
                public void afterTextChanged(Editable s) { }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count)
                {
                    if (s.length() > 5)
                        ButtonNext.setEnabled(true);
                    else
                        ButtonNext.setEnabled(false);
                }
            });

            IMM = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            IMM.showSoftInput(EditTextPassword, 0);
            IMM.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);

            return RootView;
        }

        @Override
        public void onResume()
        {
            super.onResume();

            if (IMM != null && EditTextPassword != null)
            {
                EditTextPassword.requestFocus();
                IMM.showSoftInput(EditTextPassword, 0);
                IMM.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        }
    }

    public static class SignUpEmailFragment extends Fragment
    {
        private InputMethodManager IMM;
        private EditText EditTextEmail;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup Parent, Bundle savedInstanceState)
        {
            View RootView = inflater.inflate(R.layout.activity_welcome_fragment_email, Parent, false);

            final Context context = getActivity();

            RootView.findViewById(R.id.ImageViewBack).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    MiscHandler.HideSoftKey(getActivity());
                    getActivity().onBackPressed();
                }
            });

            final LoadingView LoadingViewEmail = (LoadingView) RootView.findViewById(R.id.LoadingViewEmail);
            final Button ButtonFinish = (Button) RootView.findViewById(R.id.ButtonFinish);

            ButtonFinish.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    ButtonFinish.setVisibility(View.INVISIBLE);
                    LoadingViewEmail.Start();

                    AndroidNetworking.post(URLHandler.GetURL(URLHandler.URL.SIGN_UP))
                    //.addBodyParameter("Username", Username)
                    //.addBodyParameter("Password", Password)
                    .addBodyParameter("Email", EditTextEmail.getText().toString())
                    .addBodyParameter("Session", GenerateSession())
                    .setTag("SignUpEmailFragment").build().getAsString(new StringRequestListener()
                    {
                        @Override
                        public void onResponse(String Response)
                        {
                            try
                            {
                                JSONObject Result = new JSONObject(Response);

                                if (Result.getInt("Message") == 1000)
                                {
                                    SharedHandler.SetBoolean("IsLogin", true);
                                    SharedHandler.SetString("TOKEN", Result.getString("TOKEN"));
                                    SharedHandler.SetString("ID", Result.getString("ID"));
                                    SharedHandler.SetString("Username", Result.getString("Username"));
                                    SharedHandler.SetString("Avatar", Result.getString("Avatar"));

                                    getActivity().startActivity(new Intent(getActivity(), ActivityMain.class));
                                    getActivity().finish();
                                }
                                else
                                    MiscHandler.Toast(context, getString(R.string.ActivityWelcomeFragmentEmailError));
                            }
                            catch (Exception e)
                            {
                                // Leave Me Alone
                            }

                            LoadingViewEmail.Stop();
                            ButtonFinish.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError(ANError error)
                        {
                            LoadingViewEmail.Stop();
                            ButtonFinish.setVisibility(View.VISIBLE);
                            MiscHandler.Toast(context, getString(R.string.GeneralCheckInternet));
                        }
                    });
                }
            });

            RootView.findViewById(R.id.TextViewPrivacy).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com")));
                }
            });

            EditTextEmail = (EditText) RootView.findViewById(R.id.EditTextEmail);
            EditTextEmail.requestFocus();
            EditTextEmail.addTextChangedListener(new TextWatcher()
            {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                @Override
                public void afterTextChanged(Editable s) { }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count)
                {
                    if (s.length() > 6 && Patterns.EMAIL_ADDRESS.matcher(s).matches())
                        ButtonFinish.setEnabled(true);
                    else
                        ButtonFinish.setEnabled(false);
                }
            });

            IMM = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            IMM.showSoftInput(EditTextEmail, 0);
            IMM.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);

            return RootView;
        }

        @Override
        public void onResume()
        {
            super.onResume();

            if (IMM != null && EditTextEmail != null)
            {
                EditTextEmail.requestFocus();
                IMM.showSoftInput(EditTextEmail, 0);
                IMM.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        }

        @Override
        public void onPause()
        {
            super.onPause();
            AndroidNetworking.cancel("SignUpEmailFragment");
        }
    }

    public static class SignInFragment extends Fragment
    {
        private InputMethodManager IMM;
        private EditText EmailOrUsername;
        private boolean RequestUsername = false;
        private boolean RequestPassword = false;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup Parent, Bundle savedInstanceState)
        {
            View RootView = inflater.inflate(R.layout.activity_welcome_fragment_signin, Parent, false);

            final Context context = getActivity();

            RootView.findViewById(R.id.ImageViewBack).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    MiscHandler.HideSoftKey(getActivity());
                    getActivity().onBackPressed();
                }
            });

            final LoadingView LoadingViewSignIn = (LoadingView) RootView.findViewById(R.id.LoadingViewSignIn);
            final Button ButtonSignIn = (Button) RootView.findViewById(R.id.ButtonSignIn);

            EmailOrUsername = (EditText) RootView.findViewById(R.id.EmailOrUsername);
            EmailOrUsername.requestFocus();
            EmailOrUsername.addTextChangedListener(new TextWatcher()
            {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                @Override
                public void afterTextChanged(Editable s) { }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count)
                {
                    RequestUsername = (s.length() > 2);

                    if (RequestUsername && RequestPassword)
                        ButtonSignIn.setEnabled(true);
                    else
                        ButtonSignIn.setEnabled(false);
                }
            });

            final EditText EditTextPassword = (EditText) RootView.findViewById(R.id.EditTextPassword);
            EditTextPassword.addTextChangedListener(new TextWatcher()
            {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                @Override
                public void afterTextChanged(Editable s) { }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count)
                {
                    RequestPassword = (s.length() > 5);

                    if (RequestUsername && RequestPassword)
                        ButtonSignIn.setEnabled(true);
                    else
                        ButtonSignIn.setEnabled(false);
                }
            });

            ButtonSignIn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    ButtonSignIn.setVisibility(View.INVISIBLE);
                    LoadingViewSignIn.Start();

                    AndroidNetworking.post(URLHandler.GetURL(URLHandler.URL.SIGN_IN))
                    .addBodyParameter("EmailOrUsername", EmailOrUsername.getText().toString())
                    .addBodyParameter("Password", EditTextPassword.getText().toString())
                    .addBodyParameter("Session", GenerateSession())
                    .setTag("SignInFragment").build().getAsString(new StringRequestListener()
                    {
                        @Override
                        public void onResponse(String Response)
                        {
                            try
                            {
                                JSONObject Result = new JSONObject(Response);

                                if (Result.getInt("Message") == 1000)
                                {
                                    SharedHandler.SetBoolean("IsLogin", true);
                                    SharedHandler.SetString("TOKEN", Result.getString("TOKEN"));
                                    SharedHandler.SetString("ID", Result.getString("ID"));
                                    SharedHandler.SetString("Username", Result.getString("Username"));
                                    SharedHandler.SetString("Avatar", Result.getString("Avatar"));

                                    getActivity().startActivity(new Intent(getActivity(), ActivityMain.class));
                                    getActivity().finish();
                                }
                                else
                                    MiscHandler.Toast(context, getString(R.string.ActivityWelcomeFragmentSignInError));
                            }
                            catch (Exception e)
                            {
                                // Leave Me Alone
                            }

                            LoadingViewSignIn.Stop();
                            ButtonSignIn.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError(ANError error)
                        {
                            LoadingViewSignIn.Stop();
                            ButtonSignIn.setVisibility(View.VISIBLE);
                            MiscHandler.Toast(context, getString(R.string.GeneralCheckInternet));
                        }
                    });
                }
            });

            RootView.findViewById(R.id.TextViewForgot).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.FrameLayoutContainer, new ResetFragment()).addToBackStack("ResetFragment").commit();
                }
            });

            IMM = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            IMM.showSoftInput(EmailOrUsername, 0);
            IMM.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);

            return RootView;
        }

        @Override
        public void onResume()
        {
            super.onResume();

            if (IMM != null && EmailOrUsername != null)
            {
                EmailOrUsername.requestFocus();
                IMM.showSoftInput(EmailOrUsername, 0);
                IMM.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        }

        @Override
        public void onPause()
        {
            super.onPause();
            AndroidNetworking.cancel("SignInFragment");
        }
    }

    public static class ResetFragment extends Fragment
    {
        private InputMethodManager IMM;
        private EditText EditTextEmailOrUsername;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup Parent, Bundle savedInstanceState)
        {
            View RootView = inflater.inflate(R.layout.activity_welcome_fragment_reset, Parent, false);

            RootView.findViewById(R.id.ImageViewBack).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    MiscHandler.HideSoftKey(getActivity());
                    getActivity().onBackPressed();
                }
            });

            final Button ButtonSubmit = (Button) RootView.findViewById(R.id.ButtonSubmit);
            final LinearLayout LinearLayoutLoading = (LinearLayout) RootView.findViewById(R.id.LinearLayoutLoading);
            final LoadingView LoadingViewReset = (LoadingView) RootView.findViewById(R.id.LoadingViewReset);

            EditTextEmailOrUsername = (EditText) RootView.findViewById(R.id.EditTextEmailOrUsername);
            EditTextEmailOrUsername.requestFocus();
            EditTextEmailOrUsername.addTextChangedListener(new TextWatcher()
            {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                @Override
                public void afterTextChanged(Editable s) { }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count)
                {
                    ButtonSubmit.setEnabled((s.length() > 2));
                }
            });

            final Context context = getActivity();

            ButtonSubmit.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    ButtonSubmit.setVisibility(View.INVISIBLE);
                    LinearLayoutLoading.setVisibility(View.VISIBLE);
                    LoadingViewReset.Start();

                    AndroidNetworking.post(URLHandler.GetURL(URLHandler.URL.RESET_PASSWORD))
                    .addBodyParameter("EmailOrUsername", EditTextEmailOrUsername.getText().toString())
                    .setTag("ResetFragment").build().getAsString(new StringRequestListener()
                    {
                        @Override
                        public void onResponse(String Response)
                        {
                            try
                            {
                                if (new JSONObject(Response).getInt("Message") == 1000)
                                    MiscHandler.Toast(context, getString(R.string.ActivityWelcomeFragmentResetSuccess));
                                else
                                    MiscHandler.Toast(context, getString(R.string.ActivityWelcomeFragmentResetError));
                            }
                            catch (Exception e)
                            {
                                // Leave Me Alone
                            }

                            LoadingViewReset.Stop();
                            LinearLayoutLoading.setVisibility(View.INVISIBLE);
                            ButtonSubmit.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError(ANError error)
                        {
                            LoadingViewReset.Stop();
                            LinearLayoutLoading.setVisibility(View.INVISIBLE);
                            ButtonSubmit.setVisibility(View.VISIBLE);
                            MiscHandler.Toast(context, getString(R.string.GeneralCheckInternet));
                        }
                    });
                }
            });

            IMM = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            IMM.showSoftInput(EditTextEmailOrUsername, 0);
            IMM.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);

            return RootView;
        }

        @Override
        public void onResume()
        {
            super.onResume();

            if (IMM != null && EditTextEmailOrUsername != null)
            {
                EditTextEmailOrUsername.requestFocus();
                IMM.showSoftInput(EditTextEmailOrUsername, 0);
                IMM.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        }

        @Override
        public void onPause()
        {
            super.onPause();
            AndroidNetworking.cancel("ResetFragment");
        }
    }

    private static String GenerateSession()
    {
        return "BioGram Android " + BuildConfig.VERSION_NAME + " - " + Build.MODEL + " - " + Build.MANUFACTURER + " - SDK " + Build.VERSION.SDK_INT;
    }
}
