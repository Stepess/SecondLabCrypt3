package ua.crypto.hash;

import ua.crypto.cipher.IMisty;
import ua.crypto.cipher.Misty;
import ua.crypto.util.ShiftUtils;

import java.io.IOException;
import java.io.InputStream;

public class ISByteHasher {

    private IMisty misty = new Misty();

    private int BUFFER_SIZE;
    private static final int BITS_IN_BYTE = 8;
    private int BYTES_TO_READ = BITS_IN_BYTE * BUFFER_SIZE;

    public ISByteHasher(int bufferSize) {
        this.BUFFER_SIZE = bufferSize;
    }

    public long hash(InputStream is) throws IOException {
        byte[] buffer = new byte[BYTES_TO_READ];
        byte[] temp = new byte[8];

        int read;
        long resultHash = 0L;

        while ((read = is.read(buffer)) == BYTES_TO_READ) {
            resultHash = hashFullBlocks(buffer, temp, resultHash, BUFFER_SIZE);
        }

        read = read < 0 ? 0 : read;
        int amountOfFullBlocks = read / BITS_IN_BYTE;
        int restOfBytes = read % BITS_IN_BYTE;

        resultHash = hashFullBlocks(buffer, temp, resultHash, amountOfFullBlocks);

        long lastBlock = getLastBlock(buffer, temp, restOfBytes, amountOfFullBlocks);

        return G(lastBlock, resultHash);
    }

    private long getLastBlock(byte[] buffer, byte[] temp, int read, int limit) {
        System.arraycopy(buffer, limit * BITS_IN_BYTE, temp, 0, read);
        return bytesToLong(temp, read);
    }

    private long hashFullBlocks(byte[] buffer, byte[] temp, long prevHash, int limit) {
        long resultHash = prevHash;

        for (int i = 0; i < limit; i++) {
            System.arraycopy(buffer, i * BITS_IN_BYTE, temp, 0, 8);
            resultHash = G(bytesToLong(temp), resultHash);
        }

        return resultHash;
    }

    private long G(long text, long hash) {
        return misty.encrypt(hash, text ^ hash) ^ text ^ hash;
    }

    private long bytesToLong(byte[] bytes) {
        long result = 0;

        for (int i = 0; i < bytes.length; i++) {
            result ^= ShiftUtils.leftShift(Byte.toUnsignedLong(bytes[i]), i * BITS_IN_BYTE);
        }

        return result;
    }

    private long bytesToLong(byte[] bytes, int read) {
        long result = 0;

        for (int i = 0; i < read; i++) {
            result ^= ShiftUtils.leftShift(Byte.toUnsignedLong(bytes[i]), i * BITS_IN_BYTE);
        }

        return result ^ ShiftUtils.leftShift(0x80L, (BITS_IN_BYTE * read));
    }
}
