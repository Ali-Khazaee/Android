package co.biogram.main;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.support.annotation.Keep;

import com.androidnetworking.AndroidNetworking;

import com.bumptech.glide.Glide;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.leakcanary.LeakCanary;

import java.io.InputStream;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import co.biogram.main.handler.MiscHandler;
import co.biogram.main.handler.SharedHandler;
import co.biogram.main.service.SocketService;

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

        Context context = getApplicationContext();

        context.startService(new Intent(context, SocketService.class));

        OKClient = GetOKClient();

        AndroidNetworking.initialize(context, OKClient);

        //CacheHandler.SetUp(context);
        //CrashHandler.SetUp(context);
        //UpdateHandler.SetUp(context);
    }

    @Override
    protected void attachBaseContext(Context base)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            Locale locale = new Locale(SharedHandler.GetString(base, "Language", "fa"));
            Locale.setDefault(locale);

            Configuration configuration = base.getResources().getConfiguration();
            configuration.setLocale(locale);
            configuration.setLayoutDirection(locale);

            base = base.createConfigurationContext(configuration);
        }
        else
        {
            Locale locale = new Locale(SharedHandler.GetString(base, "Language", "fa"));
            Locale.setDefault(locale);

            Configuration config = new Configuration();
            // noinspection deprecation
            config.locale = locale;

            // noinspection deprecation
            base.getResources().updateConfiguration(config, base.getResources().getDisplayMetrics());
        }

        super.attachBaseContext(base);
    }

    @GlideModule
    private class CustomGlideModule extends AppGlideModule
    {
        @Override
        public void applyOptions(Context context, GlideBuilder builder)
        {
            MiscHandler.Debug("Load Shod Glide3");
            builder.setDefaultRequestOptions(new RequestOptions().format(DecodeFormat.PREFER_ARGB_8888).disallowHardwareConfig());
        }

        @Override
        public void registerComponents(Context context, Glide glide, Registry registry)
        {
            MiscHandler.Debug("Load Shod Glide1");
            glide.getRegistry().replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(GetOKClient()));
        }

        @Override
        public boolean isManifestParsingEnabled()
        {
            MiscHandler.Debug("Load Shod Glide2");
            return false;
        }
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

/*
    Shared Data:
    TOKEN     - For Authentication
    IsLogin   - For Logged
    ID        - For Account ID
    Username  - For Username
    Avatar    - For Profile Image
*/
