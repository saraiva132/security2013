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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author rafael
 */
public class FPC {

    private EncryptionPP encryption;
    private IntegrityPP integrity;
    private byte[] key;
    private byte[] dummyKey;
    private String source;
    private String sourceDummy;
    private String sourceSteganography;
    private String output;
    private boolean steganography = false;
    private boolean dummy = false;
    private String PPencName;
    private String PPintName;
    private byte[] file1, file2;
    private Header head1, head2;
    private int offset1, offset2;

    public FPC(String source, String output, byte[] key) {
        this.key = key;
        this.source = source;
        this.output = output;
        File folder = new File("temp");
        if (!folder.exists()) {
            folder.mkdir();
        }
    }

    public void CipherRun() {
        try {
            FileInputStream in = new FileInputStream(source);
            try {
                file1 = new byte[in.available()];
                int len;
                while ((len = in.read(file1)) > 0) {
                    in.read(file1, 0, len);
                }
                in.close();
            } catch (IOException ex) {
                deleteFolder(new File("temp"));
                JOptionPane.showMessageDialog(new JFrame(), "Error opening file.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (FileNotFoundException ex) {
            deleteFolder(new File("temp"));
            JOptionPane.showMessageDialog(new JFrame(), "The selected file doesn't exist.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!dummy) {
            dummyKey = new byte[key.length];
            new Random().nextBytes(dummyKey);
            file2 = getDummy(file1.length);
        } else {
            try {
                FileInputStream in = new FileInputStream(sourceDummy);
                try {
                    file2 = new byte[in.available()];
                    int len;
                    while ((len = in.read(file2)) > 0) {
                        in.read(file2, 0, len);
                    }
                    in.close();
                } catch (IOException ex) {
                    deleteFolder(new File("temp"));
                    JOptionPane.showMessageDialog(new JFrame(), "Error opening file.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (FileNotFoundException ex) {
                deleteFolder(new File("temp"));
                JOptionPane.showMessageDialog(new JFrame(), "The selected file doesn't exist.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        Cipher();
    }

    private void Cipher() {
        toZip(PPintName, "temp/zip1", PPEngine.getInstance().getIntegrityPPSerialization(PPintName), file1);
        toZip(PPintName, "temp/zip2", PPEngine.getInstance().getIntegrityPPSerialization(PPintName), file2);

        byte[] macReal;
        byte[] macDummy;
        try {
            macReal = integrity.sign(file1, key);
            macDummy = integrity.sign(file2, dummyKey);
        } catch (ProtectionPluginException ex) {
            deleteFolder(new File("temp"));
            JOptionPane.showMessageDialog(new JFrame(), "Malformed Integrity Protection Plugin.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        byte[] zipToEnc1;
        byte[] zipToEnc2;
        int len;
        FileInputStream in;
        try {
            in = new FileInputStream("temp/zip1");

            zipToEnc1 = new byte[getOffset(offset1 = in.available())];
            System.out.println("Tamanho: " + zipToEnc1.length + " . PadPos: " + offset1);
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
            deleteFolder(new File("temp"));
            JOptionPane.showMessageDialog(new JFrame(), "Something went wrong.", "Error", JOptionPane.ERROR_MESSAGE);
            return;

        }

        Pad.fill(zipToEnc1, offset1, zipToEnc1.length);
        Pad.fill(zipToEnc2, offset2, zipToEnc2.length);
        try {
            zipToEnc1 = encryption.cipher(zipToEnc1, key);
            zipToEnc2 = encryption.cipher(zipToEnc2, dummyKey);
        } catch (ProtectionPluginException ex) {
            deleteFolder(new File("temp"));
            JOptionPane.showMessageDialog(new JFrame(), "Malformed Encryption Protection Plugin.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
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
            deleteFolder(new File("temp"));
            JOptionPane.showMessageDialog(new JFrame(), "Malformed Encryption Protection Plugin.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        toZip(PPencName, "files/" + output + ".zip", PPEngine.getInstance().getEncryptionPPSerialization(PPencName), encHead1, encHead2, toEnc);
        System.out.println(PPEngine.getInstance().getEncryptionPPSerialization(PPencName).length);

        if (steganography) {
            BufferedPNG toIMG;
            try {
                System.out.println("A procura da imagem:" + sourceSteganography);
                toIMG = new BufferedPNG(sourceSteganography);
            } catch (IOException ex) {
                deleteFolder(new File("temp"));
                JOptionPane.showMessageDialog(new JFrame(), "Problem opening image.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                toIMG.encode("files/" + output + ".zip", "files/" + output + ".png");
            } catch (IOException ex) {
                deleteFolder(new File("temp"));
                JOptionPane.showMessageDialog(new JFrame(), "Something is wrong with the image.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            } catch (InvalidPNGImageSizeException ex) {
                deleteFolder(new File("temp"));
                JOptionPane.showMessageDialog(new JFrame(), "Image should be at least 8 times \n larger than the input files.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

        }
        deleteFolder(new File("temp"));
    }

    public void DeCipher() {
        if (source.endsWith(".png")) {
            BufferedPNG toIMG = null;
            try {
                System.out.println("A procura da imagem:" + source);
                toIMG = new BufferedPNG(source);
            } catch (IOException ex) {
                deleteFolder(new File("temp"));
                JOptionPane.showMessageDialog(new JFrame(), "Image not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                toIMG.decode("temp/temp.zip");
            } catch (IOException | ArrayIndexOutOfBoundsException ex) {
                deleteFolder(new File("temp"));
                JOptionPane.showMessageDialog(new JFrame(), "Wrong password, corrupted file, or not a File Protection Container.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            source = "temp/temp.zip";
        }
        UnZip unzip = new UnZip(source);
        try {
            unzip.run();
        } catch (IOException ex) {
            deleteFolder(new File("temp"));
            JOptionPane.showMessageDialog(new JFrame(), "Wrong password, corrupted file, or not a File Protection Container.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            encryption = PPDecompressor.getInstance().decompressEncryptionPP(unzip.getName(0), unzip.getEntry(0));
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoClassDefFoundError | ClassFormatError ex) {
            deleteFolder(new File("temp"));
            JOptionPane.showMessageDialog(new JFrame(), "Wrong password, corrupted file, or not a File Protection Container.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        byte[] content = unzip.getEntry(3);
        try {
            head1 = new Header(encryption.decipher(unzip.getEntry(1), key));
            head2 = new Header(encryption.decipher(unzip.getEntry(2), key));
        } catch (ProtectionPluginException ex) {
            deleteFolder(new File("temp"));
            JOptionPane.showMessageDialog(new JFrame(), "Wrong password, corrupted file, or not a File Protection Container.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        System.out.println("Headers createad....");
        if (head1.checksum()) {
            System.out.println("Header Dummy Checksum GOOD");
            DecipherZip(head1, content, true);
        } else if (head2.checksum()) {
            System.out.println("Header Content Checksum GOOD");
            DecipherZip(head2, content, false);
        } else {
            deleteFolder(new File("temp"));
            JOptionPane.showMessageDialog(new JFrame(), "Wrong password, corrupted file, or not a File Protection Container.", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void DecipherZip(Header header, byte[] content, boolean identity) {
        UnZip Unzip;
        byte[] zip = new byte[content.length / 2];
        byte[] toZipp = new byte[(int) header.getPadPos()];

        System.out.println("Choosing Content to Unzip...");

        if (identity) {
            System.arraycopy(content, 0, zip, 0, content.length / 2);
            try {
                zip = encryption.decipher(zip, key);
            } catch (ProtectionPluginException ex) {
                deleteFolder(new File("temp"));
                JOptionPane.showMessageDialog(new JFrame(), "Wrong password, corrupted file, or not a File Protection Container.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            System.arraycopy(zip, 0, toZipp, 0, (int) (header.getPadPos()));
            try {
                Unzip = new UnZip("files/" + output, toZipp);
            } catch (IOException ex) {
                deleteFolder(new File("temp"));
                JOptionPane.showMessageDialog(new JFrame(), "Wrong password, corrupted file, or not a File Protection Container.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                Unzip.run();
            } catch (IOException ex) {
                deleteFolder(new File("temp"));
                JOptionPane.showMessageDialog(new JFrame(), "Wrong password, corrupted file, or not a File Protection Container.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else {

            try {
                zip = encryption.decipher(zip, key);
            } catch (ProtectionPluginException ex) {
                deleteFolder(new File("temp"));
                JOptionPane.showMessageDialog(new JFrame(), "Wrong password, corrupted file, or not a File Protection Container.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            System.arraycopy(zip, 0, toZipp, 0, (int) (header.getPadPos()));
            try {
                Unzip = new UnZip("files/" + output, toZipp);
            } catch (IOException ex) {
                deleteFolder(new File("temp"));
                JOptionPane.showMessageDialog(new JFrame(), "Wrong password, corrupted file, or not a File Protection Container.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                Unzip.run();
            } catch (IOException ex) {
                deleteFolder(new File("temp"));
                JOptionPane.showMessageDialog(new JFrame(), "Wrong password, corrupted file, or not a File Protection Container.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        try {
            integrity = PPDecompressor.getInstance().decompressIntegrityPP(Unzip.getName(0), Unzip.getEntry(0));
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            JOptionPane.showMessageDialog(new JFrame(), "Wrong password, corrupted file, or not a File Protection Container.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        byte[] mac;
        try {
            mac = integrity.sign(Unzip.getEntry(1), key);
        } catch (ProtectionPluginException ex) {
            deleteFolder(new File("temp"));
            JOptionPane.showMessageDialog(new JFrame(), "Wrong password, corrupted file, or not a File Protection Container.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        byte[] macToCheck = header.getMac();

        if (mac.length == macToCheck.length) {
            for (int i = 0; i < mac.length; i++) {
                if (mac[i] != macToCheck[i]) {
                    System.out.println(i);
                    System.out.println("OMG: MAC NÂO SE VERIFICA");
                    return;
                }
            }
            try {
                Unzip.writeZip();
            } catch (IOException ex) {
                deleteFolder(new File("temp"));
                JOptionPane.showMessageDialog(new JFrame(), "Wrong password, corrupted file, or not a File Protection Container.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else {
            JOptionPane.showMessageDialog(new JFrame(), "Wrong password, corrupted file, or not a File Protection Container.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        deleteFolder(new File("temp"));
    }

    public void toZip(String name, String outs, byte[]  
        ... oi) {
        List<File> fields = new ArrayList<>();
        File zip;
        int i = 0;
        for (byte[] b : oi) {
            if (i == 0) {
                zip = new File("temp/" + name);
            } else {
                zip = new File("temp/d4e1t5r" + i);
            }
            i++;
            try (FileOutputStream out = new FileOutputStream(zip)) {

                out.write(b);

            } catch (IOException ex) {
                deleteFolder(new File("temp"));
                JOptionPane.showMessageDialog(new JFrame(), "Something went wrong.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            fields.add(zip);
        }

        Zip zipit = new Zip(outs, fields);
        try {
            zipit.run();
        } catch (IOException ex) {
            deleteFolder(new File("temp"));
            JOptionPane.showMessageDialog(new JFrame(), "Something went wrong.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

    public byte[] getDummy(int size) {
        byte[] dummyContent = new byte[size];
        new Random().nextBytes(dummyContent);
        return dummyContent;
    }

    public int getOffset(int toBeOrNotToBe) {
        int toBe = 1;

        while (toBe < toBeOrNotToBe) {
            toBe = toBe << 1;
        }
        return toBe;
    }

    public void setPPenc(String name) {
        this.PPencName = name;
        this.encryption = PPEngine.getInstance().getEncryptionPP(name);
    }

    public void setPPint(String name) {
        this.PPintName = name;
        this.integrity = PPEngine.getInstance().getIntegrityPP(name);
    }

    public void setStega(String fonte) {
        sourceSteganography = fonte;
        steganography = true;
    }

    public void setDummy(String fonte, byte[] key) {
        sourceDummy = fonte;
        dummy = true;
        dummyKey = key;
    }

    public static void deleteFolder(File folder) {
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
}
