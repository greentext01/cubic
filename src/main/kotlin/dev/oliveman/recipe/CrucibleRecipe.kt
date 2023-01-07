package dev.oliveman.recipe

import dev.oliveman.registry.RecipeTypes
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import xyz.xenondevs.nova.data.recipe.NovaRecipe
import xyz.xenondevs.nova.data.recipe.ResultingRecipe

class CrucibleRecipe(
    override val key: NamespacedKey,
    override val result: ItemStack,
) : NovaRecipe, ResultingRecipe {
    override val type = RecipeTypes.CRUCIBLE
}
