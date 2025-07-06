package com.nezuko.main

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.nezuko.data.network.Downloader
import com.nezuko.domain.repository.FilesMetadataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val filesMetadataRepository: FilesMetadataRepository,
    private val downloader: Downloader
) : ViewModel() {
    private val TAG = "MainViewModel"
    val fileFlow = filesMetadataRepository.getFiles().cachedIn(viewModelScope)

    fun updateLastOpen(fileId: Int) {
        viewModelScope.launch {
            filesMetadataRepository.updateLastOpen(fileId)
        }
    }

    fun processUri(uri: Uri, onEnd: (Int) -> Unit) {
        viewModelScope.launch {
            val id = filesMetadataRepository.addFile(uri.toString())
            Log.i(TAG, "processUri: ID = $id")
            onEnd(id)
        }
    }

    fun downloadFromInternet(
        url: String,
        context: Context,
        onEnd: (Int) -> Unit,
        onError: () -> Unit
    ) {
        viewModelScope.launch {
            val file = createUniqueCacheFile(context, "downloaded")
            val res = downloader.downloadMdFileKtor(url, file)
            if (res) {
                processUri(file.toUri(), onEnd)
            } else {
                 onError()
            }
        }
    }

    private fun createUniqueCacheFile(
        context: Context,
        baseName: String,
        extension: String = ".md"
    ): File {
        var index = 1
        var file = File(context.cacheDir, "$baseName$extension")
        while (file.exists()) {
            file = File(context.cacheDir, "$baseName($index)$extension")
            index++
        }
        return file
    }
}