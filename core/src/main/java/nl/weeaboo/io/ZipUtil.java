package nl.weeaboo.io;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import nl.weeaboo.common.Checks;

public final class ZipUtil {

    public enum Compression {
        NONE,
        DEFLATE,
        @Deprecated
        NONE_BAD_CRC
    }

    private ZipUtil() {
    }

    public static void writeFolderEntry(ZipOutputStream zout, String relpath) throws IOException {
        if (relpath.length() > 0 && !relpath.endsWith("/")) {
            relpath += "/";
        }

        ZipEntry entry = new ZipEntry(relpath);
        zout.putNextEntry(entry);
        zout.closeEntry();
    }

    public static void add(ZipOutputStream zout, String relpath, File file, Compression c)
            throws IOException {

        if (file.isDirectory()) {
            writeFolderEntry(zout, relpath);
        } else {
            FileInputStream fin = new FileInputStream(file);
            try {
                writeFileEntry(zout, relpath, fin, file.length(), c);
            } finally {
                fin.close();
            }
        }
    }

    public static void writeFileEntry(ZipOutputStream zout, String relpath, byte[] b, int off, int len,
            Compression c) throws IOException
    {

        ByteArrayInputStream bin = new ByteArrayInputStream(b, off, len);
        try {
            writeFileEntry(zout, relpath, bin, len, c);
        } finally {
            bin.close();
        }
    }

       public static void writeFileEntry(ZipOutputStream zout, String relpath,
                InputStream in, long size, Compression compression) throws IOException
       {
        ZipEntryWriter writer = new ZipEntryWriter(relpath, in, size, compression);
        try {
            writer.writeEntry(zout);
        } finally {
            writer.close();
        }
    }

    private static class ZipEntryWriter implements Closeable {

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
            byte buf[] = new byte[READ_BUFFER_SIZE];
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

}
