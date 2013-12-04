package mmaa.net;

import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.X509Certificate;

/**
 *
 * @author Simão Paulo Rato Alves Reis
 */
public class Challenge extends ProtocolContainer {

    public static final int LENGTH = 32;
    private final byte[] chlg;
    private byte[] sign;

    public Challenge() {
        chlg = new byte[LENGTH];
        new SecureRandom().nextBytes(chlg);
    }

    @Override
    public String toString() {
        String str = super.toString() + "Message Type: " + getType().name() + "\n"
                + "Challenge:\n";
        for (byte b : getChallenge()) {
            str += Integer.toHexString(b & 0xFF).toUpperCase() + " ";
        }
        if (sign != null) {
            str += "\nSignature:\n";
            for (byte b : getSignature()) {
                str += Integer.toHexString(b & 0xFF).toUpperCase() + " ";
            }
        }
        return str;
    }

    @Override
    public ContainerType getType() {
        return ContainerType.CHALLENGE;
    }

    public void sign(KeyStore ks, String alias, String pass) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, KeyStoreException, UnrecoverableKeyException {
        X509Certificate cert = (X509Certificate) ks.getCertificate(alias);
        System.out.println(cert);
        Signature s = Signature.getInstance(cert.getSigAlgName());
        s.initSign((PrivateKey) ks.getKey(alias, pass.toCharArray()));
        s.update(chlg);
        sign = s.sign();
    }

    public boolean verify(X509Certificate cert, Challenge chall) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature sv = Signature.getInstance(cert.getSigAlgName());
        sv.initVerify(cert);
        sv.update(chlg);
        return sv.verify(chall.sign);
    }

    private byte[] getChallenge() {
        return chlg;
    }
    
    private byte[] getSignature() {
        return sign;
    }
}
