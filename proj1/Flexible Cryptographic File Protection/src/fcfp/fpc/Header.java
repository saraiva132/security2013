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
        byte[] padPosStream = new byte[Byte.SIZE];
        System.arraycopy(header, 0, padPosStream, 0, 8);
        padPos = ByteCast.byteArray2Long(padPosStream);
        byte[] checksumStream = new byte[Byte.SIZE];
        System.arraycopy(header, 8, checksumStream, 0, 8);
        checksum = ByteCast.byteArray2Long(checksumStream);
        padding = new byte[256];
        System.arraycopy(header, padPosStream.length + checksumStream.length, padding, 0, padding.length);
        mac = new byte[header.length - padPosStream.length - checksumStream.length - padding.length];
        System.arraycopy(header, padPosStream.length + checksumStream.length + padding.length, mac, 0, header.length - padPosStream.length - checksumStream.length - padding.length);
        System.out.println("pasPos: " + padPosStream[3] + padPosStream[2] + padPosStream[1] + padPosStream[0]);
        System.out.println("checksum: " + checksumStream[3] + checksumStream[2] + checksumStream[1] + checksumStream[0]);
    }

    public Header(long padPos, byte[] mac) {
        this.checksum = 0;
        this.padPos = padPos;
        this.mac = mac;
        this.padding = new byte[256];
        Pad.fill(padding, 0, padding.length);
        checksum = getChecksum();
    }

    public byte[] getStream() {
        byte[] padPosStream = ByteCast.long2ByteArray(padPos);
        byte[] checksumStream = ByteCast.long2ByteArray(checksum);
        byte[] header = new byte[padPosStream.length + checksumStream.length + mac.length + padding.length];
        System.arraycopy(padPosStream, 0, header, 0, padPosStream.length);
        System.arraycopy(checksumStream, 0, header, padPosStream.length, checksumStream.length);
        System.arraycopy(padding, 0, header, padPosStream.length + checksumStream.length, padding.length);
        System.arraycopy(mac, 0, header, padPosStream.length + checksumStream.length + padding.length, mac.length);
        System.out.println("pasPos: " + padPosStream[3] + padPosStream[2] + padPosStream[1] + padPosStream[0]);
        System.out.println("checksum: " + checksumStream[3] + checksumStream[2] + checksumStream[1] + checksumStream[0]);
        return header;
    }

    private long getChecksum() {
        CRC32 crc = new CRC32();
        crc.update(padding);
        return crc.getValue();
    }

    public boolean checksum() {
        long temp;
        temp = getChecksum();
        System.out.println(temp + " : " + checksum + " JustToCheck: PadPos - " + padPos + " why not mac: - " + mac.length);
        return temp == checksum;
    }

    public byte[] getMac() {
        return mac;
    }

    public long getPadPos() {
        return padPos;
    }
}
