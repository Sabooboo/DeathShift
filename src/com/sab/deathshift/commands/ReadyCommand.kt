package com.sab.deathshift.commands

import com.sab.deathshift.DeathShift
import com.sab.deathshift.managers.GameManager
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ReadyCommand(private val plugin: DeathShift) : CommandExecutor {
    init {
        plugin.getCommand("ready")!!.setExecutor(this)
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

        // I felt really cool making this. Basically if the
        // sender is ready, pretend they did the unready command.
        if (GameManager.get(sender)!!.ready) {
            return UnreadyCommand(plugin).onCommand(sender, cmd, label, args)
        }

        GameManager.get(sender)!!.ready = true
        return true
    }
}