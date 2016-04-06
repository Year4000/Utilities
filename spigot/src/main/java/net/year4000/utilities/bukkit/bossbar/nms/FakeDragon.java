package net.year4000.utilities.bukkit.bossbar.nms;

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

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof FakeDragon)) return false;
        final FakeDragon other = (FakeDragon) o;
        if (!other.canEqual((Object) this)) return false;
        if (Float.compare(this.health, other.health) != 0) return false;
        final Object this$name = this.name;
        final Object other$name = other.name;
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        if (this.x != other.x) return false;
        if (this.y != other.y) return false;
        if (this.z != other.z) return false;
        if (this.pitch != other.pitch) return false;
        if (this.yaw != other.yaw) return false;
        if (this.xvel != other.xvel) return false;
        if (this.yvel != other.yvel) return false;
        if (this.zvel != other.zvel) return false;
        if (this.visible != other.visible) return false;
        final Object this$world = this.world;
        final Object other$world = other.world;
        if (this$world == null ? other$world != null : !this$world.equals(other$world)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + Float.floatToIntBits(this.health);
        final Object $name = this.name;
        result = result * PRIME + ($name == null ? 0 : $name.hashCode());
        result = result * PRIME + this.x;
        result = result * PRIME + this.y;
        result = result * PRIME + this.z;
        result = result * PRIME + this.pitch;
        result = result * PRIME + this.yaw;
        result = result * PRIME + this.xvel;
        result = result * PRIME + this.yvel;
        result = result * PRIME + this.zvel;
        result = result * PRIME + (this.visible ? 79 : 97);
        final Object $world = this.world;
        result = result * PRIME + ($world == null ? 0 : $world.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof FakeDragon;
    }

    public String toString() {
        return "net.year4000.utilities.bukkit.bossbar.nms.FakeDragon(health=" + this.health + ", name=" + this.name + ", x=" + this.x + ", y=" + this.y + ", z=" + this.z + ", pitch=" + this.pitch + ", yaw=" + this.yaw + ", xvel=" + this.xvel + ", yvel=" + this.yvel + ", zvel=" + this.zvel + ", visible=" + this.visible + ", world=" + this.world + ")";
    }
}
