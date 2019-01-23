package com.kojin15.workbenchplus.block

import com.kojin15.workbenchplus.WPGuiHandler
import com.kojin15.workbenchplus.WorkbenchPlus
import com.kojin15.workbenchplus.tile.TileWorkbenchPlus
import net.minecraft.block.BlockContainer
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumBlockRenderType
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World

/**
 * @author kojin15.
 */
@Suppress("OverridingDeprecatedMember")
class BlockWorkbenchPlus : BlockContainer(Material.IRON) {
    init {
        unlocalizedName = "WorkbenchPlus"
        blockHardness = 5.0f
        blockResistance = 10.0f
        soundType = SoundType.METAL
        setCreativeTab(CreativeTabs.DECORATIONS)
    }

    override fun onBlockActivated(worldIn: World, pos: BlockPos, state: IBlockState, playerIn: EntityPlayer, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        if (!playerIn.isSneaking) playerIn.openGui(WorkbenchPlus, WPGuiHandler.GUI_ID_WORKBENCHPLUS, worldIn, pos.x, pos.y, pos.z)
        return true
    }

    override fun getBoundingBox(state: IBlockState, source: IBlockAccess, pos: BlockPos): AxisAlignedBB {
        return AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.375, 1.0)
    }

    override fun isFullCube(state: IBlockState): Boolean = false

    override fun isOpaqueCube(state: IBlockState): Boolean = false

    override fun getRenderType(state: IBlockState): EnumBlockRenderType {
        return EnumBlockRenderType.MODEL
    }

    override fun createNewTileEntity(worldIn: World?, meta: Int): TileEntity {
        return TileWorkbenchPlus()
    }
}