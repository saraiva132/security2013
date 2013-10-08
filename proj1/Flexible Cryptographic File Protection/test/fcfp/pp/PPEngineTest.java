package fcfp.pp;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Simão Paulo Rato Alves Reis
 * @version 1.1
 */
public class PPEngineTest {

    /**
     * Test of getInstance method, of class PPEngine.
     */
    @Test
    public void testSingletonPattern() {
        System.out.println("singletonPattern");
        PPEngine instance = PPEngine.getInstance();
        PPEngine other = PPEngine.getInstance();
        assertEquals(instance, other);
    }

    /**
     * Test of loadPPs method, of class PPEngine.
     */
    @Test
    public void testEncryptionPP() throws ProtectionPluginException {
        System.out.println("encryptionPP");
        PPEngine ppEngine = PPEngine.getInstance();
        ppEngine.loadPPs();
        EncryptionPP xor = ppEngine.getEncryptionPP("pp.Xor");
        byte[] msg = {0, 1, 2, 3, 4, 5, 6, 7};
        byte[] key = {0, 1, 6, 2, 3, 4};
        byte[] cpy = new byte[msg.length];
        System.arraycopy(msg, 0, cpy, 0, cpy.length);
        xor.cypher(cpy, key);
        xor.decypher(cpy, key);
        assertArrayEquals(msg, cpy);
    }
}