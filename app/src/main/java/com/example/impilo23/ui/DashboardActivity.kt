package com.example.impilo23.ui

import android.content.Intent
import android.os.Bundle
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

        loadFirebaseData()

        binding.btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        binding.btnSaveMetrics.setOnClickListener {
            saveDailyHealthLogs()
        }

        binding.btnFetchApi.setOnClickListener {
            fetchRestApiData()
        }
    }

    override fun onResume() {
        super.onResume()
        if (currentUid.isNotEmpty()) {
            loadFirebaseData()
        }
    }

    private fun loadFirebaseData() {
        val databaseRef = FirebaseDatabase.getInstance().reference
        val todayStr = getTodayDateString()

        databaseRef.child("users").child(currentUid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val name = snapshot.child("username").value?.toString() ?: "User"
                    binding.tvWelcome.text = "Welcome back, " + name + "!"
                    
                    targetWaterMl = snapshot.child("targetWaterMl").value?.toString()?.toIntOrNull() ?: 2500
                    weightGoalKg = snapshot.child("weightGoalKg").value?.toString()?.toDoubleOrNull() ?: 70.0

                    databaseRef.child("health_logs").child(currentUid).child(todayStr)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
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
                            }

                            override fun onCancelled(error: DatabaseError) {}
                        })
                }
            }

            override fun onCancelled(error: DatabaseError) {}
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

        if (waterStr.isNotEmpty()) {
            if (ValidationUtils.isValidInt(waterStr)) {
                currentWaterLog += waterStr.toInt()
            }
        }

        if (weightStr.isNotEmpty()) {
            if (ValidationUtils.isValidDouble(weightStr)) {
                currentWeightLog = weightStr.toDouble()
            }
        }

        if (hrStr.isNotEmpty() && ValidationUtils.isValidInt(hrStr)) currentHeartRateLog = hrStr.toInt()
        if (sysStr.isNotEmpty() && ValidationUtils.isValidInt(sysStr)) currentSysLog = sysStr.toInt()
        if (diaStr.isNotEmpty() && ValidationUtils.isValidInt(diaStr)) currentDiaLog = diaStr.toInt()

        val logMap = HashMap<String, Any>()
        logMap["waterMl"] = currentWaterLog
        logMap["weightKg"] = currentWeightLog
        logMap["heartRateBpm"] = currentHeartRateLog
        logMap["bloodPressureSys"] = currentSysLog
        logMap["bloodPressureDia"] = currentDiaLog

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

                updateUI()
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
}
