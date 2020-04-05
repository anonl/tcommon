package nl.weeaboo.io;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Functions for working with zip-files.
 */
public final class ZipUtil {

    /**
     * Compression method.
     */
    public enum Compression {
        NONE,
        DEFLATE;
    }

    private ZipUtil() {
    }

    /**
     * Writes a new folder entry to the ZIP file.
     *
     * @throws IOException If an I/O error occurs while writing the entry.
     */
    public static void writeFolderEntry(ZipOutputStream zout, String relpath) throws IOException {
        if (relpath.length() > 0 && !relpath.endsWith("/")) {
            relpath += "/";
        }

        ZipEntry entry = new ZipEntry(relpath);
        zout.putNextEntry(entry);
        zout.closeEntry();
    }

    /**
     * Writes a new entry to the ZIP file.
     *
     * @throws IOException If an I/O error occurs while writing the entry.
     */
    public static void add(ZipOutputStream zout, String relpath, File file, Compression c) throws IOException {
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

    /**
     * Writes a new file entry to the ZIP file.
     *
     * @throws IOException If an I/O error occurs while writing the entry.
     */
    public static void writeFileEntry(ZipOutputStream zout, String relpath, byte[] b, int off, int len, Compression c)
            throws IOException {

        ByteArrayInputStream bin = new ByteArrayInputStream(b, off, len);
        try {
            writeFileEntry(zout, relpath, bin, len, c);
        } finally {
            bin.close();
        }
    }

    /**
     * Writes a new file entry to the ZIP file.
     *
     * @throws IOException If an I/O error occurs while writing the entry.
     */
    public static void writeFileEntry(ZipOutputStream zout, String relpath, InputStream in, long size,
            Compression compression) throws IOException {

        ZipEntryWriter writer = new ZipEntryWriter(relpath, in, size, compression);
        try {
            writer.writeEntry(zout);
        } finally {
            writer.close();
        }
    }

}
