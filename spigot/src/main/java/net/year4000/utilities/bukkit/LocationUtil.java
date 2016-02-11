/*
 * Copyright 2015 Year4000.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package net.year4000.utilities.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

@SuppressWarnings("unused")
public final class LocationUtil {
    private LocationUtil() {}

    /**
     * Create a location
     * @param world The world.
     * @param x The x cord.
     * @param y The y cord.
     * @param z The z cord.
     * @return Location
     */
    public static Location create(World world, int x, int y, int z) {
        return create(world, (double) x, (double) y, (double) z);
    }

    /**
     * Create a location
     * @param world The world.
     * @param x The x cord.
     * @param y The y cord.
     * @param z The z cord.
     * @return Location
     */
    public static Location create(World world, double x, double y, double z) {
        return new Location(world, x, y, z);
    }

    /**
     * Make a location centered
     * @param location The location to make centered.
     * @return The centered location.
     */
    public static Location center(Location location) {
        return create(
            location.getWorld(),
            (int)location.getX() + 0.5,
            (int)location.getY() + 0.5,
            (int)location.getZ() + 0.5
        );
    }

    /**
     * Make a location centered
     * @param block The location to make centered.
     * @return The centered location.
     */
    public static Location center(Block block) {
        return center(block.getLocation());
    }

    /**
     * Parse a string to a given location.
     * @param world The world.
     * @param loc The string to parse.
     * @param regex The split regex key.
     * @return Parsed location.
     */
    public static Location parseLocation(World world, String loc, String regex) {
        String[] cords = loc.split(regex);
        return create(
            world,
            Double.valueOf(cords[0]),
            Double.valueOf(cords[1]),
            Double.valueOf(cords[2])
        );
    }

    /**
     * Parse a string to a given location.
     * @param world The world.
     * @param loc The string to parse.
     * @return Parsed location.
     */
    public static Location parseLocation(World world, String loc) {
        return parseLocation(world, loc, ":");
    }

    /**
     * Parse a string to a given location.
     * @param loc The string to parse.
     * @param regex The split regex key.
     * @return Parsed location.
     */
    public static Location parseLocation(String loc, String regex) {
        String[] cords = loc.split(regex);
        return parseLocation(Bukkit.getWorld(cords[0]), cords[1]);
    }

    /**
     * Parse a string to a given location.
     * @param loc The string to parse.
     * @return Parsed location.
     */
    public static Location parseLocation(String loc) {
        return parseLocation(loc, "\\|");
    }
}
