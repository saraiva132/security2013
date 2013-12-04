package mmaa.net;

import javax.crypto.Cipher;

/**
 *
 * @author Simão Paulo Rato Alves Reis
 */
public class SessionKey extends ProtocolContainer {
    
    private final Cipher cipher;
    
    public SessionKey(Cipher cipher) {
        this.cipher = cipher;
    }
    
    @Override
    public String toString() {
        return super.toString() + "Message Type: " + getType().name();
    }

    @Override
    public ContainerType getType() {
        return ContainerType.SESSION;
    }
    
    public Cipher getCipher() {
        return cipher;
    }
}
