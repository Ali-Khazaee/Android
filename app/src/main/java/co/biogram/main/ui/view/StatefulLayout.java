package co.biogram.main.ui.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Keep;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import co.biogram.main.R;
import co.biogram.main.handler.Misc;

public class StatefulLayout extends LinearLayout {
    public StatefulLayout(Context context) {
        this(context, null);
    }

    public StatefulLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StatefulLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setGravity(Gravity.CENTER);
    }

    public void Loading() {
        Loading(Misc.ToDP(6), R.color.Gray);
    }

    public void Loading(int Size, int Color) {
        removeAllViews();
        setVisibility(VISIBLE);
        setOrientation(HORIZONTAL);

        final ArrayList<ValueAnimator> AnimatorList = new ArrayList<>();

        LayoutParams BounceParam = new LayoutParams(Size, Size);
        LayoutParams SpaceParam = new LayoutParams(Misc.ToDP(5), Size * 3);

        GradientDrawable DrawableBounce = new GradientDrawable();
        DrawableBounce.setShape(GradientDrawable.OVAL);
        DrawableBounce.setColor(Misc.Color(Color));

        for (int I = 0; I < 3; I++) {
            Bounce bounce = new Bounce(getContext());
            bounce.setBackground(DrawableBounce);

            addView(bounce, BounceParam);

            if (I < 2)
                addView(new View(getContext()), SpaceParam);

            ValueAnimator GrowAnim = ObjectAnimator.ofFloat(bounce, "scale", 1.0f, 2.0f, 1.0f);
            GrowAnim.setDuration(300);

            if (I == 2) {
                GrowAnim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        for (ValueAnimator animator : AnimatorList)
                            animator.start();
                    }
                });
            }

            GrowAnim.setStartDelay(I * 120);
            GrowAnim.start();

            AnimatorList.add(GrowAnim);
        }
    }

    public void Connection(int Size) {
        removeAllViews();
        setVisibility(VISIBLE);
        setOrientation(VERTICAL);

        ImageView ImageViewIcon = new ImageView(getContext());
        ImageViewIcon.setLayoutParams(new LayoutParams(Size, Size));
        ImageViewIcon.setImageResource(R.drawable.z_social_stateful_network);

        addView(ImageViewIcon);

        TextView TextViewMessage = new TextView(getContext());
        TextViewMessage.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        TextViewMessage.setText(Misc.String(R.string.StatefulLayoutNoConnection));
        TextViewMessage.setTypeface(Misc.GetTypeface());
        TextViewMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        TextViewMessage.setTextColor(Misc.Color(R.color.Gray));

        addView(TextViewMessage);

        TextView TextViewTry = new TextView(getContext());
        TextViewTry.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        TextViewTry.setText(Misc.String(R.string.StatefulLayoutTry));
        TextViewTry.setTypeface(Misc.GetTypeface());
        TextViewTry.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        TextViewTry.setTextColor(Misc.Color(R.color.Primary));

        addView(TextViewTry);
    }

    public void Content(int Size) {
        removeAllViews();
        setVisibility(VISIBLE);
        setOrientation(VERTICAL);

        ImageView ImageViewIcon = new ImageView(getContext());
        ImageViewIcon.setLayoutParams(new LayoutParams(Size, Size));
        ImageViewIcon.setImageResource(R.drawable.z_social_stateful_content);

        addView(ImageViewIcon);

        TextView TextViewMessage = new TextView(getContext());
        TextViewMessage.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        TextViewMessage.setText(Misc.String(R.string.StatefulLayoutNoContent));
        TextViewMessage.setTypeface(Misc.GetTypeface());
        TextViewMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        TextViewMessage.setTextColor(Misc.Color(R.color.Gray));

        addView(TextViewMessage);
    }

    public void Hide() {
        removeAllViews();
        setVisibility(GONE);
    }

    private class Bounce extends View {
        Bounce(Context context) {
            super(context);
        }

        @Keep
        @SuppressWarnings("unused")
        public void setScale(float Scale) {
            setScaleX(Scale);
            setScaleY(Scale);
        }
    }
}
