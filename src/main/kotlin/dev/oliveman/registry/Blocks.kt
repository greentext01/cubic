package dev.oliveman.registry

import dev.oliveman.Cubic
import dev.oliveman.tileentities.Bank
import dev.oliveman.tileentities.Crucible
import org.bukkit.Material
import xyz.xenondevs.nova.material.BlockOptions
import xyz.xenondevs.nova.material.NovaMaterialRegistry.registerTileEntity
import xyz.xenondevs.nova.world.block.sound.SoundGroup

object Blocks {
    private val BANK_MATERIAL = BlockOptions(0.5, listOf(), null, false, SoundGroup.STONE, Material.SANDSTONE, true)
    private val CRUCIBLE_MATERIAL = BlockOptions(0.5, listOf(), null, false, SoundGroup.STONE, Material.WHITE_CONCRETE, true)

    val BANK = registerTileEntity(Cubic, "bank", BANK_MATERIAL, ::Bank)
    val CRUCIBLE = registerTileEntity(Cubic, "crucible", CRUCIBLE_MATERIAL, ::Crucible)
    fun init() = Unit
}