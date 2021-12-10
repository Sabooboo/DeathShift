package com.sab.deathshift.tasks

import com.sab.deathshift.DeathShift
import com.sab.deathshift.managers.GameManager
import com.sab.deathshift.managers.PlayerManager
import org.bukkit.ChatColor
import org.bukkit.scheduler.BukkitRunnable

class Teleport(private val plugin: DeathShift) : BukkitRunnable() {
    override fun run() {
        GameManager.broadcastParticipants("${ChatColor.GREEN}${ChatColor.ITALIC}Teleporting!")

        for(i in GameManager.players.indices) {
            val manager = GameManager.players[i]

            var next: PlayerManager
            if (i == GameManager.players.size - 1) {
                next = GameManager.players[0]
            }
            else {
                next = GameManager.players[i + 1]
            }
            manager.destination = next.player.location
        }

        for(manager in GameManager.players) {
            manager.player.teleport(manager.destination!!)
        }
    }
}