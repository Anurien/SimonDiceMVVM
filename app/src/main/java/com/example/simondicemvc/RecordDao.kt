package com.example.simondicemvc

import androidx.room.*
@Dao
interface RecordDao {
    @Query("SELECT * FROM record WHERE id= 1")
    fun getRecord(): Int

    @Query("INSERT INTO record (record) VALUES (0)")
    suspend fun crearRecord()

    @Update
    suspend fun update(record: Record)

   /* @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(record: Int)*/

    @Delete
    fun delete(user: Record)
}