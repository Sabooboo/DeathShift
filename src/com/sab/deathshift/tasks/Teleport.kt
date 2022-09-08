package com.sab.deathshift.tasks

import com.sab.deathshift.DeathShift
import com.sab.deathshift.managers.ConfigManager
import com.sab.deathshift.managers.GameManager
import com.sab.deathshift.utilities.Broadcast
import org.bukkit.ChatColor
import org.bukkit.scheduler.BukkitRunnable

class Teleport(private val plugin: DeathShift) : BukkitRunnable() {
    override fun run() {
        Broadcast.all("${ChatColor.GREEN}${ChatColor.ITALIC}Teleporting!")

        var indices = GameManager.players.indices.toList()
        if (ConfigManager.randomTeleport) indices = indices.shuffled()


        for (i in GameManager.players.indices) {
            val manager = GameManager.players[indices[i]]

            val next = if (i == indices.size - 1) {
                GameManager.players[indices[0]]
            } else {
                GameManager.players[indices[i + 1]]
            }
            manager.destination = next.player.location
        }

        for (i in GameManager.players.indices) {
            val manager = GameManager.players[indices[i]]
            manager.player.teleport(manager.destination)

            if (ConfigManager.knowNextTarget) {
                manager.player.sendMessage(
                    "${ChatColor.GRAY}${ChatColor.ITALIC}You've teleported to ${
                        if (i == indices.size - 1) GameManager.players[indices[0]].player.name
                        else GameManager.players[indices[i + 1]].player.name
                    }"
                )
            }
        }
    }
}
