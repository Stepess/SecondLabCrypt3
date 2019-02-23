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


    public boolean verify(BigInteger S, BigInteger y, BigInteger k, long hash) {
        return S.mod(p).equals(a.modPow(y.modPow(k, p).multiply(S).mod(p).add(transofrmHash(hash).multiply(k)), p));
    }

    public Pair<BigInteger, BigInteger> generateKeys() {
        BigInteger x = new BigInteger(p.bitLength()-1, random);
        return new Pair<>(
                x,
                a.modPow(x, p)
        );
    }

    public Pair<BigInteger, BigInteger> sign(long hash, BigInteger x) {
        BigInteger H = transofrmHash(hash);
        BigInteger U = getRandomU();
        BigInteger Z = a.modPow(U, p);

        Pair<BigInteger, BigInteger> kandG = calculateKandG(H, U, Z, x);

        return new Pair<>(
                kandG.getLeft(),
                calculateS(kandG.getRight())
        );
    }

    private BigInteger calculateS(BigInteger g) {
        return a.modPow(g, p);
    }

    private Pair<BigInteger, BigInteger> calculateKandG(BigInteger H, BigInteger U, BigInteger Z, BigInteger x) {
        BigInteger xPlusHInverse = x.add(U).modInverse(q);
        return new Pair<>(
                U.subtract(Z).mod(q).multiply(xPlusHInverse),
                x.multiply(Z).add(U.multiply(H)).mod(q).multiply(xPlusHInverse)
        );
    }

    private BigInteger getRandomU() {
        return new BigInteger(128, random);
    }

    private BigInteger transofrmHash(long hash) {
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
