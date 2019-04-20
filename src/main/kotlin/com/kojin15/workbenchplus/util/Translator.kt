package com.kojin15.workbenchplus.util

import net.minecraft.util.text.TextComponentTranslation

/**
 * @author kojin15.
 */
object Translator {
    fun translateToLocal(key: String): String = TextComponentTranslation(key).formattedText
}