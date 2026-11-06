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
import com.google.android.material.button.MaterialButton

class AnimatedSuccessDialog(
    context: Context,
    private val titleText: String,
    private val messageText: String,
    private val onContinue: () -> Unit
) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_success_popup)

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(false)

        val tvTitle = findViewById<TextView>(R.id.tvSuccessTitle)
        val tvMessage = findViewById<TextView>(R.id.tvSuccessMessage)
        val btnContinue = findViewById<MaterialButton>(R.id.btnContinue)
        val ivCheck = findViewById<ImageView>(R.id.ivCheckmark)

        tvTitle.text = titleText
        tvMessage.text = messageText

        // Animated Green Checkmark Scale-Bounce Entrance
        ivCheck.scaleX = 0.1f
        ivCheck.scaleY = 0.1f
        ivCheck.animate()
            .scaleX(1.0f)
            .scaleY(1.0f)
            .setDuration(450)
            .setInterpolator(OvershootInterpolator(2.0f))
            .start()

        btnContinue.setOnClickListener {
            dismiss()
            onContinue.invoke()
        }
    }
}
