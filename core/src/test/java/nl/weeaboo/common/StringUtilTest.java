package nl.weeaboo.common;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

public class StringUtilTest {

    @Test
    public void whitespace() {
        assertWhitespace(true, ""); // Empty counts as 'all whitespace'
        assertWhitespace(true, " \f\t\r\n"); // Various whitespace characters
        assertWhitespace(false, " . "); // One non-whitespace character and the method returns false
    }

    private void assertWhitespace(boolean expectedResult, String str) {
        Assert.assertEquals(expectedResult, StringUtil.isWhitespace(str));
    }

    @Test
    public void formatMemoryAmount() {
        assertMemoryString("0B", 0);
        assertMemoryString("100B", 100);
        assertMemoryString("1023B", 1023); // Memory formatting uses 2^10, not SI units
        assertMemoryString("976.56KiB", 1000000);
        assertMemoryString("953.67MiB", 1000000000);
        assertMemoryString("1.86GiB", 2000000000);
    }

    @Test(expected = IllegalArgumentException.class)
    public void formatNegativeMemoryAmount() {
        StringUtil.formatMemoryAmount(Long.MIN_VALUE);
    }

    private void assertMemoryString(String expected, long bytes) {
        Assert.assertEquals(expected, StringUtil.formatMemoryAmount(bytes));
    }

    @Test
    public void formatTime() {
        assertTimeString("0s", 0, TimeUnit.SECONDS);
        assertTimeString("0ns", 0, TimeUnit.NANOSECONDS);
        assertTimeString("123ns", 123, TimeUnit.NANOSECONDS);
        assertTimeString("123.46μs", 123456, TimeUnit.NANOSECONDS);
        assertTimeString("123.46ms", 123456789, TimeUnit.NANOSECONDS);

        assertTimeString("12s", 12, TimeUnit.SECONDS);
        assertTimeString("2m:03s", 123, TimeUnit.SECONDS);
        assertTimeString("34h:18m", 123456, TimeUnit.SECONDS);
    }

    private void assertTimeString(String expected, long time, TimeUnit unit) {
        Assert.assertEquals(expected, StringUtil.formatTime(time, unit));
    }

    @Test
    public void encodeUtf8() {
        String str = "☃"; // 'snowman'
        byte[] utf8 = StringUtil.toUTF8(str);
        Assert.assertArrayEquals(new byte[] { (byte)0xE2, (byte)0x98, (byte)0x83 }, utf8);
        Assert.assertEquals(str, StringUtil.fromUTF8(utf8));
    }

    @Test
    public void formatRoot() {
        Assert.assertEquals("1000000", StringUtil.formatRoot("%d", 1000000));
    }

}
