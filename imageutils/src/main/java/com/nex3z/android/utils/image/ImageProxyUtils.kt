package com.nex3z.android.utils.image

import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.camera.core.ImageProxy
import java.nio.ByteBuffer

private const val K_MAX_CHANNEL_VALUE = 262143


fun ImageProxy.toBitmap(): Bitmap {
    val rgbBytes = IntArray(width * height)
    yuv420ToArgb8888(
        planes[0].buffer.toByteArray(),
        planes[1].buffer.toByteArray(),
        planes[2].buffer.toByteArray(),
        width,
        height,
        planes[0].rowStride,
        planes[1].rowStride,
        planes[1].pixelStride,
        rgbBytes
    )
    var bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    bitmap.setPixels(rgbBytes, 0, width, 0, 0, width, height)

    if (imageInfo.rotationDegrees != 0) {
        val matrix = Matrix()
        matrix.postRotate(imageInfo.rotationDegrees.toFloat())
        bitmap = Bitmap.createBitmap(
            bitmap,
            0,
            0,
            bitmap.width,
            bitmap.height,
            matrix,
            true
        )
    }

    return bitmap
}

fun yuv420ToArgb8888(yData: ByteArray, uData: ByteArray, vData: ByteArray, width: Int, height: Int,
                     yRowStride: Int, uvRowStride: Int, uvPixelStride: Int, out: IntArray) {
    var yp = 0
    for (j in 0 until height) {
        val pY = yRowStride * j
        val pUv = uvRowStride * (j shr 1)

        for (i in 0 until width) {
            val uvOffset = pUv + (i shr 1) * uvPixelStride
            out[yp++] = yuv2Rgb(
                0xff and yData[pY + i].toInt(),
                0xff and uData[uvOffset].toInt(),
                0xff and vData[uvOffset].toInt()
            )
        }
    }
}

private fun yuv2Rgb(y: Int, u: Int, v: Int): Int {
    val adjustY = if (y - 16 < 0) 0 else y - 16
    val adjustU = u - 128
    val adjustV = v - 128

    val y1192 = 1192 * adjustY
    var r = y1192 + 1634 * adjustV
    var g = y1192 - 833 * adjustV - 400 * adjustU
    var b = y1192 + 2066 * adjustU

    r = if (r > K_MAX_CHANNEL_VALUE) K_MAX_CHANNEL_VALUE else if (r < 0) 0 else r
    g = if (g > K_MAX_CHANNEL_VALUE) K_MAX_CHANNEL_VALUE else if (g < 0) 0 else g
    b = if (b > K_MAX_CHANNEL_VALUE) K_MAX_CHANNEL_VALUE else if (b < 0) 0 else b

    return -0x1000000 or (r shl 6 and 0xff0000) or (g shr 2 and 0xff00) or (b shr 10 and 0xff)
}

private fun ByteBuffer.toByteArray(): ByteArray {
    rewind()
    val data = ByteArray(remaining())
    get(data)
    return data
}
