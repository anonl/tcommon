package nl.weeaboo.filesystem;

import java.io.IOException;

public class InMemoryFileSystemTest extends AbstractFileSystemTest {

    @Override
    protected InMemoryFileSystem createTestFileSystem() throws IOException {
        InMemoryFileSystem fs = new InMemoryFileSystem(false);

        FileSystemUtil.writeString(fs, VALID_NAME, VALID_CONTENTS);

        return fs;
    }

}
