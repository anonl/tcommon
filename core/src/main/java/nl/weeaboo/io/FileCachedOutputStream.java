package nl.weeaboo.io;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import nl.weeaboo.common.Checks;

/**
 * Alternative to {@link ByteArrayOutputStream} that uses a backing file instead of in-memory storage if the
 * output becomes too large.
 */
public class FileCachedOutputStream extends OutputStream {

    private static final int DEFAULT_THRESHOLD = 1 << 20; // 1MiB

    private final int fileThreshold;
    private ByteArrayOutputStream memoryBuffer;

    private File tempFile;
    private FileOutputStream fileOutput;

    public FileCachedOutputStream() {
        this(DEFAULT_THRESHOLD);
    }
    public FileCachedOutputStream(int fileThreshold) {
        this.fileThreshold = Checks.checkRange(fileThreshold, "fileThreshold", 0);
        this.memoryBuffer = new ByteArrayOutputStream(fileThreshold);
    }

    @Override
    public synchronized void close() throws IOException {
        try {
            if (memoryBuffer != null) {
                memoryBuffer.close();
                memoryBuffer = null;
            }
            if (fileOutput != null) {
                fileOutput.close();
            }
        } finally {
            tempFile.delete();
        }
    }

    @Override
    public synchronized void write(byte[] b, int off, int len) throws IOException {
        if (fileOutput != null) {
            fileOutput.write(b, off, len);
        } else {
            if (memoryBuffer.size() + len < fileThreshold) {
                memoryBuffer.write(b, off, len);
            } else {
                flushMemoryBufferToFile();

                fileOutput.write(b, off, len);
            }
        }
    }

    @Override
    public synchronized void write(int b) throws IOException {
        if (fileOutput != null) {
            fileOutput.write(b);
        } else {
            if (memoryBuffer.size() + 1 < fileThreshold) {
                memoryBuffer.write(b);
            } else {
                flushMemoryBufferToFile();

                fileOutput.write(b);
            }
        }
    }

    private synchronized void flushMemoryBufferToFile() throws IOException {
        // Flush memory buffer to file
        fileOutput = openFileStream();

        memoryBuffer.writeTo(fileOutput);
        memoryBuffer.close();
        memoryBuffer = null;
    }

    private FileOutputStream openFileStream() throws IOException {
        tempFile = File.createTempFile("filecachedoutputstream", ".bin");
        tempFile.deleteOnExit();
        return new FileOutputStream(tempFile);
    }

    public synchronized void writeTo(OutputStream out) throws IOException {
        FileInputStream fin = new FileInputStream(tempFile);
        try {
            StreamUtil.writeBytes(fin, out);
        } finally {
            fin.close();
        }
    }
}
