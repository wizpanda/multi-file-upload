# Grails Multi File Upload

[![GitHub version](https://badge.fury.io/gh/wizpanda%2Fmulti-file-upload.svg)](https://badge.fury.io/gh/wizpanda%2Fmulti-file-upload)  

A plugin for multi-purpose file upload functionality for Grails 3+ application. This plugin allows uploading & saving files
to the following destinations:

1. To the local server or the local system.
2. To the Amazon S3 storage (can be deleted as well).

**The upload APIs are highly customizable, so you can always override the classes or add your own implementation.**

## Installation & Usage

### Installation

Add the following to `build.gradle` file of your Grails 3 application

**Under `repositories` section**

```groovy
maven { url "https://maven.pkg.github.com/wizpanda/multi-file-upload" }
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
            accessKey = "EXAMPLE-KEY-ALIAI4VT211NNPSV3YGQ2"
            accessSecret = "EXAMPLE-SECRET-803a3E2Ul0HkImA2kLQAyF4ngM3P6St"
        }
    }
    groups {
        fooSliderImages {
            // Add the service which you want to configure here
            service = com.wizpanda.file.service.AmazonS3UploaderService
            container = "foo-images"
            cacheControlSeconds = 604800    // 1 week
        }
    }
}
```

Read [docs/aws-access-configuration.md](docs/aws-access-configuration.md) to read how to get the access key & secret for AWS S3.

### Using in controllers or services

```groovy
fileUploadService.save(params.file, "fooSliderImages")
```

## Uses of containers/buckets

The containers defined in the configuration above i.e. `foo-images` are suffixed with environment name for non-production environment.
For example, an S3 container with the name `foo-images-development` will be used when running locally. So that container must be there
before.

**This means, you don't need to use Grails environments to use the different buckets for different environments.** 

## Releasing new version

1. Change the version in the `build.gradle`.
2. Make sure Bintray configuration are configured properly as given [here](https://github.com/grails/grails-core/blob/639d7039d24031dbc1353f95b6d2c88a100da850/grails-gradle-plugin/src/main/groovy/org/grails/gradle/plugin/publishing/GrailsCentralPublishGradlePlugin.groovy).
2. Run `gradle bintrayUpload`
