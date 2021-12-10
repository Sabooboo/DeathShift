package com.sab.deathshift

import com.sab.deathshift.managers.GameManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent


object Events : Listener {

    @EventHandler
    fun onPlayerQuit(e: PlayerQuitEvent) {
        if (GameManager.inLobby(e.player)) {
            GameManager.remove(e.player)
        }
    }


}