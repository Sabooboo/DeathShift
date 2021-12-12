package com.sab.deathshift.tasks

import com.sab.deathshift.DeathShift
import com.sab.deathshift.managers.GameManager
import com.sab.deathshift.managers.PlayerManager
import com.sab.deathshift.utilities.Broadcast
import org.bukkit.ChatColor
import org.bukkit.scheduler.BukkitRunnable

/*
 * TODO:
 *  I want the players to see a countdown until the next teleport.
 *  Something that alert them when there is 3/4, 1/2, 1/4, etc.,
 *  and then every second until the teleport occurs. I want this so
 *  I can change the time between teleports in a config or something
 *  and not have to worry about how the code may break in the meantime.
 *  The problem is, I don't know exactly how to make a countdown like this
 *  in a non-blocking way. Shut I be scheduling a different Runnable
 *  for every second that does these checks and then only triggers Teleport
 *  if timeLeft == 0?
 */

class Teleport(private val plugin: DeathShift) : BukkitRunnable() {
    override fun run() {
        Broadcast.participants("${ChatColor.GREEN}${ChatColor.ITALIC}Teleporting!")

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