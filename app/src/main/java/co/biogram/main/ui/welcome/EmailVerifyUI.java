package co.biogram.main.ui.welcome;

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
import android.view.KeyEvent;
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

import co.biogram.main.fragment.FragmentBase;
import co.biogram.main.handler.Misc;
import co.biogram.main.R;
import co.biogram.main.ui.view.Button;
import co.biogram.main.ui.view.LoadingView;
import co.biogram.main.ui.view.TextView;

class EmailVerifyUI extends FragmentBase
{
    private ViewTreeObserver.OnGlobalLayoutListener LayoutListener;
    private RelativeLayout RelativeLayoutMain;
    private EditText EditTextCode1;
    private EditText EditTextCode2;
    private EditText EditTextCode3;
    private EditText EditTextCode4;
    private EditText EditTextCode5;
    private CountDownTimer CountDownTimerResend;
    private boolean Field1 = false;
    private boolean Field2 = false;
    private boolean Field3 = false;
    private boolean Field4 = false;
    private boolean Field5 = false;
    private final String Username;
    private final String Password;
    private final String Email;

    EmailVerifyUI(String username, String password, String email)
    {
        Username = username;
        Password = password;
        Email = email;
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
        ImageViewBack.setPadding(Misc.ToDP(GetActivity(), 12), Misc.ToDP(GetActivity(), 12), Misc.ToDP(GetActivity(), 12), Misc.ToDP(GetActivity(), 12));
        ImageViewBack.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { GetActivity().onBackPressed(); } });
        ImageViewBack.setImageResource(Misc.IsRTL() ? R.drawable.ic_back_white_rtl : R.drawable.ic_back_white);

        RelativeLayoutHeader.addView(ImageViewBack);

        RelativeLayout.LayoutParams TextViewTitleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTitleParam.addRule(Misc.AlignTo("R"), ImageViewBack.getId());
        TextViewTitleParam.addRule(RelativeLayout.CENTER_VERTICAL);

        TextView TextViewTitle = new TextView(GetActivity(), 16, true);
        TextViewTitle.setLayoutParams(TextViewTitleParam);
        TextViewTitle.setPadding(0, Misc.ToDP(GetActivity(), 6), 0, 0);
        TextViewTitle.setText(GetActivity().getString(R.string.EmailVerifyUI));

        RelativeLayoutHeader.addView(TextViewTitle);

        RelativeLayout.LayoutParams TextViewTimeParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTimeParam.addRule(RelativeLayout.CENTER_VERTICAL);
        TextViewTimeParam.addRule(Misc.Align("L"));

        final TextView TextViewTime = new TextView(GetActivity(), 16, false);
        TextViewTime.setLayoutParams(TextViewTimeParam);
        TextViewTime.setPadding(Misc.ToDP(GetActivity(), 15), 0, Misc.ToDP(GetActivity(), 15), 0);

        RelativeLayoutHeader.addView(TextViewTime);

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

        RelativeLayout.LayoutParams TextViewVerificationCodeParam = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewVerificationCodeParam.addRule(Misc.Align("R"));

        TextView TextViewVerificationCode = new TextView(GetActivity(), 16, false);
        TextViewVerificationCode.setLayoutParams(TextViewVerificationCodeParam);
        TextViewVerificationCode.setPadding(Misc.ToDP(GetActivity(), 20), Misc.ToDP(GetActivity(), 40), Misc.ToDP(GetActivity(), 20), Misc.ToDP(GetActivity(), 15));
        TextViewVerificationCode.setTextColor(ContextCompat.getColor(GetActivity(), R.color.Gray4));
        TextViewVerificationCode.setText(GetActivity().getString(R.string.EmailVerifyUICode));
        TextViewVerificationCode.setId(Misc.GenerateViewID());

        RelativeLayoutScroll.addView(TextViewVerificationCode);

        RelativeLayout.LayoutParams LinearLayoutVerificationCodeParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LinearLayoutVerificationCodeParam.addRule(RelativeLayout.BELOW, TextViewVerificationCode.getId());

        LinearLayout LinearLayoutVerificationCode = new LinearLayout(GetActivity());
        LinearLayoutVerificationCode.setLayoutParams(LinearLayoutVerificationCodeParam);
        LinearLayoutVerificationCode.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayoutVerificationCode.setId(Misc.GenerateViewID());

        RelativeLayoutScroll.addView(LinearLayoutVerificationCode);

        LinearLayout.LayoutParams EditTextVerificationCode1Param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        EditTextVerificationCode1Param.setMargins(Misc.ToDP(GetActivity(), 10), 0, Misc.ToDP(GetActivity(), 10), 0);

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

                ButtonNext.setEnabled(Field1 && Field2 && Field3 && Field4 && Field5);

                if (s.length() == 0)
                    return;

                EditText NextField = (EditText) EditTextCode1.focusSearch(View.FOCUS_RIGHT);
                NextField.requestFocus();
            }
        });

        LinearLayoutVerificationCode.addView(EditTextCode1);

        LinearLayout.LayoutParams EditTextVerificationCode2Param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        EditTextVerificationCode2Param.setMargins(Misc.ToDP(GetActivity(), 10), 0, Misc.ToDP(GetActivity(), 10), 0);

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

                ButtonNext.setEnabled(Field1 && Field2 && Field3 && Field4 && Field5);

                EditText NextField = (EditText) EditTextCode2.focusSearch(View.FOCUS_RIGHT);

                if (s.length() == 0)
                    NextField = (EditText) EditTextCode2.focusSearch(View.FOCUS_LEFT);

                NextField.requestFocus();
            }
        });
        EditTextCode2.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (keyCode == KeyEvent.KEYCODE_DEL && EditTextCode2.getText().length() == 0)
                {
                    EditText NextField = (EditText) EditTextCode2.focusSearch(View.FOCUS_LEFT);
                    NextField.requestFocus();
                }

                return false;
            }
        });

        LinearLayoutVerificationCode.addView(EditTextCode2);

        LinearLayout.LayoutParams EditTextVerificationCode3Param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        EditTextVerificationCode3Param.setMargins(Misc.ToDP(GetActivity(), 10), 0, Misc.ToDP(GetActivity(), 10), 0);

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

                ButtonNext.setEnabled(Field1 && Field2 && Field3 && Field4 && Field5);

                EditText NextField = (EditText) EditTextCode3.focusSearch(View.FOCUS_RIGHT);

                if (s.length() == 0)
                    NextField = (EditText) EditTextCode3.focusSearch(View.FOCUS_LEFT);

                NextField.requestFocus();
            }
        });
        EditTextCode3.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (keyCode == KeyEvent.KEYCODE_DEL && EditTextCode3.getText().length() == 0)
                {
                    EditText NextField = (EditText) EditTextCode3.focusSearch(View.FOCUS_LEFT);
                    NextField.requestFocus();
                }

                return false;
            }
        });

        LinearLayoutVerificationCode.addView(EditTextCode3);

        LinearLayout.LayoutParams EditTextVerificationCode4Param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        EditTextVerificationCode4Param.setMargins(Misc.ToDP(GetActivity(), 10), 0, Misc.ToDP(GetActivity(), 10), 0);

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

                ButtonNext.setEnabled(Field1 && Field2 && Field3 && Field4 && Field5);

                EditText NextField = (EditText) EditTextCode4.focusSearch(View.FOCUS_RIGHT);

                if (s.length() == 0)
                    NextField = (EditText) EditTextCode4.focusSearch(View.FOCUS_LEFT);

                NextField.requestFocus();
            }
        });
        EditTextCode4.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (keyCode == KeyEvent.KEYCODE_DEL && EditTextCode4.getText().length() == 0)
                {
                    EditText NextField = (EditText) EditTextCode4.focusSearch(View.FOCUS_LEFT);
                    NextField.requestFocus();
                }

                return false;
            }
        });

        LinearLayoutVerificationCode.addView(EditTextCode4);

        LinearLayout.LayoutParams EditTextVerificationCode5Param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        EditTextVerificationCode5Param.setMargins(Misc.ToDP(GetActivity(), 10), 0, Misc.ToDP(GetActivity(), 10), 0);

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

                ButtonNext.setEnabled(Field1 && Field2 && Field3 && Field4 && Field5);

                if (s.length() == 0)
                {
                    EditText NextField = (EditText) EditTextCode5.focusSearch(View.FOCUS_LEFT);
                    NextField.requestFocus();
                }
            }
        });
        EditTextCode5.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (keyCode == KeyEvent.KEYCODE_DEL && EditTextCode5.getText().length() == 0)
                {
                    EditText NextField = (EditText) EditTextCode5.focusSearch(View.FOCUS_LEFT);
                    NextField.requestFocus();
                }

                return false;
            }
        });

        LinearLayoutVerificationCode.addView(EditTextCode5);

        RelativeLayout.LayoutParams TextViewMessageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewMessageParam.addRule(RelativeLayout.BELOW, LinearLayoutVerificationCode.getId());
        TextViewMessageParam.addRule(Misc.Align("R"));

        TextView TextViewMessage = new TextView(GetActivity(), 14, false);
        TextViewMessage.setLayoutParams(TextViewMessageParam);
        TextViewMessage.setTextColor(ContextCompat.getColor(GetActivity(), R.color.Black));
        TextViewMessage.setId(Misc.GenerateViewID());
        TextViewMessage.setPadding(Misc.ToDP(GetActivity(), 15), Misc.ToDP(GetActivity(), 15), Misc.ToDP(GetActivity(), 15), Misc.ToDP(GetActivity(), 15));
        TextViewMessage.setMovementMethod(LinkMovementMethod.getInstance());
        TextViewMessage.setText((GetActivity().getString(R.string.EmailVerifyUIMessage) + " " + Email), TextView.BufferType.SPANNABLE);

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
        Span.setSpan(CharacterStyleMessage, GetActivity().getString(R.string.PhoneVerifyUIMessage).length() + 1, Span.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        RelativeLayoutScroll.addView(TextViewMessage);

        RelativeLayout.LayoutParams RelativeLayoutBottomParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayoutBottomParam.addRule(RelativeLayout.BELOW, TextViewMessage.getId());

        RelativeLayout RelativeLayoutBottom = new RelativeLayout(GetActivity());
        RelativeLayoutBottom.setLayoutParams(RelativeLayoutBottomParam);

        RelativeLayoutScroll.addView(RelativeLayoutBottom);

        RelativeLayout.LayoutParams LoadingViewResendParam = new RelativeLayout.LayoutParams(Misc.ToDP(GetActivity(), 56), Misc.ToDP(GetActivity(), 56));
        LoadingViewResendParam.addRule(Misc.Align("R"));

        final LoadingView LoadingViewResend = new LoadingView(GetActivity());
        LoadingViewResend.setLayoutParams(LoadingViewResendParam);

        RelativeLayoutBottom.addView(LoadingViewResend);

        RelativeLayout.LayoutParams TextViewResendParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewResendParam.addRule(RelativeLayout.CENTER_VERTICAL);
        TextViewResendParam.addRule(Misc.Align("R"));

        final TextView TextViewResend = new TextView(GetActivity(), 14, false);
        TextViewResend.setLayoutParams(TextViewResendParam);
        TextViewResend.setTextColor(ContextCompat.getColor(GetActivity(), R.color.Gray7));
        TextViewResend.setText(GetActivity().getString(R.string.EmailVerifyUIResend));
        TextViewResend.setPadding(Misc.ToDP(GetActivity(), 15), Misc.ToDP(GetActivity(), 15), Misc.ToDP(GetActivity(), 15), Misc.ToDP(GetActivity(), 15));
        TextViewResend.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (!TextViewResend.isEnabled())
                    return;

                TextViewResend.setVisibility(View.GONE);
                LoadingViewResend.Start();

                AndroidNetworking.post(Misc.GetRandomServer("SignUpEmail"))
                .addBodyParameter("Username", Username)
                .addBodyParameter("Password", Password)
                .addBodyParameter("Email", Email)
                .setTag("EmailVerifyUI")
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
                                    Misc.Toast(GetActivity(), GetActivity().getString(R.string.EmailVerifyUIResendDone));
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
                            Misc.Debug("EmailVerifyUI-SignUpEmail: " + e.toString());
                        }
                    }

                    @Override
                    public void onError(ANError e)
                    {
                        LoadingViewResend.Stop();
                        TextViewResend.setVisibility(View.VISIBLE);
                        Misc.Toast(GetActivity(), GetActivity().getString(R.string.GeneralNoInternet));
                    }
                });
            }
        });

        // TODO Memory Leak
        CountDownTimerResend = new CountDownTimer(120000, 1000)
        {
            private boolean Enabled = true;

            @Override
            public void onTick(long Counter)
            {
                long Min = (Counter / 1000) / 60;
                long Sec = (Counter / 1000) - (Min * 60);

                TextViewTime.setText(("0" + Min + ":" + (Sec < 9 ? "0" + String.valueOf(Sec) : String.valueOf(Sec))));

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

        GradientDrawable DrawableEnable = new GradientDrawable();
        DrawableEnable.setColor(ContextCompat.getColor(GetActivity(), R.color.BlueLight));
        DrawableEnable.setCornerRadius(Misc.ToDP(GetActivity(), 7));

        GradientDrawable DrawableDisable = new GradientDrawable();
        DrawableDisable.setCornerRadius(Misc.ToDP(GetActivity(), 7));
        DrawableDisable.setColor(ContextCompat.getColor(GetActivity(), R.color.Gray2));

        StateListDrawable StateListNext = new StateListDrawable();
        StateListNext.addState(new int[] { android.R.attr.state_enabled }, DrawableEnable);
        StateListNext.addState(new int[] { -android.R.attr.state_enabled }, DrawableDisable);

        RelativeLayout.LayoutParams RelativeLayoutNextParam = new RelativeLayout.LayoutParams(Misc.ToDP(GetActivity(), 90), Misc.ToDP(GetActivity(), 35));
        RelativeLayoutNextParam.setMargins(Misc.ToDP(GetActivity(), 15), Misc.ToDP(GetActivity(), 15), Misc.ToDP(GetActivity(), 15), Misc.ToDP(GetActivity(), 15));
        RelativeLayoutNextParam.addRule(Misc.Align("L"));

        RelativeLayout RelativeLayoutNext = new RelativeLayout(GetActivity());
        RelativeLayoutNext.setLayoutParams(RelativeLayoutNextParam);
        RelativeLayoutNext.setBackground(DrawableEnable);

        RelativeLayoutBottom.addView(RelativeLayoutNext);

        ButtonNext.setLayoutParams(new RelativeLayout.LayoutParams(Misc.ToDP(GetActivity(), 90), Misc.ToDP(GetActivity(), 35)));
        ButtonNext.setText(GetActivity().getString(R.string.GeneralNext));
        ButtonNext.setBackground(StateListNext);
        ButtonNext.setEnabled(false);
        ButtonNext.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ButtonNext.setVisibility(View.GONE);
                LoadingViewNext.Start();

                final String VerifyCode = EditTextCode1.getText().toString() + EditTextCode2.getText().toString() + EditTextCode3.getText().toString() + EditTextCode4.getText().toString() + EditTextCode5.getText().toString();

                AndroidNetworking.post(Misc.GetRandomServer("SignUpEmailVerify"))
                .addBodyParameter("VerifyCode", VerifyCode)
                .setTag("EmailVerifyUI")
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

                                    GetActivity().GetManager().OpenView(new DescriptionUI(VerifyCode), R.id.WelcomeActivityContainer, "DescriptionUI");
                                    break;
                                case 1:
                                case 2:
                                    Misc.Toast(GetActivity(), GetActivity().getString(R.string.EmailVerifyUICodeCount));
                                    break;
                                case 3:
                                    Misc.Toast(GetActivity(), GetActivity().getString(R.string.EmailVerifyUICodeWrong));
                                    break;
                                default:
                                    Misc.GeneralError(GetActivity(), Result.getInt("Message"));
                                    break;
                            }
                        }
                        catch (Exception e)
                        {
                            Misc.Debug("EmailVerifyUI-SignUpEmailVerify: " + e.toString());
                        }
                    }

                    @Override
                    public void onError(ANError e)
                    {
                        LoadingViewNext.Stop();
                        ButtonNext.setVisibility(View.VISIBLE);
                        Misc.Toast(GetActivity(), GetActivity().getString(R.string.GeneralNoInternet));
                    }
                });
            }
        });

        RelativeLayoutNext.addView(ButtonNext);

        RelativeLayout.LayoutParams LoadingViewNextParam = new RelativeLayout.LayoutParams(Misc.ToDP(GetActivity(), 90), Misc.ToDP(GetActivity(), 35));
        LoadingViewNextParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        LoadingViewNext.setLayoutParams(LoadingViewNextParam);
        LoadingViewNext.SetColor(R.color.White);

        RelativeLayoutNext.addView(LoadingViewNext);

        TranslateAnimation Anim = Misc.IsRTL() ? new TranslateAnimation(1000f, 0f, 0f, 0f) : new TranslateAnimation(-1000f, 0f, 0f, 0f);
        Anim.setDuration(200);

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
        AndroidNetworking.forceCancel("EmailVerifyUI");
        RelativeLayoutMain.getViewTreeObserver().removeOnGlobalLayoutListener(LayoutListener);
    }

    @Override
    public void OnDestroy()
    {
        CountDownTimerResend.cancel();
        super.OnDestroy();
    }
}
