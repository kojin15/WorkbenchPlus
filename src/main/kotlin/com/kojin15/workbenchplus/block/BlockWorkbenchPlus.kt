/*
 * Copyright (c) 2019 kojin15
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kojin15.workbenchplus.block

import com.kojin15.workbenchplus.WorkbenchPlus
import com.kojin15.workbenchplus.WorkbenchPlus.MOD_ID
import com.kojin15.workbenchplus.gui.WPGuiHandler
import com.kojin15.workbenchplus.tile.TileWorkbenchPlus
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.block.Block
import net.minecraft.block.BlockContainer
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.IIcon
import net.minecraft.world.World

/**
 * @author kojin15.
 */
class BlockWorkbenchPlus : BlockContainer(Material.iron) {
    init {
        setCreativeTab(CreativeTabs.tabDecorations)
        setBlockName("WorkbenchPlus")
        blockHardness = 5.0f
        blockResistance = 10.0f
        setStepSound(Block.soundTypeMetal)
        setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.375f, 1.0f)
    }

    @SideOnly(Side.CLIENT)
    private lateinit var topIcon: IIcon

    @SideOnly(Side.CLIENT)
    private lateinit var sideIcon: IIcon

    @SideOnly(Side.CLIENT)
    private lateinit var bottomIcon: IIcon

    override fun isOpaqueCube(): Boolean = false

    override fun onBlockActivated(world: World?, x: Int, y: Int, z: Int, player: EntityPlayer?, side: Int, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        player?.openGui(WorkbenchPlus, WPGuiHandler.GUI_ID_WORKBENCHPLUS, world, x, y, z)
        return true
    }

    override fun createNewTileEntity(p_149915_1_: World?, p_149915_2_: Int): TileEntity {
        return TileWorkbenchPlus()
    }

    override fun registerBlockIcons(register: IIconRegister) {
        this.topIcon = register.registerIcon("$MOD_ID:workbenchplus_top")
        this.sideIcon = register.registerIcon("$MOD_ID:workbenchplus_side")
        this.bottomIcon = register.registerIcon("$MOD_ID:workbenchplus_bottom")
    }

    override fun getIcon(side: Int, meta: Int): IIcon {
        return when (side) {
            0 -> bottomIcon
            1 -> topIcon
            else -> sideIcon
        }
    }
}