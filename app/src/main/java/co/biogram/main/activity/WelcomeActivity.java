package co.biogram.main.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;

import co.biogram.fragment.FragmentActivity;
import co.biogram.main.handler.SharedHandler;
import co.biogram.main.ui.Welcome;

public class WelcomeActivity extends FragmentActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Context context = this;

        if (SharedHandler.GetBoolean(context, "IsLogin"))
        {
            startActivity(new Intent(context, MainActivity.class));
            finish();
            return;
        }

        RelativeLayout RelativeLayoutMain = new RelativeLayout(context);
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

        SetContentView(RelativeLayoutMain);

        GetManager().Create(new Welcome());
    }

    /*
    public static class SignUpFragmentUsername extends Fragment
    {
        private ViewTreeObserver.OnGlobalLayoutListener RelativeLayoutMainListener;
        private int RelativeLayoutMainHeightDifference = 0;
        private RelativeLayout RelativeLayoutMain;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup Parent, Bundle savedInstanceState)
        {
            final Context context = getActivity();
            final Button ButtonSignUp = new Button(context, null, android.R.attr.borderlessButtonStyle);
            final LoadingView LoadingViewSignUp = new LoadingView(context);

            RelativeLayoutMain = new RelativeLayout(context);
            RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            RelativeLayoutMain.setBackgroundResource(R.color.White);
            RelativeLayoutMain.setClickable(true);

            RelativeLayoutMainListener = new ViewTreeObserver.OnGlobalLayoutListener()
            {
                @Override
                public void onGlobalLayout()
                {
                    Rect rect = new Rect();
                    RelativeLayoutMain.getWindowVisibleDisplayFrame(rect);

                    int ScreenHeight = RelativeLayoutMain.getHeight();
                    int DifferenceHeight = ScreenHeight - (rect.bottom - rect.top);

                    if (DifferenceHeight > ScreenHeight / 3 && DifferenceHeight != RelativeLayoutMainHeightDifference)
                    {
                        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ScreenHeight - DifferenceHeight));
                        RelativeLayoutMainHeightDifference = DifferenceHeight;
                    }
                    else if (DifferenceHeight != RelativeLayoutMainHeightDifference)
                    {
                        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ScreenHeight));
                        RelativeLayoutMainHeightDifference = DifferenceHeight;
                    }
                    else if (RelativeLayoutMainHeightDifference != 0)
                    {
                        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ScreenHeight + Math.abs(RelativeLayoutMainHeightDifference)));
                        RelativeLayoutMainHeightDifference = 0;
                    }

                    RelativeLayoutMain.requestLayout();
                }
            };

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
            TextViewHeader.setText(getString(R.string.SignUpFragmentUsername));
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

            RelativeLayout.LayoutParams ScrollViewMainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            ScrollViewMainParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

            ScrollView ScrollViewMain = new ScrollView(context);
            ScrollViewMain.setLayoutParams(ScrollViewMainParam);
            ScrollViewMain.setFillViewport(true);
            ScrollViewMain.setId(MiscHandler.GenerateViewID());

            RelativeLayoutMain.addView(ScrollViewMain);

            RelativeLayout RelativeLayoutMain2 = new RelativeLayout(context);
            RelativeLayoutMain2.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

            ScrollViewMain.addView(RelativeLayoutMain2);

            RelativeLayout.LayoutParams TextViewUsernameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewUsernameParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));

            TextView TextViewUsername = new TextView(context);
            TextViewUsername.setLayoutParams(TextViewUsernameParam);
            TextViewUsername.setTextColor(ContextCompat.getColor(context, R.color.Gray4));
            TextViewUsername.setText(getString(R.string.SignUpFragmentUsername));
            TextViewUsername.setTypeface(null, Typeface.BOLD);
            TextViewUsername.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            TextViewUsername.setId(MiscHandler.GenerateViewID());

            RelativeLayoutMain2.addView(TextViewUsername);

            RelativeLayout.LayoutParams EditTextUsernameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            EditTextUsernameParam.addRule(RelativeLayout.BELOW, TextViewUsername.getId());
            EditTextUsernameParam.setMargins(MiscHandler.ToDimension(context, 10), 0, MiscHandler.ToDimension(context, 10), 0);

            final EditText EditTextUsername = new EditText(new ContextThemeWrapper(context, R.style.GeneralEditTextTheme));
            EditTextUsername.setLayoutParams(EditTextUsernameParam);
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
                        ButtonSignUp.setEnabled(true);
                    else
                        ButtonSignUp.setEnabled(false);
                }
            });

            RelativeLayoutMain2.addView(EditTextUsername);

            RelativeLayout.LayoutParams TextViewMessageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewMessageParam.addRule(RelativeLayout.BELOW, EditTextUsername.getId());

            TextView TextViewMessage = new TextView(context);
            TextViewMessage.setLayoutParams(TextViewMessageParam);
            TextViewMessage.setTextColor(ContextCompat.getColor(context, R.color.Black));
            TextViewMessage.setText(getString(R.string.SignUpFragmentUsernameMessage));
            TextViewMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            TextViewMessage.setId(MiscHandler.GenerateViewID());
            TextViewMessage.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));

            RelativeLayoutMain2.addView(TextViewMessage);

            RelativeLayout.LayoutParams RelativeLayoutBottomParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            RelativeLayoutBottomParam.addRule(RelativeLayout.BELOW, TextViewMessage.getId());

            RelativeLayout RelativeLayoutBottom = new RelativeLayout(context);
            RelativeLayoutBottom.setLayoutParams(RelativeLayoutBottomParam);

            RelativeLayoutMain2.addView(RelativeLayoutBottom);

            RelativeLayout.LayoutParams TextViewPrivacyParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewPrivacyParam.addRule(RelativeLayout.CENTER_VERTICAL);
            TextViewPrivacyParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

            TextView TextViewPrivacy = new TextView(context);
            TextViewPrivacy.setLayoutParams(TextViewPrivacyParam);
            TextViewPrivacy.setTextColor(ContextCompat.getColor(context, R.color.BlueLight));
            TextViewPrivacy.setText(getString(R.string.WelcomeActivityGeneralTerm));
            TextViewPrivacy.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            TextViewPrivacy.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
            TextViewPrivacy.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"))); } });

            RelativeLayoutBottom.addView(TextViewPrivacy);

            RelativeLayout.LayoutParams RelativeLayoutNextParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 90), MiscHandler.ToDimension(context, 35));
            RelativeLayoutNextParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            RelativeLayoutNextParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));

            GradientDrawable ShapeEnable = new GradientDrawable();
            ShapeEnable.setColor(ContextCompat.getColor(context, R.color.BlueLight));
            ShapeEnable.setCornerRadius(MiscHandler.ToDimension(context, 7));

            RelativeLayout RelativeLayoutNext = new RelativeLayout(context);
            RelativeLayoutNext.setLayoutParams(RelativeLayoutNextParam);
            RelativeLayoutNext.setBackground(ShapeEnable);

            RelativeLayoutBottom.addView(RelativeLayoutNext);

            GradientDrawable ShapeDisable = new GradientDrawable();
            ShapeDisable.setCornerRadius(MiscHandler.ToDimension(context, 7));
            ShapeDisable.setColor(ContextCompat.getColor(context, R.color.Gray2));

            StateListDrawable StateSignUp = new StateListDrawable();
            StateSignUp.addState(new int[] { android.R.attr.state_enabled }, ShapeEnable);
            StateSignUp.addState(new int[] { -android.R.attr.state_enabled }, ShapeDisable);

            ButtonSignUp.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 90), MiscHandler.ToDimension(context, 35)));
            ButtonSignUp.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            ButtonSignUp.setTextColor(ContextCompat.getColor(context, R.color.White));
            ButtonSignUp.setText(getString(R.string.WelcomeActivityGeneralNext));
            ButtonSignUp.setBackground(StateSignUp);
            ButtonSignUp.setPadding(0, 0, 0, 0);
            ButtonSignUp.setEnabled(false);
            ButtonSignUp.setAllCaps(false);
            ButtonSignUp.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    ButtonSignUp.setVisibility(View.GONE);
                    LoadingViewSignUp.Start();

                    AndroidNetworking.post(MiscHandler.GetRandomServer("UsernameIsAvailable"))
                    .addBodyParameter("Username", EditTextUsername.getText().toString())
                    .setTag("SignUpFragmentUsername")
                    .build()
                    .getAsString(new StringRequestListener()
                    {
                        @Override
                        public void onResponse(String Response)
                        {
                            ButtonSignUp.setVisibility(View.VISIBLE);
                            LoadingViewSignUp.Stop();

                            try
                            {
                                if (new JSONObject(Response).getInt("Message") == 1000)
                                {
                                   // WelcomeActivity Parent = (WelcomeActivity) getActivity();
                                   // Parent.Username = EditTextUsername.getText().toString();
                                    //Parent.getSupportFragmentManager().beginTransaction().add(R.id.WelcomeActivityContainer, new SignUpFragmentPassword()).addToBackStack("SignUpFragmentPassword").commitAllowingStateLoss();
                                    return;
                                }

                                MiscHandler.Toast(context, getString(R.string.SignUpFragmentUsernameError));
                            }
                            catch (Exception e)
                            {
                                MiscHandler.Debug("WelcomeActivity-RequestUsername: " + e.toString());
                            }
                        }

                        @Override
                        public void onError(ANError anError)
                        {
                            LoadingViewSignUp.Stop();
                            ButtonSignUp.setVisibility(View.VISIBLE);
                            MiscHandler.Toast(context, getString(R.string.NoInternet));
                        }
                    });
                }
            });

            RelativeLayoutNext.addView(ButtonSignUp);

            RelativeLayout.LayoutParams LoadingViewUsernameParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 90), MiscHandler.ToDimension(context, 35));
            LoadingViewUsernameParam.addRule(RelativeLayout.CENTER_IN_PARENT);

            LoadingViewSignUp.setLayoutParams(LoadingViewUsernameParam);
            LoadingViewSignUp.SetColor(R.color.White);

            RelativeLayoutNext.addView(LoadingViewSignUp);

            return RelativeLayoutMain;
        }

        @Override
        public void onResume()
        {
            super.onResume();
            RelativeLayoutMain.getViewTreeObserver().addOnGlobalLayoutListener(RelativeLayoutMainListener);

            InputMethodManager IMM = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            IMM.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }

        @Override
        public void onPause()
        {
            super.onPause();
            MiscHandler.HideSoftKey(getActivity());
            AndroidNetworking.forceCancel("SignUpFragmentUsername");
            RelativeLayoutMain.getViewTreeObserver().removeOnGlobalLayoutListener(RelativeLayoutMainListener);
        }
    }

    public static class SignUpFragmentPassword extends Fragment
    {
        private ViewTreeObserver.OnGlobalLayoutListener RelativeLayoutMainListener;
        private int RelativeLayoutMainHeightDifference = 0;
        private RelativeLayout RelativeLayoutMain;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup Parent, Bundle savedInstanceState)
        {
            final Context context = getActivity();
            final Button ButtonSignUp = new Button(context, null, android.R.attr.borderlessButtonStyle);

            RelativeLayoutMain = new RelativeLayout(context);
            RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            RelativeLayoutMain.setBackgroundResource(R.color.White);
            RelativeLayoutMain.setClickable(true);

            RelativeLayoutMainListener = new ViewTreeObserver.OnGlobalLayoutListener()
            {
                @Override
                public void onGlobalLayout()
                {
                    Rect rect = new Rect();
                    RelativeLayoutMain.getWindowVisibleDisplayFrame(rect);

                    int ScreenHeight = RelativeLayoutMain.getHeight();
                    int DifferenceHeight = ScreenHeight - (rect.bottom - rect.top);

                    if (DifferenceHeight > ScreenHeight / 3 && DifferenceHeight != RelativeLayoutMainHeightDifference)
                    {
                        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ScreenHeight - DifferenceHeight));
                        RelativeLayoutMainHeightDifference = DifferenceHeight;
                    }
                    else if (DifferenceHeight != RelativeLayoutMainHeightDifference)
                    {
                        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ScreenHeight));
                        RelativeLayoutMainHeightDifference = DifferenceHeight;
                    }
                    else if (RelativeLayoutMainHeightDifference != 0)
                    {
                        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ScreenHeight + Math.abs(RelativeLayoutMainHeightDifference)));
                        RelativeLayoutMainHeightDifference = 0;
                    }

                    RelativeLayoutMain.requestLayout();
                }
            };

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
            TextViewHeader.setText(getString(R.string.SignUpFragmentPassword));
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

            RelativeLayout.LayoutParams ScrollViewMainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            ScrollViewMainParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

            ScrollView ScrollViewMain = new ScrollView(context);
            ScrollViewMain.setLayoutParams(ScrollViewMainParam);
            ScrollViewMain.setFillViewport(true);
            ScrollViewMain.setId(MiscHandler.GenerateViewID());

            RelativeLayoutMain.addView(ScrollViewMain);

            RelativeLayout RelativeLayoutMain2 = new RelativeLayout(context);
            RelativeLayoutMain2.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

            ScrollViewMain.addView(RelativeLayoutMain2);

            RelativeLayout.LayoutParams TextViewPasswordParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewPasswordParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));

            TextView TextViewPassword = new TextView(context);
            TextViewPassword.setLayoutParams(TextViewPasswordParam);
            TextViewPassword.setTextColor(ContextCompat.getColor(context, R.color.Gray4));
            TextViewPassword.setText(getString(R.string.SignUpFragmentPassword));
            TextViewPassword.setTypeface(null, Typeface.BOLD);
            TextViewPassword.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            TextViewPassword.setId(MiscHandler.GenerateViewID());

            RelativeLayoutMain2.addView(TextViewPassword);

            RelativeLayout.LayoutParams EditTextPasswordParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            EditTextPasswordParam.addRule(RelativeLayout.BELOW, TextViewPassword.getId());
            EditTextPasswordParam.setMargins(MiscHandler.ToDimension(context, 10), 0, MiscHandler.ToDimension(context, 10), 0);

            final EditText EditTextPassword = new EditText(new ContextThemeWrapper(context, R.style.GeneralEditTextTheme));
            EditTextPassword.setLayoutParams(EditTextPasswordParam);
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
                        ButtonSignUp.setEnabled(true);
                    else
                        ButtonSignUp.setEnabled(false);
                }
            });

            RelativeLayoutMain2.addView(EditTextPassword);

            RelativeLayout.LayoutParams TextViewMessageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewMessageParam.addRule(RelativeLayout.BELOW, EditTextPassword.getId());

            TextView TextViewMessage = new TextView(context);
            TextViewMessage.setLayoutParams(TextViewMessageParam);
            TextViewMessage.setTextColor(ContextCompat.getColor(context, R.color.Black));
            TextViewMessage.setText(getString(R.string.SignUpFragmentPasswordMessage));
            TextViewMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            TextViewMessage.setId(MiscHandler.GenerateViewID());
            TextViewMessage.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));

            RelativeLayoutMain2.addView(TextViewMessage);

            RelativeLayout.LayoutParams RelativeLayoutBottomParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            RelativeLayoutBottomParam.addRule(RelativeLayout.BELOW, TextViewMessage.getId());

            RelativeLayout RelativeLayoutBottom = new RelativeLayout(context);
            RelativeLayoutBottom.setLayoutParams(RelativeLayoutBottomParam);

            RelativeLayoutMain2.addView(RelativeLayoutBottom);

            RelativeLayout.LayoutParams TextViewPrivacyParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewPrivacyParam.addRule(RelativeLayout.CENTER_VERTICAL);
            TextViewPrivacyParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

            TextView TextViewPrivacy = new TextView(context);
            TextViewPrivacy.setLayoutParams(TextViewPrivacyParam);
            TextViewPrivacy.setTextColor(ContextCompat.getColor(context, R.color.BlueLight));
            TextViewPrivacy.setText(getString(R.string.WelcomeActivityGeneralTerm));
            TextViewPrivacy.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            TextViewPrivacy.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
            TextViewPrivacy.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"))); } });

            RelativeLayoutBottom.addView(TextViewPrivacy);

            RelativeLayout.LayoutParams RelativeLayoutNextParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 90), MiscHandler.ToDimension(context, 35));
            RelativeLayoutNextParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            RelativeLayoutNextParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));

            GradientDrawable ShapeEnable = new GradientDrawable();
            ShapeEnable.setColor(ContextCompat.getColor(context, R.color.BlueLight));
            ShapeEnable.setCornerRadius(MiscHandler.ToDimension(context, 7));

            RelativeLayout RelativeLayoutNext = new RelativeLayout(context);
            RelativeLayoutNext.setLayoutParams(RelativeLayoutNextParam);
            RelativeLayoutNext.setBackground(ShapeEnable);

            RelativeLayoutBottom.addView(RelativeLayoutNext);

            GradientDrawable ShapeDisable = new GradientDrawable();
            ShapeDisable.setCornerRadius(MiscHandler.ToDimension(context, 7));
            ShapeDisable.setColor(ContextCompat.getColor(context, R.color.Gray2));

            StateListDrawable StateSignUp = new StateListDrawable();
            StateSignUp.addState(new int[] { android.R.attr.state_enabled }, ShapeEnable);
            StateSignUp.addState(new int[] { -android.R.attr.state_enabled }, ShapeDisable);

            ButtonSignUp.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 90), MiscHandler.ToDimension(context, 35)));
            ButtonSignUp.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            ButtonSignUp.setTextColor(ContextCompat.getColor(context, R.color.White));
            ButtonSignUp.setText(getString(R.string.WelcomeActivityGeneralNext));
            ButtonSignUp.setBackground(StateSignUp);
            ButtonSignUp.setPadding(0, 0, 0, 0);
            ButtonSignUp.setEnabled(false);
            ButtonSignUp.setAllCaps(false);
            ButtonSignUp.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    //WelcomeActivity Parent = (WelcomeActivity) getActivity();
                    //Parent.Password = EditTextPassword.getText().toString();
                    //Parent.getSupportFragmentManager().beginTransaction().add(R.id.WelcomeActivityContainer, new SignUpFragmentEmail()).addToBackStack("SignUpFragmentEmail").commitAllowingStateLoss();
                }
            });

            RelativeLayoutNext.addView(ButtonSignUp);

            return RelativeLayoutMain;
        }

        @Override
        public void onResume()
        {
            super.onResume();
            RelativeLayoutMain.getViewTreeObserver().addOnGlobalLayoutListener(RelativeLayoutMainListener);

            InputMethodManager IMM = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            IMM.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }

        @Override
        public void onPause()
        {
            super.onPause();
            MiscHandler.HideSoftKey(getActivity());
            RelativeLayoutMain.getViewTreeObserver().removeOnGlobalLayoutListener(RelativeLayoutMainListener);
        }
    }

    public static class SignUpFragmentEmail extends Fragment
    {
        private ViewTreeObserver.OnGlobalLayoutListener RelativeLayoutMainListener;
        private int RelativeLayoutMainHeightDifference = 0;
        private RelativeLayout RelativeLayoutMain;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup Parent, Bundle savedInstanceState)
        {
            final Context context = getActivity();
            final Button ButtonSignUp = new Button(context, null, android.R.attr.borderlessButtonStyle);
            final LoadingView LoadingViewSignUp = new LoadingView(context);

            RelativeLayoutMain = new RelativeLayout(context);
            RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            RelativeLayoutMain.setBackgroundResource(R.color.White);
            RelativeLayoutMain.setClickable(true);

            RelativeLayoutMainListener = new ViewTreeObserver.OnGlobalLayoutListener()
            {
                @Override
                public void onGlobalLayout()
                {
                    Rect rect = new Rect();
                    RelativeLayoutMain.getWindowVisibleDisplayFrame(rect);

                    int ScreenHeight = RelativeLayoutMain.getHeight();
                    int DifferenceHeight = ScreenHeight - (rect.bottom - rect.top);

                    if (DifferenceHeight > ScreenHeight / 3 && DifferenceHeight != RelativeLayoutMainHeightDifference)
                    {
                        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ScreenHeight - DifferenceHeight));
                        RelativeLayoutMainHeightDifference = DifferenceHeight;
                    }
                    else if (DifferenceHeight != RelativeLayoutMainHeightDifference)
                    {
                        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ScreenHeight));
                        RelativeLayoutMainHeightDifference = DifferenceHeight;
                    }
                    else if (RelativeLayoutMainHeightDifference != 0)
                    {
                        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ScreenHeight + Math.abs(RelativeLayoutMainHeightDifference)));
                        RelativeLayoutMainHeightDifference = 0;
                    }

                    RelativeLayoutMain.requestLayout();
                }
            };

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
            TextViewHeader.setText(getString(R.string.SignUpFragmentEmailTitle));
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

            RelativeLayout.LayoutParams ScrollViewMainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            ScrollViewMainParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

            ScrollView ScrollViewMain = new ScrollView(context);
            ScrollViewMain.setLayoutParams(ScrollViewMainParam);
            ScrollViewMain.setFillViewport(true);
            ScrollViewMain.setId(MiscHandler.GenerateViewID());

            RelativeLayoutMain.addView(ScrollViewMain);

            RelativeLayout RelativeLayoutMain2 = new RelativeLayout(context);
            RelativeLayoutMain2.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

            ScrollViewMain.addView(RelativeLayoutMain2);

            RelativeLayout.LayoutParams TextViewUsernameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewUsernameParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));

            TextView TextViewUsername = new TextView(context);
            TextViewUsername.setLayoutParams(TextViewUsernameParam);
            TextViewUsername.setTextColor(ContextCompat.getColor(context, R.color.Gray4));
            TextViewUsername.setText(getString(R.string.SignUpFragmentEmail));
            TextViewUsername.setTypeface(null, Typeface.BOLD);
            TextViewUsername.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            TextViewUsername.setId(MiscHandler.GenerateViewID());

            RelativeLayoutMain2.addView(TextViewUsername);

            RelativeLayout.LayoutParams EditTextEmailParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            EditTextEmailParam.addRule(RelativeLayout.BELOW, TextViewUsername.getId());
            EditTextEmailParam.setMargins(MiscHandler.ToDimension(context, 10), 0, MiscHandler.ToDimension(context, 10), 0);

            final EditText EditTextEmail = new EditText(new ContextThemeWrapper(context, R.style.GeneralEditTextTheme));
            EditTextEmail.setLayoutParams(EditTextEmailParam);
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
                        ButtonSignUp.setEnabled(true);
                    else
                        ButtonSignUp.setEnabled(false);
                }
            });

            RelativeLayoutMain2.addView(EditTextEmail);

            RelativeLayout.LayoutParams TextViewMessageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewMessageParam.addRule(RelativeLayout.BELOW, EditTextEmail.getId());

            TextView TextViewMessage = new TextView(context);
            TextViewMessage.setLayoutParams(TextViewMessageParam);
            TextViewMessage.setTextColor(ContextCompat.getColor(context, R.color.Black));
            TextViewMessage.setText(getString(R.string.SignUpFragmentEmailMessage));
            TextViewMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            TextViewMessage.setId(MiscHandler.GenerateViewID());
            TextViewMessage.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));

            RelativeLayoutMain2.addView(TextViewMessage);

            RelativeLayout.LayoutParams RelativeLayoutBottomParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            RelativeLayoutBottomParam.addRule(RelativeLayout.BELOW, TextViewMessage.getId());

            RelativeLayout RelativeLayoutBottom = new RelativeLayout(context);
            RelativeLayoutBottom.setLayoutParams(RelativeLayoutBottomParam);

            RelativeLayoutMain2.addView(RelativeLayoutBottom);

            RelativeLayout.LayoutParams TextViewPrivacyParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewPrivacyParam.addRule(RelativeLayout.CENTER_VERTICAL);
            TextViewPrivacyParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

            TextView TextViewPrivacy = new TextView(context);
            TextViewPrivacy.setLayoutParams(TextViewPrivacyParam);
            TextViewPrivacy.setTextColor(ContextCompat.getColor(context, R.color.BlueLight));
            TextViewPrivacy.setText(getString(R.string.WelcomeActivityGeneralTerm));
            TextViewPrivacy.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            TextViewPrivacy.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
            TextViewPrivacy.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"))); } });

            RelativeLayoutBottom.addView(TextViewPrivacy);

            RelativeLayout.LayoutParams RelativeLayoutFinishParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 90), MiscHandler.ToDimension(context, 35));
            RelativeLayoutFinishParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            RelativeLayoutFinishParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));

            GradientDrawable ShapeEnable = new GradientDrawable();
            ShapeEnable.setColor(ContextCompat.getColor(context, R.color.BlueLight));
            ShapeEnable.setCornerRadius(MiscHandler.ToDimension(context, 7));

            RelativeLayout RelativeLayoutFinish = new RelativeLayout(context);
            RelativeLayoutFinish.setLayoutParams(RelativeLayoutFinishParam);
            RelativeLayoutFinish.setBackground(ShapeEnable);

            RelativeLayoutBottom.addView(RelativeLayoutFinish);

            GradientDrawable ShapeDisable = new GradientDrawable();
            ShapeDisable.setCornerRadius(MiscHandler.ToDimension(context, 7));
            ShapeDisable.setColor(ContextCompat.getColor(context, R.color.Gray2));

            StateListDrawable StateSignUp = new StateListDrawable();
            StateSignUp.addState(new int[] { android.R.attr.state_enabled }, ShapeEnable);
            StateSignUp.addState(new int[] { -android.R.attr.state_enabled }, ShapeDisable);

            ButtonSignUp.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 90), MiscHandler.ToDimension(context, 35)));
            ButtonSignUp.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            ButtonSignUp.setTextColor(ContextCompat.getColor(context, R.color.White));
            ButtonSignUp.setText(getString(R.string.SignUpFragmentEmailFinish));
            ButtonSignUp.setBackground(StateSignUp);
            ButtonSignUp.setPadding(0, 0, 0, 0);
            ButtonSignUp.setEnabled(false);
            ButtonSignUp.setAllCaps(false);
            ButtonSignUp.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    /*final WelcomeActivity Parent = WelcomeActivity) getActivity();

                    ButtonSignUp.setVisibility(View.GONE);
                    LoadingViewSignUp.Start();

                    AndroidNetworking.post(MiscHandler.GetRandomServer("SignUp"))
                    .addBodyParameter("Username", Parent.Username)
                    .addBodyParameter("Password", Parent.Password)
                    .addBodyParameter("Email", EditTextEmail.getText().toString())
                    .addBodyParameter("Session", GenerateSession())
                    .setTag("SignUpFragmentEmail")
                    .build()
                    .getAsString(new StringRequestListener()
                    {
                        @Override
                        public void onResponse(String Response)
                        {
                            LoadingViewSignUp.Stop();
                            ButtonSignUp.setVisibility(View.VISIBLE);

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

                                    Parent.startActivity(new Intent(context, MainActivity.class));
                                    Parent.finish();
                                    return;
                                }

                                MiscHandler.Toast(context, getString(R.string.SignUpFragmentEmailError));
                            }
                            catch (Exception e)
                            {
                                MiscHandler.Debug("WelcomeActivity-RequestSignUp: " + e.toString());
                            }
                        }

                        @Override
                        public void onError(ANError anError)
                        {
                            LoadingViewSignUp.Stop();
                            ButtonSignUp.setVisibility(View.VISIBLE);
                            MiscHandler.Toast(context, getString(R.string.NoInternet));
                        }
                    });
                }
            });

            RelativeLayoutFinish.addView(ButtonSignUp);

            RelativeLayout.LayoutParams LoadingViewUsernameParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 90), MiscHandler.ToDimension(context, 35));
            LoadingViewUsernameParam.addRule(RelativeLayout.CENTER_IN_PARENT);

            LoadingViewSignUp.setLayoutParams(LoadingViewUsernameParam);
            LoadingViewSignUp.SetColor(R.color.White);

            RelativeLayoutFinish.addView(LoadingViewSignUp);

            return RelativeLayoutMain;
        }

        @Override
        public void onResume()
        {
            super.onResume();
            RelativeLayoutMain.getViewTreeObserver().addOnGlobalLayoutListener(RelativeLayoutMainListener);

            InputMethodManager IMM = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            IMM.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }

        @Override
        public void onPause()
        {
            super.onPause();
            MiscHandler.HideSoftKey(getActivity());
            AndroidNetworking.forceCancel("SignUpFragmentEmail");
            RelativeLayoutMain.getViewTreeObserver().removeOnGlobalLayoutListener(RelativeLayoutMainListener);
        }
    }

    public static class SignInFragment extends Fragment
    {
        private ViewTreeObserver.OnGlobalLayoutListener RelativeLayoutMainListener;
        private int RelativeLayoutMainHeightDifference = 0;
        private RelativeLayout RelativeLayoutMain;

        private boolean RequestUsername = false;
        private boolean RequestPassword = false;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup Parent, Bundle savedInstanceState)
        {
            final Context context = getActivity();
            final Button ButtonSignIn = new Button(context, null, android.R.attr.borderlessButtonStyle);
            final LoadingView LoadingViewSignIn = new LoadingView(context);

            RelativeLayoutMain = new RelativeLayout(context);
            RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            RelativeLayoutMain.setBackgroundResource(R.color.White);
            RelativeLayoutMain.setClickable(true);

            RelativeLayoutMainListener = new ViewTreeObserver.OnGlobalLayoutListener()
            {
                @Override
                public void onGlobalLayout()
                {
                    Rect rect = new Rect();
                    RelativeLayoutMain.getWindowVisibleDisplayFrame(rect);

                    int ScreenHeight = RelativeLayoutMain.getHeight();
                    int DifferenceHeight = ScreenHeight - (rect.bottom - rect.top);

                    if (DifferenceHeight > ScreenHeight / 3 && DifferenceHeight != RelativeLayoutMainHeightDifference)
                    {
                        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ScreenHeight - DifferenceHeight));
                        RelativeLayoutMainHeightDifference = DifferenceHeight;
                    }
                    else if (DifferenceHeight != RelativeLayoutMainHeightDifference)
                    {
                        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ScreenHeight));
                        RelativeLayoutMainHeightDifference = DifferenceHeight;
                    }
                    else if (RelativeLayoutMainHeightDifference != 0)
                    {
                        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ScreenHeight + Math.abs(RelativeLayoutMainHeightDifference)));
                        RelativeLayoutMainHeightDifference = 0;
                    }

                    RelativeLayoutMain.requestLayout();
                }
            };

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
            TextViewHeader.setText(getString(R.string.SignUpFragmentEmailTitle));
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

            RelativeLayout.LayoutParams ScrollViewMainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            ScrollViewMainParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

            ScrollView ScrollViewMain = new ScrollView(context);
            ScrollViewMain.setLayoutParams(ScrollViewMainParam);
            ScrollViewMain.setFillViewport(true);
            ScrollViewMain.setId(MiscHandler.GenerateViewID());

            RelativeLayoutMain.addView(ScrollViewMain);

            RelativeLayout RelativeLayoutMain2 = new RelativeLayout(context);
            RelativeLayoutMain2.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

            ScrollViewMain.addView(RelativeLayoutMain2);

            RelativeLayout.LayoutParams TextViewEmailOrUsernameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewEmailOrUsernameParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));

            TextView TextViewEmailOrUsername = new TextView(context);
            TextViewEmailOrUsername.setLayoutParams(TextViewEmailOrUsernameParam);
            TextViewEmailOrUsername.setTextColor(ContextCompat.getColor(context, R.color.Gray4));
            TextViewEmailOrUsername.setText(getString(R.string.SignInFragmentEmailOrUsername));
            TextViewEmailOrUsername.setTypeface(null, Typeface.BOLD);
            TextViewEmailOrUsername.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            TextViewEmailOrUsername.setId(MiscHandler.GenerateViewID());

            RelativeLayoutMain2.addView(TextViewEmailOrUsername);

            RelativeLayout.LayoutParams EditTextEmailOrUsernameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            EditTextEmailOrUsernameParam.addRule(RelativeLayout.BELOW, TextViewEmailOrUsername.getId());
            EditTextEmailOrUsernameParam.setMargins(MiscHandler.ToDimension(context, 10), 0, MiscHandler.ToDimension(context, 10), 0);

            final EditText EditTextEmailOrUsername = new EditText(new ContextThemeWrapper(context, R.style.GeneralEditTextTheme));
            EditTextEmailOrUsername.setLayoutParams(EditTextEmailOrUsernameParam);
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

            RelativeLayoutMain2.addView(EditTextEmailOrUsername);

            RelativeLayout.LayoutParams TextViewPasswordParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewPasswordParam.addRule(RelativeLayout.BELOW, EditTextEmailOrUsername.getId());
            TextViewPasswordParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));

            TextView TextViewPassword = new TextView(context);
            TextViewPassword.setLayoutParams(TextViewPasswordParam);
            TextViewPassword.setTextColor(ContextCompat.getColor(context, R.color.Gray4));
            TextViewPassword.setText(getString(R.string.SignInFragmentPassword));
            TextViewPassword.setTypeface(null, Typeface.BOLD);
            TextViewPassword.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            TextViewPassword.setId(MiscHandler.GenerateViewID());

            RelativeLayoutMain2.addView(TextViewPassword);

            RelativeLayout.LayoutParams EditTextPasswordParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            EditTextPasswordParam.addRule(RelativeLayout.BELOW, TextViewPassword.getId());
            EditTextPasswordParam.setMargins(MiscHandler.ToDimension(context, 10), 0, MiscHandler.ToDimension(context, 10), 0);

            final EditText EditTextPassword = new EditText(new ContextThemeWrapper(context, R.style.GeneralEditTextTheme));
            EditTextPassword.setLayoutParams(EditTextPasswordParam);
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

            RelativeLayoutMain2.addView(EditTextPassword);

            RelativeLayout.LayoutParams TextViewMessageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewMessageParam.addRule(RelativeLayout.BELOW, EditTextPassword.getId());

            TextView TextViewMessage = new TextView(context);
            TextViewMessage.setLayoutParams(TextViewMessageParam);
            TextViewMessage.setTextColor(ContextCompat.getColor(context, R.color.Black));
            TextViewMessage.setText(getString(R.string.SignInFragmentMessage));
            TextViewMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            TextViewMessage.setId(MiscHandler.GenerateViewID());
            TextViewMessage.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));

            RelativeLayoutMain2.addView(TextViewMessage);

            RelativeLayout.LayoutParams RelativeLayoutBottomParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            RelativeLayoutBottomParam.addRule(RelativeLayout.BELOW, TextViewMessage.getId());

            RelativeLayout RelativeLayoutBottom = new RelativeLayout(context);
            RelativeLayoutBottom.setLayoutParams(RelativeLayoutBottomParam);

            RelativeLayoutMain2.addView(RelativeLayoutBottom);

            RelativeLayout.LayoutParams TextViewResetParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewResetParam.addRule(RelativeLayout.CENTER_VERTICAL);
            TextViewResetParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

            TextView TextViewReset = new TextView(context);
            TextViewReset.setLayoutParams(TextViewResetParam);
            TextViewReset.setTextColor(ContextCompat.getColor(context, R.color.BlueLight));
            TextViewReset.setText(getString(R.string.SignInFragmentForgot));
            TextViewReset.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            TextViewReset.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
            TextViewReset.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.WelcomeActivityContainer, new ResetFragment()).addToBackStack("ResetFragment").commitAllowingStateLoss();
                }
            });

            RelativeLayoutBottom.addView(TextViewReset);

            RelativeLayout.LayoutParams RelativeLayoutNextParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 90), MiscHandler.ToDimension(context, 35));
            RelativeLayoutNextParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            RelativeLayoutNextParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));

            GradientDrawable ShapeEnable = new GradientDrawable();
            ShapeEnable.setColor(ContextCompat.getColor(context, R.color.BlueLight));
            ShapeEnable.setCornerRadius(MiscHandler.ToDimension(context, 7));

            RelativeLayout RelativeLayoutNext = new RelativeLayout(context);
            RelativeLayoutNext.setLayoutParams(RelativeLayoutNextParam);
            RelativeLayoutNext.setBackground(ShapeEnable);

            RelativeLayoutBottom.addView(RelativeLayoutNext);

            GradientDrawable ShapeDisable = new GradientDrawable();
            ShapeDisable.setCornerRadius(MiscHandler.ToDimension(context, 7));
            ShapeDisable.setColor(ContextCompat.getColor(context, R.color.Gray2));

            StateListDrawable StateSignUp = new StateListDrawable();
            StateSignUp.addState(new int[] { android.R.attr.state_enabled }, ShapeEnable);
            StateSignUp.addState(new int[] { -android.R.attr.state_enabled }, ShapeDisable);

            ButtonSignIn.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 90), MiscHandler.ToDimension(context, 35)));
            ButtonSignIn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            ButtonSignIn.setTextColor(ContextCompat.getColor(context, R.color.White));
            ButtonSignIn.setText(getString(R.string.SignInFragmentTitle));
            ButtonSignIn.setBackground(StateSignUp);
            ButtonSignIn.setPadding(0, 0, 0, 0);
            ButtonSignIn.setEnabled(false);
            ButtonSignIn.setAllCaps(false);
            ButtonSignIn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    ButtonSignIn.setVisibility(View.GONE);
                    LoadingViewSignIn.Start();

                    AndroidNetworking.post(MiscHandler.GetRandomServer("SignIn"))
                    .addBodyParameter("EmailOrUsername", EditTextEmailOrUsername.getText().toString())
                    .addBodyParameter("Password", EditTextPassword.getText().toString())
                    .addBodyParameter("Session", GenerateSession())
                    .setTag("FragmentSignUpEmail")
                    .build()
                    .getAsString(new StringRequestListener()
                    {
                        @Override
                        public void onResponse(String Response)
                        {
                            LoadingViewSignIn.Stop();
                            ButtonSignIn.setVisibility(View.VISIBLE);

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

                                    getActivity().startActivity(new Intent(context, MainActivity.class));
                                    getActivity().finish();
                                    return;
                                }

                                MiscHandler.Toast(context, getString(R.string.SignInFragmentError));
                            }
                            catch (Exception e)
                            {
                                MiscHandler.Debug("WelcomeActivity-RequestSignIn: " + e.toString());
                            }
                        }

                        @Override
                        public void onError(ANError anError)
                        {
                            LoadingViewSignIn.Stop();
                            ButtonSignIn.setVisibility(View.VISIBLE);
                            MiscHandler.Toast(context, getString(R.string.NoInternet));
                        }
                    });
                }
            });

            RelativeLayoutNext.addView(ButtonSignIn);

            RelativeLayout.LayoutParams LoadingViewUsernameParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 90), MiscHandler.ToDimension(context, 35));
            LoadingViewUsernameParam.addRule(RelativeLayout.CENTER_IN_PARENT);

            LoadingViewSignIn.setLayoutParams(LoadingViewUsernameParam);
            LoadingViewSignIn.SetColor(R.color.White);

            RelativeLayoutNext.addView(LoadingViewSignIn);

            return RelativeLayoutMain;
        }

        @Override
        public void onResume()
        {
            super.onResume();
            RelativeLayoutMain.getViewTreeObserver().addOnGlobalLayoutListener(RelativeLayoutMainListener);

            InputMethodManager IMM = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            IMM.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }

        @Override
        public void onPause()
        {
            super.onPause();
            MiscHandler.HideSoftKey(getActivity());
            AndroidNetworking.forceCancel("SignInFragment");
            RelativeLayoutMain.getViewTreeObserver().removeOnGlobalLayoutListener(RelativeLayoutMainListener);
        }
    }

    public static class ResetFragment extends Fragment
    {
        private ViewTreeObserver.OnGlobalLayoutListener RelativeLayoutMainListener;
        private int RelativeLayoutMainHeightDifference = 0;
        private RelativeLayout RelativeLayoutMain;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup Parent, Bundle savedInstanceState)
        {
            final Context context = getActivity();
            final Button ButtonReset = new Button(context, null, android.R.attr.borderlessButtonStyle);
            final LoadingView LoadingViewReset = new LoadingView(context);
            final LinearLayout LinearLayoutLoading = new LinearLayout(context);

            RelativeLayoutMain = new RelativeLayout(context);
            RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            RelativeLayoutMain.setBackgroundResource(R.color.White);
            RelativeLayoutMain.setClickable(true);

            RelativeLayoutMainListener = new ViewTreeObserver.OnGlobalLayoutListener()
            {
                @Override
                public void onGlobalLayout()
                {
                    Rect rect = new Rect();
                    RelativeLayoutMain.getWindowVisibleDisplayFrame(rect);

                    int ScreenHeight = RelativeLayoutMain.getHeight();
                    int DifferenceHeight = ScreenHeight - (rect.bottom - rect.top);

                    if (DifferenceHeight > ScreenHeight / 3 && DifferenceHeight != RelativeLayoutMainHeightDifference)
                    {
                        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ScreenHeight - DifferenceHeight));
                        RelativeLayoutMainHeightDifference = DifferenceHeight;
                    }
                    else if (DifferenceHeight != RelativeLayoutMainHeightDifference)
                    {
                        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ScreenHeight));
                        RelativeLayoutMainHeightDifference = DifferenceHeight;
                    }
                    else if (RelativeLayoutMainHeightDifference != 0)
                    {
                        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ScreenHeight + Math.abs(RelativeLayoutMainHeightDifference)));
                        RelativeLayoutMainHeightDifference = 0;
                    }

                    RelativeLayoutMain.requestLayout();
                }
            };

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
            TextViewHeader.setText(getString(R.string.ResetFragmentTitle));
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

            RelativeLayout.LayoutParams ScrollViewMainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            ScrollViewMainParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

            ScrollView ScrollViewMain = new ScrollView(context);
            ScrollViewMain.setLayoutParams(ScrollViewMainParam);
            ScrollViewMain.setFillViewport(true);
            ScrollViewMain.setId(MiscHandler.GenerateViewID());

            RelativeLayoutMain.addView(ScrollViewMain);

            RelativeLayout RelativeLayoutMain2 = new RelativeLayout(context);
            RelativeLayoutMain2.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

            ScrollViewMain.addView(RelativeLayoutMain2);

            RelativeLayout.LayoutParams TextViewMessageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextViewMessageParam.setMargins(MiscHandler.ToDimension(context, 25), MiscHandler.ToDimension(context, 25), MiscHandler.ToDimension(context, 25), MiscHandler.ToDimension(context, 25));
            TextViewMessageParam.addRule(RelativeLayout.CENTER_HORIZONTAL);

            TextView TextViewMessage = new TextView(context);
            TextViewMessage.setLayoutParams(TextViewMessageParam);
            TextViewMessage.setTextColor(ContextCompat.getColor(context, R.color.Black));
            TextViewMessage.setText(getString(R.string.ResetFragmentMessage));
            TextViewMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            TextViewMessage.setGravity(Gravity.CENTER);
            TextViewMessage.setId(MiscHandler.GenerateViewID());

            RelativeLayoutMain2.addView(TextViewMessage);

            RelativeLayout.LayoutParams EditTextEmailOrUsernameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 45));
            EditTextEmailOrUsernameParam.addRule(RelativeLayout.BELOW, TextViewMessage.getId());
            EditTextEmailOrUsernameParam.setMargins(MiscHandler.ToDimension(context, 15), 0, MiscHandler.ToDimension(context, 15), 0);

            final EditText EditTextEmailOrUsername = new EditText(new ContextThemeWrapper(context, R.style.GeneralEditTextTheme));
            EditTextEmailOrUsername.setLayoutParams(EditTextEmailOrUsernameParam);
            EditTextEmailOrUsername.setGravity(Gravity.CENTER);
            EditTextEmailOrUsername.setPadding(MiscHandler.ToDimension(context, 10),MiscHandler.ToDimension(context, 10),MiscHandler.ToDimension(context, 10),MiscHandler.ToDimension(context, 10));
            EditTextEmailOrUsername.setHint(getString(R.string.ResetFragmentEmailOrUsername));
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

            RelativeLayoutMain2.addView(EditTextEmailOrUsername);

            RelativeLayout.LayoutParams ButtonResetParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 45));
            ButtonResetParam.addRule(RelativeLayout.BELOW, EditTextEmailOrUsername.getId());
            ButtonResetParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));

            GradientDrawable ShapeEnable = new GradientDrawable();
            ShapeEnable.setColor(ContextCompat.getColor(context, R.color.BlueLight));
            ShapeEnable.setCornerRadius(MiscHandler.ToDimension(context, 7));

            GradientDrawable ShapeDisable = new GradientDrawable();
            ShapeDisable.setCornerRadius(MiscHandler.ToDimension(context, 7));
            ShapeDisable.setColor(ContextCompat.getColor(context, R.color.Gray2));

            StateListDrawable StateReset = new StateListDrawable();
            StateReset.addState(new int[] { android.R.attr.state_enabled }, ShapeEnable);
            StateReset.addState(new int[] { -android.R.attr.state_enabled }, ShapeDisable);

            ButtonReset.setLayoutParams(ButtonResetParam);
            ButtonReset.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            ButtonReset.setTextColor(ContextCompat.getColor(context, R.color.White));
            ButtonReset.setText(getString(R.string.ResetFragmentSubmit));
            ButtonReset.setBackground(StateReset);
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
                    .setTag("ResetPassword")
                    .build()
                    .getAsString(new StringRequestListener()
                    {
                        @Override
                        public void onResponse(String Response)
                        {
                            LoadingViewReset.Stop();
                            LinearLayoutLoading.setVisibility(View.INVISIBLE);
                            ButtonReset.setVisibility(View.VISIBLE);

                            try
                            {
                                if (new JSONObject(Response).getInt("Message") == 1000)
                                    MiscHandler.Toast(context, getString(R.string.ResetFragmentSuccess));
                                else
                                    MiscHandler.Toast(context, getString(R.string.ResetFragmentError));
                            }
                            catch (Exception e)
                            {
                                MiscHandler.Debug("WelcomeActivity-RequestReset: " + e.toString());
                            }
                        }

                        @Override
                        public void onError(ANError anError)
                        {
                            LoadingViewReset.Stop();
                            LinearLayoutLoading.setVisibility(View.INVISIBLE);
                            ButtonReset.setVisibility(View.VISIBLE);
                            MiscHandler.Toast(context, getString(R.string.NoInternet));
                        }
                    });
                }
            });

            RelativeLayoutMain2.addView(ButtonReset);

            RelativeLayout.LayoutParams LinearLayoutLoadingParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 45));
            LinearLayoutLoadingParam.addRule(RelativeLayout.BELOW, EditTextEmailOrUsername.getId());
            LinearLayoutLoadingParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));

            LinearLayoutLoading.setLayoutParams(LinearLayoutLoadingParam);
            LinearLayoutLoading.setVisibility(View.INVISIBLE);
            LinearLayoutLoading.setGravity(Gravity.CENTER);
            LinearLayoutLoading.setBackground(ShapeEnable);

            RelativeLayoutMain2.addView(LinearLayoutLoading);

            RelativeLayout.LayoutParams LoadingViewResetParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            LoadingViewResetParam.addRule(RelativeLayout.CENTER_IN_PARENT);

            LoadingViewReset.setLayoutParams(LoadingViewResetParam);
            LoadingViewReset.SetColor(R.color.White);

            LinearLayoutLoading.addView(LoadingViewReset);

            return RelativeLayoutMain;
        }

        @Override
        public void onResume()
        {
            super.onResume();
            RelativeLayoutMain.getViewTreeObserver().addOnGlobalLayoutListener(RelativeLayoutMainListener);

            InputMethodManager IMM = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            IMM.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }

        @Override
        public void onPause()
        {
            super.onPause();
            MiscHandler.HideSoftKey(getActivity());
            AndroidNetworking.forceCancel("ResetFragment");
            RelativeLayoutMain.getViewTreeObserver().removeOnGlobalLayoutListener(RelativeLayoutMainListener);
        }
    }

    */
}
