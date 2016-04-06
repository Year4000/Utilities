/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.net;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import net.year4000.utilities.Callback;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author zh32 <zh32 at zh32.de> modify by ewized to add lombok support,
 *         update the code to latest java standards, and various tweaks.
 */
@SuppressWarnings("unused")
public final class Pinger {
    public static final int TIME_OUT = (int) TimeUnit.SECONDS.toMillis(5);
    private static final Gson gson = new Gson();
    private InetSocketAddress host;
    private int timeout = TIME_OUT;

    @java.beans.ConstructorProperties({"host", "timeout"})
    public Pinger(InetSocketAddress host, int timeout) {
        this.host = host;
        this.timeout = timeout;
    }

    public Pinger() {
    }

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
        }
        catch (Exception error) {
            callback.callback(error);
        }
    }

    public StatusResponse fetchData() throws IOException {
        try (
            Socket socket = new Socket() {{
                setSoTimeout(TIME_OUT);
                connect(host, TIME_OUT);
            }};
            OutputStream outputStream = socket.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            InputStream inputStream = socket.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream handshake = new DataOutputStream(b);
            DataInputStream dataInputStream = new DataInputStream(inputStream)
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

            Preconditions.checkArgument(id != -1, "Premature end of stream.");

            //we want a status response
            Preconditions.checkArgument(id == 0x00, "Invalid packetID, expecting 0x00(" + 0x00 + ") but was " + id);

            // There was a netty header sent, off set everything by one
            if (one != size) {
                readVarInt(dataInputStream);
            }

            int length = readVarInt(dataInputStream); //length of json string

            Preconditions.checkArgument(length != -1, "Premature end of stream.");
            Preconditions.checkArgument(length != 0, "Invalid string length.");

            byte[] in = new byte[length];
            dataInputStream.readFully(in);  //read json string
            String json = new String(in);

            long now = System.currentTimeMillis();
            dataOutputStream.writeByte(0x09); //size of packet
            dataOutputStream.writeByte(0x01); //0x01 for ping
            dataOutputStream.writeLong(now); //time!?

            readVarInt(dataInputStream);
            id = readVarInt(dataInputStream);

            Preconditions.checkArgument(id != -1, "Premature end of stream.");

            Preconditions.checkArgument(id == 0x01, "Invalid packetID, expecting 0x01(" + 0x01 + ") but was " + id);

            long pingtime = dataInputStream.readLong(); //read response

            StatusResponse response = gson.fromJson(json, StatusResponse.class);
            response.setTime((int) pingtime);

            return response;
        }
        catch (IllegalStateException e) {
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

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Pinger)) return false;
        final Pinger other = (Pinger) o;
        final Object this$host = this.host;
        final Object other$host = other.host;
        if (this$host == null ? other$host != null : !this$host.equals(other$host)) return false;
        if (this.timeout != other.timeout) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $host = this.host;
        result = result * PRIME + ($host == null ? 0 : $host.hashCode());
        result = result * PRIME + this.timeout;
        return result;
    }

    public String toString() {
        return "net.year4000.utilities.net.Pinger(host=" + this.host + ", timeout=" + this.timeout + ")";
    }

    public class StatusResponse {
        private String description;
        private Players players;
        private Version version;
        private String favicon;
        private Integer time;

        @java.beans.ConstructorProperties({"description", "players", "version", "favicon", "time"})
        public StatusResponse(String description, Players players, Version version, String favicon, Integer time) {
            this.description = description;
            this.players = players;
            this.version = version;
            this.favicon = favicon;
            this.time = time;
        }

        public StatusResponse() {
        }

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
            this.description = description;
        }

        public void setPlayers(Players players) {
            this.players = players;
        }

        public void setVersion(Version version) {
            this.version = version;
        }

        public void setFavicon(String favicon) {
            this.favicon = favicon;
        }

        public void setTime(Integer time) {
            this.time = time;
        }

        public boolean equals(Object o) {
            if (o == this) return true;
            if (!(o instanceof StatusResponse)) return false;
            final StatusResponse other = (StatusResponse) o;
            if (!other.canEqual((Object) this)) return false;
            final Object this$description = this.description;
            final Object other$description = other.description;
            if (this$description == null ? other$description != null : !this$description.equals(other$description))
                return false;
            final Object this$players = this.players;
            final Object other$players = other.players;
            if (this$players == null ? other$players != null : !this$players.equals(other$players)) return false;
            final Object this$version = this.version;
            final Object other$version = other.version;
            if (this$version == null ? other$version != null : !this$version.equals(other$version)) return false;
            final Object this$favicon = this.favicon;
            final Object other$favicon = other.favicon;
            if (this$favicon == null ? other$favicon != null : !this$favicon.equals(other$favicon)) return false;
            final Object this$time = this.time;
            final Object other$time = other.time;
            if (this$time == null ? other$time != null : !this$time.equals(other$time)) return false;
            return true;
        }

        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            final Object $description = this.description;
            result = result * PRIME + ($description == null ? 0 : $description.hashCode());
            final Object $players = this.players;
            result = result * PRIME + ($players == null ? 0 : $players.hashCode());
            final Object $version = this.version;
            result = result * PRIME + ($version == null ? 0 : $version.hashCode());
            final Object $favicon = this.favicon;
            result = result * PRIME + ($favicon == null ? 0 : $favicon.hashCode());
            final Object $time = this.time;
            result = result * PRIME + ($time == null ? 0 : $time.hashCode());
            return result;
        }

        protected boolean canEqual(Object other) {
            return other instanceof StatusResponse;
        }

        public String toString() {
            return "net.year4000.utilities.net.Pinger.StatusResponse(description=" + this.description + ", players=" + this.players + ", version=" + this.version + ", favicon=" + this.favicon + ", time=" + this.time + ")";
        }
    }

    public class Players {
        private Integer max;
        private Integer online;
        private List<Player> sample;

        @java.beans.ConstructorProperties({"max", "online", "sample"})
        public Players(Integer max, Integer online, List<Player> sample) {
            this.max = max;
            this.online = online;
            this.sample = sample;
        }

        public Players() {
        }

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

        public Integer getMax() {
            return this.max;
        }

        public Integer getOnline() {
            return this.online;
        }

        public List<Player> getSample() {
            return this.sample;
        }

        public void setMax(Integer max) {
            this.max = max;
        }

        public void setOnline(Integer online) {
            this.online = online;
        }

        public void setSample(List<Player> sample) {
            this.sample = sample;
        }

        public boolean equals(Object o) {
            if (o == this) return true;
            if (!(o instanceof Players)) return false;
            final Players other = (Players) o;
            if (!other.canEqual((Object) this)) return false;
            final Object this$max = this.max;
            final Object other$max = other.max;
            if (this$max == null ? other$max != null : !this$max.equals(other$max)) return false;
            final Object this$online = this.online;
            final Object other$online = other.online;
            if (this$online == null ? other$online != null : !this$online.equals(other$online)) return false;
            final Object this$sample = this.sample;
            final Object other$sample = other.sample;
            if (this$sample == null ? other$sample != null : !this$sample.equals(other$sample)) return false;
            return true;
        }

        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            final Object $max = this.max;
            result = result * PRIME + ($max == null ? 0 : $max.hashCode());
            final Object $online = this.online;
            result = result * PRIME + ($online == null ? 0 : $online.hashCode());
            final Object $sample = this.sample;
            result = result * PRIME + ($sample == null ? 0 : $sample.hashCode());
            return result;
        }

        protected boolean canEqual(Object other) {
            return other instanceof Players;
        }

        public String toString() {
            return "net.year4000.utilities.net.Pinger.Players(max=" + this.max + ", online=" + this.online + ", sample=" + this.sample + ")";
        }
    }

    public class Player {
        private String name;
        private String id;

        @java.beans.ConstructorProperties({"name", "id"})
        public Player(String name, String id) {
            this.name = name;
            this.id = id;
        }

        public Player() {
        }

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
            this.name = name;
        }

        public void setId(String id) {
            this.id = id;
        }

        public boolean equals(Object o) {
            if (o == this) return true;
            if (!(o instanceof Player)) return false;
            final Player other = (Player) o;
            if (!other.canEqual((Object) this)) return false;
            final Object this$name = this.name;
            final Object other$name = other.name;
            if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
            final Object this$id = this.id;
            final Object other$id = other.id;
            if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
            return true;
        }

        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            final Object $name = this.name;
            result = result * PRIME + ($name == null ? 0 : $name.hashCode());
            final Object $id = this.id;
            result = result * PRIME + ($id == null ? 0 : $id.hashCode());
            return result;
        }

        protected boolean canEqual(Object other) {
            return other instanceof Player;
        }

        public String toString() {
            return "net.year4000.utilities.net.Pinger.Player(name=" + this.name + ", id=" + this.id + ")";
        }
    }

    public class Version {
        private String name;
        private String protocol;

        @java.beans.ConstructorProperties({"name", "protocol"})
        public Version(String name, String protocol) {
            this.name = name;
            this.protocol = protocol;
        }

        public Version() {
        }

        public Pinger.Version copy() {
            return new Version(name, protocol);
        }

        public String getName() {
            return this.name;
        }

        public String getProtocol() {
            return this.protocol;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setProtocol(String protocol) {
            this.protocol = protocol;
        }

        public boolean equals(Object o) {
            if (o == this) return true;
            if (!(o instanceof Version)) return false;
            final Version other = (Version) o;
            if (!other.canEqual((Object) this)) return false;
            final Object this$name = this.name;
            final Object other$name = other.name;
            if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
            final Object this$protocol = this.protocol;
            final Object other$protocol = other.protocol;
            if (this$protocol == null ? other$protocol != null : !this$protocol.equals(other$protocol)) return false;
            return true;
        }

        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            final Object $name = this.name;
            result = result * PRIME + ($name == null ? 0 : $name.hashCode());
            final Object $protocol = this.protocol;
            result = result * PRIME + ($protocol == null ? 0 : $protocol.hashCode());
            return result;
        }

        protected boolean canEqual(Object other) {
            return other instanceof Version;
        }

        public String toString() {
            return "net.year4000.utilities.net.Pinger.Version(name=" + this.name + ", protocol=" + this.protocol + ")";
        }
    }
}