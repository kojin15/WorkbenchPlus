package com.kojin15.workbenchplus

import com.kojin15.workbenchplus.WorkbenchPlus.MOD_ID
import com.kojin15.workbenchplus.tile.TileWorkbenchPlus
import net.minecraft.item.ItemBlock
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.registry.ForgeRegistries
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.fml.relauncher.Side

/**
 * @author kojin15.
 */
abstract class CommonProxy {
    abstract val side: Side

    open fun preInit() {
        ForgeRegistries.BLOCKS.register(WorkbenchPlus.Blocks.workbenchplus)
        ForgeRegistries.ITEMS.register(ItemBlock(WorkbenchPlus.Blocks.workbenchplus).setRegistryName(WorkbenchPlus.Blocks.workbenchplus.registryName))
    }

    open fun init() {
        GameRegistry.registerTileEntity(TileWorkbenchPlus::class.java, ResourceLocation(MOD_ID, "workbenchplus"))
    }

    open fun postInit() = Unit
}