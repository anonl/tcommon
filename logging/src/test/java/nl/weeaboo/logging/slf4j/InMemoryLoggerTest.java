package nl.weeaboo.logging.slf4j;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import nl.weeaboo.logging.slf4j.InMemoryLogger.LogEntry;
import nl.weeaboo.logging.slf4j.InMemoryLogger.LogLevel;

public class InMemoryLoggerTest {

    private InMemoryLogger logger;

    @Before
    public void before() {
        logger = new InMemoryLogger();
    }

    /** Log at various levels, check that the generated events match the correct log levels */
    @Test
    public void testLevels() {
        logger.trace("trace");
        logger.debug("debug");
        logger.info("info");
        logger.warn("warn");
        logger.error("error");

        List<LogEntry> entries = logger.consumeEntries();
        assertEntry(entries.get(0), LogLevel.TRACE, "trace");
        assertEntry(entries.get(1), LogLevel.DEBUG, "debug");
        assertEntry(entries.get(2), LogLevel.INFO, "info");
        assertEntry(entries.get(3), LogLevel.WARN, "warn");
        assertEntry(entries.get(4), LogLevel.ERROR, "error");
    }

    /** Check that format strings are handled properly */
    @Test
    public void formatStrings() {
        RuntimeException ex = new RuntimeException("test");
        logger.warn("a{}{}", "bc", 123, ex);

        List<LogEntry> entries = logger.consumeEntries();
        assertEntry(entries.get(0), LogLevel.WARN, "abc123", ex);
    }

    private void assertEntry(LogEntry entry, LogLevel level, String message) {
        assertEntry(entry, level, message, null);
    }
    private void assertEntry(LogEntry entry, LogLevel level, String message, Throwable exception) {
        Assert.assertEquals(level, entry.getLevel());
        Assert.assertEquals(message, entry.getMessage());
        Assert.assertEquals(exception, entry.getException());
    }

}
