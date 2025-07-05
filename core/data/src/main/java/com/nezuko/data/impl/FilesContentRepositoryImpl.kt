package com.nezuko.data.impl

import android.R.attr.name
import android.content.Context
import android.util.Log.e
import androidx.core.net.toUri
import com.nezuko.domain.model.Result
import com.nezuko.domain.repository.FilesContentRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class FilesContentRepositoryImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : FilesContentRepository {
    override suspend fun readFile(name: String): String {
        val text = withContext(Dispatchers.IO) {
            context.contentResolver.openInputStream(name.toUri())?.use {
                it.bufferedReader().use { reader -> reader.readText() }
            } ?: throw IOException("Cannot open InputStream: $name")
        }
        return text
    }

    override suspend fun writeFile(name: String, content: String) {
        withContext(Dispatchers.IO) {
            context.contentResolver.openOutputStream(name.toUri())?.use { outputStream ->
                outputStream.writer().use { it.write(content) }
            } ?: throw IOException("Cannot open OutputStream: $name")
        }
    }
}