/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fcfp.png;

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
public class LessSignificantBitTest {
    
    /**
     * Test of encode method, of class LessSignificantBit.
     */
    @Test
    public void testEncode() throws Exception {
        System.out.println("encode");
        byte[] image = null;
        byte[] content = null;
        int offset = 0;
        LessSignificantBit.encode(image, content, offset);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of decode method, of class LessSignificantBit.
     */
    @Test
    public void testDecode() {
        System.out.println("decode");
        byte[] image = null;
        byte[] expResult = null;
        byte[] result = LessSignificantBit.decode(image);
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}