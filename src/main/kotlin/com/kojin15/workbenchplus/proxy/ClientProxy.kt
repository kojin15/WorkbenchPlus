package com.kojin15.workbenchplus.proxy

import com.kojin15.workbenchplus.CommonProxy
import com.kojin15.workbenchplus.WorkbenchPlus
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.item.Item
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.fml.relauncher.Side

/**
 * @author kojin15.
 */
class ClientProxy : CommonProxy() {
    override val side: Side
        get() = Side.CLIENT

    override fun preInit() {
        super.preInit()

        ModelLoader.setCustomModelResourceLocation(
                Item.getItemFromBlock(WorkbenchPlus.Blocks.workbenchplus),
                0,
                ModelResourceLocation(WorkbenchPlus.Blocks.workbenchplus.registryName!!, "inventory"))
    }
}