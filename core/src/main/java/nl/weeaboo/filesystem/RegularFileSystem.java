package nl.weeaboo.filesystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import nl.weeaboo.common.Checks;

/**
 * File system implementation using regular files.
 */
public final class RegularFileSystem extends AbstractWritableFileSystem {

    private final File rootFolder;

    public RegularFileSystem(File rootFolder) {
        this.rootFolder = Checks.checkNotNull(rootFolder);
    }

    private final File resolve(FilePath path) {
        return new File(rootFolder, path.toString());
    }

    private final File resolveExisting(FilePath path) throws FileNotFoundException {
        File file = resolve(path);
        if (!file.exists()) {
            throw new FileNotFoundException(path.toString());
        }
        return file;
    }

    @Override
    protected void closeImpl() {
    }

    @Override
    protected InputStream openInputStreamImpl(FilePath path) throws IOException {
        return new FileInputStream(resolveExisting(path));
    }

    @Override
    protected OutputStream newOutputStreamImpl(FilePath path, boolean append) throws IOException {
        File file = resolve(path);

        File parentFolder = file.getParentFile();
        if (parentFolder != null) {
            parentFolder.mkdirs();
        }

        return new FileOutputStream(file, append);
    }

    @Override
    protected void deleteImpl(FilePath path) throws IOException {
        File file = resolveExisting(path);
        file.delete();

        if (file.exists()) {
            throw new IOException("Error deleting file: " + path);
        }
    }

    @Override
    public boolean isFolder(FilePath path) {
        return resolve(path).isDirectory();
    }

    @Override
    protected boolean getFileExistsImpl(FilePath path) {
        return resolve(path).exists();
    }

    @Override
    protected long getFileSizeImpl(FilePath path) throws IOException {
        return resolveExisting(path).length();
    }

    @Override
    protected long getFileModifiedTimeImpl(FilePath path) throws IOException {
        return resolveExisting(path).lastModified();
    }

    @Override
    public Iterable<FilePath> getFiles(FileCollectOptions opts) {
        List<FilePath> result = new ArrayList<FilePath>();

        FilePath baseSearchPath = opts.getBaseFolder();
        File baseSearchFolder = resolve(baseSearchPath);
        if (baseSearchFolder.isDirectory()) {
            if (opts.collectFolders && opts.isValid(baseSearchPath)) {
                result.add(baseSearchPath);
            }
            getFolderContents(result, baseSearchFolder, baseSearchPath, opts);
        } else {
            if (opts.collectFiles && opts.isValid(baseSearchPath)) {
                result.add(baseSearchPath);
            }
        }

        return result;
    }

    private void getFolderContents(List<FilePath> result, File folder, FilePath folderPath, FileCollectOptions opts) {
        File[] childFiles = folder.listFiles();
        if (childFiles != null) {
            for (File childFile : childFiles) {
                FilePath childPath = folderPath.resolve(childFile.getName());
                if (childFile.isDirectory()) {
                    if (opts.isValid(childPath)) {
                        if (opts.collectFolders) {
                            result.add(childPath);
                        }
                        if (opts.recursive) {
                            getFolderContents(result, childFile, childPath, opts);
                        }
                    }
                } else {
                    if (opts.collectFiles && opts.isValid(childPath)) {
                        result.add(childPath);
                    }
                }
            }
        }
    }

}
