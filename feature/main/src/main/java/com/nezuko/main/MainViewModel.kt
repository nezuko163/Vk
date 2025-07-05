package com.nezuko.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.nezuko.domain.dto.FileDto
import com.nezuko.domain.repository.FilesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val filesRepository: FilesRepository,
) : ViewModel() {
    private val TAG = "MainViewModel"
    val fileFlow = filesRepository.getFiles().cachedIn(viewModelScope)
    fun updateLastOpen(fileId: Int) {
        viewModelScope.launch {
            filesRepository.updateLastOpen(fileId)
        }
    }

    fun insertFile(fileDto: FileDto) {
        viewModelScope.launch {
            filesRepository.addFile(fileDto)
        }
    }

    fun __create() {
        viewModelScope.launch {
            filesRepository.__setRandomFiles()
        }
    }

    fun count() {
        viewModelScope.launch { filesRepository.getCount().also { Log.i(TAG, "count: $it") } }
    }
}