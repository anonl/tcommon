package nl.weeaboo.io;

import java.io.RandomAccessFile;

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
