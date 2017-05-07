package co.biogram.main.handler;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.BitmapRequestListener;

import java.util.concurrent.atomic.AtomicInteger;

import co.biogram.main.App;
import co.biogram.main.R;

public class MiscHandler
{
    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    public static int GenerateViewID()
    {
        for (;;)
        {
            final int result = sNextGeneratedId.get();

            int newValue = result + 1;

            if (newValue > 0x00FFFFFF)
                newValue = 1;

            if (sNextGeneratedId.compareAndSet(result, newValue))
                return result;
        }
    }

    public static void Toast(Context context, String Message)
    {
        GradientDrawable Shape = new GradientDrawable();
        Shape.setCornerRadius(50.0f);
        Shape.setColor(ContextCompat.getColor(context, R.color.Toast));

        RelativeLayout Main = new RelativeLayout(context);
        Main.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        Main.setBackground(Shape);

        RelativeLayout.LayoutParams TextMessageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextMessageParam.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

        TextView TextMessage = new TextView(context);
        TextMessage.setLayoutParams(TextMessageParam);
        TextMessage.setTextColor(ContextCompat.getColor(context, R.color.Black));
        TextMessage.setText(Message);
        TextMessage.setPadding(MiscHandler.DpToPx(15), MiscHandler.DpToPx(10), MiscHandler.DpToPx(15), MiscHandler.DpToPx(10));
        TextMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        Main.addView(TextMessage);

        Toast toast = new Toast(context);
        toast.setGravity(Gravity.BOTTOM, 0, MiscHandler.DpToPx(62));
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(Main);
        toast.show();
    }

    public static int DpToPx(float DpValue)
    {
        DisplayMetrics Metrics = App.GetContext().getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DpValue, Metrics);
    }

    public static String GetTime(long T)
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

    public static void HideKeyBoard(Activity A)
    {
        View view = A.getCurrentFocus();

        if (view != null)
            ((InputMethodManager) A.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void Log(String Message)
    {
        Log.e("Bio", Message);
    }
}
