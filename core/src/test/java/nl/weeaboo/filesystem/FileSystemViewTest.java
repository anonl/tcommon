package nl.weeaboo.filesystem;

import java.io.IOException;

public final class FileSystemViewTest extends AbstractFileSystemTest<FileSystemView> {

    @Override
    protected FileSystemView createTestFileSystem() throws IOException {
        FilePath base = FilePath.of("base");

        InMemoryFileSystem inner = new InMemoryFileSystem(false);
        FileSystemUtil.writeString(inner, base.resolve(VALID_NAME), VALID_CONTENTS);
        FileSystemUtil.writeString(inner, base.resolve(SUBFOLDER_FILE), SUBFOLDER_FILE_CONTENTS);

        return new FileSystemView(inner, base);
    }

}
