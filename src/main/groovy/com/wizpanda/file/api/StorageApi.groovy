package com.wizpanda.file.api

import com.wizpanda.file.StoredFile
import com.wizpanda.file.exception.FileUploadException
import groovy.transform.CompileStatic
import org.jclouds.blobstore.domain.Blob
import org.springframework.web.multipart.MultipartFile

@CompileStatic
interface StorageApi {

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

    Blob getBlob(StoredFile storedFile)
}
