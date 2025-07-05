package com.nezuko.data.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nezuko.data.db.entity.FileEntity
import kotlinx.coroutines.flow.Flow
import java.time.Instant

@Dao
interface FileDao {
    @Query("SELECT * FROM files ORDER BY last_open DESC")
    fun getFilesPagingSource(): PagingSource<Int, FileEntity>

    @Query("UPDATE files SET last_open = :lastOpen WHERE id = :id")
    suspend fun updateLastOpen(id: Int, lastOpen: Instant)

    @Query("SELECT COUNT(*) FROM files")
    fun getCount(): Flow<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(fileEntity: FileEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(files: List<FileEntity>)

    @Query("SELECT * FROM files WHERE name = :name")
    suspend fun getFileByName(name: String): List<FileEntity>

    @Query("SELECT * FROM files WHERE id = :id")
    suspend fun getFileById(id: Int): FileEntity


}