package co.biogram.main.ui.welcome;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.androidnetworking.AndroidNetworking;

import co.biogram.main.R;
import co.biogram.main.fragment.FragmentView;
import co.biogram.main.handler.Misc;
import co.biogram.main.ui.view.LoadingView;

class EmailSignInUI extends FragmentView
{
    private boolean RequestUsername = false;
    private boolean RequestPassword = false;

    @Override
    public void OnCreate()
    {
        View view = View.inflate(Activity, R.layout.welcome_email_signin, null);

        final Button ButtonSignIn = view.findViewById(R.id.buttonSignup);
        final LoadingView LoadingViewSignIn = new LoadingView(Activity);

        view.findViewById(R.id.ImageButtonBack).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Activity.onBackPressed();
            }
        });

        final EditText EditTextEmailOrUsername = view.findViewById(R.id.EditTextEmail);
        Misc.SetCursorColor(EditTextEmailOrUsername, R.color.Primary);
        EditTextEmailOrUsername.requestFocus();
        Misc.changeEditTextUnderlineColor(EditTextEmailOrUsername, R.color.Primary, R.color.Gray);
        EditTextEmailOrUsername.addTextChangedListener(new TextWatcher()
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
                RequestUsername = s.length() > 2;
                ButtonSignIn.setEnabled(RequestUsername && RequestPassword);
            }
        });

        final EditText EditTextPassword = view.findViewById(R.id.EditTextPassword);
        Misc.SetCursorColor(EditTextEmailOrUsername, R.color.Primary);
        Misc.changeEditTextUnderlineColor(EditTextPassword, R.color.Primary, R.color.Gray);
        EditTextPassword.addTextChangedListener(new TextWatcher()
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
                RequestPassword = s.length() > 5;
                ButtonSignIn.setEnabled(RequestUsername && RequestPassword);
            }
        });

        ButtonSignIn.setEnabled(false);
        ButtonSignIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });

        ViewMain = view;
        Misc.fontSetter(view);
    }

    @Override
    public void OnPause()
    {
        Misc.HideSoftKey(Activity);
        AndroidNetworking.forceCancel("EmailSignInUI");
    }
}
