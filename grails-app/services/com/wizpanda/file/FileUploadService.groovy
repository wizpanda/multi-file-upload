package com.wizpanda.file

import com.wizpanda.file.api.StorageApi
import com.wizpanda.file.exception.FileUploadException
import com.wizpanda.file.exception.InvalidFileGroupException
import com.wizpanda.file.service.UploaderService
import grails.compiler.GrailsCompileStatic
import groovy.util.logging.Slf4j
import org.springframework.web.multipart.MultipartFile

/**
 * The primary service for starting the file upload.
 *
 * @author Shashank Agrawal
 */
@Slf4j
@GrailsCompileStatic
class FileUploadService {

    private static Map<String, UploaderService> services = [:]

    /**
     * Save a given Multipart file based on the given group name.
     * @param multipartFile
     * @param groupName As defined in the configuration under `fileUpload` -> `groups`.
     * @return
     * @throws FileUploadException
     */
    StoredFile save(MultipartFile multipartFile, String groupName) throws FileUploadException {
        // TODO Add check for validating group name

        return getStorageApi(groupName).save(multipartFile)
    }

    StoredFile save(File file, String groupName) throws FileUploadException {
        // TODO Add check for validating group name

        return getStorageApi(groupName).save(file)
    }

    void delete(StoredFile file) {
        if (!file) {
            log.warn 'StoredFile is null.'
            return
        }

        getStorageApi(file.groupName).delete(file)
    }

    void cloneFile(StoredFile file, String newGroupName) {
        if (!file) {
            log.warn 'StoredFile cannot be null.'
            return
        }

        if (!newGroupName) {
            log.warn 'New Group Name cannot be null.'
            return
        }

        getStorageApi(newGroupName).cloneStoredFile(file, newGroupName)
    }

    void init() {
        //log.debug "Verifying all service"
        println "Verifying all service"

        ConfigHelper.allGroups.each { String groupName, Map groupConfigValue ->
            if (!groupConfigValue) {
                throw new InvalidFileGroupException("The configuration for group [$groupName] is not configured")
            }

            if (!groupConfigValue.service) {
                throw new InvalidFileGroupException("The service API missing for [${groupName}]")
            }

            Class<? extends UploaderService> serviceClass = groupConfigValue.service

            UploaderService service
            try {
                service = serviceClass.newInstance(groupName, groupConfigValue)
            } catch (IllegalAccessException | InstantiationException | RuntimeException e) {
                log.error "Error creating uploader service object", e
                throw new InvalidFileGroupException("Error while creating the uploader service object")
            }

            services[service.groupName] = service
        }
    }

    StorageApi getStorageApi(String groupName) {
        return services.get(groupName).instance()
    }
}
