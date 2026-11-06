package com.example.impilo23.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.impilo23.databinding.ActivityLoginBinding
import com.example.impilo23.ui.dialog.AnimatedSuccessDialog
import com.example.impilo23.util.ValidationUtils
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        if (auth.currentUser != null) {
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
            return
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            binding.tilEmail.error = null
            binding.tilPassword.error = null

            var isValid = true

            if (!ValidationUtils.isValidEmail(email)) {
                binding.tilEmail.error = "Please enter a valid email address"
                isValid = false
            }

            if (!ValidationUtils.isValidPassword(password)) {
                binding.tilPassword.error = "Password must be at least 4 characters long"
                isValid = false
            }

            if (isValid) {
                Toast.makeText(this, "Logging in...", Toast.LENGTH_SHORT).show()

                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            AnimatedSuccessDialog(
                                this,
                                "Welcome Back!",
                                "You have successfully signed in"
                            ) {
                                startActivity(Intent(this, DashboardActivity::class.java))
                                finish()
                            }.show()
                        } else {
                            Toast.makeText(this, "Login failed: " + task.exception?.message, Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }

        binding.btnGoogleSso.setOnClickListener {
            Toast.makeText(this, "Initiating Google Single Sign-On...", Toast.LENGTH_SHORT).show()
            auth.signInAnonymously().addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    AnimatedSuccessDialog(
                        this,
                        "Welcome Back!",
                        "You have successfully signed in"
                    ) {
                        startActivity(Intent(this, DashboardActivity::class.java))
                        finish()
                    }.show()
                } else {
                    Toast.makeText(this, "SSO Sign-in error: " + task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.tvForgotPassword.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            if (email.isNotEmpty() && ValidationUtils.isValidEmail(email)) {
                auth.sendPasswordResetEmail(email)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Password reset link sent to $email", Toast.LENGTH_LONG).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Reset failed: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                binding.tilEmail.error = "Enter email to reset password"
            }
        }

        binding.btnGoToRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
