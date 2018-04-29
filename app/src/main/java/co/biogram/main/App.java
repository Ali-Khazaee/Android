package co.biogram.main;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;

import com.androidnetworking.AndroidNetworking;

import com.squareup.leakcanary.LeakCanary;

import okhttp3.OkHttpClient;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import co.biogram.main.handler.Misc;

public class App extends Application
{
    private static volatile OkHttpClient OKClient;

    @Override
    public void onCreate()
    {
        super.onCreate();

        if (LeakCanary.isInAnalyzerProcess(this))
            return;

        LeakCanary.install(this);

        Misc.Initial(getApplicationContext());

        AndroidNetworking.initialize(getApplicationContext(), GetOKClient());
    }

    @Override
    protected void attachBaseContext(Context base)
    {


        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            Locale locale = new Locale(Misc.GetString("Language", "fa"));
            Locale.setDefault(locale);

            Configuration configuration = base.getResources().getConfiguration();
            configuration.setLocale(locale);
            configuration.setLayoutDirection(locale);

            base = base.createConfigurationContext(configuration);
        }
        else
        {
            Locale locale = new Locale(Misc.GetString("Language", "fa"));
            Locale.setDefault(locale);

            Configuration config = new Configuration();
            // noinspection deprecation
            config.locale = locale;

            // noinspection deprecation
            base.getResources().updateConfiguration(config, base.getResources().getDisplayMetrics());
        }*/

        super.attachBaseContext(base);
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
