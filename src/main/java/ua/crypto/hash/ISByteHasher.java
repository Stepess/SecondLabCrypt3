package ua.crypto.hash;

import ua.crypto.cipher.IMisty;
import ua.crypto.cipher.Misty;
import ua.crypto.util.ShiftUtils;

import java.io.IOException;
import java.io.InputStream;

public class ISByteHasher {

    private IMisty misty = new Misty();

    private int BUFFER_SIZE;
    private int BYTES_TO_READ;
    private static final int BITS_IN_BYTE = 8;

    public ISByteHasher(int bufferSize) {
        this.BUFFER_SIZE = bufferSize;
        this.BYTES_TO_READ = BITS_IN_BYTE * BUFFER_SIZE;
    }

    public long hash(InputStream is) throws IOException {
        byte[] buffer = new byte[BYTES_TO_READ];
        byte[] temp = new byte[8];

        long resultHash = 0L;
        int amountOfRemainedBytes;

        while ((amountOfRemainedBytes = is.read(buffer)) == BYTES_TO_READ) {
            for (int i = 0; i < BUFFER_SIZE; i++) {
                System.arraycopy(buffer, i * BITS_IN_BYTE, temp, 0, BITS_IN_BYTE);
                resultHash = G(bytesToLong(temp), resultHash);
            }
        }

        long lastBlock;

        amountOfRemainedBytes = amountOfRemainedBytes < 0 ? 0 : amountOfRemainedBytes;


        int limit = amountOfRemainedBytes / BITS_IN_BYTE;
        for (int i = 0; i < limit; i++) {
            System.arraycopy(buffer, i * BITS_IN_BYTE, temp, 0, BITS_IN_BYTE);
            resultHash = G(bytesToLong(temp), resultHash);
        }
        System.arraycopy(buffer, limit * BITS_IN_BYTE, temp, 0, amountOfRemainedBytes % BITS_IN_BYTE);
        lastBlock = bytesToLong(temp, amountOfRemainedBytes % BITS_IN_BYTE);


        return G(lastBlock, resultHash);
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
