package com.example.impilo23.ui

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.impilo23.databinding.ActivityPlantCareBinding
import com.example.impilo23.ui.dialog.AnimatedCongratsDialog

class PlantCareActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlantCareBinding
    private var completedCount = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlantCareBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }

        // Search Filter
        binding.etSearchGuides.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterChapters(s.toString().trim().lowercase())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Category Chips
        binding.chipAll.setOnClickListener { filterChapters("") }
        binding.chipSoil.setOnClickListener { filterChapters("soil") }
        binding.chipWater.setOnClickListener { filterChapters("water") }
        binding.chipSun.setOnClickListener { filterChapters("sun") }
        binding.chipPest.setOnClickListener { filterChapters("pest") }
        binding.chipHarvest.setOnClickListener { filterChapters("harvest") }

        setupChapterCheckboxes()
        setupWateringCalculator()
    }

    private fun filterChapters(query: String) {
        val ch1Match = query.isEmpty() || "soil compost organic drainage aeration".contains(query) || "chapter 1".contains(query)
        val ch2Match = query.isEmpty() || "water watering deep rain damp knuckle".contains(query) || "chapter 2".contains(query)
        val ch3Match = query.isEmpty() || "sun sunlight shade UV shade cloth microclimate".contains(query) || "chapter 3".contains(query)
        val ch4Match = query.isEmpty() || "pest eco insect aphid neem oil marigold".contains(query) || "chapter 4".contains(query)
        val ch5Match = query.isEmpty() || "prune pruning sucker stem propagation propagate".contains(query) || "chapter 5".contains(query)
        val ch6Match = query.isEmpty() || "harvest storage dry herb store".contains(query) || "chapter 6".contains(query)

        binding.cardChapter1.visibility = if (ch1Match) View.VISIBLE else View.GONE
        binding.cardChapter2.visibility = if (ch2Match) View.VISIBLE else View.GONE
        binding.cardChapter3.visibility = if (ch3Match) View.VISIBLE else View.GONE
        binding.cardChapter4.visibility = if (ch4Match) View.VISIBLE else View.GONE
        binding.cardChapter5.visibility = if (ch5Match) View.VISIBLE else View.GONE
        binding.cardChapter6.visibility = if (ch6Match) View.VISIBLE else View.GONE
    }

    private fun setupChapterCheckboxes() {
        val checkBoxes = arrayOf(
            Pair(binding.cbCh1Complete, binding.cardChapter1),
            Pair(binding.cbCh2Complete, binding.cardChapter2),
            Pair(binding.cbCh3Complete, binding.cardChapter3),
            Pair(binding.cbCh4Complete, binding.cardChapter4),
            Pair(binding.cbCh5Complete, binding.cardChapter5),
            Pair(binding.cbCh6Complete, binding.cardChapter6)
        )

        for ((index, pair) in checkBoxes.withIndex()) {
            val (cb, card) = pair
            cb.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    card.animate().scaleX(1.03f).scaleY(1.03f).setDuration(150).withEndAction {
                        card.animate().scaleX(1.0f).scaleY(1.0f).setDuration(150).start()
                    }.start()
                    AnimatedCongratsDialog(this, "Masterclass Chapter ${index + 1}", 25).show()
                }
                recalculateProgress()
            }
        }
    }

    private fun recalculateProgress() {
        var count = 0
        if (binding.cbCh1Complete.isChecked) count++
        if (binding.cbCh2Complete.isChecked) count++
        if (binding.cbCh3Complete.isChecked) count++
        if (binding.cbCh4Complete.isChecked) count++
        if (binding.cbCh5Complete.isChecked) count++
        if (binding.cbCh6Complete.isChecked) count++

        completedCount = count
        val percent = (completedCount / 6f * 100f).toInt()
        binding.progressGuide.progress = percent
        binding.tvProgressText.text = "Chapter $completedCount of 6 Completed"
        binding.tvXpEarned.text = "+${completedCount * 25} XP"
    }

    private fun setupWateringCalculator() {
        binding.btnCalcGreens.setOnClickListener {
            binding.btnCalcGreens.setBackgroundColor(Color.parseColor("#52B788"))
            binding.btnCalcHerbs.setBackgroundColor(Color.parseColor("#4081C784"))
            binding.btnCalcSucculents.setBackgroundColor(Color.parseColor("#4081C784"))
            binding.tvCalcResult.text = "💧 Recommended: Water every 1-2 days (Keep soil consistently moist for juicy leaves)"
        }

        binding.btnCalcHerbs.setOnClickListener {
            binding.btnCalcGreens.setBackgroundColor(Color.parseColor("#4081C784"))
            binding.btnCalcHerbs.setBackgroundColor(Color.parseColor("#52B788"))
            binding.btnCalcSucculents.setBackgroundColor(Color.parseColor("#4081C784"))
            binding.tvCalcResult.text = "💧 Recommended: Water every 3-4 days (Allow top inch of soil to dry out between waterings)"
        }

        binding.btnCalcSucculents.setOnClickListener {
            binding.btnCalcGreens.setBackgroundColor(Color.parseColor("#4081C784"))
            binding.btnCalcHerbs.setBackgroundColor(Color.parseColor("#4081C784"))
            binding.btnCalcSucculents.setBackgroundColor(Color.parseColor("#52B788"))
            binding.tvCalcResult.text = "🌵 Recommended: Water every 10-14 days (Deep soak then dry out completely to prevent root rot)"
        }
    }
}
