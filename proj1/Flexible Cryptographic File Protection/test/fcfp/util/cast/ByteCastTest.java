/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fcfp.util.cast;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Simão
 */
public class ByteCastTest {

    /**
     * Test of long2ByteArray method, of class ByteCast.
     */
    @Test
    public void testCast() {
        System.out.println("long2ByteArray");
        long expResult = Long.MIN_VALUE;
        byte[] num = ByteCast.long2ByteArray(expResult);
        for (byte b : num) {
            System.out.print(b + " ");
        }
        System.out.println();
        long result = ByteCast.byteArray2Long(num);
        System.out.println();
        assertEquals(expResult, result);
    }
}