package com.nex3z.android.utils.image.processor

import android.content.Context
import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicColorMatrix

class RsRgbToGreyScaleProcessor(context: Context) : BitmapProcessor {
    private val rs = RenderScript.create(context)

    private val script: ScriptIntrinsicColorMatrix by lazy {
        ScriptIntrinsicColorMatrix.create(rs).apply {
            setGreyscale()
        }
    }

    private lateinit var outputAllocation: Allocation

    override fun process(src: Bitmap, dst: Bitmap) {
        val inputAllocation = Allocation.createFromBitmap(rs, src)

        if (!this::outputAllocation.isInitialized) {
            outputAllocation = Allocation.createTyped(rs, inputAllocation.type)
        }

        script.forEach(inputAllocation, outputAllocation)
        outputAllocation.copyTo(dst)
    }
}