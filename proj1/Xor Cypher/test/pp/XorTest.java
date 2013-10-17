package pp;

import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNot;
import org.junit.Assert;
import static org.junit.Assert.assertArrayEquals;
import org.junit.Test;

/**
 *
 * @author Simão Paulo Rato Alves Reis
 * @version 1.1
 */
public class XorTest {

    /**
     * Test cypher method.
     */
    @Test
    public void testCypher() {
        Xor pp = new Xor();
        byte[] msg = {0, 1, 2, 3, 4, 5};
        byte[] key = {0, 1, 6, 2, 3, 4};
        byte[] cpyMsg = new byte[msg.length];
        System.arraycopy(msg, 0, cpyMsg, 0, cpyMsg.length);
        pp.cipher(cpyMsg, key);
        Assert.assertThat(msg, IsNot.not(IsEqual.equalTo(cpyMsg)));
    }

    /**
     * Test decypher method.
     */
    @Test
    public void testDecypher() {
        Xor pp = new Xor();
        byte[] msg = {0, 1, 2, 3, 4, 5};
        byte[] key = {0, 1, 6, 2, 3, 4};
        byte[] cpyMsg = new byte[msg.length];
        System.arraycopy(msg, 0, cpyMsg, 0, cpyMsg.length);
        pp.cipher(cpyMsg, key);
        pp.decipher(cpyMsg, key);
        assertArrayEquals(msg, cpyMsg);
    }

    /**
     * Test cypher and decypher method with key.length smaller than msg.length.
     */
    @Test
    public void testKeySmallerThanMsg() {
        Xor pp = new Xor();
        byte[] msg = {0, 1, 2, 3, 4, 5, 6, 7};
        byte[] key = {0, 1, 6, 2, 3, 4};
        byte[] cpyMsg = new byte[msg.length];
        System.arraycopy(msg, 0, cpyMsg, 0, cpyMsg.length);
        pp.cipher(cpyMsg, key);
        pp.decipher(cpyMsg, key);
        assertArrayEquals(msg, cpyMsg);
    }

    /**
     * Test cypher and decypher method with msg.length smaller than key.length.
     */
    @Test
    public void testMsgSmallerThanKey() {
        Xor pp = new Xor();
        byte[] msg = {0, 1, 2, 3, 4, 5, 6, 7};
        byte[] key = {0, 1, 6, 2, 3, 4, 3, 2, 6, 4};
        byte[] cpyMsg = new byte[msg.length];
        System.arraycopy(msg, 0, cpyMsg, 0, cpyMsg.length);
        pp.cipher(cpyMsg, key);
        pp.decipher(cpyMsg, key);
        assertArrayEquals(msg, cpyMsg);
    }

    /**
     * Test cypher method with msg equal to key.
     */
    @Test
    public void testMsgequalsKey() {
        Xor pp = new Xor();
        byte[] msg = {0, 1, 2, 3, 4, 5, 6, 7};
        byte[] key = {0, 1, 2, 3, 4, 5, 6, 7};
        pp.cipher(msg, key);
        assertArrayEquals(msg, new byte[]{0, 0, 0, 0, 0, 0, 0, 0});
    }
}