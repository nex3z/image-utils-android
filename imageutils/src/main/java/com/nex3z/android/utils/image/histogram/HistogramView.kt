package com.nex3z.android.utils.image.histogram

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

class HistogramView(
    context: Context,
    attrs: AttributeSet? = null
): View(context, attrs) {
    private val redPaint: Paint = buildPaint(Color.RED)
    private val greenPaint: Paint = buildPaint(Color.GREEN)
    private val bluePaint: Paint = buildPaint(Color.BLUE)
    private var histogram: IntArray? = null

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) { return }
        histogram?.run {
            draw(canvas, redPaint, slice(0 until size step 4))
            draw(canvas, greenPaint, slice(1 until size step 4))
            draw(canvas, bluePaint, slice(2 until size step 4))
        }
    }

    private fun draw(canvas: Canvas, paint: Paint, color: List<Int>) {
        Path().apply {
            moveTo(paddingLeft.toFloat(), height - paddingBottom.toFloat())

            val graphWidth = width - paddingLeft - paddingRight
            val graphHeight = height - paddingTop - paddingBottom
            val maxValue = color.max()?.toFloat() ?: return
            val numColor = color.size.toFloat()

            for (i in color.indices) {
                val x = (graphWidth * i.toFloat() / numColor) + paddingLeft
                val y = (graphHeight - (graphHeight * color[i] / maxValue)) + paddingBottom
                lineTo(x, y)
            }
            canvas.drawPath(this, paint)
        }
    }

    fun render(hist: IntArray) {
        this.histogram = hist
        invalidate()
    }

    companion object {
        fun buildPaint(color: Int): Paint = Paint().apply {
            this.color = color
            style = Paint.Style.STROKE
            strokeWidth = 2f
        }
    }
}
