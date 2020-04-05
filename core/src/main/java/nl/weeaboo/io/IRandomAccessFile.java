package nl.weeaboo.io;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Generic interface for seekable files/streams.
 */
public interface IRandomAccessFile extends Closeable {

    /**
     * Attempts to skip {@code s} bytes of input.
     * @return The number of bytes actually skipped.
     * @throws IOException If the file is not seekable, or no longer usable.
     * @see InputStream#skip(long)
     */
    long skip(long s) throws IOException;

    /**
     * Reads an unsigned byte from the file.
     * @return The unsigned byte, or {@code -1} if no byte could be read due to reaching the end of the file.
     * @throws IOException If the file is not readable, or no longer usable.
     * @see InputStream#read()
     */
    int read() throws IOException;

    /**
     * Reads {@code len} bytes into the output buffer {@code buf}, starting at buffer offset {@code off}.
     *
     * @throws IOException If the file is not readable, or no longer usable.
     * @see InputStream#read(byte[], int, int)
     */
    int read(byte[] buf, int off, int len) throws IOException;

    /**
     * Writes a single unsigned byte to the file.
     *
     * @throws IOException If the file is not writable, or no longer usable.
     * @see OutputStream#write(int)
     */
    void write(int b) throws IOException;

    /**
     * Writes {@code len} bytes to the file, using the bytes stored in {@code buf} starting at offset {@code off}.
     *
     * @throws IOException If the file is not writable, or no longer usable.
     * @see OutputStream#write(byte[], int, int)
     */
    void write(byte[] buf, int off, int len) throws IOException;

    /**
     * Returns the current byte-offset within the file.
     *
     * @throws IOException If the file is not accessible.
     */
    long pos() throws IOException;

    /**
     * Modifies the current byte-offset within the file.
     *
     * @throws IOException If the file is not accessible.
     */
    void seek(long pos) throws IOException;

    /**
     * Returns the length of the file in bytes.
     *
     * @throws IOException If the file is not accessible.
     */
    long length() throws IOException;

    /**
     * Opens an inputstream for reading the file's contents, starting at the beginning of the file.
     *
     * @throws IOException If the file is not accessible for reading.
     */
    InputStream getInputStream() throws IOException;

    /**
     * Opens an inputstream for reading the file's contents, limiting the stream to the specified file segment.
     *
     * @throws IOException If the file is not accessible for reading.
     */
    InputStream getInputStream(long offset, long length) throws IOException;

}
