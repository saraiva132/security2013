package fcfp.pp;

/**
 * Protection Plugin Generic Exception.
 *
 * @author Simão Paulo Rato Alves Reis
 * @version 1.1
 */
public class ProtectionPluginException extends Exception {

    /**
     * Default Exception Constructor.
     */
    public ProtectionPluginException() {
    }

    /**
     * Default Exception Constructor with message error.
     */
    public ProtectionPluginException(String err) {
        super(err);
    }
}
