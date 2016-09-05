package nl.weeaboo.filesystem;

import java.io.IOException;

public class InMemoryFileSystemTest extends AbstractWritableFileSystemTest<InMemoryFileSystem> {

    @Override
    protected InMemoryFileSystem createTestFileSystem() throws IOException {
        InMemoryFileSystem fs = new InMemoryFileSystem(false);

        FileSystemUtil.writeString(fs, VALID_NAME, VALID_CONTENTS);
        FileSystemUtil.writeString(fs, SUBFOLDER_FILE, SUBFOLDER_FILE_CONTENTS);

        return fs;
    }

}
