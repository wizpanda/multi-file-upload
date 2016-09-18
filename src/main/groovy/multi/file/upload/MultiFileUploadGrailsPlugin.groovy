package multi.file.upload

import grails.plugins.Plugin

class MultiFileUploadGrailsPlugin extends Plugin {

    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "3.1.11 > *"
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
        "grails-app/views/error.gsp"
    ]

    String title = "Multi File Upload Plugin" // Headline display name of the plugin
    String author = "Shashank Agrawal"
    String authorEmail = ""
    String description = "A Grails plugin to provide multi purpose file upload functionality."

    Map organization = [name: "Wiz Panda", url: ""]

    Map issueManagement = [system: "GITHUB", url: "https://github.com/wizpanda/multi-file-upload/issues"]

    Map scm = [ url: "https://github.com/wizpanda/multi-file-upload" ]
}
