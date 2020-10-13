package com.nex3z.android.utils.image.histogram

import android.content.Context
import android.graphics.Bitmap
import android.renderscript.*

class RsHistogramCalculator(context: Context) {
    private val rs = RenderScript.create(context)

    private val script: ScriptIntrinsicHistogram by lazy {
        ScriptIntrinsicHistogram.create(rs, Element.U8_4(rs))
    }

    private lateinit var outputAllocation: Allocation

    fun compute(bitmap: Bitmap, hist: IntArray) {
        val inputAllocation = Allocation.createFromBitmap(rs, bitmap)

        if (!this::outputAllocation.isInitialized) {
            outputAllocation = Allocation.createSized(rs, Element.I32_4(rs), COLOR_DEPTH)
        }

        script.setOutput(outputAllocation)
        script.forEach(inputAllocation)
        outputAllocation.copyTo(hist)
    }

    companion object {
        const val COLOR_DEPTH = 256
        const val NUM_CHANNELS = 4

        fun createHistBuffer(): IntArray = IntArray(COLOR_DEPTH * NUM_CHANNELS)
    }
}