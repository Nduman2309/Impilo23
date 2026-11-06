package com.example.impilo23.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.impilo23.databinding.ActivityDashboardBinding
import com.example.impilo23.ui.dialog.AnimatedCongratsDialog
import com.example.impilo23.util.StreakManager
import com.example.impilo23.util.XpManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var auth: FirebaseAuth
    private var currentUid: String = ""

    private var targetWaterMl = 2500
    private var currentWaterLog = 1800

    private var userListener: ValueEventListener? = null
    private var healthLogListener: ValueEventListener? = null

    private val xpObserver: (Int) -> Unit = { updatedXp ->
        val level = XpManager.getLevel(updatedXp)
        binding.tvXpVal.text = String.format(Locale.getDefault(), "%,d", updatedXp)
        binding.tvUserSubtitle.text = "Level $level • Master Grower"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        if (user == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }
        currentUid = user.uid

        // Dynamic Streak Calculation
        val streak = StreakManager.getStreak(this)
        binding.tvStreakVal.text = "$streak Days"

        XpManager.addXpListener(xpObserver)
        updateXpUI()

        startRealtimeListeners()

        // Hub Navigation Wiring
        binding.btnNotificationBell.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        binding.btnViewHealthHub.setOnClickListener {
            startActivity(Intent(this, HealthHubActivity::class.java))
        }
        binding.btnNavHealth.setOnClickListener {
            startActivity(Intent(this, HealthHubActivity::class.java))
        }

        binding.btnViewGardenHub.setOnClickListener {
            startActivity(Intent(this, GardenHubActivity::class.java))
        }
        binding.btnNavGarden.setOnClickListener {
            startActivity(Intent(this, GardenHubActivity::class.java))
        }

        binding.btnNavChallenges.setOnClickListener {
            startActivity(Intent(this, ChallengesActivity::class.java))
        }

        binding.btnStartQuiz.setOnClickListener {
            startActivity(Intent(this, ChallengesActivity::class.java))
        }

        setupTaskCheckboxes()
    }

    override fun onResume() {
        super.onResume()
        updateXpUI()
        val streak = StreakManager.getStreak(this)
        binding.tvStreakVal.text = "$streak Days"
    }

    private fun updateXpUI() {
        val totalXp = XpManager.getXp(this)
        val level = XpManager.getLevel(totalXp)
        binding.tvXpVal.text = String.format(Locale.getDefault(), "%,d", totalXp)
        binding.tvUserSubtitle.text = "Level $level • Master Grower"
    }

    private fun setupTaskCheckboxes() {
        binding.cbTask1.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.cardTask1.animate().scaleX(1.03f).scaleY(1.03f).setDuration(150).withEndAction {
                    binding.cardTask1.animate().scaleX(1.0f).scaleY(1.0f).setDuration(150).start()
                }.start()
                AnimatedCongratsDialog(this, "Water Spinach Patch", 20) {
                    updateXpUI()
                }.show()
            }
        }
        binding.cbTask2.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.cardTask2.animate().scaleX(1.03f).scaleY(1.03f).setDuration(150).withEndAction {
                    binding.cardTask2.animate().scaleX(1.0f).scaleY(1.0f).setDuration(150).start()
                }.start()
                AnimatedCongratsDialog(this, "Check Soil Moisture", 20) {
                    updateXpUI()
                }.show()
            }
        }
        binding.cbTask3.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.cardTask3.animate().scaleX(1.03f).scaleY(1.03f).setDuration(150).withEndAction {
                    binding.cardTask3.animate().scaleX(1.0f).scaleY(1.0f).setDuration(150).start()
                }.start()
            }
        }
    }

    private fun startRealtimeListeners() {
        binding.loadingOverlay.visibility = View.VISIBLE
        val databaseRef = FirebaseDatabase.getInstance().reference
        val todayStr = getTodayDateString()

        userListener = databaseRef.child("users").child(currentUid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val name = snapshot.child("username").value?.toString() ?: "Thabo"
                    val displayName = name.split(" ").firstOrNull() ?: "Thabo"
                    binding.tvWelcome.text = "Sawubona, $displayName"
                    
                    val initials = name.split(" ")
                        .mapNotNull { it.firstOrNull()?.toString() }
                        .take(2)
                        .joinToString("")
                        .uppercase()
                    if (initials.isNotEmpty()) {
                        binding.tvAvatarInitials.text = initials
                    }

                    targetWaterMl = snapshot.child("targetWaterMl").value?.toString()?.toIntOrNull() ?: 2500
                    
                    val firebaseXp = snapshot.child("totalXp").value?.toString()?.toIntOrNull()
                    if (firebaseXp != null) {
                        XpManager.addXp(this@DashboardActivity, firebaseXp - XpManager.getXp(this@DashboardActivity))
                    }
                    updateXpUI()
                    updateUI()
                } else {
                    val userMap = HashMap<String, Any>()
                    userMap["username"] = auth.currentUser?.displayName ?: "Thabo"
                    userMap["email"] = auth.currentUser?.email ?: "sso_user@impilo.com"
                    userMap["targetWaterMl"] = 2500
                    userMap["weightGoalKg"] = 70.0
                    userMap["totalXp"] = 2450
                    databaseRef.child("users").child(currentUid).setValue(userMap)
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })

        healthLogListener = databaseRef.child("health_logs").child(currentUid).child(todayStr).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(logSnapshot: DataSnapshot) {
                if (logSnapshot.exists()) {
                    currentWaterLog = logSnapshot.child("waterMl").value?.toString()?.toIntOrNull() ?: 1800
                } else {
                    currentWaterLog = 1800
                }
                updateUI()
                binding.loadingOverlay.visibility = View.GONE
            }
            override fun onCancelled(error: DatabaseError) {
                binding.loadingOverlay.visibility = View.GONE
            }
        })
    }

    private fun updateUI() {
        val waterL = String.format(Locale.getDefault(), "%.1fL", currentWaterLog / 1000f)
        val targetL = String.format(Locale.getDefault(), "%.1fL", targetWaterMl / 1000f)
        binding.tvSummaryWater.text = "$waterL / $targetL"
    }

    private fun getTodayDateString(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }

    override fun onDestroy() {
        super.onDestroy()
        XpManager.removeXpListener(xpObserver)
        val databaseRef = FirebaseDatabase.getInstance().reference
        val todayStr = getTodayDateString()
        userListener?.let { databaseRef.child("users").child(currentUid).removeEventListener(it) }
        healthLogListener?.let { databaseRef.child("health_logs").child(currentUid).child(todayStr).removeEventListener(it) }
    }
}
