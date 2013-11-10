package fcfp.png;

/**
 * Exception to be thrown when the image file is to small to contain the
 * user's file content.
 * @author Simão Paulo Rato Alves Reis
 * @version 1.0
 */
public class InvalidPNGImageSizeException extends Exception {
    
    /**
     * Default Exception Constructor.
     */
    public InvalidPNGImageSizeException() {
    }

    /**
     * Default Exception Constructor with message error.
     * @param err error message.
     */
    public InvalidPNGImageSizeException(String err) {
        super(err);
    }
}
