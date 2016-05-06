package net.year4000.utilities.sponge.protocol;

import static net.year4000.utilities.sponge.protocol.PacketTypes.Binding.INBOUND;
import static net.year4000.utilities.sponge.protocol.PacketTypes.Binding.OUTBOUND;
import static net.year4000.utilities.sponge.protocol.PacketTypes.State.LOGIN;
import static net.year4000.utilities.sponge.protocol.PacketTypes.State.PLAY;
import static net.year4000.utilities.sponge.protocol.PacketTypes.State.STATUS;

import com.google.common.collect.Maps;
import net.year4000.utilities.Conditions;
import net.year4000.utilities.sponge.protocol.proxy.ProxyEnumConnectionState;
import net.year4000.utilities.utils.UtilityConstructError;
import net.year4000.utilities.value.Value;

import java.util.concurrent.ConcurrentMap;


/** A enumeration of all the type of packets Minecraft 1.9.2 can handle */
public final class PacketTypes {
    private static final ConcurrentMap<Class<?>, PacketType> map = Maps.newConcurrentMap();

    private PacketTypes() {
        UtilityConstructError.raise();
    }

    /** Create the packet type */
    public static PacketType of(State state, Binding binding, int id) {
        PacketType type = new PacketType(id, state.ordinal(), binding.ordinal());
        fromType(type); // tries to of the cache
        return type;
    }

    /** Try and get the class from the type */
    public static Value<Class<?>> fromType(PacketType type) {
        Conditions.nonNull(type, "type");
        try {
            ProxyEnumConnectionState connectionState = ProxyEnumConnectionState.get();
            ProxyEnumConnectionState instance = connectionState.value(type.state());
            Class<?> clazz = instance.packet(type);
            if (clazz != null) {
                map.put(clazz, type);
            }
            return Value.of(clazz);
        } catch (Exception error) {
            return Value.empty();
        }
    }

    /** Get the PacketType from the class */
    public static Value<PacketType> fromClass(Class<?> clazz) {
        Conditions.nonNull(clazz, "clazz");
        return Value.of(map.get(clazz));
    }

    /** Which way the packets are bounded to from the server perspective */
    public enum Binding {INBOUND, OUTBOUND}

    /** The type of packet they are */
    public enum State {HANDSHAKE, PLAY, STATUS, LOGIN}

    /** The latest protocol version this class has been updated for */
    public static final int PROTOCOL_VERSION = 109;

    // Play Packets
    // Client
    public static final PacketType PLAY_CLIENT_SPAWN_OBJECT = of(PLAY, OUTBOUND, 0x00);
    public static final PacketType PLAY_CLIENT_SPAWN_EXPERIENCE_ORB = of(PLAY, OUTBOUND, 0x01);
    public static final PacketType PLAY_CLIENT_SPAWN_GLOBAL_ENTITY = of(PLAY, OUTBOUND, 0x02);
    public static final PacketType PLAY_CLIENT_SPAWN_MOB = of(PLAY, OUTBOUND, 0x03);
    public static final PacketType PLAY_CLIENT_SPAWN_PAINTING = of(PLAY, OUTBOUND, 0x04);
    public static final PacketType PLAY_CLIENT_SPAWN_PLAYER = of(PLAY, OUTBOUND, 0x05);
    public static final PacketType PLAY_CLIENT_ANIMATION = of(PLAY, OUTBOUND, 0x06);
    public static final PacketType PLAY_CLIENT_STATISTICS = of(PLAY, OUTBOUND, 0x07);
    public static final PacketType PLAY_CLIENT_BLOCK_BREAK_ANIMATION = of(PLAY, OUTBOUND, 0x08);
    public static final PacketType PLAY_CLIENT_UPDATE_BLOCK_ENTITY = of(PLAY, OUTBOUND, 0x09);
    public static final PacketType PLAY_CLIENT_BLOCK_ACTION = of(PLAY, OUTBOUND, 0x0A);
    public static final PacketType PLAY_CLIENT_BLOCK_CHANGE = of(PLAY, OUTBOUND, 0x0B);
    public static final PacketType PLAY_CLIENT_BOSS_BAR = of(PLAY, OUTBOUND, 0x0C);
    public static final PacketType PLAY_CLIENT_SERVER_DIFFICULTY = of(PLAY, OUTBOUND, 0x0D);
    public static final PacketType PLAY_CLIENT_TAB_COMPLETE = of(PLAY, OUTBOUND, 0x0E);
    public static final PacketType PLAY_CLIENT_CHAT_MESSAGE = of(PLAY, OUTBOUND, 0x0F);
    public static final PacketType PLAY_CLIENT_MULTI_BLOCK_CHANGE = of(PLAY, OUTBOUND, 0x10);
    public static final PacketType PLAY_CLIENT_CONFIRM_TRANSACTION = of(PLAY, OUTBOUND, 0x11);
    public static final PacketType PLAY_CLIENT_CLOSE_WINDOW = of(PLAY, OUTBOUND, 0x12);
    public static final PacketType PLAY_CLIENT_OPEN_WINDOW = of(PLAY, OUTBOUND, 0x13);
    public static final PacketType PLAY_CLIENT_WINDOW_ITEMS = of(PLAY, OUTBOUND, 0x14);
    public static final PacketType PLAY_CLIENT_WINDOW_PROPERTY = of(PLAY, OUTBOUND, 0x15);
    public static final PacketType PLAY_CLIENT_SET_SLOT = of(PLAY, OUTBOUND, 0x16);
    public static final PacketType PLAY_CLIENT_SET_COOLDOWN = of(PLAY, OUTBOUND, 0x17);
    public static final PacketType PLAY_CLIENT_PLUGIN_MESSAGE = of(PLAY, OUTBOUND, 0x18);
    public static final PacketType PLAY_CLIENT_NAMED_SOUND_EFFECT = of(PLAY, OUTBOUND, 0x19);
    public static final PacketType PLAY_CLIENT_DISCONNECT = of(PLAY, OUTBOUND, 0x1A);
    public static final PacketType PLAY_CLIENT_ENTITY_STATUS = of(PLAY, OUTBOUND, 0x1B);
    public static final PacketType PLAY_CLIENT_EXPLOSION = of(PLAY, OUTBOUND, 0x1C);
    public static final PacketType PLAY_CLIENT_UNLOAD_CHUNK = of(PLAY, OUTBOUND, 0x1D);
    public static final PacketType PLAY_CLIENT_CHANGE_GAME_STATE = of(PLAY, OUTBOUND, 0x1E);
    public static final PacketType PLAY_CLIENT_KEEP_ALIVE = of(PLAY, OUTBOUND, 0x1F);
    public static final PacketType PLAY_CLIENT_CHUNK_DATA = of(PLAY, OUTBOUND, 0x20);
    public static final PacketType PLAY_CLIENT_EFFECT = of(PLAY, OUTBOUND, 0x21);
    public static final PacketType PLAY_CLIENT_PARTICLE = of(PLAY, OUTBOUND, 0x22);
    public static final PacketType PLAY_CLIENT_JOIN_GAME = of(PLAY, OUTBOUND, 0x23);
    public static final PacketType PLAY_CLIENT_MAP = of(PLAY, OUTBOUND, 0x24);
    public static final PacketType PLAY_CLIENT_ENTITY_RELATIVE_MOVE = of(PLAY, OUTBOUND, 0x25);
    public static final PacketType PLAY_CLIENT_ENTITY_LOOK_AND_RELATIVE_MOVE = of(PLAY, OUTBOUND, 0x26);
    public static final PacketType PLAY_CLIENT_ENTITY_LOOK = of(PLAY, OUTBOUND, 0x27);
    public static final PacketType PLAY_CLIENT_ENTITY = of(PLAY, OUTBOUND, 0x28);
    public static final PacketType PLAY_CLIENT_VEHICLE_MOVE = of(PLAY, OUTBOUND, 0x29);
    public static final PacketType PLAY_CLIENT_OPEN_SIGN_EDITOR = of(PLAY, OUTBOUND, 0x2A);
    public static final PacketType PLAY_CLIENT_PLAYER_ABILITIES = of(PLAY, OUTBOUND, 0x2B);
    public static final PacketType PLAY_CLIENT_COMBAT_EVENT = of(PLAY, OUTBOUND, 0x2C);
    public static final PacketType PLAY_CLIENT_PLAYER_LIST_ITEM = of(PLAY, OUTBOUND, 0x2D);
    public static final PacketType PLAY_CLIENT_PLAYER_POSITION_AND_LOOK = of(PLAY, OUTBOUND, 0x2E);
    public static final PacketType PLAY_CLIENT_USE_BED = of(PLAY, OUTBOUND, 0x2F);
    public static final PacketType PLAY_CLIENT_DESTROY_ENTITIES = of(PLAY, OUTBOUND, 0x30);
    public static final PacketType PLAY_CLIENT_REMOVE_ENTITY_EFFECT = of(PLAY, OUTBOUND, 0x31);
    public static final PacketType PLAY_CLIENT_RESOURCE_PACK_SEND = of(PLAY, OUTBOUND, 0x32);
    public static final PacketType PLAY_CLIENT_RESPAWN = of(PLAY, OUTBOUND, 0x33);
    public static final PacketType PLAY_CLIENT_ENTITY_HEAD_LOOK = of(PLAY, OUTBOUND, 0x34);
    public static final PacketType PLAY_CLIENT_WORLD_BORDER = of(PLAY, OUTBOUND, 0x35);
    public static final PacketType PLAY_CLIENT_CAMERA = of(PLAY, OUTBOUND, 0x36);
    public static final PacketType PLAY_CLIENT_HELD_ITEM_CHANGE = of(PLAY, OUTBOUND, 0x37);
    public static final PacketType PLAY_CLIENT_DISPLAY_SCOREBOARD = of(PLAY, OUTBOUND, 0x38);
    public static final PacketType PLAY_CLIENT_ENTITY_METADATA = of(PLAY, OUTBOUND, 0x39);
    public static final PacketType PLAY_CLIENT_ATTACH_ENTITY = of(PLAY, OUTBOUND, 0x3A);
    public static final PacketType PLAY_CLIENT_ENTITY_VELOCITY = of(PLAY, OUTBOUND, 0x3B);
    public static final PacketType PLAY_CLIENT_ENTITY_EQUIPMENT = of(PLAY, OUTBOUND, 0x3C);
    public static final PacketType PLAY_CLIENT_SET_EXPERIENCE = of(PLAY, OUTBOUND, 0x3D);
    public static final PacketType PLAY_CLIENT_UPDATE_HEALTH = of(PLAY, OUTBOUND, 0x3E);
    public static final PacketType PLAY_CLIENT_SCOREBOARD_OBJECTIVE = of(PLAY, OUTBOUND, 0x3F);
    public static final PacketType PLAY_CLIENT_SET_PASSENGERS = of(PLAY, OUTBOUND, 0x40);
    public static final PacketType PLAY_CLIENT_TEAMS = of(PLAY, OUTBOUND, 0x41);
    public static final PacketType PLAY_CLIENT_UPDATE_SCORE = of(PLAY, OUTBOUND, 0x42);
    public static final PacketType PLAY_CLIENT_SPAWN_POSITION = of(PLAY, OUTBOUND, 0x43);
    public static final PacketType PLAY_CLIENT_TIME_UPDATE = of(PLAY, OUTBOUND, 0x44);
    public static final PacketType PLAY_CLIENT_TITLE = of(PLAY, OUTBOUND, 0x45);
    public static final PacketType PLAY_CLIENT_UPDATE_SIGN = of(PLAY, OUTBOUND, 0x46);
    public static final PacketType PLAY_CLIENT_SOUND_EFFECT = of(PLAY, OUTBOUND, 0x47);
    public static final PacketType PLAY_CLIENT_PLAYER_LIST_HEADER_AND_FOOTER = of(PLAY, OUTBOUND, 0x48);
    public static final PacketType PLAY_CLIENT_COLLECT_ITEM = of(PLAY, OUTBOUND, 0x49);
    public static final PacketType PLAY_CLIENT_ENTITY_TELEPORT = of(PLAY, OUTBOUND, 0x4A);
    public static final PacketType PLAY_CLIENT_ENTITY_PROPERTIES = of(PLAY, OUTBOUND, 0x4B);
    public static final PacketType PLAY_CLIENT_ENTITY_EFFECT = of(PLAY, OUTBOUND, 0x4C);
    // Server
    public static final PacketType PLAY_SERVER_TELEPORT_CONFIRM = of(PLAY, INBOUND, 0x00);
    public static final PacketType PLAY_SERVER_TAB_COMPLETE = of(PLAY, INBOUND, 0x01);
    public static final PacketType PLAY_SERVER_CHAT_MESSAGE = of(PLAY, INBOUND, 0x02);
    public static final PacketType PLAY_SERVER_CLIENT_STATUS = of(PLAY, INBOUND, 0x03);
    public static final PacketType PLAY_SERVER_CLIENT_SETTINGS = of(PLAY, INBOUND, 0x04);
    public static final PacketType PLAY_SERVER_CONFIRM_TRANSACTION = of(PLAY, INBOUND, 0x05);
    public static final PacketType PLAY_SERVER_ENCHANT_ITEM = of(PLAY, INBOUND, 0x06);
    public static final PacketType PLAY_SERVER_CLICK_WINDOW = of(PLAY, INBOUND, 0x07);
    public static final PacketType PLAY_SERVER_CLOSE_WINDOW = of(PLAY, INBOUND, 0x08);
    public static final PacketType PLAY_SERVER_PLUGIN_MESSAGE = of(PLAY, INBOUND, 0x09);
    public static final PacketType PLAY_SERVER_USE_ENTITY = of(PLAY, INBOUND, 0x0A);
    public static final PacketType PLAY_SERVER_KEEP_ALIVE = of(PLAY, INBOUND, 0x0B);
    public static final PacketType PLAY_SERVER_PLAYER_POSITION = of(PLAY, INBOUND, 0x0C);
    public static final PacketType PLAY_SERVER_PLAYER_POSITION_AND_LOOK = of(PLAY, INBOUND, 0x0D);
    public static final PacketType PLAY_SERVER_PLAYER_LOOK = of(PLAY, INBOUND, 0x0E);
    public static final PacketType PLAY_SERVER_PLAYER = of(PLAY, INBOUND, 0x0F);
    public static final PacketType PLAY_SERVER_VEHICLE_MOVE = of(PLAY, INBOUND, 0x10);
    public static final PacketType PLAY_SERVER_STEER_BOAT = of(PLAY, INBOUND, 0x11);
    public static final PacketType PLAY_SERVER_PLAYER_ABILITIES = of(PLAY, INBOUND, 0x12);
    public static final PacketType PLAY_SERVER_PLAYER_DIGGING = of(PLAY, INBOUND, 0x13);
    public static final PacketType PLAY_SERVER_ENTITY_ACTION = of(PLAY, INBOUND, 0x14);
    public static final PacketType PLAY_SERVER_STEER_VEHCLE = of(PLAY, INBOUND, 0x15);
    public static final PacketType PLAY_SERVER_RESOURCE_PACK_STATUS = of(PLAY, INBOUND, 0x16);
    public static final PacketType PLAY_SERVER_HELD_ITEM_CHANGE = of(PLAY, INBOUND, 0x17);
    public static final PacketType PLAY_SERVER_CREATIVE_INVENTORY_ACTION = of(PLAY, INBOUND, 0x18);
    public static final PacketType PLAY_SERVER_UPDATE_SIGN = of(PLAY, INBOUND, 0x19);
    public static final PacketType PLAY_SERVER_ANIMATION = of(PLAY, INBOUND, 0x1A);
    public static final PacketType PLAY_SERVER_SPECTATE = of(PLAY, INBOUND, 0x1B);
    public static final PacketType PLAY_SERVER_PLAYER_BLOCK_PLACEMENT = of(PLAY, INBOUND, 0x1C);
    public static final PacketType PLAY_SERVER_USE_ITEM = of(PLAY, INBOUND, 0x1D);

    // Status Packets
    // Client
    public static final PacketType STATUS_CLIENT_RESPONSE = of(STATUS, INBOUND, 0x00);
    public static final PacketType STATUS_CLIENT_PONG = of(STATUS, INBOUND, 0x01);
    // Server
    public static final PacketType STATUS_SERVER_REQUEST = of(STATUS, OUTBOUND, 0x00);
    public static final PacketType STATUS_SERVER_PING = of(STATUS, OUTBOUND, 0x01);


    // Login Packets
    // Client
    public static final PacketType LOGIN_CLIENT_DISCONNECT = of(LOGIN, INBOUND, 0x00);
    public static final PacketType LOGIN_CLIENT_ENCRYPTION_REQUEST = of(LOGIN, INBOUND, 0x01);
    public static final PacketType LOGIN_CLIENT_LOGIN_SUCCESS = of(LOGIN, INBOUND, 0x02);
    public static final PacketType LOGIN_CLIENT_ENABLE_COMPRESSION = of(LOGIN, INBOUND, 0x03);
    // Server
    public static final PacketType LOGIN_SERVER_LOGIN_START = of(LOGIN, OUTBOUND, 0x00);
    public static final PacketType LOGIN_SERVER_ENCRYPTION_RESPONSE = of(LOGIN, OUTBOUND, 0x01);
}
