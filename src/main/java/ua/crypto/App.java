package ua.crypto;

import ua.crypto.DigitalSignature.Pair;
import ua.crypto.DigitalSignature.Signer;

import java.math.BigInteger;

public class App {

    public static void main(String[] args) {
        Signer signer =  new Signer();

        Pair<BigInteger, BigInteger> key = signer.generateKeys();

        long hash = 2323523523423L;
        Pair<BigInteger, BigInteger> sign = signer.sign(hash, key.getLeft());

        System.out.println(signer.verify(sign.getRight(), key.getRight(), sign.getLeft(), hash));
    }
}
