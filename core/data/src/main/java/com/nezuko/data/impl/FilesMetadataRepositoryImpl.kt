package com.nezuko.data.impl

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.nezuko.data.db.dao.FileDao
import com.nezuko.data.db.entity.FileEntity
import com.nezuko.data.toDto
import com.nezuko.domain.dto.FileDto
import com.nezuko.domain.repository.FilesMetadataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import javax.inject.Inject

class FilesMetadataRepositoryImpl @Inject constructor(
    private val dao: FileDao
) : FilesMetadataRepository {
    private val TAG = "FilesMetadataRepositoryImpl"
    override fun getFiles(): Flow<PagingData<FileDto>> {
        Log.i(TAG, "getFiles: start")
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { dao.getFilesPagingSource().also { Log.i(TAG, "getFiles: ") } },
            initialKey = null
        ).flow.map {
            it.map { entity -> entity.toDto() }
        }
    }

    override suspend fun updateLastOpen(fileId: Int) {
        dao.updateLastOpen(fileId, Instant.now())
    }

    override suspend fun addFile(name: String): Int {
        return dao.insert(FileEntity(0, name, Instant.now())).toInt()
    }

    override fun getCount(): Flow<Long> {
        return dao.getCount()
    }

    override suspend fun getFileByName(name: String): FileDto {
        return dao.getFileByName(name).first().toDto()
    }

    override suspend fun getFileById(id: Int): FileDto {
        return dao.getFileById(id).toDto()
    }
}