package co.biogram.main.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import co.biogram.main.handler.Socket;
import co.biogram.main.handler.Analyze;

public class NetworkService extends Service
{
    private static Socket socket;

    @Override
    public int onStartCommand(Intent intent, int Flags, int StartID)
    {
        try
        {
            socket = new Socket("172.17.0.43", 3000);
            socket.Connect();
        }
        catch (Exception e)
        {
            Analyze.Log("NetworkService", e);
        }

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    public static void Emit(String Event, String Data, Socket.Listener Listener)
    {
        Analyze.Debug("NetworkService", "Emit: " + Event);
        socket.emit(Event, Data, Listener);
    }

    public void On(String Event, Socket.Listener Listener)
    {
        Analyze.Debug("NetworkService", "On: " + Event);
        socket.on(Event, Listener);
    }

    private String GetBestServer()
    {
        return "198.50.232.192";
    }
}
