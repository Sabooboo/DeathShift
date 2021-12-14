package com.sab.deathshift.commands

import com.sab.deathshift.DeathShift
import com.sab.deathshift.managers.ConfigManager
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import kotlin.NumberFormatException

class SetCommand(plugin: DeathShift) : CommandExecutor {
    init {
        plugin.getCommand("set")!!.setExecutor(this)
    }

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<String>): Boolean {
        if (args.size != 2) {
            sender.sendMessage("Incorrect positional arguments: /help DeathShift")
            return true
        }

        val path = args[0]
        val valueRaw = args[1]

        if (ConfigManager.get(path) == null) {
            sender.sendMessage("$path does not exist in the config.")
            return true
        }

        var value: Any?
        try {
            value = when (ConfigManager.get(path)) {
                is Int -> valueRaw.toInt()
                is Boolean -> valueRaw.lowercase().toBooleanStrict()
                else -> { null }
            }
        } catch (err: Exception) {
            return when (err) {
                is NumberFormatException -> {
                    sender.sendMessage("Argument 2 needs to be an integer to be assigned to ${path}.")
                    true
                }
                else -> {
                    sender.sendMessage("Argument 2 needs to be true or false to be assigned to ${path}.")
                    true
                }
            }
        }
        if (value == null) {
            sender.sendMessage("There was an error setting $path to $valueRaw")
            return true
        }
        ConfigManager.set(path, value)
        sender.sendMessage("${ChatColor.GRAY}${ChatColor.ITALIC}$path successfully set to $value")
        return true
    }
}
