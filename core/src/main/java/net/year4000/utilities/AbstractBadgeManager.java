package net.year4000.utilities;

import lombok.AllArgsConstructor;
import lombok.Getter;

public abstract class AbstractBadgeManager<P> {
    public static final int MAX_RANK = Badges.values().length;

    public abstract Badges findBadge(P player);

    public abstract String getBadge(P player);

    @AllArgsConstructor
    public static enum Badges {
        ALPHA(ChatColor.DARK_AQUA, "α", "alpha", 1),
        THETA(ChatColor.GRAY, "Θ", "theta", 2),
        MU(ChatColor.YELLOW, "μ", "mu", 3),
        PI(ChatColor.AQUA, "π", "pi", 4),
        SIGMA(ChatColor.GOLD, "σ", "sigma", 5),
        PHI(ChatColor.LIGHT_PURPLE, "Φ", "phi", 6),
        DELTA(ChatColor.BLUE, "δ", "delta", 7),
        OMEGA(ChatColor.RED, "Ω", "omega", 8),
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
