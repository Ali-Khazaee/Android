package co.biogram.main.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONObject;

import co.biogram.main.BuildConfig;
import co.biogram.main.R;
import co.biogram.main.handler.MiscHandler;
import co.biogram.main.handler.SharedHandler;
import co.biogram.main.misc.LoadingView;

public class ActivityWelcome extends FragmentActivity
{
    private String Username;
    private String Password;

    private GoogleApiClient GoogleClient;
    private final int FrameLayoutContainerID = MiscHandler.GenerateViewID();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Context context = this;

        if (SharedHandler.GetBoolean(context, "IsLogin"))
        {
            startActivity(new Intent(ActivityWelcome.this, ActivityMain.class));
            finish();
            return;
        }

        if (Build.VERSION.SDK_INT > 20)
            getWindow().setStatusBarColor(ContextCompat.getColor(context, R.color.BlueLight));

        ScrollView Root = new ScrollView(context);
        Root.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        Root.setBackgroundResource(R.color.White);
        Root.setHorizontalScrollBarEnabled(false);
        Root.setVerticalScrollBarEnabled(false);
        Root.setFillViewport(true);

        RelativeLayout RelativeLayoutMain = new RelativeLayout(context);
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

        Root.addView(RelativeLayoutMain);

        View ViewHeader = new View(context);
        ViewHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 170)));
        ViewHeader.setBackgroundResource(R.color.BlueLight);
        ViewHeader.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain.addView(ViewHeader);

        LinearLayout LinearLayoutHeader = new LinearLayout(context);
        LinearLayoutHeader.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 170)));
        LinearLayoutHeader.setOrientation(LinearLayout.VERTICAL);
        LinearLayoutHeader.setGravity(Gravity.CENTER);

        RelativeLayoutMain.addView(LinearLayoutHeader);

        ImageView ImageViewHeader = new ImageView(context);
        ImageViewHeader.setLayoutParams(new LinearLayout.LayoutParams(MiscHandler.ToDimension(context, 150), MiscHandler.ToDimension(context, 65)));
        ImageViewHeader.setImageResource(R.drawable.ic_logo);

        LinearLayoutHeader.addView(ImageViewHeader);

        LinearLayout.LayoutParams TextViewHeaderParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        TextViewHeaderParam.setMargins(0, MiscHandler.ToDimension(context, 10), 0, 0);

        TextView TextViewHeader = new TextView(context);
        TextViewHeader.setLayoutParams(TextViewHeaderParam);
        TextViewHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        TextViewHeader.setText(getString(R.string.ActivityWelcomeHeader));
        TextViewHeader.setTextColor(ContextCompat.getColor(context, R.color.White));

        LinearLayoutHeader.addView(TextViewHeader);

        LinearLayout.LayoutParams TextViewHeader2Param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        TextViewHeader2Param.setMargins(0, MiscHandler.ToDimension(context, 5), 0, 0);

        TextView TextViewHeader2 = new TextView(context);
        TextViewHeader2.setLayoutParams(TextViewHeader2Param);
        TextViewHeader2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        TextViewHeader2.setText(getString(R.string.ActivityWelcomeHeader2));
        TextViewHeader2.setTextColor(ContextCompat.getColor(context, R.color.White));

        LinearLayoutHeader.addView(TextViewHeader2);

        RelativeLayout.LayoutParams ButtonSignUpParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 270), MiscHandler.ToDimension(context, 45));
        ButtonSignUpParam.setMargins(0, MiscHandler.ToDimension(context, 30), 0, 0);
        ButtonSignUpParam.addRule(RelativeLayout.BELOW, ViewHeader.getId());
        ButtonSignUpParam.addRule(RelativeLayout.CENTER_HORIZONTAL);

        GradientDrawable ShapeButtonSignUp = new GradientDrawable();
        ShapeButtonSignUp.setColor(ContextCompat.getColor(context, R.color.BlueLight));
        ShapeButtonSignUp.setCornerRadius(MiscHandler.ToDimension(context, 7));

        Button ButtonSignUp = new Button(context, null, android.R.attr.borderlessButtonStyle);
        ButtonSignUp.setLayoutParams(ButtonSignUpParam);
        ButtonSignUp.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        ButtonSignUp.setTextColor(ContextCompat.getColor(context, R.color.White));
        ButtonSignUp.setText(getString(R.string.ActivityWelcomeSignUp));
        ButtonSignUp.setId(MiscHandler.GenerateViewID());
        ButtonSignUp.setBackground(ShapeButtonSignUp);
        ButtonSignUp.setAllCaps(false);
        ButtonSignUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getSupportFragmentManager().beginTransaction().replace(FrameLayoutContainerID, new FragmentSignUpUsername()).addToBackStack("FragmentSignUpUsername").commit();
            }
        });

        RelativeLayoutMain.addView(ButtonSignUp);

        RelativeLayout.LayoutParams RelativeLayoutORParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 45));
        RelativeLayoutORParam.setMargins(0, MiscHandler.ToDimension(context, 20), 0, MiscHandler.ToDimension(context, 5));
        RelativeLayoutORParam.addRule(RelativeLayout.BELOW, ButtonSignUp.getId());

        RelativeLayout RelativeLayoutOR = new RelativeLayout(context);
        RelativeLayoutOR.setLayoutParams(RelativeLayoutORParam);
        RelativeLayoutOR.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain.addView(RelativeLayoutOR);

        RelativeLayout.LayoutParams TextViewORParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 40), RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewORParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        TextView TextViewOR = new TextView(context);
        TextViewOR.setLayoutParams(TextViewORParam);
        TextViewOR.setTypeface(null, Typeface.BOLD);
        TextViewOR.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        TextViewOR.setText(getString(R.string.ActivityWelcomeOR));
        TextViewOR.setId(MiscHandler.GenerateViewID());
        TextViewOR.setGravity(Gravity.CENTER);
        TextViewOR.setTextColor(ContextCompat.getColor(context, R.color.BlueGray));

        RelativeLayoutOR.addView(TextViewOR);

        RelativeLayout.LayoutParams ViewOrLine1Param = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 115), MiscHandler.ToDimension(context, 1));
        ViewOrLine1Param.addRule(RelativeLayout.RIGHT_OF, TextViewOR.getId());
        ViewOrLine1Param.addRule(RelativeLayout.CENTER_VERTICAL);

        View ViewOrLine1 = new View(context);
        ViewOrLine1.setLayoutParams(ViewOrLine1Param);
        ViewOrLine1.setBackgroundResource(R.color.BlueGray);

        RelativeLayoutOR.addView(ViewOrLine1);

        RelativeLayout.LayoutParams ViewOrLine2Param = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 115), MiscHandler.ToDimension(context, 1));
        ViewOrLine2Param.addRule(RelativeLayout.LEFT_OF, TextViewOR.getId());
        ViewOrLine2Param.addRule(RelativeLayout.CENTER_VERTICAL);

        View ViewOrLine2 = new View(context);
        ViewOrLine2.setLayoutParams(ViewOrLine2Param);
        ViewOrLine2.setBackgroundResource(R.color.BlueGray);

        RelativeLayoutOR.addView(ViewOrLine2);

        RelativeLayout.LayoutParams LinearLayoutGoogleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LinearLayoutGoogleParam.addRule(RelativeLayout.BELOW, RelativeLayoutOR.getId());
        LinearLayoutGoogleParam.addRule(RelativeLayout.CENTER_HORIZONTAL);

        LinearLayout LinearLayoutGoogle = new LinearLayout(context);
        LinearLayoutGoogle.setLayoutParams(LinearLayoutGoogleParam);
        LinearLayoutGoogle.setGravity(Gravity.CENTER);
        LinearLayoutGoogle.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayoutGoogle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivityForResult(Auth.GoogleSignInApi.getSignInIntent(GoogleClient), 100);
            }
        });

        RelativeLayoutMain.addView(LinearLayoutGoogle);

        LinearLayout.LayoutParams ImageViewGoogleParam = new LinearLayout.LayoutParams(MiscHandler.ToDimension(context, 30), MiscHandler.ToDimension(context, 30));
        ImageViewGoogleParam.setMargins(MiscHandler.ToDimension(context, 5), 0, MiscHandler.ToDimension(context, 5), 0);

        ImageView ImageViewGoogle = new ImageView(context);
        ImageViewGoogle.setLayoutParams(ImageViewGoogleParam);
        ImageViewGoogle.setBackgroundResource(R.drawable.ic_google);

        LinearLayoutGoogle.addView(ImageViewGoogle);

        TextView TextViewGoogle = new TextView(context);
        TextViewGoogle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        TextViewGoogle.setTextColor(ContextCompat.getColor(context, R.color.BlueLight));
        TextViewGoogle.setTypeface(null, Typeface.BOLD);
        TextViewGoogle.setText(getString(R.string.ActivityWelcomeSignInGoogle));
        TextViewGoogle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

        LinearLayoutGoogle.addView(TextViewGoogle);

        RelativeLayout.LayoutParams LinearLayoutSignInParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56));
        LinearLayoutSignInParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        LinearLayout LinearLayoutSignIn = new LinearLayout(context);
        LinearLayoutSignIn.setLayoutParams(LinearLayoutSignInParam);
        LinearLayoutSignIn.setBackgroundResource(R.color.White5);
        LinearLayoutSignIn.setGravity(Gravity.CENTER);
        LinearLayoutSignIn.setId(MiscHandler.GenerateViewID());
        LinearLayoutSignIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getSupportFragmentManager().beginTransaction().replace(FrameLayoutContainerID, new FragmentSignIn()).addToBackStack("FragmentSignIn").commit();
            }
        });

        RelativeLayoutMain.addView(LinearLayoutSignIn);

        TextView TextViewSignIn = new TextView(context);
        TextViewSignIn.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewSignIn.setTextColor(ContextCompat.getColor(context, R.color.Gray4));
        TextViewSignIn.setText(getString(R.string.ActivityWelcomeSignIn));
        TextViewSignIn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

        LinearLayoutSignIn.addView(TextViewSignIn);

        LinearLayout.LayoutParams TextViewSignIn2Param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        TextViewSignIn2Param.setMargins(MiscHandler.ToDimension(context, 5), 0, MiscHandler.ToDimension(context, 5), 0);

        TextView TextViewSignIn2 = new TextView(context);
        TextViewSignIn2.setLayoutParams(TextViewSignIn2Param);
        TextViewSignIn2.setTextColor(ContextCompat.getColor(context, R.color.BlueLight));
        TextViewSignIn2.setText(getString(R.string.ActivityWelcomeSignIn2));
        TextViewSignIn2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

        LinearLayoutSignIn.addView(TextViewSignIn2);

        RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1));
        ViewLineParam.addRule(RelativeLayout.ABOVE, LinearLayoutSignIn.getId());

        View ViewLine = new View(context);
        ViewLine.setLayoutParams(ViewLineParam);
        ViewLine.setBackgroundResource(R.color.Gray2);
        ViewLine.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain.addView(ViewLine);

        RelativeLayout.LayoutParams TextViewTerm2Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTerm2Param.addRule(RelativeLayout.ABOVE, ViewLine.getId());
        TextViewTerm2Param.addRule(RelativeLayout.CENTER_HORIZONTAL);
        TextViewTerm2Param.setMargins(0, 0, 0, MiscHandler.ToDimension(context, 20));

        TextView TextViewTerm2 = new TextView(context);
        TextViewTerm2.setLayoutParams(TextViewTerm2Param);
        TextViewTerm2.setTextColor(ContextCompat.getColor(context, R.color.BlueGray));
        TextViewTerm2.setText(getString(R.string.ActivityWelcomeTerm2));
        TextViewTerm2.setTypeface(null, Typeface.BOLD);
        TextViewTerm2.setId(MiscHandler.GenerateViewID());
        TextViewTerm2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        TextViewTerm2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com")));
            }
        });

        RelativeLayoutMain.addView(TextViewTerm2);

        RelativeLayout.LayoutParams TextViewTermParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTermParam.addRule(RelativeLayout.ABOVE, TextViewTerm2.getId());
        TextViewTermParam.addRule(RelativeLayout.CENTER_HORIZONTAL);

        TextView TextViewTerm = new TextView(context);
        TextViewTerm.setLayoutParams(TextViewTermParam);
        TextViewTerm.setTextColor(ContextCompat.getColor(context, R.color.BlueGray));
        TextViewTerm.setText(getString(R.string.ActivityWelcomeTerm));
        TextViewTerm.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

        RelativeLayoutMain.addView(TextViewTerm);

        FrameLayout FrameLayoutContainer = new FrameLayout(context);
        FrameLayoutContainer.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        FrameLayoutContainer.setId(FrameLayoutContainerID);

        RelativeLayoutMain.addView(FrameLayoutContainer);

        setContentView(Root);

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
                    final Context context = ActivityWelcome.this;

                    AndroidNetworking.post(MiscHandler.GetRandomServer("SignInGoogle"))
                    .addBodyParameter("Token", Acc.getIdToken())
                    .addBodyParameter("Session", GenerateSession())
                    .setTag("ActivityWelcome")
                    .build().getAsString(new StringRequestListener()
                    {
                        @Override
                        public void onResponse(String Response)
                        {
                            try
                            {
                                JSONObject Result = new JSONObject(Response);

                                if (Result.getInt("Message") == 1000)
                                {
                                    SharedHandler.SetBoolean(context, "IsLogin", true);
                                    SharedHandler.SetString(context, "TOKEN", Result.getString("Token"));
                                    SharedHandler.SetString(context, "ID", Result.getString("ID"));
                                    SharedHandler.SetString(context, "Username", Result.getString("Username"));
                                    SharedHandler.SetString(context, "Avatar", Result.getString("Avatar"));

                                    startActivity(new Intent(context, ActivityMain.class));
                                    finish();
                                    return;
                                }

                                MiscHandler.Toast(context, getString(R.string.ActivityWelcomeGoogleError));
                            }
                            catch (Exception e)
                            {
                                // Leave Me Alone
                            }
                        }

                        @Override
                        public void onError(ANError anError)
                        {
                            MiscHandler.Toast(context, getString(R.string.NoInternet));
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
        MiscHandler.HideSoftKey(this);
        AndroidNetworking.cancel("ActivityWelcome");
    }

    public static class FragmentSignUpUsername extends Fragment
    {
        private ScrollView Root;
        private ViewTreeObserver.OnGlobalLayoutListener RootListener;

        private Button ButtonUsername;
        private LoadingView LoadingViewUsername;

        private int LastDifferenceHeight = 0;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup Parent, Bundle savedInstanceState)
        {
            final Context context = getActivity();

            Root = new ScrollView(context);
            Root.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            Root.setBackgroundResource(R.color.White);
            Root.setHorizontalScrollBarEnabled(false);
            Root.setVerticalScrollBarEnabled(false);
            Root.setFillViewport(true);
            Root.setClickable(true);

            RootListener = new ViewTreeObserver.OnGlobalLayoutListener()
            {
                @Override
                public void onGlobalLayout()
                {
                    Rect rect = new Rect();
                    Root.getWindowVisibleDisplayFrame(rect);

                    int ScreenHeight = Root.getHeight();
                    int DifferenceHeight = ScreenHeight - (rect.bottom - rect.top);

                    if (DifferenceHeight > ScreenHeight / 3 && DifferenceHeight != LastDifferenceHeight)
                    {
                        Root.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, ScreenHeight - DifferenceHeight));
                        LastDifferenceHeight = DifferenceHeight;
                    }
                    else if (DifferenceHeight != LastDifferenceHeight)
                    {
                        Root.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, ScreenHeight));
                        LastDifferenceHeight = DifferenceHeight;
                    }
                    else if (LastDifferenceHeight != 0)
                    {
                        Root.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, ScreenHeight + Math.abs(LastDifferenceHeight)));
                        LastDifferenceHeight = 0;
                    }
                }
            };

            RelativeLayout RelativeLayoutMain = new RelativeLayout(context);
            RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

            Root.addView(RelativeLayoutMain);

            RelativeLayout RelativeLayoutHeader = new RelativeLayout(context);
            RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56)));
            RelativeLayoutHeader.setBackgroundResource(R.color.BlueLight);
            RelativeLayoutHeader.setId(MiscHandler.GenerateViewID());

            RelativeLayoutMain.addView(RelativeLayoutHeader);

            ImageView ImageViewBack = new ImageView(context);
            ImageViewBack.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), RelativeLayout.LayoutParams.MATCH_PARENT));
            ImageViewBack.setScaleType(ImageView.ScaleType.FIT_XY);
            ImageViewBack.setId(MiscHandler.GenerateViewID());
            ImageViewBack.setImageResource(R.drawable.ic_back_white);
            ImageViewBack.setPadding(MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12));
            ImageViewBack.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    MiscHandler.HideSoftKey(getActivity());
                    getActivity().onBackPressed();
                }
            });

            RelativeLayoutHeader.addView(ImageViewBack);

            RelativeLayout.LayoutParams TextViewHeaderParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewHeaderParam.addRule(RelativeLayout.RIGHT_OF, ImageViewBack.getId());
            TextViewHeaderParam.addRule(RelativeLayout.CENTER_VERTICAL);

            TextView TextViewHeader = new TextView(context);
            TextViewHeader.setLayoutParams(TextViewHeaderParam);
            TextViewHeader.setTextColor(ContextCompat.getColor(context, R.color.White));
            TextViewHeader.setText(getString(R.string.FragmentSignUpUsername));
            TextViewHeader.setTypeface(null, Typeface.BOLD);
            TextViewHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

            RelativeLayoutHeader.addView(TextViewHeader);

            RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1));
            ViewLineParam.addRule(RelativeLayout.BELOW, RelativeLayoutHeader.getId());

            View ViewLine = new View(context);
            ViewLine.setLayoutParams(ViewLineParam);
            ViewLine.setBackgroundResource(R.color.Gray2);
            ViewLine.setId(MiscHandler.GenerateViewID());

            RelativeLayoutMain.addView(ViewLine);

            RelativeLayout.LayoutParams TextViewUsernameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewUsernameParam.addRule(RelativeLayout.BELOW, ViewLine.getId());
            TextViewUsernameParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));

            TextView TextViewUsername = new TextView(context);
            TextViewUsername.setLayoutParams(TextViewUsernameParam);
            TextViewUsername.setTextColor(ContextCompat.getColor(context, R.color.Gray4));
            TextViewUsername.setText(getString(R.string.FragmentSignUpUsername));
            TextViewUsername.setTypeface(null, Typeface.BOLD);
            TextViewUsername.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            TextViewUsername.setId(MiscHandler.GenerateViewID());

            RelativeLayoutMain.addView(TextViewUsername);

            RelativeLayout.LayoutParams EditTextUsernameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            EditTextUsernameParam.addRule(RelativeLayout.BELOW, TextViewUsername.getId());
            EditTextUsernameParam.setMargins(MiscHandler.ToDimension(context, 10), 0, MiscHandler.ToDimension(context, 10), 0);

            final EditText EditTextUsername = new EditText(new ContextThemeWrapper(context, R.style.GeneralEditTextTheme));
            EditTextUsername.setLayoutParams(EditTextUsernameParam);
            EditTextUsername.setMaxLines(1);
            EditTextUsername.setId(MiscHandler.GenerateViewID());
            EditTextUsername.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            EditTextUsername.setFilters(new InputFilter[]
            {
                new InputFilter.LengthFilter(32), new InputFilter()
                {
                    @Override
                    public CharSequence filter(CharSequence Source, int Start, int End, Spanned Dest, int DestStart, int DestEnd)
                    {
                        if (End > Start)
                        {
                            char[] AllowChar = new char[] {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '.', '_', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

                            for (int I = Start; I < End; I++)
                            {
                                if (!new String(AllowChar).contains(String.valueOf(Source.charAt(I))))
                                {
                                    return "";
                                }
                            }
                        }

                        return null;
                    }
                }
            });
            EditTextUsername.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            EditTextUsername.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.BlueLight), PorterDuff.Mode.SRC_ATOP);
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
                        ButtonUsername.setEnabled(true);
                    else
                        ButtonUsername.setEnabled(false);
                }
            });

            RelativeLayoutMain.addView(EditTextUsername);

            RelativeLayout.LayoutParams RelativeLayoutContentParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            RelativeLayoutContentParam.addRule(RelativeLayout.BELOW, EditTextUsername.getId());
            RelativeLayoutContentParam.setMargins(0, 0, 0, MiscHandler.ToDimension(context, 5));

            RelativeLayout RelativeLayoutContent = new RelativeLayout(context);
            RelativeLayoutContent.setLayoutParams(RelativeLayoutContentParam);

            RelativeLayoutMain.addView(RelativeLayoutContent);

            TextView TextViewMessage = new TextView(context);
            TextViewMessage.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            TextViewMessage.setTextColor(ContextCompat.getColor(context, R.color.Black));
            TextViewMessage.setText(getString(R.string.FragmentSignUpUsernameMessage));
            TextViewMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            TextViewMessage.setId(MiscHandler.GenerateViewID());
            TextViewMessage.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));

            RelativeLayoutContent.addView(TextViewMessage);

            RelativeLayout.LayoutParams RelativeLayoutContent2Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            RelativeLayoutContent2Param.addRule(RelativeLayout.BELOW, TextViewMessage.getId());

            RelativeLayout RelativeLayoutContent2 = new RelativeLayout(context);
            RelativeLayoutContent2.setLayoutParams(RelativeLayoutContent2Param);

            RelativeLayoutContent.addView(RelativeLayoutContent2);

            RelativeLayout.LayoutParams TextViewPrivacyParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewPrivacyParam.addRule(RelativeLayout.CENTER_VERTICAL);
            TextViewPrivacyParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

            TextView TextViewPrivacy = new TextView(context);
            TextViewPrivacy.setLayoutParams(TextViewPrivacyParam);
            TextViewPrivacy.setTextColor(ContextCompat.getColor(context, R.color.BlueLight));
            TextViewPrivacy.setText(getString(R.string.ActivityWelcomeGeneralTerm));
            TextViewPrivacy.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            TextViewPrivacy.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
            TextViewPrivacy.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com")));
                }
            });

            RelativeLayoutContent2.addView(TextViewPrivacy);

            RelativeLayout.LayoutParams RelativeLayoutNextParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 90), MiscHandler.ToDimension(context, 35));
            RelativeLayoutNextParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            RelativeLayoutNextParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));

            GradientDrawable ShapeEnable = new GradientDrawable();
            ShapeEnable.setColor(ContextCompat.getColor(context, R.color.BlueLight));
            ShapeEnable.setCornerRadius(MiscHandler.ToDimension(context, 7));

            RelativeLayout RelativeLayoutNext = new RelativeLayout(context);
            RelativeLayoutNext.setLayoutParams(RelativeLayoutNextParam);
            RelativeLayoutNext.setBackground(ShapeEnable);

            RelativeLayoutContent2.addView(RelativeLayoutNext);

            GradientDrawable ShapeDisable = new GradientDrawable();
            ShapeDisable.setCornerRadius(MiscHandler.ToDimension(context, 7));
            ShapeDisable.setColor(ContextCompat.getColor(context, R.color.Gray2));

            StateListDrawable StateUsername = new StateListDrawable();
            StateUsername.addState(new int[] {android.R.attr.state_enabled}, ShapeEnable);
            StateUsername.addState(new int[] {-android.R.attr.state_enabled}, ShapeDisable);

            ButtonUsername = new Button(context, null, android.R.attr.borderlessButtonStyle);
            ButtonUsername.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 90), MiscHandler.ToDimension(context, 35)));
            ButtonUsername.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            ButtonUsername.setTextColor(ContextCompat.getColor(context, R.color.White));
            ButtonUsername.setText(getString(R.string.ActivityWelcomeGeneralNext));
            ButtonUsername.setBackground(StateUsername);
            ButtonUsername.setPadding(0, 0, 0, 0);
            ButtonUsername.setEnabled(false);
            ButtonUsername.setAllCaps(false);
            ButtonUsername.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    ButtonUsername.setVisibility(View.INVISIBLE);
                    LoadingViewUsername.Start();

                    AndroidNetworking.post(MiscHandler.GetRandomServer("UsernameIsAvailable"))
                    .addBodyParameter("Username", EditTextUsername.getText().toString())
                    .setTag("FragmentSignUpUsername")
                    .build().getAsString(new StringRequestListener()
                    {
                        @Override
                        public void onResponse(String Response)
                        {MiscHandler.Debug("AA" + Response);
                            ButtonUsername.setVisibility(View.VISIBLE);
                            LoadingViewUsername.Stop();

                            try
                            {
                                if (new JSONObject(Response).getInt("Message") == 1000)
                                {
                                    ActivityWelcome Parent = (ActivityWelcome) getActivity();
                                    Parent.Username = EditTextUsername.getText().toString();
                                    MiscHandler.HideSoftKey(Parent);
                                    Parent.getSupportFragmentManager().beginTransaction().replace(Parent.FrameLayoutContainerID, new FragmentSignUpPassword()).addToBackStack("FragmentSignUpPassword").commit();
                                    return;
                                }

                                MiscHandler.Toast(context, getString(R.string.FragmentSignUpUsernameError));
                            }
                            catch (Exception e)
                            {MiscHandler.Debug("AA" + e.toString());
                                // Leave Me Alone
                            }
                        }

                        @Override
                        public void onError(ANError anError)
                        {MiscHandler.Debug("AA" + anError.toString());
                            ButtonUsername.setVisibility(View.VISIBLE);
                            LoadingViewUsername.Stop();

                            MiscHandler.Toast(context, getString(R.string.NoInternet));
                        }
                    });
                }
            });

            RelativeLayoutNext.addView(ButtonUsername);

            RelativeLayout.LayoutParams LoadingViewUsernameParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 90), MiscHandler.ToDimension(context, 35));
            LoadingViewUsernameParam.addRule(RelativeLayout.CENTER_IN_PARENT);

            LoadingViewUsername = new LoadingView(context);
            LoadingViewUsername.setLayoutParams(LoadingViewUsernameParam);
            LoadingViewUsername.SetColor(R.color.White);

            RelativeLayoutNext.addView(LoadingViewUsername);

            return Root;
        }

        @Override
        public void onResume()
        {
            super.onResume();

            Root.getViewTreeObserver().addOnGlobalLayoutListener(RootListener);

            InputMethodManager IMM = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            IMM.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }

        @Override
        public void onPause()
        {
            super.onPause();
            AndroidNetworking.cancel("FragmentSignUpUsername");
            Root.getViewTreeObserver().removeOnGlobalLayoutListener(RootListener);
        }
    }

    public static class FragmentSignUpPassword extends Fragment
    {
        private ScrollView Root;
        private ViewTreeObserver.OnGlobalLayoutListener RootListener;

        private Button ButtonPassword;
        private int LastDifferenceHeight = 0;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup Parent, Bundle savedInstanceState)
        {
            final Context context = getActivity();

            Root = new ScrollView(context);
            Root.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            Root.setBackgroundResource(R.color.White);
            Root.setHorizontalScrollBarEnabled(false);
            Root.setVerticalScrollBarEnabled(false);
            Root.setFillViewport(true);
            Root.setClickable(true);

            RootListener = new ViewTreeObserver.OnGlobalLayoutListener()
            {
                @Override
                public void onGlobalLayout()
                {
                    Rect rect = new Rect();
                    Root.getWindowVisibleDisplayFrame(rect);

                    int ScreenHeight = Root.getHeight();
                    int DifferenceHeight = ScreenHeight - (rect.bottom - rect.top);

                    if (DifferenceHeight > ScreenHeight / 3 && DifferenceHeight != LastDifferenceHeight)
                    {
                        Root.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, ScreenHeight - DifferenceHeight));
                        LastDifferenceHeight = DifferenceHeight;
                    }
                    else if (DifferenceHeight != LastDifferenceHeight)
                    {
                        Root.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, ScreenHeight));
                        LastDifferenceHeight = DifferenceHeight;
                    }
                    else if (LastDifferenceHeight != 0)
                    {
                        Root.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, ScreenHeight + Math.abs(LastDifferenceHeight)));
                        LastDifferenceHeight = 0;
                    }
                }
            };

            RelativeLayout RelativeLayoutMain = new RelativeLayout(context);
            RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

            Root.addView(RelativeLayoutMain);

            RelativeLayout RelativeLayoutHeader = new RelativeLayout(context);
            RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56)));
            RelativeLayoutHeader.setBackgroundResource(R.color.BlueLight);
            RelativeLayoutHeader.setId(MiscHandler.GenerateViewID());

            RelativeLayoutMain.addView(RelativeLayoutHeader);

            ImageView ImageViewBack = new ImageView(context);
            ImageViewBack.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), RelativeLayout.LayoutParams.MATCH_PARENT));
            ImageViewBack.setScaleType(ImageView.ScaleType.FIT_XY);
            ImageViewBack.setId(MiscHandler.GenerateViewID());
            ImageViewBack.setImageResource(R.drawable.ic_back_white);
            ImageViewBack.setPadding(MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12));
            ImageViewBack.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    MiscHandler.HideSoftKey(getActivity());
                    getActivity().onBackPressed();
                }
            });

            RelativeLayoutHeader.addView(ImageViewBack);

            RelativeLayout.LayoutParams TextViewHeaderParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewHeaderParam.addRule(RelativeLayout.RIGHT_OF, ImageViewBack.getId());
            TextViewHeaderParam.addRule(RelativeLayout.CENTER_VERTICAL);

            TextView TextViewHeader = new TextView(context);
            TextViewHeader.setLayoutParams(TextViewHeaderParam);
            TextViewHeader.setTextColor(ContextCompat.getColor(context, R.color.White));
            TextViewHeader.setText(getString(R.string.FragmentSignUpPassword));
            TextViewHeader.setTypeface(null, Typeface.BOLD);
            TextViewHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

            RelativeLayoutHeader.addView(TextViewHeader);

            RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1));
            ViewLineParam.addRule(RelativeLayout.BELOW, RelativeLayoutHeader.getId());

            View ViewLine = new View(context);
            ViewLine.setLayoutParams(ViewLineParam);
            ViewLine.setBackgroundResource(R.color.Gray2);
            ViewLine.setId(MiscHandler.GenerateViewID());

            RelativeLayoutMain.addView(ViewLine);

            RelativeLayout.LayoutParams TextViewUsernameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewUsernameParam.addRule(RelativeLayout.BELOW, ViewLine.getId());
            TextViewUsernameParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));

            TextView TextViewPassword = new TextView(context);
            TextViewPassword.setLayoutParams(TextViewUsernameParam);
            TextViewPassword.setTextColor(ContextCompat.getColor(context, R.color.Gray4));
            TextViewPassword.setText(getString(R.string.FragmentSignUpPassword));
            TextViewPassword.setTypeface(null, Typeface.BOLD);
            TextViewPassword.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            TextViewPassword.setId(MiscHandler.GenerateViewID());

            RelativeLayoutMain.addView(TextViewPassword);

            RelativeLayout.LayoutParams EditTextPasswordParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            EditTextPasswordParam.addRule(RelativeLayout.BELOW, TextViewPassword.getId());
            EditTextPasswordParam.setMargins(MiscHandler.ToDimension(context, 10), 0, MiscHandler.ToDimension(context, 10), 0);

            final EditText EditTextPassword = new EditText(new ContextThemeWrapper(context, R.style.GeneralEditTextTheme));
            EditTextPassword.setLayoutParams(EditTextPasswordParam);
            EditTextPassword.setMaxLines(1);
            EditTextPassword.setId(MiscHandler.GenerateViewID());
            EditTextPassword.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            EditTextPassword.setFilters(new InputFilter[] { new InputFilter.LengthFilter(32) });
            EditTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            EditTextPassword.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.BlueLight), PorterDuff.Mode.SRC_ATOP);
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
                        ButtonPassword.setEnabled(true);
                    else
                        ButtonPassword.setEnabled(false);
                }
            });

            RelativeLayoutMain.addView(EditTextPassword);

            RelativeLayout.LayoutParams RelativeLayoutContentParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            RelativeLayoutContentParam.addRule(RelativeLayout.BELOW, EditTextPassword.getId());
            RelativeLayoutContentParam.setMargins(0, 0, 0, MiscHandler.ToDimension(context, 5));

            RelativeLayout RelativeLayoutContent = new RelativeLayout(context);
            RelativeLayoutContent.setLayoutParams(RelativeLayoutContentParam);

            RelativeLayoutMain.addView(RelativeLayoutContent);

            TextView TextViewMessage = new TextView(context);
            TextViewMessage.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            TextViewMessage.setTextColor(ContextCompat.getColor(context, R.color.Black));
            TextViewMessage.setText(getString(R.string.FragmentSignUpPasswordMessage));
            TextViewMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            TextViewMessage.setId(MiscHandler.GenerateViewID());
            TextViewMessage.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));

            RelativeLayoutContent.addView(TextViewMessage);

            RelativeLayout.LayoutParams RelativeLayoutContent2Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            RelativeLayoutContent2Param.addRule(RelativeLayout.BELOW, TextViewMessage.getId());

            RelativeLayout RelativeLayoutContent2 = new RelativeLayout(context);
            RelativeLayoutContent2.setLayoutParams(RelativeLayoutContent2Param);

            RelativeLayoutContent.addView(RelativeLayoutContent2);

            RelativeLayout.LayoutParams TextViewPrivacyParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewPrivacyParam.addRule(RelativeLayout.CENTER_VERTICAL);
            TextViewPrivacyParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

            TextView TextViewPrivacy = new TextView(context);
            TextViewPrivacy.setLayoutParams(TextViewPrivacyParam);
            TextViewPrivacy.setTextColor(ContextCompat.getColor(context, R.color.BlueLight));
            TextViewPrivacy.setText(getString(R.string.ActivityWelcomeGeneralTerm));
            TextViewPrivacy.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            TextViewPrivacy.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
            TextViewPrivacy.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com")));
                }
            });

            RelativeLayoutContent2.addView(TextViewPrivacy);

            RelativeLayout.LayoutParams RelativeLayoutNextParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 90), MiscHandler.ToDimension(context, 35));
            RelativeLayoutNextParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            RelativeLayoutNextParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));

            GradientDrawable ShapeEnable = new GradientDrawable();
            ShapeEnable.setColor(ContextCompat.getColor(context, R.color.BlueLight));
            ShapeEnable.setCornerRadius(MiscHandler.ToDimension(context, 7));

            RelativeLayout RelativeLayoutNext = new RelativeLayout(context);
            RelativeLayoutNext.setLayoutParams(RelativeLayoutNextParam);
            RelativeLayoutNext.setBackground(ShapeEnable);

            RelativeLayoutContent2.addView(RelativeLayoutNext);

            GradientDrawable ShapeDisable = new GradientDrawable();
            ShapeDisable.setCornerRadius(MiscHandler.ToDimension(context, 7));
            ShapeDisable.setColor(ContextCompat.getColor(context, R.color.Gray2));

            StateListDrawable StatePassword = new StateListDrawable();
            StatePassword.addState(new int[] {android.R.attr.state_enabled}, ShapeEnable);
            StatePassword.addState(new int[] {-android.R.attr.state_enabled}, ShapeDisable);

            ButtonPassword = new Button(context, null, android.R.attr.borderlessButtonStyle);
            ButtonPassword.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 90), MiscHandler.ToDimension(context, 35)));
            ButtonPassword.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            ButtonPassword.setTextColor(ContextCompat.getColor(context, R.color.White));
            ButtonPassword.setText(getString(R.string.ActivityWelcomeGeneralNext));
            ButtonPassword.setBackground(StatePassword);
            ButtonPassword.setPadding(0, 0, 0, 0);
            ButtonPassword.setEnabled(false);
            ButtonPassword.setAllCaps(false);
            ButtonPassword.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    ActivityWelcome Parent = (ActivityWelcome) getActivity();
                    Parent.Password = EditTextPassword.getText().toString();
                    MiscHandler.HideSoftKey(Parent);
                    Parent.getSupportFragmentManager().beginTransaction().replace(Parent.FrameLayoutContainerID, new FragmentSignUpEmail()).addToBackStack("FragmentSignUpEmail").commit();
                }
            });

            RelativeLayoutNext.addView(ButtonPassword);

            return Root;
        }

        @Override
        public void onResume()
        {
            super.onResume();

            Root.getViewTreeObserver().addOnGlobalLayoutListener(RootListener);

            InputMethodManager IMM = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            IMM.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }

        @Override
        public void onPause()
        {
            super.onPause();
            Root.getViewTreeObserver().removeOnGlobalLayoutListener(RootListener);
        }
    }

    public static class FragmentSignUpEmail extends Fragment
    {
        private ScrollView Root;
        private ViewTreeObserver.OnGlobalLayoutListener RootListener;

        private Button ButtonEmail;
        private LoadingView LoadingViewEmail;

        private int LastDifferenceHeight = 0;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup Parent, Bundle savedInstanceState)
        {
            final Context context = getActivity();

            Root = new ScrollView(context);
            Root.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            Root.setBackgroundResource(R.color.White);
            Root.setHorizontalScrollBarEnabled(false);
            Root.setVerticalScrollBarEnabled(false);
            Root.setFillViewport(true);
            Root.setClickable(true);

            RootListener = new ViewTreeObserver.OnGlobalLayoutListener()
            {
                @Override
                public void onGlobalLayout()
                {
                    Rect rect = new Rect();
                    Root.getWindowVisibleDisplayFrame(rect);

                    int ScreenHeight = Root.getHeight();
                    int DifferenceHeight = ScreenHeight - (rect.bottom - rect.top);

                    if (DifferenceHeight > ScreenHeight / 3 && DifferenceHeight != LastDifferenceHeight)
                    {
                        Root.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, ScreenHeight - DifferenceHeight));
                        LastDifferenceHeight = DifferenceHeight;
                    }
                    else if (DifferenceHeight != LastDifferenceHeight)
                    {
                        Root.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, ScreenHeight));
                        LastDifferenceHeight = DifferenceHeight;
                    }
                    else if (LastDifferenceHeight != 0)
                    {
                        Root.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, ScreenHeight + Math.abs(LastDifferenceHeight)));
                        LastDifferenceHeight = 0;
                    }
                }
            };

            RelativeLayout RelativeLayoutMain = new RelativeLayout(context);
            RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

            Root.addView(RelativeLayoutMain);

            RelativeLayout RelativeLayoutHeader = new RelativeLayout(context);
            RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56)));
            RelativeLayoutHeader.setBackgroundResource(R.color.BlueLight);
            RelativeLayoutHeader.setId(MiscHandler.GenerateViewID());

            RelativeLayoutMain.addView(RelativeLayoutHeader);

            ImageView ImageViewBack = new ImageView(context);
            ImageViewBack.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), RelativeLayout.LayoutParams.MATCH_PARENT));
            ImageViewBack.setScaleType(ImageView.ScaleType.FIT_XY);
            ImageViewBack.setId(MiscHandler.GenerateViewID());
            ImageViewBack.setImageResource(R.drawable.ic_back_white);
            ImageViewBack.setPadding(MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12));
            ImageViewBack.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    MiscHandler.HideSoftKey(getActivity());
                    getActivity().onBackPressed();
                }
            });

            RelativeLayoutHeader.addView(ImageViewBack);

            RelativeLayout.LayoutParams TextViewHeaderParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewHeaderParam.addRule(RelativeLayout.RIGHT_OF, ImageViewBack.getId());
            TextViewHeaderParam.addRule(RelativeLayout.CENTER_VERTICAL);

            TextView TextViewHeader = new TextView(context);
            TextViewHeader.setLayoutParams(TextViewHeaderParam);
            TextViewHeader.setTextColor(ContextCompat.getColor(context, R.color.White));
            TextViewHeader.setText(getString(R.string.FragmentSignUpEmailTitle));
            TextViewHeader.setTypeface(null, Typeface.BOLD);
            TextViewHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

            RelativeLayoutHeader.addView(TextViewHeader);

            RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1));
            ViewLineParam.addRule(RelativeLayout.BELOW, RelativeLayoutHeader.getId());

            View ViewLine = new View(context);
            ViewLine.setLayoutParams(ViewLineParam);
            ViewLine.setBackgroundResource(R.color.Gray2);
            ViewLine.setId(MiscHandler.GenerateViewID());

            RelativeLayoutMain.addView(ViewLine);

            RelativeLayout.LayoutParams TextViewUsernameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewUsernameParam.addRule(RelativeLayout.BELOW, ViewLine.getId());
            TextViewUsernameParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));

            TextView TextViewEmail = new TextView(context);
            TextViewEmail.setLayoutParams(TextViewUsernameParam);
            TextViewEmail.setTextColor(ContextCompat.getColor(context, R.color.Gray4));
            TextViewEmail.setText(getString(R.string.FragmentSignUpEmail));
            TextViewEmail.setTypeface(null, Typeface.BOLD);
            TextViewEmail.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            TextViewEmail.setId(MiscHandler.GenerateViewID());

            RelativeLayoutMain.addView(TextViewEmail);

            RelativeLayout.LayoutParams EditTextEmailParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            EditTextEmailParam.addRule(RelativeLayout.BELOW, TextViewEmail.getId());
            EditTextEmailParam.setMargins(MiscHandler.ToDimension(context, 10), 0, MiscHandler.ToDimension(context, 10), 0);

            final EditText EditTextEmail = new EditText(new ContextThemeWrapper(context, R.style.GeneralEditTextTheme));
            EditTextEmail.setLayoutParams(EditTextEmailParam);
            EditTextEmail.setMaxLines(1);
            EditTextEmail.setId(MiscHandler.GenerateViewID());
            EditTextEmail.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            EditTextEmail.setFilters(new InputFilter[] { new InputFilter.LengthFilter(64) });
            EditTextEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            EditTextEmail.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.BlueLight), PorterDuff.Mode.SRC_ATOP);
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
                        ButtonEmail.setEnabled(true);
                    else
                        ButtonEmail.setEnabled(false);
                }
            });

            RelativeLayoutMain.addView(EditTextEmail);

            RelativeLayout.LayoutParams RelativeLayoutContentParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            RelativeLayoutContentParam.addRule(RelativeLayout.BELOW, EditTextEmail.getId());
            RelativeLayoutContentParam.setMargins(0, 0, 0, MiscHandler.ToDimension(context, 5));

            RelativeLayout RelativeLayoutContent = new RelativeLayout(context);
            RelativeLayoutContent.setLayoutParams(RelativeLayoutContentParam);

            RelativeLayoutMain.addView(RelativeLayoutContent);

            TextView TextViewMessage = new TextView(context);
            TextViewMessage.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            TextViewMessage.setTextColor(ContextCompat.getColor(context, R.color.Black));
            TextViewMessage.setText(getString(R.string.FragmentSignUpEmailMessage));
            TextViewMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            TextViewMessage.setId(MiscHandler.GenerateViewID());
            TextViewMessage.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));

            RelativeLayoutContent.addView(TextViewMessage);

            RelativeLayout.LayoutParams RelativeLayoutContent2Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            RelativeLayoutContent2Param.addRule(RelativeLayout.BELOW, TextViewMessage.getId());

            RelativeLayout RelativeLayoutContent2 = new RelativeLayout(context);
            RelativeLayoutContent2.setLayoutParams(RelativeLayoutContent2Param);

            RelativeLayoutContent.addView(RelativeLayoutContent2);

            RelativeLayout.LayoutParams TextViewPrivacyParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewPrivacyParam.addRule(RelativeLayout.CENTER_VERTICAL);
            TextViewPrivacyParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

            TextView TextViewPrivacy = new TextView(context);
            TextViewPrivacy.setLayoutParams(TextViewPrivacyParam);
            TextViewPrivacy.setTextColor(ContextCompat.getColor(context, R.color.BlueLight));
            TextViewPrivacy.setText(getString(R.string.ActivityWelcomeGeneralTerm));
            TextViewPrivacy.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            TextViewPrivacy.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
            TextViewPrivacy.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com")));
                }
            });

            RelativeLayoutContent2.addView(TextViewPrivacy);

            RelativeLayout.LayoutParams RelativeLayoutNextParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 90), MiscHandler.ToDimension(context, 35));
            RelativeLayoutNextParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            RelativeLayoutNextParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));

            GradientDrawable ShapeEnable = new GradientDrawable();
            ShapeEnable.setColor(ContextCompat.getColor(context, R.color.BlueLight));
            ShapeEnable.setCornerRadius(MiscHandler.ToDimension(context, 7));

            RelativeLayout RelativeLayoutNext = new RelativeLayout(context);
            RelativeLayoutNext.setLayoutParams(RelativeLayoutNextParam);
            RelativeLayoutNext.setBackground(ShapeEnable);

            RelativeLayoutContent2.addView(RelativeLayoutNext);

            GradientDrawable ShapeDisable = new GradientDrawable();
            ShapeDisable.setCornerRadius(MiscHandler.ToDimension(context, 7));
            ShapeDisable.setColor(ContextCompat.getColor(context, R.color.Gray2));

            StateListDrawable StateEmail = new StateListDrawable();
            StateEmail.addState(new int[] {android.R.attr.state_enabled}, ShapeEnable);
            StateEmail.addState(new int[] {-android.R.attr.state_enabled}, ShapeDisable);

            ButtonEmail = new Button(context, null, android.R.attr.borderlessButtonStyle);
            ButtonEmail.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 90), MiscHandler.ToDimension(context, 35)));
            ButtonEmail.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            ButtonEmail.setTextColor(ContextCompat.getColor(context, R.color.White));
            ButtonEmail.setText(getString(R.string.FragmentSignUpEmailFinish));
            ButtonEmail.setBackground(StateEmail);
            ButtonEmail.setPadding(0, 0, 0, 0);
            ButtonEmail.setEnabled(false);
            ButtonEmail.setAllCaps(false);
            ButtonEmail.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    final ActivityWelcome Parent = (ActivityWelcome) getActivity();
                    ButtonEmail.setVisibility(View.INVISIBLE);
                    LoadingViewEmail.Start();

                    AndroidNetworking.post(MiscHandler.GetRandomServer("SignUp"))
                    .addBodyParameter("Username", Parent.Username)
                    .addBodyParameter("Password", Parent.Password)
                    .addBodyParameter("Email", EditTextEmail.getText().toString())
                    .addBodyParameter("Session", GenerateSession())
                    .setTag("FragmentSignUpEmail")
                    .build().getAsString(new StringRequestListener()
                    {
                        @Override
                        public void onResponse(String Response)
                        {
                            ButtonEmail.setVisibility(View.VISIBLE);
                            LoadingViewEmail.Stop();

                            try
                            {
                                JSONObject Result = new JSONObject(Response);

                                if (Result.getInt("Message") == 1000)
                                {
                                    SharedHandler.SetBoolean(context, "IsLogin", true);
                                    SharedHandler.SetString(context, "TOKEN", Result.getString("TOKEN"));
                                    SharedHandler.SetString(context, "ID", Result.getString("ID"));
                                    SharedHandler.SetString(context, "Username", Result.getString("Username"));
                                    SharedHandler.SetString(context, "Avatar", "");

                                    Parent.startActivity(new Intent(context, ActivityMain.class));
                                    Parent.finish();
                                    return;
                                }

                                MiscHandler.Toast(context, getString(R.string.FragmentSignUpEmailError));
                            }
                            catch (Exception e)
                            {
                                // Leave Me Alone
                            }
                        }

                        @Override
                        public void onError(ANError anError)
                        {
                            ButtonEmail.setVisibility(View.VISIBLE);
                            LoadingViewEmail.Stop();

                            MiscHandler.Toast(context, getString(R.string.NoInternet));
                        }
                    });
                }
            });

            RelativeLayoutNext.addView(ButtonEmail);

            RelativeLayout.LayoutParams LoadingViewEmailParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 90), MiscHandler.ToDimension(context, 35));
            LoadingViewEmailParam.addRule(RelativeLayout.CENTER_IN_PARENT);

            LoadingViewEmail = new LoadingView(context);
            LoadingViewEmail.setLayoutParams(LoadingViewEmailParam);
            LoadingViewEmail.SetColor(R.color.White);

            RelativeLayoutNext.addView(LoadingViewEmail);

            return Root;
        }

        @Override
        public void onResume()
        {
            super.onResume();

            Root.getViewTreeObserver().addOnGlobalLayoutListener(RootListener);

            InputMethodManager IMM = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            IMM.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }

        @Override
        public void onPause()
        {
            super.onPause();
            AndroidNetworking.cancel("FragmentSignUpEmail");
            Root.getViewTreeObserver().removeOnGlobalLayoutListener(RootListener);
        }
    }

    public static class FragmentSignIn extends Fragment
    {
        private ScrollView Root;
        private ViewTreeObserver.OnGlobalLayoutListener RootListener;

        private Button ButtonSignIn;
        private LoadingView LoadingViewSignIn;

        private int LastDifferenceHeight = 0;

        private boolean RequestUsername = false;
        private boolean RequestPassword = false;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup Parent, Bundle savedInstanceState)
        {
            final Context context = getActivity();

            Root = new ScrollView(context);
            Root.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            Root.setBackgroundResource(R.color.White);
            Root.setHorizontalScrollBarEnabled(false);
            Root.setVerticalScrollBarEnabled(false);
            Root.setFillViewport(true);
            Root.setClickable(true);

            RootListener = new ViewTreeObserver.OnGlobalLayoutListener()
            {
                @Override
                public void onGlobalLayout()
                {
                    Rect rect = new Rect();
                    Root.getWindowVisibleDisplayFrame(rect);

                    int ScreenHeight = Root.getHeight();
                    int DifferenceHeight = ScreenHeight - (rect.bottom - rect.top);

                    if (DifferenceHeight > ScreenHeight / 3 && DifferenceHeight != LastDifferenceHeight)
                    {
                        Root.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, ScreenHeight - DifferenceHeight));
                        LastDifferenceHeight = DifferenceHeight;
                    }
                    else if (DifferenceHeight != LastDifferenceHeight)
                    {
                        Root.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, ScreenHeight));
                        LastDifferenceHeight = DifferenceHeight;
                    }
                    else if (LastDifferenceHeight != 0)
                    {
                        Root.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, ScreenHeight + Math.abs(LastDifferenceHeight)));
                        LastDifferenceHeight = 0;
                    }
                }
            };

            RelativeLayout RelativeLayoutMain = new RelativeLayout(context);
            RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

            Root.addView(RelativeLayoutMain);

            RelativeLayout RelativeLayoutHeader = new RelativeLayout(context);
            RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56)));
            RelativeLayoutHeader.setBackgroundResource(R.color.BlueLight);
            RelativeLayoutHeader.setId(MiscHandler.GenerateViewID());

            RelativeLayoutMain.addView(RelativeLayoutHeader);

            ImageView ImageViewBack = new ImageView(context);
            ImageViewBack.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), RelativeLayout.LayoutParams.MATCH_PARENT));
            ImageViewBack.setScaleType(ImageView.ScaleType.FIT_XY);
            ImageViewBack.setId(MiscHandler.GenerateViewID());
            ImageViewBack.setImageResource(R.drawable.ic_back_white);
            ImageViewBack.setPadding(MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12));
            ImageViewBack.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    MiscHandler.HideSoftKey(getActivity());
                    getActivity().onBackPressed();
                }
            });

            RelativeLayoutHeader.addView(ImageViewBack);

            RelativeLayout.LayoutParams TextViewHeaderParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewHeaderParam.addRule(RelativeLayout.RIGHT_OF, ImageViewBack.getId());
            TextViewHeaderParam.addRule(RelativeLayout.CENTER_VERTICAL);

            TextView TextViewHeader = new TextView(context);
            TextViewHeader.setLayoutParams(TextViewHeaderParam);
            TextViewHeader.setTextColor(ContextCompat.getColor(context, R.color.White));
            TextViewHeader.setText(getString(R.string.FragmentSignInTitle));
            TextViewHeader.setTypeface(null, Typeface.BOLD);
            TextViewHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

            RelativeLayoutHeader.addView(TextViewHeader);

            RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1));
            ViewLineParam.addRule(RelativeLayout.BELOW, RelativeLayoutHeader.getId());

            View ViewLine = new View(context);
            ViewLine.setLayoutParams(ViewLineParam);
            ViewLine.setBackgroundResource(R.color.Gray2);
            ViewLine.setId(MiscHandler.GenerateViewID());

            RelativeLayoutMain.addView(ViewLine);

            RelativeLayout.LayoutParams TextViewEmailOrUsernameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewEmailOrUsernameParam.addRule(RelativeLayout.BELOW, ViewLine.getId());
            TextViewEmailOrUsernameParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));

            TextView TextViewEmailOrUsername = new TextView(context);
            TextViewEmailOrUsername.setLayoutParams(TextViewEmailOrUsernameParam);
            TextViewEmailOrUsername.setTextColor(ContextCompat.getColor(context, R.color.Gray4));
            TextViewEmailOrUsername.setText(getString(R.string.FragmentSignInEmailOrUsername));
            TextViewEmailOrUsername.setTypeface(null, Typeface.BOLD);
            TextViewEmailOrUsername.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            TextViewEmailOrUsername.setId(MiscHandler.GenerateViewID());

            RelativeLayoutMain.addView(TextViewEmailOrUsername);

            RelativeLayout.LayoutParams EditTextEmailOrUsernameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            EditTextEmailOrUsernameParam.addRule(RelativeLayout.BELOW, TextViewEmailOrUsername.getId());
            EditTextEmailOrUsernameParam.setMargins(MiscHandler.ToDimension(context, 10), 0, MiscHandler.ToDimension(context, 10), 0);

            final EditText EditTextEmailOrUsername = new EditText(new ContextThemeWrapper(context, R.style.GeneralEditTextTheme));
            EditTextEmailOrUsername.setLayoutParams(EditTextEmailOrUsernameParam);
            EditTextEmailOrUsername.setMaxLines(1);
            EditTextEmailOrUsername.setId(MiscHandler.GenerateViewID());
            EditTextEmailOrUsername.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            EditTextEmailOrUsername.setFilters(new InputFilter[] { new InputFilter.LengthFilter(64) });
            EditTextEmailOrUsername.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            EditTextEmailOrUsername.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.BlueLight), PorterDuff.Mode.SRC_ATOP);
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
                    RequestUsername = (s.length() > 2);

                    if (RequestUsername && RequestPassword)
                        ButtonSignIn.setEnabled(true);
                    else
                        ButtonSignIn.setEnabled(false);
                }
            });

            RelativeLayoutMain.addView(EditTextEmailOrUsername);

            RelativeLayout.LayoutParams TextViewPasswordParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewPasswordParam.addRule(RelativeLayout.BELOW, EditTextEmailOrUsername.getId());
            TextViewPasswordParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));

            TextView TextViewPassword = new TextView(context);
            TextViewPassword.setLayoutParams(TextViewPasswordParam);
            TextViewPassword.setTextColor(ContextCompat.getColor(context, R.color.Gray4));
            TextViewPassword.setText(getString(R.string.FragmentSignInPassword));
            TextViewPassword.setTypeface(null, Typeface.BOLD);
            TextViewPassword.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            TextViewPassword.setId(MiscHandler.GenerateViewID());

            RelativeLayoutMain.addView(TextViewPassword);

            RelativeLayout.LayoutParams EditTextPasswordParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            EditTextPasswordParam.addRule(RelativeLayout.BELOW, TextViewPassword.getId());
            EditTextPasswordParam.setMargins(MiscHandler.ToDimension(context, 10), 0, MiscHandler.ToDimension(context, 10), 0);

            final EditText EditTextPassword = new EditText(new ContextThemeWrapper(context, R.style.GeneralEditTextTheme));
            EditTextPassword.setLayoutParams(EditTextPasswordParam);
            EditTextPassword.setMaxLines(1);
            EditTextPassword.setId(MiscHandler.GenerateViewID());
            EditTextPassword.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            EditTextPassword.setFilters(new InputFilter[] { new InputFilter.LengthFilter(32) });
            EditTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            EditTextPassword.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.BlueLight), PorterDuff.Mode.SRC_ATOP);
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

            RelativeLayoutMain.addView(EditTextPassword);

            RelativeLayout.LayoutParams RelativeLayoutContentParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            RelativeLayoutContentParam.addRule(RelativeLayout.BELOW, EditTextPassword.getId());
            RelativeLayoutContentParam.setMargins(0, 0, 0, MiscHandler.ToDimension(context, 5));

            RelativeLayout RelativeLayoutContent = new RelativeLayout(context);
            RelativeLayoutContent.setLayoutParams(RelativeLayoutContentParam);

            RelativeLayoutMain.addView(RelativeLayoutContent);

            TextView TextViewMessage = new TextView(context);
            TextViewMessage.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            TextViewMessage.setTextColor(ContextCompat.getColor(context, R.color.Black));
            TextViewMessage.setText(getString(R.string.FragmentSignInMessage));
            TextViewMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            TextViewMessage.setId(MiscHandler.GenerateViewID());
            TextViewMessage.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));

            RelativeLayoutContent.addView(TextViewMessage);

            RelativeLayout.LayoutParams RelativeLayoutContent2Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            RelativeLayoutContent2Param.addRule(RelativeLayout.BELOW, TextViewMessage.getId());

            RelativeLayout RelativeLayoutContent2 = new RelativeLayout(context);
            RelativeLayoutContent2.setLayoutParams(RelativeLayoutContent2Param);

            RelativeLayoutContent.addView(RelativeLayoutContent2);

            RelativeLayout.LayoutParams TextViewPrivacyParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewPrivacyParam.addRule(RelativeLayout.CENTER_VERTICAL);
            TextViewPrivacyParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

            TextView TextViewReset = new TextView(context);
            TextViewReset.setLayoutParams(TextViewPrivacyParam);
            TextViewReset.setTextColor(ContextCompat.getColor(context, R.color.BlueLight));
            TextViewReset.setText(getString(R.string.FragmentSignInForgot));
            TextViewReset.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            TextViewReset.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
            TextViewReset.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    ActivityWelcome Parent = (ActivityWelcome) getActivity();
                    Parent.getSupportFragmentManager().beginTransaction().replace(Parent.FrameLayoutContainerID, new FragmentReset()).addToBackStack("FragmentReset").commit();
                }
            });

            RelativeLayoutContent2.addView(TextViewReset);

            RelativeLayout.LayoutParams RelativeLayoutNextParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 90), MiscHandler.ToDimension(context, 35));
            RelativeLayoutNextParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            RelativeLayoutNextParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));

            GradientDrawable ShapeEnable = new GradientDrawable();
            ShapeEnable.setColor(ContextCompat.getColor(context, R.color.BlueLight));
            ShapeEnable.setCornerRadius(MiscHandler.ToDimension(context, 7));

            RelativeLayout RelativeLayoutNext = new RelativeLayout(context);
            RelativeLayoutNext.setLayoutParams(RelativeLayoutNextParam);
            RelativeLayoutNext.setBackground(ShapeEnable);

            RelativeLayoutContent2.addView(RelativeLayoutNext);

            GradientDrawable ShapeDisable = new GradientDrawable();
            ShapeDisable.setCornerRadius(MiscHandler.ToDimension(context, 7));
            ShapeDisable.setColor(ContextCompat.getColor(context, R.color.Gray2));

            StateListDrawable StateEmail = new StateListDrawable();
            StateEmail.addState(new int[] {android.R.attr.state_enabled}, ShapeEnable);
            StateEmail.addState(new int[] {-android.R.attr.state_enabled}, ShapeDisable);

            ButtonSignIn = new Button(context, null, android.R.attr.borderlessButtonStyle);
            ButtonSignIn.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 90), MiscHandler.ToDimension(context, 35)));
            ButtonSignIn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            ButtonSignIn.setTextColor(ContextCompat.getColor(context, R.color.White));
            ButtonSignIn.setText(getString(R.string.FragmentSignInTitle));
            ButtonSignIn.setBackground(StateEmail);
            ButtonSignIn.setPadding(0, 0, 0, 0);
            ButtonSignIn.setEnabled(false);
            ButtonSignIn.setAllCaps(false);
            ButtonSignIn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    ButtonSignIn.setVisibility(View.INVISIBLE);
                    LoadingViewSignIn.Start();

                    AndroidNetworking.post(MiscHandler.GetRandomServer("SignIn"))
                    .addBodyParameter("EmailOrUsername", EditTextEmailOrUsername.getText().toString())
                    .addBodyParameter("Password", EditTextPassword.getText().toString())
                    .addBodyParameter("Session", GenerateSession())
                    .setTag("FragmentSignUpEmail")
                    .build().getAsString(new StringRequestListener()
                    {
                        @Override
                        public void onResponse(String Response)
                        {
                            ButtonSignIn.setVisibility(View.VISIBLE);
                            LoadingViewSignIn.Stop();

                            try
                            {
                                JSONObject Result = new JSONObject(Response);

                                if (Result.getInt("Message") == 1000)
                                {
                                    SharedHandler.SetBoolean(context, "IsLogin", true);
                                    SharedHandler.SetString(context, "TOKEN", Result.getString("TOKEN"));
                                    SharedHandler.SetString(context, "ID", Result.getString("ID"));
                                    SharedHandler.SetString(context, "Username", Result.getString("Username"));
                                    SharedHandler.SetString(context, "Avatar", Result.getString("Avatar"));

                                    ActivityWelcome Parent = (ActivityWelcome) getActivity();
                                    Parent.startActivity(new Intent(context, ActivityMain.class));
                                    Parent.finish();
                                    return;
                                }

                                MiscHandler.Toast(context, getString(R.string.FragmentSignInError));
                            }
                            catch (Exception e)
                            {
                                // Leave Me Alone
                            }
                        }

                        @Override
                        public void onError(ANError anError)
                        {
                            ButtonSignIn.setVisibility(View.VISIBLE);
                            LoadingViewSignIn.Stop();

                            MiscHandler.Toast(context, getString(R.string.NoInternet));
                        }
                    });
                }
            });

            RelativeLayoutNext.addView(ButtonSignIn);

            RelativeLayout.LayoutParams LoadingViewEmailParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 90), MiscHandler.ToDimension(context, 35));
            LoadingViewEmailParam.addRule(RelativeLayout.CENTER_IN_PARENT);

            LoadingViewSignIn = new LoadingView(context);
            LoadingViewSignIn.setLayoutParams(LoadingViewEmailParam);
            LoadingViewSignIn.SetColor(R.color.White);

            RelativeLayoutNext.addView(LoadingViewSignIn);

            return Root;
        }

        @Override
        public void onResume()
        {
            super.onResume();

            Root.getViewTreeObserver().addOnGlobalLayoutListener(RootListener);

            InputMethodManager IMM = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            IMM.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }

        @Override
        public void onPause()
        {
            super.onPause();
            AndroidNetworking.cancel("FragmentSignIn");
            Root.getViewTreeObserver().removeOnGlobalLayoutListener(RootListener);
        }
    }

    public static class FragmentReset extends Fragment
    {
        private ScrollView Root;
        private ViewTreeObserver.OnGlobalLayoutListener RootListener;

        private Button ButtonReset;
        private LoadingView LoadingViewReset;
        private LinearLayout LinearLayoutLoading;

        private int LastDifferenceHeight = 0;

        @Override
        public View onCreateView(LayoutInflater a, ViewGroup b, Bundle c)
        {
            final Context context = getActivity();

            Root = new ScrollView(context);
            Root.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            Root.setBackgroundResource(R.color.White);
            Root.setHorizontalScrollBarEnabled(false);
            Root.setVerticalScrollBarEnabled(false);
            Root.setFillViewport(true);
            Root.setClickable(true);

            RootListener = new ViewTreeObserver.OnGlobalLayoutListener()
            {
                @Override
                public void onGlobalLayout()
                {
                    Rect rect = new Rect();
                    Root.getWindowVisibleDisplayFrame(rect);

                    int ScreenHeight = Root.getHeight();
                    int DifferenceHeight = ScreenHeight - (rect.bottom - rect.top);

                    if (DifferenceHeight > ScreenHeight / 3 && DifferenceHeight != LastDifferenceHeight)
                    {
                        Root.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, ScreenHeight - DifferenceHeight));
                        LastDifferenceHeight = DifferenceHeight;
                    }
                    else if (DifferenceHeight != LastDifferenceHeight)
                    {
                        Root.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, ScreenHeight));
                        LastDifferenceHeight = DifferenceHeight;
                    }
                    else if (LastDifferenceHeight != 0)
                    {
                        Root.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, ScreenHeight + Math.abs(LastDifferenceHeight)));
                        LastDifferenceHeight = 0;
                    }
                }
            };

            RelativeLayout RelativeLayoutMain = new RelativeLayout(context);
            RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

            Root.addView(RelativeLayoutMain);

            RelativeLayout RelativeLayoutHeader = new RelativeLayout(context);
            RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56)));
            RelativeLayoutHeader.setBackgroundResource(R.color.BlueLight);
            RelativeLayoutHeader.setId(MiscHandler.GenerateViewID());

            RelativeLayoutMain.addView(RelativeLayoutHeader);

            ImageView ImageViewBack = new ImageView(context);
            ImageViewBack.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), RelativeLayout.LayoutParams.MATCH_PARENT));
            ImageViewBack.setScaleType(ImageView.ScaleType.FIT_XY);
            ImageViewBack.setId(MiscHandler.GenerateViewID());
            ImageViewBack.setImageResource(R.drawable.ic_back_white);
            ImageViewBack.setPadding(MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12));
            ImageViewBack.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    MiscHandler.HideSoftKey(getActivity());
                    getActivity().onBackPressed();
                }
            });

            RelativeLayoutHeader.addView(ImageViewBack);

            RelativeLayout.LayoutParams TextViewHeaderParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewHeaderParam.addRule(RelativeLayout.RIGHT_OF, ImageViewBack.getId());
            TextViewHeaderParam.addRule(RelativeLayout.CENTER_VERTICAL);

            TextView TextViewHeader = new TextView(context);
            TextViewHeader.setLayoutParams(TextViewHeaderParam);
            TextViewHeader.setTextColor(ContextCompat.getColor(context, R.color.White));
            TextViewHeader.setText(getString(R.string.FragmentResetTitle));
            TextViewHeader.setTypeface(null, Typeface.BOLD);
            TextViewHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

            RelativeLayoutHeader.addView(TextViewHeader);

            RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1));
            ViewLineParam.addRule(RelativeLayout.BELOW, RelativeLayoutHeader.getId());

            View ViewLine = new View(context);
            ViewLine.setLayoutParams(ViewLineParam);
            ViewLine.setBackgroundResource(R.color.Gray2);
            ViewLine.setId(MiscHandler.GenerateViewID());

            RelativeLayoutMain.addView(ViewLine);

            RelativeLayout.LayoutParams TextViewMessageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewMessageParam.setMargins(MiscHandler.ToDimension(context, 25), MiscHandler.ToDimension(context, 25), MiscHandler.ToDimension(context, 25), MiscHandler.ToDimension(context, 25));
            TextViewMessageParam.addRule(RelativeLayout.BELOW, ViewLine.getId());
            TextViewMessageParam.addRule(RelativeLayout.CENTER_HORIZONTAL);

            TextView TextViewMessage = new TextView(context);
            TextViewMessage.setLayoutParams(TextViewMessageParam);
            TextViewMessage.setTextColor(ContextCompat.getColor(context, R.color.Black));
            TextViewMessage.setText(getString(R.string.FragmentResetMessage));
            TextViewMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            TextViewMessage.setGravity(Gravity.CENTER);
            TextViewMessage.setId(MiscHandler.GenerateViewID());

            RelativeLayoutMain.addView(TextViewMessage);

            RelativeLayout.LayoutParams EditTextEmailOrUsernameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 45));
            EditTextEmailOrUsernameParam.addRule(RelativeLayout.BELOW, TextViewMessage.getId());
            EditTextEmailOrUsernameParam.setMargins(MiscHandler.ToDimension(context, 15), 0, MiscHandler.ToDimension(context, 15), 0);

            final EditText EditTextEmailOrUsername = new EditText(new ContextThemeWrapper(context, R.style.GeneralEditTextTheme));
            EditTextEmailOrUsername.setLayoutParams(EditTextEmailOrUsernameParam);
            EditTextEmailOrUsername.setMaxLines(1);
            EditTextEmailOrUsername.setGravity(Gravity.CENTER);
            EditTextEmailOrUsername.setPadding(MiscHandler.ToDimension(context, 10),MiscHandler.ToDimension(context, 10),MiscHandler.ToDimension(context, 10),MiscHandler.ToDimension(context, 10));
            EditTextEmailOrUsername.setHint(getString(R.string.FragmentResetEmailOrUsername));
            EditTextEmailOrUsername.setId(MiscHandler.GenerateViewID());
            EditTextEmailOrUsername.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            EditTextEmailOrUsername.setFilters(new InputFilter[] { new InputFilter.LengthFilter(64) });
            EditTextEmailOrUsername.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            EditTextEmailOrUsername.setBackgroundColor(Color.parseColor("#4fcbd6dc"));
            EditTextEmailOrUsername.requestFocus();
            EditTextEmailOrUsername.setId(MiscHandler.GenerateViewID());
            EditTextEmailOrUsername.addTextChangedListener(new TextWatcher()
            {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                @Override
                public void afterTextChanged(Editable s) { }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count)
                {
                    ButtonReset.setEnabled((s.length() > 2));
                }
            });

            RelativeLayoutMain.addView(EditTextEmailOrUsername);

            RelativeLayout.LayoutParams ButtonResetParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 45));
            ButtonResetParam.addRule(RelativeLayout.BELOW, EditTextEmailOrUsername.getId());
            ButtonResetParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));

            GradientDrawable ShapeEnable = new GradientDrawable();
            ShapeEnable.setColor(ContextCompat.getColor(context, R.color.BlueLight));
            ShapeEnable.setCornerRadius(MiscHandler.ToDimension(context, 7));

            GradientDrawable ShapeDisable = new GradientDrawable();
            ShapeDisable.setCornerRadius(MiscHandler.ToDimension(context, 7));
            ShapeDisable.setColor(ContextCompat.getColor(context, R.color.Gray2));

            StateListDrawable StateEmail = new StateListDrawable();
            StateEmail.addState(new int[] {android.R.attr.state_enabled}, ShapeEnable);
            StateEmail.addState(new int[] {-android.R.attr.state_enabled}, ShapeDisable);

            ButtonReset = new Button(context, null, android.R.attr.borderlessButtonStyle);
            ButtonReset.setLayoutParams(ButtonResetParam);
            ButtonReset.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            ButtonReset.setTextColor(ContextCompat.getColor(context, R.color.White));
            ButtonReset.setText(getString(R.string.FragmentResetSubmit));
            ButtonReset.setBackground(StateEmail);
            ButtonReset.setPadding(0, 0, 0, 0);
            ButtonReset.setEnabled(false);
            ButtonReset.setAllCaps(false);
            ButtonReset.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    ButtonReset.setVisibility(View.GONE);
                    LinearLayoutLoading.setVisibility(View.VISIBLE);
                    LoadingViewReset.Start();

                    AndroidNetworking.post(MiscHandler.GetRandomServer("ResetPassword"))
                    .addBodyParameter("EmailOrUsername", EditTextEmailOrUsername.getText().toString())
                    .setTag("FragmentReset")
                    .build().getAsString(new StringRequestListener()
                    {
                        @Override
                        public void onResponse(String Response)
                        {
                            ButtonReset.setVisibility(View.VISIBLE);
                            LinearLayoutLoading.setVisibility(View.GONE);
                            LoadingViewReset.Stop();

                            try
                            {
                                if (new JSONObject(Response).getInt("Message") == 1000)
                                    MiscHandler.Toast(context, getString(R.string.FragmentResetSuccess));
                                else
                                    MiscHandler.Toast(context, getString(R.string.FragmentResetError));
                            }
                            catch (Exception e)
                            {
                                // Leave Me Alone
                            }
                        }

                        @Override
                        public void onError(ANError anError)
                        {
                            ButtonReset.setVisibility(View.VISIBLE);
                            LinearLayoutLoading.setVisibility(View.GONE);
                            LoadingViewReset.Stop();

                            MiscHandler.Toast(context, getString(R.string.NoInternet));
                        }
                    });
                }
            });

            RelativeLayoutMain.addView(ButtonReset);

            RelativeLayout.LayoutParams LinearLayoutLoadingParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 45));
            LinearLayoutLoadingParam.addRule(RelativeLayout.BELOW, EditTextEmailOrUsername.getId());
            LinearLayoutLoadingParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));

            LinearLayoutLoading = new LinearLayout(context);
            LinearLayoutLoading.setLayoutParams(LinearLayoutLoadingParam);
            LinearLayoutLoading.setVisibility(View.INVISIBLE);
            LinearLayoutLoading.setGravity(Gravity.CENTER);
            LinearLayoutLoading.setBackground(ShapeEnable);

            RelativeLayoutMain.addView(LinearLayoutLoading);

            RelativeLayout.LayoutParams LoadingViewResetParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            LoadingViewResetParam.addRule(RelativeLayout.CENTER_IN_PARENT);

            LoadingViewReset = new LoadingView(context);
            LoadingViewReset.setLayoutParams(LoadingViewResetParam);
            LoadingViewReset.SetColor(R.color.White);

            LinearLayoutLoading.addView(LoadingViewReset);

            return  Root;
        }

        @Override
        public void onResume()
        {
            super.onResume();

            Root.getViewTreeObserver().addOnGlobalLayoutListener(RootListener);

            InputMethodManager IMM = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            IMM.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }

        @Override
        public void onPause()
        {
            super.onPause();
            AndroidNetworking.cancel("FragmentReset");
            Root.getViewTreeObserver().removeOnGlobalLayoutListener(RootListener);
        }
    }

    private static String GenerateSession()
    {
        return "BioGram Android " + BuildConfig.VERSION_NAME + " - " + Build.MODEL + " - " + Build.MANUFACTURER + " - API " + Build.VERSION.SDK_INT;
    }
}
