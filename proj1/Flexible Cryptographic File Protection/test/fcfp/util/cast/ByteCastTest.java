/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fcfp.util.cast;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
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
    public void testLong2ByteArray() {
        System.out.println("long2ByteArray");
        long num = 256L;
        byte[] expResult = new byte[8];
        expResult[1] = (byte) 1;
        byte[] result = ByteCast.long2ByteArray(num);
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of byteArray2Long method, of class ByteCast.
     */
    @Test
    public void testByteArray2Long() {
        System.out.println("byteArray2Long");
        byte[] data = new byte[8];
        data[1] = 1;
        long expResult = 256L;
        long result = ByteCast.byteArray2Long(data);
        assertEquals(expResult, result);
    }
}