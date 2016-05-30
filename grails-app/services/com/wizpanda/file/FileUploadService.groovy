package com.wizpanda.file

import org.springframework.context.MessageSource
import org.springframework.web.multipart.commons.CommonsMultipartFile

import java.nio.file.Files

class FileUploadService {

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
    File saveTemporarily(CommonsMultipartFile multipartFile) throws FileNotFoundException, IOException {
        if (!multipartFile || multipartFile.isEmpty()) {
            log.debug "Received file is either empty or does not exists"

            throw new FileNotFoundException(messageSource.getMessage("kernel.uploaded.file.empty", null, null))
        }

        try {
            String originalFilename = multipartFile.getOriginalFilename()
            File temporaryDirectory = Files.createTempDirectory(null).toFile()

            log.debug "Uploaded file [$originalFilename] will be saved in [${temporaryDirectory.absolutePath}]"

            File temporaryFile = new File(temporaryDirectory, originalFilename)
            multipartFile.transferTo(temporaryFile)

            return temporaryFile
        } catch (IllegalStateException e) {
            log.error "Problem saving file", e
            throw new FileNotFoundException(e.message)

        } catch (IOException e) {
            log.error "Exception saving file", e
            throw e
        }
    }
}