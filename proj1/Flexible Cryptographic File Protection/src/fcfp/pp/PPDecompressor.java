package fcfp.pp;

/**
 * A Protection Plugin loader module, using the singleton pattern.
 *
 * @author Simão Paulo Rato Alves Reis
 * @version 1.0
 */
public class PPDecompressor {

    private static PPDecompressor ppCompressor;
    private static ByteClassLoader bcl;

    /**
     * Initializes the internal PPDecompressor and ByteClassLoader.
     */
    private PPDecompressor() {

        bcl = new ByteClassLoader();
    }

    /**
     * Initialize singleton instance.
     *
     * @return PPDecompressor singelton instance.
     */
    public static PPDecompressor getInstance() {

        if (ppCompressor == null) {
            ppCompressor = new PPDecompressor();
        }
        return ppCompressor;
    }

    /**
     * Decompresses a Encryption Protection Plugin from a byte stream.
     *
     * @param ppName the Protection Plugin class name.
     * @param stream the Protection Plugin serialization.
     * @return the Encryption Protection Plugin.
     * @throws ClassNotFoundException when the classByets don't contain any
     * class with the given name.
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public EncryptionPP decompressEncryptionPP(String ppName, byte[] stream) throws ClassNotFoundException, InstantiationException, IllegalAccessException {

        Class<?> pp = bcl.defineClass(ppName, stream);
        return (EncryptionPP) pp.newInstance();
    }

    /**
     * Decompresses a Integrity Protection Plugin from a byte stream.
     *
     * @param ppName the Protection Plugin class name.
     * @param stream the Protection Plugin serialization.
     * @return the Integrity Protection Plugin.
     * @throws ClassNotFoundException when the classByets don't contain any
     * class with the given name.
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public IntegrityPP decompressIntegrityPP(String ppName, byte[] stream) throws ClassNotFoundException, InstantiationException, IllegalAccessException {

        Class<?> pp = bcl.defineClass(ppName, stream);
        return (IntegrityPP) pp.newInstance();
    }
}
