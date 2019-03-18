package ua.crypto.DigitalSignature;

import java.math.BigInteger;

public class SignatureWithAllValues {

    private BigInteger k;
    private BigInteger s;
    private BigInteger u;
    private BigInteger z;
    private BigInteger g;


    public SignatureWithAllValues(BigInteger k, BigInteger s, BigInteger u, BigInteger z, BigInteger g) {
        this.k = k;
        this.s = s;
        this.u = u;
        this.z = z;
        this.g = g;
    }

    public BigInteger getK() {
        return k;
    }

    public BigInteger getS() {
        return s;
    }

    public BigInteger getU() {
        return u;
    }

    public BigInteger getZ() {
        return z;
    }

    public BigInteger getG() {
        return g;
    }
}
