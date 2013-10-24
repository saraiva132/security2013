package fcfp.util.iv;

/**
 * Initialization Vector available sizes.
 *
 * @author Simão Paulo Rato Alves Reis
 * @version 1.0
 */
public enum InitVectorSIZE {

    SIZE32(32), SIZE64(64), SIZE128(128);
    private final int size;

    InitVectorSIZE(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}
