package net.year4000.utilities.bukkit.protocol;

import com.comphenix.packetwrapper.WrapperPlayClientResourcePackStatus;
import com.comphenix.packetwrapper.WrapperPlayServerResourcePackSend;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.google.common.collect.Maps;
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
    private static Map<UUID, PackWrapper> packs = Maps.newHashMap();
    private final ProtocolManager manager;
    private final Plugin plugin;
    private final PackAdapter adapter;

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

    public static class PackAdapter extends PacketAdapter {
        private final String code; // Used for equals and hashCode

        public PackAdapter(ResourcePacks packs) {
            super(packs.plugin, ListenerPriority.NORMAL, PacketType.Play.Client.RESOURCE_PACK_STATUS);
            code = packs.plugin.getName();
        }

        @Override
        public void onPacketReceiving(PacketEvent event) {
            if (event.getPacketType() != PacketType.Play.Client.RESOURCE_PACK_STATUS) {
                return;
            }

            WrapperPlayClientResourcePackStatus resource = new WrapperPlayClientResourcePackStatus(event.getPacket());

            Optional<PackWrapper> wrapper = packs.values().stream()
                .filter(packWrapper -> packWrapper.hash.equals(resource.getHash()))
                .findFirst();

            wrapper.ifPresent(packWrapper -> packWrapper.consumer.accept(event.getPlayer(), resource.getResult()));
        }

        public boolean equals(Object o) {
            if (o == this) return true;
            if (!(o instanceof PackAdapter)) return false;
            final PackAdapter other = (PackAdapter) o;
            if (!other.canEqual((Object) this)) return false;
            final Object this$code = this.code;
            final Object other$code = other.code;
            if (this$code == null ? other$code != null : !this$code.equals(other$code)) return false;
            return true;
        }

        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            final Object $code = this.code;
            result = result * PRIME + ($code == null ? 0 : $code.hashCode());
            return result;
        }

        protected boolean canEqual(Object other) {
            return other instanceof PackAdapter;
        }
    }

    /** A class that wraps the BiConsumer of the packet listener */
    private class PackWrapper {
        private Player player;
        private String url;
        private String hash;
        private BiConsumer<Player, EnumWrappers.ResourcePackStatus> consumer;

        @java.beans.ConstructorProperties({"player", "url", "hash", "consumer"})
        public PackWrapper(Player player, String url, String hash, BiConsumer<Player, EnumWrappers.ResourcePackStatus> consumer) {
            this.player = player;
            this.url = url;
            this.hash = hash;
            this.consumer = consumer;
        }

        public boolean equals(Object o) {
            if (o == this) return true;
            if (!(o instanceof PackWrapper)) return false;
            final PackWrapper other = (PackWrapper) o;
            if (!other.canEqual((Object) this)) return false;
            final Object this$url = this.url;
            final Object other$url = other.url;
            if (this$url == null ? other$url != null : !this$url.equals(other$url)) return false;
            final Object this$hash = this.hash;
            final Object other$hash = other.hash;
            if (this$hash == null ? other$hash != null : !this$hash.equals(other$hash)) return false;
            return true;
        }

        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            final Object $url = this.url;
            result = result * PRIME + ($url == null ? 0 : $url.hashCode());
            final Object $hash = this.hash;
            result = result * PRIME + ($hash == null ? 0 : $hash.hashCode());
            return result;
        }

        protected boolean canEqual(Object other) {
            return other instanceof PackWrapper;
        }

        public String toString() {
            return "net.year4000.utilities.bukkit.protocol.ResourcePacks.PackWrapper(player=" + this.player + ", url=" + this.url + ", hash=" + this.hash + ", consumer=" + this.consumer + ")";
        }
    }
}
