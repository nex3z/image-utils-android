package com.nex3z.android.utils.cv.processor

import android.graphics.Bitmap
import com.nex3z.android.utils.cv.util.toBitmap
import com.nex3z.android.utils.cv.util.toMat
import com.nex3z.android.utils.image.processor.BitmapProcessor
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

class CvRgbClaheProcessor : BitmapProcessor {
    private val clahe = Imgproc.createCLAHE()

    override fun process(src: Bitmap, dst: Bitmap) {
        val srcMat = src.toMat()
        val buffer = Mat()

        Imgproc.cvtColor(srcMat, buffer, Imgproc.COLOR_BGR2Lab)
        val splits = mutableListOf<Mat>()
        Core.split(buffer, splits)

        clahe.apply(splits[0], buffer)
        buffer.copyTo(splits[0])
        Core.merge(splits, buffer)
        Imgproc.cvtColor(buffer, buffer, Imgproc.COLOR_Lab2BGR)

        buffer.toBitmap(dst)
    }
}