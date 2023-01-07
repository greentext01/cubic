package dev.oliveman.registry

import dev.oliveman.Cubic
import xyz.xenondevs.nova.item.behavior.Damageable
import xyz.xenondevs.nova.item.behavior.Tool
import xyz.xenondevs.nova.material.NovaMaterialRegistry

@Suppress("unused")
object Items {
    val COIN = NovaMaterialRegistry.registerDefaultItem(Cubic, "coin")
    val TIN_INGOT = NovaMaterialRegistry.registerDefaultItem(Cubic, "tin_ingot")
    val BRONZE_INGOT = NovaMaterialRegistry.registerDefaultItem(Cubic, "bronze_ingot")
    val FLINT_PICKAXE = NovaMaterialRegistry.registerDefaultItem(Cubic, "flint_pickaxe", Tool, Damageable)
    fun init() = Unit
}