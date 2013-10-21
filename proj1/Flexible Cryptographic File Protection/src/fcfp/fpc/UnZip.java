/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fcfp.fpc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 *
 * @author rafael
 */
public class UnZip {

    private List<byte[]> fileList = new ArrayList<>();
    private List<String> fileName = new ArrayList<>();
    private File zip;
    private String output;
    private String source;

    public UnZip(String source) {
        File zip = new File(source);
        this.source = source;
    }

    public UnZip(String output, byte[] toUnzip) throws FileNotFoundException, IOException {
        this.output = output;
        try (FileOutputStream out = new FileOutputStream(zip)) {
            out.write(toUnzip);
        }
    }

    public UnZip(byte[] toUnzip) throws FileNotFoundException, IOException {
        try (FileOutputStream out = new FileOutputStream(zip)) {
            out.write(toUnzip);
        }
    }

    void run() {
        unZipIt();
    }

    /**
     * Unzip it
     *
     * @param zipFile input zip file
     * @param output zip file output folder
     */
    private void unZipIt() {

        byte[] buffer;
        InputStream in = null;
        try {
            System.out.println("Unzipping Content!..");
            //get the zip file content
            ZipFile zip = new ZipFile(source);
            //get the zipped file list entry
            Enumeration<? extends ZipEntry> oi = zip.entries();
            while (oi.hasMoreElements()) {
                final ZipEntry ze = oi.nextElement();
                fileName.add(ze.getName());
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

    public void writeZip() throws FileNotFoundException, IOException {
        byte[] buffer = new byte[1024];
        ZipInputStream zis;
        zis = new ZipInputStream(new FileInputStream(zip));
        ZipEntry ze = zis.getNextEntry();
        FileOutputStream fos = new FileOutputStream(output + ze.getName());
        int len;
        while ((len = zis.read(buffer)) > 0) {
            fos.write(buffer);
        }
        fos.flush();
        fos.close();

        if (zis != null) {
            try {
                zis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public byte[] getEntry(int i) {
        return fileList.get(i);
    }

    public String getName(int i) {
        return fileName.get(i);
    }

    public int getSize() {
        return fileList.size();
    }
}
