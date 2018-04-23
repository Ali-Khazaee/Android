package co.biogram.main.handler;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.Locale;

import co.biogram.main.R;
import co.biogram.main.BuildConfig;
import co.biogram.main.activity.WelcomeActivity;

public class Misc
{
    @SuppressLint("StaticFieldLeak")
    private static volatile Context context;
    private static volatile Typeface TypeFontCache;

    public final static int DOWNLOAD = 0;
    public final static int DOCUMENT = 1;
    public final static int PICTURE = 2;
    public final static int VIDEO = 3;
    public final static int AUDIO = 4;
    public final static int FILE = 5;

    private Misc()
    {

    }

    public static void SetUp(Context c)
    {
        context = c;

        File TempFolder = Temp();

        if (TempFolder.exists() && TempFolder.isDirectory())
        {
            File[] TempFiles = TempFolder.listFiles();

            for (File file : TempFiles)
                file.delete();
        }
    }

    public static File Dir(int Type)
    {
        String Folder = "";

        switch (Type)
        {
            case DOWNLOAD: Folder = "Download"; break;
            case DOCUMENT: Folder = "Document"; break;
            case PICTURE: Folder = "Picture"; break;
            case VIDEO: Folder = "Video"; break;
            case AUDIO: Folder = "Audio"; break;
            case FILE: Folder = "File"; break;
        }

        File BioDir = new File(Environment.getExternalStorageDirectory(), "Bio");

        if (!BioDir.exists())
            BioDir.mkdir();

        File FolderDir = new File(BioDir, Folder);

        if (!FolderDir.exists())
            FolderDir.mkdir();

        return FolderDir;
    }

    public static File Temp()
    {
        File TempFolder = new File(context.getCacheDir(), "Temp");

        if (!TempFolder.exists())
            TempFolder.mkdir();

        return TempFolder;
    }

    public static Typeface GetTypeface()
    {
        if (TypeFontCache == null)
            TypeFontCache = Typeface.createFromAsset(context.getAssets(), "iran-sans.ttf");

        return TypeFontCache;
    }

    public static void Toast(int Message)
    {
        GradientDrawable drawableToast = new GradientDrawable();
        drawableToast.setStroke(ToDP(1), Color(R.color.ToastLine));
        drawableToast.setColor(Color(R.color.Toast));
        drawableToast.setCornerRadius(10.0f);

        RelativeLayout RelativeLayoutMain = new RelativeLayout(context);
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayoutMain.setBackground(drawableToast);

        RelativeLayout.LayoutParams TextViewMessageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewMessageParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        TextView TextViewMessage = new TextView(context);
        TextViewMessage.setLayoutParams(TextViewMessageParam);
        TextViewMessage.setPadding(ToDP(15), ToDP(15), ToDP(15), ToDP(15));
        TextViewMessage.setTypeface(GetTypeface());
        TextViewMessage.setText(String(Message));
        TextViewMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        TextViewMessage.setTextColor(Color(R.color.TextDark));

        RelativeLayoutMain.addView(TextViewMessage);

        Toast toast = new Toast(context);
        toast.setGravity(Gravity.BOTTOM, 0, ToDP(65));
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(RelativeLayoutMain);
        toast.show();
    }

    public static int Color(int C)
    {
        return ContextCompat.getColor(context, C);
    }

    public static String String(int S)
    {
        return context.getString(S);
    }

    public static int SampleSize(int W, int H, int MW, int MH)
    {
        int S = 1;

        if (H > MH || W > MW)
        {
            while ((H / S) >= MH || (W / S) >= MW)
            {
                S *= 2;
            }
        }

        return S;
    }
    public static void ShowSoftKey(View v)
    {
        InputMethodManager IMM = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (IMM == null)
            return;

        IMM.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
    }

    public static void HideSoftKey(Activity a)
    {
        View v = a.getCurrentFocus();

        if (v == null)
            return;

        InputMethodManager IMM = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (IMM == null)
            return;

        IMM.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public static boolean checkPermission(String p)
    {
        return ContextCompat.checkSelfPermission(context, p) == PackageManager.PERMISSION_GRANTED;
    }












































    public static int generateViewId()
    {

            return View.generateViewId();


    }

    private static boolean IsRTL = false;
    private static boolean IsRTLInit = true;

    public static boolean IsRTL()
    {
        if (IsRTLInit)
        {
            Locale locale = Locale.getDefault();
            String Language = locale.getLanguage();

            if (Language == null)
                Language = "en";

            IsRTL = Language.toLowerCase().equals("fa");
            IsRTLInit = false;
        }

        return IsRTL;
    }

    private static boolean IsFa = false;
    private static boolean IsFaInit = true;

    public static boolean IsFa()
    {
        if (IsFaInit)
        {
            Locale locale = Locale.getDefault();
            String Language = locale.getLanguage();

            if (Language == null)
                Language = "en";

            IsFaInit = false;
            IsFa = Language.toLowerCase().equals("fa");
        }

        return IsFa;
    }

    public static int Align(String Direction)
    {
        if (Direction.equals("R"))
            return IsRTL() ? RelativeLayout.ALIGN_PARENT_RIGHT : RelativeLayout.ALIGN_PARENT_LEFT;
        else
            return IsRTL() ? RelativeLayout.ALIGN_PARENT_LEFT : RelativeLayout.ALIGN_PARENT_RIGHT;
    }

    public static int AlignTo(String Direction)
    {
        if (Direction.equals("R"))
            return IsRTL() ? RelativeLayout.LEFT_OF : RelativeLayout.RIGHT_OF;
        else
            return IsRTL() ? RelativeLayout.RIGHT_OF : RelativeLayout.LEFT_OF;
    }

    public static int Gravity(String Direction)
    {
        if (Direction.equals("R"))
            return IsRTL() ? Gravity.START : Gravity.END;
        else
            return IsRTL() ? Gravity.END : Gravity.START;
    }

    public static int ToDP(float Value)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, Value, context.getResources().getDisplayMetrics());
    }

    public static void Toast(String Message)
    {
        GradientDrawable DrawableToast = new GradientDrawable();
        DrawableToast.setColor(ContextCompat.getColor(context, R.color.Toast));
        DrawableToast.setCornerRadius(10.0f);
        DrawableToast.setStroke(ToDP(1), ContextCompat.getColor(context, R.color.ToastLine));

        RelativeLayout RelativeLayoutMain = new RelativeLayout(context);
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayoutMain.setBackground(DrawableToast);

        RelativeLayout.LayoutParams TextViewMessageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewMessageParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        TextView TextViewMessage = new TextView(context);
        TextViewMessage.setLayoutParams(TextViewMessageParam);
        TextViewMessage.setPadding(ToDP(15), ToDP(15), ToDP(15), ToDP(15));
        TextViewMessage.setText(Message);

        RelativeLayoutMain.addView(TextViewMessage);

        Toast ToastMain = new Toast(context);
        ToastMain.setGravity(Gravity.BOTTOM, 0, ToDP(65));
        ToastMain.setDuration(Toast.LENGTH_SHORT);
        ToastMain.setView(RelativeLayoutMain);
        ToastMain.show();
    }



    public static void UIThread(Runnable r, long d)
    {
        new Handler(context.getApplicationContext().getMainLooper()).postDelayed(r, d);
    }

    public static int SampleSize(BitmapFactory.Options o, int RW, int RH)
    {
        int H = o.outHeight;
        int W = o.outWidth;

        int S = 1;

        if (H > RH || W > RW)
        {

            int HH = H / 2;
            int HW = W / 2;

            while ((HH / S) >= RH && (HW / S) >= RW)
            {
                S *= 2;
            }
        }

        return S;
    }



    public static void ChangeLanguage(String Language)
    {
        SharedPreferences.Editor Editor = context.getSharedPreferences("BioGram", Context.MODE_PRIVATE).edit();
        Editor.putString("Language", Language);
        // noinspection all
        Editor.commit();

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (alarmManager != null)
            alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + 100, PendingIntent.getActivity(context, 123456, new Intent(context, WelcomeActivity.class), PendingIntent.FLAG_CANCEL_CURRENT));

        System.exit(0);
    }

    public static void GeneralError(int Error)
    {
        switch (Error)
        {
            case -1: Toast(String(R.string.GeneralError1)); break;
            case -2: Toast(String(R.string.GeneralError2)); break;
            case -3: Toast(String(R.string.GeneralError3)); break;
            case -4: Toast(String(R.string.GeneralError4)); break;
            case -6: Toast(String(R.string.GeneralError6)); break;
            case -7: Toast(String(R.string.GeneralError7)); break;
        }
    }

    public static void ChangeTheme()
    {
        SharedPreferences.Editor Editor = context.getSharedPreferences("BioGram", Context.MODE_PRIVATE).edit();
        Editor.putBoolean("IsDark", !SharedHandler.GetBoolean("IsDark"));
        // noinspection all
        Editor.commit();

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (alarmManager != null)
            alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + 100, PendingIntent.getActivity(context, 123456, new Intent(context, WelcomeActivity.class), PendingIntent.FLAG_CANCEL_CURRENT));

        System.exit(0);
    }



    public static boolean IsDark()
    {
        return SharedHandler.GetBoolean("IsDark");
    }

    public static void IsFullScreen(Activity activity, boolean Show)
    {
        if (Show && SharedHandler.GetBoolean("IsFullScreen"))
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        else if (!Show && !SharedHandler.GetBoolean("IsFullScreen"))
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public static String GetRandomServer(String URL)
    {
        return "http://5.160.219.218:5000/" + URL;
    }

    public static String GenerateSession()
    {
        return "BioGram Android " + BuildConfig.VERSION_NAME + " - " + Build.MODEL + " - " + Build.MANUFACTURER + " - API " + Build.VERSION.SDK_INT;
    }

    public static String TimeAgo(long Time)
    {
        Time = Time * 1000;
        long Now = System.currentTimeMillis();

        if (Time > Now || Time <= 0)
            return "";

        int Diff = Math.round((Math.abs(Now - Time) / 1000) / 60);

        if (Diff == 0)
            return String(R.string.TimeAgoNow);
        else if (Diff == 1)
            return String(R.string.TimeAgoMin);
        else if (Diff >= 2 && Diff <= 59)
            return Diff + " " + String(R.string.TimeAgoMins);
        else if (Diff >= 60 && Diff <= 119)
            return String(R.string.TimeAgoHour);
        else if (Diff >= 120 && Diff <= 1439)
            return (Math.round(Diff / 60)) + " " + String(R.string.TimeAgoHours);
        else if (Diff >= 1440 && Diff <= 2519)
            return String(R.string.TimeAgoDay);
        else if (Diff >= 2520  && Diff <= 43199)
            return (Math.round(Diff / 1440)) + " " + String(R.string.TimeAgoDays);
        else if (Diff >= 43200 && Diff <= 86399)
            return String(R.string.TimeAgoMonth);
        else if (Diff >= 86400 && Diff <= 525599)
            return (Math.round(Diff / 43200)) + " " + String(R.string.TimeAgoMonths);
        else if (Diff >= 525600 && Diff <= 655199)
            return String(R.string.TimeAgoYear);

        return Math.round(Diff / 525600) + " " + String(R.string.TimeAgoYears);
    }

    public static String TimeLeft(long Time)
    {
        Time = Time * 1000;
        long Now = System.currentTimeMillis();

        if (Time < Now)
            return String(R.string.TimeLeftFinish);

        int Diff = Math.round((Math.abs(Now - Time) / 1000) / 60);

        if (Diff == 0)
            return String(R.string.TimeLeftNow);
        else if (Diff == 1)
            return String(R.string.TimeLeftMin);
        else if (Diff >= 2 && Diff <= 59)
            return Diff + " " + String(R.string.TimeLeftMins);
        else if (Diff >= 60 && Diff <= 90)
            return String(R.string.TimeLeftHour);
        else if (Diff >= 90 && Diff <= 1439)
            return (Math.round(Diff / 60)) + " " + String(R.string.TimeLeftHours);
        else if (Diff >= 1440 && Diff <= 2519)
            return String(R.string.TimeLeftDay);

        return (Math.round(Diff / 1440)) + " " + String(R.string.TimeLeftDays);
    }

    public static void Debug(String Message)
    {
        Log.e("Debug", Message);
    }

    public static Bitmap Blurry(Bitmap sentBitmap)
    {
        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        int radius = 25;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] Pixel = new int[width * height];

        bitmap.getPixels(Pixel, 0, width, 0, 0, width, height);

        int widthMaximum = width - 1;
        int heightMaximum = height - 1;
        int wh = width * height;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(width, height)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];

        for (i = 0; i < 256 * divsum; i++)
        {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < height; y++)
        {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;

            for (i = -radius; i <= radius; i++)
            {
                p = Pixel[yi + Math.min(widthMaximum, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;

                if (i > 0)
                {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                }
                else
                {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }

            stackpointer = radius;

            for (x = 0; x < width; x++)
            {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0)
                {
                    vmin[x] = Math.min(x + radius + 1, widthMaximum);
                }

                p = Pixel[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }

            yw += width;
        }

        for (x = 0; x < width; x++)
        {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * width;

            for (i = -radius; i <= radius; i++)
            {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < heightMaximum) {
                    yp += width;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < height; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                Pixel[yi] = ( 0xff000000 & Pixel[yi] ) | ( dv[rsum] << 16 ) | ( dv[gsum] << 8 ) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, heightMaximum) * width;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += width;
            }
        }

        bitmap.setPixels(Pixel, 0, width, 0, 0, width, height);

        return bitmap;
    }
}
