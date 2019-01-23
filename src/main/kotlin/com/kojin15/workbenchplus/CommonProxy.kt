package com.kojin15.workbenchplus

import com.kojin15.workbenchplus.WorkbenchPlus.MOD_ID
import com.kojin15.workbenchplus.gui.ContainerWorkbenchPlus
import com.kojin15.workbenchplus.tile.TileWorkbenchPlus
import net.minecraft.item.ItemBlock
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.event.FMLInterModComms
import net.minecraftforge.fml.common.network.NetworkRegistry
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

        FMLInterModComms.sendMessage("craftingtweaks", "RegisterProvider",
                NBTTagCompound().apply {
                    setString("ContainerClass", ContainerWorkbenchPlus::class.java.name)
                    setInteger("ButtonOffsetX", 7)
                    setInteger("ButtonOffsetY", 18)
                })
        NetworkRegistry.INSTANCE.registerGuiHandler(WorkbenchPlus, WPGuiHandler)
    }

    open fun postInit() = Unit
}