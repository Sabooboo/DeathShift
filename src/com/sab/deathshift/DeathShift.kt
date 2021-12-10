package com.sab.deathshift

import com.sab.deathshift.commands.*
import com.sab.deathshift.managers.GameManager
import org.bukkit.plugin.java.JavaPlugin

class DeathShift : JavaPlugin() {

    override fun onEnable() {
        server.pluginManager.registerEvents(Events, this)
        JoinCommand(this)
        LeaveCommand(this)
        ReadyCommand(this)
        UnreadyCommand(this)
        PlayingCommand(this)
    }

    override fun onDisable() {}
}