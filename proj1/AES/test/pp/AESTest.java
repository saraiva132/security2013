package pp;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * AES Protection Plugin Test Cases
 *
 * @author Simão Paulo Rato Alves Reis
 * @author Rafael Saraiva Figueiredo
 * @version 1.0
 */
public class AESTest {

    /**
     * Test of cipher and decipher method, of class AES.
     */
    @Test
    public void testAES_ValidKey() {
        String text = "Ola como nhg nhg";
        String pass = "3hd7s6h4453dgtredgdfgdfgdfg";
        byte[] msg = text.getBytes();
        System.out.println("length: " + msg.length + " " + "length: " + text.length() + " " + text);
        byte[] key = pass.getBytes();
        AES instance = new AES();
        byte[] result = instance.cipher(msg, key);
        System.out.println("length: " + result.length + " " + "length: " + new String(result).length() + " " + new String(result));
        result = instance.decipher(result, key);
        System.out.println("length: " + result.length + " " + "length: " + new String(result).length() + " " + new String(result));
        assertArrayEquals(msg, result);
    }
    
    @Test
    public void testDoubleTranspose() {
        byte[][] array = new byte[][]{
            {1,2,3},
            {4,5,6},
            {7,8,9}
        };
        byte[][] cpy = AES.MtrxDoublTranspose(array);
        cpy = AES.InvMtrxDoublTranspose(cpy);
        assertArrayEquals(array, cpy);
    }
}