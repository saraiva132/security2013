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
        this.zip = new File(source);
        this.source = source;
    }

    public UnZip(String output, byte[] toUnzip) throws FileNotFoundException, IOException {
        this.output = output;
        this.zip = new File("cocozadas");
        try (FileOutputStream out = new FileOutputStream(zip)) {
            out.write(toUnzip);
        }
    }

    public UnZip(byte[] toUnzip) throws FileNotFoundException, IOException {
        this.zip = new File("cocozadas");
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

        byte[] bufToAdd;
        InputStream in = null;
        try {
            System.out.println("Unzipping Content!..");
            System.out.println(zip == null);
            try (ZipFile zipFile = new ZipFile(zip)) {
                Enumeration<? extends ZipEntry> oi = zipFile.entries();
                while (oi.hasMoreElements()) {
                    final ZipEntry ze = oi.nextElement();
                    System.out.println("addingtoZip: " + ze.getName());
                    fileName.add(ze.getName());
                    in = zipFile.getInputStream(ze);
                    bufToAdd = new byte[in.available()];
                    int len=0;
                    int offset=0;
                    byte [] buffer = new byte[in.available()];
                    while(offset < buffer.length && (len = in.read(buffer, offset, buffer   .length - offset)) >= 0)
                    {
                        offset+=len;
                    }
                    fileList.add(buffer);
                }
                in.close();
            }
            System.out.println("Done");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void writeZip() throws FileNotFoundException, IOException {
        
       /* File folder = new File(output);
    	if(!folder.exists()){
    		folder.mkdir();
    	}
        
        byte[] buffer = new byte[1024];
        FileOutputStream zos = new FileOutputStream(output);
        InputStream in = null;
        ZipInputStream zis = new ZipInputStream(new FileInputStream(zip));
        ZipEntry ze = zis.getNextEntry();
        while(ze != null)
        {
           String fileName = ze.getName();
           File newFile = new File(output + File.separator + fileName);
 
           System.out.println("file unzip : "+ newFile.getAbsoluteFile());
 
            FileOutputStream fos = new FileOutputStream(newFile);             
 
            int len;
            while ((len = zis.read(buffer)) > 0) {
       		fos.write(buffer, 0, len);
            }
 
            fos.close();   
            ze = zis.getNextEntry();
        }*/
        
        FileOutputStream zos = null;
        int i = 0;
        while(i < fileList.size())
        {
            zos = new FileOutputStream(output+fileName.get(i));
            zos.write(fileList.get(i),0,fileList.get(i).length);
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
