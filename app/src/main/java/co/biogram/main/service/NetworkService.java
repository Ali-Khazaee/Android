package co.biogram.main.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import co.biogram.main.handler.AnalyzeHandler;

public class NetworkService extends Service
{
    private Socket socket;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        try
        {
            socket = IO.socket(GetBestServer());
            socket.connect();
        }
        catch (Exception e)
        {
            AnalyzeHandler.Log("NetworkService", e);
        }

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    public void Emit(String Event, String Message)
    {
        if (socket == null)
            return;

        socket.emit(Event, Message);
    }

    public void Emit(String Event, String Message, Ack ack)
    {
        if (socket == null)
            return;

        socket.emit(Event, Message, ack);
    }

    public void On(String Event, Emitter.Listener Listener)
    {
        if (socket == null)
            return;

        socket.on(Event, Listener);
    }

    public void Off(String Event)
    {
        if (socket == null)
            return;

        socket.off(Event);
    }

    public void Off(String Event, Emitter.Listener Listener)
    {
        if (socket == null)
            return;

        socket.off(Event, Listener);
    }

    private String GetBestServer()
    {
        return "http://198.50.232.192";
    }
}
