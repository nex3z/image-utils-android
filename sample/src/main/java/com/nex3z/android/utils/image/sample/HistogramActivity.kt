package com.nex3z.android.utils.image.sample

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.fragment.app.Fragment
import com.nex3z.android.utils.image.convert.YuvToRgbConverter
import com.nex3z.android.utils.image.camera.CameraFragment
import com.nex3z.android.utils.image.histogram.computeHistogram
import com.nex3z.android.utils.image.convert.toBitmap
import kotlinx.android.synthetic.main.activity_histogram.*

class HistogramActivity : AppCompatActivity(), ImageAnalysis.Analyzer {
    private lateinit var converter: YuvToRgbConverter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_histogram)
        converter = YuvToRgbConverter(this)
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        if (fragment is CameraFragment) {
            fragment.analyzer = this
            Log.v(TAG, "onAttachFragment(): analyzer set")
        }
    }

    override fun analyze(image: ImageProxy) {
        val bitmap = image.toBitmap()
        val histogram = bitmap.computeHistogram()
        iv_ah_image.post {
            iv_ah_image.setImageBitmap(bitmap)
            hv_ah_image.render(histogram)
        }
        image.close()
    }

    companion object {
        private val TAG: String = HistogramActivity::class.java.simpleName
    }
}