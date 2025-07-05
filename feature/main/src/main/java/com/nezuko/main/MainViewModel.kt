package com.nezuko.main

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.nezuko.domain.repository.FilesMetadataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val filesMetadataRepository: FilesMetadataRepository
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

}