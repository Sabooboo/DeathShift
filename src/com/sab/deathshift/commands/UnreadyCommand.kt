package com.sab.deathshift.commands

import com.sab.deathshift.DeathShift
import com.sab.deathshift.managers.GameManager
import com.sab.deathshift.managers.GameState
import com.sab.deathshift.managers.PlayerState
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class UnreadyCommand(plugin: DeathShift) : CommandExecutor {
    init {
        plugin.getCommand("unready")!!.setExecutor(this)
    }

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("Only players can use this command!")
            return true
        }
        if (!GameManager.inLobby(sender)) {
            sender.sendMessage("${ChatColor.GRAY}${ChatColor.ITALIC}You aren't in a game of DeathShift!")
            return true
        }
        if (GameManager.state == GameState.PLAYING) {
            sender.sendMessage("${ChatColor.RED}${ChatColor.BOLD}YOU ALREADY SIGNED UP FOR THIS!!!")
            return true
        }

        GameManager.get(sender)?.state = PlayerState.UNREADY
        return true
    }
}
