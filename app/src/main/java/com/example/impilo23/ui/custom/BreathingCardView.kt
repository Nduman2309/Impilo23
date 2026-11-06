package com.example.impilo23.ui.custom

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.animation.AccelerateDecelerateInterpolator
import com.google.android.material.card.MaterialCardView

/**
 * MaterialCardView subclass providing a subtle 3D breathing physics & float animation.
 * Gives StatCards and summary metrics a dimensional feel.
 */
class BreathingCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = com.google.android.material.R.attr.materialCardViewStyle
) : MaterialCardView(context, attrs, defStyleAttr) {

    private var breathingAnimator: ValueAnimator? = null
    private val baseElevationPx = 6f
    private val maxElevationPx = 18f

    init {
        cardElevation = baseElevationPx
        radius = 48f // 16dp rounded corners
        strokeWidth = 2
        setStrokeColor(android.graphics.Color.parseColor("#E2E8F0"))
        startBreathingAnimation()
    }

    private fun startBreathingAnimation() {
        breathingAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 3200L
            repeatMode = ValueAnimator.REVERSE
            repeatCount = ValueAnimator.INFINITE
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener {
                val fraction = it.animatedValue as Float
                cardElevation = baseElevationPx + (maxElevationPx - baseElevationPx) * fraction
                translationY = -4f * fraction
            }
            start()
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                animate().scaleX(0.97f).scaleY(0.97f).setDuration(120).start()
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start()
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        breathingAnimator?.cancel()
    }
}
