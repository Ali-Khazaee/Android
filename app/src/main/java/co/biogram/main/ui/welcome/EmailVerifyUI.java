package co.biogram.main.ui.welcome;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;

import co.biogram.main.R;
import co.biogram.main.fragment.FragmentView;
import co.biogram.main.handler.Misc;
import co.biogram.main.ui.view.LoadingView;

class EmailVerifyUI extends FragmentView
{
    private final String Email;
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

    EmailVerifyUI(String email)
    {
        Email = email;
    }

    @Override
    public void OnCreate()
    {
        View view = View.inflate(Activity, R.layout.welcome_email_verify, null);

        final Button ButtonNext = view.findViewById(R.id.buttonNextStep);
        final LoadingView LoadingViewNext = new LoadingView(Activity);

        InputMethodManager imm = (InputMethodManager) Activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        view.findViewById(R.id.ImageButtonBack).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Activity.onBackPressed();
            }
        });

        EditTextCode1 = view.findViewById(R.id.editTextCode1);
        Misc.SetCursorColor(EditTextCode1, R.color.Primary);
        EditTextCode1.requestFocus();
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
                    EditText NextField = (EditText) EditTextCode2.focusSearch(View.FOCUS_LEFT);
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

        Misc.changeEditTextUnderlineColor(EditTextCode1, R.color.Primary, R.color.Gray);
        Misc.changeEditTextUnderlineColor(EditTextCode2, R.color.Primary, R.color.Gray);
        Misc.changeEditTextUnderlineColor(EditTextCode3, R.color.Primary, R.color.Gray);
        Misc.changeEditTextUnderlineColor(EditTextCode4, R.color.Primary, R.color.Gray);
        Misc.changeEditTextUnderlineColor(EditTextCode5, R.color.Primary, R.color.Gray);

        ((android.widget.TextView) view.findViewById(R.id.textViewEmail)).setText(Email);

        final TextView TextViewResend = view.findViewById(R.id.textViewResendCode);
        Misc.SetCursorColor(EditTextCode4, R.color.Primary);
        TextViewResend.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });

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
                    TextViewResend.setTextColor(ResourcesCompat.getColor(Activity.getResources(), R.color.Gray, null));
                }
            }

            @Override
            public void onFinish()
            {
                cancel();

                Enabled = true;

                TextViewResend.setEnabled(true);
                TextViewResend.setTextColor(ResourcesCompat.getColor(Activity.getResources(), R.color.Primary, null));
                TextViewTime.setText("");
            }
        };
        CountDownTimerResend.start();

        ButtonNext.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final String VerifyCode = EditTextCode1.getText().toString() + EditTextCode2.getText().toString() + EditTextCode3.getText().toString() + EditTextCode4.getText().toString() + EditTextCode5.getText().toString();

                Activity.GetManager().OpenView(new UsernameUI(VerifyCode, 1), "UsernameUI", true);


            }
        });

        ViewMain = view;
        Misc.fontSetter(view);
    }

    @Override
    public void OnPause()
    {
        AndroidNetworking.forceCancel("EmailVerifyUI");
    }

    @Override
    public void OnDestroy()
    {
        CountDownTimerResend.cancel();
    }
}
