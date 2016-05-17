/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities;

import java.util.concurrent.TimeUnit;

public final class TimeUtil {
    private final String SEPARATOR = ":";
    private TimeUnit unit;
    private long amount;
    private long numberSeconds;
    private long numberHours;
    private long numberMinutes;

    public TimeUtil(long amount, TimeUnit unit) {
        this.unit = unit;
        this.amount = amount;
        convertUnits();
    }

    /** returns rawOutput with default separator ':' */
    public String rawOutput() {
        return rawOutput(SEPARATOR);
    }

    /** Outputs time in format hh:mm:ss with defined separator */
    public String rawOutput(String separator) {
        StringBuilder builder = new StringBuilder();

        builder = numberHours >= 10 ? builder.append(numberHours) : builder.append("0").append(numberHours);
        builder.append(separator);

        builder = numberMinutes >= 10 ? builder.append(numberMinutes) : builder.append("0").append(numberMinutes);
        builder.append(separator);

        builder = numberSeconds >= 10 ? builder.append(numberSeconds) : builder.append("0").append(numberSeconds);

        return builder.toString();
    }

    /** returns prettyOutput with default separator ':' */
    public String prettyOutput() {
        return prettyOutput(SEPARATOR);
    }

    /** Outputs time in clean format, omits empty hours and leading zero */
    public String prettyOutput(String separator) {
        StringBuilder builder = new StringBuilder();

        builder = numberHours < 1 ? builder : builder.append(numberHours).append(separator);
        builder = numberMinutes < 10 && numberHours > 0 ? builder.append("0").append(numberMinutes) : builder.append(numberMinutes);
        builder.append(separator);
        builder = numberSeconds >= 10 ? builder.append(numberSeconds) : builder.append("0").append(numberSeconds);

        return builder.toString();
    }

    private void convertUnits() {
        TimeUnit seconds = TimeUnit.SECONDS;
        TimeUnit hours = TimeUnit.HOURS;
        TimeUnit minutes = TimeUnit.MINUTES;

        numberHours = hours.convert(amount, unit);
        amount -= unit.convert(numberHours, hours);
        numberMinutes = minutes.convert(amount, unit);
        amount -= unit.convert(numberMinutes, minutes);
        numberSeconds = seconds.convert(amount, unit);
    }
}
