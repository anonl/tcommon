package nl.weeaboo.filesystem;

import java.io.Serializable;
import java.util.Calendar;

@javax.annotation.concurrent.Immutable
@com.google.errorprone.annotations.Immutable
public final class ArchiveFileRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private final FilePath path;
    private final boolean isFolder;

    private final long headerOffset;
    private final long compressedLength;
    private final long uncompressedLength;
    private final byte compression;

    private final int dosDateTime;

    ArchiveFileRecord(String path, long offset, long compressedLength, long uncompressedLength, byte compression,
            int dosDateTime) {

        this.path = FilePath.of(path);
        this.isFolder = path.endsWith("/");

        this.headerOffset = offset;
        this.compressedLength = compressedLength;
        this.uncompressedLength = uncompressedLength;
        this.compression = compression;

        this.dosDateTime = dosDateTime;
    }

    /**
     * Returns the path for this file record within its file archive.
     */
    public FilePath getPath() {
        return path;
    }

    /**
     * Returns {@code true} if this record represents a folder entry.
     */
    public boolean isFolder() {
        return isFolder;
    }

    /**
     * Returns the last modified time for this file record.
     *
     * @return The modified time as UTC milliseconds from the epoch, or {@code 0} if this record doesn't have a valid
     *         modified time associated with it.
     */
    public long getModifiedTime() {
        int time = (dosDateTime) & 0xFFFF;
        int second = 2 * (time & 31);
        int minute = ((time >> 5) & 63);
        int hour = ((time >> 11) & 31);
        int date = (dosDateTime >> 16) & 0xFFFF;
        int day = (date & 31);
        int month = ((date >> 5) & 15);
        int year = 1980 + (date >> 9);

        if (second >= 60 || minute >= 60 || hour >= 24 || day == 0 || month == 0 || month > 12) {
            return 0; // Invalid timestamp
        }

        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(year, month - 1, day, hour, minute, second);
        return calendar.getTimeInMillis();
    }

    /** Byte offset of the file record within the archive file. */
    public long getHeaderOffset() {
        return headerOffset;
    }

    /** The compressed file size in bytes. */
    public long getCompressedLength() {
        return compressedLength;
    }

    /** The uncompressed file size in bytes. */
    public long getUncompressedLength() {
        return uncompressedLength;
    }

    /** Record-specific compression method. */
    public byte getCompression() {
        return compression;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + getPath() + ")";
    }

}
