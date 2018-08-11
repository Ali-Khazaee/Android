package co.biogram.main.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import co.biogram.main.handler.Misc;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class NetworkService extends Service
{
    public static final int PACKET_USERNAME = 1;
    public static final int PACKET_SIGN_IN = 2;
    public static final int PACKET_SIGN_IN_VERIFY = 3;
    public static final int PACKET_SIGN_UP = 4;
    public static final int PACKET_SIGN_UP_VERIFY = 5;
    private static boolean IsConnected;
    private static BufferedInputStream BIS;
    private static BufferedOutputStream BOS;
    @SuppressWarnings("all")
    private static Map<Integer, LinkedList<Listener>> ListenerMain = new HashMap<>();

    //
    // Send & Receive
    //
    private final Handler HandlerMain = new Handler();
    private final Runnable RunnableMain = new Runnable()
    {
        @Override
        public void run()
        {
            HandlerMain.postDelayed(RunnableMain, 5000);

            if (IsConnected)
            {
                return;
            }

            new SocketThread().start();
        }
    };
    private int MessageLength = -1;
    private byte[] BufferMain = new byte[0];

    //
    // Encode & Decode
    //
    public static void Send(int Packet, String Message)
    {
        byte[] Buffer = new byte[2 + 4 + 4 + Message.length()];

        Write2Byte(Packet, Buffer);
        Write4Byte(Buffer.length, Buffer, 2);
        Write4Byte(0, Buffer, 6);

        System.arraycopy(Message.getBytes(), 0, Buffer, 10, Message.length());

        try
        {
            if (!IsConnected)
            {
                return;
            }

            BOS.write(Buffer);
            BOS.flush();
        }
        catch (Exception e)
        {
            Misc.Debug("NetworkService-Send: " + e);
        }
    }
    private static void Write4Byte(int Value, byte[] Buffer, int Offset)
    {
        Buffer[Offset] = (byte) Value;
        Buffer[Offset + 1] = (byte) (Value >> 8);
        Buffer[Offset + 2] = (byte) (Value >> 16);
        Buffer[Offset + 3] = (byte) (Value >> 24);
    }

    private static void Write2Byte(int Value, byte[] Buffer)
    {
        Buffer[0] = (byte) Value;
        Buffer[1] = (byte) (Value >> 8);
    }

    private static int Read4Byte(byte[] Buffer)
    {
        return (Buffer[2] & 0xFF) | (Buffer[3] & 0xFF) << 8 | (Buffer[4] & 0xFF) << 16 | (Buffer[5] & 0xFF) << 24;
    }

    private static int Read2Byte(byte[] Buffer)
    {
        return (Buffer[0] & 0xFF) | (Buffer[1] & 0xFF) << 8;
    }

    //
    // Emitter
    //
    public static void On(int Packet, Listener listener)
    {
        LinkedList<Listener> ListenerList = ListenerMain.get(Packet);

        if (ListenerList == null)
        {
            ListenerList = new LinkedList<>();
            ListenerMain.put(Packet, ListenerList);
        }

        ListenerList.add(listener);
    }
    public static void Once(int Packet, Listener listener)
    {
        On(Packet, new OnceListener(Packet, listener));
    }
    public static void RemoveAll(int Packet)
    {
        LinkedList<Listener> ListenerList = ListenerMain.get(Packet);

        if (ListenerList != null)
        {
            for (Listener listener : ListenerList)
            {
                listener.Remove = true;
            }
        }
    }
    public static void Remove(int Packet, Listener listener)
    {
        LinkedList<Listener> ListenerList = ListenerMain.get(Packet);

        if (ListenerList != null)
        {
            for (Listener listener2 : ListenerList)
            {
                if (SameAs(listener, listener2))
                {
                    listener2.Remove = true;
                }
            }
        }
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
    private static boolean SameAs(Listener listener, Listener listener2)
    {
        return listener.equals(listener2) || (listener2 instanceof OnceListener && listener.equals(((OnceListener) listener2).ListenerMain));
    }
    @Override
    public int onStartCommand(Intent intent, int Flags, int StartID)
    {
        HandlerMain.post(RunnableMain);

        return START_STICKY;
    }
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
    private void Deserialize(byte[] Chunk, int Length)
    {
        byte[] BufferTemp = new byte[ BufferMain.length + Length ];

        System.arraycopy(BufferMain, 0, BufferTemp, 0, BufferMain.length);
        System.arraycopy(Chunk, 0, BufferTemp, BufferMain.length, Length);

        BufferMain = BufferTemp;

        if (BufferMain.length < 6)
        {
            return;
        }

        if (MessageLength == -1)
        {
            MessageLength = Read4Byte(BufferMain);
        }

        while (MessageLength != -1 && BufferMain.length >= MessageLength)
        {
            byte[] QuestBuffer = new byte[ MessageLength ];

            System.arraycopy(BufferMain, 0, QuestBuffer, 0, MessageLength);

            OnMessage(QuestBuffer);

            BufferTemp = new byte[ BufferMain.length - MessageLength ];

            System.arraycopy(BufferMain, MessageLength, BufferTemp, 0, BufferTemp.length);

            MessageLength = -1;
            BufferMain = BufferTemp;

            if (BufferMain.length < 6)
            {
                return;
            }

            MessageLength = Read4Byte(BufferMain);
        }
    }
    private void OnMessage(byte[] Buffer)
    {
        int Packet = Read2Byte(Buffer);

        byte[] BufferTemp = new byte[ Buffer.length - 6 ];

        System.arraycopy(Buffer, 6, BufferTemp, 0, BufferTemp.length);

        try
        {
            Emitter(Packet, new String(BufferTemp, "UTF-8"));
        }
        catch (Exception e)
        {
            Misc.Debug("NetworkService-OnMessage: " + e);
        }
    }

    //
    // Find Server
    //
    private void Emitter(int Packet, String Message)
    {
        LinkedList<Listener> ListenerList = ListenerMain.get(Packet);

        if (ListenerList != null)
        {
            Iterator<Listener> itr = ListenerList.iterator();

            while (itr.hasNext())
            {
                Listener listener = itr.next();

                if (listener.Remove)
                {
                    itr.remove();
                }
                else
                {
                    listener.Call(Message);
                }
            }
        }
    }

    //
    // Packet List
    //
    private String GetBestServer()
    {
        return "157.119.190.112";
    }

    private static class OnceListener extends Listener
    {
        private final int Packet;
        private final Listener ListenerMain;

        private OnceListener(int packet, Listener listener)
        {
            Packet = packet;
            ListenerMain = listener;
        }

        @Override
        public void Call(String Message)
        {
            Remove(Packet, this);
            ListenerMain.Call(Message);
        }
    }

    public static abstract class Listener
    {
        private boolean Remove = false;

        abstract public void Call(String Message);
    }

    private final class SocketThread extends Thread
    {
        @Override
        public void run()
        {
            try
            {
                Socket SocketMain = new Socket();
                SocketMain.connect(new InetSocketAddress(GetBestServer(), 37001), 120000);

                BIS = new BufferedInputStream(SocketMain.getInputStream());
                BOS = new BufferedOutputStream(SocketMain.getOutputStream());

                IsConnected = true;

                new ReceiveThread().start();
            }
            catch (Exception e)
            {
                Misc.Debug("NetworkService: " + e);
            }
        }
    }

    private final class ReceiveThread extends Thread
    {
        @Override
        public void run()
        {
            try
            {
                int Length;
                byte[] Chunk = new byte[ 1024 ];

                while ((Length = BIS.read(Chunk)) != -1)
                {
                    Deserialize(Chunk, Length);
                }
            }
            catch (Exception e)
            {
                Misc.Debug("NetworkService-Receive: " + e);
            }

            IsConnected = false;
        }
    }
}
