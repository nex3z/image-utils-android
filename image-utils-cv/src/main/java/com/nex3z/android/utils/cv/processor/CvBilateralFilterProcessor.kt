package com.nex3z.android.utils.cv.processor

import android.graphics.Bitmap
import com.nex3z.android.utils.cv.util.toBitmap
import com.nex3z.android.utils.cv.util.toMat
import com.nex3z.android.utils.image.processor.BitmapProcessor
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

class CvBilateralFilterProcessor(
    private val d: Int = 5,
    private val sigmaColor: Double = 80.0,
    private val sigmaSpace: Double = 80.0,
    private val borderType: Int = Core.BORDER_DEFAULT
) : BitmapProcessor {

    override fun process(src: Bitmap, dst: Bitmap) {
        val srcMat = src.toMat()

        val rgbMat = Mat()
        Imgproc.cvtColor(srcMat, rgbMat, Imgproc.COLOR_RGBA2RGB)

        val buffer = Mat()
        Imgproc.bilateralFilter(rgbMat, buffer, d, sigmaColor, sigmaSpace, borderType)

        buffer.toBitmap(dst)
    }
}