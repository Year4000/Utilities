package net.year4000.utilities.bukkit.bossbar;

import net.year4000.utilities.bukkit.Utilities;
import net.year4000.utilities.bukkit.bossbar.nms.FakeDragon;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.HashMap;

/** Allows plugins to safely set a health bar message. */
@SuppressWarnings("unused")
public class BossBar {
    private static HashMap<String, FakeDragon> players = new HashMap<>();
    private static HashMap<String, Integer> timers = new HashMap<>();

    public static void handleTeleport(final Player player, final Location loc) {

        if (!hasBar(player)) {
            return;
        }

        Bukkit.getScheduler().runTaskLater(Utilities.getInst(), () -> {
            // Check if the player still has a dragon after the two ticks! ;)
            if (!hasBar(player)) {
                return;
            }

            FakeDragon oldDragon = getDragon(player, "");

            float health = oldDragon.health;
            String message = oldDragon.name;

            BarUtil.sendPacket(player, getDragon(player, "").getDestroyPacket());

            players.remove(player.getName());

            FakeDragon dragon = addDragon(player, loc, message);
            dragon.health = health;
            //dragon.setVisible(false);

            sendDragon(dragon, player);
        }, 2L);
    }

    /**
     * Set a message for all players.<br>
     * It will remain there until the player logs off or another plugin overrides it.<br>
     * This method will show a full health bar and will cancel any running timers.
     *
     * @param message The message shown.<br>
     *                Due to limitations in Minecraft this message cannot be longer than 64 characters.<br>
     *                It will be cut to that size automatically.
     */
    public static void setMessage(String message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            setMessage(player, message);
        }
    }

    /**
     * Set a message for the given player.<br>
     * It will remain there until the player logs off or another plugin overrides it.<br>
     * This method will show a full health bar and will cancel any running timers.
     *
     * @param player  The player who should see the given message.
     * @param message The message shown to the player.<br>
     *                Due to limitations in Minecraft this message cannot be longer than 64 characters.<br>
     *                It will be cut to that size automatically.
     */
    public static void setMessage(Player player, String message) {
        FakeDragon dragon = getDragon(player, message);

        dragon.name = cleanMessage(message);
        dragon.health = FakeDragon.MAX_HEALTH;

        cancelTimer(player);

        sendDragon(dragon, player);

    }

    /**
     * Set a message for all players.<br>
     * It will remain there for each player until the player logs off or another plugin overrides it.<br>
     * This method will show a health bar using the given percentage value and will cancel any running timers.
     *
     * @param message The message shown to the player.<br>
     *                Due to limitations in Minecraft this message cannot be longer than 64 characters.<br>
     *                It will be cut to that size automatically.
     * @param percent The percentage of the health bar filled.<br>
     *                This value must be between 0F (inclusive) and 100F (inclusive).
     * @throws IllegalArgumentException If the percentage is not within valid bounds.
     */
    public static void setMessage(String message, float percent) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            setMessage(player, message, percent);
        }
    }

    /**
     * Set a message for the given player.<br>
     * It will remain there until the player logs off or another plugin overrides it.<br>
     * This method will show a health bar using the given percentage value and will cancel any running timers.
     *
     * @param player  The player who should see the given message.
     * @param message The message shown to the player.<br>
     *                Due to limitations in Minecraft this message cannot be longer than 64 characters.<br>
     *                It will be cut to that size automatically.
     * @param percent The percentage of the health bar filled.<br>
     *                This value must be between 0F (inclusive) and 100F (inclusive).
     * @throws IllegalArgumentException If the percentage is not within valid bounds.
     */
    public static void setMessage(Player player, String message, float percent) {
        Validate.isTrue(0F <= percent && percent <= 100F, "Percent must be between 0F and 100F, but was: ", percent);

        FakeDragon dragon = getDragon(player, message);

        dragon.name = cleanMessage(message);
        dragon.health = (percent / 100f) * FakeDragon.MAX_HEALTH;

        cancelTimer(player);

        sendDragon(dragon, player);
    }

    /**
     * Set a message for all players.<br>
     * It will remain there for each player until the player logs off or another plugin overrides it.<br>
     * This method will use the health bar as a decreasing timer, all previously started timers will be cancelled.<br>
     * The timer starts with a full bar.<br>
     * The health bar will be removed automatically if it hits zero.
     *
     * @param message The message shown.<br>
     *                Due to limitations in Minecraft this message cannot be longer than 64 characters.<br>
     *                It will be cut to that size automatically.
     * @param seconds The amount of seconds displayed by the timer.<br>
     *                Supports values above 1 (inclusive).
     * @throws IllegalArgumentException If seconds is zero or below.
     */
    public static void setMessage(String message, int seconds) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            setMessage(player, message, seconds);
        }
    }

    /**
     * Set a message for the given player.<br>
     * It will remain there until the player logs off or another plugin overrides it.<br>
     * This method will use the health bar as a decreasing timer, all previously started timers will be cancelled.<br>
     * The timer starts with a full bar.<br>
     * The health bar will be removed automatically if it hits zero.
     *
     * @param player  The player who should see the given timer/message.
     * @param message The message shown to the player.<br>
     *                Due to limitations in Minecraft this message cannot be longer than 64 characters.<br>
     *                It will be cut to that size automatically.
     * @param seconds The amount of seconds displayed by the timer.<br>
     *                Supports values above 1 (inclusive).
     * @throws IllegalArgumentException If seconds is zero or below.
     */
    public static void setMessage(final Player player, String message, int seconds) {
        Validate.isTrue(seconds > 0, "Seconds must be above 1 but was: ", seconds);

        FakeDragon dragon = getDragon(player, message);

        dragon.name = cleanMessage(message);
        dragon.health = FakeDragon.MAX_HEALTH;

        final float dragonHealthMinus = FakeDragon.MAX_HEALTH / seconds;

        cancelTimer(player);

        timers.put(player.getName(), Bukkit.getScheduler().runTaskTimer(Utilities.getInst(), () -> {
            FakeDragon drag = getDragon(player, "");
            drag.health -= dragonHealthMinus;

            if (drag.health <= 1) {
                removeBar(player);
                cancelTimer(player);
            }
            else {
                sendDragon(drag, player);
            }
        }, 20L, 20L).getTaskId());

        sendDragon(dragon, player);
    }

    /**
     * Checks whether the given player has a bar.
     *
     * @param player The player who should be checked.
     * @return True, if the player has a bar, False otherwise.
     */
    public static boolean hasBar(Player player) {
        return players.get(player.getName()) != null;
    }

    /**
     * Removes the bar from the given player.<br>
     * If the player has no bar, this method does nothing.
     *
     * @param player The player whose bar should be removed.
     */
    public static void removeBar(Player player) {
        if (!hasBar(player)) {
            return;
        }

        BarUtil.sendPacket(player, getDragon(player, "").getDestroyPacket());

        players.remove(player.getName());

        cancelTimer(player);
    }

    /**
     * Modifies the health of an existing bar.<br>
     * If the player has no bar, this method does nothing.
     *
     * @param player  The player whose bar should be modified.
     * @param percent The percentage of the health bar filled.<br>
     *                This value must be between 0F and 100F (inclusive).
     */
    public static void setHealth(Player player, float percent) {
        if (!hasBar(player)) {
            return;
        }

        FakeDragon dragon = getDragon(player, "");
        dragon.health = (percent / 100f) * FakeDragon.MAX_HEALTH;

        cancelTimer(player);

        if (percent == 0) {
            removeBar(player);
        }
        else {
            sendDragon(dragon, player);
        }
    }

    /**
     * Get the health of an existing bar.
     *
     * @param player The player whose bar's health should be returned.
     * @return The current absolute health of the bar.<br>
     * If the player has no bar, this method returns -1.
     */
    public static float getHealth(Player player) {
        if (!hasBar(player)) {
            return -1;
        }

        return getDragon(player, "").health;
    }

    /**
     * Get the message of an existing bar.
     *
     * @param player The player whose bar's message should be returned.
     * @return The current message displayed to the player.<br>
     * If the player has no bar, this method returns an empty string.
     */
    public static String getMessage(Player player) {
        if (!hasBar(player)) {
            return "";
        }

        return getDragon(player, "").name;
    }

    private static String cleanMessage(String message) {
        if (message.length() > 64) {
            message = message.substring(0, 63);
        }

        return message;
    }

    private static void cancelTimer(Player player) {
        Integer timerID = timers.remove(player.getName());

        if (timerID != null) {
            Bukkit.getScheduler().cancelTask(timerID);
        }
    }

    private static void sendDragon(FakeDragon dragon, Player player) {
        BarUtil.sendPacket(player, dragon.getMetaPacket(dragon.getWatcher()));
        BarUtil.sendPacket(player, dragon.getTeleportPacket(looking(player)));
    }

    private static FakeDragon getDragon(Player player, String message) {
        if (hasBar(player)) {
            return players.get(player.getName());
        }
        else {
            return addDragon(player, cleanMessage(message));
        }
    }

    private static FakeDragon addDragon(Player player, String message) {
        FakeDragon dragon = BarUtil.newDragon(message, looking(player));

        BarUtil.sendPacket(player, dragon.getSpawnPacket());

        players.put(player.getName(), dragon);

        return dragon;
    }

    private static FakeDragon addDragon(Player player, Location loc, String message) {
        FakeDragon dragon = BarUtil.newDragon(message, looking(player));

        BarUtil.sendPacket(player, dragon.getSpawnPacket());

        players.put(player.getName(), dragon);

        return dragon;
    }

    private static Location looking(Player player) {
        Location loc = player.getLocation();
        float pitch = loc.getPitch();

        if (pitch >= 55) {
            loc.add(0, -300, 0);
        }
        else if (pitch <= -55) {
            loc.add(0, 300, 0);
        }
        else {
            loc = loc.getBlock().getRelative(getDirection(loc), 176).getLocation();
        }

        return loc;
    }

    private static BlockFace getDirection(Location loc) {
        float dir = Math.round(loc.getYaw() / 90);

        if (dir == -4 || dir == 0 || dir == 4) {
            return BlockFace.SOUTH;
        }
        if (dir == -1 || dir == 3) {
            return BlockFace.EAST;
        }
        if (dir == -2 || dir == 2) {
            return BlockFace.NORTH;
        }
        if (dir == -3 || dir == 1) {
            return BlockFace.WEST;
        }

        return null;
    }

    private void quit(Player player) {
        removeBar(player);
    }
}
