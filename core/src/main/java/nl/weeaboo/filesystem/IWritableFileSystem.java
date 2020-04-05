package nl.weeaboo.filesystem;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

/**
 * File system which allows modifications.
 */
public interface IWritableFileSystem extends IFileSystem {

    /**
     * Deletes the file with the given path.
     *
     * @throws FileNotFoundException If the path doesn't point to a valid file.
     * @throws IOException If an I/O exception occurs while trying to delete the file.
     */
    void delete(FilePath path) throws IOException;

    /**
     * Renames a file. If a file with the new name already exists, that file is overwritten.
     *
     * @throws FileNotFoundException If the source path doesn't point to a valid file.
     * @throws IOException If an I/O exception occurs while moving the file.
     */
    void rename(FilePath src, FilePath dst) throws IOException;

    /**
     * Copies a file. If a file with the destination path already exists, that file is overwritten.
     *
     * @throws FileNotFoundException If the source path doesn't point to a valid file.
     * @throws IOException If an I/O exception occurs while copying the file.
     */
    void copy(FilePath src, FilePath dst) throws IOException;

    /**
     * Opens a file for writing.
     *
     * @param append If {@code true}, writing to the output stream appends to the end of the file. If {@code false}, any
     *        existing file content is discarded first.
     * @throws IOException If an I/O exception occurs while writing the file.
     */
    OutputStream openOutputStream(FilePath path, boolean append) throws IOException;

}
