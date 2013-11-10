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
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 *
 * @author rafael
 */
public class UnZip {

    private final List<byte[]> fileList = new ArrayList<>();
    private final List<String> fileName = new ArrayList<>();
    private final File zip;

    public UnZip(String source) {
        this.zip = new File(source);
    }

    public UnZip(byte[] toUnzip) throws FileNotFoundException, IOException {
        this.zip = new File("temp/temp");
        try (FileOutputStream out = new FileOutputStream(zip)) {
            out.write(toUnzip);
        }
    }

    void run() throws IOException {
        unZipIt();
    }

    /**
     * Unzip it
     *
     * @param zipFile input zip file
     * @param output zip file output folder
     */
    private void unZipIt() throws IOException {
        InputStream in;
        //System.out.println("Unzipping Content!..");
        //System.out.println(zip == null);
        ZipFile zipFile = new ZipFile(zip);
        Enumeration<? extends ZipEntry> oi = zipFile.entries();
        while (oi.hasMoreElements()) {
            final ZipEntry ze = oi.nextElement();
            //System.out.println("addingtoZip: " + ze.getName());
            fileName.add(ze.getName());
            in = zipFile.getInputStream(ze);
            int len = 0;
            int offset = 0;
            byte[] buffer = new byte[in.available()];
            while (offset < buffer.length && (len = in.read(buffer, offset, buffer.length - offset)) >= 0) {
                offset += len;
            }
            fileList.add(buffer);
            in.close();
        }
        System.out.println("Done");

    }

    public void writeZip(String output) throws FileNotFoundException, IOException {
        FileOutputStream zos;
        int i = 1;
        while (i < fileList.size()) {
            zos = new FileOutputStream(output);
            zos.write(fileList.get(i), 0, fileList.get(i).length);
            zos.close();
            i++;
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
