package pp;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * DES Protection Plugin Test Cases
 *
 * @author Simão Paulo Rato Alves Reis
 * @author Rafael Saraiva Figueiredo
 * @version 1.0
 */
public class DESTest {

    /**
     * Test of cipher and decipher method, of class DES.
     */
    @Test
    public void testDES_ValidKey() {
        String text = "Ola como nhg nhg";
        String pass = "3hd7s6h4453dgtredgdfgdfgdfg";
        byte[] msg = text.getBytes();
        System.out.println("length: " + msg.length + " " + "length: " + text.length() + " " + text);
        byte[] key = pass.getBytes();
        DES instance = new DES();
        byte[] result = instance.cipher(msg, key);
        System.out.println("length: " + result.length + " " + "length: " + new String(result).length() + " " + new String(result));
        result = instance.decipher(result, key);
        System.out.println("length: " + result.length + " " + "length: " + new String(result).length() + " " + new String(result));
        assertArrayEquals(msg, result);
    }
}