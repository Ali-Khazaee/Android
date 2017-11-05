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
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import co.biogram.main.fragment.FragmentBase;
import co.biogram.main.R;
import co.biogram.main.handler.MiscHandler;
import co.biogram.main.ui.view.Button;
import co.biogram.main.ui.view.TextView;

class SignUpPasswordUI extends FragmentBase
{
    private ViewTreeObserver.OnGlobalLayoutListener RelativeLayoutMainListener;
    private RelativeLayout RelativeLayoutMain;
    private int HeightDifference = 0;
    private String Username = "";

    SignUpPasswordUI(String username)
    {
        Username = username;
    }

    @Override
    public void OnCreate()
    {
        final Button ButtonNext = new Button(GetActivity(), 16, false);

        RelativeLayoutMain = new RelativeLayout(GetActivity());
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

                if (DifferenceHeight > ScreenHeight / 3 && DifferenceHeight != HeightDifference)
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

        ImageView ImageViewBack = new ImageView(GetActivity());
        ImageViewBack.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(GetActivity(), 56), RelativeLayout.LayoutParams.MATCH_PARENT));
        ImageViewBack.setScaleType(ImageView.ScaleType.FIT_XY);
        ImageViewBack.setId(MiscHandler.GenerateViewID());
        ImageViewBack.setImageResource(MiscHandler.IsRTL() ? R.drawable.ic_back_white_rtl : R.drawable.ic_back_white);
        ImageViewBack.setPadding(MiscHandler.ToDimension(GetActivity(), 12), MiscHandler.ToDimension(GetActivity(), 12), MiscHandler.ToDimension(GetActivity(), 12), MiscHandler.ToDimension(GetActivity(), 12));
        ImageViewBack.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { GetActivity().onBackPressed(); } });

        RelativeLayoutHeader.addView(ImageViewBack);

        RelativeLayout.LayoutParams TextViewTitleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTitleParam.addRule(RelativeLayout.RIGHT_OF, ImageViewBack.getId());
        TextViewTitleParam.addRule(RelativeLayout.CENTER_VERTICAL);

        TextView TextViewTitle = new TextView(GetActivity(), 16, true);
        TextViewTitle.setLayoutParams(TextViewTitleParam);
        TextViewTitle.setText(GetActivity().getString(R.string.SignUpPassword));

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
        ScrollViewMain.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain.addView(ScrollViewMain);

        RelativeLayout RelativeLayoutScroll = new RelativeLayout(GetActivity());
        RelativeLayoutScroll.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

        ScrollViewMain.addView(RelativeLayoutScroll);

        RelativeLayout.LayoutParams TextViewPasswordParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewPasswordParam.setMargins(MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15), MiscHandler.ToDimension(GetActivity(), 15));

        TextView TextViewPassword = new TextView(GetActivity(), 16, true);
        TextViewPassword.setLayoutParams(TextViewPasswordParam);
        TextViewPassword.setTextColor(ContextCompat.getColor(GetActivity(), R.color.Gray));
        TextViewPassword.setText(GetActivity().getString(R.string.SignUpPassword));
        TextViewPassword.setId(MiscHandler.GenerateViewID());

        RelativeLayoutScroll.addView(TextViewPassword);

        RelativeLayout.LayoutParams EditTextPasswordParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        EditTextPasswordParam.addRule(RelativeLayout.BELOW, TextViewPassword.getId());
        EditTextPasswordParam.setMargins(MiscHandler.ToDimension(GetActivity(), 10), 0, MiscHandler.ToDimension(GetActivity(), 10), 0);

        final EditText EditTextPassword = new EditText(GetActivity());
        EditTextPassword.setLayoutParams(EditTextPasswordParam);
        EditTextPassword.setId(MiscHandler.GenerateViewID());
        EditTextPassword.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        EditTextPassword.setFilters(new InputFilter[] { new InputFilter.LengthFilter(32) });
        EditTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        EditTextPassword.getBackground().setColorFilter(ContextCompat.getColor(GetActivity(), R.color.BlueLight), PorterDuff.Mode.SRC_ATOP);
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
                ButtonNext.setEnabled(s.length() > 5);
            }
        });

        RelativeLayoutScroll.addView(EditTextPassword);

        RelativeLayout.LayoutParams TextViewMessageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewMessageParam.addRule(RelativeLayout.BELOW, EditTextPassword.getId());

        TextView TextViewMessage = new TextView(GetActivity(), 14, false);
        TextViewMessage.setLayoutParams(TextViewMessageParam);
        TextViewMessage.setTextColor(ContextCompat.getColor(GetActivity(), R.color.Black));
        TextViewMessage.setText(GetActivity().getString(R.string.SignUpPasswordMessage));
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
        TextViewPrivacy.setText(GetActivity().getString(R.string.SignUpUsernameTerm));
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

        ButtonNext.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(GetActivity(), 90), MiscHandler.ToDimension(GetActivity(), 35)));
        ButtonNext.setTextColor(ContextCompat.getColor(GetActivity(), R.color.White));
        ButtonNext.setText(GetActivity().getString(R.string.SignUpUsernameNext));
        ButtonNext.setBackground(ListDrawableNext);
        ButtonNext.setPadding(0, 0, 0, 0);
        ButtonNext.setEnabled(false);
        ButtonNext.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                TranslateAnimation Anim = MiscHandler.IsRTL() ? new TranslateAnimation(0f, -1000f, 0f, 0f) : new TranslateAnimation(0f, 1000f, 0f, 0f);
                Anim.setDuration(200);

                RelativeLayoutMain.setAnimation(Anim);

                GetActivity().GetManager().OpenView(new SignUpEmailUI(Username, EditTextPassword.getText().toString()), R.id.WelcomeActivityContainer, "SignUpEmailUI");
            }
        });

        RelativeLayoutNext.addView(ButtonNext);

        TranslateAnimation Anim = MiscHandler.IsRTL() ? new TranslateAnimation(1000f, 0f, 0f, 0f) : new TranslateAnimation(-1000f, 0f, 0f, 0f);
        Anim.setDuration(200);
        Anim.setAnimationListener(new Animation.AnimationListener()
        {
            @Override public void onAnimationStart(Animation animation) { }
            @Override public void onAnimationRepeat(Animation animation) { }
            @Override public void onAnimationEnd(Animation animation) { MiscHandler.ShowSoftKey(EditTextPassword); }
        });

        RelativeLayoutMain.startAnimation(Anim);

        ViewMain = RelativeLayoutMain;
    }

    @Override
    public void OnResume()
    {
        RelativeLayoutMain.getViewTreeObserver().addOnGlobalLayoutListener(RelativeLayoutMainListener);
    }

    @Override
    public void OnPause()
    {
        MiscHandler.HideSoftKey(GetActivity());
        RelativeLayoutMain.getViewTreeObserver().removeOnGlobalLayoutListener(RelativeLayoutMainListener);
    }
}
