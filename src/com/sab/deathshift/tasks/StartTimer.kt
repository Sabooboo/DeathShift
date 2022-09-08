package com.sab.deathshift.tasks

import com.sab.deathshift.DeathShift
import com.sab.deathshift.managers.ConfigManager
import com.sab.deathshift.managers.GameManager
import com.sab.deathshift.utilities.GameSound
import com.sab.deathshift.utilities.SoundUtil
import com.sab.deathshift.utilities.Title
import org.bukkit.ChatColor
import org.bukkit.scheduler.BukkitRunnable

class StartTimer(private val plugin: DeathShift) : BukkitRunnable() {
    private var time = ConfigManager.countdown

    override fun run() {
        if (time == 0) {
            SoundUtil.pingParticipants(GameSound.HIGH_PLING)
            GameManager.start()
            this.cancel()
            return
        }
        SoundUtil.pingParticipants(GameSound.LOW_PLING)
        Title.participants("${ChatColor.GREEN}Starting in $time")
        time--
    }
}
