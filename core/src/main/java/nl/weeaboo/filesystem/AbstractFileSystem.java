package nl.weeaboo.filesystem;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractFileSystem implements IFileSystem {

    private AtomicBoolean closed = new AtomicBoolean();

    protected AbstractFileSystem() {
    }

    /**
     * @param path The user-specified path.
     */
    protected FilePath resolvePath(FilePath path) {
        return path;
    }

    @Override
    public final void close() {
        if (closed.compareAndSet(false, true)) {
            closeImpl();
        }
    }

    protected abstract void closeImpl();

    @Override
    public boolean isOpen() {
        return !closed.get();
    }

    @Override
    public final InputStream openInputStream(FilePath path) throws IOException {
        return openInputStreamImpl(resolvePath(path));
    }

    protected abstract InputStream openInputStreamImpl(FilePath path) throws IOException;

    @Override
    public final boolean getFileExists(FilePath path) {
        return getFileExistsImpl(resolvePath(path));
    }

    protected abstract boolean getFileExistsImpl(FilePath path);

    @Override
    public final long getFileSize(FilePath path) throws IOException {
        return getFileSizeImpl(resolvePath(path));
    }

    protected abstract long getFileSizeImpl(FilePath path) throws IOException;

    @Override
    public final long getFileModifiedTime(FilePath path) throws IOException {
        return getFileModifiedTimeImpl(resolvePath(path));
    }

    protected abstract long getFileModifiedTimeImpl(FilePath path) throws IOException;

}
