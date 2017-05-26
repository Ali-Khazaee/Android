package co.biogram.main;

import android.app.Application;

import com.androidnetworking.AndroidNetworking;

import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;

import com.squareup.leakcanary.LeakCanary;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import co.biogram.main.handler.CacheHandler;

import okhttp3.OkHttpClient;

public class App extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();

        if (LeakCanary.isInAnalyzerProcess(this))
            return;

        LeakCanary.install(this);

        OkHttpClient OKClient = new OkHttpClient().newBuilder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();

        AndroidNetworking.initialize(getApplicationContext(), OKClient);

        Glide.get(getApplicationContext()).register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(OKClient));

        CacheHandler.SetUp();
    }
}
