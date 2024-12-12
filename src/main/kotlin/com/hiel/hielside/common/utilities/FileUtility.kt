package com.hiel.hielside.common.utilities

import com.hiel.hielside.common.domains.ResultCode
import com.hiel.hielside.common.exceptions.ServiceException
import org.apache.commons.io.FilenameUtils
import org.springframework.web.multipart.MultipartFile

private const val BINARY_UNIT_MULTIPLIER = 1024

fun Long.kiloByteToByte() = this * BINARY_UNIT_MULTIPLIER

fun Long.megaByteToByte() = this.kiloByteToByte() * BINARY_UNIT_MULTIPLIER

fun Int.kiloByteToByte() = this * BINARY_UNIT_MULTIPLIER.toLong()

fun Int.megaByteToByte() = this.kiloByteToByte() * BINARY_UNIT_MULTIPLIER.toLong()

fun MultipartFile.isValidSize(maxSize: Long) = this.size in 1..maxSize

fun MultipartFile.isValidExtension(extensions: List<String>) = FilenameUtils.isExtension(this.originalFilename, extensions)

fun MultipartFile.validate(maxSizeByte: Long, allowExtensions: List<String>) {
    if (!this.isValidSize(maxSizeByte)) {
        throw ServiceException(
            resultCode = ResultCode.File.FILE_SIZE_TOO_LARGE,
            args = arrayOf(maxSizeByte),
        )
    }
    if (!FilenameUtils.isExtension(this.originalFilename, allowExtensions)) {
        throw ServiceException(
            resultCode = ResultCode.File.INVALID_FILE_EXTENSION,
            args = arrayOf(allowExtensions),
        )
    }
}
