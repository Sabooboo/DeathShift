package com.sab.deathshift.utilities

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.block.BlockFace
import kotlin.random.Random

object LocationTools {

    // originally stole from https://www.spigotmc.org/threads/determine-if-an-area-is-safe-to-teleport-to.364421/
    // but so heavily butchered that I feel like it doesn't matter.
    /**
     * Calculates whether a location is safe assuming the location's y is the highest block at x and z.
     */
    private fun isSafe(location: Location): Boolean {
        return location.block.getRelative(BlockFace.DOWN).type.isSolid
    }
    
    fun generateRandomLocation(): Location {
        var isSafe = false
        lateinit var location: Location
        while (!isSafe) {
            var x = Random.nextInt(-2_000_000, 2_000_000).toDouble()
            var z = Random.nextInt(-2_000_000, 2_000_000).toDouble()
            // Finding y takes a painfully long time on first pass.
            var y = Bukkit.getWorld("world")!!.getHighestBlockYAt(x.toInt(), z.toInt()).toDouble()
            location = Location(Bukkit.getWorld("world")!!, x, y, z)

            isSafe = isSafe(location)
        }
        return location.add(0.0, 1.0, 0.0)
    }
}
