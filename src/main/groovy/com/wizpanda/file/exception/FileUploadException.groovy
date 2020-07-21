package com.wizpanda.file.exception

import groovy.transform.CompileStatic

@CompileStatic
class FileUploadException extends Exception {

    FileUploadException(String message = '') {
        super(message)
    }
}
