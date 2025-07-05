package com.nezuko.data.network

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsChannel
import io.ktor.utils.io.jvm.javaio.toInputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class Downloader @Inject constructor(
    private val client: HttpClient
) {
    suspend fun downloadMdFileKtor(url: String, outputFile: File) {
        withContext(Dispatchers.IO) {
            val byteReadChannel = client.get(url).bodyAsChannel()
            outputFile.outputStream().buffered().use { fileStream ->
                byteReadChannel.toInputStream().copyTo(fileStream)
            }
        }
    }
}
