package co.biogram.main;

import android.app.Application;

import com.androidnetworking.AndroidNetworking;

import com.bumptech.glide.Glide;

import com.squareup.leakcanary.LeakCanary;

import java.util.concurrent.TimeUnit;

import co.biogram.main.handler.CacheHandler;

import okhttp3.OkHttpClient;

public class App extends Application
{
    private static OkHttpClient OKClient;

    @Override
    public void onCreate()
    {
        super.onCreate();

        if (LeakCanary.isInAnalyzerProcess(this))
            return;

        LeakCanary.install(this);

        OKClient = GetOKClient();

        AndroidNetworking.initialize(getApplicationContext(), OKClient);

        CacheHandler.SetUp();
    }

    @Override
    public void onTrimMemory(int level)
    {
        Glide.with(this).onTrimMemory(level);

        super.onTrimMemory(level);
    }

    public static OkHttpClient GetOKClient()
    {
        if (OKClient == null)
        {
            OKClient = new OkHttpClient()
                    .newBuilder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .build();
        }

        return OKClient;
    }
}
