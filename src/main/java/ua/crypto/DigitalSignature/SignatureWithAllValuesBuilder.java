package ua.crypto.DigitalSignature;

import java.math.BigInteger;

public class SignatureWithAllValuesBuilder {
    private BigInteger k;
    private BigInteger s;
    private BigInteger u;
    private BigInteger z;
    private BigInteger g;

    public SignatureWithAllValuesBuilder setK(BigInteger k) {
        this.k = k;
        return this;
    }

    public SignatureWithAllValuesBuilder setS(BigInteger s) {
        this.s = s;
        return this;
    }

    public SignatureWithAllValuesBuilder setU(BigInteger u) {
        this.u = u;
        return this;
    }

    public SignatureWithAllValuesBuilder setZ(BigInteger z) {
        this.z = z;
        return this;
    }

    public SignatureWithAllValuesBuilder setG(BigInteger g) {
        this.g = g;
        return this;
    }

    public SignatureWithAllValues createSignatureWithAllValues() {
        return new SignatureWithAllValues(k, s, u, z, g);
    }
}