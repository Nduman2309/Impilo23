package com.example.impilo23.util

import android.util.Patterns

/**
 * Client-Side Input Boundary Validation Suite.
 * Prevents illegal form data from generating runtime or database level crashes.
 */
object ValidationUtils {

    /**
     * Checks if the email matches the standard pattern constraints.
     */
    fun isValidEmail(email: String?): Boolean {
        if (email.isNullOrBlank()) return false
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        return email.trim().matches(emailRegex)
    }

    /**
     * Password validation requiring at least 4 characters for rapid prototype accessibility.
     */
    fun isValidPassword(password: String?): Boolean {
        if (password.isNullOrBlank()) return false
        return password.trim().length >= 4
    }

    /**
     * Validates if a text value can be formatted into a positive double metric value.
     */
    fun isValidDouble(value: String?): Boolean {
        if (value.isNullOrBlank()) return false
        return try {
            val parsed = value.trim().toDouble()
            parsed >= 0.0
        } catch (e: NumberFormatException) {
            false
        }
    }

    /**
     * Validates if a text input can be safely parsed into an integer metric value.
     */
    fun isValidInt(value: String?): Boolean {
        if (value.isNullOrBlank()) return false
        return try {
            val parsed = value.trim().toInt()
            parsed >= 0
        } catch (e: NumberFormatException) {
            false
        }
    }
}
