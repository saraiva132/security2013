package pp;

import fcfp.pp.IntegrityPP;

/**
 *
 * @author Simão Paulo Rato Alves Reis
 * @author Rafael Saraiva Figueiredo
 * @version 1.0
 */
public class HMacSHA1 implements IntegrityPP {

    private static final byte opad = 0x36;
    private static final byte ipad = 0x5C;

    @Override
    public byte[] sign(byte[] msg, byte[] key) {
        byte[] ikey = new byte[key.length];
        for (int i = 0; i < ikey.length; i++) {
            ikey[i] = (byte) (key[i] ^ ipad);
        }
        byte[] imsg = new byte[ikey.length + msg.length];
        System.arraycopy(ikey, 0, imsg, 0, ikey.length);
        System.arraycopy(msg, 0, imsg, ikey.length, msg.length);
        byte[] hmsg = SHA1.hash(new String(imsg)).getBytes();
        byte[] okey = new byte[key.length];
        for (int i = 0; i < okey.length; i++) {
            okey[i] = (byte) (key[i] ^ ipad);
        }
        byte[] omsg = new byte[hmsg.length + okey.length];
        System.arraycopy(okey, 0, omsg, 0, okey.length);
        System.arraycopy(hmsg, 0, omsg, okey.length, hmsg.length);
        return SHA1.hash(new String(omsg)).getBytes();
    }
}
