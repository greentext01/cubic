package dev.oliveman.registry;

import dev.oliveman.Cubic
import dev.oliveman.recipe.CrucibleRecipe
import dev.oliveman.recipe.deserializer.CrucibleRecipeDeserializer
import dev.oliveman.recipe.group.CrucibleRecipeGroup
import xyz.xenondevs.nova.data.recipe.RecipeTypeRegistry.register

object RecipeTypes {
    val CRUCIBLE = register(Cubic, "crucible", CrucibleRecipe::class, CrucibleRecipeGroup, CrucibleRecipeDeserializer)

    fun init() = Unit
}
