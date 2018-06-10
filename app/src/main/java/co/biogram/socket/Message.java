package co.biogram.socket;

class Message {
    String event;
    byte[] data;
    int id;
    byte mt;
    byte dt;

    Message(String data) {
        this(null, data.getBytes(), Serializer.MT_ERROR, Serializer.DT_STRING, 0);
    }

    Message(String event, byte[] data, byte mt, byte dt, int id) {
        this.event = event;
        this.data = data;
        this.id = id;
        this.mt = mt;
        this.dt = dt;
    }
}
