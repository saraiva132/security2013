package fcfp.pp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Flexible Cryptographic File Protection module that loads and manages
 * Protection Plugins (PPs).
 *
 * @author Simão Paulo Rato Alves Reis
 * @version 1.0
 */
public class PPEngine {

    /**
     * A protection plugin container data structure which holds the Encryption
     * Protection Plugin and it's serialization form.
     */
    private class EncryptionPPContainer {

        private EncryptionPP encryptionPP;
        private byte[] stream;

        /**
         * Creates a new container for the Encryption Protection Plugin.
         *
         * @param encryptionPP the Encryption Protection Plugin.
         * @param stream the Encryption Protection Plugin serialization.
         */
        EncryptionPPContainer(EncryptionPP encryptionPP, byte[] stream) {

            this.encryptionPP = encryptionPP;
            this.stream = stream;
        }

        /**
         * Get the saved Encryption Protection Plugin.
         *
         * @return the saved Encryption Protection Plugin.
         */
        EncryptionPP getEncryptionPP() {
            return encryptionPP;
        }

        /**
         * Get the saved Encryption Protection Plugin serialization.
         *
         * @return the saved Encryption Protection Plugin serialization.
         */
        byte[] getByteStream() {
            return stream;
        }
    }

    /**
     * A protection plugin container data structure which holds the Integrity
     * Protection Plugin and it's serialization form.
     */
    private class IntegrityPPContainer {

        private IntegrityPP integrityPP;
        private byte[] stream;

        /**
         * Creates a new container for the Integrity Protection Plugin.
         *
         * @param integrityPP the Encryption Protection Plugin.
         * @param stream the Encryption Protection Plugin serialization.
         */
        IntegrityPPContainer(IntegrityPP integrityPP, byte[] stream) {

            this.integrityPP = integrityPP;
            this.stream = stream;
        }

        /**
         * Get the saved Integrity Protection Plugin.
         *
         * @return the saved Integrity Protection Plugin.
         */
        IntegrityPP getIntegrityPP() {
            return integrityPP;
        }

        /**
         * Get the saved Integrity Protection Plugin serialization.
         *
         * @return the saved Integrity Protection Plugin serialization.
         */
        byte[] getByteStream() {
            return stream;
        }
    }
    private static PPEngine ppEngine; // Singleton instance.
    private static HashMap<String, EncryptionPPContainer> encryptionPPs; // Mapping between PPs name's and PPs.
    private static HashMap<String, IntegrityPPContainer> integrityPPs; // Mapping between PPs name's and PPs.

    /**
     * Initializes all the PPEngine global attributes.
     */
    private PPEngine() {

        encryptionPPs = new HashMap<>();
        integrityPPs = new HashMap<>();
    }

    /**
     * Initialize singleton instance.
     *
     * @return PPEngine singelton instance.
     */
    public static PPEngine getInstance() {

        if (ppEngine == null) {
            ppEngine = new PPEngine();
        }
        return ppEngine;
    }

    /**
     * Load's all ".class" files in all ".jar" files in the system plugins
     * directory. Only the ".class" files that implements EncryptionPP and/or
     * IntegrityPP are loaded. Can be used multiple times to reload or load new
     * plugins at runtime.
     */
    public void loadPPs() {

        File ppsFolder; // The system's protection plugin folder.
        File[] ppsFolderContent; // The protection plugin folder entries.
        JarFile ppJar; // A protection plugin folder's jar file entry.
        Enumeration<JarEntry> ppJarEntries; // The entries list of a jar file.
        JarEntry entry; // A entry of the current jar file in the query.
        String ppName; // Name of the protection plugin.
        URLClassLoader classLoader; // A generic class loader.
        Class<?> pp; // A protection plugin yet to be validated.
        Object ppInstance; // A protection plugin instance.
        InputStream is; // a generic inputstream.
        byte[] data; // a class file serialization.

        // Query to the system's protection plugin folder entries.
        ppsFolder = new File("plugins");
        System.out.println(ppsFolder.getAbsolutePath());
        ppsFolderContent = ppsFolder.listFiles();
        if (ppsFolderContent == null) {
            return;
        }

        for (File file : ppsFolderContent) {

            // Filtering the folder entries. We only want jar files.
            if (file.getName().endsWith(".jar")) {

                // Opening a jar file in the protection plugin folder.
                try {
                    ppJar = new JarFile(file);
                } catch (IOException ex) {
                    System.out.println(ex);
                    continue;
                }

                // Query to the jar file entries.
                ppJarEntries = ppJar.entries();
                if (ppJarEntries == null) {
                    continue;
                }

                try {
                    classLoader = URLClassLoader.newInstance(new URL[]{file.toURI().toURL()});
                } catch (MalformedURLException ex) {
                    System.out.println(ex);
                    continue;
                }

                while (ppJarEntries.hasMoreElements()) {
                    entry = ppJarEntries.nextElement();

                    // Filtering the jar entries. We only want class files.
                    if (entry.getName().endsWith(".class")) {
                        ppName = entry.getName().replaceAll(".class", "").replaceAll("/", ".");

                        // Loading the potential protection plugin.
                        try {
                            pp = classLoader.loadClass(ppName);
                            ppInstance = pp.newInstance();
                        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
                            System.out.println(ex);
                            break;
                        }

                        // Obtaining all the interfaces that the potential protection plugin implements.
                        for (Class<?> ppInterface : pp.getInterfaces()) {
                            if (ppInterface.equals(EncryptionPP.class) || ppInterface.equals(IntegrityPP.class)) {
                                try {
                                    // get the class serialization
                                    is = ppJar.getInputStream(entry);
                                    data = new byte[is.available()];
                                    is.read(data, 0, data.length);
                                } catch (IOException ex) {
                                    System.out.println(ex);
                                    continue;
                                }
                                if (ppInterface.equals(EncryptionPP.class)) {
                                    encryptionPPs.put(ppName, new EncryptionPPContainer((EncryptionPP) ppInstance, data));
                                }
                                if (ppInterface.equals(IntegrityPP.class)) {
                                    integrityPPs.put(ppName, new IntegrityPPContainer((IntegrityPP) ppInstance, data));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Get a loaded EncryptionPP by it's name.
     *
     * @param name The Protection Plugin name.
     * @return loaded EncryptionPP.
     */
    public EncryptionPP getEncryptionPP(String name) {

        return encryptionPPs.get(name).getEncryptionPP();
    }

    /**
     * Get a loaded IntegrityPP by it's name.
     *
     * @param name The Protection Plugin name.
     * @return loaded IntegrityPP.
     */
    public IntegrityPP getIntegrityPP(String name) {

        return integrityPPs.get(name).getIntegrityPP();
    }

    /**
     * Get a EncryptionPP serialization.
     *
     * @param name The Protection Plugin name.
     * @return the serialized class file.
     */
    public byte[] getEncryptionPPSerialization(String name) {

        return encryptionPPs.get(name).getByteStream();
    }

    /**
     * Get a IntegrityPP serialization.
     *
     * @param name The Protection Plugin name.
     * @return the serialized class file.
     */
    public byte[] getIntegrityPPSerialization(String name) {

        return integrityPPs.get(name).getByteStream();
    }

    /**
     * Get the list of all loaded EncryptionPP name's.
     *
     * @return list of all loaded EncryptionPP name's.
     */
    public Set<String> getEncryptionPPNames() {

        return encryptionPPs.keySet();
    }

    /**
     * Get the list of all loaded IntegrityPP name's.
     *
     * @return list of all loaded IntegrityPP name's.
     */
    public Set<String> getIntegrityPPNames() {

        return integrityPPs.keySet();
    }

    /**
     * Check if any EncryptionPP has been loaded.
     *
     * @return true if any EncryptionPP has been loaded. false otherwise.
     */
    public boolean hasEncryptionPPs() {

        return encryptionPPs.keySet().size() > 0;
    }

    /**
     * Check if any IntegrityPP has been loaded.
     *
     * @return true if any EncryptionPP has been loaded. false otherwise.
     */
    public boolean hasIntegrityPPs() {

        return integrityPPs.keySet().size() > 0;
    }
}
