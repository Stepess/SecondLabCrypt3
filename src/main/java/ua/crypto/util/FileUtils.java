package ua.crypto.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

public class FileUtils {
    public List<Byte> readFileAsByteList(String fileName) {
        InputStream is = null;
        try {
            is = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int buff;

        List<Byte> bytes = new LinkedList<>();

        try {
            while ((buff = is.read()) != -1) { bytes.add((byte) buff); }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bytes;
    }
}
