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

import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack

/**
 * @author kojin15.
 */
data class CraftingResourcesProvider(val provider: IInventory, val start: Int, val end: Int) {
    fun getSize() = end - start

    fun getStackInSlot(index: Int): ItemStack? {
        return provider.getStackInSlot(index + start)
    }

    fun setInventorySlotContents(index: Int, stack: ItemStack?) {
        provider.setInventorySlotContents(index + start, stack)
    }

    fun decrStackSize(index: Int, amount: Int): ItemStack? {
        return provider.decrStackSize(index + start, amount)
    }

    inline fun forEach(action: (ItemStack?) -> Unit) {
        for (i in 0 until this.getSize()) action(this.getStackInSlot(i))
    }

    inline fun forEachIndexed(action: (index: Int, ItemStack?) -> Unit) {
        for (i in 0 until this.getSize()) action(i, this.getStackInSlot(i))
    }
}