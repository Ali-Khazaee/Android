package co.biogram.main.ui.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import co.biogram.main.R;
import co.biogram.main.handler.Misc;

public class ScrollNumber extends LinearLayout {
    private TextView Number;
    private int Value;
    private int Max;

    public ScrollNumber(Context c) {
        super(c);

        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);

        ImageView Up = new ImageView(c);
        Up.setLayoutParams(new LinearLayout.LayoutParams(Misc.ToDP(32), Misc.ToDP(32)));
        Up.setImageResource(R.drawable._write_arrow_up);
        Up.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (++Value >= Max)
                    Value = Max;

                Anim();
            }
        });

        addView(Up);

        Number = new TextView(c, 14, false);
        Number.setLayoutParams(new LinearLayout.LayoutParams(Misc.ToDP(32), Misc.ToDP(32)));
        Number.setGravity(Gravity.CENTER);
        Number.SetColor(R.color.White);
        Number.setText("0");

        addView(Number);

        ImageView Down = new ImageView(c);
        Down.setLayoutParams(new LinearLayout.LayoutParams(Misc.ToDP(32), Misc.ToDP(32)));
        Down.setImageResource(R.drawable.general_arrow_down);
        Down.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (--Value < 0)
                    Value = 0;

                Anim();
            }
        });

        addView(Down);
    }

    public int GetValue() {
        return Value;
    }

    public void SetValues(int current, int max) {
        Max = max;
        Value = current;

        Number.setText(String.valueOf(Value));
    }

    private void Anim() {
        ObjectAnimator SizeX = ObjectAnimator.ofFloat(Number, "scaleX", 1.5f);
        SizeX.setDuration(100);

        ObjectAnimator SizeY = ObjectAnimator.ofFloat(Number, "scaleY", 1.5f);
        SizeY.setDuration(100);

        ObjectAnimator Fade = ObjectAnimator.ofFloat(Number, "alpha", 0.1f, 1f);
        Fade.setDuration(200);

        ObjectAnimator SizeX2 = ObjectAnimator.ofFloat(Number, "scaleX", 1f);
        SizeX2.setDuration(100);
        SizeX2.setStartDelay(100);

        ObjectAnimator SizeY2 = ObjectAnimator.ofFloat(Number, "scaleY", 1f);
        SizeY2.setDuration(100);
        SizeY2.setStartDelay(100);

        AnimatorSet AnimationSet = new AnimatorSet();
        AnimationSet.playTogether(SizeX, SizeY, Fade, SizeX2, SizeY2);
        AnimationSet.start();

        Number.setText(String.valueOf(Value));
    }
}
