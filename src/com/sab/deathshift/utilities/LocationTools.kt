package com.sab.deathshift.utilities

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.block.BlockFace
import kotlin.random.Random

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
        val ground = feet.getRelative(BlockFace.DOWN)
        return ground.type.isSolid
    }
    
    fun generateRandomLocation(): Location {
        var isSafe = false
        lateinit var location: Location
        while (!isSafe) {
            var x = Random.nextDouble(-1.0, 1.0) * 2_000_000
            var z = Random.nextDouble(-1.0, 1.0) * 2_000_000
            Broadcast.all("$x, $z") // debug
            location = Location(
                Bukkit.getWorld("world")!!,
                x,
                Bukkit.getWorld("world")!!.getHighestBlockAt(x.toInt(), z.toInt()).y.toDouble(),
                z
            )
            isSafe = isSafe(location)
        }
        return location.add(0.0, 1.0, 0.0)
    }
}
