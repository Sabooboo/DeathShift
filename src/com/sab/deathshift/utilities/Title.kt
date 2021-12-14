package com.sab.deathshift.utilities

import com.sab.deathshift.managers.GameManager
import org.bukkit.Bukkit


object Title {
    fun participants(title: String) {
        val online = Bukkit.getOnlinePlayers()
        for (p in online) {
            if (GameManager.inLobby(p)) {
                p.sendTitle(title, "", 5, 20, 5)
            }
        }
    }
}