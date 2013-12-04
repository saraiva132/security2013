package mmaa;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import sun.security.pkcs11.SunPKCS11;

/**
 *
 * @author Simão Paulo Rato Alves Reis
 */
public class TestCC {

    public static void main(String... args) throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnrecoverableKeyException, SignatureException {
        Provider prov = new SunPKCS11("config/pkcs11.cfg");
        //Security.addProvider(prov);
        char[] pin = {'8', '8', '6', '3'};
        KeyStore cc;
        cc = KeyStore.getInstance("PKCS11", prov);
        cc.load(null, pin);
        Enumeration aliases = cc.aliases();
        while (aliases.hasMoreElements()) {
            String name = (String) aliases.nextElement();
            System.out.println("Certificate : " + name);
        }
        X509Certificate cert = (X509Certificate) cc.getCertificate("CITIZEN AUTHENTICATION CERTIFICATE");
        
        // *******************************************************
        
        KeyStore ks = KeyStore.getInstance("jceks");
        ks.load(new FileInputStream("keyStore/PT.jks"), "security".toCharArray());
        aliases = ks.aliases();
        while (aliases.hasMoreElements()) {
            String name = (String) aliases.nextElement();
            System.out.println("Certificate : " + name);
        }
        
        Signature s = Signature.getInstance(cert.getSigAlgName());
        s.initSign((PrivateKey) cc.getKey("CITIZEN AUTHENTICATION CERTIFICATE", null));
        String msg = "Babel";
        s.update(msg.getBytes());
        byte[] text_cipher = s.sign();
        
        
        
        X509Certificate certPT = (X509Certificate) ks.getCertificate("ec de autenticação do cartão de cidadão 0004 (cartão de cidadão 001)");
        System.out.println(certPT);
        Signature sv = Signature.getInstance(certPT.getSigAlgName());
        sv.initVerify(certPT);
        sv.update(msg.getBytes());
        System.out.println(sv.verify(text_cipher));
        
    }
}
