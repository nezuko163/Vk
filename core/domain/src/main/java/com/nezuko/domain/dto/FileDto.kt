package com.nezuko.domain.dto

import java.time.Instant

data class FileDto(
    val fileName: String,
    val lastOpen: Instant,
    val fileId: Int
)