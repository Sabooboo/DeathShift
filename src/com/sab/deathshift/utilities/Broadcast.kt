package com.sab.deathshift.utilities

import com.sab.deathshift.managers.GameManager
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object Broadcast {
    fun participants(message: String) {
        val online = Bukkit.getOnlinePlayers()
        for (p in online) {
            if (GameManager.inLobby(p)) {
                p.sendMessage(message)
            }
        }
    }

    fun all(message: String) {
        Bukkit.getOnlinePlayers().forEach { p: Player ->
            p.sendMessage(message)
        }
    }
}
