fastlane documentation
----

# Installation

Make sure you have the latest version of the Xcode command line tools installed:

```sh
xcode-select --install
```

For _fastlane_ installation instructions, see [Installing _fastlane_](https://docs.fastlane.tools/#installing-fastlane)

# Available Actions

## Android

### android test

```sh
[bundle exec] fastlane android test
```

Runs tests

### android lint

```sh
[bundle exec] fastlane android lint
```

Run linters

### android playstore

```sh
[bundle exec] fastlane android playstore
```

Deploy a new version to the Google Play

### android github

```sh
[bundle exec] fastlane android github
```

Submit a new GitHub release

### android beta

```sh
[bundle exec] fastlane android beta
```

Submit a new Beta Build

### android release

```sh
[bundle exec] fastlane android release
```

Submit a new GitHub release & Google Play

### android check_code

```sh
[bundle exec] fastlane android check_code
```

Run tests and static analysis

----

This README.md is auto-generated and will be re-generated every time [_fastlane_](https://fastlane.tools) is run.

More information about _fastlane_ can be found on [fastlane.tools](https://fastlane.tools).

The documentation of _fastlane_ can be found on [docs.fastlane.tools](https://docs.fastlane.tools).
