package co.biogram.main.handler;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.atomic.AtomicInteger;

import co.biogram.main.App;
import co.biogram.main.R;

public class MiscHandler
{
    private static final AtomicInteger NextGeneratedID = new AtomicInteger(1);

    public static int GenerateViewID()
    {
        for (;;)
        {
            int Result = NextGeneratedID.get();

            int Value = Result + 1;

            if (Value > 0x00FFFFFF)
                Value = 1;

            if (NextGeneratedID.compareAndSet(Result, Value))
                return Result;
        }
    }

    public static void Toast(Context context, String Message)
    {
        GradientDrawable Shape = new GradientDrawable();
        Shape.setCornerRadius(50.0f);
        Shape.setColor(ContextCompat.getColor(context, R.color.Toast));

        RelativeLayout Root = new RelativeLayout(context);
        Root.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        Root.setBackground(Shape);

        RelativeLayout.LayoutParams TextViewMessageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewMessageParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        TextView TextViewMessage = new TextView(context);
        TextViewMessage.setLayoutParams(TextViewMessageParam);
        TextViewMessage.setTextColor(ContextCompat.getColor(context, R.color.Black));
        TextViewMessage.setText(Message);
        TextViewMessage.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 10));
        TextViewMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        Root.addView(TextViewMessage);

        Toast toast = new Toast(context);
        toast.setGravity(Gravity.BOTTOM, 0, MiscHandler.ToDimension(context, 65));
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(Root);
        toast.show();
    }

    public static int ToDimension(Context context, float Value)
    {
        DisplayMetrics Metrics = context.getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, Value, Metrics);
    }

    @Deprecated
    public static int ToDimension(float Value)
    {
        DisplayMetrics Metrics = App.GetContext().getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, Value, Metrics);
    }

    public static String GetTimeName(long T)
    {
        long Time = ((System.currentTimeMillis() - (T * 1000)) / 1000);

        long SEC = (long) Math.round(Time % 60);
        Time /= 60;
        long MIN = (long) Math.round(Time % 60);
        Time /= 60;
        long HOUR = (long) Math.round(Time % 24);
        Time /= 24;
        long DAY = (long) Math.round(Time % 24);
        Time /= 7;
        long WEEK = (long) Math.round(Time % 7);

        if (WEEK > 0)
            return WEEK + "w";
        else if (DAY > 0)
            return DAY + "d";
        else if (HOUR > 0)
            return HOUR + "h";
        else if (MIN > 0)
            return MIN + "m";
        else if (SEC > 0)
            return SEC + "s";

        return "";
    }

    public static void HideSoftKey(Activity activity)
    {
        View view = activity.getCurrentFocus();

        if (view != null)
        {
            InputMethodManager IMM = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            IMM.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void Log(String Message)
    {
        Log.e("Bio", Message);
    }
}
