package net.year4000.utilities.sponge.protocol;

import static net.year4000.utilities.sponge.protocol.PacketTypes.Binding.INBOUND;
import static net.year4000.utilities.sponge.protocol.PacketTypes.Binding.OUTBOUND;
import static net.year4000.utilities.sponge.protocol.PacketTypes.State.PLAY;

import com.google.common.collect.Maps;
import net.year4000.utilities.Conditions;
import net.year4000.utilities.sponge.protocol.proxy.ProxyEnumConnectionState;
import net.year4000.utilities.utils.UtilityConstructError;
import net.year4000.utilities.value.Value;

import java.util.concurrent.ConcurrentMap;


/** A enumeration of all the type of packets Minecraft 1.9.2 can handle */
public final class PacketTypes {
    private static final ConcurrentMap<PacketType, Class<?>> cache = Maps.newConcurrentMap();
    private static final ConcurrentMap<Class<?>, PacketType> cacheInverse = Maps.newConcurrentMap();

    private PacketTypes() {
        UtilityConstructError.raise();
    }

    /** Create the packet type */
    public static PacketType register(State state, Binding binding, int id) {
        PacketType type = new PacketType(id, state.ordinal() + 1, binding.ordinal() + 1);
        fromType(type); // tries to register the cache
        return type;
    }

    /** Try and get the class from the type */
    public static Value<Class<?>> fromType(PacketType type) {
        Conditions.nonNull(type, "type");
        Class<?> clazz = cache.computeIfAbsent(type, a -> {
            try {
                ProxyEnumConnectionState connectionState = ProxyEnumConnectionState.get();
                ProxyEnumConnectionState instance = connectionState.value(type.state());
                return instance.packet(type);
            } catch (Exception error) {
                return null;
            }
        });
        if (clazz == null) {
            return Value.empty();
        }
        cache.putIfAbsent(type, clazz);
        cacheInverse.putIfAbsent(clazz, type);
        //System.out.println(type + ": " + cache.get(type));
        //System.out.println(clazz + ": " + cacheInverse.get(clazz));
        return Value.of(clazz);
    }

    /** Get the PacketType from the class */
    public static Value<PacketType> fromClass(Class<?> clazz) {
        Conditions.nonNull(clazz, "clazz");
        return Value.of(cacheInverse.get(clazz));
    }

    /** Which way the packets are bounded to */
    public enum Binding {OUTBOUND, INBOUND}

    /** The type of packet they are */
    public enum State {PLAY, STATUS, LOGIN}

    // Play Packets
    // Client
    public static final PacketType PLAY_CLIENT_SPAWN_OBJECT = register(PLAY, INBOUND, 0x00);
    public static final PacketType PLAY_CLIENT_SPAWN_EXPERIENCE_ORB = register(PLAY, INBOUND, 0x01);
    public static final PacketType PLAY_CLIENT_SPAWN_GLOBAL_ENTITY = register(PLAY, INBOUND, 0x02);
    public static final PacketType PLAY_CLIENT_SPAWN_MOB = register(PLAY, INBOUND, 0x03);
    public static final PacketType PLAY_CLIENT_SPAWN_PAINTING = register(PLAY, INBOUND, 0x04);
    public static final PacketType PLAY_CLIENT_SPAWN_PLAYER = register(PLAY, INBOUND, 0x05);
    public static final PacketType PLAY_CLIENT_ANIMATION = register(PLAY, INBOUND, 0x06);
    public static final PacketType PLAY_CLIENT_STATISTICS = register(PLAY, INBOUND, 0x07);
    public static final PacketType PLAY_CLIENT_BLOCK_BREAK_ANIMATION = register(PLAY, INBOUND, 0x25);
    public static final PacketType PLAY_CLIENT_UPDATE_BLOCK_ENTITY = register(PLAY, INBOUND, 0x09);
    public static final PacketType PLAY_CLIENT_BLOCK_ACTION = register(PLAY, INBOUND, 0x0A);
    public static final PacketType PLAY_CLIENT_BLOCK_CHANGE = register(PLAY, INBOUND, 0x0B);
    public static final PacketType PLAY_CLIENT_BOSS_BAR = register(PLAY, INBOUND, 0x0C);
    public static final PacketType PLAY_CLIENT_SERVER_DIFFICULTY = register(PLAY, INBOUND, 0x0D);
    public static final PacketType PLAY_CLIENT_TAB_COMPLETE = register(PLAY, INBOUND, 0x0E);
    public static final PacketType PLAY_CLIENT_CHAT_MESSAGE = register(PLAY, INBOUND, 0x0F);
    public static final PacketType PLAY_CLIENT_MULTI_BLOCK_CHANGE = register(PLAY, INBOUND, 0x10);
    public static final PacketType PLAY_CLIENT_CONFIRM_TRANSACTION = register(PLAY, INBOUND, 0x11);
    public static final PacketType PLAY_CLIENT_CLOSE_WINDOW = register(PLAY, INBOUND, 0x12);
    public static final PacketType PLAY_CLIENT_OPEN_WINDOW = register(PLAY, INBOUND, 0x13);
    public static final PacketType PLAY_CLIENT_WINDOW_ITEMS = register(PLAY, INBOUND, 0x14);
    public static final PacketType PLAY_CLIENT_WINDOW_PROPERTY = register(PLAY, INBOUND, 0x15);
    public static final PacketType PLAY_CLIENT_SET_SLOT = register(PLAY, INBOUND, 0x16);
    public static final PacketType PLAY_CLIENT_SET_COOLDOWN = register(PLAY, INBOUND, 0x17);
    public static final PacketType PLAY_CLIENT_PLUGIN_MESSAGE = register(PLAY, INBOUND, 0x18);
    public static final PacketType PLAY_CLIENT_NAMED_SOUND_EFFECT = register(PLAY, INBOUND, 0x19);
    public static final PacketType PLAY_CLIENT_DISCONNECT = register(PLAY, INBOUND, 0x1A);
    public static final PacketType PLAY_CLIENT_ENTITY_STATUS = register(PLAY, INBOUND, 0x1B);
    public static final PacketType PLAY_CLIENT_EXPLOSION = register(PLAY, INBOUND, 0x1C);
    public static final PacketType PLAY_CLIENT_UNLOAD_CHUNK = register(PLAY, INBOUND, 0x1D);
    public static final PacketType PLAY_CLIENT_CHANGE_GAME_STATE = register(PLAY, INBOUND, 0x1E);
    public static final PacketType PLAY_CLIENT_KEEP_ALIVE = register(PLAY, INBOUND, 0x1F);
    public static final PacketType PLAY_CLIENT_CHUNK_DATA = register(PLAY, INBOUND, 0x20);
    public static final PacketType PLAY_CLIENT_EFFECT = register(PLAY, INBOUND, 0x21);
    public static final PacketType PLAY_CLIENT_PARTICLE = register(PLAY, INBOUND, 0x22);
    public static final PacketType PLAY_CLIENT_JOIN_GAME = register(PLAY, INBOUND, 0x23);
    public static final PacketType PLAY_CLIENT_MAP = register(PLAY, INBOUND, 0x24);
    public static final PacketType PLAY_CLIENT_ENTITY_RELATIVE_MOVE = register(PLAY, INBOUND, 0x25);
    public static final PacketType PLAY_CLIENT_ENTITY_LOOK_AND_RELATIVE_MOVE = register(PLAY, INBOUND, 0x26);
    public static final PacketType PLAY_CLIENT_ENTITY_LOOK = register(PLAY, INBOUND, 0x27);
    public static final PacketType PLAY_CLIENT_ENTITY = register(PLAY, INBOUND, 0x28);
    public static final PacketType PLAY_CLIENT_VEHICLE_MOVE = register(PLAY, INBOUND, 0x29);
    public static final PacketType PLAY_CLIENT_OPEN_SIGN_EDITOR = register(PLAY, INBOUND, 0x2A);
    public static final PacketType PLAY_CLIENT_PLAYER_ABILITIES = register(PLAY, INBOUND, 0x2B);
    public static final PacketType PLAY_CLIENT_COMBAT_EVENT = register(PLAY, INBOUND, 0x2C);
    public static final PacketType PLAY_CLIENT_PLAYER_LIST_ITEM = register(PLAY, INBOUND, 0x2D);
    public static final PacketType PLAY_CLIENT_PLAYER_POSITION_AND_LOOK = register(PLAY, INBOUND, 0x2E);
    public static final PacketType PLAY_CLIENT_USE_BED = register(PLAY, INBOUND, 0x2F);
    public static final PacketType PLAY_CLIENT_DESTROY_ENTITIES = register(PLAY, INBOUND, 0x30);
    public static final PacketType PLAY_CLIENT_REMOVE_ENTITY_EFFECT = register(PLAY, INBOUND, 0x31);
    public static final PacketType PLAY_CLIENT_RESOURCE_PACK_SEND = register(PLAY, INBOUND, 0x32);
    public static final PacketType PLAY_CLIENT_RESPAWN = register(PLAY, INBOUND, 0x33);
    public static final PacketType PLAY_CLIENT_ENTITY_HEAD_LOOK = register(PLAY, INBOUND, 0x34);
    public static final PacketType PLAY_CLIENT_WORLD_BORDER = register(PLAY, INBOUND, 0x35);
    public static final PacketType PLAY_CLIENT_CAMERA = register(PLAY, INBOUND, 0x36);
    public static final PacketType PLAY_CLIENT_HELD_ITEM_CHANGE = register(PLAY, INBOUND, 0x37);
    public static final PacketType PLAY_CLIENT_DISPLAY_SCOREBOARD = register(PLAY, INBOUND, 0x38);
    public static final PacketType PLAY_CLIENT_ENTITY_METADATA = register(PLAY, INBOUND, 0x39);
    public static final PacketType PLAY_CLIENT_ATTACH_ENTITY = register(PLAY, INBOUND, 0x3A);
    public static final PacketType PLAY_CLIENT_ENTITY_VELOCITY = register(PLAY, INBOUND, 0x3B);
    public static final PacketType PLAY_CLIENT_ENTITY_EQUIPMENT = register(PLAY, INBOUND, 0x3C);
    public static final PacketType PLAY_CLIENT_SET_EXPERIENCE = register(PLAY, INBOUND, 0x3D);
    public static final PacketType PLAY_CLIENT_UPDATE_HEALTH = register(PLAY, INBOUND, 0x3E);
    public static final PacketType PLAY_CLIENT_SCOREBOARD_OBJECTIVE = register(PLAY, INBOUND, 0x3F);
    public static final PacketType PLAY_CLIENT_SET_PASSENGERS = register(PLAY, INBOUND, 0x40);
    public static final PacketType PLAY_CLIENT_TEAMS = register(PLAY, INBOUND, 0x41);
    public static final PacketType PLAY_CLIENT_UPDATE_SCORE = register(PLAY, INBOUND, 0x42);
    public static final PacketType PLAY_CLIENT_SPAWN_POSITION = register(PLAY, INBOUND, 0x43);
    public static final PacketType PLAY_CLIENT_TIME_UPDATE = register(PLAY, INBOUND, 0x44);
    public static final PacketType PLAY_CLIENT_TITLE = register(PLAY, INBOUND, 0x45);
    public static final PacketType PLAY_CLIENT_UPDATE_SIGN = register(PLAY, INBOUND, 0x46);
    public static final PacketType PLAY_CLIENT_SOUND_EFFECT = register(PLAY, INBOUND, 0x47);
    public static final PacketType PLAY_CLIENT_PLAYER_LIST_HEADER_AND_FOOTER = register(PLAY, INBOUND, 0x48);
    public static final PacketType PLAY_CLIENT_COLLECT_ITEM = register(PLAY, INBOUND, 0x49);
    public static final PacketType PLAY_CLIENT_ENTITY_TELEPORT = register(PLAY, INBOUND, 0x4A);
    public static final PacketType PLAY_CLIENT_ENTITY_PROPERTIES = register(PLAY, INBOUND, 0x4B);
    public static final PacketType PLAY_CLIENT_ENTITY_EFFECT = register(PLAY, INBOUND, 0x4C);
    // Server
    public static final PacketType PLAY_SERVER_TELEPORT_CONFIRM = register(PLAY, OUTBOUND, 0x00);
    public static final PacketType PLAY_SERVER_TAB_COMPLETE = register(PLAY, OUTBOUND, 0x01);
    public static final PacketType PLAY_SERVER_CHAT_MESSAGE = register(PLAY, OUTBOUND, 0x02);
    public static final PacketType PLAY_SERVER_CLIENT_STATUS = register(PLAY, OUTBOUND, 0x03);
    public static final PacketType PLAY_SERVER_CLIENT_SETTINGS = register(PLAY, OUTBOUND, 0x04);
    public static final PacketType PLAY_SERVER_CONFIRM_TRANSACTION = register(PLAY, OUTBOUND, 0x05);
    public static final PacketType PLAY_SERVER_ENCHANT_ITEM = register(PLAY, OUTBOUND, 0x06);
    public static final PacketType PLAY_SERVER_CLICK_WINDOW = register(PLAY, OUTBOUND, 0x07);
    public static final PacketType PLAY_SERVER_CLOSE_WINDOW = register(PLAY, OUTBOUND, 0x08);
    public static final PacketType PLAY_SERVER_PLUGIN_MESSAGE = register(PLAY, OUTBOUND, 0x09);
    public static final PacketType PLAY_SERVER_USE_ENTITY = register(PLAY, OUTBOUND, 0x0A);
    public static final PacketType PLAY_SERVER_KEEP_ALIVE = register(PLAY, OUTBOUND, 0x0B);
    public static final PacketType PLAY_SERVER_PLAYER_POSITION = register(PLAY, OUTBOUND, 0x0C);
    public static final PacketType PLAY_SERVER_PLAYER_POSITION_AND_LOOK = register(PLAY, OUTBOUND, 0x0D);
    public static final PacketType PLAY_SERVER_PLAYER_LOOK = register(PLAY, OUTBOUND, 0x0E);
    public static final PacketType PLAY_SERVER_PLAYER = register(PLAY, OUTBOUND, 0x0F);
    public static final PacketType PLAY_SERVER_VEHICLE_MOVE = register(PLAY, OUTBOUND, 0x10);
    public static final PacketType PLAY_SERVER_STEER_BOAT = register(PLAY, OUTBOUND, 0x11);
    public static final PacketType PLAY_SERVER_PLAYER_ABILITIES = register(PLAY, OUTBOUND, 0x12);
    public static final PacketType PLAY_SERVER_PLAYER_DIGGING = register(PLAY, OUTBOUND, 0x13);
    public static final PacketType PLAY_SERVER_ENTITY_ACTION = register(PLAY, OUTBOUND, 0x14);
    public static final PacketType PLAY_SERVER_STEER_VEHCLE = register(PLAY, OUTBOUND, 0x15);
    public static final PacketType PLAY_SERVER_RESOURCE_PACK_STATUS = register(PLAY, OUTBOUND, 0x16);
    public static final PacketType PLAY_SERVER_HELD_ITEM_CHANGE = register(PLAY, OUTBOUND, 0x17);
    public static final PacketType PLAY_SERVER_CREATIVE_INVENTORY_ACTION = register(PLAY, OUTBOUND, 0x18);
    public static final PacketType PLAY_SERVER_UPDATE_SIGN = register(PLAY, OUTBOUND, 0x19);
    public static final PacketType PLAY_SERVER_ANIMATION = register(PLAY, OUTBOUND, 0x1A);
    public static final PacketType PLAY_SERVER_SPECTATE = register(PLAY, OUTBOUND, 0x1B);
    public static final PacketType PLAY_SERVER_PLAYER_BLOCK_PLACEMENT = register(PLAY, OUTBOUND, 0x1C);
    public static final PacketType PLAY_SERVER_USE_ITEM = register(PLAY, OUTBOUND, 0x1D);

//    // Status Packets
//    // Client
//    public static final PacketType STATUS_CLIENT_RESPONSE = register(STATUS, OUTBOUND, 0x00);
//    public static final PacketType STATUS_CLIENT_PONG = register(STATUS, OUTBOUND, 0x01);
//    // Server
//    public static final PacketType STATUS_SERVER_REQUEST = register(STATUS, INBOUND, 0x00);
//    public static final PacketType STATUS_SERVER_PING = register(STATUS, INBOUND, 0x01);
//
//
//    // Login Packets
//    // Client
//    public static final PacketType LOGIN_CLIENT_DISCONNECT = register(LOGIN, OUTBOUND, 0x00);
//    public static final PacketType LOGIN_CLIENT_ENCRYPTION_REQUEST = register(LOGIN, OUTBOUND, 0x01);
//    public static final PacketType LOGIN_CLIENT_LOGIN_SUCCESS = register(LOGIN, OUTBOUND, 0x02);
//    public static final PacketType LOGIN_CLIENT_ENABLE_COMPRESSION = register(LOGIN, OUTBOUND, 0x03);
//    // Server
//    public static final PacketType LOGIN_SERVER_LOGIN_START = register(LOGIN, INBOUND, 0x00);
//    public static final PacketType LOGIN_SERVER_ENCRYPTION_RESPONSE = register(LOGIN, INBOUND, 0x01);
}
