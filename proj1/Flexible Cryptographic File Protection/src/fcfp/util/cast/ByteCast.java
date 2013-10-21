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
//        data[7] = (byte) ((num & 0xFF00000000000000L) >>> 56);
//        data[6] = (byte) ((num & 0x00FF000000000000L) >>> 48);
//        data[5] = (byte) ((num & 0x0000FF0000000000L) >>> 40);
//        data[4] = (byte) ((num & 0x000000FF00000000L) >>> 32);
//        data[3] = (byte) ((num & 0x00000000FF000000) >>> 24);
//        data[2] = (byte) ((num & 0x0000000000FF0000) >>> 16);
//        data[1] = (byte) ((num & 0x000000000000FF00) >>> 8);
//        data[0] = (byte) ((num & 0x00000000000000FF));
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
            num += (data[i] << (i << 3));
        }
        return num;
    }
}
