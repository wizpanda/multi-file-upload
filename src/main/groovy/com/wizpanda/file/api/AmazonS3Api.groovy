package com.wizpanda.file.api

import com.wizpanda.file.ConfigHelper
import com.wizpanda.file.StoredFile
import com.wizpanda.file.service.AmazonS3UploaderService
import grails.util.Environment
import grails.util.GrailsStringUtils
import groovy.util.logging.Slf4j
import org.jclouds.ContextBuilder
import org.jclouds.aws.s3.AWSS3Client
import org.jclouds.aws.s3.blobstore.options.AWSS3PutObjectOptions
import org.jclouds.blobstore.BlobStore
import org.jclouds.blobstore.BlobStoreContext
import org.jclouds.http.HttpResponseException
import org.jclouds.s3.domain.CannedAccessPolicy
import org.jclouds.s3.domain.internal.MutableObjectMetadataImpl
import org.jclouds.s3.options.CopyObjectOptions

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

        //log.info "BlobStore ${blobStore.class}"

        // Storing wrapped Api of S3Client with Apache JCloud
        client = context.unwrap().getApi()
    }

    void close() {
        context.close()
    }

    void setContentType(MutableObjectMetadataImpl mutableObjectMetadata) {
        String contentType = new MimetypesFileTypeMap().getContentType(this.rawFile.name)

        log.debug "ContentType for [${this.rawFile.name}] is [$contentType]"
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

    @Override
    String getFileName(StoredFile file) {
        String name = UUID.randomUUID().toString()
        String originalFileName = file.originalName
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
            log.warn "File not present in the S3 bucket, deleting the Stored file instance."

            file.delete()
            this.close()

            return
        }

        client.deleteObject(container, fileName)
        file.delete()

        this.close()
    }

    @Override
    StoredFile cloneStoredFile(StoredFile file, String newGroupName) {
        StoredFile clonedFile = new StoredFile()
        clonedFile.originalName = file.originalName
        clonedFile.groupName = newGroupName
        clonedFile.size = file.size
        clonedFile.name = getFileName(file)

        String currentContainer = ConfigHelper.getGroup(file.groupName).container ?:
                ConfigHelper.getFlatConfig("global.amazon.container")

        String newContainer = getContainerName()

        CopyObjectOptions fileOptions = new CopyObjectOptions()
        // For now using the same policy of original file, it gets changed to private scope if not overridden.
        fileOptions.overrideAcl(CannedAccessPolicy.PUBLIC_READ)

        try {
            this.authenticate()
            client.copyObject(currentContainer, file.name, newContainer, clonedFile.name, fileOptions)
            clonedFile.url = client.getObject(newContainer, clonedFile.name, null).metadata.uri

            this.gormFile = clonedFile
            this.gormFile.uploadedOn = new Date()

            this.saveGORMFile()

            log.info "Successfully cloned StoredFile: ${file} as ${this.gormFile}"

            return this.gormFile
        } catch (HttpResponseException hre) {
            log.warn 'Could not copy StoredFile!', hre
        } finally {
            this.close()
        }
    }

    void setCacheControl(MutableObjectMetadataImpl mutableObjectMetadata) {
        Long cacheControlSeconds = this.service.flatGroupConfig.cacheControlSeconds

        if (!cacheControlSeconds) {
            return
        }

        mutableObjectMetadata.setCacheControl("max-age=$cacheControlSeconds, public, must-revalidate, proxy-revalidate")
    }

    void setAccessPolicy(AWSS3PutObjectOptions fileOptions) {
        Boolean makePrivate = this.service.flatGroupConfig.makePrivate

        if (makePrivate) {
            fileOptions.withAcl(CannedAccessPolicy.PRIVATE)
        } else {
            fileOptions.withAcl(CannedAccessPolicy.PUBLIC_READ)
        }
    }
}
