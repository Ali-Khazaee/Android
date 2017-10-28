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

import co.biogram.fragment.FragmentBase;
import co.biogram.main.handler.MiscHandler;
import co.biogram.main.R;
import co.biogram.main.view.Button;
import co.biogram.main.view.LoadingView;
import co.biogram.main.view.TextView;

class SignUpPhoneVerificationUI extends FragmentBase
{
    private ViewTreeObserver.OnGlobalLayoutListener RelativeLayoutMainListener;
    private RelativeLayout RelativeLayoutMain;

    private EditText EditTextVerificationCode1;
    private EditText EditTextVerificationCode2;
    private EditText EditTextVerificationCode3;
    private EditText EditTextVerificationCode4;
    private EditText EditTextVerificationCode5;

    private int HeightDifference = 0;

    private boolean Field1 = false;
    private boolean Field2 = false;
    private boolean Field3 = false;
    private boolean Field4 = false;
    private boolean Field5 = false;

    private final String Code;
    private final String Phone;

    private CountDownTimer CountDownTimerResend;

    SignUpPhoneVerificationUI(String code, String phone)
    {
        Code = code;
        Phone = phone;
    }

    @Override
    public void OnCreate()
    {
        final Button ButtonNext = new Button(GetActivity(), 16, false);
        final LoadingView LoadingViewNext = new LoadingView(GetActivity());

        RelativeLayoutMain = new RelativeLayout(GetActivity());
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

                if (DifferenceHeight > (ScreenHeight / 3) && DifferenceHeight != HeightDifference)
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
        RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, D56));
        RelativeLayoutHeader.setBackgroundResource(R.color.BlueLight);
        RelativeLayoutHeader.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain.addView(RelativeLayoutHeader);

        RelativeLayout.LayoutParams ImageViewBackParam = new RelativeLayout.LayoutParams(D56, D56);
        ImageViewBackParam.addRule(MiscHandler.Align("R"));

        ImageView ImageViewBack = new ImageView(GetActivity());
        ImageViewBack.setLayoutParams(ImageViewBackParam);
        ImageViewBack.setScaleType(ImageView.ScaleType.FIT_XY);
        ImageViewBack.setId(MiscHandler.GenerateViewID());
        ImageViewBack.setPadding(MiscHandler.ToDimension(GetActivity(), 12), MiscHandler.ToDimension(GetActivity(), 12), MiscHandler.ToDimension(GetActivity(), 12), MiscHandler.ToDimension(GetActivity(), 12));
        ImageViewBack.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { GetActivity().onBackPressed(); } });
        ImageViewBack.setImageResource(MiscHandler.IsRTL() ? R.drawable.ic_back_white_rtl : R.drawable.ic_back_white);

        RelativeLayoutHeader.addView(ImageViewBack);

        RelativeLayout.LayoutParams TextViewTitleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTitleParam.addRule(MiscHandler.AlignTo("R"), ImageViewBack.getId());
        TextViewTitleParam.addRule(RelativeLayout.CENTER_VERTICAL);

        TextView TextViewTitle = new TextView(GetActivity(), 18, true);
        TextViewTitle.setLayoutParams(TextViewTitleParam);
        TextViewTitle.setText(GetActivity().getString(R.string.SignUpPhoneVerification));

        RelativeLayoutHeader.addView(TextViewTitle);

        RelativeLayout.LayoutParams TextViewTimeParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTimeParam.addRule(RelativeLayout.CENTER_VERTICAL);
        TextViewTimeParam.addRule(MiscHandler.Align("L"));

        final TextView TextViewTime = new TextView(GetActivity(), 16, false);
        TextViewTime.setLayoutParams(TextViewTimeParam);
        TextViewTime.setPadding(MiscHandler.ToDimension(GetActivity(), 15), 0, MiscHandler.ToDimension(GetActivity(), 15), 0);

        RelativeLayoutHeader.addView(TextViewTime);

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

        RelativeLayout.LayoutParams TextViewVerificationCodeParam = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewVerificationCodeParam.addRule(MiscHandler.Align("R"));

        TextView TextViewVerificationCode = new TextView(GetActivity(), 18, true);
        TextViewVerificationCode.setLayoutParams(TextViewVerificationCodeParam);
        TextViewVerificationCode.setPadding(MiscHandler.ToDimension(GetActivity(), 20), MiscHandler.ToDimension(GetActivity(), 40), MiscHandler.ToDimension(GetActivity(), 20), MiscHandler.ToDimension(GetActivity(), 15));
        TextViewVerificationCode.setTextColor(ContextCompat.getColor(GetActivity(), R.color.Gray4));
        TextViewVerificationCode.setText(GetActivity().getString(R.string.SignUpPhoneVerificationCode));
        TextViewVerificationCode.setId(MiscHandler.GenerateViewID());

        RelativeLayoutScroll.addView(TextViewVerificationCode);

        RelativeLayout.LayoutParams LinearLayoutVerificationCodeParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LinearLayoutVerificationCodeParam.addRule(RelativeLayout.BELOW, TextViewVerificationCode.getId());

        LinearLayout LinearLayoutVerificationCode = new LinearLayout(GetActivity());
        LinearLayoutVerificationCode.setLayoutParams(LinearLayoutVerificationCodeParam);
        LinearLayoutVerificationCode.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayoutVerificationCode.setId(MiscHandler.GenerateViewID());

        RelativeLayoutScroll.addView(LinearLayoutVerificationCode);

        LinearLayout.LayoutParams EditTextVerificationCode1Param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        EditTextVerificationCode1Param.setMargins(MiscHandler.ToDimension(GetActivity(), 10), 0, MiscHandler.ToDimension(GetActivity(), 10), 0);

        EditTextVerificationCode1 = new EditText(GetActivity());
        EditTextVerificationCode1.setLayoutParams(EditTextVerificationCode1Param);
        EditTextVerificationCode1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        EditTextVerificationCode1.setTypeface(null, Typeface.BOLD);
        EditTextVerificationCode1.getBackground().setColorFilter(ContextCompat.getColor(GetActivity(), R.color.BlueLight), PorterDuff.Mode.SRC_ATOP);
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
        EditTextVerificationCode2Param.setMargins(MiscHandler.ToDimension(GetActivity(), 10), 0, MiscHandler.ToDimension(GetActivity(), 10), 0);

        EditTextVerificationCode2 = new EditText(GetActivity());
        EditTextVerificationCode2.setLayoutParams(EditTextVerificationCode2Param);
        EditTextVerificationCode2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        EditTextVerificationCode2.setTypeface(null, Typeface.BOLD);
        EditTextVerificationCode2.getBackground().setColorFilter(ContextCompat.getColor(GetActivity(), R.color.BlueLight), PorterDuff.Mode.SRC_ATOP);
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
        EditTextVerificationCode3Param.setMargins(MiscHandler.ToDimension(GetActivity(), 10), 0, MiscHandler.ToDimension(GetActivity(), 10), 0);

        EditTextVerificationCode3 = new EditText(GetActivity());
        EditTextVerificationCode3.setLayoutParams(EditTextVerificationCode3Param);
        EditTextVerificationCode3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        EditTextVerificationCode3.setTypeface(null, Typeface.BOLD);
        EditTextVerificationCode3.getBackground().setColorFilter(ContextCompat.getColor(GetActivity(), R.color.BlueLight), PorterDuff.Mode.SRC_ATOP);
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
        EditTextVerificationCode4Param.setMargins(MiscHandler.ToDimension(GetActivity(), 10), 0, MiscHandler.ToDimension(GetActivity(), 10), 0);

        EditTextVerificationCode4 = new EditText(GetActivity());
        EditTextVerificationCode4.setLayoutParams(EditTextVerificationCode4Param);
        EditTextVerificationCode4.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        EditTextVerificationCode4.setTypeface(null, Typeface.BOLD);
        EditTextVerificationCode4.getBackground().setColorFilter(ContextCompat.getColor(GetActivity(), R.color.BlueLight), PorterDuff.Mode.SRC_ATOP);
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
        EditTextVerificationCode5Param.setMargins(MiscHandler.ToDimension(GetActivity(), 10), 0, MiscHandler.ToDimension(GetActivity(), 10), 0);

        EditTextVerificationCode5 = new EditText(GetActivity());
        EditTextVerificationCode5.setLayoutParams(EditTextVerificationCode5Param);
        EditTextVerificationCode5.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        EditTextVerificationCode5.setTypeface(null, Typeface.BOLD);
        EditTextVerificationCode5.getBackground().setColorFilter(ContextCompat.getColor(GetActivity(), R.color.BlueLight), PorterDuff.Mode.SRC_ATOP);
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

        TextView TextViewMessage = new TextView(GetActivity(), 14, false);
        TextViewMessage.setLayoutParams(TextViewMessageParam);
        TextViewMessage.setTextColor(ContextCompat.getColor(GetActivity(), R.color.Black));
        TextViewMessage.setId(MiscHandler.GenerateViewID());
        TextViewMessage.setPadding(MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15));
        TextViewMessage.setMovementMethod(LinkMovementMethod.getInstance());
        TextViewMessage.setText(GetActivity().getString(R.string.SignUpPhoneVerificationMessage) + " " + (Code + Phone), TextView.BufferType.SPANNABLE);

        Spannable Span = (Spannable) TextViewMessage.getText();
        CharacterStyle CharacterStyleMessage = new CharacterStyle()
        {
            @Override
            public void updateDrawState(TextPaint t)
            {
                t.setColor(ContextCompat.getColor(GetActivity(), R.color.Gray7));
                t.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            }
        };
        Span.setSpan(CharacterStyleMessage, GetActivity().getString(R.string.SignUpPhoneVerificationMessage).length() + 1, Span.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        RelativeLayoutScroll.addView(TextViewMessage);

        RelativeLayout.LayoutParams RelativeLayoutBottomParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayoutBottomParam.addRule(RelativeLayout.BELOW, TextViewMessage.getId());

        RelativeLayout RelativeLayoutBottom = new RelativeLayout(GetActivity());
        RelativeLayoutBottom.setLayoutParams(RelativeLayoutBottomParam);

        RelativeLayoutScroll.addView(RelativeLayoutBottom);

        RelativeLayout.LayoutParams LoadingViewResendParam = new RelativeLayout.LayoutParams(D56, D56);
        LoadingViewResendParam.addRule(MiscHandler.Align("R"));

        final LoadingView LoadingViewResend = new LoadingView(GetActivity());
        LoadingViewResend.setLayoutParams(LoadingViewResendParam);

        RelativeLayoutBottom.addView(LoadingViewResend);

        RelativeLayout.LayoutParams TextViewResendParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewResendParam.addRule(RelativeLayout.CENTER_VERTICAL);
        TextViewResendParam.addRule(MiscHandler.Align("R"));

        final TextView TextViewResend = new TextView(GetActivity(), 14, false);
        TextViewResend.setLayoutParams(TextViewResendParam);
        TextViewResend.setTextColor(ContextCompat.getColor(GetActivity(), R.color.Gray7));
        TextViewResend.setText(GetActivity().getString(R.string.SignUpPhoneVerificationResend));
        TextViewResend.setPadding(MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15));
        TextViewResend.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (!TextViewResend.isEnabled())
                    return;

                TextViewResend.setVisibility(View.GONE);
                LoadingViewResend.Start();

                AndroidNetworking.post(MiscHandler.GetRandomServer("SignUpPhoneUI"))
                .addBodyParameter("Code", Code)
                .addBodyParameter("Phone", Phone)
                .setTag("SignUpPhoneVerificationUI")
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
                                    MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.GeneralError6));
                                    break;
                                case -2:
                                    MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.GeneralError2));
                                    break;
                                case -1:
                                    MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.GeneralError1));
                                    break;
                                case 0:
                                    CountDownTimerResend.start();
                                    MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.SignUpPhoneVerificationResendDone));
                                    break;
                                case 1:
                                case 2:
                                case 3:
                                    MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.SignUpPhoneCodeError));
                                    break;
                                case 4:
                                case 5:
                                case 6:
                                    MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.SignUpPhoneError));
                                    break;
                                case 7:
                                    MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.SignUpPhoneAlready));
                                    break;
                            }
                        }
                        catch (Exception e)
                        {
                            MiscHandler.Debug("SignUpPhoneVerificationUI-RequestSignUpPhone: " + e.toString());
                        }
                    }

                    @Override
                    public void onError(ANError e)
                    {
                        LoadingViewResend.Stop();
                        TextViewResend.setVisibility(View.VISIBLE);
                        MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.GeneralNoInternet));
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
                    TextViewResend.setTextColor(ContextCompat.getColor(GetActivity(), R.color.Gray7));
                }
            }

            @Override
            public void onFinish()
            {
                cancel();

                Enabled = true;

                TextViewResend.setEnabled(true);
                TextViewResend.setTextColor(ContextCompat.getColor(GetActivity(), R.color.BlueLight));
                TextViewTime.setText("");
            }
        };
        CountDownTimerResend.start();

        RelativeLayoutBottom.addView(TextViewResend);

        GradientDrawable DrawableNext = new GradientDrawable();
        DrawableNext.setColor(ContextCompat.getColor(GetActivity(), R.color.BlueLight));
        DrawableNext.setCornerRadius(MiscHandler.ToDimension(GetActivity(), 7));

        GradientDrawable DrawableNext2 = new GradientDrawable();
        DrawableNext2.setCornerRadius(MiscHandler.ToDimension(GetActivity(), 7));
        DrawableNext2.setColor(ContextCompat.getColor(GetActivity(), R.color.Gray2));

        StateListDrawable StateListNext = new StateListDrawable();
        StateListNext.addState(new int[] { android.R.attr.state_enabled }, DrawableNext);
        StateListNext.addState(new int[] { -android.R.attr.state_enabled }, DrawableNext2);

        RelativeLayout.LayoutParams RelativeLayoutNextParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(GetActivity(), 90), MiscHandler.ToDimension(GetActivity(), 35));
        RelativeLayoutNextParam.setMargins(MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15));
        RelativeLayoutNextParam.addRule(MiscHandler.Align("L"));

        RelativeLayout RelativeLayoutNext = new RelativeLayout(GetActivity());
        RelativeLayoutNext.setLayoutParams(RelativeLayoutNextParam);
        RelativeLayoutNext.setBackground(DrawableNext);

        RelativeLayoutBottom.addView(RelativeLayoutNext);

        ButtonNext.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(GetActivity(), 90), MiscHandler.ToDimension(GetActivity(), 35)));
        ButtonNext.setText(GetActivity().getString(R.string.SignUpPhoneNext));
        ButtonNext.setBackground(StateListNext);
        ButtonNext.setPadding(0, 0, 0, 0);
        ButtonNext.setEnabled(false);
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
                .setTag("SignUpPhoneVerificationUI")
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
                                    MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.GeneralError6));
                                    break;
                                case -2:
                                    MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.GeneralError2));
                                    break;
                                case -1:
                                    MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.GeneralError1));
                                    break;
                                case 0:
                                    TranslateAnimation Anim = MiscHandler.IsRTL() ? new TranslateAnimation(0f, -1000f, 0f, 0f) : new TranslateAnimation(0f, 1000f, 0f, 0f);
                                    Anim.setDuration(200);

                                    RelativeLayoutMain.setAnimation(Anim);

                                    GetActivity().GetManager().OpenView(new SignUpUsernameUI(VerifyCode, 1), R.id.WelcomeActivityContainer, "SignUpUsernameUI");
                                    break;
                                case 1:
                                case 2:
                                case 3:
                                    MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.SignUpPhoneCodeError));
                                    break;
                                case 4:
                                case 5:
                                case 6:
                                    MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.SignUpPhoneError));
                                    break;
                                case 7:
                                    MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.SignUpPhoneVerificationCodeEmpty));
                                    break;
                                case 8:
                                    MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.SignUpPhoneVerificationCodeCount));
                                    break;
                                case 9:
                                    MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.SignUpPhoneVerificationCodeWrong));
                                    break;
                            }
                        }
                        catch (Exception e)
                        {
                            MiscHandler.Debug("SignUpPhoneVerificationUI-RequestSignUpPhoneVerify: " + e.toString());
                        }
                    }

                    @Override
                    public void onError(ANError e)
                    {
                        LoadingViewNext.Stop();
                        ButtonNext.setVisibility(View.VISIBLE);
                        MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.GeneralNoInternet));
                    }
                });
            }
        });

        RelativeLayoutNext.addView(ButtonNext);

        RelativeLayout.LayoutParams LoadingViewNextParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(GetActivity(), 90), MiscHandler.ToDimension(GetActivity(), 35));
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
        GetActivity().registerReceiver(UpdateReceiver, new IntentFilter("co.biogram.main.ui.SignUpPhoneVerificationUI"));
    }

    @Override
    public void OnPause()
    {
        RelativeLayoutMain.getViewTreeObserver().removeOnGlobalLayoutListener(RelativeLayoutMainListener);
        AndroidNetworking.forceCancel("SignUpPhoneVerificationUI");

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
            if (intent.getAction().equalsIgnoreCase("co.biogram.main.ui.SignUpPhoneVerificationUI"))
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
