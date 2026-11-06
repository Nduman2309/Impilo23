package com.example.impilo23.ui.custom

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import kotlin.math.sin
import kotlin.random.Random

/**
 * Custom hardware-accelerated 3D Leaf Particle view.
 * Simulates gentle 3D rotating leaf particles floating across the background header.
 */
class ThreeDLeafParticleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private class Leaf(
        var x: Float,
        var y: Float,
        var speedY: Float,
        var speedX: Float,
        var size: Float,
        var rotationY: Float,
        var rotationSpeedY: Float,
        var angle: Float,
        var alpha: Int
    )

    private val leaves = ArrayList<Leaf>()
    private val leafPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.parseColor("#4081C784") // Soft translucent sage green
    }
    private val leafVeinPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 2f
        color = Color.parseColor("#602D6A4F")
    }

    private var animator: ValueAnimator? = null
    private var isInitialized = false

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w > 0 && h > 0 && !isInitialized) {
            initLeaves(w, h)
            startAnimation()
            isInitialized = true
        }
    }

    private fun initLeaves(width: Int, height: Int) {
        leaves.clear()
        for (i in 0..14) {
            leaves.add(
                Leaf(
                    x = Random.nextFloat() * width,
                    y = Random.nextFloat() * height,
                    speedY = 0.5f + Random.nextFloat() * 1.5f,
                    speedX = -0.4f + Random.nextFloat() * 0.8f,
                    size = 18f + Random.nextFloat() * 24f,
                    rotationY = Random.nextFloat() * 360f,
                    rotationSpeedY = 1.5f + Random.nextFloat() * 3.5f,
                    angle = Random.nextFloat() * 360f,
                    alpha = 100 + Random.nextInt(120)
                )
            )
        }
    }

    private fun startAnimation() {
        animator?.cancel()
        animator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 100000L
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            addUpdateListener {
                val w = width.toFloat()
                val h = height.toFloat()
                if (w <= 0f || h <= 0f) return@addUpdateListener

                for (leaf in leaves) {
                    leaf.y += leaf.speedY
                    leaf.angle += 0.02f
                    leaf.x += leaf.speedX + sin(leaf.angle.toDouble()).toFloat() * 0.8f
                    leaf.rotationY = (leaf.rotationY + leaf.rotationSpeedY) % 360f

                    if (leaf.y > h + 40f) {
                        leaf.y = -40f
                        leaf.x = Random.nextFloat() * w
                    }
                }
                invalidate()
            }
            start()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val matrix = Matrix()
        val camera = Camera()

        for (leaf in leaves) {
            leafPaint.alpha = leaf.alpha

            canvas.save()
            camera.save()

            // 3D Rotation on Y Axis to create perspective depth
            camera.rotateY(leaf.rotationY)
            camera.getMatrix(matrix)

            matrix.preTranslate(-leaf.size / 2f, -leaf.size / 2f)
            matrix.postTranslate(leaf.x, leaf.y)

            camera.restore()
            canvas.concat(matrix)

            // Draw 3D-styled leaf path
            val path = Path().apply {
                moveTo(0f, -leaf.size)
                quadTo(leaf.size, -leaf.size / 2f, 0f, leaf.size)
                quadTo(-leaf.size, -leaf.size / 2f, 0f, -leaf.size)
                close()
            }
            canvas.drawPath(path, leafPaint)
            canvas.drawLine(0f, -leaf.size, 0f, leaf.size, leafVeinPaint)

            canvas.restore()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator?.cancel()
    }
}
