package com.example.impilo23.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import android.widget.TextView
import com.example.impilo23.R
import com.example.impilo23.util.XpManager
import com.google.android.material.button.MaterialButton

class AnimatedCongratsDialog(
    context: Context,
    private val taskTitle: String,
    private val xpReward: Int,
    private val onClaim: (() -> Unit)? = null
) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_congrats_popup)

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(true)

        val tvTaskTitle = findViewById<TextView>(R.id.tvTaskCompletedTitle)
        val tvRewardXp = findViewById<TextView>(R.id.tvRewardXpBadge)
        val btnClaim = findViewById<MaterialButton>(R.id.btnClaimCongrats)
        val ivStar = findViewById<ImageView>(R.id.ivStar)

        tvTaskTitle.text = "Task Completed: $taskTitle"
        tvRewardXp.text = "+$xpReward XP Granted!"

        // Persist XP reward immediately
        XpManager.addXp(context, xpReward, taskTitle)

        // Animated Star Badge Entrance with 3D bounce
        ivStar.scaleX = 0.2f
        ivStar.scaleY = 0.2f
        ivStar.rotation = -45f
        ivStar.animate()
            .scaleX(1.0f)
            .scaleY(1.0f)
            .rotation(0f)
            .setDuration(500)
            .setInterpolator(OvershootInterpolator(2.5f))
            .start()

        btnClaim.setOnClickListener {
            dismiss()
            onClaim?.invoke()
        }
    }
}
