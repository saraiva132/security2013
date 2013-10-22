package fcfp.util.cast;

import java.nio.ByteBuffer;

/**
 *
 * @author Simão Paulo Rato Alves Reis
 * @version 1.1
 */
public class ByteCast {
    
    /**
     * Convert a long number to a byte array with Long.SIZE / BYTE.size;
     *
     * @param num the number to convert.
     * @return the byte array representing the long number.
     */
    public static byte[] long2ByteArray(long num) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(num);
        return buffer.array();
    }

    /**
     *
     * @param data
     * @return
     */
    public static long byteArray2Long(byte[] data) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.put(data);
        buffer.flip();
        return buffer.getLong();
    }
}
