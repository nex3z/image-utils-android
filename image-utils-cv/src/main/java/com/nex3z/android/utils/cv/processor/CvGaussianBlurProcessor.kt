package com.nex3z.android.utils.cv.processor

import android.graphics.Bitmap
import com.nex3z.android.utils.cv.util.toBitmap
import com.nex3z.android.utils.cv.util.toMat
import com.nex3z.android.utils.image.processor.BitmapProcessor
import org.opencv.core.Mat
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc

class CvGaussianBlurProcessor : BitmapProcessor {
    override fun process(src: Bitmap, dst: Bitmap) {
        val srcMat = src.toMat()
        val buffer = Mat()

        Imgproc.GaussianBlur(srcMat, buffer, Size(15.0, 15.0), 0.0)

        buffer.toBitmap(dst)
    }
}