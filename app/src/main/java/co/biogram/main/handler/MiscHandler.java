package co.biogram.main.handler;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.style.URLSpan;
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

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import co.biogram.main.R;

public class MiscHandler
{
    private static final AtomicInteger NextGeneratedID = new AtomicInteger(1);

    private static String AvailbleHost = "";

    public static int GenerateViewID()
    {
        if (Build.VERSION.SDK_INT > 16)
            return View.generateViewId();

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
        Shape.setColor(ContextCompat.getColor(context, R.color.Toast));
        Shape.setCornerRadius(50.0f);

        RelativeLayout RelativeLayoutMain = new RelativeLayout(context);
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayoutMain.setBackground(Shape);

        RelativeLayout.LayoutParams TextViewMessageParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextViewMessageParam.addRule(RelativeLayout.CENTER_IN_PARENT);

        TextView TextViewMessage = new TextView(context);
        TextViewMessage.setLayoutParams(TextViewMessageParam);
        TextViewMessage.setTextColor(ContextCompat.getColor(context, R.color.Black));
        TextViewMessage.setText(Message);
        TextViewMessage.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 10), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 10));
        TextViewMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        RelativeLayoutMain.addView(TextViewMessage);

        Toast toast = new Toast(context);
        toast.setGravity(Gravity.BOTTOM, 0, MiscHandler.ToDimension(context, 65));
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(RelativeLayoutMain);
        toast.show();
    }

    public static int ToDimension(Context context, float Value)
    {
        DisplayMetrics Metrics = context.getResources().getDisplayMetrics();
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

    public static void StripUnderlines(Spannable Span)
    {
        for (URLSpan span : Span.getSpans(0, Span.length(), URLSpan.class))
        {
            int Start = Span.getSpanStart(span);
            int End = Span.getSpanEnd(span);

            Span.removeSpan(span);

            span = new URLSpanNoUnderline(span.getURL());
            Span.setSpan(span, Start, End, 0);
        }
    }

    @SuppressLint("all")
    private static class URLSpanNoUnderline extends URLSpan
    {
        URLSpanNoUnderline(String Span)
        {
            super(Span);
        }

        @Override
        public void updateDrawState(TextPaint textPaint)
        {
            super.updateDrawState(textPaint);
            textPaint.setUnderlineText(false);
        }
    }

    public static void HideSoftKey(Activity activity)
    {
        View view = activity.getCurrentFocus();

        if (view != null)
        {
            InputMethodManager IMM = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            IMM.hideSoftInputFromWindow(view.getWindowToken(), 0);

            view.clearFocus();
        }
    }

    public static String GetRandomServer(String URL)
    {
        if (!AvailbleHost.equals(""))
            return AvailbleHost;

        final String Host;

        switch (new Random().nextInt(2) + 1)
        {
            case 1:  Host = "http://5.160.219.220/" + URL; break;
            case 2:  Host = "http://5.160.219.220/" + URL; break;
            default: Host = "http://5.160.219.220/" + URL; break;
        }

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    Socket socket = new Socket();
                    socket.connect(new InetSocketAddress(Host, 80), 5000);
                    socket.close();

                    AvailbleHost = Host;
                }
                catch (Exception e)
                {
                    Debug("MiscHandler-PingRequest: " + e.toString());
                }
            }
        }).start();

        return Host;
    }

    public static void Debug(String Message)
    {
        Log.e("Debug", Message);
    }

    public static void CreateVideoThumbnail(String Url, Context context, ImageView View)
    {
        if (CacheHandler.BitmapIsCache(context, Url))
        {
            Bitmap bitmap = CacheHandler.BitmapFind(context, Url);
            View.setImageBitmap(bitmap);
            return;
        }

        new VideoThumbnailTask(View, Url).execute();
    }

    private static class VideoThumbnailTask extends AsyncTask<Void, Void, Bitmap>
    {
        final String Url;
        final ImageView ImageViewMain;

        VideoThumbnailTask(ImageView imageView, String url)
        {
            ImageViewMain = imageView;
            Url = url;
        }

        protected Bitmap doInBackground(Void... Voids)
        {
            Bitmap bitmap = null;

            try
            {
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(Url, new HashMap<String, String>());
                bitmap = retriever.getFrameAtTime();
                retriever.release();
            }
            catch (Exception e)
            {
                MiscHandler.Debug("MiscHandler-VideoThumbnail: " + e.toString());
            }

            return bitmap;
        }

        protected void onPostExecute(Bitmap Result)
        {
            if (Result == null || ImageViewMain == null)
                return;

            ImageViewMain.setImageBitmap(Result);
            CacheHandler.BitmapStore(ImageViewMain.getContext(), Result, Url);
        }
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
