package dev.oliveman.tileentities

import de.studiocode.invui.gui.GUI
import de.studiocode.invui.gui.builder.GUIBuilder
import de.studiocode.invui.gui.builder.guitype.GUIType
import de.studiocode.invui.item.builder.ItemBuilder
import de.studiocode.invui.item.impl.CycleItem
import de.studiocode.invui.virtualinventory.event.UpdateReason
import dev.oliveman.gui.ProgressArrowItem
import dev.oliveman.gui.ProgressFlameItem
import dev.oliveman.recipe.CrucibleRecipe
import dev.oliveman.registry.Blocks.CRUCIBLE
import dev.oliveman.registry.RecipeTypes
import org.bukkit.Material
import org.bukkit.block.BlockFace
import xyz.xenondevs.nova.data.config.NovaConfig
import xyz.xenondevs.nova.data.config.configReloadable
import xyz.xenondevs.nova.data.recipe.RecipeManager
import xyz.xenondevs.nova.data.world.block.state.NovaTileEntityState
import xyz.xenondevs.nova.tileentity.NetworkedTileEntity
import xyz.xenondevs.nova.tileentity.network.NetworkConnectionType
import xyz.xenondevs.nova.tileentity.network.item.holder.NovaItemHolder
import xyz.xenondevs.nova.util.enumMapOf
import xyz.xenondevs.nova.util.item.novaMaterial
import xyz.xenondevs.nova.util.mapToArray
import java.util.UUID

private val COOK_TIME by configReloadable { NovaConfig[CRUCIBLE].getInt("cookTime") }
private val FUEL_TIME by configReloadable { NovaConfig[CRUCIBLE].getInt("fuelTime") }

class Crucible(blockState: NovaTileEntityState) : NetworkedTileEntity(blockState) {
    override val gui = lazy(::CrucibleGUI)
    private val inputInventory = getInventory("ingredients", 1, { event ->
        if (event.isAdd && event.newItemStack.type != Material.COPPER_INGOT) event.isCancelled = true
    })
    private val fuelInventory = getInventory("fuel", 1, { event ->
        if (event.isAdd && event.newItemStack.type != Material.CHARCOAL) event.isCancelled = true
    })
    private val outputInventory = getInventory("output", 1, { event ->
        if (event.updateReason != SELF_UPDATE_REASON && event.isAdd) event.isCancelled = true
    })

    private val recipes = RecipeManager.novaRecipes[RecipeTypes.CRUCIBLE]!!.values.filterIsInstance<CrucibleRecipe>()

    // Get all recipe results, and create ItemProviders with them.
    private val craftChoice = CycleItem(*recipes.mapToArray { ItemBuilder(it.result) })
    private var heatTicks: Int by storedValue("heatAmount") { 0 }
    private var ticksCooked: Int by storedValue("progress") { 0 }
    override val itemHolder = NovaItemHolder(this,
        inputInventory to NetworkConnectionType.INSERT,
        outputInventory to NetworkConnectionType.EXTRACT,
        fuelInventory to NetworkConnectionType.INSERT,

        defaultInvConfig = {
            enumMapOf<BlockFace, UUID>(
                BlockFace.NORTH to inputInventory.uuid,
                BlockFace.SOUTH to inputInventory.uuid,
                BlockFace.EAST to fuelInventory.uuid,
                BlockFace.WEST to fuelInventory.uuid,
                BlockFace.UP to inputInventory.uuid,
                BlockFace.DOWN to outputInventory.uuid,
            )
        },
        defaultConnectionConfig = {
            enumMapOf<BlockFace, NetworkConnectionType>(
                BlockFace.NORTH to NetworkConnectionType.INSERT,
                BlockFace.SOUTH to NetworkConnectionType.INSERT,
                BlockFace.EAST to NetworkConnectionType.INSERT,
                BlockFace.WEST to NetworkConnectionType.INSERT,
                BlockFace.UP to NetworkConnectionType.INSERT,
                BlockFace.DOWN to NetworkConnectionType.EXTRACT,
            )
        })

    /**
     * Check if the input inventory contains a copper ingot. If not, then set the progress to 0, because you can only cook copper.
     */
    private fun checkCopper(): Boolean {
        print(inputInventory.getItemStack(0))
        if (inputInventory.getItemStack(0)?.type?.equals(Material.COPPER_INGOT) == true) {
            return true
        } else {
            ticksCooked = 0
            return false
        }
    }

    override fun handleTick() {
        // If not hot and has coal, burn the coal.
        if (heatTicks <= 0 && fuelInventory.getItemStack(0)?.type?.equals(Material.CHARCOAL) == true && ticksCooked < COOK_TIME) {
            fuelInventory.addItemAmount(SELF_UPDATE_REASON, 0, -1)

            // Enough to cook 8 ingots
            heatTicks = FUEL_TIME
        }

        if (checkCopper() && heatTicks > 0) {
            ticksCooked += 1
            heatTicks -= 1
        }

        if (ticksCooked >= COOK_TIME && checkCopper()) {
            finishCook()
        }

        if (gui.isInitialized()) gui.value.updateProgress()
    }

    private fun finishCook() {
        if (ticksCooked >= COOK_TIME) {
            val out = recipes[craftChoice.state].result
            if (outputInventory.canHold(out)) {
                inputInventory.addItemAmount(SELF_UPDATE_REASON, 0, -1)
                outputInventory.putItemStack(
                    SELF_UPDATE_REASON, 0, out
                )
                ticksCooked = 0
            }
        }
    }

    inner class CrucibleGUI : TileEntityGUI() {
        private val progressArrowItem = ProgressArrowItem()
        private val flameProgressItem = ProgressFlameItem()

        init {
            updateProgress()
        }

        fun updateProgress() {
            progressArrowItem.percentage = ticksCooked / COOK_TIME.toDouble()
            flameProgressItem.percentage = heatTicks / FUEL_TIME.toDouble()
        }

        override val gui: GUI = GUIBuilder(GUIType.NORMAL).setStructure(
            "1 - - - - - - - 2",
            "| i # # # # # # |",
            "| h # c # > # o |",
            "| f # # # # # # |",
            "3 - - - - - - - 4",
        ).addIngredient('c', craftChoice).addIngredient('>', progressArrowItem).addIngredient('h', flameProgressItem)
            .addIngredient('f', fuelInventory).addIngredient('o', outputInventory).addIngredient('i', inputInventory)
            .build()

    }
}

