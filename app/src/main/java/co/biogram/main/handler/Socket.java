package co.biogram.main.handler;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Socket
{
    private static final byte DT_STRING = 1;
    private static final byte DT_BINARY = 2;
    private static final byte DT_INTEGER = 3;
    private static final byte DT_DECIMAL = 4;
    private static final byte DT_OBJECT = 5;
    private static final byte DT_BOOLEAN = 6;
    private static final byte DT_EMPTY = 7;

    private int Port;
    private String Host;

    public Socket(String host, int port)
    {
        Host = host;
        Port = port;
    }

    class Message
    {
        String Event;
        byte[] Data;
        byte MessageType;
        byte DataType;
        int ID;

        Message(String event, byte[] data, byte messageType, byte dataType, int id)
        {
            Event = event;
            Data = data;
            MessageType = messageType;
            DataType = dataType;
            ID = id;
        }
    }










































    private boolean IsConnected;
    private boolean ManuallyClosed;
    private java.net.Socket SocketMain;
    private BufferedInputStream InputStream;
    private BufferedOutputStream OutputStream;
    private EventThread EventThreadMain = new EventThread();



    public class Emitter
    {
        private Map<String, LinkedList<Listener>> callbacks = new HashMap<>();

        Emitter on(String event, Listener fn)
        {
            LinkedList<Listener> callbacksList = callbacks.get(event);
            if (callbacksList == null)
            {
                callbacksList = new LinkedList<>();
                callbacks.put(event, callbacksList);
            }
            callbacksList.add(fn);
            return this;
        }

        Emitter once(String event, Listener fn)
        {
            on(event, new OnceListener(event, fn));
            return this;
        }

        Emitter removeAllListeners(String event)
        {
            callbacks.remove(event);
            return this;
        }

        Emitter removeListener(String event, Listener fn)
        {
            LinkedList<Listener> callbacks = this.callbacks.get(event);
            if (callbacks != null)
            {
                Iterator<Listener> it = callbacks.iterator();
                while (it.hasNext())
                {
                    Listener internal = it.next();
                    if (sameAs(fn, internal))
                    {
                        it.remove();
                        break;
                    }
                }
            }
            return this;
        }

        Emitter emit(String event, Object... args)
        {
            LinkedList<Listener> callbacks = this.callbacks.get(event);
            if (callbacks != null)
            {
                for (Listener fn : callbacks)
                {
                    fn.call(args);
                }
            }
            return this;
        }

        int listenerCount(String event)
        {
            LinkedList<Listener> callbacks = this.callbacks.get(event);
            return callbacks == null ? 0 : callbacks.size();
        }

        boolean hasListeners(String event)
        {
            return listenerCount(event) > 0;
        }

        private boolean sameAs(Listener fn, Listener internal)
        {
            if (fn.equals(internal))
            {
                return true;
            }
            else if (internal instanceof OnceListener)
            {
                return fn.equals(((OnceListener) internal).fn);
            }
            else
            {
                return false;
            }
        }

        private class OnceListener implements Listener
        {
            private final String event;
            private final Listener fn;

            private OnceListener(String event, Listener fn)
            {
                this.event = event;
                this.fn = fn;
            }

            @Override
            public void call(Object... args)
            {
                removeListener(event, this);
                fn.call(args);
            }
        }
    }

    public interface Listener
    {
        void call(Object... args);
    }


    public interface FutureCallback<V>
    {
        void onSuccess(V result);

        void onFailure(Throwable failure);
    }



















    public void Connect()
    {
        if (IsConnected)
            return;

        ManuallyClosed = false;

        EventThreadMain.run(new Callable<Void>()
        {
            @Override
            public Void call() throws Exception
            {
                Misc.Debug("Trying To Connect To: " + Host);

                SocketMain = new java.net.Socket();
                SocketMain.connect(new InetSocketAddress(Host, Port), 60);

                InputStream = new BufferedInputStream(SocketMain.getInputStream());
                OutputStream = new BufferedOutputStream(SocketMain.getOutputStream());

                new SocketReceiverThread().start();

                return null;
            }
        }).addCallback(new FutureCallback<Void>()
        {
            @Override
            public void onSuccess(Void result)
            {
                Misc.Debug("Connected To: " + Host);

                IsConnected = true;

                try
                {
                    if (Queue.size() > 0)
                    {
                        for (byte[] message : Queue)
                            OutputStream.write(message);

                        OutputStream.flush();
                        Queue.clear();
                    }
                }
                catch (Exception e)
                {
                    emitter.emit(EVENT_ERROR, e);
                }

                emitter.emit(EVENT_CONNECTED);
            }

            @Override
            public void onFailure(Throwable failure)
            {
                Misc.Debug("Failed Connect To: " + Host + " Message: " + failure.getMessage());

                emitter.emit(EVENT_ERROR, failure);
                emitter.emit(EVENT_CLOSE);

                if (!ManuallyClosed)
                {
                    reconnect();
                }
                else
                {
                    EventThreadMain.stop();
                }
            }
        });
    }



    public class ListenableFuture<V>
    {
        private FutureCallback<V> callback;
        private V result;
        private Throwable failure;
        private boolean isCompleted;

        public void addCallback(FutureCallback<V> callback)
        {
            this.callback = callback;
            resolve();
        }

        void setResult(V result)
        {
            this.result = result;
            isCompleted = true;
            resolve();
        }

        void setFailure(Throwable failure)
        {
            this.failure = failure;
            isCompleted = true;
            resolve();
        }

        private void resolve()
        {
            if (callback != null && isCompleted)
            {
                if (failure == null)
                {
                    callback.onSuccess(result);
                }
                else
                {
                    callback.onFailure(failure);
                }
            }
        }
    }

    private static final String EVENT_ERROR = "error";
    private static final String EVENT_CONNECTED = "connected";


    private static final String EVENT_CONNECT = "Connect";
    private static final String EVENT_SOCKET_CONNECT = "socket_connect";
    private static final String EVENT_CLOSE = "close";

    private static final String EVENT_RECONNECTING = "reconnecting";

    public String id;

    private static final int MAX_MESSAGE_ID = Integer.MAX_VALUE;

    private Options Options = new Options();

    private int messageId = 1;

    private LinkedList<byte[]> Queue = new LinkedList<>();
    private Emitter emitter = new Emitter();
    private Map<Integer, Listener> acks = new HashMap<>();

    public void close()
    {
        if (!IsConnected)
            return;

        ManuallyClosed = true;
        EventThreadMain.run(new Callable<Void>()
        {
            @Override
            public Void call() throws Exception
            {
                SocketMain.close();
                return null;
            }
        }).addCallback(new FutureCallback<Void>()
        {
            @Override
            public void onSuccess(Void result)
            {
                IsConnected = false;

                emitter.emit(EVENT_CLOSE);
                EventThreadMain.stop();
            }

            @Override
            public void onFailure(Throwable failure)
            {
                emitter.emit(EVENT_ERROR, failure);
            }
        });
    }

    public void emit(String event)
    {
        send(event, EMPTY_BYTE_ARRAY, MT_DATA, DT_EMPTY);
    }

    public void emit(String event, EmitOptions emitOpts)
    {
        emitTo(event, EMPTY_BYTE_ARRAY, DT_EMPTY, emitOpts);
    }

    public void emit(String event, Listener cb)
    {
        send(event, EMPTY_BYTE_ARRAY, MT_DATA_WITH_ACK, DT_EMPTY, cb);
    }

    public void emit(String event, String data)
    {
        send(event, data.getBytes(), MT_DATA, DT_STRING);
    }

    public void emit(String event, String data, EmitOptions emitOpts)
    {
        emitTo(event, data.getBytes(), DT_STRING, emitOpts);
    }

    public void emit(String event, String data, Listener cb)
    {
        send(event, data.getBytes(), MT_DATA_WITH_ACK, DT_STRING, cb);
    }

    public void emit(String event, long data)
    {
        send(event, int48ToByteArray(data), MT_DATA, DT_INTEGER);
    }

    public void emit(String event, long data, EmitOptions emitOpts)
    {
        emitTo(event, int48ToByteArray(data), DT_INTEGER, emitOpts);
    }

    public void emit(String event, long data, Listener cb)
    {
        send(event, int48ToByteArray(data), MT_DATA_WITH_ACK, DT_INTEGER, cb);
    }

    public void emit(String event, double data)
    {
        send(event, doubleToByteArray(data), MT_DATA, DT_DECIMAL);
    }

    public void emit(String event, double data, EmitOptions emitOpts)
    {
        emitTo(event, doubleToByteArray(data), DT_DECIMAL, emitOpts);
    }

    public void emit(String event, double data, Listener cb)
    {
        send(event, doubleToByteArray(data), MT_DATA_WITH_ACK, DT_DECIMAL, cb);
    }

    public void emit(String event, Object data)
    {
        send(event, Options.objectSerializer.serialize(data), MT_DATA, DT_OBJECT);
    }

    public void emit(String event, Object data, EmitOptions emitOpts)
    {
        emitTo(event, Options.objectSerializer.serialize(data), DT_OBJECT, emitOpts);
    }

    public void emit(String event, Object data, Listener cb)
    {
        send(event, Options.objectSerializer.serialize(data), MT_DATA_WITH_ACK, DT_OBJECT, cb);
    }

    public void emit(String event, boolean data)
    {
        send(event, booleanToByteArray(data), MT_DATA, DT_BOOLEAN);
    }

    public void emit(String event, boolean data, EmitOptions emitOpts)
    {
        emitTo(event, booleanToByteArray(data), DT_BOOLEAN, emitOpts);
    }

    public void emit(String event, boolean data, Listener cb)
    {
        send(event, booleanToByteArray(data), MT_DATA_WITH_ACK, DT_BOOLEAN, cb);
    }

    public void emit(String event, byte[] data)
    {
        send(event, data, MT_DATA, DT_BINARY);
    }

    public void emit(String event, byte[] data, EmitOptions emitOpts)
    {
        emitTo(event, data, DT_BINARY, emitOpts);
    }

    public void emit(String event, byte[] data, Listener cb)
    {
        send(event, data, MT_DATA_WITH_ACK, DT_BINARY, cb);
    }

    public void join(String room)
    {
        send(EMPTY_STRING, room.getBytes(), MT_JOIN_ROOM, DT_STRING);
    }

    public void leave(String room)
    {
        send(EMPTY_STRING, room.getBytes(), MT_LEAVE_ROOM, DT_STRING);
    }

    public void leaveAll()
    {
        send(EMPTY_STRING, EMPTY_BYTE_ARRAY, MT_LEAVE_ALL_ROOMS, DT_STRING);
    }


    public void on(String event, Listener fn)
    {
        emitter.on(event, fn);
    }

    public void once(String event, Listener fn)
    {
        emitter.once(event, fn);
    }

    public void removeListener(String event, Listener fn)
    {
        emitter.removeListener(event, fn);
    }

    public void removeAllListeners(String event)
    {
        emitter.removeAllListeners(event);
    }







        private static final byte MT_ERROR = 0;
        private static final byte MT_REGISTER = 1;
        private static final byte MT_DATA = 2;
        private static final byte MT_DATA_TO_SOCKET = 3;
        private static final byte MT_DATA_TO_ROOM = 4;
        private static final byte MT_DATA_BROADCAST = 5;
        private static final byte MT_DATA_WITH_ACK = 6;
        private static final byte MT_ACK = 7;
        private static final byte MT_JOIN_ROOM = 8;
        private static final byte MT_LEAVE_ROOM = 9;
        private static final byte MT_LEAVE_ALL_ROOMS = 10;

        private byte[] serialize(byte[] event, byte[] data, byte mt, byte dt, int messageId)
        {
            short eventLength = (short) event.length;
            int dataLength = data.length;
            int messageLength = 4 + 1 + 1 + 4 + 2 + eventLength + 4 + dataLength;

            byte[] buffer = new byte[ 4 + messageLength ];
            int offset = 0;

            writeInt(messageLength, buffer, offset);
            offset += 4;

            buffer[ offset++ ] = dt;
            buffer[ offset++ ] = mt;

            writeInt(messageId, buffer, offset);
            offset += 4;

            writeShort(eventLength, buffer, offset);
            offset += 2;

            System.arraycopy(event, 0, buffer, offset, eventLength);
            offset += eventLength;

            writeInt(dataLength, buffer, offset);
            offset += 4;

            System.arraycopy(data, 0, buffer, offset, dataLength);

            return buffer;
        }

        private Message deserialize(byte[] buffer) throws Exception
        {
            int offset = 4;

            byte dt = buffer[ offset++ ];
            byte mt = buffer[ offset++ ];

            int messageId = readInt(buffer, offset);
            offset += 4;

            short eventLength = readShort(buffer, offset);
            offset += 2;

            String event = new String(buffer, offset, eventLength, "UTF-8");
            offset += eventLength;

            int dataLength = readInt(buffer, offset);
            offset += 4;

            byte[] data = Arrays.copyOfRange(buffer, offset, offset + dataLength);

            return new Message(event, data, mt, dt, messageId);
        }



    private void reconnect()
    {
        EventThreadMain.run(new Callable<Void>()
        {
            @Override
            public Void call() throws Exception
            {
                Thread.sleep(Options.ReconnectInterval);
                emitter.emit(EVENT_RECONNECTING);
                Connect();
                return null;
            }
        });
    }

    private void emitTo(String event, byte[] data, byte dt, EmitOptions emitOpts)
    {
        if (emitOpts.broadcast)
        {
            send(event, data, MT_DATA_BROADCAST, dt);
        }

        if (emitOpts.socketIds != null && emitOpts.socketIds.size() > 0)
        {
            send(buildDataToEvent(emitOpts.socketIds, event), data, MT_DATA_TO_SOCKET, dt);
        }

        if (emitOpts.rooms != null && emitOpts.rooms.size() > 0)
        {
            send(buildDataToEvent(emitOpts.rooms, event), data, MT_DATA_TO_ROOM, dt);
        }
    }

    private void send(String event, byte[] data, byte mt, byte dt)
    {
        send(event, data, mt, dt, nextMessageId());
    }

    private void send(String event, byte[] data, byte mt, byte dt, Listener cb)
    {
        int messageId = nextMessageId();
        acks.put(messageId, cb);
        send(event, data, mt, dt, messageId);
    }

    private void send(String event, byte[] data, byte mt, byte dt, int messageId)
    {
        byte[] message = serialize(event.getBytes(), data, mt, dt, messageId);

        if (IsConnected)
        {
            try
            {
                OutputStream.write(message);
                OutputStream.flush();
            }
            catch (IOException failure)
            {
                emitter.emit(EVENT_ERROR, failure);
            }
        }
        else if (Options.useQueue)
        {
            if (Queue.size() + 1 > Options.queueSize)
                Queue.poll();

            Queue.offer(message);
        }
    }

    private int nextMessageId()
    {
        if (++messageId >= MAX_MESSAGE_ID)
            messageId = 1;

        return messageId;
    }

    private Ack createAck(final Message message)
    {
        return new Ack()
        {
            @Override
            public void send(String data)
            {
                send(data.getBytes(), DT_STRING);
            }

            @Override
            public void send(long data)
            {
                send(int48ToByteArray(data), DT_INTEGER);
            }

            @Override
            public void send(double data)
            {
                send(doubleToByteArray(data), DT_DECIMAL);
            }

            @Override
            public void send(Object data)
            {
                send(Options.objectSerializer.serialize(data), DT_OBJECT);
            }

            @Override
            public void send(boolean data)
            {
                send(booleanToByteArray(data), DT_BOOLEAN);
            }

            @Override
            public void send(byte[] data)
            {
                send(data, DT_BINARY);
            }

            @Override
            public void send()
            {
                send(EMPTY_BYTE_ARRAY, DT_EMPTY);
            }

            private void send(byte[] data, byte dt)
            {
                Socket.this.send(EMPTY_STRING, data, MT_ACK, dt, message.ID);
            }
        };
    }

    public interface ObjectSerializer
    {
        byte[] serialize(Object object);

        Object deserialize(byte[] data);
    }

    public interface Ack
    {
        void send(String data);

        void send(long data);

        void send(double data);

        void send(Object data);

        void send(boolean data);

        void send(byte[] data);

        void send();
    }

    private static class EmitOptions
    {
        private Set<String> socketIds;
        private Set<String> rooms;
        private boolean broadcast;
    }

    private class Options
    {
        private boolean reconnect = true;
        private long ReconnectInterval = 1000;
        private boolean useQueue = true;
        private int queueSize = 1024;
        private int timeout = 0; // Disabled by default
        private Boolean keepAlive = null;
        private Boolean noDelay = null;
        private ObjectSerializer objectSerializer = new ObjectSerializer()
        {
            @Override
            public byte[] serialize(Object object)
            {
                throw new RuntimeException("objectSerializer not provided, use Socket.Options");
            }

            @Override
            public Object deserialize(byte[] data)
            {
                throw new RuntimeException("objectSerializer not provided, use Socket.Options");
            }
        };
    }

    class FutureExecutor
    {
        private ExecutorService executor;

        FutureExecutor(ExecutorService executor)
        {
            this.executor = executor;
        }

        public <V> ListenableFuture<V> submit(final Callable<V> callable)
        {
            final ListenableFuture<V> future = new ListenableFuture<>();
            executor.submit(new Callable<Object>()
            {
                @Override
                public Object call() throws Exception
                {
                    try
                    {
                        V result = callable.call();
                        future.setResult(result);
                        return result;
                    }
                    catch (Exception e)
                    {
                        future.setFailure(e);
                        throw e;
                    }
                }
            });

            return future;
        }
    }



    private final byte[] EMPTY_BYTE_ARRAY = new byte[ 0 ];
    private final String EMPTY_STRING = "";

    private byte[] int48ToByteArray(long value)
        {
            return new byte[] { (byte) value, (byte) (value >> 8), (byte) (value >> 16), (byte) (value >> 24), (byte) (value >> 32), (byte) (value >> 40) };
        }

    private byte[] longToByteArray(long value)
        {
            return new byte[] { (byte) value, (byte) (value >> 8), (byte) (value >> 16), (byte) (value >> 24), (byte) (value >> 32), (byte) (value >> 40), (byte) (value >> 48), (byte) (value >> 56) };
        }

    private byte[] doubleToByteArray(double value)
        {
            return longToByteArray(Double.doubleToRawLongBits(value));
        }

    private byte[] booleanToByteArray(boolean value)
        {
            return new byte[] { (byte) (value ? 1 : 0) };
        }

    private void writeInt(long value, byte[] buffer, int offset)
    {
        buffer[ offset ] = (byte) value;
        buffer[ offset + 1 ] = (byte) (value >> 8);
        buffer[ offset + 2 ] = (byte) (value >> 16);
        buffer[ offset + 3 ] = (byte) (value >> 24);
    }

    private void writeShort(long value, byte[] buffer, int offset)
    {
        buffer[ offset ] = (byte) value;
        buffer[ offset + 1 ] = (byte) (value >> 8);
    }

    private long readLong(byte[] buffer, int offset)
        {
            return (buffer[ offset ] & 0xFFL) | (buffer[ offset + 1 ] & 0xFFL) << 8 | (buffer[ offset + 2 ] & 0xFFL) << 16 | (buffer[ offset + 3 ] & 0xFFL) << 24 | (buffer[ offset + 4 ] & 0xFFL) << 32 | (buffer[ offset + 5 ] & 0xFFL) << 40 | (buffer[ offset + 6 ] & 0xFFL) << 48 | (buffer[ offset + 7 ] & 0xFFL) << 56;
        }

    private double readDouble(byte[] buffer, int offset)
        {
            return Double.longBitsToDouble(readLong(buffer, offset));
        }

    private long readInt48(byte[] buffer, int offset)
        {
            return (buffer[ offset ] & 0xFFL) | (buffer[ offset + 1 ] & 0xFFL) << 8 | (buffer[ offset + 2 ] & 0xFFL) << 16 | (buffer[ offset + 3 ] & 0xFFL) << 24 | (buffer[ offset + 4 ] & 0xFFL) << 32 | (buffer[ offset + 5 ] & 0xFFL) << 40;
        }

    private int readInt(byte[] buffer, int offset)
        {
            return (buffer[ offset ] & 0xFF) | (buffer[ offset + 1 ] & 0xFF) << 8 | (buffer[ offset + 2 ] & 0xFF) << 16 | (buffer[ offset + 3 ] & 0xFF) << 24;
        }

    private short readShort(byte[] buffer, int offset)
        {
            return (short) ((buffer[ offset ] & 0xFF) | (buffer[ offset + 1 ] & 0xFF) << 8);
        }

    private boolean readBoolean(byte[] buffer, int offset)
        {
            return buffer[ offset ] == 1;
        }

    private String buildDataToEvent(Set<String> recipients, String event)
    {
        return join(recipients, ",") + "|" + event;
    }

    private String join(Collection<String> collection, String delimiter)
    {
        StringBuilder sb = new StringBuilder();
        for (String item : collection)
        {
            sb.append(item).append(delimiter);
        }
        return sb.substring(0, sb.length() > 0 ? sb.length() - 1 : 0);
    }

    private class EventThread
    {
        private ExecutorService executorService;
        private FutureExecutor futureExecutor;

        private ListenableFuture<Void> run(Callable<Void> callable)
        {
            if (executorService == null || executorService.isShutdown())
            {
                executorService = Executors.newSingleThreadExecutor();
                futureExecutor = new FutureExecutor(executorService);
            }

            return futureExecutor.submit(callable);
        }

        private void stop()
        {
            executorService.shutdown();
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
                        createBuffer();
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

        private void createBuffer()
        {
            buffer = new byte[ 4 + messageLength ];
            writeInt(messageLength, buffer, offset);
            offset += 4;
        }
    }

    private class SocketReceiverThread extends Thread
    {
        private Reader reader = new Reader();
        private byte[] chunk = new byte[ 1024 ];

        @Override
        public void run()
        {
            int bytesRead;
            try
            {
                configureSocket();
                while ((bytesRead = InputStream.read(chunk)) != -1)
                {
                    ArrayList<byte[]> buffers = reader.read(chunk, bytesRead);
                    for (byte[] buffer : buffers)
                    {
                        try
                        {
                            process(deserialize(buffer));
                        }
                        catch (Exception e)
                        {
                            //
                        }
                    }
                }
            }
            catch (IOException e)
            {
                if (!ManuallyClosed)
                {
                    emitter.emit(EVENT_ERROR, e);
                }
            }

            if (!ManuallyClosed)
            {
                emitter.emit(EVENT_CLOSE);
                IsConnected = false;

                if (Options.reconnect)
                {
                    reconnect();
                }
                else
                {
                    EventThreadMain.stop();
                }
            }
        }

        private void configureSocket() throws IOException
        {
            SocketMain.setSoTimeout(Options.timeout);

            if (Options.keepAlive != null)
            {
                SocketMain.setKeepAlive(Options.keepAlive);
            }

            if (Options.noDelay != null)
            {
                SocketMain.setTcpNoDelay(Options.noDelay);
            }
        }

        private void process(Message message) throws UnsupportedEncodingException
        {
            switch (message.MessageType)
            {
                case MT_DATA:
                    emitter.emit(message.Event, getTypedData(message));
                    break;
                case MT_DATA_WITH_ACK:
                    emitter.emit(message.Event, getTypedData(message), createAck(message));
                    break;
                case MT_ACK:
                    if (acks.containsKey(message.ID))
                    {
                        acks.get(message.ID).call(getTypedData(message));
                        acks.remove(message.ID);
                    }
                    break;
                case MT_REGISTER:
                    id = (String) getTypedData(message);
                    emitter.emit(EVENT_CONNECT);
                    break;
                case MT_ERROR:
                    emitter.emit(EVENT_ERROR, new Exception((String) getTypedData(message)));
            }
        }

        private Object getTypedData(Message message) throws UnsupportedEncodingException
        {
            switch (message.DataType)
            {
                case DT_STRING:
                    return new String(message.Data, "UTF-8");
                case DT_BINARY:
                    return message.Data;
                case DT_OBJECT:
                    return Options.objectSerializer.deserialize(message.Data);
                case DT_INTEGER:
                    return readInt48(message.Data, 0);
                case DT_DECIMAL:
                    return readDouble(message.Data, 0);
                case DT_BOOLEAN:
                    return readBoolean(message.Data, 0);
            }

            return null;
        }
    }
}
