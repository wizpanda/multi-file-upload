package com.wizpanda.file.api

import com.wizpanda.file.StoredFile
import com.wizpanda.file.exception.FileUploadException
import com.wizpanda.file.service.UploaderService
import org.springframework.web.multipart.MultipartFile

interface StorageApi {

    UploaderService service

    void saveNativeFile()

    void validateRawFile()

    void deleteNativeFile(StoredFile file)

    String getFileName(File file)

    String getFileName(StoredFile file)

    StoredFile saveGORMFile() throws FileUploadException

    /**
     * The entry point of the Storage API instance which will accept a raw file to be saved/uploaded via an API.
     * @param rawFile
     * @return Newly saved GORM StoredFile instance
     */
    StoredFile save(File rawFile) throws FileUploadException

    /**
     * The entry point of the Storage API instance which will accept an uploaded multipart file to be saved/uploaded via
     * an API.
     * @param multipartFile
     * @return Newly saved GORM StoredFile instance
     */
    StoredFile save(MultipartFile multipartFile) throws FileUploadException

    void delete(StoredFile file)

    // Used to clone an instance of StoredFile and get the new StoredFile instance.
    StoredFile cloneStoredFile(StoredFile file, String newGroupName)
}