package com.example.impilo23.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.impilo23.databinding.ActivityRegisterBinding
import com.example.impilo23.util.ValidationUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnRegister.setOnClickListener {
            val username = binding.etRegUsername.text.toString().trim()
            val email = binding.etRegEmail.text.toString().trim()
            val password = binding.etRegPassword.text.toString().trim()
            val waterGoalStr = binding.etWaterGoal.text.toString().trim()
            val weightGoalStr = binding.etWeightGoal.text.toString().trim()

            binding.tilRegUsername.error = null
            binding.tilRegEmail.error = null
            binding.tilRegPassword.error = null
            binding.tilWaterGoal.error = null
            binding.tilWeightGoal.error = null

            var isValid = true

            if (username.isEmpty()) {
                binding.tilRegUsername.error = "Name cannot be empty"
                isValid = false
            }

            if (!ValidationUtils.isValidEmail(email)) {
                binding.tilRegEmail.error = "Invalid email address"
                isValid = false
            }

            if (!ValidationUtils.isValidPassword(password)) {
                binding.tilRegPassword.error = "Password must be at least 4 characters long"
                isValid = false
            }

            if (!ValidationUtils.isValidInt(waterGoalStr)) {
                binding.tilWaterGoal.error = "Enter a valid water target"
                isValid = false
            }

            if (!ValidationUtils.isValidDouble(weightGoalStr)) {
                binding.tilWeightGoal.error = "Enter a valid weight goal"
                isValid = false
            }

            if (isValid) {
                Toast.makeText(this, "Registering...", Toast.LENGTH_SHORT).show()

                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val uid = auth.currentUser?.uid
                            if (uid != null) {
                                val userMap = HashMap<String, Any>()
                                userMap["email"] = email
                                userMap["username"] = username
                                userMap["targetWaterMl"] = waterGoalStr.toInt()
                                userMap["weightGoalKg"] = weightGoalStr.toDouble()

                                FirebaseDatabase.getInstance().getReference("users").child(uid)
                                    .setValue(userMap)
                                    .addOnSuccessListener {
                                        Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
                                        val intent = Intent(this, DashboardActivity::class.java)
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                        startActivity(intent)
                                        finish()
                                    }
                            }
                        } else {
                            Toast.makeText(this, "Registration failed: " + task.exception?.message, Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }

        binding.btnGoToLogin.setOnClickListener {
            finish()
        }
    }
}
