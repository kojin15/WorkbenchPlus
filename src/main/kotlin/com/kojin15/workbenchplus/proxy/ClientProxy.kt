package com.kojin15.workbenchplus.proxy

import com.kojin15.workbenchplus.CommonProxy
import net.minecraftforge.fml.relauncher.Side

/**
 * @author kojin15.
 */
class ClientProxy : CommonProxy() {
    override val side: Side
        get() = Side.CLIENT
}