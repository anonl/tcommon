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

    public SecureFileWriter(IWritableFileSystem fs) {
        this.fs = fs;
    }

    public InputStream newInputStream(FilePath path) throws IOException {
        if (fs.getFileExists(path) && fs.getFileSize(path) > 0) {
            return fs.openInputStream(path);
        }
        return fs.openInputStream(backupPath(path));
    }

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

    public long getFileSize(FilePath path) throws IOException {
        if (isMainFileOK(path)) {
            return fs.getFileSize(path);
        }
        return fs.getFileSize(backupPath(path));
    }

}
