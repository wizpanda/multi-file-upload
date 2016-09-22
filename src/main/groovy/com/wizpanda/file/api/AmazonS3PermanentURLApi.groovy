package com.wizpanda.file.api

import com.wizpanda.file.StoredFile
import com.wizpanda.file.service.AmazonS3UploaderService
import org.jclouds.aws.s3.blobstore.options.AWSS3PutObjectOptions
import org.jclouds.blobstore.domain.Blob
import org.jclouds.s3.domain.CannedAccessPolicy
import org.jclouds.s3.domain.S3Object
import org.jclouds.s3.domain.internal.MutableObjectMetadataImpl
import org.jclouds.s3.domain.internal.S3ObjectImpl
import org.springframework.web.multipart.MultipartFile

class AmazonS3PermanentURLApi extends AmazonS3Api {

    AmazonS3PermanentURLApi(AmazonS3UploaderService service) {
        this.service = service
    }

    @Override
    StoredFile saveFile(File file) {
        authenticate()
        upload(file)
        close()

        return null
    }

    String upload(File file) {
        AWSS3PutObjectOptions fileOptions = new AWSS3PutObjectOptions()
        fileOptions.withAcl(CannedAccessPolicy.PUBLIC_READ)

        String fileName = getFileName(file)

        MutableObjectMetadataImpl mutableObjectMetadata = new MutableObjectMetadataImpl()
        mutableObjectMetadata.setKey(fileName)
        setContentType(mutableObjectMetadata, file)

        S3Object s3Object = new S3ObjectImpl(mutableObjectMetadata)
        s3Object.setPayload(file)

        return client.putObject(getContainerName(), s3Object, fileOptions)
        /*Blob blob = blobStore.blobBuilder("blob-name")
                .payload(file)
                .contentLength(file.size())
                .build()

        blobStore.putBlob(getContainerName(), blob)*/
    }
}