package com.kojin15.workbenchplus.gui

import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack

/**
 * @author kojin15.
 */
data class CraftingResourcesProvider(val provider: IInventory, val start: Int, val end: Int) {
    fun getSize() = end - start

    fun getStackInSlot(index: Int): ItemStack {
        return provider.getStackInSlot(index + start)
    }

    fun setInventorySlotContents(index: Int, stack: ItemStack) {
        provider.setInventorySlotContents(index + start, stack)
    }

    fun decrStackSize(index: Int, amount: Int): ItemStack {
        return provider.decrStackSize(index + start, amount)
    }
}