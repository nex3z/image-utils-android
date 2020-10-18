package com.nex3z.android.utils.image.sample

import android.app.Application
import com.nex3z.android.utils.cv.CvUtils
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        CvUtils.init()
    }
}