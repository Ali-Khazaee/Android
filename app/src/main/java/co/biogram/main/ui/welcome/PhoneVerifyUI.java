package co.biogram.main.ui.welcome;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;

import org.json.JSONException;
import org.json.JSONObject;

import co.biogram.main.R;
import co.biogram.main.fragment.FragmentView;
import co.biogram.main.handler.Misc;
import co.biogram.main.service.NetworkService;
import co.biogram.main.ui.view.LoadingView;

class PhoneVerifyUI extends FragmentView
{
    private final String Code;
    private final String Phone;
    private final boolean IsSignUp;
    private EditText EditTextCode1;
    private EditText EditTextCode2;
    private EditText EditTextCode3;
    private EditText EditTextCode4;
    private EditText EditTextCode5;
    private final BroadcastReceiver VerifyReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (intent.getAction() == null || intent.getExtras() == null)
                return;

            if (intent.getAction().equalsIgnoreCase("Biogram.SMS.Verify"))
            {
                String VerifyCode = intent.getExtras().getString("Issue", "");

                if (VerifyCode.length() < 4)
                    return;

                final String[] Separated = VerifyCode.split("(?!^)");

                Misc.UIThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        EditTextCode1.setText(Separated[ 0 ]);
                        EditTextCode2.setText(Separated[ 1 ]);
                        EditTextCode3.setText(Separated[ 2 ]);
                        EditTextCode4.setText(Separated[ 3 ]);
                        EditTextCode5.setText(Separated[ 4 ]);
                    }
                }, 0);
            }
        }
    };
    private CountDownTimer CountDownTimerResend;
    private boolean Field1 = false;
    private boolean Field2 = false;
    private boolean Field3 = false;
    private boolean Field4 = false;
    private boolean Field5 = false;

    PhoneVerifyUI(String code, String phone, boolean isSignUp)
    {
        Code = code;
        Phone = phone;
        IsSignUp = isSignUp;
    }

    @Override
    public void OnCreate()
    {
        View view = View.inflate(Activity, R.layout.welcome_phone_verify, null);
        final Button ButtonNext = view.findViewById(R.id.buttonNextStep);
        final LoadingView LoadingViewNext = new LoadingView(Activity);

        view.findViewById(R.id.ImageButtonBack).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Activity.onBackPressed();
            }
        });

        InputMethodManager imm = (InputMethodManager) Activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        EditTextCode1 = view.findViewById(R.id.editTextCode1);
        EditTextCode1.requestFocus();
        Misc.SetCursorColor(EditTextCode1, R.color.Primary);
        EditTextCode1.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
            }

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

        EditTextCode2 = view.findViewById(R.id.editTextCode2);
        Misc.SetCursorColor(EditTextCode2, R.color.Primary);
        EditTextCode2.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
            }

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
                    EditText NextField = (EditText) EditTextCode2.focusSearch(View.FOCUS_RIGHT);
                    NextField.requestFocus();
                }

                return false;
            }
        });

        EditTextCode3 = view.findViewById(R.id.editTextCode3);
        Misc.SetCursorColor(EditTextCode3, R.color.Primary);
        EditTextCode3.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
            }

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

        EditTextCode4 = view.findViewById(R.id.editTextCode4);
        Misc.SetCursorColor(EditTextCode4, R.color.Primary);
        EditTextCode4.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
            }

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

        EditTextCode5 = view.findViewById(R.id.editTextCode5);
        Misc.SetCursorColor(EditTextCode4, R.color.Primary);
        EditTextCode5.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                Field5 = (s.length() != 0);

                ButtonNext.setEnabled(Field1 && Field2 && Field3 && Field4 && Field5);

                if (s.length() == 0)
                {
                    EditText NextField = (EditText) EditTextCode5.focusSearch(View.FOCUS_RIGHT);
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
                    EditText NextField = (EditText) EditTextCode5.focusSearch(View.FOCUS_RIGHT);
                    NextField.requestFocus();
                }

                return false;
            }
        });

        ((android.widget.TextView) view.findViewById(R.id.textViewPhone)).setText(String.format("%s %s", Code, Phone));

        Misc.changeEditTextUnderlineColor(EditTextCode1, R.color.Primary, R.color.Gray);
        Misc.changeEditTextUnderlineColor(EditTextCode2, R.color.Primary, R.color.Gray);
        Misc.changeEditTextUnderlineColor(EditTextCode3, R.color.Primary, R.color.Gray);
        Misc.changeEditTextUnderlineColor(EditTextCode4, R.color.Primary, R.color.Gray);
        Misc.changeEditTextUnderlineColor(EditTextCode5, R.color.Primary, R.color.Gray);

        final TextView TextViewResend = view.findViewById(R.id.textViewResendCode);
        Misc.SetCursorColor(EditTextCode4, R.color.Primary);


        final TextView TextViewTime = view.findViewById(R.id.textViewTime);
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
                    //TextViewResend.SetColor(R.color.Gray);
                }
            }

            @Override
            public void onFinish()
            {
                cancel();
                Enabled = true;
                TextViewResend.setEnabled(true);
                // TextViewResend.SetColor(R.color.Primary);
                TextViewTime.setText("");
            }
        };
        CountDownTimerResend.start();

        ButtonNext.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ButtonNext.setEnabled(false);
                LoadingViewNext.Start();

                final String VerifyCode = EditTextCode1.getText().toString() + EditTextCode2.getText().toString() + EditTextCode3.getText().toString() + EditTextCode4.getText().toString() + EditTextCode5.getText().toString();

                if (IsSignUp)
                {

                    JSONObject signupObject = new JSONObject();
                    try
                    {
                        signupObject.put("Code", VerifyCode);
                        signupObject.put("Number", Phone);
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                    NetworkService.Send(NetworkService.PACKET_SIGN_UP, signupObject.toString());
                    NetworkService.Once(NetworkService.PACKET_SIGN_UP, new NetworkService.Listener()
                    {
                        @Override
                        public void Call(String Message)
                        {
                            Log.d("MESSAGE", Message);
                        }
                    });

                    Activity.GetManager().OpenView(new UsernameUI(VerifyCode, 1), "UsernameUI", true);

                    //                    AndroidNetworking.post(Misc.GetRandomServer("SignUpPhoneVerify")).addBodyParameter("Issue", Code).addBodyParameter("Phone", Phone).addBodyParameter("VerifyCode", VerifyCode).setTag("PhoneVerifyUI").build().getAsString(new StringRequestListener()
                    //                    {
                    //                        @Override
                    //                        public void onResponse(String Response)
                    //                        {
                    //                            LoadingViewNext.Stop();
                    //                            ButtonNext.setEnabled(true);
                    //
                    //                            try
                    //                            {
                    //                                JSONObject Result = new JSONObject(Response);
                    //
                    //                                switch (Result.getInt("Message"))
                    //                                {
                    //                                    case 0:
                    //
                    //                                        Activity.GetManager().OpenView(new UsernameUI(VerifyCode, 1), "UsernameUI", true);
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
                    //                                    case 8:
                    //                                        Misc.ToastOld(Misc.String(R.string.PhoneVerifyUICodeCount));
                    //                                        break;
                    //                                    case 9:
                    //                                        Misc.ToastOld(Misc.String(R.string.PhoneVerifyUICodeWrong));
                    //                                        break;
                    //                                    default:
                    //                                        Misc.GeneralError(Result.getInt("Message"));
                    //                                        break;
                    //                                }
                    //                            }
                    //                            catch (Exception e)
                    //                            {
                    //                                Misc.Debug("PhoneVerifyUI-SignUpPhoneVerify: " + e.toString());
                    //                            }
                    //                        }
                    //
                    //                        @Override
                    //                        public void onError(ANError e)
                    //                        {
                    //                            LoadingViewNext.Stop();
                    //                            ButtonNext.setEnabled(true);
                    //                            Misc.ToastOld(Misc.String(R.string.GeneralNoInternet));
                    //                        }
                    //                    });
                }
                //                else
                //                {
                //                    AndroidNetworking.post(Misc.GetRandomServer("SignInPhoneVerify")).addBodyParameter("Issue", Code).addBodyParameter("Phone", Phone).addBodyParameter("VerifyCode", VerifyCode).addBodyParameter("Session", Misc.GenerateSession()).setTag("PhoneVerifyUI").build().getAsString(new StringRequestListener()
                //                    {
                //                        @Override
                //                        public void onResponse(String Response)
                //                        {
                //                            LoadingViewNext.Stop();
                //                            ButtonNext.setEnabled(true);
                //
                //                            try
                //                            {
                //                                JSONObject Result = new JSONObject(Response);
                //
                //                                switch (Result.getInt("Message"))
                //                                {
                //                                    case 0:
                //
                //                                        Misc.SetBoolean("IsLogin", true);
                //                                        Misc.SetBoolean("IsGoogle", false);
                //                                        Misc.SetString("Token", Result.getString("Token"));
                //                                        Misc.SetString("ID", Result.getString("ID"));
                //                                        Misc.SetString("Username", Result.getString("Username"));
                //                                        Misc.SetString("Avatar", Result.getString("Avatar"));
                //
                //                                        Activity.startActivity(new Intent(Activity, SocialActivity.class));
                //                                        Activity.finish();
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
                //                                    case 8:
                //                                        Misc.ToastOld(Misc.String(R.string.PhoneVerifyUICodeCount));
                //                                        break;
                //                                    case 9:
                //                                        Misc.ToastOld(Misc.String(R.string.PhoneVerifyUICodeWrong));
                //                                        break;
                //                                    case 10:
                //                                        Misc.ToastOld(Misc.String(R.string.PhoneVerifyUICodeNotFound));
                //                                        break;
                //                                    default:
                //                                        Misc.GeneralError(Result.getInt("Message"));
                //                                        break;
                //                                }
                //                            }
                //                            catch (Exception e)
                //                            {
                //                                Misc.Debug("PhoneVerifyUI-SignInPhoneVerify: " + e.toString());
                //                            }
                //                        }
                //
                //                        @Override
                //                        public void onError(ANError e)
                //                        {
                //                            LoadingViewNext.Stop();
                //                            ButtonNext.setEnabled(true);
                //                            Misc.ToastOld(Misc.String(R.string.GeneralNoInternet));
                //                        }
                //                    });
                //                }
            }
        });

        ViewMain = view;
        Misc.fontSetter(view);
    }

    @Override
    public void OnResume()
    {

        Intent i = new Intent("Biogram.SMS.Request");
        i.putExtra("SetWaiting", true);

        Activity.sendBroadcast(i);
        Activity.registerReceiver(VerifyReceiver, new IntentFilter("Biogram.SMS.Verify"));
    }

    @Override
    public void OnPause()
    {
        AndroidNetworking.forceCancel("PhoneVerifyUI");
    }

    @Override
    public void OnDestroy()
    {
        Intent i = new Intent("Biogram.SMS.Request");
        i.putExtra("SetWaiting", false);

        Activity.sendBroadcast(i);
        Activity.unregisterReceiver(VerifyReceiver);

        CountDownTimerResend.cancel();
    }
}
