package net.year4000.utilities.sponge.protocol;

import net.year4000.utilities.utils.UtilityConstructError;

import static net.year4000.utilities.sponge.protocol.PacketTypes.Binding.*;
import static net.year4000.utilities.sponge.protocol.PacketTypes.State.*;

/** A enumeration of all the type of packets Minecraft 1.9.2 can handle */
public final class PacketTypes {
    private PacketTypes() {
        UtilityConstructError.raise();
    }

    /** Create the packet type */
    public static PacketType type(State type, Binding binding, int id) {
        return new PacketType(id, binding.ordinal(), type.ordinal());
    }

    // Which way the packets are bounded to
    enum Binding {CLIENT, SERVER}

    // The type of packet they are
    enum State {PLAY, STATUS, LOGIN}

    // Play Packets
    // Client
    public static final PacketType PLAY_CLIENT_SPAWN_OBJECT = type(PLAY, CLIENT, 0x00);
    public static final PacketType PLAY_CLIENT_SPAWN_EXPERIENCE_ORB = type(PLAY, CLIENT, 0x01);
    public static final PacketType PLAY_CLIENT_SPAWN_GLOBAL_ENTITY = type(PLAY, CLIENT, 0x02);
    public static final PacketType PLAY_CLIENT_SPAWN_MOB = type(PLAY, CLIENT, 0x03);
    public static final PacketType PLAY_CLIENT_SPAWN_PAINTING = type(PLAY, CLIENT, 0x04);
    public static final PacketType PLAY_CLIENT_SPAWN_PLAYER = type(PLAY, CLIENT, 0x05);
    public static final PacketType PLAY_CLIENT_ANIMATION = type(PLAY, CLIENT, 0x06);
    public static final PacketType PLAY_CLIENT_STATISTICS = type(PLAY, CLIENT, 0x07);
    public static final PacketType PLAY_CLIENT_BLOCK_BREAK_ANIMATION = type(PLAY, CLIENT, 0x08);
    public static final PacketType PLAY_CLIENT_UPDATE_BLOCK_ENTITY = type(PLAY, CLIENT, 0x09);
    public static final PacketType PLAY_CLIENT_BLOCK_ACTION = type(PLAY, CLIENT, 0x0A);
    public static final PacketType PLAY_CLIENT_BLOCK_CHANGE = type(PLAY, CLIENT, 0x0B);
    public static final PacketType PLAY_CLIENT_BOSS_BAR = type(PLAY, CLIENT, 0x0C);
    public static final PacketType PLAY_CLIENT_SERVER_DIFFICULTY = type(PLAY, CLIENT, 0x0D);
    public static final PacketType PLAY_CLIENT_TAB_COMPLETE = type(PLAY, CLIENT, 0x0E);
    public static final PacketType PLAY_CLIENT_CHAT_MESSAGE = type(PLAY, CLIENT, 0x0F);
    public static final PacketType PLAY_CLIENT_MULTI_BLOCK_CHANGE = type(PLAY, CLIENT, 0x10);
    public static final PacketType PLAY_CLIENT_CONFIRM_TRANSACTION = type(PLAY, CLIENT, 0x11);
    public static final PacketType PLAY_CLIENT_CLOSE_WINDOW = type(PLAY, CLIENT, 0x12);
    public static final PacketType PLAY_CLIENT_OPEN_WINDOW = type(PLAY, CLIENT, 0x13);
    public static final PacketType PLAY_CLIENT_WINDOW_ITEMS = type(PLAY, CLIENT, 0x14);
    public static final PacketType PLAY_CLIENT_WINDOW_PROPERTY = type(PLAY, CLIENT, 0x15);
    public static final PacketType PLAY_CLIENT_SET_SLOT = type(PLAY, CLIENT, 0x16);
    public static final PacketType PLAY_CLIENT_SET_COOLDOWN = type(PLAY, CLIENT, 0x17);
    public static final PacketType PLAY_CLIENT_PLUGIN_MESSAGE = type(PLAY, CLIENT, 0x18);
    public static final PacketType PLAY_CLIENT_NAMED_SOUND_EFFECT = type(PLAY, CLIENT, 0x19);
    public static final PacketType PLAY_CLIENT_DISCONNECT = type(PLAY, CLIENT, 0x1A);
    public static final PacketType PLAY_CLIENT_ENTITY_STATUS = type(PLAY, CLIENT, 0x1B);
    public static final PacketType PLAY_CLIENT_EXPLOSION = type(PLAY, CLIENT, 0x1C);
    public static final PacketType PLAY_CLIENT_UNLOAD_CHUNK = type(PLAY, CLIENT, 0x1D);
    public static final PacketType PLAY_CLIENT_CHANGE_GAME_STATE = type(PLAY, CLIENT, 0x1E);
    public static final PacketType PLAY_CLIENT_KEEP_ALIVE = type(PLAY, CLIENT, 0x1F);
    public static final PacketType PLAY_CLIENT_CHUNK_DATA = type(PLAY, CLIENT, 0x20);
    public static final PacketType PLAY_CLIENT_EFFECT = type(PLAY, CLIENT, 0x21);
    public static final PacketType PLAY_CLIENT_PARTICLE = type(PLAY, CLIENT, 0x22);
    public static final PacketType PLAY_CLIENT_JOIN_GAME = type(PLAY, CLIENT, 0x23);
    public static final PacketType PLAY_CLIENT_MAP = type(PLAY, CLIENT, 0x24);
    public static final PacketType PLAY_CLIENT_ENTITY_RELATIVE_MOVE = type(PLAY, CLIENT, 0x25);
    public static final PacketType PLAY_CLIENT_ENTITY_LOOK_AND_RELATIVE_MOVE = type(PLAY, CLIENT, 0x26);
    public static final PacketType PLAY_CLIENT_ENTITY_LOOK = type(PLAY, CLIENT, 0x27);
    public static final PacketType PLAY_CLIENT_ENTITY = type(PLAY, CLIENT, 0x28);
    public static final PacketType PLAY_CLIENT_VEHICLE_MOVE = type(PLAY, CLIENT, 0x29);
    public static final PacketType PLAY_CLIENT_OPEN_SIGN_EDITOR = type(PLAY, CLIENT, 0x2A);
    public static final PacketType PLAY_CLIENT_PLAYER_ABILITIES = type(PLAY, CLIENT, 0x2B);
    public static final PacketType PLAY_CLIENT_COMBAT_EVENT = type(PLAY, CLIENT, 0x2C);
    public static final PacketType PLAY_CLIENT_PLAYER_LIST_ITEM = type(PLAY, CLIENT, 0x2D);
    public static final PacketType PLAY_CLIENT_PLAYER_POSITION_AND_LOOK = type(PLAY, CLIENT, 0x2E);
    public static final PacketType PLAY_CLIENT_USE_BED = type(PLAY, CLIENT, 0x2F);
    public static final PacketType PLAY_CLIENT_DESTROY_ENTITIES = type(PLAY, CLIENT, 0x30);
    public static final PacketType PLAY_CLIENT_REMOVE_ENTITY_EFFECT = type(PLAY, CLIENT, 0x31);
    public static final PacketType PLAY_CLIENT_RESOURCE_PACK_SEND = type(PLAY, CLIENT, 0x32);
    public static final PacketType PLAY_CLIENT_RESPAWN = type(PLAY, CLIENT, 0x33);
    public static final PacketType PLAY_CLIENT_ENTITY_HEAD_LOOK = type(PLAY, CLIENT, 0x34);
    public static final PacketType PLAY_CLIENT_WORLD_BORDER = type(PLAY, CLIENT, 0x35);
    public static final PacketType PLAY_CLIENT_CAMERA = type(PLAY, CLIENT, 0x36);
    public static final PacketType PLAY_CLIENT_HELD_ITEM_CHANGE = type(PLAY, CLIENT, 0x37);
    public static final PacketType PLAY_CLIENT_DISPLAY_SCOREBOARD = type(PLAY, CLIENT, 0x38);
    public static final PacketType PLAY_CLIENT_ENTITY_METADATA = type(PLAY, CLIENT, 0x39);
    public static final PacketType PLAY_CLIENT_ATTACH_ENTITY = type(PLAY, CLIENT, 0x3A);
    public static final PacketType PLAY_CLIENT_ENTITY_VELOCITY = type(PLAY, CLIENT, 0x3B);
    public static final PacketType PLAY_CLIENT_ENTITY_EQUIPMENT = type(PLAY, CLIENT, 0x3C);
    public static final PacketType PLAY_CLIENT_SET_EXPERIENCE = type(PLAY, CLIENT, 0x3D);
    public static final PacketType PLAY_CLIENT_UPDATE_HEALTH = type(PLAY, CLIENT, 0x3E);
    public static final PacketType PLAY_CLIENT_SCOREBOARD_OBJECTIVE = type(PLAY, CLIENT, 0x3F);
    public static final PacketType PLAY_CLIENT_SET_PASSENGERS = type(PLAY, CLIENT, 0x40);
    public static final PacketType PLAY_CLIENT_TEAMS = type(PLAY, CLIENT, 0x41);
    public static final PacketType PLAY_CLIENT_UPDATE_SCORE = type(PLAY, CLIENT, 0x42);
    public static final PacketType PLAY_CLIENT_SPAWN_POSITION = type(PLAY, CLIENT, 0x43);
    public static final PacketType PLAY_CLIENT_TIME_UPDATE = type(PLAY, CLIENT, 0x44);
    public static final PacketType PLAY_CLIENT_TITLE = type(PLAY, CLIENT, 0x45);
    public static final PacketType PLAY_CLIENT_UPDATE_SIGN = type(PLAY, CLIENT, 0x46);
    public static final PacketType PLAY_CLIENT_SOUND_EFFECT = type(PLAY, CLIENT, 0x47);
    public static final PacketType PLAY_CLIENT_PLAYER_LIST_HEADER_AND_FOOTER = type(PLAY, CLIENT, 0x48);
    public static final PacketType PLAY_CLIENT_COLLECT_ITEM = type(PLAY, CLIENT, 0x49);
    public static final PacketType PLAY_CLIENT_ENTITY_TELEPORT = type(PLAY, CLIENT, 0x4A);
    public static final PacketType PLAY_CLIENT_ENTITY_PROPERTIES = type(PLAY, CLIENT, 0x4B);
    public static final PacketType PLAY_CLIENT_ENTITY_EFFECT = type(PLAY, CLIENT, 0x4C);
    // Server
    public static final PacketType PLAY_SERVER_TELEPORT_CONFIRM = type(PLAY, SERVER, 0x00);
    public static final PacketType PLAY_SERVER_TAB_COMPLETE = type(PLAY, SERVER, 0x01);
    public static final PacketType PLAY_SERVER_CHAT_MESSAGE = type(PLAY, SERVER, 0x02);
    public static final PacketType PLAY_SERVER_CLIENT_STATUS = type(PLAY, SERVER, 0x03);
    public static final PacketType PLAY_SERVER_CLIENT_SETTINGS = type(PLAY, SERVER, 0x04);
    public static final PacketType PLAY_SERVER_CONFIRM_TRANSACTION = type(PLAY, SERVER, 0x05);
    public static final PacketType PLAY_SERVER_ENCHANT_ITEM = type(PLAY, SERVER, 0x06);
    public static final PacketType PLAY_SERVER_CLICK_WINDOW = type(PLAY, SERVER, 0x07);
    public static final PacketType PLAY_SERVER_CLOSE_WINDOW = type(PLAY, SERVER, 0x08);
    public static final PacketType PLAY_SERVER_PLUGIN_MESSAGE = type(PLAY, SERVER, 0x09);
    public static final PacketType PLAY_SERVER_USE_ENTITY = type(PLAY, SERVER, 0x0A);
    public static final PacketType PLAY_SERVER_KEEP_ALIVE = type(PLAY, SERVER, 0x0B);
    public static final PacketType PLAY_SERVER_PLAYER_POSITION = type(PLAY, SERVER, 0x0C);
    public static final PacketType PLAY_SERVER_PLAYER_POSITION_AND_LOOK = type(PLAY, SERVER, 0x0D);
    public static final PacketType PLAY_SERVER_PLAYER_LOOK = type(PLAY, SERVER, 0x0E);
    public static final PacketType PLAY_SERVER_PLAYER = type(PLAY, SERVER, 0x0F);
    public static final PacketType PLAY_SERVER_VEHICLE_MOVE = type(PLAY, SERVER, 0x10);
    public static final PacketType PLAY_SERVER_STEER_BOAT = type(PLAY, SERVER, 0x11);
    public static final PacketType PLAY_SERVER_PLAYER_ABILITIES = type(PLAY, SERVER, 0x12);
    public static final PacketType PLAY_SERVER_PLAYER_DIGGING = type(PLAY, SERVER, 0x13);
    public static final PacketType PLAY_SERVER_ENTITY_ACTION = type(PLAY, SERVER, 0x14);
    public static final PacketType PLAY_SERVER_STEER_VEHCLE = type(PLAY, SERVER, 0x15);
    public static final PacketType PLAY_SERVER_RESOURCE_PACK_STATUS = type(PLAY, SERVER, 0x16);
    public static final PacketType PLAY_SERVER_HELD_ITEM_CHANGE = type(PLAY, SERVER, 0x17);
    public static final PacketType PLAY_SERVER_CREATIVE_INVENTORY_ACTION = type(PLAY, SERVER, 0x18);
    public static final PacketType PLAY_SERVER_UPDATE_SIGN = type(PLAY, SERVER, 0x19);
    public static final PacketType PLAY_SERVER_ANIMATION = type(PLAY, SERVER, 0x1A);
    public static final PacketType PLAY_SERVER_SPECTATE = type(PLAY, SERVER, 0x1B);
    public static final PacketType PLAY_SERVER_PLAYER_BLOCK_PLACEMENT = type(PLAY, SERVER, 0x1C);
    public static final PacketType PLAY_SERVER_USE_ITEM = type(PLAY, SERVER, 0x1D);

    // Status Packets
    // Client
    public static final PacketType STATUS_CLIENT_RESPONSE = type(STATUS, CLIENT, 0x00);
    public static final PacketType STATUS_CLIENT_PONG = type(STATUS, CLIENT, 0x01);
    // Server
    public static final PacketType STATUS_SERVER_REQUEST = type(STATUS, SERVER, 0x00);
    public static final PacketType STATUS_SERVER_PING = type(STATUS, SERVER, 0x01);


    // Login Packets
    // Client
    public static final PacketType LOGIN_CLIENT_DISCONNECT = type(LOGIN, CLIENT, 0x00);
    public static final PacketType LOGIN_CLIENT_ENCRYPTION_REQUEST = type(LOGIN, CLIENT, 0x01);
    public static final PacketType LOGIN_CLIENT_LOGIN_SUCCESS = type(LOGIN, CLIENT, 0x02);
    public static final PacketType LOGIN_CLIENT_ENABLE_COMPRESSION = type(LOGIN, CLIENT, 0x03);
    // Server
    public static final PacketType LOGIN_SERVER_LOGIN_START = type(LOGIN, SERVER, 0x00);
    public static final PacketType LOGIN_SERVER_ENCRYPTION_RESPONSE = type(LOGIN, SERVER, 0x01);
}
