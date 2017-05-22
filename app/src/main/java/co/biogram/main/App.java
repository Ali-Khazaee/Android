package co.biogram.main;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

import co.biogram.main.handler.CacheHandler;

public class App extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();

        if (LeakCanary.isInAnalyzerProcess(this))
            return;

        LeakCanary.install(this);

        CacheHandler.SetUp();
    }
}
