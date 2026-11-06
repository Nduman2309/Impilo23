package com.example.impilo23.ui.custom

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator

/**
 * Dimensional 3D Circular Hydration Progress Indicator with radial depth gradient
 * and smooth sweep fill animation. Matches Health Hub & Dashboard design screenshots.
 */
class DimensionalProgressRing @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var progressPercentage: Float = 75f
    private var animatedProgress: Float = 0f

    private val trackPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 24f
        strokeCap = Paint.Cap.ROUND
        color = Color.parseColor("#E2E8F0")
    }

    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 24f
        strokeCap = Paint.Cap.ROUND
        color = Color.parseColor("#2D6A4F")
    }

    private val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 28f
        strokeCap = Paint.Cap.ROUND
        color = Color.parseColor("#15000000")
    }

    private val rectF = RectF()
    private var animator: ValueAnimator? = null

    fun setProgress(percent: Float, animate: Boolean = true) {
        val target = percent.coerceIn(0f, 100f)
        progressPercentage = target

        if (animate) {
            animator?.cancel()
            animator = ValueAnimator.ofFloat(animatedProgress, target).apply {
                duration = 1200L
                interpolator = DecelerateInterpolator()
                addUpdateListener {
                    animatedProgress = it.animatedValue as Float
                    invalidate()
                }
                start()
            }
        } else {
            animatedProgress = target
            invalidate()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val padding = 36f
        rectF.set(padding, padding, w.toFloat() - padding, h.toFloat() - padding)

        // Apply a subtle 3D linear gradient depth to the stroke
        val shader = LinearGradient(
            0f, 0f, w.toFloat(), h.toFloat(),
            intArrayOf(
                Color.parseColor("#40916C"),
                Color.parseColor("#2D6A4F"),
                Color.parseColor("#1B4332")
            ),
            floatArrayOf(0f, 0.5f, 1f),
            Shader.TileMode.CLAMP
        )
        progressPaint.shader = shader
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw track base
        canvas.drawArc(rectF, -90f, 360f, false, trackPaint)

        // Draw 3D soft shadow line under progress
        val sweepAngle = (animatedProgress / 100f) * 360f
        if (sweepAngle > 0f) {
            canvas.drawArc(rectF, -88f, sweepAngle, false, shadowPaint)
            canvas.drawArc(rectF, -90f, sweepAngle, false, progressPaint)
        }
    }
}
