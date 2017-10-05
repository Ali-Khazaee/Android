package co.biogram.main.ui;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.json.JSONObject;

import co.biogram.fragment.FragmentActivity;
import co.biogram.fragment.FragmentBase;
import co.biogram.main.handler.MiscHandler;
import co.biogram.main.misc.LoadingView;
import co.biogram.main.R;

class SignUpPhoneVerification extends FragmentBase
{
    private ViewTreeObserver.OnGlobalLayoutListener RelativeLayoutMainListener;
    private int RelativeLayoutMainHeightDifference = 0;
    private RelativeLayout RelativeLayoutMain;

    @Override
    public void OnCreate()
    {
        final FragmentActivity activity = GetActivity();
        final Button ButtonNext = new Button(activity, null, android.R.attr.borderlessButtonStyle);
        final LoadingView LoadingViewNext = new LoadingView(activity);

        RelativeLayoutMain = new RelativeLayout(activity);
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

                if (DifferenceHeight > (ScreenHeight / 3) && DifferenceHeight != RelativeLayoutMainHeightDifference)
                {
                    RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ScreenHeight - DifferenceHeight));
                    RelativeLayoutMainHeightDifference = DifferenceHeight;
                }
                else if (DifferenceHeight != RelativeLayoutMainHeightDifference)
                {
                    RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ScreenHeight));
                    RelativeLayoutMainHeightDifference = DifferenceHeight;
                }
                else if (RelativeLayoutMainHeightDifference != 0)
                {
                    RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ScreenHeight + Math.abs(RelativeLayoutMainHeightDifference)));
                    RelativeLayoutMainHeightDifference = 0;
                }

                RelativeLayoutMain.requestLayout();
            }
        };

        RelativeLayout RelativeLayoutHeader = new RelativeLayout(activity);
        RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(activity, 56)));
        RelativeLayoutHeader.setBackgroundResource(R.color.BlueLight);
        RelativeLayoutHeader.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain.addView(RelativeLayoutHeader);

        RelativeLayout.LayoutParams ImageViewBackParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(activity, 56), RelativeLayout.LayoutParams.MATCH_PARENT);
        ImageViewBackParam.addRule(MiscHandler.Align("R"));

        ImageView ImageViewBack = new ImageView(activity);
        ImageViewBack.setLayoutParams(ImageViewBackParam);
        ImageViewBack.setScaleType(ImageView.ScaleType.FIT_XY);
        ImageViewBack.setId(MiscHandler.GenerateViewID());
        ImageViewBack.setPadding(MiscHandler.ToDimension(activity, 12), MiscHandler.ToDimension(activity, 12), MiscHandler.ToDimension(activity, 12), MiscHandler.ToDimension(activity, 12));
        ImageViewBack.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { GetActivity().onBackPressed(); } });
        ImageViewBack.setImageResource(MiscHandler.IsFA() ? R.drawable.ic_back_white_fa : R.drawable.ic_back_white);

        RelativeLayoutHeader.addView(ImageViewBack);

        RelativeLayout.LayoutParams TextViewHeaderParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewHeaderParam.addRule(MiscHandler.AlignTo("R"), ImageViewBack.getId());
        TextViewHeaderParam.addRule(RelativeLayout.CENTER_VERTICAL);

        TextView TextViewTitle = new TextView(activity);
        TextViewTitle.setLayoutParams(TextViewHeaderParam);
        TextViewTitle.setTextColor(ContextCompat.getColor(activity, R.color.White));
        TextViewTitle.setText(activity.getString(R.string.SignUpPhoneVerification));
        TextViewTitle.setTypeface(null, Typeface.BOLD);
        TextViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

        if (MiscHandler.IsFA())
            TextViewTitle.setTypeface(Typeface.createFromAsset(activity.getAssets(), "iran-sans.ttf"));

        RelativeLayoutHeader.addView(TextViewTitle);

        RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(activity, 1));
        ViewLineParam.addRule(RelativeLayout.BELOW, RelativeLayoutHeader.getId());

        View ViewLine = new View(activity);
        ViewLine.setLayoutParams(ViewLineParam);
        ViewLine.setBackgroundResource(R.color.Gray2);
        ViewLine.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain.addView(ViewLine);

        RelativeLayout.LayoutParams ScrollViewMainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        ScrollViewMainParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

        ScrollView ScrollViewMain = new ScrollView(activity);
        ScrollViewMain.setLayoutParams(ScrollViewMainParam);
        ScrollViewMain.setFillViewport(true);

        RelativeLayoutMain.addView(ScrollViewMain);

        RelativeLayout RelativeLayoutScroll = new RelativeLayout(activity);
        RelativeLayoutScroll.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

        ScrollViewMain.addView(RelativeLayoutScroll);

        RelativeLayout.LayoutParams TextViewVerificationCodeParam = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewVerificationCodeParam.addRule(MiscHandler.Align("R"));

        TextView TextViewVerificationCode = new TextView(activity);
        TextViewVerificationCode.setLayoutParams(TextViewVerificationCodeParam);
        TextViewVerificationCode.setPadding(MiscHandler.ToDimension(activity, 20), MiscHandler.ToDimension(activity, 20), MiscHandler.ToDimension(activity, 20), 0);
        TextViewVerificationCode.setTextColor(ContextCompat.getColor(activity, R.color.Gray4));
        TextViewVerificationCode.setText(activity.getString(R.string.SignUpPhoneVerificationCode));
        TextViewVerificationCode.setTypeface(null, Typeface.BOLD);
        TextViewVerificationCode.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        TextViewVerificationCode.setId(MiscHandler.GenerateViewID());

        if (MiscHandler.IsFA())
            TextViewVerificationCode.setTypeface(Typeface.createFromAsset(activity.getAssets(), "iran-sans.ttf"));

        RelativeLayoutScroll.addView(TextViewVerificationCode);

        RelativeLayout.LayoutParams LinearLayoutVerificationCodeParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LinearLayoutVerificationCodeParam.addRule(RelativeLayout.BELOW, TextViewVerificationCode.getId());

        LinearLayout LinearLayoutVerificationCode = new LinearLayout(activity);
        LinearLayoutVerificationCode.setLayoutParams(LinearLayoutVerificationCodeParam);
        LinearLayoutVerificationCode.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayoutVerificationCode.setId(MiscHandler.GenerateViewID());

        RelativeLayoutScroll.addView(LinearLayoutVerificationCode);

        LinearLayout.LayoutParams EditTextVerificationCode1Param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        EditTextVerificationCode1Param.setMargins(MiscHandler.ToDimension(activity, 10), 0, MiscHandler.ToDimension(activity, 10), 0);

        EditText EditTextVerificationCode1 = new EditText(activity);
        EditTextVerificationCode1.setLayoutParams(EditTextVerificationCode1Param);
        EditTextVerificationCode1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        EditTextVerificationCode1.getBackground().setColorFilter(ContextCompat.getColor(activity, R.color.BlueLight), PorterDuff.Mode.SRC_ATOP);
        EditTextVerificationCode1.setGravity(Gravity.CENTER_HORIZONTAL);
        EditTextVerificationCode1.setInputType(InputType.TYPE_CLASS_PHONE);
        EditTextVerificationCode1.setFilters(new InputFilter[] { new InputFilter.LengthFilter(1) });
        EditTextVerificationCode1.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void afterTextChanged(Editable s) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (s.length() > 7)
                    ButtonNext.setEnabled(true);
                else
                    ButtonNext.setEnabled(false);
            }
        });

        LinearLayoutVerificationCode.addView(EditTextVerificationCode1);

        LinearLayout.LayoutParams EditTextVerificationCode2Param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        EditTextVerificationCode2Param.setMargins(MiscHandler.ToDimension(activity, 10), 0, MiscHandler.ToDimension(activity, 10), 0);

        EditText EditTextVerificationCode2 = new EditText(activity);
        EditTextVerificationCode2.setLayoutParams(EditTextVerificationCode2Param);
        EditTextVerificationCode2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        EditTextVerificationCode2.getBackground().setColorFilter(ContextCompat.getColor(activity, R.color.BlueLight), PorterDuff.Mode.SRC_ATOP);
        EditTextVerificationCode2.setGravity(Gravity.CENTER_HORIZONTAL);
        EditTextVerificationCode2.setInputType(InputType.TYPE_CLASS_PHONE);
        EditTextVerificationCode2.setFilters(new InputFilter[] { new InputFilter.LengthFilter(1) });
        EditTextVerificationCode2.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void afterTextChanged(Editable s) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (s.length() > 7)
                    ButtonNext.setEnabled(true);
                else
                    ButtonNext.setEnabled(false);
            }
        });

        LinearLayoutVerificationCode.addView(EditTextVerificationCode2);

        LinearLayout.LayoutParams EditTextVerificationCode3Param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        EditTextVerificationCode3Param.setMargins(MiscHandler.ToDimension(activity, 10), 0, MiscHandler.ToDimension(activity, 10), 0);

        EditText EditTextVerificationCode3 = new EditText(activity);
        EditTextVerificationCode3.setLayoutParams(EditTextVerificationCode3Param);
        EditTextVerificationCode3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        EditTextVerificationCode3.getBackground().setColorFilter(ContextCompat.getColor(activity, R.color.BlueLight), PorterDuff.Mode.SRC_ATOP);
        EditTextVerificationCode3.setGravity(Gravity.CENTER_HORIZONTAL);
        EditTextVerificationCode3.setInputType(InputType.TYPE_CLASS_PHONE);
        EditTextVerificationCode3.setFilters(new InputFilter[] { new InputFilter.LengthFilter(1) });
        EditTextVerificationCode3.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void afterTextChanged(Editable s) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (s.length() > 7)
                    ButtonNext.setEnabled(true);
                else
                    ButtonNext.setEnabled(false);
            }
        });

        LinearLayoutVerificationCode.addView(EditTextVerificationCode3);

        LinearLayout.LayoutParams EditTextVerificationCode4Param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        EditTextVerificationCode4Param.setMargins(MiscHandler.ToDimension(activity, 10), 0, MiscHandler.ToDimension(activity, 10), 0);

        EditText EditTextVerificationCode4 = new EditText(activity);
        EditTextVerificationCode4.setLayoutParams(EditTextVerificationCode4Param);
        EditTextVerificationCode4.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        EditTextVerificationCode4.getBackground().setColorFilter(ContextCompat.getColor(activity, R.color.BlueLight), PorterDuff.Mode.SRC_ATOP);
        EditTextVerificationCode4.setGravity(Gravity.CENTER_HORIZONTAL);
        EditTextVerificationCode4.setInputType(InputType.TYPE_CLASS_PHONE);
        EditTextVerificationCode4.setFilters(new InputFilter[] { new InputFilter.LengthFilter(1) });
        EditTextVerificationCode4.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void afterTextChanged(Editable s) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (s.length() > 7)
                    ButtonNext.setEnabled(true);
                else
                    ButtonNext.setEnabled(false);
            }
        });

        LinearLayoutVerificationCode.addView(EditTextVerificationCode4);

        LinearLayout.LayoutParams EditTextVerificationCode5Param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        EditTextVerificationCode5Param.setMargins(MiscHandler.ToDimension(activity, 10), 0, MiscHandler.ToDimension(activity, 10), 0);

        EditText EditTextVerificationCode5 = new EditText(activity);
        EditTextVerificationCode5.setLayoutParams(EditTextVerificationCode5Param);
        EditTextVerificationCode5.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        EditTextVerificationCode5.getBackground().setColorFilter(ContextCompat.getColor(activity, R.color.BlueLight), PorterDuff.Mode.SRC_ATOP);
        EditTextVerificationCode5.setGravity(Gravity.CENTER_HORIZONTAL);
        EditTextVerificationCode5.setInputType(InputType.TYPE_CLASS_PHONE);
        EditTextVerificationCode5.setFilters(new InputFilter[] { new InputFilter.LengthFilter(1) });
        EditTextVerificationCode5.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void afterTextChanged(Editable s) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (s.length() > 7)
                    ButtonNext.setEnabled(true);
                else
                    ButtonNext.setEnabled(false);
            }
        });

        LinearLayoutVerificationCode.addView(EditTextVerificationCode5);

        RelativeLayout.LayoutParams TextViewMessageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewMessageParam.addRule(RelativeLayout.BELOW, LinearLayoutVerificationCode.getId());
        TextViewMessageParam.addRule(MiscHandler.Align("R"));

        TextView TextViewMessage = new TextView(activity);
        TextViewMessage.setLayoutParams(TextViewMessageParam);
        TextViewMessage.setTextColor(ContextCompat.getColor(activity, R.color.Black));
        TextViewMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        TextViewMessage.setId(MiscHandler.GenerateViewID());
        TextViewMessage.setPadding(MiscHandler.ToDimension(activity, 15), MiscHandler.ToDimension(activity, 15), MiscHandler.ToDimension(activity, 15), MiscHandler.ToDimension(activity, 15));
        TextViewMessage.setMovementMethod(LinkMovementMethod.getInstance());
        TextViewMessage.setText(activity.getString(R.string.SignUpPhoneMessage) + " " + activity.getString(R.string.SignUpPhoneMessageEmail), TextView.BufferType.SPANNABLE);

        if (MiscHandler.IsFA())
            TextViewMessage.setTypeface(Typeface.createFromAsset(activity.getAssets(), "iran-sans.ttf"));

        Spannable Span = (Spannable) TextViewMessage.getText();
        ClickableSpan ClickableSpanMessage = new ClickableSpan()
        {
            @Override
            public void onClick(View v)
            {
                GetActivity().GetManager().OpenView(new SignUpPhone(), R.id.WelcomeActivityContainer, "SignUpPhone");
            }

            @Override
            public void updateDrawState(TextPaint t)
            {
                super.updateDrawState(t);
                t.setColor(ContextCompat.getColor(activity, R.color.BlueLight));
                t.setUnderlineText(false);
            }
        };
        Span.setSpan(ClickableSpanMessage, activity.getString(R.string.SignUpPhoneMessage).length() + 1, Span.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        RelativeLayoutScroll.addView(TextViewMessage);

        RelativeLayout.LayoutParams RelativeLayoutBottomParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayoutBottomParam.addRule(RelativeLayout.BELOW, TextViewMessage.getId());

        RelativeLayout RelativeLayoutBottom = new RelativeLayout(activity);
        RelativeLayoutBottom.setLayoutParams(RelativeLayoutBottomParam);

        RelativeLayoutScroll.addView(RelativeLayoutBottom);

        RelativeLayout.LayoutParams TextViewPrivacyParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewPrivacyParam.addRule(RelativeLayout.CENTER_VERTICAL);
        TextViewPrivacyParam.addRule(MiscHandler.Align("R"));

        TextView TextViewPrivacy = new TextView(activity);
        TextViewPrivacy.setLayoutParams(TextViewPrivacyParam);
        TextViewPrivacy.setTextColor(ContextCompat.getColor(activity, R.color.BlueLight));
        TextViewPrivacy.setText(activity.getString(R.string.SignUpPhoneTerm));
        TextViewPrivacy.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        TextViewPrivacy.setPadding(MiscHandler.ToDimension(activity, 15), MiscHandler.ToDimension(activity, 15), MiscHandler.ToDimension(activity, 15), MiscHandler.ToDimension(activity, 15));
        TextViewPrivacy.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://biogram.co/"))); } });

        RelativeLayoutBottom.addView(TextViewPrivacy);

        if (MiscHandler.IsFA())
            TextViewPrivacy.setTypeface(Typeface.createFromAsset(activity.getAssets(), "iran-sans.ttf"));

        GradientDrawable GradientDrawableNext = new GradientDrawable();
        GradientDrawableNext.setColor(ContextCompat.getColor(activity, R.color.BlueLight));
        GradientDrawableNext.setCornerRadius(MiscHandler.ToDimension(activity, 7));

        GradientDrawable GradientDrawableNextDisable = new GradientDrawable();
        GradientDrawableNextDisable.setCornerRadius(MiscHandler.ToDimension(activity, 7));
        GradientDrawableNextDisable.setColor(ContextCompat.getColor(activity, R.color.Gray2));

        StateListDrawable StateSignUp = new StateListDrawable();
        StateSignUp.addState(new int[] { android.R.attr.state_enabled }, GradientDrawableNext);
        StateSignUp.addState(new int[] { -android.R.attr.state_enabled }, GradientDrawableNextDisable);

        RelativeLayout.LayoutParams RelativeLayoutNextParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(activity, 90), MiscHandler.ToDimension(activity, 35));
        RelativeLayoutNextParam.setMargins(MiscHandler.ToDimension(activity, 15), MiscHandler.ToDimension(activity, 15), MiscHandler.ToDimension(activity, 15), MiscHandler.ToDimension(activity, 15));
        RelativeLayoutNextParam.addRule(MiscHandler.Align("L"));

        RelativeLayout RelativeLayoutNext = new RelativeLayout(activity);
        RelativeLayoutNext.setLayoutParams(RelativeLayoutNextParam);
        RelativeLayoutNext.setBackground(GradientDrawableNext);

        RelativeLayoutBottom.addView(RelativeLayoutNext);

        ButtonNext.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(activity, 90), MiscHandler.ToDimension(activity, 35)));
        ButtonNext.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        ButtonNext.setTextColor(ContextCompat.getColor(activity, R.color.White));
        ButtonNext.setText(activity.getString(R.string.SignUpPhoneNext));
        ButtonNext.setBackground(StateSignUp);
        ButtonNext.setPadding(0, 0, 0, 0);
        ButtonNext.setEnabled(false);
        ButtonNext.setAllCaps(false);
        ButtonNext.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ButtonNext.setVisibility(View.GONE);
                LoadingViewNext.Start();

                AndroidNetworking.post(MiscHandler.GetRandomServer("SignUpPhone"))
                        //.addBodyParameter("Code", EditTextPhoneCode.getText().toString())
                        //.addBodyParameter("Phone", EditTextPhone.getText().toString())
                        .setTag("SignUpPhoneVerification")
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
                                        case -6:
                                            MiscHandler.Toast(activity, activity.getString(R.string.GeneralError6));
                                            break;
                                        case -2:
                                            MiscHandler.Toast(activity, activity.getString(R.string.GeneralError2));
                                            break;
                                        case -1:
                                            MiscHandler.Toast(activity, activity.getString(R.string.GeneralError1));
                                            break;
                                        case 0:

                                            break;
                                        case 1:
                                        case 2:
                                        case 3:
                                            MiscHandler.Toast(activity, activity.getString(R.string.SignUpPhoneCodeError));
                                            break;
                                        case 4:
                                        case 5:
                                        case 6:
                                            MiscHandler.Toast(activity, activity.getString(R.string.SignUpPhoneError));
                                            break;
                                        case 7:
                                            MiscHandler.Toast(activity, activity.getString(R.string.SignUpPhoneAlready));
                                            break;
                                    }
                                }
                                catch (Exception e)
                                {
                                    MiscHandler.Debug("SignUpPhone-RequestSignUpPhoneVerification: " + e.toString());
                                }
                            }

                            @Override
                            public void onError(ANError anError)
                            {
                                LoadingViewNext.Stop();
                                ButtonNext.setVisibility(View.VISIBLE);
                                MiscHandler.Toast(activity, activity.getString(R.string.GeneralNoInternet));
                            }
                        });
            }
        });

        if (MiscHandler.IsFA())
            ButtonNext.setTypeface(Typeface.createFromAsset(activity.getAssets(), "iran-sans.ttf"));

        RelativeLayoutNext.addView(ButtonNext);

        RelativeLayout.LayoutParams LoadingViewUsernameParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(activity, 90), MiscHandler.ToDimension(activity, 35));
        LoadingViewUsernameParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        LoadingViewNext.setLayoutParams(LoadingViewUsernameParam);
        LoadingViewNext.SetColor(R.color.White);

        RelativeLayoutNext.addView(LoadingViewNext);

        TranslateAnimation Anim = MiscHandler.IsFA() ? new TranslateAnimation(1000f, 0f, 0f, 0f) : new TranslateAnimation(-1000f, 0f, 0f, 0f);
        Anim.setDuration(150);

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
        RelativeLayoutMain.getViewTreeObserver().removeOnGlobalLayoutListener(RelativeLayoutMainListener);
        AndroidNetworking.forceCancel("SignUpPhoneVerification");
    }
}
