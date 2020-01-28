package com.wizpanda.file

import grails.gorm.transactions.Transactional

/**
 * Service class to perform delete operations on file whom deleteAfter date is passed.
 *
 * @author Ankit Kumar Singh
 * @since 0.1.3
 */
@Transactional
class FileDeletionService {

    private static final int MAX = 100

    FileUploadService fileUploadService

    /**
     * Method to delete stored files in every 2 hr whom deleteAfter date is passed.
     */
    void deleteMarkedFiles() {
        List<StoredFile> storedFileList = StoredFile.createCriteria().list {
            isNotNull("deleteAfter")
            le("deleteAfter", new Date())

            maxResults(MAX)
        }

        storedFileList.each { StoredFile storedFile ->
            try {
                fileUploadService.delete(storedFile)
                log.debug "$storedFile deleted from S3"
                storedFile.delete(flush: true)
            } catch (Exception e) {
                log.debug "File deletion failed due to $e"
            }
        }

        if (storedFileList.size() == MAX) {
            deleteMarkedFiles()
        }
    }
}
