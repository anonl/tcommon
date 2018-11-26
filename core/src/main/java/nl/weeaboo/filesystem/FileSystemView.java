package nl.weeaboo.filesystem;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import nl.weeaboo.common.Checks;

public class FileSystemView implements IFileSystem {

    private final IFileSystem fileSystem;
    private final FilePath basePath;

    /**
     * @param basePath Base path for this file system view. This view will resolve all of its paths relative to the base
     *        path.
     */
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

    /**
     * Returns the base path that's used to resolve all path strings unto the underlying file system.
     */
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

        FilePath oldPrefix = opts.getPrefix();
        try {
            opts.setPrefix(basePath.resolve(oldPrefix));

            for (FilePath path : fileSystem.getFiles(opts)) {
                FilePath relpath = basePath.relativize(path);
                if (relpath.equals(FilePath.empty())) {
                    /*
                     * Don't include the 'root' folder in search results. This is needed to match the behavior
                     * of the other filesystem implementations.
                     */
                    continue;
                }

                result.add(relpath);
            }
        } finally {
            opts.setPrefix(oldPrefix);
        }
        return result;
    }

}
