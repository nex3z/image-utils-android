package com.nex3z.android.utils.cv

import android.util.Log
import org.opencv.android.OpenCVLoader

class CvUtils {
    companion object {
        private val TAG: String = CvUtils::class.java.simpleName

        fun init() {
            val loaded = OpenCVLoader.initDebug()
            Log.v(TAG, "init(): loaded = $loaded")
        }
    }
}