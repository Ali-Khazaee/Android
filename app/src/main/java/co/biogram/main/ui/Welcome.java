package co.biogram.main.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONObject;

import co.biogram.fragment.FragmentActivity;
import co.biogram.fragment.FragmentBase;
import co.biogram.main.R;
import co.biogram.main.activity.MainActivity;
import co.biogram.main.handler.MiscHandler;
import co.biogram.main.handler.SharedHandler;

public class Welcome extends FragmentBase
{
    private GoogleApiClient GoogleClientApi;
    private boolean GoogleIsAvailable = false;

    @Override
    public void OnCreate()
    {
        final FragmentActivity activity = GetActivity();

        final RelativeLayout RelativeLayoutMain = new RelativeLayout(activity);
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

        ScrollView ScrollViewMain = new ScrollView(activity);
        ScrollViewMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        ScrollViewMain.setBackgroundResource(R.color.White);
        ScrollViewMain.setFillViewport(true);

        RelativeLayoutMain.addView(ScrollViewMain);

        RelativeLayout RelativeLayoutScroll = new RelativeLayout(activity);
        RelativeLayoutScroll.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

        ScrollViewMain.addView(RelativeLayoutScroll);

        LinearLayout LinearLayoutHeader = new LinearLayout(activity);
        LinearLayoutHeader.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(activity, 200)));
        LinearLayoutHeader.setOrientation(LinearLayout.VERTICAL);
        LinearLayoutHeader.setGravity(Gravity.CENTER_HORIZONTAL);
        LinearLayoutHeader.setId(MiscHandler.GenerateViewID());
        LinearLayoutHeader.setBackgroundResource(R.color.BlueLight);

        RelativeLayoutScroll.addView(LinearLayoutHeader);

        RelativeLayout RelativeLayoutLanguage = new RelativeLayout(activity);
        RelativeLayoutLanguage.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(activity, 35)));

        LinearLayoutHeader.addView(RelativeLayoutLanguage);

        RelativeLayout.LayoutParams TextViewLanguageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewLanguageParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        TextView TextViewLanguage = new TextView(activity);
        TextViewLanguage.setLayoutParams(TextViewLanguageParam);
        TextViewLanguage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        TextViewLanguage.setText(activity.getString(R.string.WelcomeLanguage));
        TextViewLanguage.setTextColor(ContextCompat.getColor(activity, R.color.White));
        TextViewLanguage.setTypeface(Typeface.createFromAsset(activity.getAssets(), "iran-sans.ttf"));
        TextViewLanguage.setId(MiscHandler.GenerateViewID());
        TextViewLanguage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Dialog DialogLanguage = new Dialog(activity);
                DialogLanguage.requestWindowFeature(Window.FEATURE_NO_TITLE);
                DialogLanguage.setCancelable(false);

                RelativeLayout RelativeLayoutLanguage = new RelativeLayout(activity);
                RelativeLayoutLanguage.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

                RelativeLayout.LayoutParams TextViewTitleLanguageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, MiscHandler.ToDimension(activity, 56));
                TextViewTitleLanguageParam.addRule(MiscHandler.Align("R"));

                TextView TextViewTitleLanguage = new TextView(activity);
                TextViewTitleLanguage.setLayoutParams(TextViewTitleLanguageParam);
                TextViewTitleLanguage.setTextColor(ContextCompat.getColor(activity, R.color.Black));
                TextViewTitleLanguage.setText(activity.getString(R.string.WelcomeLanguageSelect));
                TextViewTitleLanguage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                TextViewTitleLanguage.setPadding(MiscHandler.ToDimension(activity, 10), 0, MiscHandler.ToDimension(activity, 10), 0);
                TextViewTitleLanguage.setTypeface(Typeface.createFromAsset(activity.getAssets(), "iran-sans.ttf"));
                TextViewTitleLanguage.setGravity(Gravity.CENTER);

                RelativeLayoutLanguage.addView(TextViewTitleLanguage);

                RelativeLayout.LayoutParams ImageViewCloseLanguageParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(activity, 56), MiscHandler.ToDimension(activity, 56));
                ImageViewCloseLanguageParam.addRule(MiscHandler.Align("L"));

                ImageView ImageViewCloseLanguage = new ImageView(activity);
                ImageViewCloseLanguage.setLayoutParams(ImageViewCloseLanguageParam);
                ImageViewCloseLanguage.setImageResource(R.drawable.ic_close_blue);
                ImageViewCloseLanguage.setPadding(MiscHandler.ToDimension(activity, 14), MiscHandler.ToDimension(activity, 14), MiscHandler.ToDimension(activity, 14), MiscHandler.ToDimension(activity, 14));
                ImageViewCloseLanguage.setId(MiscHandler.GenerateViewID());
                ImageViewCloseLanguage.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { DialogLanguage.dismiss(); } });

                RelativeLayoutLanguage.addView(ImageViewCloseLanguage);

                RelativeLayout.LayoutParams ViewLineLanguageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(activity, 1));
                ViewLineLanguageParam.addRule(RelativeLayout.BELOW, ImageViewCloseLanguage.getId());

                View ViewLineLanguage = new View(activity);
                ViewLineLanguage.setLayoutParams(ViewLineLanguageParam);
                ViewLineLanguage.setId(MiscHandler.GenerateViewID());
                ViewLineLanguage.setBackgroundResource(R.color.Gray2);

                RelativeLayoutLanguage.addView(ViewLineLanguage);

                RelativeLayout.LayoutParams TextViewEnglishLanguageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, MiscHandler.ToDimension(activity, 56));
                TextViewEnglishLanguageParam.addRule(RelativeLayout.BELOW, ViewLineLanguage.getId());
                TextViewEnglishLanguageParam.addRule(MiscHandler.Align("R"));

                TextView TextViewEnglishLanguage = new TextView(activity);
                TextViewEnglishLanguage.setLayoutParams(TextViewEnglishLanguageParam);
                TextViewEnglishLanguage.setTextColor(ContextCompat.getColor(activity, R.color.Black));
                TextViewEnglishLanguage.setText(activity.getString(R.string.WelcomeLanguageEnglish));
                TextViewEnglishLanguage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                TextViewEnglishLanguage.setPadding(MiscHandler.ToDimension(activity, 10), 0, MiscHandler.ToDimension(activity, 10), 0);
                TextViewEnglishLanguage.setId(MiscHandler.GenerateViewID());
                TextViewEnglishLanguage.setGravity(Gravity.CENTER_VERTICAL);
                TextViewEnglishLanguage.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { DialogLanguage.dismiss(); MiscHandler.ChangeLanguage(activity, "en"); } });

                RelativeLayoutLanguage.addView(TextViewEnglishLanguage);

                RelativeLayout.LayoutParams ViewLine2LanguageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(activity, 1));
                ViewLine2LanguageParam.addRule(RelativeLayout.BELOW, TextViewEnglishLanguage.getId());

                View ViewLine2Language = new View(activity);
                ViewLine2Language.setLayoutParams(ViewLine2LanguageParam);
                ViewLine2Language.setId(MiscHandler.GenerateViewID());
                ViewLine2Language.setBackgroundResource(R.color.Gray);

                RelativeLayoutLanguage.addView(ViewLine2Language);

                RelativeLayout.LayoutParams TextViewPersianLanguageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, MiscHandler.ToDimension(activity, 56));
                TextViewPersianLanguageParam.addRule(MiscHandler.Align("R"));
                TextViewPersianLanguageParam.addRule(RelativeLayout.BELOW, ViewLine2Language.getId());

                TextView TextViewPersianLanguage = new TextView(activity);
                TextViewPersianLanguage.setLayoutParams(TextViewPersianLanguageParam);
                TextViewPersianLanguage.setTextColor(ContextCompat.getColor(activity, R.color.Black));
                TextViewPersianLanguage.setText(activity.getString(R.string.WelcomeLanguagePersian));
                TextViewPersianLanguage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                TextViewPersianLanguage.setPadding(MiscHandler.ToDimension(activity, 10), 0, MiscHandler.ToDimension(activity, 10), 0);
                TextViewPersianLanguage.setTypeface(Typeface.createFromAsset(activity.getAssets(), "iran-sans.ttf"));
                TextViewPersianLanguage.setGravity(Gravity.CENTER_VERTICAL);
                TextViewPersianLanguage.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { DialogLanguage.dismiss(); MiscHandler.ChangeLanguage(activity, "fa"); } });

                RelativeLayoutLanguage.addView(TextViewPersianLanguage);

                DialogLanguage.setContentView(RelativeLayoutLanguage);
                DialogLanguage.show();
            }
        });

        RelativeLayoutLanguage.addView(TextViewLanguage);

        RelativeLayout.LayoutParams ImageViewLanguageParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(activity, 30), MiscHandler.ToDimension(activity, 30));
        ImageViewLanguageParam.addRule(MiscHandler.AlignTo("R"), TextViewLanguage.getId());

        ImageView ImageViewLanguage = new ImageView(activity);
        ImageViewLanguage.setLayoutParams(ImageViewLanguageParam);
        ImageViewLanguage.setImageResource(R.drawable.ic_option_white);
        ImageViewLanguage.setPadding(MiscHandler.ToDimension(activity, 7), MiscHandler.ToDimension(activity, 7), MiscHandler.ToDimension(activity, 7), MiscHandler.ToDimension(activity, 7));

        RelativeLayoutLanguage.addView(ImageViewLanguage);

        LinearLayout.LayoutParams ImageViewHeaderParam = new LinearLayout.LayoutParams(MiscHandler.ToDimension(activity, 150), MiscHandler.ToDimension(activity, 65));
        ImageViewHeaderParam.setMargins(0, MiscHandler.ToDimension(activity, 15), 0, 0);

        ImageView ImageViewHeader = new ImageView(activity);
        ImageViewHeader.setLayoutParams(ImageViewHeaderParam);
        ImageViewHeader.setImageResource(MiscHandler.IsFa() ? R.drawable.ic_logo_fa : R.drawable.ic_logo);
        ImageViewHeader.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DialogPermission DialogPermissionSMS = new DialogPermission(activity);
                DialogPermissionSMS.SetContentView(R.drawable.ic_permission_sms, "دسترسی به خوانده شدن پیامک را فعال کنید");
            }
        });

        LinearLayoutHeader.addView(ImageViewHeader);

        LinearLayout.LayoutParams TextViewHeaderParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        TextViewHeaderParam.setMargins(0, MiscHandler.ToDimension(activity, 10), 0, 0);

        TextView TextViewHeader = new TextView(activity);
        TextViewHeader.setLayoutParams(TextViewHeaderParam);
        TextViewHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        TextViewHeader.setText(activity.getString(R.string.WelcomeHeader));
        TextViewHeader.setTextColor(ContextCompat.getColor(activity, R.color.White));
        TextViewHeader.setTypeface(Typeface.createFromAsset(activity.getAssets(), "iran-sans.ttf"));

        LinearLayoutHeader.addView(TextViewHeader);

        TextView TextViewHeader2 = new TextView(activity);
        TextViewHeader2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        TextViewHeader2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        TextViewHeader2.setText(activity.getString(R.string.WelcomeHeader2));
        TextViewHeader2.setTextColor(ContextCompat.getColor(activity, R.color.White));
        TextViewHeader2.setTypeface(Typeface.createFromAsset(activity.getAssets(), "iran-sans.ttf"));

        LinearLayoutHeader.addView(TextViewHeader2);

        RelativeLayout.LayoutParams RelativeLayoutSignUpParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(activity, 270), MiscHandler.ToDimension(activity, 45));
        RelativeLayoutSignUpParam.setMargins(0, MiscHandler.ToDimension(activity, 30), 0, 0);
        RelativeLayoutSignUpParam.addRule(RelativeLayout.BELOW, LinearLayoutHeader.getId());
        RelativeLayoutSignUpParam.addRule(RelativeLayout.CENTER_HORIZONTAL);

        GradientDrawable GradientDrawableSignUp = new GradientDrawable();
        GradientDrawableSignUp.setColor(ContextCompat.getColor(activity, R.color.BlueLight));
        GradientDrawableSignUp.setCornerRadius(MiscHandler.ToDimension(activity, 7));

        Button ButtonSignUp = new Button(activity, null, android.R.attr.borderlessButtonStyle);
        ButtonSignUp.setLayoutParams(RelativeLayoutSignUpParam);
        ButtonSignUp.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        ButtonSignUp.setTextColor(ContextCompat.getColor(activity, R.color.White));
        ButtonSignUp.setText(activity.getString(R.string.WelcomeSignUp));
        ButtonSignUp.setId(MiscHandler.GenerateViewID());
        ButtonSignUp.setBackground(GradientDrawableSignUp);
        ButtonSignUp.setAllCaps(false);
        ButtonSignUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                TranslateAnimation Anim = MiscHandler.IsRTL() ? new TranslateAnimation(0f, -1000f, 0f, 0f) : new TranslateAnimation(-100f, 0f, 0f, 0f);
                Anim.setDuration(300);

                RelativeLayoutMain.setAnimation(Anim);

                GetActivity().GetManager().OpenView(new SignUpPhone(), R.id.WelcomeActivityContainer, "SignUpPhone");
            }
        });

        RelativeLayoutScroll.addView(ButtonSignUp);

        RelativeLayout.LayoutParams RelativeLayoutORParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(activity, 45));
        RelativeLayoutORParam.setMargins(0, MiscHandler.ToDimension(activity, 20), 0, MiscHandler.ToDimension(activity, 5));
        RelativeLayoutORParam.addRule(RelativeLayout.BELOW, ButtonSignUp.getId());

        RelativeLayout RelativeLayoutOR = new RelativeLayout(activity);
        RelativeLayoutOR.setLayoutParams(RelativeLayoutORParam);
        RelativeLayoutOR.setId(MiscHandler.GenerateViewID());

        RelativeLayoutScroll.addView(RelativeLayoutOR);

        RelativeLayout.LayoutParams TextViewORParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(activity, 40), RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewORParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        TextView TextViewOR = new TextView(activity);
        TextViewOR.setLayoutParams(TextViewORParam);
        TextViewOR.setTypeface(null, Typeface.BOLD);
        TextViewOR.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        TextViewOR.setText(activity.getString(R.string.WelcomeOR));
        TextViewOR.setId(MiscHandler.GenerateViewID());
        TextViewOR.setGravity(Gravity.CENTER);
        TextViewOR.setTextColor(ContextCompat.getColor(activity, R.color.BlueGray));

        RelativeLayoutOR.addView(TextViewOR);

        RelativeLayout.LayoutParams ViewOrLine1Param = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(activity, 115), MiscHandler.ToDimension(activity, 1));
        ViewOrLine1Param.addRule(RelativeLayout.RIGHT_OF, TextViewOR.getId());
        ViewOrLine1Param.addRule(RelativeLayout.CENTER_VERTICAL);

        View ViewOrLine1 = new View(activity);
        ViewOrLine1.setLayoutParams(ViewOrLine1Param);
        ViewOrLine1.setBackgroundResource(R.color.BlueGray);

        RelativeLayoutOR.addView(ViewOrLine1);

        RelativeLayout.LayoutParams ViewOrLine2Param = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(activity, 115), MiscHandler.ToDimension(activity, 1));
        ViewOrLine2Param.addRule(RelativeLayout.LEFT_OF, TextViewOR.getId());
        ViewOrLine2Param.addRule(RelativeLayout.CENTER_VERTICAL);

        View ViewOrLine2 = new View(activity);
        ViewOrLine2.setLayoutParams(ViewOrLine2Param);
        ViewOrLine2.setBackgroundResource(R.color.BlueGray);

        RelativeLayoutOR.addView(ViewOrLine2);

        RelativeLayout.LayoutParams LinearLayoutGoogleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LinearLayoutGoogleParam.addRule(RelativeLayout.BELOW, RelativeLayoutOR.getId());
        LinearLayoutGoogleParam.addRule(RelativeLayout.CENTER_HORIZONTAL);

        LinearLayout LinearLayoutGoogle = new LinearLayout(activity);
        LinearLayoutGoogle.setLayoutParams(LinearLayoutGoogleParam);
        LinearLayoutGoogle.setGravity(Gravity.CENTER);
        LinearLayoutGoogle.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayoutGoogle.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { activity.startActivityForResult(Auth.GoogleSignInApi.getSignInIntent(GoogleClientApi), 100); } });

        RelativeLayoutScroll.addView(LinearLayoutGoogle);

        LinearLayout.LayoutParams ImageViewGoogleParam = new LinearLayout.LayoutParams(MiscHandler.ToDimension(activity, 30), MiscHandler.ToDimension(activity, 30));
        ImageViewGoogleParam.setMargins(MiscHandler.ToDimension(activity, 5), 0, MiscHandler.ToDimension(activity, 5), 0);

        ImageView ImageViewGoogle = new ImageView(activity);
        ImageViewGoogle.setLayoutParams(ImageViewGoogleParam);
        ImageViewGoogle.setBackgroundResource(R.drawable.ic_google);

        LinearLayoutGoogle.addView(ImageViewGoogle);

        TextView TextViewGoogle = new TextView(activity);
        TextViewGoogle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        TextViewGoogle.setTextColor(ContextCompat.getColor(activity, R.color.BlueLight));
        TextViewGoogle.setTypeface(null, Typeface.BOLD);
        TextViewGoogle.setText(activity.getString(R.string.WelcomeSignInGoogle));
        TextViewGoogle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

        LinearLayoutGoogle.addView(TextViewGoogle);

        RelativeLayout.LayoutParams RelativeLayoutSignInParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(activity, 56));
        RelativeLayoutSignInParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        RelativeLayout RelativeLayoutSignIn = new RelativeLayout(activity);
        RelativeLayoutSignIn.setLayoutParams(RelativeLayoutSignInParam);
        RelativeLayoutSignIn.setBackgroundResource(R.color.White5);
        RelativeLayoutSignIn.setGravity(Gravity.CENTER);
        RelativeLayoutSignIn.setId(MiscHandler.GenerateViewID());
        RelativeLayoutSignIn.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { GetActivity().GetManager().OpenView(new SignUpPhoneVerification(), R.id.WelcomeActivityContainer, "SignUpPhoneVerification"); } });

        RelativeLayoutScroll.addView(RelativeLayoutSignIn);

        TextView TextViewSignIn = new TextView(activity);
        TextViewSignIn.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewSignIn.setTextColor(ContextCompat.getColor(activity, R.color.Gray4));
        TextViewSignIn.setText(activity.getString(R.string.WelcomeSignIn));
        TextViewSignIn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        TextViewSignIn.setId(MiscHandler.GenerateViewID());

        RelativeLayoutSignIn.addView(TextViewSignIn);

        RelativeLayout.LayoutParams TextViewSignIn2Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewSignIn2Param.setMargins(MiscHandler.ToDimension(activity, 5), 0, MiscHandler.ToDimension(activity, 5), 0);
        TextViewSignIn2Param.addRule(MiscHandler.AlignTo("R"), TextViewSignIn.getId());

        TextView TextViewSignIn2 = new TextView(activity);
        TextViewSignIn2.setLayoutParams(TextViewSignIn2Param);
        TextViewSignIn2.setTextColor(ContextCompat.getColor(activity, R.color.BlueLight));
        TextViewSignIn2.setText(activity.getString(R.string.WelcomeSignIn2));
        TextViewSignIn2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

        RelativeLayoutSignIn.addView(TextViewSignIn2);

        RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(activity, 1));
        ViewLineParam.addRule(RelativeLayout.ABOVE, RelativeLayoutSignIn.getId());

        View ViewLine = new View(activity);
        ViewLine.setLayoutParams(ViewLineParam);
        ViewLine.setBackgroundResource(R.color.Gray2);
        ViewLine.setId(MiscHandler.GenerateViewID());

        RelativeLayoutScroll.addView(ViewLine);

        RelativeLayout.LayoutParams TextViewTerm2Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTerm2Param.addRule(RelativeLayout.ABOVE, ViewLine.getId());
        TextViewTerm2Param.addRule(RelativeLayout.CENTER_HORIZONTAL);
        TextViewTerm2Param.setMargins(0, 0, 0, MiscHandler.ToDimension(activity, 20));

        TextView TextViewTerm2 = new TextView(activity);
        TextViewTerm2.setLayoutParams(TextViewTerm2Param);
        TextViewTerm2.setTextColor(ContextCompat.getColor(activity, R.color.BlueGray));
        TextViewTerm2.setText(activity.getString(R.string.WelcomeTerm2));
        TextViewTerm2.setTypeface(null, Typeface.BOLD);
        TextViewTerm2.setId(MiscHandler.GenerateViewID());
        TextViewTerm2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        TextViewTerm2.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://biogram.co"))); } });

        RelativeLayoutScroll.addView(TextViewTerm2);

        RelativeLayout.LayoutParams TextViewTermParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTermParam.addRule(RelativeLayout.ABOVE, TextViewTerm2.getId());
        TextViewTermParam.addRule(RelativeLayout.CENTER_HORIZONTAL);

        TextView TextViewTerm = new TextView(activity);
        TextViewTerm.setLayoutParams(TextViewTermParam);
        TextViewTerm.setTextColor(ContextCompat.getColor(activity, R.color.BlueGray));
        TextViewTerm.setText(activity.getString(R.string.WelcomeTerm));
        TextViewTerm.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

        if (MiscHandler.IsFa())
        {
            ButtonSignUp.setTypeface(null, Typeface.BOLD);
            ButtonSignUp.setPadding(0, -MiscHandler.ToDimension(activity, 2), 0, 0);
            ButtonSignUp.setTypeface(Typeface.createFromAsset(activity.getAssets(), "iran-sans.ttf"));

            TextViewOR.setTypeface(Typeface.createFromAsset(activity.getAssets(), "iran-sans.ttf"));

            TextViewGoogle.setTypeface(Typeface.createFromAsset(activity.getAssets(), "iran-sans.ttf"));

            TextViewSignIn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            TextViewSignIn.setTypeface(Typeface.createFromAsset(activity.getAssets(), "iran-sans.ttf"));

            TextViewSignIn2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            TextViewSignIn2.setTypeface(Typeface.createFromAsset(activity.getAssets(), "iran-sans.ttf"));

            TextViewTerm2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            TextViewTerm2.setTypeface(Typeface.createFromAsset(activity.getAssets(), "iran-sans.ttf"));

            TextViewTerm.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            TextViewTerm.setTypeface(Typeface.createFromAsset(activity.getAssets(), "iran-sans.ttf"));
        }

        RelativeLayoutScroll.addView(TextViewTerm);

        ViewMain = RelativeLayoutMain;

        GoogleIsAvailable = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(activity) == ConnectionResult.SUCCESS;

        if (GoogleIsAvailable)
        {
            GoogleClientApi = new GoogleApiClient.Builder(activity.getApplicationContext())
            .addApi(Auth.GOOGLE_SIGN_IN_API, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().requestIdToken("590625045379-pnhlgdqpr5i8ma705ej7akcggsr08vdf.apps.googleusercontent.com").build())
            .build();
        }
    }

    @Override
    public void OnResume()
    {
        if (GoogleIsAvailable)
            GoogleClientApi.connect();
    }

    @Override
    public void OnPause()
    {
        AndroidNetworking.forceCancel("Welcome");

        if (GoogleIsAvailable)
        {
            if (GoogleClientApi.isConnected())
                Auth.GoogleSignInApi.signOut(GoogleClientApi);

            GoogleClientApi.disconnect();
        }
    }

    @Override
    public void OnActivityResult(int RequestCode, int ResultCode, Intent intent)
    {
        if (RequestCode == 100)
        {
            GoogleSignInResult SignResult = Auth.GoogleSignInApi.getSignInResultFromIntent(intent);

            if (SignResult.isSuccess())
            {
                GoogleSignInAccount AccountResult = SignResult.getSignInAccount();

                if (AccountResult != null)
                {
                    final Activity activity = GetActivity();

                    AndroidNetworking.post(MiscHandler.GetRandomServer("SignInGoogle"))
                    .addBodyParameter("Token", AccountResult.getIdToken())
                    .addBodyParameter("Session", MiscHandler.GenerateSession())
                    .setTag("Welcome")
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
                                    SharedHandler.SetBoolean(activity, "IsLogin", true);
                                    SharedHandler.SetString(activity, "TOKEN", Result.getString("TOKEN"));
                                    SharedHandler.SetString(activity, "ID", Result.getString("ID"));
                                    SharedHandler.SetString(activity, "Username", Result.getString("Username"));
                                    SharedHandler.SetString(activity, "Avatar", Result.getString("Avatar"));
                                    SharedHandler.SetBoolean(activity, "IsGoogle", true);

                                    activity.startActivity(new Intent(activity, MainActivity.class));
                                    activity.finish();
                                    return;
                                }

                                MiscHandler.Toast(activity, activity.getString(R.string.WelcomeGoogleError));
                            }
                            catch (Exception e)
                            {
                                MiscHandler.Debug("Welcome-SignInGoogle: " + e.toString());
                            }
                        }

                        @Override
                        public void onError(ANError e)
                        {
                            MiscHandler.Toast(activity, activity.getString(R.string.GeneralNoInternet));
                        }
                    });
                }
            }
        }
    }
}
