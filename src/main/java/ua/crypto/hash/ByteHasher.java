package ua.crypto.hash;

import ua.crypto.cipher.Misty;
import ua.crypto.util.ShiftUtils;

public class ByteHasher {

    public static final int BLOCK_LENGTH_IN_BYTES = 8;
    public static final int BITS_IN_BYTE = 8;

    private Misty misty = new Misty();

    public long hash(byte[] text) {
        long resultHash = 0L;

        for (long l: padding(text)) {
            resultHash = G(l, resultHash);
        }

        return resultHash;
    }

    private long G(long text, long hash) {
        return misty.encrypt(hash, text ^ hash) ^ text ^ hash;
    }

    private int getLengthWithPadding(int initialLength) {
        int missing = ++initialLength % (BLOCK_LENGTH_IN_BYTES);
        return missing == 0 ? initialLength / BLOCK_LENGTH_IN_BYTES : (initialLength + BLOCK_LENGTH_IN_BYTES - missing) / BLOCK_LENGTH_IN_BYTES;
    }

    private long[] padding(byte[] text) {
        long[] padded = new long[getLengthWithPadding(text.length)];

        for (int i = 0; i < text.length; i++) {
            padded[i / BLOCK_LENGTH_IN_BYTES] ^= text[i] << (BITS_IN_BYTE * (i % BLOCK_LENGTH_IN_BYTES));
        }

        padded[ShiftUtils.rightShift(text.length, 3)] ^= ShiftUtils.leftShift(1, (text.length << 3) % 63);

        return padded;
    }
}
