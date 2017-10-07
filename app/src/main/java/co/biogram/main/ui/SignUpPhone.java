package co.biogram.main.ui;

import android.Manifest;
import android.app.Dialog;
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
import android.view.animation.Animation;
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

class SignUpPhone extends FragmentBase
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
        ImageViewBack.setImageResource(MiscHandler.IsFa() ? R.drawable.ic_back_white_fa : R.drawable.ic_back_white);

        RelativeLayoutHeader.addView(ImageViewBack);

        RelativeLayout.LayoutParams TextViewHeaderParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewHeaderParam.addRule(MiscHandler.AlignTo("R"), ImageViewBack.getId());
        TextViewHeaderParam.addRule(RelativeLayout.CENTER_VERTICAL);

        TextView TextViewTitle = new TextView(activity);
        TextViewTitle.setLayoutParams(TextViewHeaderParam);
        TextViewTitle.setTextColor(ContextCompat.getColor(activity, R.color.White));
        TextViewTitle.setText(activity.getString(R.string.SignUpPhone));
        TextViewTitle.setTypeface(null, Typeface.BOLD);
        TextViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

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

        LinearLayout LinearLayoutCode = new LinearLayout(activity);
        LinearLayoutCode.setLayoutParams(new LinearLayout.LayoutParams(MiscHandler.ToDimension(activity, 90), LinearLayout.LayoutParams.WRAP_CONTENT));
        LinearLayoutCode.setOrientation(LinearLayout.VERTICAL);
        LinearLayoutCode.setGravity(Gravity.CENTER_HORIZONTAL);
        LinearLayoutCode.setId(MiscHandler.GenerateViewID());

        RelativeLayoutScroll.addView(LinearLayoutCode);

        TextView TextViewPhoneCode = new TextView(activity);
        TextViewPhoneCode.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        TextViewPhoneCode.setPadding(0, MiscHandler.ToDimension(activity, 20), 0, 0);
        TextViewPhoneCode.setTextColor(ContextCompat.getColor(activity, R.color.Gray4));
        TextViewPhoneCode.setText(activity.getString(R.string.SignUpPhoneCode));
        TextViewPhoneCode.setTypeface(null, Typeface.BOLD);
        TextViewPhoneCode.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        TextViewPhoneCode.setId(MiscHandler.GenerateViewID());

        LinearLayoutCode.addView(TextViewPhoneCode);

        final EditText EditTextPhoneCode = new EditText(activity);
        EditTextPhoneCode.setLayoutParams(new LinearLayout.LayoutParams(MiscHandler.ToDimension(activity, 70), LinearLayout.LayoutParams.WRAP_CONTENT));
        EditTextPhoneCode.setId(MiscHandler.GenerateViewID());
        EditTextPhoneCode.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        EditTextPhoneCode.getBackground().setColorFilter(ContextCompat.getColor(activity, R.color.BlueLight), PorterDuff.Mode.SRC_ATOP);
        EditTextPhoneCode.setFocusable(false);
        EditTextPhoneCode.setText(MiscHandler.IsFa() ? "+98" : "+1"); // TODO Add More Country
        EditTextPhoneCode.setGravity(Gravity.CENTER_HORIZONTAL);
        EditTextPhoneCode.setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View view)
            {
                view.clearFocus();
            }
        });

        LinearLayoutCode.addView(EditTextPhoneCode);

        RelativeLayout.LayoutParams LinearLayoutPhoneParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LinearLayoutPhoneParam.addRule(RelativeLayout.RIGHT_OF, LinearLayoutCode.getId());

        LinearLayout LinearLayoutPhone = new LinearLayout(activity);
        LinearLayoutPhone.setLayoutParams(LinearLayoutPhoneParam);
        LinearLayoutPhone.setOrientation(LinearLayout.VERTICAL);

        RelativeLayoutScroll.addView(LinearLayoutPhone);

        TextView TextViewPhone = new TextView(activity);
        TextViewPhone.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        TextViewPhone.setPadding(0, MiscHandler.ToDimension(activity, 20), 0, 0);
        TextViewPhone.setTextColor(ContextCompat.getColor(activity, R.color.Gray4));
        TextViewPhone.setText(activity.getString(R.string.SignUpPhoneNumber));
        TextViewPhone.setTypeface(null, Typeface.BOLD);
        TextViewPhone.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        TextViewPhone.setId(MiscHandler.GenerateViewID());

        LinearLayoutPhone.addView(TextViewPhone);

        final EditText EditTextPhone = new EditText(activity);
        EditTextPhone.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        EditTextPhone.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        EditTextPhone.getBackground().setColorFilter(ContextCompat.getColor(activity, R.color.BlueLight), PorterDuff.Mode.SRC_ATOP);
        EditTextPhone.setInputType(InputType.TYPE_CLASS_PHONE);
        EditTextPhone.requestFocus();
        EditTextPhone.setFilters(new InputFilter[] { new InputFilter.LengthFilter(16) });
        EditTextPhone.addTextChangedListener(new TextWatcher()
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

        LinearLayoutPhone.addView(EditTextPhone);

        RelativeLayout.LayoutParams TextViewMessageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewMessageParam.addRule(RelativeLayout.BELOW, LinearLayoutCode.getId());
        TextViewMessageParam.addRule(MiscHandler.Align("R"));

        TextView TextViewMessage = new TextView(activity);
        TextViewMessage.setLayoutParams(TextViewMessageParam);
        TextViewMessage.setTextColor(ContextCompat.getColor(activity, R.color.Black));
        TextViewMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        TextViewMessage.setId(MiscHandler.GenerateViewID());
        TextViewMessage.setPadding(MiscHandler.ToDimension(activity, 15), MiscHandler.ToDimension(activity, 15), MiscHandler.ToDimension(activity, 15), MiscHandler.ToDimension(activity, 15));
        TextViewMessage.setMovementMethod(LinkMovementMethod.getInstance());
        TextViewMessage.setText(activity.getString(R.string.SignUpPhoneMessage) + " " + activity.getString(R.string.SignUpPhoneMessageEmail), TextView.BufferType.SPANNABLE);

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
                if (MiscHandler.HasPermission(activity, Manifest.permission.RECEIVE_SMS))
                {
                    SendRequest(activity, ButtonNext, LoadingViewNext, EditTextPhone, EditTextPhoneCode);
                    return;
                }

                DialogPermission DialogPermissionSMS = new DialogPermission(activity);
                DialogPermissionSMS.SetContentView(R.drawable.ic_permission_sms, "دسترسی به خوانده شدن پیامک را فعال کنید");
            }
        });

        RelativeLayoutNext.addView(ButtonNext);

        RelativeLayout.LayoutParams LoadingViewUsernameParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(activity, 90), MiscHandler.ToDimension(activity, 35));
        LoadingViewUsernameParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        LoadingViewNext.setLayoutParams(LoadingViewUsernameParam);
        LoadingViewNext.SetColor(R.color.White);

        RelativeLayoutNext.addView(LoadingViewNext);

        if (MiscHandler.IsRTL())
        {
            TextViewTitle.setTypeface(Typeface.createFromAsset(activity.getAssets(), "iran-sans.ttf"));

            TextViewPhoneCode.setTypeface(Typeface.createFromAsset(activity.getAssets(), "iran-sans.ttf"));

            TextViewPhone.setTypeface(Typeface.createFromAsset(activity.getAssets(), "iran-sans.ttf"));

            TextViewMessage.setTypeface(Typeface.createFromAsset(activity.getAssets(), "iran-sans.ttf"));

            TextViewPrivacy.setTypeface(Typeface.createFromAsset(activity.getAssets(), "iran-sans.ttf"));

            ButtonNext.setTypeface(Typeface.createFromAsset(activity.getAssets(), "iran-sans.ttf"));
        }

        TranslateAnimation Anim = MiscHandler.IsRTL() ? new TranslateAnimation(1000f, 0f, 0f, 0f) : new TranslateAnimation(-1000f, 0f, 0f, 0f);
        Anim.setDuration(300);
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
        AndroidNetworking.forceCancel("SignUpPhone");
        MiscHandler.HideSoftKey(GetActivity());
    }

    private void SendRequest(final FragmentActivity activity, final Button ButtonNext, final LoadingView LoadingViewNext, EditText EditTextPhone, EditText EditTextPhoneCode)
    {
        ButtonNext.setVisibility(View.GONE);
        LoadingViewNext.Start();

        AndroidNetworking.post(MiscHandler.GetRandomServer("SignUpPhone"))
        .addBodyParameter("Code", EditTextPhoneCode.getText().toString())
        .addBodyParameter("Phone", EditTextPhone.getText().toString())
        .setTag("SignUpPhone")
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
                            TranslateAnimation Anim = MiscHandler.IsRTL() ? new TranslateAnimation(0f, -1000f, 0f, 0f) : new TranslateAnimation(-100f, 0f, 0f, 0f);
                            Anim.setDuration(300);

                            RelativeLayoutMain.setAnimation(Anim);
                            GetActivity().GetManager().OpenView(new SignUpPhoneVerification(), R.id.WelcomeActivityContainer, "SignUpPhoneVerification");
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
                    MiscHandler.Debug("SignUpPhone-RequestSignUpPhone: " + e.toString());
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
}
