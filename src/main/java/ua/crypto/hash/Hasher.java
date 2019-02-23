package ua.crypto.hash;

import ua.crypto.cipher.Misty;

public class Hasher {

    private Misty misty = new Misty();

    public long hash(String text) {
        String padded = padding(text);

        String[] strings = padded.split("(?<=\\G.{4})");
        long prevHash = 0L;
        long resultHash = 0L;

        for (String s: strings) {
            resultHash = G(Long.parseLong(s, 2), prevHash);
            prevHash = resultHash;
        }

        return resultHash;
    }

    private long G(long text, long hash) {
        return misty.encrypt(hash, text ^ hash) ^ text ^ hash;
    }

    private String padding(String text) {
        StringBuilder textBuilder = new StringBuilder(text);
        textBuilder.append("1");

        while (!(textBuilder.length() % 64 == 0)) {
            textBuilder.append("0");
        }

        return textBuilder.toString();
    }


}
