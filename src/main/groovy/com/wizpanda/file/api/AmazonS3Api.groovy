package com.wizpanda.file.api

import com.wizpanda.file.StoredFile
import com.wizpanda.file.service.AmazonS3UploaderService
import grails.util.Environment
import grails.util.GrailsStringUtils
import org.jclouds.ContextBuilder
import org.jclouds.aws.s3.AWSS3Client
import org.jclouds.blobstore.BlobStore
import org.jclouds.blobstore.BlobStoreContext
import org.jclouds.s3.domain.internal.MutableObjectMetadataImpl

import javax.activation.MimetypesFileTypeMap

abstract class AmazonS3Api extends AbstractStorageApi {

    BlobStore blobStore
    AWSS3Client client
    BlobStoreContext context
    AmazonS3UploaderService service

    void authenticate() {
        println service.accessKey
        println service.accessSecret
        context = ContextBuilder.newBuilder("aws-s3")
                .credentials(service.accessKey, service.accessSecret)
                .buildView(BlobStoreContext.class)
        println "Context created ${context.class}"

        blobStore = context.getBlobStore()
        println "BlobStore ${blobStore.class}"

        // Storing wrapped Api of S3Client with Apache JCloud
        client = context.unwrap().getApi()
    }

    void close() {
        context.close()
    }

    void setContentType(MutableObjectMetadataImpl mutableObjectMetadata, File file) {
        String contentType = new MimetypesFileTypeMap().getContentType(file.name)

        if (contentType) {
            mutableObjectMetadata.getContentMetadata().setContentType(contentType)
        }
    }

    @Override
    String getFileName(File file) {
        String name = UUID.randomUUID().toString()
        String originalFileName = file.name
        String extension = GrailsStringUtils.substringAfterLast(originalFileName, ".")

        return name + "." + extension
    }

    String getContainerName() {
        String name = service.container
        if (Environment.current != Environment.PRODUCTION) {
            name += "-" + Environment.current.name.toLowerCase()
        }

        return name
    }

    String getDirectory() {

    }

    @Override
    void validateRawFile() {

    }

    @Override
    void deleteNativeFile(StoredFile file) {
        // TODO implement me
    }
}
