package co.biogram.socket;

import android.util.Log;

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







    public static final String EVENT_CONNECT = "Connect";
    public static final String EVENT_SOCKET_CONNECT = "socket_connect";
    public static final String EVENT_CLOSE = "close";
    public static final String EVENT_ERROR = "error";
    public static final String EVENT_RECONNECTING = "reconnecting";

    public String id;

    private static final int MAX_MESSAGE_ID = Integer.MAX_VALUE;

    private String host;
    private int port;
    private Options opts;
    private java.net.Socket socket;
    private boolean connected;
    private boolean manuallyClosed;
    private int messageId = 1;
    private BufferedInputStream bufferedInputStream;
    private BufferedOutputStream bufferedOutputStream;
    private EventThread eventThread = new EventThread();
    private LinkedList<byte[]> queue;
    private Emitter emitter = new Emitter();
    private Map<Integer, Emitter.Listener> acks = new HashMap<>();

    public Socket(String host, int port) {
        this(host, port, new Options());
    }

    public Socket(String host, int port, Options opts) {
        this.host = host;
        this.port = port;
        this.opts = opts;

        if (opts.useQueue) {
            queue = new LinkedList<>();
        }
    }

    public void Connect()
    {
        if (connected)
            return;

        manuallyClosed = false;
        eventThread.run(new Callable<Void>()
        {
            @Override
            public Void call() throws Exception
            {
                Log.e("QQ", "connecting to: " + host);
                socket = new java.net.Socket(host, 7000);

                bufferedInputStream = new BufferedInputStream(socket.getInputStream());
                bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
                new SocketReceiverThread().start();
                return null;
            }
        })
        .addCallback(new FutureCallback<Void>()
        {
            @Override
            public void onSuccess(Void result)
            {
                connected = true;Log.e("QQ", "vasl shod");

                try {
                    flushQueue();
                } catch (Exception e) {
                    emitter.emit(EVENT_ERROR, e);
                }

                emitter.emit(EVENT_SOCKET_CONNECT);
            }

            @Override
            public void onFailure(Throwable failure)
            {
                Log.e("QQ", failure.getMessage());
                emitter.emit(EVENT_ERROR, failure);
                emitter.emit(EVENT_CLOSE);

                if (opts.reconnect && !manuallyClosed) {
                    reconnect();
                } else {
                    eventThread.stop();
                }
            }
        });
    }

    public void close() {
        if (!connected) return;

        manuallyClosed = true;
        eventThread.run(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                socket.close();
                return null;
            }
        }).addCallback(new FutureCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                connected = false;

                emitter.emit(EVENT_CLOSE);
                eventThread.stop();
            }

            @Override
            public void onFailure(Throwable failure) {
                emitter.emit(EVENT_ERROR, failure);
            }
        });
    }

    public void emit(String event) {
        send(event, Utils.EMPTY_BYTE_ARRAY, Serializer.MT_DATA, Serializer.DT_EMPTY);
    }

    public void emit(String event, EmitOptions emitOpts) {
        emitTo(event, Utils.EMPTY_BYTE_ARRAY, Serializer.DT_EMPTY, emitOpts);
    }

    public void emit(String event, Emitter.Listener cb) {
        send(event, Utils.EMPTY_BYTE_ARRAY, Serializer.MT_DATA_WITH_ACK, Serializer.DT_EMPTY, cb);
    }

    public void emit(String event, String data) {
        send(event, data.getBytes(), Serializer.MT_DATA, Serializer.DT_STRING);
    }

    public void emit(String event, String data, EmitOptions emitOpts) {
        emitTo(event, data.getBytes(), Serializer.DT_STRING, emitOpts);
    }

    public void emit(String event, String data, Emitter.Listener cb) {
        send(event, data.getBytes(), Serializer.MT_DATA_WITH_ACK, Serializer.DT_STRING, cb);
    }

    public void emit(String event, long data) {
        send(event, Utils.int48ToByteArray(data), Serializer.MT_DATA, Serializer.DT_INTEGER);
    }

    public void emit(String event, long data, EmitOptions emitOpts) {
        emitTo(event, Utils.int48ToByteArray(data), Serializer.DT_INTEGER, emitOpts);
    }

    public void emit(String event, long data, Emitter.Listener cb) {
        send(event, Utils.int48ToByteArray(data), Serializer.MT_DATA_WITH_ACK, Serializer.DT_INTEGER, cb);
    }

    public void emit(String event, double data) {
        send(event, Utils.doubleToByteArray(data), Serializer.MT_DATA, Serializer.DT_DECIMAL);
    }

    public void emit(String event, double data, EmitOptions emitOpts) {
        emitTo(event, Utils.doubleToByteArray(data), Serializer.DT_DECIMAL, emitOpts);
    }

    public void emit(String event, double data, Emitter.Listener cb) {
        send(event, Utils.doubleToByteArray(data), Serializer.MT_DATA_WITH_ACK, Serializer.DT_DECIMAL, cb);
    }

    public void emit(String event, Object data) {
        send(event, opts.objectSerializer.serialize(data), Serializer.MT_DATA, Serializer.DT_OBJECT);
    }

    public void emit(String event, Object data, EmitOptions emitOpts) {
        emitTo(event, opts.objectSerializer.serialize(data), Serializer.DT_OBJECT, emitOpts);
    }

    public void emit(String event, Object data, Emitter.Listener cb) {
        send(event, opts.objectSerializer.serialize(data), Serializer.MT_DATA_WITH_ACK, Serializer.DT_OBJECT, cb);
    }

    public void emit(String event, boolean data) {
        send(event, Utils.booleanToByteArray(data), Serializer.MT_DATA, Serializer.DT_BOOLEAN);
    }

    public void emit(String event, boolean data, EmitOptions emitOpts) {
        emitTo(event, Utils.booleanToByteArray(data), Serializer.DT_BOOLEAN, emitOpts);
    }

    public void emit(String event, boolean data, Emitter.Listener cb) {
        send(event, Utils.booleanToByteArray(data), Serializer.MT_DATA_WITH_ACK, Serializer.DT_BOOLEAN, cb);
    }

    public void emit(String event, byte[] data) {
        send(event, data, Serializer.MT_DATA, Serializer.DT_BINARY);
    }

    public void emit(String event, byte[] data, EmitOptions emitOpts) {
        emitTo(event, data, Serializer.DT_BINARY, emitOpts);
    }

    public void emit(String event, byte[] data, Emitter.Listener cb) {
        send(event, data, Serializer.MT_DATA_WITH_ACK, Serializer.DT_BINARY, cb);
    }

    public void join(String room) {
        send(Utils.EMPTY_STRING, room.getBytes(), Serializer.MT_JOIN_ROOM, Serializer.DT_STRING);
    }

    public void leave(String room) {
        send(Utils.EMPTY_STRING, room.getBytes(), Serializer.MT_LEAVE_ROOM, Serializer.DT_STRING);
    }

    public void leaveAll() {
        send(Utils.EMPTY_STRING, Utils.EMPTY_BYTE_ARRAY, Serializer.MT_LEAVE_ALL_ROOMS, Serializer.DT_STRING);
    }

    public int getSerializerVersion() {
        return Serializer.VERSION;
    }

    public void on(String event, Emitter.Listener fn) {
        emitter.on(event, fn);
    }

    public void once(String event, Emitter.Listener fn) {
        emitter.once(event, fn);
    }

    public void removeListener(String event, Emitter.Listener fn) {
        emitter.removeListener(event, fn);
    }

    public void removeAllListeners(String event) {
        emitter.removeAllListeners(event);
    }

    private void flushQueue() throws IOException {
        if (!opts.useQueue || queue.size() == 0) {
            return;
        }

        for (byte[] message : queue) {
            bufferedOutputStream.write(message);
        }
        bufferedOutputStream.flush();

        queue.clear();
    }

    private void reconnect() {
        eventThread.run(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                Thread.sleep(opts.reconnectInterval);
                emitter.emit(EVENT_RECONNECTING);
                Connect();
                return null;
            }
        });
    }

    private void emitTo(String event, byte[] data, byte dt, EmitOptions emitOpts) {
        if (emitOpts.broadcast) {
            send(event, data, Serializer.MT_DATA_BROADCAST, dt);
        }

        if (emitOpts.socketIds != null && emitOpts.socketIds.size() > 0) {
            send(Utils.buildDataToEvent(emitOpts.socketIds, event), data, Serializer.MT_DATA_TO_SOCKET, dt);
        }

        if (emitOpts.rooms != null && emitOpts.rooms.size() > 0) {
            send(Utils.buildDataToEvent(emitOpts.rooms, event), data, Serializer.MT_DATA_TO_ROOM, dt);
        }
    }

    private void send(String event, byte[] data, byte mt, byte dt) {
        send(event, data, mt, dt, nextMessageId());
    }

    private void send(String event, byte[] data, byte mt, byte dt, Emitter.Listener cb) {
        int messageId = nextMessageId();
        acks.put(messageId, cb);
        send(event, data, mt, dt, messageId);
    }

    private void send(String event, byte[] data, byte mt, byte dt, int messageId) {
        byte[] message = Serializer.serialize(event.getBytes(), data, mt, dt, messageId);

        if (connected) {
            try {
                bufferedOutputStream.write(message);
                bufferedOutputStream.flush();
            } catch (IOException failure) {
                emitter.emit(EVENT_ERROR, failure);
            }
        } else if (opts.useQueue) {
            if (queue.size() + 1 > opts.queueSize) {
                queue.poll();
            }
            queue.offer(message);
        }
    }

    private int nextMessageId() {
        if (++messageId >= MAX_MESSAGE_ID) {
            messageId = 1;
        }

        return messageId;
    }

    private Ack createAck(final Message message) {
        return new Ack() {
            @Override
            public void send(String data) {
                send(data.getBytes(), Serializer.DT_STRING);
            }

            @Override
            public void send(long data) {
                send(Utils.int48ToByteArray(data), Serializer.DT_INTEGER);
            }

            @Override
            public void send(double data) {
                send(Utils.doubleToByteArray(data), Serializer.DT_DECIMAL);
            }

            @Override
            public void send(Object data) {
                send(opts.objectSerializer.serialize(data), Serializer.DT_OBJECT);
            }

            @Override
            public void send(boolean data) {
                send(Utils.booleanToByteArray(data), Serializer.DT_BOOLEAN);
            }

            @Override
            public void send(byte[] data) {
                send(data, Serializer.DT_BINARY);
            }

            @Override
            public void send() {
                send(Utils.EMPTY_BYTE_ARRAY, Serializer.DT_EMPTY);
            }

            private void send(byte[] data, byte dt) {
                Socket.this.send(Utils.EMPTY_STRING, data, Serializer.MT_ACK, dt, message.id);
            }
        };
    }

    public interface ObjectSerializer {
        byte[] serialize(Object object);

        Object deserialize(byte[] data);
    }

    public interface Ack {
        void send(String data);

        void send(long data);

        void send(double data);

        void send(Object data);

        void send(boolean data);

        void send(byte[] data);

        void send();
    }

    public static class EmitOptions {
        private Set<String> socketIds;
        private Set<String> rooms;
        private boolean broadcast;

        public EmitOptions socketIds(Set<String> socketIds) {
            this.socketIds = socketIds;
            return this;
        }

        public EmitOptions rooms(Set<String> rooms) {
            this.rooms = rooms;
            return this;
        }

        public EmitOptions broadcast(boolean broadcast) {
            this.broadcast = broadcast;
            return this;
        }
    }

    public static class Options {
        private boolean reconnect = true;
        private long reconnectInterval = 1000;
        private boolean useQueue = true;
        private int queueSize = 1024;
        private int timeout = 0; // Disabled by default
        private Boolean keepAlive = null;
        private Boolean noDelay = null;
        private ObjectSerializer objectSerializer = new ObjectSerializer() {
            @Override
            public byte[] serialize(Object object) {
                throw new RuntimeException("objectSerializer not provided, use Socket.Options");
            }

            @Override
            public Object deserialize(byte[] data) {
                throw new RuntimeException("objectSerializer not provided, use Socket.Options");
            }
        };

        public Options reconnect(boolean reconnect) {
            this.reconnect = reconnect;
            return this;
        }

        public Options reconnectInterval(long reconnectInterval) {
            this.reconnectInterval = reconnectInterval;
            return this;
        }

        public Options useQueue(boolean useQueue) {
            this.useQueue = useQueue;
            return this;
        }

        public Options queueSize(int queueSize) {
            this.queueSize = queueSize;
            return this;
        }

        public Options timeout(int timeout) {
            this.timeout = timeout;
            return this;
        }

        public Options keepAlive(boolean enabled) {
            this.keepAlive = enabled;
            return this;
        }

        public Options noDelay(boolean enabled) {
            this.noDelay = enabled;
            return this;
        }

        public Options objectSerializer(ObjectSerializer objectSerializer) {
            this.objectSerializer = objectSerializer;
            return this;
        }
    }

    private class EventThread {
        private ExecutorService executorService;
        private FutureExecutor futureExecutor;

        private ListenableFuture<Void> run(Callable<Void> callable) {
            if (executorService == null || executorService.isShutdown()) {
                executorService = Executors.newSingleThreadExecutor();
                futureExecutor = new FutureExecutor(executorService);
            }

            return futureExecutor.submit(callable);
        }

        private void stop() {
            executorService.shutdown();
        }
    }

    private class SocketReceiverThread extends Thread {
        private Reader reader = new Reader();
        private byte[] chunk = new byte[1024];

        @Override
        public void run() {
            int bytesRead;
            try {
                configureSocket();
                while ((bytesRead = bufferedInputStream.read(chunk)) != -1) {
                    ArrayList<byte[]> buffers = reader.read(chunk, bytesRead);
                    for (byte[] buffer : buffers) {
                        process(Serializer.deserialize(buffer));
                    }
                }
            } catch (IOException e) {
                if (!manuallyClosed) {
                    emitter.emit(EVENT_ERROR, e);
                }
            }

            if (!manuallyClosed) {
                emitter.emit(EVENT_CLOSE);
                connected = false;

                if (opts.reconnect) {
                    reconnect();
                } else {
                    eventThread.stop();
                }
            }
        }

        private void configureSocket() throws IOException {
            socket.setSoTimeout(opts.timeout);

            if (opts.keepAlive != null) {
                socket.setKeepAlive(opts.keepAlive);
            }

            if (opts.noDelay != null) {
                socket.setTcpNoDelay(opts.noDelay);
            }
        }

        private void process(Message message) throws UnsupportedEncodingException {
            switch (message.mt) {
                case Serializer.MT_DATA:
                    emitter.emit(message.event, getTypedData(message));
                    break;
                case Serializer.MT_DATA_WITH_ACK:
                    emitter.emit(message.event, getTypedData(message), createAck(message));
                    break;
                case Serializer.MT_ACK:
                    if (acks.containsKey(message.id)) {
                        acks.get(message.id).call(getTypedData(message));
                        acks.remove(message.id);
                    }
                    break;
                case Serializer.MT_REGISTER:
                    id = (String) getTypedData(message);
                    emitter.emit(EVENT_CONNECT);
                    break;
                case Serializer.MT_ERROR:
                    emitter.emit(EVENT_ERROR, new Exception((String) getTypedData(message)));
            }
        }

        private Object getTypedData(Message message) throws UnsupportedEncodingException {
            switch (message.dt) {
                case Serializer.DT_STRING:
                    return new String(message.data, "UTF-8");
                case Serializer.DT_BINARY:
                    return message.data;
                case Serializer.DT_OBJECT:
                    return opts.objectSerializer.deserialize(message.data);
                case Serializer.DT_INTEGER:
                    return Utils.readInt48(message.data, 0);
                case Serializer.DT_DECIMAL:
                    return Utils.readDouble(message.data, 0);
                case Serializer.DT_BOOLEAN:
                    return Utils.readBoolean(message.data, 0);
            }

            return null;
        }
    }
}
