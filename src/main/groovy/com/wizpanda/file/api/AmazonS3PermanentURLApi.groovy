package com.wizpanda.file.api

import com.wizpanda.file.StoredFile
import com.wizpanda.file.service.AmazonS3UploaderService
import org.jclouds.aws.s3.blobstore.options.AWSS3PutObjectOptions
import org.jclouds.s3.domain.CannedAccessPolicy
import org.jclouds.s3.domain.S3Object
import org.jclouds.s3.domain.internal.MutableObjectMetadataImpl
import org.jclouds.s3.domain.internal.S3ObjectImpl

class AmazonS3PermanentURLApi extends AmazonS3Api {

    AmazonS3PermanentURLApi(AmazonS3UploaderService service) {
        this.service = service
    }

    @Override
    void saveNativeFile() {
        this.authenticate()

        AWSS3PutObjectOptions fileOptions = new AWSS3PutObjectOptions()
        fileOptions.withAcl(CannedAccessPolicy.PUBLIC_READ)

        String fileName = getFileName(this.rawFile)
        String containerName = getContainerName()

        MutableObjectMetadataImpl mutableObjectMetadata = new MutableObjectMetadataImpl()
        mutableObjectMetadata.setKey(fileName)
        setContentType(mutableObjectMetadata, this.rawFile)

        S3Object s3Object = new S3ObjectImpl(mutableObjectMetadata)
        s3Object.setPayload(this.rawFile)

        client.putObject(containerName, s3Object, fileOptions)

        this.gormFile.name = fileName
        this.gormFile.url = client.getObject(containerName, fileName, null).metadata.uri

        this.close()
    }
}