package com.wizpanda.file

/**
 * Job to delete marked stored files from S3.
 *
 * @author Ankit Kumar Singh
 * @since 1.0.3
 */
class DeleteMarkedFileJob {

    FileDeletionService fileDeletionService

    static triggers = {
        simple repeatInterval: 1000l * 60 * 60 * 2 // execute job once in 2 hour
    }

    def execute() {
        fileDeletionService.deleteMarkedFiles()
    }
}
