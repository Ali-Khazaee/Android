package co.biogram.main.ui.welcome;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.json.JSONObject;

import co.biogram.main.fragment.FragmentBase;
import co.biogram.main.R;
import co.biogram.main.handler.MiscHandler;
import co.biogram.main.ui.view.Button;
import co.biogram.main.ui.view.LoadingView;
import co.biogram.main.ui.view.TextView;

class PasswordResetUI extends FragmentBase
{
    private ViewTreeObserver.OnGlobalLayoutListener LayoutListener;
    private RelativeLayout RelativeLayoutMain;

    @Override
    public void OnCreate()
    {
        final Button ButtonFinish = new Button(GetActivity(), 16, false);
        final LoadingView LoadingViewFinish = new LoadingView(GetActivity());

        RelativeLayoutMain = new RelativeLayout(GetActivity());
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayoutMain.setBackgroundResource(R.color.White);
        RelativeLayoutMain.setClickable(true);

        LayoutListener = new ViewTreeObserver.OnGlobalLayoutListener()
        {
            int HeightDifference = 0;

            @Override
            public void onGlobalLayout()
            {
                Rect rect = new Rect();
                RelativeLayoutMain.getWindowVisibleDisplayFrame(rect);

                int ScreenHeight = RelativeLayoutMain.getHeight();
                int DifferenceHeight = ScreenHeight - (rect.bottom - rect.top);

                if (DifferenceHeight > (ScreenHeight / 3) && DifferenceHeight != HeightDifference)
                {
                    RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ScreenHeight - DifferenceHeight));
                    HeightDifference = DifferenceHeight;
                }
                else if (DifferenceHeight != HeightDifference)
                {
                    RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ScreenHeight));
                    HeightDifference = DifferenceHeight;
                }
                else if (HeightDifference != 0)
                {
                    RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ScreenHeight + Math.abs(HeightDifference)));
                    HeightDifference = 0;
                }

                RelativeLayoutMain.requestLayout();
            }
        };

        RelativeLayout RelativeLayoutHeader = new RelativeLayout(GetActivity());
        RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(GetActivity(), 56)));
        RelativeLayoutHeader.setBackgroundResource(R.color.BlueLight);
        RelativeLayoutHeader.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain.addView(RelativeLayoutHeader);

        RelativeLayout.LayoutParams ImageViewBackParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(GetActivity(), 56), MiscHandler.ToDimension(GetActivity(), 56));
        ImageViewBackParam.addRule(MiscHandler.Align("R"));

        ImageView ImageViewBack = new ImageView(GetActivity());
        ImageViewBack.setLayoutParams(ImageViewBackParam);
        ImageViewBack.setScaleType(ImageView.ScaleType.FIT_XY);
        ImageViewBack.setId(MiscHandler.GenerateViewID());
        ImageViewBack.setImageResource(MiscHandler.IsRTL() ? R.drawable.ic_back_white_rtl : R.drawable.ic_back_white);
        ImageViewBack.setPadding(MiscHandler.ToDimension(GetActivity(), 12), MiscHandler.ToDimension(GetActivity(), 12), MiscHandler.ToDimension(GetActivity(), 12), MiscHandler.ToDimension(GetActivity(), 12));
        ImageViewBack.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { GetActivity().onBackPressed(); } });

        RelativeLayoutHeader.addView(ImageViewBack);

        RelativeLayout.LayoutParams TextViewTitleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTitleParam.addRule(MiscHandler.AlignTo("R"), ImageViewBack.getId());
        TextViewTitleParam.addRule(RelativeLayout.CENTER_VERTICAL);

        TextView TextViewTitle = new TextView(GetActivity(), 16, true);
        TextViewTitle.setLayoutParams(TextViewTitleParam);
        TextViewTitle.setText(GetActivity().getString(R.string.PasswordResetUI));

        RelativeLayoutHeader.addView(TextViewTitle);

        RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(GetActivity(), 1));
        ViewLineParam.addRule(RelativeLayout.BELOW, RelativeLayoutHeader.getId());

        View ViewLine = new View(GetActivity());
        ViewLine.setLayoutParams(ViewLineParam);
        ViewLine.setBackgroundResource(R.color.Gray2);
        ViewLine.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain.addView(ViewLine);

        RelativeLayout.LayoutParams ScrollViewMainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        ScrollViewMainParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

        ScrollView ScrollViewMain = new ScrollView(GetActivity());
        ScrollViewMain.setLayoutParams(ScrollViewMainParam);
        ScrollViewMain.setFillViewport(true);

        RelativeLayoutMain.addView(ScrollViewMain);

        RelativeLayout RelativeLayoutScroll = new RelativeLayout(GetActivity());
        RelativeLayoutScroll.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

        ScrollViewMain.addView(RelativeLayoutScroll);

        RelativeLayout.LayoutParams TextViewUsernameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewUsernameParam.setMargins(MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15));
        TextViewUsernameParam.addRule(MiscHandler.Align("R"));

        TextView TextViewEmail = new TextView(GetActivity(), 16, false);
        TextViewEmail.setLayoutParams(TextViewUsernameParam);
        TextViewEmail.setTextColor(ContextCompat.getColor(GetActivity(), R.color.Gray4));
        TextViewEmail.setText(GetActivity().getString(R.string.GeneralEmailAddress));
        TextViewEmail.setId(MiscHandler.GenerateViewID());

        RelativeLayoutScroll.addView(TextViewEmail);

        RelativeLayout.LayoutParams EditTextUsernameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        EditTextUsernameParam.setMargins(MiscHandler.ToDimension(GetActivity(), 10), 0, MiscHandler.ToDimension(GetActivity(), 10), 0);
        EditTextUsernameParam.addRule(RelativeLayout.BELOW, TextViewEmail.getId());

        final EditText EditTextEmailOrUsername = new EditText(GetActivity());
        EditTextEmailOrUsername.setLayoutParams(EditTextUsernameParam);
        EditTextEmailOrUsername.setId(MiscHandler.GenerateViewID());
        EditTextEmailOrUsername.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        EditTextEmailOrUsername.setFilters(new InputFilter[] { new InputFilter.LengthFilter(128) });
        EditTextEmailOrUsername.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        EditTextEmailOrUsername.getBackground().setColorFilter(ContextCompat.getColor(GetActivity(), R.color.BlueLight), PorterDuff.Mode.SRC_ATOP);
        EditTextEmailOrUsername.requestFocus();
        EditTextEmailOrUsername.addTextChangedListener(new TextWatcher()
        {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void afterTextChanged(Editable s) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                ButtonFinish.setEnabled(s.length() > 2 && Patterns.EMAIL_ADDRESS.matcher(s).matches());
            }
        });

        RelativeLayoutScroll.addView(EditTextEmailOrUsername);

        RelativeLayout.LayoutParams TextViewMessageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewMessageParam.addRule(RelativeLayout.BELOW, EditTextEmailOrUsername.getId());

        TextView TextViewMessage = new TextView(GetActivity(), 14, false);
        TextViewMessage.setLayoutParams(TextViewMessageParam);
        TextViewMessage.setTextColor(ContextCompat.getColor(GetActivity(), R.color.Black));
        TextViewMessage.setText(GetActivity().getString(R.string.PasswordResetUIMessage));
        TextViewMessage.setId(MiscHandler.GenerateViewID());
        TextViewMessage.setPadding(MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15));

        RelativeLayoutScroll.addView(TextViewMessage);

        RelativeLayout.LayoutParams RelativeLayoutBottomParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayoutBottomParam.addRule(RelativeLayout.BELOW, TextViewMessage.getId());

        RelativeLayout RelativeLayoutBottom = new RelativeLayout(GetActivity());
        RelativeLayoutBottom.setLayoutParams(RelativeLayoutBottomParam);

        RelativeLayoutScroll.addView(RelativeLayoutBottom);

        RelativeLayout.LayoutParams TextViewPrivacyParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewPrivacyParam.addRule(RelativeLayout.CENTER_VERTICAL);
        TextViewPrivacyParam.addRule(MiscHandler.Align("R"));

        TextView TextViewPrivacy = new TextView(GetActivity(), 14, false);
        TextViewPrivacy.setLayoutParams(TextViewPrivacyParam);
        TextViewPrivacy.setTextColor(ContextCompat.getColor(GetActivity(), R.color.BlueLight));
        TextViewPrivacy.setText(GetActivity().getString(R.string.GeneralTerm));
        TextViewPrivacy.setPadding(MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15));
        TextViewPrivacy.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { GetActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://biogram.co"))); } });

        RelativeLayoutBottom.addView(TextViewPrivacy);

        RelativeLayout.LayoutParams RelativeLayoutNextParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(GetActivity(), 90), MiscHandler.ToDimension(GetActivity(), 35));
        RelativeLayoutNextParam.setMargins(MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15));
        RelativeLayoutNextParam.addRule(MiscHandler.Align("L"));

        GradientDrawable DrawableEnable = new GradientDrawable();
        DrawableEnable.setColor(ContextCompat.getColor(GetActivity(), R.color.BlueLight));
        DrawableEnable.setCornerRadius(MiscHandler.ToDimension(GetActivity(), 7));

        GradientDrawable DrawableDisable = new GradientDrawable();
        DrawableDisable.setCornerRadius(MiscHandler.ToDimension(GetActivity(), 7));
        DrawableDisable.setColor(ContextCompat.getColor(GetActivity(), R.color.Gray2));

        StateListDrawable ListDrawableNext = new StateListDrawable();
        ListDrawableNext.addState(new int[] { android.R.attr.state_enabled }, DrawableEnable);
        ListDrawableNext.addState(new int[] { -android.R.attr.state_enabled }, DrawableDisable);

        RelativeLayout RelativeLayoutNext = new RelativeLayout(GetActivity());
        RelativeLayoutNext.setLayoutParams(RelativeLayoutNextParam);
        RelativeLayoutNext.setBackground(ListDrawableNext);

        RelativeLayoutBottom.addView(RelativeLayoutNext);

        ButtonFinish.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(GetActivity(), 90), MiscHandler.ToDimension(GetActivity(), 35)));
        ButtonFinish.setText(GetActivity().getString(R.string.PasswordResetUIFinish));
        ButtonFinish.setBackground(ListDrawableNext);
        ButtonFinish.setEnabled(false);
        ButtonFinish.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ButtonFinish.setVisibility(View.GONE);
                LoadingViewFinish.Start();

                AndroidNetworking.post(MiscHandler.GetRandomServer("ResetPassword"))
                .addBodyParameter("EmailOrUsername", EditTextEmailOrUsername.getText().toString())
                .setTag("PasswordResetUI")
                .build()
                .getAsString(new StringRequestListener()
                {
                    @Override
                    public void onResponse(String Response)
                    {
                        LoadingViewFinish.Stop();
                        ButtonFinish.setVisibility(View.VISIBLE);

                        try
                        {
                            JSONObject Result = new JSONObject(Response);

                            switch (Result.getInt("Message"))
                            {
                                case 0:
                                    MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.PasswordResetUIError4));
                                    break;
                                case 1:
                                    MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.PasswordResetUIError1));
                                    break;
                                case 2:
                                    MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.PasswordResetUIError2));
                                    break;
                                case 3:
                                    MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.PasswordResetUIError3));
                                    break;
                                default:
                                    MiscHandler.GeneralError(GetActivity(), Result.getInt("Message"));
                            }
                        }
                        catch (Exception e)
                        {
                            MiscHandler.Debug("PasswordResetUI: " + e.toString());
                        }
                    }

                    @Override
                    public void onError(ANError e)
                    {
                        LoadingViewFinish.Stop();
                        ButtonFinish.setVisibility(View.VISIBLE);
                        MiscHandler.Toast(GetActivity(), GetActivity().getString(R.string.GeneralNoInternet));
                    }
                });
            }
        });

        RelativeLayoutNext.addView(ButtonFinish);

        RelativeLayout.LayoutParams LoadingViewNextParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(GetActivity(), 90), MiscHandler.ToDimension(GetActivity(), 35));
        LoadingViewNextParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        LoadingViewFinish.setLayoutParams(LoadingViewNextParam);
        LoadingViewFinish.SetColor(R.color.White);

        RelativeLayoutNext.addView(LoadingViewFinish);

        TranslateAnimation Anim = MiscHandler.IsRTL() ? new TranslateAnimation(1000f, 0f, 0f, 0f) : new TranslateAnimation(-1000f, 0f, 0f, 0f);
        Anim.setDuration(200);
        Anim.setAnimationListener(new Animation.AnimationListener()
        {
            @Override public void onAnimationStart(Animation animation) { }
            @Override public void onAnimationRepeat(Animation animation) { }
            @Override public void onAnimationEnd(Animation animation) { MiscHandler.ShowSoftKey(EditTextEmailOrUsername); }
        });

        RelativeLayoutMain.startAnimation(Anim);

        ViewMain = RelativeLayoutMain;
    }

    @Override
    public void OnResume()
    {
        RelativeLayoutMain.getViewTreeObserver().addOnGlobalLayoutListener(LayoutListener);
    }

    @Override
    public void OnPause()
    {
        AndroidNetworking.forceCancel("PasswordResetUI");
        RelativeLayoutMain.getViewTreeObserver().removeOnGlobalLayoutListener(LayoutListener);
    }
}