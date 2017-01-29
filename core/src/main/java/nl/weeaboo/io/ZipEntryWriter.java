package nl.weeaboo.io;

import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import nl.weeaboo.common.Checks;
import nl.weeaboo.io.ZipUtil.Compression;

final class ZipEntryWriter implements Closeable {

    private static final int READ_BUFFER_SIZE = 4096;

    private final String relpath;
    private final InputStream in;
    private final long size;
    private final Compression compression;

    // Temporary storage of InputStream contents (may be null, depending on settings)
    private FileCachedOutputStream contentStorage;

    public ZipEntryWriter(String relpath, InputStream in, long size, Compression compression) {
        this.relpath = Checks.checkNotNull(relpath);
        this.in = Checks.checkNotNull(in);

        Checks.checkArgument(size >= 0, "size must be >= 0, was: " + size);
        this.size = size;

        this.compression = Checks.checkNotNull(compression);
    }

    @Override
    public void close() throws IOException {
        if (contentStorage != null) {
            contentStorage.close();
        }
    }

    public final void writeEntry(ZipOutputStream zout) throws IOException {
        ZipEntry entry = new ZipEntry(relpath);
        if (compression == Compression.DEFLATE) {
            entry.setMethod(ZipEntry.DEFLATED);
            // Stores sizes and CRC after record instead of before
        } else {
            entry.setMethod(ZipEntry.STORED);
            entry.setSize(size);
            // Must write CRC before record if compression == STORED
            entry.setCrc(calculateCRC());
        }
        zout.putNextEntry(entry);
        writeContentsTo(zout);
        zout.closeEntry();
    }

    public long calculateCRC() throws IOException {
        contentStorage = new FileCachedOutputStream();

        CRC32 crc = new CRC32();
        long read = 0;
        byte[] buf = new byte[READ_BUFFER_SIZE];
        while (read < size) {
            int r = in.read(buf);
            if (r < 0) {
                throw new EOFException("Unexpected end of file, read=" + read + " expected=" + size);
            }

            read += r;
            crc.update(buf, 0, r);
            contentStorage.write(buf, 0, r);
        }
        return crc.getValue();
    }

    public void writeContentsTo(ZipOutputStream zout) throws IOException {
        if (contentStorage != null) {
            contentStorage.writeTo(zout);
        } else {
            StreamUtil.writeBytes(in, zout);
        }
    }

}