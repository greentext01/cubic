package dev.oliveman

import dev.oliveman.tileentities.Bank
import org.bukkit.Material
import xyz.xenondevs.nova.material.BlockOptions
import xyz.xenondevs.nova.material.NovaMaterialRegistry.registerTileEntity
import xyz.xenondevs.nova.world.block.sound.SoundGroup

object Blocks {
    private val BANK_MATERIAL = BlockOptions(0.5, listOf(), null, false, SoundGroup.STONE, Material.SANDSTONE, true)
    
    val BANK = registerTileEntity(Cubic, "bank", BANK_MATERIAL, ::Bank)
    fun init() = Unit
}