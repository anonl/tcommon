package nl.weeaboo.filesystem;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

public interface IFileSystem {

    void close();

	InputStream openInputStream(String path) throws IOException;

	boolean isOpen();
	boolean isReadOnly();
	boolean getFileExists(String path);
	long getFileSize(String path) throws IOException;
	long getFileModifiedTime(String path) throws IOException;

	void getFiles(Collection<String> out, String path, boolean recursive) throws IOException;
	void getSubFolders(Collection<String> out, String path, boolean recursive) throws IOException;

}
