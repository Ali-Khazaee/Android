package co.biogram.main.ui.welcome;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import co.biogram.main.R;
import co.biogram.main.activity.SocialActivity;
import co.biogram.main.fragment.FragmentView;
import co.biogram.main.handler.Misc;
import co.biogram.main.ui.view.LoadingView;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import org.json.JSONObject;

class EmailSignInUI extends FragmentView {
    private boolean RequestUsername = false;
    private boolean RequestPassword = false;

    @Override
    public void OnCreate() {
        View view = View.inflate(Activity, R.layout.welcome_email_signin, null);

        final Button ButtonSignIn = view.findViewById(R.id.buttonSignup);
        final LoadingView LoadingViewSignIn = new LoadingView(Activity);

        view.findViewById(R.id.ImageButtonBack).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Activity.onBackPressed();
            }
        });

        final EditText EditTextEmailOrUsername = view.findViewById(R.id.EditTextEmail);
        Misc.SetCursorColor(EditTextEmailOrUsername, R.color.Primary);
        EditTextEmailOrUsername.requestFocus();
        EditTextEmailOrUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                RequestUsername = s.length() > 2;
                ButtonSignIn.setEnabled(RequestUsername && RequestPassword);
            }
        });

        final EditText EditTextPassword = view.findViewById(R.id.EditTextPassword);
        Misc.SetCursorColor(EditTextEmailOrUsername, R.color.Primary);
        EditTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                RequestPassword = s.length() > 5;
                ButtonSignIn.setEnabled(RequestUsername && RequestPassword);
            }
        });

        ButtonSignIn.setEnabled(false);
        ButtonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ButtonSignIn.setEnabled(false);

                LoadingViewSignIn.Start();

                AndroidNetworking.post(Misc.GetRandomServer("SignInEmail"))
                        .addBodyParameter("EmailOrUsername", EditTextEmailOrUsername.getText().toString())
                        .addBodyParameter("Password", EditTextPassword.getText().toString())
                        .addBodyParameter("Session", Misc.GenerateSession())
                        .setTag("EmailSignInUI")
                        .build()
                        .getAsString(new StringRequestListener() {
                            @Override
                            public void onResponse(String Response) {
                                LoadingViewSignIn.Stop();
                                ButtonSignIn.setEnabled(true);

                                try {
                                    JSONObject Result = new JSONObject(Response);

                                    switch (Result.getInt("Message")) {
                                        case 0:
                                            Misc.SetBoolean("IsLogin", true);
                                            Misc.SetBoolean("IsGoogle", false);
                                            Misc.SetString("Token", Result.getString("Token"));
                                            Misc.SetString("ID", Result.getString("ID"));
                                            Misc.SetString("Username", Result.getString("Username"));
                                            Misc.SetString("Avatar", Result.getString("Avatar"));

                                            Activity.startActivity(new Intent(Activity, SocialActivity.class));
                                            Activity.finish();
                                            break;
                                        case 1:
                                            Misc.ToastOld(Misc.String(R.string.EmailSignInUIError1));
                                            break;
                                        case 2:
                                            Misc.ToastOld(Misc.String(R.string.EmailSignInUIError2));
                                            break;
                                        case 3:
                                            Misc.ToastOld(Misc.String(R.string.EmailSignInUIError3));
                                            break;
                                        case 4:
                                            Misc.ToastOld(Misc.String(R.string.EmailSignInUIError4));
                                            break;
                                        case 5:
                                            Misc.ToastOld(Misc.String(R.string.EmailSignInUIError5));
                                            break;
                                        case 6:
                                            Misc.ToastOld(Misc.String(R.string.EmailSignInUIError6));
                                            break;
                                        case 7:
                                            Misc.ToastOld(Misc.String(R.string.EmailSignInUIError7));
                                            break;
                                        case 8:
                                            Misc.ToastOld(Misc.String(R.string.EmailSignInUIError8));
                                            break;
                                        default:
                                            Misc.GeneralError(Result.getInt("Message"));
                                            break;
                                    }
                                } catch (Exception e) {
                                    Misc.Debug("EmailSignInUI: " + e.toString());
                                }
                            }

                            @Override
                            public void onError(ANError e) {
                                LoadingViewSignIn.Stop();
                                ButtonSignIn.setEnabled(true);
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
        AndroidNetworking.forceCancel("EmailSignInUI");
    }
}
