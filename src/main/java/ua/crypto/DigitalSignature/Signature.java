package ua.crypto.DigitalSignature;

import java.math.BigInteger;

public class Signature {

    private BigInteger k;
    private BigInteger s;

    public Signature(BigInteger k, BigInteger s) {
        this.k = k;
        this.s = s;
    }

    public BigInteger getK() {
        return k;
    }

    public BigInteger getS() {
        return s;
    }
}
