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

    inline fun forEach(action: (ItemStack) -> Unit) {
        for (i in 0 until this.getSize()) action(this.getStackInSlot(i))
    }

    inline fun forEachIndexed(action: (index: Int, ItemStack) -> Unit) {
        for (i in 0 until this.getSize()) action(i, this.getStackInSlot(i))
    }
}