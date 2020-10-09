package com.nex3z.android.utils.image.histogram

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.View

class HistogramView(
    context: Context,
    attrs: AttributeSet? = null
): View(context, attrs) {
    private val redPaint: Paint = buildPaint(Color.RED)
    private val greenPaint: Paint = buildPaint(Color.GREEN)
    private val bluePaint: Paint = buildPaint(Color.BLUE)
    private var histogram: Histogram? = null

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) { return }
        histogram?.run {
            val maxValue = maxOf(redBins.max(), greenBins.max(), blueBins.max())
            draw(canvas, redPaint, redBins, maxValue)
            draw(canvas, greenPaint, greenBins, maxValue)
            draw(canvas, bluePaint, blueBins, maxValue)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val contentWidth = if (suggestedMinimumWidth > 256) suggestedMinimumWidth else 256
        val contentHeight = if (suggestedMinimumHeight > 256) suggestedMinimumHeight else 256

        val desiredWidth = contentWidth + paddingLeft + paddingRight
        val desiredHeight = contentHeight + paddingTop + paddingBottom

        val widthSpecs = resolveSizeAndState(desiredWidth, widthMeasureSpec, 0)
        val heightSpecs = resolveSizeAndState(desiredHeight, heightMeasureSpec, 0)

        setMeasuredDimension(widthSpecs, heightSpecs)
    }

    private fun draw(canvas: Canvas, paint: Paint, color: FloatArray, maxValue: Float) {
        Path().apply {
            moveTo(paddingLeft.toFloat(), height - paddingBottom.toFloat())

            val graphWidth = width - paddingLeft - paddingRight
            val graphHeight = height - paddingTop - paddingBottom

            for (i in color.indices) {
                val x = (graphWidth * i.toFloat() / color.size) + paddingLeft
                val y = (graphHeight - (graphHeight * color[i] / maxValue)) + paddingBottom
                lineTo(x, y)
            }
            canvas.drawPath(this, paint)
        }
    }

    fun render(hist: Histogram) {
        this.histogram = hist
        Log.v("XXX", "hist = $hist")
        invalidate()
    }

    companion object {
        fun buildPaint(color: Int): Paint = Paint().apply {
            this.color = color
            style = Paint.Style.STROKE
            strokeWidth = 2f
        }

        private fun FloatArray.max() = this.maxOrNull()
            ?: throw IllegalArgumentException("Array cannot be empty")
    }
}
