package nl.weeaboo.filesystem;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class ZipFileArchiveTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private ZipFileArchive arc;

    @Before
    public void before() {
        arc = new ZipFileArchive();
    }

    @After
    public void after() {
        arc.close();
    }

    @Test
    public void open() throws IOException {
        File zipFile = tempFolder.newFile();
        ResourceUtil.extractResource(getClass(), "/test.zip", zipFile);

        arc.open(zipFile);

        ArchiveFileRecord record = arc.getFile("a.txt");
        Assert.assertNotNull(record);

        // Check file contents
        Assert.assertEquals("a", FileSystemUtil.readString(arc, "a.txt"));
    }

}
