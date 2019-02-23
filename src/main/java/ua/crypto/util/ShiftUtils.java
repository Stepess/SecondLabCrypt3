package ua.crypto.util;

public class ShiftUtils {

    public static int rightCyclicShift(int i, int step) {
        return Integer.rotateRight(i, step);
    }

    public static int leftCyclicShift(int i, int step) {
        return Integer.rotateLeft(i, step);
    }

    public static int rightShift(int i, int step) {
        return i >>> step;
    }

    public static int leftShift(int i, int step) {
        return i << step;
    }
}
