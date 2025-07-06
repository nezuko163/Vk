package com.nezuko.data.impl

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.core.net.toUri
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.nezuko.data.db.dao.FileDao
import com.nezuko.data.db.entity.FileEntity
import com.nezuko.data.toDto
import com.nezuko.domain.dto.FileDto
import com.nezuko.domain.repository.FilesMetadataRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import javax.inject.Inject

class FilesMetadataRepositoryImpl @Inject constructor(
    private val dao: FileDao,
    @param:ApplicationContext private val context: Context
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

    override fun getAll(): List<FileDto> {
        return dao.getAll().map { it.toDto() }
    }

    override suspend fun addFile(path: String): Int {
        return dao.insert(FileEntity(0, getFileName(context, path.toUri()), path, Instant.now())).toInt()
    }

    override fun getCount(): Flow<Long> {
        return dao.getCount()
    }

    override suspend fun getFileByPath(name: String): FileDto {
        return dao.getFileByPath(name).first().toDto()
    }

    override suspend fun getFileById(id: Int): FileDto {
        return dao.getFileById(id).toDto()
    }

    fun getFileName(context: Context, uri: Uri): String {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (it.moveToFirst() && nameIndex >= 0) {
                    result = it.getString(nameIndex)
                }
            }
        }

        if (result == null) {
            result = uri.path
            val cut = result?.lastIndexOf('/')
            if (cut != null && cut != -1) {
                result = result.substring(cut + 1)
            }
        }

        return result ?: "Неизвестный"
    }
}