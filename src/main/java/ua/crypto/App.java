package ua.crypto;

import ua.crypto.DigitalSignature.Key;
import ua.crypto.DigitalSignature.Signature;
import ua.crypto.DigitalSignature.SignatureWithAllValues;
import ua.crypto.DigitalSignature.Signer;
import ua.crypto.hash.ISByteHasher;

import java.io.*;
import java.math.BigInteger;

public class App {

    public static final String DIVIDER = "------------------------------\n";
    public static final String SIGN_FLAG = "-sign";
    public static final String CHECK_FLAG = "-check";

    private static ISByteHasher hasher;
    private static Signer signer;
    private static Integer bufferSize = 51200; // probably optimal value

    static {
        signer = new Signer();
    }

    public static void main(String[] args) throws IOException {

        try {
            if (args.length == 3) {
                bufferSize = Integer.parseInt(args[2]);
                if (bufferSize < 1) {
                    throw new NumberFormatException();
                }
            }
        }
        catch (NumberFormatException ex) {
            System.out.println("Buffer size should be positive value");
        }

        hasher = new ISByteHasher(bufferSize);

        try {
            String flag = args[0];

            switch (flag) {
                case SIGN_FLAG:
                    String fileToSign = args[1];

                    if (fileToSign == null || fileToSign.trim().equals("")) {
                        System.out.println("File name to sign not provided. Please provide file name without extension.");
                        return;
                    }

                    if (fileToSign.contains(".")) {
                        System.out.println("Please provide file name without extension");
                        return;
                    }

                    long hashToSign = 0L;

                    try(FileInputStream fis = new FileInputStream(fileToSign + ".txt")) {
                        hashToSign = hasher.hash(fis);
                    }

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

                        System.out.println("Signed. Please check " + fileToSign + ".sig");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    break;

                case CHECK_FLAG:
                    String fileToCheck = args[1];

                    long hashToCheck = hasher.hash(new FileInputStream(fileToCheck + ".txt"));

                    if (fileToCheck == null || fileToCheck.trim().equals("")) {
                        System.out.println("File name to check not provided. Please provide file name without extension.");
                        return;
                    }

                    if (fileToCheck.contains(".")) {
                        System.out.println("Please provide file name without extension");
                        return;
                    }

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
                    System.out.println("Wrong flag!\n" +
                            "Available flags: [-sign, -check]");
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("Wrong usage!\n" +
                    "Possible usage: 'java -jar DSProtocol-1.1.jar <flag> <fileName> <bufferSize>'\n" +
                    "Available flags: [-sign, -check]\n" +
                    "<bufferSize> could be omitted");
        }

    }

    private static String retrieveNum(String rawNum) {
        return rawNum.substring(4);
    }
}
