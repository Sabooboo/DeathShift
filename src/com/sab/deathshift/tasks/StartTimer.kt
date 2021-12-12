package com.sab.deathshift.tasks

import com.sab.deathshift.DeathShift
import com.sab.deathshift.managers.GameManager
import com.sab.deathshift.managers.PlayerManager
import com.sab.deathshift.utilities.Broadcast
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.scheduler.BukkitRunnable

class StartTimer(private val plugin: DeathShift) : BukkitRunnable() {
    var time = 5

    override fun run() {
        if (time == 0) {
            GameManager.start()
            this.cancel()
            return
        }
        Broadcast.participants("${ChatColor.GREEN}Starting in ${time--}")
    }
}
