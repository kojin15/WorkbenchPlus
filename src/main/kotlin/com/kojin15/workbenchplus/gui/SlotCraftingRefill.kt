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

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.Container
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.InventoryCrafting
import net.minecraft.inventory.SlotCrafting
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.CraftingManager
import net.minecraft.item.crafting.IRecipe
import net.minecraftforge.oredict.OreDictionary
import net.minecraftforge.oredict.ShapedOreRecipe
import net.minecraftforge.oredict.ShapelessOreRecipe

/**
 * @author kojin15.
 */
class SlotCraftingRefill(player: EntityPlayer, private val matrix: InventoryCrafting, result: IInventory, private val provider: CraftingResourcesProvider, private val container: Container, index: Int, x: Int, y: Int) :
        SlotCrafting(player, matrix, result, index, x, y) {

    private fun findMatch(stack: ItemStack, recipe: IRecipe): Int {
        provider.forEachIndexed { index, p ->
            if (p != null && OreDictionary.itemMatches(stack, p, false)) return index
        }
        provider.forEachIndexed { index, p ->
            if (p != null) {
                when (recipe) {
                    is ShapedOreRecipe -> {
                        recipe.input.forEach {
                            when (it) {
                                is ItemStack -> if (OreDictionary.itemMatches(stack, p, false)) return index
                                is ArrayList<*> -> it.forEach { instack ->
                                    instack as ItemStack?
                                    if (OreDictionary.itemMatches(instack, p, false) && OreDictionary.itemMatches(instack, stack, false)) return index
                                }
                            }
                        }
                    }
                    is ShapelessOreRecipe -> {
                        recipe.input.forEach { instack ->
                            instack as ItemStack?
                            if (OreDictionary.itemMatches(instack, p, false) && OreDictionary.itemMatches(instack, stack, false)) return index
                        }
                    }
                }
            }
        }
        return -1
    }

    override fun onPickupFromSlot(player: EntityPlayer, stack: ItemStack?) {
        val oldMatrix = (0..8).map { matrix.getStackInSlot(it)?.copy() }
        val recipe = CraftingManager.getInstance().recipeList.find { (it as IRecipe).matches(matrix, player.worldObj) } as? IRecipe
        super.onPickupFromSlot(player, stack)

        var isChanged = false
        if (recipe != null) for (i in 0..8) {
            if (oldMatrix[i] != null && matrix.getStackInSlot(i) == null) {
                val index = findMatch(oldMatrix[i]!!, recipe)
                if (index == -1) continue
                matrix.setInventorySlotContents(i, provider.decrStackSize(index, 1))
                if (!isChanged) isChanged = true
            }
        }
        if (isChanged) container.onCraftMatrixChanged(matrix)
    }
}