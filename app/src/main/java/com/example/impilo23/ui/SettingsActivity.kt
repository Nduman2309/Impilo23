package com.example.impilo23.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.impilo23.databinding.ActivitySettingsBinding
import com.example.impilo23.util.ValidationUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var auth: FirebaseAuth
    private var currentUid: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        if (user == null) {
            navigateOut()
            return
        }
        currentUid = user.uid

        loadSettings()

        binding.btnSaveSettings.setOnClickListener {
            val username = binding.etSetUsername.text.toString().trim()
            val waterStr = binding.etSetWaterGoal.text.toString().trim()
            val weightStr = binding.etSetWeightGoal.text.toString().trim()
            val newPassword = binding.etSetNewPassword.text.toString().trim()

            binding.tilSetUsername.error = null
            binding.tilSetWaterGoal.error = null
            binding.tilSetWeightGoal.error = null
            binding.tilSetNewPassword.error = null

            var isValid = true

            if (username.isEmpty()) {
                binding.tilSetUsername.error = "Name cannot be empty"
                isValid = false
            }
            if (!ValidationUtils.isValidInt(waterStr)) {
                binding.tilSetWaterGoal.error = "Enter a valid water target"
                isValid = false
            }
            if (!ValidationUtils.isValidDouble(weightStr)) {
                binding.tilSetWeightGoal.error = "Enter a valid weight goal"
                isValid = false
            }
            if (newPassword.isNotEmpty() && !ValidationUtils.isValidPassword(newPassword)) {
                binding.tilSetNewPassword.error = "Password must be at least 4 characters long"
                isValid = false
            }

            if (isValid) {
                Toast.makeText(this, "Saving settings...", Toast.LENGTH_SHORT).show()

                val updateMap = HashMap<String, Any>()
                updateMap["username"] = username
                updateMap["targetWaterMl"] = waterStr.toInt()
                updateMap["weightGoalKg"] = weightStr.toDouble()

                FirebaseDatabase.getInstance().reference.child("users").child(currentUid)
                    .updateChildren(updateMap)
                    .addOnSuccessListener {
                        if (newPassword.isNotEmpty()) {
                            auth.currentUser?.updatePassword(newPassword)
                                ?.addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Toast.makeText(this, "Settings and password updated!", Toast.LENGTH_SHORT).show()
                                        finish()
                                    } else {
                                        Toast.makeText(this, "Failed to update password: " + task.exception?.message, Toast.LENGTH_LONG).show()
                                    }
                                }
                        } else {
                            Toast.makeText(this, "Settings updated successfully!", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }
            }
        }

        binding.btnLogout.setOnClickListener {
            auth.signOut()
            Toast.makeText(this, "Signed out successfully", Toast.LENGTH_SHORT).show()
            navigateOut()
        }
    }

    private fun loadSettings() {
        FirebaseDatabase.getInstance().reference.child("users").child(currentUid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        binding.etSetUsername.setText(snapshot.child("username").value?.toString() ?: "")
                        binding.etSetWaterGoal.setText(snapshot.child("targetWaterMl").value?.toString() ?: "2500")
                        binding.etSetWeightGoal.setText(snapshot.child("weightGoalKg").value?.toString() ?: "70.0")
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun navigateOut() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }
}
