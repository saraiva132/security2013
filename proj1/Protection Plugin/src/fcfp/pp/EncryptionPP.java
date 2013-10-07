package fcfp.pp;

/**
 * Generic cypher/decypher protection plugin header.
 *
 * @author Simão Paulo Rato Alves Reis
 * @version 1.0
 */
public interface EncryptionPP {

    /**
     * Cypher algorithm's header.
     *
     * @param msg message to be encrypted.
     * @param key cypher key.
     * @throws ProtectionPluginException when something went wrong.
     */
    void cypher(byte[] msg, byte[] key) throws ProtectionPluginException;

    /**
     * Decypher algorithm's header.
     *
     * @param msg message to be decrypted.
     * @param key decypher key.
     * @throws ProtectionPluginException when something went wrong.
     */
    void decypher(byte[] msg, byte[] key) throws ProtectionPluginException;
}
