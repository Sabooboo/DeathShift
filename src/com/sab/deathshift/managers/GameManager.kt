package com.sab.deathshift.managers

import com.sab.deathshift.DeathShift
import com.sab.deathshift.tasks.Teleport
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable


object GameManager {
    private const val TELEPORT_DELAY: Long = 20 * 60 / 6 // 500 ticks * 60s = 1m, 1m/6 = 10s

    private var plugin: DeathShift = Bukkit.getPluginManager().getPlugin("DeathShift") as DeathShift
    var players: MutableList<PlayerManager> = mutableListOf<PlayerManager>()
    private var teleport: BukkitRunnable = Teleport(plugin)

    var inProgress: Boolean = false
        private set(value) {
            val old = field
            if (old == value) return
            if (value) {
                broadcastAll("${ChatColor.GREEN}${ChatColor.ITALIC}Game starting!")
                /* TODO: fix bug where after a game has already completed, starting a new one breaks things.
                 * The way I replicate this is by starting a game, ending it via /leave, and then
                 * having one player join and ready up, and then the other join and ready up.
                 * When the last player readies up (starting the game) an error happens right after
                 * the "Game starting!" broadcasts.
                 *
                 * I have ideas about players being some weird nully monstrosity, but as much as I want
                 * to test, it is almost 6 AM and I want to get some sleep tonight...
                 */
                for (manager in players) {
                    manager.playing = true
                    manager.player.teleport(manager.destination!!)
                }
                teleport.runTaskTimer(plugin, TELEPORT_DELAY, TELEPORT_DELAY) // wait 1s, run every 1 min/6 = 10s
            }
            else {
                teleport.cancel()
                broadcastAll("${ChatColor.GOLD}${ChatColor.BOLD}${players[0].player.name.uppercase()} WINS!")
                players = mutableListOf<PlayerManager>()
            }
            field = value
        }

    fun inLobby(player: Player): Boolean {
        return get(player) != null
    }

    fun get(player: Player): PlayerManager? {
        for(playerManager in players) {
            if (player.uniqueId == playerManager.player.uniqueId) return playerManager
        }
        return null
    }

    private fun countReady(): Int {
        var readyAmount = 0
        for (playerManager in players) {
            if (playerManager.ready) readyAmount++
        }
        return readyAmount
    }

    fun notifyReady(manager: PlayerManager) {
        if (!inLobby(manager.player) && inProgress) return
        // I suppose this is sloppy but this is done under the pretense that
        // when the game ends, the players list will be emptied and every
        // PlayerManager will be deleted anyway, so this doesn't care
        // about who is ready or not when game is in progress.
        // Thanks for coming to my TED Talk.

        broadcastReady(manager)
        if (!inProgress && manager.ready && (countReady() == players.size) && countReady() > 1) {
            inProgress = true
        }
    }

    private fun broadcastReady(manager: PlayerManager) {
        if (manager.ready) {
            broadcastParticipants(
                "${ChatColor.YELLOW}${manager.player.name} is ready! (${countReady()}/${players.size})"
            )
        } else {
            broadcastParticipants(
                "${ChatColor.YELLOW}${manager.player.name} is no longer ready! (${countReady()}/${players.size})"
            )
        }
    }

    fun add(player: Player) {
        if (!inLobby(player) && !inProgress) {
            players.add(PlayerManager(player))
            broadcastParticipants("${ChatColor.GREEN}${ChatColor.ITALIC}${player.name} has joined!")
        }
    }

    fun remove(player: Player) {
        if (inLobby(player)) {
            broadcastLeave(player)
            players.remove(get(player))
            if (inProgress && players.size == 1) {
                inProgress = false
            }
        }
    }

    private fun broadcastLeave(player: Player) {
        if (inProgress) {
            broadcastParticipants("${ChatColor.RED}${ChatColor.BOLD}${player.name} has been eliminated!")
        } else {
            broadcastParticipants("${ChatColor.RED}${ChatColor.ITALIC}${player.name} has left!")
        }
    }

    fun broadcastParticipants(message: String) {
        val online = Bukkit.getOnlinePlayers()
        for (p in online) {
            if (inLobby(p)) {
                p.sendMessage(message)
            }
        }
    }

    private fun broadcastAll(message: String) {
        Bukkit.getOnlinePlayers().forEach {
            p: Player -> p.sendMessage(message)
        }
    }
}