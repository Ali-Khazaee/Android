package co.biogram.main.ui.welcome;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Patterns;
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
import co.biogram.main.handler.Misc;
import co.biogram.main.ui.view.Button;
import co.biogram.main.ui.view.LoadingView;
import co.biogram.main.ui.view.TextView;

class EmailUI extends FragmentBase
{
    private ViewTreeObserver.OnGlobalLayoutListener LayoutListener;
    private RelativeLayout RelativeLayoutMain;
    private final String Username;
    private final String Password;

    EmailUI(String username, String password)
    {
        Username = username;
        Password = password;
    }

    @Override
    public void OnCreate()
    {
        final Button ButtonNext = new Button(GetActivity(), 16, false);
        final LoadingView LoadingViewNext = new LoadingView(GetActivity());

        RelativeLayoutMain = new RelativeLayout(GetActivity());
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayoutMain.setBackgroundResource(R.color.TextDark);
        RelativeLayoutMain.setClickable(true);

        LayoutListener = new ViewTreeObserver.OnGlobalLayoutListener()
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

        RelativeLayout RelativeLayoutHeader = new RelativeLayout(GetActivity());
        RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(GetActivity(), 56)));
        RelativeLayoutHeader.setBackgroundResource(R.color.BlueLight);
        RelativeLayoutHeader.setId(Misc.GenerateViewID());

        RelativeLayoutMain.addView(RelativeLayoutHeader);

        RelativeLayout.LayoutParams ImageViewBackParam = new RelativeLayout.LayoutParams(Misc.ToDP(GetActivity(), 56), Misc.ToDP(GetActivity(), 56));
        ImageViewBackParam.addRule(Misc.Align("R"));

        ImageView ImageViewBack = new ImageView(GetActivity());
        ImageViewBack.setLayoutParams(ImageViewBackParam);
        ImageViewBack.setScaleType(ImageView.ScaleType.FIT_XY);
        ImageViewBack.setId(Misc.GenerateViewID());
        ImageViewBack.setImageResource(Misc.IsRTL() ? R.drawable.back_white_rtl : R.drawable.back_white);
        ImageViewBack.setPadding(Misc.ToDP(GetActivity(), 12), Misc.ToDP(GetActivity(), 12), Misc.ToDP(GetActivity(), 12), Misc.ToDP(GetActivity(), 12));
        ImageViewBack.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { GetActivity().onBackPressed(); } });

        RelativeLayoutHeader.addView(ImageViewBack);

        RelativeLayout.LayoutParams TextViewTitleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTitleParam.addRule(Misc.AlignTo("R"), ImageViewBack.getId());
        TextViewTitleParam.addRule(RelativeLayout.CENTER_VERTICAL);

        TextView TextViewTitle = new TextView(GetActivity(), 16, true);
        TextViewTitle.setLayoutParams(TextViewTitleParam);
        TextViewTitle.setPadding(0, Misc.ToDP(GetActivity(), 6), 0, 0);
        TextViewTitle.setText(GetActivity().getString(R.string.GeneralEmail));

        RelativeLayoutHeader.addView(TextViewTitle);

        RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(GetActivity(), 1));
        ViewLineParam.addRule(RelativeLayout.BELOW, RelativeLayoutHeader.getId());

        View ViewLine = new View(GetActivity());
        ViewLine.setLayoutParams(ViewLineParam);
        ViewLine.setBackgroundResource(R.color.Gray2);
        ViewLine.setId(Misc.GenerateViewID());

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

        RelativeLayout.LayoutParams TextViewEmailParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewEmailParam.setMargins(Misc.ToDP(GetActivity(), 15), Misc.ToDP(GetActivity(), 15), Misc.ToDP(GetActivity(), 15), Misc.ToDP(GetActivity(), 15));

        TextView TextViewEmail = new TextView(GetActivity(), 14, false);
        TextViewEmail.setLayoutParams(TextViewEmailParam);
        TextViewEmail.setTextColor(ContextCompat.getColor(GetActivity(), R.color.Gray4));
        TextViewEmail.setText(GetActivity().getString(R.string.GeneralEmailAddress));
        TextViewEmail.setId(Misc.GenerateViewID());

        RelativeLayoutScroll.addView(TextViewEmail);

        RelativeLayout.LayoutParams EditTextEmailParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        EditTextEmailParam.addRule(RelativeLayout.BELOW, TextViewEmail.getId());
        EditTextEmailParam.setMargins(Misc.ToDP(GetActivity(), 10), 0, Misc.ToDP(GetActivity(), 10), 0);

        final EditText EditTextEmail = new EditText(GetActivity());
        EditTextEmail.setLayoutParams(EditTextEmailParam);
        EditTextEmail.setId(Misc.GenerateViewID());
        EditTextEmail.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        EditTextEmail.setFilters(new InputFilter[] { new InputFilter.LengthFilter(64) });
        EditTextEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        EditTextEmail.getBackground().setColorFilter(ContextCompat.getColor(GetActivity(), R.color.BlueLight), PorterDuff.Mode.SRC_ATOP);
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
                ButtonNext.setEnabled(s.length() > 6 && Patterns.EMAIL_ADDRESS.matcher(s).matches());
            }
        });

        RelativeLayoutScroll.addView(EditTextEmail);

        RelativeLayout.LayoutParams TextViewMessageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewMessageParam.addRule(RelativeLayout.BELOW, EditTextEmail.getId());

        TextView TextViewMessage = new TextView(GetActivity(), 14, false);
        TextViewMessage.setLayoutParams(TextViewMessageParam);
        TextViewMessage.setTextColor(ContextCompat.getColor(GetActivity(), R.color.TextWhite));
        TextViewMessage.setText(GetActivity().getString(R.string.EmailUIMessage));
        TextViewMessage.setId(Misc.GenerateViewID());
        TextViewMessage.setPadding(Misc.ToDP(GetActivity(), 15), Misc.ToDP(GetActivity(), 15), Misc.ToDP(GetActivity(), 15), Misc.ToDP(GetActivity(), 15));

        RelativeLayoutScroll.addView(TextViewMessage);

        RelativeLayout.LayoutParams RelativeLayoutBottomParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayoutBottomParam.addRule(RelativeLayout.BELOW, TextViewMessage.getId());

        RelativeLayout RelativeLayoutBottom = new RelativeLayout(GetActivity());
        RelativeLayoutBottom.setLayoutParams(RelativeLayoutBottomParam);

        RelativeLayoutScroll.addView(RelativeLayoutBottom);

        RelativeLayout.LayoutParams TextViewPrivacyParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewPrivacyParam.addRule(RelativeLayout.CENTER_VERTICAL);
        TextViewPrivacyParam.addRule(Misc.Align("R"));

        TextView TextViewPrivacy = new TextView(GetActivity(), 14, false);
        TextViewPrivacy.setLayoutParams(TextViewPrivacyParam);
        TextViewPrivacy.setTextColor(ContextCompat.getColor(GetActivity(), R.color.BlueLight));
        TextViewPrivacy.setText(GetActivity().getString(R.string.GeneralTerm));
        TextViewPrivacy.setPadding(Misc.ToDP(GetActivity(), 15), Misc.ToDP(GetActivity(), 15), Misc.ToDP(GetActivity(), 15), Misc.ToDP(GetActivity(), 15));
        TextViewPrivacy.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { GetActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://biogram.co"))); } });

        RelativeLayoutBottom.addView(TextViewPrivacy);

        RelativeLayout.LayoutParams RelativeLayoutNextParam = new RelativeLayout.LayoutParams(Misc.ToDP(GetActivity(), 90), Misc.ToDP(GetActivity(), 35));
        RelativeLayoutNextParam.setMargins(Misc.ToDP(GetActivity(), 15), Misc.ToDP(GetActivity(), 15), Misc.ToDP(GetActivity(), 15), Misc.ToDP(GetActivity(), 15));
        RelativeLayoutNextParam.addRule(Misc.Align("L"));

        GradientDrawable DrawableEnable = new GradientDrawable();
        DrawableEnable.setColor(ContextCompat.getColor(GetActivity(), R.color.BlueLight));
        DrawableEnable.setCornerRadius(Misc.ToDP(GetActivity(), 7));

        GradientDrawable DrawableDisable = new GradientDrawable();
        DrawableDisable.setCornerRadius(Misc.ToDP(GetActivity(), 7));
        DrawableDisable.setColor(ContextCompat.getColor(GetActivity(), R.color.Gray2));

        StateListDrawable ListDrawableNext = new StateListDrawable();
        ListDrawableNext.addState(new int[] { android.R.attr.state_enabled }, DrawableEnable);
        ListDrawableNext.addState(new int[] { -android.R.attr.state_enabled }, DrawableDisable);

        RelativeLayout RelativeLayoutNext = new RelativeLayout(GetActivity());
        RelativeLayoutNext.setLayoutParams(RelativeLayoutNextParam);
        RelativeLayoutNext.setBackground(ListDrawableNext);

        RelativeLayoutBottom.addView(RelativeLayoutNext);

        ButtonNext.setLayoutParams(new RelativeLayout.LayoutParams(Misc.ToDP(GetActivity(), 90), Misc.ToDP(GetActivity(), 35)));
        ButtonNext.setText(GetActivity().getString(R.string.GeneralNext));
        ButtonNext.setBackground(ListDrawableNext);
        ButtonNext.setEnabled(false);
        ButtonNext.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ButtonNext.setVisibility(View.GONE);
                LoadingViewNext.Start();

                AndroidNetworking.post(Misc.GetRandomServer("SignUpEmail"))
                .addBodyParameter("Username", Username)
                .addBodyParameter("Password", Password)
                .addBodyParameter("Email", EditTextEmail.getText().toString())
                .setTag("EmailUI")
                .build()
                .getAsString(new StringRequestListener()
                {
                    @Override
                    public void onResponse(String Response)
                    {
                        LoadingViewNext.Stop();
                        ButtonNext.setVisibility(View.VISIBLE);

                        try
                        {
                            JSONObject Result = new JSONObject(Response);

                            switch (Result.getInt("Message"))
                            {
                                case 0:
                                    TranslateAnimation Anim = Misc.IsRTL() ? new TranslateAnimation(0f, -1000f, 0f, 0f) : new TranslateAnimation(0f, 1000f, 0f, 0f);
                                    Anim.setDuration(200);

                                    RelativeLayoutMain.setAnimation(Anim);

                                    Misc.Toast(GetActivity(), GetActivity().getString(R.string.EmailUIError5));

                                    GetActivity().GetManager().OpenView(new EmailVerifyUI(Username, Password, EditTextEmail.getText().toString()), R.id.ContainerFull, "EmailVerifyUI");
                                    break;
                                case 1:
                                case 2:
                                case 3:
                                case 4:
                                    Misc.Toast(GetActivity(), GetActivity().getString(R.string.EmailUIError1));
                                    break;
                                case 5:
                                case 6:
                                case 7:
                                    Misc.Toast(GetActivity(), GetActivity().getString(R.string.EmailUIError2));
                                    break;
                                case 8:
                                case 9:
                                    Misc.Toast(GetActivity(), GetActivity().getString(R.string.EmailUIError3));
                                    break;
                                case 10:
                                    Misc.Toast(GetActivity(), GetActivity().getString(R.string.EmailUIError4));
                                    break;
                                default:
                                    Misc.GeneralError(GetActivity(), Result.getInt("Message"));
                                    break;
                            }
                        }
                        catch (Exception e)
                        {
                            Misc.Debug("EmailUI: " + e.toString());
                        }
                    }

                    @Override
                    public void onError(ANError anError)
                    {
                        LoadingViewNext.Stop();
                        ButtonNext.setVisibility(View.VISIBLE);
                        Misc.Toast(GetActivity(), GetActivity().getString(R.string.GeneralNoInternet));
                    }
                });
            }
        });

        RelativeLayoutNext.addView(ButtonNext);

        RelativeLayout.LayoutParams LoadingViewUsernameParam = new RelativeLayout.LayoutParams(Misc.ToDP(GetActivity(), 90), Misc.ToDP(GetActivity(), 35));
        LoadingViewUsernameParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        LoadingViewNext.setLayoutParams(LoadingViewUsernameParam);
        LoadingViewNext.SetColor(R.color.TextDark);

        RelativeLayoutNext.addView(LoadingViewNext);

        TranslateAnimation Anim = Misc.IsRTL() ? new TranslateAnimation(1000f, 0f, 0f, 0f) : new TranslateAnimation(-1000f, 0f, 0f, 0f);
        Anim.setDuration(200);
        Anim.setAnimationListener(new Animation.AnimationListener()
        {
            @Override public void onAnimationStart(Animation animation) { }
            @Override public void onAnimationRepeat(Animation animation) { }
            @Override public void onAnimationEnd(Animation animation) { Misc.ShowSoftKey(EditTextEmail); }
        });

        RelativeLayoutMain.startAnimation(Anim);

        ViewMain = RelativeLayoutMain;
    }

    @Override
    public void OnResume()
    {
        RelativeLayoutMain.getViewTreeObserver().addOnGlobalLayoutListener(LayoutListener);
    }

    @Override
    public void OnPause()
    {
        Misc.HideSoftKey(GetActivity());
        AndroidNetworking.forceCancel("EmailUI");
        RelativeLayoutMain.getViewTreeObserver().removeOnGlobalLayoutListener(LayoutListener);
    }
}
