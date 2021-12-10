package com.sab.deathshift.commands

import com.sab.deathshift.DeathShift
import com.sab.deathshift.managers.GameManager
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class PlayingCommand(private val plugin: DeathShift) : CommandExecutor {
    init {
        plugin.getCommand("playing")!!.setExecutor(this)
    }

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<String>): Boolean {
        val sb = StringBuilder()
        if (GameManager.players.size == 0) {
            sender.sendMessage("${ChatColor.GRAY}${ChatColor.ITALIC}Nobody is playing right now...")
            return true
        }
        for(manager in GameManager.players) {
            val colour = if (manager.inLobby()) ChatColor.GREEN else ChatColor.RED
            sb.append("$colour${manager.player.name}, ${ChatColor.RESET}")
        }

        val msg = sb.toString()
        sender.sendMessage(msg.substring(0, msg.length - 4))
        return true
    }
}