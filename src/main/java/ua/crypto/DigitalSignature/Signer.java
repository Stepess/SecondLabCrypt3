package ua.crypto.DigitalSignature;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Signer {
    private static BigInteger p = new BigInteger("AF5228967057FE1CB84B92511BE89A47", 16);
    private static BigInteger q = new BigInteger("57A9144B382BFF0E5C25C9288DF44D23", 16);
    private static BigInteger a = new BigInteger("9E93A4096E5416CED0242228014B67B5", 16);

    public Signer() {
        try {
            random = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private SecureRandom random;

    public boolean verify(Signature s, BigInteger y, long hash) {
        BigInteger exponent = y.modPow(s.getK(), p)
                .multiply(s.getS()).mod(p).add(formatHash(hash).multiply(s.getK()));
        return s.getS().mod(p).equals(a.modPow(exponent, p));
    }

    public Key generateKeys() {
        BigInteger x = new BigInteger(p.bitLength(), random).mod(p);
        return new Key(x, a.modPow(x, p));
    }

    public Signature sign(long hash, BigInteger x) {
        BigInteger H = formatHash(hash);
        BigInteger U = new BigInteger(p.bitLength(), random).mod(p);
        BigInteger Z = a.modPow(U, p);

        BigInteger xPlusHModInverse = x.add(H).modInverse(q);
        BigInteger k = U.subtract(Z).multiply(xPlusHModInverse).mod(q);
        BigInteger g = x.multiply(Z).add(U.multiply(H)).multiply(xPlusHModInverse).mod(q);

        return new Signature(k, a.modPow(g, p));
    }

    public SignatureWithAllValues signWithAllValues(long hash, BigInteger x) {
        BigInteger H = formatHash(hash);
        BigInteger U = new BigInteger(p.bitLength(), random).mod(p);
        BigInteger Z = a.modPow(U, p);

        BigInteger xPlusHModInverse = x.add(H).modInverse(q);
        BigInteger k = U.subtract(Z).multiply(xPlusHModInverse).mod(q);
        BigInteger g = x.multiply(Z).add(U.multiply(H)).multiply(xPlusHModInverse).mod(q);

        BigInteger S = a.modPow(g, p);

        return new SignatureWithAllValuesBuilder()
                .setG(g)
                .setK(k)
                .setS(S)
                .setU(U)
                .setZ(Z)
                .createSignatureWithAllValues();
    }

    private BigInteger formatHash(long hash) {
        BigInteger result = appendBigIntegerWithByte(BigInteger.valueOf(hash), 0x00);
        for (int i=0; i<6; i++) {
            result = appendBigIntegerWithByte(result, 0xFF);
        }
        return appendBigIntegerWithByte(result, 0x00);
    }

    private BigInteger appendBigIntegerWithByte(BigInteger integer, int appender) {
        return integer.shiftLeft(8).add(BigInteger.valueOf(appender));
    }

}
