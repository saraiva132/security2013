/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fcfp.fpc;

import fcfp.util.cast.ByteCast;
import java.util.Random;
import java.util.zip.CRC32;

/**
 *
 * @author rafael
 */
public class Header {

    private long padPos;
    private long checksum;
    private byte[] padding;
    private byte[] mac;


    public Header(byte[] header) {
        byte[] temp1 = new byte[Byte.SIZE];
        byte[] temp2 = new byte[Byte.SIZE];
        System.arraycopy(header, 0, temp1, 0, 8);
        System.arraycopy(header, 8, temp2, 0, 8);
        System.out.println(temp2[0] + temp2[1] + temp2[2] + temp2[3]);
        System.out.println(temp1[0] + temp1[1] + temp1[2] + temp1[3]);
        padPos = ByteCast.byteArray2Long(temp1);
        checksum = ByteCast.byteArray2Long(temp2);
        padding = new byte[256];
        System.arraycopy(header, temp1.length+temp2.length, padding, 0, padding.length);
        mac = new byte[header.length - temp1.length - temp2.length - padding.length];
        System.arraycopy(header,temp1.length+temp2.length+padding.length, mac, 0, header.length - temp1.length - temp2.length - padding.length);

    }

    public Header(long padPos, byte[] mac) {
        this.checksum = 0;
        this.padPos = padPos;
        this.mac = mac;
        this.padding = new byte[256];
        Pad.fill(padding, 0, padding.length);
        checksum = setChecksum();
    }

    public byte[] getHeader() {
        byte[] temp1 = new byte[Byte.SIZE];
        byte[] temp2 = new byte[Byte.SIZE];
        temp1 = ByteCast.long2ByteArray(padPos);
        temp2 = ByteCast.long2ByteArray(checksum);
        byte[] temp = new byte[temp1.length + temp2.length + mac.length + padding.length];
        System.arraycopy(temp1, 0, temp, 0, temp1.length);
        System.arraycopy(temp2, 0, temp, temp1.length, temp2.length);
        System.arraycopy(padding, 0, temp, temp1.length+temp2.length,padding.length);
        System.arraycopy(mac, 0, temp, temp1.length + temp2.length+padding.length,mac.length);
        System.out.println(temp2[0] + temp2[1] + temp2[2] + temp2[3]);
        System.out.println(temp1[0] + temp1[1] + temp1[2] + temp1[3]);
        return temp;
    }

    private long setChecksum() {
        CRC32 crc = new CRC32();
        crc.update(padding);
        return crc.getValue();
    }

    public boolean checksum() {
        long temp;
        temp = setChecksum();
        System.out.println(temp + " : " + checksum + " JustToCheck: PadPos - " + padPos + " why not mac: - " + mac.length);
        if (temp == checksum) {
            return true;
        } else {
            return false;
        }
    }

    public byte[] getMac() {
        return mac;
    }

    public long getPadPos() {
        return padPos;
    }

}
