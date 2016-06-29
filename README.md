# Multi File Upload

A plugin for multi purpose file upload functionality.

## Installation

Add the following to your `BuildConfig.groovy` file:

**Under `respositories` block**

```groovy
mavenRepo "http://dl.bintray.com/wizpanda/grails-plugins"
```

**Under `plugins` block**

```groovy
compile "com.wizpanda.plugins:multi-file-upload:0.0.1"
```

## Releasing new version

1. Change the version in the `KernalGrailsPlugin.groovy`
2. Run `grails maven-deploy`

You should have following in your `~/.grails/settings.groovy`

```groovy
grails.project.repos.default = "wizpandaRepo"
grails.project.repos.wizpandaRepo.username = "my-username"
grails.project.repos.wizpandaRepo.password = "my-password"
```