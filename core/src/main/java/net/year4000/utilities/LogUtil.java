/*
<<<<<<< HEAD
 * Copyright 2015 Year4000.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
=======
 * Copyright 2016 Year4000. All Rights Reserved.
>>>>>>> utils/master
 */

package net.year4000.utilities;

import java.util.logging.Level;
import java.util.logging.Logger;

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

    @java.beans.ConstructorProperties({"logger", "level", "debug"})
    public LogUtil(Logger logger, Level level, boolean debug) {
        this.logger = logger;
        this.level = level;
        this.debug = debug;
    }

    /** Logs a message to the console */
    public synchronized void log(String message, Object... args) {
        logger.log(level, String.format(message, args));
    }

    /** Logs a debug message to the console */
    public synchronized void debug(String message, Object... args) {
        if (debug) {
            logger.warning("DEBUG: " + String.format(message, args));
        }
    }

    /** Print out the stack trace */
    public synchronized void debug(Exception exception, boolean simple) {
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
    public synchronized void log(Exception exception, boolean simple) {
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

    public Logger getLogger() {
        return this.logger;
    }

    public Level getLevel() {
        return this.level;
    }

    public boolean isDebug() {
        return this.debug;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    @Override
    public String toString() {
        return ObjectHelper.toString(this);
    }

    @Override
    public boolean equals(Object other) {
        return ObjectHelper.equals(this, other);
    }

    @Override
    public int hashCode() {
        return ObjectHelper.hashCode(this);
    }
}
