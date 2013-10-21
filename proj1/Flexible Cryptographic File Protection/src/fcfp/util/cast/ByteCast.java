package fcfp.util.cast;

/**
 *
 * @author Simão Paulo Rato Alves Reis
 * @version 1.0
 */
public class ByteCast {
    
    private static final int bytesPerLong = Long.SIZE / Byte.SIZE;

    /**
     * Convert a long number to a byte array with Long.SIZE / BYTE.size;
     *
     * @param num the number to convert.
     * @return the byte array representing the long number.
     */
    public static byte[] long2ByteArray(long num) {
        byte[] data = new byte[bytesPerLong];
        long mask = 0x00000000000000FFL;
        for (int i = 0; i < bytesPerLong; i++) {
            data[i] = (byte) ((num & mask) >>> (i << 3));
            mask = mask << Byte.SIZE;
        }
        return data;
    }
    
    /**
     * 
     * @param data
     * @return 
     */
    public static long byteArray2Long(byte[] data) {
        long num = 0;
        for (int i = 0; i < bytesPerLong; i++) {
            num |= (data[i] << (i << 3));
        }
        return num;
    }
}
