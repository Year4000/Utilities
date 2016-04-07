package net.year4000.utilities.bukkit.bossbar.nms;

import net.year4000.utilities.ObjectHelper;
import net.year4000.utilities.bukkit.bossbar.BarUtil;
import org.bukkit.Location;

public abstract class FakeDragon {
    public static final float MAX_HEALTH = 200;
    public float health = 0;
    public String name;
    private int x;
    private int y;
    private int z;
    private int pitch = 0;
    private int yaw = 0;
    private byte xvel = 0;
    private byte yvel = 0;
    private byte zvel = 0;
    private boolean visible = true;
    private Object world;

    public FakeDragon(String name, Location loc, int percent) {
        this.name = name;
        this.x = loc.getBlockX();
        this.y = loc.getBlockY();
        this.z = loc.getBlockZ();
        this.health = percent / 100F * MAX_HEALTH;
        this.world = BarUtil.getHandle(loc.getWorld());
    }

    public FakeDragon(String name, Location loc) {
        this.name = name;
        this.x = loc.getBlockX();
        this.y = loc.getBlockY();
        this.z = loc.getBlockZ();
        this.world = BarUtil.getHandle(loc.getWorld());
    }

    public float getMaxHealth() {
        return MAX_HEALTH;
    }

    public void setHealth(int percent) {
        this.health = percent / 100F * MAX_HEALTH;
    }

    public abstract Object getSpawnPacket();

    public abstract Object getDestroyPacket();

    public abstract Object getMetaPacket(Object watcher);

    public abstract Object getTeleportPacket(Location loc);

    public abstract Object getWatcher();

    public float getHealth() {
        return this.health;
    }

    public String getName() {
        return this.name;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }

    public int getPitch() {
        return this.pitch;
    }

    public int getYaw() {
        return this.yaw;
    }

    public byte getXvel() {
        return this.xvel;
    }

    public byte getYvel() {
        return this.yvel;
    }

    public byte getZvel() {
        return this.zvel;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public Object getWorld() {
        return this.world;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public void setPitch(int pitch) {
        this.pitch = pitch;
    }

    public void setYaw(int yaw) {
        this.yaw = yaw;
    }

    public void setXvel(byte xvel) {
        this.xvel = xvel;
    }

    public void setYvel(byte yvel) {
        this.yvel = yvel;
    }

    public void setZvel(byte zvel) {
        this.zvel = zvel;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setWorld(Object world) {
        this.world = world;
    }

    @Override
    public String toString() {
        return ObjectHelper.toString(this);
    }

    @Override
    public boolean equals(Object other) {
        return ObjectHelper.equals(this, other);
    }

    @Override
    public int hashCode() {
        return ObjectHelper.hashCode(this);
    }
}
