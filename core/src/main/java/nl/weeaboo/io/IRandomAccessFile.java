package nl.weeaboo.io;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface IRandomAccessFile extends Closeable {

    /**
     * Attempts to skip {@code s} bytes of input.
     * @return The number of bytes actually skipped.
     * @see InputStream#skip(long)
     */
    long skip(long s) throws IOException;

    /**
     * Reads an unsigned byte from the file.
     * @return The unsigned byte, or {@code -1} if no byte could be read due to reaching the end of the file.
     * @see InputStream#read()
     */
    int read() throws IOException;

    /**
     * Reads {@code len} bytes into the output buffer {@code buf}, starting at buffer offset {@code off}.
     *
     * @see InputStream#read(byte[], int, int)
     */
    int read(byte[] buf, int off, int len) throws IOException;

    /**
     * Writes a single unsigned byte to the file.
     *
     * @see OutputStream#write(int)
     */
    void write(int b) throws IOException;

    /**
     * Writes {@code len} bytes to the file, using the bytes stored in {@code buf} starting at offset {@code off}.
     *
     * @see OutputStream#write(byte[], int, int)
     */
    void write(byte[] buf, int off, int len) throws IOException;

    long pos() throws IOException;

    void seek(long pos) throws IOException;

    long length() throws IOException;

    InputStream getInputStream() throws IOException;

    InputStream getInputStream(long offset, long length) throws IOException;

}
