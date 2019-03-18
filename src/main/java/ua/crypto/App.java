package ua.crypto;

import ua.crypto.DigitalSignature.Key;
import ua.crypto.DigitalSignature.Signature;
import ua.crypto.DigitalSignature.SignatureWithAllValues;
import ua.crypto.DigitalSignature.Signer;
import ua.crypto.cipher.Misty;
import ua.crypto.hash.ISByteHasher;
import ua.crypto.util.FileUtils;

import java.io.*;

public class App {

    public static final String DIVIDER = "------------------------------\n";
    public static final String SIGN_FLAG = "-sign";
    public static final String CHECK_FLAG = "-check";

    private static Misty misty;
    //private static ByteHasher hasher;
    private static FileUtils fileUtils;
    private static ISByteHasher hasher;
    private static Signer signer;

    static {
        misty = new Misty();
        //hasher = new ByteHasher();
        hasher = new ISByteHasher();
        fileUtils = new FileUtils();
        signer = new Signer();
    }


    public static void main(String[] args) throws IOException {

        //String flag = args[0];
        String flag = "-check";

        switch (flag) {
            case SIGN_FLAG:
                String fileName = "text";

                long hash = hasher.hash(new FileInputStream(fileName + ".txt"));

                Key key = signer.generateKeys();
                SignatureWithAllValues signature = signer.signWithAllValues(hash, key.getX());

                try(Writer writer = new FileWriter(fileName + ".sig")) {
                    writer.write(DIVIDER);


                    writer.write(fileName);
                    writer.append('\n');
                    writer.write("H = " + Long.toUnsignedString(Long.reverseBytes(hash), 16)); // to achieve example's view
                    writer.append('\n');



                    writer.write("Y = " + key.getY().toString(16));
                    writer.append('\n');
                    writer.write("K = " + signature.getK().toString(16));
                    writer.append('\n');
                    writer.write("S = " + signature.getS().toString(16));
                    writer.append('\n');

                    writer.write(DIVIDER);

                    writer.write("U = " + signature.getU().toString(16));
                    writer.append('\n');
                    writer.write("Z = " + signature.getZ().toString(16));
                    writer.append('\n');
                    writer.write("G = " + signature.getG().toString(16));
                    writer.append('\n');

                    writer.write(DIVIDER);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                break;
            case CHECK_FLAG:
                String fileToCheck= "text";

                long hash1 = hasher.hash(new FileInputStream(fileToCheck + ".txt"));

                try(BufferedReader reader = new BufferedReader(new FileReader(fileToCheck + ".sig"))) {
                    reader.skip(30);
                    System.out.println(reader.readLine());
                    System.out.println(reader.readLine());
                    System.out.println(reader.readLine());
                    System.out.println(reader.readLine());
                }
             catch (IOException ex) {
                ex.printStackTrace();
            }
                break;
            default:
                System.out.println("Wrong flag!");
        }


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
