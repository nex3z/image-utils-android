package com.nex3z.android.utils.image.sample

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.fragment.app.Fragment
import com.nex3z.android.utils.image.camera.CameraFragment
import com.nex3z.android.utils.image.convert.RsYuvToRgbConverter
import com.nex3z.android.utils.image.histogram.RsHistogramCalculator
import com.nex3z.android.utils.image.histogram.RsHistogramCalculator.Companion.createHistBuffer
import com.nex3z.android.utils.image.util.Timer
import kotlinx.android.synthetic.main.activity_histogram.*
import timber.log.Timber

class HistogramActivity : AppCompatActivity(), ImageAnalysis.Analyzer {
    private lateinit var rgbConverter: RsYuvToRgbConverter
    private lateinit var histCalc: RsHistogramCalculator
    private lateinit var imageBuffer: Bitmap
    private val histBuffer: IntArray = createHistBuffer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_histogram)
        init()
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        if (fragment is CameraFragment) {
            fragment.analyzer = this
            Timber.v("onAttachFragment(): analyzer set")
        }
    }

    private fun init() {
        rgbConverter = RsYuvToRgbConverter(this)
        histCalc = RsHistogramCalculator(this)
    }

    @SuppressLint("UnsafeExperimentalUsageError")
    override fun analyze(image: ImageProxy) {
        if (!::imageBuffer.isInitialized) {
            imageBuffer = Bitmap.createBitmap(image.width, image.height, Bitmap.Config.ARGB_8888)
            Timber.v("analyze(): size = ${image.width} x ${image.height}, rotationDegrees = ${image.imageInfo.rotationDegrees}")
        }

        rgbConverter.yuvToRgb(
            image.image ?: throw IllegalArgumentException("Image cannot bt null"), imageBuffer)

        val timer = Timer()
        histCalc.compute(imageBuffer, histBuffer)
        Timber.v("time cost = ${timer.stop()}")

        iv_ah_image.post {
            iv_ah_image.setImageBitmap(imageBuffer)
            iv_ah_image.rotation = image.imageInfo.rotationDegrees.toFloat()
            hv_ah_image.render(histBuffer)
        }
        image.close()
    }
}