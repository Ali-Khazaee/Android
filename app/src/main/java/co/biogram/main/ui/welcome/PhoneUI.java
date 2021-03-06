package co.biogram.main.ui.welcome;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;

import org.json.JSONException;
import org.json.JSONObject;

import co.biogram.main.R;
import co.biogram.main.fragment.FragmentView;
import co.biogram.main.handler.Misc;
import co.biogram.main.service.NetworkService;
import co.biogram.main.ui.view.LoadingView;
import co.biogram.main.ui.view.PermissionDialog;
import co.biogram.main.ui.view.TextView;

class PhoneUI extends FragmentView {
    private final boolean IsSignUp;

    PhoneUI(boolean isSignUp) {
        IsSignUp = isSignUp;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void OnCreate() {

        View view = View.inflate(Activity, R.layout.welcome_phone, null);

        final Button ButtonNext = view.findViewById(R.id.buttonNextStep);
        final LoadingView LoadingViewNext = new LoadingView(Activity);

        InputMethodManager imm = (InputMethodManager) Activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

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

        final EditText EditTextPhoneCode = view.findViewById(R.id.editTextCode);
        EditTextPhoneCode.setText(CountryCode);
        Misc.SetCursorColor(EditTextPhoneCode, R.color.Primary);
        Misc.changeEditTextUnderlineColor(EditTextPhoneCode, R.color.Primary, R.color.Gray);
        EditTextPhoneCode.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
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
                    Misc.fontSetter(RelativeLayoutCode);
                    DialogCode.show();
                }

                return true;
            }
        });

        view.findViewById(R.id.ImageButtonBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity.onBackPressed();
            }
        });

        final EditText EditTextPhone = view.findViewById(R.id.EditTextPhone);
        Misc.SetCursorColor(EditTextPhone, R.color.Primary);
        Misc.changeEditTextUnderlineColor(EditTextPhone, R.color.Primary, R.color.Gray);
        EditTextPhone.requestFocus();
        EditTextPhone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16), new InputFilter() {
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
        }});
        EditTextPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence a, int b, int c, int d) {
            }

            @Override
            public void afterTextChanged(Editable a) {
            }

            @Override
            public void onTextChanged(CharSequence a, int b, int c, int d) {
                ButtonNext.setEnabled(a.length() > 9);
            }
        });

        android.widget.TextView TextViewMessage = view.findViewById(R.id.textViewHelp);
        TextViewMessage.setText((Misc.String(IsSignUp ? R.string.PhoneUIMessageUp : R.string.PhoneUIMessageIn) + " " + Misc.String(IsSignUp ? R.string.PhoneUIMessageUp2 : R.string.PhoneUIMessageIn2)), TextView.BufferType.SPANNABLE);

        TextViewMessage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                    if (!IsSignUp)
                        Activity.GetManager().OpenView(new UsernameUI(), "UsernameUI", true);
                    else
                        Activity.GetManager().OpenView(new EmailUI(), "EmailUI", true);

                return true;
            }
        });

        Spannable Span = (Spannable) TextViewMessage.getText();
        ClickableSpan ClickableSpanMessage = new ClickableSpan() {
            @Override
            public void onClick(View v) {

            }

            @Override
            public void updateDrawState(TextPaint t) {
                super.updateDrawState(t);
                t.setColor(ContextCompat.getColor(Activity, R.color.Primary));
                t.setUnderlineText(false);
            }
        };
        Span.setSpan(ClickableSpanMessage, Misc.String(IsSignUp ? R.string.PhoneUIMessageUp : R.string.PhoneUIMessageIn).length() + 1, Span.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        view.findViewById(R.id.textViewTerm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://biogram.co/")));
            }
        });

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

        ViewMain = view;
        Misc.fontSetter(view);
    }

    @Override
    public void OnPause() {
        Misc.HideSoftKey(Activity);
        AndroidNetworking.forceCancel("PhoneUI");
    }

    private void SendRequest(final Button ButtonNext, final LoadingView LoadingViewNext, final EditText EditTextPhone, final EditText EditTextPhoneCode) {
        ButtonNext.setEnabled(false);
        LoadingViewNext.Start();

        if (IsSignUp) {

            JSONObject signupObject = new JSONObject();
            try {
                signupObject.put("Country", EditTextPhoneCode.getText().toString());
                signupObject.put("Number", EditTextPhone.getText().toString());
                signupObject.put("Username", Misc.generateViewId());
            } catch (JSONException e) {
                e.printStackTrace();

            }

            NetworkService.Once(NetworkService.PACKET_SIGN_UP, new NetworkService.Listener() {
                @Override
                public void Call(String Message) {
                    try {
                        JSONObject result = new JSONObject(Message);
                        switch (result.getInt("Result")) {
                            case -1:
                            case -2:
                            case -3:
                                Toast.makeText(Activity, "Internal Server Error", Toast.LENGTH_LONG).show();
                                break;
                            case 1:
                                Toast.makeText(Activity, "Country Undefined", Toast.LENGTH_LONG).show();
                                break;
                            case 2:
                                Toast.makeText(Activity, "Number Undefined", Toast.LENGTH_LONG).show();
                                break;
                            case 3:
                                Toast.makeText(Activity, "Username Undefined", Toast.LENGTH_LONG).show();
                                break;
                            case 4:
                                Toast.makeText(Activity, "Not Allowed", Toast.LENGTH_LONG).show();
                                break;
                            case 5:
                                Toast.makeText(Activity, "Not Allowed", Toast.LENGTH_LONG).show();
                                break;
                            case 6:
                                Toast.makeText(Activity, "Username Already Used", Toast.LENGTH_LONG).show();
                                break;
                            case 7:
                                Toast.makeText(Activity, "Username Already Used", Toast.LENGTH_LONG).show();
                                break;

                            default:
                                Activity.GetManager().OpenView(new PhoneVerifyUI(EditTextPhoneCode.getText().toString(), EditTextPhone.getText().toString(), true), "PhoneVerfiyUI", true);


                        }
                        Log.d("MESSAGE", Message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            });
            NetworkService.Send(NetworkService.PACKET_SIGN_UP, signupObject.toString());

            ButtonNext.setEnabled(true);
        } else {

        }
    }
}
