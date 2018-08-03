package co.biogram.main.handler;

import android.annotation.SuppressLint;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketHandler
{
    public static final String EVENT_CONNECT = "connect";
    public static final String EVENT_DISCONNECT = "disconnect";
    public static final String EVENT_ERROR = "error";

    private static final byte DATA_TYPE_STRING = 1;
    private static final byte DATA_TYPE_BUFFER = 2;
    private static final byte DATA_TYPE_INT = 3;
    private static final byte DATA_TYPE_DOUBLE = 4;
    private static final byte DATA_TYPE_OBJECT = 5;

    private static final byte MESSAGE_TYPE_ACK = 7;
    private static final byte MESSAGE_TYPE_DATA = 2;
    private static final byte MESSAGE_TYPE_DATA_WITH_ACK = 6;

    @SuppressLint("all")
    private Map<Integer, Callback> AckList = new HashMap<>();
    private Map<String, Listener> CallList = new HashMap<>();
    private LinkedList<byte[]> Queue = new LinkedList<>();
    private long ReconnectIntervalTime = 1000;
    private BufferedOutputStream OutputStream;
    private BufferedInputStream InputStream;
    private ExecutorService ExecutorMain;
    private java.net.Socket SocketMain;
    private boolean IsConnected;
    private int MessageID = 1;
    private String Host;
    private int Port;

    public SocketHandler(String host, int port)
    {
        Host = host;
        Port = port;

        Connect();
    }

    private void Connect()
    {
        if (IsConnected)
            return;

        if (ExecutorMain == null || ExecutorMain.isShutdown())
        {
            ExecutorMain = Executors.newSingleThreadExecutor();
            ExecutorMain.submit(new Callable<Void>()
            {
                @Override
                public Void call()
                {
                    try
                    {
                        SocketMain = new java.net.Socket();
                        SocketMain.connect(new InetSocketAddress(Host, Port), 120000);

                        InputStream = new BufferedInputStream(SocketMain.getInputStream());
                        OutputStream = new BufferedOutputStream(SocketMain.getOutputStream());

                        new Thread(new Runnable()
                        {
                            private Reader ReaderMain = new Reader();
                            private byte[] Chunk = new byte[ 1024 ];

                            @Override
                            public void run()
                            {
                                try
                                {
                                    int ByteRead;

                                    while ((ByteRead = InputStream.read(Chunk)) != -1)
                                    {
                                        for (byte[] Buffer : ReaderMain.read(Chunk, ByteRead))
                                        {
                                            Packet packet = Deserialize(Buffer);

                                            switch (packet.MessageType)
                                            {
                                                case MESSAGE_TYPE_DATA:
                                                    Call(packet.Event, packet.Data);
                                                    break;
                                                case MESSAGE_TYPE_DATA_WITH_ACK:
                                                    Call(packet.Event, packet.Data, CreateAck(packet));
                                                    break;
                                                case MESSAGE_TYPE_ACK:
                                                    if (AckList.containsKey(packet.MessageID))
                                                    {
                                                        AckList.get(packet.MessageID).Call(packet.Data);
                                                        AckList.remove(packet.MessageID);
                                                    }
                                                    break;
                                            }
                                        }
                                    }
                                }
                                catch (Exception e)
                                {
                                    Call(EVENT_ERROR, e);
                                }

                                IsConnected = false;
                                Call(EVENT_DISCONNECT);
                                Reconnect();
                            }
                        }).start();

                        IsConnected = true;

                        if (Queue.size() > 0)
                        {
                            for (byte[] Data : Queue)
                                OutputStream.write(Data);

                            OutputStream.flush();
                            Queue.clear();
                        }

                        Call(EVENT_CONNECT);
                    }
                    catch (Exception e)
                    {
                        Call(EVENT_ERROR, e);
                        Call(EVENT_DISCONNECT);

                        Reconnect();
                    }

                    return null;
                }
            });
        }
    }

    private static byte[] Serialize(byte[] Event, byte[] Data, byte MessageType, byte DataType, int MessageID)
    {
        short EventLength = (short) Event.length;
        int DataLength = Data.length;
        int MessageLength = 4 + 1 + 1 + 4 + 2 + EventLength + 4 + DataLength;

        byte[] Buffer = new byte[ 4 + MessageLength ];
        int Offset = 0;

        Utils.writeInt(MessageLength, Buffer, Offset);
        Offset += 4;

        Buffer[ Offset++ ] = DataType;
        Buffer[ Offset++ ] = MessageType;

        Utils.writeInt(MessageID, Buffer, Offset);
        Offset += 4;

        Utils.writeShort(EventLength, Buffer, Offset);
        Offset += 2;

        System.arraycopy(Event, 0, Buffer, Offset, EventLength);
        Offset += EventLength;

        Utils.writeInt(DataLength, Buffer, Offset);
        Offset += 4;

        System.arraycopy(Data, 0, Buffer, Offset, DataLength);

        return Buffer;
    }

    private static Packet Deserialize(byte[] Buffer) throws Exception
    {
        int Offset = 4;

        byte DataType = Buffer[ Offset++ ];
        byte MessageType = Buffer[ Offset++ ];

        int MessageID = Utils.readInt(Buffer, Offset);
        Offset += 4;

        short EventLength = Utils.readShort(Buffer, Offset);
        Offset += 2;

        String Event = new String(Arrays.copyOfRange(Buffer, Offset, Offset + EventLength), "UTF-8");
        Offset += EventLength;

        int DataLength = Utils.readInt(Buffer, Offset);
        Offset += 4;

        switch (DataType)
        {
            case DATA_TYPE_STRING:
            {
                return new Packet(Event, new String(Arrays.copyOfRange(Buffer, Offset, Offset + DataLength), "UTF-8"), MessageID, MessageType);
            }
            case DATA_TYPE_BUFFER:
            {
                return new Packet(Event, Arrays.copyOfRange(Buffer, Offset, Offset + DataLength), MessageID, MessageType);
            }
            case DATA_TYPE_OBJECT:
            {
                byte[] DataByte = Arrays.copyOfRange(Buffer, Offset, Offset + DataLength);

                if (Utils.isJsonObject(DataByte))
                    return new Packet(Event, new JSONObject(new String(DataByte, "UTF-8")), MessageID, MessageType);
                else if (Utils.isJsonArray(DataByte))
                    return new Packet(Event, new JSONArray(new String(DataByte, "UTF-8")), MessageID, MessageType);
                else
                    throw new Exception("Invalid Object Type: " + DataType);
            }
            case DATA_TYPE_INT:
            {
                return new Packet(Event, Utils.readInt48(Buffer, Offset), MessageID, MessageType);
            }
            case DATA_TYPE_DOUBLE:
            {
                return new Packet(Event, Utils.readDouble(Buffer, Offset), MessageID, MessageType);
            }
            default:
                throw new Exception("Invalid Data Type: " + DataType);
        }
    }

    private void Reconnect()
    {
        ExecutorMain.submit(new Callable<Void>()
        {
            @Override
            public Void call() throws Exception
            {
                Thread.sleep(ReconnectIntervalTime);
                Connect();
                return null;
            }
        });
    }

    private int MessageID()
    {
        if (++MessageID >= Integer.MAX_VALUE)
            MessageID = 1;

        return MessageID;
    }

    public void Add(String Event, Listener Call)
    {
        CallList.put(Event, Call);
    }

    public void Remove(String Event)
    {
        CallList.remove(Event);
    }

    private void Call(String Event, Object... Param)
    {
        Listener Callback = CallList.get(Event);

        if (Callback != null)
            Callback.Call(Param);
    }

    static class Packet
    {
        String Event;
        Object Data;
        int MessageID;
        byte MessageType;

        Packet(String event, Object data, int messageID, byte messageType)
        {
            Event = event;
            Data = data;
            MessageID = messageID;
            MessageType = messageType;
        }
    }

    public interface Listener
    {
        void Call(Object... Param);
    }

    public interface Callback
    {
        void Call(Object Param);
    }

    private void Send(String Event, byte[] Data, byte MessageType, byte DataType, int messageID, Callback callback)
    {
        messageID = messageID > 0 ? messageID : MessageID();

        if (callback != null)
            AckList.put(messageID, callback);

        byte[] packet = Serialize(Event.getBytes(), Data, MessageType, DataType, messageID);

        try
        {
            if (IsConnected)
            {
                OutputStream.write(packet);
                OutputStream.flush();
            }
            else
            {
                if (Queue.size() + 1 > 1024)
                    Queue.poll();

                Queue.offer(packet);
            }
        }
        catch (Exception e)
        {
            Call(EVENT_ERROR, e);
        }
    }

    public void QQ()
    {
        try
        {
            Misc.Debug("QQ");

            String A = "Salam";

            byte[] packet = new byte[ 2 + 2 + 2 + A.length() ];



            Utils.writeInt(4, packet, 0);
            Utils.writeInt(9090, packet, 2);
            Utils.writeInt(A.length(), packet, 4);
            System.arraycopy(A.getBytes(), 0, packet, 6, A.length());


            OutputStream.write(packet);
            OutputStream.flush();
        }
        catch (Exception e)
        {
            Misc.Debug("DD: " + e);
        }
    }

    public void Call(String Event, String Data)
    {
        Send(Event, Data.getBytes(), MESSAGE_TYPE_DATA, DATA_TYPE_STRING, -1, null);
    }

    public void Call(String Event, String Data, Callback callback)
    {
        Send(Event, Data.getBytes(), MESSAGE_TYPE_DATA_WITH_ACK, DATA_TYPE_STRING, -1, callback);
    }

    public void Call(String Event, JSONObject Data)
    {
        Send(Event, Data.toString().getBytes(), MESSAGE_TYPE_DATA, DATA_TYPE_OBJECT, -1, null);
    }

    public void Call(String Event, JSONObject Data, Callback callback)
    {
        Send(Event, Data.toString().getBytes(), MESSAGE_TYPE_DATA_WITH_ACK, DATA_TYPE_OBJECT, -1, callback);
    }

    public void Call(String Event, JSONArray Data)
    {
        Send(Event, Data.toString().getBytes(), MESSAGE_TYPE_DATA, DATA_TYPE_OBJECT, -1, null);
    }

    public void Call(String Event, JSONArray Data, Callback callback)
    {
        Send(Event, Data.toString().getBytes(), MESSAGE_TYPE_DATA_WITH_ACK, DATA_TYPE_OBJECT, -1, callback);
    }

    public void Call(String Event, byte[] Data)
    {
        Send(Event, Data, MESSAGE_TYPE_DATA, DATA_TYPE_BUFFER, -1, null);
    }

    public void Call(String Event, byte[] Data, Callback callback)
    {
        Send(Event, Data, MESSAGE_TYPE_DATA_WITH_ACK, DATA_TYPE_BUFFER, -1, callback);
    }

    private Ack CreateAck(final Packet packet)
    {
        return new Ack()
        {
            @Override
            public void SendAck(String data)
            {
                SendAck(data.getBytes(), DATA_TYPE_STRING);
            }

            @Override
            public void SendAck(JSONObject data)
            {
                SendAck(data.toString().getBytes(), DATA_TYPE_OBJECT);
            }

            @Override
            public void SendAck(JSONArray data)
            {
                SendAck(data.toString().getBytes(), DATA_TYPE_OBJECT);
            }

            @Override
            public void SendAck(byte[] data)
            {
                SendAck(data, DATA_TYPE_BUFFER);
            }

            private void SendAck(byte[] data, byte dt)
            {
                Send("", data, MESSAGE_TYPE_ACK, dt, packet.MessageID, null);
            }
        };
    }

    public interface Ack
    {
        void SendAck(String Data);
        void SendAck(JSONObject Data);
        void SendAck(JSONArray Data);
        void SendAck(byte[] Data);
    }
}

class Reader
{
    private byte[] buffer;
    private int offset;
    private int bytesRead;
    private int messageLength;

    private int offsetChunk;

    ArrayList<byte[]> read(byte[] chunk, int effectiveChunkLength)
    {
        offsetChunk = 0;
        ArrayList<byte[]> buffers = new ArrayList<>();

        while (offsetChunk < effectiveChunkLength)
        {
            if (bytesRead < 4)
            {
                if (readMessageLength(chunk, effectiveChunkLength))
                {
                    try
                    {
                        buffer = new byte[ 4 + messageLength ];
                        Utils.writeInt48(messageLength, buffer, offset);
                        offset += 4;
                    }
                    catch (Exception e)
                    {
                        Misc.Debug("ZZ: " + e.getMessage());
                    }
                }
                else
                {
                    break;
                }
            }

            if (bytesRead < buffer.length && !readMessageContent(chunk, effectiveChunkLength))
            {
                break;
            }

            // Buffer ready, store it and keep reading the chunk
            buffers.add(buffer);
            offset = 0;
            bytesRead = 0;
            messageLength = 0;
        }

        return buffers;
    }

    private boolean readMessageLength(byte[] chunk, int effectiveChunkLength)
    {
        for (; offsetChunk < effectiveChunkLength && bytesRead < 4; offsetChunk++, bytesRead++)
        {
            messageLength |= chunk[ offsetChunk ] << (bytesRead * 8);
        }

        return bytesRead == 4;
    }

    private boolean readMessageContent(byte[] chunk, int effectiveChunkLength)
    {
        int bytesToRead = buffer.length - bytesRead;
        int bytesInChunk = effectiveChunkLength - offsetChunk;
        int end = bytesToRead > bytesInChunk ? effectiveChunkLength : offsetChunk + bytesToRead;

        System.arraycopy(chunk, offsetChunk, buffer, offset, end - offsetChunk);

        int bytesActuallyRead = end - offsetChunk;

        bytesRead += bytesActuallyRead;
        offset += bytesActuallyRead;
        offsetChunk = end;

        return bytesRead == buffer.length;
    }

}

class Utils
{
    private static final byte CHAR_CODE_OPEN_BRACKET = 91;
    private static final byte CHAR_CODE_OPEN_BRACE = 123;


    static void writeInt48(long value, byte[] buffer, int offset)
    {
        buffer[ offset ] = (byte) value;
        buffer[ offset + 1 ] = (byte) (value >> 8);
        buffer[ offset + 2 ] = (byte) (value >> 16);
        buffer[ offset + 3 ] = (byte) (value >> 24);
        buffer[ offset + 4 ] = (byte) (value >> 32);
        buffer[ offset + 5 ] = (byte) (value >> 40);
    }

    static void writeInt(long value, byte[] buffer, int offset)
    {
        buffer[ offset ] = (byte) value;
        buffer[ offset + 1 ] = (byte) (value >> 8);
        buffer[ offset + 2 ] = (byte) (value >> 16);
        buffer[ offset + 3 ] = (byte) (value >> 24);
    }

    static void writeShort(long value, byte[] buffer, int offset)
    {
        buffer[ offset ] = (byte) value;
        buffer[ offset + 1 ] = (byte) (value >> 8);
    }

    private static long readLong(byte[] buffer, int offset)
    {
        return (buffer[ offset ] & 0xFFL) | (buffer[ offset + 1 ] & 0xFFL) << 8 | (buffer[ offset + 2 ] & 0xFFL) << 16 | (buffer[ offset + 3 ] & 0xFFL) << 24 | (buffer[ offset + 4 ] & 0xFFL) << 32 | (buffer[ offset + 5 ] & 0xFFL) << 40 | (buffer[ offset + 6 ] & 0xFFL) << 48 | (buffer[ offset + 7 ] & 0xFFL) << 56;
    }

    static double readDouble(byte[] buffer, int offset)
    {
        return Double.longBitsToDouble(readLong(buffer, offset));
    }

    static long readInt48(byte[] buffer, int offset)
    {
        return (buffer[ offset ] & 0xFFL) | (buffer[ offset + 1 ] & 0xFFL) << 8 | (buffer[ offset + 2 ] & 0xFFL) << 16 | (buffer[ offset + 3 ] & 0xFFL) << 24 | (buffer[ offset + 4 ] & 0xFFL) << 32 | (buffer[ offset + 5 ] & 0xFFL) << 40;
    }

    static int readInt(byte[] buffer, int offset)
    {
        return (buffer[ offset ] & 0xFF) | (buffer[ offset + 1 ] & 0xFF) << 8 | (buffer[ offset + 2 ] & 0xFF) << 16 | (buffer[ offset + 3 ] & 0xFF) << 24;
    }

    static short readShort(byte[] buffer, int offset)
    {
        return (short) ((buffer[ offset ] & 0xFF) | (buffer[ offset + 1 ] & 0xFF) << 8);
    }

    static boolean isJsonObject(byte[] buffer)
    {
        return buffer[ 0 ] == CHAR_CODE_OPEN_BRACE;
    }

    static boolean isJsonArray(byte[] buffer)
    {
        return buffer[ 0 ] == CHAR_CODE_OPEN_BRACKET;
    }
}
