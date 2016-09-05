package nl.weeaboo.filesystem;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import nl.weeaboo.common.StringUtil;

public abstract class AbstractFileSystemTest {

    protected static final FilePath VALID_NAME = FilePath.of("valid.txt");
    protected static final String VALID_CONTENTS = "valid";
    protected static final FilePath INVALID_NAME = FilePath.of("invalid.txt");

    protected static final FilePath SUBFOLDER_FILE = FilePath.of("subfolder/sub.txt");
    protected static final String SUBFOLDER_FILE_CONTENTS = "sub";

    private long startTime;
    private IWritableFileSystem fs;

    @Before
    public final void beforeTest() throws IOException {
        startTime = System.currentTimeMillis();
        fs = createTestFileSystem();
    }

    protected abstract IWritableFileSystem createTestFileSystem() throws IOException;

    @Test
    public void testFileExists() {
        Assert.assertEquals(true, fs.getFileExists(VALID_NAME));
        Assert.assertEquals(false, fs.getFileExists(INVALID_NAME));
    }

    @Test
    public void modifiedTime() throws IOException {
        long modTime = fs.getFileModifiedTime(VALID_NAME);
        Assert.assertTrue("Modified: " + modTime + ", Start: " + startTime, modTime >= startTime);
    }

    @Test(expected = FileNotFoundException.class)
    public void modifiedTimeNonExisting() throws IOException {
        fs.getFileModifiedTime(INVALID_NAME);
    }

    @Test
    public void fileSize() throws IOException {
        Assert.assertEquals(StringUtil.toUTF8(VALID_CONTENTS).length, fs.getFileSize(VALID_NAME));
    }

    @Test(expected = FileNotFoundException.class)
    public void fileSizeNonExisting() throws IOException {
        fs.getFileSize(INVALID_NAME);
    }

    @Test
    public void deleteFile() throws IOException {
        Assert.assertEquals(true, fs.getFileExists(VALID_NAME));
        fs.delete(VALID_NAME);
        Assert.assertEquals(false, fs.getFileExists(VALID_NAME));
    }

    @Test(expected = FileNotFoundException.class)
    public void deleteNonExisting() throws IOException {
        Assert.assertEquals(false, fs.getFileExists(INVALID_NAME));
        fs.delete(INVALID_NAME);
    }

    @Test
    public void renameFile() throws IOException {
        Assert.assertEquals(true, fs.getFileExists(VALID_NAME));
        Assert.assertEquals(false, fs.getFileExists(INVALID_NAME));
        fs.rename(VALID_NAME, INVALID_NAME);
        Assert.assertEquals(false, fs.getFileExists(VALID_NAME));
        Assert.assertEquals(true, fs.getFileExists(INVALID_NAME));
        Assert.assertEquals(VALID_CONTENTS, FileSystemUtil.readString(fs, INVALID_NAME));
    }

    @Test(expected = FileNotFoundException.class)
    public void renameNonExisting() throws IOException {
        Assert.assertEquals(true, fs.getFileExists(VALID_NAME));
        Assert.assertEquals(false, fs.getFileExists(INVALID_NAME));
        fs.rename(INVALID_NAME, VALID_NAME);
    }

    @Test
    public void copyFile() throws IOException {
        Assert.assertEquals(true, fs.getFileExists(VALID_NAME));
        Assert.assertEquals(false, fs.getFileExists(INVALID_NAME));
        fs.copy(VALID_NAME, INVALID_NAME);
        Assert.assertEquals(true, fs.getFileExists(VALID_NAME));
        Assert.assertEquals(true, fs.getFileExists(INVALID_NAME));
        Assert.assertEquals(VALID_CONTENTS, FileSystemUtil.readString(fs, INVALID_NAME));
    }

    @Test(expected = FileNotFoundException.class)
    public void copyNonExisting() throws IOException {
        Assert.assertEquals(true, fs.getFileExists(VALID_NAME));
        Assert.assertEquals(false, fs.getFileExists(INVALID_NAME));
        fs.copy(INVALID_NAME, VALID_NAME);
    }

    /** Attempt to write to a folder path */
    @Test(expected = FileNotFoundException.class)
    public void writeToFolder() throws IOException {
        fs.openOutputStream(SUBFOLDER_FILE.getParent(), false);
    }

    @Test
    public void writeAppend() throws IOException {
        OutputStream out = fs.openOutputStream(VALID_NAME, true);
        try {
            out.write(StringUtil.toUTF8("append"));
        } finally {
            out.close();
        }

        Assert.assertEquals(VALID_CONTENTS + "append", FileSystemUtil.readString(fs, VALID_NAME));
    }

    @Test
    public void testGetFolders() throws IOException {
        Set<FilePath> files;

        // Find subfolder from root (recursive)
        files = getFiles(FileCollectOptions.folders(FilePath.empty()));
        assertFileSet(Arrays.asList(SUBFOLDER_FILE.getParent()), files);

        // Find subfolder from root (non-recursive)
        files = getFiles(nonRecursiveFolders(FilePath.empty()));
        assertFileSet(Arrays.<FilePath>asList(), files);

        // Find subfolder, starting from subfolder (recursive)
        files = getFiles(FileCollectOptions.folders(SUBFOLDER_FILE.getParent()));
        assertFileSet(Arrays.asList(SUBFOLDER_FILE.getParent()), files);

        // Find subfolder, starting from subfolder (non-recursive)
        files = getFiles(nonRecursiveFolders(SUBFOLDER_FILE.getParent()));
        assertFileSet(Arrays.asList(SUBFOLDER_FILE.getParent()), files);

        // We don't find subfolder if we start from a file within that subfolder
        files = getFiles(FileCollectOptions.folders(SUBFOLDER_FILE));
        assertFileSet(Arrays.<FilePath>asList(), files);
    }

    @Test
    public void testGetFiles() throws IOException {
        Set<FilePath> files;

        // Find file from root (recursive)
        files = getFiles(FileCollectOptions.files(FilePath.empty()));
        assertFileSet(Arrays.asList(VALID_NAME, SUBFOLDER_FILE), files);

        // Find file from root (non-recursive)
        files = getFiles(nonRecursiveFiles(FilePath.empty()));
        assertFileSet(Arrays.<FilePath>asList(), files);

        // Find file, starting from subfolder (recursive)
        files = getFiles(FileCollectOptions.files(SUBFOLDER_FILE.getParent()));
        assertFileSet(Arrays.asList(SUBFOLDER_FILE), files);

        // Find file, starting from subfolder (non-recursive)
        files = getFiles(nonRecursiveFiles(SUBFOLDER_FILE.getParent()));
        assertFileSet(Arrays.asList(SUBFOLDER_FILE), files);
    }

    private static FileCollectOptions nonRecursiveFolders(FilePath basePath) {
        FileCollectOptions opts = FileCollectOptions.folders(basePath);
        opts.recursive = false;
        return opts;
    }

    private FileCollectOptions nonRecursiveFiles(FilePath basePath) {
        FileCollectOptions opts = FileCollectOptions.files(basePath);
        opts.recursive = false;
        return opts;
    }

    private Set<FilePath> getFiles(FileCollectOptions opts) throws IOException {
        Set<FilePath> files = new HashSet<FilePath>();
        for (FilePath path : fs.getFiles(opts)) {
            files.add(path);
        }
        return files;
    }

    private void assertFileSet(Collection<FilePath> expected, Set<FilePath> actual) {
        Assert.assertEquals(new HashSet<FilePath>(expected), actual);
    }

}
