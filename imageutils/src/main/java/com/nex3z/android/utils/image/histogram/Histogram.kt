package com.nex3z.android.utils.image.histogram

import android.graphics.Bitmap
import android.graphics.Color

data class Histogram(
    val redBins: FloatArray,
    val greenBins: FloatArray,
    val blueBins: FloatArray
) {

    override fun toString(): String {
        return "R(${redBins.contentToString()}), \n" +
                "G${redBins.contentToString()}), \n" +
                "B${redBins.contentToString()})"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Histogram

        if (!redBins.contentEquals(other.redBins)) return false
        if (!greenBins.contentEquals(other.greenBins)) return false
        if (!blueBins.contentEquals(other.blueBins)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = redBins.contentHashCode()
        result = 31 * result + greenBins.contentHashCode()
        result = 31 * result + blueBins.contentHashCode()
        return result
    }
}


fun Bitmap.computeHistogram(): Histogram {
    val size = width * height
    val pixels = IntArray(size)
    getPixels(pixels, 0, width, 0, 0, width, height)

    val redBins = FloatArray(256) {0.0f}
    val greenBins = FloatArray(256) {0.0f}
    val blueBins = FloatArray(256) {0.0f}
    for (pixel in pixels) {
        val red = Color.red(pixel)
        val green = Color.green(pixel)
        val blue = Color.blue(pixel)
        redBins[red] += 1.0f
        greenBins[green] += 1.0f
        blueBins[blue] += 1.0f
    }

    val floatSize = size.toFloat()
    for (i in 0..255) {
        redBins[i] /= floatSize
        greenBins[i] /= floatSize
        blueBins[i] /= floatSize
    }

    return Histogram(redBins, greenBins, blueBins)
}
