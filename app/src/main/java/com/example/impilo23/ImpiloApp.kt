package com.example.impilo23

import android.app.Application
import com.google.firebase.database.FirebaseDatabase

/**
 * Custom Application class to initialize global configurations.
 */
class ImpiloApp : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Enable Firebase Disk Persistence for instant data retrieval
        // This allows health logs to load from local storage even when offline
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }
}
