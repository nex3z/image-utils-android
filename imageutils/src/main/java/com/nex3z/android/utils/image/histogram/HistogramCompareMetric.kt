package com.nex3z.android.utils.image.histogram

interface HistogramCompareMetric {
    fun compare(h1: Histogram, h2: Histogram): Double
}

class Intersection : HistogramCompareMetric {
    override fun compare(h1: Histogram, h2: Histogram): Double {
        var d = 0.0
        for (i in h1.redBins.indices) {
            d += minOf(
                minOf(h1.redBins[i], h2.redBins[i]),
                minOf(h1.greenBins[i], h2.greenBins[i]),
                minOf(h1.blueBins[i], h2.blueBins[i])
            )
        }
        return d
    }
}
