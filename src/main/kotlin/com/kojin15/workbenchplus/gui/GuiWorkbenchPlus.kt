package com.kojin15.workbenchplus.gui

import com.kojin15.workbenchplus.Translator
import com.kojin15.workbenchplus.WorkbenchPlus
import com.kojin15.workbenchplus.tile.TileWorkbenchPlus
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.*
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.CraftingManager
import net.minecraft.util.ResourceLocation
import net.minecraftforge.items.SlotItemHandler

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
        fontRenderer.drawString(Translator.translateToLocal("container.workbenchplus"), 8, 78, 0x404040)
        fontRenderer.drawString(Translator.translateToLocal("container.inventory"), 8, 146, 0x404040)
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
        if (!player.world.isRemote) {
            for (i in 0..8) {
                craftMatrix.setInventorySlotContents(i, tile.removeStackFromSlot(i))
            }
        }
        addSlotToContainer(SlotCraftingRefill(player, craftMatrix, craftResult, provider, this, 0, 120, 36))

        for (i in 0..8) {
            addSlotToContainer(Slot(craftMatrix, i, 26 + (i % 3) * 18, 18 + (i / 3) * 18))
        }
        var index = 9
        for (i in 0..2) {
            for (j in 0..8) {
                addSlotToContainer(SlotItemHandler(tile.inventory, index++, 8 + j * 18, 90 + i * 18))
            }
        }

        addSlotToContainer(SlotItemHandler(tile.inventory, index, 152, 54))

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
        craftResult.setInventorySlotContents(0, CraftingManager.findMatchingResult(this.craftMatrix, player.world))
    }

    override fun canMergeSlot(stack: ItemStack, slotIn: Slot): Boolean {
        return slotIn.inventory !== this.craftResult && super.canMergeSlot(stack, slotIn)
    }

    override fun transferStackInSlot(playerIn: EntityPlayer?, index: Int): ItemStack {
        val slot = inventorySlots[index]
        if (!slot.hasStack || slot.stack.isEmpty) return ItemStack.EMPTY
        val itemStack = slot.stack
        val itemStackOrig = itemStack.copy()

        if (index == 0) {
            if (!mergeItemStack(itemStack, 10, 37, false)) {
                if (!mergeItemStack(itemStack, 38, 74, false)) {
                    return ItemStack.EMPTY
                }
            }
            slot.onSlotChange(itemStack, itemStackOrig)
        } else if (index in 1..9) {
            if (!mergeItemStack(itemStack, 10, 37, false)) {
                return ItemStack.EMPTY
            }
        } else if (index in 10..37) {
            if (!mergeItemStack(itemStack, 38, 74, false)) {
                return ItemStack.EMPTY
            }
        } else {
            if (!mergeItemStack(itemStack, 10, 37, false)) {
                return ItemStack.EMPTY
            }
        }

        if (itemStack.count == 0) {
            slot.putStack(ItemStack.EMPTY)
        } else {
            slot.onSlotChanged()
        }
        if (itemStack.count == itemStackOrig.count) return ItemStack.EMPTY
        slot.onTake(playerIn, itemStack)
        return itemStackOrig
    }
}