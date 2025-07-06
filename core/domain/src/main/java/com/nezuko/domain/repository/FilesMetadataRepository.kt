package com.nezuko.domain.repository

import androidx.paging.PagingData
import com.nezuko.domain.dto.FileDto
import kotlinx.coroutines.flow.Flow

interface FilesMetadataRepository {
    fun getFiles(): Flow<PagingData<FileDto>>
    fun getCount(): Flow<Long>
    suspend fun getFileByPath(name: String): FileDto
    suspend fun getFileById(id: Int): FileDto
    suspend fun addFile(path: String): Int
    suspend fun updateLastOpen(fileId: Int)
    fun getAll(): List<FileDto>
}
