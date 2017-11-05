package co.biogram.main.ui.welcome;

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

import co.biogram.main.activity.SocialActivity;
import co.biogram.main.fragment.FragmentBase;
import co.biogram.main.handler.MiscHandler;
import co.biogram.main.R;
import co.biogram.main.handler.SharedHandler;
import co.biogram.main.ui.view.Button;
import co.biogram.main.ui.view.LoadingView;
import co.biogram.main.ui.view.TextView;

class SignPhoneVerificationUI extends FragmentBase
{
    private ViewTreeObserver.OnGlobalLayoutListener RelativeLayoutMainListener;
    private RelativeLayout RelativeLayoutMain;
    private EditText EditTextCode1;
    private EditText EditTextCode2;
    private EditText EditTextCode3;
    private EditText EditTextCode4;
    private EditText EditTextCode5;
    private CountDownTimer CountDownTimerResend;
    private int HeightDifference = 0;
    private boolean Field1 = false;
    private boolean Field2 = false;
    private boolean Field3 = false;
    private boolean Field4 = false;
    private boolean Field5 = false;
    private final String Code;
    private final String Phone;
    private final boolean IsSignUp;

    SignPhoneVerificationUI(String code, String phone, boolean isSignUp)
    {
        Code = code;
        Phone = phone;
        IsSignUp = isSignUp;
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
        ImageViewBack.setPadding(MiscHandler.ToDimension(GetActivity(), 12), MiscHandler.ToDimension(GetActivity(), 12), MiscHandler.ToDimension(GetActivity(), 12), MiscHandler.ToDimension(GetActivity(), 12));
        ImageViewBack.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { GetActivity().onBackPressed(); } });
        ImageViewBack.setImageResource(MiscHandler.IsRTL() ? R.drawable.ic_back_white_rtl : R.drawable.ic_back_white);

        RelativeLayoutHeader.addView(ImageViewBack);

        RelativeLayout.LayoutParams TextViewTitleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTitleParam.addRule(MiscHandler.AlignTo("R"), ImageViewBack.getId());
        TextViewTitleParam.addRule(RelativeLayout.CENTER_VERTICAL);

        TextView TextViewTitle = new TextView(GetActivity(), 16, true);
        TextViewTitle.setLayoutParams(TextViewTitleParam);
        TextViewTitle.setText(GetActivity().getString(R.string.SignUpPhoneVerify));

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

        TextView TextViewVerificationCode = new TextView(GetActivity(), 16, false);
        TextViewVerificationCode.setLayoutParams(TextViewVerificationCodeParam);
        TextViewVerificationCode.setPadding(MiscHandler.ToDimension(GetActivity(), 20), MiscHandler.ToDimension(GetActivity(), 40), MiscHandler.ToDimension(GetActivity(), 20), MiscHandler.ToDimension(GetActivity(), 15));
        TextViewVerificationCode.setTextColor(ContextCompat.getColor(GetActivity(), R.color.Gray4));
        TextViewVerificationCode.setText(GetActivity().getString(R.string.SignUpPhoneVerifyCode));
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

        EditTextCode1 = new EditText(GetActivity());
        EditTextCode1.setLayoutParams(EditTextVerificationCode1Param);
        EditTextCode1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        EditTextCode1.setTypeface(null, Typeface.BOLD);
        EditTextCode1.getBackground().setColorFilter(ContextCompat.getColor(GetActivity(), R.color.BlueLight), PorterDuff.Mode.SRC_ATOP);
        EditTextCode1.setGravity(Gravity.CENTER_HORIZONTAL);
        EditTextCode1.setInputType(InputType.TYPE_CLASS_PHONE);
        EditTextCode1.setFilters(new InputFilter[] { new InputFilter.LengthFilter(1) });
        EditTextCode1.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        EditTextCode1.addTextChangedListener(new TextWatcher()
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

                EditText NextField = (EditText) EditTextCode1.focusSearch(View.FOCUS_RIGHT);
                NextField.requestFocus();
            }
        });

        LinearLayoutVerificationCode.addView(EditTextCode1);

        LinearLayout.LayoutParams EditTextVerificationCode2Param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        EditTextVerificationCode2Param.setMargins(MiscHandler.ToDimension(GetActivity(), 10), 0, MiscHandler.ToDimension(GetActivity(), 10), 0);

        EditTextCode2 = new EditText(GetActivity());
        EditTextCode2.setLayoutParams(EditTextVerificationCode2Param);
        EditTextCode2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        EditTextCode2.setTypeface(null, Typeface.BOLD);
        EditTextCode2.getBackground().setColorFilter(ContextCompat.getColor(GetActivity(), R.color.BlueLight), PorterDuff.Mode.SRC_ATOP);
        EditTextCode2.setGravity(Gravity.CENTER_HORIZONTAL);
        EditTextCode2.setInputType(InputType.TYPE_CLASS_PHONE);
        EditTextCode2.setFilters(new InputFilter[] { new InputFilter.LengthFilter(1) });
        EditTextCode2.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        EditTextCode2.addTextChangedListener(new TextWatcher()
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

                EditText NextField = (EditText) EditTextCode2.focusSearch(View.FOCUS_RIGHT);

                if (s.length() == 0)
                    NextField = (EditText) EditTextCode2.focusSearch(View.FOCUS_LEFT);

                NextField.requestFocus();
            }
        });

        LinearLayoutVerificationCode.addView(EditTextCode2);

        LinearLayout.LayoutParams EditTextVerificationCode3Param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        EditTextVerificationCode3Param.setMargins(MiscHandler.ToDimension(GetActivity(), 10), 0, MiscHandler.ToDimension(GetActivity(), 10), 0);

        EditTextCode3 = new EditText(GetActivity());
        EditTextCode3.setLayoutParams(EditTextVerificationCode3Param);
        EditTextCode3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        EditTextCode3.setTypeface(null, Typeface.BOLD);
        EditTextCode3.getBackground().setColorFilter(ContextCompat.getColor(GetActivity(), R.color.BlueLight), PorterDuff.Mode.SRC_ATOP);
        EditTextCode3.setGravity(Gravity.CENTER_HORIZONTAL);
        EditTextCode3.setInputType(InputType.TYPE_CLASS_PHONE);
        EditTextCode3.setFilters(new InputFilter[] { new InputFilter.LengthFilter(1) });
        EditTextCode3.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        EditTextCode3.addTextChangedListener(new TextWatcher()
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

                EditText NextField = (EditText) EditTextCode3.focusSearch(View.FOCUS_RIGHT);

                if (s.length() == 0)
                    NextField = (EditText) EditTextCode3.focusSearch(View.FOCUS_LEFT);

                NextField.requestFocus();
            }
        });

        LinearLayoutVerificationCode.addView(EditTextCode3);

        LinearLayout.LayoutParams EditTextVerificationCode4Param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        EditTextVerificationCode4Param.setMargins(MiscHandler.ToDimension(GetActivity(), 10), 0, MiscHandler.ToDimension(GetActivity(), 10), 0);

        EditTextCode4 = new EditText(GetActivity());
        EditTextCode4.setLayoutParams(EditTextVerificationCode4Param);
        EditTextCode4.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        EditTextCode4.setTypeface(null, Typeface.BOLD);
        EditTextCode4.getBackground().setColorFilter(ContextCompat.getColor(GetActivity(), R.color.BlueLight), PorterDuff.Mode.SRC_ATOP);
        EditTextCode4.setGravity(Gravity.CENTER_HORIZONTAL);
        EditTextCode4.setInputType(InputType.TYPE_CLASS_PHONE);
        EditTextCode4.setFilters(new InputFilter[] { new InputFilter.LengthFilter(1) });
        EditTextCode4.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        EditTextCode4.addTextChangedListener(new TextWatcher()
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

                EditText NextField = (EditText) EditTextCode4.focusSearch(View.FOCUS_RIGHT);

                if (s.length() == 0)
                    NextField = (EditText) EditTextCode4.focusSearch(View.FOCUS_LEFT);

                NextField.requestFocus();
            }
        });

        LinearLayoutVerificationCode.addView(EditTextCode4);

        LinearLayout.LayoutParams EditTextVerificationCode5Param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        EditTextVerificationCode5Param.setMargins(MiscHandler.ToDimension(GetActivity(), 10), 0, MiscHandler.ToDimension(GetActivity(), 10), 0);

        EditTextCode5 = new EditText(GetActivity());
        EditTextCode5.setLayoutParams(EditTextVerificationCode5Param);
        EditTextCode5.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        EditTextCode5.setTypeface(null, Typeface.BOLD);
        EditTextCode5.getBackground().setColorFilter(ContextCompat.getColor(GetActivity(), R.color.BlueLight), PorterDuff.Mode.SRC_ATOP);
        EditTextCode5.setGravity(Gravity.CENTER_HORIZONTAL);
        EditTextCode5.setInputType(InputType.TYPE_CLASS_PHONE);
        EditTextCode5.setFilters(new InputFilter[] { new InputFilter.LengthFilter(1) });
        EditTextCode5.addTextChangedListener(new TextWatcher()
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
                    EditText NextField = (EditText) EditTextCode5.focusSearch(View.FOCUS_LEFT);
                    NextField.requestFocus();
                }
            }
        });

        LinearLayoutVerificationCode.addView(EditTextCode5);

        RelativeLayout.LayoutParams TextViewMessageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewMessageParam.addRule(RelativeLayout.BELOW, LinearLayoutVerificationCode.getId());
        TextViewMessageParam.addRule(MiscHandler.Align("R"));

        TextView TextViewMessage = new TextView(GetActivity(), 14, false);
        TextViewMessage.setLayoutParams(TextViewMessageParam);
        TextViewMessage.setTextColor(ContextCompat.getColor(GetActivity(), R.color.Black));
        TextViewMessage.setId(MiscHandler.GenerateViewID());
        TextViewMessage.setPadding(MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15));
        TextViewMessage.setMovementMethod(LinkMovementMethod.getInstance());
        TextViewMessage.setText(GetActivity().getString(R.string.SignUpPhoneVerifyMessage) + " " + (Code + Phone), TextView.BufferType.SPANNABLE);

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
        Span.setSpan(CharacterStyleMessage, GetActivity().getString(R.string.SignUpPhoneVerifyMessage).length() + 1, Span.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        RelativeLayoutScroll.addView(TextViewMessage);

        RelativeLayout.LayoutParams RelativeLayoutBottomParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayoutBottomParam.addRule(RelativeLayout.BELOW, TextViewMessage.getId());

        RelativeLayout RelativeLayoutBottom = new RelativeLayout(GetActivity());
        RelativeLayoutBottom.setLayoutParams(RelativeLayoutBottomParam);

        RelativeLayoutScroll.addView(RelativeLayoutBottom);

        RelativeLayout.LayoutParams LoadingViewResendParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(GetActivity(), 56), MiscHandler.ToDimension(GetActivity(), 56));
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
        TextViewResend.setText(GetActivity().getString(R.string.SignUpPhoneVerifyResend));
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

                if (IsSignUp)
                {
                    AndroidNetworking.post(MiscHandler.GetRandomServer("SignUpPhone"))
                    .addBodyParameter("Code", Code)
                    .addBodyParameter("Phone", Phone)
                    .setTag("SignPhoneVerificationUI")
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
                                    case 0:
                                        CountDownTimerResend.start();
                                        MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.SignUpPhoneVerifyResendDone));
                                        break;
                                    case 1:
                                    case 2:
                                    case 3:
                                        MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.SignUpPhoneErrorCountry));
                                        break;
                                    case 4:
                                    case 5:
                                    case 6:
                                        MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.SignUpPhoneErrorPhone));
                                        break;
                                    case 7:
                                        MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.SignUpPhoneErrorAlready));
                                        break;
                                    default:
                                        MiscHandler.GeneralError(GetActivity(), Result.getInt("Message"));
                                        break;
                                }
                            }
                            catch (Exception e)
                            {
                                MiscHandler.Debug("SignPhoneVerificationUI-SignUpPhone: " + e.toString());
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

                    return;
                }

                AndroidNetworking.post(MiscHandler.GetRandomServer("SignInPhone"))
                .addBodyParameter("Code", Code)
                .addBodyParameter("Phone", Phone)
                .setTag("SignPhoneVerificationUI")
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
                                case 0:
                                    CountDownTimerResend.start();
                                    MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.SignUpPhoneVerifyResendDone));
                                    break;
                                case 1:
                                case 2:
                                case 3:
                                    MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.SignUpPhoneErrorCountry));
                                    break;
                                case 4:
                                case 5:
                                case 6:
                                    MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.SignUpPhoneErrorPhone));
                                    break;
                                case 7:
                                    MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.SignUpPhoneErrorNotFound));
                                    break;
                                default:
                                    MiscHandler.GeneralError(GetActivity(), Result.getInt("Message"));
                                    break;
                            }
                        }
                        catch (Exception e)
                        {
                            MiscHandler.Debug("SignPhoneVerificationUI-SignInPhone: " + e.toString());
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

                final String VerifyCode = EditTextCode1.getText().toString() + EditTextCode2.getText().toString() + EditTextCode3.getText().toString() + EditTextCode4.getText().toString() + EditTextCode5.getText().toString();

                if (IsSignUp)
                {
                    AndroidNetworking.post(MiscHandler.GetRandomServer("SignUpPhoneVerify"))
                    .addBodyParameter("Code", Code)
                    .addBodyParameter("Phone", Phone)
                    .addBodyParameter("VerifyCode", VerifyCode)
                    .setTag("SignPhoneVerificationUI")
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
                                        TranslateAnimation Anim = MiscHandler.IsRTL() ? new TranslateAnimation(0f, -1000f, 0f, 0f) : new TranslateAnimation(0f, 1000f, 0f, 0f);
                                        Anim.setDuration(200);

                                        RelativeLayoutMain.setAnimation(Anim);

                                        GetActivity().GetManager().OpenView(new SignUpUsernameUI(VerifyCode, 1), R.id.WelcomeActivityContainer, "SignUpUsernameUI");
                                        break;
                                    case 1:
                                    case 2:
                                    case 3:
                                        MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.SignUpPhoneErrorCountry));
                                        break;
                                    case 4:
                                    case 5:
                                    case 6:
                                        MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.SignUpPhoneErrorPhone));
                                        break;
                                    case 7:
                                        MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.SignUpPhoneVerifyCodeEmpty));
                                        break;
                                    case 8:
                                        MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.SignUpPhoneVerifyCodeCount));
                                        break;
                                    case 9:
                                        MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.SignUpPhoneVerifyCodeWrong));
                                        break;
                                    default:
                                        MiscHandler.GeneralError(GetActivity(), Result.getInt("Message"));
                                        break;
                                }
                            }
                            catch (Exception e)
                            {
                                MiscHandler.Debug("SignPhoneVerificationUI-SignUpPhoneVerify: " + e.toString());
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

                    return;
                }

                AndroidNetworking.post(MiscHandler.GetRandomServer("SignInPhoneVerify"))
                .addBodyParameter("Code", Code)
                .addBodyParameter("Phone", Phone)
                .addBodyParameter("VerifyCode", VerifyCode)
                .setTag("SignPhoneVerificationUI")
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
                                    TranslateAnimation Anim = MiscHandler.IsRTL() ? new TranslateAnimation(0f, -1000f, 0f, 0f) : new TranslateAnimation(0f, 1000f, 0f, 0f);
                                    Anim.setDuration(200);

                                    RelativeLayoutMain.setAnimation(Anim);

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
                                case 2:
                                case 3:
                                    MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.SignUpPhoneErrorCountry));
                                    break;
                                case 4:
                                case 5:
                                case 6:
                                    MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.SignUpPhoneErrorPhone));
                                    break;
                                case 7:
                                    MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.SignUpPhoneVerifyCodeEmpty));
                                    break;
                                case 8:
                                    MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.SignUpPhoneVerifyCodeCount));
                                    break;
                                case 9:
                                    MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.SignUpPhoneVerifyCodeWrong));
                                    break;
                                case 10:
                                    MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.SignUpPhoneVerifyCodeNotFound));
                                    break;
                                default:
                                    MiscHandler.GeneralError(GetActivity(), Result.getInt("Message"));
                                    break;
                            }
                        }
                        catch (Exception e)
                        {
                            MiscHandler.Debug("SignPhoneVerificationUI-SignInPhoneVerify: " + e.toString());
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

        Intent i = new Intent("Biogram.SMS.Request");
        i.putExtra("SetWaiting", true);

        GetActivity().sendBroadcast(i);
        GetActivity().registerReceiver(VerifyReceiver, new IntentFilter("Biogram.SMS.Verify"));
    }

    @Override
    public void OnPause()
    {
        RelativeLayoutMain.getViewTreeObserver().removeOnGlobalLayoutListener(RelativeLayoutMainListener);
        AndroidNetworking.forceCancel("SignPhoneVerificationUI");

        Intent i = new Intent("Biogram.SMS.Request");
        i.putExtra("SetWaiting", false);

        GetActivity().sendBroadcast(i);
        GetActivity().unregisterReceiver(VerifyReceiver);
    }

    @Override
    public void OnDestroy()
    {
        CountDownTimerResend.cancel();
        super.OnDestroy();
    }

    private final BroadcastReceiver VerifyReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (intent.getAction().equalsIgnoreCase("Biogram.SMS.Verify"))
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
                        EditTextCode1.setText(Separated[0]);
                        EditTextCode2.setText(Separated[1]);
                        EditTextCode3.setText(Separated[2]);
                        EditTextCode4.setText(Separated[3]);
                        EditTextCode5.setText(Separated[4]);
                    }
                }, 0);
            }
        }
    };
}
