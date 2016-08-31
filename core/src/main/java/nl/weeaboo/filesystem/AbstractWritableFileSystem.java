package nl.weeaboo.filesystem;

import java.io.IOException;
import java.io.OutputStream;

public abstract class AbstractWritableFileSystem extends AbstractFileSystem implements IWritableFileSystem {

    @Override
    public boolean isReadOnly() {
        return false;
    }

    protected void checkWritable() throws IOException {
        if (isReadOnly()) {
            throw new IOException("FileSystem is read-only");
        }
        if (!isOpen()) {
            throw new IOException("FileSystem is closed");
        }
    }

    @Override
    public final OutputStream openOutputStream(FilePath path, boolean append) throws IOException {
        checkWritable();
        return newOutputStreamImpl(resolvePath(path, true), append);
    }

    protected abstract OutputStream newOutputStreamImpl(FilePath path, boolean append) throws IOException;

    @Override
    public final void delete(FilePath path) throws IOException {
        checkWritable();
        deleteImpl(resolvePath(path, true));
    }

    protected abstract void deleteImpl(FilePath path) throws IOException;

    @Override
    public final void rename(FilePath src, FilePath dst) throws IOException {
        checkWritable();
        renameImpl(resolvePath(src, false), resolvePath(dst, true));
    }

    protected void renameImpl(FilePath src, FilePath dst) throws IOException {
        copyImpl(src, dst);
        deleteImpl(src);
    }

    @Override
    public final void copy(FilePath src, FilePath dst) throws IOException {
        checkWritable();
        copyImpl(resolvePath(src, false), resolvePath(dst, true));
    }

    protected abstract void copyImpl(FilePath src, FilePath dst) throws IOException;

}
