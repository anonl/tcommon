package nl.weeaboo.io;

public class BufferBasedRandomAccessFileTest extends AbstractRandomAccessFileTest {

    @Override
    protected IRandomAccessFile openFile() {
        byte[] buffer = new byte[5];
        return RandomAccessUtil.wrap(buffer, 0, buffer.length);
    }

}
