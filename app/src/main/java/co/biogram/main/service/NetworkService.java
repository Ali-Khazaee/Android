package co.biogram.main.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import co.biogram.main.handler.Misc;

public class NetworkService extends Service
{
    private static Socket SocketMain;
    private static BufferedInputStream BIS;
    private static BufferedOutputStream BOS;

    @Override
    public int onStartCommand(Intent intent, int Flags, int StartID)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    SocketMain = new Socket();
                    SocketMain.connect(new InetSocketAddress(GetBestServer(), 37001), 120000);

                    BIS = new BufferedInputStream(SocketMain.getInputStream());
                    BOS = new BufferedOutputStream(SocketMain.getOutputStream());
                }
                catch (Exception e)
                {
                    Misc.Debug("Socket: " + e);
                }
            }
        }).start();

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    Thread.sleep(2000);
                    int Length;
                    byte[] Chunk = new byte[1024];

                    while ((Length = BIS.read(Chunk)) != -1)
                    {
                        Deserialize(Chunk, Length);
                    }
                }
                catch (Exception e)
                {
                    Misc.Debug("Socket-Receive: " + e);
                }
            }
        }).start();

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    //
    // Send & Receive
    //

    private int MessageLength = -1;
    private byte[] BufferMain = new byte[0];

    private void Deserialize(byte[] Chunk, int Length)
    {
        byte[] BufferTemp = new byte[BufferMain.length + Length];

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
            byte[] QuestBuffer = new byte[MessageLength];

            System.arraycopy(BufferMain, 0, QuestBuffer, 0, MessageLength);

            OnMessage(QuestBuffer);

            BufferTemp = new byte[BufferMain.length - MessageLength];

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

    public static void Send(int Packet, String Message)
    {
        byte[] Buffer = new byte[2 + 4 + 4 + Message.length()];

        Write2Byte(Packet, Buffer);
        Write4Byte(Buffer.length, Buffer, 2);
        Write4Byte(0, Buffer, 6);

        System.arraycopy(Message.getBytes(), 0, Buffer, 10, Message.length());

        try
        {
            BOS.write(Buffer);
            BOS.flush();
        }
        catch (Exception e)
        {
            Misc.Debug("NetworkService-Send: " + e);
        }
    }

    //
    // Encode & Decode
    //

    private void OnMessage(byte[] Buffer)
    {
        int Packet = Read2Byte(Buffer);

        byte[] BufferTemp = new byte[Buffer.length - 6];

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

    @SuppressWarnings("all")
    private static Map<Integer, LinkedList<Listener>> ListenerMain = new HashMap<>();

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

    public void RemoveAll(int Packet)
    {
        ListenerMain.remove(Packet);
    }

    public static void Remove(int Packet, Listener listener)
    {
        LinkedList<Listener> ListenerList = ListenerMain.get(Packet);

        if (ListenerList != null)
        {
            Iterator<Listener> itr = ListenerList.iterator();

            while (itr.hasNext())
            {
                Listener name = itr.next();

                if (SameAs(listener, name))
                {
                    itr.remove();
                }
            }
        }
    }

    private void Emitter(int Packet, String Message)
    {
        LinkedList<Listener> ListenerList = ListenerMain.get(Packet);

        if (ListenerList != null)
        {
            for (Listener listener : ListenerList)
            {
                listener.Call(Message);
            }
        }
    }

    private static boolean SameAs(Listener listener, Listener listener2)
    {
        return listener.equals(listener2) || (listener2 instanceof OnceListener && listener.equals(((OnceListener) listener2).ListenerMain));
    }

    private static class OnceListener implements Listener
    {
        private int Packet;
        private Listener ListenerMain;

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

    public interface Listener
    {
        void Call(String Message);
    }

    //
    // Find Server
    //

    private String GetBestServer()
    {
        return "157.119.190.112";
    }

    //
    // Packet List
    //

    public static final int PACKET_USERNAME = 1;
}



    /*
    public void Send(int Packet, int FileLength, String Message)
    {
        byte[] Buffer = new byte[2 + 4 + 4 + Message.length()];

        Write2Byte(Packet, Buffer);
        Write4Byte(Buffer.length, Buffer, 2);
        Write4Byte(FileLength, Buffer, 6);

        System.arraycopy(Message.getBytes(), 0, Buffer, 10, Message.length());

        try
        {
            BOS.write(Buffer);
        }
        catch (Exception e)
        {
            Misc.Debug("Send-File: " + e);
        }
    }

    public void Send(byte[] Message, boolean Flush)
    {
        try
        {
            BOS.write(Message);

            if (Flush)
            {
                BOS.flush();
            }
        }
        catch (Exception e)
        {
            Misc.Debug("Send-Data: " + e);
        }
    }
     */
