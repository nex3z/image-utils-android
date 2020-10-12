package com.nex3z.android.utils.image.histogram

interface HistogramCompareMetric {
    fun compare(h1: IntArray, h2: IntArray): Double
}

class Intersection : HistogramCompareMetric {
    override fun compare(h1: IntArray, h2: IntArray): Double {
        var d: Long = 0
        for (i in h1.indices) {
            d += minOf(h1[i], h2[i])
        }
        return d.toDouble() / h1.sum()
    }
}
