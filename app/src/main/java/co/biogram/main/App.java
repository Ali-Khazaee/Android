package co.biogram.main;

import android.app.Application;
import android.content.Intent;
import android.content.res.Configuration;

import java.util.Locale;

import com.squareup.leakcanary.LeakCanary;

import co.biogram.main.handler.Misc;
import co.biogram.main.service.NetworkService;

public class App extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();

        if (LeakCanary.isInAnalyzerProcess(this))
            return;

        LeakCanary.install(this);

        Locale locale = new Locale("fa");
        Locale.setDefault(locale);

        Configuration Config = new Configuration();
        Config.locale = locale;

        getBaseContext().getResources().updateConfiguration(Config, getBaseContext().getResources().getDisplayMetrics());

        setTheme(Misc.GetTheme());
        Misc.SetUp(getApplicationContext());

        startService(new Intent(this, NetworkService.class));

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while (!Thread.interrupted())
                {
                    try
                    {
                        Thread.sleep(1000);

                        NetworkService.Send(1, "Salam");
                    }
                    catch (Exception e)
                    {
                        Misc.Debug("Socket-Send: " + e);
                    }
                }
            }
        }).start();

        NetworkService.On(NetworkService.PACKET_USERNAME, new NetworkService.Listener()
        {
            @Override
            public void Call(String Message)
            {
                Misc.Debug("On1: " + Message);
            }
        });

        NetworkService.On(NetworkService.PACKET_USERNAME, new NetworkService.Listener()
        {
            @Override
            public void Call(String Message)
            {
                Misc.Debug("On2: " + Message);

                NetworkService.Remove(1, this);
            }
        });

        NetworkService.Once(NetworkService.PACKET_USERNAME, new NetworkService.Listener()
        {
            @Override
            public void Call(String Message)
            {
                Misc.Debug("Once: " + Message);
            }
        });
    }
}
