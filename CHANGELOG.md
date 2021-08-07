# Change Logs

## v0.1.10

Use JitPack

## v0.1.9

1. Fixed Java inheritance problem.
2. Using Plugin hook `doWithApplicationContext` instead of `@PostConstruct`.

## v0.1.7, 0.1.8

1. Using `@GrailsCompileStatic` & `@CompileStatic` in all files for performance.
2. Switched to GitHub packages.

## v0.1.6

1. Renamed field `markedForDeletion` to `deleteAfter` in `StoredFile` domain.
2. By default deleting the file after 1 day.

## v0.1.5

1. Upgrading `jcloud` dependency to the latest to fix conflicts with Guava dependency #13
