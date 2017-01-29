package nl.weeaboo.filesystem;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Writes files in a way that prevents data loss even if a crash occurs while writing.
 */
public class SecureFileWriter {

    private final IWritableFileSystem fs;

    /**
     * @param fs File system that this writes delegates its read/write operations to.
     */
    public SecureFileWriter(IWritableFileSystem fs) {
        this.fs = fs;
    }

    /**
     * Opens a file for reading, in a way that's compatible with the behavior of
     * {@link #newOutputStream(FilePath, boolean)}.
     *
     * @throws IOException If an I/O exception occurs.
     */
    public InputStream newInputStream(FilePath path) throws IOException {
        if (fs.getFileExists(path) && fs.getFileSize(path) > 0) {
            return fs.openInputStream(path);
        }
        return fs.openInputStream(backupPath(path));
    }

    /**
     * Opens a file for writing. The file is written in a way that prevents data loss if the application dies while
     * writing. Always use {@link #newInputStream(FilePath)} to read files written in this way.
     *
     * @throws IOException If an I/O exception occurs.
     */
    public OutputStream newOutputStream(final FilePath path, boolean append) throws IOException {
        final FilePath backupPath = backupPath(path);

        if (append) {
            if (fs.getFileExists(path)) {
                fs.copy(path, backupPath);
            }
        }
        return new FilterOutputStream(fs.openOutputStream(backupPath, append)) {
            @Override
            public void close() throws IOException {
                super.close();

                if (fs.getFileExists(path)) {
                    fs.delete(path);
                }
                fs.rename(backupPath, path);
            }
        };
    }

    private static FilePath backupPath(FilePath path) {
        return FilePath.of(path + ".bak");
    }

    protected boolean isMainFileOK(FilePath path) {
        try {
            return fs.getFileExists(path) && fs.getFileSize(path) > 0;
        } catch (IOException e) {
            return false;
        }
    }

}
