package co.biogram.main.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.CharacterStyle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.json.JSONObject;

import co.biogram.fragment.FragmentActivity;
import co.biogram.fragment.FragmentBase;
import co.biogram.main.handler.MiscHandler;
import co.biogram.main.R;

class SignUpPhoneVerification extends FragmentBase
{
    private ViewTreeObserver.OnGlobalLayoutListener RelativeLayoutMainListener;
    private int RelativeLayoutMainHeightDifference = 0;
    private RelativeLayout RelativeLayoutMain;

    private EditText EditTextVerificationCode1;
    private EditText EditTextVerificationCode2;
    private EditText EditTextVerificationCode3;
    private EditText EditTextVerificationCode4;
    private EditText EditTextVerificationCode5;

    private boolean Field1 = false;
    private boolean Field2 = false;
    private boolean Field3 = false;
    private boolean Field4 = false;
    private boolean Field5 = false;

    private final String Code;
    private final String Phone;

    private CountDownTimer CountDownTimerResend;

    SignUpPhoneVerification(String code, String phone)
    {
        Code = code;
        Phone = phone;
    }

    @Override
    public void OnCreate()
    {
        final FragmentActivity activity = GetActivity();
        final Button ButtonNext = new Button(activity, false);
        final LoadingView LoadingViewNext = new LoadingView(activity);

        RelativeLayoutMain = new RelativeLayout(activity);
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayoutMain.setBackgroundResource(R.color.White);
        RelativeLayoutMain.setFocusableInTouchMode(true);
        RelativeLayoutMain.setFocusable(true);
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
        ImageViewBack.setImageResource(MiscHandler.IsFa() ? R.drawable.ic_back_white_fa : R.drawable.ic_back_white);

        RelativeLayoutHeader.addView(ImageViewBack);

        RelativeLayout.LayoutParams TextViewTitleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTitleParam.addRule(MiscHandler.AlignTo("R"), ImageViewBack.getId());
        TextViewTitleParam.addRule(RelativeLayout.CENTER_VERTICAL);

        TextView TextViewTitle = new TextView(activity, true);
        TextViewTitle.setLayoutParams(TextViewTitleParam);
        TextViewTitle.setTextColor(ContextCompat.getColor(activity, R.color.White));
        TextViewTitle.setText(activity.getString(R.string.SignUpPhoneVerification));
        TextViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

        RelativeLayoutHeader.addView(TextViewTitle);

        RelativeLayout.LayoutParams TextViewTimeParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTimeParam.addRule(RelativeLayout.CENTER_VERTICAL);
        TextViewTimeParam.addRule(MiscHandler.Align("L"));

        final TextView TextViewTime = new TextView(activity, false);
        TextViewTime.setLayoutParams(TextViewTimeParam);
        TextViewTime.setTextColor(ContextCompat.getColor(activity, R.color.White));
        TextViewTime.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        TextViewTime.setPadding(MiscHandler.ToDimension(activity, 15), 0, MiscHandler.ToDimension(activity, 15), 0);

        RelativeLayoutHeader.addView(TextViewTime);

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

        TextView TextViewVerificationCode = new TextView(activity, true);
        TextViewVerificationCode.setLayoutParams(TextViewVerificationCodeParam);
        TextViewVerificationCode.setPadding(MiscHandler.ToDimension(activity, 20), MiscHandler.ToDimension(activity, 40), MiscHandler.ToDimension(activity, 20), MiscHandler.ToDimension(activity, 15));
        TextViewVerificationCode.setTextColor(ContextCompat.getColor(activity, R.color.Gray4));
        TextViewVerificationCode.setText(activity.getString(R.string.SignUpPhoneVerificationCode));
        TextViewVerificationCode.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        TextViewVerificationCode.setId(MiscHandler.GenerateViewID());

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

        EditTextVerificationCode1 = new EditText(activity);
        EditTextVerificationCode1.setLayoutParams(EditTextVerificationCode1Param);
        EditTextVerificationCode1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        EditTextVerificationCode1.setTypeface(null, Typeface.BOLD);
        EditTextVerificationCode1.getBackground().setColorFilter(ContextCompat.getColor(activity, R.color.BlueLight), PorterDuff.Mode.SRC_ATOP);
        EditTextVerificationCode1.setGravity(Gravity.CENTER_HORIZONTAL);
        EditTextVerificationCode1.setInputType(InputType.TYPE_CLASS_PHONE);
        EditTextVerificationCode1.setFilters(new InputFilter[] { new InputFilter.LengthFilter(1) });
        EditTextVerificationCode1.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        EditTextVerificationCode1.addTextChangedListener(new TextWatcher()
        {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void afterTextChanged(Editable s) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                Field1 = (s.length() != 0);

                if (Field1 && Field2 && Field3 && Field4 && Field5)
                    ButtonNext.setEnabled(true);
                else
                    ButtonNext.setEnabled(false);

                if (s.length() == 0)
                    return;

                EditText NextField = (EditText) EditTextVerificationCode1.focusSearch(View.FOCUS_RIGHT);
                NextField.requestFocus();
            }
        });

        LinearLayoutVerificationCode.addView(EditTextVerificationCode1);

        LinearLayout.LayoutParams EditTextVerificationCode2Param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        EditTextVerificationCode2Param.setMargins(MiscHandler.ToDimension(activity, 10), 0, MiscHandler.ToDimension(activity, 10), 0);

        EditTextVerificationCode2 = new EditText(activity);
        EditTextVerificationCode2.setLayoutParams(EditTextVerificationCode2Param);
        EditTextVerificationCode2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        EditTextVerificationCode2.setTypeface(null, Typeface.BOLD);
        EditTextVerificationCode2.getBackground().setColorFilter(ContextCompat.getColor(activity, R.color.BlueLight), PorterDuff.Mode.SRC_ATOP);
        EditTextVerificationCode2.setGravity(Gravity.CENTER_HORIZONTAL);
        EditTextVerificationCode2.setInputType(InputType.TYPE_CLASS_PHONE);
        EditTextVerificationCode2.setFilters(new InputFilter[] { new InputFilter.LengthFilter(1) });
        EditTextVerificationCode2.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        EditTextVerificationCode2.addTextChangedListener(new TextWatcher()
        {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void afterTextChanged(Editable s) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                Field2 = (s.length() != 0);

                if (Field1 && Field2 && Field3 && Field4 && Field5)
                    ButtonNext.setEnabled(true);
                else
                    ButtonNext.setEnabled(false);

                EditText NextField = (EditText) EditTextVerificationCode2.focusSearch(View.FOCUS_RIGHT);

                if (s.length() == 0)
                    NextField = (EditText) EditTextVerificationCode2.focusSearch(View.FOCUS_LEFT);

                NextField.requestFocus();
            }
        });

        LinearLayoutVerificationCode.addView(EditTextVerificationCode2);

        LinearLayout.LayoutParams EditTextVerificationCode3Param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        EditTextVerificationCode3Param.setMargins(MiscHandler.ToDimension(activity, 10), 0, MiscHandler.ToDimension(activity, 10), 0);

        EditTextVerificationCode3 = new EditText(activity);
        EditTextVerificationCode3.setLayoutParams(EditTextVerificationCode3Param);
        EditTextVerificationCode3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        EditTextVerificationCode3.setTypeface(null, Typeface.BOLD);
        EditTextVerificationCode3.getBackground().setColorFilter(ContextCompat.getColor(activity, R.color.BlueLight), PorterDuff.Mode.SRC_ATOP);
        EditTextVerificationCode3.setGravity(Gravity.CENTER_HORIZONTAL);
        EditTextVerificationCode3.setInputType(InputType.TYPE_CLASS_PHONE);
        EditTextVerificationCode3.setFilters(new InputFilter[] { new InputFilter.LengthFilter(1) });
        EditTextVerificationCode3.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        EditTextVerificationCode3.addTextChangedListener(new TextWatcher()
        {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void afterTextChanged(Editable s) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                Field3 = (s.length() != 0);

                if (Field1 && Field2 && Field3 && Field4 && Field5)
                    ButtonNext.setEnabled(true);
                else
                    ButtonNext.setEnabled(false);

                EditText NextField = (EditText) EditTextVerificationCode3.focusSearch(View.FOCUS_RIGHT);

                if (s.length() == 0)
                    NextField = (EditText) EditTextVerificationCode3.focusSearch(View.FOCUS_LEFT);

                NextField.requestFocus();
            }
        });

        LinearLayoutVerificationCode.addView(EditTextVerificationCode3);

        LinearLayout.LayoutParams EditTextVerificationCode4Param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        EditTextVerificationCode4Param.setMargins(MiscHandler.ToDimension(activity, 10), 0, MiscHandler.ToDimension(activity, 10), 0);

        EditTextVerificationCode4 = new EditText(activity);
        EditTextVerificationCode4.setLayoutParams(EditTextVerificationCode4Param);
        EditTextVerificationCode4.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        EditTextVerificationCode4.setTypeface(null, Typeface.BOLD);
        EditTextVerificationCode4.getBackground().setColorFilter(ContextCompat.getColor(activity, R.color.BlueLight), PorterDuff.Mode.SRC_ATOP);
        EditTextVerificationCode4.setGravity(Gravity.CENTER_HORIZONTAL);
        EditTextVerificationCode4.setInputType(InputType.TYPE_CLASS_PHONE);
        EditTextVerificationCode4.setFilters(new InputFilter[] { new InputFilter.LengthFilter(1) });
        EditTextVerificationCode4.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        EditTextVerificationCode4.addTextChangedListener(new TextWatcher()
        {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void afterTextChanged(Editable s) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                Field4 = (s.length() != 0);

                if (Field1 && Field2 && Field3 && Field4 && Field5)
                    ButtonNext.setEnabled(true);
                else
                    ButtonNext.setEnabled(false);

                EditText NextField = (EditText) EditTextVerificationCode4.focusSearch(View.FOCUS_RIGHT);

                if (s.length() == 0)
                    NextField = (EditText) EditTextVerificationCode4.focusSearch(View.FOCUS_LEFT);

                NextField.requestFocus();
            }
        });

        LinearLayoutVerificationCode.addView(EditTextVerificationCode4);

        LinearLayout.LayoutParams EditTextVerificationCode5Param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        EditTextVerificationCode5Param.setMargins(MiscHandler.ToDimension(activity, 10), 0, MiscHandler.ToDimension(activity, 10), 0);

        EditTextVerificationCode5 = new EditText(activity);
        EditTextVerificationCode5.setLayoutParams(EditTextVerificationCode5Param);
        EditTextVerificationCode5.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        EditTextVerificationCode5.setTypeface(null, Typeface.BOLD);
        EditTextVerificationCode5.getBackground().setColorFilter(ContextCompat.getColor(activity, R.color.BlueLight), PorterDuff.Mode.SRC_ATOP);
        EditTextVerificationCode5.setGravity(Gravity.CENTER_HORIZONTAL);
        EditTextVerificationCode5.setInputType(InputType.TYPE_CLASS_PHONE);
        EditTextVerificationCode5.setFilters(new InputFilter[] { new InputFilter.LengthFilter(1) });
        EditTextVerificationCode5.addTextChangedListener(new TextWatcher()
        {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void afterTextChanged(Editable s) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                Field5 = (s.length() != 0);

                if (Field1 && Field2 && Field3 && Field4 && Field5)
                    ButtonNext.setEnabled(true);
                else
                    ButtonNext.setEnabled(false);

                if (s.length() == 0)
                {
                    EditText NextField = (EditText) EditTextVerificationCode5.focusSearch(View.FOCUS_LEFT);
                    NextField.requestFocus();
                }
            }
        });

        LinearLayoutVerificationCode.addView(EditTextVerificationCode5);

        RelativeLayout.LayoutParams TextViewMessageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewMessageParam.addRule(RelativeLayout.BELOW, LinearLayoutVerificationCode.getId());
        TextViewMessageParam.addRule(MiscHandler.Align("R"));

        final String PhoneNumber = Code + Phone;

        TextView TextViewMessage = new TextView(activity, false);
        TextViewMessage.setLayoutParams(TextViewMessageParam);
        TextViewMessage.setTextColor(ContextCompat.getColor(activity, R.color.Black));
        TextViewMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        TextViewMessage.setId(MiscHandler.GenerateViewID());
        TextViewMessage.setPadding(MiscHandler.ToDimension(activity, 15), MiscHandler.ToDimension(activity, 15), MiscHandler.ToDimension(activity, 15), MiscHandler.ToDimension(activity, 15));
        TextViewMessage.setMovementMethod(LinkMovementMethod.getInstance());
        TextViewMessage.setText(activity.getString(R.string.SignUpPhoneVerificationMessage) + " " + PhoneNumber, TextView.BufferType.SPANNABLE);

        Spannable Span = (Spannable) TextViewMessage.getText();
        CharacterStyle CharacterStyleMessage = new CharacterStyle()
        {
            @Override
            public void updateDrawState(TextPaint t)
            {
                t.setColor(ContextCompat.getColor(activity, R.color.Gray7));
                t.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            }
        };
        Span.setSpan(CharacterStyleMessage, activity.getString(R.string.SignUpPhoneVerificationMessage).length() + 1, Span.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        RelativeLayoutScroll.addView(TextViewMessage);

        RelativeLayout.LayoutParams RelativeLayoutBottomParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayoutBottomParam.addRule(RelativeLayout.BELOW, TextViewMessage.getId());

        RelativeLayout RelativeLayoutBottom = new RelativeLayout(activity);
        RelativeLayoutBottom.setLayoutParams(RelativeLayoutBottomParam);

        RelativeLayoutScroll.addView(RelativeLayoutBottom);

        RelativeLayout.LayoutParams LoadingViewResendParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(activity, 56), MiscHandler.ToDimension(activity, 56));
        LoadingViewResendParam.addRule(MiscHandler.Align("R"));

        final LoadingView LoadingViewResend = new LoadingView(activity);
        LoadingViewResend.setLayoutParams(LoadingViewResendParam);

        RelativeLayoutBottom.addView(LoadingViewResend);

        RelativeLayout.LayoutParams TextViewResendParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewResendParam.addRule(RelativeLayout.CENTER_VERTICAL);
        TextViewResendParam.addRule(MiscHandler.Align("R"));

        final TextView TextViewResend = new TextView(activity, false);
        TextViewResend.setLayoutParams(TextViewResendParam);
        TextViewResend.setTextColor(ContextCompat.getColor(activity, R.color.Gray7));
        TextViewResend.setText(activity.getString(R.string.SignUpPhoneVerificationResend));
        TextViewResend.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        TextViewResend.setPadding(MiscHandler.ToDimension(activity, 15), MiscHandler.ToDimension(activity, 15), MiscHandler.ToDimension(activity, 15), MiscHandler.ToDimension(activity, 15));
        TextViewResend.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (!TextViewResend.isEnabled())
                    return;

                TextViewResend.setVisibility(View.GONE);
                LoadingViewResend.Start();

                AndroidNetworking.post(MiscHandler.GetRandomServer("SignUpPhone"))
                .addBodyParameter("Code", Code)
                .addBodyParameter("Phone", Phone)
                .setTag("SignUpPhoneVerification")
                .build()
                .getAsString(new StringRequestListener()
                {
                    @Override
                    public void onResponse(String Response)
                    {
                        LoadingViewResend.Stop();
                        TextViewResend.setVisibility(View.VISIBLE);

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
                                    CountDownTimerResend.start();
                                    MiscHandler.Toast(activity, activity.getString(R.string.SignUpPhoneVerificationResendDone));
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
                            MiscHandler.Debug("SignUpPhoneVerification-RequestSignUpPhone: " + e.toString());
                        }
                    }

                    @Override
                    public void onError(ANError e)
                    {
                        LoadingViewResend.Stop();
                        TextViewResend.setVisibility(View.VISIBLE);
                        MiscHandler.Toast(activity, activity.getString(R.string.GeneralNoInternet));
                    }
                });
            }
        });

        CountDownTimerResend = new CountDownTimer(120000, 1000)
        {
            private boolean Enabled = true;

            @Override
            public void onTick(long Counter)
            {
                long Min = (Counter / 1000) / 60;
                long Sec = (Counter / 1000) - (Min * 60);

                TextViewTime.setText("0" + Min + ":" + (Sec < 9 ? "0" + String.valueOf(Sec) : String.valueOf(Sec)));

                if (Enabled)
                {
                    Enabled = false;
                    TextViewResend.setEnabled(false);
                    TextViewResend.setTextColor(ContextCompat.getColor(activity, R.color.Gray7));
                }
            }

            @Override
            public void onFinish()
            {
                cancel();

                Enabled = true;

                TextViewResend.setEnabled(true);
                TextViewResend.setTextColor(ContextCompat.getColor(activity, R.color.BlueLight));
                TextViewTime.setText("");
            }
        };
        CountDownTimerResend.start();

        RelativeLayoutBottom.addView(TextViewResend);

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

                final String VerifyCode = EditTextVerificationCode1.getText().toString() + EditTextVerificationCode2.getText().toString() + EditTextVerificationCode3.getText().toString() + EditTextVerificationCode4.getText().toString() + EditTextVerificationCode5.getText().toString();

                AndroidNetworking.post(MiscHandler.GetRandomServer("SignUpPhoneVerify"))
                .addBodyParameter("Code", Code)
                .addBodyParameter("Phone", Phone)
                .addBodyParameter("VerifyCode", VerifyCode)
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
                                    TranslateAnimation Anim = MiscHandler.IsRTL() ? new TranslateAnimation(0f, -1000f, 0f, 0f) : new TranslateAnimation(0f, 1000f, 0f, 0f);
                                    Anim.setDuration(200);

                                    RelativeLayoutMain.setAnimation(Anim);

                                    GetActivity().GetManager().OpenView(new SignUpUsername(VerifyCode), R.id.WelcomeActivityContainer, "SignUpUsername");
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
                                    MiscHandler.Toast(activity, activity.getString(R.string.SignUpPhoneVerificationCodeEmpty));
                                    break;
                                case 8:
                                    MiscHandler.Toast(activity, activity.getString(R.string.SignUpPhoneVerificationCodeCount));
                                    break;
                                case 9:
                                    MiscHandler.Toast(activity, activity.getString(R.string.SignUpPhoneVerificationCodeWrong));
                                    break;
                            }
                        }
                        catch (Exception e)
                        {
                            MiscHandler.Debug("SignUpPhoneVerification-RequestSignUpPhoneVerify: " + e.toString());
                        }
                    }

                    @Override
                    public void onError(ANError e)
                    {
                        LoadingViewNext.Stop();
                        ButtonNext.setVisibility(View.VISIBLE);
                        MiscHandler.Toast(activity, activity.getString(R.string.GeneralNoInternet));
                    }
                });
            }
        });

        RelativeLayoutNext.addView(ButtonNext);

        RelativeLayout.LayoutParams LoadingViewNextParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(activity, 90), MiscHandler.ToDimension(activity, 35));
        LoadingViewNextParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        LoadingViewNext.setLayoutParams(LoadingViewNextParam);
        LoadingViewNext.SetColor(R.color.White);

        RelativeLayoutNext.addView(LoadingViewNext);

        TranslateAnimation Anim = MiscHandler.IsRTL() ? new TranslateAnimation(1000f, 0f, 0f, 0f) : new TranslateAnimation(-1000f, 0f, 0f, 0f);
        Anim.setDuration(200);

        RelativeLayoutMain.startAnimation(Anim);

        ViewMain = RelativeLayoutMain;
    }

    @Override
    public void OnResume()
    {
        RelativeLayoutMain.getViewTreeObserver().addOnGlobalLayoutListener(RelativeLayoutMainListener);

        Intent i = new Intent("co.biogram.main.service.SMSService");
        i.putExtra("SetWaiting", true);

        GetActivity().sendBroadcast(i);
        GetActivity().registerReceiver(UpdateReceiver, new IntentFilter("co.biogram.main.ui.SignUpPhoneVerification"));
    }

    @Override
    public void OnPause()
    {
        RelativeLayoutMain.getViewTreeObserver().removeOnGlobalLayoutListener(RelativeLayoutMainListener);
        AndroidNetworking.forceCancel("SignUpPhoneVerification");

        Intent i = new Intent("co.biogram.main.service.SMSService");
        i.putExtra("SetWaiting", false);

        GetActivity().sendBroadcast(i);
        GetActivity().unregisterReceiver(UpdateReceiver);
    }

    @Override
    public void OnDestroy()
    {
        CountDownTimerResend.cancel();
        super.OnDestroy();
    }

    private BroadcastReceiver UpdateReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (intent.getAction().equalsIgnoreCase("co.biogram.main.ui.SignUpPhoneVerification"))
            {
                String VerifyCode = intent.getExtras().getString("Code", "");

                if (VerifyCode.length() < 4)
                    return;

                final String[] Separated = VerifyCode.split("(?!^)");

                MiscHandler.RunOnUIThread(context, new Runnable()
                {
                    @Override
                    public void run()
                    {
                        EditTextVerificationCode1.setText(Separated[0]);
                        EditTextVerificationCode2.setText(Separated[1]);
                        EditTextVerificationCode3.setText(Separated[2]);
                        EditTextVerificationCode4.setText(Separated[3]);
                        EditTextVerificationCode5.setText(Separated[4]);
                    }
                }, 0);
            }
        }
    };
}
