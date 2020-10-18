package com.nex3z.android.utils.cv.util

import android.graphics.Bitmap
import org.opencv.android.Utils
import org.opencv.core.Mat

fun Bitmap.toMat(): Mat = Mat().apply { Utils.bitmapToMat(this@toMat, this) }

fun Mat.toBitmap(bitmap: Bitmap) = Utils.matToBitmap(this, bitmap)
