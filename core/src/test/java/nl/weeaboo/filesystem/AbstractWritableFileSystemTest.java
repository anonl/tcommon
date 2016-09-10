package nl.weeaboo.filesystem;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import org.junit.Assert;
import org.junit.Test;

import nl.weeaboo.common.StringUtil;

public abstract class AbstractWritableFileSystemTest<FS extends IWritableFileSystem>
        extends AbstractFileSystemTest<FS> {

    @Test
    public void deleteFile() throws IOException {
        Assert.assertEquals(true, fileSystem.getFileExists(VALID_NAME));
        fileSystem.delete(VALID_NAME);
        Assert.assertEquals(false, fileSystem.getFileExists(VALID_NAME));
    }

    @Test(expected = FileNotFoundException.class)
    public void deleteNonExisting() throws IOException {
        Assert.assertEquals(false, fileSystem.getFileExists(INVALID_NAME));
        fileSystem.delete(INVALID_NAME);
    }

    @Test
    public void renameFile() throws IOException {
        Assert.assertEquals(true, fileSystem.getFileExists(VALID_NAME));
        Assert.assertEquals(false, fileSystem.getFileExists(INVALID_NAME));
        fileSystem.rename(VALID_NAME, INVALID_NAME);
        Assert.assertEquals(false, fileSystem.getFileExists(VALID_NAME));
        Assert.assertEquals(true, fileSystem.getFileExists(INVALID_NAME));
        Assert.assertEquals(VALID_CONTENTS, FileSystemUtil.readString(fileSystem, INVALID_NAME));
    }

    @Test(expected = FileNotFoundException.class)
    public void renameNonExisting() throws IOException {
        Assert.assertEquals(true, fileSystem.getFileExists(VALID_NAME));
        Assert.assertEquals(false, fileSystem.getFileExists(INVALID_NAME));
        fileSystem.rename(INVALID_NAME, VALID_NAME);
    }

    @Test
    public void copyFile() throws IOException {
        Assert.assertEquals(true, fileSystem.getFileExists(VALID_NAME));
        Assert.assertEquals(false, fileSystem.getFileExists(INVALID_NAME));
        fileSystem.copy(VALID_NAME, INVALID_NAME);
        Assert.assertEquals(true, fileSystem.getFileExists(VALID_NAME));
        Assert.assertEquals(true, fileSystem.getFileExists(INVALID_NAME));
        Assert.assertEquals(VALID_CONTENTS, FileSystemUtil.readString(fileSystem, INVALID_NAME));
    }

    @Test(expected = FileNotFoundException.class)
    public void copyNonExisting() throws IOException {
        Assert.assertEquals(true, fileSystem.getFileExists(VALID_NAME));
        Assert.assertEquals(false, fileSystem.getFileExists(INVALID_NAME));
        fileSystem.copy(INVALID_NAME, VALID_NAME);
    }

    /** Attempt to write to a folder path */
    @Test(expected = FileNotFoundException.class)
    public void writeToFolder() throws IOException {
        fileSystem.openOutputStream(SUBFOLDER_FILE.getParent(), false);
    }

    @Test
    public void writeAppend() throws IOException {
        OutputStream out = fileSystem.openOutputStream(VALID_NAME, true);
        try {
            out.write(StringUtil.toUTF8("append"));
        } finally {
            out.close();
        }

        Assert.assertEquals(VALID_CONTENTS + "append", FileSystemUtil.readString(fileSystem, VALID_NAME));
    }

}
