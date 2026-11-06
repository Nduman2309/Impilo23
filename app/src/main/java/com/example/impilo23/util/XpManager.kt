package com.example.impilo23.util

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

/**
 * Single Source of Truth for User XP and Level Progression.
 * Handles local SharedPreferences persistence & Firebase Realtime Database sync.
 */
object XpManager {
    private const val PREFS_NAME = "impilo_xp_prefs"
    private const val KEY_TOTAL_XP = "key_total_xp"
    private const val DEFAULT_XP = 2450

    private val listeners = ArrayList<(Int) -> Unit>()

    fun getXp(context: Context): Int {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_TOTAL_XP, DEFAULT_XP)
    }

    fun addXp(context: Context, amount: Int, reason: String = ""): Int {
        val current = getXp(context)
        val updated = current + amount
        
        // Save locally
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putInt(KEY_TOTAL_XP, updated).apply()

        // Sync to Firebase if user is logged in
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            val dbRef = FirebaseDatabase.getInstance().reference.child("users").child(uid).child("totalXp")
            dbRef.setValue(updated)
        }

        // Notify active UI listeners
        notifyListeners(updated)
        return updated
    }

    fun getLevel(xp: Int): Int {
        return 1 + (xp / 200)
    }

    fun addXpListener(listener: (Int) -> Unit) {
        if (!listeners.contains(listener)) {
            listeners.add(listener)
        }
    }

    fun removeXpListener(listener: (Int) -> Unit) {
        listeners.remove(listener)
    }

    private fun notifyListeners(xp: Int) {
        for (l in listeners) {
            l.invoke(xp)
        }
    }
}
