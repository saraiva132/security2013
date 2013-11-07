package fcfp.util;

import java.util.Random;

/**
 *
 * @author rafael
 */
public class Pad {
    
    public static void fill(byte[] contentToPad, int offset, int length) {
        if (length > contentToPad.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        Random r = new Random();
        byte[] padData = new byte[length - offset];
        new Random().nextBytes(padData);
        System.arraycopy(padData, 0, contentToPad, offset, length-offset);
    }
}
