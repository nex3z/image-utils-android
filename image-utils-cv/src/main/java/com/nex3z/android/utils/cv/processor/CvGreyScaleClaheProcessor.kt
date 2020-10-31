package com.nex3z.android.utils.cv.processor

import android.graphics.Bitmap
import com.nex3z.android.utils.cv.util.toBitmap
import com.nex3z.android.utils.cv.util.toMat
import com.nex3z.android.utils.image.processor.BitmapProcessor
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

class CvGreyScaleClaheProcessor : BitmapProcessor {
    private val clahe = Imgproc.createCLAHE()

    override fun process(src: Bitmap, dst: Bitmap) {
        val srcMat = src.toMat()
        val buffer = Mat()

        Imgproc.cvtColor(srcMat, buffer, Imgproc.COLOR_RGB2GRAY)
        clahe.apply(buffer, buffer)

        buffer.toBitmap(dst)
    }
}