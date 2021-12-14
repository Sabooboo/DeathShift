package com.sab.deathshift.tasks

import com.sab.deathshift.DeathShift
import com.sab.deathshift.managers.ConfigManager
import com.sab.deathshift.managers.GameManager
import com.sab.deathshift.managers.PlayerManager
import com.sab.deathshift.utilities.Broadcast
import com.sab.deathshift.utilities.Title
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.scheduler.BukkitRunnable

class StartTimer(private val plugin: DeathShift) : BukkitRunnable() {
    var time = ConfigManager.countdown

    override fun run() {
        if (time == 0) {
            GameManager.start()
            this.cancel()
            return
        }

        Title.participants("${ChatColor.GREEN}Starting in $time")
        time--
    }
}
