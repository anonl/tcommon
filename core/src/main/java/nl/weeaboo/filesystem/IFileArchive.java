package nl.weeaboo.filesystem;

import java.io.File;
import java.io.IOException;

import nl.weeaboo.io.IRandomAccessFile;

public interface IFileArchive extends Iterable<ArchiveFileRecord> {

    /**
     * Opens the file archive.
     *
     * @throws IOException If an I/O exception occurred while opening the archive file.
     */
    void open(File f) throws IOException;

    /**
     * Opens the file archive.
     *
     * @throws IOException If an I/O exception occurred while opening the archive file.
     */
    void open(IRandomAccessFile f) throws IOException;

}
