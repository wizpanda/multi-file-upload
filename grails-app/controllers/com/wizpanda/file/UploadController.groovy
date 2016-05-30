package com.wizpanda.file

import grails.util.Holders

/**
 * Endpoint for uploading file(s) to a temporary location. Used for AJAX based file upload.
 *
 * @author Shashank Agrawal
 * @since 0.0.1
 */
class UploadController {

    static allowedMethods = [index: "POST"]
    static responseFormats = ["json"]
    static scope = "singleton"

    FileUploadService fileUploadService

    /**
     * This endpoint accepts the file in binary form, uploaded by the AJAX based application.
     *
     * @example
     *
     * When single file is uploaded:
     * <pre>
     *      params.file = (a single CommonsMultipartFile)
     * </pre>
     *
     * Then, the response should be:
     * <pre>
     *      {
     *          filepath: "./temp/my-avatar.png",
     *          filename: "my-avatar.png"
     *      }
     * </pre>
     *
     * Note: By default this endpoint is blocked to allow upload. You need to set following to your config in order
     * to allow uploading files using this endpoint:
     *
     * <pre>
     *     wizpanda.plugins.kernel.allow.file.upload = true
     * </pre>
     *
     * This is done because if you install this plugin and forget to protect this endpoint then someone may bloat
     * your server by uploading junk files.
     */
    def index() {
        if (!Holders.getFlatConfig()["wizpanda.plugins.kernel.allow.file.upload"]) {
            log.warn "App is not allowing to upload files"
            return
        }

        log.debug "Temporary upload with $params"

        File file = fileUploadService.saveUploadedFile(params.file)
        Map result = [filepath: file.getPath(), filename: file.getName()]

        // Work around for IE
        response.contentType = "text/plain"
        respond(result)
    }
}