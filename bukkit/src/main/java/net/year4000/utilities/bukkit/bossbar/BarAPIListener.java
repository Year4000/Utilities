package net.year4000.utilities.bukkit.bossbar;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

@SuppressWarnings("unused")
public class BarAPIListener implements Listener {
    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        BossBar.handleTeleport(event.getPlayer(), event.getTo());
    }

    @EventHandler
    public void onDeath(PlayerRespawnEvent event) {
        BossBar.handleTeleport(event.getPlayer(), event.getRespawnLocation());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        BossBar.removeBar(event.getPlayer());
    }
}
