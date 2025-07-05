package com.nezuko.data.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nezuko.data.db.entity.FileEntity
import java.time.Instant

@Dao
interface FileDao {
    @Query("SELECT * FROM fileentity ORDER BY last_open DESC")
    fun getFilesPagingSource(): PagingSource<Int, FileEntity>

    @Query("UPDATE fileentity SET last_open = :lastOpen WHERE id = :id")
    suspend fun updateLastOpen(id: Int, lastOpen: Instant)

    @Query("SELECT COUNT(*) FROM fileentity")
    suspend fun getCount(): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg fileEntities: FileEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(files: List<FileEntity>)
}