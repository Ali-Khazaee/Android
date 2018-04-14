package co.biogram.main.ui.welcome;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.json.JSONObject;

import co.biogram.main.fragment.FragmentView;
import co.biogram.main.R;
import co.biogram.main.activity.SocialActivity;
import co.biogram.main.handler.Misc;
import co.biogram.main.handler.SharedHandler;
import co.biogram.main.ui.view.Button;
import co.biogram.main.ui.view.LoadingView;
import co.biogram.main.ui.view.TextView;

class EmailSignInUI extends FragmentView
{
    private ViewTreeObserver.OnGlobalLayoutListener RelativeLayoutMainListener;
    private RelativeLayout RelativeLayoutMain;
    private boolean RequestUsername = false;
    private boolean RequestPassword = false;

    @Override
    public void OnCreate()
    {
        final Button ButtonSignIn = new Button(Activity, 16, false);
        final LoadingView LoadingViewSignIn = new LoadingView(Activity);

        RelativeLayoutMain = new RelativeLayout(Activity);
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayoutMain.setBackgroundResource(R.color.TextDark);
        RelativeLayoutMain.setClickable(true);

        RelativeLayoutMainListener = new ViewTreeObserver.OnGlobalLayoutListener()
        {
            int HeightDifference = 0;

            @Override
            public void onGlobalLayout()
            {
                Rect rect = new Rect();
                RelativeLayoutMain.getWindowVisibleDisplayFrame(rect);

                int ScreenHeight = RelativeLayoutMain.getHeight();
                int DifferenceHeight = ScreenHeight - (rect.bottom - rect.top);

                if (DifferenceHeight > ScreenHeight / 3 && DifferenceHeight != HeightDifference)
                {
                    RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ScreenHeight - DifferenceHeight));
                    HeightDifference = DifferenceHeight;
                }
                else if (DifferenceHeight != HeightDifference)
                {
                    RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ScreenHeight));
                    HeightDifference = DifferenceHeight;
                }
                else if (HeightDifference != 0)
                {
                    RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ScreenHeight + Math.abs(HeightDifference)));
                    HeightDifference = 0;
                }

                RelativeLayoutMain.requestLayout();
            }
        };

        RelativeLayout RelativeLayoutHeader = new RelativeLayout(Activity);
        RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56)));
        RelativeLayoutHeader.setBackgroundResource(R.color.Primary);
        RelativeLayoutHeader.setId(Misc.generateViewId());

        RelativeLayoutMain.addView(RelativeLayoutHeader);

        RelativeLayout.LayoutParams ImageViewBackParam = new RelativeLayout.LayoutParams(Misc.ToDP(56), Misc.ToDP(56));
        ImageViewBackParam.addRule(Misc.Align("R"));

        ImageView ImageViewBack = new ImageView(Activity);
        ImageViewBack.setLayoutParams(ImageViewBackParam);
        ImageViewBack.setScaleType(ImageView.ScaleType.FIT_XY);
        ImageViewBack.setId(Misc.generateViewId());
        ImageViewBack.setImageResource(Misc.IsRTL() ? R.drawable.back_white_rtl : R.drawable.back_white);
        ImageViewBack.setPadding(Misc.ToDP(12), Misc.ToDP(12), Misc.ToDP(12), Misc.ToDP(12));
        ImageViewBack.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { Activity.onBackPressed(); } });

        RelativeLayoutHeader.addView(ImageViewBack);

        RelativeLayout.LayoutParams TextViewTitleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTitleParam.addRule(Misc.AlignTo("R"), ImageViewBack.getId());
        TextViewTitleParam.addRule(RelativeLayout.CENTER_VERTICAL);

        TextView TextViewTitle = new TextView(Activity, 16, true);
        TextViewTitle.setLayoutParams(TextViewTitleParam);
        TextViewTitle.setPadding(0, Misc.ToDP(6), 0, 0);
        TextViewTitle.setText(Misc.String(R.string.GeneralEmail));

        RelativeLayoutHeader.addView(TextViewTitle);

        RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1));
        ViewLineParam.addRule(RelativeLayout.BELOW, RelativeLayoutHeader.getId());

        View ViewLine = new View(Activity);
        ViewLine.setLayoutParams(ViewLineParam);
        ViewLine.setBackgroundResource(R.color.Gray);
        ViewLine.setId(Misc.generateViewId());

        RelativeLayoutMain.addView(ViewLine);

        RelativeLayout.LayoutParams ScrollViewMainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        ScrollViewMainParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

        ScrollView ScrollViewMain = new ScrollView(Activity);
        ScrollViewMain.setLayoutParams(ScrollViewMainParam);
        ScrollViewMain.setFillViewport(true);

        RelativeLayoutMain.addView(ScrollViewMain);

        RelativeLayout RelativeLayoutScroll = new RelativeLayout(Activity);
        RelativeLayoutScroll.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

        ScrollViewMain.addView(RelativeLayoutScroll);

        RelativeLayout.LayoutParams TextViewEmailOrUsernameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewEmailOrUsernameParam.setMargins(Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15));

        TextView TextViewEmailOrUsername = new TextView(Activity, 14, false);
        TextViewEmailOrUsername.setLayoutParams(TextViewEmailOrUsernameParam);
        TextViewEmailOrUsername.SetColor(R.color.Gray);
        TextViewEmailOrUsername.setText(Misc.String(R.string.EmailSignInUIOrUsername));
        TextViewEmailOrUsername.setId(Misc.generateViewId());

        RelativeLayoutScroll.addView(TextViewEmailOrUsername);

        RelativeLayout.LayoutParams EditTextEmailOrUsernameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        EditTextEmailOrUsernameParam.setMargins(Misc.ToDP(10), 0, Misc.ToDP(10), 0);
        EditTextEmailOrUsernameParam.addRule(RelativeLayout.BELOW, TextViewEmailOrUsername.getId());

        final EditText EditTextEmailOrUsername = new EditText(Activity);
        EditTextEmailOrUsername.setLayoutParams(EditTextEmailOrUsernameParam);
        EditTextEmailOrUsername.setId(Misc.generateViewId());
        EditTextEmailOrUsername.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        EditTextEmailOrUsername.setFilters(new InputFilter[] { new InputFilter.LengthFilter(64) });
        EditTextEmailOrUsername.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        EditTextEmailOrUsername.getBackground().setColorFilter(ContextCompat.getColor(Activity, R.color.Primary), PorterDuff.Mode.SRC_ATOP);
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
                RequestUsername = s.length() > 2;
                ButtonSignIn.setEnabled(RequestUsername && RequestPassword);
            }
        });

        RelativeLayoutScroll.addView(EditTextEmailOrUsername);

        RelativeLayout.LayoutParams TextViewPasswordParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewPasswordParam.setMargins(Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15));
        TextViewPasswordParam.addRule(RelativeLayout.BELOW, EditTextEmailOrUsername.getId());

        TextView TextViewPassword = new TextView(Activity, 14, false);
        TextViewPassword.setLayoutParams(TextViewPasswordParam);
        TextViewPassword.SetColor(R.color.Gray);
        TextViewPassword.setText(Misc.String(R.string.GeneralPassword));
        TextViewPassword.setId(Misc.generateViewId());
        TextViewPassword.setGravity(Misc.Gravity("R"));

        RelativeLayoutScroll.addView(TextViewPassword);

        RelativeLayout.LayoutParams EditTextPasswordParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        EditTextPasswordParam.addRule(RelativeLayout.BELOW, TextViewPassword.getId());
        EditTextPasswordParam.setMargins(Misc.ToDP(10), 0, Misc.ToDP(10), 0);

        final EditText EditTextPassword = new EditText(Activity);
        EditTextPassword.setLayoutParams(EditTextPasswordParam);
        EditTextPassword.setId(Misc.generateViewId());
        EditTextPassword.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        EditTextPassword.setFilters(new InputFilter[] { new InputFilter.LengthFilter(32) });
        EditTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        EditTextPassword.getBackground().setColorFilter(ContextCompat.getColor(Activity, R.color.Primary), PorterDuff.Mode.SRC_ATOP);
        EditTextPassword.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void afterTextChanged(Editable s) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                RequestPassword = s.length() > 5;
                ButtonSignIn.setEnabled(RequestUsername && RequestPassword);
            }
        });

        RelativeLayoutScroll.addView(EditTextPassword);

        RelativeLayout.LayoutParams TextViewMessageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewMessageParam.addRule(RelativeLayout.BELOW, EditTextPassword.getId());

        TextView TextViewMessage = new TextView(Activity, 14, false);
        TextViewMessage.setLayoutParams(TextViewMessageParam);
        TextViewMessage.SetColor(R.color.TextWhite);
        TextViewMessage.setText(Misc.String(R.string.EmailSignInUIMessage));
        TextViewMessage.setId(Misc.generateViewId());
        TextViewMessage.setPadding(Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15));

        RelativeLayoutScroll.addView(TextViewMessage);

        RelativeLayout.LayoutParams RelativeLayoutBottomParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayoutBottomParam.addRule(RelativeLayout.BELOW, TextViewMessage.getId());

        RelativeLayout RelativeLayoutBottom = new RelativeLayout(Activity);
        RelativeLayoutBottom.setLayoutParams(RelativeLayoutBottomParam);

        RelativeLayoutScroll.addView(RelativeLayoutBottom);

        RelativeLayout.LayoutParams TextViewResetParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewResetParam.addRule(RelativeLayout.CENTER_VERTICAL);
        TextViewResetParam.addRule(Misc.Align("R"));

        TextView TextViewReset = new TextView(Activity, 14, false);
        TextViewReset.setLayoutParams(TextViewResetParam);
        TextViewReset.SetColor(R.color.Primary);
        TextViewReset.setText(Misc.String(R.string.EmailSignInUIForgot));
        TextViewReset.setPadding(Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15));
        TextViewReset.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                TranslateAnimation Anim = Misc.IsRTL() ? new TranslateAnimation(0f, -1000f, 0f, 0f) : new TranslateAnimation(0f, 1000f, 0f, 0f);
                Anim.setDuration(200);

                RelativeLayoutMain.setAnimation(Anim);

                Activity.GetManager().OpenView(new PasswordResetUI(), R.id.ContainerFull, "PasswordResetUI");
            }
        });

        RelativeLayoutBottom.addView(TextViewReset);

        GradientDrawable DrawableEnable = new GradientDrawable();
        DrawableEnable.setColor(ContextCompat.getColor(Activity, R.color.Primary));
        DrawableEnable.setCornerRadius(Misc.ToDP(7));

        GradientDrawable DrawableDisable = new GradientDrawable();
        DrawableDisable.setCornerRadius(Misc.ToDP(7));
        DrawableDisable.setColor(ContextCompat.getColor(Activity, R.color.Gray));

        StateListDrawable ListDrawableSignIn = new StateListDrawable();
        ListDrawableSignIn.addState(new int[] { android.R.attr.state_enabled }, DrawableEnable);
        ListDrawableSignIn.addState(new int[] { -android.R.attr.state_enabled }, DrawableDisable);

        RelativeLayout.LayoutParams RelativeLayoutSignInParam = new RelativeLayout.LayoutParams(Misc.ToDP(90), Misc.ToDP(35));
        RelativeLayoutSignInParam.setMargins(Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15));
        RelativeLayoutSignInParam.addRule(Misc.Align("L"));

        RelativeLayout RelativeLayoutSignIn = new RelativeLayout(Activity);
        RelativeLayoutSignIn.setLayoutParams(RelativeLayoutSignInParam);
        RelativeLayoutSignIn.setBackground(DrawableEnable);

        RelativeLayoutBottom.addView(RelativeLayoutSignIn);

        ButtonSignIn.setLayoutParams(new RelativeLayout.LayoutParams(Misc.ToDP(90), Misc.ToDP(35)));
        ButtonSignIn.setText(Misc.String(R.string.GeneralSignIn));
        ButtonSignIn.setBackground(ListDrawableSignIn);
        ButtonSignIn.setEnabled(false);
        ButtonSignIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ButtonSignIn.setVisibility(View.GONE);
                LoadingViewSignIn.Start();

                AndroidNetworking.post(Misc.GetRandomServer("SignInEmail"))
                .addBodyParameter("EmailOrUsername", EditTextEmailOrUsername.getText().toString())
                .addBodyParameter("Password", EditTextPassword.getText().toString())
                .addBodyParameter("Session", Misc.GenerateSession())
                .setTag("EmailSignInUI")
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

                            switch (Result.getInt("Message"))
                            {
                                case 0:
                                    SharedHandler.SetBoolean("IsLogin", true);
                                    SharedHandler.SetBoolean("IsGoogle", false);
                                    SharedHandler.SetString("Token", Result.getString("Token"));
                                    SharedHandler.SetString("ID", Result.getString("ID"));
                                    SharedHandler.SetString("Username", Result.getString("Username"));
                                    SharedHandler.SetString("Avatar", Result.getString("Avatar"));

                                    Activity.startActivity(new Intent(Activity, SocialActivity.class));
                                    Activity.finish();
                                break;
                                case 1:
                                    Misc.Toast( Misc.String(R.string.EmailSignInUIError1));
                                    break;
                                case 2:
                                    Misc.Toast( Misc.String(R.string.EmailSignInUIError2));
                                    break;
                                case 3:
                                    Misc.Toast( Misc.String(R.string.EmailSignInUIError3));
                                    break;
                                case 4:
                                    Misc.Toast( Misc.String(R.string.EmailSignInUIError4));
                                    break;
                                case 5:
                                    Misc.Toast( Misc.String(R.string.EmailSignInUIError5));
                                    break;
                                case 6:
                                    Misc.Toast( Misc.String(R.string.EmailSignInUIError6));
                                    break;
                                case 7:
                                    Misc.Toast( Misc.String(R.string.EmailSignInUIError7));
                                    break;
                                case 8:
                                    Misc.Toast( Misc.String(R.string.EmailSignInUIError8));
                                    break;
                                default:
                                    Misc.GeneralError(Result.getInt("Message"));
                                    break;
                            }
                        }
                        catch (Exception e)
                        {
                            Misc.Debug("EmailSignInUI: " + e.toString());
                        }
                    }

                    @Override
                    public void onError(ANError e)
                    {
                        LoadingViewSignIn.Stop();
                        ButtonSignIn.setVisibility(View.VISIBLE);
                        Misc.Toast( Misc.String(R.string.GeneralNoInternet));
                    }
                });
            }
        });

        RelativeLayoutSignIn.addView(ButtonSignIn);

        RelativeLayout.LayoutParams LoadingViewUsernameParam = new RelativeLayout.LayoutParams(Misc.ToDP(90), Misc.ToDP(35));
        LoadingViewUsernameParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        LoadingViewSignIn.setLayoutParams(LoadingViewUsernameParam);
        LoadingViewSignIn.SetColor(R.color.TextDark);

        RelativeLayoutSignIn.addView(LoadingViewSignIn);

        TranslateAnimation Anim = Misc.IsRTL() ? new TranslateAnimation(1000f, 0f, 0f, 0f) : new TranslateAnimation(-1000f, 0f, 0f, 0f);
        Anim.setDuration(200);
        Anim.setAnimationListener(new Animation.AnimationListener()
        {
            @Override public void onAnimationStart(Animation animation) { }
            @Override public void onAnimationRepeat(Animation animation) { }
            @Override public void onAnimationEnd(Animation animation) { Misc.ShowSoftKey(EditTextEmailOrUsername); }
        });

        RelativeLayoutMain.startAnimation(Anim);

        ViewMain = RelativeLayoutMain;
    }

    @Override
    public void OnResume()
    {
        RelativeLayoutMain.getViewTreeObserver().addOnGlobalLayoutListener(RelativeLayoutMainListener);
    }

    @Override
    public void OnPause()
    {
        Misc.HideSoftKey(Activity);
        AndroidNetworking.forceCancel("EmailSignInUI");
        RelativeLayoutMain.getViewTreeObserver().removeOnGlobalLayoutListener(RelativeLayoutMainListener);
    }
}
