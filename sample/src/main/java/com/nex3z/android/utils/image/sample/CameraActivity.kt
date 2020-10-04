package com.nex3z.android.utils.image.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_camera.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class CameraActivity : AppCompatActivity() {
    private val executor: ExecutorService = Executors.newSingleThreadExecutor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
    }

    override fun onResume() {
        super.onResume()
        bindCamera()
    }

    override fun onDestroy() {
        super.onDestroy()
        executor.shutdown()
    }

    private fun bindCamera() = pv_ac_preview.post {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val metrics = DisplayMetrics().also { pv_ac_preview.display.getRealMetrics(it) }
            val ratio = aspectRatio(metrics.widthPixels, metrics.heightPixels)
            val rotation = pv_ac_preview.display.rotation
            Log.v(TAG, "bindCamera(): metrics = $metrics, ratio = $ratio, rotation = $rotation")

            val cameraProvider = cameraProviderFuture.get()

            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build()

            val preview = Preview.Builder()
                .setTargetAspectRatio(ratio)
                .setTargetRotation(rotation)
                .build()

            val imageAnalysis = ImageAnalysis.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                .setTargetRotation(rotation)
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(executor, { image ->
                        image.close()
                    })
                }

            cameraProvider.unbindAll()

            try {
                val camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis)
                Log.v(TAG, "bindCamera(): sensorRotationDegrees = ${camera.cameraInfo.sensorRotationDegrees}")
                preview.setSurfaceProvider(pv_ac_preview.surfaceProvider)
            } catch (e: Exception) {
                Log.e(TAG, "bindCamera(): Failed to bind use cases", e)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    companion object{
        private val TAG: String = CameraActivity::class.java.simpleName
        private const val RATIO_4_3_VALUE = 4.0 / 3.0
        private const val RATIO_16_9_VALUE = 16.0 / 9.0

        fun aspectRatio(width: Int, height: Int): Int {
            val previewRatio = max(width, height).toDouble() / min(width, height)
            if (abs(previewRatio - RATIO_4_3_VALUE) <= abs(previewRatio - RATIO_16_9_VALUE)) {
                return AspectRatio.RATIO_4_3
            }
            return AspectRatio.RATIO_16_9
        }
    }
}