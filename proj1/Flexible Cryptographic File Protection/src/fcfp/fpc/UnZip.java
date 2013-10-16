/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fcfp.fpc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 *
 * @author rafael
 */
public class UnZip {

    private List<byte[]> fileList;
    private File zip;


    public UnZip(String source) {
        this.zip = new File(source);
    }
    
    public UnZip(byte [] toUnzip) throws FileNotFoundException, IOException
    {
        try (FileOutputStream out = new FileOutputStream(zip)) {
            out.write(toUnzip);
        }
    }

    void run() {
    unZipIt(zip);
    }

    /**
     * Unzip it
     *
     * @param zipFile input zip file
     * @param output zip file output folder
     */
    private void unZipIt(File zipIt) {

        byte[] buffer;
        InputStream in = null;
        try {
            //get the zip file content
            ZipFile zip = new ZipFile(zipIt);
            //get the zipped file list entry
            Enumeration<? extends ZipEntry> oi = zip.entries();
            while (oi.hasMoreElements()) {
                final ZipEntry ze = oi.nextElement();
                in = zip.getInputStream(ze);
                buffer = new byte[in.available()];
                in.read(buffer);
                fileList.add(buffer);
            }
            in.close();
            zip.close();
            System.out.println("Done");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public byte [] getEntry(int i)
    {
        return fileList.get(i);
    }
    
    public int getSize()
    {
        return fileList.size();
    }
}
