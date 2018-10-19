package com.wizpanda.file


import grails.gorm.transactions.Transactional
import groovy.time.TimeCategory

/**
 * Service class to perform delete operations on file which are marked for deletion.
 *
 * @author Ankit Kumar Singh
 * @since 1.0.3
 */
@Transactional
class FileDeletionService {

    private static final int MAX = 100

    FileUploadService fileUploadService

    /**
     * Method to delete stored files in every 2 hr.
     */
    void deleteMarkedFiles() {
        Date dateForDeletion

        use(TimeCategory) {
            dateForDeletion = new Date() - 2.hours
        }

        List<StoredFile> storedFileList = StoredFile.createCriteria().list {
            isNotNull("markedForDeletion")
            le("markedForDeletion", dateForDeletion)

            maxResult(MAX)
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
