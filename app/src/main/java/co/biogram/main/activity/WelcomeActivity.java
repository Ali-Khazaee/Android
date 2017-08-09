package co.biogram.main.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
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
import co.biogram.main.handler.SharedHandler;
import co.biogram.main.misc.LoadingView;

public class WelcomeActivity extends FragmentActivity
{
    private GoogleApiClient GoogleClientApi;

    private String Username = "";
    private String Password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Context context = this;

        if (SharedHandler.GetBoolean(context, "IsLogin"))
        {
            startActivity(new Intent(WelcomeActivity.this, ActivityMain.class));
            finish();
            return;
        }

        if (Build.VERSION.SDK_INT > 20)
            getWindow().setStatusBarColor(ContextCompat.getColor(context, R.color.BlueLight));

        RelativeLayout RelativeLayoutMain = new RelativeLayout(context);
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

        ScrollView ScrollViewMain = new ScrollView(context);
        ScrollViewMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        ScrollViewMain.setBackgroundResource(R.color.White);
        ScrollViewMain.setFillViewport(true);

        RelativeLayoutMain.addView(ScrollViewMain);

        RelativeLayout RelativeLayoutMain2 = new RelativeLayout(context);
        RelativeLayoutMain2.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

        ScrollViewMain.addView(RelativeLayoutMain2);

        View ViewHeader = new View(context);
        ViewHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 170)));
        ViewHeader.setBackgroundResource(R.color.BlueLight);
        ViewHeader.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain2.addView(ViewHeader);

        LinearLayout LinearLayoutHeader = new LinearLayout(context);
        LinearLayoutHeader.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 170)));
        LinearLayoutHeader.setOrientation(LinearLayout.VERTICAL);
        LinearLayoutHeader.setGravity(Gravity.CENTER);

        RelativeLayoutMain2.addView(LinearLayoutHeader);

        ImageView ImageViewHeader = new ImageView(context);
        ImageViewHeader.setLayoutParams(new LinearLayout.LayoutParams(MiscHandler.ToDimension(context, 150), MiscHandler.ToDimension(context, 65)));
        ImageViewHeader.setImageResource(R.drawable.ic_logo);

        LinearLayoutHeader.addView(ImageViewHeader);

        LinearLayout.LayoutParams TextViewHeaderParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        TextViewHeaderParam.setMargins(0, MiscHandler.ToDimension(context, 10), 0, 0);

        TextView TextViewHeader = new TextView(context);
        TextViewHeader.setLayoutParams(TextViewHeaderParam);
        TextViewHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        TextViewHeader.setText(getString(R.string.WelcomeActivityHeader));
        TextViewHeader.setTextColor(ContextCompat.getColor(context, R.color.White));

        LinearLayoutHeader.addView(TextViewHeader);

        LinearLayout.LayoutParams TextViewHeader2Param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        TextViewHeader2Param.setMargins(0, MiscHandler.ToDimension(context, 5), 0, 0);

        TextView TextViewHeader2 = new TextView(context);
        TextViewHeader2.setLayoutParams(TextViewHeader2Param);
        TextViewHeader2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        TextViewHeader2.setText(getString(R.string.WelcomeActivityHeader2));
        TextViewHeader2.setTextColor(ContextCompat.getColor(context, R.color.White));

        LinearLayoutHeader.addView(TextViewHeader2);

        RelativeLayout.LayoutParams RelativeLayoutSignUpParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 270), MiscHandler.ToDimension(context, 45));
        RelativeLayoutSignUpParam.setMargins(0, MiscHandler.ToDimension(context, 30), 0, 0);
        RelativeLayoutSignUpParam.addRule(RelativeLayout.BELOW, ViewHeader.getId());
        RelativeLayoutSignUpParam.addRule(RelativeLayout.CENTER_HORIZONTAL);

        GradientDrawable ShapeSignUp = new GradientDrawable();
        ShapeSignUp.setColor(ContextCompat.getColor(context, R.color.BlueLight));
        ShapeSignUp.setCornerRadius(MiscHandler.ToDimension(context, 7));

        Button ButtonSignUp = new Button(context, null, android.R.attr.borderlessButtonStyle);
        ButtonSignUp.setLayoutParams(RelativeLayoutSignUpParam);
        ButtonSignUp.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        ButtonSignUp.setTextColor(ContextCompat.getColor(context, R.color.White));
        ButtonSignUp.setText(getString(R.string.WelcomeActivitySignUp));
        ButtonSignUp.setId(MiscHandler.GenerateViewID());
        ButtonSignUp.setBackground(ShapeSignUp);
        ButtonSignUp.setAllCaps(false);
        ButtonSignUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getSupportFragmentManager().beginTransaction().replace(R.id.WelcomeActivityContainer, new SignUpFragmentUsername()).addToBackStack("SignUpFragmentUsername").commit();
            }
        });

        RelativeLayoutMain2.addView(ButtonSignUp);

        RelativeLayout.LayoutParams RelativeLayoutORParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 45));
        RelativeLayoutORParam.setMargins(0, MiscHandler.ToDimension(context, 20), 0, MiscHandler.ToDimension(context, 5));
        RelativeLayoutORParam.addRule(RelativeLayout.BELOW, ButtonSignUp.getId());

        RelativeLayout RelativeLayoutOR = new RelativeLayout(context);
        RelativeLayoutOR.setLayoutParams(RelativeLayoutORParam);
        RelativeLayoutOR.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain2.addView(RelativeLayoutOR);

        RelativeLayout.LayoutParams TextViewORParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 40), RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewORParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        TextView TextViewOR = new TextView(context);
        TextViewOR.setLayoutParams(TextViewORParam);
        TextViewOR.setTypeface(null, Typeface.BOLD);
        TextViewOR.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        TextViewOR.setText(getString(R.string.WelcomeActivityOR));
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
                startActivityForResult(Auth.GoogleSignInApi.getSignInIntent(GoogleClientApi), 100);
            }
        });

        RelativeLayoutMain2.addView(LinearLayoutGoogle);

        LinearLayout.LayoutParams ImageViewGoogleParam = new LinearLayout.LayoutParams(MiscHandler.ToDimension(context, 30), MiscHandler.ToDimension(context, 30));
        ImageViewGoogleParam.setMargins(MiscHandler.ToDimension(context, 5), 0, MiscHandler.ToDimension(context, 5), 0);

        ImageView ImageViewGoogle = new ImageView(context);
        ImageViewGoogle.setLayoutParams(ImageViewGoogleParam);
        ImageViewGoogle.setBackgroundResource(R.drawable.ic_google);

        LinearLayoutGoogle.addView(ImageViewGoogle);

        TextView TextViewGoogle = new TextView(context);
        TextViewGoogle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        TextViewGoogle.setTextColor(ContextCompat.getColor(context, R.color.BlueLight));
        TextViewGoogle.setTypeface(null, Typeface.BOLD);
        TextViewGoogle.setText(getString(R.string.WelcomeActivitySignInGoogle));
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
                getSupportFragmentManager().beginTransaction().replace(R.id.WelcomeActivityContainer, new SignInFragment()).addToBackStack("SignInFragment").commit();
            }
        });

        RelativeLayoutMain2.addView(LinearLayoutSignIn);

        TextView TextViewSignIn = new TextView(context);
        TextViewSignIn.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewSignIn.setTextColor(ContextCompat.getColor(context, R.color.Gray4));
        TextViewSignIn.setText(getString(R.string.WelcomeActivitySignIn));
        TextViewSignIn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

        LinearLayoutSignIn.addView(TextViewSignIn);

        LinearLayout.LayoutParams TextViewSignIn2Param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        TextViewSignIn2Param.setMargins(MiscHandler.ToDimension(context, 5), 0, MiscHandler.ToDimension(context, 5), 0);

        TextView TextViewSignIn2 = new TextView(context);
        TextViewSignIn2.setLayoutParams(TextViewSignIn2Param);
        TextViewSignIn2.setTextColor(ContextCompat.getColor(context, R.color.BlueLight));
        TextViewSignIn2.setText(getString(R.string.WelcomeActivitySignIn2));
        TextViewSignIn2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

        LinearLayoutSignIn.addView(TextViewSignIn2);

        RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1));
        ViewLineParam.addRule(RelativeLayout.ABOVE, LinearLayoutSignIn.getId());

        View ViewLine = new View(context);
        ViewLine.setLayoutParams(ViewLineParam);
        ViewLine.setBackgroundResource(R.color.Gray2);
        ViewLine.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain2.addView(ViewLine);

        RelativeLayout.LayoutParams TextViewTerm2Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTerm2Param.addRule(RelativeLayout.ABOVE, ViewLine.getId());
        TextViewTerm2Param.addRule(RelativeLayout.CENTER_HORIZONTAL);
        TextViewTerm2Param.setMargins(0, 0, 0, MiscHandler.ToDimension(context, 20));

        TextView TextViewTerm2 = new TextView(context);
        TextViewTerm2.setLayoutParams(TextViewTerm2Param);
        TextViewTerm2.setTextColor(ContextCompat.getColor(context, R.color.BlueGray));
        TextViewTerm2.setText(getString(R.string.WelcomeActivityTerm2));
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

        RelativeLayoutMain2.addView(TextViewTerm2);

        RelativeLayout.LayoutParams TextViewTermParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTermParam.addRule(RelativeLayout.ABOVE, TextViewTerm2.getId());
        TextViewTermParam.addRule(RelativeLayout.CENTER_HORIZONTAL);

        TextView TextViewTerm = new TextView(context);
        TextViewTerm.setLayoutParams(TextViewTermParam);
        TextViewTerm.setTextColor(ContextCompat.getColor(context, R.color.BlueGray));
        TextViewTerm.setText(getString(R.string.WelcomeActivityTerm));
        TextViewTerm.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

        RelativeLayoutMain2.addView(TextViewTerm);

        FrameLayout FrameLayoutContainer = new FrameLayout(context);
        FrameLayoutContainer.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        FrameLayoutContainer.setId(R.id.WelcomeActivityContainer);

        RelativeLayoutMain.addView(FrameLayoutContainer);

        setContentView(RelativeLayoutMain);

        GoogleSignInOptions GoogleOption = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().requestIdToken("590625045379-sdgme2k81supeig9iruse656uj2e3geb.apps.googleusercontent.com").build();
        GoogleClientApi = new GoogleApiClient.Builder(context).enableAutoManage(this, null).addApi(Auth.GOOGLE_SIGN_IN_API, GoogleOption).build();

        if (GoogleClientApi.isConnected())
            Auth.GoogleSignInApi.signOut(GoogleClientApi);
    }

    @Override
    public void onActivityResult(int RequestCode, int ResultCode, Intent intent)
    {
        if (RequestCode == 100)
        {
            GoogleSignInResult Result = Auth.GoogleSignInApi.getSignInResultFromIntent(intent);

            if (Result.isSuccess())
            {
                GoogleSignInAccount Acc = Result.getSignInAccount();

                if (Acc != null)
                {
                    final Context context = WelcomeActivity.this;

                    AndroidNetworking.post(MiscHandler.GetRandomServer("SignInGoogle"))
                    .addBodyParameter("Token", Acc.getIdToken())
                    .addBodyParameter("Session", GenerateSession())
                    .setTag("WelcomeActivity")
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
                                    SharedHandler.SetBoolean(context, "IsLogin", true);
                                    SharedHandler.SetString(context, "TOKEN", Result.getString("TOKEN"));
                                    SharedHandler.SetString(context, "ID", Result.getString("ID"));
                                    SharedHandler.SetString(context, "Username", Result.getString("Username"));
                                    SharedHandler.SetString(context, "Avatar", Result.getString("Avatar"));
                                    SharedHandler.SetBoolean(context, "Password", Result.getBoolean("Password"));

                                    startActivity(new Intent(context, ActivityMain.class));
                                    finish();
                                    return;
                                }

                                MiscHandler.Toast(context, getString(R.string.WelcomeActivityGoogleError));
                            }
                            catch (Exception e)
                            {
                                MiscHandler.Debug("WelcomeActivity-RequestGoogle: " + e.toString());
                            }
                        }

                        @Override
                        public void onError(ANError anError)
                        {
                            MiscHandler.Toast(context, getString(R.string.NoInternet));
                        }
                    });
                }

                if (GoogleClientApi.isConnected())
                    Auth.GoogleSignInApi.signOut(GoogleClientApi);
            }
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();

        if (GoogleClientApi.isConnected())
            Auth.GoogleSignInApi.signOut(GoogleClientApi);

        GoogleClientApi.disconnect();
        MiscHandler.HideSoftKey(this);
        AndroidNetworking.forceCancel("WelcomeActivity");
    }

    public static class SignUpFragmentUsername extends Fragment
    {
        private ViewTreeObserver.OnGlobalLayoutListener RelativeLayoutMainListener;
        private int RelativeLayoutMainHeightDifference = 0;
        private RelativeLayout RelativeLayoutMain;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup Parent, Bundle savedInstanceState)
        {
            final Context context = getActivity();
            final Button ButtonSignUp = new Button(context, null, android.R.attr.borderlessButtonStyle);
            final LoadingView LoadingViewSignUp = new LoadingView(context);

            RelativeLayoutMain = new RelativeLayout(context);
            RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            RelativeLayoutMain.setBackgroundResource(R.color.White);
            RelativeLayoutMain.setClickable(true);

            RelativeLayoutMainListener = new ViewTreeObserver.OnGlobalLayoutListener()
            {
                @Override
                public void onGlobalLayout()
                {
                    Rect rect = new Rect();
                    RelativeLayoutMain.getWindowVisibleDisplayFrame(rect);

                    int ScreenHeight = RelativeLayoutMain.getHeight();
                    int DifferenceHeight = ScreenHeight - (rect.bottom - rect.top);

                    if (DifferenceHeight > ScreenHeight / 3 && DifferenceHeight != RelativeLayoutMainHeightDifference)
                    {
                        RelativeLayoutMain.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, ScreenHeight - DifferenceHeight));
                        RelativeLayoutMainHeightDifference = DifferenceHeight;
                    }
                    else if (DifferenceHeight != RelativeLayoutMainHeightDifference)
                    {
                        RelativeLayoutMain.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, ScreenHeight));
                        RelativeLayoutMainHeightDifference = DifferenceHeight;
                    }
                    else if (RelativeLayoutMainHeightDifference != 0)
                    {
                        RelativeLayoutMain.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, ScreenHeight + Math.abs(RelativeLayoutMainHeightDifference)));
                        RelativeLayoutMainHeightDifference = 0;
                    }

                    RelativeLayoutMain.requestLayout();
                }
            };

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
                    getActivity().onBackPressed();
                }
            });

            RelativeLayoutHeader.addView(ImageViewBack);

            RelativeLayout.LayoutParams TextViewHeaderParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewHeaderParam.addRule(RelativeLayout.RIGHT_OF, ImageViewBack.getId());
            TextViewHeaderParam.addRule(RelativeLayout.CENTER_VERTICAL);

            TextView TextViewHeader = new TextView(context);
            TextViewHeader.setLayoutParams(TextViewHeaderParam);
            TextViewHeader.setTextColor(ContextCompat.getColor(context, R.color.White));
            TextViewHeader.setText(getString(R.string.SignUpFragmentUsername));
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

            RelativeLayout.LayoutParams ScrollViewMainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            ScrollViewMainParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

            ScrollView ScrollViewMain = new ScrollView(context);
            ScrollViewMain.setLayoutParams(ScrollViewMainParam);
            ScrollViewMain.setFillViewport(true);
            ScrollViewMain.setId(MiscHandler.GenerateViewID());

            RelativeLayoutMain.addView(ScrollViewMain);

            RelativeLayout RelativeLayoutMain2 = new RelativeLayout(context);
            RelativeLayoutMain2.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

            ScrollViewMain.addView(RelativeLayoutMain2);

            RelativeLayout.LayoutParams TextViewUsernameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewUsernameParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));

            TextView TextViewUsername = new TextView(context);
            TextViewUsername.setLayoutParams(TextViewUsernameParam);
            TextViewUsername.setTextColor(ContextCompat.getColor(context, R.color.Gray4));
            TextViewUsername.setText(getString(R.string.SignUpFragmentUsername));
            TextViewUsername.setTypeface(null, Typeface.BOLD);
            TextViewUsername.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            TextViewUsername.setId(MiscHandler.GenerateViewID());

            RelativeLayoutMain2.addView(TextViewUsername);

            RelativeLayout.LayoutParams EditTextUsernameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            EditTextUsernameParam.addRule(RelativeLayout.BELOW, TextViewUsername.getId());
            EditTextUsernameParam.setMargins(MiscHandler.ToDimension(context, 10), 0, MiscHandler.ToDimension(context, 10), 0);

            final EditText EditTextUsername = new EditText(new ContextThemeWrapper(context, R.style.GeneralEditTextTheme));
            EditTextUsername.setLayoutParams(EditTextUsernameParam);
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
                            char[] AllowChar = new char[] {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '.', '_', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

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
                    if (s.length() > 2)
                        ButtonSignUp.setEnabled(true);
                    else
                        ButtonSignUp.setEnabled(false);
                }
            });

            RelativeLayoutMain2.addView(EditTextUsername);

            RelativeLayout.LayoutParams TextViewMessageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewMessageParam.addRule(RelativeLayout.BELOW, EditTextUsername.getId());

            TextView TextViewMessage = new TextView(context);
            TextViewMessage.setLayoutParams(TextViewMessageParam);
            TextViewMessage.setTextColor(ContextCompat.getColor(context, R.color.Black));
            TextViewMessage.setText(getString(R.string.SignUpFragmentUsernameMessage));
            TextViewMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            TextViewMessage.setId(MiscHandler.GenerateViewID());
            TextViewMessage.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));

            RelativeLayoutMain2.addView(TextViewMessage);

            RelativeLayout.LayoutParams RelativeLayoutBottomParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            RelativeLayoutBottomParam.addRule(RelativeLayout.BELOW, TextViewMessage.getId());

            RelativeLayout RelativeLayoutBottom = new RelativeLayout(context);
            RelativeLayoutBottom.setLayoutParams(RelativeLayoutBottomParam);

            RelativeLayoutMain2.addView(RelativeLayoutBottom);

            RelativeLayout.LayoutParams TextViewPrivacyParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewPrivacyParam.addRule(RelativeLayout.CENTER_VERTICAL);
            TextViewPrivacyParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

            TextView TextViewPrivacy = new TextView(context);
            TextViewPrivacy.setLayoutParams(TextViewPrivacyParam);
            TextViewPrivacy.setTextColor(ContextCompat.getColor(context, R.color.BlueLight));
            TextViewPrivacy.setText(getString(R.string.WelcomeActivityGeneralTerm));
            TextViewPrivacy.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            TextViewPrivacy.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
            TextViewPrivacy.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"))); } });

            RelativeLayoutBottom.addView(TextViewPrivacy);

            RelativeLayout.LayoutParams RelativeLayoutNextParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 90), MiscHandler.ToDimension(context, 35));
            RelativeLayoutNextParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            RelativeLayoutNextParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));

            GradientDrawable ShapeEnable = new GradientDrawable();
            ShapeEnable.setColor(ContextCompat.getColor(context, R.color.BlueLight));
            ShapeEnable.setCornerRadius(MiscHandler.ToDimension(context, 7));

            RelativeLayout RelativeLayoutNext = new RelativeLayout(context);
            RelativeLayoutNext.setLayoutParams(RelativeLayoutNextParam);
            RelativeLayoutNext.setBackground(ShapeEnable);

            RelativeLayoutBottom.addView(RelativeLayoutNext);

            GradientDrawable ShapeDisable = new GradientDrawable();
            ShapeDisable.setCornerRadius(MiscHandler.ToDimension(context, 7));
            ShapeDisable.setColor(ContextCompat.getColor(context, R.color.Gray2));

            StateListDrawable StateSignUp = new StateListDrawable();
            StateSignUp.addState(new int[] { android.R.attr.state_enabled }, ShapeEnable);
            StateSignUp.addState(new int[] { -android.R.attr.state_enabled }, ShapeDisable);

            ButtonSignUp.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 90), MiscHandler.ToDimension(context, 35)));
            ButtonSignUp.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            ButtonSignUp.setTextColor(ContextCompat.getColor(context, R.color.White));
            ButtonSignUp.setText(getString(R.string.WelcomeActivityGeneralNext));
            ButtonSignUp.setBackground(StateSignUp);
            ButtonSignUp.setPadding(0, 0, 0, 0);
            ButtonSignUp.setEnabled(false);
            ButtonSignUp.setAllCaps(false);
            ButtonSignUp.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    ButtonSignUp.setVisibility(View.GONE);
                    LoadingViewSignUp.Start();

                    AndroidNetworking.post(MiscHandler.GetRandomServer("UsernameIsAvailable"))
                    .addBodyParameter("Username", EditTextUsername.getText().toString())
                    .setTag("SignUpFragmentUsername")
                    .build()
                    .getAsString(new StringRequestListener()
                    {
                        @Override
                        public void onResponse(String Response)
                        {
                            ButtonSignUp.setVisibility(View.VISIBLE);
                            LoadingViewSignUp.Stop();

                            try
                            {
                                if (new JSONObject(Response).getInt("Message") == 1000)
                                {
                                    WelcomeActivity Parent = (WelcomeActivity) getActivity();
                                    Parent.Username = EditTextUsername.getText().toString();
                                    Parent.getSupportFragmentManager().beginTransaction().replace(R.id.WelcomeActivityContainer, new SignUpFragmentPassword()).addToBackStack("SignUpFragmentPassword").commit();
                                    return;
                                }

                                MiscHandler.Toast(context, getString(R.string.SignUpFragmentUsernameError));
                            }
                            catch (Exception e)
                            {
                                MiscHandler.Debug("WelcomeActivity-RequestUsername: " + e.toString());
                            }
                        }

                        @Override
                        public void onError(ANError anError)
                        {
                            LoadingViewSignUp.Stop();
                            ButtonSignUp.setVisibility(View.VISIBLE);
                            MiscHandler.Toast(context, getString(R.string.NoInternet));
                        }
                    });
                }
            });

            RelativeLayoutNext.addView(ButtonSignUp);

            RelativeLayout.LayoutParams LoadingViewUsernameParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 90), MiscHandler.ToDimension(context, 35));
            LoadingViewUsernameParam.addRule(RelativeLayout.CENTER_IN_PARENT);

            LoadingViewSignUp.setLayoutParams(LoadingViewUsernameParam);
            LoadingViewSignUp.SetColor(R.color.White);

            RelativeLayoutNext.addView(LoadingViewSignUp);

            return RelativeLayoutMain;
        }

        @Override
        public void onResume()
        {
            super.onResume();
            RelativeLayoutMain.getViewTreeObserver().addOnGlobalLayoutListener(RelativeLayoutMainListener);

            InputMethodManager IMM = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            IMM.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }

        @Override
        public void onPause()
        {
            super.onPause();
            MiscHandler.HideSoftKey(getActivity());
            AndroidNetworking.forceCancel("SignUpFragmentUsername");
            RelativeLayoutMain.getViewTreeObserver().removeOnGlobalLayoutListener(RelativeLayoutMainListener);
        }
    }

    public static class SignUpFragmentPassword extends Fragment
    {
        private ViewTreeObserver.OnGlobalLayoutListener RelativeLayoutMainListener;
        private int RelativeLayoutMainHeightDifference = 0;
        private RelativeLayout RelativeLayoutMain;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup Parent, Bundle savedInstanceState)
        {
            final Context context = getActivity();
            final Button ButtonSignUp = new Button(context, null, android.R.attr.borderlessButtonStyle);

            RelativeLayoutMain = new RelativeLayout(context);
            RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            RelativeLayoutMain.setBackgroundResource(R.color.White);
            RelativeLayoutMain.setClickable(true);

            RelativeLayoutMainListener = new ViewTreeObserver.OnGlobalLayoutListener()
            {
                @Override
                public void onGlobalLayout()
                {
                    Rect rect = new Rect();
                    RelativeLayoutMain.getWindowVisibleDisplayFrame(rect);

                    int ScreenHeight = RelativeLayoutMain.getHeight();
                    int DifferenceHeight = ScreenHeight - (rect.bottom - rect.top);

                    if (DifferenceHeight > ScreenHeight / 3 && DifferenceHeight != RelativeLayoutMainHeightDifference)
                    {
                        RelativeLayoutMain.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, ScreenHeight - DifferenceHeight));
                        RelativeLayoutMainHeightDifference = DifferenceHeight;
                    }
                    else if (DifferenceHeight != RelativeLayoutMainHeightDifference)
                    {
                        RelativeLayoutMain.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, ScreenHeight));
                        RelativeLayoutMainHeightDifference = DifferenceHeight;
                    }
                    else if (RelativeLayoutMainHeightDifference != 0)
                    {
                        RelativeLayoutMain.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, ScreenHeight + Math.abs(RelativeLayoutMainHeightDifference)));
                        RelativeLayoutMainHeightDifference = 0;
                    }

                    RelativeLayoutMain.requestLayout();
                }
            };

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
                    getActivity().onBackPressed();
                }
            });

            RelativeLayoutHeader.addView(ImageViewBack);

            RelativeLayout.LayoutParams TextViewHeaderParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewHeaderParam.addRule(RelativeLayout.RIGHT_OF, ImageViewBack.getId());
            TextViewHeaderParam.addRule(RelativeLayout.CENTER_VERTICAL);

            TextView TextViewHeader = new TextView(context);
            TextViewHeader.setLayoutParams(TextViewHeaderParam);
            TextViewHeader.setTextColor(ContextCompat.getColor(context, R.color.White));
            TextViewHeader.setText(getString(R.string.SignUpFragmentPassword));
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

            RelativeLayout.LayoutParams ScrollViewMainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            ScrollViewMainParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

            ScrollView ScrollViewMain = new ScrollView(context);
            ScrollViewMain.setLayoutParams(ScrollViewMainParam);
            ScrollViewMain.setFillViewport(true);
            ScrollViewMain.setId(MiscHandler.GenerateViewID());

            RelativeLayoutMain.addView(ScrollViewMain);

            RelativeLayout RelativeLayoutMain2 = new RelativeLayout(context);
            RelativeLayoutMain2.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

            ScrollViewMain.addView(RelativeLayoutMain2);

            RelativeLayout.LayoutParams TextViewPasswordParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewPasswordParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));

            TextView TextViewPassword = new TextView(context);
            TextViewPassword.setLayoutParams(TextViewPasswordParam);
            TextViewPassword.setTextColor(ContextCompat.getColor(context, R.color.Gray4));
            TextViewPassword.setText(getString(R.string.SignUpFragmentUsername));
            TextViewPassword.setTypeface(null, Typeface.BOLD);
            TextViewPassword.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            TextViewPassword.setId(MiscHandler.GenerateViewID());

            RelativeLayoutMain2.addView(TextViewPassword);

            RelativeLayout.LayoutParams EditTextPasswordParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            EditTextPasswordParam.addRule(RelativeLayout.BELOW, TextViewPassword.getId());
            EditTextPasswordParam.setMargins(MiscHandler.ToDimension(context, 10), 0, MiscHandler.ToDimension(context, 10), 0);

            final EditText EditTextPassword = new EditText(new ContextThemeWrapper(context, R.style.GeneralEditTextTheme));
            EditTextPassword.setLayoutParams(EditTextPasswordParam);
            EditTextPassword.setId(MiscHandler.GenerateViewID());
            EditTextPassword.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            EditTextPassword.setFilters(new InputFilter[] { new InputFilter.LengthFilter(32) });
            EditTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            EditTextPassword.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.BlueLight), PorterDuff.Mode.SRC_ATOP);
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
                        ButtonSignUp.setEnabled(true);
                    else
                        ButtonSignUp.setEnabled(false);
                }
            });

            RelativeLayoutMain2.addView(EditTextPassword);

            RelativeLayout.LayoutParams TextViewMessageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewMessageParam.addRule(RelativeLayout.BELOW, EditTextPassword.getId());

            TextView TextViewMessage = new TextView(context);
            TextViewMessage.setLayoutParams(TextViewMessageParam);
            TextViewMessage.setTextColor(ContextCompat.getColor(context, R.color.Black));
            TextViewMessage.setText(getString(R.string.SignUpFragmentPasswordMessage));
            TextViewMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            TextViewMessage.setId(MiscHandler.GenerateViewID());
            TextViewMessage.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));

            RelativeLayoutMain2.addView(TextViewMessage);

            RelativeLayout.LayoutParams RelativeLayoutBottomParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            RelativeLayoutBottomParam.addRule(RelativeLayout.BELOW, TextViewMessage.getId());

            RelativeLayout RelativeLayoutBottom = new RelativeLayout(context);
            RelativeLayoutBottom.setLayoutParams(RelativeLayoutBottomParam);

            RelativeLayoutMain2.addView(RelativeLayoutBottom);

            RelativeLayout.LayoutParams TextViewPrivacyParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewPrivacyParam.addRule(RelativeLayout.CENTER_VERTICAL);
            TextViewPrivacyParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

            TextView TextViewPrivacy = new TextView(context);
            TextViewPrivacy.setLayoutParams(TextViewPrivacyParam);
            TextViewPrivacy.setTextColor(ContextCompat.getColor(context, R.color.BlueLight));
            TextViewPrivacy.setText(getString(R.string.WelcomeActivityGeneralTerm));
            TextViewPrivacy.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            TextViewPrivacy.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
            TextViewPrivacy.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"))); } });

            RelativeLayoutBottom.addView(TextViewPrivacy);

            RelativeLayout.LayoutParams RelativeLayoutNextParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 90), MiscHandler.ToDimension(context, 35));
            RelativeLayoutNextParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            RelativeLayoutNextParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));

            GradientDrawable ShapeEnable = new GradientDrawable();
            ShapeEnable.setColor(ContextCompat.getColor(context, R.color.BlueLight));
            ShapeEnable.setCornerRadius(MiscHandler.ToDimension(context, 7));

            RelativeLayout RelativeLayoutNext = new RelativeLayout(context);
            RelativeLayoutNext.setLayoutParams(RelativeLayoutNextParam);
            RelativeLayoutNext.setBackground(ShapeEnable);

            RelativeLayoutBottom.addView(RelativeLayoutNext);

            GradientDrawable ShapeDisable = new GradientDrawable();
            ShapeDisable.setCornerRadius(MiscHandler.ToDimension(context, 7));
            ShapeDisable.setColor(ContextCompat.getColor(context, R.color.Gray2));

            StateListDrawable StateSignUp = new StateListDrawable();
            StateSignUp.addState(new int[] { android.R.attr.state_enabled }, ShapeEnable);
            StateSignUp.addState(new int[] { -android.R.attr.state_enabled }, ShapeDisable);

            ButtonSignUp.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 90), MiscHandler.ToDimension(context, 35)));
            ButtonSignUp.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            ButtonSignUp.setTextColor(ContextCompat.getColor(context, R.color.White));
            ButtonSignUp.setText(getString(R.string.WelcomeActivityGeneralNext));
            ButtonSignUp.setBackground(StateSignUp);
            ButtonSignUp.setPadding(0, 0, 0, 0);
            ButtonSignUp.setEnabled(false);
            ButtonSignUp.setAllCaps(false);
            ButtonSignUp.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    WelcomeActivity Parent = (WelcomeActivity) getActivity();
                    Parent.Password = EditTextPassword.getText().toString();
                    Parent.getSupportFragmentManager().beginTransaction().replace(R.id.WelcomeActivityContainer, new SignUpFragmentEmail()).addToBackStack("SignUpFragmentEmail").commit();
                }
            });

            RelativeLayoutNext.addView(ButtonSignUp);

            return RelativeLayoutMain;
        }

        @Override
        public void onResume()
        {
            super.onResume();
            RelativeLayoutMain.getViewTreeObserver().addOnGlobalLayoutListener(RelativeLayoutMainListener);

            InputMethodManager IMM = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            IMM.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }

        @Override
        public void onPause()
        {
            super.onPause();
            MiscHandler.HideSoftKey(getActivity());
            RelativeLayoutMain.getViewTreeObserver().removeOnGlobalLayoutListener(RelativeLayoutMainListener);
        }
    }

    public static class SignUpFragmentEmail extends Fragment
    {
        private ViewTreeObserver.OnGlobalLayoutListener RelativeLayoutMainListener;
        private int RelativeLayoutMainHeightDifference = 0;
        private RelativeLayout RelativeLayoutMain;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup Parent, Bundle savedInstanceState)
        {
            final Context context = getActivity();
            final Button ButtonSignUp = new Button(context, null, android.R.attr.borderlessButtonStyle);
            final LoadingView LoadingViewSignUp = new LoadingView(context);

            RelativeLayoutMain = new RelativeLayout(context);
            RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            RelativeLayoutMain.setBackgroundResource(R.color.White);
            RelativeLayoutMain.setClickable(true);

            RelativeLayoutMainListener = new ViewTreeObserver.OnGlobalLayoutListener()
            {
                @Override
                public void onGlobalLayout()
                {
                    Rect rect = new Rect();
                    RelativeLayoutMain.getWindowVisibleDisplayFrame(rect);

                    int ScreenHeight = RelativeLayoutMain.getHeight();
                    int DifferenceHeight = ScreenHeight - (rect.bottom - rect.top);

                    if (DifferenceHeight > ScreenHeight / 3 && DifferenceHeight != RelativeLayoutMainHeightDifference)
                    {
                        RelativeLayoutMain.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, ScreenHeight - DifferenceHeight));
                        RelativeLayoutMainHeightDifference = DifferenceHeight;
                    }
                    else if (DifferenceHeight != RelativeLayoutMainHeightDifference)
                    {
                        RelativeLayoutMain.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, ScreenHeight));
                        RelativeLayoutMainHeightDifference = DifferenceHeight;
                    }
                    else if (RelativeLayoutMainHeightDifference != 0)
                    {
                        RelativeLayoutMain.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, ScreenHeight + Math.abs(RelativeLayoutMainHeightDifference)));
                        RelativeLayoutMainHeightDifference = 0;
                    }

                    RelativeLayoutMain.requestLayout();
                }
            };

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
                    getActivity().onBackPressed();
                }
            });

            RelativeLayoutHeader.addView(ImageViewBack);

            RelativeLayout.LayoutParams TextViewHeaderParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewHeaderParam.addRule(RelativeLayout.RIGHT_OF, ImageViewBack.getId());
            TextViewHeaderParam.addRule(RelativeLayout.CENTER_VERTICAL);

            TextView TextViewHeader = new TextView(context);
            TextViewHeader.setLayoutParams(TextViewHeaderParam);
            TextViewHeader.setTextColor(ContextCompat.getColor(context, R.color.White));
            TextViewHeader.setText(getString(R.string.SignUpFragmentEmailTitle));
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

            RelativeLayout.LayoutParams ScrollViewMainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            ScrollViewMainParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

            ScrollView ScrollViewMain = new ScrollView(context);
            ScrollViewMain.setLayoutParams(ScrollViewMainParam);
            ScrollViewMain.setFillViewport(true);
            ScrollViewMain.setId(MiscHandler.GenerateViewID());

            RelativeLayoutMain.addView(ScrollViewMain);

            RelativeLayout RelativeLayoutMain2 = new RelativeLayout(context);
            RelativeLayoutMain2.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

            ScrollViewMain.addView(RelativeLayoutMain2);

            RelativeLayout.LayoutParams TextViewUsernameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewUsernameParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));

            TextView TextViewUsername = new TextView(context);
            TextViewUsername.setLayoutParams(TextViewUsernameParam);
            TextViewUsername.setTextColor(ContextCompat.getColor(context, R.color.Gray4));
            TextViewUsername.setText(getString(R.string.SignUpFragmentEmail));
            TextViewUsername.setTypeface(null, Typeface.BOLD);
            TextViewUsername.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            TextViewUsername.setId(MiscHandler.GenerateViewID());

            RelativeLayoutMain2.addView(TextViewUsername);

            RelativeLayout.LayoutParams EditTextEmailParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            EditTextEmailParam.addRule(RelativeLayout.BELOW, TextViewUsername.getId());
            EditTextEmailParam.setMargins(MiscHandler.ToDimension(context, 10), 0, MiscHandler.ToDimension(context, 10), 0);

            final EditText EditTextEmail = new EditText(new ContextThemeWrapper(context, R.style.GeneralEditTextTheme));
            EditTextEmail.setLayoutParams(EditTextEmailParam);
            EditTextEmail.setId(MiscHandler.GenerateViewID());
            EditTextEmail.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            EditTextEmail.setFilters(new InputFilter[] { new InputFilter.LengthFilter(64) });
            EditTextEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            EditTextEmail.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.BlueLight), PorterDuff.Mode.SRC_ATOP);
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
                        ButtonSignUp.setEnabled(true);
                    else
                        ButtonSignUp.setEnabled(false);
                }
            });

            RelativeLayoutMain2.addView(EditTextEmail);

            RelativeLayout.LayoutParams TextViewMessageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewMessageParam.addRule(RelativeLayout.BELOW, EditTextEmail.getId());

            TextView TextViewMessage = new TextView(context);
            TextViewMessage.setLayoutParams(TextViewMessageParam);
            TextViewMessage.setTextColor(ContextCompat.getColor(context, R.color.Black));
            TextViewMessage.setText(getString(R.string.SignUpFragmentEmailMessage));
            TextViewMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            TextViewMessage.setId(MiscHandler.GenerateViewID());
            TextViewMessage.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));

            RelativeLayoutMain2.addView(TextViewMessage);

            RelativeLayout.LayoutParams RelativeLayoutBottomParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            RelativeLayoutBottomParam.addRule(RelativeLayout.BELOW, TextViewMessage.getId());

            RelativeLayout RelativeLayoutBottom = new RelativeLayout(context);
            RelativeLayoutBottom.setLayoutParams(RelativeLayoutBottomParam);

            RelativeLayoutMain2.addView(RelativeLayoutBottom);

            RelativeLayout.LayoutParams TextViewPrivacyParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewPrivacyParam.addRule(RelativeLayout.CENTER_VERTICAL);
            TextViewPrivacyParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

            TextView TextViewPrivacy = new TextView(context);
            TextViewPrivacy.setLayoutParams(TextViewPrivacyParam);
            TextViewPrivacy.setTextColor(ContextCompat.getColor(context, R.color.BlueLight));
            TextViewPrivacy.setText(getString(R.string.WelcomeActivityGeneralTerm));
            TextViewPrivacy.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            TextViewPrivacy.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
            TextViewPrivacy.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"))); } });

            RelativeLayoutBottom.addView(TextViewPrivacy);

            RelativeLayout.LayoutParams RelativeLayoutFinishParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 90), MiscHandler.ToDimension(context, 35));
            RelativeLayoutFinishParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            RelativeLayoutFinishParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));

            GradientDrawable ShapeEnable = new GradientDrawable();
            ShapeEnable.setColor(ContextCompat.getColor(context, R.color.BlueLight));
            ShapeEnable.setCornerRadius(MiscHandler.ToDimension(context, 7));

            RelativeLayout RelativeLayoutFinish = new RelativeLayout(context);
            RelativeLayoutFinish.setLayoutParams(RelativeLayoutFinishParam);
            RelativeLayoutFinish.setBackground(ShapeEnable);

            RelativeLayoutBottom.addView(RelativeLayoutFinish);

            GradientDrawable ShapeDisable = new GradientDrawable();
            ShapeDisable.setCornerRadius(MiscHandler.ToDimension(context, 7));
            ShapeDisable.setColor(ContextCompat.getColor(context, R.color.Gray2));

            StateListDrawable StateSignUp = new StateListDrawable();
            StateSignUp.addState(new int[] { android.R.attr.state_enabled }, ShapeEnable);
            StateSignUp.addState(new int[] { -android.R.attr.state_enabled }, ShapeDisable);

            ButtonSignUp.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 90), MiscHandler.ToDimension(context, 35)));
            ButtonSignUp.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            ButtonSignUp.setTextColor(ContextCompat.getColor(context, R.color.White));
            ButtonSignUp.setText(getString(R.string.SignUpFragmentEmailFinish));
            ButtonSignUp.setBackground(StateSignUp);
            ButtonSignUp.setPadding(0, 0, 0, 0);
            ButtonSignUp.setEnabled(false);
            ButtonSignUp.setAllCaps(false);
            ButtonSignUp.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    final WelcomeActivity Parent = (WelcomeActivity) getActivity();

                    ButtonSignUp.setVisibility(View.GONE);
                    LoadingViewSignUp.Start();

                    AndroidNetworking.post(MiscHandler.GetRandomServer("SignUp"))
                    .addBodyParameter("Username", Parent.Username)
                    .addBodyParameter("Password", Parent.Password)
                    .addBodyParameter("Email", EditTextEmail.getText().toString())
                    .addBodyParameter("Session", GenerateSession())
                    .setTag("SignUpFragmentEmail")
                    .build()
                    .getAsString(new StringRequestListener()
                    {
                        @Override
                        public void onResponse(String Response)
                        {
                            LoadingViewSignUp.Stop();
                            ButtonSignUp.setVisibility(View.VISIBLE);

                            try
                            {
                                JSONObject Result = new JSONObject(Response);

                                if (Result.getInt("Message") == 1000)
                                {
                                    SharedHandler.SetBoolean(context, "IsLogin", true);
                                    SharedHandler.SetString(context, "TOKEN", Result.getString("TOKEN"));
                                    SharedHandler.SetString(context, "ID", Result.getString("ID"));
                                    SharedHandler.SetString(context, "Username", Result.getString("Username"));
                                    SharedHandler.SetString(context, "Avatar", "");

                                    Parent.startActivity(new Intent(context, ActivityMain.class));
                                    Parent.finish();
                                    return;
                                }

                                MiscHandler.Toast(context, getString(R.string.SignUpFragmentEmailError));
                            }
                            catch (Exception e)
                            {
                                MiscHandler.Debug("WelcomeActivity-RequestSignUp: " + e.toString());
                            }
                        }

                        @Override
                        public void onError(ANError anError)
                        {
                            LoadingViewSignUp.Stop();
                            ButtonSignUp.setVisibility(View.VISIBLE);
                            MiscHandler.Toast(context, getString(R.string.NoInternet));
                        }
                    });
                }
            });

            RelativeLayoutFinish.addView(ButtonSignUp);

            RelativeLayout.LayoutParams LoadingViewUsernameParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 90), MiscHandler.ToDimension(context, 35));
            LoadingViewUsernameParam.addRule(RelativeLayout.CENTER_IN_PARENT);

            LoadingViewSignUp.setLayoutParams(LoadingViewUsernameParam);
            LoadingViewSignUp.SetColor(R.color.White);

            RelativeLayoutFinish.addView(LoadingViewSignUp);

            return RelativeLayoutMain;
        }

        @Override
        public void onResume()
        {
            super.onResume();
            RelativeLayoutMain.getViewTreeObserver().addOnGlobalLayoutListener(RelativeLayoutMainListener);

            InputMethodManager IMM = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            IMM.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }

        @Override
        public void onPause()
        {
            super.onPause();
            MiscHandler.HideSoftKey(getActivity());
            AndroidNetworking.forceCancel("SignUpFragmentEmail");
            RelativeLayoutMain.getViewTreeObserver().removeOnGlobalLayoutListener(RelativeLayoutMainListener);
        }
    }

    public static class SignInFragment extends Fragment
    {
        private ViewTreeObserver.OnGlobalLayoutListener RelativeLayoutMainListener;
        private int RelativeLayoutMainHeightDifference = 0;
        private RelativeLayout RelativeLayoutMain;

        private boolean RequestUsername = false;
        private boolean RequestPassword = false;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup Parent, Bundle savedInstanceState)
        {
            final Context context = getActivity();
            final Button ButtonSignIn = new Button(context, null, android.R.attr.borderlessButtonStyle);
            final LoadingView LoadingViewSignIn = new LoadingView(context);

            RelativeLayoutMain = new RelativeLayout(context);
            RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            RelativeLayoutMain.setBackgroundResource(R.color.White);
            RelativeLayoutMain.setClickable(true);

            RelativeLayoutMainListener = new ViewTreeObserver.OnGlobalLayoutListener()
            {
                @Override
                public void onGlobalLayout()
                {
                    Rect rect = new Rect();
                    RelativeLayoutMain.getWindowVisibleDisplayFrame(rect);

                    int ScreenHeight = RelativeLayoutMain.getHeight();
                    int DifferenceHeight = ScreenHeight - (rect.bottom - rect.top);

                    if (DifferenceHeight > ScreenHeight / 3 && DifferenceHeight != RelativeLayoutMainHeightDifference)
                    {
                        RelativeLayoutMain.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, ScreenHeight - DifferenceHeight));
                        RelativeLayoutMainHeightDifference = DifferenceHeight;
                    }
                    else if (DifferenceHeight != RelativeLayoutMainHeightDifference)
                    {
                        RelativeLayoutMain.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, ScreenHeight));
                        RelativeLayoutMainHeightDifference = DifferenceHeight;
                    }
                    else if (RelativeLayoutMainHeightDifference != 0)
                    {
                        RelativeLayoutMain.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, ScreenHeight + Math.abs(RelativeLayoutMainHeightDifference)));
                        RelativeLayoutMainHeightDifference = 0;
                    }

                    RelativeLayoutMain.requestLayout();
                }
            };

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
                    getActivity().onBackPressed();
                }
            });

            RelativeLayoutHeader.addView(ImageViewBack);

            RelativeLayout.LayoutParams TextViewHeaderParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewHeaderParam.addRule(RelativeLayout.RIGHT_OF, ImageViewBack.getId());
            TextViewHeaderParam.addRule(RelativeLayout.CENTER_VERTICAL);

            TextView TextViewHeader = new TextView(context);
            TextViewHeader.setLayoutParams(TextViewHeaderParam);
            TextViewHeader.setTextColor(ContextCompat.getColor(context, R.color.White));
            TextViewHeader.setText(getString(R.string.SignUpFragmentEmailTitle));
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

            RelativeLayout.LayoutParams ScrollViewMainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            ScrollViewMainParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

            ScrollView ScrollViewMain = new ScrollView(context);
            ScrollViewMain.setLayoutParams(ScrollViewMainParam);
            ScrollViewMain.setFillViewport(true);
            ScrollViewMain.setId(MiscHandler.GenerateViewID());

            RelativeLayoutMain.addView(ScrollViewMain);

            RelativeLayout RelativeLayoutMain2 = new RelativeLayout(context);
            RelativeLayoutMain2.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

            ScrollViewMain.addView(RelativeLayoutMain2);

            RelativeLayout.LayoutParams TextViewEmailOrUsernameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewEmailOrUsernameParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));

            TextView TextViewEmailOrUsername = new TextView(context);
            TextViewEmailOrUsername.setLayoutParams(TextViewEmailOrUsernameParam);
            TextViewEmailOrUsername.setTextColor(ContextCompat.getColor(context, R.color.Gray4));
            TextViewEmailOrUsername.setText(getString(R.string.SignInFragmentEmailOrUsername));
            TextViewEmailOrUsername.setTypeface(null, Typeface.BOLD);
            TextViewEmailOrUsername.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            TextViewEmailOrUsername.setId(MiscHandler.GenerateViewID());

            RelativeLayoutMain2.addView(TextViewEmailOrUsername);

            RelativeLayout.LayoutParams EditTextEmailOrUsernameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            EditTextEmailOrUsernameParam.addRule(RelativeLayout.BELOW, TextViewEmailOrUsername.getId());
            EditTextEmailOrUsernameParam.setMargins(MiscHandler.ToDimension(context, 10), 0, MiscHandler.ToDimension(context, 10), 0);

            final EditText EditTextEmailOrUsername = new EditText(new ContextThemeWrapper(context, R.style.GeneralEditTextTheme));
            EditTextEmailOrUsername.setLayoutParams(EditTextEmailOrUsernameParam);
            EditTextEmailOrUsername.setId(MiscHandler.GenerateViewID());
            EditTextEmailOrUsername.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            EditTextEmailOrUsername.setFilters(new InputFilter[] { new InputFilter.LengthFilter(64) });
            EditTextEmailOrUsername.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            EditTextEmailOrUsername.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.BlueLight), PorterDuff.Mode.SRC_ATOP);
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
                    RequestUsername = (s.length() > 2);

                    if (RequestUsername && RequestPassword)
                        ButtonSignIn.setEnabled(true);
                    else
                        ButtonSignIn.setEnabled(false);
                }
            });

            RelativeLayoutMain2.addView(EditTextEmailOrUsername);

            RelativeLayout.LayoutParams TextViewPasswordParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewPasswordParam.addRule(RelativeLayout.BELOW, EditTextEmailOrUsername.getId());
            TextViewPasswordParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));

            TextView TextViewPassword = new TextView(context);
            TextViewPassword.setLayoutParams(TextViewPasswordParam);
            TextViewPassword.setTextColor(ContextCompat.getColor(context, R.color.Gray4));
            TextViewPassword.setText(getString(R.string.SignInFragmentPassword));
            TextViewPassword.setTypeface(null, Typeface.BOLD);
            TextViewPassword.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            TextViewPassword.setId(MiscHandler.GenerateViewID());

            RelativeLayoutMain2.addView(TextViewPassword);

            RelativeLayout.LayoutParams EditTextPasswordParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            EditTextPasswordParam.addRule(RelativeLayout.BELOW, TextViewPassword.getId());
            EditTextPasswordParam.setMargins(MiscHandler.ToDimension(context, 10), 0, MiscHandler.ToDimension(context, 10), 0);

            final EditText EditTextPassword = new EditText(new ContextThemeWrapper(context, R.style.GeneralEditTextTheme));
            EditTextPassword.setLayoutParams(EditTextPasswordParam);
            EditTextPassword.setId(MiscHandler.GenerateViewID());
            EditTextPassword.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            EditTextPassword.setFilters(new InputFilter[] { new InputFilter.LengthFilter(32) });
            EditTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            EditTextPassword.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.BlueLight), PorterDuff.Mode.SRC_ATOP);
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

            RelativeLayoutMain2.addView(EditTextPassword);

            RelativeLayout.LayoutParams TextViewMessageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewMessageParam.addRule(RelativeLayout.BELOW, EditTextPassword.getId());

            TextView TextViewMessage = new TextView(context);
            TextViewMessage.setLayoutParams(TextViewMessageParam);
            TextViewMessage.setTextColor(ContextCompat.getColor(context, R.color.Black));
            TextViewMessage.setText(getString(R.string.SignInFragmentMessage));
            TextViewMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            TextViewMessage.setId(MiscHandler.GenerateViewID());
            TextViewMessage.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));

            RelativeLayoutMain2.addView(TextViewMessage);

            RelativeLayout.LayoutParams RelativeLayoutBottomParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            RelativeLayoutBottomParam.addRule(RelativeLayout.BELOW, TextViewMessage.getId());

            RelativeLayout RelativeLayoutBottom = new RelativeLayout(context);
            RelativeLayoutBottom.setLayoutParams(RelativeLayoutBottomParam);

            RelativeLayoutMain2.addView(RelativeLayoutBottom);

            RelativeLayout.LayoutParams TextViewResetParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewResetParam.addRule(RelativeLayout.CENTER_VERTICAL);
            TextViewResetParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

            TextView TextViewReset = new TextView(context);
            TextViewReset.setLayoutParams(TextViewResetParam);
            TextViewReset.setTextColor(ContextCompat.getColor(context, R.color.BlueLight));
            TextViewReset.setText(getString(R.string.SignInFragmentForgot));
            TextViewReset.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            TextViewReset.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
            TextViewReset.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.WelcomeActivityContainer, new ResetFragment()).addToBackStack("ResetFragment").commit();
                }
            });

            RelativeLayoutBottom.addView(TextViewReset);

            RelativeLayout.LayoutParams RelativeLayoutNextParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 90), MiscHandler.ToDimension(context, 35));
            RelativeLayoutNextParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            RelativeLayoutNextParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));

            GradientDrawable ShapeEnable = new GradientDrawable();
            ShapeEnable.setColor(ContextCompat.getColor(context, R.color.BlueLight));
            ShapeEnable.setCornerRadius(MiscHandler.ToDimension(context, 7));

            RelativeLayout RelativeLayoutNext = new RelativeLayout(context);
            RelativeLayoutNext.setLayoutParams(RelativeLayoutNextParam);
            RelativeLayoutNext.setBackground(ShapeEnable);

            RelativeLayoutBottom.addView(RelativeLayoutNext);

            GradientDrawable ShapeDisable = new GradientDrawable();
            ShapeDisable.setCornerRadius(MiscHandler.ToDimension(context, 7));
            ShapeDisable.setColor(ContextCompat.getColor(context, R.color.Gray2));

            StateListDrawable StateSignUp = new StateListDrawable();
            StateSignUp.addState(new int[] { android.R.attr.state_enabled }, ShapeEnable);
            StateSignUp.addState(new int[] { -android.R.attr.state_enabled }, ShapeDisable);

            ButtonSignIn.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 90), MiscHandler.ToDimension(context, 35)));
            ButtonSignIn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            ButtonSignIn.setTextColor(ContextCompat.getColor(context, R.color.White));
            ButtonSignIn.setText(getString(R.string.SignInFragmentTitle));
            ButtonSignIn.setBackground(StateSignUp);
            ButtonSignIn.setPadding(0, 0, 0, 0);
            ButtonSignIn.setEnabled(false);
            ButtonSignIn.setAllCaps(false);
            ButtonSignIn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    ButtonSignIn.setVisibility(View.GONE);
                    LoadingViewSignIn.Start();

                    AndroidNetworking.post(MiscHandler.GetRandomServer("SignIn"))
                    .addBodyParameter("EmailOrUsername", EditTextEmailOrUsername.getText().toString())
                    .addBodyParameter("Password", EditTextPassword.getText().toString())
                    .addBodyParameter("Session", GenerateSession())
                    .setTag("FragmentSignUpEmail")
                    .build()
                    .getAsString(new StringRequestListener()
                    {
                        @Override
                        public void onResponse(String Response)
                        {
                            LoadingViewSignIn.Stop();
                            ButtonSignIn.setVisibility(View.VISIBLE);

                            try
                            {
                                JSONObject Result = new JSONObject(Response);

                                if (Result.getInt("Message") == 1000)
                                {
                                    SharedHandler.SetBoolean(context, "IsLogin", true);
                                    SharedHandler.SetString(context, "TOKEN", Result.getString("TOKEN"));
                                    SharedHandler.SetString(context, "ID", Result.getString("ID"));
                                    SharedHandler.SetString(context, "Username", Result.getString("Username"));
                                    SharedHandler.SetString(context, "Avatar", Result.getString("Avatar"));

                                    getActivity().startActivity(new Intent(context, ActivityMain.class));
                                    getActivity().finish();
                                    return;
                                }

                                MiscHandler.Toast(context, getString(R.string.SignInFragmentError));
                            }
                            catch (Exception e)
                            {
                                MiscHandler.Debug("WelcomeActivity-RequestSignIn: " + e.toString());
                            }
                        }

                        @Override
                        public void onError(ANError anError)
                        {
                            LoadingViewSignIn.Stop();
                            ButtonSignIn.setVisibility(View.VISIBLE);
                            MiscHandler.Toast(context, getString(R.string.NoInternet));
                        }
                    });
                }
            });

            RelativeLayoutNext.addView(ButtonSignIn);

            RelativeLayout.LayoutParams LoadingViewUsernameParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 90), MiscHandler.ToDimension(context, 35));
            LoadingViewUsernameParam.addRule(RelativeLayout.CENTER_IN_PARENT);

            LoadingViewSignIn.setLayoutParams(LoadingViewUsernameParam);
            LoadingViewSignIn.SetColor(R.color.White);

            RelativeLayoutNext.addView(LoadingViewSignIn);

            return RelativeLayoutMain;
        }

        @Override
        public void onResume()
        {
            super.onResume();
            RelativeLayoutMain.getViewTreeObserver().addOnGlobalLayoutListener(RelativeLayoutMainListener);

            InputMethodManager IMM = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            IMM.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }

        @Override
        public void onPause()
        {
            super.onPause();
            MiscHandler.HideSoftKey(getActivity());
            AndroidNetworking.forceCancel("SignInFragment");
            RelativeLayoutMain.getViewTreeObserver().removeOnGlobalLayoutListener(RelativeLayoutMainListener);
        }
    }

    public static class ResetFragment extends Fragment
    {
        private ViewTreeObserver.OnGlobalLayoutListener RelativeLayoutMainListener;
        private int RelativeLayoutMainHeightDifference = 0;
        private RelativeLayout RelativeLayoutMain;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup Parent, Bundle savedInstanceState)
        {
            final Context context = getActivity();
            final Button ButtonReset = new Button(context, null, android.R.attr.borderlessButtonStyle);
            final LoadingView LoadingViewReset = new LoadingView(context);
            final LinearLayout LinearLayoutLoading = new LinearLayout(context);

            RelativeLayoutMain = new RelativeLayout(context);
            RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            RelativeLayoutMain.setBackgroundResource(R.color.White);
            RelativeLayoutMain.setClickable(true);

            RelativeLayoutMainListener = new ViewTreeObserver.OnGlobalLayoutListener()
            {
                @Override
                public void onGlobalLayout()
                {
                    Rect rect = new Rect();
                    RelativeLayoutMain.getWindowVisibleDisplayFrame(rect);

                    int ScreenHeight = RelativeLayoutMain.getHeight();
                    int DifferenceHeight = ScreenHeight - (rect.bottom - rect.top);

                    if (DifferenceHeight > ScreenHeight / 3 && DifferenceHeight != RelativeLayoutMainHeightDifference)
                    {
                        RelativeLayoutMain.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, ScreenHeight - DifferenceHeight));
                        RelativeLayoutMainHeightDifference = DifferenceHeight;
                    }
                    else if (DifferenceHeight != RelativeLayoutMainHeightDifference)
                    {
                        RelativeLayoutMain.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, ScreenHeight));
                        RelativeLayoutMainHeightDifference = DifferenceHeight;
                    }
                    else if (RelativeLayoutMainHeightDifference != 0)
                    {
                        RelativeLayoutMain.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, ScreenHeight + Math.abs(RelativeLayoutMainHeightDifference)));
                        RelativeLayoutMainHeightDifference = 0;
                    }

                    RelativeLayoutMain.requestLayout();
                }
            };

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
                    getActivity().onBackPressed();
                }
            });

            RelativeLayoutHeader.addView(ImageViewBack);

            RelativeLayout.LayoutParams TextViewHeaderParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewHeaderParam.addRule(RelativeLayout.RIGHT_OF, ImageViewBack.getId());
            TextViewHeaderParam.addRule(RelativeLayout.CENTER_VERTICAL);

            TextView TextViewHeader = new TextView(context);
            TextViewHeader.setLayoutParams(TextViewHeaderParam);
            TextViewHeader.setTextColor(ContextCompat.getColor(context, R.color.White));
            TextViewHeader.setText(getString(R.string.ResetFragmentTitle));
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

            RelativeLayout.LayoutParams ScrollViewMainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            ScrollViewMainParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

            ScrollView ScrollViewMain = new ScrollView(context);
            ScrollViewMain.setLayoutParams(ScrollViewMainParam);
            ScrollViewMain.setFillViewport(true);
            ScrollViewMain.setId(MiscHandler.GenerateViewID());

            RelativeLayoutMain.addView(ScrollViewMain);

            RelativeLayout RelativeLayoutMain2 = new RelativeLayout(context);
            RelativeLayoutMain2.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

            ScrollViewMain.addView(RelativeLayoutMain2);

            RelativeLayout.LayoutParams TextViewMessageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewMessageParam.setMargins(MiscHandler.ToDimension(context, 25), MiscHandler.ToDimension(context, 25), MiscHandler.ToDimension(context, 25), MiscHandler.ToDimension(context, 25));
            TextViewMessageParam.addRule(RelativeLayout.CENTER_HORIZONTAL);

            TextView TextViewMessage = new TextView(context);
            TextViewMessage.setLayoutParams(TextViewMessageParam);
            TextViewMessage.setTextColor(ContextCompat.getColor(context, R.color.Black));
            TextViewMessage.setText(getString(R.string.ResetFragmentMessage));
            TextViewMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            TextViewMessage.setGravity(Gravity.CENTER);
            TextViewMessage.setId(MiscHandler.GenerateViewID());

            RelativeLayoutMain2.addView(TextViewMessage);

            RelativeLayout.LayoutParams EditTextEmailOrUsernameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 45));
            EditTextEmailOrUsernameParam.addRule(RelativeLayout.BELOW, TextViewMessage.getId());
            EditTextEmailOrUsernameParam.setMargins(MiscHandler.ToDimension(context, 15), 0, MiscHandler.ToDimension(context, 15), 0);

            final EditText EditTextEmailOrUsername = new EditText(new ContextThemeWrapper(context, R.style.GeneralEditTextTheme));
            EditTextEmailOrUsername.setLayoutParams(EditTextEmailOrUsernameParam);
            EditTextEmailOrUsername.setGravity(Gravity.CENTER);
            EditTextEmailOrUsername.setPadding(MiscHandler.ToDimension(context, 10),MiscHandler.ToDimension(context, 10),MiscHandler.ToDimension(context, 10),MiscHandler.ToDimension(context, 10));
            EditTextEmailOrUsername.setHint(getString(R.string.ResetFragmentEmailOrUsername));
            EditTextEmailOrUsername.setId(MiscHandler.GenerateViewID());
            EditTextEmailOrUsername.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            EditTextEmailOrUsername.setFilters(new InputFilter[] { new InputFilter.LengthFilter(64) });
            EditTextEmailOrUsername.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            EditTextEmailOrUsername.setBackgroundColor(Color.parseColor("#4fcbd6dc"));
            EditTextEmailOrUsername.requestFocus();
            EditTextEmailOrUsername.setId(MiscHandler.GenerateViewID());
            EditTextEmailOrUsername.addTextChangedListener(new TextWatcher()
            {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                @Override
                public void afterTextChanged(Editable s) { }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count)
                {
                    ButtonReset.setEnabled((s.length() > 2));
                }
            });

            RelativeLayoutMain2.addView(EditTextEmailOrUsername);

            RelativeLayout.LayoutParams ButtonResetParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 45));
            ButtonResetParam.addRule(RelativeLayout.BELOW, EditTextEmailOrUsername.getId());
            ButtonResetParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));

            GradientDrawable ShapeEnable = new GradientDrawable();
            ShapeEnable.setColor(ContextCompat.getColor(context, R.color.BlueLight));
            ShapeEnable.setCornerRadius(MiscHandler.ToDimension(context, 7));

            GradientDrawable ShapeDisable = new GradientDrawable();
            ShapeDisable.setCornerRadius(MiscHandler.ToDimension(context, 7));
            ShapeDisable.setColor(ContextCompat.getColor(context, R.color.Gray2));

            StateListDrawable StateReset = new StateListDrawable();
            StateReset.addState(new int[] { android.R.attr.state_enabled }, ShapeEnable);
            StateReset.addState(new int[] { -android.R.attr.state_enabled }, ShapeDisable);

            ButtonReset.setLayoutParams(ButtonResetParam);
            ButtonReset.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            ButtonReset.setTextColor(ContextCompat.getColor(context, R.color.White));
            ButtonReset.setText(getString(R.string.ResetFragmentSubmit));
            ButtonReset.setBackground(StateReset);
            ButtonReset.setPadding(0, 0, 0, 0);
            ButtonReset.setEnabled(false);
            ButtonReset.setAllCaps(false);
            ButtonReset.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    ButtonReset.setVisibility(View.GONE);
                    LinearLayoutLoading.setVisibility(View.VISIBLE);
                    LoadingViewReset.Start();

                    AndroidNetworking.post(MiscHandler.GetRandomServer("ResetPassword"))
                    .addBodyParameter("EmailOrUsername", EditTextEmailOrUsername.getText().toString())
                    .setTag("ResetPassword")
                    .build()
                    .getAsString(new StringRequestListener()
                    {
                        @Override
                        public void onResponse(String Response)
                        {
                            LoadingViewReset.Stop();
                            LinearLayoutLoading.setVisibility(View.INVISIBLE);
                            ButtonReset.setVisibility(View.VISIBLE);

                            try
                            {
                                if (new JSONObject(Response).getInt("Message") == 1000)
                                    MiscHandler.Toast(context, getString(R.string.ResetFragmentSuccess));
                                else
                                    MiscHandler.Toast(context, getString(R.string.ResetFragmentError));
                            }
                            catch (Exception e)
                            {
                                MiscHandler.Debug("WelcomeActivity-RequestReset: " + e.toString());
                            }
                        }

                        @Override
                        public void onError(ANError anError)
                        {
                            LoadingViewReset.Stop();
                            LinearLayoutLoading.setVisibility(View.INVISIBLE);
                            ButtonReset.setVisibility(View.VISIBLE);
                            MiscHandler.Toast(context, getString(R.string.NoInternet));
                        }
                    });
                }
            });

            RelativeLayoutMain2.addView(ButtonReset);

            RelativeLayout.LayoutParams LinearLayoutLoadingParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 45));
            LinearLayoutLoadingParam.addRule(RelativeLayout.BELOW, EditTextEmailOrUsername.getId());
            LinearLayoutLoadingParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));

            LinearLayoutLoading.setLayoutParams(LinearLayoutLoadingParam);
            LinearLayoutLoading.setVisibility(View.INVISIBLE);
            LinearLayoutLoading.setGravity(Gravity.CENTER);
            LinearLayoutLoading.setBackground(ShapeEnable);

            RelativeLayoutMain2.addView(LinearLayoutLoading);

            RelativeLayout.LayoutParams LoadingViewResetParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            LoadingViewResetParam.addRule(RelativeLayout.CENTER_IN_PARENT);

            LoadingViewReset.setLayoutParams(LoadingViewResetParam);
            LoadingViewReset.SetColor(R.color.White);

            LinearLayoutLoading.addView(LoadingViewReset);

            return RelativeLayoutMain;
        }

        @Override
        public void onResume()
        {
            super.onResume();
            RelativeLayoutMain.getViewTreeObserver().addOnGlobalLayoutListener(RelativeLayoutMainListener);

            InputMethodManager IMM = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            IMM.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }

        @Override
        public void onPause()
        {
            super.onPause();
            MiscHandler.HideSoftKey(getActivity());
            AndroidNetworking.forceCancel("ResetFragment");
            RelativeLayoutMain.getViewTreeObserver().removeOnGlobalLayoutListener(RelativeLayoutMainListener);
        }
    }

    private static String GenerateSession()
    {
        return "BioGram Android " + BuildConfig.VERSION_NAME + " - " + Build.MODEL + " - " + Build.MANUFACTURER + " - API " + Build.VERSION.SDK_INT;
    }
}