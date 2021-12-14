package com.sab.deathshift.managers

import com.sab.deathshift.DeathShift
import com.sab.deathshift.tasks.StartTimer
import com.sab.deathshift.tasks.TeleportTimer
import com.sab.deathshift.utilities.Broadcast
import com.sab.deathshift.utilities.GameSound
import com.sab.deathshift.utilities.SoundUtil
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

object GameManager {
    private var plugin: DeathShift = Bukkit.getPluginManager().getPlugin("DeathShift") as DeathShift
    private lateinit var startTimer: BukkitRunnable
    private lateinit var teleportTimer: BukkitRunnable

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
            manager.player.gameMode = GameMode.SURVIVAL
            manager.player.inventory.clear();
            manager.player.health = 20.toDouble()
            manager.player.saturation = 20.toFloat()
        }
        teleportTimer = TeleportTimer(plugin)
        teleportTimer.runTaskTimer(plugin, 20, 20)
    }

    private fun startQueue() {
        if (starting) return
        starting = true
        startTimer = StartTimer(plugin)
        startTimer.runTaskTimer(plugin,20, 20)
    }

    fun stop() {
        if (!inProgress) return
        teleportTimer.cancel()
        if (players.size == 1) {
            Broadcast.all("${ChatColor.GOLD}${players[0].player.name.uppercase()} WINS!")
        }
        else {
            Broadcast.all("${ChatColor.YELLOW}The game has been stopped!")
        }
        SoundUtil.pingNonParticipants(GameSound.HIGH_PLING)
        for (p in Bukkit.getOnlinePlayers()) {
            p.gameMode = GameMode.CREATIVE
            p.teleport(Location(
                Bukkit.getWorld("world")!!,
                0.toDouble(),
                Bukkit.getWorld("world")!!.getHighestBlockAt(0, 0).y.toDouble() + 1,
                0.toDouble()
            ))
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
        player.gameMode = GameMode.SPECTATOR

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
            SoundUtil.pingParticipants(GameSound.DEATH)
            return
        }
        Broadcast.participants("${ChatColor.RED}${ChatColor.ITALIC}${player.name} has left DeathShift!")
    }
}
