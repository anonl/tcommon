package nl.weeaboo.filesystem;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import nl.weeaboo.common.StringUtil;

public abstract class AbstractFileSystemTest {

    protected static final FilePath VALID_NAME = FilePath.of("valid.txt");
    protected static final String VALID_CONTENTS = "test";
    protected static final FilePath INVALID_NAME = FilePath.of("invalid.txt");

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
}
