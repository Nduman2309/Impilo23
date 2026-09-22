package com.example.impilo23.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Health metric history entity capturing multiple data categories per day for a user account.
 */
@Entity(tableName = "health_logs")
data class HealthLog(
    @PrimaryKey(autoGenerate = true) val logId: Long = 0,
    val userId: Long,
    val logDate: String, // YYYY-MM-DD format
    var waterMl: Int = 0,
    var weightKg: Double = 0.0,
    var heartRateBpm: Int = 0,
    var bloodPressureSys: Int = 0,
    var bloodPressureDia: Int = 0
)
