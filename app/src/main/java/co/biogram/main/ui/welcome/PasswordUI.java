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

import co.biogram.main.fragment.FragmentView;
import co.biogram.main.R;
import co.biogram.main.handler.Misc;
import co.biogram.main.ui.view.Button;
import co.biogram.main.ui.view.TextView;

class PasswordUI extends FragmentView
{
    private ViewTreeObserver.OnGlobalLayoutListener LayoutListener;
    private RelativeLayout RelativeLayoutMain;
    private final String Username;

    PasswordUI(String username)
    {
        Username = username;
    }

    @Override
    public void OnCreate()
    {
        final Button ButtonNext = new Button(Activity, 16, false);

        RelativeLayoutMain = new RelativeLayout(Activity);
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayoutMain.setBackgroundResource(R.color.TextDark);
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

        RelativeLayout RelativeLayoutHeader = new RelativeLayout(Activity);
        RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56)));
        RelativeLayoutHeader.setBackgroundResource(R.color.Primary);
        RelativeLayoutHeader.setId(Misc.generateViewId());

        RelativeLayoutMain.addView(RelativeLayoutHeader);

        RelativeLayout.LayoutParams ImageViewBackParam = new RelativeLayout.LayoutParams(Misc.ToDP(56), Misc.ToDP(56));
        ImageViewBackParam.addRule(Misc.Align("R"));

        ImageView ImageViewBack = new ImageView(Activity);
        ImageViewBack.setLayoutParams(ImageViewBackParam);
        ImageViewBack.setScaleType(ImageView.ScaleType.FIT_XY);
        ImageViewBack.setId(Misc.generateViewId());
        ImageViewBack.setImageResource(Misc.IsRTL() ? R.drawable.back_white_rtl : R.drawable.back_white);
        ImageViewBack.setPadding(Misc.ToDP(12), Misc.ToDP(12), Misc.ToDP(12), Misc.ToDP(12));
        ImageViewBack.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { Activity.onBackPressed(); } });

        RelativeLayoutHeader.addView(ImageViewBack);

        RelativeLayout.LayoutParams TextViewTitleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTitleParam.addRule(Misc.AlignTo("R"), ImageViewBack.getId());
        TextViewTitleParam.addRule(RelativeLayout.CENTER_VERTICAL);

        TextView TextViewTitle = new TextView(Activity, 16, true);
        TextViewTitle.setLayoutParams(TextViewTitleParam);
        TextViewTitle.setPadding(0, Misc.ToDP(6), 0, 0);
        TextViewTitle.setText(Misc.String(R.string.GeneralPassword));

        RelativeLayoutHeader.addView(TextViewTitle);

        RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1));
        ViewLineParam.addRule(RelativeLayout.BELOW, RelativeLayoutHeader.getId());

        View ViewLine = new View(Activity);
        ViewLine.setLayoutParams(ViewLineParam);
        ViewLine.setBackgroundResource(R.color.Gray);
        ViewLine.setId(Misc.generateViewId());

        RelativeLayoutMain.addView(ViewLine);

        RelativeLayout.LayoutParams ScrollViewMainParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        ScrollViewMainParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

        ScrollView ScrollViewMain = new ScrollView(Activity);
        ScrollViewMain.setLayoutParams(ScrollViewMainParam);
        ScrollViewMain.setFillViewport(true);

        RelativeLayoutMain.addView(ScrollViewMain);

        RelativeLayout RelativeLayoutScroll = new RelativeLayout(Activity);
        RelativeLayoutScroll.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

        ScrollViewMain.addView(RelativeLayoutScroll);

        RelativeLayout.LayoutParams TextViewPasswordParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewPasswordParam.setMargins(Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15));

        TextView TextViewPassword = new TextView(Activity, 16, false);
        TextViewPassword.setLayoutParams(TextViewPasswordParam);
        TextViewPassword.SetColor(R.color.Gray);
        TextViewPassword.setText(Misc.String(R.string.GeneralPassword));
        TextViewPassword.setId(Misc.generateViewId());

        RelativeLayoutScroll.addView(TextViewPassword);

        RelativeLayout.LayoutParams EditTextPasswordParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        EditTextPasswordParam.addRule(RelativeLayout.BELOW, TextViewPassword.getId());
        EditTextPasswordParam.setMargins(Misc.ToDP(10), 0, Misc.ToDP(10), 0);

        final EditText EditTextPassword = new EditText(Activity);
        EditTextPassword.setLayoutParams(EditTextPasswordParam);
        EditTextPassword.setId(Misc.generateViewId());
        EditTextPassword.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        EditTextPassword.setFilters(new InputFilter[] { new InputFilter.LengthFilter(32) });
        EditTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        EditTextPassword.getBackground().setColorFilter(ContextCompat.getColor(Activity, R.color.Primary), PorterDuff.Mode.SRC_ATOP);
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

        TextView TextViewMessage = new TextView(Activity, 14, false);
        TextViewMessage.setLayoutParams(TextViewMessageParam);
        TextViewMessage.SetColor(R.color.TextWhite);
        TextViewMessage.setText(Misc.String(R.string.PasswordUIMessage));
        TextViewMessage.setId(Misc.generateViewId());
        TextViewMessage.setPadding(Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15));

        RelativeLayoutScroll.addView(TextViewMessage);

        RelativeLayout.LayoutParams RelativeLayoutBottomParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayoutBottomParam.addRule(RelativeLayout.BELOW, TextViewMessage.getId());

        RelativeLayout RelativeLayoutBottom = new RelativeLayout(Activity);
        RelativeLayoutBottom.setLayoutParams(RelativeLayoutBottomParam);

        RelativeLayoutScroll.addView(RelativeLayoutBottom);

        RelativeLayout.LayoutParams TextViewPrivacyParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewPrivacyParam.addRule(RelativeLayout.CENTER_VERTICAL);
        TextViewPrivacyParam.addRule(Misc.Align("R"));

        TextView TextViewPrivacy = new TextView(Activity, 14, false);
        TextViewPrivacy.setLayoutParams(TextViewPrivacyParam);
        TextViewPrivacy.SetColor(R.color.Primary);
        TextViewPrivacy.setText(Misc.String(R.string.GeneralTerm));
        TextViewPrivacy.setPadding(Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15));
        TextViewPrivacy.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { Activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://biogram.co"))); } });

        RelativeLayoutBottom.addView(TextViewPrivacy);

        RelativeLayout.LayoutParams RelativeLayoutNextParam = new RelativeLayout.LayoutParams(Misc.ToDP(90), Misc.ToDP(35));
        RelativeLayoutNextParam.setMargins(Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15));
        RelativeLayoutNextParam.addRule(Misc.Align("L"));

        GradientDrawable DrawableEnable = new GradientDrawable();
        DrawableEnable.setColor(ContextCompat.getColor(Activity, R.color.Primary));
        DrawableEnable.setCornerRadius(Misc.ToDP(7));

        GradientDrawable DrawableDisable = new GradientDrawable();
        DrawableDisable.setCornerRadius(Misc.ToDP(7));
        DrawableDisable.setColor(ContextCompat.getColor(Activity, R.color.Gray));

        StateListDrawable ListDrawableNext = new StateListDrawable();
        ListDrawableNext.addState(new int[] { android.R.attr.state_enabled }, DrawableEnable);
        ListDrawableNext.addState(new int[] { -android.R.attr.state_enabled }, DrawableDisable);

        RelativeLayout RelativeLayoutNext = new RelativeLayout(Activity);
        RelativeLayoutNext.setLayoutParams(RelativeLayoutNextParam);
        RelativeLayoutNext.setBackground(ListDrawableNext);

        RelativeLayoutBottom.addView(RelativeLayoutNext);

        ButtonNext.setLayoutParams(new RelativeLayout.LayoutParams(Misc.ToDP(90), Misc.ToDP(35)));
        ButtonNext.setText(Misc.String(R.string.GeneralNext));
        ButtonNext.setBackground(ListDrawableNext);
        ButtonNext.setEnabled(false);
        ButtonNext.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                TranslateAnimation Anim = Misc.IsRTL() ? new TranslateAnimation(0f, -1000f, 0f, 0f) : new TranslateAnimation(0f, 1000f, 0f, 0f);
                Anim.setDuration(200);

                RelativeLayoutMain.setAnimation(Anim);

                Activity.GetManager().OpenView(new EmailUI(Username, EditTextPassword.getText().toString()), R.id.ContainerFull, "EmailUI");
            }
        });

        RelativeLayoutNext.addView(ButtonNext);

        TranslateAnimation Anim = Misc.IsRTL() ? new TranslateAnimation(1000f, 0f, 0f, 0f) : new TranslateAnimation(-1000f, 0f, 0f, 0f);
        Anim.setDuration(200);
        Anim.setAnimationListener(new Animation.AnimationListener()
        {
            @Override public void onAnimationStart(Animation animation) { }
            @Override public void onAnimationRepeat(Animation animation) { }
            @Override public void onAnimationEnd(Animation animation) { Misc.ShowSoftKey(EditTextPassword); }
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
        Misc.HideSoftKey(Activity);
        RelativeLayoutMain.getViewTreeObserver().removeOnGlobalLayoutListener(LayoutListener);
    }
}
