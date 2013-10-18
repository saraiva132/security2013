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
    
    private int padPos;
    private int checksum=0;
    private byte [] verification = new byte[4];
    private int macsize;
    private byte [] Mac;
    private byte [] IV;
    
    public Header(byte [] header)
    {
        byte [] temp1 = new byte[4];
        byte [] temp2  = new byte[4];
        byte [] temp3 = new byte[4];
        
        System.arraycopy(temp1, 0, header, 0,3);
        System.arraycopy(temp2, 0, header, 4, 7);
        System.arraycopy(verification, 0, header, 8, 11);
        System.arraycopy(temp3, 0, header, 12, 15);
        padPos = temp1[0] + (temp1[1] << 8) + (temp1[2]) << 16 + (temp1[3]) << 24;
          checksum = temp2[0] + (temp2[1] << 8) + (temp2[2]) << 16 + (temp2[3]) << 24;
              macsize = temp3[0] + (temp3[1] << 8) + (temp3[2]) << 16 + (temp3[3]) << 24;
        System.arraycopy(Mac, 0, header, 16, 15 + macsize);
        System.arraycopy(Mac, 0, header, (15 + macsize) + 1, header.length);
        

    }
    
    public Header(int padPos, byte [] mac, byte [] IV)
    {
        this.padPos = padPos;
        Random random = new Random();
        random.nextBytes(verification);
        this.Mac = mac;
        this.IV = IV;
    }
   
    public byte [] getHeader()
    {   
        byte [] temp = new byte[12 + IV.length + Mac.length + verification.length];
        byte [] temp1 = new byte[4];
        byte [] temp2 = new byte[4];
        byte [] temp3 = new byte[4];
        temp1[0] =  (byte)(padPos | 0x000f);
         temp1[1] =  (byte)(padPos >> 8| 0x000f);
          temp1[2] =  (byte)(padPos >> 16 | 0x000f);
           temp1[3] =  (byte)(padPos >> 24| 0x000f); 
        temp2[0] =  (byte)(checksum | 0x000f);
         temp2[1] =  (byte)(checksum >> 8| 0x000f);
          temp2[2] =  (byte)(checksum >> 16 | 0x000f);
           temp2[3] =  (byte)(checksum >> 24| 0x000f);
        temp3[0] =  (byte)(macsize | 0x000f);
         temp3[1] =  (byte)(macsize >> 8| 0x000f);
          temp3[2] =  (byte)(macsize >> 16 | 0x000f);
           temp3[3] =  (byte)(macsize >> 24| 0x000f);  
        System.arraycopy(temp, 0, temp1, 0, 3);
        System.arraycopy(temp, 0, temp2, 4, 7);
        System.arraycopy(temp, 0, verification, 8, 11);
        System.arraycopy(temp, 0, temp3, 12, 15);
        System.arraycopy(temp, 0, Mac, 16, 15 + Mac.length);
        System.arraycopy(temp, 0, IV, 16 + Mac.length, 15 + Mac.length + IV.length);
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
    
    public int getPadPos()
            
    {
        return padPos;
    }
    
}
