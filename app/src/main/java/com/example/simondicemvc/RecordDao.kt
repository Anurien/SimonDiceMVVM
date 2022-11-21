package com.example.simondicemvc

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

interface RecordDao {
    @Query("SELECT * FROM record ORDER BY name")
    fun getRecord(): Flow<List<Record>>

    @Query("SELECT * FROM record WHERE name = :record")
    fun getRecord(record: String): Flow<Record>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(record: List<Record>)

    @Delete
    fun delete(user: Record)

}