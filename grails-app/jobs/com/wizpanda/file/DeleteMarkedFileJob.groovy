package com.wizpanda.file

import grails.compiler.GrailsCompileStatic

/**
 * Job to delete marked stored files from S3 whom deleteAfter date is passed.
 *
 * @author Ankit Kumar Singh
 * @since 0.1.3
 */
@GrailsCompileStatic
class DeleteMarkedFileJob {

    FileDeletionService fileDeletionService

    def concurrent = false

    static triggers = {
        simple startDelay:  1000l * 60 * 15, repeatInterval: 1000l * 60 * 60 * 2 // execute job once in 2 hour
    }

    def execute() {
        fileDeletionService.deleteMarkedFiles()
    }
}
