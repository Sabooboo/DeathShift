package com.sab.deathshift.managers

import com.sab.deathshift.DeathShift
import org.bukkit.Bukkit

object ConfigManager {
    private var plugin: DeathShift = Bukkit.getPluginManager().getPlugin("DeathShift") as DeathShift

    init {
        plugin.config.addDefault("shift_time", 120)
        plugin.config.addDefault("countdown", 5)
        plugin.config.addDefault("random_teleport", false)
        plugin.saveDefaultConfig()
    }

    var shiftTime: Int
        get() = plugin.config.getInt("shift_time")
        set(value) {
            plugin.config.set("shift_time", value)
            plugin.saveConfig()
        }

    var countdown: Int
        get() = plugin.config.getInt("countdown")
        set(value) {
            plugin.config.set("countdown", value)
            plugin.saveConfig()
        }

    var randomTeleport: Boolean
        get() = plugin.config.getBoolean("random_teleport")
        set(value) {
            plugin.config.set("random_teleport", value)
            plugin.saveConfig()
        }

    fun get(path: String): Any? {
        return plugin.config.get(path)
    }

    fun set(path: String, value: Any) {
        plugin.config.set(path, value)
        plugin.saveConfig()
    }
}
