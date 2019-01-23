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

/**
 * @author kojin15.
 */
class SlotCraftingRefill(player: EntityPlayer, private val matrix: InventoryCrafting, result: IInventory, private val provider: CraftingResourcesProvider, private val container: Container, index: Int, x: Int, y: Int) :
        SlotCrafting(player, matrix, result, index, x, y) {

    private fun findMatch(stack: ItemStack, recipe: IRecipe): Int {
        for (i in 0 until provider.getSize()) {
            val p_stack = provider.getStackInSlot(i)
            if (!p_stack.isEmpty) {
                if (OreDictionary.itemMatches(stack, p_stack, false)) return i
                recipe.ingredients.forEach {
                    if (it.apply(stack) && it.apply(p_stack)) return i
                }
            }
        }
        return -1
    }

    override fun onTake(thePlayer: EntityPlayer, stack: ItemStack): ItemStack {
        val oldMatrix = (0..8).map { matrix.getStackInSlot(it).copy() }
        val recipe = CraftingManager.findMatchingRecipe(matrix, thePlayer.world)
        super.onTake(thePlayer, stack)

        var isChanged = false
        if (recipe != null) for (i in 0..8) {
            if (!oldMatrix[i].isEmpty && matrix.getStackInSlot(i).isEmpty) {
                val index = findMatch(oldMatrix[i], recipe)
                if (index == -1) continue
                matrix.setInventorySlotContents(i, provider.decrStackSize(index, 1))
                if (!isChanged) isChanged = true
            }
        }
        if (isChanged) container.onCraftMatrixChanged(matrix)
        return stack
    }
}