package co.biogram.main;

import android.app.Application;
import android.content.Context;

import com.androidnetworking.AndroidNetworking;

import com.squareup.leakcanary.LeakCanary;

import java.util.concurrent.TimeUnit;

import co.biogram.main.handler.CacheHandler;
import co.biogram.main.handler.DataBaseHandler;

import okhttp3.OkHttpClient;

public class App extends Application
{
    @SuppressWarnings("all")
    private static Context context;

    @Override
    public void onCreate()
    {
        super.onCreate();

        if (LeakCanary.isInAnalyzerProcess(this))
            return;

        LeakCanary.install(this);

        context = getApplicationContext();

        AndroidNetworking.initialize(context, new OkHttpClient().newBuilder().connectTimeout(45, TimeUnit.SECONDS).readTimeout(45, TimeUnit.SECONDS).writeTimeout(45, TimeUnit.SECONDS).build());

        DataBaseHandler.SetUp();
        CacheHandler.ClearExpired();
    }

    @Deprecated
    public static Context GetContext()
    {
        return context;
    }
}
