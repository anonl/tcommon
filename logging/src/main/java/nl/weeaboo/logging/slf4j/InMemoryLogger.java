package nl.weeaboo.logging.slf4j;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MarkerIgnoringBase;
import org.slf4j.helpers.MessageFormatter;

import nl.weeaboo.common.Checks;

public class InMemoryLogger extends MarkerIgnoringBase {

    private static final long serialVersionUID = 1L;

    public enum LogLevel {
        // Note: Order is significant -- do not change
        ALL,
        TRACE,
        DEBUG,
        INFO,
        WARN,
        ERROR;
    }

    private LogLevel logLevel = LogLevel.ALL;
    private final Deque<LogEntry> entries = new ArrayDeque<LogEntry>();

    public void log(LogLevel level, String message, Throwable throwable) {
        synchronized (entries) {
            entries.add(new LogEntry(level, message, throwable));
        }
    }

    public List<LogEntry> getEntries() {
        synchronized (entries) {
            return Collections.unmodifiableList(new ArrayList<LogEntry>(entries));
        }
    }

    public List<LogEntry> consumeEntries() {
        synchronized (entries) {
            List<LogEntry> result = new ArrayList<LogEntry>(entries);
            entries.clear();
            return result;
        }
    }

    @Override
    public boolean isTraceEnabled() {
        return logLevel.compareTo(LogLevel.TRACE) <= 0;
    }
    @Override
    public boolean isDebugEnabled() {
        return logLevel.compareTo(LogLevel.DEBUG) <= 0;
    }
    @Override
    public boolean isInfoEnabled() {
        return logLevel.compareTo(LogLevel.INFO) <= 0;
    }
    @Override
    public boolean isWarnEnabled() {
        return logLevel.compareTo(LogLevel.WARN) <= 0;
    }
    @Override
    public boolean isErrorEnabled() {
        return logLevel.compareTo(LogLevel.ERROR) <= 0;
    }

    @Override
    public void trace(String msg) {
        trace(msg, new Object[0]);
    }
    @Override
    public void trace(String format, Object arg) {
        trace(format, new Object[] {arg});
    }
    @Override
    public void trace(String format, Object arg1, Object arg2) {
        trace(format, new Object[] {arg1, arg2});
    }
    @Override
    public void trace(String msg, Throwable t) {
        log(LogLevel.TRACE, msg, t);
    }
    @Override
    public void trace(String format, Object... arguments) {
        FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
        log(LogLevel.TRACE, ft.getMessage(), ft.getThrowable());
    }

    @Override
    public void debug(String msg) {
        debug(msg, new Object[0]);
    }
    @Override
    public void debug(String format, Object arg) {
        debug(format, new Object[] {arg});
    }
    @Override
    public void debug(String format, Object arg1, Object arg2) {
        debug(format, new Object[] {arg1, arg2});
    }
    @Override
    public void debug(String format, Object... arguments) {
        FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
        log(LogLevel.DEBUG, ft.getMessage(), ft.getThrowable());
    }
    @Override
    public void debug(String msg, Throwable t) {
        log(LogLevel.DEBUG, msg, t);
    }

    @Override
    public void info(String msg) {
        info(msg, new Object[0]);
    }
    @Override
    public void info(String format, Object arg) {
        info(format, new Object[] {arg});
    }
    @Override
    public void info(String format, Object arg1, Object arg2) {
        info(format, new Object[]{arg1, arg2});
    }
    @Override
    public void info(String format, Object... arguments) {
        FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
        log(LogLevel.INFO, ft.getMessage(), ft.getThrowable());
    }
    @Override
    public void info(String msg, Throwable t) {
        log(LogLevel.INFO, msg, t);
    }

    @Override
    public void warn(String msg) {
        warn(msg, new Object[0]);
    }
    @Override
    public void warn(String format, Object arg) {
        warn(format, new Object[]{arg});
    }
    @Override
    public void warn(String format, Object arg1, Object arg2) {
        warn(format, new Object[]{arg1, arg2});
    }
    @Override
    public void warn(String format, Object... arguments) {
        FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
        log(LogLevel.WARN, ft.getMessage(), ft.getThrowable());
    }
    @Override
    public void warn(String msg, Throwable t) {
        log(LogLevel.WARN, msg, t);
    }

    @Override
    public void error(String msg) {
        error(msg, new Object[0]);
    }
    @Override
    public void error(String format, Object arg) {
        error(format, new Object[] {arg});
    }
    @Override
    public void error(String format, Object arg1, Object arg2) {
        error(format, new Object[] {arg1, arg2});
    }
    @Override
    public void error(String format, Object... arguments) {
        FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
        log(LogLevel.ERROR, ft.getMessage(), ft.getThrowable());
    }
    @Override
    public void error(String msg, Throwable t) {
        log(LogLevel.ERROR, msg, t);
    }

    public static class LogEntry {

        private final LogLevel level;
        private final String message;
        private final Throwable exception;

        public LogEntry(LogLevel level, String message) {
            this(level, message, null);
        }
        public LogEntry(LogLevel level, String message, Throwable exception) {
            this.level = Checks.checkNotNull(level);
            this.message = Checks.checkNotNull(message);
            this.exception = exception;
        }

        public LogLevel getLevel() {
            return level;
        }

        public String getMessage() {
            return message;
        }

        /** The exception associated with the log entry, or {@code null} if not available. */
        public Throwable getException() {
            return exception;
        }

    }

}
