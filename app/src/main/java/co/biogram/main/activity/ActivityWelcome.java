package co.biogram.main.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONObject;

import co.biogram.main.App;
import co.biogram.main.BuildConfig;
import co.biogram.main.R;
import co.biogram.main.handler.MiscHandler;
import co.biogram.main.handler.SharedHandler;
import co.biogram.main.handler.URLHandler;
import co.biogram.main.misc.LoadingView;

public class ActivityWelcome extends AppCompatActivity
{
    private static String Username;
    private static String Password;
    private GoogleApiClient GoogleClient;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (SharedHandler.GetBoolean("IsLogin"))
        {
            startActivity(new Intent(ActivityWelcome.this, ActivityMain.class));
            finish();
            return;
        }

        if (Build.VERSION.SDK_INT > 20)
            getWindow().setStatusBarColor(ContextCompat.getColor(App.GetContext(), R.color.BlueLight));

        setContentView(R.layout.activity_welcome);

        findViewById(R.id.ButtonSignUp).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getSupportFragmentManager().beginTransaction().add(R.id.FrameLayoutContainer, new SignUpUsernameFragment()).addToBackStack("SignUp").commit();
            }
        });

        findViewById(R.id.LinearLayoutGoogle).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivityForResult(Auth.GoogleSignInApi.getSignInIntent(GoogleClient), 100);
            }
        });

        findViewById(R.id.TextViewTerm).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com")));
            }
        });

        findViewById(R.id.LinearLayoutSignIn).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getSupportFragmentManager().beginTransaction().add(R.id.FrameLayoutContainer, new SignInFragment()).addToBackStack("SignIn").commit();
            }
        });

        GoogleSignInOptions GoogleOption = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestProfile().requestEmail().requestId().requestIdToken("590625045379-9pgbc6r8v0794rij59jj50o1gp6ijnvl.apps.googleusercontent.com").build();
        GoogleClient = new GoogleApiClient.Builder(this).enableAutoManage(this, null).addApi(Auth.GOOGLE_SIGN_IN_API, GoogleOption).build();

        if (GoogleClient.isConnected())
            Auth.GoogleSignInApi.signOut(GoogleClient);
    }

    @Override
    public void onActivityResult(int RequestCode, int ResultCode, Intent Data)
    {
        if (RequestCode == 100)
        {
            GoogleSignInResult Result = Auth.GoogleSignInApi.getSignInResultFromIntent(Data);

            if (Result.isSuccess())
            {
                GoogleSignInAccount Acc = Result.getSignInAccount();

                if (Acc != null)
                {
                    AndroidNetworking.post(URLHandler.GetURL(URLHandler.URL.SIGN_IN_GOOGLE)).addBodyParameter("Token", Acc.getIdToken()).addBodyParameter("Session", GenerateSession()).setTag("ActivityWelcome").build().getAsString(new StringRequestListener()
                    {
                        @Override
                        public void onResponse(String Response)
                        {
                            try
                            {
                                JSONObject Result = new JSONObject(Response);

                                if (Result.getInt("Message") == 1000)
                                {
                                    SharedHandler.SetBoolean("IsLogin", true);
                                    SharedHandler.SetString("TOKEN", Result.getString("Token"));
                                    SharedHandler.SetString("ID", Result.getString("AccountID"));
                                    SharedHandler.SetString("Username", Result.getString("AccountID"));
                                    SharedHandler.SetString("Avatar", Result.getString("Avatar"));

                                    startActivity(new Intent(ActivityWelcome.this, ActivityMain.class));
                                    finish();
                                    return;
                                }

                                MiscHandler.Toast(getString(R.string.ActivityWelcomeGoogleError));
                            }
                            catch (Exception e)
                            {
                                // Leave Me Alone
                            }
                        }

                        @Override
                        public void onError(ANError error)
                        {
                            MiscHandler.Toast(getString(R.string.GeneralCheckInternet));
                        }
                    });
                }

                if (GoogleClient.isConnected())
                    Auth.GoogleSignInApi.signOut(GoogleClient);
            }
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();
        GoogleClient.disconnect();
        MiscHandler.HideKeyBoard(this);
        AndroidNetworking.cancel("ActivityWelcome");
    }

    public static class SignUpUsernameFragment extends Fragment
    {
        private InputMethodManager IMM;
        private EditText EditTextUsername;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup Parent, Bundle savedInstanceState)
        {
            View RootView = inflater.inflate(R.layout.activity_welcome_fragment_username, Parent, false);

            RootView.findViewById(R.id.ImageViewBack).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    MiscHandler.HideKeyBoard(getActivity());
                    getActivity().onBackPressed();
                }
            });

            final LoadingView LoadingViewUsername = (LoadingView) RootView.findViewById(R.id.LoadingViewUsername);
            final Button ButtonNext = (Button) RootView.findViewById(R.id.ButtonNext);

            ButtonNext.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(final View v)
                {
                    LoadingViewUsername.Start();
                    ButtonNext.setVisibility(View.INVISIBLE);

                    AndroidNetworking.post(URLHandler.GetURL(URLHandler.URL.USERNAME_IS_AVAILABLE)).addBodyParameter("Username", EditTextUsername.getText().toString()).setTag("SignUpUsernameFragment").build().getAsString(new StringRequestListener()
                    {
                        @Override
                        public void onResponse(String Response)
                        {
                            try
                            {
                                if (new JSONObject(Response).getInt("Message") == 1000)
                                {
                                    Username = EditTextUsername.getText().toString();
                                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.FrameLayoutContainer, new SignUpPasswordFragment()).addToBackStack("SignUpUsernameFragment").commit();
                                }
                                else
                                    MiscHandler.Toast(getString(R.string.ActivityWelcomeFragmentUsernameError));
                            }
                            catch (Exception e)
                            {
                                // Leave Me Alone
                            }

                            LoadingViewUsername.Stop();
                            ButtonNext.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError(ANError error)
                        {
                            LoadingViewUsername.Stop();
                            ButtonNext.setVisibility(View.VISIBLE);
                            MiscHandler.Toast(getString(R.string.GeneralCheckInternet));
                        }
                    });
                }
            });

            RootView.findViewById(R.id.TextViewPrivacy).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com")));
                }
            });

            EditTextUsername = (EditText) RootView.findViewById(R.id.EditTextUsername);
            EditTextUsername.requestFocus();
            EditTextUsername.addTextChangedListener(new TextWatcher()
            {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                @Override
                public void afterTextChanged(Editable s) { }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count)
                {
                    if (s.length() > 2)
                        ButtonNext.setEnabled(true);
                    else
                        ButtonNext.setEnabled(false);
                }
            });

            IMM = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            IMM.showSoftInput(EditTextUsername, 0);
            IMM.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);

            return RootView;
        }

        @Override
        public void onResume()
        {
            super.onResume();

            if (IMM != null && EditTextUsername != null)
            {
                EditTextUsername.requestFocus();
                IMM.showSoftInput(EditTextUsername, 0);
                IMM.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        }

        @Override
        public void onPause()
        {
            super.onPause();
            AndroidNetworking.cancel("SignUpUsernameFragment");
        }
    }

    public static class SignUpPasswordFragment extends Fragment
    {
        private InputMethodManager IMM;
        private EditText EditTextPassword;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup Parent, Bundle savedInstanceState)
        {
            View RootView = inflater.inflate(R.layout.activity_welcome_fragment_password, Parent, false);

            RootView.findViewById(R.id.ImageViewBack).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    MiscHandler.HideKeyBoard(getActivity());
                    getActivity().onBackPressed();
                }
            });

            final Button ButtonNext = (Button) RootView.findViewById(R.id.ButtonNext);
            ButtonNext.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Password = EditTextPassword.getText().toString();
                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.FrameLayoutContainer, new SignUpEmailFragment()).addToBackStack("SignUpPasswordFragment").commit();
                }
            });

            RootView.findViewById(R.id.TextViewPrivacy).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com")));
                }
            });

            EditTextPassword = (EditText) RootView.findViewById(R.id.EditTextPassword);
            EditTextPassword.requestFocus();
            EditTextPassword.addTextChangedListener(new TextWatcher()
            {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                @Override
                public void afterTextChanged(Editable s) { }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count)
                {
                    if (s.length() > 5)
                        ButtonNext.setEnabled(true);
                    else
                        ButtonNext.setEnabled(false);
                }
            });

            IMM = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            IMM.showSoftInput(EditTextPassword, 0);
            IMM.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);

            return RootView;
        }

        @Override
        public void onResume()
        {
            super.onResume();

            if (IMM != null && EditTextPassword != null)
            {
                EditTextPassword.requestFocus();
                IMM.showSoftInput(EditTextPassword, 0);
                IMM.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        }
    }

    public static class SignUpEmailFragment extends Fragment
    {
        private InputMethodManager IMM;
        private EditText EditTextEmail;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup Parent, Bundle savedInstanceState)
        {
            View RootView = inflater.inflate(R.layout.activity_welcome_fragment_email, Parent, false);

            RootView.findViewById(R.id.ImageViewBack).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    MiscHandler.HideKeyBoard(getActivity());
                    getActivity().onBackPressed();
                }
            });

            final LoadingView LoadingViewEmail = (LoadingView) RootView.findViewById(R.id.LoadingViewEmail);
            final Button ButtonFinish = (Button) RootView.findViewById(R.id.ButtonFinish);

            ButtonFinish.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    ButtonFinish.setVisibility(View.INVISIBLE);
                    LoadingViewEmail.Start();

                    AndroidNetworking.post(URLHandler.GetURL(URLHandler.URL.SIGN_UP))
                    .addBodyParameter("Username", Username)
                    .addBodyParameter("Password", Password)
                    .addBodyParameter("Email", EditTextEmail.getText().toString())
                    .addBodyParameter("Session", GenerateSession())
                    .setTag("SignUpEmailFragment").build().getAsString(new StringRequestListener()
                    {
                        @Override
                        public void onResponse(String Response)
                        {
                            try
                            {
                                JSONObject Result = new JSONObject(Response);

                                if (Result.getInt("Message") == 1000)
                                {
                                    SharedHandler.SetBoolean("IsLogin", true);
                                    SharedHandler.SetString("TOKEN", Result.getString("TOKEN"));
                                    SharedHandler.SetString("ID", Result.getString("ID"));
                                    SharedHandler.SetString("Username", Result.getString("Username"));
                                    SharedHandler.SetString("Avatar", Result.getString("Avatar"));

                                    getActivity().startActivity(new Intent(getActivity(), ActivityMain.class));
                                    getActivity().finish();
                                }
                                else
                                    MiscHandler.Toast(getString(R.string.ActivityWelcomeFragmentEmailError));
                            }
                            catch (Exception e)
                            {
                                // Leave Me Alone
                            }

                            LoadingViewEmail.Stop();
                            ButtonFinish.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError(ANError error)
                        {
                            LoadingViewEmail.Stop();
                            ButtonFinish.setVisibility(View.VISIBLE);
                            MiscHandler.Toast(getString(R.string.GeneralCheckInternet));
                        }
                    });
                }
            });

            RootView.findViewById(R.id.TextViewPrivacy).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com")));
                }
            });

            EditTextEmail = (EditText) RootView.findViewById(R.id.EditTextEmail);
            EditTextEmail.requestFocus();
            EditTextEmail.addTextChangedListener(new TextWatcher()
            {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                @Override
                public void afterTextChanged(Editable s) { }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count)
                {
                    if (s.length() > 6 && Patterns.EMAIL_ADDRESS.matcher(s).matches())
                        ButtonFinish.setEnabled(true);
                    else
                        ButtonFinish.setEnabled(false);
                }
            });

            IMM = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            IMM.showSoftInput(EditTextEmail, 0);
            IMM.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);

            return RootView;
        }

        @Override
        public void onResume()
        {
            super.onResume();

            if (IMM != null && EditTextEmail != null)
            {
                EditTextEmail.requestFocus();
                IMM.showSoftInput(EditTextEmail, 0);
                IMM.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        }

        @Override
        public void onPause()
        {
            super.onPause();
            AndroidNetworking.cancel("SignUpEmailFragment");
        }
    }

    public static class SignInFragment extends Fragment
    {
        private InputMethodManager IMM;
        private EditText EmailOrUsername;
        private boolean RequestUsername = false;
        private boolean RequestPassword = false;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup Parent, Bundle savedInstanceState)
        {
            View RootView = inflater.inflate(R.layout.activity_welcome_fragment_signin, Parent, false);

            RootView.findViewById(R.id.ImageViewBack).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    MiscHandler.HideKeyBoard(getActivity());
                    getActivity().onBackPressed();
                }
            });

            final LoadingView LoadingViewSignIn = (LoadingView) RootView.findViewById(R.id.LoadingViewSignIn);
            final Button ButtonSignIn = (Button) RootView.findViewById(R.id.ButtonSignIn);

            EmailOrUsername = (EditText) RootView.findViewById(R.id.EmailOrUsername);
            EmailOrUsername.requestFocus();
            EmailOrUsername.addTextChangedListener(new TextWatcher()
            {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                @Override
                public void afterTextChanged(Editable s) { }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count)
                {
                    RequestUsername = (s.length() > 2);

                    if (RequestUsername && RequestPassword)
                        ButtonSignIn.setEnabled(true);
                    else
                        ButtonSignIn.setEnabled(false);
                }
            });

            final EditText EditTextPassword = (EditText) RootView.findViewById(R.id.EditTextPassword);
            EditTextPassword.addTextChangedListener(new TextWatcher()
            {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                @Override
                public void afterTextChanged(Editable s) { }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count)
                {
                    RequestPassword = (s.length() > 5);

                    if (RequestUsername && RequestPassword)
                        ButtonSignIn.setEnabled(true);
                    else
                        ButtonSignIn.setEnabled(false);
                }
            });

            ButtonSignIn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    ButtonSignIn.setVisibility(View.INVISIBLE);
                    LoadingViewSignIn.Start();

                    AndroidNetworking.post(URLHandler.GetURL(URLHandler.URL.SIGN_IN))
                    .addBodyParameter("EmailOrUsername", EmailOrUsername.getText().toString())
                    .addBodyParameter("Password", EditTextPassword.getText().toString())
                    .addBodyParameter("Session", GenerateSession())
                    .setTag("SignInFragment").build().getAsString(new StringRequestListener()
                    {
                        @Override
                        public void onResponse(String Response)
                        {
                            try
                            {
                                JSONObject Result = new JSONObject(Response);

                                if (Result.getInt("Message") == 1000)
                                {
                                    SharedHandler.SetBoolean("IsLogin", true);
                                    SharedHandler.SetString("TOKEN", Result.getString("TOKEN"));
                                    SharedHandler.SetString("ID", Result.getString("ID"));
                                    SharedHandler.SetString("Username", Result.getString("Username"));
                                    SharedHandler.SetString("Avatar", Result.getString("Avatar"));

                                    getActivity().startActivity(new Intent(getActivity(), ActivityMain.class));
                                    getActivity().finish();
                                }
                                else
                                    MiscHandler.Toast(getString(R.string.ActivityWelcomeFragmentSignInError));
                            }
                            catch (Exception e)
                            {
                                // Leave Me Alone
                            }

                            LoadingViewSignIn.Stop();
                            ButtonSignIn.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError(ANError error)
                        {
                            LoadingViewSignIn.Stop();
                            ButtonSignIn.setVisibility(View.VISIBLE);
                            MiscHandler.Toast(getString(R.string.GeneralCheckInternet));
                        }
                    });
                }
            });

            RootView.findViewById(R.id.TextViewForgot).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.FrameLayoutContainer, new ResetFragment()).addToBackStack("ResetFragment").commit();
                }
            });

            IMM = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            IMM.showSoftInput(EmailOrUsername, 0);
            IMM.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);

            return RootView;
        }

        @Override
        public void onResume()
        {
            super.onResume();

            if (IMM != null && EmailOrUsername != null)
            {
                EmailOrUsername.requestFocus();
                IMM.showSoftInput(EmailOrUsername, 0);
                IMM.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        }

        @Override
        public void onPause()
        {
            super.onPause();
            AndroidNetworking.cancel("SignInFragment");
        }
    }

    public static class ResetFragment extends Fragment
    {
        private InputMethodManager IMM;
        private EditText EditTextEmailOrUsername;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup Parent, Bundle savedInstanceState)
        {
            View RootView = inflater.inflate(R.layout.activity_welcome_fragment_reset, Parent, false);

            RootView.findViewById(R.id.ImageViewBack).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    MiscHandler.HideKeyBoard(getActivity());
                    getActivity().onBackPressed();
                }
            });

            final Button ButtonSubmit = (Button) RootView.findViewById(R.id.ButtonSubmit);
            final LinearLayout LinearLayoutLoading = (LinearLayout) RootView.findViewById(R.id.LinearLayoutLoading);
            final LoadingView LoadingViewReset = (LoadingView) RootView.findViewById(R.id.LoadingViewReset);

            EditTextEmailOrUsername = (EditText) RootView.findViewById(R.id.EditTextEmailOrUsername);
            EditTextEmailOrUsername.requestFocus();
            EditTextEmailOrUsername.addTextChangedListener(new TextWatcher()
            {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                @Override
                public void afterTextChanged(Editable s) { }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count)
                {
                    ButtonSubmit.setEnabled((s.length() > 2));
                }
            });

            ButtonSubmit.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    ButtonSubmit.setVisibility(View.INVISIBLE);
                    LinearLayoutLoading.setVisibility(View.VISIBLE);
                    LoadingViewReset.Start();

                    AndroidNetworking.post(URLHandler.GetURL(URLHandler.URL.RESET_PASSWORD))
                    .addBodyParameter("EmailOrUsername", EditTextEmailOrUsername.getText().toString())
                    .setTag("ResetFragment").build().getAsString(new StringRequestListener()
                    {
                        @Override
                        public void onResponse(String Response)
                        {
                            try
                            {
                                if (new JSONObject(Response).getInt("Message") == 1000)
                                    MiscHandler.Toast(getString(R.string.ActivityWelcomeFragmentResetSuccess));
                                else
                                    MiscHandler.Toast(getString(R.string.ActivityWelcomeFragmentResetError));
                            }
                            catch (Exception e)
                            {
                                // Leave Me Alone
                            }

                            LoadingViewReset.Stop();
                            LinearLayoutLoading.setVisibility(View.INVISIBLE);
                            ButtonSubmit.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError(ANError error)
                        {
                            LoadingViewReset.Stop();
                            LinearLayoutLoading.setVisibility(View.INVISIBLE);
                            ButtonSubmit.setVisibility(View.VISIBLE);
                            MiscHandler.Toast(getString(R.string.GeneralCheckInternet));
                        }
                    });
                }
            });

            IMM = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            IMM.showSoftInput(EditTextEmailOrUsername, 0);
            IMM.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);

            return RootView;
        }

        @Override
        public void onResume()
        {
            super.onResume();

            if (IMM != null && EditTextEmailOrUsername != null)
            {
                EditTextEmailOrUsername.requestFocus();
                IMM.showSoftInput(EditTextEmailOrUsername, 0);
                IMM.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        }

        @Override
        public void onPause()
        {
            super.onPause();
            AndroidNetworking.cancel("ResetFragment");
        }
    }

    private static String GenerateSession()
    {
        return "BioGram Android " + BuildConfig.VERSION_NAME + " - " + Build.MODEL + " - " + Build.MANUFACTURER + " - SDK " + Build.VERSION.SDK_INT;
    }
}
