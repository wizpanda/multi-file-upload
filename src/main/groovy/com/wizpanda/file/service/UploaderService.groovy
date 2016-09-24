package com.wizpanda.file.service

import com.wizpanda.file.api.StorageApi

abstract class UploaderService {

    UploaderService() {
    }

    UploaderService(String groupName, Map config) {
        this.groupName = groupName
        this.flatGroupConfig = config
        verifyConfig()
    }

    String groupName

    Map flatGroupConfig

    abstract StorageApi instance()

    abstract void verifyConfig()
}