package nl.weeaboo.io;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class StreamUtil {

    private static final int READ_BUFFER_SIZE = 4096;

    private StreamUtil() {
    }

    /**
     * Convenience method for skipping the optional byte-order-marks in an UTF-8/UTF-16 encoded text file.
     *
     * @return The value of {@code off} incremented by the necessary offset to skip the byte-order-mark.
     */
    public static int skipBOM(byte[] bytes, int off, int len) {
        if (isUtf16BOM(bytes, off, len)) {
            return off + 2;
        } else if (isUtf8BOM(bytes, off, len)) {
            return off + 3;
        } else {
            return off;
        }
    }

    private static boolean isUtf16BOM(byte[] bytes, int off, int len) {
        if (len < 2) {
            // Not enough room to contain an UTF-16 BOM
            return false;
        }

        // Check for both big endian (0xFEFF) as well as little endian (0xFFFE) versions
        return (bytes[off] == (byte)0xFF && bytes[off + 1] == (byte)0xFE)
                || (bytes[off] == (byte)0xFF && bytes[off + 1] == (byte)0xFE);
    }

    private static boolean isUtf8BOM(byte[] bytes, int off, int len) {
        if (len < 3) {
            // Not enough room to contain an UTF-8 BOM
            return false;
        }

        return bytes[off] == (byte)0xEF && bytes[off + 1] == (byte)0xBB && bytes[off + 2] == (byte)0xBF;
    }

    public static byte[] readBytes(InputStream in) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream(READ_BUFFER_SIZE);
        writeBytes(in, bout);
        return bout.toByteArray();
    }

    public static void readFully(InputStream in, byte[] dst, int dstOff, int dstLen) throws IOException {
        int read = 0;
        while (read < dstLen) {
            int r = in.read(dst, dstOff + read, dstLen - read);
            if (r < 0) {
                throw new EOFException();
            }
            read += r;
        }
    }

    public static void forceSkip(InputStream in, long toSkip) throws IOException {
        long skipped = 0;
        while (skipped < toSkip) {
            long s = in.skip(toSkip - skipped);
            if (s <= 0) {
                break;
            }
            skipped += s;
        }
        for (long n = skipped; n < toSkip; n++) {
            in.read();
        }
    }

    /**
     * Fully reads the input stream and writes its contents to the output stream.
     */
    public static void writeBytes(InputStream in, OutputStream out) throws IOException {
        byte[] buf = new byte[READ_BUFFER_SIZE];

        int r;
        while ((r = in.read(buf)) > 0) {
            out.write(buf, 0, r);
        }
    }

}
