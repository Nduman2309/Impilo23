package com.example.impilo23.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.impilo23.api.HealthDataResponse
import com.example.impilo23.api.RetrofitClient
import com.example.impilo23.databinding.ActivityDashboardBinding
import com.example.impilo23.util.ValidationUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var auth: FirebaseAuth
    private var currentUid: String = ""

    private var targetWaterMl = 2500
    private var weightGoalKg = 70.0

    private var currentWaterLog = 0
    private var currentWeightLog = 0.0
    private var currentHeartRateLog = 0
    private var currentSysLog = 0
    private var currentDiaLog = 0
    
    private var userListener: ValueEventListener? = null
    private var healthLogListener: ValueEventListener? = null

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

        // Start listening to cloud data immediately
        startRealtimeListeners()

        binding.btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        binding.btnSaveMetrics.setOnClickListener {
            saveDailyHealthLogs()
        }

        binding.btnFetchApi.setOnClickListener {
            fetchRestApiData()
        }

        binding.btnHealthFinder.setOnClickListener {
            startActivity(Intent(this, HealthFinderActivity::class.java))
        }

        binding.btnPlantCare.setOnClickListener {
            startActivity(Intent(this, PlantCareActivity::class.java))
        }
    }

    private fun startRealtimeListeners() {
        binding.loadingOverlay.visibility = View.VISIBLE
        val databaseRef = FirebaseDatabase.getInstance().reference
        val todayStr = getTodayDateString()

        // 1. Listen for User Profile & Goals
        userListener = databaseRef.child("users").child(currentUid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val name = snapshot.child("username").value?.toString() ?: "User"
                    binding.tvWelcome.text = "Welcome back, " + name + "!"
                    
                    targetWaterMl = snapshot.child("targetWaterMl").value?.toString()?.toIntOrNull() ?: 2500
                    weightGoalKg = snapshot.child("weightGoalKg").value?.toString()?.toDoubleOrNull() ?: 70.0
                    
                    updateUI()
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })

        // 2. Listen for Daily Health Logs
        healthLogListener = databaseRef.child("health_logs").child(currentUid).child(todayStr).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(logSnapshot: DataSnapshot) {
                if (logSnapshot.exists()) {
                    currentWaterLog = logSnapshot.child("waterMl").value?.toString()?.toIntOrNull() ?: 0
                    currentWeightLog = logSnapshot.child("weightKg").value?.toString()?.toDoubleOrNull() ?: 0.0
                    currentHeartRateLog = logSnapshot.child("heartRateBpm").value?.toString()?.toIntOrNull() ?: 0
                    currentSysLog = logSnapshot.child("bloodPressureSys").value?.toString()?.toIntOrNull() ?: 0
                    currentDiaLog = logSnapshot.child("bloodPressureDia").value?.toString()?.toIntOrNull() ?: 0
                } else {
                    currentWaterLog = 0
                    currentWeightLog = 0.0
                    currentHeartRateLog = 0
                    currentSysLog = 0
                    currentDiaLog = 0
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
        binding.tvStatusWater.text = "Water Intake: " + currentWaterLog + " / " + targetWaterMl + " ml"
        val weightStr = if (currentWeightLog > 0) currentWeightLog.toString() + " kg" else "Not logged yet"
        binding.tvStatusWeight.text = "Weight Status: " + weightStr + " (Goal Target: " + weightGoalKg + " kg)"

        if (currentHeartRateLog > 0) {
            binding.tvStatusVitals.text = "Vitals Level: HR " + currentHeartRateLog + " bpm | Blood Pressure: " + currentSysLog + "/" + currentDiaLog + " mmHg"
        } else {
            binding.tvStatusVitals.text = "Vitals Level: HR -- bpm | Blood Pressure: --/-- mmHg"
        }
    }

    private fun saveDailyHealthLogs() {
        val waterStr = binding.etLogWater.text.toString().trim()
        val weightStr = binding.etLogWeight.text.toString().trim()
        val hrStr = binding.etLogHeartRate.text.toString().trim()
        val sysStr = binding.etLogSys.text.toString().trim()
        val diaStr = binding.etLogDia.text.toString().trim()

        if (waterStr.isEmpty() && weightStr.isEmpty() && hrStr.isEmpty() && sysStr.isEmpty() && diaStr.isEmpty()) {
            Toast.makeText(this, "Please enter some data to save", Toast.LENGTH_SHORT).show()
            return
        }

        // We use local variables for calculations to avoid overwrite glitches
        var newWater = currentWaterLog
        var newWeight = currentWeightLog
        var newHR = currentHeartRateLog
        var newSys = currentSysLog
        var newDia = currentDiaLog

        if (waterStr.isNotEmpty() && ValidationUtils.isValidInt(waterStr)) {
            newWater += waterStr.toInt()
        }

        if (weightStr.isNotEmpty() && ValidationUtils.isValidDouble(weightStr)) {
            newWeight = weightStr.toDouble()
        }

        if (hrStr.isNotEmpty() && ValidationUtils.isValidInt(hrStr)) newHR = hrStr.toInt()
        if (sysStr.isNotEmpty() && ValidationUtils.isValidInt(sysStr)) newSys = sysStr.toInt()
        if (diaStr.isNotEmpty() && ValidationUtils.isValidInt(diaStr)) newDia = diaStr.toInt()

        val logMap = HashMap<String, Any>()
        logMap["waterMl"] = newWater
        logMap["weightKg"] = newWeight
        logMap["heartRateBpm"] = newHR
        logMap["bloodPressureSys"] = newSys
        logMap["bloodPressureDia"] = newDia

        val todayStr = getTodayDateString()
        FirebaseDatabase.getInstance().reference.child("health_logs").child(currentUid).child(todayStr)
            .setValue(logMap)
            .addOnSuccessListener {
                Toast.makeText(this, "Health log updated successfully!", Toast.LENGTH_SHORT).show()
                binding.etLogWater.text?.clear()
                binding.etLogWeight.text?.clear()
                binding.etLogHeartRate.text?.clear()
                binding.etLogSys.text?.clear()
                binding.etLogDia.text?.clear()
            }
    }

    private fun fetchRestApiData() {
        val country = binding.etCountryQuery.text.toString().trim()
        if (country.isEmpty()) {
            binding.tvApiResponse.text = "Please enter a country name"
            return
        }

        binding.tvApiResponse.text = "Fetching data..."

        RetrofitClient.instance.getCountryStats(country).enqueue(object : Callback<HealthDataResponse> {
            override fun onResponse(call: Call<HealthDataResponse>, response: Response<HealthDataResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val data = response.body()!!
                    val reportText = "🌍 Live Regional Statistics for: " + (data.countryName ?: country) + "\n" +
                            "━━━━━━━━━━━━━━━━━━━━━━━━\n" +
                            "• Total Accumulated Cases: " + data.totalCases + "\n" +
                            "• Active Running Incidents: " + data.activeCases + "\n" +
                            "• Today's Registered Cases: " + data.todayCases + "\n" +
                            "• Total Registered Deaths: " + data.totalDeaths + "\n" +
                            "• Critical Care Patients: " + data.criticalCases + "\n" +
                            "• Total Patient Recoveries: " + data.totalRecovered + "\n" +
                            "• General Population Scope: " + data.populationSize
                    binding.tvApiResponse.text = reportText
                } else {
                    binding.tvApiResponse.text = "Error: Unable to find stats for this country."
                }
            }

            override fun onFailure(call: Call<HealthDataResponse>, t: Throwable) {
                binding.tvApiResponse.text = "Network Error: " + t.message
            }
        })
    }

    private fun getTodayDateString(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }

    override fun onDestroy() {
        super.onDestroy()
        // Clean up listeners to prevent memory leaks
        val databaseRef = FirebaseDatabase.getInstance().reference
        val todayStr = getTodayDateString()
        userListener?.let { databaseRef.child("users").child(currentUid).removeEventListener(it) }
        healthLogListener?.let { databaseRef.child("health_logs").child(currentUid).child(todayStr).removeEventListener(it) }
    }
}
