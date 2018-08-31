package co.biogram.main.handler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

interface FutureCallback<V>
{
    void onSuccess(V result);

    void onFailure(Throwable failure);
}

public class SocketHandler
{

    public static final String EVENT_CONNECT = "connect";
    public static final String EVENT_SOCKET_CONNECT = "socket_connect";
    public static final String EVENT_END = "end";
    public static final String EVENT_CLOSE = "close";
    public static final String EVENT_ERROR = "error";
    public static final String EVENT_RECONNECTING = "reconnecting";

    private static final int MAX_MESSAGE_ID = Integer.MAX_VALUE;
    public String id;
    private String host;
    private int port;
    private Opts opts;
    private java.net.Socket socket;
    private boolean connected;
    private boolean manuallyClosed;
    private int messageId = 1;
    private BufferedInputStream bufferedInputStream;
    private BufferedOutputStream bufferedOutputStream;
    private Emitter emitter = new Emitter();
    private EventThread eventThread = new EventThread();
    private LinkedList<byte[]> queue;
    private Map<Integer, Callback> acks = new HashMap<>();

    public SocketHandler(String host, int port)
    {
        this(host, port, new Opts());
    }

    public SocketHandler(String host, int port, Opts opts)
    {
        this.host = host;
        this.port = port;
        this.opts = opts;

        if (opts.useQueue)
        {
            queue = new LinkedList<>();
        }
    }
    private static boolean sameAs(Listener fn, Listener internal)
    {
        if (fn.equals(internal))
        {
            return true;
        }
        else if (internal instanceof Emitter.OnceListener)
        {
            return fn.equals(((Emitter.OnceListener) internal).fn);
        }
        else
        {
            return false;
        }
    }
    public void connect()
    {
        if (connected)
            return;

        manuallyClosed = false;
        eventThread.run(new Callable<Void>()
        {
            @Override
            public Void call() throws Exception
            {
                socket = new java.net.Socket();
                socket.connect(new InetSocketAddress(host, port), opts.timeout);
                bufferedInputStream = new BufferedInputStream(socket.getInputStream());
                bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
                new SocketReceiverThread().start();
                return null;
            }
        }).addCallback(new FutureCallback<Void>()
        {
            @Override
            public void onSuccess(Void result)
            {
                connected = true;

                try
                {
                    flushQueue();
                }
                catch (Exception e)
                {
                    emitter.emit(EVENT_ERROR, e);
                }

                emitter.emit(EVENT_SOCKET_CONNECT);
            }

            @Override
            public void onFailure(Throwable failure)
            {
                emitter.emit(EVENT_ERROR, failure);
                emitter.emit(EVENT_CLOSE);

                if (opts.reconnect && !manuallyClosed)
                {
                    reconnect();
                }
                else
                {
                    eventThread.stop();
                }
            }
        });
    }
    public void end()
    {
        destroy();
    }
    public void destroy()
    {
        if (!connected)
            return;

        manuallyClosed = true;
        eventThread.run(new Callable<Void>()
        {
            @Override
            public Void call() throws Exception
            {
                socket.close();
                return null;
            }
        }).addCallback(new FutureCallback<Void>()
        {
            @Override
            public void onSuccess(Void result)
            {
                emitter.emit(EVENT_END);
                emitter.emit(EVENT_CLOSE);

                eventThread.stop();
            }

            @Override
            public void onFailure(Throwable failure)
            {
                emitter.emit(EVENT_ERROR, failure);
            }
        });
    }
    public void emit(String event, String data) throws IOException
    {
        send(event, data.getBytes(), Serializer.MT_DATA, Serializer.DT_STRING);
    }
    public void emit(String event, String data, EmitOpts emitOpts) throws IOException
    {
        emitTo(event, data.getBytes(), Serializer.DT_STRING, emitOpts);
    }
    public void emit(String event, String data, Callback cb)
    {
        send(event, data.getBytes(), Serializer.MT_DATA_WITH_ACK, Serializer.DT_STRING, cb);
    }
    public void emit(String event, long data) throws IOException
    {
        send(event, Utils.int48ToByteArray(data), Serializer.MT_DATA, Serializer.DT_INT);
    }
    public void emit(String event, long data, EmitOpts emitOpts) throws IOException
    {
        emitTo(event, Utils.int48ToByteArray(data), Serializer.DT_INT, emitOpts);
    }
    public void emit(String event, long data, Callback cb) {
        send(event, Utils.int48ToByteArray(data), Serializer.MT_DATA_WITH_ACK, Serializer.DT_INT, cb);
    }
    public void emit(String event, double data) throws IOException
    {
        send(event, Utils.doubleToByteArray(data), Serializer.MT_DATA, Serializer.DT_DOUBLE);
    }
    public void emit(String event, double data, EmitOpts emitOpts) throws IOException
    {
        emitTo(event, Utils.doubleToByteArray(data), Serializer.DT_DOUBLE, emitOpts);
    }
    public void emit(String event, double data, Callback cb) {
        send(event, Utils.doubleToByteArray(data), Serializer.MT_DATA_WITH_ACK, Serializer.DT_DOUBLE, cb);
    }
    public void emit(String event, JSONObject data) throws IOException
    {
        send(event, data.toString().getBytes(), Serializer.MT_DATA, Serializer.DT_OBJECT);
    }
    public void emit(String event, JSONObject data, EmitOpts emitOpts) throws IOException
    {
        emitTo(event, data.toString().getBytes(), Serializer.DT_OBJECT, emitOpts);
    }
    public void emit(String event, JSONObject data, Callback cb) {
        send(event, data.toString().getBytes(), Serializer.MT_DATA_WITH_ACK, Serializer.DT_OBJECT, cb);
    }
    public void emit(String event, JSONArray data) throws IOException
    {
        send(event, data.toString().getBytes(), Serializer.MT_DATA, Serializer.DT_OBJECT);
    }
    public void emit(String event, JSONArray data, EmitOpts emitOpts) throws IOException
    {
        emitTo(event, data.toString().getBytes(), Serializer.DT_OBJECT, emitOpts);
    }
    public void emit(String event, JSONArray data, Callback cb) {
        send(event, data.toString().getBytes(), Serializer.MT_DATA_WITH_ACK, Serializer.DT_OBJECT, cb);
    }
    public void emit(String event, byte[] data) throws IOException
    {
        send(event, data, Serializer.MT_DATA, Serializer.DT_BUFFER);
    }
    public void emit(String event, byte[] data, EmitOpts emitOpts) throws IOException
    {
        emitTo(event, data, Serializer.DT_BUFFER, emitOpts);
    }
    public void emit(String event, byte[] data, Callback cb) {
        send(event, data, Serializer.MT_DATA_WITH_ACK, Serializer.DT_BUFFER, cb);
    }
    public void join(String room) throws IOException
    {
        send("", room.getBytes(), Serializer.MT_JOIN_ROOM, Serializer.DT_STRING);
    }
    public void leave(String room) throws IOException
    {
        send("", room.getBytes(), Serializer.MT_LEAVE_ROOM, Serializer.DT_STRING);
    }
    public void leaveAll() throws IOException
    {
        send("", new byte[ 0 ], Serializer.MT_LEAVE_ALL_ROOMS, Serializer.DT_STRING);
    }
    public int getVersion()
    {
        return Serializer.VERSION;
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
    private void flushQueue() throws IOException
    {
        if (!opts.useQueue || queue.size() == 0)
        {
            return;
        }

        for (byte[] data : queue)
        {
            bufferedOutputStream.write(data);
        }
        bufferedOutputStream.flush();

        queue.clear();
    }

    private void reconnect()
    {
        eventThread.run(new Callable<Void>()
        {
            @Override
            public Void call() throws Exception
            {
                Thread.sleep(opts.reconnectInterval);
                emitter.emit(EVENT_RECONNECTING);
                connect();
                return null;
            }
        });
    }

    private void emitTo(String event, byte[] data, byte dt, EmitOpts emitOpts) throws IOException
    {
        if (emitOpts.broadcast)
        {
            send(event, data, Serializer.MT_DATA_BROADCAST, dt);
        }

        if (emitOpts.socketIds != null && emitOpts.socketIds.size() > 0)
        {
            send(Utils.join(emitOpts.socketIds, ",") + ":" + event, data, Serializer.MT_DATA_TO_SOCKET, dt);
        }

        if (emitOpts.rooms != null && emitOpts.rooms.size() > 0)
        {
            send(Utils.join(emitOpts.rooms, ",") + ":" + event, data, Serializer.MT_DATA_TO_ROOM, dt);
        }
    }

    private void send(String event, byte[] data, byte mt, byte dt) throws IOException
    {
        send(event, data, mt, dt, -1, null);
    }

    private void send(String event, byte[] data, byte mt, byte dt, int messageId) throws IOException
    {
        send(event, data, mt, dt, messageId, null);
    }

    private void send(String event, byte[] data, byte mt, byte dt, Callback cb)
    {
        try
        {
            send(event, data, mt, dt, -1, cb);
        }
        catch (IOException e)
        {
            Misc.Debug("ee: " + e.getMessage());
        }
    }

    private void send(String event, byte[] data, byte mt, byte dt, int messageId, Callback cb) throws IOException
    {
        messageId = messageId > 0 ? messageId : nextMessageId();

        if (cb != null)
        {
            acks.put(messageId, cb);

        }

        byte[] message = Serializer.serialize(event.getBytes(), data, mt, dt, messageId);

        if (connected)
        {
            bufferedOutputStream.write(message);
            bufferedOutputStream.flush();
        }
        else if (opts.useQueue)
        {
            if (queue.size() + 1 > opts.queueSize)
            {
                queue.poll();
            }
            queue.offer(message);
        }
    }

    private int nextMessageId()
    {
        if (++messageId >= MAX_MESSAGE_ID)
        {
            messageId = 1;
        }

        return messageId;
    }

    private Ack createAck(final Message message)
    {
        return new Ack()
        {
            @Override
            public void send(String data)
            {
                send(data.getBytes(), Serializer.DT_STRING);
            }

            @Override
            public void send(long data)
            {
                send(Utils.int48ToByteArray(data), Serializer.DT_INT);
            }

            @Override
            public void send(double data)
            {
                send(Utils.doubleToByteArray(data), Serializer.DT_DOUBLE);
            }

            @Override
            public void send(JSONObject data)
            {
                send(data.toString().getBytes(), Serializer.DT_OBJECT);
            }

            @Override
            public void send(JSONArray data)
            {
                send(data.toString().getBytes(), Serializer.DT_OBJECT);
            }

            @Override
            public void send(byte[] data)
            {
                send(data, Serializer.DT_BUFFER);
            }

            private void send(byte[] data, byte dt)
            {
                try
                {
                    SocketHandler.this.send("", data, Serializer.MT_ACK, dt, message.messageId);
                }
                catch (IOException e)
                {
                    emitter.emit(EVENT_ERROR, e);
                }
            }
        };
    }

    public interface Listener
    {
        void call(Object... args);
    }

    public interface Ack
    {
        void send(String data);

        void send(long data);

        void send(double data);

        void send(JSONObject data);

        void send(JSONArray data);

        void send(byte[] data);
    }

    public interface Callback
    {
        void call(Object data);
    }

    public static class EmitOpts
    {
        private List<String> socketIds;
        private List<String> rooms;
        private boolean broadcast;

        public EmitOpts socketIds(List<String> socketIds)
        {
            this.socketIds = socketIds;
            return this;
        }

        public EmitOpts rooms(List<String> rooms)
        {
            this.rooms = rooms;
            return this;
        }

        public EmitOpts broadcast(boolean broadcast)
        {
            this.broadcast = broadcast;
            return this;
        }
    }

    public static class Opts
    {
        private boolean reconnect = true;
        private long reconnectInterval = 1000;
        private boolean useQueue = true;
        private int queueSize = 1024;
        private int timeout = 0; // Disabled by default

        public Opts reconnect(boolean reconnect)
        {
            this.reconnect = reconnect;
            return this;
        }

        public Opts reconnectInterval(long reconnectInterval)
        {
            this.reconnectInterval = reconnectInterval;
            return this;
        }

        public Opts useQueue(boolean useQueue)
        {
            this.useQueue = useQueue;
            return this;
        }

        public Opts queueSize(int queueSize)
        {
            this.queueSize = queueSize;
            return this;
        }

        public Opts timeout(int timeout)
        {
            this.timeout = timeout;
            return this;
        }
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
                while ((bytesRead = bufferedInputStream.read(chunk)) != -1)
                {
                    ArrayList<byte[]> buffers = reader.read(chunk, bytesRead);
                    for (byte[] buffer : buffers)
                    {
                        try
                        {
                            process(Serializer.deserialize(buffer));
                        }
                        catch (Exception e)
                        {
                            Misc.Debug("Ww: " + e.getMessage());
                        }
                    }
                }

            }
            catch (IOException e)
            {
                emitter.emit(EVENT_ERROR, e);
            }

            if (opts.reconnect && !manuallyClosed)
            {
                reconnect();
            }
            else
            {
                eventThread.stop();
            }

            emitter.emit(EVENT_END);
            emitter.emit(EVENT_CLOSE);

            connected = false;
        }

        private void process(final Message message)
        {
            switch (message.mt)
            {
                case Serializer.MT_DATA:
                    emitter.emit(message.event, message.data);
                    break;
                case Serializer.MT_DATA_WITH_ACK:
                    emitter.emit(message.event, message.data, createAck(message));
                    break;
                case Serializer.MT_ACK:
                    if (acks.containsKey(message.messageId))
                    {
                        acks.get(message.messageId).call(message.data);
                        acks.remove(message.messageId);
                    }
                    break;
                case Serializer.MT_REGISTER:
                    id = (String) message.data;
                    emitter.emit(EVENT_CONNECT);
                    break;
                default:
                    throw new RuntimeException("Not implemented message type " + message.mt);
            }
        }
    }

    class Emitter
    {
        private Map<String, LinkedList<Listener>> callbacks = new HashMap<>();

        Emitter()
        {
        }



        Emitter on(String event, Listener fn)
        {
            LinkedList<Listener> callbacks = this.callbacks.get(event);
            if (callbacks == null)
            {
                callbacks = new LinkedList<>();
            }
            callbacks.add(fn);
            return this;
        }

        Emitter once(final String event, final Listener fn)
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

        List<Listener> listeners(String event)
        {
            LinkedList<Listener> callbacks = this.callbacks.get(event);
            return callbacks != null ? new ArrayList<Listener>(callbacks) : new ArrayList<Listener>(0);
        }

        boolean hasListeners(String event)
        {
            LinkedList<Listener> callbacks = this.callbacks.get(event);
            return callbacks != null && !callbacks.isEmpty();
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
}

class Serializer
{
    static final byte VERSION = 1;

    static final byte DT_STRING = 1;
    static final byte DT_BUFFER = 2;
    static final byte DT_INT = 3;
    static final byte DT_DOUBLE = 4;
    static final byte DT_OBJECT = 5;

    static final byte MT_REGISTER = 1;
    static final byte MT_DATA = 2;
    static final byte MT_DATA_TO_SOCKET = 3;
    static final byte MT_DATA_TO_ROOM = 4;
    static final byte MT_DATA_BROADCAST = 5;
    static final byte MT_DATA_WITH_ACK = 6;
    static final byte MT_ACK = 7;
    static final byte MT_JOIN_ROOM = 8;
    static final byte MT_LEAVE_ROOM = 9;
    static final byte MT_LEAVE_ALL_ROOMS = 10;

    static byte[] serialize(byte[] event, byte[] data, byte mt, byte dt, int messageId)
    {
        short eventLength = (short) event.length;
        int dataLength = data.length;
        int messageLength = 4 + 1 + 1 + 4 + 2 + eventLength + 4 + dataLength;

        byte[] buffer = new byte[ 4 + messageLength ];
        int offset = 0;

        Utils.writeInt(messageLength, buffer, offset);
        offset += 4;

        buffer[ offset++ ] = dt;
        buffer[ offset++ ] = mt;

        Utils.writeInt(messageId, buffer, offset);
        offset += 4;

        Utils.writeShort(eventLength, buffer, offset);
        offset += 2;

        System.arraycopy(event, 0, buffer, offset, eventLength);
        offset += eventLength;

        Utils.writeInt(dataLength, buffer, offset);
        offset += 4;

        System.arraycopy(data, 0, buffer, offset, dataLength);

        return buffer;
    }

    static Message deserialize(byte[] buffer) throws Exception
    {
        int offset = 4;

        byte dt = buffer[ offset++ ];
        byte mt = buffer[ offset++ ];

        int messageId = Utils.readInt(buffer, offset);
        offset += 4;

        short eventLength = Utils.readShort(buffer, offset);
        offset += 2;

        byte[] eventBytes = Arrays.copyOfRange(buffer, offset, offset + eventLength);
        String event = new String(eventBytes, "UTF-8");
        offset += eventLength;

        int dataLength = Utils.readInt(buffer, offset);
        offset += 4;

        switch (dt)
        {
            case DT_STRING:
                byte[] dataBytes = Arrays.copyOfRange(buffer, offset, offset + dataLength);
                return new Message(event, new String(dataBytes, "UTF-8"), messageId, mt, dt);
            case DT_BUFFER:
                byte[] dataBytes2 = Arrays.copyOfRange(buffer, offset, offset + dataLength);
                return new Message(event, dataBytes2, messageId, mt, dt);
            case DT_OBJECT:
                byte[] dataBytes3 = Arrays.copyOfRange(buffer, offset, offset + dataLength);

                if (Utils.isJsonObject(dataBytes3))
                {
                    return new Message(event, new JSONObject(new String(dataBytes3, "UTF-8")), messageId, mt, dt);
                }
                else if (Utils.isJsonArray(dataBytes3))
                {
                    return new Message(event, new JSONArray(new String(dataBytes3, "UTF-8")), messageId, mt, dt);
                }
                else
                {
                    throw new Exception(new Error("Invalid object"));
                }
            case DT_INT:
                return new Message(event, Utils.readInt48(buffer, offset), messageId, mt, dt);
            case DT_DOUBLE:
                return new Message(event, Utils.readDouble(buffer, offset), messageId, mt, dt);
            default:
                throw new Exception(new Error("Invalid data type: " + dt));
        }
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
        Utils.writeInt48(messageLength, buffer, offset);
        offset += 4;
    }
}

class Message
{
    final String event;
    final Object data;
    final int messageId;
    final byte mt;
    final byte dt;

    Message(String event, Object data, int messageId, byte mt, byte dt)
    {
        this.event = event;
        this.data = data;
        this.messageId = messageId;
        this.mt = mt;
        this.dt = dt;
    }
}

class ListenableFuture<V>
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

    public void setResult(V result)
    {
        this.result = result;
        isCompleted = true;
        resolve();
    }

    public void setFailure(Throwable failure)
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

class FutureExecutor
{
    private ExecutorService executor;

    public FutureExecutor(ExecutorService executor)
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

class Utils
{
    private static final byte CHAR_CODE_OPEN_BRACKET = 91;
    private static final byte CHAR_CODE_OPEN_BRACE = 123;

    static byte[] int48ToByteArray(long value)
    {
        return new byte[] { (byte) value, (byte) (value >> 8), (byte) (value >> 16), (byte) (value >> 24), (byte) (value >> 32), (byte) (value >> 40) };
    }

    static byte[] longToByteArray(long value)
    {
        return new byte[] { (byte) value, (byte) (value >> 8), (byte) (value >> 16), (byte) (value >> 24), (byte) (value >> 32), (byte) (value >> 40), (byte) (value >> 48), (byte) (value >> 56) };
    }

    static byte[] doubleToByteArray(double value)
    {
        return longToByteArray(Double.doubleToRawLongBits(value));
    }

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

    static long readLong(byte[] buffer, int offset)
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

    static String byteArrayToLiteralString(byte[] array)
    {
        StringBuilder sb = new StringBuilder("[ ");
        for (byte bytee : array)
        {
            sb.append(Integer.toString(bytee & 0xFF, 16));
            sb.append(" ");
        }
        sb.append("]");
        return sb.toString();
    }

    static String join(List<String> list, String delimiter)
    {
        StringBuilder sb = new StringBuilder();
        int size = list.size();
        for (int i = 0; i < size; i++)
        {
            sb.append(list.get(i)).append(i < size - 1 ? delimiter : "");
        }
        return sb.toString();
    }
}
