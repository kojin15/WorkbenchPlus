package com.kojin15.workbenchplus.gui

import com.kojin15.workbenchplus.tile.TileWorkbenchPlus
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.common.network.IGuiHandler

/**
 * @author kojin15.
 */
object WPGuiHandler : IGuiHandler {
    const val GUI_ID_WORKBENCHPLUS = 1

    override fun getClientGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Any? {
        return when (ID) {
            GUI_ID_WORKBENCHPLUS -> GuiWorkbenchPlus(world.getTileEntity(BlockPos(x, y, z)) as TileWorkbenchPlus, player)
            else -> null
        }
    }

    override fun getServerGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Any? {
        return when (ID) {
            GUI_ID_WORKBENCHPLUS -> ContainerWorkbenchPlus(world.getTileEntity(BlockPos(x, y, z)) as TileWorkbenchPlus, player)
            else -> null
        }
    }
}