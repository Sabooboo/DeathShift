package com.sab.deathshift

import com.sab.deathshift.commands.*
import com.sab.deathshift.managers.GameManager
import org.bukkit.plugin.java.JavaPlugin

class DeathShift : JavaPlugin() {

    override fun onEnable() {
        server.pluginManager.registerEvents(Events, this)

        SetCommand(this)
        JoinCommand(this)
        LeaveCommand(this)
        UnreadyCommand(this)
        ReadyCommand(this)
        PlayingCommand(this)

        // I'm pretty sure calling it here instantiates the object, so I don't need to
        // worry about cold start times for the first joiner. Delay appears to be
        // significantly reduced.
        GameManager
    }

    override fun onDisable() {}
}
