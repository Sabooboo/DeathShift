package com.sab.deathshift.commands

import com.sab.deathshift.DeathShift
import com.sab.deathshift.managers.GameManager
import com.sab.deathshift.managers.GameState
import com.sab.deathshift.managers.PlayerState
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class PlayingCommand(plugin: DeathShift) : CommandExecutor {
    init {
        plugin.getCommand("playing")!!.setExecutor(this)
    }

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<String>): Boolean {
        val sb = StringBuilder()
        val playing = mutableListOf<String>()
        val ready = mutableListOf<String>()
        val unready = mutableListOf<String>()
        for (manager in GameManager.players) {
            when (manager.state) {
                PlayerState.PLAYING -> {
                    playing.add(manager.player.name)
                }

                PlayerState.READY -> {
                    ready.add(manager.player.name)
                }

                PlayerState.UNREADY -> {
                    unready.add(manager.player.name)
                }
            }
        }

        if (GameManager.state == GameState.PLAYING) {
            sb.append("${ChatColor.GREEN}Playing: ${playing.joinToString(", ")}")
            sender.sendMessage("$sb")
            return true
        }
        sb.appendLine("${ChatColor.GREEN}Ready: ${ChatColor.GRAY}${ready.joinToString(", ")}")
        sb.append("${ChatColor.YELLOW}Unready: ${ChatColor.GRAY}${unready.joinToString(", ")}")
        sender.sendMessage("$sb")
        return true
    }
}
