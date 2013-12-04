package mmaa;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

/**
 *
 * @author Simão Paulo Rato Alves Reis
 */
public class TestPT {

    public static void main(String... args) throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException, InvalidKeyException, UnrecoverableKeyException, SignatureException {
        KeyStore ks = KeyStore.getInstance("jceks");
        ks.load(new FileInputStream("keyStore/SecurityCA.jks"), "security".toCharArray());
        //ks.load(new FileInputStream("keyStore/PT.jks"), "security".toCharArray());
        Enumeration aliases = ks.aliases();
        while (aliases.hasMoreElements()) {
            String name = (String) aliases.nextElement();
            System.out.println("Certificate : " + name);
        }
        X509Certificate cert = (X509Certificate) ks.getCertificate("SecurityCA");
        //X509Certificate cert = (X509Certificate) ks.getCertificate("ec de autenticação do cartão de cidadão 0004 (cartão de cidadão 001)");
        System.out.println(cert);
        Signature s = Signature.getInstance(cert.getSigAlgName());
        s.initSign((PrivateKey) ks.getKey("SecurityCA", "security".toCharArray()));
        //s.initSign((PrivateKey) ks.getKey("ec de autenticação do cartão de cidadão 0004 (cartão de cidadão 001)", "security".toCharArray()));
        String msg = "Babel";
        s.update(msg.getBytes());
        byte[] text_cipher = s.sign();
        Signature sv = Signature.getInstance(cert.getSigAlgName());
        sv.initVerify(cert);
        sv.update(msg.getBytes());
        System.out.println(sv.verify(text_cipher));
    }
}
