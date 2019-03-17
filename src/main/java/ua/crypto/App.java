package ua.crypto;

import ua.crypto.cipher.Misty;
import ua.crypto.hash.ByteHasher;
import ua.crypto.hash.ISByteHasher;
import ua.crypto.hash.StringHasher;
import ua.crypto.util.FileUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class App {

    private static Misty misty;
    private static ByteHasher hasher;
    private static FileUtils fileUtils;
    //private static ISByteHasher hasher;

    static {
        misty = new Misty();
        hasher = new ByteHasher();
        //hasher = new ISByteHasher();
        fileUtils = new FileUtils();
    }

    /*public static void main(String[] args) {
        InputStream is = null;
        try {
            is = new FileInputStream("random.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int buff;
        long block = 0;
        long hash = 0;
        int counter=0;


        try {
            while ((buff = is.read()) != -1) {

                block = block ^ (Byte.toUnsignedLong((byte) buff) << counter*8);
                counter++;
                if (counter == 8) {
                     hash = hasher.hash(block, hash);
                     block = 0;
                     counter = 0;
                }
                System.out.println("kkkkk");
            }
            block = hasher.setHighestOneBit(block);
                System.out.println("kkkkk12");
            hash = hasher.hash(block, hash);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(Long.toUnsignedString(hash, 16));
    }*/


    public static void main(String[] args) {
        List<Byte> bytes = fileUtils.readFileAsByteList("123.txt");

        byte[] bytesArray = new byte[bytes.size()];

        for (int i=0; i< bytes.size(); i++) {
            bytesArray[i] = bytes.get(i);
        }

        System.out.println(Long.toUnsignedString(hasher.hash(bytesArray), 16));
    }
}
