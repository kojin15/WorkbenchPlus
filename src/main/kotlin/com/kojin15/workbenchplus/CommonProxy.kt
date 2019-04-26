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

package com.kojin15.workbenchplus

import com.kojin15.workbenchplus.gui.WPGuiHandler
import com.kojin15.workbenchplus.tile.TileWorkbenchPlus
import cpw.mods.fml.common.network.NetworkRegistry
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.relauncher.Side
import net.minecraft.init.Blocks
import net.minecraftforge.oredict.OreDictionary
import net.minecraftforge.oredict.ShapedOreRecipe

/**
 * @author kojin15.
 */
abstract class CommonProxy {
    abstract val side: Side

    open fun preInit() {
        GameRegistry.registerBlock(WorkbenchPlus.Blocks.workbenchplus, "workbenchplus")
    }

    open fun init() {
        GameRegistry.registerTileEntity(TileWorkbenchPlus::class.java, "workbenchplus")

        NetworkRegistry.INSTANCE.registerGuiHandler(WorkbenchPlus, WPGuiHandler)
    }

    open fun postInit() {
        val iron = if (OreDictionary.doesOreNameExist("plateIron")) "plateIron" else "ingotIron"
        GameRegistry.addRecipe(ShapedOreRecipe(WorkbenchPlus.Blocks.workbenchplus,
                "ABA",
                "ACA",
                "ADA",
                'A', iron, 'B', Blocks.trapdoor, 'C', Blocks.crafting_table, 'D', Blocks.chest))
    }
}