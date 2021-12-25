package com.sab.deathshift

import com.sab.deathshift.commands.*
import com.sab.deathshift.managers.ConfigManager
import org.bukkit.plugin.java.JavaPlugin

class DeathShift : JavaPlugin() {

    override fun onEnable() {
        ConfigManager
        server.pluginManager.registerEvents(Events, this)
        SetCommand(this)
        JoinCommand(this)
        LeaveCommand(this)
        UnreadyCommand(this)
        ReadyCommand(this)
        PlayingCommand(this)
        SettingsCommand(this)
    }

    override fun onDisable() {}
}
