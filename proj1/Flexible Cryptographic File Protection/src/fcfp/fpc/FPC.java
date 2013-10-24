/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fcfp.fpc;

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
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    }

    public void CipherStartUP() {
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
            }
        } catch (FileNotFoundException ex) {
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
                }
            } catch (FileNotFoundException ex) {
            }
        }
    }

    public void Cipher() {
        try {
            Zip zip1 = toZip(PPintName, "files/zip1", PPEngine.getInstance().getIntegrityPPSerialization(PPintName), file1);
            Zip zip2 = toZip(PPintName, "files/zip2", PPEngine.getInstance().getIntegrityPPSerialization(PPintName), file2);
        } catch (FileNotFoundException ex) {
            System.out.println("Problema a ler os zips do content! Zips não encontrados.");
            return;
        } catch (IOException ex) {
            System.out.println("Problema a ler os zips do content! IO Issues dude");
            return;
        }

        byte[] macReal = null;
        byte[] macDummy = null;
        try {
            macReal = integrity.sign(file1, key);
            macDummy = integrity.sign(file2, dummyKey);
        } catch (ProtectionPluginException ex) {
            System.out.println("Problema a criar os Macs! Verificar os plugins!");
            return;
        }

        byte[] zipToEnc1 = null;
        byte[] zipToEnc2 = null;
        try {
            FileInputStream in = new FileInputStream("files/zip1");
            try {
                zipToEnc1 = new byte[getOffset(offset1 = in.available())];
                int len;
                System.out.println("Tamanho: " + zipToEnc1.length + " . PadPos: " + offset1);
                while ((len = in.read(zipToEnc1)) > 0) {
                    in.read(zipToEnc1, 0, len);
                }
                in.close();
            } catch (IOException ex) {
            }
        } catch (FileNotFoundException ex) {
        }
        try {
            FileInputStream in = new FileInputStream("files/zip2");
            try {
                zipToEnc2 = new byte[getOffset(offset2 = in.available())];
                int len;
                while ((len = in.read(zipToEnc2)) > 0) {
                    in.read(zipToEnc2, 0, len);
                }
                in.close();
            } catch (IOException ex) {
                return;
            }
        } catch (FileNotFoundException ex) {
            return;
        }

        Pad.fill(zipToEnc1, offset1, zipToEnc1.length);
        Pad.fill(zipToEnc2, offset2, zipToEnc2.length);
        try {
            System.out.println(zipToEnc1.length);
            zipToEnc1 = encryption.cipher(zipToEnc1, key);
            System.out.println(zipToEnc1.length);
        } catch (ProtectionPluginException ex) {
            System.out.println("Não conseguiu encriptar o Zip real content!");
            return;
        }
        try {
            System.out.println(zipToEnc2.length);
            zipToEnc2 = encryption.cipher(zipToEnc2, dummyKey);
            System.out.println(zipToEnc2.length);
        } catch (ProtectionPluginException ex) {
            System.out.println("Não conseguiu encriptar os Zip hidden content!");
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
            System.out.println("Não conseguiu encriptar os Headers!");
        }
        try {
            Zip zipFinal = toZip(PPencName, "files/" + output + ".zip", PPEngine.getInstance().getEncryptionPPSerialization(PPencName), encHead1, encHead2, toEnc);
            System.out.println(PPEngine.getInstance().getEncryptionPPSerialization(PPencName).length);
        } catch (FileNotFoundException ex) {
            System.out.println("Não conseguiu criar o zip de output!!");
        } catch (IOException ex) {
            System.out.println("Oops. Problema IO no zip output");
        }
        if (steganography) {
            BufferedPNG toIMG = null;
            try {
                System.out.println("A procura da imagem:" + sourceSteganography);
                toIMG = new BufferedPNG(sourceSteganography);
            } catch (IOException ex) {
                System.out.println("Imagem não encontrada");
            }
            try {
                toIMG.encode("files/" + output + ".zip", "files/" + output + ".png");
            } catch (IOException ex) {

            } catch (InvalidPNGImageSizeException ex) {

            }

        }
        System.out.println("Sucess!!! =)");
    }

    public void DeCipher() throws ProtectionPluginException, ClassNotFoundException, InstantiationException, IllegalAccessException, FileNotFoundException, IOException {
        if (source.endsWith(".png")) {
            BufferedPNG toIMG = null;
            try {
                System.out.println("A procura da imagem:" + source);
                toIMG = new BufferedPNG(source);
            } catch (IOException ex) {
                System.out.println("Imagem não encontrada");
            }
            try {
                toIMG.decode("files/temp.zip");
            } catch (IOException ex) {

            }
            source = "files/temp.zip";
        }
        UnZip unzip = new UnZip(source);
        unzip.run();
        encryption = PPDecompressor.getInstance().decompressEncryptionPP(unzip.getName(0), unzip.getEntry(0));
        byte[] content = unzip.getEntry(3);

        head1 = new Header(encryption.decipher(unzip.getEntry(1), key));
        head2 = new Header(encryption.decipher(unzip.getEntry(2), key));
        System.out.println("Headers createad....");
        if (head1.checksum()) {
            System.out.println("Header Dummy Checksum GOOD");
            DecipherZip(head1, content, true);
        } else if (head2.checksum()) {
            System.out.println("Header Content Checksum GOOD");
            DecipherZip(head2, content, false);
        } else {
            System.out.println("Headers Checksum BOTH WRONG");
            System.exit(0);
        }

    }

    private void DecipherZip(Header header, byte[] content, boolean identity) throws FileNotFoundException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, ProtectionPluginException {
        UnZip Unzip;
        byte[] toZipp;
        System.out.println("Choosing Content to Unzip...");
        if (identity) {
            System.out.println("Tamanho: " + content.length / 2 + " . PadPos: " + header.getPadPos());
            toZipp = Arrays.copyOfRange(content, 0, (int) header.getPadPos() + content.length / 2);
            Unzip = new UnZip("files/" + output, encryption.decipher(toZipp, key));
            Unzip.run();
        } else {
            toZipp = Arrays.copyOfRange(content, content.length / 2, (int) header.getPadPos() + content.length);
            Unzip = new UnZip("files/" + output, encryption.decipher(toZipp, key));
            Unzip.run();
        }
        integrity = PPDecompressor.getInstance().decompressIntegrityPP(Unzip.getName(0), Unzip.getEntry(0));
        byte[] mac = integrity.sign(Unzip.getEntry(1), key);
        byte[] macToCheck = header.getMac();

        if (mac.length == macToCheck.length) {
            for (int i = 0; i < mac.length; i++) {
                if (mac[i] != macToCheck[i]) {
                    System.out.println(i);
                    System.out.println("OMG: MAC NÂO SE VERIFICA");
                    return;
                }
            }
            Unzip.writeZip();
            System.out.println("Great Success");
        } else {
            System.out.println("OMG: MAC NÂO SE VERIFICA");
        }
    }

    public Zip toZip(String name, String outs, byte[]   ... oi) throws FileNotFoundException, IOException {
        List<File> fields = new ArrayList<>();
        File zip;
        int i = 0;
        for (byte[] b : oi) {
            if (i == 0) {
                zip = new File(name);
            } else {
                zip = new File("d4e1t5r" + i);
            }
            i++;
            try (FileOutputStream out = new FileOutputStream(zip)) {
                out.write(b);
            }
            fields.add(zip);
        }

        Zip zipit = new Zip(outs, fields);
        zipit.run();
        return zipit;
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
}
