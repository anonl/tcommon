package nl.weeaboo.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import nl.weeaboo.common.StringUtil;

/**
 * Functions for working with files.
 */
public final class FileUtil {

    private FileUtil() {
    }

    /**
     * Fully reads the contents of the specified file, and interprets it as an UTF-8 encoded string. This is
     * the reverse operation of {@link #writeUtf8(File, String)}.
     *
     * @throws IOException If the file can't be read.
     * @see #writeUtf8(File, String)
     */
    public static String readUtf8(File file) throws IOException {
        byte[] contents;

        InputStream in = new FileInputStream(file);
        try {
            contents = StreamUtil.readBytes(in);
        } finally {
            in.close();
        }

        return StringUtil.fromUTF8(contents);
    }

    /**
     * Writes the specified string to a file using UTF-8 encoding.
     *
     * @throws IOException If the file can't be written.
     */
    public static void writeUtf8(File file, String string) throws IOException {
        Writer out = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
        try {
            out.write(string);
        } finally {
            out.close();
        }
    }

}
