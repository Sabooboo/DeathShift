package com.sab.deathshift.managers

import com.sab.deathshift.DeathShift
import org.bukkit.Bukkit

object ConfigManager {
    private var plugin: DeathShift = Bukkit.getPluginManager().getPlugin("DeathShift") as DeathShift

    init {
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

    var warnTime: Int
        get() = plugin.config.getInt("warn_time")
        set(value) {
            plugin.config.set("warn_time", value)
            plugin.saveConfig()
        }

    var warnHalf: Boolean
        get() = plugin.config.getBoolean("warn_half")
        set(value) {
            plugin.config.set("warn_half", value)
            plugin.saveConfig()
        }

    var randomTeleport: Boolean
        get() = plugin.config.getBoolean("random_teleport")
        set(value) {
            plugin.config.set("random_teleport", value)
            plugin.saveConfig()
        }

    var knowNextTarget: Boolean
        get() = plugin.config.getBoolean("know_next_target")
        set(value) {
            plugin.config.set("know_next_target", value)
            plugin.saveConfig()
        }

    var randomInitialTeleport: Boolean
        get() = plugin.config.getBoolean("random_initial_teleport")
        set(value) {
            plugin.config.set("random_initial_teleport", value)
            plugin.saveConfig()
        }

    fun get(path: String): Any? {
        return plugin.config.get(path)
    }

    /**
     * Sets an existing variable at path to the specified value.
     * This performs no type validation, so passing in user input may set properties to strings.
     * @param path the name of the variable in the config being changed.
     * @param value the value to change the variable at path to.
     *
     */
    fun set(path: String, value: Any) {
        if (get(path) == null) throw Exception("path does not exist in config")
        plugin.config.set(path, value)
        plugin.saveConfig()
    }
}
