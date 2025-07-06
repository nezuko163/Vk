package com.nezuko.data.network

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.isSuccess
import io.ktor.utils.io.jvm.javaio.toInputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class Downloader @Inject constructor(
    private val client: HttpClient
) {
    suspend fun downloadMdFileKtor(url: String, outputFile: File): Boolean =
        withContext(Dispatchers.IO) {
            val q = client.get(url)
            if (q.status.isSuccess()) {
                val byteReadChannel = q.bodyAsChannel()
                outputFile.outputStream().buffered().use { fileStream ->
                    byteReadChannel.toInputStream().copyTo(fileStream)
                }
                return@withContext true
            }
            false
        }
}
