package com.nezuko.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity
data class FileEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo("name") val fileName: String,
    @ColumnInfo("last_open") val lastOpen: Instant,
)
