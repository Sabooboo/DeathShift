package com.sab.deathshift.tasks

import com.sab.deathshift.DeathShift
import com.sab.deathshift.managers.ConfigManager
import com.sab.deathshift.utilities.Broadcast
import com.sab.deathshift.utilities.GameSound
import com.sab.deathshift.utilities.SoundUtil
import org.bukkit.ChatColor
import org.bukkit.scheduler.BukkitRunnable

class TeleportTimer(private val plugin: DeathShift) : BukkitRunnable() {
    private var maxTime = ConfigManager.shiftTime
    private var time = maxTime
    private var alertedHalf = false

    override fun run() {
        if (time == 0) {
            SoundUtil.pingParticipants(GameSound.HIGH_PLING)
            Teleport(plugin).run()
            maxTime = ConfigManager.shiftTime
            time = maxTime
            alertedHalf = false
            return
        }

        if (!alertedHalf && time <= maxTime / 2 && ConfigManager.warnHalf) {
            SoundUtil.pingParticipants(GameSound.HIGH_PLING)
            Broadcast.all("${ChatColor.RED}${time} seconds until teleport!")
            alertedHalf = true
        }

        if (time <= ConfigManager.warnTime) {
            SoundUtil.pingParticipants(GameSound.LOW_PLING)
            Broadcast.participants("${ChatColor.RED}${ChatColor.BOLD}${time}...")
        }
        time--
    }
}
