package net.year4000.utilities;


import com.google.common.collect.ImmutableList;
import net.year4000.utilities.value.Value;

import java.io.PrintStream;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/** Simple way to write errors to a print stream */
public final class ErrorReporter {
    private static int id;
    private final Throwable throwable;
    private final boolean hide;
    private final String[] args;

    private ErrorReporter(boolean hide, Throwable throwable, String... args) {
        this.throwable = Conditions.nonNull(throwable, "throwable");
        this.args = Conditions.nonNull(args, "args");
        this.hide = hide;
        id++;
    }

    /** Is the stack trace been hidden from output */
    public boolean isStackTraceHidden() {
        return !hide;
    }

    /** Get the throwable object this is tide to this report */
    public Throwable getThrowable() {
        return throwable;
    }

    /** Report it to the print stream */
    public synchronized RuntimeException report(PrintStream out) {
        String clean = "\nError Report #" + Integer.toHexString(id) + "\n";
        for (String arg : args) {
            clean += " -\t" + arg + "\n";
        }
        if (!hide) { // Hide the stacktrace
            clean +=  "\nMsg:\t" + throwable.getMessage() + "\n";
            clean += "Trace:\t" + Stream.of(throwable.getStackTrace()).map(String::valueOf).collect(Collectors.joining("\n -\t"));
        }
        out.println(clean.substring(0, clean.length() - 1)); // Strip off last endline
        throw new RuntimeException(getThrowable());
    }

    /** Set the current threads uncaught exception handler */
    public static void setUncaughtExceptionHandler(Thread thread) {
        thread.setUncaughtExceptionHandler(new ErrorReporterHandler());
    }

    /** Set the current threads uncaught exception handler */
    public static void setUncaughtExceptionHandler() {
        setUncaughtExceptionHandler(Thread.currentThread());
    }

    /** Create a new builder for this error reporter */
    public static Builder builder() {
        return new Builder(null);
    }

    /** Create a new builder for this error reporter */
    public static Builder builder(Throwable throwable) {
        return new Builder(throwable);
    }

    public static class Builder implements net.year4000.utilities.Builder<ErrorReporter> {
        private final ImmutableList.Builder<String> lines = new ImmutableList.Builder<>();
        private Value<Throwable> throwable;
        private boolean hide = false;

        public Builder(Throwable throwable) {
            this.throwable = Value.of(throwable);
            hide = throwable == null;
        }

        /** Hide the stack trace */
        public Builder hideStackTrace() {
            hide = true;
            return this;
        }

        /** Get the throwable for the error reporter */
        public Builder throwable(Throwable throwable) {
            this.throwable = Value.of(Conditions.nonNull(throwable, "throwable"));
            return this;
        }

        /** Add a object with a message to the reporter*/
        public Builder add(String message, Object object) {
            Conditions.nonNullOrEmpty(message, "message");
            lines.add(message + ((object == null) ? "null" : String.valueOf(object)));
            return this;
        }

        /** Add a bunch of object separated by a comma */
        public Builder add(String message, Object... object) {
            Conditions.nonNullOrEmpty(message, "message");
            String joined = (object == null) ? "null" : Stream.of(object).map(String::valueOf).collect(Collectors.joining(", "));
            lines.add(message + joined);
            return this;
        }

        @Override
        public ErrorReporter build() {
            Throwable throwable = this.throwable.getOrElse(new Throwable());
            ImmutableList<String> lines = this.lines.build();
            return new ErrorReporter(hide, throwable, lines.toArray(new String[lines.size()]));
        }

        /** Build the report and print it out to the print stream */
        public RuntimeException buildAndReport(PrintStream out) {
            return build().report(out);
        }
    }

    /** Catch errors and wrap them around this error reporter */
    public static class ErrorReporterHandler implements Thread.UncaughtExceptionHandler {
        @Override
        public void uncaughtException(Thread thread, Throwable throwable) {
            ErrorReporter.builder(throwable).buildAndReport(System.err);
        }
    }
}
