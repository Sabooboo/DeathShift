package com.sab.deathshift

import com.sab.deathshift.managers.ConfigManager
import com.sab.deathshift.managers.GameManager
import com.sab.deathshift.managers.GameState
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerPortalEvent
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
    fun onPlayerDamage(e: EntityDamageEvent) {
        if (e.entity !is Player) return
        
        val player = e.entity as Player
        if (GameManager.inLobby(player) && (GameManager.state != GameState.PLAYING)) return
        if (player.health - e.damage >= 1) return
        e.isCancelled = true
        GameManager.remove(player)
    }

    @EventHandler
    fun onPlayerRespawn(e: PlayerRespawnEvent) {
        if (GameManager.inLobby(e.player) && GameManager.state == GameState.PLAYING) {
            GameManager.remove(e.player)
        }
    }

    @EventHandler
    fun onPortal(e: PlayerPortalEvent) {
        if (!ConfigManager.portalsEnabled) {
            e.isCancelled = true
            e.canCreatePortal = false
            e.player.sendMessage("${ChatColor.GRAY}${ChatColor.ITALIC}Portals are disabled!")
        }
    }
}
