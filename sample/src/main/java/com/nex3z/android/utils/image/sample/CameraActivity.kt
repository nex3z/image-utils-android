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
import com.nex3z.android.utils.image.util.Timer
import kotlinx.android.synthetic.main.activity_camera.*
import timber.log.Timber

class CameraActivity : AppCompatActivity(), ImageAnalysis.Analyzer {
    private lateinit var rgbConverter: RsYuvToRgbConverter
    private lateinit var imageBuffer: Bitmap
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
    }

    @SuppressLint("UnsafeExperimentalUsageError")
    override fun analyze(image: ImageProxy) {
        if (!::imageBuffer.isInitialized) {
            imageBuffer = Bitmap.createBitmap(image.width, image.height, Bitmap.Config.ARGB_8888)
            rotationDegrees = image.imageInfo.rotationDegrees
            rotateMatrix.postRotate(image.imageInfo.rotationDegrees.toFloat())
            Timber.v("analyze(): image size = ${image.width} x ${image.height}, rotationDegrees = $rotationDegrees")
        }

        val timer = Timer()
        rgbConverter.yuvToRgb(
            image.image ?: throw IllegalArgumentException("Image cannot bt null"), imageBuffer)
        Timber.v("analyze(): convert time cost = ${timer.stop()}")

        val bitmap = if (rotationDegrees != 0) {
            Bitmap.createBitmap(imageBuffer, 0, 0, imageBuffer.width, imageBuffer.height,
                rotateMatrix, true)
        } else {
            imageBuffer
        }

        iv_ac_image.post {
            iv_ac_image.setImageBitmap(bitmap)
        }
        image.close()
    }
}