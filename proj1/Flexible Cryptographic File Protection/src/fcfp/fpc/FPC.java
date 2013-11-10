/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fcfp.fpc;

import fcfp.util.Pad;
import fcfp.png.BufferedPNG;
import fcfp.png.InvalidPNGImageSizeException;
import fcfp.pp.EncryptionPP;
import fcfp.pp.IntegrityPP;
import fcfp.pp.PPDecompressor;
import fcfp.pp.PPEngine;
import fcfp.pp.ProtectionPluginException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author rafael
 */
public class FPC {

    private EncryptionPP encryption;
    private IntegrityPP integrity;
    private final byte[] key;
    private byte[] dummyKey;
    private String source;
    private String sourceDummy;
    private String sourceSteganography;
    private final String output;
    private boolean steganography = false;
    private boolean dummy = false;
    private String PPencName;
    private String PPintName;
    private byte[] file1, file2;
    private Header head1, head2;
    private int offset1, offset2;
    private String extension;
    private String extensionDummy;

    /**
     * Folder temp generated to store temporary files. Deleted at the end.
     *
     * @param source Entry file Path.
     * @param output Output file Path
     * @param key Key to use.
     */
    public FPC(String source, String output, byte[] key) {
        this.key = key;
        this.source = source;
        this.output = output;
        int lastindex = source.lastIndexOf(".");
        extensionDummy = "";
        extension = "";
        if (lastindex != -1) {
            extension = source.substring(lastindex);
        }
        File folder = new File("temp");
        if (!folder.exists()) {
            folder.mkdir();
        }
    }

    /**
     * This method deals with the instantiation of the secondary and primary
     * content. If the user does not provide a secondary content path it will be
     * replaced with trash.
     *
     * @return Return true if successful else return error message pop-up.
     */
    public boolean CipherRun() {
        int len;
        FileInputStream in;
        try {
            in = new FileInputStream(source);
            try {
                file1 = new byte[in.available()];
                while ((len = in.read(file1)) > 0) {
                    in.read(file1, 0, len);
                }
                in.close();
            } catch (IOException ex) {
                errorMessage("Error opening file.");
                return false;
            }
        } catch (FileNotFoundException ex) {
            errorMessage("The selected file does not exist.");
            return false;
        }
        if (!dummy) {
            dummyKey = new byte[key.length];
            new Random().nextBytes(dummyKey);
            file2 = getDummy(file1.length);
        } else {
            try {
                int lastindex = sourceDummy.lastIndexOf(".");

                if (lastindex != -1) {
                    extensionDummy = sourceDummy.substring(lastindex);
                }
                in = new FileInputStream(sourceDummy);
                try {
                    file2 = new byte[in.available()];
                    while ((len = in.read(file2)) > 0) {
                        in.read(file2, 0, len);
                    }
                    in.close();
                } catch (IOException ex) {
                    errorMessage("Error opening file.");
                    return false;
                }
            } catch (FileNotFoundException ex) {
                errorMessage("The selected file doesn't exist.");
                return false;
            }
        }

        return Cipher();
    }

    /**
     * Encryption algorithm
     *
     * @return Return true if successful else return error message pop-up.
     */
    private boolean Cipher() {
        if (extension.compareTo("") == 0) {
            extension = "d3r5s5";
        }
        if (extensionDummy.compareTo("") == 0) {
            extensionDummy = "d3r5s6";
        }
        System.out.println(":::" + extension);
        System.out.println(extensionDummy);
        toZip(extension, PPintName, "temp/zip1", PPEngine.getInstance().getIntegrityPPSerialization(PPintName), file1);
        toZip(extensionDummy, PPintName, "temp/zip2", PPEngine.getInstance().getIntegrityPPSerialization(PPintName), file2);

        byte[] macReal;
        byte[] macDummy;
        try {
            macReal = integrity.sign(file1, key);
            macDummy = integrity.sign(file2, dummyKey);
        } catch (ProtectionPluginException ex) {
            errorMessage("Malformed Integrity Protection Plugin.");
            return false;
        }

        byte[] zipToEnc1;
        byte[] zipToEnc2;
        int len;

        try {
            FileInputStream in = new FileInputStream("temp/zip1");

            zipToEnc1 = new byte[getOffset(offset1 = in.available())];
            //System.out.println("Tamanho: " + zipToEnc1.length + " . PadPos: " + offset1);
            while ((len = in.read(zipToEnc1)) > 0) {
                in.read(zipToEnc1, 0, len);
            }
            in.close();

            in = new FileInputStream("temp/zip2");

            zipToEnc2 = new byte[getOffset(offset2 = in.available())];
            while ((len = in.read(zipToEnc2)) > 0) {
                in.read(zipToEnc2, 0, len);
            }
            in.close();
        } catch (IOException ex) {
            errorMessage("Something went wrong.");
            return false;
        }
        if (zipToEnc1.length > zipToEnc2.length) {
            byte[] temp = new byte[zipToEnc1.length];
            System.arraycopy(zipToEnc2, 0, temp, 0, zipToEnc2.length);
            zipToEnc2 = temp;
        } else if (zipToEnc2.length > zipToEnc1.length) {
            byte[] temp = new byte[zipToEnc2.length];
            System.arraycopy(zipToEnc1, 0, temp, 0, zipToEnc1.length);
            zipToEnc1 = temp;
        }
        Pad.fill(zipToEnc1, offset1, zipToEnc1.length);
        Pad.fill(zipToEnc2, offset2, zipToEnc2.length);
        try {
            zipToEnc1 = encryption.cipher(zipToEnc1, key);
            zipToEnc2 = encryption.cipher(zipToEnc2, dummyKey);
        } catch (ProtectionPluginException ex) {
            errorMessage("Malformed Encryption Protection Plugin.");
            return false;
        }
        byte[] toEnc = new byte[zipToEnc1.length + zipToEnc2.length];
        System.arraycopy(zipToEnc2, 0, toEnc, 0, zipToEnc2.length);
        System.arraycopy(zipToEnc1, 0, toEnc, zipToEnc2.length, zipToEnc1.length);
        head2 = new Header((long) offset1, macReal);
        head1 = new Header((long) offset2, macDummy);
        byte[] encHead1 = head1.getStream();
        byte[] encHead2 = head2.getStream();
        try {
            encHead1 = encryption.cipher(encHead1, dummyKey);
            encHead2 = encryption.cipher(encHead2, key);
        } catch (ProtectionPluginException ex) {
            errorMessage("Malformed Encryption Protection Plugin.");
            return false;
        }
        toZip("d3r5s6", PPencName, "files/" + output + ".zip", PPEngine.getInstance().getEncryptionPPSerialization(PPencName), encHead1, encHead2, toEnc);

        if (steganography) {
            BufferedPNG toIMG;
            try {
                toIMG = new BufferedPNG(sourceSteganography);
            } catch (IOException ex) {
                errorMessage("Problem opening image.");
                return false;
            }
            try {
                toIMG.encode("files/" + output + ".zip", "files/" + output + ".png");
            } catch (IOException ex) {
                errorMessage("Something is wrong with the message.");
                return false;
            } catch (InvalidPNGImageSizeException ex) {
                errorMessage("Image should be at least 8 times \n larger than the input files.");
                return false;
            }
        }
        deleteFolder(new File("temp"));
        return true;
    }

    /**
     * Decipher algorithm, checks input for .png extension. If true runs
     * steganography algorithm.
     *
     * @return Return true if successful else return error message pop-up.
     */
    public boolean DeCipher() {
        if (source.endsWith(".png")) {
            BufferedPNG toIMG;
            try {
                System.out.println("A procura da imagem:" + source);
                toIMG = new BufferedPNG(source);
            } catch (IOException ex) {
                errorMessage("Image not found.");
                return false;
            }
            try {
                toIMG.decode("temp/temp.zip");
            } catch (IOException | ArrayIndexOutOfBoundsException ex) {
                errorMessage("Wrong password, corrupted file, or not a File Protection Container.");
                return false;
            }
            source = "temp/temp.zip";
        }
        UnZip unzip = new UnZip(source);
        try {
            unzip.run();
        } catch (IOException ex) {
            errorMessage("Wrong password, corrupted file, or not a File Protection Container.");
            return false;
        }
        try {
            encryption = PPDecompressor.getInstance().decompressEncryptionPP(unzip.getName(0), unzip.getEntry(0));
        } catch (InstantiationException | IllegalAccessException ex) {
            errorMessage("Wrong password, corrupted file, or not a File Protection Container.");
            System.out.println(ex);
            return false;
        }
        byte[] content = unzip.getEntry(3);
        try {
            head1 = new Header(encryption.decipher(unzip.getEntry(1), key));
            head2 = new Header(encryption.decipher(unzip.getEntry(2), key));
        } catch (ProtectionPluginException ex) {
            errorMessage("Wrong password, corrupted file, or not a File Protection Container.");
            return false;
        }
        //System.out.println("Headers createad....");
        if (head1.checksum()) {
            //System.out.println("Header Dummy Checksum GOOD");
            return DecipherZip(head1, content, true);
        } else if (head2.checksum()) {
            //System.out.println("Header Content Checksum GOOD");
            return DecipherZip(head2, content, false);
        } else {
            errorMessage("Wrong password, corrupted file, or not a File Protection Container.");
            return false;
        }
    }

    /**
     * we receive the header, flag to determine the content and the full
     * content. We extract from the header the padPosition to know where the zip
     * ends. If mac checks the file will be written in the output path.
     *
     * @param header This was the successfully deciphered header.
     * @param content Full content(Secondary and primary).
     * @param identity Flag to determine which content to decipher.
     * @return Return true if successful else return error message pop-up.
     */
    private boolean DecipherZip(Header header, byte[] content, boolean identity) {
        UnZip Unzip;
        byte[] zip = new byte[content.length / 2];
        byte[] toZipp = new byte[(int) header.getPadPos()];

        //System.out.println("Choosing Content to Unzip...");
        if (identity) {
            System.arraycopy(content, 0, zip, 0, content.length / 2);
            try {
                zip = encryption.decipher(zip, key);
            } catch (ProtectionPluginException ex) {
                errorMessage("Wrong password, corrupted file, or not a File Protection Container.");
                return false;
            }
            System.arraycopy(zip, 0, toZipp, 0, (int) (header.getPadPos()));
            try {
                Unzip = new UnZip(toZipp);
            } catch (IOException ex) {
                errorMessage("Wrong password, corrupted file, or not a File Protection Container.");
                return false;
            }
            try {
                Unzip.run();
            } catch (IOException ex) {
                errorMessage("Wrong password, corrupted file, or not a File Protection Container.");
                return false;
            }
        } else {
            System.arraycopy(content, content.length / 2, zip, 0, content.length / 2);
            try {
                zip = encryption.decipher(zip, key);
            } catch (ProtectionPluginException ex) {
                errorMessage("Wrong password, corrupted file, or not a File Protection Container.");
                return false;
            }
            System.arraycopy(zip, 0, toZipp, 0, (int) (header.getPadPos()));
            try {
                Unzip = new UnZip(toZipp);
            } catch (IOException ex) {
                errorMessage("Wrong password, corrupted file, or not a File Protection Container.");
                return false;
            }
            //System.out.println(header.getPadPos() + " : " + content.length / 2 + " : " + zip.length + " : " + toZipp.length);
            try {
                Unzip.run();
            } catch (IOException ex) {
                deleteFolder(new File("temp"));
                errorMessage("Wrong password, corrupted file, or not a File Protection Container.");
                return false;
            }
        }
        try {
            integrity = PPDecompressor.getInstance().decompressIntegrityPP(Unzip.getName(0), Unzip.getEntry(0));
        } catch (InstantiationException | IllegalAccessException ex) {
            errorMessage("Wrong password, corrupted file, or not a File Protection Container.");
            return false;
        }
        byte[] mac;
        try {
            mac = integrity.sign(Unzip.getEntry(1), key);
        } catch (ProtectionPluginException ex) {
            errorMessage("Wrong password, corrupted file, or not a File Protection Container.");
            return false;
        }
        byte[] macToCheck = header.getMac();

        if (mac.length == macToCheck.length) {
            for (int i = 0; i < mac.length; i++) {
                if (mac[i] != macToCheck[i]) {
                    //System.out.println("OMG: MAC NÂO SE VERIFICA");
                    errorMessage("Wrong password, corrupted file, or not a File Protection Container.");
                    return false;
                }
            }
            String extension = Unzip.getName(1);
            if (!extension.startsWith(".")) {
                extension = "";
            }
            try {
                Unzip.writeZip("files/" + output + extension);
            } catch (IOException ex) {
                errorMessage("Wrong password, corrupted file, or not a File Protection Container.");
                return false;
            }
        } else {
            errorMessage("Wrong password, corrupted file, or not a File Protection Container.");
            return false;
        }
        deleteFolder(new File("temp"));
        return true;
    }

    /**
     * Method that deals with the zipping calls.
     *
     * @param entryName Name of the file
     * @param name PP name. We need this in order to assure we know which PP we
     * are using in decipher.
     * @param outs Output path;
     * @param oi List of zip entries(files)
     */
    public void toZip(String entryName, String name, String outs, byte[]  
        ... oi) {
        List<File> fields = new ArrayList<>();
        File zip;
        int i = 0;
        for (byte[] b : oi) {
            if (i == 0) {
                zip = new File("temp/" + name);
            } else if (i == 1) {
                zip = new File("temp/" + entryName);
            } else {
                zip = new File("temp/" + entryName + i);
            }
            i++;
            try (FileOutputStream out = new FileOutputStream(zip)) {

                out.write(b);

            } catch (IOException ex) {
                errorMessage("Something went wrong.");
                return;
            }
            fields.add(zip);
        }

        Zip zipit = new Zip(outs, fields);
        try {
            zipit.run();
        } catch (IOException ex) {
            errorMessage("Something went wrong.");
        }
    }

    /**
     *
     * @param size creates trash with size bytes
     * @return returns trash content
     */
    public byte[] getDummy(int size) {
        byte[] dummyContent = new byte[size];
        new Random().nextBytes(dummyContent);
        return dummyContent;
    }

    /**
     *
     * @param toBeOrNotToBe Pads content to the next potency of 2.
     * @return size.
     */
    public int getOffset(int toBeOrNotToBe) {
        int toBe = 1;

        while (toBe < toBeOrNotToBe) {
            toBe = toBe << 1;
        }
        return toBe;
    }

    /**
     *
     * @param name set encryption PP to use and instance it.
     */
    public void setPPenc(String name) {
        this.PPencName = name;
        this.encryption = PPEngine.getInstance().getEncryptionPP(name);
    }

    /**
     *
     * @param name set integrity PP to use and instance it.
     */
    public void setPPint(String name) {
        this.PPintName = name;
        this.integrity = PPEngine.getInstance().getIntegrityPP(name);
    }

    /**
     *
     * @param fonte set if user wants to use steganography and set image path
     */
    public void setStega(String fonte) {
        sourceSteganography = fonte;
        steganography = true;
    }

    /**
     *
     * @param fonte set if user wants secondary content.
     * @param key set secondary content key
     */
    public void setDummy(String fonte, byte[] key) {
        sourceDummy = fonte;
        dummy = true;
        dummyKey = key;
    }

    /**
     * Used to delete temporary folder and it's files.
     *
     * @param folder directory to delete.
     */
    private void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if (files != null) { //some JVMs return null for empty dirs
            for (File f : files) {
                if (f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }
        folder.delete();
    }

    /**
     * Error treatment.
     *
     * @param message error message
     */
    private void errorMessage(String message) {
        deleteFolder(new File("temp"));
        JOptionPane.showMessageDialog(new JFrame(), message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
