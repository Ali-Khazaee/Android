package co.biogram.main.ui.welcome;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.*;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.*;
import co.biogram.main.R;
import co.biogram.main.fragment.FragmentView;
import co.biogram.main.handler.Misc;
import co.biogram.main.service.NetworkService;
import co.biogram.main.ui.view.LoadingView;
import co.biogram.main.ui.view.TextView;
import com.androidnetworking.AndroidNetworking;
import org.json.JSONException;
import org.json.JSONObject;

class PhoneUI extends FragmentView
{
    private final boolean IsSignUp;

    PhoneUI(boolean isSignUp)
    {
        IsSignUp = isSignUp;
    }

    @Override
    public void OnCreate()
    {

        View view = View.inflate(Activity, R.layout.welcome_phone, null);

        final Button ButtonNext = view.findViewById(R.id.buttonNextStep);
        final LoadingView LoadingViewNext = new LoadingView(Activity);

        TelephonyManager Telephony = (TelephonyManager) Activity.getSystemService(Context.TELEPHONY_SERVICE);
        String CountryCode = Telephony == null ? "" : Telephony.getNetworkCountryIso();

        if (Telephony != null && (CountryCode == null || CountryCode.equals("")))
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

        final EditText EditTextPhoneCode = view.findViewById(R.id.editTextCode);
        EditTextPhoneCode.setText(CountryCode);
        Misc.SetCursorColor(EditTextPhoneCode, R.color.Primary);
        EditTextPhoneCode.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                {
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
                    ImageViewClose.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
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
                    TextViewIran.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            DialogCode.dismiss();
                        }
                    });

                    LinearLayoutList.addView(TextViewIran);

                    DialogCode.setContentView(RelativeLayoutCode);
                    Misc.fontSetter(RelativeLayoutCode);
                    DialogCode.show();
                }

                return true;
            }
        });

        view.findViewById(R.id.ImageButtonBack).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Activity.onBackPressed();
            }
        });

        final EditText EditTextPhone = view.findViewById(R.id.EditTextPhone);
        Misc.SetCursorColor(EditTextPhone, R.color.Primary);
        EditTextPhone.requestFocus();
        EditTextPhone.setFilters(new InputFilter[] { new InputFilter.LengthFilter(16), new InputFilter()
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
        } });
        EditTextPhone.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence a, int b, int c, int d)
            {
            }

            @Override
            public void afterTextChanged(Editable a)
            {
            }

            @Override
            public void onTextChanged(CharSequence a, int b, int c, int d)
            {
                ButtonNext.setEnabled(a.length() > 9);
            }
        });

        android.widget.TextView TextViewMessage = view.findViewById(R.id.textViewHelp);
        TextViewMessage.setText((Misc.String(IsSignUp ? R.string.PhoneUIMessageUp : R.string.PhoneUIMessageIn) + " " + Misc.String(IsSignUp ? R.string.PhoneUIMessageUp2 : R.string.PhoneUIMessageIn2)), TextView.BufferType.SPANNABLE);

        Spannable Span = (Spannable) TextViewMessage.getText();
        ClickableSpan ClickableSpanMessage = new ClickableSpan()
        {
            @Override
            public void onClick(View v)
            {
                if (IsSignUp)
                    Activity.GetManager().OpenView(new UsernameUI(), null, true);
                else
                    Activity.GetManager().OpenView(new EmailSignInUI(), null, true);
            }

            @Override
            public void updateDrawState(TextPaint t)
            {
                super.updateDrawState(t);
                t.setColor(ContextCompat.getColor(Activity, R.color.Primary));
                t.setUnderlineText(false);
            }
        };
        Span.setSpan(ClickableSpanMessage, Misc.String(IsSignUp ? R.string.PhoneUIMessageUp : R.string.PhoneUIMessageIn).length() + 1, Span.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        view.findViewById(R.id.textViewTerm).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://biogram.co/")));
            }
        });

        ButtonNext.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Activity.GetManager().OpenView(new PhoneVerifyUI(EditTextPhoneCode.getText().toString(), EditTextPhone.getText().toString(), true), null, true);

                //                PermissionDialog PermissionDialogSMS = new PermissionDialog(Activity);
                //                PermissionDialogSMS.SetContentView(R.drawable.permission_sms_white, R.string.PhoneUIPermission, Manifest.permission.RECEIVE_SMS, new PermissionDialog.OnChoiceListener()
                //                {
                //                    @Override
                //                    public void OnChoice(boolean Allow)
                //                    {
                //                        SendRequest(ButtonNext, LoadingViewNext, EditTextPhone, EditTextPhoneCode);
                //                    }
                //                });
            }
        });

        ViewMain = view;
        Misc.fontSetter(view);
    }

    @Override
    public void OnPause()
    {
        Misc.HideSoftKey(Activity);
        AndroidNetworking.forceCancel("PhoneUI");
    }

    private void SendRequest(final Button ButtonNext, final LoadingView LoadingViewNext, final EditText EditTextPhone, final EditText EditTextPhoneCode)
    {
        ButtonNext.setEnabled(false);
        LoadingViewNext.Start();

        if (IsSignUp)
        {
            //            AndroidNetworking.post(Misc.GetRandomServer("SignUpPhone"))
            //                    .addBodyParameter("Issue", EditTextPhoneCode.getText().toString())
            //                    .addBodyParameter("Phone", EditTextPhone.getText().toString())
            //                    .setTag("PhoneUI")
            //                    .build()
            //                    .getAsString(new StringRequestListener() {
            //                        @Override
            //                        public void onResponse(String Response) {
            //                            LoadingViewNext.Stop();
            //                            ButtonNext.setEnabled(true);
            //
            //                            try {
            //                                JSONObject Result = new JSONObject(Response);
            //
            //                                switch (Result.getInt("Message")) {
            //                                    case 0:
            //
            //                                        String Phone = EditTextPhone.getText().toString();
            //
            //                                        while (Phone.charAt(0) == '0')
            //                                            Phone = Phone.substring(1);
            //
            //                                        Activity.GetManager().OpenView(new PhoneVerifyUI(EditTextPhoneCode.getText().toString(), Phone, true), "PhoneVerifyUI", true);
            //                                        break;
            //                                    case 1:
            //                                    case 2:
            //                                    case 3:
            //                                        Misc.ToastOld(Misc.String(R.string.GeneralPhoneCode));
            //                                        break;
            //                                    case 4:
            //                                    case 5:
            //                                    case 6:
            //                                        Misc.ToastOld(Misc.String(R.string.GeneralPhone));
            //                                        break;
            //                                    case 7:
            //                                        Misc.ToastOld(Misc.String(R.string.PhoneUIError));
            //                                        break;
            //                                    default:
            //                                        Misc.GeneralError(Result.getInt("Message"));
            //                                        break;
            //                                }
            //                            } catch (Exception e) {
            //                                Misc.Debug("PhoneUI-SignUpPhone: " + e.toString());
            //                            }
            //                        }
            //
            //                        @Override
            //                        public void onError(ANError e) {
            //                            LoadingViewNext.Stop();
            //                            ButtonNext.setEnabled(true);
            //
            //                            Misc.ToastOld(Misc.String(R.string.GeneralNoInternet));
            //                        }
            //                    });
            JSONObject signupObject = new JSONObject();
            try
            {
                signupObject.put("Country", EditTextPhoneCode.getText().toString());
                signupObject.put("Number", EditTextPhone.getText().toString());
                signupObject.put("Username", Misc.generateViewId());
            }
            catch (JSONException e)
            {
                e.printStackTrace();

            }

            NetworkService.Once(NetworkService.PACKET_SIGN_UP, new NetworkService.Listener()
            {
                @Override
                public void Call(String Message)
                {
                    Log.d("MESSAGE", Message);
                }
            });
            NetworkService.Send(NetworkService.PACKET_SIGN_UP, signupObject.toString());
            ButtonNext.setEnabled(true);
        }
        else
        {
            //            AndroidNetworking.post(Misc.GetRandomServer("SignInPhone"))
            //                    .addBodyParameter("Issue", EditTextPhoneCode.getText().toString())
            //                    .addBodyParameter("Phone", EditTextPhone.getText().toString())
            //                    .setTag("PhoneUI")
            //                    .build()
            //                    .getAsString(new StringRequestListener() {
            //                        @Override
            //                        public void onResponse(String Response) {
            //                            LoadingViewNext.Stop();
            //                            ButtonNext.setEnabled(true);
            //
            //
            //                            try {
            //                                JSONObject Result = new JSONObject(Response);
            //
            //                                switch (Result.getInt("Message")) {
            //                                    case 0:
            //
            //                                        String Phone = EditTextPhone.getText().toString();
            //
            //                                        while (Phone.charAt(0) == '0')
            //                                            Phone = Phone.substring(1);
            //
            //                                        Activity.GetManager().OpenView(new PhoneVerifyUI(EditTextPhoneCode.getText().toString(), Phone, false), "PhoneVerifyUI", true);
            //                                        break;
            //                                    case 1:
            //                                    case 2:
            //                                    case 3:
            //                                        Misc.ToastOld(Misc.String(R.string.GeneralPhoneCode));
            //                                        break;
            //                                    case 4:
            //                                    case 5:
            //                                    case 6:
            //                                        Misc.ToastOld(Misc.String(R.string.GeneralPhone));
            //                                        break;
            //                                    case 7:
            //                                        Misc.ToastOld(Misc.String(R.string.PhoneUIError2));
            //                                        break;
            //                                    default:
            //                                        Misc.GeneralError(Result.getInt("Message"));
            //                                        break;
            //                                }
            //                            } catch (Exception e) {
            //                                Misc.Debug("PhoneUI-SignInPhone: " + e.toString());
            //                            }
            //                        }
            //
            //                        @Override
            //                        public void onError(ANError e) {
            //                            LoadingViewNext.Stop();
            //                            ButtonNext.setEnabled(true);
            //                            Misc.ToastOld(Misc.String(R.string.GeneralNoInternet));
            //                        }
            //                    });
        }
    }
}
