package co.biogram.main.ui.welcome;

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

import co.biogram.main.fragment.FragmentView;
import co.biogram.main.handler.Misc;
import co.biogram.main.R;
import co.biogram.main.ui.view.Button;
import co.biogram.main.ui.view.LoadingView;
import co.biogram.main.ui.view.PermissionDialog;
import co.biogram.main.ui.view.TextView;

class PhoneUI extends FragmentView {
    private final boolean IsSignUp;
    private ViewTreeObserver.OnGlobalLayoutListener LayoutListener;
    private RelativeLayout RelativeLayoutMain;

    PhoneUI(boolean isSignUp) {
        IsSignUp = isSignUp;
    }

    @Override
    public void OnCreate() {
        final Button ButtonNext = new Button(Activity, 16, false);
        final LoadingView LoadingViewNext = new LoadingView(Activity);

        RelativeLayoutMain = new RelativeLayout(Activity);
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayoutMain.setBackgroundResource(R.color.TextDark);
        RelativeLayoutMain.setClickable(true);

        LayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            int HeightDifference = 0;

            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                RelativeLayoutMain.getWindowVisibleDisplayFrame(rect);

                int ScreenHeight = RelativeLayoutMain.getHeight();
                int DifferenceHeight = ScreenHeight - (rect.bottom - rect.top);

                if (DifferenceHeight > (ScreenHeight / 3) && DifferenceHeight != HeightDifference) {
                    RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ScreenHeight - DifferenceHeight));
                    HeightDifference = DifferenceHeight;
                } else if (DifferenceHeight != HeightDifference) {
                    RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ScreenHeight));
                    HeightDifference = DifferenceHeight;
                } else if (HeightDifference != 0) {
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
        ImageViewBack.setPadding(Misc.ToDP(12), Misc.ToDP(12), Misc.ToDP(12), Misc.ToDP(12));
        ImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity.onBackPressed();
            }
        });
        ImageViewBack.setImageResource(Misc.IsRTL() ? R.drawable.z_general_back_white : R.drawable.z_general_back_white);

        RelativeLayoutHeader.addView(ImageViewBack);

        RelativeLayout.LayoutParams TextViewTitleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTitleParam.addRule(Misc.AlignTo("R"), ImageViewBack.getId());
        TextViewTitleParam.addRule(RelativeLayout.CENTER_VERTICAL);

        TextView TextViewTitle = new TextView(Activity, 16, true);
        TextViewTitle.setLayoutParams(TextViewTitleParam);
        TextViewTitle.setText(Misc.String(IsSignUp ? R.string.GeneralSignUp : R.string.GeneralSignIn));
        TextViewTitle.setPadding(0, Misc.ToDP(6), 0, 0);

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

        LinearLayout LinearLayoutCode = new LinearLayout(Activity);
        LinearLayoutCode.setLayoutParams(new LinearLayout.LayoutParams(Misc.ToDP(90), LinearLayout.LayoutParams.WRAP_CONTENT));
        LinearLayoutCode.setOrientation(LinearLayout.VERTICAL);
        LinearLayoutCode.setGravity(Gravity.CENTER_HORIZONTAL);
        LinearLayoutCode.setId(Misc.generateViewId());

        RelativeLayoutScroll.addView(LinearLayoutCode);

        TextView TextViewPhoneCode = new TextView(Activity, 14, false);
        TextViewPhoneCode.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        TextViewPhoneCode.setPadding(0, Misc.ToDP(20), 0, 0);
        TextViewPhoneCode.SetColor(R.color.Gray);
        TextViewPhoneCode.setText(Misc.String(R.string.PhoneUICode));

        LinearLayoutCode.addView(TextViewPhoneCode);

        TelephonyManager Telephony = (TelephonyManager) Activity.getSystemService(Context.TELEPHONY_SERVICE);
        String CountryCode = Telephony == null ? "" : Telephony.getNetworkCountryIso();

        if (Telephony != null && (CountryCode == null || CountryCode.equals("")))
            CountryCode = Telephony.getSimCountryIso();

        switch (CountryCode) {
            case "ir":
                CountryCode = "+98";
                break;
            default:
                CountryCode = "+98";
                break;
        }

        final EditText EditTextPhoneCode = new EditText(Activity);
        EditTextPhoneCode.setLayoutParams(new LinearLayout.LayoutParams(Misc.ToDP(70), LinearLayout.LayoutParams.WRAP_CONTENT));
        EditTextPhoneCode.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        EditTextPhoneCode.getBackground().setColorFilter(ContextCompat.getColor(Activity, R.color.Primary), PorterDuff.Mode.SRC_ATOP);
        EditTextPhoneCode.setFocusable(false);
        EditTextPhoneCode.setText(CountryCode);
        EditTextPhoneCode.setGravity(Gravity.CENTER_HORIZONTAL);
        EditTextPhoneCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog DialogCode = new Dialog(Activity);
                DialogCode.requestWindowFeature(Window.FEATURE_NO_TITLE);
                DialogCode.setCancelable(true);

                RelativeLayout RelativeLayoutCode = new RelativeLayout(Activity);
                RelativeLayoutCode.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ScrollView.LayoutParams.MATCH_PARENT));

                RelativeLayout.LayoutParams TextViewTitleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, Misc.ToDP(56));
                TextViewTitleParam.addRule(Misc.Align("R"));

                TextView TextViewTitle = new TextView(Activity, 16, false);
                TextViewTitle.setLayoutParams(TextViewTitleParam);
                TextViewTitle.SetColor(R.color.TextWhite);
                TextViewTitle.setText(Misc.String(R.string.PhoneUIPreCode));
                TextViewTitle.setPadding(Misc.ToDP(10), 0, Misc.ToDP(10), 0);
                TextViewTitle.setGravity(Gravity.CENTER);

                RelativeLayoutCode.addView(TextViewTitle);

                RelativeLayout.LayoutParams ImageViewCloseParam = new RelativeLayout.LayoutParams(Misc.ToDP(56), Misc.ToDP(56));
                ImageViewCloseParam.addRule(Misc.Align("L"));

                ImageView ImageViewClose = new ImageView(Activity);
                ImageViewClose.setLayoutParams(ImageViewCloseParam);
                ImageViewClose.setImageResource(R.drawable.close_blue);
                ImageViewClose.setPadding(Misc.ToDP(9), Misc.ToDP(9), Misc.ToDP(9), Misc.ToDP(9));
                ImageViewClose.setId(Misc.generateViewId());
                ImageViewClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogCode.dismiss();
                    }
                });

                RelativeLayoutCode.addView(ImageViewClose);

                RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1));
                ViewLineParam.addRule(RelativeLayout.BELOW, ImageViewClose.getId());

                View ViewLine = new View(Activity);
                ViewLine.setLayoutParams(ViewLineParam);
                ViewLine.setId(Misc.generateViewId());
                ViewLine.setBackgroundResource(R.color.Gray);

                RelativeLayoutCode.addView(ViewLine);

                RelativeLayout.LayoutParams ScrollViewCodeParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                ScrollViewCodeParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

                ScrollView ScrollViewCode = new ScrollView(Activity);
                ScrollViewCode.setLayoutParams(ScrollViewCodeParam);

                RelativeLayoutCode.addView(ScrollViewCode);

                LinearLayout LinearLayoutList = new LinearLayout(Activity);
                LinearLayoutList.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                LinearLayoutList.setOrientation(LinearLayout.VERTICAL);

                ScrollViewCode.addView(LinearLayoutList);

                String Iran = "( 98+ ) ایران";

                TextView TextViewIran = new TextView(Activity, 16, false);
                TextViewIran.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56)));
                TextViewIran.SetColor(R.color.TextWhite);
                TextViewIran.setText(Iran);
                TextViewIran.setPadding(Misc.ToDP(10), 0, Misc.ToDP(10), 0);
                TextViewIran.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                TextViewIran.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogCode.dismiss();
                    }
                });

                LinearLayoutList.addView(TextViewIran);

                DialogCode.setContentView(RelativeLayoutCode);
                DialogCode.show();
            }
        });

        LinearLayoutCode.addView(EditTextPhoneCode);

        RelativeLayout.LayoutParams LinearLayoutPhoneParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LinearLayoutPhoneParam.addRule(RelativeLayout.RIGHT_OF, LinearLayoutCode.getId());

        LinearLayout LinearLayoutPhone = new LinearLayout(Activity);
        LinearLayoutPhone.setLayoutParams(LinearLayoutPhoneParam);
        LinearLayoutPhone.setOrientation(LinearLayout.VERTICAL);

        RelativeLayoutScroll.addView(LinearLayoutPhone);

        TextView TextViewPhone = new TextView(Activity, 14, false);
        TextViewPhone.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        TextViewPhone.setPadding(0, Misc.ToDP(20), 0, 0);
        TextViewPhone.SetColor(R.color.Gray);
        TextViewPhone.setText(Misc.String(R.string.PhoneUINumber));

        LinearLayoutPhone.addView(TextViewPhone);

        LinearLayout.LayoutParams EditTextPhoneParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        EditTextPhoneParam.setMargins(0, 0, Misc.ToDP(15), 0);

        final EditText EditTextPhone = new EditText(Activity);
        EditTextPhone.setLayoutParams(EditTextPhoneParam);
        EditTextPhone.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        EditTextPhone.getBackground().setColorFilter(ContextCompat.getColor(Activity, R.color.Primary), PorterDuff.Mode.SRC_ATOP);
        EditTextPhone.setInputType(InputType.TYPE_CLASS_PHONE);
        EditTextPhone.requestFocus();
        EditTextPhone.setFilters(new InputFilter[]
                {
                        new InputFilter.LengthFilter(16), new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence s, int Start, int End, Spanned d, int ds, int de) {
                        if (End > Start) {
                            char[] AllowChar = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

                            for (int I = Start; I < End; I++) {
                                if (!new String(AllowChar).contains(String.valueOf(s.charAt(I)))) {
                                    return "";
                                }
                            }
                        }

                        return null;
                    }
                }
                });
        EditTextPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence a, int b, int c, int d) {
            }

            @Override
            public void afterTextChanged(Editable a) {
            }

            @Override
            public void onTextChanged(CharSequence a, int b, int c, int d) {
                ButtonNext.setEnabled(a.length() > 7);
            }
        });

        LinearLayoutPhone.addView(EditTextPhone);

        RelativeLayout.LayoutParams TextViewMessageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewMessageParam.addRule(RelativeLayout.BELOW, LinearLayoutCode.getId());
        TextViewMessageParam.addRule(Misc.Align("R"));

        TextView TextViewMessage = new TextView(Activity, 14, false);
        TextViewMessage.setLayoutParams(TextViewMessageParam);
        TextViewMessage.SetColor(R.color.TextWhite);
        TextViewMessage.setId(Misc.generateViewId());
        TextViewMessage.setMovementMethod(new LinkMovementMethod());
        TextViewMessage.setPadding(Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15));
        TextViewMessage.setText((Misc.String(IsSignUp ? R.string.PhoneUIMessageUp : R.string.PhoneUIMessageIn) + " " + Misc.String(IsSignUp ? R.string.PhoneUIMessageUp2 : R.string.PhoneUIMessageIn2)), TextView.BufferType.SPANNABLE);

        Spannable Span = (Spannable) TextViewMessage.getText();
        ClickableSpan ClickableSpanMessage = new ClickableSpan() {
            @Override
            public void onClick(View v) {
                TranslateAnimation Anim = Misc.IsRTL() ? new TranslateAnimation(0f, -1000f, 0f, 0f) : new TranslateAnimation(0f, 1000f, 0f, 0f);
                Anim.setDuration(200);

                RelativeLayoutMain.setAnimation(Anim);

                if (IsSignUp)
                    Activity.GetManager().OpenView(new UsernameUI(), "UsernameUI", true);
                else
                    Activity.GetManager().OpenView(new EmailSignInUI(), "EmailSignInUI", true);
            }

            @Override
            public void updateDrawState(TextPaint t) {
                super.updateDrawState(t);
                t.setColor(ContextCompat.getColor(Activity, R.color.Primary));
                t.setUnderlineText(false);
            }
        };
        Span.setSpan(ClickableSpanMessage, Misc.String(IsSignUp ? R.string.PhoneUIMessageUp : R.string.PhoneUIMessageIn).length() + 1, Span.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        RelativeLayoutScroll.addView(TextViewMessage);

        RelativeLayout.LayoutParams RelativeLayoutBottomParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayoutBottomParam.addRule(RelativeLayout.BELOW, TextViewMessage.getId());

        RelativeLayout RelativeLayoutBottom = new RelativeLayout(Activity);
        RelativeLayoutBottom.setLayoutParams(RelativeLayoutBottomParam);

        RelativeLayoutScroll.addView(RelativeLayoutBottom);

        RelativeLayout.LayoutParams TextViewPrivacyParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewPrivacyParam.addRule(RelativeLayout.CENTER_VERTICAL);
        TextViewPrivacyParam.addRule(Misc.Align("R"));

        TextView TextViewPrivacy = new TextView(Activity, 14, false);
        TextViewPrivacy.setLayoutParams(TextViewPrivacyParam);
        TextViewPrivacy.SetColor(R.color.Primary);
        TextViewPrivacy.setText(Misc.String(R.string.GeneralTerm));
        TextViewPrivacy.setPadding(Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15));
        TextViewPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://biogram.co/")));
            }
        });

        RelativeLayoutBottom.addView(TextViewPrivacy);

        GradientDrawable DrawableEnable = new GradientDrawable();
        DrawableEnable.setColor(ContextCompat.getColor(Activity, R.color.Primary));
        DrawableEnable.setCornerRadius(Misc.ToDP(7));

        GradientDrawable DrawableDisable = new GradientDrawable();
        DrawableDisable.setCornerRadius(Misc.ToDP(7));
        DrawableDisable.setColor(ContextCompat.getColor(Activity, R.color.Gray));

        StateListDrawable DrawableStateNext = new StateListDrawable();
        DrawableStateNext.addState(new int[]{android.R.attr.state_enabled}, DrawableEnable);
        DrawableStateNext.addState(new int[]{-android.R.attr.state_enabled}, DrawableDisable);

        RelativeLayout.LayoutParams RelativeLayoutNextParam = new RelativeLayout.LayoutParams(Misc.ToDP(90), Misc.ToDP(35));
        RelativeLayoutNextParam.setMargins(Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15));
        RelativeLayoutNextParam.addRule(Misc.Align("L"));

        RelativeLayout RelativeLayoutNext = new RelativeLayout(Activity);
        RelativeLayoutNext.setLayoutParams(RelativeLayoutNextParam);
        RelativeLayoutNext.setBackground(DrawableStateNext);

        RelativeLayoutBottom.addView(RelativeLayoutNext);

        ButtonNext.setLayoutParams(new RelativeLayout.LayoutParams(Misc.ToDP(90), Misc.ToDP(35)));
        ButtonNext.setText(Misc.String(R.string.GeneralNext));
        ButtonNext.setBackground(DrawableStateNext);
        ButtonNext.setEnabled(false);
        ButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionDialog PermissionDialogSMS = new PermissionDialog(Activity);
                PermissionDialogSMS.SetContentView(R.drawable.permission_sms_white, R.string.PhoneUIPermission, Manifest.permission.RECEIVE_SMS, new PermissionDialog.OnChoiceListener() {
                    @Override
                    public void OnChoice(boolean Allow) {
                        SendRequest(ButtonNext, LoadingViewNext, EditTextPhone, EditTextPhoneCode);
                    }
                });
            }
        });

        RelativeLayoutNext.addView(ButtonNext);

        RelativeLayout.LayoutParams LoadingViewNextParam = new RelativeLayout.LayoutParams(Misc.ToDP(90), Misc.ToDP(35));
        LoadingViewNextParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        LoadingViewNext.setLayoutParams(LoadingViewNextParam);
        LoadingViewNext.SetColor(R.color.TextDark);

        RelativeLayoutNext.addView(LoadingViewNext);

        TranslateAnimation Anim = Misc.IsRTL() ? new TranslateAnimation(1000f, 0f, 0f, 0f) : new TranslateAnimation(-1000f, 0f, 0f, 0f);
        Anim.setDuration(200);
        Anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Misc.ShowSoftKey(EditTextPhone);
            }
        });

        RelativeLayoutMain.startAnimation(Anim);

        ViewMain = RelativeLayoutMain;
    }

    @Override
    public void OnResume() {
        RelativeLayoutMain.getViewTreeObserver().addOnGlobalLayoutListener(LayoutListener);
    }

    @Override
    public void OnPause() {
        Misc.HideSoftKey(Activity);
        AndroidNetworking.forceCancel("PhoneUI");
        RelativeLayoutMain.getViewTreeObserver().removeOnGlobalLayoutListener(LayoutListener);
    }

    private void SendRequest(final Button ButtonNext, final LoadingView LoadingViewNext, final EditText EditTextPhone, final EditText EditTextPhoneCode) {
        ButtonNext.setVisibility(View.GONE);
        LoadingViewNext.Start();

        if (IsSignUp) {
            AndroidNetworking.post(Misc.GetRandomServer("SignUpPhone"))
                    .addBodyParameter("Issue", EditTextPhoneCode.getText().toString())
                    .addBodyParameter("Phone", EditTextPhone.getText().toString())
                    .setTag("PhoneUI")
                    .build()
                    .getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String Response) {
                            LoadingViewNext.Stop();
                            ButtonNext.setVisibility(View.VISIBLE);

                            try {
                                JSONObject Result = new JSONObject(Response);

                                switch (Result.getInt("Message")) {
                                    case 0:
                                        TranslateAnimation Anim = Misc.IsRTL() ? new TranslateAnimation(0f, -1000f, 0f, 0f) : new TranslateAnimation(0f, 1000f, 0f, 0f);
                                        Anim.setDuration(200);

                                        RelativeLayoutMain.setAnimation(Anim);

                                        String Phone = EditTextPhone.getText().toString();

                                        while (Phone.charAt(0) == '0')
                                            Phone = Phone.substring(1);

                                        Activity.GetManager().OpenView(new PhoneVerifyUI(EditTextPhoneCode.getText().toString(), Phone, true), "PhoneVerifyUI", true);
                                        break;
                                    case 1:
                                    case 2:
                                    case 3:
                                        Misc.ToastOld(Misc.String(R.string.GeneralPhoneCode));
                                        break;
                                    case 4:
                                    case 5:
                                    case 6:
                                        Misc.ToastOld(Misc.String(R.string.GeneralPhone));
                                        break;
                                    case 7:
                                        Misc.ToastOld(Misc.String(R.string.PhoneUIError));
                                        break;
                                    default:
                                        Misc.GeneralError(Result.getInt("Message"));
                                        break;
                                }
                            } catch (Exception e) {
                                Misc.Debug("PhoneUI-SignUpPhone: " + e.toString());
                            }
                        }

                        @Override
                        public void onError(ANError e) {
                            LoadingViewNext.Stop();
                            ButtonNext.setVisibility(View.VISIBLE);
                            Misc.ToastOld(Misc.String(R.string.GeneralNoInternet));
                        }
                    });
        } else {
            AndroidNetworking.post(Misc.GetRandomServer("SignInPhone"))
                    .addBodyParameter("Issue", EditTextPhoneCode.getText().toString())
                    .addBodyParameter("Phone", EditTextPhone.getText().toString())
                    .setTag("PhoneUI")
                    .build()
                    .getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String Response) {
                            LoadingViewNext.Stop();
                            ButtonNext.setVisibility(View.VISIBLE);

                            try {
                                JSONObject Result = new JSONObject(Response);

                                switch (Result.getInt("Message")) {
                                    case 0:
                                        TranslateAnimation Anim = Misc.IsRTL() ? new TranslateAnimation(0f, -1000f, 0f, 0f) : new TranslateAnimation(0f, 1000f, 0f, 0f);
                                        Anim.setDuration(200);

                                        RelativeLayoutMain.setAnimation(Anim);

                                        String Phone = EditTextPhone.getText().toString();

                                        while (Phone.charAt(0) == '0')
                                            Phone = Phone.substring(1);

                                        Activity.GetManager().OpenView(new PhoneVerifyUI(EditTextPhoneCode.getText().toString(), Phone, false), "PhoneVerifyUI", true);
                                        break;
                                    case 1:
                                    case 2:
                                    case 3:
                                        Misc.ToastOld(Misc.String(R.string.GeneralPhoneCode));
                                        break;
                                    case 4:
                                    case 5:
                                    case 6:
                                        Misc.ToastOld(Misc.String(R.string.GeneralPhone));
                                        break;
                                    case 7:
                                        Misc.ToastOld(Misc.String(R.string.PhoneUIError2));
                                        break;
                                    default:
                                        Misc.GeneralError(Result.getInt("Message"));
                                        break;
                                }
                            } catch (Exception e) {
                                Misc.Debug("PhoneUI-SignInPhone: " + e.toString());
                            }
                        }

                        @Override
                        public void onError(ANError e) {
                            LoadingViewNext.Stop();
                            ButtonNext.setVisibility(View.VISIBLE);
                            Misc.ToastOld(Misc.String(R.string.GeneralNoInternet));
                        }
                    });
        }
    }
}
