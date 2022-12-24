package dev.oliveman.tileentities

import de.studiocode.invui.gui.builder.GUIBuilder
import de.studiocode.invui.gui.builder.guitype.GUIType
import de.studiocode.invui.gui.impl.SimpleScrollVIGUI
import de.studiocode.invui.virtualinventory.event.ItemUpdateEvent
import dev.oliveman.Blocks
import dev.oliveman.GUITextures
import dev.oliveman.Items
import org.bukkit.inventory.ItemStack
import xyz.xenondevs.nova.data.config.NovaConfig
import xyz.xenondevs.nova.data.config.configReloadable
import xyz.xenondevs.nova.data.world.block.state.NovaTileEntityState
import xyz.xenondevs.nova.tileentity.NetworkedTileEntity
import xyz.xenondevs.nova.util.item.novaMaterial
import java.util.Date

val INTEREST_RATE = configReloadable { NovaConfig[Blocks.BANK].getDouble("interest_rate") }

class Bank(blockState: NovaTileEntityState) : NetworkedTileEntity(blockState) {
    override val gui = lazy { BankGUI() }
    private val inputInventory = getInventory("bank", 24, ::preUpdateInputInventory)
    private val outputInventory = getInventory("output", 24)
    private var lastUpdated = Date()
    
    private fun preUpdateInputInventory(event: ItemUpdateEvent) {
        // If adding something other than a coin
        event.isCancelled = event.isAdd && !(event.newItemStack.novaMaterial?.equals(Items.COIN) ?: false);
    }
    
    override fun handleTick() {
        
        val mcDaysPassed = (Date().time - lastUpdated.time) / (20 * 60 * 1000)
        if (mcDaysPassed >= 1) {
            val numCoins = inputInventory.items.filter { items: ItemStack? ->
                // Check anyway to avoid exploits
                items?.novaMaterial?.equals(Items.COIN) ?: false
            }.sumOf { items: ItemStack? ->
                items?.amount ?: 0
            }
            
            val newCoins = (mcDaysPassed * numCoins * INTEREST_RATE.value).toInt()
            if (newCoins >= 1) {
                val items = Items.COIN.createItemStack(newCoins)
                outputInventory.addItem(SELF_UPDATE_REASON, items)
                lastUpdated = Date()
            }
        }
    }
    
    inner class BankGUI : TileEntityGUI(GUITextures.BANK) {
        override val gui: SimpleScrollVIGUI = GUIBuilder(GUIType.SCROLL_INVENTORY).setStructure(
            "x x x x . o o o o",
            "x x x x . o o o o",
            "x x x x . o o o o",
            "x x x x . o o o o",
            "x x x x . o o o o",
            "x x x x . o o o o",
        )
            .setInventory(inputInventory)
            .addIngredient('o', outputInventory)
            .build()
    }
}