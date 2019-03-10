package ua.crypto;

import ua.crypto.DigitalSignature.Key;
import ua.crypto.DigitalSignature.Signature;
import ua.crypto.DigitalSignature.Signer;

public class App {

    public static void main(String[] args) {
        Signer signer =  new Signer();

        Key key = signer.generateKeys();

        long hash = 2323523568683423L;
        Signature sign = signer.sign(hash, key.getX());

        System.out.println(signer.verify(sign, key.getY(),  hash));
    }
}
