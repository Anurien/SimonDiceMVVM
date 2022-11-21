package com.example.simondicemvc

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "record")
data class Record(
    @PrimaryKey val rid: Int,
    @ColumnInfo(name = "name") val nombre: String?,
    @ColumnInfo(name = "record") val record: Int?

)
