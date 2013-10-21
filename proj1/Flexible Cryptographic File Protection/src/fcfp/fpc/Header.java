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
      
        System.arraycopy(header, 0, temp1, 0, 8);
        System.arraycopy(header, 8, temp2, 0, 4);
        System.arraycopy(header, 12, verification,0, 4);
          System.out.println(temp2[0]+temp2[1]+temp2[2]+temp2[3]);
                  System.out.println(temp1[0]+temp1[1]+temp1[2]+temp1[3]);
        padPos = temp1[0] + ((long)temp1[1] << 8) + ((long)temp1[2]) << 16 + ((long)temp1[3] << 24) + ((long)temp1[4] << 32) +((long)temp1[5] << 40) + ((long)temp1[6] << 48) + ((long)temp1[7]) << 56;
          checksum = (int)temp2[0] + (int)(temp2[1] << 8) + (int)(temp2[2]) << 16 + (int)(temp2[3]) << 24;
          Mac = new byte[header.length-16];
        System.arraycopy(header, 16, Mac,0, header.length-16);

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
        temp1[0] =  (byte)(padPos & 0x00ff);
         temp1[1] =  (byte)(padPos >> 8 & 0x00ff);
          temp1[2] =  (byte)(padPos >> 16 & 0x00ff);
           temp1[3] =  (byte)(padPos >> 24 & 0x00ff);
             temp1[4] =  (byte)(padPos >> 32 & 0x00ff); 
               temp1[5] =  (byte)(padPos >> 40 & 0x00ff); 
                 temp1[6] =  (byte)(padPos >> 48 & 0x00ff); 
                   temp1[7] =  (byte)(padPos >> 56 & 0x00ff); 
        temp2[0] =  (byte)(checksum & 0x00ff);
         temp2[1] =  (byte)((checksum >> 8) & 0x00ff);
          temp2[2] =  (byte)((checksum >> 16) & 0x00ff);
           temp2[3] =  (byte)((checksum >> 24) & 0x00ff); 
        byte [] temp = new byte[temp1.length + temp2.length + Mac.length + verification.length];   
        System.arraycopy(temp1, 0,temp ,0, temp1.length);
        System.arraycopy(temp2, 0, temp, temp1.length, temp2.length);
        System.arraycopy(verification, 0, temp,temp1.length+temp2.length,verification.length);
        System.arraycopy(Mac, 0, temp, temp1.length+temp2.length+verification.length,Mac.length);
        System.out.println(temp2[0]+temp2[1]+temp2[2]+temp2[3]);
                  System.out.println(temp1[0]+temp1[1]+temp1[2]+temp1[3]);
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
        System.out.println(temp + " : " + checksum + " JustToCheck: PadPos - " +padPos + " why not mac: - " + Mac.length);
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
