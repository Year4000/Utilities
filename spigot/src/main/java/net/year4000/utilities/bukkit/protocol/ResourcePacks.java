package net.year4000.utilities.bukkit.protocol;

import com.comphenix.packetwrapper.WrapperPlayClientResourcePackStatus;
import com.comphenix.packetwrapper.WrapperPlayServerResourcePackSend;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.*;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.year4000.utilities.URLBuilder;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.Closeable;
import java.lang.reflect.InvocationTargetException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;

import static com.google.common.base.Preconditions.checkNotNull;

public class ResourcePacks implements Closeable {
    private final ProtocolManager manager;
    private final Plugin plugin;
    private final PackAdapter adapter;
    private static Map<UUID, PackWrapper> packs = Maps.newHashMap();

    /** Create an instance of ResourcePacks */
    public ResourcePacks(ProtocolManager manager, Plugin plugin) {
        this.manager = checkNotNull(manager, "manager");
        this.plugin = checkNotNull(plugin, "plugin");
        this.adapter = new PackAdapter(this);
        this.manager.addPacketListener(adapter);
    }

    /** Sends the resource pack to the player */
    public void sendResourcePack(Player player, String url, BiConsumer<Player, EnumWrappers.ResourcePackStatus> results) {
        checkNotNull(player, "player");
        checkNotNull(url, "url");
        checkNotNull(results, "results");
        String hash;

        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            byte[] bytes = sha.digest(url.getBytes());
            StringBuilder stringBuilder = new StringBuilder();

            for (byte b : bytes) {
                stringBuilder.append(Integer.toHexString(b & 0xFFF));
            }

            stringBuilder.setLength(40);
            hash = stringBuilder.toString();
        }
        catch (NoSuchAlgorithmException e) {
            return;
        }

        url = URLBuilder.fromURL(url).addQuery("hash", hash).build();
        PackWrapper wrapper = new PackWrapper(player, url, hash, results);
        packs.put(player.getUniqueId(), wrapper);
        WrapperPlayServerResourcePackSend pack = new WrapperPlayServerResourcePackSend();
        pack.setUrl(url);
        pack.setHash(hash);

        try {
            manager.sendServerPacket(player, pack.getHandle());
        }
        catch (InvocationTargetException e) {
            // Remove from listener could not send to player
            packs.remove(player.getUniqueId());
        }
    }

    /** Remove listener for the selected player */
    public void removePlayerListener(Player player) {
        checkNotNull(player, "player");
        packs.remove(player.getUniqueId());
    }

    /** Remove the packet listener */
    @Override
    public void close() {
        manager.removePacketListener(adapter);
    }

    @EqualsAndHashCode(callSuper = false)
    public static class PackAdapter extends PacketAdapter {
        private final String code; // Used for equals and hashCode

        public PackAdapter(ResourcePacks packs) {
            super(packs.plugin, ListenerPriority.NORMAL, PacketType.Play.Client.RESOURCE_PACK_STATUS);
            code = packs.plugin.getName();
        }

        @Override
        public void onPacketReceiving(PacketEvent event) {
            if (event.getPacketType() != PacketType.Play.Client.RESOURCE_PACK_STATUS) return;

            WrapperPlayClientResourcePackStatus resource = new WrapperPlayClientResourcePackStatus(event.getPacket());

            Optional<PackWrapper> wrapper = packs.values().stream()
                .filter(packWrapper -> packWrapper.hash.equals(resource.getHash()))
                .findFirst();

            wrapper.ifPresent(packWrapper -> packWrapper.consumer.accept(event.getPlayer(), resource.getResult()));
        }
    }

    /** A class that wraps the BiConsumer of the packet listener */
    @AllArgsConstructor
    @EqualsAndHashCode(of = {"url", "hash"})
    @ToString
    private class PackWrapper {
        private Player player;
        private String url;
        private String hash;
        private BiConsumer<Player, EnumWrappers.ResourcePackStatus> consumer;
    }
}
