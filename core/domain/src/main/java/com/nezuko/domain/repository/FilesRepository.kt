package com.nezuko.domain.repository

import androidx.paging.PagingData
import com.nezuko.domain.dto.FileDto
import kotlinx.coroutines.flow.Flow

interface FilesRepository {
    fun getFiles(): Flow<PagingData<FileDto>>
    suspend fun updateLastOpen(fileId: Int)
    suspend fun addFile(fileDto: FileDto)
    suspend fun __setRandomFiles()
    suspend fun getCount(): Long
}