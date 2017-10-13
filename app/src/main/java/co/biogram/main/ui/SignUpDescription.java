package co.biogram.main.ui;

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
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;

import co.biogram.fragment.FragmentActivity;
import co.biogram.fragment.FragmentBase;
import co.biogram.main.R;
import co.biogram.main.handler.MiscHandler;

class SignUpDescription extends FragmentBase
{
    private ViewTreeObserver.OnGlobalLayoutListener RelativeLayoutMainListener;
    private int RelativeLayoutMainHeightDifference = 0;
    private RelativeLayout RelativeLayoutMain;

    SignUpDescription(String code, String Username)
    {

    }

    @Override
    public void OnCreate()
    {
        final FragmentActivity activity = GetActivity();
        final Button ButtonFinish = new Button(activity, null, android.R.attr.borderlessButtonStyle);
        final LoadingView LoadingViewFinish = new LoadingView(activity);

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
        ImageViewBack.setImageResource(MiscHandler.IsFa() ? R.drawable.ic_back_white_fa : R.drawable.ic_back_white);
        ImageViewBack.setPadding(MiscHandler.ToDimension(activity, 12), MiscHandler.ToDimension(activity, 12), MiscHandler.ToDimension(activity, 12), MiscHandler.ToDimension(activity, 12));
        ImageViewBack.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { GetActivity().onBackPressed(); } });

        RelativeLayoutHeader.addView(ImageViewBack);

        RelativeLayout.LayoutParams TextViewTitleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewTitleParam.addRule(MiscHandler.AlignTo("R"), ImageViewBack.getId());
        TextViewTitleParam.addRule(RelativeLayout.CENTER_VERTICAL);

        TextView TextViewTitle = new TextView(activity);
        TextViewTitle.setLayoutParams(TextViewTitleParam);
        TextViewTitle.setTextColor(ContextCompat.getColor(activity, R.color.White));
        TextViewTitle.setText(activity.getString(R.string.SignUpDescription));
        TextViewTitle.setTypeface(null, Typeface.BOLD);
        TextViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

        RelativeLayoutHeader.addView(TextViewTitle);

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

        RelativeLayout RelativeLayoutScroll = new RelativeLayout(activity);
        RelativeLayoutScroll.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

        ScrollViewMain.addView(RelativeLayoutScroll);

        RelativeLayout.LayoutParams CircleImageViewProfileParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(activity, 75), MiscHandler.ToDimension(activity, 75));
        CircleImageViewProfileParam.setMargins(MiscHandler.ToDimension(activity, 25), MiscHandler.ToDimension(activity, 25), MiscHandler.ToDimension(activity, 15), MiscHandler.ToDimension(activity, 25));
        CircleImageViewProfileParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

        CircleImageView CircleImageViewProfile = new CircleImageView(activity);
        CircleImageViewProfile.setLayoutParams(CircleImageViewProfileParam);
        CircleImageViewProfile.setId(MiscHandler.GenerateViewID());
        CircleImageViewProfile.setCircleBackgroundColor(R.color.BlueLight);
        CircleImageViewProfile.setImageResource(R.drawable.ic_person_white);
        CircleImageViewProfile.setBorderColor(R.color.Gray);
        CircleImageViewProfile.setBorderWidth(2);
        CircleImageViewProfile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                
            }
        });

        RelativeLayoutScroll.addView(CircleImageViewProfile);

        RelativeLayout.LayoutParams LinearLayoutNameParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LinearLayoutNameParam.setMargins(0, MiscHandler.ToDimension(activity, 30), MiscHandler.ToDimension(activity, 15), 0);
        LinearLayoutNameParam.addRule(RelativeLayout.RIGHT_OF, CircleImageViewProfile.getId());

        LinearLayout LinearLayoutName = new LinearLayout(activity);
        LinearLayoutName.setLayoutParams(LinearLayoutNameParam);
        LinearLayoutName.setOrientation(LinearLayout.VERTICAL);
        LinearLayoutName.setId(MiscHandler.GenerateViewID());

        RelativeLayoutScroll.addView(LinearLayoutName);

        TextView TextViewName = new TextView(activity);
        TextViewName.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextViewName.setTextColor(ContextCompat.getColor(activity, R.color.Gray4));
        TextViewName.setText(activity.getString(R.string.SignUpDescriptionName));
        TextViewName.setTypeface(null, Typeface.BOLD);
        TextViewName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        TextViewName.setGravity(Gravity.START);

        LinearLayoutName.addView(TextViewName);

        final EditText EditTextName = new EditText(activity);
        EditTextName.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        EditTextName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        EditTextName.setFilters(new InputFilter[]
        {
            new InputFilter.LengthFilter(32), new InputFilter()
            {
                @Override
                public CharSequence filter(CharSequence s, int Start, int End, Spanned d, int ds, int de)
                {
                    for (int I = Start; I < End; I++)
                    {
                        int type = Character.getType(s.charAt(I));

                        if (type == Character.SURROGATE || type == Character.OTHER_SYMBOL)
                            return "";
                    }

                    return null;
                }
            }
        });
        EditTextName.setInputType(InputType.TYPE_CLASS_TEXT);
        EditTextName.getBackground().setColorFilter(ContextCompat.getColor(activity, R.color.BlueLight), PorterDuff.Mode.SRC_ATOP);
        EditTextName.requestFocus();
        EditTextName.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void afterTextChanged(Editable s) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (s.length() > 1)
                    ButtonFinish.setEnabled(true);
                else
                    ButtonFinish.setEnabled(false);
            }
        });

        LinearLayoutName.addView(EditTextName);

        RelativeLayout.LayoutParams TextViewDescriptionParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewDescriptionParam.setMargins(MiscHandler.ToDimension(activity, 15), MiscHandler.ToDimension(activity, 15), MiscHandler.ToDimension(activity, 15), 0);
        TextViewDescriptionParam.addRule(RelativeLayout.BELOW, LinearLayoutName.getId());
        TextViewDescriptionParam.addRule(MiscHandler.Align("R"));

        TextView TextViewDescription = new TextView(activity);
        TextViewDescription.setLayoutParams(TextViewDescriptionParam);
        TextViewDescription.setTextColor(ContextCompat.getColor(activity, R.color.Gray4));
        TextViewDescription.setText(activity.getString(R.string.SignUpDescriptionInfo));
        TextViewDescription.setTypeface(null, Typeface.BOLD);
        TextViewDescription.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        TextViewDescription.setId(MiscHandler.GenerateViewID());

        RelativeLayoutScroll.addView(TextViewDescription);

        RelativeLayout.LayoutParams EditTextDescriptionParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        EditTextDescriptionParam.setMargins(MiscHandler.ToDimension(activity, 15), 0, MiscHandler.ToDimension(activity, 15), 0);
        EditTextDescriptionParam.addRule(RelativeLayout.BELOW, TextViewDescription.getId());

        EditText EditTextDescription = new EditText(activity);
        EditTextDescription.setLayoutParams(EditTextDescriptionParam);
        EditTextDescription.setId(MiscHandler.GenerateViewID());
        EditTextDescription.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        EditTextDescription.setFilters(new InputFilter[] { new InputFilter.LengthFilter(150) });
        EditTextDescription.setInputType(InputType.TYPE_CLASS_TEXT);
        EditTextDescription.getBackground().setColorFilter(ContextCompat.getColor(activity, R.color.BlueLight), PorterDuff.Mode.SRC_ATOP);
        EditTextDescription.setMaxLines(5);
        EditTextDescription.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        RelativeLayoutScroll.addView(EditTextDescription);

        RelativeLayout.LayoutParams RelativeLayoutBottomParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayoutBottomParam.addRule(RelativeLayout.BELOW, EditTextDescription.getId());

        RelativeLayout RelativeLayoutBottom = new RelativeLayout(activity);
        RelativeLayoutBottom.setLayoutParams(RelativeLayoutBottomParam);

        RelativeLayoutScroll.addView(RelativeLayoutBottom);

        RelativeLayout.LayoutParams TextViewPrivacyParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewPrivacyParam.addRule(RelativeLayout.CENTER_VERTICAL);
        TextViewPrivacyParam.addRule(MiscHandler.Align("R"));

        TextView TextViewPrivacy = new TextView(activity);
        TextViewPrivacy.setLayoutParams(TextViewPrivacyParam);
        TextViewPrivacy.setTextColor(ContextCompat.getColor(activity, R.color.BlueLight));
        TextViewPrivacy.setText(activity.getString(R.string.SignUpDescriptionTerm));
        TextViewPrivacy.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        TextViewPrivacy.setPadding(MiscHandler.ToDimension(activity, 15), MiscHandler.ToDimension(activity, 15), MiscHandler.ToDimension(activity, 15), MiscHandler.ToDimension(activity, 15));
        TextViewPrivacy.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://biogram.co"))); } });

        RelativeLayoutBottom.addView(TextViewPrivacy);

        RelativeLayout.LayoutParams RelativeLayoutFinishParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(activity, 90), MiscHandler.ToDimension(activity, 35));
        RelativeLayoutFinishParam.setMargins(MiscHandler.ToDimension(activity, 15), MiscHandler.ToDimension(activity, 15), MiscHandler.ToDimension(activity, 15), MiscHandler.ToDimension(activity, 15));
        RelativeLayoutFinishParam.addRule(MiscHandler.Align("L"));

        GradientDrawable GradientDrawableFinish = new GradientDrawable();
        GradientDrawableFinish.setColor(ContextCompat.getColor(activity, R.color.BlueLight));
        GradientDrawableFinish.setCornerRadius(MiscHandler.ToDimension(activity, 7));

        GradientDrawable GradientDrawableFinishDisable = new GradientDrawable();
        GradientDrawableFinishDisable.setCornerRadius(MiscHandler.ToDimension(activity, 7));
        GradientDrawableFinishDisable.setColor(ContextCompat.getColor(activity, R.color.Gray2));

        StateListDrawable StateListDrawableNext = new StateListDrawable();
        StateListDrawableNext.addState(new int[] { android.R.attr.state_enabled }, GradientDrawableFinish);
        StateListDrawableNext.addState(new int[] { -android.R.attr.state_enabled }, GradientDrawableFinishDisable);

        RelativeLayout RelativeLayoutFinish = new RelativeLayout(activity);
        RelativeLayoutFinish.setLayoutParams(RelativeLayoutFinishParam);
        RelativeLayoutFinish.setBackground(GradientDrawableFinish);

        RelativeLayoutBottom.addView(RelativeLayoutFinish);

        ButtonFinish.setLayoutParams(new RelativeLayout.LayoutParams(MiscHandler.ToDimension(activity, 90), MiscHandler.ToDimension(activity, 35)));
        ButtonFinish.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        ButtonFinish.setTextColor(ContextCompat.getColor(activity, R.color.White));
        ButtonFinish.setText(activity.getString(R.string.SignUpDescriptionFinish));
        ButtonFinish.setBackground(StateListDrawableNext);
        ButtonFinish.setPadding(0, 0, 0, 0);
        ButtonFinish.setEnabled(false);
        ButtonFinish.setAllCaps(false);
        ButtonFinish.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });

        RelativeLayoutFinish.addView(ButtonFinish);

        RelativeLayout.LayoutParams LoadingViewFinishParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(activity, 90), MiscHandler.ToDimension(activity, 35));
        LoadingViewFinishParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        LoadingViewFinish.setLayoutParams(LoadingViewFinishParam);
        LoadingViewFinish.SetColor(R.color.White);

        RelativeLayoutFinish.addView(LoadingViewFinish);

        if (MiscHandler.IsRTL())
        {
            TextViewTitle.setTypeface(Typeface.createFromAsset(activity.getAssets(), "iran-sans.ttf"));

            TextViewName.setTypeface(Typeface.createFromAsset(activity.getAssets(), "iran-sans.ttf"));

            TextViewPrivacy.setTypeface(Typeface.createFromAsset(activity.getAssets(), "iran-sans.ttf"));

            ButtonFinish.setTypeface(Typeface.createFromAsset(activity.getAssets(), "iran-sans.ttf"));
        }

        TranslateAnimation Anim = MiscHandler.IsRTL() ? new TranslateAnimation(1000f, 0f, 0f, 0f) : new TranslateAnimation(-1000f, 0f, 0f, 0f);
        Anim.setDuration(200);
        Anim.setAnimationListener(new Animation.AnimationListener()
        {
            @Override public void onAnimationStart(Animation animation) { }
            @Override public void onAnimationRepeat(Animation animation) { }
            @Override public void onAnimationEnd(Animation animation) { MiscHandler.ShowSoftKey(EditTextName); }
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
        RelativeLayoutMain.getViewTreeObserver().removeOnGlobalLayoutListener(RelativeLayoutMainListener);
        AndroidNetworking.forceCancel("SignUpDescription");
        MiscHandler.HideSoftKey(GetActivity());
    }
}
