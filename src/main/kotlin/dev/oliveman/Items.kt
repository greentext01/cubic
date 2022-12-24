package dev.oliveman

import xyz.xenondevs.nova.item.behavior.Damageable
import xyz.xenondevs.nova.item.behavior.Tool
import xyz.xenondevs.nova.material.NovaMaterialRegistry

@Suppress()
object Items {
    val COIN = NovaMaterialRegistry.registerDefaultItem(Cubic, "coin")
    val FLINT_PICKAXE = NovaMaterialRegistry.registerDefaultItem(Cubic, "flint_pickaxe", Tool, Damageable)
    fun init() = Unit
}