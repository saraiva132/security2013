package pp;

import fcfp.pp.EncryptionPP;

/**
 *
 * @author Simão Paulo Rato Alves Reis
 * @version 1.1
 */
public class Xor implements EncryptionPP {

    @Override
    public void cipher(byte[] msg, byte[] key) {

        for (int i = 0; i < msg.length; i++) {
            msg[i] ^= key[i % key.length];
        }
    }

    @Override
    public void decipher(byte[] msg, byte[] key) {

        cipher(msg, key);
    }
}
