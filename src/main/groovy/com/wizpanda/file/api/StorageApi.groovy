package com.wizpanda.file.api

import com.wizpanda.file.StoredFile
import com.wizpanda.file.service.UploaderService
import org.springframework.web.multipart.MultipartFile

interface StorageApi {

    UploaderService service

    StoredFile saveFile(File file)

    String getFileName(File file)
}