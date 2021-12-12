package com.sab.deathshift.utilities

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.block.BlockFace

object LocationTools {
    /**
     * Checks whether a Location is safe or not
     * @param location the location to check
     * @return true if location is safe, otherwise false.
     * @author TheCyberCode on spigot forums, converted to Kotlin, slightly modified
     * https://www.spigotmc.org/threads/determine-if-an-area-is-safe-to-teleport-to.364421/
     */
    fun isSafe(location: Location): Boolean {
        val feet = location.block
        if (feet.type.isOccluding && feet.location.add(0.0, 1.0, 0.0).block.type.isOccluding) {
            return false
        }
        val head = feet.getRelative(BlockFace.UP)
        if (head.type.isOccluding) {
            return false
        }
        val ground = feet.getRelative(BlockFace.DOWN)
        return ground.type.isSolid
    }

    fun generateRandomLocation(): Location {
        var x = (-2_000_000..2_000_000).random()
        var z = (-2_000_000..2_000_000).random()
        var location = Location(
            Bukkit.getWorld("world")!!,
            x.toDouble(),
            Bukkit.getWorld("world")!!.getHighestBlockAt(x, z).y.toDouble() + 1,
            z.toDouble()
        )
        return if (isSafe(location)) location else generateRandomLocation()
    }
}