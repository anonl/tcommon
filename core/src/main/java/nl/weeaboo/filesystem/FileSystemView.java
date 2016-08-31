package nl.weeaboo.filesystem;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import nl.weeaboo.common.Checks;

public class FileSystemView implements IFileSystem {

	private final IFileSystem fileSystem;
	private final FilePath basePath;

	public FileSystemView(IFileSystem fileSystem, FilePath basePath) {
		this.fileSystem = Checks.checkNotNull(fileSystem);
		this.basePath = Checks.checkNotNull(basePath);
	}

	@Override
	public void close() {
		fileSystem.close();
	}

	@Override
	public InputStream openInputStream(FilePath path) throws IOException {
		return fileSystem.openInputStream(resolvePath(path));
	}

	@Override
	public boolean isOpen() {
		return fileSystem.isOpen();
	}

	@Override
	public boolean isReadOnly() {
		return fileSystem.isReadOnly();
	}

	public FilePath getBasePath() {
		return basePath;
	}

    protected FilePath resolvePath(FilePath relPath) {
        return basePath.resolve(relPath);
    }

    protected FilePath relativizePath(FilePath fullPath) {
        return basePath.relativize(fullPath);
    }

	@Override
	public boolean getFileExists(FilePath path) {
		return fileSystem.getFileExists(resolvePath(path));
	}

	@Override
	public long getFileSize(FilePath path) throws IOException {
		return fileSystem.getFileSize(resolvePath(path));
	}

	@Override
	public long getFileModifiedTime(FilePath path) throws IOException {
		return fileSystem.getFileModifiedTime(resolvePath(path));
	}

    @Override
    public Iterable<FilePath> getFiles(FileCollectOptions opts) throws IOException {
        List<FilePath> result = new ArrayList<FilePath>();
        for (FilePath path : fileSystem.getFiles(opts)) {
            result.add(basePath.relativize(path));
        }
        return result;
    }

}
