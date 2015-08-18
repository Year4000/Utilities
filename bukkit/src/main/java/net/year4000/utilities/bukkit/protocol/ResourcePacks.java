package net.year4000.utilities.bukkit.protocol;

import com.comphenix.packetwrapper.WrapperPlayServerResourcePackSend;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.*;
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
    private Map<UUID, PackWrapper> packs = Maps.newConcurrentMap();

    /** Create an instance of ResourePacks */
    public ResourcePacks(ProtocolManager manager, Plugin plugin) {
        this.manager = checkNotNull(manager, "manager");
        this.plugin = checkNotNull(plugin, "plugin");
        this.adapter = new PackAdapter(this);
        this.manager.addPacketListener(adapter);
    }

    /** Sends the resource pack to the player */
    public void sendResourcePack(Player player, String url, BiConsumer<Player, Status> results) {
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

        URLBuilder builder = URLBuilder.fromURL(url).addQuery("hash", hash);
        PackWrapper wrapper = new PackWrapper(player, builder.build(), hash, results);
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

    /** Remove the packet listener */
    @Override
    public void close() {
        manager.removePacketListener(adapter);
    }

    @EqualsAndHashCode(callSuper = false)
    public class PackAdapter extends PacketAdapter {
        private final String code; // Used for equals and hashCode

        public PackAdapter(ResourcePacks packs) {
            super(packs.plugin, ListenerPriority.NORMAL, PacketType.Play.Client.RESOURCE_PACK_STATUS);
            code = packs.plugin.getName();
        }

        @Override
        public void onPacketReceiving(PacketEvent event) {
            if (event.getPacketType() != PacketType.Play.Client.RESOURCE_PACK_STATUS) return;

            String hash = event.getPacket().getStrings().read(0);
            int result = event.getPacket().getIntegers().read(0);

            Optional<PackWrapper> wrapper = packs.values().stream()
                .filter(packWrapper -> packWrapper.hash.equals(hash))
                .findFirst();

            wrapper.ifPresent(packWrapper -> {
                Player player = packWrapper.player;
                Status status = Status.getById(result);
                packWrapper.consumer.accept(player, status);
                packs.remove(player.getUniqueId());
            });
        }
    }

    /** The status of the sent resource pack */
    @AllArgsConstructor
    public enum Status {
        LOADED(0),
        DECLINED(1),
        FAILED(2),
        ACCEPTED(3),
        ;

        private int status;

        /** Get the status by the id */
        public static Status getById(int id) {
            for (Status status : Status.values()) {
                if (status.status == id) {
                    return status;
                }
            }
            throw new IllegalArgumentException();
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
        private BiConsumer<Player, Status> consumer;
    }
}
