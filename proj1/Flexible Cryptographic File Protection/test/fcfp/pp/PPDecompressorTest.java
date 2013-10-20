package fcfp.pp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Simão
 */
public class PPDecompressorTest {

    /**
     * Test of decompressEncryptionPP method, of class PPDecompressor.
     */
    @Test
    public void testDecompressEncryptionPP() throws Exception {

        File ppsFolder; // The system's protection plugin folder.
        File[] ppsFolderContent; // The protection plugin folder entries.
        JarFile ppJar; // A protection plugin folder's jar file entry.
        Enumeration<JarEntry> ppJarEntries; // The entries list of a jar file.
        JarEntry entry; // A entry of the current jar file in the query.
        String ppName; // Name of the protection plugin.
        Class<?> pp; // A protection plugin yet to be validated.
        InputStream is; // a generic inputstream.
        byte[] data; // a class file serialization.
        int offset;
        int numRead;

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

                while (ppJarEntries.hasMoreElements()) {
                    entry = ppJarEntries.nextElement();

                    // Filtering the jar entries. We only want class files.
                    if (entry.getName().endsWith(".class")) {
                        ppName = entry.getName().replaceAll(".class", "").replaceAll("/", ".");

                        System.out.println(ppName);

                        try {
                            is = ppJar.getInputStream(entry);
                            data = new byte[is.available()];
                            offset = 0;
                            numRead = 0;
                            while (offset < data.length && (numRead = is.read(data, offset, data.length - offset)) >= 0) {
                                offset += numRead;
                            }

                        } catch (IOException ex) {
                            System.out.println(ex);
                            continue;
                        }

//                        File f2 = new File("files/pp/AES.class");
//                        FileOutputStream fos2 = new FileOutputStream(f2);
//                        fos2.write(data);

//                        byte[] data2;
//                        File f = new File("files/pp/AES_1.class");
//                        try (FileInputStream fis = new FileInputStream(f)) {
//                            data2 = new byte[fis.available()];
//                            fis.read(data2);
//                        }
//                        System.out.println(data.length);
//                        System.out.println(data2.length);

//                        assertArrayEquals(data2, data);

                        EncryptionPP aes = PPDecompressor.getInstance().decompressEncryptionPP(ppName, data);
                        String text = "Ola como nhg nhg";
                        String pass = "3hd7s6h4453dgtredgdfgdfgdfg";
                        byte[] msg = text.getBytes();
                        System.out.println("length: " + msg.length + " " + "length: " + text.length() + " " + text);
                        byte[] key = pass.getBytes();
                        byte[] result = aes.cipher(msg, key);
                        System.out.println("length: " + result.length + " " + "length: " + new String(result).length() + " " + new String(result));
                        result = aes.decipher(result, key);
                        System.out.println("length: " + result.length + " " + "length: " + new String(result).length() + " " + new String(result));
                        assertArrayEquals(msg, result);
                        return;
                    }
                }
            }
        }
    }
}