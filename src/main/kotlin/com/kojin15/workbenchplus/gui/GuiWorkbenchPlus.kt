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

import com.kojin15.workbenchplus.WorkbenchPlus
import com.kojin15.workbenchplus.tile.TileWorkbenchPlus
import com.kojin15.workbenchplus.util.Translator
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.*
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.CraftingManager
import net.minecraft.util.ResourceLocation

/**
 * @author kojin15.
 */
class GuiWorkbenchPlus(tile: TileWorkbenchPlus, player: EntityPlayer) : GuiContainer(ContainerWorkbenchPlus(tile, player)) {
    init {
        ySize = 240
    }

    private val texture = ResourceLocation(WorkbenchPlus.MOD_ID, "textures/gui/guibenchplus.png")

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        drawDefaultBackground()
        super.drawScreen(mouseX, mouseY, partialTicks)
    }

    override fun drawGuiContainerForegroundLayer(mouseX: Int, mouseY: Int) {
        fontRendererObj.drawString(Translator.translateToLocal("container.workbenchplus"), 8, 78, 0x404040)
        fontRendererObj.drawString(Translator.translateToLocal("container.inventory"), 8, 146, 0x404040)
    }

    override fun drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int) {
        this.mc.textureManager.bindTexture(texture)
        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, this.xSize, this.ySize)
    }
}

class ContainerWorkbenchPlus(private val tile: TileWorkbenchPlus,private val  player: EntityPlayer) : Container() {
    private val craftMatrix = InventoryCrafting(this, 3, 3)
    private val craftResult = InventoryCraftResult()
    private val provider = CraftingResourcesProvider(tile, 9, 35)
    init {
        if (!player.worldObj.isRemote) {
            for (i in 0..8) {
                craftMatrix.setInventorySlotContents(i, tile.getStackInSlot(i))
                tile.setInventorySlotContents(i, null)
            }
        }
        addSlotToContainer(SlotCraftingRefill(player, craftMatrix, craftResult, provider, this, 0, 120, 36))

        for (i in 0..8) {
            addSlotToContainer(Slot(craftMatrix, i, 26 + (i % 3) * 18, 18 + (i / 3) * 18))
        }
        var index = 9
        for (i in 0..2) {
            for (j in 0..8) {
                addSlotToContainer(Slot(tile, index++, 8 + j * 18, 90 + i * 18))
            }
        }

        addSlotToContainer(Slot(tile, index, 152, 54))

        for (i in 0..2) {
            for (j in 0..8) {
                addSlotToContainer(Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 158 + i * 18))
            }
        }

        for (i in 0..8) {
            addSlotToContainer(Slot(player.inventory, i, 8 + i * 18, 216))
        }
        this.onCraftMatrixChanged(this.craftMatrix)
    }

    override fun canInteractWith(playerIn: EntityPlayer): Boolean = true

    override fun onCraftMatrixChanged(inventoryIn: IInventory) {
        craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, player.worldObj))
    }

    override fun func_94530_a(p_94530_1_: ItemStack?, p_94530_2_: Slot?): Boolean {
        return super.func_94530_a(p_94530_1_, p_94530_2_)
    }

    override fun transferStackInSlot(playerIn: EntityPlayer, index: Int): ItemStack? {
        val slot = inventorySlots[index] as Slot
        if (!slot.hasStack || slot.stack == null) return null
        val itemStack = slot.stack
        val itemStackOrig = itemStack.copy()

        if (index == 0) {
            if (!mergeItemStack(itemStack, 10, 37, false)) {
                if (!mergeItemStack(itemStack, 38, 74, false)) {
                    return null
                }
            }
            slot.onSlotChange(itemStack, itemStackOrig)
        } else if (index in 1..9) {
            if (!mergeItemStack(itemStack, 10, 37, false)) {
                return null
            }
        } else if (index in 10..37) {
            if (!mergeItemStack(itemStack, 38, 74, false)) {
                return null
            }
        } else {
            if (!mergeItemStack(itemStack, 10, 37, false)) {
                return null
            }
        }

        if (itemStack.stackSize == 0) {
            slot.putStack(null)
        } else {
            slot.onSlotChanged()
        }
        if (itemStack.stackSize == itemStackOrig.stackSize) return null
        slot.onPickupFromSlot(playerIn, itemStack)
        return itemStackOrig
    }
}