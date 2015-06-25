/*
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
