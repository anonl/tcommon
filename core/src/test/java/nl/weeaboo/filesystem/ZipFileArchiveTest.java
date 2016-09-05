package nl.weeaboo.filesystem;

import java.io.File;
import java.io.IOException;

import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

public class ZipFileArchiveTest extends AbstractFileSystemTest<ZipFileArchive> {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Override
    protected ZipFileArchive createTestFileSystem() throws IOException {
        File zipFile = tempFolder.newFile();
        ResourceUtil.extractResource(getClass(), "/test.zip", zipFile);

        ZipFileArchive arc = new ZipFileArchive();
        arc.open(zipFile);
        return arc;
    }

}
