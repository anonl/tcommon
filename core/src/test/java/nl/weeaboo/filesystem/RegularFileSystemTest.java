package nl.weeaboo.filesystem;

import java.io.IOException;

import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

public final class RegularFileSystemTest extends AbstractWritableFileSystemTest<RegularFileSystem> {

    @Rule
    public final TemporaryFolder tempFolder = new TemporaryFolder();

    @Override
    protected RegularFileSystem createTestFileSystem() throws IOException {
        RegularFileSystem fs = new RegularFileSystem(tempFolder.newFolder());

        FileSystemUtil.writeString(fs, VALID_NAME, VALID_CONTENTS);
        FileSystemUtil.writeString(fs, SUBFOLDER_FILE, SUBFOLDER_FILE_CONTENTS);

        return fs;
    }

}
