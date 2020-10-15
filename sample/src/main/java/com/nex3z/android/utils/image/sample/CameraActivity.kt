package com.nex3z.android.utils.image.sample

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.fragment.app.Fragment
import com.nex3z.android.utils.image.camera.CameraFragment
import com.nex3z.android.utils.image.convert.RsYuvToRgbConverter
import com.nex3z.android.utils.image.processor.RsRgbToGreyScaleProcessor
import com.nex3z.android.utils.image.util.Timer
import kotlinx.android.synthetic.main.activity_camera.*
import timber.log.Timber

class CameraActivity : AppCompatActivity(), ImageAnalysis.Analyzer {
    private lateinit var rgbConverter: RsYuvToRgbConverter
    private lateinit var greyScaleProcessor: RsRgbToGreyScaleProcessor
    private lateinit var rgbImageBuffer: Bitmap
    private lateinit var greyScaleImageBuffer: Bitmap
    private var rotationDegrees: Int = 0
    private val rotateMatrix: Matrix = Matrix()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        init()
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        if (fragment is CameraFragment) {
            fragment.analyzer = this
        }
    }

    private fun init() {
        rgbConverter = RsYuvToRgbConverter(this)
        greyScaleProcessor = RsRgbToGreyScaleProcessor(this)
    }

    @SuppressLint("UnsafeExperimentalUsageError")
    override fun analyze(image: ImageProxy) {
        if (!::rgbImageBuffer.isInitialized) {
            rgbImageBuffer = Bitmap.createBitmap(image.width, image.height, Bitmap.Config.ARGB_8888)
            greyScaleImageBuffer = Bitmap.createBitmap(image.width, image.height, Bitmap.Config.ARGB_8888)
            rotationDegrees = image.imageInfo.rotationDegrees
            rotateMatrix.postRotate(image.imageInfo.rotationDegrees.toFloat())
            Timber.v("analyze(): image size = ${image.width} x ${image.height}, rotationDegrees = $rotationDegrees")
        }

        image.image?.let {
            val timer = Timer()
            rgbConverter.yuvToRgb(it, rgbImageBuffer)
            Timber.v("analyze(): rgb convert time cost = ${timer.delta()}")
            greyScaleProcessor.process(rgbImageBuffer, greyScaleImageBuffer)
            Timber.v("analyze(): grey scale convert time cost = ${timer.delta()}")

            iv_ac_rgb.post {
                iv_ac_rgb.setImageBitmap(rotate(rgbImageBuffer))
                iv_ac_grey.setImageBitmap(rotate(greyScaleImageBuffer))
            }
        }

        image.close()
    }

    private fun rotate(image: Bitmap): Bitmap {
        return if (rotationDegrees != 0) {
            Bitmap.createBitmap(image, 0, 0, image.width, image.height, rotateMatrix, true)
        } else {
            image
        }
    }
}