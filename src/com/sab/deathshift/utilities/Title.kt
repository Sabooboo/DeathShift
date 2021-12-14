package com.sab.deathshift.utilities

import com.sab.deathshift.managers.GameManager
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import sun.audio.AudioPlayer
import java.awt.SystemColor.text
import java.util.*


object Title {
    fun participants(title: String, subtitle: String, fadeIn: Int, stay: Int, fadeOut: Int) {
        val online = Bukkit.getOnlinePlayers()
        for (p in online) {
            if (GameManager.inLobby(p)) {
                p.sendTitle(title, subtitle, fadeIn, stay, fadeOut)
            }
        }
    }

    fun participants(title: String, subtitle: String) {
        participants(title, subtitle, 5, 20, 5)
    }

    fun participants(title: String) {
        participants(title, "")
    }
}