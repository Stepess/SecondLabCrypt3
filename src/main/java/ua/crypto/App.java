package ua.crypto;

import ua.crypto.cipher.Misty;
import ua.crypto.hash.ByteHasher;
import ua.crypto.hash.StringHasher;
import ua.crypto.util.FileUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class App {

    private static Misty misty;
    private static ByteHasher hasher;
    private static FileUtils fileUtils;

    static {
        misty = new Misty();
        hasher = new ByteHasher();
        fileUtils = new FileUtils();
    }

    public static void main(String[] args) {
        List<Byte> bytes = fileUtils.readFileAsByteList("text.txt");

        byte[] bytesArray = new byte[bytes.size()];

        for (int i=0; i< bytes.size(); i++) {
            bytesArray[i] = bytes.get(i);
        }

        System.out.println(Long.toUnsignedString(hasher.hash(bytesArray), 16));
    }
}
