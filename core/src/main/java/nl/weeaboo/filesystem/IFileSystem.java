package nl.weeaboo.filesystem;

import java.io.IOException;
import java.io.InputStream;

/**
 * Represents a file system containing files and folders.
 *
 * @see IWritableFileSystem
 */
public interface IFileSystem {

    /**
     * Closes the file system.
     */
    void close();

    /**
     * Opens an input stream for the file speficied by the given path.
     *
     * @throws IOException If the specified file doesn't exist or couldn't be opened for reading.
     */
    InputStream openInputStream(FilePath path) throws IOException;

    /**
     * Returns {@code true} if this file system is open.
     * @see #close()
     */
    boolean isOpen();

    /**
     * Returns {@code true} if this file system is read-only.
     */
    boolean isReadOnly();

    /**
     * Returns {@code true} if the given path represents a folder.
     */
    boolean isFolder(FilePath path);

    /**
     * Checks if a file with the specified path exists.
     */
    boolean getFileExists(FilePath path);

    /**
     * Returns the size of the file, or {@code 0} if unknown.
     *
     * @throws IOException If the file's metadata couldn't be read.
     */
    long getFileSize(FilePath path) throws IOException;

    /**
     * Returns the last modified time of the file.
     * @return The modified time as UTC milliseconds from the epoch, or {@code 0} if this record doesn't have a valid
     *         modified time associated with it.
     * @throws IOException If the file's metadata couldn't be read.
     */
    long getFileModifiedTime(FilePath path) throws IOException;

    /**
     * Searches for files in this filesystem based on the supplied file collect options.
     */
    Iterable<FilePath> getFiles(FileCollectOptions opts);

}
