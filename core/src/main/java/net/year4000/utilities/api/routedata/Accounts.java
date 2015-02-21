package net.year4000.utilities.api.routedata;

import lombok.Data;

@Data
public class Accounts {
    /**
     * Total amount of players
     */
    final int size;
    /**
     * Amount of players active in the last 24 hours
     */
    final int active;
    /**
     * Amount of new players in the last 24 hours
     */
    final int recent;
    /**
     * Current page
     */
    final int page;
    /**
     * Total amount of pages
     */
    final int total_pages;
    /**
     * Players on the current page
     */
    final SmallAccount[] accounts;
}
