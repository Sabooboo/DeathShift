package com.sab.deathshift.commands

import com.sab.deathshift.DeathShift
import com.sab.deathshift.managers.ConfigManager
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

        /*
         * TODO:
         *  I would like to make this cleaner but it seems needlessly complex for my scope right now.
         *  I'm especially displeased with this because future me will cringe when he adds more config
         *  properties, but the idea of performing type validation at runtime seems foreign to Kotlin
         *  devs on stackoverflow and the like.
         *  I'll figure something out later. It's 07:30 and I haven't slept yet...
         */
        val path = args[0]
        val value = args[1]
        when (path) {
            "shift_time" -> {
                try { value.toInt() }
                catch (err: NumberFormatException) {
                    sender.sendMessage("Argument 2 needs to be an integer to be assigned to shift_time.")
                    return true
                }
                ConfigManager.shiftTime = value.toInt()
            }
            "countdown" -> {
                try { value.toInt() }
                catch (err: NumberFormatException) {
                    sender.sendMessage("Argument 2 needs to be an integer to be assigned to countdown.")
                    return true
                }
                ConfigManager.countdown = value.toInt()
            }
            "warn_time" -> {
                try { value.toInt() }
                catch (err: NumberFormatException) {
                    sender.sendMessage("Argument 2 needs to be an integer to be assigned to warn_time.")
                    return true
                }
                ConfigManager.warnTime = value.toInt()
            }
            "warn_half" -> {
                try { value.lowercase().toBooleanStrict() }
                catch (err: Exception) {
                    sender.sendMessage("Argument 2 needs to be true or false to be assigned to warn_half.")
                    return true
                }
                ConfigManager.warnHalf = value.toBooleanStrict()
            }
            "random_teleport" -> {
                try { value.lowercase().toBooleanStrict() }
                catch (err: Exception) {
                    sender.sendMessage("Argument 2 needs to be true or false to be assigned to random_teleport.")
                    return true
                }
                ConfigManager.randomTeleport = value.toBooleanStrict()
            }
            "know_next_target" -> {
                try { value.lowercase().toBooleanStrict() }
                catch (err: Exception) {
                    sender.sendMessage("Argument 2 needs to be true or false to be assigned to know_next_target.")
                    return true
                }
                ConfigManager.knowNextTarget = value.toBooleanStrict()
            }
            else -> {
                sender.sendMessage("$path does not exist in the config!")
            }
        }
        return true
    }
}
