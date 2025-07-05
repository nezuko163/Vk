package com.nezuko.mdreader

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nezuko.domain.md.MdBlock
import com.nezuko.domain.model.Result
import com.nezuko.domain.model.asd
import com.nezuko.domain.model.resultFlow
import com.nezuko.domain.repository.FilesContentRepository
import com.nezuko.domain.repository.FilesMetadataRepository
import com.nezuko.domain.repository.md.MdParserRepository
import com.nezuko.domain.repository.md.MdRenderer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ReaderViewModel @Inject constructor(
    private val fileContentRepository: FilesContentRepository,
    private val filesMetadataRepository: FilesMetadataRepository,
    private val mdParser: MdParserRepository,
    private val mdRenderer: MdRenderer
) : ViewModel() {
    private val _content = MutableStateFlow<Result<View>>(Result.None())
    val content = _content.asStateFlow()
    private val TAG = "ReaderViewModel"

    private fun getTextFromFileId(id: Int): Flow<Result<List<MdBlock>>> = resultFlow {
        val fileName = withContext(Dispatchers.IO) {
            filesMetadataRepository.getFileById(id).fileName
        }
        val fileContent = fileContentRepository.readFile(fileName)
        withContext(viewModelScope.coroutineContext) {
//            mdParser.parseMd(fileContent)
            mdParser.parseMd("**asd** asd zxc asd *zxc*")
        }
    }

    fun load(id: Int) {
        viewModelScope.launch {
            Log.i(TAG, "load: id - $id")
            getTextFromFileId(id)
                .asd { res ->
                    mdRenderer.render(res)
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