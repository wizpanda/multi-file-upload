# Multi File Upload

A plugin for multi purpose file upload functionality for Grails 3 application. This plugin allows to upload & save files
to the following destinations:

1. To the local server or the local system
2. To the Amazon S3 storage (can be deleted as well)
3. To the Rackspace cloud storage (coming soon)

**The upload APIs are highly customizable so you can always override the classes or add your own implementation.**

## Compatibility

Grails Version | Supported
--------- | ---------
<= 3.2.11         |  >= v0.0.6
>= 3.3.0         |  >= v0.0.7

## Installation & Usage

### Installation

Add the following to `build.gradle` file of your Grails 3 application

**Under `repositories` section**

```groovy
maven { url "http://dl.bintray.com/wizpanda/grails-plugins" }
```

**Under `dependencies` section**

```groovy
compile "com.wizpanda.plugins:multi-file-upload:<version>"

```

### Configuration

In the `application.groovy` file, define your groups & AWS configuration. For example:

```groovy
fileUpload {
    global {
        amazon {
            accessKey = "ALIAI4VT2YYNNPSV3YGQ2"
            accessSecret = "803a3E2Ul0HkImA2kLQAyF4ngM3P6StQGEMUC"
        }
    }
    groups {
        sliderImages {
            service = com.wizpanda.file.service.AmazonS3UploaderService
            container = "example-slider-images"
            cacheControlSeconds = 604800    // 1 week
        }
    }
}

```

## Releasing new version

1. Change the version in the `build.gradle`.
2. Make sure Bintray configuration are configured properly as given [here](https://github.com/grails/grails-core/blob/639d7039d24031dbc1353f95b6d2c88a100da850/grails-gradle-plugin/src/main/groovy/org/grails/gradle/plugin/publishing/GrailsCentralPublishGradlePlugin.groovy).
2. Run `gradle bintrayUpload`