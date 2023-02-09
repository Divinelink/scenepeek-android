[![codecov](https://img.shields.io/codecov/c/gh/Divinelink/MovieRama/main?token=FPANRF2HZ5)](https://codecov.io/gh/Divinelink/MovieRama)


# MovieRama

MovieRama is an android application, built with [Jetpack Compose][compose]. The application provides
information about movies &amp; television shows and other forms of entertainment. It includes
information such as cast and crew, plot summaries, user reviews, ratings, and more.

### Note

This application is built using Jetpack Compose, which is a modern UI toolkit for building native
Android apps. However, it is still in the early stages of development, and may result in some
performance issues, such as lagging, when running the debug version of the app. It is recommended to
use the release version for optimal performance.

## Getting Started

Welcome! The application uses an MVVM architecture which you can read about [here](documentation/Architecture.md). The [documentation folder](documentation) can also serve as a guide to getting familiar with this project.

This application is built with a thorough suite of unit tests to ensure that all functionality is working as intended. These tests are run automatically with every build, and are a crucial part of our development process. They help us catch and fix bugs early on, and ensure that new changes don't break existing functionality. By using unit tests, we can have confidence that the app is working as expected, and that it will continue to work correctly as we make updates and improvements over time.

In addition to the unit tests, the development process includes a system of pull requests. By reviewing the pull request history, you can get a detailed understanding of how the application was implemented. This can be especially helpful if you're looking to learn more about how a specific feature was developed or if you're trying to understand how the codebase is organized.

## Local Data Storage with Room

Our application uses Room, a persistence library provided by Google as a part of the Android Architecture Components, to store information locally on the device. Room provides an abstraction layer over SQLite, making it easier to work with databases in Android. Room also allows for a more robust and maintainable codebase, by introducing features such as compile-time checks of SQLite statements, and the ability to define relationships between entities. Room is a great solution for our application's data storage needs, as it allows for quick and easy access to our data, while also providing a level of security and organization.


## Hiding Sensitive Information

This app uses Firebase Remote Config and EncryptedSharedPreferences to securely store and retrieve API keys, allowing for easy updates and management of sensitive information without the need for hard coding or additional configuration. This ensures that the app remains secure and in compliance with best practices for handling sensitive data, as the information is encrypted.
