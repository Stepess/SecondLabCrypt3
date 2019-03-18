package ua.crypto.cipher;

import static ua.crypto.util.ShiftUtils.*;

public class Misty implements IMisty {

    public long encrypt(long key, long input) {

        int r, l;
        r = rightBytes(input);
        l = leftBytes(input);

        int[] keys = generateRoundKeys(key);

        for (int i = 0; i <= 3; i++) {
            int buff = r;
            r = l;
            l = roundFunction(keys[i], buff) ^ r;
        }


        return concatResult(r, l);

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

    private int[] generateRoundKeys(long key) {
        int[] keys = new int[4];

        int r, l;
        r = rightBytes(key);
        l = leftBytes(key);

        keys[0] = l; keys[1] = r; keys[2] = ~r; keys[3] = ~l;

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

}
