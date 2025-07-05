package com.nezuko.data.impl

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.nezuko.data.db.dao.FileDao
import com.nezuko.data.db.entity.FileEntity
import com.nezuko.data.toDto
import com.nezuko.data.toEntity
import com.nezuko.domain.dto.FileDto
import com.nezuko.domain.repository.FilesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import javax.inject.Inject

class FilesRepositoryImpl @Inject constructor(
    private val dao: FileDao
) : FilesRepository {
    private val TAG = "FilesRepositoryImpl"
    override fun getFiles(): Flow<PagingData<FileDto>> {
        Log.i(TAG, "getFiles: start")
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { dao.getFilesPagingSource().also { Log.i(TAG, "getFiles: ") } },
            initialKey = null
        ).flow.map {
            it.map { it.toDto().also { Log.i(TAG, "getFiles: $it") } } }
    }

    override suspend fun updateLastOpen(fileId: Int) {
        dao.updateLastOpen(fileId, Instant.now())
    }

    override suspend fun addFile(fileDto: FileDto) {
        dao.insert(fileDto.toEntity())
    }

    override suspend fun __setRandomFiles() {
        Log.i(TAG, "__setRandomFiles: setting random files")
        dao.insertAll(List(20) { id -> FileEntity(0, "", Instant.now())})
        Thread.sleep(1000)
        getCount()
    }

    override suspend fun getCount(): Long {
        return dao.getCount().also { Log.i(TAG, "getCount: $it") }
    }
}