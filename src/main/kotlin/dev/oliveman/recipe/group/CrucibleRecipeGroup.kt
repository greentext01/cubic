package dev.oliveman.recipe.group;

import de.studiocode.invui.gui.GUI
import de.studiocode.invui.gui.builder.GUIBuilder
import de.studiocode.invui.gui.builder.guitype.GUIType
import de.studiocode.invui.item.builder.ItemBuilder
import dev.oliveman.recipe.CrucibleRecipe
import dev.oliveman.registry.Blocks
import dev.oliveman.registry.GUIMaterials
import dev.oliveman.registry.GUITextures
import org.bukkit.Material
import xyz.xenondevs.nova.data.recipe.RecipeContainer
import xyz.xenondevs.nova.ui.menu.item.recipes.group.RecipeGroup

object CrucibleRecipeGroup : RecipeGroup() {
    override val icon = Blocks.CRUCIBLE.basicClientsideProvider
    override val priority = 0
    override val texture = GUITextures.RECIPE_CRUCIBLE

    override fun createGUI(container: RecipeContainer): GUI {
        val recipe = container.recipe as CrucibleRecipe
        return GUIBuilder(GUIType.NORMAL)
            .setStructure(
                ". c . . . . . . .",
                ". f . r . . r . .",
                ". h . . . . . . .",
            )
            .addIngredient('c', ItemBuilder(Material.COPPER_INGOT))
            .addIngredient('h', ItemBuilder(Material.CHARCOAL))
            .addIngredient('f', GUIMaterials.FLAME_PROGRESS.clientsideProvider)
            .addIngredient('r', recipe.result)
            .build()
    }
}
