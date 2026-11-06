package com.example.impilo23.ui

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.impilo23.R
import com.example.impilo23.databinding.ActivityHealthHubBinding
import com.example.impilo23.ui.dialog.AnimatedCongratsDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HealthHubActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHealthHubBinding
    private var currentWaterMl = 1850
    private var currentKcal = 1240

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHealthHubBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.progressRing.setProgress(74f, animate = true)

        binding.btnLogWaterPill.setOnClickListener {
            currentWaterMl += 250
            val percent = (currentWaterMl / 2500f * 100f).coerceAtMost(100f)
            binding.progressRing.setProgress(percent, animate = true)
            binding.tvRingSubtitle.text = "${percent.toInt()}% of your 2.5L target reached"
            binding.tvWaterStatVal.text = String.format(Locale.getDefault(), "%.1fL", currentWaterMl / 1000f)
            
            saveWaterToFirebase(currentWaterMl)
            AnimatedCongratsDialog(this, "250ml Water Intake", 10).show()
        }

        // Open Food / Meal / Activity Logger Modal
        binding.btnLogActivityPill.setOnClickListener { showLogActivityDialog() }
        binding.tvSeeAllLogs.setOnClickListener { showLogActivityDialog() }
    }

    private fun showLogActivityDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_log_activity)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnFood = dialog.findViewById<MaterialButton>(R.id.btnTypeFood)
        val btnWorkout = dialog.findViewById<MaterialButton>(R.id.btnTypeWorkout)
        val etName = dialog.findViewById<EditText>(R.id.etLogName)
        val etCat = dialog.findViewById<EditText>(R.id.etLogCategory)
        val etVal = dialog.findViewById<EditText>(R.id.etLogValue)
        val btnSave = dialog.findViewById<MaterialButton>(R.id.btnSaveActivityLog)

        var selectedType = "FOOD" // FOOD or WORKOUT

        btnFood.setOnClickListener {
            selectedType = "FOOD"
            btnFood.setBackgroundColor(Color.parseColor("#2D6A4F"))
            btnFood.setTextColor(Color.WHITE)
            btnWorkout.setBackgroundColor(Color.TRANSPARENT)
            btnWorkout.setTextColor(Color.parseColor("#1F2937"))
        }

        btnWorkout.setOnClickListener {
            selectedType = "WORKOUT"
            btnWorkout.setBackgroundColor(Color.parseColor("#2D6A4F"))
            btnWorkout.setTextColor(Color.WHITE)
            btnFood.setBackgroundColor(Color.TRANSPARENT)
            btnFood.setTextColor(Color.parseColor("#1F2937"))
        }

        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            val category = etCat.text.toString().trim()
            val valueStr = etVal.text.toString().trim()

            if (name.isEmpty()) {
                etName.error = "Enter meal / activity name"
                return@setOnClickListener
            }

            val amount = valueStr.toIntOrNull() ?: 250
            currentKcal += amount
            binding.tvBurnedStatVal.text = String.format(Locale.getDefault(), "%,d", currentKcal)

            // Inflate new log item card dynamically into layoutRecentLogsList
            val cardInflater = LayoutInflater.from(this)
            val newLogView = cardInflater.inflate(R.layout.activity_health_hub, binding.layoutRecentLogsList, false)
            
            // Or create custom dynamic card
            val card = MaterialCardView(this).apply {
                radius = 48f
                strokeWidth = 2
                setStrokeColor(Color.parseColor("#E2E8F0"))
                setCardBackgroundColor(Color.WHITE)
            }
            val container = android.widget.RelativeLayout(this).apply { setPadding(48, 48, 48, 48) }

            val tvTitle = TextView(this).apply {
                text = name
                textSize = 15f
                typeface = android.graphics.Typeface.DEFAULT_BOLD
                setTextColor(Color.parseColor("#1F2937"))
            }

            val tvCat = TextView(this).apply {
                text = if (category.isNotEmpty()) category else if (selectedType == "FOOD") "Meal • Nutrition" else "Workout Session"
                textSize = 13f
                setTextColor(Color.parseColor("#2D6A4F"))
            }

            val tvVal = TextView(this).apply {
                text = "+$amount"
                textSize = 16f
                typeface = android.graphics.Typeface.DEFAULT_BOLD
                setTextColor(Color.parseColor("#1B4332"))
            }

            val layoutVertical = android.widget.LinearLayout(this).apply {
                orientation = android.widget.LinearLayout.VERTICAL
                addView(tvTitle)
                addView(tvCat)
            }

            val paramsRight = android.widget.RelativeLayout.LayoutParams(
                android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
                android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT
            ).apply { addRule(android.widget.RelativeLayout.ALIGN_PARENT_RIGHT) }

            container.addView(layoutVertical)
            container.addView(tvVal, paramsRight)
            card.addView(container)

            binding.layoutRecentLogsList.addView(card, 0)

            dialog.dismiss()
            
            saveLogToFirebase(name, category, amount)
            AnimatedCongratsDialog(this, name, 15).show()
        }

        dialog.show()
    }

    private fun saveWaterToFirebase(waterMl: Int) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        FirebaseDatabase.getInstance().reference.child("health_logs").child(uid).child(todayStr).child("waterMl")
            .setValue(waterMl)
    }

    private fun saveLogToFirebase(name: String, category: String, amount: Int) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val logMap = HashMap<String, Any>()
        logMap["title"] = name
        logMap["category"] = category
        logMap["amount"] = amount
        logMap["timestamp"] = System.currentTimeMillis()

        FirebaseDatabase.getInstance().reference.child("health_logs").child(uid).child(todayStr).child("meals")
            .push().setValue(logMap)
    }
}
