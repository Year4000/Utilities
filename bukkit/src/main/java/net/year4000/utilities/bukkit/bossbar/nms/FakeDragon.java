package net.year4000.utilities.bukkit.bossbar.nms;

import lombok.Data;
import net.year4000.utilities.bukkit.bossbar.BarUtil;
import org.bukkit.Location;

@Data
public abstract class FakeDragon {
    public static final float MAX_HEALTH = 300;
    private int x;
    private int y;
    private int z;

    private int pitch = 0;
    private int yaw = 0;
    private byte xvel = 0;
    private byte yvel = 0;
    private byte zvel = 0;
    public float health = 0;
    private boolean visible = true;
    public String name;
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

    public abstract Object getEffectPacket();

    public abstract Object getDestroyPacket();

    public abstract Object getMetaPacket(Object watcher);

    public abstract Object getTeleportPacket(Location loc);

    public abstract Object getWatcher();
}
