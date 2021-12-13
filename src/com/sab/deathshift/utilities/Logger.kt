package com.sab.deathshift.utilities

import org.bukkit.Bukkit.getServer
import org.bukkit.ChatColor
import org.bukkit.plugin.java.JavaPlugin

object Logger {

    fun print(message: String) {
        getServer().consoleSender.sendMessage("[DeathShift] $message");
    }

    fun print() {
        getServer().consoleSender.sendMessage("[DeathShift] ");
    }
}
