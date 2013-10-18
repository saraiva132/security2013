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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

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
    private boolean mode;
    private boolean steganography = false;
    private boolean dummy = false;
    private String PPencName;
    private String PPintName;

    public FPC(String source, String output, byte[] key) {
        this.key = key;
        this.source = source;
        this.output = output;
    }

    public void run() {
    }

    public void Cipher() {
    }

    public void DeCipher() throws ProtectionPluginException, ClassNotFoundException, InstantiationException, IllegalAccessException, FileNotFoundException, IOException {
        UnZip unzip = new UnZip(source);
        unzip.run();
        encryption = PPDecompressor.getInstance().decompressEncryptionPP(PPencName, unzip.getEntry(0));
        byte[] content = unzip.getEntry(3);
        encryption.decipher(unzip.getEntry(1), key);
        encryption.decipher(unzip.getEntry(2), key);
        Header header1 = new Header(unzip.getEntry(1));
        Header header2 = new Header(unzip.getEntry(2));
        encryption.decipher(content, key);
        if (header1.checksum()) {
            DecipherZip(header1, content, true);
        } else if (header2.checksum()) {
            DecipherZip(header2, content, false);
        } else {
            return;
        }

    }

    private void DecipherZip(Header header, byte[] content, boolean identity) throws FileNotFoundException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, ProtectionPluginException {
        UnZip Unzip;
        
            if (identity) {
                Unzip = new UnZip(output,Arrays.copyOfRange(content, 0, header.getPadPos() - 1));
                Unzip.run();
            } else {
                Unzip = new UnZip(output,Arrays.copyOfRange(content, content.length / 2, header.getPadPos() - 1));
                Unzip.run();
            }
            integrity = PPDecompressor.getInstance().decompressIntegrityPP(PPintName, Unzip.getEntry(0));
            if(header.getMac() == integrity.sign(Unzip.getEntry(1), key))
            {
                Unzip.writeZip();
                System.out.println("Great Success");
            }
            else
            {
                
            }
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
