/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fcfp.fpc;

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
import java.io.InputStream;
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
    Header head1, head2;
    private int offset1,offset2;

    public FPC(String source, String output, byte[] key) {
        this.key = key;
        this.source = source;
        this.output = output;
    }

    public void run() {
        try {
            FileInputStream in = new FileInputStream(source);
            try {
                file1 = new byte[getOffset(offset1=in.available())];
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
            file2 = getDummy(getOffset(offset2=file1.length));
        } else {
            try {
                FileInputStream in = new FileInputStream(sourceDummy);
                try {
                    file2 = new byte[getOffset(offset2=in.available())];
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

    public void Cipher() throws FileNotFoundException, IOException, ProtectionPluginException {
        Zip zip1 = toZip("zip1",file1,PPEngine.getInstance().getIntegrityPPSerialization(PPintName));
        Zip zip2 = toZip("zip2",file2,PPEngine.getInstance().getIntegrityPPSerialization(PPintName));
        byte[] macReal = integrity.sign(file1, dummyKey);
        byte[] macDummy = integrity.sign(file2, dummyKey);
        byte[] toEnc = new byte[file1.length + file2.length];
        pad(file1, offset1, file1.length);
        pad(file2, offset2, file2.length);
        head1 = new Header((long)offset1,macReal);
        head2 = new Header((long)offset2,macDummy);
        encryption.cipher(toEnc, key);
        byte [] encHead1 = head1.getHeader();
        byte [] encHead2 = head2.getHeader();
        encryption.cipher(encHead1, key);
        encryption.cipher(encHead2, dummyKey);
        Zip zipFinal = toZip(output,PPEngine.getInstance().getEncryptionPPSerialization(PPencName),encHead1,encHead2,toEnc);
        System.out.println("Sucess!!! =)");
    }

    public void DeCipher() throws ProtectionPluginException, ClassNotFoundException, InstantiationException, IllegalAccessException, FileNotFoundException, IOException {
        UnZip unzip = new UnZip(source);
        unzip.run();
        encryption = PPDecompressor.getInstance().decompressEncryptionPP(PPencName, unzip.getEntry(0));
        byte[] content = unzip.getEntry(3);
        encryption.decipher(unzip.getEntry(1), key);
        encryption.decipher(unzip.getEntry(2), key);
        head1 = new Header(unzip.getEntry(1));
        head2 = new Header(unzip.getEntry(2));
        encryption.decipher(content, key);
        if (head1.checksum()) {
            DecipherZip(head1, content, true);
        } else if (head2.checksum()) {
            DecipherZip(head2, content, false);
        } else {
            return;
        }

    }

    private void DecipherZip(Header header, byte[] content, boolean identity) throws FileNotFoundException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, ProtectionPluginException {
        UnZip Unzip;

        if (identity) {
            Unzip = new UnZip(output, Arrays.copyOfRange(content, 0, (int) header.getPadPos() - 1));
            Unzip.run();
        } else {
            Unzip = new UnZip(output, Arrays.copyOfRange(content, content.length / 2, (int) header.getPadPos() - 1));
            Unzip.run();
        }
        integrity = PPDecompressor.getInstance().decompressIntegrityPP(PPintName, Unzip.getEntry(0));
        if (header.getMac() == integrity.sign(Unzip.getEntry(1), key)) {
            Unzip.writeZip();
            System.out.println("Great Success");
        } else {
        }
    }

    public Zip toZip(String outs,byte[]... oi) throws FileNotFoundException, IOException {
        List<File> fields = null;
        File zip = null;
        for (byte[] b  : oi) {
            try (FileOutputStream out = new FileOutputStream(zip)) {
                out.write(b);
            }
            fields.add(zip);
        }
        Zip zipit = new Zip(outs,fields);
        zipit.run();
        return zipit;
    }

    public void pad(byte[] contentToPad, int offset, int length) {
        if (length > contentToPad.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        Random r = new Random();
        //Pad tem que ter um valor pseudo-aleatorio entre 5% e 15% do content(talvez meter menos??)
        //double randomValue = 0.05 + (0.15 - 0.05) * r.nextDouble();
        //int pad = (int) ((int) contentToPad.length * randomValue);
        byte[] padData = new byte[length - offset];
        new Random().nextBytes(padData);
        System.arraycopy(contentToPad, offset, padData, 0, length);
    }

    public byte[] getDummy(int size) {
        byte[] dummyContent = new byte[size];
        new Random().nextBytes(dummyContent);
        return dummyContent;
    }
    
    public int getOffset(int toBeOrNotToBe)
    {
        int toBe = 1;
        
        while(toBe < toBeOrNotToBe)
            toBe=toBe<<1;
        return toBe;
    }
    
    public void setPPenc(String name) {
        this.encryption = PPEngine.getInstance().getEncryptionPP(name);
    }

    public void setPPint(String name) {
        this.integrity = PPEngine.getInstance().getIntegrityPP(name);
    }

    public void setStega(String source) {
        sourceSteganography = source;
        steganography = true;
    }

    public void setDummy(String source, byte[] key) {
        sourceDummy = source;
        dummy = true;
        dummyKey = key;
    }
}
