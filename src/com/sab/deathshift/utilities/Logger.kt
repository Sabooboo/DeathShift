package com.sab.deathshift.utilities

import org.bukkit.Bukkit.getServer

object Logger {

    fun print(message: String) {
        getServer().consoleSender.sendMessage("[DeathShift] $message")
    }

    fun print() {
        getServer().consoleSender.sendMessage("[DeathShift] ")
    }
}
