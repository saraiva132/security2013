package fcfp.util;

import java.nio.ByteBuffer;

/**
 *
 * @author Simão Paulo Rato Alves Reis
 * @version 1.2
 */
public class ByteCast {

    /**
     * Convert a long number to a byte array with Long.SIZE / Byte.size;
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

    /**
     * Convert a int number to a byte array with Integer.SIZE / Byte.size;
     *
     * @param num the number to convert.
     * @return the byte array representing the long number.
     */
    public static byte[] int2ByteArray(int num) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(num);
        return buffer.array();
    }

    /**
     *
     * @param data
     * @return
     */
    public static int byteArray2Int(byte[] data) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.put(data);
        buffer.flip();
        return buffer.getInt();
    }
}
