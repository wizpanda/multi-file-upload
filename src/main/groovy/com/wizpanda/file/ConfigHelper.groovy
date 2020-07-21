package com.wizpanda.file

import com.wizpanda.file.exception.FileUploadException
import grails.compiler.GrailsCompileStatic
import grails.util.Holders

@GrailsCompileStatic
class ConfigHelper {

    private static final String BASE_CONFIG = "fileUpload"

    static Object getFlatConfig(String suffix) {
        return Holders.getFlatConfig().get(BASE_CONFIG + "." + suffix)
    }

    static Map<String, Map> getAllGroups() {
        return Holders.getFlatConfig().get(BASE_CONFIG + ".groups") as Map
    }

    static ConfigObject getGroup(String group) {
        ConfigObject groupConfig = Holders.getConfig().get(BASE_CONFIG + ".groups." + group) as ConfigObject

        if (!groupConfig || groupConfig.isEmpty()) {
            throw new FileUploadException("No service found under [${BASE_CONFIG}.${group}]")
        }

        return groupConfig
    }

    static void verifyGroup(String group) {
        ConfigObject groupConfig = Holders.getConfig().get(BASE_CONFIG + ".groups." + group) as ConfigObject

        if (!groupConfig || groupConfig.isEmpty()) {
            throw new FileUploadException("No service found under [${BASE_CONFIG}.${group}]")
        }

        // TODO complete implementation
    }
}
