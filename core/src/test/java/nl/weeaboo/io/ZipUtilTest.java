package nl.weeaboo.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipOutputStream;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import nl.weeaboo.filesystem.FilePath;
import nl.weeaboo.filesystem.FileSystemUtil;
import nl.weeaboo.filesystem.ZipFileArchive;
import nl.weeaboo.io.ZipUtil.Compression;

public class ZipUtilTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private File zipFile;
    private ZipOutputStream zout;

    @Before
    public void before() throws IOException {
        zipFile = tempFolder.newFile();
        zout = new ZipOutputStream(new FileOutputStream(zipFile));
    }

    @After
    public void after() {
        if (zout != null) {
            try {
                zout.close();
            } catch (IOException e) {
                System.err.println("Error closing zip output " + e);
            }
        }
        if (zipFile != null) {
            zipFile.delete();
        }
    }

    @Test
    public void writeHugeEntryStored() throws IOException {
        long size = 50 << 20;

        writeHugeEntry(size, Compression.NONE);

        // Check that the entire input was written to the ZIP file
        Assert.assertTrue("ZIP file size: " + zipFile.length(), zipFile.length() > size);
    }

    @Test
    public void writeHugeEntryDeflated() throws IOException {
        long size = 50 << 20;

        writeHugeEntry(size, Compression.DEFLATE);

        // Since we're using compression, expect the resulting ZIP file to be much smaller than the input
        Assert.assertTrue("ZIP file size: " + zipFile.length(), zipFile.length() < size / 10);
    }

    /** Add File to ZIP archive. */
    @Test
    public void addFile() throws IOException {
        File file = tempFolder.newFile("file");
        File folder = tempFolder.newFolder("folder");

        ZipUtil.add(zout, file.getName(), file, Compression.DEFLATE);
        ZipUtil.add(zout, folder.getName(), folder, Compression.DEFLATE);

        ZipFileArchive arc = openZipFile();
        try {
            Assert.assertEquals(true, arc.getFileExists(FilePath.of("file")));
            Assert.assertEquals(true, arc.getFileExists(FilePath.of("folder/")));
        } finally {
            arc.close();
        }
    }

    @Test
    public void addFromByteArray() throws IOException {
        byte[] bytes = {9, 8, 7, 6};
        ZipUtil.writeFileEntry(zout, "bytes", bytes, 1, 2, Compression.NONE);

        ZipFileArchive arc = openZipFile();
        try {
            Assert.assertArrayEquals(new byte[] {8, 7},
                    FileSystemUtil.readBytes(arc, FilePath.of("bytes")));
        } finally {
            arc.close();
        }
    }

    private ZipFileArchive openZipFile() throws IOException {
        zout.close();

        ZipFileArchive arc = new ZipFileArchive();
        arc.open(zipFile);
        return arc;
    }

    private void writeHugeEntry(long size, Compression compression) throws IOException {
        InputStream in = newTestInput(size);

        ZipUtil.writeFileEntry(zout, "huge", in, size, compression);
    }

    private InputStream newTestInput(final long size) {
        return new InputStream() {

            long read;

            @Override
            public int read() throws IOException {
                if (read >= size) {
                    return -1;
                }
                read++;
                return 1;
            }
        };
    }

}
