package com.sab.deathshift.commands

import com.sab.deathshift.DeathShift
import com.sab.deathshift.managers.GameManager
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class JoinCommand(private val plugin: DeathShift) : CommandExecutor {
    init {
        plugin.getCommand("join")!!.setExecutor(this)
    }

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("Only players can use this command!")
            return true
        }
        if (GameManager.inProgress) {
            sender.sendMessage("${ChatColor.GRAY}${ChatColor.ITALIC}You can't join games in progress!")
            return true
        }
        if (!GameManager.inLobby(sender)) GameManager.add(sender)
        return true
    }
}