package com.sab.deathshift.utilities

import org.bukkit.Sound

enum class GameSound(val sound: Sound, val volume: Float, val pitch: Float) {
    HIGH_PLING(Sound.BLOCK_NOTE_BLOCK_PLING, 0.1.toFloat(), 2.0.toFloat()),
    LOW_PLING(Sound.BLOCK_NOTE_BLOCK_PLING, 0.1.toFloat(), 1.0.toFloat()),
    TICK(Sound.BLOCK_NOTE_BLOCK_SNARE, 0.1.toFloat(), 1.0.toFloat()),
    DEATH(Sound.ENTITY_ENDER_DRAGON_GROWL, 0.1.toFloat(), 0.5.toFloat())
}