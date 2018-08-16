package co.biogram.main.ui.welcome;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import co.biogram.main.R;
import co.biogram.main.fragment.FragmentView;
import co.biogram.main.handler.Misc;
import co.biogram.main.ui.view.LoadingView;
import com.androidnetworking.AndroidNetworking;

class UsernameUI extends FragmentView
{
    private final int Type;
    private ViewTreeObserver.OnGlobalLayoutListener LayoutListener;
    private RelativeLayout RelativeLayoutMain;
    private String Code;

    UsernameUI()
    {
        Type = 2;
    }

    UsernameUI(String code, int type)
    {
        Code = code;
        Type = type;
    }

    @Override
    public void OnCreate()
    {

        View view = View.inflate(Activity, R.layout.welcome_username, null);

        final Button ButtonNext = view.findViewById(R.id.buttonNextStep);
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

        final EditText EditTextUsername = view.findViewById(R.id.EditTextUsername);
        Misc.SetCursorColor(EditTextUsername, R.color.Primary);
        Misc.changeEditTextUnderlineColor(EditTextUsername, R.color.Primary, R.color.Gray);
        EditTextUsername.setFilters(new InputFilter[] { new InputFilter.LengthFilter(32), new InputFilter()
        {
            @Override
            public CharSequence filter(CharSequence s, int Start, int End, Spanned d, int ds, int de)
            {
                if (End > Start)
                {
                    char[] AllowChar = new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '.', '_', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

                    for (int I = Start; I < End; I++)
                    {
                        if (!new String(AllowChar).contains(String.valueOf(s.charAt(I))))
                        {
                            return "";
                        }
                    }
                }

                return null;
            }
        } });
        EditTextUsername.addTextChangedListener(new TextWatcher()
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
                ButtonNext.setEnabled(s.length() > 2);
            }
        });

        TextView TextViewPrivacy = view.findViewById(R.id.textViewTerm);
        TextViewPrivacy.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://biogram.co")));
            }
        });

        ButtonNext.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Activity.GetManager().OpenView(new DescriptionUI(Code, EditTextUsername.getText().toString(), Type), "PhoneVerfiyUI", true);

                //                ButtonNext.setEnabled(false);
                //                LoadingViewNext.Start();
                //
                //                AndroidNetworking.post(Misc.GetRandomServer("Username")).addBodyParameter("Username", EditTextUsername.getText().toString()).setTag("UsernameUI").build().getAsString(new StringRequestListener()
                //                {
                //                    @Override
                //                    public void onResponse(String Response)
                //                    {
                //                        LoadingViewNext.Stop();
                //                        ButtonNext.setEnabled(true);
                //
                //                        try
                //                        {
                //                            JSONObject Result = new JSONObject(Response);
                //
                //                            switch (Result.getInt("Message"))
                //                            {
                //                                case 0:
                //                                    TranslateAnimation Anim = Misc.IsRTL() ? new TranslateAnimation(0f, -1000f, 0f, 0f) : new TranslateAnimation(0f, 1000f, 0f, 0f);
                //                                    Anim.setDuration(200);
                //
                //                                    RelativeLayoutMain.setAnimation(Anim);
                //
                //                                    if (Type == 0)
                //                                        Activity.GetManager().OpenView(new DescriptionUI(Code, EditTextUsername.getText().toString(), 0), "DescriptionUI", true);
                //                                    else if (Type == 1)
                //                                        Activity.GetManager().OpenView(new DescriptionUI(Code, EditTextUsername.getText().toString(), 1), "DescriptionUI", true);
                //                                    else if (Type == 2)
                //                                        Activity.GetManager().OpenView(new PasswordUI(EditTextUsername.getText().toString()), "PasswordUI", true);
                //                                    break;
                //                                case 1:
                //                                    Misc.ToastOld(Misc.String(R.string.UsernameUIError1));
                //                                    break;
                //                                case 2:
                //                                    Misc.ToastOld(Misc.String(R.string.UsernameUIError2));
                //                                    break;
                //                                case 3:
                //                                    Misc.ToastOld(Misc.String(R.string.UsernameUIError3));
                //                                    break;
                //                                case 4:
                //                                    Misc.ToastOld(Misc.String(R.string.UsernameUIError4));
                //                                    break;
                //                                case 5:
                //                                    Misc.ToastOld(Misc.String(R.string.UsernameUIError5));
                //                                    break;
                //                                default:
                //                                    Misc.GeneralError(Result.getInt("Message"));
                //                            }
                //                        }
                //                        catch (Exception e)
                //                        {
                //                            Misc.Debug("UsernameUI: " + e.toString());
                //                        }
                //                    }
                //
                //                    @Override
                //                    public void onError(ANError e)
                //                    {
                //                        LoadingViewNext.Stop();
                //                        ButtonNext.setEnabled(true);
                //                        Misc.ToastOld(Misc.String(R.string.GeneralNoInternet));
                //                    }
                //                });
            }
        });

        ViewMain = view;
        Misc.fontSetter(view);
    }

    @Override
    public void OnPause()
    {
        Misc.HideSoftKey(Activity);
        AndroidNetworking.forceCancel("UsernameUI");
    }
}
