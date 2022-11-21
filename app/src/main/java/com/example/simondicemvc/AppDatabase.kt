package com.example.simondicemvc

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [Record::class], version = 1, exportSchema = false)
class AppDatabase {
    abstract class AppDatabase : RoomDatabase() {
        abstract fun recordDao(): RecordDao
    }
}