package co.biogram.main.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.json.JSONObject;

import co.biogram.fragment.FragmentActivity;
import co.biogram.fragment.FragmentBase;
import co.biogram.main.R;
import co.biogram.main.handler.MiscHandler;
import co.biogram.main.misc.LoadingView;

class SignUpPhone extends FragmentBase
{
    private ViewTreeObserver.OnGlobalLayoutListener RelativeLayoutMainListener;
    private int RelativeLayoutMainHeightDifference = 0;
    private RelativeLayout RelativeLayoutMain;

    @Override
    public void OnCreate()
    {
        final FragmentActivity activity = GetActivity();
        final Button ButtonMain = new Button(activity, null, android.R.attr.borderlessButtonStyle);
        final LoadingView LoadingViewMain = new LoadingView(activity);

        RelativeLayoutMain = new RelativeLayout(activity);
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

                if (DifferenceHeight > (ScreenHeight / 3) && DifferenceHeight != RelativeLayoutMainHeightDifference)
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

        RelativeLayout RelativeLayoutHeader = new RelativeLayout(activity);
        RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(activity, 56)));
        RelativeLayoutHeader.setBackgroundResource(R.color.BlueLight);
        RelativeLayoutHeader.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain.addView(RelativeLayoutHeader);

        RelativeLayout.LayoutParams ImageViewBackParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(activity, 56), RelativeLayout.LayoutParams.MATCH_PARENT);
        ImageViewBackParam.addRule(MiscHandler.Align("R"));

        ImageView ImageViewBack = new ImageView(activity);
        ImageViewBack.setLayoutParams(ImageViewBackParam);
        ImageViewBack.setScaleType(ImageView.ScaleType.FIT_XY);
        ImageViewBack.setId(MiscHandler.GenerateViewID());
        ImageViewBack.setPadding(MiscHandler.ToDimension(activity, 12), MiscHandler.ToDimension(activity, 12), MiscHandler.ToDimension(activity, 12), MiscHandler.ToDimension(activity, 12));
        ImageViewBack.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { GetActivity().onBackPressed(); } });

        if (MiscHandler.IsFA())
            ImageViewBack.setImageResource(R.drawable.ic_back_white_fa);
        else
            ImageViewBack.setImageResource(R.drawable.ic_back_white);

        RelativeLayoutHeader.addView(ImageViewBack);

        RelativeLayout.LayoutParams TextViewHeaderParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewHeaderParam.addRule(MiscHandler.AlignTo("R"), ImageViewBack.getId());
        TextViewHeaderParam.addRule(RelativeLayout.CENTER_VERTICAL);

        TextView TextViewHeader = new TextView(activity);
        TextViewHeader.setLayoutParams(TextViewHeaderParam);
        TextViewHeader.setTextColor(ContextCompat.getColor(activity, R.color.White));
        TextViewHeader.setText(activity.getString(R.string.SignUpPhone));
        TextViewHeader.setTypeface(null, Typeface.BOLD);
        TextViewHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

        if (MiscHandler.IsFA())
            TextViewHeader.setTypeface(Typeface.createFromAsset(activity.getAssets(), "iran-sans.ttf"));

        RelativeLayoutHeader.addView(TextViewHeader);

        RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(activity, 1));
        ViewLineParam.addRule(RelativeLayout.BELOW, RelativeLayoutHeader.getId());

        View ViewLine = new View(activity);
        ViewLine.setLayoutParams(ViewLineParam);
        ViewLine.setBackgroundResource(R.color.Gray2);
        ViewLine.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain.addView(ViewLine);

        RelativeLayout.LayoutParams ScrollViewMainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        ScrollViewMainParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

        ScrollView ScrollViewMain = new ScrollView(activity);
        ScrollViewMain.setLayoutParams(ScrollViewMainParam);
        ScrollViewMain.setFillViewport(true);

        RelativeLayoutMain.addView(ScrollViewMain);

        RelativeLayout RelativeLayoutMain2 = new RelativeLayout(activity);
        RelativeLayoutMain2.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

        ScrollViewMain.addView(RelativeLayoutMain2);

        RelativeLayout.LayoutParams TextViewPhoneParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewPhoneParam.setMargins(MiscHandler.ToDimension(activity, 15), MiscHandler.ToDimension(activity, 15), MiscHandler.ToDimension(activity, 15), MiscHandler.ToDimension(activity, 15));
        TextViewPhoneParam.addRule(MiscHandler.Align("R"));

        TextView TextViewPhone = new TextView(activity);
        TextViewPhone.setLayoutParams(TextViewPhoneParam);
        TextViewPhone.setTextColor(ContextCompat.getColor(activity, R.color.Gray4));
        TextViewPhone.setText(activity.getString(R.string.SignUpPhoneNumber));
        TextViewPhone.setTypeface(null, Typeface.BOLD);
        TextViewPhone.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        TextViewPhone.setId(MiscHandler.GenerateViewID());

        if (MiscHandler.IsFA())
            TextViewPhone.setTypeface(Typeface.createFromAsset(activity.getAssets(), "iran-sans.ttf"));

        RelativeLayoutMain2.addView(TextViewPhone);

        RelativeLayout.LayoutParams EditTextUsernameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        EditTextUsernameParam.addRule(RelativeLayout.BELOW, TextViewPhone.getId());
        EditTextUsernameParam.setMargins(MiscHandler.ToDimension(activity, 10), 0, MiscHandler.ToDimension(activity, 10), 0);

        final EditText EditTextUsername = new EditText(new ContextThemeWrapper(activity, R.style.GeneralEditTextTheme));
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
        EditTextUsername.getBackground().setColorFilter(ContextCompat.getColor(activity, R.color.BlueLight), PorterDuff.Mode.SRC_ATOP);
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
                    ButtonMain.setEnabled(true);
                else
                    ButtonMain.setEnabled(false);
            }
        });

        RelativeLayoutMain2.addView(EditTextUsername);

        RelativeLayout.LayoutParams TextViewMessageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewMessageParam.addRule(RelativeLayout.BELOW, EditTextUsername.getId());

        TextView TextViewMessage = new TextView(activity);
        TextViewMessage.setLayoutParams(TextViewMessageParam);
        TextViewMessage.setTextColor(ContextCompat.getColor(activity, R.color.Black));
        TextViewMessage.setText(activity.getString(R.string.SignUpFragmentUsernameMessage));
        TextViewMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        TextViewMessage.setId(MiscHandler.GenerateViewID());
        TextViewMessage.setPadding(MiscHandler.ToDimension(activity, 15), MiscHandler.ToDimension(activity, 15), MiscHandler.ToDimension(activity, 15), MiscHandler.ToDimension(activity, 15));

        RelativeLayoutMain2.addView(TextViewMessage);

        RelativeLayout.LayoutParams RelativeLayoutBottomParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayoutBottomParam.addRule(RelativeLayout.BELOW, TextViewMessage.getId());

        RelativeLayout RelativeLayoutBottom = new RelativeLayout(activity);
        RelativeLayoutBottom.setLayoutParams(RelativeLayoutBottomParam);

        RelativeLayoutMain2.addView(RelativeLayoutBottom);

        RelativeLayout.LayoutParams TextViewPrivacyParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewPrivacyParam.addRule(RelativeLayout.CENTER_VERTICAL);
        TextViewPrivacyParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

        TextView TextViewPrivacy = new TextView(activity);
        TextViewPrivacy.setLayoutParams(TextViewPrivacyParam);
        TextViewPrivacy.setTextColor(ContextCompat.getColor(activity, R.color.BlueLight));
        TextViewPrivacy.setText(activity.getString(R.string.WelcomeActivityGeneralTerm));
        TextViewPrivacy.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        TextViewPrivacy.setPadding(MiscHandler.ToDimension(activity, 15), MiscHandler.ToDimension(activity, 15), MiscHandler.ToDimension(activity, 15), MiscHandler.ToDimension(activity, 15));
        TextViewPrivacy.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://biogram.co/"))); } });

        RelativeLayoutBottom.addView(TextViewPrivacy);

        RelativeLayout.LayoutParams RelativeLayoutNextParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(activity, 90), MiscHandler.ToDimension(activity, 35));
        RelativeLayoutNextParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        RelativeLayoutNextParam.setMargins(MiscHandler.ToDimension(activity, 15), MiscHandler.ToDimension(activity, 15), MiscHandler.ToDimension(activity, 15), MiscHandler.ToDimension(activity, 15));

        GradientDrawable ShapeEnable = new GradientDrawable();
        ShapeEnable.setColor(ContextCompat.getColor(activity, R.color.BlueLight));
        ShapeEnable.setCornerRadius(MiscHandler.ToDimension(activity, 7));

        RelativeLayout RelativeLayoutNext = new RelativeLayout(activity);
        RelativeLayoutNext.setLayoutParams(RelativeLayoutNextParam);
        RelativeLayoutNext.setBackground(ShapeEnable);

        RelativeLayoutBottom.addView(RelativeLayoutNext);

        GradientDrawable ShapeDisable = new GradientDrawable();
        ShapeDisable.setCornerRadius(MiscHandler.ToDimension(activity, 7));
        ShapeDisable.setColor(ContextCompat.getColor(activity, R.color.Gray2));

        StateListDrawable StateSignUp = new StateListDrawable();
        StateSignUp.addState(new int[] { android.R.attr.state_enabled }, ShapeEnable);
        StateSignUp.addState(new int[] { -android.R.attr.state_enabled }, ShapeDisable);

        ButtonMain.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(activity, 90), MiscHandler.ToDimension(activity, 35)));
        ButtonMain.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        ButtonMain.setTextColor(ContextCompat.getColor(activity, R.color.White));
        ButtonMain.setText(activity.getString(R.string.WelcomeActivityGeneralNext));
        ButtonMain.setBackground(StateSignUp);
        ButtonMain.setPadding(0, 0, 0, 0);
        ButtonMain.setEnabled(false);
        ButtonMain.setAllCaps(false);
        ButtonMain.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ButtonMain.setVisibility(View.GONE);
                LoadingViewMain.Start();

                AndroidNetworking.post(MiscHandler.GetRandomServer("UsernameIsAvailable"))
                .addBodyParameter("Username", EditTextUsername.getText().toString())
                .setTag("SignUpFragmentUsername")
                .build()
                .getAsString(new StringRequestListener()
                {
                    @Override
                    public void onResponse(String Response)
                    {
                        ButtonMain.setVisibility(View.VISIBLE);
                        LoadingViewMain.Stop();

                        try
                        {
                            if (new JSONObject(Response).getInt("Message") == 1000)
                            {
                                // WelcomeActivity Parent = (WelcomeActivity) getActivity();
                                // Parent.Username = EditTextUsername.getText().toString();
                                //Parent.getSupportFragmentManager().beginTransaction().add(R.id.WelcomeActivityContainer, new SignUpFragmentPassword()).addToBackStack("SignUpFragmentPassword").commitAllowingStateLoss();
                                return;
                            }

                            MiscHandler.Toast(activity, activity.getString(R.string.SignUpFragmentUsernameError));
                        }
                        catch (Exception e)
                        {
                            MiscHandler.Debug("WelcomeActivity-RequestUsername: " + e.toString());
                        }
                    }

                    @Override
                    public void onError(ANError anError)
                    {
                        LoadingViewMain.Stop();
                        ButtonMain.setVisibility(View.VISIBLE);
                        MiscHandler.Toast(activity, activity.getString(R.string.NoInternet));
                    }
                });
            }
        });

        RelativeLayoutNext.addView(ButtonMain);

        RelativeLayout.LayoutParams LoadingViewUsernameParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(activity, 90), MiscHandler.ToDimension(activity, 35));
        LoadingViewUsernameParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        LoadingViewMain.setLayoutParams(LoadingViewUsernameParam);
        LoadingViewMain.SetColor(R.color.White);

        RelativeLayoutNext.addView(LoadingViewMain);

        SetRootView(RelativeLayoutMain);
    }

    @Override
    public void OnResume()
    {
        RelativeLayoutMain.getViewTreeObserver().addOnGlobalLayoutListener(RelativeLayoutMainListener);

        InputMethodManager IMM = (InputMethodManager) GetActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        IMM.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    @Override
    public void OnPause()
    {
        RelativeLayoutMain.getViewTreeObserver().removeOnGlobalLayoutListener(RelativeLayoutMainListener);
        AndroidNetworking.forceCancel("SignUpPhone");
    }
}
