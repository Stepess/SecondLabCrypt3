package ua.crypto.cipher;

import static ua.crypto.util.ShiftUtils.*;

public class MistyDebug implements IMisty {

    public long encrypt(long key, long input) {
        System.out.println();
        System.out.print("DEBUG: EncryptBlock input = ");
        printBytes(input, 16);
        System.out.print("DEBUG: EncryptBlock key   = ");
        printBytes(key, 16);


        int r, l;
        r = rightBytes(input);
        l = leftBytes(input); //TODO think about it (left , right)

        int[] keys = generateRoundKeys(key);

        System.out.println();

        System.out.println("DEBUG: L0 = " + Integer.toUnsignedString(l, 16) + " R0 = " +Integer.toUnsignedString(r, 16));


        for (int i = 0; i <= 3; i++) {
            int buff = r;
            r = l;
            l = roundFunction(keys[i], buff) ^ r;
            System.out.println("DEBUG: L" + (i+1) + " = " + Integer.toUnsignedString(l, 16) );
        }


        long result = concatResult(r, l);
        //long result = concatResultAsInExample(r, l);


        /*long result = Long.reverseBytes(leftShift(l,32));
        result ^= Long.reverseBytes(r);*/

        //result = Long.reverseBytes(result);
        System.out.print("DEBUG: EncryptBlock output = ");
        printBytes(result, 16);
        System.out.println();



        System.out.println("DEBUG: Hash = " + Long.toUnsignedString(result, 16));
        System.out.println();

        return result;

    }

    private int rightBytes(long num) {
        return (int) rightShift(num, 32);
    }

    private int leftBytes(long num) {
        return (int) num;
    }

    /**
     * Like was described in the book
     * @param left
     * @param right
     * @return
     */
    private long concatResult(int left, int right) {
        return Integer.toUnsignedLong(left) ^ leftShift(Integer.toUnsignedLong(right), 32);
    }

    /**
     * Just to obtain that save output as in the example
     * @param left
     * @param right
     * @return
     */
    private long concatResultAsInExample(int left, int right) {
        return Long.reverseBytes(Integer.toUnsignedLong(left)) ^ Long.reverseBytes(leftShift(Integer.toUnsignedLong(right), 32));
    }

    private int[] generateRoundKeys(long key) {
        int[] keys = new int[4];

        int r, l;
        r = rightBytes(key);
        l = leftBytes(key);

        keys[0] = l;
        keys[1] = r;
        keys[2] = ~r;
        keys[3] = ~l;

        System.out.println();
        System.out.print("DEBUG: ");
        for (int i = 0; i < 4; i++) {
            System.out.print("K" + (i+1) + " = " + Integer.toUnsignedString(keys[i],16) + " " );
        }

        return keys;
    }

    private int roundFunction(int key, int text) {
        return leftCyclicShift(s(key ^ text), 13);
    }

    private int s(int in) {
        int[] bytes = new int[4];

        for (int i = 0; i < 4; i++) {
            bytes[i] = rightShift(in, i*8) & 0xff;
        }

        int result = 0;
        for (int i = 0; i < 4; i++) {
            result ^= leftShift(s_box[bytes[i]], i*8);
        }

        return result;
    }

    //util methods

    private void printBytes(long num, int radix) {
        for (int i = 0; i < 8; i++) {
            System.out.print(Integer.toString((int) (num >> (i * Byte.SIZE)) & 0xFF, radix) + "   ");
        }
        System.out.print('\n');
    }
}
