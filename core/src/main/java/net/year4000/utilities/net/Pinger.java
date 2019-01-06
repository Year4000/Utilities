/*
 * Copyright 2019 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.net;

import com.google.gson.*;
import net.year4000.utilities.Callback;
import net.year4000.utilities.Conditions;
import net.year4000.utilities.Utils;

import java.io.*;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public final class Pinger {
    public static final int TIME_OUT = (int) TimeUnit.SECONDS.toMillis(5);
    private static final Gson gson = new GsonBuilder().registerTypeAdapter(String.class, new TextDeserializer()).create();
    private InetSocketAddress host;
    private int timeout = TIME_OUT;

    public Pinger(InetSocketAddress host, int timeout) {
        this.host = Conditions.nonNull(host, "host");
        this.timeout = Conditions.isLarger(timeout, 1);
    }

    public Pinger(String address, short port, int timeout) {
        this(new InetSocketAddress(address, port), timeout);
    }

    public Pinger() {}

    public int readVarInt(DataInputStream in) throws IOException {
        int i = 0;
        int j = 0;
        while (true) {
            int k = in.readByte();
            i |= (k & 0x7F) << j++ * 7;
            if (j > 5) {
                throw new RuntimeException("VarInt too big");
            }
            if ((k & 0x80) != 128) {
                break;
            }
        }
        return i;
    }

    public void writeVarInt(DataOutputStream out, int paramInt) throws IOException {
        while (true) {
            if ((paramInt & 0xFFFFFF80) == 0) {
                out.writeByte(paramInt);
                return;
            }
            out.writeByte(paramInt & 0x7F | 0x80);
            paramInt >>>= 7;
        }
    }

    public void fetchDataAsync(Callback<StatusResponse> callback) {
        try {
            callback.callback(fetchData());
        } catch (Exception error) {
            callback.callback(error);
        }
    }

    public StatusResponse fetchData() throws IOException {
        try (
            Socket socket = new Socket() {{
                setSoTimeout(TIME_OUT);
                connect(host, TIME_OUT);
            }};
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream handshake = new DataOutputStream(b);
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream())
        ) {
            handshake.writeByte(0x00); //packet id for handshake
            writeVarInt(handshake, 4); //protocol version
            writeVarInt(handshake, host.getHostString().length()); //host length
            handshake.writeBytes(host.getHostString()); //host string
            handshake.writeShort(host.getPort()); //port
            writeVarInt(handshake, 1); //state (1 for handshake)
            writeVarInt(dataOutputStream, b.size()); //prepend size
            dataOutputStream.write(b.toByteArray()); //write handshake packet
            dataOutputStream.writeByte(0x01); //size is only 1
            dataOutputStream.writeByte(0x00); //packet id for ping
            int one = readVarInt(dataInputStream); //size of packet
            int two = readVarInt(dataInputStream); //packet id
            int id = Math.min(one, two);
            int size = Math.max(one, two);
            Conditions.condition(id != -1, "Premature end of stream.");
            //we want a status response
            Conditions.condition(id == 0x00, "Invalid packetID, expecting 0x00(" + 0x00 + ") but was " + id);
            // There was a netty header sent, off set everything by one
            if (one != size) {
                readVarInt(dataInputStream);
            }
            int length = readVarInt(dataInputStream); //length of json string
            Conditions.condition(length != -1, "Premature end of stream.");
            Conditions.condition(length != 0, "Invalid string length.");
            byte[] in = new byte[length];
            dataInputStream.readFully(in);  //read json string
            String json = new String(in);
            long now = System.currentTimeMillis();
            dataOutputStream.writeByte(0x09); //size of packet
            dataOutputStream.writeByte(0x01); //0x01 for ping
            dataOutputStream.writeLong(now); //time!?
            readVarInt(dataInputStream);
            id = readVarInt(dataInputStream);
            Conditions.condition(id != -1, "Premature end of stream.");
            Conditions.condition(id == 0x01, "Invalid packetID, expecting 0x01(" + 0x01 + ") but was " + id);
            long pingtime = dataInputStream.readLong(); //read response
            StatusResponse response = gson.fromJson(json, StatusResponse.class);
            response.setTime((int) pingtime);
            return response;
        } catch (IllegalStateException e) {
            throw new IOException(e.getMessage());
        }
    }

    public InetSocketAddress getHost() {
        return this.host;
    }

    public int getTimeout() {
        return this.timeout;
    }

    public void setHost(InetSocketAddress host) {
        this.host = host;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    @Override
    public String toString() {
        return Utils.toString(this);
    }

    @Override
    public boolean equals(Object other) {
        return Utils.equals(this, other);
    }

    @Override
    public int hashCode() {
        return Utils.hashCode(this);
    }

    public class StatusResponse {
        private String description;
        private Players players;
        private Version version;
        private String favicon;
        private Integer time;

        public StatusResponse(String description, Players players, Version version, String favicon, Integer time) {
            this.description = Conditions.nonNull(description, "description");
            this.players = Conditions.nonNull(players, "players");
            this.version = Conditions.nonNull(version, "version");
            this.favicon = favicon;
            this.time = Conditions.nonNull(time, "time");
        }

        public StatusResponse() {}

        public Pinger.StatusResponse copy() {
            Pinger.StatusResponse copy = new StatusResponse();
            copy.setDescription(description);
            copy.setFavicon(favicon);
            copy.setTime(time);
            copy.setPlayers(players.copy());
            copy.setVersion(version.copy());
            return copy;
        }

        public String getDescription() {
            return this.description;
        }

        public Players getPlayers() {
            return this.players;
        }

        public Version getVersion() {
            return this.version;
        }

        public String getFavicon() {
            return this.favicon;
        }

        public Integer getTime() {
            return this.time;
        }

        public void setDescription(String description) {
            this.description = Conditions.nonNull(description, "description");
        }

        public void setPlayers(Players players) {
            this.players = players;
        }

        public void setVersion(Version version) {
            this.version = version;
        }

        public void setFavicon(String favicon) {
            // the favicon is optional
            this.favicon = favicon;
        }

        public void setTime(int time) {
            this.time = time;
        }

        @Override
        public String toString() {
            return Utils.toString(this);
        }

        @Override
        public boolean equals(Object other) {
            return Utils.equals(this, other);
        }

        @Override
        public int hashCode() {
            return Utils.hashCode(this);
        }
    }

    public class Players {
        private int max;
        private int online;
        private List<Player> sample;

        public Players(int max, int online, List<Player> sample) {
            this.max = max;
            this.online = online;
            this.sample = Conditions.nonNull(sample, "sample");
        }

        public Players() {}

        public Pinger.Players copy() {
            Players copy = new Players();
            copy.setMax(max);
            copy.setOnline(online);
            if (sample != null) {
                List<Player> samples = new ArrayList<>();
                sample.forEach(player -> samples.add(player.copy()));
                copy.setSample(samples);
            }
            return copy;
        }

        public int getMax() {
            return this.max;
        }

        public int getOnline() {
            return this.online;
        }

        public List<Player> getSample() {
            return this.sample;
        }

        public void setMax(int max) {
            this.max = max;
        }

        public void setOnline(int online) {
            this.online = online;
        }

        public void setSample(List<Player> sample) {
            this.sample = sample;
        }

        @Override
        public String toString() {
            return Utils.toString(this);
        }

        @Override
        public boolean equals(Object other) {
            return Utils.equals(this, other);
        }

        @Override
        public int hashCode() {
            return Utils.hashCode(this);
        }
    }

    public class Player {
        private String name;
        private String id;

        public Player(String name, String id) {
            this.name = Conditions.nonNullOrEmpty(name, "name");
            this.id = Conditions.nonNullOrEmpty(id, "id");
        }

        public Player() {}

        public Pinger.Player copy() {
            return new Player(name, id);
        }

        public String getName() {
            return this.name;
        }

        public String getId() {
            return this.id;
        }

        public void setName(String name) {
            this.name = Conditions.nonNullOrEmpty(name, "name");
        }

        public void setId(String id) {
            this.id = Conditions.nonNullOrEmpty(id, "id");
        }

        @Override
        public String toString() {
            return Utils.toString(this);
        }

        @Override
        public boolean equals(Object other) {
            return Utils.equals(this, other);
        }

        @Override
        public int hashCode() {
            return Utils.hashCode(this);
        }
    }

    public class Version {
        private String name;
        private int protocol;

        public Version(String name, int protocol) {
            this.name = Conditions.nonNullOrEmpty(name, "name");
            this.protocol = protocol;
        }

        public Version() {}

        public Pinger.Version copy() {
            return new Version(name, protocol);
        }

        public String getName() {
            return this.name;
        }

        public int getProtocol() {
            return this.protocol;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setProtocol(int protocol) {
            this.protocol = protocol;
        }

        @Override
        public String toString() {
            return Utils.toString(this);
        }

        @Override
        public boolean equals(Object other) {
            return Utils.equals(this, other);
        }

        @Override
        public int hashCode() {
            return Utils.hashCode(this);
        }
    }

    /** This class will deserialize Minecraft text object to a string since some servers use legacy string format */
    private static class TextDeserializer implements JsonDeserializer<String> {
        private static final String TEXT_KEY = "text";
        private static final String EXTRA_KEY = "extra";


        /** Simplistic enumeration of all supported color values. */
        public enum ChatColor {

            BLACK('0', "black"),
            DARK_BLUE('1', "dark_blue"),
            DARK_GREEN('2', "dark_green"),
            DARK_AQUA('3', "dark_aqua"),
            DARK_RED('4', "dark_red"),
            DARK_PURPLE('5', "dark_purple"),
            GOLD('6', "gold"),
            GRAY('7', "gray"),
            DARK_GRAY('8', "dark_gray"),
            BLUE('9', "blue"),
            GREEN('a', "green"),
            AQUA('b', "aqua"),
            RED('c', "red"),
            LIGHT_PURPLE('d', "light_purple"),
            YELLOW('e', "yellow"),
            WHITE('f', "white"),
            MAGIC('k', "obfuscated"),
            BOLD('l', "bold"),
            STRIKETHROUGH('m', "strikethrough"),
            UNDERLINE('n', "underline"),
            ITALIC('o', "italic"),
            RESET('r', "reset");

            /**
             * The special character which prefixes all chat colour codes. Use this if
             * you need to dynamically convert colour codes from your custom format.
             */
            public static final char COLOR_CHAR = '\u00A7';
            public static final String ALL_CODES = "0123456789AaBbCcDdEeFfKkLlMmNnOoRr";
            /** Pattern to remove all colour codes. */
            public static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + String.valueOf(COLOR_CHAR) + "[0-9A-FK-OR]");
            /** Colour instances keyed by their active character. */
            private static final Map<String, ChatColor> BY_NAME = new HashMap<>();

            static {
                for (ChatColor colour : values()) {
                    BY_NAME.put(colour.name, colour);
                }
            }

            /** The code appended to {@link #COLOR_CHAR} to make usable colour. */
            private final char code;
            /** This colour's colour char prefixed by the {@link #COLOR_CHAR}. */
            private final String toString;
            private final String name;

            ChatColor(char code, String name) {
                this.code = code;
                this.name = name;
                this.toString = new String(new char[]{
                    COLOR_CHAR, code
                });
            }

            /**
             * Get the colour represented by the specified code.
             *
             * @param name the name to search for
             * @return the mapped colour, or null if non exists
             */
            public static ChatColor getByName(String name) {
                return BY_NAME.get(name);
            }

            /** Get the name of the chat color */
            public String getName() {
                return this.name;
            }

            @Override
            public String toString() {
                return toString;
            }
        }

        /** Add the chat color and effects to the chat */
        public String deserializeChat(JsonObject chatObject) {
            String text = chatObject.get(TEXT_KEY).getAsString();
            if (chatObject.has("color")) {
                text = ChatColor.getByName(chatObject.get("color").getAsString()).toString() + text;
            }
            if (chatObject.has(ChatColor.BOLD.getName()) && chatObject.getAsJsonPrimitive(ChatColor.BOLD.getName()).getAsBoolean()) {
                text = ChatColor.BOLD.toString() + text;
            }
            if (chatObject.has(ChatColor.STRIKETHROUGH.getName()) && chatObject.getAsJsonPrimitive(ChatColor.STRIKETHROUGH.getName()).getAsBoolean()) {
                text = ChatColor.STRIKETHROUGH.toString() + text;
            }
            if (chatObject.has(ChatColor.UNDERLINE.getName()) && chatObject.getAsJsonPrimitive(ChatColor.UNDERLINE.getName()).getAsBoolean()) {
                text = ChatColor.UNDERLINE.toString() + text;
            }
            if (chatObject.has(ChatColor.ITALIC.getName()) && chatObject.getAsJsonPrimitive(ChatColor.ITALIC.getName()).getAsBoolean()) {
                text = ChatColor.ITALIC.toString() + text;
            }
            return text;
        }

        @Override
        public String deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json.isJsonObject()) {
                JsonObject textObject = json.getAsJsonObject();
                if (textObject.has(TEXT_KEY)) {
                    StringBuilder extras = new StringBuilder().append(deserializeChat(textObject));
                    if (textObject.has(EXTRA_KEY)) {
                        for (JsonElement element : textObject.get(EXTRA_KEY).getAsJsonArray()) {
                            extras.append(deserialize(element.getAsJsonObject(), typeOfT, context));
                        }
                    }
                    return extras.toString();
                }

            }
            return json.getAsString();
        }
    }
}
