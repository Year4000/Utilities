/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.mc;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("unused")
public final class Pinger {
    public static final int TIME_OUT = (int) TimeUnit.SECONDS.toMillis(5);
    private static final Gson gson = new Gson();
    private InetSocketAddress host;
    private int timeout = TIME_OUT;

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
            callback.callback(fetchData(), null);
        }
        catch (Exception e) {
            callback.callback(null, e);
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

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class StatusResponse {
        private String description;
        private Players players;
        private Version version;
        private String favicon;
        private Integer time;

        public Pinger.StatusResponse copy() {
            Pinger.StatusResponse copy = new StatusResponse();

            copy.setDescription(description);
            copy.setFavicon(favicon);
            copy.setTime(time);
            copy.setPlayers(players.copy());
            copy.setVersion(version.copy());

            return copy;
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class Players {
        private Integer max;
        private Integer online;
        private List<Player> sample;

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
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class Player {
        private String name;
        private String id;

        public Pinger.Player copy() {
            return new Player(name, id);
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class Version {
        private String name;
        private String protocol;

        public Pinger.Version copy() {
            return new Version(name, protocol);
        }
    }
}