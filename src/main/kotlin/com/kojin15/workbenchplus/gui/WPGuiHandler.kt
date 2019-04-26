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

package com.kojin15.workbenchplus.gui

import com.kojin15.workbenchplus.tile.TileWorkbenchPlus
import cpw.mods.fml.common.network.IGuiHandler
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.World

/**
 * @author kojin15.
 */
object WPGuiHandler : IGuiHandler {
    const val GUI_ID_WORKBENCHPLUS = 1

    override fun getClientGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Any? {
        return when (ID) {
            GUI_ID_WORKBENCHPLUS -> GuiWorkbenchPlus(world.getTileEntity(x, y, z) as TileWorkbenchPlus, player)
            else -> null
        }
    }

    override fun getServerGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Any? {
        return when (ID) {
            GUI_ID_WORKBENCHPLUS -> ContainerWorkbenchPlus(world.getTileEntity(x, y, z) as TileWorkbenchPlus, player)
            else -> null
        }
    }
}