package com.sab.deathshift.managers

import com.sab.deathshift.utilities.LocationTools
import org.bukkit.GameMode
import org.bukkit.entity.Player

class PlayerManager(val player: Player) {

    var destination = LocationTools.generateRandomLocation(
        -ConfigManager.randomLocationBounds, ConfigManager.randomLocationBounds
    )

    var state = PlayerState.UNREADY
        set(value) {
            if (field == value) return
            field = value
            when (value) {
                PlayerState.PLAYING -> {
                    return
                }

                PlayerState.READY, PlayerState.UNREADY -> {
                    GameManager.notifyReady(this)
                    return
                }
            }
        }

    fun setup() {
        player.gameMode = GameMode.SURVIVAL
        player.inventory.clear()
        player.health = 20.toDouble()
        player.foodLevel = 20
        player.saturation = 20.toFloat()
    }
}
