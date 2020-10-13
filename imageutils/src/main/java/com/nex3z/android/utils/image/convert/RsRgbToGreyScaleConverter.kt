package com.nex3z.android.utils.image.convert

import android.content.Context
import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicColorMatrix

class RsRgbToGreyScaleConverter(context: Context) {
    private val rs = RenderScript.create(context)

    private val script: ScriptIntrinsicColorMatrix by lazy {
        ScriptIntrinsicColorMatrix.create(rs).apply {
            setGreyscale()
        }
    }

    private lateinit var outputAllocation: Allocation

    fun convert(rgb: Bitmap, greyScale: Bitmap) {
        val inputAllocation = Allocation.createFromBitmap(rs, rgb)

        if (!this::outputAllocation.isInitialized) {
            outputAllocation = Allocation.createTyped(rs, inputAllocation.type)
        }

        script.forEach(inputAllocation, outputAllocation)
        outputAllocation.copyTo(greyScale)
    }
}