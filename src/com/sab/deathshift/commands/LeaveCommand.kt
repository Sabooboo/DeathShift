package com.sab.deathshift.commands

import com.sab.deathshift.DeathShift
import com.sab.deathshift.managers.GameManager
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class LeaveCommand(plugin: DeathShift) : CommandExecutor {
    init {
        plugin.getCommand("leave")!!.setExecutor(this)
    }

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<String>): Boolean {
        // TODO: These next 8 lines are duplicated a bunch. I wonder what I should do to fix this.
        if (sender !is Player) {
            sender.sendMessage("Only players can use this command!")
            return true
        }
        if (!GameManager.inLobby(sender)) {
            sender.sendMessage("${ChatColor.GRAY}${ChatColor.ITALIC}You aren't in a game of DeathShift!")
            return true
        }
        GameManager.remove(sender)
        return true
    }
}