package com.nex3z.android.utils.cv.processor

import android.graphics.Bitmap
import com.nex3z.android.utils.cv.util.toBitmap
import com.nex3z.android.utils.cv.util.toMat
import com.nex3z.android.utils.image.processor.BitmapProcessor
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

class CvBinaryProcessor(
    private val convertToGreyScale: Boolean = true
) : BitmapProcessor {

    override fun process(src: Bitmap, dst: Bitmap) {
        val srcMat = src.toMat()
        val dstMat = Mat()

        val img = if (convertToGreyScale) {
            Imgproc.cvtColor(srcMat, dstMat, Imgproc.COLOR_RGB2GRAY)
            dstMat
        } else {
            srcMat
        }

        Imgproc.threshold(img, dstMat, 123.0, 255.0, Imgproc.THRESH_BINARY)
        dstMat.toBitmap(dst)
    }
}