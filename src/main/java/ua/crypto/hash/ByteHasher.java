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
            resultHash = G(l, resultHash);
        }

        return resultHash;
    }

    private long G(long text, long hash) {

        long e = misty.encrypt(hash, text ^ hash);

        System.out.println("DEBUG: check for equality H = E_K(A) xor B");
        System.out.println("DEBUG: after EncryptBlock: A = " + Long.toUnsignedString(text ^ hash, 16));
        System.out.println("DEBUG: after EncryptBlock: K = " + Long.toUnsignedString(hash, 16));
        System.out.println("DEBUG: after EncryptBlock: E = " + Long.toUnsignedString(e, 16));
        System.out.println("DEBUG: after EncryptBlock: B = " + Long.toUnsignedString(text ^ hash, 16));
        System.out.println("DEBUG: after EncryptBlock: H = " + Long.toUnsignedString(e ^ text ^ hash, 16));
        System.out.println();
        return e ^ text ^ hash;
    }

    private long GLikeInExample(long text, long hash) {

        long a = Long.reverseBytes(text ^ hash);
        long k = Long.reverseBytes(hash);
        long e = Long.reverseBytes(misty.encrypt(hash, text ^ hash));

        System.out.println("DEBUG: check for equality H = E_K(A) xor B");
        System.out.println("DEBUG: after EncryptBlock: A = " + Long.toUnsignedString(a, 16));
        System.out.println("DEBUG: after EncryptBlock: K = " + Long.toUnsignedString(k, 16));
        System.out.println("DEBUG: after EncryptBlock: E = " + Long.toUnsignedString(e, 16));
        System.out.println("DEBUG: after EncryptBlock: B = " + Long.toUnsignedString(a, 16));
        System.out.println("DEBUG: after EncryptBlock: H = " + Long.toUnsignedString(e ^ a, 16));
        System.out.println();
        return e ^ a;
    }

    private int getLengthWithPadding(int initialLength) {
        int missing = ++initialLength % (BLOCK_LENGTH_IN_BYTES);
        return missing == 0 ? initialLength / BLOCK_LENGTH_IN_BYTES : (initialLength + BLOCK_LENGTH_IN_BYTES - missing) / BLOCK_LENGTH_IN_BYTES;
    }

    private long[] padding(byte[] text) {
        long[] padded = new long[getLengthWithPadding(text.length)];


        for (int i = 0; i < text.length; i++) {
            padded[i / BLOCK_LENGTH_IN_BYTES] ^=  Byte.toUnsignedLong(text[i]) << (BITS_IN_BYTE * (i % BLOCK_LENGTH_IN_BYTES));
        }

        padded[ShiftUtils.rightShift(text.length, 3)] ^= ShiftUtils.leftShift(0x80L, 8*(text.length%8) );

        return padded;
    }

    private long setHighestOneBit(long num, int byteLength) {
        int leftByte = (byteLength << 3) & 63;
        while ((num & ShiftUtils.leftShift(1L, leftByte)) == 0) {
            leftByte--;
        }
        //return num ^ ShiftUtils.leftShift(512L, ++leftByte); text.txt
        return num ^ ShiftUtils.leftShift(0x80L, ++leftByte);
    }

    private void printBytes(long num, int radix) {
        for (int i = 0; i < 8; i++) {
            System.out.print(Integer.toString((int) ShiftUtils.rightShift(num, (i * Byte.SIZE)) & 0xFF, radix) + "   ");
        }
        System.out.print('\n');
    }
}
