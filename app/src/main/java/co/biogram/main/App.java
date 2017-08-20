package co.biogram.main;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.androidnetworking.AndroidNetworking;

import com.bumptech.glide.Glide;

import java.util.concurrent.TimeUnit;

import co.biogram.main.handler.CacheHandler;
import co.biogram.main.service.NotificationService;

import okhttp3.OkHttpClient;

public class App extends Application
{
    private static OkHttpClient OKClient;

    @Override
    public void onCreate()
    {
        super.onCreate();

        Context context = getApplicationContext();

        context.startService(new Intent(context, NotificationService.class));

        OKClient = GetOKClient();

        AndroidNetworking.initialize(context, OKClient);

        CacheHandler.SetUp(context);
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
