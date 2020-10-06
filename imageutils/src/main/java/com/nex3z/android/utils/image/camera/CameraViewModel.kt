package com.nex3z.android.utils.image.camera

import androidx.camera.core.ImageProxy
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CameraViewModel : ViewModel() {
    val image: MutableLiveData<ImageProxy> = MutableLiveData()
}