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

// SpaghettiManager
object GameManager {

    private var plugin: DeathShift = Bukkit.getPluginManager().getPlugin("DeathShift") as DeathShift
    private lateinit var startTimer: BukkitRunnable
    private lateinit var teleportTimer: BukkitRunnable

    var players = mutableListOf<PlayerManager>()
        private set
    var state = GameState.STOPPED
        private set

    private fun startQueue() {
        if (state != GameState.STOPPED) return
        state = GameState.STARTING
        startTimer = StartTimer(plugin)
        startTimer.runTaskTimer(plugin, 20, 20)
    }

    private fun stopQueue() {
        if (state != GameState.STARTING) return
        state = GameState.STOPPED
        startTimer.cancel()
        startTimer = StartTimer(plugin) // this doesn't seem like industry-standard practise...
    }

    fun start() {
        if (state == GameState.PLAYING) return
        state = GameState.PLAYING
        Broadcast.all("${ChatColor.GREEN}${ChatColor.ITALIC}Game starting!")
        val randomInitialTeleport = ConfigManager.randomInitialTeleport
        for (manager in players) {
            manager.state = PlayerState.PLAYING
            if (randomInitialTeleport) manager.player.teleport(manager.destination)
            manager.setup()
        }
        teleportTimer = TeleportTimer(plugin)
        teleportTimer.runTaskTimer(plugin, 20, 20)
    }

    fun stop() {
        if (state == GameState.STARTING) {
            stopQueue()
            return
        }
        if (state == GameState.STOPPED) return

        teleportTimer.cancel()
        if (players.size == 1) {
            Broadcast.all("${ChatColor.GOLD}${players[0].player.name.uppercase()} WINS!")
        } else {
            Broadcast.all("${ChatColor.YELLOW}The game has been stopped!")
        }
        SoundUtil.pingNonParticipants(GameSound.HIGH_PLING)
        for (p in Bukkit.getOnlinePlayers()) {
            p.gameMode = GameMode.CREATIVE
            p.teleport(
                Location(
                    Bukkit.getWorld("world")!!,
                    0.toDouble(),
                    Bukkit.getWorld("world")!!.getHighestBlockAt(0, 0).y.toDouble() + 1,
                    0.toDouble()
                )
            )
        }
        players = mutableListOf()
        state = GameState.STOPPED
    }

    fun inLobby(player: Player): Boolean {
        return get(player) != null
    }

    fun get(player: Player): PlayerManager? {
        for (playerManager in players) {
            if (player.uniqueId == playerManager.player.uniqueId) return playerManager
        }
        return null
    }

    private fun countReady(): Int {
        var readyAmount = 0
        for (playerManager in players) {
            if (playerManager.state == PlayerState.READY) readyAmount++
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
        if (!inLobby(manager.player) || state == GameState.PLAYING) return
        broadcastReady(manager)
        if (manager.state == PlayerState.READY && (countReady() == players.size) && countReady() > 1) {
            startQueue()
            return
        }
        stopQueue()
    }

    fun add(player: Player) {
        if (inLobby(player) || state == GameState.PLAYING) return
        players.add(PlayerManager(player))
        Broadcast.participants("${ChatColor.GREEN}${ChatColor.ITALIC}${player.name} has joined DeathShift!")
        stopQueue()
    }

    fun remove(player: Player) {
        if (!inLobby(player)) return
        broadcastLeave(player)
        players.remove(get(player))

        if (state != GameState.PLAYING) {
            for (manager in players) {
                manager.state = PlayerState.UNREADY
            }
        } else {
            player.gameMode = GameMode.SPECTATOR
            if (players.size == 1) stop()
        }
    }

    private fun broadcastReady(manager: PlayerManager) {
        if (manager.state == PlayerState.READY) {
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
        if (state == GameState.PLAYING) {
            Broadcast.participants("${ChatColor.RED}${ChatColor.BOLD}${player.name} has been eliminated!")
            SoundUtil.pingParticipants(GameSound.LIGHTNING)
            return
        }
        Broadcast.participants("${ChatColor.RED}${ChatColor.ITALIC}${player.name} has left DeathShift!")
    }
}
