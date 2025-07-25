package com.nezuko.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(
    tableName = "files",
    indices = [
        Index("path", unique = true),
        Index("last_open")
    ]
)
data class FileEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo("name") val fileName: String,
    @ColumnInfo("path") val filePath: String,
    @ColumnInfo("last_open") val lastOpen: Instant,
)
