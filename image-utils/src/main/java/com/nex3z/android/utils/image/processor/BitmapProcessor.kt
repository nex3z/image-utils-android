package com.nex3z.android.utils.image.processor

import android.graphics.Bitmap

interface BitmapProcessor {

    fun process(src: Bitmap, dst: Bitmap)

}