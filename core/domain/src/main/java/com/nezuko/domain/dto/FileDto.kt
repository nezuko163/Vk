package com.nezuko.domain.dto

import java.time.Instant

data class FileDto(
    val filePath: String,
    val lastOpen: Instant,
    val fileId: Int,
    val fileName: String
)