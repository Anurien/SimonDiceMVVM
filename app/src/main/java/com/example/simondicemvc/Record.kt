package com.example.simondicemvc

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "record")
data class Record(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "record") val record: Int?



)
