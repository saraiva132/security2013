package pp;

import fcfp.pp.EncryptionPP;

/**
 *
 * @author Simão Paulo Rato Alves Reis
 * @version 1.0
 */
public class Xor implements EncryptionPP {

    @Override
    public void cypher(byte[] msg, byte[] key) {

        for (int i = 0; i < msg.length; i++) {
            msg[i] ^= key[i % key.length];
        }
    }

    @Override
    public void decypher(byte[] msg, byte[] key) {

        cypher(msg, key);
    }
}
