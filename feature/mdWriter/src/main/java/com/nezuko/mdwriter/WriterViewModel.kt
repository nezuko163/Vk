package com.nezuko.mdwriter

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nezuko.domain.model.Result
import com.nezuko.domain.model.asd
import com.nezuko.domain.model.resultFlow
import com.nezuko.domain.repository.FilesContentRepository
import com.nezuko.domain.repository.FilesMetadataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class WriterViewModel @Inject constructor(
    private val filesMetadataRepository: FilesMetadataRepository,
    private val filesContentRepository: FilesContentRepository
) : ViewModel() {
    private val TAG = "WriterViewModel"

    private val _content = MutableStateFlow<Result<String>>(Result.None())
    val content = _content.asStateFlow()

    private fun getMdBlocksFromFileId(id: Int): Flow<Result<String>> = resultFlow {
        val filePath = withContext(Dispatchers.IO) {
            filesMetadataRepository.getFileById(id).filePath
        }
        filesContentRepository.readFile(filePath)
    }

    suspend fun save(id: Int, text: String, context: Context) {
        withContext(Dispatchers.IO) {
            val fileDto = filesMetadataRepository.getFileById(id)
            val uri = fileDto.filePath.toUri()

            context.contentResolver.openOutputStream(uri, "w")?.use { outputStream ->
                outputStream.writer().use { writer ->
                    writer.write(text)
                }
            } ?: Toast.makeText(context, "Ошибка при сохранении", Toast.LENGTH_SHORT).show()
        }
    }

    fun load(id: Int) {
        viewModelScope.launch {
            Log.i(TAG, "load: id - $id")
            getMdBlocksFromFileId(id)
                .asd { res ->
                    Log.i(TAG, "load: $res")
                    res
                }
                .collect {
                    _content.emit(it)
                }
        }
    }

    fun setNone() {
        viewModelScope.launch {
            _content.emit(Result.None())
        }
    }
}