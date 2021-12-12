package com.sab.deathshift.managers

import com.sab.deathshift.DeathShift
import com.sab.deathshift.tasks.StartTimer
import com.sab.deathshift.tasks.Teleport
import com.sab.deathshift.utilities.Broadcast
import com.sab.deathshift.utilities.Logger
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import java.lang.IllegalStateException

object GameManager {
    const val TELEPORT_DELAY: Long = 20 * 60 / 6 // 500 ticks * 60s = 1m, 1m/6 = 10s

    private var plugin: DeathShift = Bukkit.getPluginManager().getPlugin("DeathShift") as DeathShift
    private var startTimer: BukkitRunnable = StartTimer(plugin)
    private var teleport: BukkitRunnable = Teleport(plugin)
    var players = mutableListOf<PlayerManager>()
        private set
    var starting = false
        private set
    var inProgress: Boolean = false
        private set

    fun start() {
        if (inProgress) return
        inProgress = true
        Broadcast.all("${ChatColor.GREEN}${ChatColor.ITALIC}Game starting!")
        for (manager in players) {
            manager.playing = true
            manager.player.teleport(manager.destination)
        }
        teleport.runTaskTimer(plugin, TELEPORT_DELAY, TELEPORT_DELAY)
    }

    private fun startQueue() {
        if (starting) return
        starting = true
        startTimer.runTaskTimer(plugin,20, 20)
    }

    fun stop() {
        teleport.cancel()
        teleport = Teleport(plugin)
        if (players.size == 1) {
            Broadcast.all("${ChatColor.GOLD}${ChatColor.BOLD}${players[0].player.name.uppercase()} WINS!")
        }
        else {
            Broadcast.all("${ChatColor.YELLOW}The game has been stopped!")
        }
        players = mutableListOf()
        inProgress = false
    }

    private fun stopQueue() {
        if (!starting) return
        starting = false
        startTimer.cancel()
        startTimer = StartTimer(plugin) // this doesn't seem like industry-standard practise...
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

    /**
     *  Notifies the GameManager that a player is ready or not.
     *  This will tell all participants the players ready status
     *  if it has changed. If all players are ready, and there
     *  are 2 or more players in the lobby, the game will start.
     *
     *  @param manager the PlayerManager whose ready status is being
     *  notified.
     */
    fun notifyReady(manager: PlayerManager) {
        if (!inLobby(manager.player) || inProgress) return
        // I suppose this is sloppy but this is done under the pretense that
        // when the game ends, the players list will be emptied and every
        // PlayerManager will be deleted anyway, so this doesn't care
        // about who is ready or not when game is in progress.
        // Thanks for coming to my TED Talk.

        broadcastReady(manager)
        if (!inProgress && manager.ready && (countReady() == players.size) && countReady() > 1) {
            startQueue()
            return
        }
        stopQueue()
    }

    fun add(player: Player) {
        if (inLobby(player) || inProgress) return
        players.add(PlayerManager(player))
        Broadcast.participants("${ChatColor.GREEN}${ChatColor.ITALIC}${player.name} has joined DeathShift!")
        stopQueue()
    }

    fun remove(player: Player) {
        if (!inLobby(player)) return
        broadcastLeave(player)
        get(player)?.playing = false
        players.remove(get(player))

        if (!inProgress) {
            for (manager in players) {
                manager.ready = false
            }
        }

        if (inProgress && players.size == 1) {
            stop()
            return
        }
    }

    private fun broadcastReady(manager: PlayerManager) {
        if (manager.ready) {
            Broadcast.participants(
                "${ChatColor.YELLOW}${manager.player.name} is ready! (${countReady()}/${players.size})"
            )
        } else {
            Broadcast.participants(
                "${ChatColor.YELLOW}${manager.player.name} is no longer ready! (${countReady()}/${players.size})"
            )
        }
    }

    private fun broadcastLeave(player: Player) {
        if (inProgress) {
            Broadcast.participants("${ChatColor.RED}${ChatColor.BOLD}${player.name} has been eliminated!")
            return
        }
        Broadcast.participants("${ChatColor.RED}${ChatColor.ITALIC}${player.name} has left DeathShift!")
    }
}