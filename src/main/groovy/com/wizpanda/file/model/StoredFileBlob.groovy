package com.wizpanda.file.model

import org.jclouds.blobstore.domain.Blob

/**
 *
 * @author Ankit Kumar Singh
 * @since 0.1.3
 */
class StoredFileBlob {

    Blob blob
    Object rawContent

    StoredFileBlob(Blob blob) {
        this.blob = blob
        rawContent = blob.payload.getRawContent()
    }

    /**
     * Method to get string from a blob.
     * @return
     */
    String getText() {
        if (this.rawContent instanceof InputStream) {
            InputStream rawStream = this.rawContent
            return rawStream.text
        }

        null
    }

    /**
     * Method to get string from a blob for a specific charset.
     * @param charset
     * @return
     */
    String getText(String charset) {
        if (this.rawContent instanceof InputStream) {
            InputStream rawStream = this.rawContent
            return rawStream.getText(charset)
        }

        null
    }

    /**
     * Method to return an input stream from a blob.
     * @return
     */
    InputStream getInputStream() {
        if (this.rawContent instanceof InputStream) {
            return this.rawContent as InputStream
        }
    }
}
