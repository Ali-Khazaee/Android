package co.biogram.main;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.androidnetworking.AndroidNetworking;

import com.squareup.leakcanary.LeakCanary;

import java.util.concurrent.TimeUnit;

import co.biogram.main.handler.CacheHandler;
import co.biogram.main.handler.DataBaseHandler;
import co.biogram.main.handler.SharedHandler;
import co.biogram.main.handler.URLHandler;

import okhttp3.OkHttpClient;

public class App extends Application
{
    @SuppressWarnings("all")
    private static Context _Context;

    private final Runnable KeepOnlineRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            AndroidNetworking.post(URLHandler.GetURL(URLHandler.URL.MISC_LAST_ONLINE)).addHeaders("TOKEN", SharedHandler.GetString("Token")).build().getAsString(null);
            new Handler().postDelayed(KeepOnlineRunnable, 300000);
        }
    };

    @Override
    public void onCreate()
    {
        super.onCreate();

        if (LeakCanary.isInAnalyzerProcess(this))
            return;

        LeakCanary.install(this);

        _Context = getApplicationContext();

        AndroidNetworking.initialize(_Context, new OkHttpClient().newBuilder().connectTimeout(45, TimeUnit.SECONDS).readTimeout(45, TimeUnit.SECONDS).writeTimeout(45, TimeUnit.SECONDS).build());

        if (SharedHandler.GetBoolean("IsLogin"))
            new Handler().postDelayed(KeepOnlineRunnable, 5000);

        DataBaseHandler.SetUp();
        CacheHandler.ClearExpired();
    }

    public static Context GetContext()
    {
        return _Context;
    }
}
