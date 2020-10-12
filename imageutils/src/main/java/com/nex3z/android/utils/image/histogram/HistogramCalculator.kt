package com.nex3z.android.utils.image.histogram

import android.graphics.Bitmap
import android.graphics.Color

class HistogramCalculator {
    private lateinit var imageBuffer: IntArray

    fun compute(bitmap: Bitmap, hist: IntArray) {
        if (!::imageBuffer.isInitialized) {
            imageBuffer = IntArray(bitmap.width * bitmap.height)
        }
        bitmap.getPixels(imageBuffer, 0, bitmap.width, 0, 0, bitmap.height, bitmap.height)
        hist.fill(0)
        for (pixel in imageBuffer) {
            hist[Color.red(pixel) * NUM_CHANNELS] ++
            hist[Color.green(pixel) * NUM_CHANNELS + 1] ++
            hist[Color.blue(pixel) * NUM_CHANNELS + 2] ++
            hist[Color.alpha(pixel) * NUM_CHANNELS + 3] ++
        }
    }

    companion object {
        private const val NUM_CHANNELS = 4
    }
}