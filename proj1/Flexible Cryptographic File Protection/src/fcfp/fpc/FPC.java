/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fcfp.fpc;

import fcfp.pp.EncryptionPP;
import fcfp.pp.IntegrityPP;
import fcfp.pp.PPDecompressor;
import fcfp.pp.ProtectionPluginException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
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
    private String output ;
    private boolean mode;
    private boolean steganography = false;
    private boolean dummy = false;
    private String PPencName;
    private String PPintName;
    
    public FPC(byte [] key, EncryptionPP encryption, IntegrityPP integrity, String source, String output)
    {   
        mode = true; //Encrypt
        this.key = key;
        this.encryption = encryption;
        this.integrity = integrity;
        this.source = source;
        this.output = output;
    }
 
    public FPC(byte [] key, String source, String output)
    {
        mode = false; //Decrypt
        this.key = key;
        this.source = source;
        this.output = output;
    }
    
    public void run() throws ProtectionPluginException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        if(mode == false) //Decrypt
        {
            UnZip unzip = new UnZip(source);
            unzip.run();
            PPDecompressor dec = PPDecompressor.getInstance();
            encryption = dec.decompressEncryptionPP(PPencName,unzip.getEntry(0));
            byte [] content = unzip.getEntry(2);
            encryption.decypher(unzip.getEntry(1), key);
            Header header = new Header(unzip.getEntry(1));
            if(!header.checksum())
                return;
            encryption.decypher(content, key);
               UnZip UnzipDummy;
               UnZip UnzipContent; 
               
            try {
                UnzipContent = new UnZip(Arrays.copyOfRange(content,header.getContentPos(),header.getDummyPos()-1));
                UnzipContent.run();
            } 
            catch (FileNotFoundException ex) {
               System.out.println("Content: Zip");
               System.out.print("Ficheiro não encontrado. De certeza que o caminho está certo?");
               return;
            } catch (IOException ex) {
                System.out.println("Content: Zip");
                System.out.print("Erro a criar o zip. O ficheiro pode estar comprometido");
               return;
            }
            try {            
                UnzipDummy = new UnZip(Arrays.copyOfRange(content,header.getDummyPos(),content.length));
                UnzipDummy.run();
            } 
            catch (FileNotFoundException ex) 
            {
               System.out.println("DummyContent:"); 
               System.out.println("Ficheiro não encontrado. De certeza que o caminho está certo?");
               return;
            } 
            catch (IOException ex) 
            {
               System.out.println("DummyContent:"); 
               System.out.println("Erro a criar o zip. O ficheiro pode estar comprometido");
               return;
            }
            
            
        }
        else              //Encrypt
        {
            Zip zip = new Zip(source,output);
            zip.run();
        }
    }
    
    public byte[] pad(byte [] contentToPad)
    {
        Random r = new Random();
        //Pad tem que ter um valor pseudo-aleatorio entre 5% e 15% do content(talvez meter menos??)
        double randomValue = 0.05 + (0.15 - 0.05) * r.nextDouble();
        int pad = (int) ((int) contentToPad.length * randomValue);
        byte[] padData = new byte[pad];
        new Random().nextBytes(padData);   
        return padData;
    }
    
    public void setPPenc(String name)
    {
        PPencName = name;
    }
    
    public void setPPint(String name)
    {
        PPintName = name;
    }
    
    public void setStega(String source)
    {
        sourceSteganography = source;
        steganography = true;
    }
    
    public void setDummy(String source, byte [] key)
    {
        sourceDummy = source;
        dummy = true;
        dummyKey = key;
    }
}
    
