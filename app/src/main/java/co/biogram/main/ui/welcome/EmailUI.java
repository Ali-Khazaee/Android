package co.biogram.main.ui.welcome;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import co.biogram.main.R;
import co.biogram.main.fragment.FragmentView;
import co.biogram.main.handler.Misc;
import co.biogram.main.ui.view.LoadingView;
import com.androidnetworking.AndroidNetworking;

class EmailUI extends FragmentView
{


    @Override
    public void OnCreate()
    {
        View view = View.inflate(Activity, R.layout.welcome_email, null);

        final android.widget.Button ButtonNext = view.findViewById(R.id.buttonNextStep);
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

        final EditText EditTextEmail = view.findViewById(R.id.EditTextEmail);
        Misc.SetCursorColor(EditTextEmail, R.color.Primary);
        EditTextEmail.requestFocus();
        Misc.changeEditTextUnderlineColor(EditTextEmail, R.color.Primary, R.color.Gray);
        EditTextEmail.addTextChangedListener(new TextWatcher()
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
                ButtonNext.setEnabled(s.length() > 6 && Patterns.EMAIL_ADDRESS.matcher(s).matches());
            }
        });

        ButtonNext.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Activity.GetManager().OpenView(new EmailVerifyUI(EditTextEmail.getText().toString()), "EmailVerifyUI", true);

            }
        });

        ViewMain = view;
        Misc.fontSetter(view);
    }

    @Override
    public void OnPause()
    {
        Misc.HideSoftKey(Activity);
        AndroidNetworking.forceCancel("EmailUI");
    }
}
