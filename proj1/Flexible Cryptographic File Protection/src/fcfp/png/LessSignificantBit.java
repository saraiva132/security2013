package fcfp.png;

/**
 *
 * @author Simão Paulo Rato Alves Reis
 * @version 1.0
 */
public class LessSignificantBit {

    /**
     * Encode the file content at the LSB of each byte of the image RGB data.
     * @param image the container image.
     * @param content the file byte stream.
     * @param offset the starting byte of the image.
     */
    public static void encode(byte[] image, byte[] content, int offset) throws InvalidPNGImageSizeException {
        if (((content.length + offset) << 3) > image.length) {
            throw new InvalidPNGImageSizeException();
        }
        for (int i = 0; i < content.length; i++) {
            int add = content[i];
            for (int bit = 7; bit >= 0; bit--, offset++) {
                int b = (add >>> bit) & 1;
                image[offset] = (byte) ((image[offset] & 0xFE) | b);
            }
        }
    }

    /**
     * Decode the file content at the LSB of each byte of the image RGB data.
     * @param image the container image.
     * @return the original file byte stream.
     */
    public static byte[] decode(byte[] image) {
        int length = 0;
        int offset = Long.SIZE;
        for (int i = 0; i < Long.SIZE; i++) {
            length = (length << 1) | (image[i] & 1);
        }
        byte[] content = new byte[length];
        for (int b = 0; b < content.length; b++) {
            for (int i = 0; i < 8; i++, offset++) {
                content[b] = (byte) ((content[b] << 1) | (image[offset] & 1));
            }
        }
        return content;
    }
}
