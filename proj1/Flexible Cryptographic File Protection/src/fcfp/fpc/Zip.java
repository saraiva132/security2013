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
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
 
public class Zip
{
    private List<String> fileList;
    private String source ;
    private String output ;
    
    public Zip(String source, String output){
	fileList = new ArrayList<>();
        this.source = source;
        this.output = output;
    }
    
     public Zip(String source){
	fileList = new ArrayList<>();
        this.source = source;
    } 
    
    /**
     * Zip it 2
     */
    public void run()
    {
    generateFileList(new File(source));
    zipIt();
    }
    
    public void zipIt(){
 
     byte[] buffer = new byte[1024];
 
     try{
        if(output == null)
        {
        output = source;
        }
    	FileOutputStream fos = new FileOutputStream(output);
    	ZipOutputStream zos = new ZipOutputStream(fos);
 
    	System.out.println("Output to Zip : " + output);
        //Enquanto houver ficheiros para escrever no zip
    	for(String file : this.fileList){
 
    		System.out.println("File Added : " + file);
    		ZipEntry ze= new ZipEntry(file);
        	zos.putNextEntry(ze);
 
        	FileInputStream in = 
                       new FileInputStream(source + File.separator + file);
                //Escrever a proxima fileEntry
        	int len;
        	while ((len = in.read(buffer)) > 0) {
        		zos.write(buffer, 0, len);
        	}
 
        	in.close();
    	}
    	zos.closeEntry();
    	zos.close();
 
    	System.out.println("Done");
    }catch(IOException ex){
       ex.printStackTrace();   
    }
   }
 
    /**
     * Traverse a directory and get all files,
     * and add the file into fileList  
     * @param node file or directory
     */
    public void generateFileList(File node){
 
    	//Se for um ficheiro adicionar à lista de ficheiros para zipar
	if(node.isFile()){
                //adiciona o ficheiro à lista(caminho absoluto a partir do diretório origem)
		fileList.add(generateZipEntry(node.getAbsoluteFile().toString()));
	}
        //Se for um diretório chamar recursivamente a função para cada ficheiro no diretório
	if(node.isDirectory()){
		String[] subNote = node.list();
		for(String filename : subNote){
			generateFileList(new File(node, filename));
		}
	}
 
    }
    /**
     * Format the file path for zip
     * @param file file path
     * @return Formatted file path
     */
    
    public String getOutput()
    {
        return output;
    }
    /**
     * Format the file path for zip
     * @return String output path
     */
    private String generateZipEntry(String file){
    	return file.substring(source.length()+1, file.length());
    }
}