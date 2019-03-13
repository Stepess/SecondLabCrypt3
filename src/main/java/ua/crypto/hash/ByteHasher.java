package ua.crypto.hash;

import ua.crypto.cipher.Misty;
import ua.crypto.util.ShiftUtils;

import java.util.Arrays;

public class ByteHasher {

    public static final int BLOCK_LENGTH_IN_BYTES = 8;
    public static final int BITS_IN_BYTE = 8;

    private Misty misty = new Misty();

    public long hash(byte[] text) {
        System.out.println("Incoming bytes: " + Arrays.toString(text));
        long resultHash = 0L;

        for (long l: padding(text)) {
            //printBytes(l, 16);
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
            //System.out.println(Long.toBinaryString(text[i]));
            //System.out.println((BITS_IN_BYTE * (i % BLOCK_LENGTH_IN_BYTES)));
            padded[i / BLOCK_LENGTH_IN_BYTES] ^=  Byte.toUnsignedLong(text[i]) << (BITS_IN_BYTE * (i % BLOCK_LENGTH_IN_BYTES));
            //System.out.println(Long.toBinaryString(padded[i / BLOCK_LENGTH_IN_BYTES]));
            //System.out.println(i / BLOCK_LENGTH_IN_BYTES);
            //1\System.out.println();
        }

        padded[ShiftUtils.rightShift(text.length, 3)] ^= ShiftUtils.leftShift(1, (text.length << 3) % 63);

        return padded;
    }

    private void printBytes(long num, int radix) {
        for (int i = 0; i < 8; i++) {
            System.out.print(Integer.toString((int) ShiftUtils.rightShift(num, (i * Byte.SIZE)) & 0xFF, radix) + "   ");
        }
        System.out.print('\n');
    }
}
