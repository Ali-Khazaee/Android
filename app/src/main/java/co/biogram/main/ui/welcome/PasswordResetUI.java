package co.biogram.main.ui.welcome;

import android.content.Intent;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import co.biogram.main.R;
import co.biogram.main.fragment.FragmentView;
import co.biogram.main.handler.Misc;
import co.biogram.main.ui.view.LoadingView;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import org.json.JSONObject;

class PasswordResetUI extends FragmentView {

    @Override
    public void OnCreate() {
        View view = View.inflate(Activity, R.layout.welcome_password_reset, null);

        final Button ButtonFinish = view.findViewById(R.id.buttonFinish);
        final LoadingView LoadingViewFinish = new LoadingView(Activity);

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
                ButtonFinish.setEnabled(s.length() > 2 && Patterns.EMAIL_ADDRESS.matcher(s).matches());
            }
        });

        android.widget.TextView TextViewPrivacy = view.findViewById(R.id.textViewTerm);
        TextViewPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://biogram.co")));
            }
        });


        ButtonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ButtonFinish.setEnabled(false);
                LoadingViewFinish.Start();

                AndroidNetworking.post(Misc.GetRandomServer("ResetPassword"))
                        .addBodyParameter("EmailOrUsername", EditTextEmailOrUsername.getText().toString())
                        .setTag("PasswordResetUI")
                        .build()
                        .getAsString(new StringRequestListener() {
                            @Override
                            public void onResponse(String Response) {
                                LoadingViewFinish.Stop();
                                ButtonFinish.setEnabled(true);

                                try {
                                    JSONObject Result = new JSONObject(Response);

                                    switch (Result.getInt("Message")) {
                                        case 0:
                                            Misc.ToastOld(Misc.String(R.string.PasswordResetUIError4));
                                            break;
                                        case 1:
                                            Misc.ToastOld(Misc.String(R.string.PasswordResetUIError1));
                                            break;
                                        case 2:
                                            Misc.ToastOld(Misc.String(R.string.PasswordResetUIError2));
                                            break;
                                        case 3:
                                            Misc.ToastOld(Misc.String(R.string.PasswordResetUIError3));
                                            break;
                                        default:
                                            Misc.GeneralError(Result.getInt("Message"));
                                    }
                                } catch (Exception e) {
                                    Misc.Debug("PasswordResetUI: " + e.toString());
                                }
                            }

                            @Override
                            public void onError(ANError e) {
                                LoadingViewFinish.Stop();
                                ButtonFinish.setEnabled(true);
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
        AndroidNetworking.forceCancel("PasswordResetUI");
    }
}
