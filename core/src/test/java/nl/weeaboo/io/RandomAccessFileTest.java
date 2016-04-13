package nl.weeaboo.io;

import java.io.IOException;
import java.io.RandomAccessFile;

import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

public class RandomAccessFileTest extends AbstractRandomAccessFileTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Override
    protected IRandomAccessFile openFile() throws IOException {
        return RandomAccessUtil.wrap(new RandomAccessFile(tempFolder.newFile(), "rw"));
    }

}
