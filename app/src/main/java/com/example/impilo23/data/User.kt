package com.example.impilo23.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room persistence entity representing the security credentials and configuration metrics of a profile.
 */
@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val email: String,
    var username: String,
    var passwordHash: String,
    val salt: String,
    var targetWaterMl: Int = 2500,
    var weightGoalKg: Double = 70.0
)
