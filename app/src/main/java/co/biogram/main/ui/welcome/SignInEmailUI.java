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

import co.biogram.main.fragment.FragmentBase;
import co.biogram.main.R;
import co.biogram.main.activity.SocialActivity;
import co.biogram.main.handler.MiscHandler;
import co.biogram.main.handler.SharedHandler;
import co.biogram.main.ui.view.Button;
import co.biogram.main.ui.view.LoadingView;
import co.biogram.main.ui.view.TextView;

class SignInEmailUI extends FragmentBase
{
    private ViewTreeObserver.OnGlobalLayoutListener RelativeLayoutMainListener;
    private RelativeLayout RelativeLayoutMain;
    private boolean RequestUsername = false;
    private boolean RequestPassword = false;
    private int HeightDifference = 0;

    @Override
    public void OnCreate()
    {
        final Button ButtonSignIn = new Button(GetActivity(), 16, false);
        final LoadingView LoadingViewSignIn = new LoadingView(GetActivity());

        RelativeLayoutMain = new RelativeLayout(GetActivity());
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

        RelativeLayout RelativeLayoutHeader = new RelativeLayout(GetActivity());
        RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(GetActivity(), 56)));
        RelativeLayoutHeader.setBackgroundResource(R.color.BlueLight);
        RelativeLayoutHeader.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain.addView(RelativeLayoutHeader);

        RelativeLayout.LayoutParams ImageViewBackParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(GetActivity(), 56), MiscHandler.ToDimension(GetActivity(), 56));
        ImageViewBackParam.addRule(MiscHandler.Align("R"));

        ImageView ImageViewBack = new ImageView(GetActivity());
        ImageViewBack.setLayoutParams(ImageViewBackParam);
        ImageViewBack.setScaleType(ImageView.ScaleType.FIT_XY);
        ImageViewBack.setId(MiscHandler.GenerateViewID());
        ImageViewBack.setImageResource(MiscHandler.IsRTL() ? R.drawable.ic_back_white_rtl : R.drawable.ic_back_white);
        ImageViewBack.setPadding(MiscHandler.ToDimension(GetActivity(), 12), MiscHandler.ToDimension(GetActivity(), 12), MiscHandler.ToDimension(GetActivity(), 12), MiscHandler.ToDimension(GetActivity(), 12));
        ImageViewBack.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { GetActivity().onBackPressed(); } });

        RelativeLayoutHeader.addView(ImageViewBack);

        RelativeLayout.LayoutParams TextViewTitleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTitleParam.addRule(MiscHandler.AlignTo("R"), ImageViewBack.getId());
        TextViewTitleParam.addRule(RelativeLayout.CENTER_VERTICAL);

        TextView TextViewTitle = new TextView(GetActivity(), 16, true);
        TextViewTitle.setLayoutParams(TextViewTitleParam);
        TextViewTitle.setText(GetActivity().getString(R.string.SignInEmail));

        RelativeLayoutHeader.addView(TextViewTitle);

        RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(GetActivity(), 1));
        ViewLineParam.addRule(RelativeLayout.BELOW, RelativeLayoutHeader.getId());

        View ViewLine = new View(GetActivity());
        ViewLine.setLayoutParams(ViewLineParam);
        ViewLine.setBackgroundResource(R.color.Gray2);
        ViewLine.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain.addView(ViewLine);

        RelativeLayout.LayoutParams ScrollViewMainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        ScrollViewMainParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

        ScrollView ScrollViewMain = new ScrollView(GetActivity());
        ScrollViewMain.setLayoutParams(ScrollViewMainParam);
        ScrollViewMain.setFillViewport(true);

        RelativeLayoutMain.addView(ScrollViewMain);

        RelativeLayout RelativeLayoutScroll = new RelativeLayout(GetActivity());
        RelativeLayoutScroll.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

        ScrollViewMain.addView(RelativeLayoutScroll);

        RelativeLayout.LayoutParams TextViewEmailOrUsernameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewEmailOrUsernameParam.setMargins(MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15));

        TextView TextViewEmailOrUsername = new TextView(GetActivity(), 14, true);
        TextViewEmailOrUsername.setLayoutParams(TextViewEmailOrUsernameParam);
        TextViewEmailOrUsername.setTextColor(ContextCompat.getColor(GetActivity(), R.color.Gray4));
        TextViewEmailOrUsername.setText(GetActivity().getString(R.string.SignInEmailOrUsername));
        TextViewEmailOrUsername.setId(MiscHandler.GenerateViewID());
        TextViewEmailOrUsername.setGravity(MiscHandler.Gravity("R"));

        RelativeLayoutScroll.addView(TextViewEmailOrUsername);

        RelativeLayout.LayoutParams EditTextEmailOrUsernameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        EditTextEmailOrUsernameParam.setMargins(MiscHandler.ToDimension(GetActivity(), 10), 0, MiscHandler.ToDimension(GetActivity(), 10), 0);
        EditTextEmailOrUsernameParam.addRule(RelativeLayout.BELOW, TextViewEmailOrUsername.getId());

        final EditText EditTextEmailOrUsername = new EditText(GetActivity());
        EditTextEmailOrUsername.setLayoutParams(EditTextEmailOrUsernameParam);
        EditTextEmailOrUsername.setId(MiscHandler.GenerateViewID());
        EditTextEmailOrUsername.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        EditTextEmailOrUsername.setFilters(new InputFilter[] { new InputFilter.LengthFilter(64) });
        EditTextEmailOrUsername.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        EditTextEmailOrUsername.getBackground().setColorFilter(ContextCompat.getColor(GetActivity(), R.color.BlueLight), PorterDuff.Mode.SRC_ATOP);
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

        RelativeLayout.LayoutParams TextViewPasswordParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewPasswordParam.setMargins(MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15));
        TextViewPasswordParam.addRule(RelativeLayout.BELOW, EditTextEmailOrUsername.getId());

        TextView TextViewPassword = new TextView(GetActivity(), 14, true);
        TextViewPassword.setLayoutParams(TextViewPasswordParam);
        TextViewPassword.setTextColor(ContextCompat.getColor(GetActivity(), R.color.Gray4));
        TextViewPassword.setText(GetActivity().getString(R.string.SignInEmailPassword));
        TextViewPassword.setId(MiscHandler.GenerateViewID());
        TextViewPassword.setGravity(MiscHandler.Gravity("R"));

        RelativeLayoutScroll.addView(TextViewPassword);

        RelativeLayout.LayoutParams EditTextPasswordParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        EditTextPasswordParam.addRule(RelativeLayout.BELOW, TextViewPassword.getId());
        EditTextPasswordParam.setMargins(MiscHandler.ToDimension(GetActivity(), 10), 0, MiscHandler.ToDimension(GetActivity(), 10), 0);

        final EditText EditTextPassword = new EditText(GetActivity());
        EditTextPassword.setLayoutParams(EditTextPasswordParam);
        EditTextPassword.setId(MiscHandler.GenerateViewID());
        EditTextPassword.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        EditTextPassword.setFilters(new InputFilter[] { new InputFilter.LengthFilter(32) });
        EditTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        EditTextPassword.getBackground().setColorFilter(ContextCompat.getColor(GetActivity(), R.color.BlueLight), PorterDuff.Mode.SRC_ATOP);
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

        TextView TextViewMessage = new TextView(GetActivity(), 14, false);
        TextViewMessage.setLayoutParams(TextViewMessageParam);
        TextViewMessage.setTextColor(ContextCompat.getColor(GetActivity(), R.color.Black));
        TextViewMessage.setText(GetActivity().getString(R.string.SignInEmailMessage));
        TextViewMessage.setId(MiscHandler.GenerateViewID());
        TextViewMessage.setPadding(MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15));

        RelativeLayoutScroll.addView(TextViewMessage);

        RelativeLayout.LayoutParams RelativeLayoutBottomParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayoutBottomParam.addRule(RelativeLayout.BELOW, TextViewMessage.getId());

        RelativeLayout RelativeLayoutBottom = new RelativeLayout(GetActivity());
        RelativeLayoutBottom.setLayoutParams(RelativeLayoutBottomParam);

        RelativeLayoutScroll.addView(RelativeLayoutBottom);

        RelativeLayout.LayoutParams TextViewResetParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewResetParam.addRule(RelativeLayout.CENTER_VERTICAL);
        TextViewResetParam.addRule(MiscHandler.Align("R"));

        TextView TextViewReset = new TextView(GetActivity(), 14, false);
        TextViewReset.setLayoutParams(TextViewResetParam);
        TextViewReset.setTextColor(ContextCompat.getColor(GetActivity(), R.color.BlueLight));
        TextViewReset.setText(GetActivity().getString(R.string.SignInEmailForgot));
        TextViewReset.setPadding(MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15));
        TextViewReset.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                TranslateAnimation Anim = MiscHandler.IsRTL() ? new TranslateAnimation(0f, -1000f, 0f, 0f) : new TranslateAnimation(0f, 1000f, 0f, 0f);
                Anim.setDuration(200);

                RelativeLayoutMain.setAnimation(Anim);

                GetActivity().GetManager().OpenView(new ResetPasswordUI(), R.id.WelcomeActivityContainer, "ResetPasswordUI");
            }
        });

        RelativeLayoutBottom.addView(TextViewReset);

        GradientDrawable DrawableEnable = new GradientDrawable();
        DrawableEnable.setColor(ContextCompat.getColor(GetActivity(), R.color.BlueLight));
        DrawableEnable.setCornerRadius(MiscHandler.ToDimension(GetActivity(), 7));

        GradientDrawable DrawableDisable = new GradientDrawable();
        DrawableDisable.setCornerRadius(MiscHandler.ToDimension(GetActivity(), 7));
        DrawableDisable.setColor(ContextCompat.getColor(GetActivity(), R.color.Gray2));

        StateListDrawable DrawableSignIn = new StateListDrawable();
        DrawableSignIn.addState(new int[] { android.R.attr.state_enabled }, DrawableEnable);
        DrawableSignIn.addState(new int[] { -android.R.attr.state_enabled }, DrawableDisable);

        RelativeLayout.LayoutParams RelativeLayoutSignInParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(GetActivity(), 90), MiscHandler.ToDimension(GetActivity(), 35));
        RelativeLayoutSignInParam.setMargins(MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15));
        RelativeLayoutSignInParam.addRule(MiscHandler.Align("L"));

        RelativeLayout RelativeLayoutSignIn = new RelativeLayout(GetActivity());
        RelativeLayoutSignIn.setLayoutParams(RelativeLayoutSignInParam);
        RelativeLayoutSignIn.setBackground(DrawableEnable);

        RelativeLayoutBottom.addView(RelativeLayoutSignIn);

        ButtonSignIn.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(GetActivity(), 90), MiscHandler.ToDimension(GetActivity(), 35)));
        ButtonSignIn.setText(GetActivity().getString(R.string.SignInEmail));
        ButtonSignIn.setBackground(DrawableSignIn);
        ButtonSignIn.setPadding(0, 0, 0, 0);
        ButtonSignIn.setEnabled(false);
        ButtonSignIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ButtonSignIn.setVisibility(View.GONE);
                LoadingViewSignIn.Start();

                AndroidNetworking.post(MiscHandler.GetRandomServer("SignInEmail"))
                .addBodyParameter("EmailOrUsername", EditTextEmailOrUsername.getText().toString())
                .addBodyParameter("Password", EditTextPassword.getText().toString())
                .addBodyParameter("Session", MiscHandler.GenerateSession())
                .setTag("SignInEmailUI")
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
                                    SharedHandler.SetBoolean(GetActivity(), "IsLogin", true);
                                    SharedHandler.SetBoolean(GetActivity(), "IsGoogle", false);
                                    SharedHandler.SetString(GetActivity(), "Token", Result.getString("Token"));
                                    SharedHandler.SetString(GetActivity(), "ID", Result.getString("ID"));
                                    SharedHandler.SetString(GetActivity(), "Username", Result.getString("Username"));
                                    SharedHandler.SetString(GetActivity(), "Avatar", Result.getString("Avatar"));

                                    GetActivity().startActivity(new Intent(GetActivity(), SocialActivity.class));
                                    GetActivity().finish();
                                break;
                                case 1:
                                    MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.SignInEmailError1));
                                    break;
                                case 2:
                                    MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.SignInEmailError2));
                                    break;
                                case 3:
                                    MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.SignInEmailError3));
                                    break;
                                case 4:
                                    MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.SignInEmailError4));
                                    break;
                                case 5:
                                    MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.SignInEmailError5));
                                    break;
                                case 6:
                                    MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.SignInEmailError6));
                                    break;
                                case 7:
                                    MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.SignInEmailError7));
                                    break;
                                case 8:
                                    MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.SignInEmailError8));
                                    break;
                                default:
                                    MiscHandler.GeneralError(GetActivity(), Result.getInt("Message"));
                                    break;
                            }
                        }
                        catch (Exception e)
                        {
                            MiscHandler.Debug("SignInEmailUI-SignInEmail: " + e.toString());
                        }
                    }

                    @Override
                    public void onError(ANError e)
                    {
                        LoadingViewSignIn.Stop();
                        ButtonSignIn.setVisibility(View.VISIBLE);
                        MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.GeneralNoInternet));
                    }
                });
            }
        });

        RelativeLayoutSignIn.addView(ButtonSignIn);

        RelativeLayout.LayoutParams LoadingViewUsernameParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(GetActivity(), 90), MiscHandler.ToDimension(GetActivity(), 35));
        LoadingViewUsernameParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        LoadingViewSignIn.setLayoutParams(LoadingViewUsernameParam);
        LoadingViewSignIn.SetColor(R.color.White);

        RelativeLayoutSignIn.addView(LoadingViewSignIn);

        TranslateAnimation Anim = MiscHandler.IsRTL() ? new TranslateAnimation(1000f, 0f, 0f, 0f) : new TranslateAnimation(-1000f, 0f, 0f, 0f);
        Anim.setDuration(200);
        Anim.setAnimationListener(new Animation.AnimationListener()
        {
            @Override public void onAnimationStart(Animation animation) { }
            @Override public void onAnimationRepeat(Animation animation) { }
            @Override public void onAnimationEnd(Animation animation) { MiscHandler.ShowSoftKey(EditTextEmailOrUsername); }
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
        MiscHandler.HideSoftKey(GetActivity());
        AndroidNetworking.forceCancel("SignInEmailUI");
        RelativeLayoutMain.getViewTreeObserver().removeOnGlobalLayoutListener(RelativeLayoutMainListener);
    }
}
