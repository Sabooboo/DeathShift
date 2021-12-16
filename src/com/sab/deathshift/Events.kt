package com.sab.deathshift

import com.sab.deathshift.managers.GameManager
import com.sab.deathshift.managers.GameState
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerRespawnEvent


object Events : Listener {

    @EventHandler
    fun onPlayerQuit(e: PlayerQuitEvent) {
        if (GameManager.inLobby(e.player)) {
            GameManager.remove(e.player)
        }
    }

    @EventHandler
    fun onPlayerDeath(e: PlayerDeathEvent) {
        if (GameManager.inLobby(e.entity) && (GameManager.state != GameState.STOPPED)) {
            GameManager.remove(e.entity)
        }
    }

    @EventHandler
    fun onPlayerRespawn(e: PlayerRespawnEvent) {
        if (GameManager.inLobby(e.player) && GameManager.state == GameState.PLAYING) {
            GameManager.remove(e.player)
        }
    }
}
