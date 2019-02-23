package ua.crypto.util;

import org.junit.Test;

import static java.lang.Integer.toBinaryString;

import static org.junit.Assert.*;

public class ShiftUtilsTest {

    @Test
    public void rightCyclicShift() {
        int unshifted = 0b1111_1111_1111_1111_1111_1111_0000_1111;
        int shifted = 0b1111_1111_1111_1111_1111_1111_1111_0000;
        assertEquals(toBinaryString(shifted), toBinaryString(ShiftUtils.rightCyclicShift(unshifted, 4)));
    }

    @Test
    public void rightShift() {
        int unshifted = 0b1111_1111_1111_1111_1111_1111_1111_1111;
        int shifted = 0b0000_0000_1111_1111_1111_1111_1111_1111;
        assertEquals(toBinaryString(shifted), toBinaryString(ShiftUtils.rightShift(unshifted, 8)));
    }

    @Test
    public void leftCyclicShift() {
        int unshifted = 0b1111_1111_1111_1111_1111_1111_0000_1111;
        int shifted = 0b1111_1111_1111_1111_1111_0000_1111_1111;
        assertEquals(toBinaryString(shifted), toBinaryString(ShiftUtils.leftCyclicShift(unshifted, 4)));
    }


    @Test
    public void leftShift() {
        int unshifted = 0b1111_1111_1111_1111_1111_1111_0000_1111;
        int shifted = 0b1111_1111_1111_1111_1111_0000_1111_0000;
        assertEquals(toBinaryString(shifted), toBinaryString(ShiftUtils.leftShift(unshifted, 4)));
    }
}