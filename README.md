![Coverage Status](https://s3.amazonaws.com/assets.coveralls.io/badges/coveralls_19.svg)

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

## Hiding Sensitive Information

This app uses Firebase Remote Config to securely store and retrieve API keys, allowing for easy
updates and management of sensitive information without the need for hard coding or additional
configuration. This ensures that the app remains secure and in compliance with best practices for
handling sensitive data.
