package nl.weeaboo.filesystem;

import java.io.IOException;
import java.io.OutputStream;

public interface IWritableFileSystem extends IFileSystem {

    void delete(FilePath path) throws IOException;

    void rename(FilePath src, FilePath dst) throws IOException;

    void copy(FilePath src, FilePath dst) throws IOException;

    OutputStream openOutputStream(FilePath path, boolean append) throws IOException;

}
