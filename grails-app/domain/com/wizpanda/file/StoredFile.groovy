package com.wizpanda.file

import grails.util.Holders

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
        FileUploadService fileUploadService = Holders.getApplicationContext()['fileUploadService']

        if (!fileUploadService) {
            log.warn "FileUploadService bean not injected!"
            return
        }

        return fileUploadService
    }

    void remove() {
        fileUploadService?.delete(this)
    }

    void cloneFile(String newGroupName) {
        fileUploadService?.cloneFile(this, newGroupName)
    }
}
