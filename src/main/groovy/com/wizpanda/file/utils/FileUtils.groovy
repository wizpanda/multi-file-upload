package com.wizpanda.file.utils

import groovy.transform.CompileStatic
import org.springframework.web.multipart.MultipartFile

import java.nio.file.Files

@CompileStatic
class FileUtils {

    /**
     * Save the uploaded multipart file in the temporary directory of the local server which will be cleaned
     * automatically by the system itself.
     *
     * @param multipartFile
     * @return Saved file in a temporary location
     * @throws FileNotFoundException* @throws IOException
     */
    static File saveTemporarily(MultipartFile multipartFile) throws IllegalStateException, IOException {
        if (!multipartFile || multipartFile.isEmpty()) {
            throw new FileNotFoundException("Uploaded file is empty or null")
        }

        try {
            String originalFilename = multipartFile.getOriginalFilename()
            File temporaryDirectory = Files.createTempDirectory(null).toFile()

            File temporaryFile = new File(temporaryDirectory, originalFilename)
            multipartFile.transferTo(temporaryFile)

            return temporaryFile
        } catch (IllegalStateException | IOException e) {
            throw e
        }
    }
}
