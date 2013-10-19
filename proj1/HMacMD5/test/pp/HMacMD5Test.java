package pp;

import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNot;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * MD5 Protection Plugin Test Cases
 *
 * @author Simão Paulo Rato Alves Reis
 * @author Rafael Saraiva Figueiredo
 * @version 1.0
 */
public class HMacMD5Test {

    /**
     * Test of sign method, of class HMacMD5.
     */
    @Test
    public void testSign() {
        System.out.println("sign");
        byte[] msg = "Olá como vai isso?".getBytes();
        byte[] key = "chave privata simétrica".getBytes();
        HMacMD5 instance = new HMacMD5();
        byte[] expResult = instance.sign(msg, key);
        byte[] result = instance.sign(msg, key);
        System.out.println(new String(result));
        System.out.println(new String(expResult));
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of sign method, of class HMacMD5.
     */
    @Test
    public void testSignWithDiffKeys() {
        System.out.println("sign with diff keys");
        byte[] msg = "Olá como vai isso?".getBytes();
        byte[] key = "chave privata simétrica".getBytes();
        byte[] key2 = "chave privata simétrica2".getBytes();
        HMacMD5 instance = new HMacMD5();
        byte[] expResult = instance.sign(msg, key2);
        byte[] result = instance.sign(msg, key);
        System.out.println(new String(result));
        System.out.println(new String(expResult));
        Assert.assertThat(result, IsNot.not(IsEqual.equalTo(expResult)));
    }
}