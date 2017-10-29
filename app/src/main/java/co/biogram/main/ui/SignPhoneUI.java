package co.biogram.main.ui;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.json.JSONObject;

import java.lang.reflect.Field;

import co.biogram.fragment.FragmentActivity;
import co.biogram.fragment.FragmentBase;
import co.biogram.main.handler.MiscHandler;
import co.biogram.main.R;
import co.biogram.main.view.Button;
import co.biogram.main.view.LoadingView;
import co.biogram.main.view.PermissionDialog;
import co.biogram.main.view.TextView;

class SignPhoneUI extends FragmentBase
{
    private ViewTreeObserver.OnGlobalLayoutListener RelativeLayoutMainListener;
    private RelativeLayout RelativeLayoutMain;
    private int HeightDifference = 0;
    private final boolean IsSignUp;

    SignPhoneUI(boolean isSignUp)
    {
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
        RelativeLayoutHeader.setBackgroundResource(R.color.ColorPrimary);
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
        TextViewTitle.setText(GetActivity().getString(IsSignUp ? R.string.SignUpPhone : R.string.SignUpPhoneIn));

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

        final LinearLayout LinearLayoutCode = new LinearLayout(GetActivity());
        LinearLayoutCode.setLayoutParams(new LinearLayout.LayoutParams(MiscHandler.ToDimension(GetActivity(), 90), LinearLayout.LayoutParams.WRAP_CONTENT));
        LinearLayoutCode.setOrientation(LinearLayout.VERTICAL);
        LinearLayoutCode.setGravity(Gravity.CENTER_HORIZONTAL);
        LinearLayoutCode.setId(MiscHandler.GenerateViewID());

        RelativeLayoutScroll.addView(LinearLayoutCode);

        TextView TextViewPhoneCode = new TextView(GetActivity(), 16, true);
        TextViewPhoneCode.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        TextViewPhoneCode.setPadding(0, MiscHandler.ToDimension(GetActivity(), 20), 0, 0);
        TextViewPhoneCode.setTextColor(ContextCompat.getColor(GetActivity(), R.color.Gray4));
        TextViewPhoneCode.setText(GetActivity().getString(R.string.SignUpPhoneCode));

        LinearLayoutCode.addView(TextViewPhoneCode);

        TelephonyManager Telephony = (TelephonyManager) GetActivity().getSystemService(Context.TELEPHONY_SERVICE);
        String CountryCode = Telephony.getNetworkCountryIso();

        if (CountryCode == null || CountryCode.equals(""))
            CountryCode = Telephony.getSimCountryIso();

        switch (CountryCode)
        {
            case "ir":
                CountryCode = "+98";
                break;
            default:
                CountryCode = "+98";
                break;
        }

        final EditText EditTextPhoneCode = new EditText(GetActivity());
        EditTextPhoneCode.setLayoutParams(new LinearLayout.LayoutParams(MiscHandler.ToDimension(GetActivity(), 70), LinearLayout.LayoutParams.WRAP_CONTENT));
        EditTextPhoneCode.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        EditTextPhoneCode.getBackground().setColorFilter(ContextCompat.getColor(GetActivity(), R.color.ColorPrimary), PorterDuff.Mode.SRC_ATOP);
        EditTextPhoneCode.setFocusable(false);
        EditTextPhoneCode.setText(CountryCode);
        EditTextPhoneCode.setGravity(Gravity.CENTER_HORIZONTAL);
        EditTextPhoneCode.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                v.clearFocus();

                final Dialog DialogCode = new Dialog(GetActivity());
                DialogCode.requestWindowFeature(Window.FEATURE_NO_TITLE);
                DialogCode.setCancelable(false);

                ScrollView ScrollViewCode = new ScrollView(GetActivity());
                ScrollViewCode.setLayoutParams(new ScrollView.LayoutParams(ScrollView.LayoutParams.MATCH_PARENT, ScrollView.LayoutParams.MATCH_PARENT));
                ScrollViewCode.setFillViewport(true);

                RelativeLayout RelativeLayoutCode = new RelativeLayout(GetActivity());
                RelativeLayoutCode.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

                ScrollViewCode.addView(RelativeLayoutCode);

                RelativeLayout.LayoutParams TextViewTitleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, MiscHandler.ToDimension(GetActivity(), 56));
                TextViewTitleParam.addRule(MiscHandler.Align("R"));

                TextView TextViewTitle = new TextView(GetActivity(), 16, false);
                TextViewTitle.setLayoutParams(TextViewTitleParam);
                TextViewTitle.setTextColor(ContextCompat.getColor(GetActivity(), R.color.Black));
                TextViewTitle.setText(GetActivity().getString(R.string.SignUpPhoneTitleCode));
                TextViewTitle.setPadding(MiscHandler.ToDimension(GetActivity(), 10), 0, MiscHandler.ToDimension(GetActivity(), 10), 0);
                TextViewTitle.setGravity(Gravity.CENTER);

                RelativeLayoutCode.addView(TextViewTitle);

                RelativeLayout.LayoutParams ImageViewCloseParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(GetActivity(), 56), MiscHandler.ToDimension(GetActivity(), 56));
                ImageViewCloseParam.addRule(MiscHandler.Align("L"));

                ImageView ImageViewClose = new ImageView(GetActivity());
                ImageViewClose.setLayoutParams(ImageViewCloseParam);
                ImageViewClose.setImageResource(R.drawable.ic_close_blue);
                ImageViewClose.setPadding(MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15));
                ImageViewClose.setId(MiscHandler.GenerateViewID());
                ImageViewClose.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { DialogCode.dismiss(); } });

                RelativeLayoutCode.addView(ImageViewClose);

                RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(GetActivity(), 1));
                ViewLineParam.addRule(RelativeLayout.BELOW, ImageViewClose.getId());

                View ViewLine = new View(GetActivity());
                ViewLine.setLayoutParams(ViewLineParam);
                ViewLine.setId(MiscHandler.GenerateViewID());
                ViewLine.setBackgroundResource(R.color.Gray2);

                RelativeLayoutCode.addView(ViewLine);

                RelativeLayout.LayoutParams LinearLayoutListParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                LinearLayoutListParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

                LinearLayout LinearLayoutList = new LinearLayout(GetActivity());
                LinearLayoutList.setLayoutParams(LinearLayoutListParam);
                LinearLayoutList.setOrientation(LinearLayout.VERTICAL);

                RelativeLayoutCode.addView(LinearLayoutList);

                String Iran = "( 98+ ) ایران";

                TextView TextViewIran = new TextView(GetActivity(), 16, false);
                TextViewIran.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(GetActivity(), 56)));
                TextViewIran.setTextColor(ContextCompat.getColor(GetActivity(), R.color.Black));
                TextViewIran.setText(Iran);
                TextViewIran.setPadding(MiscHandler.ToDimension(GetActivity(), 10), 0, MiscHandler.ToDimension(GetActivity(), 10), 0);
                TextViewIran.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                TextViewIran.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { DialogCode.dismiss(); } });

                LinearLayoutList.addView(TextViewIran);

                DialogCode.setContentView(ScrollViewCode);
                DialogCode.show();
            }
        });

        LinearLayoutCode.addView(EditTextPhoneCode);

        RelativeLayout.LayoutParams LinearLayoutPhoneParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LinearLayoutPhoneParam.addRule(RelativeLayout.RIGHT_OF, LinearLayoutCode.getId());

        LinearLayout LinearLayoutPhone = new LinearLayout(GetActivity());
        LinearLayoutPhone.setLayoutParams(LinearLayoutPhoneParam);
        LinearLayoutPhone.setOrientation(LinearLayout.VERTICAL);

        RelativeLayoutScroll.addView(LinearLayoutPhone);

        TextView TextViewPhone = new TextView(GetActivity(), 16, true);
        TextViewPhone.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        TextViewPhone.setPadding(0, MiscHandler.ToDimension(GetActivity(), 20), 0, 0);
        TextViewPhone.setTextColor(ContextCompat.getColor(GetActivity(), R.color.Gray4));
        TextViewPhone.setText(GetActivity().getString(R.string.SignUpPhoneNumber));

        LinearLayoutPhone.addView(TextViewPhone);

        LinearLayout.LayoutParams EditTextPhoneParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        EditTextPhoneParam.setMargins(0, 0 , MiscHandler.ToDimension(GetActivity(), 15), 0);

        final EditText EditTextPhone = new EditText(GetActivity());
        EditTextPhone.setLayoutParams(EditTextPhoneParam);
        EditTextPhone.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        EditTextPhone.getBackground().setColorFilter(ContextCompat.getColor(GetActivity(), R.color.ColorPrimary), PorterDuff.Mode.SRC_ATOP);
        EditTextPhone.setInputType(InputType.TYPE_CLASS_PHONE);
        EditTextPhone.requestFocus();
        EditTextPhone.setFilters(new InputFilter[]
        {
            new InputFilter.LengthFilter(16), new InputFilter()
            {
                @Override
                public CharSequence filter(CharSequence s, int Start, int End, Spanned d, int ds, int de)
                {
                    if (End > Start)
                    {
                        char[] AllowChar = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

                        for (int I = Start; I < End; I++)
                        {
                            if (!new String(AllowChar).contains(String.valueOf(s.charAt(I))))
                            {
                                return "";
                            }
                        }
                    }

                    return null;
                }
            }
        });
        EditTextPhone.addTextChangedListener(new TextWatcher()
        {
            @Override public void beforeTextChanged(CharSequence a, int b, int c, int d) { }
            @Override public void afterTextChanged(Editable a) { }

            @Override
            public void onTextChanged(CharSequence a, int b, int c, int d)
            {
                if (a.length() > 7)
                    ButtonNext.setEnabled(true);
                else
                    ButtonNext.setEnabled(false);
            }
        });

        try
        {
            Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            f.set(EditTextPhone, R.color.ColorPrimary);
        }
        catch (Exception e)
        {
            MiscHandler.Debug("A: " + e);
        }

        LinearLayoutPhone.addView(EditTextPhone);

        RelativeLayout.LayoutParams TextViewMessageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewMessageParam.addRule(RelativeLayout.BELOW, LinearLayoutCode.getId());
        TextViewMessageParam.addRule(MiscHandler.Align("R"));

        TextView TextViewMessage = new TextView(GetActivity(), 14, false);
        TextViewMessage.setLayoutParams(TextViewMessageParam);
        TextViewMessage.setTextColor(ContextCompat.getColor(GetActivity(), R.color.Black));
        TextViewMessage.setId(MiscHandler.GenerateViewID());
        TextViewMessage.setMovementMethod(new LinkMovementMethod());
        TextViewMessage.setPadding(MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15));
        TextViewMessage.setText(GetActivity().getString(IsSignUp ? R.string.SignUpPhoneMessage : R.string.SignUpPhoneMessageIn) + " " + GetActivity().getString(IsSignUp ? R.string.SignUpPhoneMessageEmail : R.string.SignUpPhoneMessageIn2), TextView.BufferType.SPANNABLE);

        Spannable Span = (Spannable) TextViewMessage.getText();
        ClickableSpan ClickableSpanMessage = new ClickableSpan()
        {
            @Override
            public void onClick(View v)
            {
                TranslateAnimation Anim = MiscHandler.IsRTL() ? new TranslateAnimation(0f, -1000f, 0f, 0f) : new TranslateAnimation(0f, 1000f, 0f, 0f);
                Anim.setDuration(200);

                RelativeLayoutMain.setAnimation(Anim);

                if (IsSignUp)
                    GetActivity().GetManager().OpenView(new SignUpEmailUI(), R.id.WelcomeActivityContainer, "SignUpEmailUI");
                else
                    GetActivity().GetManager().OpenView(new SignInEmailUI(), R.id.WelcomeActivityContainer, "SignInEmailUI");
            }

            @Override
            public void updateDrawState(TextPaint t)
            {
                super.updateDrawState(t);
                t.setColor(ContextCompat.getColor(GetActivity(), R.color.ColorPrimary));
                t.setUnderlineText(false);
            }
        };
        Span.setSpan(ClickableSpanMessage, GetActivity().getString(R.string.SignUpPhoneMessage).length() + 1, Span.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        RelativeLayoutScroll.addView(TextViewMessage);

        RelativeLayout.LayoutParams RelativeLayoutBottomParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayoutBottomParam.addRule(RelativeLayout.BELOW, TextViewMessage.getId());

        RelativeLayout RelativeLayoutBottom = new RelativeLayout(GetActivity());
        RelativeLayoutBottom.setLayoutParams(RelativeLayoutBottomParam);

        RelativeLayoutScroll.addView(RelativeLayoutBottom);

        RelativeLayout.LayoutParams TextViewPrivacyParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewPrivacyParam.addRule(RelativeLayout.CENTER_VERTICAL);
        TextViewPrivacyParam.addRule(MiscHandler.Align("R"));

        TextView TextViewPrivacy = new TextView(GetActivity(), 14, false);
        TextViewPrivacy.setLayoutParams(TextViewPrivacyParam);
        TextViewPrivacy.setTextColor(ContextCompat.getColor(GetActivity(), R.color.ColorPrimary));
        TextViewPrivacy.setText(GetActivity().getString(R.string.SignUpPhoneTerm));
        TextViewPrivacy.setPadding(MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15));
        TextViewPrivacy.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { GetActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://biogram.co/"))); } });

        RelativeLayoutBottom.addView(TextViewPrivacy);

        GradientDrawable DrawableNext = new GradientDrawable();
        DrawableNext.setColor(ContextCompat.getColor(GetActivity(), R.color.ColorPrimary));
        DrawableNext.setCornerRadius(MiscHandler.ToDimension(GetActivity(), 7));

        GradientDrawable DrawableNext2 = new GradientDrawable();
        DrawableNext2.setCornerRadius(MiscHandler.ToDimension(GetActivity(), 7));
        DrawableNext2.setColor(ContextCompat.getColor(GetActivity(), R.color.Gray2));

        StateListDrawable StateNext = new StateListDrawable();
        StateNext.addState(new int[] { android.R.attr.state_enabled }, DrawableNext);
        StateNext.addState(new int[] { -android.R.attr.state_enabled }, DrawableNext2);

        RelativeLayout.LayoutParams RelativeLayoutNextParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(GetActivity(), 90), MiscHandler.ToDimension(GetActivity(), 35));
        RelativeLayoutNextParam.setMargins(MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15));
        RelativeLayoutNextParam.addRule(MiscHandler.Align("L"));

        RelativeLayout RelativeLayoutNext = new RelativeLayout(GetActivity());
        RelativeLayoutNext.setLayoutParams(RelativeLayoutNextParam);
        RelativeLayoutNext.setBackground(DrawableNext);

        RelativeLayoutBottom.addView(RelativeLayoutNext);

        ButtonNext.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(GetActivity(), 90), MiscHandler.ToDimension(GetActivity(), 35)));
        ButtonNext.setTextColor(ContextCompat.getColor(GetActivity(), R.color.White));
        ButtonNext.setText(GetActivity().getString(R.string.SignUpPhoneNext));
        ButtonNext.setBackground(StateNext);
        ButtonNext.setPadding(0, 0, 0, 0);
        ButtonNext.setEnabled(false);
        ButtonNext.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (MiscHandler.HasPermission(GetActivity(), Manifest.permission.RECEIVE_SMS))
                {
                    SendRequest(ButtonNext, LoadingViewNext, EditTextPhone, EditTextPhoneCode);
                    return;
                }

                PermissionDialog PermissionDialogSMS = new PermissionDialog(GetActivity());
                PermissionDialogSMS.SetContentView(R.drawable.ic_permission_sms, GetActivity().getString(R.string.SignUpPhonePermission), new PermissionDialog.OnSelectedListener()
                {
                    @Override
                    public void OnSelected(boolean Allow)
                    {
                        if (!Allow)
                        {
                            SendRequest(ButtonNext, LoadingViewNext, EditTextPhone, EditTextPhoneCode);
                            return;
                        }

                        GetActivity().RequestPermission(Manifest.permission.RECEIVE_SMS, new FragmentActivity.OnPermissionListener()
                        {
                            @Override
                            public void OnGranted()
                            {
                                SendRequest(ButtonNext, LoadingViewNext, EditTextPhone, EditTextPhoneCode);
                            }

                            @Override
                            public void OnDenied()
                            {
                                SendRequest(ButtonNext, LoadingViewNext, EditTextPhone, EditTextPhoneCode);
                            }
                        });
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
        Anim.setAnimationListener(new Animation.AnimationListener()
        {
            @Override public void onAnimationStart(Animation animation) { }
            @Override public void onAnimationRepeat(Animation animation) { }
            @Override public void onAnimationEnd(Animation animation) { MiscHandler.ShowSoftKey(EditTextPhone); }
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
        RelativeLayoutMain.getViewTreeObserver().removeOnGlobalLayoutListener(RelativeLayoutMainListener);
        AndroidNetworking.forceCancel("SignPhoneUI");
        MiscHandler.HideSoftKey(GetActivity());
    }

    private void SendRequest(final Button ButtonNext, final LoadingView LoadingViewNext, final EditText EditTextPhone, final EditText EditTextPhoneCode)
    {
        ButtonNext.setVisibility(View.GONE);
        LoadingViewNext.Start();

        if (IsSignUp)
        {
            AndroidNetworking.post(MiscHandler.GetRandomServer("SignUpPhone"))
            .addBodyParameter("Code", EditTextPhoneCode.getText().toString())
            .addBodyParameter("Phone", EditTextPhone.getText().toString())
            .setTag("SignPhoneUI")
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

                                String Phone = EditTextPhone.getText().toString();

                                while (Phone.charAt(0) == '0')
                                    Phone = Phone.substring(1);

                                GetActivity().GetManager().OpenView(new SignPhoneVerificationUI(EditTextPhoneCode.getText().toString(), Phone, true), R.id.WelcomeActivityContainer, "SignPhoneVerificationUI");
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
                        MiscHandler.Debug("SignPhoneUI-RequestSignUpPhone: " + e.toString());
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

        AndroidNetworking.post(MiscHandler.GetRandomServer("SignInPhone"))
        .addBodyParameter("Code", EditTextPhoneCode.getText().toString())
        .addBodyParameter("Phone", EditTextPhone.getText().toString())
        .setTag("SignPhoneUI")
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

                            String Phone = EditTextPhone.getText().toString();

                            while (Phone.charAt(0) == '0')
                                Phone = Phone.substring(1);

                            GetActivity().GetManager().OpenView(new SignPhoneVerificationUI(EditTextPhoneCode.getText().toString(), Phone, false), R.id.WelcomeActivityContainer, "SignPhoneVerificationUI");
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
                    MiscHandler.Debug("SignPhoneUI-RequestSignInPhone: " + e.toString());
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
}
