package nl.weeaboo.filesystem;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import nl.weeaboo.io.StreamUtil;

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
        return newOutputStreamImpl(resolvePath(path), append);
    }

    protected abstract OutputStream newOutputStreamImpl(FilePath path, boolean append) throws IOException;

    @Override
    public final void delete(FilePath path) throws IOException {
        checkWritable();
        deleteImpl(resolvePath(path));
    }

    protected abstract void deleteImpl(FilePath path) throws IOException;

    @Override
    public final void rename(FilePath src, FilePath dst) throws IOException {
        checkWritable();
        renameImpl(resolvePath(src), resolvePath(dst));
    }

    protected void renameImpl(FilePath src, FilePath dst) throws IOException {
        copyImpl(src, dst);
        deleteImpl(src);
    }

    @Override
    public final void copy(FilePath src, FilePath dst) throws IOException {
        checkWritable();
        copyImpl(resolvePath(src), resolvePath(dst));
    }

    protected void copyImpl(FilePath src, FilePath dst) throws IOException {
        InputStream in = openInputStream(src);
        try {
            OutputStream out = openOutputStream(dst, false);
            try {
                StreamUtil.writeBytes(in, out);
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }
    }

}
