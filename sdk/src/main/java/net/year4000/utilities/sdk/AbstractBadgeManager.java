/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.sdk;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.year4000.utilities.mc.ChatColor;
import net.year4000.utilities.mc.MessageUtil;

public abstract class AbstractBadgeManager<P> {
    public static final int MAX_RANK = Badges.values().length;

    /** Is the specific badge vip rank */
    public static boolean isVIP(Badges badge) {
        switch (badge) {
            case THETA:
            case MU:
            case PI:
            case SIGMA:
                return true;
            default:
                return false;
        }
    }

    /** Is the specific badge staff rank */
    public static boolean isStaff(Badges badge) {
        switch (badge) {
            case TAU:
            case DELTA:
            case OMEGA:
                return true;
            default:
                return false;
        }
    }

    public abstract Badges findBadge(P player);

    public abstract String getBadge(P player);

    @AllArgsConstructor
    public enum Badges {
        MISSING(ChatColor.DARK_GRAY, "Γ", "missing", 0),
        ALPHA(ChatColor.DARK_AQUA, "α", "alpha", 1),
        THETA(ChatColor.GRAY, "Θ", "theta", 2),
        MU(ChatColor.YELLOW, "μ", "mu", 3),
        PI(ChatColor.AQUA, "π", "pi", 4),
        SIGMA(ChatColor.GOLD, "σ", "sigma", 5),
        PHI(ChatColor.LIGHT_PURPLE, "Φ", "phi", 6),
        TAU(ChatColor.GREEN, "τ", "tau", 7),
        DELTA(ChatColor.BLUE, "δ", "delta", 8),
        OMEGA(ChatColor.RED, "Ω", "omega", 9),
        /* DIFF CHECKER */;

        @Getter
        private ChatColor color;
        @Getter
        private String badge, permission;
        @Getter
        private int rank;

        @Override
        public String toString() {
            return MessageUtil.replaceColors(color + badge);
        }
    }
}