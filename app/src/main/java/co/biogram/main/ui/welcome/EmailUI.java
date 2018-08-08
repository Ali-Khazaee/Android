package co.biogram.main.ui.welcome;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import co.biogram.main.R;
import co.biogram.main.fragment.FragmentView;
import co.biogram.main.handler.Misc;
import co.biogram.main.ui.view.LoadingView;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import org.json.JSONObject;

class EmailUI extends FragmentView {
    private final String Username;
    private final String Password;

    EmailUI(String username, String password) {
        Username = username;
        Password = password;
    }

    @Override
    public void OnCreate() {
        View view = View.inflate(Activity, R.layout.welcome_email, null);

        final android.widget.Button ButtonNext = view.findViewById(R.id.buttonNextStep);
        final LoadingView LoadingViewNext = new LoadingView(Activity);

        view.findViewById(R.id.ImageButtonBack).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Activity.onBackPressed();
            }
        });

        final EditText EditTextEmail = view.findViewById(R.id.EditTextPasswordReset);
        Misc.SetCursorColor(EditTextEmail, R.color.Primary);
        EditTextEmail.requestFocus();
        EditTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ButtonNext.setEnabled(s.length() > 6 && Patterns.EMAIL_ADDRESS.matcher(s).matches());
            }
        });

        ButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ButtonNext.setEnabled(false);
                LoadingViewNext.Start();

                AndroidNetworking.post(Misc.GetRandomServer("SignUpEmail"))
                        .addBodyParameter("Username", Username)
                        .addBodyParameter("Password", Password)
                        .addBodyParameter("Email", EditTextEmail.getText().toString())
                        .setTag("EmailUI")
                        .build()
                        .getAsString(new StringRequestListener() {
                            @Override
                            public void onResponse(String Response) {
                                LoadingViewNext.Stop();
                                ButtonNext.setEnabled(true);

                                try {
                                    JSONObject Result = new JSONObject(Response);

                                    switch (Result.getInt("Message")) {
                                        case 0:

                                            Misc.ToastOld(Misc.String(R.string.EmailUIError5));

                                            Activity.GetManager().OpenView(new EmailVerifyUI(Username, Password, EditTextEmail.getText().toString()), "EmailVerifyUI", true);
                                            break;
                                        case 1:
                                        case 2:
                                        case 3:
                                        case 4:
                                            Misc.ToastOld(Misc.String(R.string.EmailUIError1));
                                            break;
                                        case 5:
                                        case 6:
                                        case 7:
                                            Misc.ToastOld(Misc.String(R.string.EmailUIError2));
                                            break;
                                        case 8:
                                        case 9:
                                            Misc.ToastOld(Misc.String(R.string.EmailUIError3));
                                            break;
                                        case 10:
                                            Misc.ToastOld(Misc.String(R.string.EmailUIError4));
                                            break;
                                        default:
                                            Misc.GeneralError(Result.getInt("Message"));
                                            break;
                                    }
                                } catch (Exception e) {
                                    Misc.Debug("EmailUI: " + e.toString());
                                }
                            }

                            @Override
                            public void onError(ANError anError) {
                                LoadingViewNext.Stop();
                                ButtonNext.setEnabled(true);
                                Misc.ToastOld(Misc.String(R.string.GeneralNoInternet));
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
        AndroidNetworking.forceCancel("EmailUI");
    }
}
