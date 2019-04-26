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

package com.kojin15.workbenchplus.tile

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.ISidedInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList
import net.minecraft.network.NetworkManager
import net.minecraft.network.Packet
import net.minecraft.network.play.server.S35PacketUpdateTileEntity
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.common.util.ForgeDirection
import kotlin.experimental.and

/**
 * @author kojin15.
 */
class TileWorkbenchPlus : TileEntity(), ISidedInventory {
    private var inventory = arrayOfNulls<ItemStack>(28)

    override fun writeToNBT(nbt: NBTTagCompound) {
        super.writeToNBT(nbt)
        val tagList = NBTTagList()
        inventory.forEachIndexed { index, stack ->
            if (stack != null) {
                val tagCompound = NBTTagCompound()
                tagCompound.setInteger("Slot", index)
                stack.writeToNBT(tagCompound)
                tagList.appendTag(tagCompound)
            }
        }
        nbt.setTag("Items", tagList)
    }

    override fun readFromNBT(nbt: NBTTagCompound) {
        super.readFromNBT(nbt)
        val tagList = nbt.getTagList("Items", 10)
        inventory = arrayOfNulls(this.sizeInventory)
        for (i in 0 until tagList.tagCount()) {
            val tagCompound = tagList.getCompoundTagAt(i)
            val j: Int = tagCompound.getInteger("Slot")
            if (j in 0 until this.inventory.size) inventory[j] = ItemStack.loadItemStackFromNBT(tagCompound)
        }
    }

    override fun getDescriptionPacket(): Packet {
        val tagCompound = NBTTagCompound()
        this.writeToNBT(tagCompound)
        return S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, tagCompound)
    }

    override fun onDataPacket(net: NetworkManager?, pkt: S35PacketUpdateTileEntity?) {
        this.readFromNBT(pkt?.func_148857_g() ?: NBTTagCompound())
    }

    override fun decrStackSize(index: Int, count: Int): ItemStack? {
        this.inventory[index] ?: return null
        val stack: ItemStack?
        if (this.inventory[index]!!.stackSize <= count) {
            stack = this.inventory[index]
            this.inventory[index] = null
            this.markDirty()
            return stack
        } else {
            stack = this.inventory[index]!!.splitStack(count)
            if (this.inventory[index]!!.stackSize == 0) {
                this.inventory[index] = null
            }
            this.markDirty()
            return stack
        }
    }

    override fun setInventorySlotContents(index: Int, stack: ItemStack?) {
        this.inventory[index] = stack
        if (stack != null && stack.stackSize > this.inventoryStackLimit) {
            stack.stackSize = this.inventoryStackLimit
        }
        this.markDirty()
    }

    override fun getStackInSlot(index: Int): ItemStack? = inventory[index]

    override fun getStackInSlotOnClosing(index: Int): ItemStack? {
        val stack = this.inventory[index] ?: return null
        this.inventory[index] = null
        return stack
    }

    override fun getInventoryName(): String = "Workbench Plus"
    override fun getInventoryStackLimit(): Int = 64
    override fun openInventory() = Unit
    override fun closeInventory() = Unit
    override fun isItemValidForSlot(index: Int, stack: ItemStack?): Boolean = true
    override fun getSizeInventory(): Int = inventory.size
    override fun hasCustomInventoryName(): Boolean = false
    override fun isUseableByPlayer(player: EntityPlayer?): Boolean = true

    override fun getAccessibleSlotsFromSide(direction: Int): IntArray =
            (0..27).toList().toIntArray()

    override fun canInsertItem(index: Int, stack: ItemStack?, direction: Int): Boolean =
            index == 27 && ForgeDirection.getOrientation(direction) == ForgeDirection.DOWN

    override fun canExtractItem(index: Int, itemStackIn: ItemStack?, direction: Int): Boolean =
            index in 0 until 27
}