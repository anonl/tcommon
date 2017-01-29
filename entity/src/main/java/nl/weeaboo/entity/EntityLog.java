package nl.weeaboo.entity;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.LoggingPermission;

public final class EntityLog {

    private static final Logger INSTANCE;

    static {
        Logger log = null;

        try {
            SecurityManager sm = System.getSecurityManager();
            if (sm != null) {
                sm.checkPermission(new LoggingPermission("control", ""));
            }

            log = Logger.getLogger("nl.weeaboo.game.entity");
            log.setLevel(Level.ALL);
        } catch (SecurityException se) {
            //Ignore
        } catch (Exception e) {
            System.err.println(e);
        }

        INSTANCE = (log != null ? log : Logger.getAnonymousLogger());
    }

    /**
     * Returns the global logger instance.
     */
    public static Logger getInstance() {
        return INSTANCE;
    }

    /** Log a message at VERBOSE level. */
    public static void verbose(String message) {
        verbose(message, null);
    }

    /** Log a message at VERBOSE level. */
    public static void verbose(String message, Throwable t) {
        INSTANCE.logp(Level.CONFIG, null, null, message, t);
    }

    /** Log a message at DEBUG level. */
    public static void debug(String message) {
        debug(message, null);
    }

    /** Log a message at DEBUG level. */
    public static void debug(String message, Throwable t) {
        INSTANCE.logp(Level.INFO, null, null, message, t);
    }

    /** Log a message at WARNING level. */
    public static void warn(String message) {
        warn(message, null);
    }

    /** Log a message at WARNING level. */
    public static void warn(String message, Throwable t) {
        INSTANCE.logp(Level.WARNING, null, null, message, t);
    }

    /** Log a message at ERROR level. */
    public static void error(String message) {
        error(message, null);
    }

    /** Log a message at ERROR level. */
    public static void error(String message, Throwable t) {
        INSTANCE.logp(Level.SEVERE, null, null, message, t);
    }

}
