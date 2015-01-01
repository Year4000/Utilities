package net.year4000.utilities;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.logging.Level;
import java.util.logging.Logger;

@Data
@AllArgsConstructor
@SuppressWarnings("unused")
public final class LogUtil {
    protected Logger logger;
    protected Level level;
    protected boolean debug;

    public LogUtil() {
        this(Logger.getLogger(LogUtil.class.getName()));
    }

    public LogUtil(Logger logger) {
        this(logger, Level.INFO, Boolean.valueOf(System.getProperty("debug")));
    }

    public LogUtil(Logger logger, boolean debug) {
        this(logger, Level.INFO, debug);
    }

    public LogUtil(Logger logger, Level level) {
        this(logger, level, Boolean.valueOf(System.getProperty("debug")));
    }

    /** Logs a message to the console */
    public void log(String message, Object... args) {
        logger.log(level, String.format(MessageUtil.stripColors(message), args));
    }

    /** Logs a debug message to the console */
    public void debug(String message, Object... args) {
        if (debug) {
            Level old = level;
            setLevel(Level.WARNING);
            log("DEBUG: " + MessageUtil.stripColors(message), args);
            setLevel(old);
        }
    }

    /** Print out the stack trace */
    public void debug(Exception exception, boolean simple) {
        if (debug) {
            if (exception.getMessage() != null) {
                debug(stripArgs(exception.getMessage()));
            }
            else {
                debug(exception.getClass().getName());
            }

            if (!simple) {
                for (StackTraceElement element : exception.getStackTrace()) {
                    debug(stripArgs(element.toString()));
                }
            }
        }
    }

    /** Print out the stack trace */
    public void log(Exception exception, boolean simple) {
        if (exception.getMessage() != null) {
            log(stripArgs(exception.getMessage()));
        }
        else {
            log(exception.getClass().getName());
        }

        if (!simple) {
            for (StackTraceElement element : exception.getStackTrace()) {
                log(stripArgs(element.toString()));
            }
        }
    }

    /** Strip possible args from breaking things */
    private String stripArgs(String message) {
        return message.replaceAll("%([a-z]|[A-Z])", "");
    }
}
