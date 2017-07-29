package com.wizpanda.file

import grails.util.Holders

class StoredFile {

    String originalName
    String name
    String url
    String groupName
    Long size
    Date uploadedOn = new Date()
    Map meta = [:]

    void delete() {
        FileUploadService fileUploadService = Holders.getApplicationContext()['fileUploadService']

        if (!fileUploadService) {
            log.warn "FileUploadService bean not injected!"
            return
        }

        fileUploadService.delete(this)
    }
}
