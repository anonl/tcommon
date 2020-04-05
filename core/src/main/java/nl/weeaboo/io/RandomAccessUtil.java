package nl.weeaboo.io;

import java.io.RandomAccessFile;

/**
 * Functions for creating/using seekable (random-access) streams.
 */
public final class RandomAccessUtil {

    /**
     * Constructs a new random access file.
     */
    public static IRandomAccessFile wrap(RandomAccessFile file) {
        return new RandomAccessFileWrapper(file);
    }

    /**
     * Constructs a new random access file.
     */
    public static IRandomAccessFile wrap(byte[] bytes, int off, int len) {
        return new RandomAccessBufferWrapper(bytes, off, len);
    }

}
