package ua.crypto.hash;

import ua.crypto.cipher.Misty;
import ua.crypto.util.ShiftUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class ISByteHasher {


    public static final int BLOCK_LENGTH_IN_BYTES = 8;
    public static final int BITS_IN_BYTE = 8;

    private Misty misty = new Misty();

    public long hash(long num, long hash) {
        return G(num, hash);
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
        //return misty.encrypt(hash, text ^ hash) ^ text ^ hash;
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
        //return misty.encrypt(hash, text ^ hash) ^ text ^ hash;
    }

    private int getLengthWithPadding(int initialLength) {
        int missing = ++initialLength % (BLOCK_LENGTH_IN_BYTES);
        return missing == 0 ? initialLength / BLOCK_LENGTH_IN_BYTES : (initialLength + BLOCK_LENGTH_IN_BYTES - missing) / BLOCK_LENGTH_IN_BYTES;
    }


    public long setHighestOneBit(long num) {
        int bits = 63;
        System.out.println(bits);
        while ((num & ShiftUtils.leftShift(1L, bits)) == 0) {
            bits--;
            if (bits<0) break;
        }
        System.out.println(bits);
        return bits == 63 ? 1 : num ^ ShiftUtils.leftShift(1L, ++bits);
    }

    private void printBytes(long num, int radix) {
        for (int i = 0; i < 8; i++) {
            System.out.print(Integer.toString((int) ShiftUtils.rightShift(num, (i * Byte.SIZE)) & 0xFF, radix) + "   ");
        }
        System.out.print('\n');
    }

}
