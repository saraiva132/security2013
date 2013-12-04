package mmaa.net;

import java.io.Serializable;

/**
 *
 * @author Simão Paulo Rato Alves Reis
 */
public abstract class ProtocolContainer implements Serializable {
    
    @Override
    public String toString() {
        return "Multi-method authentication architecture Protocol\n";
    }
    
    public abstract ContainerType getType();
}
