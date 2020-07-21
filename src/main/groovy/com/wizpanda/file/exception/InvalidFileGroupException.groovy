package com.wizpanda.file.exception

import groovy.transform.CompileStatic

@CompileStatic
class InvalidFileGroupException extends Exception {

    InvalidFileGroupException(String message = null) {
        super(message)
    }
}
