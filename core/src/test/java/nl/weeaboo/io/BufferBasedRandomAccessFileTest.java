package nl.weeaboo.io;

import java.io.IOException;

public class BufferBasedRandomAccessFileTest extends AbstractRandomAccessFileTest {

    @Override
    protected IRandomAccessFile openFile() throws IOException {
        byte[] buffer = new byte[5];
        return RandomAccessUtil.wrap(buffer, 0, buffer.length);
    }

}
