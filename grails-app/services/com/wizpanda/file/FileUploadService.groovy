package com.wizpanda.file

import org.springframework.context.MessageSource
import org.springframework.web.multipart.commons.CommonsMultipartFile

import java.nio.file.Files

class FileUploadService {

    MessageSource messageSource

    /**
     * Save the uploaded multipart file to a temporary location.
     *
     * @param multipartFile
     * @return Saved file with a metaclass method "getOriginalFilename" to get the actual uploaded file name
     * @throws FileNotFoundException
     * @throws IOException
     */
    File saveUploadedFile(CommonsMultipartFile multipartFile) throws FileNotFoundException, IOException {
        if (!multipartFile || multipartFile.isEmpty()) {
            log.debug "Received file is either empty or does not exists"
            throw new FileNotFoundException(messageSource.getMessage("kernel.uploaded.file.empty", null, null))
        }


        InputStream inputStream
        FileOutputStream fileOutputStream

        byte[] fileRead = new byte[1024]

        try {
            String originalFilename = multipartFile.getOriginalFilename()

            log.debug "Uploaded file's name [$originalFilename]"

            // Remove special characters other than "a-z" "A-Z" "0-9" "." "-" or "_"
            String fileName = originalFilename.replaceAll("[^a-zA-Z0-9//._-]+", "").toLowerCase()

            File temporaryDirectory = Files.createTempDirectory(null).toFile()
            File temporaryFile = new File(temporaryDirectory, fileName)

            log.debug "File [$fileName] will be saved in [${temporaryDirectory.absolutePath}]"

            inputStream = multipartFile.getInputStream()
            fileOutputStream = new FileOutputStream(temporaryFile)

            int i = inputStream.read(fileRead)

            while (i != -1) {
                fileOutputStream.write(fileRead, 0, i)
                i = inputStream.read(fileRead)
            }

            // A dynamic method to get the original name of the file which was received
            temporaryFile.metaClass.getOriginalFilename = { ->
                return originalFilename
            }

            return temporaryFile
        } catch (FileNotFoundException e) {
            log.error "Problem saving file", e
            throw e
        } catch (IOException e) {
            log.error "Exception saving file", e
            throw e
        } finally {
            fileOutputStream?.close()
            inputStream?.close()
        }
    }
}