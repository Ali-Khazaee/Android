package co.biogram.main;

import android.app.Application;
import android.content.Intent;

import com.squareup.leakcanary.LeakCanary;

import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

import co.biogram.main.handler.Misc;
import co.biogram.main.service.NetworkService;

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

        Misc.Initial(getApplicationContext());

        startService(new Intent(getApplicationContext(), NetworkService.class));
    }

    public static OkHttpClient OKClient()
    {
        if (OKClient == null)
            OKClient = new OkHttpClient().newBuilder().connectTimeout(60, TimeUnit.SECONDS).writeTimeout(60, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS).build();

        return OKClient;
    }
}
