package nl.weeaboo.filesystem;

import java.io.IOException;
import java.util.Arrays;

public class MultiFileSystemTest extends AbstractFileSystemTest<MultiFileSystem> {

    @Override
    protected MultiFileSystem createTestFileSystem() throws IOException {
        InMemoryFileSystem readonly = new InMemoryFileSystem(true);

        InMemoryFileSystem writable = new InMemoryFileSystem(false);
        FileSystemUtil.writeString(writable, VALID_NAME, VALID_CONTENTS);
        FileSystemUtil.writeString(writable, SUBFOLDER_FILE, SUBFOLDER_FILE_CONTENTS);

        return new MultiFileSystem(Arrays.<IFileSystem>asList(readonly, writable));
    }

}
