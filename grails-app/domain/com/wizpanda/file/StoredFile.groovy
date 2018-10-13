package com.wizpanda.file

import com.wizpanda.file.model.StoredFileBlob
import grails.util.Holders
import org.jclouds.blobstore.domain.Blob

class StoredFile {

    boolean markedForDeletion

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
}
