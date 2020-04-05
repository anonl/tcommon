package nl.weeaboo.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

final class RandomAccessFileWrapper implements IRandomAccessFile {

    private final RandomAccessFile file;

    RandomAccessFileWrapper(RandomAccessFile file) {
        this.file = file;
    }

    @Override
    public synchronized void close() throws IOException {
        file.close();
    }

    @Override
    public synchronized long skip(long n) throws IOException {
        return file.skipBytes((int)Math.min(Integer.MAX_VALUE, n));
    }

    @Override
    public synchronized int read() throws IOException {
        return file.read();
    }

    @Override
    public synchronized int read(byte[] b, int off, int len) throws IOException {
        return file.read(b, off, len);
    }

    @Override
    public synchronized void write(int b) throws IOException {
        file.write(b);
    }

    @Override
    public synchronized void write(byte[] b, int off, int len) throws IOException {
        file.write(b, off, len);
    }

    @Override
    public synchronized long pos() throws IOException {
        return file.getFilePointer();
    }

    @Override
    public synchronized void seek(long pos) throws IOException {
        file.seek(pos);
    }

    @Override
    public synchronized long length() throws IOException {
        return file.length();
    }

    @Override
    public synchronized InputStream getInputStream() throws IOException {
        return getInputStream(0, length());
    }

    @Override
    public synchronized InputStream getInputStream(long offset, long length) {
        return new RandomAccessInputStream(this, offset, length);
    }

}