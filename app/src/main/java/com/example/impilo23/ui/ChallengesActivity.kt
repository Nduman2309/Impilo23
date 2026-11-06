package com.example.impilo23.ui

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.impilo23.R
import com.example.impilo23.databinding.ActivityChallengesBinding
import com.example.impilo23.ui.dialog.AnimatedCongratsDialog
import com.example.impilo23.util.StreakManager
import com.example.impilo23.util.XpManager

class ChallengesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChallengesBinding
    private var selectedTab = "DAILY" // DAILY, WEEKLY, BADGES

    private val xpObserver: (Int) -> Unit = {
        updateLevelHeader()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChallengesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        XpManager.addXpListener(xpObserver)
        updateLevelHeader()

        val streak = StreakManager.getStreak(this)
        binding.tvStreakVal.text = "$streak Day Streak"

        // Tab Filters
        binding.tabDaily.setOnClickListener {
            selectedTab = "DAILY"
            applyTabFilter()
        }
        binding.tabWeekly.setOnClickListener {
            selectedTab = "WEEKLY"
            applyTabFilter()
        }
        binding.tabBadges.setOnClickListener {
            selectedTab = "BADGES"
            applyTabFilter()
        }

        // Action Handlers
        binding.btnLogWaterCh1.setOnClickListener {
            binding.cardChallenge1.animate().scaleX(1.04f).scaleY(1.04f).setDuration(150).withEndAction {
                binding.cardChallenge1.animate().scaleX(1.0f).scaleY(1.0f).setDuration(150).start()
            }.start()

            when (selectedTab) {
                "DAILY" -> {
                    binding.progressCh1.progress = 100
                    AnimatedCongratsDialog(this, "Hydration Hero", 50) {
                        updateLevelHeader()
                    }.show()
                }
                "WEEKLY" -> {
                    binding.progressCh1.progress = 100
                    AnimatedCongratsDialog(this, "5k Community Walk", 150) {
                        updateLevelHeader()
                    }.show()
                }
                "BADGES" -> {
                    Toast.makeText(this, "Master Grower Badge Unlocked! 🌟", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnCompletedCh2.setOnClickListener {
            binding.cardChallenge2.animate().scaleX(1.04f).scaleY(1.04f).setDuration(150).withEndAction {
                binding.cardChallenge2.animate().scaleX(1.0f).scaleY(1.0f).setDuration(150).start()
            }.start()

            when (selectedTab) {
                "DAILY" -> Toast.makeText(this, "Garden Walk Already Completed! 🌟", Toast.LENGTH_SHORT).show()
                "WEEKLY" -> {
                    binding.progressCh2.progress = 100
                    AnimatedCongratsDialog(this, "Garden Bed Expansion", 200) {
                        updateLevelHeader()
                    }.show()
                }
                "BADGES" -> Toast.makeText(this, "Hydration Legend Badge Unlocked! 💧", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnStartPlantingCh3.setOnClickListener {
            binding.cardChallenge3.animate().scaleX(1.04f).scaleY(1.04f).setDuration(150).withEndAction {
                binding.cardChallenge3.animate().scaleX(1.0f).scaleY(1.0f).setDuration(150).start()
            }.start()

            when (selectedTab) {
                "DAILY" -> {
                    binding.progressCh3.progress = 100
                    AnimatedCongratsDialog(this, "Green Thumb", 100) {
                        updateLevelHeader()
                    }.show()
                }
                "WEEKLY" -> {
                    binding.progressCh3.progress = 100
                    AnimatedCongratsDialog(this, "Herbal Tea Harvest", 100) {
                        updateLevelHeader()
                    }.show()
                }
                "BADGES" -> Toast.makeText(this, "Harvest Queen Badge Unlocked! 👑", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateLevelHeader() {
        val totalXp = XpManager.getXp(this)
        val level = XpManager.getLevel(totalXp)
        binding.tvLevelBadge.text = "Level $level"
    }

    private fun applyTabFilter() {
        // Reset tab backgrounds
        binding.tabDaily.setBackgroundResource(if (selectedTab == "DAILY") R.drawable.bg_badge_healthy else 0)
        binding.tabDaily.setTextColor(if (selectedTab == "DAILY") Color.parseColor("#2D6A4F") else Color.parseColor("#6B7280"))

        binding.tabWeekly.setBackgroundResource(if (selectedTab == "WEEKLY") R.drawable.bg_badge_healthy else 0)
        binding.tabWeekly.setTextColor(if (selectedTab == "WEEKLY") Color.parseColor("#2D6A4F") else Color.parseColor("#6B7280"))

        binding.tabBadges.setBackgroundResource(if (selectedTab == "BADGES") R.drawable.bg_badge_healthy else 0)
        binding.tabBadges.setTextColor(if (selectedTab == "BADGES") Color.parseColor("#2D6A4F") else Color.parseColor("#6B7280"))

        when (selectedTab) {
            "DAILY" -> {
                binding.tvTasksHeader.text = "Today's Tasks"
                binding.tvTasksCompletedCount.text = "2/4 Completed"

                binding.ch1Title.text = "Hydration Hero"
                binding.progressCh1.progress = 80
                binding.btnLogWaterCh1.text = "Log Water"

                binding.ch2Title.text = "Garden Walk"
                binding.progressCh2.progress = 100
                binding.btnCompletedCh2.text = "Completed"

                binding.ch3Title.text = "Green Thumb"
                binding.progressCh3.progress = 0
                binding.btnStartPlantingCh3.text = "Start Planting"
            }
            "WEEKLY" -> {
                binding.tvTasksHeader.text = "Weekly Goals"
                binding.tvTasksCompletedCount.text = "1/3 Completed"

                binding.ch1Title.text = "5k Community Walk"
                binding.progressCh1.progress = 60
                binding.btnLogWaterCh1.text = "Log Steps"

                binding.ch2Title.text = "Garden Bed Expansion"
                binding.progressCh2.progress = 40
                binding.btnCompletedCh2.text = "Expand Bed"

                binding.ch3Title.text = "Herbal Tea Harvest"
                binding.progressCh3.progress = 100
                binding.btnStartPlantingCh3.text = "Harvest Tea"
            }
            "BADGES" -> {
                binding.tvTasksHeader.text = "3D Achievement Badges"
                binding.tvTasksCompletedCount.text = "3 Unlocked"

                binding.ch1Title.text = "Master Grower 🌟"
                binding.progressCh1.progress = 100
                binding.btnLogWaterCh1.text = "View Badge"

                binding.ch2Title.text = "Hydration Legend 💧"
                binding.progressCh2.progress = 100
                binding.btnCompletedCh2.text = "View Badge"

                binding.ch3Title.text = "Harvest Queen 👑"
                binding.progressCh3.progress = 100
                binding.btnStartPlantingCh3.text = "View Badge"
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        XpManager.removeXpListener(xpObserver)
    }
}
