package net.year4000.utilities.net.router;

import net.year4000.utilities.Conditions;

/** Called when there was an error when handling a Route Handle */
public class RouteHandleException extends RuntimeException {
    public RouteHandleException(Throwable throwable) {
        super(Conditions.nonNull(throwable, "throwable"));
    }
}
