package fcfp.pp;

/**
 * Generic data integrity protection plugin header.
 *
 * @author Simão Paulo Rato Alves Reis
 * @version 1.0
 */
public interface IntegrityPP {

    /**
     * MAC signature algorithm's header.
     *
     * @param msg message to be signed.
     * @param key signature key.
     * @return message's signature.
     * @throws ProtectionPluginException when something went wrong.
     */
    byte[] sign(byte[] msg, byte[] key) throws ProtectionPluginException;
}
