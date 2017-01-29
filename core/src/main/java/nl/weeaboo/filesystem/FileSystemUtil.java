package nl.weeaboo.filesystem;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import nl.weeaboo.common.StringUtil;
import nl.weeaboo.io.StreamUtil;

public final class FileSystemUtil {

    private FileSystemUtil() {
    }

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

    public static String readString(IFileSystem fs, FilePath path) throws IOException {
        byte[] bytes = readBytes(fs, path);
        int skip = StreamUtil.skipBOM(bytes, 0, bytes.length);
        return StringUtil.fromUTF8(bytes, skip, bytes.length - skip);
    }

    public static void writeString(IWritableFileSystem fs, FilePath path, String content) throws IOException {
        writeBytes(fs, path, StringUtil.toUTF8(content));
    }

    public static void writeBytes(IWritableFileSystem fs, FilePath path, byte[] content) throws IOException {
        OutputStream out = fs.openOutputStream(path, false);
        try {
            out.write(content);
        } finally {
            out.close();
        }
    }

}
