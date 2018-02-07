package com.wizpanda.file.api

import com.wizpanda.file.service.AmazonS3UploaderService
import groovy.util.logging.Slf4j
import org.jclouds.aws.s3.blobstore.options.AWSS3PutObjectOptions
import org.jclouds.s3.domain.S3Object
import org.jclouds.s3.domain.internal.MutableObjectMetadataImpl
import org.jclouds.s3.domain.internal.S3ObjectImpl

@Slf4j
class AmazonS3PermanentURLApi extends AmazonS3Api {

    AmazonS3PermanentURLApi(AmazonS3UploaderService service) {
        this.service = service
    }

    @Override
    void saveNativeFile() {
        this.authenticate()

        String fileName = getFileName(this.rawFile)
        String containerName = getContainerName()

        MutableObjectMetadataImpl mutableObjectMetadata = new MutableObjectMetadataImpl()
        mutableObjectMetadata.setKey(fileName)
        setCacheControl(mutableObjectMetadata)
        setContentType(mutableObjectMetadata)

        S3Object s3Object = new S3ObjectImpl(mutableObjectMetadata)
        s3Object.setPayload(this.rawFile)

        AWSS3PutObjectOptions fileOptions = new AWSS3PutObjectOptions()
        setAccessPolicy(fileOptions)

        log.debug "Putting object [$fileName] to container [$containerName] with $fileOptions"
        client.putObject(containerName, s3Object, fileOptions)

        this.gormFile.name = fileName
        this.gormFile.url = client.getObject(containerName, fileName, null).metadata.uri

        this.close()
    }
}