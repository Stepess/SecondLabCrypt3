package ua.crypto;

import ua.crypto.cipher.Misty;
import ua.crypto.hash.ISByteHasher;
import ua.crypto.util.FileUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class App {

    private static Misty misty;
    //private static ByteHasher hasher;
    private static FileUtils fileUtils;
    private static ISByteHasher hasher;

    static {
        misty = new Misty();
        //hasher = new ByteHasher();
        hasher = new ISByteHasher();
        fileUtils = new FileUtils();
    }



    public static void main(String[] args) throws IOException {
        InputStream inputStream = new FileInputStream("random.txt");

        long hash = hasher.hash(inputStream);

        System.out.println((Long.toUnsignedString(Long.reverseBytes(hash), 16)));

    }


   /* public static void main(String[] args) {
        List<Byte> bytes = fileUtils.readFileAsByteList("123.txt");

        byte[] bytesArray = new byte[bytes.size()];

        for (int i=0; i< bytes.size(); i++) {
            bytesArray[i] = bytes.get(i);
        }

        System.out.println(Long.toUnsignedString(Long.reverseBytes(hasher.hash(bytesArray)), 16));
    }*/
}
