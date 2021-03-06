package co.biogram.main.ui.welcome;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;

import org.json.JSONException;
import org.json.JSONObject;

import co.biogram.main.R;
import co.biogram.main.fragment.FragmentView;
import co.biogram.main.handler.Misc;
import co.biogram.main.service.NetworkService;
import co.biogram.main.ui.view.LoadingView;

class UsernameUI extends FragmentView {
    private final int Type;
    private ViewTreeObserver.OnGlobalLayoutListener LayoutListener;
    private RelativeLayout RelativeLayoutMain;
    private String Code;

    UsernameUI() {
        Type = 2;
    }

    UsernameUI(String code, int type) {
        Code = code;
        Type = type;
    }

    @Override
    public void OnCreate() {

        View view = View.inflate(Activity, R.layout.welcome_username, null);

        final Button ButtonNext = view.findViewById(R.id.buttonNextStep);
        final LoadingView LoadingViewNext = new LoadingView(Activity);

        view.findViewById(R.id.ImageButtonBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity.onBackPressed();
            }
        });

        InputMethodManager imm = (InputMethodManager) Activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        final EditText EditTextUsername = view.findViewById(R.id.EditTextUsername);
        Misc.SetCursorColor(EditTextUsername, R.color.Primary);
        Misc.changeEditTextUnderlineColor(EditTextUsername, R.color.Primary, R.color.Gray);
        EditTextUsername.setFilters(new InputFilter[]{new InputFilter.LengthFilter(32), new InputFilter() {
            @Override
            public CharSequence filter(CharSequence s, int Start, int End, Spanned d, int ds, int de) {
                if (End > Start) {
                    char[] AllowChar = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '.', '_', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

                    for (int I = Start; I < End; I++) {
                        if (!new String(AllowChar).contains(String.valueOf(s.charAt(I)))) {
                            return "";
                        }
                    }
                }

                return null;
            }
        }});
        EditTextUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ButtonNext.setEnabled(s.length() > 2);
            }
        });

        TextView TextViewPrivacy = view.findViewById(R.id.textViewTerm);
        TextViewPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://biogram.co")));
            }
        });

        ButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONObject signupObject = new JSONObject();
                try {
                    signupObject.put("Username", EditTextUsername.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                NetworkService.Once(NetworkService.PACKET_USERNAME, new NetworkService.Listener() {
                    @Override
                    public void Call(String Message) {
                        try {
                            JSONObject result = new JSONObject(Message);

                            switch (result.getInt("Result")) {
                                case -1:
                                case -2:
                                case -3:
                                    Toast.makeText(Activity, "Internal Server Error", Toast.LENGTH_LONG).show();
                                    break;
                                case 1:
                                    Toast.makeText(Activity, "Username Undefined", Toast.LENGTH_LONG).show();
                                    break;
                                case 2:
                                    Toast.makeText(Activity, "Number Already Exists", Toast.LENGTH_LONG).show();
                                    break;

                                default:
                                    Activity.GetManager().OpenView(new PasswordUI(Code, EditTextUsername.getText().toString(), Type), "PhoneVerfiyUI", true);

                            }
                            Log.d("MESSAGE", Message);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
                NetworkService.Send(NetworkService.PACKET_USERNAME, signupObject.toString());

            }
        });

        ViewMain = view;
        Misc.fontSetter(view);
    }

    @Override
    public void OnPause() {
        Misc.HideSoftKey(Activity);
        AndroidNetworking.forceCancel("UsernameUI");
    }
}
