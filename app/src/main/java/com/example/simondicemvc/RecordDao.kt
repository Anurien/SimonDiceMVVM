package com.example.simondicemvc

import androidx.room.*
@Dao
interface RecordDao {
    @Query("SELECT record FROM Record WHERE id= 1")
    suspend fun getRecord(): Int

    @Query("INSERT INTO Record (record) VALUES (0)")
    suspend fun crearRecord()

    @Update
    suspend fun update(record: Record)

   /* @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(record: Int)*/

    @Delete
    fun delete(user: Record)
}