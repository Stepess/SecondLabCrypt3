package ua.crypto.DigitalSignature;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class SignerTest {

    private Signer signer = new Signer();

    @Test
    public void shouldSignAndVerify() {
        Key key = signer.generateKeys();

        long hash = new Random().nextLong();
        Signature sign = signer.sign(hash, key.getX());

        assertTrue(signer.verify(sign, key.getY(),  hash));
    }
}