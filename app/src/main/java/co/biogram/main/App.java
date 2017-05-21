package co.biogram.main;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;

import co.biogram.main.handler.CacheHandler;
import co.biogram.main.handler.MiscHandler;

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

        CacheHandler.ClearExpired(context);
    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
        MiscHandler.Log("onLowMemory Called");
    }

    @Deprecated
    public static Context GetContext()
    {
        return context;
    }
}
