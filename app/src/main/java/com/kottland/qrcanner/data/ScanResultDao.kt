package com.kottland.qrcanner.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.kottland.qrcanner.model.ScanResult
import kotlinx.coroutines.flow.Flow

@Dao
interface ScanResultDao {
    @Insert
    suspend fun insert(scanResult: ScanResult)

    @Query("SELECT * FROM scan_results ORDER BY timestamp DESC")
    fun getAll(): Flow<List<ScanResult>>
    
    @Query("SELECT * FROM scan_results WHERE id = :id")
    suspend fun getById(id: Int): ScanResult?

    @Query("DELETE FROM scan_results WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM scan_results")
    suspend fun deleteAll()
}
