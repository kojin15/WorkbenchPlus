package com.kojin15.workbenchplus.tile

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.ISidedInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.NetworkManager
import net.minecraft.network.play.server.SPacketUpdateTileEntity
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraftforge.items.ItemStackHandler

/**
 * @author kojin15.
 */
class TileWorkbenchPlus : TileEntity(), ISidedInventory {
    val inventory = ItemStackHandler(28)

    override fun writeToNBT(compound: NBTTagCompound): NBTTagCompound {
        compound.setTag("inventory", inventory.serializeNBT())
        return super.writeToNBT(compound)
    }

    override fun readFromNBT(compound: NBTTagCompound) {
        inventory.deserializeNBT(compound.getCompoundTag("inventory"))
        super.readFromNBT(compound)
    }

    override fun getUpdateTag(): NBTTagCompound {
        return writeToNBT(NBTTagCompound())
    }

    override fun getUpdatePacket(): SPacketUpdateTileEntity? {
        return SPacketUpdateTileEntity(pos, 0, updateTag)
    }

    override fun onDataPacket(net: NetworkManager, pkt: SPacketUpdateTileEntity) {
        super.onDataPacket(net, pkt)
        readFromNBT(pkt.nbtCompound)
    }

    override fun isEmpty(): Boolean {
        return (0 until inventory.slots).map { inventory.getStackInSlot(it) }.all { it.isEmpty }
    }

    override fun clear() {
        (0 until inventory.slots).forEach {
            inventory.setStackInSlot(it, ItemStack.EMPTY)
        }
    }

    override fun removeStackFromSlot(index: Int): ItemStack {
        return inventory.extractItem(index, 64, false)
    }

    override fun decrStackSize(index: Int, count: Int): ItemStack {
        return inventory.extractItem(index, count, false)
    }

    override fun setInventorySlotContents(index: Int, stack: ItemStack) {
        inventory.setStackInSlot(index, stack)
    }

    override fun getStackInSlot(index: Int): ItemStack = inventory.getStackInSlot(index)
    override fun getName(): String = "Workbench Plus"
    override fun getInventoryStackLimit(): Int = 64
    override fun openInventory(player: EntityPlayer?) {}
    override fun closeInventory(player: EntityPlayer?) {}
    override fun getField(id: Int): Int = 0
    override fun getFieldCount(): Int = 0
    override fun isItemValidForSlot(index: Int, stack: ItemStack?): Boolean = true
    override fun getSizeInventory(): Int = inventory.slots
    override fun hasCustomName(): Boolean = false
    override fun setField(id: Int, value: Int) {}
    override fun isUsableByPlayer(player: EntityPlayer): Boolean = true
    override fun getSlotsForFace(side: EnumFacing?): IntArray {
        return (0..27).toList().toIntArray()
    }

    override fun canExtractItem(index: Int, stack: ItemStack?, direction: EnumFacing?): Boolean = index == 27 && direction == EnumFacing.DOWN
    override fun canInsertItem(index: Int, itemStackIn: ItemStack?, direction: EnumFacing?): Boolean = index in (0 until 27)

}