package dev.oliveman

import dev.oliveman.registry.Blocks
import dev.oliveman.registry.Items
import dev.oliveman.registry.RecipeTypes
import xyz.xenondevs.nova.addon.Addon

object Cubic : Addon() {
    
    override fun init() {
        // Called when the addon is initialized.
        // Register NovaMaterials, RecipeTypes, etc. here
        Items.init()
        Blocks.init()
        RecipeTypes.init()
    }
    
    override fun onEnable() {
        // Called when the addon is enabled.
        logger.info("\n" + """
                  ___           ___           ___                       ___
                 /\  \         /\__\         /\  \          ___        /\  \
                /::\  \       /:/  /        /::\  \        /\  \      /::\  \
               /:/\:\  \     /:/  /        /:/\:\  \       \:\  \    /:/\:\  \
              /:/  \:\  \   /:/  /  ___   /::\~\:\__\      /::\__\  /:/  \:\  \
             /:/__/ \:\__\ /:/__/  /\__\ /:/\:\ \:|__|  __/:/\/__/ /:/__/ \:\__\
             \:\  \  \/__/ \:\  \ /:/  / \:\~\:\/:/  / /\/:/  /    \:\  \  \/__/
              \:\  \        \:\  /:/  /   \:\ \::/  /  \::/__/      \:\  \
               \:\  \        \:\/:/  /     \:\/:/  /    \:\__\       \:\  \
                \:\__\        \::/  /       \::/__/      \/__/        \:\__\
                 \/__/         \/__/         ~~                        \/__/
             
             Cubic dev2 is starting up!
        """.trimIndent())
    }
    
    override fun onDisable() {
        // Called when the addon is disabled.
    }
    
}