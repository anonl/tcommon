package nl.weeaboo.filesystem;

import java.io.IOException;
import java.io.InputStream;

public interface IFileSystem {

    void close();

	InputStream openInputStream(FilePath path) throws IOException;

	boolean isOpen();
	boolean isReadOnly();
	boolean getFileExists(FilePath path);
	long getFileSize(FilePath path) throws IOException;
	long getFileModifiedTime(FilePath path) throws IOException;

	Iterable<FilePath> getFiles(FileCollectOptions opts) throws IOException;

}
