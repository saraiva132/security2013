/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fcfp.fpc;

import fcfp.util.cast.ByteCast;
import java.util.zip.CRC32;

/**
 *
 * @author rafael
 */
public class Header {

    private long padPos;
    private static final byte[] TRUE = "TRUE".getBytes();
    private byte[] mac;


    public Header(byte[] header) {
        byte[] padPosStream = new byte[Byte.SIZE];
        System.arraycopy(header, 0, padPosStream, 0, 8);
        padPos = ByteCast.byteArray2Long(padPosStream);
        System.arraycopy(header, padPosStream.length, mac, 0, header.length - padPosStream.length);
        mac = new byte[header.length - padPosStream.length];
        System.arraycopy(header, padPosStream.length, mac, 0, header.length - padPosStream.length);
        System.out.println("padPos: " +padPos);
    }

    public Header(long padPos, byte[] mac) {
        this.padPos = padPos;
        this.mac = mac;
    }

    public byte[] getStream() {
        byte[] padPosStream = ByteCast.long2ByteArray(padPos);
        byte[] header = new byte[padPosStream.length + mac.length];
        System.arraycopy(padPosStream, 0, header, 0, padPosStream.length);
        System.arraycopy(mac, 0, header, padPosStream.length, mac.length);
        System.out.println("padPos: " + padPosStream[3] + padPosStream[2] + padPosStream[1] + padPosStream[0]);
        return header;
    }


    public boolean checksum() {
        long temp;
        System.out.println( " : " + " JustToCheck: PadPos - " + padPos + " why not mac: - " + mac.length);
        return true;
    }

    public byte[] getMac() {
        return mac;
    }

    public long getPadPos() {
        return padPos;
    }
}
