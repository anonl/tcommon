package nl.weeaboo.filesystem;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

/**
 * Aggregation of multiple file systems.
 */
public class MultiFileSystem implements IFileSystem {

    private final IFileSystem[] fileSystems;
    private boolean closed;

    /**
     * @param fileSystems The file systems that this multi filesystem delegates to.
     * @see #MultiFileSystem(Collection)
     */
    public MultiFileSystem(IFileSystem... fileSystems) {
        this(Arrays.asList(fileSystems));
    }

    /**
     * @param fileSystems The file systems that this multi filesystem delegates to.
     */
    public MultiFileSystem(Collection<IFileSystem> fileSystems) {
        this.fileSystems = fileSystems.toArray(new IFileSystem[fileSystems.size()]);
    }

    @Override
    public void close() {
        closed = true;
        for (IFileSystem fs : fileSystems) {
            fs.close();
        }
    }

    @Override
    public boolean isOpen() {
        return !closed;
    }

    @Override
    public final boolean isReadOnly() {
        return true;
    }

    @Override
    public InputStream openInputStream(FilePath path) throws IOException {
        for (IFileSystem fs : fileSystems) {
            if (fs.getFileExists(path)) {
                return fs.openInputStream(path);
            }
        }
        throw new FileNotFoundException(path.toString());
    }

    @Override
    public boolean isFolder(FilePath path) {
        for (IFileSystem fs : fileSystems) {
            if (fs.isFolder(path)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean getFileExists(FilePath path) {
        for (IFileSystem fs : fileSystems) {
            if (fs.getFileExists(path)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public long getFileSize(FilePath path) throws IOException {
        for (IFileSystem fs : fileSystems) {
            if (fs.getFileExists(path)) {
                return fs.getFileSize(path);
            }
        }
        throw new FileNotFoundException(path.toString());
    }

    @Override
    public long getFileModifiedTime(FilePath path) throws IOException {
        for (IFileSystem fs : fileSystems) {
            if (fs.getFileExists(path)) {
                return fs.getFileModifiedTime(path);
            }
        }
        throw new FileNotFoundException(path.toString());
    }

    /**
     * @return The primary (first) writable file system in this multi filesystem, or {@code null} if no
     *         writable file system could be found.
     */
    public IWritableFileSystem getWritableFileSystem() {
        for (IFileSystem fs : fileSystems) {
            if (!fs.isReadOnly() && fs instanceof IWritableFileSystem) {
                return (IWritableFileSystem)fs;
            }
        }
        return null;
    }

    @Override
    public Iterable<FilePath> getFiles(FileCollectOptions opts) {
        Set<FilePath> files = new TreeSet<FilePath>();
        for (IFileSystem fs : fileSystems) {
            if (fs.isOpen()) {
                for (FilePath path : fs.getFiles(opts)) {
                    files.add(path);
                }
            }
        }
        return files;
    }

}
