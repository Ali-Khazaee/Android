package co.biogram.main.ui.welcome;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

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

import co.biogram.main.R;
import co.biogram.main.activity.SocialActivity;
import co.biogram.main.fragment.FragmentView;
import co.biogram.main.handler.Misc;
import co.biogram.main.ui.view.Button;
import co.biogram.main.ui.view.LoadingView;
import co.biogram.main.ui.view.TextView;

public class WelcomeUI extends FragmentView {
    private LoadingView LoadingViewGoogle;
    private RelativeLayout RelativeLayoutGoogle;
    private ScrollView ScrollViewMain;
    private GoogleApiClient GoogleApiClient;
    private boolean IsGoogleAvailable;

    @Override
    public void OnCreate() {
        ScrollViewMain = new ScrollView(Activity);
        ScrollViewMain.setLayoutParams(new ScrollView.LayoutParams(ScrollView.LayoutParams.MATCH_PARENT, ScrollView.LayoutParams.MATCH_PARENT));
        ScrollViewMain.setBackgroundResource(R.color.TextDark);
        ScrollViewMain.setFillViewport(true);

        RelativeLayout RelativeLayoutMain = new RelativeLayout(Activity);
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

        ScrollViewMain.addView(RelativeLayoutMain);

        LinearLayout LinearLayoutHeader = new LinearLayout(Activity);
        LinearLayoutHeader.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(200)));
        LinearLayoutHeader.setBackgroundResource(R.color.Primary);
        LinearLayoutHeader.setOrientation(LinearLayout.VERTICAL);
        LinearLayoutHeader.setGravity(Gravity.CENTER_HORIZONTAL);
        LinearLayoutHeader.setId(Misc.generateViewId());

        RelativeLayoutMain.addView(LinearLayoutHeader);

        RelativeLayout RelativeLayoutLanguage = new RelativeLayout(Activity);
        RelativeLayoutLanguage.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(35)));

        LinearLayoutHeader.addView(RelativeLayoutLanguage);

        RelativeLayout.LayoutParams TextViewLanguageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewLanguageParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        TextView TextViewLanguage = new TextView(Activity, 16, false);
        TextViewLanguage.setLayoutParams(TextViewLanguageParam);
        TextViewLanguage.setText(Misc.String(R.string.WelcomeUILanguage));
        TextViewLanguage.setId(Misc.generateViewId());
        TextViewLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog DialogLanguage = new Dialog(Activity);
                DialogLanguage.requestWindowFeature(Window.FEATURE_NO_TITLE);
                DialogLanguage.setCancelable(true);

                LinearLayout LinearLayoutLanguage = new LinearLayout(Activity);
                LinearLayoutLanguage.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                LinearLayoutLanguage.setOrientation(LinearLayout.VERTICAL);

                RelativeLayout RelativeLayoutHeader = new RelativeLayout(Activity);
                RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56)));

                LinearLayoutLanguage.addView(RelativeLayoutHeader);

                RelativeLayout.LayoutParams TextViewTitleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, Misc.ToDP(56));
                TextViewTitleParam.addRule(Misc.Align("R"));

                TextView TextViewTitle = new TextView(Activity, 16, false);
                TextViewTitle.setLayoutParams(TextViewTitleParam);
                TextViewTitle.SetColor(R.color.TextWhite);
                TextViewTitle.setText(Misc.String(R.string.WelcomeUILanguageSelect));
                TextViewTitle.setPadding(Misc.ToDP(15), 0, Misc.ToDP(15), 0);
                TextViewTitle.setGravity(Gravity.CENTER_VERTICAL);

                RelativeLayoutHeader.addView(TextViewTitle);

                RelativeLayout.LayoutParams ImageViewCloseParam = new RelativeLayout.LayoutParams(Misc.ToDP(56), Misc.ToDP(56));
                ImageViewCloseParam.addRule(Misc.Align("L"));

                ImageView ImageViewClose = new ImageView(Activity);
                ImageViewClose.setLayoutParams(ImageViewCloseParam);
                ImageViewClose.setImageResource(R.drawable.close_blue);
                ImageViewClose.setPadding(Misc.ToDP(6), Misc.ToDP(6), Misc.ToDP(6), Misc.ToDP(6));
                ImageViewClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogLanguage.dismiss();
                    }
                });

                RelativeLayoutHeader.addView(ImageViewClose);

                View ViewLine = new View(Activity);
                ViewLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1)));
                ViewLine.setBackgroundResource(R.color.Gray);

                LinearLayoutLanguage.addView(ViewLine);

                RelativeLayout.LayoutParams TextViewEnglishParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56));
                TextViewEnglishParam.addRule(Misc.Align("R"));

                TextView TextViewEnglish = new TextView(Activity, 16, false);
                TextViewEnglish.setLayoutParams(TextViewEnglishParam);
                TextViewEnglish.SetColor(R.color.TextWhite);
                TextViewEnglish.setText(Misc.String(R.string.WelcomeUILanguageEnglish));
                TextViewEnglish.setPadding(Misc.ToDP(15), 0, Misc.ToDP(15), 0);
                TextViewEnglish.setGravity(Misc.Gravity("L") | Gravity.CENTER_VERTICAL);
                TextViewEnglish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogLanguage.dismiss();
                        Misc.ChangeLanguage("en");
                    }
                });

                LinearLayoutLanguage.addView(TextViewEnglish);

                View ViewLine2 = new View(Activity);
                ViewLine2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1)));
                ViewLine2.setBackgroundResource(R.color.Gray);

                LinearLayoutLanguage.addView(ViewLine2);

                RelativeLayout.LayoutParams TextViewPersianParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56));
                TextViewPersianParam.addRule(Misc.Align("R"));

                TextView TextViewPersian = new TextView(Activity, 16, false);
                TextViewPersian.setLayoutParams(TextViewPersianParam);
                TextViewPersian.SetColor(R.color.TextWhite);
                TextViewPersian.setText(Misc.String(R.string.WelcomeUILanguagePersian));
                TextViewPersian.setPadding(Misc.ToDP(15), 0, Misc.ToDP(15), 0);
                TextViewPersian.setGravity(Misc.Gravity("R") | Gravity.CENTER_VERTICAL);
                TextViewPersian.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogLanguage.dismiss();
                        Misc.ChangeLanguage("fa");
                    }
                });

                LinearLayoutLanguage.addView(TextViewPersian);

                DialogLanguage.setContentView(LinearLayoutLanguage);
                DialogLanguage.show();
            }
        });

        RelativeLayoutLanguage.addView(TextViewLanguage);

        RelativeLayout.LayoutParams ImageViewLanguageParam = new RelativeLayout.LayoutParams(Misc.ToDP(18), Misc.ToDP(18));
        ImageViewLanguageParam.setMargins(Misc.ToDP(2), 0, Misc.ToDP(2), 0);
        ImageViewLanguageParam.addRule(Misc.AlignTo("R"), TextViewLanguage.getId());
        ImageViewLanguageParam.addRule(RelativeLayout.CENTER_VERTICAL);

        ImageView ImageViewLanguage = new ImageView(Activity);
        ImageViewLanguage.setLayoutParams(ImageViewLanguageParam);
        ImageViewLanguage.setImageResource(R.drawable.option_white);

        RelativeLayoutLanguage.addView(ImageViewLanguage);

        LinearLayout.LayoutParams ImageViewHeaderParam = new LinearLayout.LayoutParams(Misc.ToDP(150), Misc.ToDP(65));
        ImageViewHeaderParam.setMargins(0, Misc.ToDP(15), 0, Misc.ToDP(10));

        ImageView ImageViewHeader = new ImageView(Activity);
        ImageViewHeader.setLayoutParams(ImageViewHeaderParam);
        ImageViewHeader.setImageResource(Misc.IsFa() ? R.drawable.ic_logo_fa : R.drawable.ic_logo);

        LinearLayoutHeader.addView(ImageViewHeader);

        TextView TextViewHeader = new TextView(Activity, 14, false);
        TextViewHeader.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        TextViewHeader.setText(Misc.String(R.string.WelcomeUIHeader));

        LinearLayoutHeader.addView(TextViewHeader);

        TextView TextViewHeader2 = new TextView(Activity, 14, false);
        TextViewHeader2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        TextViewHeader2.setText(Misc.String(R.string.WelcomeUIHeader2));

        LinearLayoutHeader.addView(TextViewHeader2);

        RelativeLayout.LayoutParams RelativeLayoutSignUpParam = new RelativeLayout.LayoutParams(Misc.ToDP(270), Misc.ToDP(45));
        RelativeLayoutSignUpParam.setMargins(0, Misc.ToDP(30), 0, 0);
        RelativeLayoutSignUpParam.addRule(RelativeLayout.BELOW, LinearLayoutHeader.getId());
        RelativeLayoutSignUpParam.addRule(RelativeLayout.CENTER_HORIZONTAL);

        GradientDrawable DrawableSignUp = new GradientDrawable();
        DrawableSignUp.setColor(ContextCompat.getColor(Activity, R.color.Primary));
        DrawableSignUp.setCornerRadius(Misc.ToDP(7));

        Button ButtonSignUp = new Button(Activity, 16, true);
        ButtonSignUp.setLayoutParams(RelativeLayoutSignUpParam);
        ButtonSignUp.setText(Misc.String(R.string.GeneralSignUp));
        ButtonSignUp.setId(Misc.generateViewId());
        ButtonSignUp.setBackground(DrawableSignUp);
        ButtonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TranslateAnimation Anim = Misc.IsRTL() ? new TranslateAnimation(0f, -1000f, 0f, 0f) : new TranslateAnimation(0f, 1000f, 0f, 0f);
                Anim.setDuration(200);

                ScrollViewMain.setAnimation(Anim);

                Activity.GetManager().OpenView(new DescriptionUI("asdasd"), "PhoneUI", true);
            }
        });

        if (Misc.IsFa())
            ButtonSignUp.setPadding(0, -Misc.ToDP(1), 0, 0);

        RelativeLayoutMain.addView(ButtonSignUp);

        RelativeLayout.LayoutParams RelativeLayoutORParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(45));
        RelativeLayoutORParam.setMargins(0, Misc.ToDP(20), 0, Misc.ToDP(5));
        RelativeLayoutORParam.addRule(RelativeLayout.BELOW, ButtonSignUp.getId());

        RelativeLayout RelativeLayoutOR = new RelativeLayout(Activity);
        RelativeLayoutOR.setLayoutParams(RelativeLayoutORParam);
        RelativeLayoutOR.setId(Misc.generateViewId());

        RelativeLayoutMain.addView(RelativeLayoutOR);

        RelativeLayout.LayoutParams TextViewORParam = new RelativeLayout.LayoutParams(Misc.ToDP(40), RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewORParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        TextView TextViewOR = new TextView(Activity, 14, true);
        TextViewOR.setLayoutParams(TextViewORParam);
        TextViewOR.setText(Misc.String(R.string.WelcomeUIOR));
        TextViewOR.setId(Misc.generateViewId());
        TextViewOR.setGravity(Gravity.CENTER);
        TextViewOR.SetColor(R.color.Gray);

        RelativeLayoutOR.addView(TextViewOR);

        RelativeLayout.LayoutParams ViewOrLine1Param = new RelativeLayout.LayoutParams(Misc.ToDP(115), Misc.ToDP(1));
        ViewOrLine1Param.addRule(RelativeLayout.RIGHT_OF, TextViewOR.getId());
        ViewOrLine1Param.addRule(RelativeLayout.CENTER_VERTICAL);

        View ViewOrLine1 = new View(Activity);
        ViewOrLine1.setLayoutParams(ViewOrLine1Param);
        ViewOrLine1.setBackgroundResource(R.color.Gray);

        RelativeLayoutOR.addView(ViewOrLine1);

        RelativeLayout.LayoutParams ViewOrLine2Param = new RelativeLayout.LayoutParams(Misc.ToDP(115), Misc.ToDP(1));
        ViewOrLine2Param.addRule(RelativeLayout.LEFT_OF, TextViewOR.getId());
        ViewOrLine2Param.addRule(RelativeLayout.CENTER_VERTICAL);

        View ViewOrLine2 = new View(Activity);
        ViewOrLine2.setLayoutParams(ViewOrLine2Param);
        ViewOrLine2.setBackgroundResource(R.color.Gray);

        RelativeLayoutOR.addView(ViewOrLine2);

        RelativeLayout.LayoutParams LinearLayoutGoogleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LinearLayoutGoogleParam.addRule(RelativeLayout.BELOW, RelativeLayoutOR.getId());
        LinearLayoutGoogleParam.addRule(RelativeLayout.CENTER_HORIZONTAL);

        LinearLayout LinearLayoutGoogle = new LinearLayout(Activity);
        LinearLayoutGoogle.setLayoutParams(LinearLayoutGoogleParam);
        LinearLayoutGoogle.setGravity(Gravity.CENTER);
        LinearLayoutGoogle.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayoutGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT > 20)
                    Activity.getWindow().setStatusBarColor(ContextCompat.getColor(Activity, R.color.TextWhite));

                RelativeLayoutGoogle.setVisibility(View.VISIBLE);
                LoadingViewGoogle.Start();
                Activity.startActivityForResult(Auth.GoogleSignInApi.getSignInIntent(GoogleApiClient), 100);
            }
        });

        RelativeLayoutMain.addView(LinearLayoutGoogle);

        LinearLayout.LayoutParams ImageViewGoogleParam = new LinearLayout.LayoutParams(Misc.ToDP(30), Misc.ToDP(30));
        ImageViewGoogleParam.setMargins(Misc.ToDP(5), 0, Misc.ToDP(5), 0);

        ImageView ImageViewGoogle = new ImageView(Activity);
        ImageViewGoogle.setLayoutParams(ImageViewGoogleParam);
        ImageViewGoogle.setBackgroundResource(R.drawable.google);

        LinearLayoutGoogle.addView(ImageViewGoogle);

        TextView TextViewGoogle = new TextView(Activity, 16, true);
        TextViewGoogle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        TextViewGoogle.SetColor(R.color.Primary);
        TextViewGoogle.setText(Misc.String(R.string.WelcomeUISignInGoogle));
        TextViewGoogle.setPadding(0, Misc.ToDP(Misc.IsFa() ? 3 : 5), 0, 0);
        TextViewGoogle.setGravity(Gravity.CENTER_VERTICAL);

        LinearLayoutGoogle.addView(TextViewGoogle);

        RelativeLayout.LayoutParams RelativeLayoutSignInParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56));
        RelativeLayoutSignInParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        RelativeLayout RelativeLayoutSignIn = new RelativeLayout(Activity);
        RelativeLayoutSignIn.setLayoutParams(RelativeLayoutSignInParam);
        RelativeLayoutSignIn.setBackgroundResource(R.color.ActionBarWhite);
        RelativeLayoutSignIn.setGravity(Gravity.CENTER);
        RelativeLayoutSignIn.setId(Misc.generateViewId());
        RelativeLayoutSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TranslateAnimation Anim = Misc.IsRTL() ? new TranslateAnimation(0f, -1000f, 0f, 0f) : new TranslateAnimation(0f, 1000f, 0f, 0f);
                Anim.setDuration(200);

                ScrollViewMain.setAnimation(Anim);

                Activity.GetManager().OpenView(new PhoneUI(false), "PhoneUI", true);
            }
        });

        RelativeLayoutMain.addView(RelativeLayoutSignIn);

        RelativeLayout.LayoutParams TextViewSignInParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewSignInParam.addRule(Misc.Align("R"));

        TextView TextViewSignIn = new TextView(Activity, 14, false);
        TextViewSignIn.setLayoutParams(TextViewSignInParam);
        TextViewSignIn.SetColor(R.color.Gray);
        TextViewSignIn.setText(Misc.String(R.string.WelcomeUISignIn));
        TextViewSignIn.setId(Misc.generateViewId());

        RelativeLayoutSignIn.addView(TextViewSignIn);

        RelativeLayout.LayoutParams TextViewSignIn2Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewSignIn2Param.setMargins(Misc.ToDP(5), 0, Misc.ToDP(5), 0);
        TextViewSignIn2Param.addRule(Misc.AlignTo("R"), TextViewSignIn.getId());

        TextView TextViewSignIn2 = new TextView(Activity, 14, false);
        TextViewSignIn2.setLayoutParams(TextViewSignIn2Param);
        TextViewSignIn2.SetColor(R.color.Primary);
        TextViewSignIn2.setText(Misc.String(R.string.GeneralSignIn));

        RelativeLayoutSignIn.addView(TextViewSignIn2);

        RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1));
        ViewLineParam.addRule(RelativeLayout.ABOVE, RelativeLayoutSignIn.getId());

        View ViewLine = new View(Activity);
        ViewLine.setLayoutParams(ViewLineParam);
        ViewLine.setBackgroundResource(R.color.Gray);
        ViewLine.setId(Misc.generateViewId());

        RelativeLayoutMain.addView(ViewLine);

        RelativeLayout.LayoutParams TextViewTerm2Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTerm2Param.setMargins(0, 0, 0, Misc.ToDP(20));
        TextViewTerm2Param.addRule(RelativeLayout.ABOVE, ViewLine.getId());
        TextViewTerm2Param.addRule(RelativeLayout.CENTER_HORIZONTAL);

        TextView TextViewTerm2 = new TextView(Activity, 14, false);
        TextViewTerm2.setLayoutParams(TextViewTerm2Param);
        TextViewTerm2.SetColor(R.color.Gray);
        TextViewTerm2.setText(Misc.String(R.string.WelcomeUITerm2));
        TextViewTerm2.setId(Misc.generateViewId());
        TextViewTerm2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://biogram.co")));
            }
        });

        RelativeLayoutMain.addView(TextViewTerm2);

        RelativeLayout.LayoutParams TextViewTermParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTermParam.addRule(RelativeLayout.ABOVE, TextViewTerm2.getId());
        TextViewTermParam.addRule(RelativeLayout.CENTER_HORIZONTAL);

        TextView TextViewTerm = new TextView(Activity, 14, false);
        TextViewTerm.setLayoutParams(TextViewTermParam);
        TextViewTerm.SetColor(R.color.Gray);
        TextViewTerm.setText(Misc.String(R.string.WelcomeUITerm));

        RelativeLayoutMain.addView(TextViewTerm);

        RelativeLayoutGoogle = new RelativeLayout(Activity);
        RelativeLayoutGoogle.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayoutGoogle.setBackgroundColor(Color.parseColor("#90000000"));
        RelativeLayoutGoogle.setClickable(true);
        RelativeLayoutGoogle.setVisibility(View.GONE);

        RelativeLayoutMain.addView(RelativeLayoutGoogle);

        RelativeLayout.LayoutParams LoadingViewGoogleParam = new RelativeLayout.LayoutParams(Misc.ToDP(56), Misc.ToDP(56));
        LoadingViewGoogleParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        LoadingViewGoogle = new LoadingView(Activity);
        LoadingViewGoogle.setLayoutParams(LoadingViewGoogleParam);
        LoadingViewGoogle.SetColor(R.color.TextDark);

        RelativeLayoutGoogle.addView(LoadingViewGoogle);

        ViewMain = ScrollViewMain;

        IsGoogleAvailable = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(Activity) == ConnectionResult.SUCCESS;

        if (IsGoogleAvailable) {
            GoogleApiClient = new GoogleApiClient.Builder(Activity.getApplicationContext())
                    .addApi(Auth.GOOGLE_SIGN_IN_API, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestEmail()
                            .requestIdToken("590625045379-pnhlgdqpr5i8ma705ej7akcggsr08vdf.apps.googleusercontent.com")
                            .build())
                    .build();
        }
    }

    @Override
    public void OnResume() {
        if (IsGoogleAvailable)
            GoogleApiClient.connect();
    }

    @Override
    public void OnPause() {
        AndroidNetworking.forceCancel("WelcomeUI");

        if (IsGoogleAvailable) {
            if (GoogleApiClient.isConnected())
                Auth.GoogleSignInApi.signOut(GoogleApiClient);

            GoogleApiClient.disconnect();
        }
    }

    @Override
    public void OnActivityResult(int RequestCode, int ResultCode, Intent intent) {
        if (RequestCode == 100) {
            GoogleSignInResult Result = Auth.GoogleSignInApi.getSignInResultFromIntent(intent);

            if (Result.isSuccess()) {
                final GoogleSignInAccount Result2 = Result.getSignInAccount();

                if (Result2 != null) {
                    AndroidNetworking.post(Misc.GetRandomServer("SignInGoogle"))
                            .addBodyParameter("Token", Result2.getIdToken())
                            .addBodyParameter("Session", Misc.GenerateSession())
                            .setTag("WelcomeUI")
                            .build()
                            .getAsString(new StringRequestListener() {
                                @Override
                                public void onResponse(String Response) {
                                    HideGoogleLoading();

                                    try {
                                        JSONObject Result3 = new JSONObject(Response);

                                        switch (Result3.getInt("Message")) {
                                            case 0:
                                                if (Result3.getBoolean("Registered")) {
                                                    Misc.SetBoolean("IsLogin", true);
                                                    Misc.SetBoolean("IsGoogle", true);
                                                    Misc.SetString("Token", Result3.getString("Token"));
                                                    Misc.SetString("ID", Result3.getString("ID"));
                                                    Misc.SetString("Username", Result3.getString("Username"));
                                                    Misc.SetString("Avatar", Result3.getString("Avatar"));

                                                    Activity.startActivity(new Intent(Activity, SocialActivity.class));
                                                    Activity.finish();
                                                    return;
                                                }

                                                TranslateAnimation Anim = Misc.IsRTL() ? new TranslateAnimation(0f, -1000f, 0f, 0f) : new TranslateAnimation(0f, 1000f, 0f, 0f);
                                                Anim.setDuration(200);

                                                ScrollViewMain.setAnimation(Anim);

                                                Activity.GetManager().OpenView(new UsernameUI(Result2.getIdToken(), 0), "UsernameUI", true);
                                                break;
                                            case 1:
                                            case 2:
                                            case 3:
                                            case 4:
                                            case 5:
                                                Misc.ToastOld(Misc.String(R.string.WelcomeUIGoogleError1));
                                                break;
                                            case 6:
                                                Misc.ToastOld(Misc.String(R.string.WelcomeUIGoogleError2));
                                                break;
                                            default:
                                                Misc.GeneralError(Result3.getInt("Message"));
                                        }
                                    } catch (Exception e) {
                                        Misc.Debug("WelcomeUI: " + e.toString());
                                    }
                                }

                                @Override
                                public void onError(ANError e) {
                                    HideGoogleLoading();
                                    Misc.ToastOld(Misc.String(R.string.GeneralNoInternet));
                                }
                            });
                } else {
                    HideGoogleLoading();
                }
            } else {
                HideGoogleLoading();
            }
        }
    }

    private void HideGoogleLoading() {
        if (Build.VERSION.SDK_INT > 20)
            Activity.getWindow().setStatusBarColor(ContextCompat.getColor(Activity, R.color.Primary));

        LoadingViewGoogle.Stop();
        RelativeLayoutGoogle.setVisibility(View.GONE);
    }
}
