package mmaa.net;

import java.security.cert.X509Certificate;

/**
 *
 * @author Simão Paulo Rato Alves Reis
 */
public class JKSCertificate extends ProtocolContainer {
    
    private final X509Certificate cert;
    
    public JKSCertificate(X509Certificate cert) {
        this.cert = cert;
    }
    
    @Override
    public String toString() {
        return super.toString() + "Message Type: " + getType().name() + "\n"
                + "Certificate:\n" + getCertificate();
    }

    @Override
    public ContainerType getType() {
        return ContainerType.JKS_CERTIFICATE;
    }
    
    public X509Certificate getCertificate() {
        return cert;
    }
}
