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
import java.io.FileNotFoundException;
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
     * @throws java.io.IOException
     */
    public void run() throws IOException {
        zipIt();
    }

    public void zipIt() throws FileNotFoundException, IOException {
        
        FileOutputStream fos = new FileOutputStream(output);
        try (ZipOutputStream zos = new ZipOutputStream(fos)) {
            //System.out.println("Output to Zip : " + output);
            //Enquanto houver ficheiros para escrever no zip
            for (File file : this.fields) {
                ZipEntry ze = new ZipEntry(file.getName());
                zos.putNextEntry(ze);
                FileInputStream in = new FileInputStream(file);
                //Escrever a proxima fileEntry
                int len = 0;
                int offset = 0;
                byte[] buffer = new byte[in.available()];
                while (offset < buffer.length && (len = in.read(buffer, offset, buffer.length - offset)) >= 0) {
                    offset += len;
                    zos.write(buffer, 0, len);
                }
            }
            zos.closeEntry();
        }
        //System.out.println("Done");
    }

    public String getOutput() {
        return output;
    }
}
