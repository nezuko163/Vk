package com.nezuko.domain.repository

import com.nezuko.domain.model.Result

interface FilesContentRepository {
    suspend fun readFile(path: String): String
    suspend fun writeFile(name: String, content: String)
}
