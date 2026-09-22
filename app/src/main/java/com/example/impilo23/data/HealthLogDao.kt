package com.example.impilo23.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface HealthLogDao {
    @Query("SELECT * FROM health_logs WHERE userId = :userId AND logDate = :date LIMIT 1")
    fun getLogByDate(userId: Long, date: String): HealthLog?

    @Query("SELECT * FROM health_logs WHERE userId = :userId ORDER BY logDate DESC")
    fun getAllLogsForUser(userId: Long): List<HealthLog>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateLog(log: HealthLog): Long

    @Update
    fun updateLog(log: HealthLog): Int
}
