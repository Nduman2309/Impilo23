package com.example.impilo23.ui

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.impilo23.R
import com.example.impilo23.databinding.ActivityGardenHubBinding
import com.example.impilo23.ui.dialog.AnimatedCongratsDialog
import com.example.impilo23.util.CropImageResolver
import com.example.impilo23.util.XpManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

class GardenHubActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGardenHubBinding
    private var toWaterCount = 5
    private var activeBedsCount = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGardenHubBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGardenSettings.setOnClickListener {
            finish()
        }

        updateXpUI()

        // Watering Actions
        binding.btnCrop1Water.setOnClickListener {
            binding.cardGarden1.animate().scaleX(1.04f).scaleY(1.04f).setDuration(160).withEndAction {
                binding.cardGarden1.animate().scaleX(1.0f).scaleY(1.0f).setDuration(160).start()
            }.start()

            if (toWaterCount > 0) toWaterCount--
            binding.tvToWaterCount.text = toWaterCount.toString()
            AnimatedCongratsDialog(this, "Watered Backyard Patch", 10) { updateXpUI() }.show()
        }

        binding.btnCrop2Water.setOnClickListener {
            binding.cardGarden2.animate().scaleX(1.04f).scaleY(1.04f).setDuration(160).withEndAction {
                binding.cardGarden2.animate().scaleX(1.0f).scaleY(1.0f).setDuration(160).start()
            }.start()

            if (toWaterCount > 0) toWaterCount--
            binding.tvToWaterCount.text = toWaterCount.toString()
            AnimatedCongratsDialog(this, "Watered Balcony Herbs", 10) { updateXpUI() }.show()
        }

        // Details Modals
        binding.btnCrop1Details.setOnClickListener {
            showCropDetailsDialog("Tomatoes & Basil", "Solanum lycopersicum", "Zone 9b • South Africa", "Flowering Stage", "12")
        }
        binding.cardGarden1.setOnClickListener {
            showCropDetailsDialog("Tomatoes & Basil", "Solanum lycopersicum", "Zone 9b • South Africa", "Flowering Stage", "12")
        }
        binding.cardGarden2.setOnClickListener {
            showCropDetailsDialog("Rosemary & Mint", "Salvia rosmarinus", "Potted • Indoor/Outdoor", "Establishing Stage", "45")
        }

        // Add Crop Wizard
        binding.btnAddCropPill.setOnClickListener {
            showAddCropWizardDialog()
        }
    }

    private fun updateXpUI() {
        val totalXp = XpManager.getXp(this)
        val formattedXp = if (totalXp >= 1000) String.format("%.1fk", totalXp / 1000f) else totalXp.toString()
        // Top Metric Card 3 is Garden XP
    }

    private fun showCropDetailsDialog(title: String, botanical: String, zone: String, stage: String, days: String) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_crop_details)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val tvTitle = dialog.findViewById<TextView>(R.id.tvCropDetailTitle)
        val tvBot = dialog.findViewById<TextView>(R.id.tvBotanicalName)
        val tvZone = dialog.findViewById<TextView>(R.id.tvDetailZone)
        val tvStage = dialog.findViewById<TextView>(R.id.tvDetailStage)
        val tvHarvest = dialog.findViewById<TextView>(R.id.tvDetailHarvest)
        val btnClose = dialog.findViewById<MaterialButton>(R.id.btnCloseDetails)

        tvTitle.text = title
        tvBot.text = botanical
        tvZone.text = "🌍 Region: $zone"
        tvStage.text = "🌱 Stage: $stage"
        tvHarvest.text = "🕒 Harvest Countdown: $days days remaining"

        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showAddCropWizardDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_add_crop_wizard)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val etCrop = dialog.findViewById<EditText>(R.id.etCropName)
        val etPatch = dialog.findViewById<EditText>(R.id.etPatchLocation)
        val etStage = dialog.findViewById<EditText>(R.id.etGrowthStage)
        val etDays = dialog.findViewById<EditText>(R.id.etDaysToHarvest)
        val btnSubmit = dialog.findViewById<MaterialButton>(R.id.btnSubmitAddCrop)

        btnSubmit.setOnClickListener {
            val cropName = etCrop.text.toString().trim()
            val patchName = etPatch.text.toString().trim()
            val stageName = etStage.text.toString().trim()
            val daysStr = etDays.text.toString().trim()

            if (cropName.isEmpty()) {
                etCrop.error = "Enter crop name"
                return@setOnClickListener
            }

            val botanicalName = CropImageResolver.getBotanicalName(cropName)

            // Dynamically create & append new Crop Card
            val newCard = MaterialCardView(this).apply {
                radius = 48f
                strokeWidth = 2
                setStrokeColor(Color.parseColor("#E2E8F0"))
                setCardBackgroundColor(Color.WHITE)
            }

            val container = android.widget.LinearLayout(this).apply {
                orientation = android.widget.LinearLayout.VERTICAL
                setPadding(48, 48, 48, 48)
            }

            val tvTitle = TextView(this).apply {
                text = if (patchName.isNotEmpty()) patchName else "$cropName Bed"
                textSize = 18f
                typeface = android.graphics.Typeface.DEFAULT_BOLD
                setTextColor(Color.parseColor("#1B4332"))
            }

            val tvSub = TextView(this).apply {
                text = "$cropName • $stageName (🕒 $daysStr days to harvest)"
                textSize = 14f
                setTextColor(Color.parseColor("#2D6A4F"))
            }

            val btnViewDetails = MaterialButton(this).apply {
                text = "View $cropName Inspection Sheet"
                textSize = 13f
                setOnClickListener {
                    showCropDetailsDialog(cropName, botanicalName, "Zone 9b • South Africa", stageName, daysStr)
                }
            }

            container.addView(tvTitle)
            container.addView(tvSub)
            container.addView(btnViewDetails)
            newCard.addView(container)

            // Append to main layout
            val parentLayout = binding.cardGarden2.parent as android.view.ViewGroup
            parentLayout.addView(newCard)

            activeBedsCount++
            dialog.dismiss()

            AnimatedCongratsDialog(this, "New Crop Added: $cropName", 50) {
                updateXpUI()
            }.show()
        }

        dialog.show()
    }
}
