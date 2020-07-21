package com.wizpanda.file.service

import com.wizpanda.file.ConfigHelper
import com.wizpanda.file.api.AmazonS3PermanentURLApi
import com.wizpanda.file.api.StorageApi
import com.wizpanda.file.exception.InvalidFileGroupException
import groovy.transform.CompileStatic

@CompileStatic
class AmazonS3UploaderService extends UploaderService {

    String accessKey, accessSecret, container

    AmazonS3UploaderService(String groupName, Map config) {
        super(groupName, config)

        /**
         * New bucket region (like Mumbai, Frankfurt) does not support older version of authentication on Amazon S3
         * and only supports V4 authentication mechanism.
         *
         * http://stackoverflow.com/questions/26533245/the-authorization-mechanism-you-have-provided-is-not-supported-please-use-aws4
         * http://docs.aws.amazon.com/AmazonS3/latest/dev/UsingAWSSDK.html#specify-signature-version
         * TODO https://github.com/aws/aws-sdk-java/issues/740
         * TODO Planned to be released in https://github.com/jclouds/jclouds/pull/678
         */
        System.setProperty("com.amazonaws.services.s3.enableV4", "true")
    }

    @Override
    StorageApi instance() {
        StorageApi uploaderInstance = new AmazonS3PermanentURLApi(this)

        return uploaderInstance
    }

    @Override
    void verifyConfig() {
        container = flatGroupConfig.container ?: ConfigHelper.getFlatConfig("global.amazon.container")
        accessKey = flatGroupConfig.accessKey ?: ConfigHelper.getFlatConfig("global.amazon.accessKey")
        accessSecret = flatGroupConfig.accessSecret ?: ConfigHelper.getFlatConfig("global.amazon.accessSecret")

        if (!accessKey) {
            throw new InvalidFileGroupException("Either group specific or global access key required for Amazon's " +
                    "[$groupName] group")
        }

        if (!accessSecret) {
            throw new InvalidFileGroupException("Either group specific or global access secret required for Amazon's " +
                    "[$groupName] group")
        }

        if (!container) {
            throw new InvalidFileGroupException("Either group specific or global container name required for Amazon's" +
                    " [$groupName] group")
        }
    }
}
