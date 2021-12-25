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
    
    fun generateRandomLocation(min: Int, max: Int): Location {
        var isSafe = false
        lateinit var location: Location
        while (!isSafe) {
            val x = Random.nextInt(min, max).toDouble()
            val z = Random.nextInt(min, max).toDouble()
            // Finding y takes a painfully long time on first pass.
            val y = Bukkit.getWorld("world")!!.getHighestBlockYAt(x.toInt(), z.toInt()).toDouble()
            location = Location(Bukkit.getWorld("world")!!, x, y, z)
            // Logger.print("$x, $y, $z")
            // Logger.print("min: $min max: $max")
            // Logger.print("${x >= min && x <= max && z >= min && z <= max}")
            isSafe = isSafe(location)
        }
        return location.add(0.5, 1.0, 0.5)
    }
}
