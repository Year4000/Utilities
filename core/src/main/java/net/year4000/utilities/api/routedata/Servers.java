package net.year4000.utilities.api.routedata;

import lombok.Data;

import java.util.HashMap;

/**
 * All servers that are hosted on Y4K
 */
@Data
public class Servers extends HashMap<String, SmallServerInfo> {}
