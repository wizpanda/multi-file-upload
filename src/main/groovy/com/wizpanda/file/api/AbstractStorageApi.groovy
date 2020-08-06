package com.wizpanda.file.api

import com.wizpanda.file.StoredFile
import com.wizpanda.file.exception.FileUploadException
import com.wizpanda.file.service.UploaderService
import com.wizpanda.file.utils.FileUtils
import groovy.transform.CompileStatic
import org.springframework.web.multipart.MultipartFile

@CompileStatic
abstract class AbstractStorageApi implements StorageApi {

    File rawFile
    StoredFile gormFile
    UploaderService service

    AbstractStorageApi(UploaderService service) {
        this.service = service
    }

    StoredFile save(MultipartFile multipartFile) throws FileUploadException {
        File temporaryFile
        try {
            temporaryFile = FileUtils.saveTemporarily(multipartFile)
        } catch (IllegalStateException | IOException e) {
            throw new FileUploadException(e.message)
        }

        return save(temporaryFile)
    }

    StoredFile save(File rawFile) throws FileUploadException {
        this.rawFile = rawFile
        this.gormFile = new StoredFile()
        this.gormFile.originalName = rawFile.name
        this.gormFile.groupName = service.groupName
        this.gormFile.size = rawFile.size()

        this.validateRawFile()
        this.saveNativeFile()

        return this.saveGORMFile()
    }

    @Override
    StoredFile saveGORMFile() throws FileUploadException {
        this.gormFile.save()

        if (this.gormFile.hasErrors()) {
            println this.gormFile.errors
            // TODO Delete the native file and then throw the exception
            throw new FileUploadException()
        }

        return this.gormFile
    }

    @Override
    void delete(StoredFile file) {
        deleteNativeFile(file)
    }
}
