package nl.weeaboo.filesystem;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.errorprone.annotations.concurrent.GuardedBy;

/**
 * File system implementation which stores all created files and folders in-memory.
 */
public class InMemoryFileSystem extends AbstractWritableFileSystem {

    private final Object lock = new Object();

    @GuardedBy("lock")
    private final Set<FilePath> folders = new HashSet<FilePath>();

    @GuardedBy("lock")
    private final Map<FilePath, InMemoryFile> files = new HashMap<FilePath, InMemoryFile>();

    private final boolean readOnly;

    /**
     * @param readOnly Sets the {@link #isReadOnly()} flag, which prevents modification.
     */
    public InMemoryFileSystem(boolean readOnly) {
        this.readOnly = readOnly;

        folders.add(FilePath.empty());
    }

    @Override
    public boolean isReadOnly() {
        return readOnly;
    }

    @Override
    protected void closeImpl() {
    }

    @Override
    protected void deleteImpl(FilePath path) throws IOException {
        synchronized (lock) {
            InMemoryFile file = getFile(path, false);
            file.delete();
            files.remove(path);
        }
    }

    @Override
    protected void copyImpl(FilePath src, FilePath dst) throws IOException {
        synchronized (lock) {
            InMemoryFile file = getFile(src, false);
            files.put(dst, file.copy(dst));
        }
    }

    @Override
    protected InputStream openInputStreamImpl(FilePath path) throws IOException {
        synchronized (lock) {
            InMemoryFile file = getFile(path, false);
            return file.openInputStream();
        }
    }

    @Override
    protected OutputStream newOutputStreamImpl(FilePath path, boolean append) throws IOException {
        synchronized (lock) {
            InMemoryFile file = getFile(path, true);

            createFolders(path.getParent());

            return file.openOutputStream(append);
        }
    }

    private void createFolders(@Nullable FilePath folder) {
        if (folder == null) {
            return;
        }
        synchronized (lock) {
            folders.add(folder);
            createFolders(folder.getParent());
        }
    }

    @Override
    public boolean isFolder(FilePath path) {
        synchronized (lock) {
            return folders.contains(path);
        }
    }

    @Override
    protected boolean getFileExistsImpl(FilePath path) {
        synchronized (lock) {
            return files.containsKey(path) || isFolder(path);
        }
    }

    @Override
    protected long getFileSizeImpl(FilePath path) throws IOException {
        synchronized (lock) {
            InMemoryFile file = getFile(path, false);
            return file.getFileSize();
        }
    }

    @Override
    protected long getFileModifiedTimeImpl(FilePath path) throws IOException {
        synchronized (lock) {
            InMemoryFile file = getFile(path, false);
            return file.getModifiedTime();
        }
    }

    protected InMemoryFile getFile(FilePath path, boolean createIfNeeded) throws FileNotFoundException {
        synchronized (lock) {
            InMemoryFile file = files.get(path);
            if (file == null) {
                if (!createIfNeeded) {
                    throw new FileNotFoundException(path.toString());
                }
                if (isFolder(path)) {
                    throw new FileNotFoundException("Unable to create file using a folder path: " + path);
                }
                file = new InMemoryFile(path);
                files.put(path, file);
            }
            return file;
        }
    }

    @Override
    public Iterable<FilePath> getFiles(FileCollectOptions opts) {
        Set<FilePath> result = new HashSet<FilePath>();
        synchronized (lock) {
            if (opts.collectFolders) {
                for (FilePath folder : folders) {
                    if (opts.isValid(folder)) {
                        result.add(folder);
                    }
                }
            }
            if (opts.collectFiles) {
                for (InMemoryFile file : files.values()) {
                    FilePath path = file.getPath();
                    if (opts.isValid(path)) {
                        result.add(path);
                    }
                }
            }
        }
        return result;
    }

}
