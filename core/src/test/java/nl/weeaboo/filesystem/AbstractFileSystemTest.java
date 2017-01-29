package nl.weeaboo.filesystem;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import nl.weeaboo.common.StringUtil;

public abstract class AbstractFileSystemTest<FS extends IFileSystem> {

    protected static final FilePath VALID_NAME = FilePath.of("valid.txt");
    protected static final String VALID_CONTENTS = "valid";
    protected static final FilePath INVALID_NAME = FilePath.of("invalid.txt");

    protected static final FilePath SUBFOLDER_FILE = FilePath.of("sub1/sub2/sub.txt");
    protected static final String SUBFOLDER_FILE_CONTENTS = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";

    protected FS fileSystem;

    @Before
    public final void beforeTest() throws IOException {
        fileSystem = createTestFileSystem();
    }

    @After
    public final void afterTest() {
        if (fileSystem != null) {
            fileSystem.close();
        }
    }

    protected abstract FS createTestFileSystem() throws IOException;

    @Test
    public void testFileExists() {
        Assert.assertEquals(true, fileSystem.getFileExists(VALID_NAME));
        Assert.assertEquals(false, fileSystem.getFileExists(INVALID_NAME));
    }


    @Test(expected = FileNotFoundException.class)
    public void modifiedTimeNonExisting() throws IOException {
        fileSystem.getFileModifiedTime(INVALID_NAME);
    }

    @Test
    public void fileSize() throws IOException {
        Assert.assertEquals(StringUtil.toUTF8(VALID_CONTENTS).length, fileSystem.getFileSize(VALID_NAME));
    }

    @Test(expected = FileNotFoundException.class)
    public void fileSizeNonExisting() throws IOException {
        fileSystem.getFileSize(INVALID_NAME);
    }

    @Test
    public void testReadFile() throws IOException {
        Assert.assertEquals(VALID_CONTENTS, FileSystemUtil.readString(fileSystem, VALID_NAME));
        Assert.assertEquals(SUBFOLDER_FILE_CONTENTS, FileSystemUtil.readString(fileSystem, SUBFOLDER_FILE));
    }

    @Test
    public void testGetFolders() throws IOException {
        Set<FilePath> files;

        FilePath sub2 = SUBFOLDER_FILE.getParent();
        FilePath sub1 = sub2.getParent();

        // Find subfolders from root (recursive)
        files = getFiles(FileCollectOptions.folders(FilePath.empty()));
        assertFileSet(Arrays.asList(sub1, sub2), files);

        // Find subfolders from root (non-recursive)
        files = getFiles(nonRecursiveFolders(FilePath.empty()));
        assertFileSet(Arrays.asList(sub1), files);

        // Find subfolders, starting from sub2 (recursive)
        files = getFiles(FileCollectOptions.folders(sub2));
        assertFileSet(Arrays.asList(sub2), files);

        // Find subfolders, starting from sub2 (non-recursive)
        files = getFiles(nonRecursiveFolders(sub2));
        assertFileSet(Arrays.asList(sub2), files);

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
        assertFileSet(Arrays.asList(VALID_NAME), files);

        // Find file, starting from subfolder (recursive)
        FilePath sub2 = SUBFOLDER_FILE.getParent();
        files = getFiles(FileCollectOptions.files(sub2));
        assertFileSet(Arrays.asList(SUBFOLDER_FILE), files);

        // Find file, starting from subfolder (non-recursive)
        files = getFiles(nonRecursiveFiles(sub2));
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
        for (FilePath path : fileSystem.getFiles(opts)) {
            files.add(path);
        }
        return files;
    }

    private void assertFileSet(Collection<FilePath> expected, Set<FilePath> actual) {
        Assert.assertEquals(new HashSet<FilePath>(expected), actual);
    }

}
