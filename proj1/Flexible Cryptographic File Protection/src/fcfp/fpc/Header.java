/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fcfp.fpc;

import java.util.Random;

/**
 *
 * @author rafael
 */
public class Header {
    
    private long padPos;
    private int checksum=0;
    private byte [] verification = new byte[4];
    private byte [] Mac;
    
    public Header(byte [] header)
    {
        byte [] temp1 = new byte[8];
        byte [] temp2  = new byte[4];
        
        System.arraycopy(temp1, 0, header, 0,7);
        System.arraycopy(temp2, 0, header, 8, 11);
        System.arraycopy(verification, 0, header, 12, 15);
        padPos = temp1[0] + (temp1[1] << 8) + (temp1[2]) << 16 + (temp1[3] << 24) + (temp1[4] << 32) +(temp1[5] << 40) + (temp1[6] << 48) + (temp1[7]) << 56;
          checksum = temp2[0] + (temp2[1] << 8) + (temp2[2]) << 16 + (temp2[3]) << 24;
        System.arraycopy(Mac, 0, header,16, header.length);

    }
    
    public Header(long padPos, byte [] mac)
    {
        this.padPos = padPos;
        Random random = new Random();
        random.nextBytes(verification);
        this.Mac = mac;
    }
   
    public byte [] getHeader()
    {   
        byte [] temp1 = new byte[8];
        byte [] temp2 = new byte[4];
        temp1[0] =  (byte)(padPos | 0x000f);
         temp1[1] =  (byte)(padPos >> 8| 0x000f);
          temp1[2] =  (byte)(padPos >> 16 | 0x000f);
           temp1[3] =  (byte)(padPos >> 24| 0x000f);
             temp1[4] =  (byte)(padPos >> 32| 0x000f); 
               temp1[5] =  (byte)(padPos >> 40| 0x000f); 
                 temp1[6] =  (byte)(padPos >> 48| 0x000f); 
                   temp1[7] =  (byte)(padPos >> 56| 0x000f); 
        temp2[0] =  (byte)(checksum | 0x000f);
         temp2[1] =  (byte)(checksum >> 8| 0x000f);
          temp2[2] =  (byte)(checksum >> 16 | 0x000f);
           temp2[3] =  (byte)(checksum >> 24| 0x000f); 
        byte [] temp = new byte[temp1.length + temp2.length + Mac.length + verification.length];   
        System.arraycopy(temp, 0, temp1, 0, 7);
        System.arraycopy(temp, 0, temp2, 8, 11);
        System.arraycopy(temp, 0, verification, 12, 15);
        System.arraycopy(temp, 0, Mac, 16, 15 + Mac.length);
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
    
    public byte[] getMac()
    {   
       return Mac; 
    }
    
    public long getPadPos()
            
    {
        return padPos;
    }
    
}
