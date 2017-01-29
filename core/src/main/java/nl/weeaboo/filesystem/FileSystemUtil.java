package nl.weeaboo.filesystem;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import nl.weeaboo.common.StringUtil;
import nl.weeaboo.io.StreamUtil;

public final class FileSystemUtil {

    private FileSystemUtil() {
    }

    /**
     * Reads an entire file entry and returns its contents as a byte array.
     *
     * @throws IOException If the requested file entry doesn't exist or an I/O error occurred while reading its
     *         contents.
     */
    public static byte[] readBytes(IFileSystem fs, FilePath path) throws IOException {
        byte[] bytes;
        InputStream in = fs.openInputStream(path);
        try {
            bytes = StreamUtil.readBytes(in);
        } finally {
            in.close();
        }
        return bytes;
    }

    /**
     * Reads an entire file entry and returns its contents as a UTF-8 encoded string. If the file contains a byte order
     * mark, it's skipped automatically.
     *
     * @see #readBytes(IFileSystem, FilePath)
     * @throws IOException If the requested file entry doesn't exist or an I/O error occurred while reading its
     *         contents.
     */
    public static String readString(IFileSystem fs, FilePath path) throws IOException {
        byte[] bytes = readBytes(fs, path);
        int skip = StreamUtil.skipBOM(bytes, 0, bytes.length);
        return StringUtil.fromUTF8(bytes, skip, bytes.length - skip);
    }

    /**
     * Writes an entire text file at once. The file's contents are stored in UTF-8 encoding.
     *
     * @see #writeBytes(IWritableFileSystem, FilePath, byte[])
     * @throws IOException If an I/O error occurred while attempting to write to the file.
     */
    public static void writeString(IWritableFileSystem fs, FilePath path, String content) throws IOException {
        writeBytes(fs, path, StringUtil.toUTF8(content));
    }

    /**
     * Writes an entire file at once.
     *
     * @throws IOException If an I/O error occurred while attempting to write to the file.
     */
    public static void writeBytes(IWritableFileSystem fs, FilePath path, byte[] content) throws IOException {
        OutputStream out = fs.openOutputStream(path, false);
        try {
            out.write(content);
        } finally {
            out.close();
        }
    }

}
