package com.nex3z.android.utils.image.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.fragment.app.Fragment
import com.nex3z.android.utils.image.camera.CameraFragment

class CameraActivity : AppCompatActivity(), ImageAnalysis.Analyzer {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        if (fragment is CameraFragment) {
            fragment.analyzer = this
        }
    }

    override fun analyze(image: ImageProxy) {
        Log.v(TAG, "analyze()")
        image.close()
    }

    companion object {
        private val TAG: String = CameraActivity::class.java.simpleName
    }
}