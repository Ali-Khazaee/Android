package co.biogram.main.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import co.biogram.main.R;
import co.biogram.main.handler.MiscHandler;
import co.biogram.main.handler.SharedHandler;
import co.biogram.main.view.LoadingView;

public class ChangePasswordFragment extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        final Context context = getActivity();

        final EditText EditTextCurrentPassword = new EditText(context);
        final EditText EditTextNewPassword = new EditText(context);
        final EditText EditTextConfirmNewPassword = new EditText(context);

        final Button ButtonChangePassword = new Button(context, null, android.R.attr.borderlessButtonStyle);
        final LoadingView LoadingViewChangePassword = new LoadingView(context);

        TextWatcher TextWatcherChange = new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void afterTextChanged(Editable s) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (EditTextCurrentPassword.length() > 5 && EditTextNewPassword.length() > 5 && EditTextConfirmNewPassword.length() > 5)
                    ButtonChangePassword.setEnabled(true);
                else
                    ButtonChangePassword.setEnabled(false);
            }
        };

        RelativeLayout RelativeLayoutMain = new RelativeLayout(context);
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayoutMain.setClickable(true);
        RelativeLayoutMain.setBackgroundResource(R.color.White);

        RelativeLayout RelativeLayoutHeader = new RelativeLayout(context);
        RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56)));
        RelativeLayoutHeader.setBackgroundResource(R.color.White5);
        RelativeLayoutHeader.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain.addView(RelativeLayoutHeader);

        ImageView ImageViewBack = new ImageView(context);
        ImageViewBack.setPadding(MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12), MiscHandler.ToDimension(context, 12));
        ImageViewBack.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewBack.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), MiscHandler.ToDimension(context, 56)));
        ImageViewBack.setImageResource(R.drawable.ic_back_blue);
        ImageViewBack.setId(MiscHandler.GenerateViewID());
        ImageViewBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                MiscHandler.HideSoftKey(getActivity());
                getActivity().onBackPressed();
            }
        });

        RelativeLayoutHeader.addView(ImageViewBack);

        RelativeLayout.LayoutParams TextViewTitleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTitleParam.addRule(RelativeLayout.RIGHT_OF, ImageViewBack.getId());
        TextViewTitleParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        TextView TextViewTitle = new TextView(context);
        TextViewTitle.setLayoutParams(TextViewTitleParam);
        TextViewTitle.setTextColor(ContextCompat.getColor(context, R.color.Black));
        TextViewTitle.setText(getString(R.string.ChangePasswordFragment));
        TextViewTitle.setTypeface(null, Typeface.BOLD);
        TextViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

        RelativeLayoutHeader.addView(TextViewTitle);

        RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, MiscHandler.ToDimension(context, 1));
        ViewLineParam.addRule(RelativeLayout.BELOW, RelativeLayoutHeader.getId());

        View ViewLine = new View(context);
        ViewLine.setLayoutParams(ViewLineParam);
        ViewLine.setId(MiscHandler.GenerateViewID());
        ViewLine.setBackgroundResource(R.color.Gray2);

        RelativeLayoutMain.addView(ViewLine);

        RelativeLayout.LayoutParams ScrollViewMainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        ScrollViewMainParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

        ScrollView ScrollViewMain = new ScrollView(context);
        ScrollViewMain.setLayoutParams(ScrollViewMainParam);

        RelativeLayoutMain.addView(ScrollViewMain);

        RelativeLayout RelativeLayoutMain2 = new RelativeLayout(context);
        RelativeLayoutMain2.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

        ScrollViewMain.addView(RelativeLayoutMain2);

        RelativeLayout.LayoutParams TextViewCurrentPasswordParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewCurrentPasswordParam.addRule(RelativeLayout.BELOW, ViewLine.getId());
        TextViewCurrentPasswordParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));

        TextView TextViewCurrentPassword = new TextView(context);
        TextViewCurrentPassword.setLayoutParams(TextViewCurrentPasswordParam);
        TextViewCurrentPassword.setTextColor(ContextCompat.getColor(context, R.color.Gray4));
        TextViewCurrentPassword.setText(getString(R.string.ChangePasswordFragmentCurrent));
        TextViewCurrentPassword.setTypeface(null, Typeface.BOLD);
        TextViewCurrentPassword.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        TextViewCurrentPassword.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain2.addView(TextViewCurrentPassword);

        RelativeLayout.LayoutParams EditTextCurrentPasswordParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        EditTextCurrentPasswordParam.addRule(RelativeLayout.BELOW, TextViewCurrentPassword.getId());
        EditTextCurrentPasswordParam.setMargins(MiscHandler.ToDimension(context, 10), 0, MiscHandler.ToDimension(context, 10), 0);

        EditTextCurrentPassword.setLayoutParams(EditTextCurrentPasswordParam);
        EditTextCurrentPassword.setMaxLines(1);
        EditTextCurrentPassword.setId(MiscHandler.GenerateViewID());
        EditTextCurrentPassword.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        EditTextCurrentPassword.setFilters(new InputFilter[] { new InputFilter.LengthFilter(32) });
        EditTextCurrentPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        EditTextCurrentPassword.requestFocus();
        EditTextCurrentPassword.addTextChangedListener(TextWatcherChange);

        RelativeLayoutMain2.addView(EditTextCurrentPassword);

        RelativeLayout.LayoutParams TextViewNewPasswordParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewNewPasswordParam.addRule(RelativeLayout.BELOW, EditTextCurrentPassword.getId());
        TextViewNewPasswordParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));

        TextView TextViewNewPassword = new TextView(context);
        TextViewNewPassword.setLayoutParams(TextViewNewPasswordParam);
        TextViewNewPassword.setTextColor(ContextCompat.getColor(context, R.color.Gray4));
        TextViewNewPassword.setText(getString(R.string.ChangePasswordFragmentNew));
        TextViewNewPassword.setTypeface(null, Typeface.BOLD);
        TextViewNewPassword.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        TextViewNewPassword.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain2.addView(TextViewNewPassword);

        RelativeLayout.LayoutParams EditTextNewPasswordParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        EditTextNewPasswordParam.addRule(RelativeLayout.BELOW, TextViewNewPassword.getId());
        EditTextNewPasswordParam.setMargins(MiscHandler.ToDimension(context, 10), 0, MiscHandler.ToDimension(context, 10), 0);

        EditTextNewPassword.setLayoutParams(EditTextNewPasswordParam);
        EditTextNewPassword.setMaxLines(1);
        EditTextNewPassword.setId(MiscHandler.GenerateViewID());
        EditTextNewPassword.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        EditTextNewPassword.setFilters(new InputFilter[] { new InputFilter.LengthFilter(32) });
        EditTextNewPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        EditTextNewPassword.addTextChangedListener(TextWatcherChange);

        RelativeLayoutMain2.addView(EditTextNewPassword);

        RelativeLayout.LayoutParams TextViewConfirmNewPasswordParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewConfirmNewPasswordParam.addRule(RelativeLayout.BELOW, EditTextNewPassword.getId());
        TextViewConfirmNewPasswordParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));

        TextView TextViewConfirmNewPassword = new TextView(context);
        TextViewConfirmNewPassword.setLayoutParams(TextViewConfirmNewPasswordParam);
        TextViewConfirmNewPassword.setTextColor(ContextCompat.getColor(context, R.color.Gray4));
        TextViewConfirmNewPassword.setText(getString(R.string.ChangePasswordFragmentConfirm));
        TextViewConfirmNewPassword.setTypeface(null, Typeface.BOLD);
        TextViewConfirmNewPassword.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        TextViewConfirmNewPassword.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain2.addView(TextViewConfirmNewPassword);

        RelativeLayout.LayoutParams EditTextConfirmNewPasswordParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        EditTextConfirmNewPasswordParam.addRule(RelativeLayout.BELOW, TextViewConfirmNewPassword.getId());
        EditTextConfirmNewPasswordParam.setMargins(MiscHandler.ToDimension(context, 10), 0, MiscHandler.ToDimension(context, 10), 0);

        EditTextConfirmNewPassword.setLayoutParams(EditTextConfirmNewPasswordParam);
        EditTextConfirmNewPassword.setMaxLines(1);
        EditTextConfirmNewPassword.setId(MiscHandler.GenerateViewID());
        EditTextConfirmNewPassword.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        EditTextConfirmNewPassword.setFilters(new InputFilter[] { new InputFilter.LengthFilter(32) });
        EditTextConfirmNewPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        EditTextConfirmNewPassword.addTextChangedListener(TextWatcherChange);

        RelativeLayoutMain2.addView(EditTextConfirmNewPassword);

        RelativeLayout.LayoutParams RelativeLayoutChangeParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 150), MiscHandler.ToDimension(context, 45));
        RelativeLayoutChangeParam.addRule(RelativeLayout.BELOW, EditTextConfirmNewPassword.getId());
        RelativeLayoutChangeParam.addRule(RelativeLayout.CENTER_HORIZONTAL);
        RelativeLayoutChangeParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));

        GradientDrawable ShapeEnable = new GradientDrawable();
        ShapeEnable.setColor(ContextCompat.getColor(context, R.color.BlueLight));
        ShapeEnable.setCornerRadius(MiscHandler.ToDimension(context, 7));

        RelativeLayout RelativeLayoutChange = new RelativeLayout(context);
        RelativeLayoutChange.setLayoutParams(RelativeLayoutChangeParam);
        RelativeLayoutChange.setBackground(ShapeEnable);

        RelativeLayoutMain2.addView(RelativeLayoutChange);

        GradientDrawable ShapeDisable = new GradientDrawable();
        ShapeDisable.setCornerRadius(MiscHandler.ToDimension(context, 7));
        ShapeDisable.setColor(ContextCompat.getColor(context, R.color.Gray2));

        StateListDrawable StateChange = new StateListDrawable();
        StateChange.addState(new int[] {android.R.attr.state_enabled}, ShapeEnable);
        StateChange.addState(new int[] {-android.R.attr.state_enabled}, ShapeDisable);

        ButtonChangePassword.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 150), MiscHandler.ToDimension(context, 45)));
        ButtonChangePassword.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        ButtonChangePassword.setTextColor(ContextCompat.getColor(context, R.color.White));
        ButtonChangePassword.setText(getString(R.string.ChangePasswordFragment));
        ButtonChangePassword.setBackground(StateChange);
        ButtonChangePassword.setPadding(0, 0, 0, 0);
        ButtonChangePassword.setEnabled(false);
        ButtonChangePassword.setAllCaps(false);
        ButtonChangePassword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (!EditTextNewPassword.getText().toString().equals(EditTextConfirmNewPassword.getText().toString()))
                {
                    MiscHandler.Toast(context, getString(R.string.ChangePasswordFragmentNewSame));
                    return;
                }

                ButtonChangePassword.setVisibility(View.GONE);
                LoadingViewChangePassword.Start();

                AndroidNetworking.post(MiscHandler.GetRandomServer("ChangePassword"))
                .addBodyParameter("PasswordCurrent", EditTextCurrentPassword.getText().toString())
                .addBodyParameter("PasswordNew", EditTextNewPassword.getText().toString())
                .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
                .setTag("ChangePasswordFragment")
                .build().getAsString(new StringRequestListener()
                {
                    @Override
                    public void onResponse(String Response)
                    {
                        try
                        {
                            JSONObject Result = new JSONObject(Response);
                            String Return = "";

                            switch (Result.getInt("Message"))
                            {
                                case 1: Return = getString(R.string.ChangePasswordFragmentCurrentEmpty); break;
                                case 2: Return = getString(R.string.ChangePasswordFragmentNewEmpty); break;
                                case 3: Return = getString(R.string.ChangePasswordFragmentNewBelow); break;
                                case 4: Return = getString(R.string.ChangePasswordFragmentNewAbove); break;
                                case 5: Return = getString(R.string.ChangePasswordFragmentAccount); break;
                                case 6: Return = getString(R.string.ChangePasswordFragmentWrong); break;
                                case 1000:
                                    Return = getString(R.string.ChangePasswordFragmentDone);

                                    MiscHandler.HideSoftKey(getActivity());
                                    getActivity().onBackPressed();
                                    break;
                            }

                            MiscHandler.Toast(context, Return);
                        }
                        catch (Exception e)
                        {
                            // Leave Me Alone
                        }

                        LoadingViewChangePassword.Stop();
                        ButtonChangePassword.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(ANError e)
                    {
                        LoadingViewChangePassword.Stop();
                        ButtonChangePassword.setVisibility(View.VISIBLE);
                        MiscHandler.Toast(context, getString(R.string.NoInternet));
                    }
                });
            }
        });

        RelativeLayoutChange.addView(ButtonChangePassword);

        RelativeLayout.LayoutParams LoadingViewUsernameParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 150), MiscHandler.ToDimension(context, 45));
        LoadingViewUsernameParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        LoadingViewChangePassword.setLayoutParams(LoadingViewUsernameParam);
        LoadingViewChangePassword.SetColor(R.color.White);

        RelativeLayoutChange.addView(LoadingViewChangePassword);

        return RelativeLayoutMain;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        AndroidNetworking.forceCancel("ChangePasswordFragment");
    }
}
