/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fcfp.fpc;

import fcfp.util.cast.ByteCast;
import fcfp.util.iv.InitVector;
import java.util.Arrays;

/**
 *
 * @author rafael
 */
public class Header {

    private static final byte[] TRUE = "TRUE".getBytes();
    private long padPos;
    private byte[] mac;
    private byte[] checksum;

    public Header(byte[] header) {
        checksum = new byte[TRUE.length];
        System.arraycopy(header, 0, checksum, 0, checksum.length);
        byte[] padPosStream = new byte[Byte.SIZE];
        System.arraycopy(header, checksum.length, padPosStream, 0, padPosStream.length);
        padPos = ByteCast.byteArray2Long(padPosStream);
        mac = new byte[header.length - checksum.length - padPosStream.length - InitVector.getMaxLength()];
        System.arraycopy(header, checksum.length + padPosStream.length, mac, 0, header.length - checksum.length - padPosStream.length - InitVector.getMaxLength());
    }

    public Header(long padPos, byte[] mac) {
        this.padPos = padPos;
        this.mac = mac;
    }

    public byte[] getStream() {
        byte[] padPosStream = ByteCast.long2ByteArray(padPos);
        byte[] header = new byte[TRUE.length + padPosStream.length + mac.length + InitVector.getMaxLength()];
        System.arraycopy(TRUE, 0, header, 0, TRUE.length);
        System.arraycopy(padPosStream, 0, header, TRUE.length, padPosStream.length);
        System.arraycopy(mac, 0, header, TRUE.length + padPosStream.length, mac.length);
        return header;
    }

    public boolean checksum() {
        System.out.println(" : " + " JustToCheck: PadPos - " + padPos + " why not mac: - " + mac.length);
        return Arrays.equals(checksum, TRUE);
    }

    public byte[] getMac() {
        return mac;
    }

    public long getPadPos() {
        return padPos;
    }
}
