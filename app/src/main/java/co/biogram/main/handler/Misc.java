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
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
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
import java.lang.reflect.Field;
import java.util.Locale;

import co.biogram.main.R;
import co.biogram.main.BuildConfig;
import co.biogram.main.MainActivity;

public class Misc
{
    @SuppressLint("StaticFieldLeak")
    private static volatile Context context;

    public static void SetUp(Context c)
    {
        context = c;

        File TempFolder = Temp();

        if (TempFolder.exists() && TempFolder.isDirectory())
            for (File file : TempFolder.listFiles()) // noinspection all
                file.delete();
    }

    public static File Temp()
    {
        File TempFolder = new File(context.getCacheDir(), "Temp");

        if (!TempFolder.exists()) // noinspection all
            TempFolder.mkdir();

        return TempFolder;
    }

    public static int Attr(Context c, int ID)
    {
        TypedValue Value = new TypedValue();
        c.getTheme().resolveAttribute(ID, Value, true);

        return Value.resourceId != 0 ? Value.resourceId : Value.data;
    }

    public static int GetTheme()
    {
        return R.style.ThemeLight;
    }

    public static int Color(int ID)
    {
        return ContextCompat.getColor(context, ID);
    }



























    public static final String TAG = "channel";

    public static final int DIR_DOWNLOAD = 0;
    public static final int DIR_DOCUMENT = 1;
    public static final int DIR_PICTURE = 2;
    public static final int DIR_VIDEO = 3;
    public static final int DIR_AUDIO = 4;
    public static final int DIR_FILE = 5;


    private static volatile Typeface TypeFontCache;
    private static boolean IsRTL = false;
    private static boolean IsRTLInit = true;
    private static boolean IsFa = false;
    private static boolean IsFaInit = true;






    public static File Dir(int Type)
    {
        String Folder = "";

        switch (Type)
        {
            case DIR_DOWNLOAD:
                Folder = "Download";
                break;
            case DIR_DOCUMENT:
                Folder = "Document";
                break;
            case DIR_PICTURE:
                Folder = "Picture";
                break;
            case DIR_VIDEO:
                Folder = "Video";
                break;
            case DIR_AUDIO:
                Folder = "Audio";
                break;
            case DIR_FILE:
                Folder = "File";
                break;
        }

        File AppDir = new File(Environment.getExternalStorageDirectory(), TAG);

        if (!AppDir.exists())
            AppDir.mkdir();

        File DirFolder = new File(AppDir, Folder);

        if (!DirFolder.exists())
            DirFolder.mkdir();

        return DirFolder;
    }

    public static Typeface GetTypeface()
    {
        if (TypeFontCache == null)
            TypeFontCache = Typeface.createFromAsset(context.getAssets(), "iran-sans.ttf");

        return TypeFontCache;
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

    public static boolean CheckPermission(String p)
    {
        return ContextCompat.checkSelfPermission(context, p) == PackageManager.PERMISSION_GRANTED;
    }

    public static void SetCursorColor(View view, int Color)
    {
        try
        {
            Field field = TextView.class.getDeclaredField("mCursorDrawableRes");
            field.setAccessible(true);

            Drawable drawable = ContextCompat.getDrawable(context, field.getInt(view));

            if (drawable != null)
                drawable.setColorFilter(Misc.Color(Color), PorterDuff.Mode.SRC_IN);

            field = TextView.class.getDeclaredField("mEditor");
            field.setAccessible(true);

            Object Editor = field.get(view);

            field = Editor.getClass().getDeclaredField("mCursorDrawable");
            field.setAccessible(true);
            field.set(Editor, new Drawable[] { drawable, drawable });
        }
        catch (Exception e)
        {
            //
        }
    }

    public static void SetString(String Key, String Value)
    {
        SharedPreferences.Editor Editor = context.getSharedPreferences(TAG, Context.MODE_PRIVATE).edit();
        Editor.putString(Key, Value);
        Editor.apply();
    }

    public static String GetString(String Key)
    {
        return context.getSharedPreferences(TAG, Context.MODE_PRIVATE).getString(Key, "");
    }

    public static String GetString(String Key, String Value)
    {
        return context.getSharedPreferences(TAG, Context.MODE_PRIVATE).getString(Key, Value);
    }

    public static void SetBoolean(String Key, boolean Value)
    {
        SharedPreferences.Editor Editor = context.getSharedPreferences(TAG, Context.MODE_PRIVATE).edit();
        Editor.putBoolean(Key, Value);
        Editor.apply();
    }

    public static boolean GetBoolean(String Key)
    {
        return context.getSharedPreferences(TAG, Context.MODE_PRIVATE).getBoolean(Key, false);
    }

    public static int ToDP(float Value)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, Value, context.getResources().getDisplayMetrics());
    }

    public static void ChangeTheme()
    {
        SharedPreferences.Editor Editor = context.getSharedPreferences(TAG, Context.MODE_PRIVATE).edit();
        Editor.putBoolean("ThemeDark", !Misc.GetBoolean("ThemeDark"));
        // noinspection all
        Editor.commit();
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

    public static void ToastOld(int Message)
    {
        GradientDrawable drawableToast = new GradientDrawable();
        drawableToast.setStroke(ToDP(1), Color(R.color.White));
        drawableToast.setColor(Color(R.color.White));
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
        TextViewMessage.setTextColor(Color(R.color.White));

        RelativeLayoutMain.addView(TextViewMessage);

        Toast toast = new Toast(context);
        toast.setGravity(Gravity.BOTTOM, 0, ToDP(65));
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(RelativeLayoutMain);
        toast.show();
    }

    public static int generateViewId()
    {

        return View.generateViewId();

    }

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

    public static void ToastOld(String Message)
    {
        GradientDrawable DrawableToast = new GradientDrawable();
        DrawableToast.setColor(ContextCompat.getColor(context, R.color.White));
        DrawableToast.setCornerRadius(10.0f);
        DrawableToast.setStroke(ToDP(1), ContextCompat.getColor(context, R.color.White));

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
            alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + 100, PendingIntent.getActivity(context, 123456, new Intent(context, MainActivity.class), PendingIntent.FLAG_CANCEL_CURRENT));

        System.exit(0);
    }

    public static void GeneralError(int Error)
    {
        switch (Error)
        {
            case -1:
                ToastOld(String(R.string.GeneralError1));
                break;
            case -2:
                ToastOld(String(R.string.GeneralError2));
                break;
            case -3:
                ToastOld(String(R.string.GeneralError3));
                break;
            case -4:
                ToastOld(String(R.string.GeneralError4));
                break;
            case -6:
                ToastOld(String(R.string.GeneralError6));
                break;
            case -7:
                ToastOld(String(R.string.GeneralError7));
                break;
        }
    }

    public static boolean IsDark()
    {
        return Misc.GetBoolean("IsDark");
    }

    public static void IsFullScreen(Activity activity, boolean Show)
    {
        if (Show && Misc.GetBoolean("IsFullScreen"))
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        else if (!Show && !Misc.GetBoolean("IsFullScreen"))
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
        else if (Diff >= 2520 && Diff <= 43199)
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

    public static Bitmap resize(Bitmap image, int maxWidth, int maxHeight)
    {
        if (maxHeight > 0 && maxWidth > 0)
        {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > ratioBitmap)
            {
                finalWidth = (int) ((float) maxHeight * ratioBitmap);
            }
            else
            {
                finalHeight = (int) ((float) maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        }
        else
        {
            return image;
        }
    }

    public static void closeKeyboard(Activity activity)
    {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static String createFile(int type, @NonNull String subFolder, String format)
    {
        String filepath = Misc.Dir(type).getPath();
        File file = new File(filepath, subFolder);
        if (!file.exists())
        {
            file.mkdirs();
        }
        return (file.getPath() + "/" + System.currentTimeMillis() + format);
    }

    public static String createFile(int type)
    {
        String filepath = Misc.Dir(type).getPath();
        File file = new File(filepath);
        if (!file.exists())
        {
            file.mkdirs();
        }
        return (file.getPath() + "/");
    }

    public static Bitmap scale(Bitmap bitmap)
    {
        float scale = 1f;

        if (bitmap.getWidth() < 200 || bitmap.getHeight() < 250)
            scale = 0.3f;
        if (bitmap.getWidth() < 400 || bitmap.getHeight() < 500)
            scale = 0.7f;
        else if (bitmap.getWidth() > 1200 || bitmap.getHeight() > 1800)
            scale = 1.5f;
        else if (bitmap.getWidth() > 1800 || bitmap.getHeight() > 2400)
            scale = 1.9f;
        else if (bitmap.getWidth() > 2400 || bitmap.getHeight() > 3000)
            scale = 2.5f;
        else if (bitmap.getWidth() > 3000)
            scale = 3.1f;
        else if (bitmap.getWidth() > 4000)
            scale = 4f;

        return Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() / scale), (int) (bitmap.getHeight() / scale), false);

    }

    //    public static void CreateThumbNail(String fileName, int width, int height) {
    //        try
    //        {
    //            final int THUMBNAIL_SIZE = 64;
    //
    //            FileInputStream fis = new FileInputStream(fileName);
    //            Bitmap imageBitmap = BitmapFactory.decodeStream(fis);
    //
    //            imageBitmap = Bitmap.createScaledBitmap(imageBitmap, THUMBNAIL_SIZE, THUMBNAIL_SIZE, false);
    //
    //            ByteArrayOutputStream baos = new ByteArrayOutputStream();
    //            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
    //
    //        }
    //        catch(Exception ex) {
    //
    //        }
    //    }

    public static Bitmap Blurry(Bitmap sentBitmap)
    {
        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        int radius = 25;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] Pixel = new int[ width * height ];

        bitmap.getPixels(Pixel, 0, width, 0, 0, width, height);

        int widthMaximum = width - 1;
        int heightMaximum = height - 1;
        int wh = width * height;
        int div = radius + radius + 1;

        int r[] = new int[ wh ];
        int g[] = new int[ wh ];
        int b[] = new int[ wh ];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[ Math.max(width, height) ];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[ 256 * divsum ];

        for (i = 0; i < 256 * divsum; i++)
        {
            dv[ i ] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[ div ][ 3 ];
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
                p = Pixel[ yi + Math.min(widthMaximum, Math.max(i, 0)) ];
                sir = stack[ i + radius ];
                sir[ 0 ] = (p & 0xff0000) >> 16;
                sir[ 1 ] = (p & 0x00ff00) >> 8;
                sir[ 2 ] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[ 0 ] * rbs;
                gsum += sir[ 1 ] * rbs;
                bsum += sir[ 2 ] * rbs;

                if (i > 0)
                {
                    rinsum += sir[ 0 ];
                    ginsum += sir[ 1 ];
                    binsum += sir[ 2 ];
                }
                else
                {
                    routsum += sir[ 0 ];
                    goutsum += sir[ 1 ];
                    boutsum += sir[ 2 ];
                }
            }

            stackpointer = radius;

            for (x = 0; x < width; x++)
            {

                r[ yi ] = dv[ rsum ];
                g[ yi ] = dv[ gsum ];
                b[ yi ] = dv[ bsum ];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[ stackstart % div ];

                routsum -= sir[ 0 ];
                goutsum -= sir[ 1 ];
                boutsum -= sir[ 2 ];

                if (y == 0)
                {
                    vmin[ x ] = Math.min(x + radius + 1, widthMaximum);
                }

                p = Pixel[ yw + vmin[ x ] ];

                sir[ 0 ] = (p & 0xff0000) >> 16;
                sir[ 1 ] = (p & 0x00ff00) >> 8;
                sir[ 2 ] = (p & 0x0000ff);

                rinsum += sir[ 0 ];
                ginsum += sir[ 1 ];
                binsum += sir[ 2 ];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[ (stackpointer) % div ];

                routsum += sir[ 0 ];
                goutsum += sir[ 1 ];
                boutsum += sir[ 2 ];

                rinsum -= sir[ 0 ];
                ginsum -= sir[ 1 ];
                binsum -= sir[ 2 ];

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

                sir = stack[ i + radius ];

                sir[ 0 ] = r[ yi ];
                sir[ 1 ] = g[ yi ];
                sir[ 2 ] = b[ yi ];

                rbs = r1 - Math.abs(i);

                rsum += r[ yi ] * rbs;
                gsum += g[ yi ] * rbs;
                bsum += b[ yi ] * rbs;

                if (i > 0)
                {
                    rinsum += sir[ 0 ];
                    ginsum += sir[ 1 ];
                    binsum += sir[ 2 ];
                }
                else
                {
                    routsum += sir[ 0 ];
                    goutsum += sir[ 1 ];
                    boutsum += sir[ 2 ];
                }

                if (i < heightMaximum)
                {
                    yp += width;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < height; y++)
            {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                Pixel[ yi ] = (0xff000000 & Pixel[ yi ]) | (dv[ rsum ] << 16) | (dv[ gsum ] << 8) | dv[ bsum ];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[ stackstart % div ];

                routsum -= sir[ 0 ];
                goutsum -= sir[ 1 ];
                boutsum -= sir[ 2 ];

                if (x == 0)
                {
                    vmin[ y ] = Math.min(y + r1, heightMaximum) * width;
                }
                p = x + vmin[ y ];

                sir[ 0 ] = r[ p ];
                sir[ 1 ] = g[ p ];
                sir[ 2 ] = b[ p ];

                rinsum += sir[ 0 ];
                ginsum += sir[ 1 ];
                binsum += sir[ 2 ];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[ stackpointer ];

                routsum += sir[ 0 ];
                goutsum += sir[ 1 ];
                boutsum += sir[ 2 ];

                rinsum -= sir[ 0 ];
                ginsum -= sir[ 1 ];
                binsum -= sir[ 2 ];

                yi += width;
            }
        }

        bitmap.setPixels(Pixel, 0, width, 0, 0, width, height);

        return bitmap;
    }
}
