package com.wizpanda.file

import com.wizpanda.file.model.StoredFileBlob
import grails.util.Holders
import org.jclouds.blobstore.domain.Blob

class StoredFile {

    // holds the deletion date for the file.
    Date deleteAfter

    String originalName
    String name
    String url
    String groupName
    Long size
    Date uploadedOn = new Date()
    Map meta = [:]

    FileUploadService getFileUploadService() {
        Holders.getApplicationContext()['fileUploadService']
    }

    static constraints = {
        deleteAfter nullable: true
    }

    StoredFileBlob getBlob() {
        Blob blob = getFileUploadService().getStorageApi(this.groupName).getBlob(this)

        return new StoredFileBlob(blob)
    }

    void remove() {
        fileUploadService?.delete(this)
    }

    void cloneFile(String newGroupName) {
        fileUploadService?.cloneFile(this, newGroupName)
    }

    void markForDeletion(Date date) {
        this.deleteAfter = date ?: new Date()
        this.save(failOnError: true)
    }
}
