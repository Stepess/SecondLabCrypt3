package ua.crypto.hash;

import ua.crypto.cipher.IMisty;
import ua.crypto.cipher.Misty;
import ua.crypto.cipher.MistyDebug;
import ua.crypto.util.ShiftUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class ISByteHasher {

    private IMisty misty = new MistyDebug();

    private int BUFFER_SIZE = 1;
    private int BITS_IN_BYTE = 8;
    private int BYTES_TO_READ = 8 * BUFFER_SIZE;

    public long hash(InputStream is) throws IOException {
        byte[] buffer = new byte[BYTES_TO_READ];
        byte[] temp = new byte[8];

        int read;
        long resultHash = 0L;

        while ((read = is.read(buffer)) == BYTES_TO_READ) {
            resultHash = hashFullBlocks(buffer, temp);
        }

        long lastBlock;

        read = read < 0 ? 0 : read;


            int limit = read / 8;
            for (int i=0; i<limit; i++) {
                System.arraycopy(buffer, i * BITS_IN_BYTE, temp, 0, 8);
                resultHash = G(bytesToLong(temp), resultHash);
            }
            System.arraycopy(buffer, limit * BITS_IN_BYTE, temp, 0, read % BITS_IN_BYTE);
            lastBlock = bytesToLong(temp, read % BITS_IN_BYTE);


        return G(lastBlock, resultHash);
    }

    private long hashFullBlocks(byte[] buffer, byte[] temp) {
        long resultHash = 0;

        for (int i=0; i< BUFFER_SIZE; i++) {
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
