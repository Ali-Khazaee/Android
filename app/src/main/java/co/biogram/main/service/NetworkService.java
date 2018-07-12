package co.biogram.main.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import co.biogram.main.handler.Misc;
import co.biogram.main.handler.SocketHandler;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class NetworkService extends Service
{
    private static SocketHandler socket;
    public static void Emit(String Event, String Data, SocketHandler.Callback Listener)
    {
        socket.emit(Event, Data, Listener);
    }
    public static void Emit(String Event, byte[] Data, SocketHandler.Callback Listener)
    {
        try
        {
            socket.emit(Event, Data, Listener);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    public static void On(String Event, SocketHandler.Listener Listener)
    {
        socket.on(Event, Listener);
    }
    public static JSONObject createUploadData(String ID, String data)
    {
        return baseCreateUploadData(ID, data);
    }
    public static JSONObject createUploadData(String ID, byte[] data)
    {
        return baseCreateUploadData(ID, data);
    }
    public static JSONObject createUploadData(String ID, String data, String replyID)
    {
        JSONObject json = createUploadData(ID, data);

        try
        {
            json.put("ReplyID", replyID);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return json;
    }
    public static JSONObject createUploadData(String ID, byte[] data, String replyID)
    {
        JSONObject json = createUploadData(ID, data);

        try
        {
            json.put("ReplyID", replyID);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return json;
    }
    private static JSONObject baseCreateUploadData(Object ID, Object data)
    {
        JSONObject json = new JSONObject();
        try
        {
            json.put("To", ID);
            json.put("Message", data);

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return json;
    }
    @Override
    public int onStartCommand(Intent intent, int Flags, int StartID)
    {
        try
        {
            socket = new SocketHandler(GetBestServer(), 37000);
            socket.connect();
        }
        catch (Exception e)
        {
            Misc.Debug("NetworkService: " + e);
        }

        //        new Thread(new Runnable()
        //        {
        //            @Override
        //            public void run()
        //            {
        //                while (true)
        //                {
        //                    try
        //                    {
        //                        Thread.sleep(5000);
        //
        //                        Misc.Debug("SendMessage Sent.");
        //
        //                        Emit("SendMessage", "QQ", new SocketHandler.Callback()
        //                        {
        //                            @Override
        //                            public void call(Object Results)
        //                            {
        //                                Misc.Debug("SendMessage-Result: " + Results.toString());
        //                            }
        //                        });
        //                    }
        //                    catch (Exception e)
        //                    {
        //                        Misc.Debug("SendMessage-Error: " + e.getMessage());
        //                    }
        //                }
        //            }
        //        }).start();
        //
        //        On("error", new SocketHandler.Listener()
        //        {
        //            @Override
        //            public void call(Object... Results)
        //            {
        //                Misc.Debug("error-Result: " + Results[0].toString());
        //            }
        //        });
        //
        //        On("connected", new SocketHandler.Listener()
        //        {
        //            @Override
        //            public void call(Object... Results)
        //            {
        //                Misc.Debug("connected-Result: " + Results[0]);
        //            }
        //        });
        //
        //        On("close", new SocketHandler.Listener()
        //        {
        //            @Override
        //            public void call(Object... Results)
        //            {
        //                Misc.Debug("close-Result: " + Results[0]);
        //            }
        //        });
        //
        //        On("reconnecting", new SocketHandler.Listener()
        //        {
        //            @Override
        //            public void call(Object... Results)
        //            {
        //                Misc.Debug("reconnecting-Result: " + Results[0]);
        //            }
        //        });

        return START_STICKY;
    }
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
    private String GetBestServer()
    {
        return "157.119.190.112";
    }
}
