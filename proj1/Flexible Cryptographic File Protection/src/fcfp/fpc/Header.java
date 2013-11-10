package fcfp.fpc;

import fcfp.util.ByteCast;
import java.util.Arrays;

/**
 *
 * @author rafael
 */
public class Header {

    private static final byte[] TRUE = "TRUE".getBytes();
    private final long padPos;
    private final byte[] mac;
    private byte[] checksum;
    
    /**
     * 
     * @param header 
     */
    public Header(byte[] header) {
        checksum = new byte[TRUE.length];
        System.arraycopy(header, 0, checksum, 0, checksum.length);
        byte[] padPosStream = new byte[Byte.SIZE];
        System.arraycopy(header, checksum.length, padPosStream, 0, padPosStream.length);
        padPos = ByteCast.byteArray2Long(padPosStream);
        mac = new byte[header.length - checksum.length - padPosStream.length];
        System.arraycopy(header, checksum.length + padPosStream.length, mac, 0, mac.length);
    }
    
    /**
     * 
     * @param padPos
     * @param mac 
     */
    public Header(long padPos, byte[] mac) {
        this.padPos = padPos;
        this.mac = mac;
    }
    
    /**
     * 
     * @return 
     */
    public byte[] getStream() {
        byte[] padPosStream = ByteCast.long2ByteArray(padPos);
        byte[] header = new byte[TRUE.length + padPosStream.length + mac.length];
        System.arraycopy(TRUE, 0, header, 0, TRUE.length);
        System.arraycopy(padPosStream, 0, header, TRUE.length, padPosStream.length);
        System.arraycopy(mac, 0, header, TRUE.length + padPosStream.length, mac.length);
        return header;
    }
    
    /**
     * 
     * @return 
     */
    public boolean checksum() {
        //System.out.println(" : " + " JustToCheck: PadPos - " + padPos + " why not mac: - " + mac.length);
        return Arrays.equals(checksum, TRUE);
    }
    
    /**
     * 
     * @return 
     */
    public byte[] getMac() {
        return mac;
    }
    
    /**
     * 
     * @return 
     */
    public long getPadPos() {
        return padPos;
    }
}
