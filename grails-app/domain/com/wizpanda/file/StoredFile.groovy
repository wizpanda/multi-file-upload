package com.wizpanda.file

import com.wizpanda.file.model.StoredFileBlob
import grails.compiler.GrailsCompileStatic
import grails.util.Holders
import org.jclouds.blobstore.domain.Blob

/**
 * The Grails domain class which holds the metadata of the stored file.
 *
 * @author Shashank Agrawal
 */
@GrailsCompileStatic
class StoredFile {

    /**
     * Holds the deletion date after which the file can be deleted.
     */
    Date deleteAfter

    /**
     * The name of the file which was uploaded originally from the client side.
     */
    String originalName
    String name

    /**
     * Final public URL of the file.
     */
    String url

    /**
     * The name of the group as defined in the configuration.
     */
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
     * @param afterDate The date after which, this instance & the file can be deleted. By default, the file will be deleted after 7 days.
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
