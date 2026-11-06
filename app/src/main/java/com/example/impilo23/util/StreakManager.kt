package com.example.impilo23.util

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Dynamic Daily Streak Tracker.
 * Calculates consecutive daily app usage dates and persists streak count.
 */
object StreakManager {
    private const val PREFS_NAME = "impilo_streak_prefs"
    private const val KEY_STREAK = "key_streak_count"
    private const val KEY_LAST_DATE = "key_last_activity_date"
    private const val DEFAULT_STREAK = 8

    fun getStreak(context: Context): Int {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val lastDate = prefs.getString(KEY_LAST_DATE, "")

        if (lastDate.isNullOrEmpty()) {
            prefs.edit().putInt(KEY_STREAK, DEFAULT_STREAK).putString(KEY_LAST_DATE, todayStr).apply()
            return DEFAULT_STREAK
        }

        if (lastDate != todayStr) {
            val currentStreak = prefs.getInt(KEY_STREAK, DEFAULT_STREAK)
            val newStreak = currentStreak + 1
            prefs.edit().putInt(KEY_STREAK, newStreak).putString(KEY_LAST_DATE, todayStr).apply()

            val uid = FirebaseAuth.getInstance().currentUser?.uid
            if (uid != null) {
                FirebaseDatabase.getInstance().reference.child("users").child(uid).child("streakCount").setValue(newStreak)
            }
            return newStreak
        }

        return prefs.getInt(KEY_STREAK, DEFAULT_STREAK)
    }
}
