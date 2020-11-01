package com.nex3z.android.utils.cv.processor

import android.graphics.Bitmap
import com.nex3z.android.utils.cv.util.toBitmap
import com.nex3z.android.utils.cv.util.toMat
import com.nex3z.android.utils.image.processor.BitmapProcessor
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

class CvEqualizationProcessor : BitmapProcessor {
    override fun process(src: Bitmap, dst: Bitmap) {
        val srcMat = src.toMat()
        val dstMat = Mat()

        Imgproc.cvtColor(srcMat, dstMat, Imgproc.COLOR_BGR2GRAY)
        Imgproc.equalizeHist(dstMat, dstMat)
        dstMat.toBitmap(dst)
    }
}