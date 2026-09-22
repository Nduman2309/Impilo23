package com.example.impilo23.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.impilo23.databinding.ActivityPlantCareBinding

class PlantCareActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlantCareBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlantCareBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
