package co.biogram.socket;

import java.util.ArrayList;

class Reader {
    private byte[] buffer;
    private int offset;
    private int bytesRead;
    private int messageLength;

    private int offsetChunk;

    ArrayList<byte[]> read(byte[] chunk, int effectiveChunkLength) {
        offsetChunk = 0;
        ArrayList<byte[]> buffers = new ArrayList<>();

        while (offsetChunk < effectiveChunkLength) {
            if (bytesRead < 4) {
                if (readMessageLength(chunk, effectiveChunkLength)) {
                    createBuffer();
                } else {
                    break;
                }
            }

            if (bytesRead < buffer.length && !readMessageContent(chunk, effectiveChunkLength)) {
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

    private boolean readMessageLength(byte[] chunk, int effectiveChunkLength) {
        for (; offsetChunk < effectiveChunkLength && bytesRead < 4; offsetChunk++, bytesRead++) {
            messageLength |= chunk[offsetChunk] << (bytesRead * 8);
        }

        return bytesRead == 4;
    }

    private boolean readMessageContent(byte[] chunk, int effectiveChunkLength) {
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

    private void createBuffer() {
        buffer = new byte[4 + messageLength];
        Utils.writeInt(messageLength, buffer, offset);
        offset += 4;
    }
}
