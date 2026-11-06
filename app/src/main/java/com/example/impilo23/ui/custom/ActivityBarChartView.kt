package com.example.impilo23.ui.custom

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator

/**
 * Rounded Vertical Bar Chart component for weekly Activity Overview (M T W T F S S).
 * Features smooth column height entrance animations matching Health Hub screenshot.
 */
class ActivityBarChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val days = arrayOf("M", "T", "W", "T", "F", "S", "S")
    private val values = floatArrayOf(0.45f, 0.70f, 0.55f, 0.90f, 0.65f, 0.78f, 0.40f) // Normalized 0..1
    private var animFraction = 0f

    private val barPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.parseColor("#2D6A4F")
    }

    private val labelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#6B7280")
        textSize = 32f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }

    init {
        startAnimation()
    }

    private fun startAnimation() {
        ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 1000L
            interpolator = DecelerateInterpolator()
            addUpdateListener {
                animFraction = it.animatedValue as Float
                invalidate()
            }
            start()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val w = width.toFloat()
        val h = height.toFloat()
        if (w <= 0f || h <= 0f) return

        val bottomPadding = 48f
        val topPadding = 20f
        val chartHeight = h - bottomPadding - topPadding

        val columnCount = values.size
        val barWidth = 36f
        val spacing = (w - (columnCount * barWidth)) / (columnCount + 1)

        for (i in values.indices) {
            val cx = spacing * (i + 1) + (barWidth * i) + (barWidth / 2f)
            val barLeft = cx - (barWidth / 2f)
            val barRight = cx + (barWidth / 2f)

            val currentHeight = chartHeight * values[i] * animFraction
            val barTop = topPadding + (chartHeight - currentHeight)
            val barBottom = topPadding + chartHeight

            val rect = RectF(barLeft, barTop, barRight, barBottom)
            canvas.drawRoundRect(rect, barWidth / 2f, barWidth / 2f, barPaint)

            // Draw day label below bar
            canvas.drawText(days[i], cx, h - 10f, labelPaint)
        }
    }
}
