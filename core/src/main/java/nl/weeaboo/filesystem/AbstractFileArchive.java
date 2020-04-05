package nl.weeaboo.filesystem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import nl.weeaboo.io.IRandomAccessFile;
import nl.weeaboo.io.RandomAccessUtil;

/**
 * Base implementation of a file system stored in a compressed archive (like .zip).
 */
public abstract class AbstractFileArchive extends AbstractFileSystem implements IFileArchive {

    private static final RecordPathComparator pathComparator = new RecordPathComparator();

    protected File file;
    protected IRandomAccessFile rfile;
    protected ArchiveFileRecord[] records;

    protected AbstractFileArchive() {
    }

    @Override
    public void open(File f) throws IOException {
        file = f;

        try {
            open(RandomAccessUtil.wrap(new RandomAccessFile(f, "r")));
        } catch (IOException ioe) {
            throw ioe;
        }
    }

    @Override
    public void open(IRandomAccessFile f) throws IOException {
        rfile = f;

        try {
            records = initRecords(f);
            Arrays.sort(records, pathComparator);
        } catch (IOException ioe) {
            close();
            throw ioe;
        }
    }

    @Override
    protected void closeImpl() {
        if (rfile != null) {
            try {
                rfile.close();
            } catch (IOException e) {
                // Ignore
            }
            rfile = null;
        }
    }

    protected abstract ArchiveFileRecord[] initRecords(IRandomAccessFile f) throws IOException;

    @Override
    public boolean isReadOnly() {
        return true;
    }

    @Override
    public boolean isFolder(FilePath path) {
        try {
            return FilePath.empty().equals(path) || getFileImpl(path).isFolder();
        } catch (FileNotFoundException e) {
            return false;
        }
    }

    @Override
    protected boolean getFileExistsImpl(FilePath path) {
        try {
            return getFileImpl(path) != null;
        } catch (FileNotFoundException e) {
            return false;
        }
    }

    @Override
    protected long getFileSizeImpl(FilePath path) throws IOException {
        return getFileImpl(path).getUncompressedLength();
    }

    @Override
    protected long getFileModifiedTimeImpl(FilePath path) throws IOException {
        return getFileImpl(path).getModifiedTime();
    }

    /**
     * Returns the file record for the archive entry with the specified path.
     *
     * @throws FileNotFoundException If no entry with the specified path could be found within the archive.
     */
    public final ArchiveFileRecord getFile(FilePath path) throws FileNotFoundException {
        return getFileImpl(resolvePath(path));
    }

    protected ArchiveFileRecord getFileImpl(FilePath path) throws FileNotFoundException {
        int index = Arrays.binarySearch(records, path, pathComparator);
        if (index < 0) {
            throw new FileNotFoundException(path.toString());
        }
        return records[index];
    }

    /**
     * Returns the byte offset of the file record within the archive file.
     *
     * @throws IOException If the file offset couldn't be determined.
     */
    public long getFileOffset(FilePath path) throws IOException {
        return getFileOffset(getFileImpl(path).getHeaderOffset());
    }

    protected abstract long getFileOffset(long headerOffset) throws IOException;

    @Override
    public Iterator<ArchiveFileRecord> iterator() {
        return Arrays.asList(records).iterator();
    }

    @Override
    public Iterable<FilePath> getFiles(FileCollectOptions opts) {
        FilePath prefix = opts.getBaseFolder().resolve(opts.getNamePrefix());
        int index = Arrays.binarySearch(records, prefix, pathComparator);
        if (index < 0) {
            index = -(index + 1);
        }

        List<FilePath> result = new ArrayList<FilePath>();
        while (index >= 0 && index < records.length) {
            ArchiveFileRecord record = records[index];
            FilePath path = record.getPath();
            if (!path.startsWith(prefix)) {
                break; //We're past the subrange that matches the prefix
            }

            boolean isFolder = record.isFolder();
            if ((isFolder && opts.collectFolders) || (!isFolder && opts.collectFiles)) {
                if (opts.isValid(path)) {
                    result.add(path);
                }
            }
            index++;
        }
        return result;
    }

    /**
     * @return The backing File object if one exists.
     */
    public File getBackingFile() {
        return file;
    }

    /**
     * Return the backing IRandomAccessFile object used by this ZipArchive.
     */
    public IRandomAccessFile getRandomAccessFile() {
        return rfile;
    }

    private static class RecordPathComparator implements Comparator<Object>, Serializable {

        private static final long serialVersionUID = 1L;

        @Override
        public int compare(Object a, Object b) {
            if (a == b) {
                return 0;
            }
            return getPath(a).compareTo(getPath(b));
        }

        private static FilePath getPath(Object obj) {
            if (obj instanceof ArchiveFileRecord) {
                return ((ArchiveFileRecord)obj).getPath();
            }
            return (FilePath)obj;
        }

    }

}
