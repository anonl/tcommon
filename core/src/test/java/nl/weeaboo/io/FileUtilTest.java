package nl.weeaboo.io;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public final class FileUtilTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
    public void testReadWriteUtf8() throws IOException {
        File file = tempFolder.newFile();

        String write = "☃\r\nabc"; // 'snowman', windows line ending, abc
        FileUtil.writeUtf8(file, write);

        // Check that the same data can be read back
        String read = FileUtil.readUtf8(file);
        Assert.assertEquals(write, read);
    }

}
