package com.wizpanda.file.api

import com.wizpanda.file.StoredFile
import com.wizpanda.file.service.AmazonS3UploaderService
import grails.util.Environment
import grails.util.GrailsStringUtils
import groovy.util.logging.Slf4j
import org.jclouds.ContextBuilder
import org.jclouds.aws.s3.AWSS3Client
import org.jclouds.blobstore.BlobStore
import org.jclouds.blobstore.BlobStoreContext
import org.jclouds.s3.domain.internal.MutableObjectMetadataImpl

import javax.activation.MimetypesFileTypeMap

@Slf4j
abstract class AmazonS3Api extends AbstractStorageApi {

    BlobStore blobStore
    AWSS3Client client
    BlobStoreContext context
    AmazonS3UploaderService service

    void authenticate() {
        context = ContextBuilder.newBuilder("aws-s3")
                .credentials(service.accessKey, service.accessSecret)
                .buildView(BlobStoreContext.class)

        log.info "Context created ${context.class}"

        blobStore = context.getBlobStore()

        log.info "BlobStore ${blobStore.class}"

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
        String container = this.containerName
        String fileName = file.name

        log.info "Deleting file ${file} with name ${fileName} from container ${container}."

        this.authenticate()

        if (!client.objectExists(container, fileName)) {
            log.warn "File not present in the S3 bucket."
            return
        }

        client.deleteObject(container, fileName)
        file.delete(flush: true)

        this.close()
    }
}
