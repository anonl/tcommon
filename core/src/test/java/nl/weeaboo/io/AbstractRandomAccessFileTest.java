package nl.weeaboo.io;

import java.io.IOException;
import java.io.InputStream;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public abstract class AbstractRandomAccessFileTest {

    protected IRandomAccessFile rfile;

    @Before
    public void beforeTest() throws IOException {
        rfile = openFile();
    }

    @After
    public void afterTest() throws IOException {
        rfile.close();
    }

    protected abstract IRandomAccessFile openFile() throws IOException;

    /** Test basic behavior */
    @Test
    public void writeSeekRead() throws IOException {
        for (int n = 0; n < 5; n++) {
            rfile.write(n);
        }

        Assert.assertEquals(5, rfile.pos());
        rfile.seek(0);
        Assert.assertEquals(0, rfile.pos());

        for (int n = 0; n < 5; n++) {
            Assert.assertEquals(n, rfile.read());
        }
    }

    @Test
    public void randomAccessInputStream() throws IOException {
        for (int n = 0; n < 5; n++) {
            rfile.write(n);
        }

        InputStream in = rfile.getInputStream(1, 3);
        rfile.seek(4); // Seeking in the underlying random access file doesn't affect the input stream
        try {
            Assert.assertEquals(true, in.markSupported());
            in.mark(5);

            byte[] read = StreamUtil.readBytes(in);
            Assert.assertArrayEquals(new byte[] { 1, 2, 3 }, read);

            in.reset();
            in.skip(2); // Skip 1, 2
            Assert.assertEquals(3, in.read());
            Assert.assertEquals(-1, in.read()); // EOF
        } finally {
            in.close();
        }
    }

}
