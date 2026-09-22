package com.example.impilo23.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.impilo23.databinding.ActivityHealthFinderBinding

class HealthFinderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHealthFinderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHealthFinderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSearchFacilities.setOnClickListener {
            val location = binding.etLocation.text.toString().trim().lowercase()
            if (location.isNotEmpty()) {
                searchFacilities(location)
            }
        }
    }

    private fun searchFacilities(location: String) {
        binding.tvResultsHeader.visibility = View.VISIBLE
        
        // Prototype data mapping locations to health facilities
        val results = when {
            location.contains("cape town") -> "• Groote Schuur Hospital\n• Netcare Christiaan Barnard\n• Mediclinic Cape Town\n• Somerset Hospital"
            location.contains("johannesburg") || location.contains("joburg") -> "• Charlotte Maxeke Academic\n• Netcare Milpark Hospital\n• Mediclinic Morningside\n• Helen Joseph Hospital"
            location.contains("durban") -> "• Inkosi Albert Luthuli Central\n• Netcare St Augustine's\n• Addington Hospital\n• Life Entabeni Hospital"
            location.contains("pretoria") -> "• Steve Biko Academic\n• Mediclinic Muelmed\n• Netcare Pretoria East\n• Life Wilgers Hospital"
            else -> "• Local Community Clinic\n• General Practitioner Center\n• Regional Public Hospital\n\n(No specific match for \"$location\" in prototype database. Try Cape Town or Johannesburg)"
        }
        
        binding.tvFinderResults.text = results
    }
}
