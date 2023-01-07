package dev.oliveman.recipe.deserializer

import com.google.gson.JsonObject
import dev.oliveman.recipe.CrucibleRecipe
import xyz.xenondevs.nova.data.serialization.json.RecipeDeserializer
import xyz.xenondevs.nova.util.data.getString
import xyz.xenondevs.nova.util.item.ItemUtils
import java.io.File

object CrucibleRecipeDeserializer : RecipeDeserializer<CrucibleRecipe> {
    override fun deserialize(json: JsonObject, file: File): CrucibleRecipe {
        val result = ItemUtils.getItemBuilder(json.getString("result")!!).get()
        return CrucibleRecipe(RecipeDeserializer.getRecipeKey(file), result)
    }
}