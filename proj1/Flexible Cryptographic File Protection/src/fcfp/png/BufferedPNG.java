package fcfp.png;

import fcfp.util.ByteCast;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Steganography module for hiding a file content in a PNG image.
 *
 * @author Simão Paulo Rato Alves Reis
 * @version 1.0
 */
public class BufferedPNG {

    private BufferedImage input;

    /**
     * Default construct which sets the image container file's byte stream.
     *
     * @param path the complete path to the PNG image container.
     * @throws IOException when something goes wrong while opening the image.
     */
    public BufferedPNG(String path) throws IOException {
        input = getImage(path);
    }

    /**
     * Encode a file content in the store PNG file.
     *
     * @param inPath the file to be read.
     * @param outPath the image file with the hidden content.
     * @throws FileNotFoundException in case the file was not found.
     * @throws IOException if something goes wrong while writing the output.
     * image.
     */
    public void encode(String inPath, String outPath) throws FileNotFoundException, IOException, InvalidPNGImageSizeException {
        BufferedImage output = copyImage(input);
        byte[] paint = getByteData(output);
        File msg = new File(inPath);
        byte[] content = new byte[(int) msg.length()];
        FileInputStream fileInputStream = new FileInputStream(msg);
        fileInputStream.read(content);
        byte length[] = ByteCast.long2ByteArray(content.length);
        LessSignificantBit.encode(paint, length, 0);
        LessSignificantBit.encode(paint, content, Long.SIZE);
        File file = new File(outPath);
        ImageIO.write(output, "png", file);
    }

    /**
     * Decode a file content in the store PNG file.
     *
     * @param outPath the ouputed hidden file's name.
     * @throws FileNotFoundException in case the file was not found.
     * @throws IOException if something goes wrong while writing the output
     * image.
     */
    public void decode(String outPath) throws FileNotFoundException, IOException {
        BufferedImage output = copyImage(input);
        byte[] paint = getByteData(output);
        byte[] outContent = LessSignificantBit.decode(paint);
        File out = new File(outPath);
        FileOutputStream outStream = new FileOutputStream(out);
        outStream.write(outContent);
    }

    /**
     * Convert a file image path to a associated BufferedImage object.
     *
     * @param path the full file url.
     * @return the BufferedImage of the PNG file.
     * @throws IOException if something goes wrong while writing the output.
     */
    private BufferedImage getImage(String path) throws IOException {
        BufferedImage image;
        File file = new File(path);
        image = ImageIO.read(file);
        return image;
    }

    /**
     * Copy a BufferedImage to another BufferedImage.
     *
     * @param image the original BufferedImage.
     * @return the copy BufferedImage.
     */
    private BufferedImage copyImage(BufferedImage image) {
        BufferedImage copy = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphics = copy.createGraphics();
        graphics.drawRenderedImage(image, null);
        graphics.dispose();
        return copy;
    }

    /**
     * Get the RGB data's byte stream from a BufferedImage.
     *
     * @param image the BufferedImage object.
     * @return the RGB data byte stream.
     */
    private byte[] getByteData(BufferedImage image) {
        WritableRaster raster = image.getRaster();
        DataBufferByte buffer = (DataBufferByte) raster.getDataBuffer();
        return buffer.getData();
    }
}
