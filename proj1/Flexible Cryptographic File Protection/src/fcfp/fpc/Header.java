/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fcfp.fpc;

import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author rafael
 */
public class Header {
    
    private int contentPos;
    private int dummypos;
    private byte [] verification  ;
    private int checksum=0;
    
    public Header(byte [] header)
    {
        
    }
    
    public Header(int contentPos, int dummypos)
    {
        this.contentPos = contentPos;
        this.dummypos = dummypos;
        Random random = new Random();
        random.nextBytes(verification);
        
    }
   
    public byte [] getHeader()
    {   
        byte [] temp = new byte[12];
        System.arraycopy(temp, 0, verification, 0, verification.length);
        return temp;
    }
    
    public void setChecksum()
    {
        int i = 0;
        while ( i < verification.length )
        {
            checksum += verification[i];
            i++;
        }
    }
    
    
    
    public boolean checksum()
    {
        int temp=0;
        int i = 0;
        while ( i < verification.length )
        {
            temp += verification[i];
            i++;
        }
        if(temp == checksum)
            return true;
        else
            return false;
    }
    
    public int getContentPos()
    {
       return contentPos; 
    }
    
    public int getDummyPos()
            
    {
        return dummypos;
    }
    
}
