package ua.crypto.hash;

import ua.crypto.cipher.IMisty;
import ua.crypto.cipher.Misty;
import ua.crypto.util.ShiftUtils;

import java.io.IOException;
import java.io.InputStream;

public class ISByteHasher {

    private IMisty misty = new Misty();

    public long hash(InputStream is) throws IOException {
        byte[] buff = new byte[8];

        int read;

        long resultHash = 0L;

        while ((read = is.read(buff)) == 8) {
            resultHash = G(bytesToLong(buff), resultHash);
        }

        long lastBlock = bytesToLong(buff, read < 0 ? 0 : read);

        return G(lastBlock, resultHash);
    }

    private long G(long text, long hash) {
        return misty.encrypt(hash, text ^ hash) ^ text ^ hash;
    }

    private long bytesToLong(byte[] bytes) {
        long result = 0;

        for (int i = 0; i < bytes.length; i++) {
            result ^= ShiftUtils.leftShift(Byte.toUnsignedLong(bytes[i]), i * 8);
        }

        return result;
    }

    private long bytesToLong(byte[] bytes, int read) {
        long result = 0;

        for (int i = 0; i < read; i++) {
            result ^= ShiftUtils.leftShift(Byte.toUnsignedLong(bytes[i]), i * 8);
        }

        return result ^ ShiftUtils.leftShift(0x80L, (8 * read));
    }
}
