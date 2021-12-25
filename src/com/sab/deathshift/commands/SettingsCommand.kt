package com.sab.deathshift.commands

import com.sab.deathshift.DeathShift
import com.sab.deathshift.managers.ConfigManager
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class SettingsCommand(plugin: DeathShift) : CommandExecutor {
    init {
        plugin.getCommand("settings")!!.setExecutor(this)
    }

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<String>): Boolean {
        val sb = StringBuilder()
        sb.appendLine("${ChatColor.GRAY}DeathShift Settings:")
        for (setting in ConfigManager.getAll()) {
            sb.appendLine("${ChatColor.GRAY}${setting.key}: ${ChatColor.BLUE}${setting.value}")
        }
        sender.sendMessage(sb.toString())
        return true
    }
}
