package ru.tech.firenote

import androidx.core.graphics.ColorUtils

object Utils {

    fun Int.blend(ratio: Float = 0.2f, with: Int = 0x000000) =
        ColorUtils.blendARGB(this, with, ratio)

}