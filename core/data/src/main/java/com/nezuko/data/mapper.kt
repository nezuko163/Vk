package com.nezuko.data

import com.nezuko.data.db.entity.FileEntity
import com.nezuko.domain.dto.FileDto

fun FileEntity.toDto(): FileDto {
    return FileDto(fileName, lastOpen, id)
}

fun FileDto.toEntity(): FileEntity {
    return FileEntity(fileId, fileName, lastOpen)
}