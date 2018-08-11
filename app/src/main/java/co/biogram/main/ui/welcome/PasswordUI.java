package co.biogram.main.ui.welcome;

import android.content.Intent;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import co.biogram.main.R;
import co.biogram.main.fragment.FragmentView;
import co.biogram.main.handler.Misc;

class PasswordUI extends FragmentView
{
    private final String Username;

    PasswordUI(String username)
    {
        Username = username;
    }

    @Override
    public void OnCreate()
    {
        View view = View.inflate(Activity, R.layout.welcome_password, null);
        final Button ButtonNext = view.findViewById(R.id.buttonNextStep);

        view.findViewById(R.id.ImageButtonBack).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Activity.onBackPressed();
            }
        });

        final EditText EditTextPassword = view.findViewById(R.id.EditTextPassword);
        Misc.SetCursorColor(EditTextPassword, R.color.Primary);
        EditTextPassword.requestFocus();
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
                ButtonNext.setEnabled(s.length() > 5);
            }
        });

        android.widget.TextView TextViewPrivacy = view.findViewById(R.id.textViewTerm);
        TextViewPrivacy.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://biogram.co")));
            }
        });

        ButtonNext.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Activity.GetManager().OpenView(new EmailUI(Username, EditTextPassword.getText().toString()), "EmailUI", true);
            }
        });

        ViewMain = view;
        Misc.fontSetter(view);
    }

    @Override
    public void OnPause()
    {
        Misc.HideSoftKey(Activity);
    }
}
