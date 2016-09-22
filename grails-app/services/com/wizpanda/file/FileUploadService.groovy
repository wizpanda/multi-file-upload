package com.wizpanda.file

import com.wizpanda.file.exception.FileUploadException
import com.wizpanda.file.exception.InvalidFileGroupException
import com.wizpanda.file.service.UploaderService
import org.springframework.context.MessageSource
import org.springframework.web.multipart.MultipartFile

import javax.annotation.PostConstruct
import java.nio.file.Files

class FileUploadService {

    private static Map<String, UploaderService> services = [:]

    MessageSource messageSource

    /**
     * Save the uploaded multipart file in the temporary directory of the local server which will be cleaned
     * automatically by the system itself.
     *
     * @param multipartFile
     * @return Saved file in a temporary location
     * @throws FileNotFoundException
     * @throws IOException
     */
    File saveTemporarily(MultipartFile multipartFile) throws FileNotFoundException, IllegalStateException, IOException {
        if (!multipartFile || multipartFile.isEmpty()) {
            log.debug "Received file is either empty or does not exists"

            throw new FileNotFoundException(messageSource.getMessage("kernel.uploaded.file.empty", null, null))
        }

        try {
            String originalFilename = multipartFile.getOriginalFilename()
            File temporaryDirectory = Files.createTempDirectory(null).toFile()

            //log.debug "Uploaded file [$originalFilename] will be saved in [${temporaryDirectory.absolutePath}]"

            File temporaryFile = new File(temporaryDirectory, originalFilename)
            multipartFile.transferTo(temporaryFile)

            return temporaryFile
        } catch (IllegalStateException e) {
            log.error "Problem saving file", e
            throw e

        } catch (IOException e) {
            log.error "Exception saving file", e
            throw e
        }
    }

    File save(MultipartFile multipartFile, String groupName) throws FileUploadException {
        // TODO Add check for validating group name
        File temporaryFile
        try {
            temporaryFile = saveTemporarily(multipartFile)
        } catch (FileNotFoundException | IllegalStateException | IOException e) {
            throw new FileUploadException(e.message)
        }

        return services.get(groupName).instance().saveFile(temporaryFile)
    }

    @PostConstruct
    void verifyConfig() {
        //log.debug "Verifying all service"
        println "Verifying all service"

        ConfigHelper.allGroups.each { Map.Entry groupConfigEntry ->
            String groupName = groupConfigEntry.key.toString()
            Map groupConfigValue = groupConfigEntry.value

            if (!groupConfigValue.service) {
                throw new InvalidFileGroupException("The service API missing for [${groupName}]")
            }

            Class<? extends UploaderService> serviceClass = groupConfigValue.service

            UploaderService service
            try {
                service = serviceClass.newInstance(groupName, groupConfigValue)
            } catch (IllegalAccessException | InstantiationException | RuntimeException e) {
                log.error "Error creating uploader service object", e
                throw new InvalidFileGroupException("Error while creating the uploader service object");
            }

            services[service.groupName] = service
        }
    }
}