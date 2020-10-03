package com.nex3z.android.utils.image.sample

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nex3z.android.utils.image.REQUEST_CODE
import com.nex3z.android.utils.image.hasCameraPermissions
import com.nex3z.android.utils.image.requestCameraPermissions
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        if (!hasCameraPermissions()) {
            requestCameraPermissions()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "onRequestPermissionsResult(): Permission granted")
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun initView() {
        btn_am_camera.setOnClickListener {
            Intent(this, CameraActivity::class.java).apply(this::startActivity)
        }
    }

    companion object {
        private val TAG: String = MainActivity::class.java.simpleName
    }
}