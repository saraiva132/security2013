package fcfp.png;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Simão
 */
public class LessSignificantBitTest {

    /**
     * Test of encode method, of class LessSignificantBit.
     */
    @Test
    public void testEncode() throws Exception {
        System.out.println("encode");
        byte[] image = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        byte[] content = {(byte) 0xFF, (byte) 0xFF};
        byte[] expResult = {1, 1, 3, 3, 5, 5, 7, 7, 9, 9, 11, 11, 13, 13, 15, 15};
        LessSignificantBit.encode(image, content, 0);
        assertArrayEquals(expResult, image);
    }

    /**
     * Test of decode method, of class LessSignificantBit.
     */
    @Test
    public void testDecode() {
        System.out.println("decode");
        byte[] image = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1,
            1, 3, 3, 5, 5, 7, 7, 9, 9, 11, 11, 13, 13, 15, 15};
        byte[] expResult = {(byte) 0xFF, (byte) 0xFF};
        byte[] result = LessSignificantBit.decode(image);
        assertArrayEquals(expResult, result);
    }
}