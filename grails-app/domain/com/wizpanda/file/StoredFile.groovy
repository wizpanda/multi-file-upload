package com.wizpanda.file

class StoredFile {

    String originalName
    String name
    String url
    String groupName
    Long size
    Date uploadedOn = new Date()
    Map meta = [:]
}
