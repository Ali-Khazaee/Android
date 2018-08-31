package co.biogram.main.ui.welcome;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.RelativeLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONObject;

import co.biogram.main.R;
import co.biogram.main.activity.SocialActivity;
import co.biogram.main.fragment.FragmentView;
import co.biogram.main.handler.Misc;
import co.biogram.main.ui.view.LoadingView;

public class WelcomeUI extends FragmentView
{
    private LoadingView LoadingViewGoogle;
    private RelativeLayout RelativeLayoutGoogle;
    private GoogleApiClient GoogleApiClient;
    private boolean IsGoogleAvailable;

    @Override
    public void OnCreate()
    {
        View view = View.inflate(Activity, R.layout.welcome_welcome, null);

        view.findViewById(R.id.buttonSignup).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Activity.GetManager().OpenView(new PhoneUI(true), "PhoneUI", true);
            }
        });

        view.findViewById(R.id.buttonGoogleLogin).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (Build.VERSION.SDK_INT > 20)
                    Activity.getWindow().setStatusBarColor(ContextCompat.getColor(Activity, R.color.TextWhite));
                RelativeLayoutGoogle.setVisibility(View.VISIBLE);
                LoadingViewGoogle.Start();
                Activity.startActivityForResult(Auth.GoogleSignInApi.getSignInIntent(GoogleApiClient), 100);
            }
        });

        view.findViewById(R.id.LinearLayoutSignin).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Activity.GetManager().OpenView(new UsernameUI(), "PhoneUI", true);
            }
        });

        view.findViewById(R.id.LinearLayoutTerm).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://biogram.co")));
            }
        });

        RelativeLayoutGoogle = new RelativeLayout(Activity);
        RelativeLayoutGoogle.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayoutGoogle.setBackgroundColor(Color.parseColor("#90000000"));
        RelativeLayoutGoogle.setClickable(true);
        RelativeLayoutGoogle.setVisibility(View.GONE);

        RelativeLayout.LayoutParams LoadingViewGoogleParam = new RelativeLayout.LayoutParams(Misc.ToDP(56), Misc.ToDP(56));
        LoadingViewGoogleParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        LoadingViewGoogle = new LoadingView(Activity);
        LoadingViewGoogle.setLayoutParams(LoadingViewGoogleParam);
        LoadingViewGoogle.SetColor(R.color.TextDark);

        RelativeLayoutGoogle.addView(LoadingViewGoogle);

        IsGoogleAvailable = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(Activity) == ConnectionResult.SUCCESS;

        if (IsGoogleAvailable)
        {
            GoogleApiClient = new GoogleApiClient.Builder(Activity.getApplicationContext()).addApi(Auth.GOOGLE_SIGN_IN_API, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().requestIdToken("590625045379-pnhlgdqpr5i8ma705ej7akcggsr08vdf.apps.googleusercontent.com").build()).build();
        }

        ViewMain = view;
        Misc.fontSetter(view);
    }

    @Override
    public void OnResume()
    {
        if (IsGoogleAvailable)
            GoogleApiClient.connect();
    }

    @Override
    public void OnPause()
    {
        AndroidNetworking.forceCancel("WelcomeUI");

        if (IsGoogleAvailable)
        {
            if (GoogleApiClient.isConnected())
                Auth.GoogleSignInApi.signOut(GoogleApiClient);

            GoogleApiClient.disconnect();
        }
    }

    @Override
    public void OnActivityResult(int RequestCode, int ResultCode, Intent intent)
    {
        if (RequestCode == 100)
        {
            GoogleSignInResult Result = Auth.GoogleSignInApi.getSignInResultFromIntent(intent);

            if (Result.isSuccess())
            {
                final GoogleSignInAccount Result2 = Result.getSignInAccount();

                if (Result2 != null)
                {
                    AndroidNetworking.post(Misc.GetRandomServer("SignInGoogle")).addBodyParameter("Token", Result2.getIdToken()).addBodyParameter("Session", Misc.GenerateSession()).setTag("WelcomeUI").build().getAsString(new StringRequestListener()
                    {
                        @Override
                        public void onResponse(String Response)
                        {
                            HideGoogleLoading();

                            try
                            {
                                JSONObject Result3 = new JSONObject(Response);

                                switch (Result3.getInt("Message"))
                                {
                                    case 0:
                                        if (Result3.getBoolean("Registered"))
                                        {
                                            Misc.SetBoolean("IsLogin", true);
                                            Misc.SetBoolean("IsGoogle", true);
                                            Misc.SetString("Token", Result3.getString("Token"));
                                            Misc.SetString("ID", Result3.getString("ID"));
                                            Misc.SetString("Username", Result3.getString("Username"));
                                            Misc.SetString("Avatar", Result3.getString("Avatar"));

                                            Activity.startActivity(new Intent(Activity, SocialActivity.class));
                                            Activity.finish();
                                            return;
                                        }
                                        Activity.GetManager().OpenView(new UsernameUI(Result2.getIdToken(), 0), "UsernameUI", true);
                                        break;
                                    case 1:
                                    case 2:
                                    case 3:
                                    case 4:
                                    case 5:
                                        Misc.ToastOld(Misc.String(R.string.WelcomeUIGoogleError1));
                                        break;
                                    case 6:
                                        Misc.ToastOld(Misc.String(R.string.WelcomeUIGoogleError2));
                                        break;
                                    default:
                                        Misc.GeneralError(Result3.getInt("Message"));
                                }
                            }
                            catch (Exception e)
                            {
                                Misc.Debug("WelcomeUI: " + e.toString());
                            }
                        }

                        @Override
                        public void onError(ANError e)
                        {
                            HideGoogleLoading();
                            Misc.ToastOld(Misc.String(R.string.GeneralNoInternet));
                        }
                    });
                }
                else
                {
                    HideGoogleLoading();
                }
            }
            else
            {
                HideGoogleLoading();
            }
        }
    }

    private void HideGoogleLoading()
    {
        if (Build.VERSION.SDK_INT > 20)
            Activity.getWindow().setStatusBarColor(ContextCompat.getColor(Activity, R.color.Primary));

        LoadingViewGoogle.Stop();
        RelativeLayoutGoogle.setVisibility(View.GONE);
    }
}
