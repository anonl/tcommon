package nl.weeaboo.io;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

final class RandomAccessBufferWrapper implements IRandomAccessFile {

    private final ByteBuffer buf;

    RandomAccessBufferWrapper(byte[] bytes, int off, int len) {
        buf = ByteBuffer.wrap(bytes, off, len);
    }

    @Override
    public void close() throws IOException {
        // Nothing to do
    }

    @Override
    public synchronized long skip(long n) throws IOException {
        int toSkip = (int)Math.min(buf.remaining(), n);
        buf.position(buf.position() + toSkip);
        return toSkip;
    }

    @Override
    public synchronized int read() throws IOException {
        return (buf.hasRemaining() ? buf.get() & 0xFF : -1);
    }

    @Override
    public synchronized int read(byte[] b, int off, int len) throws IOException {
        if (!buf.hasRemaining()) {
            return -1;
        }

        int r = Math.min(buf.remaining(), len);
        buf.get(b, off, r);
        return r;
    }

    @Override
    public synchronized void write(int b) throws IOException {
        if (!buf.hasRemaining()) {
            throw new EOFException("Buffer overflow");
        }
        if (buf.isReadOnly()) {
            throw new IOException("Unable to write to read-only buffer");
        }
        buf.put((byte)b);
    }

    @Override
    public synchronized void write(byte[] b, int off, int len) throws IOException {
        for (int n = 0; n < len; n++) {
            write(b[off + n]);
        }
    }

    @Override
    public synchronized long pos() throws IOException {
        return buf.position();
    }

    @Override
    public synchronized void seek(long pos) throws IOException {
        if (pos < 0 || pos > buf.limit()) {
            throw new IOException("Invalid position: " + pos);
        }
        buf.position((int)pos);
    }

    @Override
    public synchronized long length() throws IOException {
        return buf.limit();
    }

    @Override
    public synchronized InputStream getInputStream() throws IOException {
        return getInputStream(0, length());
    }

    @Override
    public synchronized InputStream getInputStream(long offset, long length) throws IOException {
        return new RandomAccessInputStream(this, offset, length);
    }

}