package com.wizpanda.file

import com.wizpanda.file.model.StoredFileBlob
import grails.compiler.GrailsCompileStatic
import grails.util.Holders
import org.jclouds.blobstore.domain.Blob

@GrailsCompileStatic
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
        Holders.getApplicationContext()["fileUploadService"] as FileUploadService
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

    void markForDeletionAfterDays(Integer afterDays) {
        this.markForDeletion(new Date() + afterDays)
    }

    /**
     * Mark this instance to be deleted by the Job.
     * @param afterDate The date after which, this instance can be deleted.
     */
    void markForDeletion(Date afterDate = null) {
        if (afterDate) {
            this.deleteAfter = afterDate
        } else {
            this.deleteAfter = new Date() + 1       // By default, delete after 1 day
        }

        this.save(failOnError: true)
    }
}
