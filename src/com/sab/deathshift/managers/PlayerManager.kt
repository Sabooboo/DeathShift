package com.sab.deathshift.managers

import com.sab.deathshift.utilities.LocationTools
import org.bukkit.Location
import org.bukkit.entity.Player
import java.lang.Error

class PlayerManager(val player: Player) {

    var destination: Location = LocationTools.generateRandomLocation()
    var ready: Boolean = false
    set(value) {
        val old = field
        field = value
        if (old != field) GameManager.notifyReady(this)
    }
    var playing: Boolean = false

    fun inLobby(): Boolean {
        return GameManager.inLobby(this.player)
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other is Player) {
            return this.player.uniqueId == other.uniqueId
        }
        if (other is PlayerManager) {
            return this.equals(other.player)
        }
        return false
    }
}