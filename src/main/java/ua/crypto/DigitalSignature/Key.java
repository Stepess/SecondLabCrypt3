package ua.crypto.DigitalSignature;

import java.math.BigInteger;

public class Key {

    private BigInteger x;
    private BigInteger y;

    public Key(BigInteger x, BigInteger y) {
        this.x = x;
        this.y = y;
    }

    public BigInteger getX() {
        return x;
    }

    public BigInteger getY() {
        return y;
    }
}
