package com.sab.deathshift.managers

import com.sab.deathshift.utilities.LocationTools
import org.bukkit.Location
import org.bukkit.entity.Player

class PlayerManager(val player: Player) {
    var destination: Location = LocationTools.generateRandomLocation()
    var playing: Boolean = false
    var ready: Boolean = false
    set(value) {
        val old = field
        field = value
        if (old != field) GameManager.notifyReady(this)
    }
}
