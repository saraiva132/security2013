package fcfp.util.iv;

import java.security.SecureRandom;
import java.util.Arrays;

/**
 * Initialization Vector
 *
 * @author Simão Paulo Rato Alves Reis
 * @version 1.1
 */
public final class InitVector {

    private static final int ivLength = 128; // the initial vector value max length
    private static byte[] ivValue; // the initial vector value

    /**
     * This class cannot be instantiated or extended.
     */
    private InitVector() {
    }

    public static void readIVfromFPC(byte[] header) {
        ivValue = new byte[ivLength];
        System.arraycopy(header, header.length - ivLength, ivValue, 0, ivLength);
    }

    public static void writeIVtoFPC(byte[] header) {
        System.arraycopy(ivValue, 0, header, header.length - ivLength, ivLength);
    }

    public static void generateIV() {
        SecureRandom sr = new SecureRandom();
        ivValue = sr.generateSeed(ivLength);
    }

    public static byte[] getIV(InitVectorSIZE ivSize) {
        return Arrays.copyOfRange(ivValue, 0, ivSize.getSize());
    }

    public static int getMaxLength() {
        return ivLength;
    }
}
