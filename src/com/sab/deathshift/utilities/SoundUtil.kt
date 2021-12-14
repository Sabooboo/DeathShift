package com.sab.deathshift.utilities

import com.sab.deathshift.managers.GameManager
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object SoundUtil {

    fun ping(p: Player, gameSound: GameSound) {
        p.playSound(p.location, gameSound.sound, gameSound.volume, gameSound.pitch)
    }

    fun pingAll(gameSound: GameSound) {
        for (p in Bukkit.getOnlinePlayers()) ping(p, gameSound)
    }

    fun pingParticipants(gameSound: GameSound) {
        for (p in Bukkit.getOnlinePlayers()) {
            if (GameManager.inLobby(p)) {
                ping(p, gameSound)
            }
        }
    }

    fun pingNonParticipants(gameSound: GameSound) {
        for (p in Bukkit.getOnlinePlayers()) {
            if (!GameManager.inLobby(p)) {
                ping(p, gameSound)
            }
        }
    }
}