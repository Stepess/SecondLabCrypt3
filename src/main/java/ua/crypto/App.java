package ua.crypto;

import ua.crypto.DigitalSignature.Key;
import ua.crypto.DigitalSignature.Signature;
import ua.crypto.DigitalSignature.SignatureWithAllValues;
import ua.crypto.DigitalSignature.Signer;
import ua.crypto.cipher.MistyDebug;
import ua.crypto.hash.ISByteHasher;
import ua.crypto.util.FileUtils;

import java.io.*;
import java.math.BigInteger;

public class App {

    public static final String DIVIDER = "------------------------------\n";
    public static final String SIGN_FLAG = "-sign";
    public static final String CHECK_FLAG = "-check";

    private static MistyDebug misty;
    //private static ByteHasher hasher;
    private static FileUtils fileUtils;
    private static ISByteHasher hasher;
    private static Signer signer;

    static {
        misty = new MistyDebug();
        //hasher = new ByteHasher();
        hasher = new ISByteHasher();
        fileUtils = new FileUtils();
        signer = new Signer();
    }


    public static void main(String[] args) throws IOException {

        //String flag = args[0];
        String flag = "-sign";

        switch (flag) {
            case SIGN_FLAG:
                String fileToSign = "kek";

                long hashToSign = hasher.hash(new FileInputStream(fileToSign + ".txt"));

                Key key = signer.generateKeys();
                SignatureWithAllValues signatureWithAllValues = signer.signWithAllValues(hashToSign, key.getX());

                try (Writer writer = new FileWriter(fileToSign + ".sig")) {
                    writer.write(DIVIDER);

                    writer.write(fileToSign);
                    writer.append('\n');
                    writer.write("H = " + Long.toUnsignedString(Long.reverseBytes(hashToSign), 16)); // to achieve example's view
                    writer.append('\n');

                    writer.write("Y = " + key.getY().toString(16));
                    writer.append('\n');
                    writer.write("K = " + signatureWithAllValues.getK().toString(16));
                    writer.append('\n');
                    writer.write("S = " + signatureWithAllValues.getS().toString(16));
                    writer.append('\n');

                    writer.write(DIVIDER);

                    writer.write("U = " + signatureWithAllValues.getU().toString(16));
                    writer.append('\n');
                    writer.write("Z = " + signatureWithAllValues.getZ().toString(16));
                    writer.append('\n');
                    writer.write("G = " + signatureWithAllValues.getG().toString(16));
                    writer.append('\n');

                    writer.write(DIVIDER);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                break;
            case CHECK_FLAG:
                String fileToCheck = "text";

                long hashToCheck = hasher.hash(new FileInputStream(fileToCheck + ".txt"));

                try (BufferedReader reader = new BufferedReader(new FileReader(fileToCheck + ".sig"))) {
                    reader.skip(31);
                    reader.readLine();
                    reader.readLine();

                    String Y = retrieveNum(reader.readLine());
                    String K = retrieveNum(reader.readLine());
                    String S = retrieveNum(reader.readLine());

                    Signature signature = new Signature(new BigInteger(K, 16), new BigInteger(S, 16));

                    boolean result = signer.verify(signature, new BigInteger(Y, 16), hashToCheck);

                    System.out.println(result ? "Signature is right" : "Signature is wrong");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                break;
            default:
                System.out.println("Wrong flag!");
        }


    }

    private static String retrieveNum(String rawNum) {
        return rawNum.substring(4);
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
