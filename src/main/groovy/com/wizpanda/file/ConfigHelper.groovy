package com.wizpanda.file

import com.wizpanda.file.exception.FileUploadException
import grails.util.Holders

class ConfigHelper {

    private static final String BASE_CONFIG = "fileUpload"

    static Object getFlatConfig(String suffix) {
        return Holders.getFlatConfig()[BASE_CONFIG + "." + suffix]
    }

    static ConfigObject getAllGroups() {
        return Holders.getConfig()[BASE_CONFIG + ".groups"]
    }

    static ConfigObject getGroup(String group) {
        ConfigObject groupConfig = Holders.getConfig()[BASE_CONFIG + ".groups." + group]

        if (!groupConfig || groupConfig.isEmpty()) {
            throw new FileUploadException("No service found under [${BASE_CONFIG}.${group}]")
        }

        return groupConfig
    }

    static void verifyGroup(String group) {
        ConfigObject groupConfig = Holders.getConfig()[BASE_CONFIG + ".groups." + group]

        if (!groupConfig || groupConfig.isEmpty()) {
            throw new FileUploadException("No service found under [${BASE_CONFIG}.${group}]")
        }

        // TODO complete implementation
    }
}
