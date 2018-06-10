package co.biogram.socket;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

class Serializer {
    static final byte VERSION = 1;

    static final byte DT_STRING = 1;
    static final byte DT_BINARY = 2;
    static final byte DT_INTEGER = 3;
    static final byte DT_DECIMAL = 4;
    static final byte DT_OBJECT = 5;
    static final byte DT_BOOLEAN = 6;
    static final byte DT_EMPTY = 7;

    static final byte MT_ERROR = 0;
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

    static byte[] serialize(byte[] event, byte[] data, byte mt, byte dt, int messageId) {
        short eventLength = (short) event.length;
        int dataLength = data.length;
        int messageLength = 8 + 2 + eventLength + 4 + dataLength;

        byte[] buffer = new byte[4 + messageLength];
        int offset = 0;

        Utils.writeInt(messageLength, buffer, offset);
        offset += 4;

        buffer[offset++] = VERSION;
        buffer[offset++] = 0;
        buffer[offset++] = dt;
        buffer[offset++] = mt;

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

    static Message deserialize(byte[] buffer) throws UnsupportedEncodingException {
        int offset = 4; // Skip message length

        byte version = buffer[offset++];
        if (version != VERSION) {
            return new Message("Serializer version mismatch. Remote " + version + " Local " + VERSION);
        }

        offset++; // Skip unused flags
        byte dt = buffer[offset++];
        byte mt = buffer[offset++];

        int messageId = Utils.readInt(buffer, offset);
        offset += 4;

        short eventLength = Utils.readShort(buffer, offset);
        offset += 2;

        String event = new String(buffer, offset, eventLength, "UTF-8");
        offset += eventLength;

        int dataLength = Utils.readInt(buffer, offset);
        offset += 4;

        byte[] data = Arrays.copyOfRange(buffer, offset, offset + dataLength);

        return new Message(event, data, mt, dt, messageId);
    }
}
