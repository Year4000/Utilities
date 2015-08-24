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

import lombok.AllArgsConstructor;
import lombok.Getter;

public abstract class AbstractBadgeManager<P> {
    public static final int MAX_RANK = Badges.values().length;

    public abstract Badges findBadge(P player);

    public abstract String getBadge(P player);

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

    @AllArgsConstructor
    public enum Badges {
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
