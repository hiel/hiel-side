package com.hiel.hielside.common.utilities.aws

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.ObjectMetadata
import com.hiel.hielside.common.utilities.LogUtility
import org.apache.commons.io.FilenameUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.util.UUID

@Component
class S3Utility(
    private val amazonS3Client: AmazonS3Client,

    @Value("\${cloud.aws.s3.bucket}")
    private val bucket: String,
) {
    companion object : LogUtility

    fun uploadFile(file: MultipartFile, fileName: String? = null): String? {
        try {
            val objectMetadata = ObjectMetadata()
            objectMetadata.contentLength = file.size
            objectMetadata.contentType = file.contentType

            var uploadFileName = fileName
            if (fileName.isNullOrBlank()) {
                uploadFileName = "${UUID.randomUUID()}" +
                    "${FilenameUtils.EXTENSION_SEPARATOR}${file.originalFilename!!.split(";")[0].substringAfterLast('.', "")}"
            }

            amazonS3Client.putObject(bucket, uploadFileName, file.inputStream, objectMetadata)
            return amazonS3Client.getUrl(bucket, uploadFileName).toString()
        } catch (e: IOException) {
            log.error(e.toString())
            return null
        }
    }
}
