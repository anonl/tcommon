package nl.weeaboo.filesystem;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class InMemoryFileSystem extends AbstractWritableFileSystem {

	private final Map<FilePath, InMemoryFile> files = new HashMap<FilePath, InMemoryFile>();

    private final boolean readOnly;

	public InMemoryFileSystem(boolean readOnly) {
		this.readOnly = readOnly;
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
		synchronized (files) {
            InMemoryFile file = getFile(path, false);
			file.delete();
			files.remove(path);
		}
	}

	@Override
	protected void copyImpl(FilePath src, FilePath dst) throws IOException {
		synchronized (files) {
            InMemoryFile file = getFile(src, false);
			files.put(dst, file.copy(dst));
		}
	}

	@Override
	protected InputStream openInputStreamImpl(FilePath path) throws IOException {
		synchronized (files) {
            InMemoryFile file = getFile(path, false);
			return file.openInputStream();
		}
	}

	@Override
	protected OutputStream newOutputStreamImpl(FilePath path, boolean append) throws IOException {
		synchronized (files) {
            InMemoryFile file = getFile(path, true);
            return file.openOutputStream(append);
		}
	}

	@Override
	protected boolean getFileExistsImpl(FilePath path) {
		synchronized (files) {
			return files.containsKey(path);
		}
	}

	@Override
	protected long getFileSizeImpl(FilePath path) throws IOException {
		synchronized (files) {
            InMemoryFile file = getFile(path, false);
			return file.getFileSize();
		}
	}

	@Override
	protected long getFileModifiedTimeImpl(FilePath path) throws IOException {
		synchronized (files) {
            InMemoryFile file = getFile(path, false);
			return file.getModifiedTime();
		}
	}

    protected InMemoryFile getFile(FilePath path, boolean createIfNeeded) throws FileNotFoundException {
		synchronized (files) {
			InMemoryFile file = files.get(path);
			if (file == null) {
                if (!createIfNeeded) {
                    throw new FileNotFoundException(path.toString());
                }
                if (path.isFolder()) {
                    throw new FileNotFoundException("Unable to create file using a folder path: " + path);
                }
                file = new InMemoryFile(path);
                files.put(path, file);
			}
			return file;
		}
	}

    @Override
    public Iterable<FilePath> getFiles(FileCollectOptions opts) throws IOException {
        Set<FilePath> result = new HashSet<FilePath>();
        synchronized (files) {
            for (InMemoryFile file : files.values()) {
                FilePath path = file.getPath();
                if (path.startsWith(opts.prefix)) {
                    if (opts.collectFolders) {
                        FilePath parent = path;
                        while ((parent = parent.getParent()) != null) {
                            if (isValid(parent, opts)) {
                                result.add(parent);
                            }
                        }
                    }

                    if (opts.collectFiles && isValid(path, opts)) {
                        result.add(path);
                    }
                }
            }
        }
        return result;
    }

    private boolean isValid(FilePath file, FileCollectOptions opts) {
        if (opts.recursive) {
            // This folder is equal to, or a descendant of the base search path
            return file.startsWith(opts.prefix);
        } else {
            // This file is equal to, or a direct child of the base search path
            return opts.prefix.equals(file) || opts.prefix.equals(file.getParent());
        }
    }

}
