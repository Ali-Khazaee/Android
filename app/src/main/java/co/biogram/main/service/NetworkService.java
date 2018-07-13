package co.biogram.main.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import co.biogram.main.handler.Misc;
import co.biogram.main.handler.SocketHandler;
import org.json.JSONObject;

public class NetworkService extends Service
{
    private static SocketHandler socket;

    @Override
    public int onStartCommand(Intent intent, int Flags, int StartID)
    {
        socket = new SocketHandler(GetBestServer(), 37000);

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while (true)
                {
                    try
                    {
                        Thread.sleep(10000);

                        JSONObject Message = new JSONObject();
                        Message.put("To", "5b464ec7a2ef1ca5e3a65a0d");
                        Message.put("Message", "0");

                        Call("SendMessage", Message.toString(), new SocketHandler.Callback()
                        {
                            @Override
                            public void Call(Object Param)
                            {
                                Misc.Debug("SendMessage-Call: " + Param);
                            }
                        });
                    }
                    catch (Exception e)
                    {
                        Misc.Debug("GetMessage-Error: " + e.getMessage());
                    }
                }
            }
        }).start();

        Add("SendMessage2", new SocketHandler.Listener()
        {
            @Override
            public void Call(Object... Param)
            {
                Misc.Debug("SendMessage2-Result: " + Param[0].toString());
            }
        });

        Add(SocketHandler.EVENT_DISCONNECT, new SocketHandler.Listener()
        {
            @Override
            public void Call(Object... Param)
            {
                Misc.Debug("Disconnect Called");
            }
        });

        Add(SocketHandler.EVENT_ERROR, new SocketHandler.Listener()
        {
            @Override
            public void Call(Object... Param)
            {
                Misc.Debug("Error Called: " + Param[0]);
            }
        });

        Add(SocketHandler.EVENT_CONNECT, new SocketHandler.Listener()
        {
            @Override
            public void Call(Object... Param)
            {
                Misc.Debug("Connect Called");
            }
        });

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    public static void Add(String Event, SocketHandler.Listener Listener)
    {
        socket.Add(Event, Listener);
    }

    public static void Call(String Event, String Data)
    {
        socket.Call(Event, Data);
    }

    public static void Call(String Event, String Data, SocketHandler.Callback Listener)
    {
        socket.Call(Event, Data, Listener);
    }

    public static void Remove(String Event)
    {
        socket.Remove(Event);
    }

    private String GetBestServer()
    {
        return "157.119.190.112";
    }
}
