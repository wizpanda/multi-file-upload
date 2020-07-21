package com.wizpanda.file.api

import com.wizpanda.file.service.AmazonS3UploaderService
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.jclouds.aws.s3.blobstore.options.AWSS3PutObjectOptions
import org.jclouds.s3.domain.S3Object
import org.jclouds.s3.domain.internal.MutableObjectMetadataImpl

@Slf4j
@CompileStatic
class AmazonS3PermanentURLApi extends AmazonS3Api {

    AmazonS3PermanentURLApi(AmazonS3UploaderService service) {
        this.service = service
    }

    @Override
    void saveNativeFile() {
        this.authenticate()

        String fileName = getFileName(this.rawFile)
        String containerName = getContainerName()

        S3Object s3Object = client.newS3Object()
        s3Object.setPayload(this.rawFile)

        MutableObjectMetadataImpl mutableObjectMetadata = s3Object.getMetadata() as MutableObjectMetadataImpl
        mutableObjectMetadata.setKey(fileName)
        setCacheControl(mutableObjectMetadata)
        /**
         * Always set the content-type after setting the payload so that it does not get overridden.
         * https://groups.google.com/d/msg/jclouds/FMuEbPo9M_k/kaXCKkuOKdMJ
         */
        setContentType(mutableObjectMetadata)

        AWSS3PutObjectOptions fileOptions = new AWSS3PutObjectOptions()
        setAccessPolicy(fileOptions)

        log.debug "Putting object [$fileName] to container [$containerName] with $fileOptions"
        client.putObject(containerName, s3Object, fileOptions)

        this.gormFile.name = fileName
        this.gormFile.url = client.getObject(containerName, fileName, null).metadata.uri

        this.close()
    }
}
