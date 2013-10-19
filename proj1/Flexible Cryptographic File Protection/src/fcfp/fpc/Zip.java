/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fcfp.fpc;

/**
 *
 * @author rafael
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Zip {

    private String output;
    private List<File> fields;

    public Zip(String output, List<File> fields) {
        this.output = output;
        this.fields = fields;
    }

    public Zip(List<File> fields) {
        // this.fields = fields;
    }

    /**
     * Zip it 2
     */
    public void run() {
        zipIt();
    }

    public void zipIt() {

        byte[] buffer = new byte[1024];

        try {
            FileOutputStream fos = new FileOutputStream(output);
            ZipOutputStream zos = new ZipOutputStream(fos);

            System.out.println("Output to Zip : " + output);
            //Enquanto houver ficheiros para escrever no zip
            int i=0;
            for (File file : this.fields) {
                ZipEntry ze = new ZipEntry(file.getName());
                zos.putNextEntry(ze);
                FileInputStream in =
                        new FileInputStream(file);
                //Escrever a proxima fileEntry
                int len;
                while ((len = in.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                }
            }
            zos.closeEntry();
            zos.close();

            System.out.println("Done");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String getOutput() {
        return output;
    }
}