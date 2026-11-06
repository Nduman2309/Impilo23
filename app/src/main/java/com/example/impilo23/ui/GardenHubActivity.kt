package com.example.impilo23.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.impilo23.databinding.ActivityGardenHubBinding
import com.example.impilo23.ui.dialog.AnimatedCongratsDialog

class GardenHubActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGardenHubBinding
    private var toWaterCount = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGardenHubBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGardenSettings.setOnClickListener {
            finish()
        }

        binding.btnCrop1Water.setOnClickListener {
            binding.cardGarden1.animate().scaleX(1.04f).scaleY(1.04f).setDuration(160).withEndAction {
                binding.cardGarden1.animate().scaleX(1.0f).scaleY(1.0f).setDuration(160).start()
            }.start()

            if (toWaterCount > 0) toWaterCount--
            binding.tvToWaterCount.text = toWaterCount.toString()
            AnimatedCongratsDialog(this, "Watered Backyard Patch", 10).show()
        }

        binding.btnCrop2Water.setOnClickListener {
            binding.cardGarden2.animate().scaleX(1.04f).scaleY(1.04f).setDuration(160).withEndAction {
                binding.cardGarden2.animate().scaleX(1.0f).scaleY(1.0f).setDuration(160).start()
            }.start()

            if (toWaterCount > 0) toWaterCount--
            binding.tvToWaterCount.text = toWaterCount.toString()
            AnimatedCongratsDialog(this, "Watered Balcony Herbs", 10).show()
        }

        binding.btnAddCropPill.setOnClickListener {
            Toast.makeText(this, "Opening Add Crop Wizard...", Toast.LENGTH_SHORT).show()
        }
    }
}
